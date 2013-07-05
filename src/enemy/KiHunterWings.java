package enemy;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import control.BaseObject;


public class KiHunterWings extends BaseObject implements ActionListener
{
	private Timer timer;
	
	public KiHunterWings(double x, double y)
	{
		setX(x);
		setY(y);
		setSpriteX(0);
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/enemies/kihunter_wings.gif")));
		} catch(IOException e) {
			System.out.println(e);
		}
		timer = new Timer(75, this);
		timer.start();
	}
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		setSpriteX((getSpriteX()+23) % (4*23));		
	}
	@Override
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), 0, 23, 29);
	}

}
