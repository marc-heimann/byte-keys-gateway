package de.bytesoftware.keys.gateway.network.commands;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.cache.Cache;
import de.bytesoftware.keys.gateway.cache.SessionCache;
import de.bytesoftware.keys.gateway.entities.Error;
import de.bytesoftware.keys.gateway.entities.Session;
import de.bytesoftware.keys.gateway.entities.Account;
import de.bytesoftware.keys.gateway.util.JsonHelper;
import de.bytesoftware.keys.gateway.util.PasswordHelper;

public class Login implements Command
{
	private static final Logger	LOG	= Logger.getLogger(Login.class);

	public String getCommand()
	{
		return "LOGIN";
	}

	public JSONObject execute(JSONObject request) throws Exception
	{
		JSONObject retval = new JSONObject();

		if (!request.containsKey("DATA"))
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.DATA_OBJECT_IS_MISSING.toString());
			return retval;
		}

		// {"COMMAND":"LOGIN", "SID":"phpsessionid", "DATA":{"USERNAME":"XXX",
		// "PASSWORD":"XXXXX"}}

		String sessionId = JsonHelper.getOptionalString(request, "SID", null);

		if (sessionId == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.MISSING_SESSION_ID.toString());
			return retval;
		}

		JSONObject data = (JSONObject) request.get("DATA");

		String username = JsonHelper.getOptionalString(data, "USERNAME", null);
		String password = JsonHelper.getOptionalString(data, "PASSWORD", null);

		if (username == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.USERNAME_MAY_NOT_BE_EMPTY.toString());
			return retval;
		}

		if (password == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.PASSWORD_MAY_NOT_BE_EMPTY.toString());
			return retval;
		}

		Account user = Cache.getInstance().getAccountByUsername(username);

		if (user == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.USER_NOT_FOUND.toString());
			return retval;
		}

		String salt = user.salt != null ? user.salt : "";
		String saltedPW = PasswordHelper.toSHA256(salt + password);

		if (saltedPW.equals(user.password))
		{

			Session session = new Session();
			session.setDateCreated(System.currentTimeMillis());
			session.setUser(user);
			session.setSid(sessionId);

			SessionCache.getInstance().registerSession(session);

			retval.put("SUCCESS", true);
			retval.put("DATA", user.toInitialJson());
			return retval;

		} else
		{

			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.USERNAME_OR_PASSWORD_FAILURE.toString());
			return retval;

		}

	}
}
