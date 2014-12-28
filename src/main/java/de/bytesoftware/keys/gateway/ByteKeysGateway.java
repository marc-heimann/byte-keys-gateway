package de.bytesoftware.keys.gateway;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import de.bytesoftware.keys.gateway.cache.Cache;
import de.bytesoftware.keys.gateway.cache.SessionCache;
import de.bytesoftware.keys.gateway.network.CommunicationMessageDispatcher;
import de.bytesoftware.keys.gateway.network.ServerConnector;
import de.bytesoftware.keys.gateway.network.filters.JSONFilter;

public class ByteKeysGateway
{
	private static final Logger			LOG	= Logger.getLogger(ByteKeysGateway.class);

	ServerConnector									communicationConnector;
	CommunicationMessageDispatcher	commMessageDispatcher;

	public ByteKeysGateway(InetSocketAddress communicationListenerAddress)
			throws Exception
	{
		if (communicationListenerAddress == null)
			throw new Exception("Communication Listener Address may not null");

		this.startCommunicationMessageConnector(communicationListenerAddress);
		Cache.getInstance();
	}

	void startCommunicationMessageConnector(InetSocketAddress address)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Starting communication message connector ...");
		}

		commMessageDispatcher = new CommunicationMessageDispatcher();
		communicationConnector = new ServerConnector(address);
		communicationConnector.addLast("JSONFilter", new JSONFilter());
		communicationConnector.setHandler(commMessageDispatcher);

		try
		{
			communicationConnector.listen();
		} catch (IOException e)
		{
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public void stop()
	{
		SessionCache.getInstance().shutdown();
		// Cache.getInstance().shutdown();
		// Database.getInstance().shutdown();
	}
}
