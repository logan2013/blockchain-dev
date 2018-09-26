package org.pl.blockchain.util;

public class StringByte {

	/**
	 * 转换字符串为32位字节组
	 * @param str
	 * @return
	 */
	public static byte[] stringToBytes32(String str) {
		byte[] bytes = new byte[32];
		System.arraycopy(str.getBytes(), 0, bytes, 32 - str.length(), str.length());
		return bytes;
	}
}
