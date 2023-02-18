/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tranquangphuc.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.openjdk.jmh.annotations.Benchmark;

//@State(Scope.Benchmark)
public class AesCbcPKCS5Benchmark {

	@Benchmark
	public void testMethod() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] encrypt = encrypt(CryptoUtils.TEXT.getBytes(CryptoUtils.UTF_8));
		byte[] decrypt = decrypt(encrypt);
		assert CryptoUtils.TEXT.equals(new String(decrypt, CryptoUtils.UTF_8));
	}

	public byte[] encrypt(byte[] source) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(CryptoUtils.CBC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, CryptoUtils.SECRET_KEY, CryptoUtils.IV_PARAMETER_SPEC);
		byte[] encrypt = cipher.doFinal(source);
		return ByteBuffer.allocate(CryptoUtils.HEADER_BYTE.length + encrypt.length).put(CryptoUtils.HEADER_BYTE)
				.put(encrypt).array();
	}

	public byte[] decrypt(byte[] source) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(CryptoUtils.CBC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, CryptoUtils.SECRET_KEY, CryptoUtils.IV_PARAMETER_SPEC);
		ByteBuffer bb = ByteBuffer.wrap(source);
		byte[] header = new byte[CryptoUtils.HEADER_BYTE.length];
		bb.get(header);
		byte[] encrypt = new byte[bb.remaining()];
		bb.get(encrypt);
		return cipher.doFinal(encrypt);
	}
}
