package enemy;

import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import control.GameUnit;

public class Bugger extends Enemy
{	
	private boolean hasTarget;
	private String sprite;
	
	public Bugger(double x, double y, GameUnit type)
	{
		setAlive(true);
		setX(x);
		setY(y);
		setVelocity(3);
		setDamage(1);
		setHealth(2);
		setSpriteX(0);
		setSpriteY(0);
		setType(type);
		switch (type) {
			case ZEB:
				setWidth(16);
				setHeight(14);
				setType(GameUnit.ZEB);
				sprite = "zeb.gif";
				break;
			case GAMET:
				setWidth(16);
				setHeight(14);
				setType(GameUnit.GAMET);
				sprite = "gamet.gif";
				break;
		}
		scoreValue = 25;
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/enemies/" + sprite)));
		} catch(IOException e) {
			System.out.println(e);
		}
		
		timer = new Timer(75, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		//System.out.println(getClass().getName()+" X: "+ getSpriteX() + " Y: "+ getSpriteY());
		if (isAlive()) {
			if(hasTarget){
				setSpriteX((getSpriteX()+getWidth()) % (getWidth()*4) + 64);
			}				
			else
				setSpriteX((getSpriteX()+getWidth()) % (getWidth()*4));
			moveHitbox(getX(), getY());
		}
		else {
			timer.stop();
		}		
	}

	@Override
	public void move()
	{
		if(hasTarget) 
			setVelocity(5);
		else
			setVelocity(3);
			
		setX(getX()-getVelocity());
	}
	public void setTarget(double targetY, int targetHeight) 
	{
		int targetY1 = (int)targetY;
		int targetY2 = targetHeight + (int)targetY;
		int Y = (int)getY();
		
		
		if (Y < targetY2 && Y > targetY1)
			hasTarget = true;
		else
			hasTarget = false;
	}
	@Override
	public void death()
	{
		playSound();
	}
	
	public void playSound()
	{
		try {
			Random randGen = new Random();
			int sound = randGen.nextInt(2);
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/MetalLightSliceFlesh"+sound+".wav"));
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
}
