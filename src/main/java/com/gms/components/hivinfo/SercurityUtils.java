package com.gms.components.hivinfo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author vvthanh
 */
public class SercurityUtils {

    public static final String UTF8 = "UTF-8";

    private static byte[] hashkey(String key, boolean useHashing) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = key.getBytes(Charset.forName(UTF8));
        if (useHashing) {
            MessageDigest m = MessageDigest.getInstance("MD5");
            bytes = m.digest(bytes);
        }
        if (bytes.length == 16) {
            byte[] tmpKey = new byte[24];
            System.arraycopy(bytes, 0, tmpKey, 0, 16);
            System.arraycopy(bytes, 0, tmpKey, 16, 8);
            bytes = tmpKey;
        }
        return bytes;
    }

    public static String encrypt(String toEncrypt, String hashkey, boolean useHashing) throws Exception {
        byte[] key = hashkey(hashkey, useHashing);
//        Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        Cipher c = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
//                new IvParameterSpec(new byte[8]));
        byte[] encrypted = c.doFinal(toEncrypt.getBytes(Charset.forName(UTF8)));
        return Base64.encodeBase64String(encrypted);
    }

    public static String decrypt(String cipherString, String hashkey, boolean useHashing) throws Exception {
        byte[] key = hashkey(hashkey, useHashing);
        
        Cipher c = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
        
        byte[] decrypted = c.doFinal(Base64.decodeBase64(cipherString));
        return new String(decrypted, Charset.forName(UTF8));
    }

    public static void main(String[] args) throws Exception {
        String key = SercurityUtils.encrypt("", "@1980đội", true);
//        String key = SercurityUtils.encrypt("Nguyễn thị Thụ ế ồ ố Thuỷ", "@1980đội", true);
//        String key = SercurityUtils.encrypt("Vũ Văn Thành", "@1980đội", true);
        System.out.println(key);
        System.out.println("----> " + SercurityUtils.decrypt(key, "@1980đội", true));
//        System.out.println("--------------");
        System.out.println("----> " + SercurityUtils.decrypt("PkvHZghSpHG8FSvVjP4lWmYK9o6Q+B4X", "@1980đội", true));
        System.out.println("----> " + SercurityUtils.decrypt("DJACQF78CIsdC8OdUUUngw==", "@1980đội", true));
//        System.out.println("----> " + SercurityUtils.decrypt("YDsa49qjxt8=", "@1980đội", true));
//        System.out.println("----> " + SercurityUtils.decrypt("JXYIzBPlIR2aIBQT6WJUSH0/jfmsqL9Y", "@1980đội", true));
//        System.out.println("----> " + SercurityUtils.decrypt("dpP1noX8VGvt8B7h5tvPaXlLSz6mFLHs", "@1980đội", true));
//        System.out.println("----> " + SercurityUtils.decrypt("nGy+nYx4YQ5aNIvGO3I+4A==", "@1980đội", true));
    }
}
