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


public class KiHunter extends Enemy
{
	private KiHunterWings wings;
	private boolean attacking;
	private boolean attackFinished;
	
	public KiHunter(double x, double y){
		setAlive(true);
		setX(x);
		setY(y);
		setVelocity(1);
		setDamage(2);
		setHealth(10);
		setSpriteX(0);
		setSpriteY(0);	
		setWidth(60);
		setHeight(45);
		setType(GameUnit.KIHUNTER);
		scoreValue = 175;
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));
		attackFinished = false;
		wings = new KiHunterWings(x,y);
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/enemies/kihunter_body.gif")));
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
		if (isAlive() && !attacking) {
			setSpriteX(0);			
			moveHitbox(getX(), getY());
			attackFinished = false;
		}
		else if (isAlive() && attacking) {
			if (getSpriteX() >= getWidth()*8) {				
				attacking = false;
				attackFinished = true;
				setSpriteX(0);			
				moveHitbox(getX(), getY());
			}
			else {	
				setSpriteX(getSpriteX()+getWidth());
				moveHitbox(getX(), getY());
			}
			
		}
		else {
			timer.stop();
		}		
	}
	
	@Override
	public void move()
	{
		setX(getX()-getVelocity());		
	}
	
	// determine the KiHunter wants to spit acid
	public void attack()
	{
		if (Math.random() < 0.0004 && !attacking && (getX()>0 +(2*getWidth()))) { //
			attacking = true;
		}			
	}
	
	public boolean isAttacking()
	{
		return attacking;
	}
	
	@Override
	public void death()
	{
		playSound();		
	}
	
	public KiHunterWings getWings()
	{
		return wings;
	}
	public void setWings()
	{
		wings = new KiHunterWings(getX(),getY());
	}
	public boolean isAttackFinished()
	{
		return attackFinished;
	}
	public void setAttackFinished(boolean set)
	{
		this.attackFinished = set;
	}

	@Override
	public void playSound()
	{
		try {
			Random randGen = new Random();
			int sound = randGen.nextInt(3);
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/KiHunterDeath"+sound+".wav"));
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
