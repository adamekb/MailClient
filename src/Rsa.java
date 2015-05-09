import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;


public class Rsa {
	static KeyPairGenerator generator;

	public static KeyPair generateNewKeys () {
		try {
			generator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generator.generateKeyPair();
	}
	
	public static String encrypt (String msg, Key key) {
		String encryptedString = null;
		try {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.ENCRYPT_MODE, key); 
			encryptedString = Base64.encodeBase64String(c.doFinal(msg.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException | BadPaddingException e) {
			e.printStackTrace();
		}
		return encryptedString;
	}

	public static String decrypt (String msg, Key key) {
		String decryptedString = null;
		try {
			Cipher c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, key);
			decryptedString = new String(c.doFinal(Base64.decodeBase64(msg)));
		} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return decryptedString;
	}
}
