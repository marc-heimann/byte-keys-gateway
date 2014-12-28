package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.entities.Platform;

public class GetPlatforms implements Command
{

	// {"COMMAND":"GET_PLATFORMS"}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject execute(JSONObject request) throws Exception
	{
		JSONObject retval = new JSONObject();
		JSONArray platforms = new JSONArray();
		for (Platform p : Platform.values())
		{
			platforms.add(p.toString());
		}
		retval.put("SUCCESS", true);
		retval.put("DATA", platforms);
		return retval;
	}
}
