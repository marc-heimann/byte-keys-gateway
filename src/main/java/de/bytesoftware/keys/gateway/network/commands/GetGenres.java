package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.entities.Genre;

public class GetGenres implements Command
{
	// {"COMMAND":"GET_GENRES"}
	@Override
	public JSONObject execute(JSONObject request) throws Exception
	{
		JSONObject retval = new JSONObject();
		JSONArray genres = new JSONArray();
		for (Genre g : Genre.values())
		{
			genres.add(g.toString());
		}
		retval.put("SUCCESS", true);
		retval.put("DATA", genres);
		return retval;
	}

}
