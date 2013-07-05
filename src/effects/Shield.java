package effects;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import control.BaseObject;


public class Shield extends BaseObject
{
	Timer timer;
	public Shield(double x, double y)
	{
		setAlive(true);
		setX(x);
		setY(y);
		setSpriteX(0);
		setSpriteY(0);	
		setWidth(60);
		setHeight(61);
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/effects/teslashield.gif")));
		} catch(IOException e) {
			System.out.println(e);
		}	

	}
	public void draw()
	{
		if (isAlive()) {
			setSpriteX((getSpriteX()+getWidth()) % (getWidth()*6));
		}
	}

	@Override
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), getSpriteY(), getWidth(), getHeight());
	}

}
