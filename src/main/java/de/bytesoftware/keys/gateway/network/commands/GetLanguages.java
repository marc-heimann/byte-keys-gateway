package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.cache.Cache;
import de.bytesoftware.keys.gateway.entities.Language;

public class GetLanguages implements Command
{

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject execute(JSONObject request) throws Exception
	{
		JSONObject retval = new JSONObject();
		JSONArray languges = new JSONArray();
		for (Language lan : Cache.getInstance().getLanguages())
			languges.add(lan.toJson());
		retval.put("SUCCESS", true);
		retval.put("DATA", languges);
		return retval;
	}
}
