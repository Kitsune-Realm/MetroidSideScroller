package enemy;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import control.BaseObject;
import control.GameUnit;


public abstract class Enemy extends BaseObject implements ActionListener
{
	protected int scoreValue;
	protected Timer timer;
	protected GameUnit type;
	protected int health;
	protected int damage;
	
	public static final int ZEB = 0;
	public static final int KIHUNTER = 1;
	public static final int REFLEC = 2;
	
	public abstract void move();
	public abstract void death();
	public abstract void playSound();	
	
	
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
	}
	public void setType(GameUnit type)
	{
		this.type = type;
	}
	public GameUnit getType()
	{
		return type;
	}
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), getSpriteY(), getWidth(), getHeight());
	}
	public int getScoreValue()
	{
		return scoreValue;
	}
	public int getDamage()
	{
		return damage;
	}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
}
