package control;
public enum GameUnit
{
	SHIP("Player - Ship"),
	ZEB("Enemy - Zeb"),
	GAMET("Enemy - Gamet"),
	KIHUNTER("Enemy - Ki Hunter"),
	REFLEC("Enemy - Reflec"),
	BULL("Enemy - Bull"),
	PREED("Enemy - Preed"),
	POWERBIRTH("Explode - Power"),
	PLASMABIRTH("Explode - Plasma"),
	PLASMACHARGEBIRTH("Explode - Charged Plasma"),
	MISCITEM("Misc"),
	HULLITEM("Hull"),
	BEAMITEM("Beams");
	
	
	private final String desc;
	
	GameUnit(String description)
	{
		desc = description;
	}
		
	public String toString() 
	{
		return desc;
	}
}
