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
import control.World;



public class Reflec extends Enemy
{	
	private boolean up;
	public Reflec(double x, double y)
	{
		setAlive(true);
		setX(x);
		setY(y);
		setVelocity(2);
		setDamage(3);
		setHealth(2);
		setSpriteX(0);
		setSpriteY(0);	
		setWidth(22);
		setHeight(32);
		setType(GameUnit.REFLEC);
		scoreValue = 300;
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/enemies/reflec.gif")));
		} catch(IOException e) {
			System.out.println(e);
		}
		
		timer = new Timer(120, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		//System.out.println(getClass().getName()+" X: "+ getSpriteX() + " Y: "+ getSpriteY());
		if (isAlive()) {
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
		if(getY() < 0)
			up = false;
		if(getY() > World.SCREEN_HEIGHT-45)
			up = true;
		
		if(getX() > World.SCREEN_WIDTH - 100)
			setX(getX()-getVelocity());
		else {
			if(up)
				setY(getY()-getVelocity()*0.5);
			else
				setY(getY()+getVelocity()*0.5);
		}
		
		
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
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/DemonHunterMissileHit"+sound+".wav"));
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
