package de.bytesoftware.keys.gateway.entities;

public enum KeyType
{
	CODE, URL, UNDEFINED;

	public static KeyType parse(String key)
	{
		if (key == null)
			return UNDEFINED;
		for (KeyType kt : KeyType.values())
		{
			if (kt.toString().equals(key))
				return kt;
		}
		return UNDEFINED;
	}
}
