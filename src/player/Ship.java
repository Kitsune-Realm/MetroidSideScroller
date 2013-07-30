package player;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import control.BaseObject;
import effects.Beam;
import effects.Shield;

public class Ship extends BaseObject implements ActionListener
{
	private boolean shieldActive;
	private Shield shield;
	private Beam loadedBeam;
	private int health;
	private int[] ammunition;
	private int charge;
	private int chargeLimit;
	private double laserCharge;
	private static int skin;	
	public static final int SKIN_HUNTER = 1;
	public static final int SKIN_FUSION = 2;
	public static final int MISC_SHIELD = 1;
	public static final int MISC_CLOAK = 2;
	public static final int MISC_THRUST = 3;
	public static final int MISC_AMP = 4;
	public static final int MISC_BATTERY1 = 5;
	public static final int MISC_BATTERY2 = 6;
	public static final int MISC_LENS = 7;

	private List<ShipItem> miscItems;
	private List<ShipItem> hullItems;
	private List<ShipItem> beamItems;
	
	public Ship()
	{		
		//Default Settings
		fillItemArrays();
		setLoadedBeam(Beam.POWER);
		setVelocity(6);
		setSkin(Ship.SKIN_HUNTER);
		
		fillAmmunition();
		setAlive(true);		
		setSpriteX(0);
		setSpriteY(0);	
		setHealth(1);
		setWidth(94);
		setHeight(42);
		setChargeLimit(70);
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		
		
		
		/*
		try {
			image = ImageIO.read(new File("src/ship.gif"));
		} catch (IOException e) {
			System.out.println("Image could not be found!");
		} */				
		Timer timer = new Timer(300, this);
		timer.start();
	}
	
	public void fillItemArrays()
	{
		miscItems = new ArrayList<ShipItem>();
		hullItems = new ArrayList<ShipItem>();
		beamItems = new ArrayList<ShipItem>();
		
		miscItems.add(new ShipItem("Shield Generator", false, false, "Protects the ship from 1 impact", Ship.MISC_SHIELD));
		miscItems.add(new ShipItem("Cloaking Device", false, true, "Enemies will not be able to see you", Ship.MISC_CLOAK));
		miscItems.add(new ShipItem("Gluino Thruster", false, true, "Increases speed", Ship.MISC_THRUST));
		miscItems.add(new ShipItem("Beam Amplifier", false, false, "Increases beam damage", Ship.MISC_AMP));
		miscItems.add(new ShipItem("Supply Battery", false, false, "Increases ship health by 1", Ship.MISC_BATTERY1));
		miscItems.add(new ShipItem("Supply Battery", false, true, "Increases ship health by 1", Ship.MISC_BATTERY2));
		miscItems.add(new ShipItem("Photonic Lens", false, true, "Increases beam speed", Ship.MISC_LENS));
		
		hullItems.add(new ShipItem("Hunter-Class", false, true, "Hunter-Class Gunship", Ship.SKIN_HUNTER));
		hullItems.add(new ShipItem("Fusion-Class", false, true, "Fusion-Class Gunship", Ship.SKIN_FUSION));
		
		beamItems.add(new ShipItem("Power", false, true, "Basic particle beam", Beam.POWER));
		beamItems.add(new ShipItem("Plasma", false, true, "Superheated plasma bolts", Beam.PLASMA));
		beamItems.add(new ShipItem("Sinus", false, true, "Enhanced trigonometric energy", Beam.SINUS));
		beamItems.add(new ShipItem("Tesla", false, true, "Ionized concentrated plasma", Beam.TESLA));
	}
	
	public void resetHullArray()
	{
		for (ShipItem item : hullItems) {
			item.setValue(false);
		}
	}
	public void resetBeamArray()
	{
		for (ShipItem item : beamItems) {
			item.setValue(false);
		}
	}	
	public void initItems()
	{
		for (ShipItem item : miscItems) {
			if (item.getValue()) {
				switch (item.getType()) {
					case Ship.MISC_SHIELD:
						setShieldActive(true);
						break;
					case Ship.MISC_THRUST:
						setVelocity(9);
						break;
					case Ship.MISC_BATTERY1:
					case Ship.MISC_BATTERY2:
						setHealth(getHealth()+1);
						break;						
				}
			}
		}
		for (ShipItem item : beamItems) {
			if (item.getValue())
			{
				setLoadedBeam(item.getBeam());
			}
		}
	}
	public void setItemValue(List<ShipItem> list, int index, boolean value)
	{
		list.get(index).setValue(value);
	}
	public int getChargeLimit()
	{
		return chargeLimit;
	}
	public void setChargeLimit(int chargeLimit)
	{
		this.chargeLimit = chargeLimit;
	}
	public Beam getLoadedBeam()
	{
		return loadedBeam;
	}

