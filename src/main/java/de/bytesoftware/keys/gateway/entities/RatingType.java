package de.bytesoftware.keys.gateway.entities;

public enum RatingType
{
	ERSB, TIGRS, USK, PEGI, ONLINE_GAME, NONE;

	public static RatingType parse(String type)
	{
		if (type == null)
			return NONE;

		for (RatingType t : RatingType.values())
		{
			if (t.toString().equals(type))
				return t;
		}
		return NONE;
	}
}