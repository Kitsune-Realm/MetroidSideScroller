package control;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public abstract class BaseObject
{
	private boolean alive;
	private double x,y;	 
	private Image image;
	private int velocity;
	private Rectangle2D hitbox; // berekenen
	private int width;
	private int height;
	private BufferedImage spriteSheet = null;
	private int SpriteX, SpriteY;
	
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}	 
	public boolean isAlive()
	{
		return alive;
	}
	public Image getImage()
	{
		return image;
	}
	public void setImage(Image image)
	{
		this.image = image;
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
	public void moveHitbox(double x, double y)
	{
		hitbox = new Rectangle2D.Double(x, y, getWidth(), getHeight());
	}
	
	public BufferedImage getSpriteSheet()
	{
		return spriteSheet;
	}
	public void setSpriteSheet(BufferedImage spriteSheet)
	{
		this.spriteSheet = spriteSheet;
	}
	public abstract Image getSprites();
	
	public int getSpriteX()
	{
		return SpriteX;
	}
	public void setSpriteX(int spriteX)
	{
		SpriteX = spriteX;
	}
	public int getSpriteY()
	{
		return SpriteY;
	}
	public void setSpriteY(int spriteY)
	{
		SpriteY = spriteY;
	}
	public void setAlive(boolean alive)
	{
		this.alive = alive;
	}
	public double getX()
	{
		return x;
	}
	public void setX(double x)
	{
		this.x = x;
	}
	public double getY()
	{
		return y;
	}
	public void setY(double y)
	{
		this.y = y;
	}
}
