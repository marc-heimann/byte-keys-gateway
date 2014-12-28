package de.bytesoftware.keys.gateway.entities;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * Represents a Language. here just a key is provided because the translation
 * will come inside the client. *
 */
public class Language
{
	transient Nodeid						id;

	private static final Logger	LOG	= Logger.getLogger(Language.class);

	@neo
	public String								key;
	@neo
	public String								imageId;

	public Nodeid getId()
	{
		return id;
	}

	public void setId(Nodeid id)
	{
		this.id = id;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getImageId()
	{
		return imageId;
	}

	/**
	 * Sets the id of the image who represents the language e.g. as a flag
	 * 
	 * @param imageId
	 *          might be a url or just an identifier
	 */
	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}

	public static Logger getLog()
	{
		return LOG;
	}

	@SuppressWarnings("unchecked")
	public Object toJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("KEY", this.key);
		jo.put("IMAGE_ID", this.imageId);
		return jo;
	}

}
