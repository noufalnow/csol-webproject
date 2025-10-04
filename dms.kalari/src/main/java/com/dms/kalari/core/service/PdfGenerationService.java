package com.dms.kalari.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

import java.util.Base64;

@Service
public class PdfGenerationService {

    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;
    
    public String generateQrCodeBase64(String text) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitMatrix matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 150, 150);
            MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
            return Base64.getEncoder().encodeToString(stream.toByteArray()); // NO prefix
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    
    public byte[] generateQrCodeBytes(String content) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", os);
            return os.toByteArray();
        }
    }


    @Autowired
    public PdfGenerationService(TemplateEngine templateEngine, ResourceLoader resourceLoader) {
        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
    }
    
    
    public byte[] generateSingleCertificate(Map<String, Object> data) throws Exception {
        Long meiId = (Long) data.get("meiId");

        String verificationUrl = "https://kalari.creativeboard.net/verify?id=" + meiId;
        String base64 = generateQrCodeBase64(verificationUrl);

        data.put("verificationIdMasked", "jsb-" + meiId);
        data.put("qrImage", "data:image/png;base64," + base64);

        Context context = new Context();
        context.setVariables(data);

        String baseUrl = ResourceUtils.getURL("classpath:static/").toString();
        String htmlContent = templateEngine.process("fragments/events/certificate-demo", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setBaseURL(baseUrl);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlContent, baseUrl);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }
    
   

    public byte[] generateMultiPageCertificate(Map<String, Object> data) throws Exception {

        Long meId = (Long) data.get("meEvent");  // Correct key name here
        Integer itemId = (Integer) data.get("itemId");

        // Build verification ID and URL
        String rawVerificationId = (itemId != null) ? meId + "-" + itemId.toString() : meId.toString();
        String verificationUrl = "https://kalari.creativeboard.net/verify?id=" + rawVerificationId;


        // Generate QR code base64
        String base64 = generateQrCodeBase64(verificationUrl);

        // Masked version for display
        String maskedVerificationId = (itemId != null)
        	    ? meId + "-XXXX" + itemId.toString().substring(Math.max(0, itemId.toString().length() - 2))
        	    : meId.toString();


        
        
        System.out.println("meId: " + meId);
        System.out.println("itemId: " + itemId);
        System.out.println("verificationIdMasked: " + maskedVerificationId);


        // Put into model BEFORE creating context
        data.put("verificationIdMasked", "jsb-" + maskedVerificationId);
        data.put("qrImage", "data:image/png;base64," + base64);

        Context context = new Context();
        context.setVariables(data);

        String baseUrl = ResourceUtils.getURL("classpath:static/").toString();

        // Process template
        String htmlContent = templateEngine.process("fragments/events/certificate-multi", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setBaseURL(baseUrl);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlContent, baseUrl);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }

}