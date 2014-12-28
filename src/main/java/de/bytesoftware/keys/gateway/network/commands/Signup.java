package de.bytesoftware.keys.gateway.network.commands;

import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.cache.Cache;
import de.bytesoftware.keys.gateway.entities.Error;
import de.bytesoftware.keys.gateway.entities.Account;
import de.bytesoftware.keys.gateway.util.Helper;
import de.bytesoftware.keys.gateway.util.JsonHelper;
import de.bytesoftware.keys.gateway.util.PasswordHelper;

public class Signup implements Command
{

	public String getCommand()
	{
		return "SIGNUP";
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

		JSONObject data = (JSONObject) request.get("DATA");

		String username = JsonHelper.getOptionalString(data, "USERNAME", null);
		String password = JsonHelper.getOptionalString(data, "PASSWORD", null);

		String salt = Helper.getRandomToken(Helper.getRandomSaltLength(20),
				System.currentTimeMillis() * Thread.currentThread().getId());

		String pw256 = PasswordHelper.toSHA256(salt + password);

		Account user = Cache.getInstance().getAccountByUsername(username);
		if (user != null)
		{
			retval.put("SUCCESS", false);
			retval.put("ERROR", Error.USER_ALLREADY_EXISTS.toString());
			return retval;
		}

		user = new Account();
		user.username = username;
		user.password = pw256;
		user.salt = salt;

		Cache.getInstance().createAccount(user);

		retval.put("SUCCESS", true);

		return retval;
	}

}
