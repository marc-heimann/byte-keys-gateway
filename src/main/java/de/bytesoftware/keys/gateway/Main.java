package de.bytesoftware.keys.gateway;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Main implements Thread.UncaughtExceptionHandler
{
	private static final Logger				LOG										= Logger
																															.getLogger(Main.class);
	static Properties									conf;

	public static final AtomicBoolean	SHUTDOWN_IN_PROGRESS	= new AtomicBoolean(
																															false);

	ByteKeysGateway										service;

	public Main()
	{
		loadConfig();
	}

	public static void main(String[] args)
	{
		final Main main = new Main();
		main.startService();

		final Thread hook = new Thread("ChatServer Shutdown Hook")
		{
			public void run()
			{
				LOG.warn("ShutdownHook.run(): abnormal VM shutdown, trying to tear down the system properly");

				main.close();
			}
		};

		Runtime.getRuntime().addShutdownHook(hook);
	}

	private void startService()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Starting Service ...");
		}

		InetSocketAddress dnsMessageAddress = new InetSocketAddress(
				conf.getProperty("dns.service.listener.host", "0.0.0.0"),
				Integer.parseInt(conf.getProperty("dns.service.listener.port", "7777")));

		InetSocketAddress commMessageAddress = new InetSocketAddress(
				conf.getProperty("communication.service.listener.host", "0.0.0.0"),
				Integer.parseInt(conf.getProperty(
						"communication.service.listener.port", "7778")));

		try
		{
			this.service = new ByteKeysGateway(commMessageAddress);
		} catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
		}

	}

	protected void close()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Closing application ...");
		}

		SHUTDOWN_IN_PROGRESS.compareAndSet(false, true);

		if (service != null)
			service.stop();

		if (LOG.isInfoEnabled())
		{
			LOG.info("Gateway stopped.");
		}
	}

	private void loadConfig()
	{
		conf = new Properties();
		try
		{
			conf.load(new BufferedInputStream(new FileInputStream(new File(
					"config/config.conf"))));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DOMConfigurator.configure(conf.getProperty("log4j.config"));
	}

	public static String getProp(String key)
	{
		return getProp(key, null);
	}

	public static String getProp(String key, String defaultValue)
	{
		return conf.getProperty(key, defaultValue);
	}

	public void uncaughtException(Thread arg0, Throwable arg1)
	{
		LOG.error(arg1.getMessage(), arg1);
	}

	public static void terminate(int i)
	{
		Runtime.getRuntime().halt(i);
	}
}
