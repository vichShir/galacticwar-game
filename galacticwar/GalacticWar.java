/*******************************************************
 * GALACTIC WAR, Capitulo 13
 *******************************************************/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/*******************************************************
 * Primary class for the game
 *******************************************************/
public class GalacticWar extends Applet implements Runnable, KeyListener
{
    private static final long serialVersionUID = 1L;

	// Global constants
	static int SCREENWIDTH = 800;
	static int SCREENHEIGHT = 600;
	static int CENTERX = SCREENWIDTH / 2;
	static int CENTERY = SCREENHEIGHT / 2;
	static int ASTEROIDS = 10;
	static int BULLETS = 10;
	static int BULLET_SPEED = 4;
	static double ACCELERATION = 0.05;
	
	// Sprite state values
	static int STATE_NORMAL = 0;
	static int STATE_COLLIDED = 1;
	static int STATE_EXPLODING = 2;

	// The main thread becomes the game loop
	Thread gameloop;
	
	// Double buffer objects
	BufferedImage backbuffer;
	Graphics2D g2d;
	
	// Various toggles
	boolean showBounds = true;
	boolean collisionTesting = true;
	long collisionTimer = 0;

	// Define the game objects
	ImageEntity background;
	Sprite ship;
	Sprite[] ast = new Sprite[ASTEROIDS];
	Sprite[] bullet = new Sprite[BULLETS];
	int currentBullet = 0;
	AnimatedSprite explosion;
	
	// Create a random number generator
	Random rand = new Random();

	// Define the sound effects objects
	SoundClip shoot;
    SoundClip explode;
    
	// Simple way to handle multiple keypress
	boolean keyDown, keyUp, keyLeft, keyRight, keyFire;

	// Frame rate counter
	int frameCount = 0, frameRate = 0;
	long startTime = System.currentTimeMillis();
	
	/*
	 * Applet init event
	 */
	public void init()
	{
		// Create the back buffer for smooth graphics
		backbuffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();

		// Load the background image
		background = new ImageEntity(this);
		background.load("media/images/bluespace.png");
		
		// Set up the ship
		ship = new Sprite(this, g2d);
        ship.load("media/images/spaceship.png");
        ship.setPosition(new Point2D(CENTERX, CENTERY));
		ship.setAlive(true);
		
		// Set up the bullets
		for(int n = 0; n < BULLETS; n++)
		{
			bullet[n] = new Sprite(this, g2d);
			bullet[n].load("media/images/plasmashot.png");
		}
		
		// Set up the asteroids
		for(int n = 0; n < ASTEROIDS; n++)
		{
			ast[n] = new Sprite(this, g2d);
			ast[n].setAlive(true);
			// Load the asteroid image
			int i = rand.nextInt(5) + 1;
			ast[n].load("media/images/asteroid" + i + ".png");
			// Set to a random position on the screen
			int x = rand.nextInt(SCREENWIDTH);
			int y = rand.nextInt(SCREENHEIGHT);
			ast[n].setPosition(new Point2D(x, y));
			// Set rotation angles to a random value
			ast[n].setFaceAngle(rand.nextInt(360));
			ast[n].setMoveAngle(rand.nextInt(360));
			ast[n].setRotationRate(rand.nextDouble());
			// Set velocity based on movement direction
			double ang = ast[n].moveAngle() - 90;
			double velx = calcAngleMoveX(ang);
			double vely = calcAngleMoveY(ang);
			ast[n].setVelocity(new Point2D(velx, vely));
		}

        // Load sound files
        shoot = new SoundClip("media/sounds/shoot.wav");
        explode = new SoundClip("media/sounds/explode.wav");

		// Load the explosion
		explosion = new AnimatedSprite(this, g2d);
		explosion.load("media/images/explosion.png", 4, 4, 96, 96);
		explosion.setFrameDelay(2);
		explosion.setAlive(false);
		
		// Start the user input listener
		addKeyListener(this);
	}
	
