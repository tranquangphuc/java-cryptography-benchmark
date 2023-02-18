package com.tranquangphuc.crypto;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

	static final String PASSWORD = "0123456789abcdef0123456789abcdef";
	static final String TEXT = "地キ教叙だト員釧ばま了蓄ば黒早後ヌエフ両29右う能期皇スサ複詳全フぎえ初微もぼし。1記ナケ転終長クメナサ事全起しくひ紙研え千熱野ユコク必16鋭ツ名著謎ょ時修級並ぜえはび。海ヲロク新読っ中第ルあ紙有だろなン兵特イト社32写知記ウエキハ試的すせ野続ぎーじの成効やぽざ個由ねぽれ浜又ライじ。";

	public static final String CBC_ALGORITHM = "AES/CBC/PKCS5Padding";
	public static final String GCM_ALGORITHM = "AES/GCM/NoPadding";
	public static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
	public static final int IV_LENGTH_BYTE = 16;
	public static final int SALT_LENGTH_BYTE = 16;
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
	public static final SecretKey SECRET_KEY = CryptoUtils.getAesKey(CryptoUtils.PASSWORD.toCharArray(), salt);
	public static byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
	public static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec(iv);

	public static final String HEADER = "=CrYp70=\r\n\r\n";
	public static final byte[] HEADER_BYTE = HEADER.getBytes(UTF_8);

	public static byte[] getRandomNonce(int length) {
		byte[] nonce = new byte[length];
		try {
			SecureRandom.getInstanceStrong().nextBytes(nonce);
		} catch (NoSuchAlgorithmException e) {
			new SecureRandom().nextBytes(nonce);
		}
		return nonce;
	}

	public static SecretKey getAesKey(int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		keygen.init(keySize, SecureRandom.getInstanceStrong());
		return keygen.generateKey();
	}

	public static SecretKey getAesKey(char[] password, byte[] salt) {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password, salt, 65535, 256);
			SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			return secretKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}
}
