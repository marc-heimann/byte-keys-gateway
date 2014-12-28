package de.bytesoftware.keys.gateway.network;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.network.commands.CommandFactory;

public class CommunicationMessageDispatcher implements IoHandler
{
	private static final Logger	LOG	= Logger
																			.getLogger(CommunicationMessageDispatcher.class);

	Timer												t		= new Timer();

	public CommunicationMessageDispatcher()
	{
		doSignupRequest();
		doLoginRequest();
		// doCreateAccountRequest();
	}

	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception
	{
		LOG.error(arg1.getMessage(), arg1);
	}

	public void messageReceived(IoSession sess, Object message) throws Exception
	{
		JSONObject jsonMessage;

		if (LOG.isDebugEnabled())
		{
			LOG.debug(" --> " + message);
		}

		if (message instanceof JSONObject)
			jsonMessage = (JSONObject) message;
		else
			throw new Exception("message isn't typeof JSONObject");

		JSONObject response = CommandFactory.getInstance().executeCommand(
				jsonMessage);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(" <-- " + response);
		}

		if (response != null)
			sess.write(response);

		sess.close(false);
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("messageSent " + arg1.toString());
		}

	}

	public void sessionClosed(IoSession arg0) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("sessionClosed " + arg0.toString());
		}

	}

	public void sessionCreated(IoSession arg0) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("sessionCreated " + arg0.toString());
		}
	}

	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("sessionIdle " + arg0.toString());
		}

	}

	public void sessionOpened(IoSession arg0) throws Exception
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("sessionOpened " + arg0.toString());
		}
	}

	public void doSignupRequest()
	{
		TimerTask task = new TimerTask()
		{

			@Override
			public void run()
			{
				JSONObject message = new JSONObject();
				message.put("COMMAND", "SIGNUP");

				JSONObject data = new JSONObject();
				data.put("USERNAME", "MARC");
				data.put("PASSWORD", "test");

				message.put("DATA", data);
				try
				{
					messageReceived(null, message);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
			}
		};

		t.schedule(task, 3000);

	}

	public void doLoginRequest()
	{
		TimerTask task = new TimerTask()
		{

			@Override
			public void run()
			{
				JSONObject message = new JSONObject();
				message.put("COMMAND", "LOGIN");
				message.put("SID", "asdfqwerzterdfgh");

				JSONObject data = new JSONObject();
				data.put("USERNAME", "MARC");
				data.put("PASSWORD", "test");

				message.put("DATA", data);
				try
				{
					messageReceived(null, message);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
			}
		};

		t.schedule(task, 7000);

	}

	public void doCreateAccountRequest()
	{
		TimerTask task = new TimerTask()
		{

			@Override
			public void run()
			{
				JSONObject message = new JSONObject();
				message.put("COMMAND", "CREATE_ACCOUNT");
				message.put("SID", "asdfqwerzterdfgh");

				JSONObject data = new JSONObject();
				data.put("NAME", "Erstes Konto");
				data.put("DESCRIPTION",
						"Das Konto auf dem ich die meisten Kontibewegungen haben werde.");

				message.put("DATA", data);
				try
				{
					messageReceived(null, message);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
			}
		};

		t.schedule(task, 15000);

	}

}
