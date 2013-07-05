package effects;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import control.BaseObject;


public class Projectile extends BaseObject implements ActionListener
{
	private int velocity;
	private Rectangle2D hitbox;
	private int damage;
	private Beam type;
	private boolean penetrate;
	
	private int amount;
	private String beam;
	
	public Projectile(Beam type)
	{
		setAlive(false); 		
		this.type = type;	
		setSpriteX(0);		
		setHitbox(new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight()));		
		switch (type) {
			case POWER:
				beam = "beam1.gif";
				setDamage(2);
				this.velocity = 20;
				setWidth(15);
				setHeight(8);
				setPenetrate(false);
				this.amount = 5;
				break;
			case PLASMA:
				beam = "beam2.gif";
				setDamage(15);
				this.velocity = 15;
				setWidth(27);
				setHeight(18);
				setPenetrate(false);
				this.amount = 4;
				break;
			case SINUS:				
				beam = "beam3.png";
				setDamage(1);
				this.velocity = 9;
				setWidth(111);
				setHeight(37);
				setPenetrate(true);
				this.amount = 1;
				break;
			case PLASMACHARGE:	
				beam = "beam4.gif";
				setDamage(10);
				this.velocity = 12;
				setWidth(60);
				setHeight(30);
				setPenetrate(true);
				this.amount = 3;
				break;
			case ACIDSPIT:	
				beam = "kihunter_acid.gif";
				setDamage(3);
				this.velocity = 8;
				setWidth(16);
				setHeight(12);
				setPenetrate(false);
				this.amount = 1;
				break;
		}	
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/effects/"+beam)));
		} catch (IOException e) {
			System.out.println("Image could not be found!");
		}
		
		Timer timer = new Timer(75, this);
		timer.start();
	}	

	@Override
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		/*if (type == Projectile.POWERBEAM) {
			return spriteSheet.getSubimage(getSpriteX(), 0, getWidth(), getHeight());
		}
		else if (type == Projectile.PLASMABEAM) {
			return spriteSheet.getSubimage(getSpriteX(), 0, getWidth(), getHeight());
		}
		else if (type == Projectile.SINUSBEAM) {
			return spriteSheet.getSubimage(getSpriteX(), 0, getWidth(), getHeight());
		}*/
		return spriteSheet.getSubimage(getSpriteX(), 0, getWidth(), getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		if (type == Beam.SINUS) {
			setSpriteX((getSpriteX()+9) % 101);
			moveHitbox(getX(), getY());			
		}	
		else if (type == Beam.ACIDSPIT) {
			setSpriteX(0);
			moveHitbox(getX(), getY());
		}
		else {
			setSpriteX((getSpriteX()+getWidth()) % (getWidth()*amount));
			moveHitbox(getX(), getY());	
		}
	}
	public Beam getType()
	{
		return type;
	}
	public int getDamage()
	{
		return damage;
	}
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	public int getVelocity()
	{
		return velocity;
	}
	public void setVelocity(int velocity)
	{
		this.velocity = velocity;
	}
	public Rectangle2D getHitbox()
	{
		return hitbox;
	}
	public void setHitbox(Rectangle2D hitbox)
	{
		this.hitbox = hitbox;
	}
	public boolean isPenetrate()
	{
		return penetrate;
	}
	public void setPenetrate(boolean penetrate)
	{
		this.penetrate = penetrate;
	}
}
