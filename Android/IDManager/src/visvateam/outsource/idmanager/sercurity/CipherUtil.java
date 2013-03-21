package visvateam.outsource.idmanager.sercurity;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.graphics.Bitmap;

public class CipherUtil {
	private static final String password = "your_password";
	private static String initializationVector = "your_iv";
	private static String salt = "your_salt";
	private static int pswdIterations = 1;
	private static int keySize = 256;

	public static byte[] encrypt(Bitmap bMap) throws

	NoSuchAlgorithmException,

	InvalidKeySpecException,

	NoSuchPaddingException,

	InvalidParameterSpecException,

	IllegalBlockSizeException,

	BadPaddingException,

	UnsupportedEncodingException,

	InvalidKeyException,

	InvalidAlgorithmParameterException

	{

		byte[] saltBytes = salt.getBytes("UTF-8");

		byte[] ivBytes = initializationVector.getBytes("UTF-8");

		// Derive the key, given password and salt.

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");

		PBEKeySpec spec = new PBEKeySpec(

		password.toCharArray(),

		saltBytes,

		pswdIterations,

		keySize

		);

		SecretKey secretKey = factory.generateSecret(spec);

		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, secret);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		bMap.compress(Bitmap.CompressFormat.PNG, 60, outStream);
		byte[] convertToByte = outStream.toByteArray();
		byte[] encryptedBytes = cipher.doFinal(convertToByte);

		return encryptedBytes;

	}

	public static byte[] decrypt(byte[] cipherBytes) throws

	NoSuchAlgorithmException,

	InvalidKeySpecException,

	NoSuchPaddingException,

	InvalidKeyException,

	InvalidAlgorithmParameterException,

	UnsupportedEncodingException

	{

		byte[] saltBytes = salt.getBytes("UTF-8");

		byte[] ivBytes = initializationVector.getBytes("UTF-8");

		// byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

		// Derive the key, given password and salt.

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");

		PBEKeySpec spec = new PBEKeySpec(

		password.toCharArray(),

		saltBytes,

		pswdIterations,

		keySize

		);

		SecretKey secretKey = factory.generateSecret(spec);

		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

		// Decrypt the message, given derived key and initialization vector.

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, secret);

		byte[] decryptedBytes = null;

		try {

			decryptedBytes = cipher.doFinal(cipherBytes);

		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();

		} catch (BadPaddingException e) {

			e.printStackTrace();

		}

		return decryptedBytes;

	}

}
