package de.bytesoftware.keys.gateway.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import de.bytesoftware.keys.gateway.entities.Session;

public class SessionCache
{

	private static final Logger					LOG					= Logger
																											.getLogger(SessionCache.class);

	private static SessionCache					instance		= null;

	ConcurrentHashMap<String, Session>	allSessions	= null;

	public static SessionCache getInstance()
	{
		if (instance == null)
		{
			instance = new SessionCache();
			instance.initialize();
		}
		return instance;
	}

	void initialize()
	{
		allSessions = new ConcurrentHashMap<String, Session>();
	}

	public Session getSessionBySessionId(String sid)
	{
		return allSessions.get(sid);
	}

	public void registerSession(Session session)
	{
		if (allSessions.containsKey(session.getSid()))
			return;

		allSessions.put(session.getSid(), session);
	}

	public void unregisterSession(Session session)
	{
		if (!allSessions.containsKey(session.getSid()))
			return;

		allSessions.remove(session.getSid());
	}

	public void shutdown()
	{
		// TODO Auto-generated method stub

	}
}
