package de.bytesoftware.keys.gateway.cache;

import java.util.Collection;

import jo4neo.ObjectGraph;
import jo4neo.ObjectGraphFactory;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import de.bytesoftware.keys.gateway.entities.Account;
import de.bytesoftware.keys.gateway.entities.Key;
import de.bytesoftware.keys.gateway.entities.Language;
import de.bytesoftware.keys.gateway.entities.items.Item;

public class Database
{
	public static final Logger	LOG				= Logger.getLogger(Database.class);
	String											DB_PATH		= "db";

	private static Database			instance	= null;

	GraphDatabaseService				db				= null;

	ObjectGraph									graph			= null;

	static Object								sync			= new Object();

	public static Database getInstance()
	{
		if (instance == null)
		{
			instance = new Database();

		}

		return instance;
	}

	public Database()
	{
		createGraph();
	}

	private GraphDatabaseService db()
	{
		if (db == null)
		{
			db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);

		}

		return db;
	}

	void createGraph()
	{
		graph = ObjectGraphFactory.instance().get(db());
	}

	void destroyGraph()
	{
		graph.close();
	}

	/*
	 * public boolean deleteAccount(Account acc) { boolean retval = false;
	 * synchronized (sync) { Transaction tx = graph.beginTx(); try {
	 * 
	 * graph.delete(acc); tx.success(); retval = true; } catch (Exception e) {
	 * LOG.error(e.getMessage(), e); tx.failure(); } finally { tx.finish(); }
	 * 
	 * } return retval; }
	 */

	public Collection<Account> getAllAccounts()
	{
		Collection<Account> retval = null;
		synchronized (sync)
		{

			try
			{

				retval = graph.get(Account.class);

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}
		return retval;
	}

	public Account getAccountByUsername(String username)
	{
		Account retval = null;

		synchronized (sync)
		{

			try
			{
				retval = new Account();
				retval = graph.find(retval).where(retval.name).is(username).result();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}

		return retval;
	}

	public void saveAccount(Account acc)
	{

		synchronized (sync)
		{
			Transaction tx = graph.beginTx();
			try
			{

				graph.persist(acc);
				tx.success();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
				tx.failure();
			} finally
			{
				tx.finish();

			}
		}

	}

	public Collection<Language> getAllLanguages()
	{
		Collection<Language> retval = null;
		synchronized (sync)
		{

			try
			{

				retval = graph.get(Language.class);

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}
		return retval;
	}

	public Language getLanguageByKey(String key)
	{
		Language retval = null;

		synchronized (sync)
		{

			try
			{
				retval = new Language();
				retval = graph.find(retval).where(retval.key).is(key).result();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}

		return retval;
	}

	public void saveLanguage(Language lang)
	{

		synchronized (sync)
		{
			Transaction tx = graph.beginTx();
			try
			{

				graph.persist(lang);
				tx.success();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
				tx.failure();
			} finally
			{
				tx.finish();

			}
		}

	}

	public Collection<Item> getAllItems()
	{
		Collection<Item> retval = null;
		synchronized (sync)
		{

			try
			{

				retval = graph.get(Item.class);

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}
		return retval;
	}

	public void saveItem(Item item)
	{
		synchronized (sync)
		{
			Transaction tx = graph.beginTx();
			try
			{

				graph.persist(item);
				tx.success();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
				tx.failure();
			} finally
			{
				tx.finish();

			}
		}
	}

	public void saveKey(Key key)
	{
		synchronized (sync)
		{
			Transaction tx = graph.beginTx();
			try
			{

				graph.persist(key);
				tx.success();

			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
				tx.failure();
			} finally
			{
				tx.finish();

			}
		}
	}

	public Collection<Key> getKeysForItem(Item item)
	{
		Collection<Key> retval = null;
		synchronized (sync)
		{

			try
			{
				Key k = new Key();
				retval = graph.find(k).where(k.item).is(item).results();
			} catch (Exception e)
			{
				LOG.error(e.getMessage(), e);
			} finally
			{

			}
		}
		return retval;
	}

	public void shutdown()
	{
		destroyGraph();
		db.shutdown();
	}

}
