/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.utils;

import com.fasterxml.jackson.databind.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.function.Function;
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

    private final String SERRET_KEY = "HACKER";

    public byte[] generQrCode(Object obContent, int width, int height) {
        try {
            //object to string json
            QRCodeWriter qRCodeWriter = new QRCodeWriter();
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String jsonContent = ow.writeValueAsString(obContent);
            //encode string json (base64)
//            Base64.Encoder base64 = Base64.getEncoder();
//            String base64Content = base64.encodeToString(jsonContent.getBytes());
            //encode -> qrcode
            String jwtContent  = generrateToken(obContent);
            BitMatrix bitMatrix = qRCodeWriter.encode(jwtContent, BarcodeFormat.QR_CODE, width, height);
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

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SERRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generrateToken(Object object) {
        //convert object to map
        ObjectMapper obMapper = new ObjectMapper();
        Map<String, Object> claims = obMapper.convertValue(object, Map.class);
        return createToken(claims);
    }

    public String createToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SERRET_KEY).compact();
    }

}
