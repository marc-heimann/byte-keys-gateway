package de.bytesoftware.keys.gateway.util;

import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public final class JsonHelper {

	/**
	 * try to get an String out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static String getOptionalString(JSONObject jo, String key,
			String fallback) {
		try {
			if (jo == null)
				return fallback;
			Object o = jo.get(key);
			if (o == null)
				return fallback;
			return (String) o;
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * try to get an String out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static String getString(JSONObject jo, String key) {
		return jo.get(key).toString();
	}

	/**
	 * try to get an Double out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static double getOptionalDouble(JSONObject jo, String key,
			double fallback) {
		try {
			if (jo == null)
				return fallback;

			String o = jo.get(key).toString();
			if (o == null)
				return fallback;

			return Double.parseDouble(o);
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * try to get an Double out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static double getDouble(JSONObject jo, String key) {
		return Double.parseDouble(jo.get(key).toString());
	}

	/**
	 * try to get an Integer out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static int getInteger(JSONObject jo, String key) {
		return ((Long) jo.get(key)).intValue();
	}

	/**
	 * try to get an Boolean out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static boolean getBoolean(JSONObject jo, String key) {
		return (Boolean) jo.get(key);
	}

	/**
	 * try to get an Double out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static long getOptionalLong(JSONObject jo, String key, long fallback) {
		try {
			if (jo == null)
				return fallback;

			String o = jo.get(key).toString();
			if (o == null)
				return fallback;

			return Long.parseLong(o);
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * try to get an Double out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static int getOptionalInt(JSONObject jo, String key, int fallback) {
		try {
			if (jo == null)
				return fallback;

			String o = jo.get(key).toString();
			if (o == null)
				return fallback;

			return Integer.parseInt(o);
		} catch (Exception e) {
			return fallback;
		}
	}

	/**
	 * try to get an Long out of an Json-Object
	 * 
	 * @param jo
	 * @param key
	 * @param fallbackValue
	 * @return
	 */
	public static long getLong(JSONObject jo, String key) {
		Object l = jo.get(key);
		if (l instanceof Long)
			return (Long) l;
		if (l instanceof Integer)
			return (Integer) l;
		if (l instanceof String)
			Long.parseLong((String) jo.get(key));
		return (Long) jo.get(key);
	}

	public static long getStringAsLong(JSONObject jo, String key) {
		return Long.parseLong((String) jo.get(key));
	}

	public static boolean getOptionalBoolean(JSONObject jo, String key,
			boolean fallback) {
		try {
			if (jo == null)
				return fallback;
			Object o = jo.get(key);
			if (o == null)
				return fallback;

			return (Boolean) o;
		} catch (Exception e) {
			return fallback;
		}
	}

	public static String pretty(JSONObject jo) {
		if (jo == null)
			return "{}";

		return prettyAppend(new StringBuilder(), jo, 0).toString();
	}

	public static String pretty(JSONArray ja) {
		if (ja == null)
			return "[]";

		return prettyAppend(new StringBuilder(), ja, 0).toString();
	}

	public static StringBuilder prettyAppend(StringBuilder sb, JSONObject jo) {
		return prettyAppend(sb, jo, 0);
	}

	public static StringBuilder prettyAppend(StringBuilder sb, JSONArray ja) {
		return prettyAppend(sb, ja, 0);
	}

	private static StringBuilder prettyAppend(StringBuilder sb, JSONObject jo,
			int level) {

		if (jo == null || jo.size() == 0) {
			sb.append("{}");
			return sb;
		}

		sb.append(getIntent(level));
		sb.append("{\r\n");
		@SuppressWarnings("rawtypes")
		Set s = jo.entrySet();
		boolean first = true;
		for (Object jelement : s) {
			if (jelement instanceof Entry) {
				if (first)
					first = false;
				else
					sb.append(",\r\n");

				@SuppressWarnings("rawtypes")
				Entry v = (Entry) jelement;
				sb.append(getIntent(level + 1));
				sb.append('"');
				sb.append(v.getKey());
				sb.append("\": ");

				appendValue(sb, v.getValue(), level + 1);

				continue;
			}

			sb.append("ERROR PARSING CLASS:");
			sb.append(JSONValue.class);
			sb.append("\r\n");
		}
		sb.append("\r\n");
		sb.append(getIntent(level));
		sb.append("}");

		return sb;

	}

	private static StringBuilder prettyAppend(StringBuilder sb, JSONArray ja,
			int level) {

		if (ja == null || ja.size() == 0) {
			sb.append("[]");
			return sb;
		}

		sb.append("[\r\n");
		boolean first = true;
		for (Object object : ja) {
			if (first)
				first = false;
			else
				sb.append(",\r\n");

			appendValue(sb, object, level + 1);

		}
		sb.append("\r\n");
		sb.append(getIntent(level));
		sb.append("]");

		return sb;
	}

	private final static StringBuilder appendValue(StringBuilder sb, Object jo,
			int level) {

		if (jo instanceof JSONArray) {
			prettyAppend(sb, (JSONArray) jo, level);
			return sb;
		}

		if (jo instanceof JSONObject) {
			prettyAppend(sb, (JSONObject) jo, level);
			return sb;
		}

		sb.append(JSONValue.toJSONString(jo));

		return sb;

	}

	private final static String getIntent(int level) {
		switch (level) {
		case 0:
			return "";
		case 1:
			return "  ";
		case 2:
			return "    ";
		case 3:
			return "      ";
		case 4:
			return "        ";
		case 5:
			return "          ";
		case 6:
			return "            ";
		case 7:
			return "              ";
		case 8:
			return "                ";

		default:
			int num = level * 2;
			final StringBuilder s = new StringBuilder();
			for (int i = 0; i < num; i++) {
				s.append(' ');
			}
			return s.toString();
		}
	}

}
