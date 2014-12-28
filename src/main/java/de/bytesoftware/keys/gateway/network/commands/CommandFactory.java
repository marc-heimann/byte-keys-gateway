package de.bytesoftware.keys.gateway.network.commands;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class CommandFactory
{
	static final Logger									LOG								= Logger
																														.getLogger(CommandFactory.class);

	static CommandFactory								instance					= null;

	ConcurrentHashMap<String, Class<?>>	registeredClasses	= new ConcurrentHashMap<String, Class<?>>();

	public static CommandFactory getInstance()
	{
		if (instance == null)
		{
			instance = new CommandFactory();
			instance.registerCommands();
		}

		return instance;
	}

	void registerCommands()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Registering commands...");
		}

		registerCommand("SIGNUP", Signup.class);
		registerCommand("LOGIN", Login.class);
		registerCommand("LOGOUT", Logout.class);
		registerCommand("GET_PLATFORMS", GetPlatforms.class);
		registerCommand("GET_GENRES", GetGenres.class);
		registerCommand("GET_LANGUAGES", GetLanguages.class);

	}

	void registerCommand(String key, Class<?> clazz)
	{
		registeredClasses.put(key, clazz);
	}

	public JSONObject executeCommand(JSONObject request) throws Exception
	{
		if (!request.containsKey("COMMAND"))
			throw new Exception("COMMAND KEY MISSING");

		String commandKey = (String) request.get("COMMAND");

		Command comm = getCommand(commandKey);

		return comm.execute(request);
	}

	private Command getCommand(String commandKey) throws Exception
	{
		if (commandKey == null)
			throw new Exception("Command key may not be null");

		Class clazz = registeredClasses.get(commandKey);
		Object rawObj = clazz.newInstance();

		if (!(rawObj instanceof Command))
			throw new Exception("Command class invalid for key: " + commandKey);

		return (Command) rawObj;
	}
}
