package de.bytesoftware.keys.gateway.entities;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class Rating
{
	transient Nodeid						id;

	private static final Logger	LOG					= Logger.getLogger(Rating.class);

	@neo(index = true)
	private RatingType					ratingType	= null;

	@neo
	private String							stringValue;
	@neo
	private int									intValue;
	@neo
	private long								longValue;
	@neo
	private double							doubleValue;
	@neo
	private Object							objectValue;
	@neo
	private JSONObject					jsonObject;

	public Rating(RatingType type)
	{
		this.setRatingType(type);
	}

	public Rating(RatingType type, int value)
	{
		this.setRatingType(type);
		this.setIntValue(value);
	}

	public Rating(RatingType type, long value)
	{
		this.setRatingType(type);
		this.setLongValue(value);
	}

	public Rating(RatingType type, double value)
	{
		this.setRatingType(type);
		this.setDoubleValue(value);
	}

	public Rating(RatingType type, Object value)
	{
		this.setRatingType(type);
		this.setObjectValue(value);
	}

	public Rating(RatingType type, JSONObject value)
	{
		this.setRatingType(type);
		this.setJsonObject(value);
	}

	public Nodeid getId()
	{
		return id;
	}

	public void setId(Nodeid id)
	{
		this.id = id;
	}

	public RatingType getRatingType()
	{
		return ratingType;
	}

	public void setRatingType(RatingType ratingType)
	{
		this.ratingType = ratingType;
	}

	public String getStringValue()
	{
		return stringValue;
	}

	public void setStringValue(String stringValue)
	{
		this.stringValue = stringValue;
	}

	public int getIntValue()
	{
		return intValue;
	}

	public void setIntValue(int intValue)
	{
		this.intValue = intValue;
	}

	public long getLongValue()
	{
		return longValue;
	}

	public void setLongValue(long longValue)
	{
		this.longValue = longValue;
	}

	public double getDoubleValue()
	{
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue)
	{
		this.doubleValue = doubleValue;
	}

	public Object getObjectValue()
	{
		return objectValue;
	}

	public void setObjectValue(Object objectValue)
	{
		this.objectValue = objectValue;
	}

	public JSONObject getJsonObject()
	{
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}

	public static Logger getLog()
	{
		return LOG;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("ID", this.id != null ? this.id.id() : -1L);
		jo.put("RATING_TYPE", ratingType.toString());
		jo.put("STRING_VALUE", this.stringValue);
		jo.put("INT_VALUE", this.intValue);
		jo.put("LONG_VALUE", this.longValue);
		jo.put("DOUBLE_VALUE", this.doubleValue);
		jo.put("OBJECT_VALUE", this.objectValue);
		jo.put("JSON_VALUE", this.jsonObject);
		return jo;
	}
}
