/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.utils;

import com.fasterxml.jackson.databind.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;

/**
 *
 * @author CHIEN
 */
@Service
public class QrCodeUtil {

    public byte[] generQrCode(Object obContent, int width, int height) {

        try {
            QRCodeWriter qRCodeWriter = new QRCodeWriter();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonContent = ow.writeValueAsString(obContent);
            BitMatrix bitMatrix = qRCodeWriter.encode(jsonContent, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (WriterException | IOException ex) {
            Logger.getLogger(QrCodeUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String decode(byte[] qrcodeByteArray) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(qrcodeByteArray));
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            Map<DecodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(DecodeHintType.PURE_BARCODE, true);
            String content = reader.decode(bitmap, hintMap).getText();
            return content;
        } catch (ChecksumException | FormatException | NotFoundException | IOException ex) {
            Logger.getLogger(QrCodeUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
