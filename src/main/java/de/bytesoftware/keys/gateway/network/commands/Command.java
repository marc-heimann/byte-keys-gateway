package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONObject;

public interface Command
{
	public JSONObject execute(JSONObject request) throws Exception;
}
