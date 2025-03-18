package com.gms.controller.api.qrcode;

import com.gms.controller.api.ApiController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.poi.sl.draw.BitmapImageRenderer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vvthanh
 */
@RestController
public class BarcodesController extends ApiController {

    /**
     * Create QR code
     *
     * @param barcodeText
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public BufferedImage generateQRCodeImage(String barcodeText, int width, int height) throws Exception {
        Map<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        hints.put(EncodeHintType.MARGIN, 1);

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @GetMapping(value = "/qrcode/{barcode}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> actionImage(@PathVariable("barcode") String barcode,
            @RequestParam(name = "width", required = false, defaultValue = "300") int width,
            @RequestParam(name = "height", required = false, defaultValue = "300") int height)
            throws Exception {
        BufferedImage str = generateQRCodeImage(barcode, width, height);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(str, "jpg", bao);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image.png\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(bao.toByteArray());
    }
}
