package de.bytesoftware.keys.gateway.cache;

import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import de.bytesoftware.keys.gateway.entities.Account;
import de.bytesoftware.keys.gateway.entities.Language;
import de.bytesoftware.keys.gateway.entities.items.Item;

public class Cache
{
	private static final Logger					LOG				= Logger.getLogger(Cache.class);
	private static Cache								instance	= null;
	ConcurrentHashMap<String, Account>	accounts	= new ConcurrentHashMap<String, Account>();
	ConcurrentHashMap<String, Language>	languages	= new ConcurrentHashMap<String, Language>();
	ConcurrentHashMap<String, Item>			items			= new ConcurrentHashMap<String, Item>();
	Timer																timer			= null;

	public static Cache getInstance()
	{
		if (instance == null)
		{
			instance = new Cache();
			instance.initialize();
		}
		return instance;
	}

	void initialize()
	{
		timer = new Timer();
		loadUsersFromDatabase();
		loadLanguagesFromDatabase();
		loadItemsFromDatabase();
	}

	public Account getAccountByUsername(String username)
	{
		return accounts.get(username);
	}

	public void createAccount(Account account)
	{
		Database.getInstance().saveAccount(account);
		accounts.put(account.username, account);
	}

	void loadUsersFromDatabase()
	{
		Collection<Account> allAccounts = Database.getInstance().getAllAccounts();
		if (allAccounts == null)
			return;

		for (Account a : allAccounts)
		{
			if (a == null)
				continue;

			accounts.put(a.username, a);
		}
	}

	public Collection<Language> getLanguages()
	{
		return languages.values();
	}

	public Language getLanguageByKey(String key)
	{
		return languages.get(key);
	}

	public void createLanguage(Language language)
	{
		Database.getInstance().saveLanguage(language);
		languages.put(language.key, language);
	}

	void loadLanguagesFromDatabase()
	{
		Collection<Language> allLanguages = Database.getInstance()
				.getAllLanguages();
		if (allLanguages == null)
			return;

		for (Language l : allLanguages)
		{
			if (l == null)
				continue;

			languages.put(l.key, l);
		}
	}

	void loadItemsFromDatabase()
	{
		Collection<Item> allItems = Database.getInstance().getAllItems();
		if (allItems == null)
			return;

		for (Item i : allItems)
		{
			if (i == null)
				continue;

			items.put(i.name, i);
		}
	}

	public void addItem(Item item)
	{
		Database.getInstance().saveItem(item);
		items.put(item.name, item);
	}

	public Collection<Item> getItems()
	{
		return items.values();
	}

	public Item getItemByName(String name)
	{
		return items.get(name);
	}
}
