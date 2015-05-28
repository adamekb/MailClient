import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class Hash {

	public static String createHash(char[] password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[24];
		random.nextBytes(salt);

		byte[] hash = pbkdf2(password, salt, 1000, 24);
		Arrays.fill(password, '\u0000');

		return 1000 + ":" + toHex(salt) + ":" +  toHex(hash);
	}
	
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
	        throws NoSuchAlgorithmException, InvalidKeySpecException {
		
	        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        Arrays.fill(password, '\u0000');
	        return skf.generateSecret(spec).getEncoded();
	    }
	
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
        	return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
        	return hex;
        }   
    }
}
