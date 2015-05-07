import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Aes {
	static SecretKeySpec secretKey = new SecretKeySpec("qwertyuiopasdfgh".getBytes(), "AES");
	
	public static String Encrypt (String msg) {
		String encryptedString = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	public static String Decrypt (String msg) {
		String decryptedString = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptedString = new String(cipher.doFinal(Base64.decodeBase64(msg)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedString;
	}
}
