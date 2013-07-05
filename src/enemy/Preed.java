package enemy;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
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


public class Preed extends Enemy
{
	private Point2D target;
	private double dirX;
	private double dirY;
	
	public Preed(double x, double y)
	{
		setAlive(true);
		setX(x);
		setY(y);
		setVelocity(2);
		setDamage(2);
		setHealth(7);
		setSpriteX(0);
		setSpriteY(0);	
		setWidth(24);
		setHeight(28);
		setType(GameUnit.PREED);
		scoreValue = 100;
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/enemies/preed.gif")));
		} catch(IOException e) {
			System.out.println(e);
		}
		
		timer = new Timer(75, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (isAlive()) {
			setSpriteX((getSpriteX()+getWidth()) % (getWidth()*6));
			moveHitbox(getX(), getY());
		}
		else {
			timer.stop();
		}	
	}

	@Override
	public void move()
	{
		if (target != null) {
			dirX = target.getX() - getX();
			dirY = target.getY() - getY();
			double hyp = Math.sqrt(dirX*dirX + dirY*dirY);
			dirX /= hyp;
			dirY /= hyp;
			//System.out.println("dirX:" +dirX+ " dirY:"+dirY);
			setX(getX() + (dirX*getVelocity()));
			setY(getY() + (dirY*getVelocity()));
		}
		else {
			//TODO doesnt happen yet, because the ship is moved outside the screen after death
			setX(getX()-getVelocity());
		}
	}
	
	public void findTarget(double targetX, double targetY)
	{
		this.target = new Point2D.Double(targetX, targetY);
	}

	@Override
	public void death()
	{
		playSound();
	}

	@Override
	public void playSound()
	{
		try {
			Random randGen = new Random();
			int sound = randGen.nextInt(2);
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/bulldeath"+sound+".wav"));
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
