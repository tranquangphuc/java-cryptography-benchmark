package com.tranquangphuc.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AbstractAesCrypto {

	static final String PASSWORD = "0123456789abcdef0123456789abcdef";
	static final String TEXT = "地キ教叙だト員釧ばま了蓄ば黒早後ヌエフ両29右う能期皇スサ複詳全フぎえ初微もぼし。1記ナケ転終長クメナサ事全起しくひ紙研え千熱野ユコク必16鋭ツ名著謎ょ時修級並ぜえはび。海ヲロク新読っ中第ルあ紙有だろなン兵特イト社32写知記ウエキハ試的すせ野続ぎーじの成効やぽざ個由ねぽれ浜又ライじ。";

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

	public static SecretKey getAesKeyFromPassword(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password, salt, 65535, 256);
		SecretKey secretKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secretKey;
	}
}
