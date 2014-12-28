package de.bytesoftware.keys.gateway.entities;

/**
 * SystemRequirements can hold more than one OperatingSystem e.g. WINDOWS7,
 * WINDOWS8, WINDOWS81
 **/
public enum OperatingSystem
{
	WINDOWS7, WINDOWS8, WINDOWS81, WINDOWS9, LINUX, MACOS, IOS, ANDROID, XBOX360, XBOXONE, PS3, PS4, NINTENDOWIIU, NINTENDOWII, NINTENDO3DS, NINTENDO3D, PSVITAE, UNKNOWN;

	public static OperatingSystem parse(String type)
	{
		if (type == null)
			return UNKNOWN;

		for (OperatingSystem system : OperatingSystem.values())
		{
			if (system.toString().equals(type))
				return system;
		}
		return UNKNOWN;
	}
}
