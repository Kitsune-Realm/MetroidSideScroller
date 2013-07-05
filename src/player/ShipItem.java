package player;

import java.io.Serializable;

import control.GameUnit;
import effects.Beam;

public class ShipItem implements Serializable
{
	private String name;
	private boolean value;
	private boolean enabled;
	private String description;
	private int type;
	private Beam beam;	
	
	public ShipItem(String name, boolean value, boolean enabled, String description)
	{
		this.name = name;
		this.value = value;
		this.enabled = enabled;
		this.description = description;
	}
	public ShipItem(String name, boolean value, boolean enabled, String description, int type)
	{
		this.name = name;
		this.value = value;
		this.enabled = enabled;
		this.description = description;
		this.type = type;
	}
	public ShipItem(String name, boolean value, boolean enabled, String description, Beam beam)
	{
		this.name = name;
		this.value = value;
		this.enabled = enabled;
		this.description = description;
		this.beam = beam;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean getValue()
	{
		return value;
	}
	public void setValue(boolean value)
	{
		this.value = value;
	}
	public boolean isEnabled()
	{
		return enabled;
	}
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public Beam getBeam()
	{
		return beam;
	}
	public void setBeam(Beam beam)
	{
		this.beam = beam;
	}	
}
