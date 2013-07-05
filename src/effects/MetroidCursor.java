package effects;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public class MetroidCursor implements ActionListener
{
	private int SpriteX, SpriteY;
	private int x, y;
	private int starter;	

	public MetroidCursor(int x, int y)
	{
		setSpriteX(0);
		setSpriteY(0);
		setX(x);
		setY(y);
		setStarter(y);
		Timer timer = new Timer(140, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{		
		setSpriteX((getSpriteX()+12) % (12*4));
	}
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(getSprites(), getX(), getY(), null);
	}
	public Image getSprites()
	{
		BufferedImage spriteSheet;
		try {
			spriteSheet = ImageIO.read(new File("src/images/interface/metroid_cursor.gif"));
			return spriteSheet.getSubimage(getSpriteX(), getSpriteY(), 12, 8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
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
	public int getStarter()
	{
		return starter;
	}
	public void setStarter(int starter)
	{
		this.starter = starter;
	}
}
