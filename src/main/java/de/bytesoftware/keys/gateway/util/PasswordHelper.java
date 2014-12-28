package de.bytesoftware.keys.gateway.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PasswordHelper {
	private static final Logger LOG = Logger.getLogger(PasswordHelper.class);

	public static void removePasswordFiled(JSONObject json) {
		json.remove("PASSWORD");
	}

	public static final String salt(JSONArray salt, long time, String password) {
		long latestValidTime = (System.currentTimeMillis() - 60000);

		if (time < latestValidTime) {
			LOG.warn("Time has expired");
			return null;
		}

		Iterator ite = salt.iterator();

		StringBuilder saltStart = new StringBuilder("" + time);
		StringBuilder saltValue = new StringBuilder();
		while (ite.hasNext()) {
			int index = ((Long) ite.next()).intValue();
			char c = password.charAt(index);
			saltValue.append(c);
		}

		String firstPart = toSHA256(saltStart.toString() + saltValue.toString());

		StringBuilder sb = new StringBuilder(firstPart);
		sb.append(password);

		String saltedPassword = toSHA256(sb.toString());

		return saltedPassword;
	}

	public static String toSHA256(String string) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			Charset cs = Charset.forName("UTF-8");
			byte[] hash = digest.digest(string.getBytes(cs));

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/*
	 * Returns null if password is ok, error message if validation failes.
	 * 
	 * @password Clear text password
	 */
	public static String checkValidity(String password) {

		if (password == null || password.isEmpty()) {
			return "FAILED: Password is empty";
		}

		if (password.length() < 7) {
			return "FAILED: Password too short (" + password.length() + ").";
		}

		if (!password.matches(".*\\d.*")) {
			return "FAILED: Password doesn't contain a number.";
		}

		return null;

	}

}
