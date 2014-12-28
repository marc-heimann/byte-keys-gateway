package de.bytesoftware.keys.gateway.entities;

import java.util.Collection;

import jo4neo.Nodeid;
import jo4neo.neo;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SystemRequirements
{
	transient Nodeid								id;

	private static final Logger						LOG	= Logger.getLogger(SystemRequirements.class);

	@neo
	private SystemRequirementType					type;

	@neo("operatingsystems")
	public Collection<OperatingSystem>				operatingSystems;
	@neo
	public Collection<String>						processors;
	@neo
	public Collection<String>						graphicsAdapters;
	@neo
	public Collection<String>						hdds;
	@neo
	public Collection<String>						extraInfos;

	private SystemRequirements()
	{
		// NOOP
	}

	public SystemRequirements(SystemRequirementType type)
	{
		this.setType(type);
	}

	public Nodeid getId()
	{
		return id;
	}

	public void setId(Nodeid id)
	{
		this.id = id;
	}

	public SystemRequirementType getType()
	{
		return type;
	}

	public void setType(SystemRequirementType type)
	{
		this.type = type;
	}

	public Collection<OperatingSystem> getOperatingSystems()
	{
		return operatingSystems;
	}

	public void setOperatingSystems(Collection<OperatingSystem> operatingSystems)
	{
		this.operatingSystems = operatingSystems;
	}

	public void addOperatingSystem(OperatingSystem system)
	{
		if (operatingSystems == null)
		{
			LOG.error("Cannot hold system because of missing storage");
			return;
		}
		operatingSystems.add(system);
	}

	public Collection<String> getProcessors()
	{
		return processors;
	}

	public void setProcessors(Collection<String> processors)
	{
		this.processors = processors;
	}

	public void addProcessor(String processorString)
	{
		if (processorString == null)
		{
			LOG.error("Cannot hold Processor because of missing storage");
			return;
		}
		processors.add(processorString);
	}

	public Collection<String> getExtraInfos()
	{
		return extraInfos;
	}

	public void setExtraInfos(Collection<String> extraInfos)
	{
		this.extraInfos = extraInfos;
	}

	public void addExtraInfo(String extraInfo)
	{
		if (extraInfos == null)
		{
			LOG.error("Cannot hold ExtraInfo because of missing storage");
			return;
		}
		extraInfos.add(extraInfo);
	}

	public Collection<String> getGraphicsAdapters()
	{
		return graphicsAdapters;
	}

	public void setGraphicsAdapter(Collection<String> graphicsAdapter)
	{
		this.graphicsAdapters = graphicsAdapter;
	}

	public Collection<String> getHdds()
	{
		return hdds;
	}

	public void setHdds(Collection<String> hdds)
	{
		this.hdds = hdds;
	}

	JSONArray getOperatingSystemsAsJsonArray()
	{
		JSONArray retval = new JSONArray();
		for (OperatingSystem os : operatingSystems)
		{
			if (os == null)
				continue;
			retval.add(os.toString());
		}
		return retval;
	}

	JSONArray getProcessorsAsJsonArray()
	{
		JSONArray retval = new JSONArray();
		for (String os : processors)
		{
			if (os == null)
				continue;
			retval.add(os);
		}
		return retval;
	}

	JSONArray getGraphicsAdaptersAsJsonArray()
	{
		JSONArray retval = new JSONArray();
		for (String os : graphicsAdapters)
		{
			if (os == null)
				continue;
			retval.add(os);
		}
		return retval;
	}

	JSONArray getHddsAsJsonArray()
	{
		JSONArray retval = new JSONArray();
		for (String os : graphicsAdapters)
		{
			if (os == null)
				continue;
			retval.add(os);
		}
		return retval;
	}

	JSONArray getExtraInfosAsJsonArray()
	{
		JSONArray retval = new JSONArray();
		for (String os : extraInfos)
		{
			if (os == null)
				continue;
			retval.add(os);
		}
		return retval;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("ID", this.id != null ? this.id.id() : -1L);
		jo.put("TYPE", this.type.toString());
		jo.put("OPERATING_SYSTEM", getOperatingSystemsAsJsonArray());
		jo.put("PROCESSORS", getProcessorsAsJsonArray());
		jo.put("GRAPHICS_ADAPTERS", getGraphicsAdaptersAsJsonArray());
		jo.put("HDDS", getHddsAsJsonArray());
		jo.put("EXTRA_INFOS", getExtraInfosAsJsonArray());
		return jo;
	}
}
