package de.bytesoftware.keys.gateway.entities;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.entities.items.Item;
import de.bytesoftware.keys.gateway.entities.items.OriginItem;
import de.bytesoftware.keys.gateway.entities.items.SteamItem;
import de.bytesoftware.keys.gateway.entities.items.UPlayItem;

public class Key
{
	transient Nodeid						id;

	private static final Logger	LOG			= Logger.getLogger(Key.class);
	@neo
	public KeyType							type		= KeyType.UNDEFINED;
	@neo
	public String								value		= null;
	@neo
	public boolean							active	= true;
	@neo
	public boolean							sold		= false;
	@neo
	public double								price		= 0.0D;
	@neo("FOR_ITEM")
	public Item									item		= null;
	@neo("FROM_ACCOUNT")
	public Account							account	= null;

	public Nodeid getId()
	{
		return id;
	}

	public void setId(Nodeid id)
	{
		this.id = id;
	}

	public KeyType getType()
	{
		return type;
	}

	public void setType(KeyType type)
	{
		this.type = type;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isSold()
	{
		return sold;
	}

	public void setSold(boolean sold)
	{
		this.sold = sold;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public JSONObject toJson()
	{
		JSONObject retval = new JSONObject();
		retval.put("ID", this.id != null ? this.id.id() : -1L);
		retval.put("KEY_TYPE", this.type.toString());
		retval.put("VALUE", this.value);
		retval.put("ACCOUNT", this.account != null ? this.account.toJson() : null);
		retval.put("ACTIVE", this.active);
		retval.put("SOLD", this.sold);
		retval.put("ACTIVE", this.active);

		if (this.item != null)
		{
			if (this.item instanceof SteamItem)
				retval.put("ITEM", ((SteamItem) this.item).toJson());
			else if (this.item instanceof OriginItem)
				retval.put("ITEM", ((OriginItem) this.item).toJson());
			else if (this.item instanceof UPlayItem)
				retval.put("ITEM", ((UPlayItem) this.item).toJson());
			else
				retval.put("ITEM", this.item.toJson());
		}

		return retval;
	}
}
