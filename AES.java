import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AES {

     static byte[] CIPHER_KEY = "1234567812345678".getBytes(StandardCharsets.UTF_8);
     static byte[] IV = "1234567890ABCDEF".getBytes(StandardCharsets.UTF_8);
     static char PADDING_CHAR = '\034';

    public static String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(CIPHER_KEY, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
        int paddingSize = 16 - text.length() % 16;
        String padding = String.format("%0" + paddingSize + "d", 0).replace('0', PADDING_CHAR);
        String padded = text + padding;
        byte[] encrypted = cipher.doFinal(padded.getBytes(StandardCharsets.UTF_8));
        
	return DatatypeConverter.printBase64Binary(encrypted);
    }

    public static String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(CIPHER_KEY, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
	byte[] encrypted = DatatypeConverter.parseBase64Binary(data);
        String padded = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        
	return padded.replaceAll(PADDING_CHAR + "+$", "");
    }


    public static void main(String[] args) {

        try {

            String message = "Nuke them All";
		
            System.out.println("secret message: " + message);
            String encrypted_message = encrypt(message);
            System.out.println("    encrypted : " + encrypted_message);
            String decrypted_message = decrypt(encrypted_message);
            System.out.println("    decrypted : " + decrypted_message);
            if (message.equals(decrypted_message)) {
                System.out.println("SUCCESS");
            } else {
                System.out.println("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
