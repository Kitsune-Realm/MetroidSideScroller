package effects;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Beam
{
	POWER("Powerbeam", "charge_beam.gif"),
	SINUS("Sinusbeam", "sinus_beam.gif"),
	PLASMA("Plasmabeam", "plasma_beam.gif"),
	TESLA("Teslabeam", ""),
	PLASMACHARGE("Plasma Charged beam", "plasma_beam.gif"),
	ACIDSPIT("Kihunter spit", "kihunter_acid.gif");
	
	private final String desc;
	private final String beamIcon;
	
	Beam(String description, String beamIcon)
	{
		desc = description;
		this.beamIcon = beamIcon;
	}
	
	public String getBeamIcon()
	{
		return beamIcon;
	}
	
	public String toString() 
	{
		return desc;
	}
	
	public Image getSprites()
	{
		BufferedImage spriteSheet;
		try {
			spriteSheet = ImageIO.read(new File("src/images/upgrades/"+this.getBeamIcon()));
			return spriteSheet.getSubimage(0, 0, 16, 16);
		} catch (IOException e) {
			System.out.println("this beam has no upgrade image!");
		}		
		return null;
	}
}