	/*
	 * Applet update event to redraw the screen
	 */
	public void update(Graphics g)
	{
		// Calculate frame rate
		frameCount++;
		if(System.currentTimeMillis() > startTime + 1000)
		{
			startTime = System.currentTimeMillis();
			frameRate = frameCount;
			frameCount = 0;
		}

		// Draw the background
		g2d.drawImage(background.getImage(), 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);

		// Draw the games graphics drawAsteroids();
		drawShip();
		drawBullets();
		drawExplosions();
		drawAsteroids();

		// Print status information on the screen
		g2d.setColor(Color.WHITE);
		g2d.drawString("FPS: " + frameRate, 5, 10);
		long x = Math.round(ship.position().X());
		long y = Math.round(ship.position().Y());
		g2d.drawString("Ship: " + x + "," + y, 5, 25);
		g2d.drawString("Move angle: " + Math.round(ship.moveAngle()) + 90, 5, 40);
		g2d.drawString("Face angle: " + Math.round(ship.faceAngle()), 5, 55);

		if(ship.state() == STATE_NORMAL)
			g2d.drawString("State: NORMAL", 5, 70);
		else if(ship.state() == STATE_COLLIDED)
			g2d.drawString("State: COLLIDED", 5, 70);
		else if(ship.state() == STATE_EXPLODING)
			g2d.drawString("State: EXPLODING", 5, 70);

		if(showBounds)
		{
			g2d.setColor(Color.GREEN);
			g2d.drawString("BOUNDING BOXES", SCREENWIDTH-150, 10);
		}

		if(collisionTesting)
		{
			g2d.setColor(Color.GREEN);
			g2d.drawString("COLLISION TESTING", SCREENWIDTH-150, 25);
		}
		
		// Repaint the applet window
		paint(g);
	}
	
	/*
	 * DrawShip called by applet update event
	 */
	public void drawShip()
	{
        // Set the transform for the image
        ship.transform();
        ship.draw();

        // Draw bounding rectangle around ship
        if(showBounds)
        {
            if(ship.state() == STATE_COLLIDED)
				ship.drawBounds(Color.RED);
			else
				ship.drawBounds(Color.BLUE);
        }
	}
	
	/*
	 * DrawBullets called by applet update event
	 */
	public void drawBullets()
	{
		// Iterate through the array of bullets
		for(int n = 0; n < BULLETS; n++)
		{
			// Is this bullet currently in use?
			if(bullet[n].alive())
			{
				// Draw the bullet
				bullet[n].transform();
				bullet[n].draw();
				if(showBounds)
				{
					if(bullet[n].state() == STATE_COLLIDED)
						bullet[n].drawBounds(Color.RED);
					else
						bullet[n].drawBounds(Color.BLUE);
				}
			}
		}
	}
	
	/*
	 * DrawAsteroids called by applet update event
	 */
	public void drawAsteroids()
	{
		// Iterate through the asteroids array
		for(int n = 0; n < ASTEROIDS; n++)
		{
			// Is this asteoids being used?
			if(ast[n].alive())
			{
				// Draw the asteroid
				ast[n].transform();
				ast[n].draw();
				if(showBounds)
				{
					if(ast[n].state() == STATE_COLLIDED)
						ast[n].drawBounds(Color.RED);
					else
						ast[n].drawBounds(Color.BLUE);
				}
			}
		}
	}

	public void drawExplosions()
	{
		// Explosions don't need separate update method
		if(explosion.alive())
		{
			explosion.updateAnimation();
			if(explosion.currentFrame() == explosion.totalFrames() - 1)
			{
				explosion.setCurrentFrame(0);
				explosion.setAlive(false);
			}
			else
			{
				explosion.draw();
			}
		}
	}
	
	/*
	 * Applet window repaint event - draw the back buffer
	 */
	public void paint(Graphics g)
	{
		// Draw the back buffer onto the applet window
		g.drawImage(backbuffer, 0, 0, this);
	}
	
	/*
	 * Thread start event - start the game loop running
	 */
	public void start()
	{
		// Create the gameloop thread for real-time updates
		gameloop = new Thread(this);
		gameloop.start();
	}
	
