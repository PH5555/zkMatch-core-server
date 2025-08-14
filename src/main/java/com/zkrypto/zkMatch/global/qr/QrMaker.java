package com.zkrypto.zkMatch.global.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import com.zkrypto.zkMatch.global.utils.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class QrMaker {

    private static final String DEFAULT_IMAGE_FORMAT = "PNG";
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    public static byte[] makeQr(Object data) {
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = qrCodeWriter.encode(JsonUtil.serializeToJson(data), BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT, hints);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, DEFAULT_IMAGE_FORMAT, pngOutputStream);
            return pngOutputStream.toByteArray();
        }catch (Exception e) {
            throw new CustomException(ErrorCode.VP_QR_MAKE_ERROR);
        }
    }
}
