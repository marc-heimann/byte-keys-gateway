package de.bytesoftware.keys.gateway.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerConnector
{
	NioSocketAcceptor			acceptor;
	InetSocketAddress			address;

	private final Logger	LOG	= Logger.getLogger(ServerConnector.class);

	public ServerConnector(InetSocketAddress address)
	{

		if (LOG.isInfoEnabled())
			LOG.info("ServerConnector.ServerConnector: New ServerConnector spawned. "
					+ address);

		this.acceptor = new NioSocketAcceptor();
		this.acceptor.getFilterChain().addFirst(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));

		this.acceptor.getSessionConfig().setReadBufferSize(64);
		this.acceptor.getSessionConfig().setSendBufferSize(64);
		this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		this.address = address;

	}

	public void addLast(String name, IoFilter filter)
	{

		if (LOG.isDebugEnabled())
			LOG.debug("ServerConnector.addLast: Adding IoFilter " + name
					+ " as last element.");

		this.acceptor.getFilterChain().addLast(name, filter);

	}

	public void addFirst(String name, IoFilter filter)
	{

		if (LOG.isDebugEnabled())
			LOG.debug("ServerConnector.addFirst: Adding IoFilter " + name
					+ " as first element.");

		this.acceptor.getFilterChain().addFirst(name, filter);

	}

	public void remove(String name)
	{
		if (LOG.isDebugEnabled())
			LOG.debug("ServerConnector.remove: Removing IoHandler " + name);

		this.acceptor.getFilterChain().remove(name);
	}

	public void setHandler(IoHandler handler)
	{
		if (LOG.isDebugEnabled())
			LOG.debug("ServerConnector.setHandler: Setting IoHandler "
					+ handler.toString());

		this.acceptor.setHandler(handler);
	}

	public void listen() throws IOException
	{

		this.acceptor.bind(address);

		if (LOG.isInfoEnabled())
			LOG.info("ServerConnector.listen: Listening on Port: "
					+ address.getHostName() + ":" + address.getPort());

	}

	public void stop()
	{
		if (LOG.isInfoEnabled())
			LOG.info("ServerConnector.stop: Halting server connector.");

		this.acceptor.unbind();
		this.acceptor.dispose();

		if (LOG.isInfoEnabled())
			LOG.info("ServerConnector.stop: Server connector successfully halted.");
	}

}