	public void setLoadedBeam(Beam loadedBeam)
	{
		this.loadedBeam = loadedBeam;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		//System.out.println(getClass().getName()+" X: "+ getSpriteX() + " Y: "+ getSpriteY());
		moveHitbox(getX(), getY());
		setSpriteX((getSpriteX()+94)%376);
		if (getSpriteX() == 0)
			setSpriteY((getSpriteY()+42)%210);
	}

	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), getSpriteY(), 94, 42);
	}	
	
	/**
	 * Custom written method to check for a collision between an enemy and the ship
	 * Must determine which version is better. This one or the hitbox collide check. 
	 */
	public boolean collide(double enemyX, double enemyY, int enemyWidth, int enemyHeight)
	{
		double x1 = getX();
		double y1 = getY();
		double x2 = getX() + getWidth();
		double y2 = getY() + getHeight();
		
		if((enemyX > x1-enemyWidth) && (enemyX < x2)) {
			if((enemyY > y1-enemyHeight) && (enemyY < y2)) {
				return true;
			}			
		}		
		return false;
	}
	public void fillAmmunition()
	{
		ammunition = new int[]{0,0,0,0};
	}
	public String getAmmunitionType(Beam index)
	{
		switch (index)
		{
			case POWER:
				return "Powerbeam";
			case SINUS:
				return "Sinusbeam";
			case PLASMA:
				return "Plasmabeam";
			case PLASMACHARGE:
				return "Charged Plasmabeam";
			default:
				return "";
		}
	}
	

	public int getAmmunition(Beam index)
	{
		switch (index)
		{
			case POWER:
				return ammunition[0];
			case SINUS:
				return ammunition[1];
			case PLASMA:
				return ammunition[2];
			case PLASMACHARGE:
				return ammunition[3];
			default:
				return 0;
		}		
	}
	public void setAmmunition(Beam index, int amount)
	{
		switch (index)
		{
			case POWER:
				ammunition[0] = amount;
				break;
			case SINUS:
				ammunition[1] = amount;
				break;
			case PLASMA:
				ammunition[2] = amount;
				break;
			case PLASMACHARGE:
				ammunition[3] = amount;
				break;
		default:
			break;
		}		
	}	
	public boolean hasShield()
	{
		return shieldActive;
	}
	public void setShieldActive(boolean shieldActive)
	{
		this.shieldActive = shieldActive;
	}	
	public void setShield(Shield shield)
	{
		this.shield = shield;
	}
	public Shield getShield()
	{
		return shield;
	}
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public int getCharge()
	{
		return charge;
	}
	public void setCharge(int charge)
	{
		this.charge = charge;
	}	
	public double getLaserCharge()
	{
		return laserCharge;
	}
	public void setLaserCharge(double laserCharge)
	{
		this.laserCharge = laserCharge;
	}
	public static int getSkin()
	{
		return skin;
	}
	public void setSkin(int skin)
	{
		this.skin = skin;
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/player/shipsheet"+skin+".gif")));
		} catch(IOException e) {
			System.out.println(e);
		}
		for (ShipItem item : hullItems) {				
			item.setValue(item.getType() == skin);				
		}
	}
	public List<ShipItem> getMiscItems()
	{
		return miscItems;
	}
	public void setMiscItems(List<ShipItem> miscItems)
	{
		this.miscItems = miscItems;
	}
	public List<ShipItem> getHullItems()
	{
		return hullItems;
	}
	public void setHullItems(List<ShipItem> hullItems)
	{
		this.hullItems = hullItems;
	}
	public List<ShipItem> getBeamItems()
	{
		return beamItems;
	}
	public void setBeamItems(List<ShipItem> beamItems)
	{
		this.beamItems = beamItems;
	}
	
}
