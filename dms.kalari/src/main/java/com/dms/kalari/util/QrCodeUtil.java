package com.dms.kalari.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;  // Correct import
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.EncodeHintType;
import java.util.HashMap;
import java.util.Map;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class QrCodeUtil {

    public static byte[] generateQrCodeBytes(String content, int width, int height) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix, config), "PNG", os);
            return os.toByteArray();
        }
    }

    // Convenience method with default size
    public static byte[] generateQrCodeBytes(String content) throws WriterException, IOException {
        return generateQrCodeBytes(content, 100, 100);
    }

    public static String generateQrCodeBase64(String content, int width, int height) throws WriterException, IOException {
        byte[] qrBytes = generateQrCodeBytes(content, width, height);
        return Base64.getEncoder().encodeToString(qrBytes);
    }
}


