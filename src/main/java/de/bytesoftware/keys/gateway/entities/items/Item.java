package de.bytesoftware.keys.gateway.entities.items;

import java.util.Collection;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bytesoftware.keys.gateway.entities.Genre;
import de.bytesoftware.keys.gateway.entities.Image;
import de.bytesoftware.keys.gateway.entities.Language;
import de.bytesoftware.keys.gateway.entities.Platform;
import de.bytesoftware.keys.gateway.entities.Rating;
import de.bytesoftware.keys.gateway.entities.SystemRequirements;
import de.bytesoftware.keys.gateway.entities.VideoAsset;

public class Item
{
	transient Nodeid							id;

	private static final Logger		LOG	= Logger.getLogger(Item.class);

	@neo("VIDEO_ASSETS")
	public Collection<VideoAsset>	videoAssets;

	@neo("IMAGES")
	public Collection<Image>			images;

	@neo("RATINGS")
	public Collection<Rating>			ratings;

	@neo("LANGUAGES")
	public Collection<Language>		availableLanguages;

	@neo(index = true)
	public String									name;

	@neo(index = true)
	public String									description;

	@neo(index = true)
	public Platform								platform;

	@neo(index = true)
	public Genre									genre;

	@neo(index = true)
	public String									websiteUrl;
	@neo
	private String								platformImageSrc;
	@neo("SYSTEM_REQUIREMENTS")
	private Collection<SystemRequirements>		systemRequirements;

	public Item()
	{
		this.platformImageSrc = "";
		this.platform = Platform.UNDEFINED;
	}

	public Genre getGenre()
	{
		return genre;
	}

	public void setGenre(Genre genre)
	{
		this.genre = genre;
	}

	public Nodeid getId()
	{
		return id;
	}

	public void setId(Nodeid id)
	{
		this.id = id;
	}

	public Collection<VideoAsset> getVideoAssets()
	{
		return videoAssets;
	}

	public void setVideoAssets(Collection<VideoAsset> videoAssets)
	{
		this.videoAssets = videoAssets;
	}

	public Collection<Image> getImage()
	{
		return images;
	}

	public void setImage(Collection<Image> image)
	{
		this.images = image;
	}

	public Collection<Rating> getRatings()
	{
		return ratings;
	}

	public void setRatings(Collection<Rating> ratings)
	{
		this.ratings = ratings;
	}

	public Collection<Language> getAvailableLanguages()
	{
		return availableLanguages;
	}

	public void setAvailableLanguages(Collection<Language> availableLanguages)
	{
		this.availableLanguages = availableLanguages;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Platform getPlatform()
	{
		return platform;
	}

	public void setPlatform(Platform platform)
	{
		this.platform = platform;
	}

	public String getWebsiteUrl()
	{
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl)
	{
		this.websiteUrl = websiteUrl;
	}

	public String getPlatformImageSrc()
	{
		return platformImageSrc;
	}

	public void setPlatformImageSrc(String platformImageSrc)
	{
		this.platformImageSrc = platformImageSrc;
	}

	JSONArray getVideoAssetsAsJSONArray()
	{
		JSONArray retval = new JSONArray();
		for (VideoAsset va : videoAssets)
		{
			if (va == null)
				continue;

			retval.add(va.toJson());
		}
		return retval;
	}

	JSONArray getImagesAsJSONArray()
	{
		JSONArray retval = new JSONArray();
		for (Image image : images)
		{
			if (image == null)
				continue;

			retval.add(image.toJson());
		}
		return retval;
	}

	JSONArray getRatingsAsJSONArray()
	{
		JSONArray retval = new JSONArray();
		for (Rating rating : ratings)
		{
			if (rating == null)
				continue;

			retval.add(rating.toJson());
		}
		return retval;
	}

	JSONArray getLanguagesAsJSONArray()
	{
		JSONArray retval = new JSONArray();
		for (Language language : availableLanguages)
		{
			if (language == null)
				continue;

			retval.add(language.toJson());
		}
		return retval;
	}

	JSONArray getSystemRequirementsAsJSONArray()
	{
		JSONArray retval = new JSONArray();
		for (SystemRequirements sr : systemRequirements)
		{
			if (sr == null)
				continue;

			retval.add(sr.toJson());
		}
		return retval;
	}
	
	public JSONObject toJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("ID", this.id != null ? this.id.id() : -1L);
		jo.put("VIDEO_ASSETS", getVideoAssetsAsJSONArray());
		jo.put("IMAGES", getImagesAsJSONArray());
		jo.put("RATINGS", getRatingsAsJSONArray());
		jo.put("LANGUAGES", getLanguagesAsJSONArray());
		jo.put("GENRE", genre.toString());
		jo.put("PLATFORM", platform.toString());
		jo.put("WEBSITE_URL", websiteUrl);
		jo.put("PLATFORM_IMAGE_SRC", websiteUrl);
		jo.put("NAME", this.name);
		jo.put("DESCRIPTION", this.description);
		jo.put("SYSTEM_REQUIREMENTS", getSystemRequirementsAsJSONArray());
		return jo;
	}
}
