package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.cache.SessionCache;
import de.bytesoftware.keys.gateway.entities.Error;
import de.bytesoftware.keys.gateway.entities.Session;
import de.bytesoftware.keys.gateway.util.JsonHelper;

public class Logout implements Command
{

	@Override
	public JSONObject execute(JSONObject request) throws Exception
	{
		JSONObject retval = new JSONObject();

		String sessionId = JsonHelper.getOptionalString(request, "SID", null);

		if (sessionId == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.MISSING_SESSION_ID.toString());
			return retval;
		}

		Session sess = SessionCache.getInstance().getSessionBySessionId(sessionId);

		if (sess == null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.USER_NOT_LOGGED_IN.toString());
			return retval;
		}

		SessionCache.getInstance().unregisterSession(sess);

		retval.put("SUCCESS", true);

		return retval;
	}

}
