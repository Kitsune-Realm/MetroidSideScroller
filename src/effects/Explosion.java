package effects;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import player.Ship;

import control.BaseObject;
import control.GameUnit;


public class Explosion extends BaseObject implements ActionListener
{
	Timer timer;
	private String exploString;
	private int amount; // amount of sprite images
	private String SFX;
	private GameUnit type;
	private int time;
	
	public Explosion(double x, double y, GameUnit type, int ShipSkin)
	{
		
	}
	
	public Explosion(double x, double y, GameUnit type)
	{
		this.setType(type);
		setAlive(true);
		setSpriteX(0);
		setSpriteY(0);	
		switch (type) {
			// Small 1
			default:
				exploString = "death1.gif";
				setX(x - 16);
				setY(y - 16);		
				setWidth(32);
				setHeight(32);
				amount = 4;
				time = 50;
				break;
			case KIHUNTER:
				exploString = "KiHunterDeath.gif";
				setX(x - 30);
				setY(y - 32);		
				setWidth(72);
				setHeight(61);
				amount = 6;
				time = 50;
				break;
			case SHIP:
				exploString = "ShipDeath"+Ship.getSkin()+".gif";
				setX(x - 47);
				setY(y - 42);		
				setWidth(94);
				setHeight(80);
				SFX = "shipDeath.wav";
				amount = 5;
				time = 80;
				break;
			case POWERBIRTH:
				exploString = "beambirth1.gif";
				setX(x+45);
				setY(y-10);		
				setWidth(20);
				setHeight(26);
				amount = 5;
				time = 15;
				break;
			case PLASMABIRTH:
				exploString = "beambirth2.gif";
				setX(x+42);
				setY(y-5);		
				setWidth(24);
				setHeight(24);
				amount = 4;
				time = 25;
				break;
			case PLASMACHARGEBIRTH:
				exploString = "beambirth4.gif";
				setX(x+42);
				setY(y-5);		
				setWidth(23);
				setHeight(28);
				SFX = "PlasmaChargedShot.wav";
				amount = 3;
				time = 45;
				break;
		}
		
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/effects/"+exploString)));
		} catch(IOException e) {
			System.out.println(e);
		}
		
		timer = new Timer(time, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		setSpriteX((getSpriteX()+getWidth()) % (getWidth()*amount));
		if (SFX != null) {
			playSound(SFX);
			SFX = null;
		}
		if(getSpriteX() == 0) {						
			timer.stop();
			setAlive(false);
		}			
	}
	
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), getSpriteY(), getWidth(), getHeight());
	}
	
	public void playSound(String sound)
	{
		try {
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/"+sound));
			Clip clip = AudioSystem.getClip();
			clip.open(SFX);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public GameUnit getType()
	{
		return type;
	}

	public void setType(GameUnit type)
	{
		this.type = type;
	}
}
