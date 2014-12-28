package de.bytesoftware.keys.gateway.entities;

public enum Platform
{
	STEAM, ORIGIN, UPLAY, UNDEFINED;

	public Platform parse(String key)
	{
		if (key == null)
			return UNDEFINED;
		for (Platform p : Platform.values())
		{
			if (p.toString().equals(key))
				return p;
		}
		return UNDEFINED;
	}
}