	/*
	 * Thread run event (game loop)
	 */
	public void run()
	{
		// Acquire the current thread
		Thread t = Thread.currentThread();
		
		// Keep going as long as the thread is alive
		while(t == gameloop)
		{
			try
			{
				// Target framerate is 50 fps
				Thread.sleep(20);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			// Update the game loop
			gameUpdate();
			repaint();
		}
	}
	
	/*
	 * Thread stop event
	 */
	public void stop()
	{
		// Kill the gameloop thread
		gameloop = null;
	}
	
	/*
	 * Move and animate the objects in the game
	 */
	public void gameUpdate()
	{
		checkInput();
		updateShip();
		updateBullets();
		updateAsteroids();
		if(collisionTesting)
		{
			checkCollisions();
			handleShipCollisions();
			handleBulletCollisions();
			handleAsteroidCollisions();
		}
	}
	
	/*
	 * Update the ship position based on velocity
	 */
	public void updateShip()
	{
		ship.updatePosition();
		double newx = ship.position().X();
		double newy = ship.position().Y();
		
		// Wrap around left/right
		if(ship.position().X() < -10)
			newx = SCREENWIDTH + 10;
		else if(ship.position().X() > SCREENWIDTH + 10)
			newx = -10;
		
		// Wrap around top/bottom
		if(ship.position().Y() < -10)
			newy = SCREENHEIGHT + 10;
		else if(ship.position().Y() > SCREENHEIGHT + 10)
			newy = -10;

		ship.setPosition(new Point2D(newx, newy));
		ship.setState(STATE_NORMAL);
	}
	
	/*
	 * Update the bullets based on velocity
	 */
	public void updateBullets()
	{
		// Move each of the bullets
		for(int n = 0; n < BULLETS; n++)
		{
			// Is this bullet being used?
			if(bullet[n].alive())
			{
				// Update bullet's x position
				bullet[n].updatePosition();
				
				// Bullet disappears at left/right edge
				if(bullet[n].position().X() < 0 || bullet[n].position().X() > SCREENWIDTH)
				{
					bullet[n].setAlive(false);
				}
				
				// Update bullet's y position
				bullet[n].updatePosition();
				
				// Bullet disappears at top/bottom edge
				if(bullet[n].position().Y() < 0 || bullet[n].position().Y() > SCREENHEIGHT)
				{
					bullet[n].setAlive(false);
				}

				bullet[n].setState(STATE_NORMAL);
			}
		}
	}
	
	/*
	 * Update the asteroids based on velocity
	 */
	public void updateAsteroids()
	{
		// Move and rotate the asteroids
		for(int n = 0; n < ASTEROIDS; n++)
		{
			// Is this asteroid begin used?
			if(ast[n].alive())
			{
				// Update the asteroid's position and rotation
				ast[n].updatePosition();
				ast[n].updateRotation();

				int w = ast[n].imageWidth() - 1;
				int h = ast[n].imageHeight() - 1;
				double newx = ast[n].position().X();
				double newy = ast[n].position().Y();
				
				// Wrap the asteroid around the screen edges
				if(ast[n].position().X() < -w)
					newx = SCREENWIDTH + w;
				else if(ast[n].position().X() > SCREENWIDTH + w)
					newx = -w;
				if(ast[n].position().Y() < -h)
					newy = SCREENHEIGHT + h;
				else if(ast[n].position().Y() > SCREENHEIGHT + h)
					newy = -h;

				ast[n].setPosition(new Point2D(newx, newy));
				ast[n].setState(STATE_NORMAL);
			}
		}
	}
	
	/*
	 * Test asteroids for collisions with ship or bullets
	 */
	public void checkCollisions()
	{
		// Iterate through the asteroids array
		for(int m = 0; m < ASTEROIDS; m++)
		{
			// Is this asteroid being used?
			if(ast[m].alive())
			{
				/*
				 * Check for colision between asteroids and bullets
				 */
				for(int n = 0; n < BULLETS; n++)
				{
					// Is this bullet being used?
					if(bullet[n].alive())
					{
						// Collision?
						if(ast[m].collidesWith(bullet[n]))
						{
							bullet[n].setState(STATE_COLLIDED);
							ast[m].setState(STATE_COLLIDED);
							explode.play();
						}
					}
				}
			}
		}

		/*
		 * Check for collision asteroids and ship
		 */
		for(int m = 0; m < ASTEROIDS; m++)
		{
			if(ast[m].alive())
			{
				if(ship.collidesWith(ast[m]))
				{
					ast[m].setState(STATE_COLLIDED);
					ship.setState(STATE_COLLIDED);
					explode.play();
				}
			}
		}
	}

	public void handleShipCollisions()
	{
		if(ship.state() == STATE_COLLIDED)
		{
			collisionTimer = System.currentTimeMillis();
			ship.setVelocity(new Point2D(0, 0));
			ship.setState(STATE_EXPLODING);
			startExplosion(ship);
		}
		else if(ship.state() == STATE_EXPLODING)
		{
			if(collisionTimer + 3000 < System.currentTimeMillis())
			{
				ship.setState(STATE_NORMAL);
			}
		}
	}

	public void startExplosion(Sprite sprite)
	{
		if(!explosion.alive())
		{
			double x = sprite.position().X() - sprite.getBounds().width / 2;
			double y = sprite.position().Y() - sprite.getBounds().height / 2;
			explosion.setPosition(new Point2D(x, y));
			explosion.setCurrentFrame(0);
			explosion.setAlive(true);
		}
	}

	public void handleBulletCollisions()
	{
		for(int n = 0; n < BULLETS; n++)
		{
			if(bullet[n].state() == STATE_COLLIDED)
			{
				// Nothing to do yet
			}
		}
	}

	public void handleAsteroidCollisions()
	{
		for(int n = 0; n < ASTEROIDS; n++)
		{
			if(ast[n].state() == STATE_COLLIDED)
			{
				// Nothing to do yet
			}
		}
	}

	/*
	 * Process keys that have been pressed
	 */
	public void checkInput()
	{
		if(keyLeft)
		{
			// Left arrow rotates ship left 5 degrees
			ship.setFaceAngle(ship.faceAngle() - 5);
			if(ship.faceAngle() < 0) ship.setFaceAngle(360 - 5);
		}
		else if(keyRight)
		{
			// Right arrow rotates ship right 5 degrees
			ship.setFaceAngle(ship.faceAngle() + 5);
			if(ship.faceAngle() > 360) ship.setFaceAngle(5);
		}

		if(keyUp)
		{
			// Up arrow applies thrust to ship
			applyThrust();
		}
	}
	
	/*
	 * Key listener events
	 */
	public void keyTyped(KeyEvent k) { }
	public void keyPressed(KeyEvent k)
	{
		switch(k.getKeyCode())
		{
			case KeyEvent.VK_LEFT:
				keyLeft = true;
				break;
			case KeyEvent.VK_RIGHT:
				keyRight = true;
				break;
			case KeyEvent.VK_UP:
				keyUp = true;
				break;
			case KeyEvent.VK_CONTROL:
				keyFire = true;
			case KeyEvent.VK_B:
				// Toogle bounding rectangles
				showBounds = !showBounds;
				break;
			case KeyEvent.VK_C:
				// Toggle collision testing
				collisionTesting = !collisionTesting;
				break;
		}
	}

	public void keyReleased(KeyEvent k) 
	{ 
		switch(k.getKeyCode())
		{
			case KeyEvent.VK_LEFT:
				keyLeft = false;
				break;
			case KeyEvent.VK_RIGHT:
				keyRight = false;
				break;
			case KeyEvent.VK_UP:
				keyUp = false;
				break;
			case KeyEvent.VK_CONTROL:
				keyFire = false;
				fireBullet();
		}
	}

	public void applyThrust()
	{
		// Up arrow adds thrust to ship (1/10 normal speed)
		ship.setMoveAngle(ship.faceAngle() - 90);

		// Calculate the X and Y velocity based on angle
		double velx = ship.velocity().X();
		velx += calcAngleMoveX(ship.moveAngle()) * ACCELERATION;
		double vely = ship.velocity().Y();
		vely += calcAngleMoveY(ship.moveAngle()) * ACCELERATION;
		ship.setVelocity(new Point2D(velx, vely));
	}

	public void fireBullet()
	{
		// Fire a bullet
		currentBullet++;
		if(currentBullet > BULLETS - 1) currentBullet = 0;
		bullet[currentBullet].setAlive(true);

		// Set bullet's starting point
		int w = bullet[currentBullet].imageWidth();
		int h = bullet[currentBullet].imageHeight();
		double x = ship.center().X() - w/2;
		double y = ship.center().Y() - h/2;
		bullet[currentBullet].setPosition(new Point2D(x, y));

		// Point bullet in same direction ship is facing
		bullet[currentBullet].setFaceAngle(ship.faceAngle());
		bullet[currentBullet].setMoveAngle(ship.faceAngle() - 90);

		// Fire bullet at angle of the ship
		double angle = bullet[currentBullet].moveAngle();
		double svx = calcAngleMoveX(angle) * BULLET_SPEED;
		double svy = calcAngleMoveY(angle) * BULLET_SPEED;
		bullet[currentBullet].setVelocity(new Point2D(svx, svy));
		
		// Play shoot sound
		shoot.play();
	}
	
	/*
	 * Calculate X movement value based on direction angle
	 */
	public double calcAngleMoveX(double angle)
	{
		double movex = Math.cos(angle * Math.PI / 180);
		return movex;
	}
	
	/*
	 * Calculate Y movement value based on direction angle
	 */
	public double calcAngleMoveY(double angle)
	{
		double movey = Math.sin(angle * Math.PI / 180);
		return movey;
	}
}
