import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;


public class Rsa {
	
	public void hej () {
		
		try {
			// Get an instance of the RSA key generator
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			// Generate the keys — might take sometime on slow computers
			KeyPair myPair = kpg.generateKeyPair();
			
			// Get an instance of the Cipher for RSA encryption/decryption
			Cipher c = Cipher.getInstance("RSA");
			// Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
			c.init(Cipher.ENCRYPT_MODE, myPair.getPublic()); 
			
			// Create a secret message
			String myMessage = new String("Secret Message");
			// Encrypt that message using a new SealedObject and the Cipher we created before
			SealedObject myEncryptedMessage = new SealedObject( myMessage, c);
			
			// Get an instance of the Cipher for RSA encryption/decryption
			Cipher dec = Cipher.getInstance("RSA");
			// Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
			dec.init(Cipher.DECRYPT_MODE, myPair.getPrivate());
			
			// Tell the SealedObject we created before to decrypt the data and return it
			String message = (String) myEncryptedMessage.getObject(dec);
			System.out.println("foo = "  + message);
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | IOException | ClassNotFoundException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
