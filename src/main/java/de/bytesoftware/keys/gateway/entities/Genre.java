package de.bytesoftware.keys.gateway.entities;

public enum Genre
{
	ACTION, INDIE, ADVANTURE, STRATEGY, RPG, CASUAL, MMO, RACING, SIMULATION, SPORTS, UNDEFINED;

	public static Genre parse(String key)
	{
		if (key == null)
			return UNDEFINED;

		for (Genre g : Genre.values())
		{
			if (g.toString().equals(key))
				return g;
		}
		return UNDEFINED;
	}
}
