package de.bytesoftware.keys.gateway.network.filters;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONFilter extends IoFilterAdapter
{
	private static final Logger	LOG			= Logger.getLogger(JSONFilter.class);

	private JSONParser					parser	= new JSONParser();

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		if (!(message instanceof String))
		{
			throw new Exception("message is not an String");
		}

		if (LOG.isDebugEnabled())
			LOG.debug("Incoming Message from " + session.getRemoteAddress());

		LOG.debug(message);

		JSONObject forwardMessage = (JSONObject) parser.parse((String) message);

		nextFilter.messageReceived(session, forwardMessage);
	}
}