package com.dms.kalari.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;

import com.dms.kalari.util.QrCodeUtil;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;

import com.dms.kalari.events.entity.MemberEventItem;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.net.URI;


@Service
public class CertificateService {

    @Autowired
    private TemplateEngine templateEngine;

    private static final String KEYSTORE_PATH = "/etc/ssl/indiankalaripayattufederation/indiankalari.p12";
    private static final String KEYSTORE_PASSWORD = "changeit";

    @Transactional
    public byte[] generateOrGetSignedCertificate(MemberEventItem mei) throws Exception {
        // Ensure BC provider is available
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        // Use modified or created timestamp
        LocalDateTime timestamp = mei.getTModified() != null ? mei.getTModified() : mei.getTCreated();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));

        // Build directory path
        Path storagePath = Paths.get(
                "uploads",
                "certificates",
                String.valueOf(mei.getMemberEvent().getEventYear()),
                String.valueOf(mei.getMemberEventHost().getNodeId()),
                String.valueOf(mei.getMemberEvent().getEventId()),
                String.valueOf(mei.getMeiId())
        );
        Files.createDirectories(storagePath);

        // Final file
        Path filePath = storagePath.resolve(formattedTimestamp + ".pdf");

        // ✅ Return if file already exists
        if (Files.exists(filePath)) {
            System.out.println("Existing certificate found: " + filePath);
            return Files.readAllBytes(filePath);
        }

        // Generate new unsigned PDF
        Map<String, Object> data = prepareCertificateData(mei);
        byte[] unsignedPdf = generatePdfFromTemplate(data);

        // Sign PDF
        byte[] signedPdf = signPdf(unsignedPdf);

        // Store locally
        Files.write(filePath, signedPdf);
        System.out.println("New certificate stored: " + filePath);

        return signedPdf;
    }

    // Prepare all certificate data for the template
    private Map<String, Object> prepareCertificateData(MemberEventItem mei) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("meiId", mei.getMeiId());
        data.put("participantName", mei.getMemberEventMember().getUserFname());
        data.put("eventName", mei.getMemberEvent().getEventName());
        data.put("hostName", mei.getMemberEventHost().getNodeName());
        data.put("resultDate", mei.getApproveDateTime());
        data.put("medalType", mei.getMemberEventGrade().name());
        data.put("itemName", mei.getMemberEventItemName());

        String medalPath = "/static/images/" + mei.getMemberEventGrade().name() + ".png";
        data.put("medalImage", medalPath);

        // Add QR code
        Long meiId = mei.getMeiId();
        byte[] qrBytes = QrCodeUtil.generateQrCodeBytes(
                "https://app.indiankalaripayattufederation.com/verify?id=" + meiId
        );
        Path tempQrFile = Files.createTempFile("qr-" + meiId, ".png");
        Files.write(tempQrFile, qrBytes);
        data.put("verificationIdMasked", "jsb-" + meiId);
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
            } catch (Exception ignored) {}
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

        Rectangle rect = new Rectangle(710f, 13f, 120f, 50f);

        PdfSignatureAppearance appearance = signer.getSignatureAppearance();
        appearance
                .setReason("Official Certificate Verification")
                .setLocation("Indian Kalarippayattu Federation")
                .setLocationCaption("Authority: ")
                .setPageRect(rect)
                .setPageNumber(1)
                .setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);

        signer.setFieldName("DigitalSignature");

        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256, "BC");
        IExternalDigest digest = new BouncyCastleDigest();

        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CADES);

        return signedPdf.toByteArray();
    }
}

