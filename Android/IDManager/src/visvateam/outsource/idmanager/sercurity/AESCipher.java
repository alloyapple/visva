package visvateam.outsource.idmanager.sercurity;

import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AESCipher {
	public static String encrypt(String plainText, String key) {
		try {
			String cipherText = "";
			// Get the KeyGenerator
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256); // 192 and 256 bits may not be available

			byte[] keyBytes = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			byte[] encrypt = cipher.doFinal(plainText.getBytes());
			cipherText = asHex(encrypt);
			return cipherText;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static String decrypt(String cipherText, String key) {
		try {
			String plainText = "";
			// Get the KeyGenerator
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256); // 192 and 256 bits may not be available

			byte[] keyBytes = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] decrypt = cipher.doFinal(hexToBytes(cipherText));
			plainText = new String(decrypt);
			return plainText;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * Turns array of bytes into string
	 * 
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public static byte[] hexToBytes(char[] hex) {
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			int value = (high << 4) | low;
			if (value > 127)
				value -= 256;
			raw[i] = (byte) value;
		}
		return raw;
	}

	public static byte[] hexToBytes(String hex) {
		return hexToBytes(hex.toCharArray());
	}

	public static byte[] encryptImg(String mPath, String key) {
		Bitmap bitmap = BitmapFactory.decodeFile(mPath);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
		byte[] convertToByte = outStream.toByteArray();
		byte[] encrypt = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256); // 192 and 256 bits may not be available

			byte[] keyBytes = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			encrypt = cipher.doFinal(convertToByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;

	}

	public static byte[] decryptImg(byte[] cipher) {
		return null;
	}

}
