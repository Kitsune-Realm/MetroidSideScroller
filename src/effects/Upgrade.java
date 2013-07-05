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


public class Upgrade extends BaseObject implements ActionListener
{
	private String URLString;
	private Beam type;
	
	public Upgrade(double x, double y, Beam type)
	{
		setX(x);
		setY(y);
		setType(type);
		setVelocity(6);
		setSpriteX(0);
		setSpriteY(0);	
		setWidth(16);
		setHeight(16);
		
		switch (type) {
			case POWER:
				URLString = "charge_beam.gif";
				break;
			case SINUS:
				URLString = "sinus_beam.gif";
				break;
			case PLASMA:
				URLString = "plasma_beam.gif";
				break;
		default:
			break;
		}
		
		try {
			setSpriteSheet(ImageIO.read(new File("src/images/upgrades/"+URLString)));
		} catch(IOException e) {
			System.out.println(e);
		}
		
		Timer timer = new Timer(1000, this);
		timer.start();
	}
	@Override
	public Image getSprites()
	{
		BufferedImage spriteSheet = getSpriteSheet();
		return spriteSheet.getSubimage(getSpriteX(), 0, getWidth(), getHeight());
	}
	public Beam getType()
	{
		return type;
	}
	public void setType(Beam type)
	{
		this.type = type;
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		setSpriteX((getSpriteX()+16)%32);		
	}
	
}
