package de.bytesoftware.keys.gateway.entities;

public class Session
{

	String	sid;
	long		dateCreated;
	long		dateLastSeen;

	Account		user;

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public long getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(long dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public long getDateLastSeen()
	{
		return dateLastSeen;
	}

	public void setDateLastSeen(long dateLastSeen)
	{
		this.dateLastSeen = dateLastSeen;
	}

	public Account getUser()
	{
		return user;
	}

	public void setUser(Account user)
	{
		this.user = user;
	}

}
