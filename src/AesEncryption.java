import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AesEncryption {
private byte[] msg;
private byte[] encryptedMsg;
byte[] key = {120, 58, 1, 108, 44, 115, 120, 22, 81, 127, 21, 87, 84, 111, 102, 5};

	public AesEncryption (String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.msg = msg.getBytes();
		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		encryptedMsg = c.doFinal(this.msg);
	}
	
	public String getEncryptedMsg () {
		return encryptedMsg.toString();
	}
}
