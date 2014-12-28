package de.bytesoftware.keys.gateway.util;

import java.nio.charset.Charset;

public class Base64Coder {
	
	static String EMPTY_STRING = "".intern();
	
	private final static Charset UTF8 = Charset.forName("UTF-8");

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];
	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			map1[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			map1[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			map1[i++] = c;
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static byte[] map2 = new byte[128];
	static {
		for (int i = 0; i < map2.length; i++)
			map2[i] = -1;
		for (int i = 0; i < 64; i++)
			map2[map1[i]] = (byte) i;
	}

	/**
	 * Encodes a string into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param s
	 *            a String to be encoded.
	 * @return A String with the Base64 encoded data.
	 */
	public static String encodeString(String s) {
		if (s == null)
			return EMPTY_STRING;
		return new String(encode(s.getBytes()));
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @return A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in) {
		return encode(in, 0, in.length);
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *            an array containing the data bytes to be encoded.
	 * @param off
	 *            starting offset in <code>in</code>.
	 * @param len
	 *            number of bytes to process in <code>in</code>.
	 * @return A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in, int off, int len) {

		final int oDataLen = (len * 4 + 2) / 3; // output length without
		// padding
		final int oLen = ((len + 2) / 3) * 4; // output length including
		// padding
		final char[] out = new char[oLen];
		for (int ip = off, op = 0, end = off + len; ip < end;) {
			final int i0 = in[ip++] & 0xff;
			final int i1 = ip < len ? in[ip++] & 0xff : 0;
			final int i2 = ip < len ? in[ip++] & 0xff : 0;
			final int o0 = i0 >>> 2;
			final int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			final int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			final int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}

		return out;

	}

	/**
	 * Decodes a string from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded. (Encoding UTF-8)
	 * @return A String containing the decoded data.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static String decodeString(String s) {
		return new String(decode(s), UTF8);
	}

	/**
	 * Decodes a string from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return A String containing the decoded data.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static String decodeString(String s, Charset cs) {
		return new String(decode(s), cs);
	}

	/**
	 * Decodes a byte array from Base64 format.
	 * 
	 * @param s
	 *            a Base64 String to be decoded.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(String s) {
		return decode(s.toCharArray());
	}

	/**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded data.
	 * 
	 * @param in
	 *            a character array containing the Base64 encoded data.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in) {
		int iLen = in.length;
		if (iLen % 4 != 0)
			throw new IllegalArgumentException(
					"Length of Base64 encoded input string is not a multiple of 4.");
		while (iLen > 0 && in[iLen - 1] == '=')
			iLen--;
		final int oLen = (iLen * 3) / 4;
		final byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			final int i0 = in[ip++];
			final int i1 = in[ip++];
			final int i2 = ip < iLen ? in[ip++] : 'A';
			final int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			final int b0 = map2[i0];
			final int b1 = map2[i1];
			final int b2 = map2[i2];
			final int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			final int o0 = (b0 << 2) | (b1 >>> 4);
			final int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			final int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < oLen)
				out[op++] = (byte) o1;
			if (op < oLen)
				out[op++] = (byte) o2;
		}
		return out;
	}

	private Base64Coder() {
		// No instances, please.
	}
}