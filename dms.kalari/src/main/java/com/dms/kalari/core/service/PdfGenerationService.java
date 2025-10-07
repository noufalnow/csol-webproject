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

import com.dms.kalari.util.QrCodeUtil;
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

        // Generate PDF-friendly QR bytes (100x100 px PNG)
        byte[] qrBytes = QrCodeUtil.generateQrCodeBytes(
                "https://app.indiankalaripayattufederation.com/verify?id=" + meiId
        );

        // Use a temporary file for the QR to pass to Thymeleaf/ITextRenderer
        // This avoids Base64 inline in HTML
        Path tempQrFile = Files.createTempFile("qr-" + meiId, ".png");
        Files.write(tempQrFile, qrBytes);

        // Put variables for Thymeleaf template
        data.put("verificationIdMasked", "jsb-" + meiId);

        // Instead of Base64, pass the path to temp file
        // In Thymeleaf: <img th:src="@{${qrImagePath}}" />
        data.put("qrImagePath", tempQrFile.toUri().toString());

        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariables(data);

        // Load HTML template
        String baseUrl = ResourceUtils.getURL("classpath:static/").toString();
        String htmlContent = templateEngine.process("fragments/events/certificate-single", context);

        // Render PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setBaseURL(baseUrl);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlContent, baseUrl);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        // Delete temporary QR file
        Files.deleteIfExists(tempQrFile);

        return outputStream.toByteArray();
    }


}