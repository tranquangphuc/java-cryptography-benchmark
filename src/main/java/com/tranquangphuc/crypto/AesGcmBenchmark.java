package com.tranquangphuc.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;

import org.openjdk.jmh.annotations.Benchmark;

//@State(Scope.Benchmark)
public class AesGcmBenchmark {

	@Benchmark
	public void testMethod() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] encrypt = encrypt(CryptoUtils.TEXT.getBytes(CryptoUtils.UTF_8));
		byte[] decrypt = decrypt(encrypt);
		assert CryptoUtils.TEXT.equals(new String(decrypt, CryptoUtils.UTF_8));
	}

	public byte[] encrypt(byte[] source) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] iv = CryptoUtils.getRandomNonce(CryptoUtils.IV_LENGTH_BYTE);
		Cipher cipher = Cipher.getInstance(CryptoUtils.GCM_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, CryptoUtils.SECRET_KEY, new GCMParameterSpec(CryptoUtils.TAG_LENGTH_BIT, iv));
		byte[] cipherText = cipher.doFinal(source);
		byte[] cipherTextWithIv = ByteBuffer.allocate(CryptoUtils.HEADER_BYTE.length + iv.length + cipherText.length)
				.put(CryptoUtils.HEADER_BYTE).put(iv).put(cipherText).array();
		return cipherTextWithIv;
	}

	public byte[] decrypt(byte[] source) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		ByteBuffer bb = ByteBuffer.wrap(source);
		byte[] header = new byte[CryptoUtils.HEADER_BYTE.length];
		bb.get(header);
		byte[] iv = new byte[CryptoUtils.IV_LENGTH_BYTE];
		bb.get(iv);
		byte[] cipherText = new byte[bb.remaining()];
		bb.get(cipherText);
		Cipher cipher = Cipher.getInstance(CryptoUtils.GCM_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, CryptoUtils.SECRET_KEY, new GCMParameterSpec(CryptoUtils.TAG_LENGTH_BIT, iv));
		return cipher.doFinal(cipherText);
	}
}
