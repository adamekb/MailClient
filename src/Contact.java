import java.security.Key;

import javax.crypto.spec.SecretKeySpec;


public class Contact {
	private String userName;
	private SecretKeySpec aesKey;
	private Key publicKey;
	
	public Contact (String userName, Key publicKey, SecretKeySpec aesKey) {
		this.userName = userName;
		this.aesKey = aesKey;
		this.publicKey = publicKey;
	}
	
	public String getUserName () {
		return userName;
	}
	
	public SecretKeySpec getAesKey () {
		return aesKey;
	}

	public Key getPublicKey () {
		return publicKey;
	}
}
