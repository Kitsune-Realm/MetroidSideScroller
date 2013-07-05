package control;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import player.Ship;
import player.ShipItem;
import view.Fullscreen;
import view.GUI;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import effects.Beam;
import effects.Explosion;
import effects.Projectile;
import effects.Shield;
import effects.Upgrade;
import enemy.Bugger;
import enemy.Bull;
import enemy.Enemy;
import enemy.KiHunter;
import enemy.Preed;
import enemy.Reflec;


public class World extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 425;
	public static final String VERSION = "0.7.1";
	private static JPanel card1;
	private static JPanel card2;
	private static JPanel cards;
	private static CardLayout cl;
	private static Ship ship;
	private static int[] highScores = new int[6];
	private static DisplayMode dm;
	
	public World()
	{		
		super("Metroid Space Shooter");
		dm = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.ship = new Ship();
		card1 = new GUI();
		cl = new CardLayout();
		cards = new JPanel(cl);
		cards.add(card1, "menu");			
		getContentPane().add(cards);
		cl.show(cards, "menu");
		pack();
		setVisible(true);
		setResizable(false);	
		
		try {
			openSettings();
		} catch (FileNotFoundException e) {
			JOptionPane.showConfirmDialog(null, "Settings could not be loaded!");
			for (int i=0; i<highScores.length; i++) {
				highScores[i] = 0;
			}
		}
	}
	
	public static void startGame(int stage)
	{
		ship.initItems();
		World.saveSettings();
		card2 = new WorldPanel(10, stage);
		cards.add(card2, "game");
		cl.show(cards, "game");
	}
	
	public static void main(String[] args)
	{		
		World world = new World();
		world.run(dm);
	}
	
	public void run(DisplayMode dm) 
	{
		Fullscreen fs = new Fullscreen();
		try {
			fs.setFullScreen(dm, this);
			try {
				Thread.sleep(10000);
			}
			catch (InterruptedException ex) {}
		}
		finally {
			//fs.CloseFullScreen();
		}
	}
	
	public static Ship getShip()
	{
		return ship;
	}
	
	public static int[] getHighScores()
	{
		return highScores;
	}
	
	public static void setHighScores(int index, int score)
	{
		highScores[index] = score;
	}
	
	public static void openSettings() throws FileNotFoundException
	{
		DataInputStream input = new DataInputStream(new FileInputStream("settings.dat"));
		try {			
			int index = 0;
			for (int i=0; i<highScores.length; i++) {			
				highScores[index] = input.readInt();
				System.out.println(index + " - " + highScores[index]);
				index++;
			}
			for (int i=0; i<ship.getHullItems().size(); i++)
			{
				ship.getHullItems().get(i).setEnabled(input.readBoolean());
			}
			for (int i=0; i<ship.getHullItems().size(); i++)
			{
				ship.getHullItems().get(i).setValue(input.readBoolean());
			}
			for (int i=0; i<ship.getMiscItems().size(); i++)
			{
				ship.getMiscItems().get(i).setEnabled(input.readBoolean());
			}
			for (int i=0; i<ship.getMiscItems().size(); i++)
			{
				ship.getMiscItems().get(i).setValue(input.readBoolean());
			}
			for (int i=0; i<ship.getBeamItems().size(); i++)
			{
				ship.getBeamItems().get(i).setEnabled(input.readBoolean());
			}
			for (int i=0; i<ship.getBeamItems().size(); i++)
			{
				ship.getBeamItems().get(i).setValue(input.readBoolean());
			}
		}
		catch (EOFException ex) {
			System.out.println("all data read!");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}		
	}
	
	public static void saveSettings()
	{
		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream("settings.dat"));
			for (int i=0; i<highScores.length; i++) {
				output.writeInt(highScores[i]);
			}
			for (ShipItem item : ship.getHullItems()) {
				output.writeBoolean(item.isEnabled());
			}
			for (ShipItem item : ship.getHullItems()) {
				output.writeBoolean(item.getValue());
			}
			for (ShipItem item : ship.getMiscItems()) {
				output.writeBoolean(item.isEnabled());
			}
			for (ShipItem item : ship.getMiscItems()) {
				output.writeBoolean(item.getValue());
			}	
			for (ShipItem item : ship.getBeamItems()) {
				output.writeBoolean(item.isEnabled());
			}
			for (ShipItem item : ship.getBeamItems()) {
				output.writeBoolean(item.getValue());
			}	
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}

