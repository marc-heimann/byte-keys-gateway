package de.bytesoftware.keys.gateway.entities;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class Account
{
	transient Nodeid						id;

	private static final Logger	LOG	= Logger.getLogger(Account.class);

	@neo(index = true)
	public String								name;

	@neo(index = true)
	public String								username;

	@neo
	public String								salt;

	@neo
	public String								password;

	@neo
	long												dateCreated;

	@neo
	double											balance;

	public Account()
	{
		dateCreated = System.currentTimeMillis();
	}

	public JSONObject toJson()
	{
		JSONObject jo = new JSONObject();

		jo.put("NAME", this.name);
		jo.put("USERNAME", this.username);
		jo.put("DATE_CREATED", this.dateCreated);

		return jo;
	}

	public JSONObject toInitialJson()
	{
		JSONObject jo = new JSONObject();

		jo.put("NAME", this.name);
		jo.put("USERNAME", this.username);
		jo.put("DATE_CREATED", this.dateCreated);

		return jo;
	}

}
