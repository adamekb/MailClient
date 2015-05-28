import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Aes {

	public static String encrypt (String msg, SecretKeySpec secretKey) {
		String encryptedString = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
	
	public static String decrypt (String msg, SecretKeySpec secretKey) {
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
