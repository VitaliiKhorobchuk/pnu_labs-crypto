package lab_9_md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Example {
    
    public static final String TEXT = "Prologistic.com.ua";

    public static void main(String[] args) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(TEXT.getBytes());

        byte byteData[] = md.digest();

        // convert byte to 16 system
        StringBuffer hexString = new StringBuffer();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        System.out.println("Текст в шестнадцатеричном виде : " + hexString.toString());
    }
}