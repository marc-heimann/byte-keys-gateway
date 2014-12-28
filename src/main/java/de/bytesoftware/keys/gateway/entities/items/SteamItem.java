package de.bytesoftware.keys.gateway.entities.items;

import jo4neo.neo;

import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.entities.Genre;
import de.bytesoftware.keys.gateway.entities.Platform;

public class SteamItem extends Item
{
	@neo(index = true)
	private boolean							halfGamepadSupport		= false;
	@neo(index = true)
	private boolean							fullGamepadSupport		= false;
	@neo(index = true)
	private boolean							singleplayer					= false;
	@neo(index = true)
	private boolean							multiplayer						= false;
	@neo(index = true)
	private boolean							coop									= false;
	@neo(index = true)
	private boolean							steamVrSupport				= false;
	@neo(index = true)
	private boolean							containsLevelEditor		= false;
	@neo(index = true)
	private boolean							steamArchievements		= false;
	@neo(index = true)
	private boolean							steamLeaderBoards			= false;
	@neo(index = true)
	private boolean							steamCloud						= false;
	@neo(index = true)
	private boolean							statistics						= false;
	@neo(index = true)
	private boolean							steamCollectableCards	= false;
	@neo
	public static final String	PLATFORM_IMAGE_SRC		= "http://store.akamai.steamstatic.com/public/images/v5/globalheader_logo.png";

	public SteamItem()
	{
		this.platform = Platform.STEAM;
		this.setPlatformImageSrc(SteamItem.PLATFORM_IMAGE_SRC);
		this.genre = Genre.UNDEFINED;
	}

	public boolean isHalfGamepadSupport()
	{
		return halfGamepadSupport;
	}

	public void setHalfGamepadSupport(boolean halfGamepadSupport)
	{
		this.halfGamepadSupport = halfGamepadSupport;
	}

	public boolean isFullGamepadSupport()
	{
		return fullGamepadSupport;
	}

	public void setFullGamepadSupport(boolean fullGamepadSupport)
	{
		this.fullGamepadSupport = fullGamepadSupport;
	}

	public boolean isSingleplayer()
	{
		return singleplayer;
	}

	public void setSingleplayer(boolean singleplayer)
	{
		this.singleplayer = singleplayer;
	}

	public boolean isMultiplayer()
	{
		return multiplayer;
	}

	public void setMultiplayer(boolean multiplayer)
	{
		this.multiplayer = multiplayer;
	}

	public boolean isCoop()
	{
		return coop;
	}

	public void setCoop(boolean coop)
	{
		this.coop = coop;
	}

	public boolean isSteamVrSupport()
	{
		return steamVrSupport;
	}

	public void setSteamVrSupport(boolean steamVrSupport)
	{
		this.steamVrSupport = steamVrSupport;
	}

	public boolean isContainsLevelEditor()
	{
		return containsLevelEditor;
	}

	public void setContainsLevelEditor(boolean containsLevelEditor)
	{
		this.containsLevelEditor = containsLevelEditor;
	}

	public boolean isSteamArchievements()
	{
		return steamArchievements;
	}

	public void setSteamArchievements(boolean steamArchievements)
	{
		this.steamArchievements = steamArchievements;
	}

	public boolean isSteamLeaderBoards()
	{
		return steamLeaderBoards;
	}

	public void setSteamLeaderBoards(boolean steamLeaderBoards)
	{
		this.steamLeaderBoards = steamLeaderBoards;
	}

	public boolean isSteamCloud()
	{
		return steamCloud;
	}

	public void setSteamCloud(boolean steamCloud)
	{
		this.steamCloud = steamCloud;
	}

	public boolean isStatistics()
	{
		return statistics;
	}

	public void setStatistics(boolean statistics)
	{
		this.statistics = statistics;
	}

	public boolean isSteamCollectableCards()
	{
		return steamCollectableCards;
	}

	public void setSteamCollectableCards(boolean steamCollectableCards)
	{
		this.steamCollectableCards = steamCollectableCards;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject jo = super.toJson();
		jo.put("HALF_GAMEPAD_SUPPORT", this.halfGamepadSupport);
		jo.put("FULL_GAMEPAD_SUPPORT", this.fullGamepadSupport);
		jo.put("SINGLEPLAYER", this.singleplayer);
		jo.put("MULTIPLAYER", this.multiplayer);
		jo.put("COOP", this.coop);
		jo.put("STEAM_VR_SUPPORT", this.steamVrSupport);
		jo.put("CONTAINS_LEVEL_EDITOR", this.containsLevelEditor);
		jo.put("STEAM_ARCHIEVEMENTS", this.steamArchievements);
		jo.put("STEAM_LEADER_BOARDS", this.steamLeaderBoards);
		jo.put("STEAM_CLOUD", this.steamCloud);
		jo.put("STATISTICS", this.statistics);
		jo.put("STEAM_COLLECTIBLE_CARDS", this.steamCollectableCards);
		return jo;
	}

}
