package view;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import player.Ship;
import player.ShipItem;

import control.World;
import effects.MetroidCursor;


public class GUI extends JPanel implements ActionListener, KeyListener
{	
	private static final long serialVersionUID = 1L;
	private String[][] menuItems = {{"Start", "Player", "Creature Database", "Credits"}, {"back", "Horsehead Nebula", "NGC 2264", "Crab Nebula", "Tallon IV", "SR388", "Ceres"},{"back", "Achievements", "Controls"}};
	private String[] planetDescriptions1 = {"", "Barnard 33 is a dark nebula in the", "Also known as the Cone Nebula,", "The Crab Nebula is a supernova remnant and", "Fallen colony of the Chozo civilization", "Homeworld of the Metroids", "Remains of Space Colony Ceres"};
	private String[] planetDescriptions2 = {"", "Orion constellation", "it is infested with Gamets", "pulsar wind nebula in the constellation of Taurus", "", "", "particle mists obscure sensors"};
	
	private String[] creatureDescriptions1 = {"",""};
	private MetroidCursor cursor;
	private int menu;	
	private int submenu;
	private static int selection;
	private int starter;	
	
	private int amountMisc;
	private int amountHull;
	private int amountBeam;
	
	public GUI()
	{
		setFocusable(true);		
		requestFocus();
		addKeyListener(this);	
		
		setSelection(0);
		starter = 272;
		cursor = new MetroidCursor((World.SCREEN_WIDTH/2)-20,starter);
		setBackground(Color.BLACK);			
		menu = 0;
				
		Timer timer = new Timer(1000/50, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// TODO
		//System.out.println(World.getShip().getMiscItems().size()+"," + amountMisc + "," + getSelection());
		cursor.draw(g2);
		
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString("version: "+World.VERSION, 920, World.SCREEN_HEIGHT-35);
		
		// Startmenu
		if(menu == 0) {
			g2.setColor(Color.LIGHT_GRAY);
			for (int i=0; i<menuItems[0].length; i++) {
				g2.drawString(menuItems[0][i], World.SCREEN_WIDTH/2, 280 + (i*20));
			}						
		}
		// Stages
		else if(menu == 1) {
			g2.setColor(Color.LIGHT_GRAY);
			for (int i=0; i<menuItems[1].length; i++) {
				g2.drawString(menuItems[1][i], 100, 100 + (i*20));
			}
			g2.drawString(planetDescriptions1[selection], 700, 100);
			g2.drawString(planetDescriptions2[selection], 700, 120);
			if(selection != 0)
				g2.drawString("Highscore: " + World.getHighScores()[selection-1], 700, 180);
			try {
				g2.drawImage(ImageIO.read(new File("src/images/interface/stage"+selection+".gif")), 400, 60, null);
			} catch (IOException e) {
			}
		}
		// Player overview
		else if(menu == 2) {
			drawDescription(g2);
			g2.setColor(Color.LIGHT_GRAY);
			for (int i=0; i<menuItems[2].length; i++) {
				g2.drawString(menuItems[2][i], World.SCREEN_WIDTH/2, 280 + (i*20));
			}
			g2.drawString("Misc items", 190, 60);
			g2.drawString("Ship Hull", 740, 60);
			g2.drawString("Beams", 740, 180);
			
			int i = 0;
			for (ShipItem item : World.getShip().getMiscItems()) {
				if(item.isEnabled()) {
					if (item.getValue()) {
						g2.setColor(Color.LIGHT_GRAY);						
					}
					else {
						g2.setColor(Color.DARK_GRAY);
					}					
				}
				else {
					g2.setColor(Color.BLACK);
				}
				g2.drawString(item.getName(), 200, 80 + (i*20));
				i++;
			}
			amountMisc = i;
			
			i = 0;
			for (ShipItem item : World.getShip().getHullItems()) {
				if(item.isEnabled()) {
					if (item.getValue()) {
						g2.setColor(Color.LIGHT_GRAY);
						World.getShip().setSkin(item.getType());
					}
					else
						g2.setColor(Color.DARK_GRAY);
					g2.drawString(item.getName(), 750, 80 + (i*20));
					i++;
				}
			}
			amountHull = i;
	
			i = 0;
			for (ShipItem item : World.getShip().getBeamItems()) {
				if(item.isEnabled()) {
					if (item.getValue())
						g2.setColor(Color.LIGHT_GRAY);				
					else
						g2.setColor(Color.DARK_GRAY);
					g2.drawString(item.getName(), 750, 200 + (i*20));
					i++;
				}
			}
			amountBeam = i;

			try {
				g2.drawImage(ImageIO.read(new File("src/images/interface/shipstats"+ World.getShip().getSkin() + ".gif")), 480, 135, null);
				g2.setColor(Color.GREEN);
				// Misc
				g2.drawLine(300, 55, 380, 55);
				g2.drawLine(380, 55, 485, 144);
				// Ship Hull
				g2.drawLine(730, 55, 650, 55);
				g2.drawLine(650, 55, 540, 130);
				// Beams
				g2.drawLine(730, 175, 700, 175);
				g2.drawLine(700, 175, 580, 160);
			} catch (IOException e) {
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent k) 
	{
		int keyCode = k.getKeyCode();
        switch(keyCode){
	        case KeyEvent.VK_UP:
	        case KeyEvent.VK_W:
	        	moveCursorUp(getMenu());	        	
	        	//setSelection((cursor.getY()%cursor.getStarter())/20);
	        	System.out.println("menu: "+ menu+ " selection: " + selection + " submenu: " + submenu);
	        	break;
	        case KeyEvent.VK_DOWN:
	        case KeyEvent.VK_S:
	        	moveCursorDown(getMenu());	
	        	//setSelection((cursor.getY()%cursor.getStarter())/20);
	        	System.out.println("menu: "+ menu+ " selection: " + selection + " submenu: " + submenu);
	        	break;
	        case KeyEvent.VK_A:
        	case KeyEvent.VK_LEFT:
        		moveCursorLeft(getSubmenu());
        		System.out.println("menu: "+ menu+ " selection: " + selection + " submenu: " + submenu);
        		break;
        	case KeyEvent.VK_D:
        	case KeyEvent.VK_RIGHT:
        		moveCursorRight(getSubmenu());
        		System.out.println("menu: "+ menu+ " selection: " + selection + " submenu: " + submenu);
        		break;
	        case KeyEvent.VK_SPACE:
	        case KeyEvent.VK_CONTROL:
	        	playSound("click_activate.wav");
	        	if(menu == 0) {
	        		switch(selection) {
	        			case 0:
	        				setMenu(1);
	        				setSelection(0);
	        				break;
	        			case 1:
	        				setMenu(2);
	        				setSelection(0);
	        				break;
	        			case 2:
	        				System.out.println("this still has to be implemented");
		        			break;
	        		}	        	
	        	}
	        	// Stage selection
	        	else if(menu == 1) {
	        		activate(selection);
	        	}
	        	// back
	        	else if(menu != 0 && selection == 0 && submenu == 0) {
	        		setSubmenu(0);
	        		activate(0);
	        	}
	        	// settings for player items
	        	else if(menu == 2) {
	        		switch (submenu) {
	        			case 1:
	        				//ship.setItem(ship.getItemValues(), (getSelection()-1), !ship.getItemValues()[(getSelection()-1)]);
	        				if (World.getShip().getMiscItems().get((getSelection()-1)).isEnabled()) {
	        					World.getShip().setItemValue(World.getShip().getMiscItems(), (getSelection()-1), !World.getShip().getMiscItems().get((getSelection()-1)).getValue());
	        				}
	        				break;
	        			case 2:
	        				if (getSelection() <= World.getShip().getHullItems().size()) {	     
	        					if (World.getShip().getHullItems().get((getSelection()-1)).isEnabled()) {
		        					World.getShip().resetHullArray();
		        					//ship.setItem(ship.getHullValues(), (getSelection()-1), !ship.getHullValues()[(getSelection()-1)]);
		        					World.getShip().setItemValue(World.getShip().getHullItems(), (getSelection()-1), !World.getShip().getHullItems().get((getSelection()-1)).getValue());
	        					}
	        				}
	        				else {
	        					//ship.setItem(ship.getBeamsValues(), 
	        					//			(getSelection()-ship.getBeamsNames().length), 
	        					//			!ship.getBeamsValues()[(getSelection()-ship.getBeamsNames().length)]);
	        					if (World.getShip().getBeamItems().get(getSelection()-(World.getShip().getHullItems().size()+1)).isEnabled()) {
		        					World.getShip().resetBeamArray();
		        					World.getShip().setItemValue(
		        							World.getShip().getBeamItems(), 
		        							(getSelection()-(World.getShip().getHullItems().size()+1)),
		        							!World.getShip().getBeamItems().get(getSelection()-(World.getShip().getHullItems().size()+1)).getValue()
		        							);
	        					}
	        				}
	        		}
	        	}	        	
	        	break;
        }
	}
	
	public void drawDescription(Graphics2D g2)
	{
		g2.setColor(Color.GREEN);
		String main = "";
		String desc = "";
		switch (getSubmenu()) {
			case 1: // Misc Items
				if(World.getShip().getMiscItems().get(getSelection()-1).isEnabled()) {
					main = "Miscellaneous items";
					desc = World.getShip().getMiscItems().get(getSelection()-1).getDescription();
				}
				break;
			case 2: // Hull and Beams
				if (getSelection() <= World.getShip().getHullItems().size()) {
					if(World.getShip().getHullItems().get(getSelection()-1).isEnabled()) {
						main = "Visualisation of your ship";
						desc = World.getShip().getHullItems().get(getSelection()-1).getDescription();
					}
				}
				else {
					if(World.getShip().getBeamItems().get(getSelection()-(World.getShip().getHullItems().size()+1)).isEnabled()) {
						main = "Default starting beam";
						desc = World.getShip().getBeamItems().get(getSelection()-(World.getShip().getHullItems().size()+1)).getDescription();
					}
				}
				break;
		
		}
		g2.drawString(main, 450, 361);
		g2.drawString(desc, 450, 380);
	}
	
	@Override
	public void keyReleased(KeyEvent arg0){}

	@Override
	public void keyTyped(KeyEvent arg0){}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		repaint();
	}
	
	public void playSound(String soundEffect)
	{
		try {
			AudioInputStream SFX = AudioSystem.getAudioInputStream(new File("src/sounds/"+soundEffect));
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
	
	public void moveCursorUp(int menuSection)
	{
		playSound("click.wav");
		switch (menuSection) {
		// Start menu
		case 0:
			if(cursor.getY() == 272) {
				cursor.setY(cursor.getY()+((menuItems[0].length-1)*20));
			   	setSelection((menuItems[0].length-1));
			}
			else {
				cursor.setY(cursor.getY()-20);
			   	selection--;
			}
			break;
		// Stage select
		case 1:		
			if(cursor.getY() == 92) {
				cursor.setY(cursor.getY()+((menuItems[1].length-1)*20));
			   	setSelection((menuItems[1].length-1));
			}
			else {
				cursor.setY(cursor.getY()-20);
			   	selection--;
			}
			break;
		// Player menu
		case 2:
			switch (submenu) {
				// Lower menu
				case 0:
					if(cursor.getY() == 272) {
						cursor.setY(52 + (amountMisc*20));
						cursor.setX(180);
					   	setSelection(amountMisc);
					   	setSubmenu(1);
					}
					else {
						cursor.setY(cursor.getY()-20);
					   	selection--;
					}
					break;
				// Misc items
				case 1:
					if(cursor.getY() == 72) {
						cursor.setY(cursor.getY()+((amountMisc-1)*20));
					   	setSelection(amountMisc);
					}
					else {
						cursor.setY(cursor.getY()-20);
					   	selection--;
					}
					break;
				// Ship hull & Beams
				case 2:
					if(cursor.getY() == 72) {
						cursor.setY(172 + (amountBeam*20));
					   	setSelection((amountHull + amountBeam));
					}
					else if(cursor.getY() == 192) {
						cursor.setY(52 + (amountHull*20));
					   	selection--;
					}
					else {
						cursor.setY(cursor.getY()-20);
					   	selection--;
					}
					break;	
			}			
			break;
		}
	}
	
	public void moveCursorDown(int menuSection)
	{
		playSound("click.wav");
		switch (menuSection) {
		// Start menu
		case 0:
			if(cursor.getY() == 272 + (menuItems[0].length-1)*20) {
				cursor.setY(cursor.getY()-((menuItems[0].length-1)*20));
			   	selection = 0;
			}
			else {
				cursor.setY(cursor.getY()+20);
			   	selection++;
			}
			break;
		// Stage select
		case 1:
			if(cursor.getY() == 92 + (menuItems[1].length-1)*20) {
				cursor.setY(cursor.getY()-((menuItems[1].length-1)*20));
			   	setSelection(0);
			}
			else {
				cursor.setY(cursor.getY()+20);
			   	selection++;
			}
			break;
		// Player menu
		case 2:
			switch (submenu) {
				// Lower menu
				case 0:
					if(cursor.getY() == 272 + (menuItems[2].length-1)*20) {
						cursor.setY(cursor.getY()-((menuItems[2].length-1)*20));
					   	setSelection(0);
					}					
					else {
						cursor.setY(cursor.getY()+20);
					   	selection++;
					}
					break;
				// Misc items
				case 1:
					if(cursor.getY() == 72 + (amountMisc-1)*20) {
						cursor.setY(272);
						cursor.setX(480);
					   	setSubmenu(0);
					   	setSelection(0);
					}
					else {
						cursor.setY(cursor.getY()+20);
					   	selection++;
					}
					break;
				// Ship hull & Beams
				case 2:
					if(cursor.getY() == 172 + (amountBeam)*20) {
						cursor.setY(272);
						cursor.setX(480);
					   	setSubmenu(0);
					   	setSelection(0);
					}
					else if(cursor.getY() == 52 + amountHull*20) {
						cursor.setY(192);
					   	selection++;
					}
					else {
						cursor.setY(cursor.getY()+20);
					   	selection++;
					}
					break;
			}
			break;
		}
	}
	
	public void moveCursorLeft(int subSection)
	{
		// Player menu
		if (getMenu() == 2) {
			playSound("click.wav");
			switch(subSection) {
			// Lower menu
			case 0:
				cursor.setY(52 + (amountMisc*20));
				cursor.setX(180);
				setSelection(amountMisc);
				setSubmenu(1);
				break;
			// Ship hull & Beams
			case 2:
				if(getSelection() > (amountMisc)) {
					cursor.setY(52 + (amountMisc*20));
					setSelection(amountMisc);
				}
				else {
					cursor.setY(52 + (getSelection()*20));
				}			
				cursor.setX(180);
				setSubmenu(1);
			   	break;
			}
		}
	}
	
	public void moveCursorRight(int subSection)
	{
		// Player menu
		if (getMenu() == 2) {
			playSound("click.wav");
			switch(subSection) {
			// Lower menu
			case 0:
				cursor.setY(172 + (amountBeam*20));
				cursor.setX(730);
				setSelection(amountHull + amountBeam);
				setSubmenu(2);
				break;
			// Misc items
			case 1:
				if(getSelection() > (amountHull + amountBeam)) {
					cursor.setY(172 + (amountBeam*20));
					setSelection(amountHull + amountBeam);
				}
				else if (getSelection() > amountHull) {
					cursor.setY(132 + (getSelection()*20));				
				}
				else
					cursor.setY(52 + (getSelection()*20));
				
				cursor.setX(730);
				setSubmenu(2);
			   	break;
			}
		}
	}
	
	public static int getSelection()
	{
		return selection;
	}
	public static void setSelection(int selection)
	{
		GUI.selection = selection;
	}
	public int getMenu()
	{
		return menu;
	}
	public void setMenu(int menu)
	{
		switch(menu) {
			case 0: // Startup menu
			case 2: // Player menu (goes to lower menu)
				cursor.setY(272);
				cursor.setX(480);
				cursor.setStarter(272);
				break;
			case 1: // Stage select			
				cursor.setY(92);
				cursor.setX(80);
				cursor.setStarter(192);
				break;	
		}
		this.menu = menu;
	}
	public void activate(int selection)
	{
		for (ShipItem item : World.getShip().getHullItems()) {
			if(item.getValue())
				World.getShip().setSkin(item.getType());
		}
		switch(selection) {
			case 0:
				setMenu(selection);
	    		setSelection(0);
	    		break;
			case 1:
				World.startGame(0);
				break;
			case 2:
				World.startGame(1);
				break;
			case 3:
				World.startGame(2);
				break;
			case 4:
				World.startGame(3);
				break;
			case 5:
				World.startGame(4);
				break;
			case 6:
				World.startGame(5);
				break;
		}
	}
	public int getSubmenu()
	{
		return submenu;
	}

	public void setSubmenu(int submenu)
	{
		this.submenu = submenu;
	}
}


