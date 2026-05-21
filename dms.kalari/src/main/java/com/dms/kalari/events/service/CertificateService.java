package com.dms.kalari.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;

import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.repository.CoreUserRepository;
//Add these imports at the top of CertificateService.java
import com.dms.kalari.branch.entity.Node; // ← fixes wrong Node type + getNodeName/getNodeType/getParent
import com.dms.kalari.branch.repository.NodeRepository; // ← fixes nodeRepository cannot be resolved
import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.core.repository.CoreFileRepository;
import com.dms.kalari.util.QrCodeUtil;
import com.dms.kalari.util.SimpleBase64;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;

import net.coobird.thumbnailator.Thumbnails;

import com.dms.kalari.events.dto.CertificateGenerationResultDTO;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.repository.MemberEventItemRepository;
import com.dms.kalari.events.service.event.CertificateGenerateEvent;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CertificateService {

    @Autowired
    private TemplateEngine templateEngine;
    private final NodeRepository nodeRepository; // ← add this
    private final CoreUserRepository coreUserRepository;
    private final CoreFileRepository fileRepository;
    private final MemberEventItemRepository memberEventItemRepository;

    private static final String KEYSTORE_PATH = "/etc/ssl/indiankalaripayattufederation/indiankalari.p12";
    private static final String KEYSTORE_PASSWORD = "changeit";
    private static final String DEFAULT_FILE_PATH = "/opt/app/uploads/default.jpg";

    @Value("${app.upload.base-path}")
    private String BASE_PATH;

    private static final Logger log = LoggerFactory.getLogger(CertificateService.class);

    public CertificateService(
	    // ... your existing constructor params ...
	    NodeRepository nodeRepository, CoreFileRepository fileRepository,MemberEventItemRepository memberEventItemRepository ,CoreUserRepository coreUserRepository// ← add this param
    ) {
	// ... your existing assignments ...
	this.nodeRepository = nodeRepository;
	this.fileRepository = fileRepository; // ← add this assignment
	this.coreUserRepository = coreUserRepository;
	this.memberEventItemRepository = memberEventItemRepository;
    }

    // @Transactional already in transaction
    public CertificateGenerationResultDTO generateOrGetSignedCertificate(MemberEventItem mei) throws Exception {
	// Ensure BC provider is available
	if (Security.getProvider("BC") == null) {
	    Security.addProvider(new BouncyCastleProvider());
	}

	// Use modified or created timestamp
	LocalDateTime timestamp = mei.getTModified() != null ? mei.getTModified() : mei.getTCreated();
	String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));

	// Build directory path
	Path storagePath = Paths.get("uploads", "certificates", String.valueOf(mei.getMemberEvent().getEventYear()),
		String.valueOf(mei.getMemberEventHost().getNodeId()), String.valueOf(mei.getMemberEvent().getEventId()),
		String.valueOf(mei.getMeiId()));
	Files.createDirectories(storagePath);

	// Final file
	Path filePath = storagePath.resolve(formattedTimestamp + ".pdf");
	String filePathString = storagePath.toString();

	// ✅ Return if file already exists
	if (Files.exists(filePath)) {
	    System.out.println("Existing certificate found: " + filePath);
	    return new CertificateGenerationResultDTO(filePath.getFileName().toString(), false, filePathString);
	}
	
	
	if (mei.getMeiCertificateNo() == null
	        || mei.getMeiCertificateNo().isBlank()) {

	    String certificateNo =
		    memberEventItemRepository.generateCertificateNumber(
	                    mei.getMeiId()
	            );

	    mei.setMeiCertificateNo(
	            certificateNo
	    );

	    memberEventItemRepository.save(mei);

	    log.info(
	        "Generated certificate number {} for mei {}",
	        certificateNo,
	        mei.getMeiId()
	    );
	}

	// Generate new unsigned PDF
	Map<String, Object> data = prepareCertificateData(mei, filePath.getFileName().toString());

	/*
	 * data.forEach( (k,v) -> log.info( "{} => {}", k, v ) );
	 */

	byte[] unsignedPdf = generatePdfFromTemplate(data);

	// Sign PDF
	byte[] signedPdf = signPdf(unsignedPdf);

	// Store locally
	Files.write(filePath, signedPdf);
	System.out.println("New certificate stored: " + filePath);

	return new CertificateGenerationResultDTO(filePath.getFileName().toString(), true, filePathString);
    }

    // Prepare all certificate data for the template
    private Map<String, Object> prepareCertificateData(MemberEventItem mei, String fileName) throws Exception {

	Map<String, Object> data = new HashMap<>();

	data.put("meiId", mei.getMeiId());

	data.put("participantName", mei.getMemberEventMember().getUserFname());

	data.put("eventName", mei.getMemberEvent().getEventName());

	data.put("hostName", mei.getMemberEventHost().getNodeName());

	data.put("resultDate", mei.getApproveDateTime());

	data.put("medalType", mei.getMemberEventGrade().name());

	data.put("itemName", mei.getMemberEventItemName());
	
	
	data.put(
		    "certificateNo",
		    mei.getMeiCertificateNo()
		);

	/*
	 * ====================================== NODE HIERARCHY
	 * ======================================
	 */

	Node eventNode = mei.getMemberEventHost();

	data.put("eventNodeName", eventNode.getNodeName());
	
	data.put("branchCertTagLine", eventNode.getBranchCertTagLine());
	
	

	String districtName = null;
	String stateName = null;
	String nationalName = null;

	String districtLogo = null;
	String stateLogo = null;
	String nationalLogo = null;

	switch (eventNode.getNodeType()) {

	case NATIONAL:

	    nationalName = eventNode.getNodeName();

	    nationalLogo = resolveNodeLogoPath(eventNode);

	    break;

	case STATE:

	    stateName = eventNode.getNodeName();

	    stateLogo = resolveNodeLogoPath(eventNode);

	    if (eventNode.getParent() != null) {

		Node national = nodeRepository.findByIdAndNotDeleted(eventNode.getParent().getNodeId()).orElse(null);

		if (national != null) {

		    nationalName = national.getNodeName();

		    nationalLogo = resolveNodeLogoPath(national);
		}
	    }

	    break;

	case DISTRICT:

	    districtName = eventNode.getNodeName();

	    districtLogo = resolveNodeLogoPath(eventNode);

	    if (eventNode.getParent() != null) {

		Node state = nodeRepository.findByIdAndNotDeleted(eventNode.getParent().getNodeId()).orElse(null);

		if (state != null) {

		    stateName = state.getNodeName();

		    stateLogo = resolveNodeLogoPath(state);

		    if (state.getParent() != null) {

			Node national = nodeRepository.findByIdAndNotDeleted(state.getParent().getNodeId())
				.orElse(null);

			if (national != null) {

			    nationalName = national.getNodeName();

			    nationalLogo = resolveNodeLogoPath(national);
			}
		    }
		}
	    }

	    break;

	default:
	    break;
	}

	data.put("districtName", districtName);
	data.put("districtLogo", districtLogo);

	data.put("stateName", stateName);
	data.put("stateLogo", stateLogo);

	data.put("nationalName", nationalName);
	data.put("nationalLogo", nationalLogo);

	/*
	 * ====================================== CERTIFICATE TRANSCRIPT DATA
	 * ======================================
	 */

	// Mr / Ms
	String salutation = "Mr";

	if (mei.getMemberEventGender() != null) {

	    switch (mei.getMemberEventGender()) {

	    case FEMALE:
		salutation = "Ms";
		break;

	    default:
		salutation = "Mr";
		break;
	    }
	}

	data.put("participantTitle", salutation);

	// Medal → Place
	String securedPlace = null;

	if (mei.getMemberEventGrade() != null) {

	    switch (mei.getMemberEventGrade()) {

	    case GOLD:
		securedPlace = "First";
		break;

	    case SILVER:
		securedPlace = "Second";
		break;

	    case BRONZE:
		securedPlace = "Third";
		break;

	    default:
		securedPlace = mei.getMemberEventGrade().name();
	    }
	}

	data.put("securedPlace", securedPlace);

	// Representing
	data.put("representing", mei.getMemberEventMember().getUserFname());

	// District
	String certificateDistrict = districtName;

	if (eventNode != null && eventNode.getAddressLine3() != null) {

	    certificateDistrict = eventNode.getAddressLine3();
	}

	data.put("certificateDistrict", certificateDistrict);

	// Event year
	data.put("certificateYear", mei.getMemberEvent().getEventYear());

	// Category
	String certificateCategory = "";

	if (mei.getMemberEventCategory() != null && mei.getMemberEventGender() != null) {

	    certificateCategory = mei.getMemberEventCategory().name() + " " + mei.getMemberEventGender().name();
	}

	data.put("certificateCategory", certificateCategory);

	// Event item
	data.put("certificateItem", mei.getMemberEventItemName());

	// Place
	data.put("certificatePlace", certificateDistrict);

	// Certificate date
	data.put("certificateDate",

		mei.getMemberEvent().getEventPeriodEnd());

	/*
	 * ====================================== MEDAL
	 * ======================================
	 */

	//data.put("medalImage", "/static/images/" + mei.getMemberEventGrade().name() + ".png");
	
	if (mei.getMemberEventGrade() != MemberEventItem.Grade.PARTICIPATION) {
	    File medalFile = ResourceUtils.getFile(
	            "classpath:static/images/"
	            + mei.getMemberEventGrade().name()
	            + ".png"
	    );
	    data.put(
	            "medalImage",
	            "file:" + medalFile.toURI().getPath()
	    );
	} else {
	    data.put("medalImage", null);
	}

	/*
	 * ====================================== QR
	 * ======================================
	 */

	/*
	 * ====================================== SIGNATORIES
	 * ======================================
	 */

	List<Map<String, Object>> signatories = coreUserRepository
		.findBranchSignatories(eventNode.getNodeId(), UserType.OFFICIAL).stream()

		.map(u -> {

		    Map<String, Object> m = new HashMap<>();

		    m.put("id", u.getUserId());

		    m.put("name", (u.getUserFname() != null ? u.getUserFname() : "")
			    + (u.getUserLname() != null ? " " + u.getUserLname() : ""));

		    m.put("designation",

			    u.getDesignation() != null ? u.getDesignation().getDesigName() : null);

		    m.put("phone", u.getMobileNumber());

		    m.put("offdesc", u.getOfficialDescription());

		    m.put("email", u.getUserEmail());

		    /*
		     * Signature
		     */

		    String signature = null;

		    if (u.getSignatureFile() != null) {

			CoreFile sf = fileRepository.findById(u.getSignatureFile()).orElse(null);

			if (sf != null && sf.getFilePath() != null) {

			    File signatureFile = new File(BASE_PATH, sf.getFilePath());

			    if (signatureFile.exists()) {

				signature = "file:" + signatureFile.toURI().getPath();
			    }
			}
		    }

		    m.put("signature", signature);

		    return m;

		})

		.toList();

	data.put("signatories", signatories);
	
	
	/*
	 * ======================================
	 * PARTICIPANT PHOTO
	 * ======================================
	 */

	String participantPhoto = null;

	if (mei.getMemberEventMember() != null
	        && mei.getMemberEventMember().getPhotoFile() != null) {

	    CoreFile pf =
	            fileRepository
	                    .findById(
	                            mei.getMemberEventMember().getPhotoFile()
	                    )
	                    .orElse(null);

	    if (pf != null && pf.getFilePath() != null) {

		    File photoFile =
		            new File(
		                    BASE_PATH,
		                    pf.getFilePath()
		            );

		    if (photoFile.exists()) {

		        File thumbnailFile =
		                new File(
		                        photoFile.getParentFile(),
		                        "thumbnails/"
		                        + pf.getFileId()
		                        + ".jpg"
		                );

		        participantPhoto =
		                "file:"
		                + (
		                    thumbnailFile.exists()
		                    ? thumbnailFile
		                    : photoFile
		                )
		                .toURI()
		                .getPath();
		    }
		}
	}

	data.put(
	        "participantPhoto",
	        participantPhoto
	);

	Long meiId = mei.getMeiId();

	String maskedId = SimpleBase64.encode(meiId, fileName);

	byte[] qrBytes = QrCodeUtil
		.generateQrCodeBytes("https://app.indiankalaripayattufederation.com/verify?id=" + maskedId);

	Path tempQrFile = Files.createTempFile("qr-" + meiId, ".png");

	Files.write(tempQrFile, qrBytes);

	data.put("verificationIdMasked", maskedId);

	data.put("qrImagePath", tempQrFile.toUri().toString());

	return data;
    }

    // Generate unsigned PDF from HTML template
    private byte[] generatePdfFromTemplate(Map<String, Object> data) throws Exception {
	Context context = new Context();
	context.setVariables(data);

	String baseUrl = ResourceUtils.getURL("classpath:static/").toString();
	String htmlContent = templateEngine.process("fragments/events/certificate-single", context);

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	ITextRenderer renderer = new ITextRenderer();
	renderer.getSharedContext().setBaseURL(baseUrl);
	renderer.setDocumentFromString(htmlContent, baseUrl);
	renderer.layout();
	renderer.createPDF(outputStream);
	renderer.finishPDF();

	// Cleanup QR temp file
	if (data.get("qrImagePath") != null) {
	    try {
		Path qrPath = Path.of(new URI(data.get("qrImagePath").toString()));
		Files.deleteIfExists(qrPath);
	    } catch (Exception ignored) {
	    }
	}

	return outputStream.toByteArray();
    }

    // Sign a PDF using PKCS12 private key
    private byte[] signPdf(byte[] unsignedPdf) throws Exception {
	KeyStore ks = KeyStore.getInstance("PKCS12");
	try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
	    ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
	}

	String alias = ks.aliases().nextElement();
	PrivateKey privateKey = (PrivateKey) ks.getKey(alias, KEYSTORE_PASSWORD.toCharArray());
	Certificate[] chain = ks.getCertificateChain(alias);

	return applySignature(unsignedPdf, privateKey, chain);
    }

    // Actual PDF signing logic
    private byte[] applySignature(byte[] unsignedPdf, PrivateKey privateKey, Certificate[] chain) throws Exception {
	ByteArrayOutputStream signedPdf = new ByteArrayOutputStream();

	PdfReader reader = new PdfReader(new ByteArrayInputStream(unsignedPdf));
	PdfSigner signer = new PdfSigner(reader, signedPdf, new StampingProperties());

	// Invisible rectangle (signature will not be drawn on page)
	Rectangle invisibleRect = new Rectangle(0, 0, 0, 0);

	PdfSignatureAppearance appearance = signer.getSignatureAppearance();
	appearance.setReason("Official Certificate Verification").setLocation("Indian Kalarippayattu Federation")
		.setReuseAppearance(false).setPageRect(invisibleRect).setPageNumber(1)
		.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);

	// Set field name and certification level
	signer.setFieldName("DigitalSignature");
	signer.setCertificationLevel(PdfSigner.CERTIFIED_NO_CHANGES_ALLOWED);

	IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "BC");
	IExternalDigest digest = new BouncyCastleDigest();

	signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);

	return signedPdf.toByteArray();
    }

    private String resolveNodeLogoPath(Node node) {

	    if (node == null || node.getPhotoFile() == null) {
	        return null;
	    }

	    CoreFile file =
	            fileRepository
	                    .findById(
	                            node.getPhotoFile()
	                    )
	                    .orElse(null);

	    if (file == null || file.getFilePath() == null) {
	        return null;
	    }

	    File originalFile =
	            new File(
	                    BASE_PATH,
	                    file.getFilePath()
	            );

	    if (!originalFile.exists()
	            || !originalFile.isFile()
	            || !originalFile.canRead()) {

	        return null;
	    }

	    File thumbnailFile =
	            new File(
	                    originalFile.getParentFile(),
	                    "thumbnails/"
	                    + file.getFileId()
	                    + ".jpg"
	            );

	    String uri =
	            "file:"
	            + (
	                thumbnailFile.exists()
	                ? thumbnailFile
	                : originalFile
	            )
	            .toURI()
	            .getPath();

	    System.out.println(
	            node.getNodeName()
	            + " -> "
	            + uri
	    );

	    return uri;
	}

    @Transactional
    public byte[] generateOrGetSignedCertificate1(MemberEventItem mei) throws Exception {
	// Ensure BC provider is available
	if (Security.getProvider("BC") == null) {
	    Security.addProvider(new BouncyCastleProvider());
	}

	// Use modified or created timestamp
	LocalDateTime timestamp = mei.getTModified() != null ? mei.getTModified() : mei.getTCreated();
	String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));

	// Build directory path
	Path storagePath = Paths.get("uploads", "certificates", String.valueOf(mei.getMemberEvent().getEventYear()),
		String.valueOf(mei.getMemberEventHost().getNodeId()), String.valueOf(mei.getMemberEvent().getEventId()),
		String.valueOf(mei.getMeiId()));
	Files.createDirectories(storagePath);

	// Final file
	Path filePath = storagePath.resolve(formattedTimestamp + ".pdf");

	// ✅ Return if file already exists
	if (Files.exists(filePath)) {
	    System.out.println("Existing certificate found: " + filePath);
	    return Files.readAllBytes(filePath);
	}

	// Generate new unsigned PDF
	Map<String, Object> data = prepareCertificateData(mei, filePath.getFileName().toString());
	byte[] unsignedPdf = generatePdfFromTemplate(data);

	// Sign PDF
	byte[] signedPdf = signPdf(unsignedPdf);

	// Store locally
	Files.write(filePath, signedPdf);
	System.out.println("New certificate stored: " + filePath);

	return signedPdf;
    }

}