class WorldPanel extends WiiTest implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;

	private final int PROJECTILES = 10;	
	
	private int score = 0;
	
	private Shape[] stars;
	private int hyperspaceVelocity;
	
	private int stage;
	private boolean dirLeft;
	private boolean dirRight;
	private boolean dirUp;
	private boolean dirDown;
	private boolean infiniteFire;
	private Ship ship;
	private boolean charging;
	private boolean charged;
	
	private Projectile[] projectiles = new Projectile[PROJECTILES];	
	private Projectile[] enemyProjectiles = new Projectile[PROJECTILES];
	private Projectile[] kiHunterProjectiles = new Projectile[PROJECTILES];
	
	private int currentProj = 0;
	private int spitter = 0;
	private Upgrade currentUpgrade;
	private Timer timer;
	private static boolean paused;
	private BufferedImage background;
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private List<Explosion> explosions = new ArrayList<Explosion>();
	
	private Image imgGluinoThruster;
	private Image imgHealth;
	
	public WorldPanel(int amount, int stage)
	{
		setFocusable(true);
		requestFocus();
		requestFocusInWindow();
		addKeyListener(this);
		this.stage = stage;
		this.ship = World.getShip();
		
		this.addComponentListener( new ComponentAdapter() {
	        @Override
	        public void componentShown( ComponentEvent e ) {
	            WorldPanel.this.requestFocusInWindow();
	        }
	    });
		
		try {
			background = ImageIO.read(new File("src/images/interface/background_"+stage+".gif"));
			imgGluinoThruster = ImageIO.read(new File("src/images/upgrades/gluino_thruster.gif"));
			imgHealth = ImageIO.read(new File("src/images/interface/health_bar.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stars = new Shape[amount];
		setBackground(Color.BLACK);		
		for (int i=0; i<amount; i++) {
			Random rand = new Random();
			stars[i] = new Ellipse2D.Double(1000,rand.nextInt(400)+5,2,2);
		}
		hyperspaceVelocity = 5;
		
		this.ship = ship;
		ship.setShield(new Shield(ship.getX(), ship.getY()));
		
		for(int n = 0; n < PROJECTILES; n++) {
            projectiles[n] = new Projectile(ship.getLoadedBeam());
        }
		
		for(int n = 0; n < PROJECTILES; n++) {
            enemyProjectiles[n] = new Projectile(Beam.POWER);
        }
		
		for(int n = 0; n < PROJECTILES; n++) {
            kiHunterProjectiles[n] = new Projectile(Beam.ACIDSPIT);
        }
		
		/*
		// cannot get this to work properly yet
		
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		PlugInManager.addPlugIn(
			"com.sun.media.codec.audio.mp3.JavaDecoder",
			new Format[]{input1, input2},
			new Format[]{output},
			PlugInManager.CODEC
		);
		try{
			Player player = Manager.createPlayer(new MediaLocator(new File("src/music/stage1.mp3").toURI().toURL()));
			player.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
		} */
	
		timer = new Timer(1000/50, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		updateStars();
		updateProjectiles();
		updateEnemyProjectiles();
		updateKiHunterProjectiles();
		ship.getShield().draw();
		if(ship != null) {
			checkCollisions();
			checkShip();
		}
		updateEnemies();
		updateExplosions();

		switch (stage) {
		// Horsehead Nebula
		case 0:
			if (enemies.size() < 30) {enemies.add(new KiHunter(1000,Math.random()*350));
			 enemies.add(new Bugger(1000,Math.random()*350, GameUnit.ZEB));
			 enemies.add(new Bugger(1000,Math.random()*350, GameUnit.ZEB));
			 enemies.add(new Bugger(1000,Math.random()*350, GameUnit.ZEB));
			 currentUpgrade = new Upgrade(100,100,Beam.values()[1]);} 
			break;
		// Cone Nebula
		case 1:
			if (enemies.size() < 100) {enemies.add(new Bugger(1000,Math.random()*350, GameUnit.GAMET)); 
			currentUpgrade = new Upgrade(100,100,Beam.values()[2]);}
			break;
		// Crab Nebula
		case 2:
			if (enemies.size() < 30) {enemies.add(new Bugger(1000,Math.random()*350, GameUnit.ZEB)); 
			enemies.add(new KiHunter(1000,Math.random()*350));
			currentUpgrade = new Upgrade(100,100,Beam.values()[1]);}
			break;
		// Tallon IV
		case 3:
			if (enemies.size() < 60) {enemies.add(new Bugger(1000,Math.random()*350, GameUnit.ZEB));
			enemies.add(new KiHunter(1000,Math.random()*350));
			enemies.add(new Reflec(1000,Math.random()*350));
			currentUpgrade = new Upgrade(100,100,Beam.values()[2]);}
			break;
		// SR388
		case 4:
			if (enemies.size() < 20) {enemies.add(new Bull(1000,Math.random()*350)); 
			enemies.add(new Preed(1000,Math.random()*350));
			currentUpgrade = new Upgrade(100,100,Beam.values()[2]);}
			break;			
		// Ceres
		case 5:
			break;		
		}
		
		if (charging && !charged) {
			ship.setCharge(ship.getCharge()+1);
		}
		if (ship.getCharge() >= ship.getChargeLimit()) {
			charged = true;
		}
		repaint();	
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void checkCollisions()
	{	
		// enemy projectile hits ship
		for (Projectile projectile : enemyProjectiles) {
			if (ship.collide(projectile.getX(), projectile.getY(), projectile.getWidth(), projectile.getHeight()) && projectile.isAlive()) {
				projectile.setAlive(false);
				ship.setHealth(ship.getHealth()-projectile.getDamage());
				continue;
			}
		}
		// KiHunter acid hits ship
				for (Projectile projectile : kiHunterProjectiles) {
					if (ship.collide(projectile.getX(), projectile.getY(), projectile.getWidth(), projectile.getHeight()) && projectile.isAlive()) {
						projectile.setAlive(false);
						ship.setHealth(ship.getHealth()-projectile.getDamage());
						continue;
					}
				}
		for (Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				// enemy collide with ship
				if (ship.collide(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {
					if(ship.hasShield()) {
						ship.setShieldActive(false);
						System.out.println("Shield lost!");
						playSound("shieldHit.wav");
					}
					explosions.add(new Explosion(enemy.getX() + (enemy.getWidth()/2), enemy.getY() + (enemy.getHeight()/2), enemy.getType()));
					enemy.setAlive(false);
					ship.setHealth(ship.getHealth()-enemy.getDamage());
					System.out.println("Ship was hit!");
				}
				
				// enemy collide with projectile
				for (Projectile projectile : projectiles) {
					if (enemy.getHitbox().intersects(projectile.getHitbox()) && projectile.isAlive()) {
						if(enemy.getType()==GameUnit.REFLEC && projectile.getType()==Beam.POWER) {
							System.out.println("reflect!");
							// TODO work out so multiple can be in play simultaneously
				            enemyProjectiles[0].setAlive(true);                
				            enemyProjectiles[0].setX(enemy.getX());
				            enemyProjectiles[0].setY(enemy.getY());	
						}
						enemy.setHealth( enemy.getHealth() - projectile.getDamage() );
						projectile.setAlive(projectile.isPenetrate());
						if (enemy.getHealth() <= 0) {
							enemy.setAlive(false);
							explosions.add(new Explosion(enemy.getX() + (enemy.getWidth()/2), enemy.getY() + (enemy.getHeight()/2), enemy.getType()));
							score += enemy.getScoreValue();
						}
						continue;
					}
				}
			}
			// pickup upgrade
			if(currentUpgrade != null) {
				if (ship.collide(currentUpgrade.getX(), currentUpgrade.getY(), currentUpgrade.getWidth(), currentUpgrade.getHeight())) {
					charging = false;
					charged = false;
					ship.setCharge(0);
					switch (currentUpgrade.getType()) {
						case POWER:
							System.out.println("Powerbeam loaded!");
							ship.setLoadedBeam(Beam.POWER);
							break;
						case SINUS:
							System.out.println("Sinusbeam loaded!");
							ship.setLoadedBeam(Beam.SINUS);
							break;
						case PLASMA:
							System.out.println("Plasmabeam loaded!");
							ship.setLoadedBeam(Beam.PLASMA);
							break;
					default:
						break;
					}
					
					/*
					for(Projectile projectile : projectiles) {
						if(!projectile.isAlive()) {
							projectile = new Projectile(ship.getLoadedBeam());
						}
					}
					
					for(int n = 0; n < PROJECTILES; n++) {
			            projectiles[n] = new Projectile(ship.getLoadedBeam());
			        }*/
					playSound("upgrade.wav");
					currentUpgrade = null;
				}
			}
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		drawBackground(g2);		
		drawShip(g2);		
		
		// draw enemies
		for(Enemy enemy : enemies) {
			g2.drawImage(enemy.getSprites(), (int)enemy.getX(), (int)enemy.getY(), this);
			if (enemy.getType() == GameUnit.KIHUNTER) {
				KiHunter current = (KiHunter)enemy;
				g2.drawImage(current.getWings().getSprites(), (int)enemy.getX()+47, (int)enemy.getY()-5, this);
			}
			//System.out.println(enemies.toString());
			//g2.draw(enemy.getHitbox());
		}
		
		// draw projectiles
		for(int i=0; i<PROJECTILES;i++) {
			if(projectiles[i].isAlive()) {
				//g2.draw(projectiles[i].getHitbox());
				g2.drawImage(projectiles[i].getSprites(),
						(int)projectiles[i].getX(),
						(int)projectiles[i].getY(),
						this);
			}
			if(enemyProjectiles[i].isAlive()) {
				//g2.draw(projectiles[i].getHitbox());
				AffineTransform tx = new AffineTransform();
				tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-enemyProjectiles[i].getSprites().getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				Image image = op.filter((BufferedImage) enemyProjectiles[i].getSprites(), null);
				
				g2.drawImage(image,
						(int)enemyProjectiles[i].getX(),
						(int)enemyProjectiles[i].getY(),
						this);
			}
			if(kiHunterProjectiles[i].isAlive()) {
				//g2.draw(kiHunterProjectiles[i].getHitbox());
				g2.drawImage(kiHunterProjectiles[i].getSprites(),
						(int)kiHunterProjectiles[i].getX(),
						(int)kiHunterProjectiles[i].getY(),
						this);
			}
		}		
		// draw explosions
		for(Explosion explosion : explosions) {
			g2.drawImage(explosion.getSprites(), (int)explosion.getX(), (int)explosion.getY(), this);
		}
		
		// draw upgrade
		if(currentUpgrade != null)
			g2.drawImage(currentUpgrade.getSprites(), (int)currentUpgrade.getX(), (int)currentUpgrade.getY(), this);
		
		// draw statistics
		g2.setColor(Color.WHITE);
		g2.drawString("X:  " + (int)ship.getX(), 5, 12);
		g2.drawString("Y:  " + (int)ship.getY(), 5, 24);
		g2.drawString("Score:", 810, 390);
		if (score > World.getHighScores()[stage])
			g2.setColor(Color.YELLOW);
		g2.drawString((new Integer(score).toString()), 850, 390);

		g2.drawImage(ship.getLoadedBeam().getSprites(), 100, 377, this);
		if(ship.getVelocity() == 9)
			g2.drawImage(imgGluinoThruster, 330, 377, this);		
				
		for(int i=0; i<ship.getHealth(); i++)
		{
			g2.drawImage(imgHealth, 10+(i*9), 382, this);
		}
		
		try {
			TexturePaint tp = new TexturePaint(ImageIO.read(new File("src/images/interface/charge_texture.gif")), new Rectangle2D.Double(140,380,140,11));
			g2.setPaint(tp);
			g2.fill(new Rectangle2D.Double(140,380, ship.getCharge()*2 ,11));
			g2.drawImage(ImageIO.read(new File("src/images/interface/charge_bar.gif")), 130, 380, this);					
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2.setColor(Color.WHITE);
		g2.drawLine(0, 375, 1000, 375);
		// draw Pause
		if (paused) {
			g2.drawString("Game paused - press P to continiue", 400, 250);
		}		
	}
	
	@Override
	public void keyTyped(KeyEvent arg0){ }
	@Override
	public void keyReleased(KeyEvent k){
		switch(k.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				chargeBeam();
				break;
			case KeyEvent.VK_A:
				dirLeft = false;
				break;
			case KeyEvent.VK_S:
				dirDown = false;
				break;
			case KeyEvent.VK_D:
				dirRight = false;
				break;
			case KeyEvent.VK_W:
				dirUp = false;
				break;
		}
	}
	@Override
	public void keyPressed(KeyEvent k)
	{
		int keyCode = k.getKeyCode();
        switch(keyCode){
	        case KeyEvent.VK_M:
	        	ship.setAmmunition(Beam.POWER, ship.getAmmunition(Beam.POWER)+10);
	        	ship.setAmmunition(Beam.SINUS, ship.getAmmunition(Beam.SINUS)+10);
	        	ship.setAmmunition(Beam.PLASMA, ship.getAmmunition(Beam.PLASMA)+10);
	        	break;
            case KeyEvent.VK_A:
            	//System.out.println("left");
            	dirLeft = true;
                break;
            case KeyEvent.VK_D:
            	//System.out.println("right");
            	dirRight = true;
            	break;
            case KeyEvent.VK_W:
            	//System.out.println("up");
            	dirUp = true;
                break;
            case KeyEvent.VK_S:
            	//System.out.println("down");
            	dirDown = true;          	
            	break;
            case KeyEvent.VK_SPACE:
            	//System.out.println("spacebar");
            	fireBeam();        	
            	break;
            case KeyEvent.VK_P:
            	pauseGame();
            	break;
        }
	}
	
	@Override
	public void onButtonsEvent(WiimoteButtonsEvent e)
	{
		if (e.isButtonAJustPressed())
			pauseGame();
		
		if (e.isButtonOneJustPressed())
			fireBeam();
		if (e.isButtonOneJustReleased())
			chargeBeam();
		
		if (!e.isButtonRightHeld())
			dirUp = false;
		if (!e.isButtonLeftHeld())
			dirDown = false;
		if (!e.isButtonUpHeld())
			dirLeft = false;
		if (!e.isButtonDownHeld())
			dirRight = false;
		
		
		if (e.isButtonRightPressed()) {
			dirUp = true;
		}
		if (e.isButtonLeftPressed()) {
			dirDown = true;
		}
		if (e.isButtonUpPressed()) {
			dirLeft = true;
		}
		if (e.isButtonDownPressed()) {
			dirRight = true;
		}		
	}
	
	public void fireBeam()
	{
		if(ship.getLoadedBeam() == Beam.PLASMA)
    		charging = true;
    	currentProj++;            	
    	if(!infiniteFire) {
    		// TODO this switch has become obsolete, play sounds in the explosion class
    		updateLoadedBeam();
    		switch(ship.getLoadedBeam()) {
    			case POWER:
    				playSound("laser.wav");
    				break;
    			case PLASMA:
    				playSound("plasma"+new Random().nextInt(2)+".wav");
    				break;
    			case SINUS:
    				playSound("sinus.wav");
    				break;
				default:
					break;
    		}
            if (ship.getLoadedBeam() == Beam.POWER)
            	explosions.add(new Explosion(ship.getX() + (ship.getWidth()/2), ship.getY() + (ship.getHeight()/2), GameUnit.POWERBIRTH));
            else if (ship.getLoadedBeam() == Beam.PLASMA)
            	explosions.add(new Explosion(ship.getX() + (ship.getWidth()/2), ship.getY() + (ship.getHeight()/2), GameUnit.PLASMABIRTH));
            else if (ship.getLoadedBeam() == Beam.PLASMACHARGE) {
            	ship.setLoadedBeam(Beam.PLASMA);
            	explosions.add(new Explosion(ship.getX() + (ship.getWidth()/2), ship.getY() + (ship.getHeight()/2), GameUnit.PLASMABIRTH));
            }	                
            if(currentProj > PROJECTILES -1){ currentProj = 0; }
            projectiles[currentProj].setAlive(true);                
            projectiles[currentProj].setX(ship.getX()+60);
            projectiles[currentProj].setY(ship.getY()+20);
            infiniteFire = true;	                
    	}    
	}
	
	public void chargeBeam()
	{
		infiniteFire = false;
		charging = false;
		if (ship.getCharge() >= ship.getChargeLimit()) {
			ship.setLoadedBeam(Beam.PLASMACHARGE);
			updateLoadedBeam();
			if(currentProj > PROJECTILES -1){ currentProj = 0; }
            projectiles[currentProj].setAlive(true);                
	        projectiles[currentProj].setX(ship.getX()+60);
	        projectiles[currentProj].setY(ship.getY()+20);
            explosions.add(new Explosion(ship.getX() + (ship.getWidth()/2), ship.getY() + (ship.getHeight()/2), GameUnit.PLASMACHARGEBIRTH));
            ship.setLoadedBeam(Beam.PLASMA);
			ship.setCharge(0);
			charged = false;
		}
		else
		{
			ship.setCharge(0);
		}
	}
	
	public void updateExplosions()
	{
		for (int i =0; i < explosions.size(); i++) {
			if(!explosions.get(i).isAlive()) {
				explosions.remove(i);
			}
		}
	}
	
	public void updateStars()
	{
		for (int i=0; i<stars.length; i++) {
			AffineTransform tx = new AffineTransform();
			tx.translate(-1*(i*hyperspaceVelocity), 0);			
			stars[i] = tx.createTransformedShape(stars[i]);
			if(stars[i].getBounds().getX() < 0) {
				Random rand = new Random();
				stars[i] = new Ellipse2D.Double(1000,rand.nextInt(400)+5,2,2);
			}
		}
	}
	
	public void updateProjectiles()
	{
		for (int i=0; i<PROJECTILES; i++) {
			if(projectiles[i].isAlive()) {
				projectiles[i].setX(projectiles[i].getX()+projectiles[i].getVelocity());
				projectiles[i].setHitbox(new Rectangle2D.Double(
						projectiles[i].getX(), 
						projectiles[i].getY(), 
						projectiles[i].getWidth(), 
						projectiles[i].getHeight()));
				if(projectiles[i].getX() > World.SCREEN_WIDTH)
					projectiles[i].setAlive(false);
			}
		}
	}
	public void updateEnemyProjectiles()
	{
		for (int i=0; i<PROJECTILES; i++) {
			if(enemyProjectiles[i].isAlive()) {
				
				enemyProjectiles[i].setX(enemyProjectiles[i].getX()-enemyProjectiles[i].getVelocity());
				enemyProjectiles[i].setHitbox(new Rectangle2D.Double(
						enemyProjectiles[i].getX(), 
						enemyProjectiles[i].getY(), 
						enemyProjectiles[i].getWidth(), 
						enemyProjectiles[i].getHeight()));
				if(enemyProjectiles[i].getX() < 0 || enemyProjectiles[i].getX() > World.SCREEN_WIDTH)
					enemyProjectiles[i].setAlive(false);
			}
		}
	}
	public void updateKiHunterProjectiles()
	{
		for (int i=0; i<PROJECTILES; i++) {
			if(kiHunterProjectiles[i].isAlive()) {
				kiHunterProjectiles[i].setX(kiHunterProjectiles[i].getX()-5);
				kiHunterProjectiles[i].setHitbox(new Rectangle2D.Double(
						kiHunterProjectiles[i].getX(), 
						kiHunterProjectiles[i].getY(), 
						kiHunterProjectiles[i].getWidth(), 
						kiHunterProjectiles[i].getHeight()));
				if(kiHunterProjectiles[i].getX() < 0 || kiHunterProjectiles[i].getY() > World.SCREEN_HEIGHT)
					kiHunterProjectiles[i].setAlive(false);
			}
		}
	}
	
	public void updateEnemies()
	{	
		for (int i =0; i < enemies.size(); i++) {
			try {				
				if(enemies.get(i).isAlive()) {
					enemies.get(i).move();
					if(enemies.get(i).getX() < 0)
						enemies.remove(i);
					
					if(enemies.get(i) instanceof KiHunter) {					
						((KiHunter) enemies.get(i)).attack();
						if (((KiHunter) enemies.get(i)).isAttackFinished()) {
							((KiHunter) enemies.get(i)).setAttackFinished(false);
							playSound("spit.wav");
							spitter++;
							if(spitter > PROJECTILES -1){ spitter = 0; }
				            kiHunterProjectiles[spitter].setAlive(true);                
				            kiHunterProjectiles[spitter].setX(enemies.get(i).getX());
				            kiHunterProjectiles[spitter].setY(enemies.get(i).getY());	
						}
					}
					else if (enemies.get(i) instanceof Bull) {
						((Bull) enemies.get(i)).findTarget(ship.getX(), ship.getY());
					}
					else if (enemies.get(i) instanceof Bugger && (enemies.get(i).getType()==GameUnit.GAMET)) {
						((Bugger) enemies.get(i)).setTarget(ship.getY(), ship.getHeight());						
					}
				}
				else {
					enemies.get(i).death();
					enemies.remove(i);
				}	
			}
			catch (IndexOutOfBoundsException ex) {
				//ex.printStackTrace();
			}
		}
	}
	
	/*
	 * This method makes sure that projectiles are only replaced if they did not exist in the game already.
	 * It currently causes lots of lag in the game.
	 */
	public void updateLoadedBeam() 
	{
		for(int n = 0; n < PROJECTILES; n++) {
			if (!projectiles[n].isAlive()) {
				projectiles[n] = new Projectile(ship.getLoadedBeam());
			}
        }
	}
	
	public void checkShip()
	{
		if (ship.getHealth() < 0) {
			ship.setHealth(0);
			explosions.add(new Explosion(ship.getX() + (ship.getWidth()/2), ship.getY() + (ship.getHeight()/2), GameUnit.SHIP));
			ship.setX(-10000);
			ship.setY(-10000);			
			if (World.getHighScores()[(stage)] < score) {
				World.setHighScores((stage), score);				
			}	
			World.saveSettings();
		}
	}
	
	// needs to worked out much better than this crap...
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
	
	public void pauseGame()
	{
		playSound("pause.wav");
		if (!paused) {
			paused = true;
			timer.stop();	
			repaint();
		}
		else {
			paused = false;
			timer.start();			
		}
	}
	
	public void drawBackground(Graphics2D g2)
	{
		// draw background image
		g2.drawImage(background, 0, 0, this);
				
		// draw pretty stars
		g2.setColor(Color.WHITE);
		for(int i=0; i<stars.length; i++) {
			g2.fill(stars[i]);
		}
	}
	
	public void drawShip(Graphics2D g2)
	{
		if(dirRight && ship.getX() < World.SCREEN_WIDTH-80)
			ship.setX(ship.getX()+ship.getVelocity());
		if(dirLeft && ship.getX() > 0)
			ship.setX(ship.getX()-ship.getVelocity());
		if(dirDown && ship.getY() < World.SCREEN_HEIGHT-90)
			ship.setY(ship.getY()+ship.getVelocity());
		if(dirUp && ship.getY() > 0 - 10)
			ship.setY(ship.getY()-ship.getVelocity());
				
		g2.setColor(Color.RED); // used to make hitboxes visible
		//g2.draw(ship.getHitbox());
				
		g2.drawImage(ship.getSprites(), (int)ship.getX(), (int)ship.getY(), this);
				
		// draw shield (over ship)
		if(ship.hasShield()) {
			g2.drawImage(ship.getShield().getSprites(), (int)ship.getX()+16, (int)ship.getY()-11, this);
		}
	}
}
