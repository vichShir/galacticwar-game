/*******************************************************
 * GALACTIC WAR, Capitulo 14
 *******************************************************/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.System;

/*******************************************************
 * Primary class for the game
 *******************************************************/
public class GalacticWar extends Game
{
    private static final long serialVersionUID = 1L;

	// These must be static because they are passed to a constructor
	static int FRAMERATE = 60;
    static int SCREENWIDTH = 800;
    static int SCREENHEIGHT = 600;
	
	// Misc global constants
	final int ASTEROIDS = 10;
    final int BULLET_SPEED = 4;
    final double ACCELERATION = 0.05;
    final double SHIPROTATION = 5.0;
	
	// Sprite state values
	final int STATE_NORMAL = 0;
    final int STATE_COLLIDED = 1;
    final int STATE_EXPLODING = 2;

	// Sprite types
	final int SPRITE_SHIP = 1;
    final int SPRITE_ASTEROID_BIG = 10;
    final int SPRITE_ASTEROID_MEDIUM = 11;
    final int SPRITE_ASTEROID_SMALL = 12;
    final int SPRITE_ASTEROID_TINY = 13;
    final int SPRITE_BULLET = 100;
    final int SPRITE_EXPLOSION = 200;
	
	// Various toggles
	boolean showBounds = false;
	boolean collisionTesting = true;

	// Define the images used in the game
	ImageEntity background;
	ImageEntity bulletImage;
	ImageEntity[] bigAsteroids = new ImageEntity[5];
	ImageEntity[] medAsteroids = new ImageEntity[2];
	ImageEntity[] smlAsteroids = new ImageEntity[3];
	ImageEntity[] tnyAsteroids = new ImageEntity[4];
	ImageEntity[] explosions = new ImageEntity[2];
	ImageEntity[] shipImage = new ImageEntity[2];
	
	// Create a random number generator
	Random rand = new Random();

	// Used to make ship temporarily invulnerable
	long collisionTimer = 0;
    
	// Some key input tracking variables
	boolean keyLeft, keyRight, keyUp, keyFire, keyB, keyC;

	/*******************************************************
     * Constructor
     ******************************************************/
	public GalacticWar()
	{
		// Call base Game class' constructor
		super(FRAMERATE, SCREENWIDTH, SCREENHEIGHT);
	}

	/*******************************************************
     * gameStartup event passed by game engine
     ******************************************************/
	void gameStartup()
	{
		// Load the background image
		background = new ImageEntity(this);
		background.load("media/images/bluespace.png");

		// Create the ship sprite-first in the sprite list
		shipImage[0] = new ImageEntity(this);
		shipImage[0].load("media/images/spaceship.png");
		shipImage[1] = new ImageEntity(this);
		shipImage[1].load("media/images/ship_thrust.png");

		AnimatedSprite ship = new AnimatedSprite(this, graphics());
		ship.setSpriteType(SPRITE_SHIP);
		ship.setImage(shipImage[0].getImage());
		ship.setFrameWidth(ship.imageWidth());
		ship.setFrameHeight(ship.imageHeight());
		ship.setPosition(new Point2D(SCREENWIDTH/2, SCREENHEIGHT/2));
		ship.setAlive(true);
		ship.setState(STATE_NORMAL);
		sprites().add(ship);

		// Load the bullet sprite image
		bulletImage = new ImageEntity(this);
		bulletImage.load("media/images/plasmashot.png");

		// Load the explosion sprite image
		explosions[0] = new ImageEntity(this);
		explosions[0].load("media/images/explosion.png");
		explosions[1] = new ImageEntity(this);
		explosions[1].load("media/images/explosion2.png");

		// Load the big asteroid images (5 total)
		for(int n = 0; n < 5; n++)
		{
			bigAsteroids[n] = new ImageEntity(this);
			String fn = "media/images/asteroid" + (n+1) + ".png";
			bigAsteroids[n].load(fn);
		}

		// Load the medium asteroid images (2 total)
		for(int n = 0; n < 2; n++)
		{
			medAsteroids[n] = new ImageEntity(this);
			String fn = "media/images/medium" + (n+1) + ".png";
			medAsteroids[n].load(fn);
		}

		// Load the small asteroid images (3 total)
		for(int n = 0; n < 3; n++)
		{
			smlAsteroids[n] = new ImageEntity(this);
			String fn = "media/images/small" + (n+1) + ".png";
			smlAsteroids[n].load(fn);
		}

		// Load the tiny asteroid images (4 total)
		for(int n = 0; n < 4; n++)
		{
			tnyAsteroids[n] = new ImageEntity(this);
			String fn = "media/images/tiny" + (n+1) + ".png";
			tnyAsteroids[n].load(fn);
		}

		// Create the random asteroid sprites
		for(int n = 0; n < ASTEROIDS; n++)
		{
			createAsteroid();
		}
	}

	/*******************************************************
     * gameTimedUpdate event passed by game engine
     ******************************************************/
	void gameTimedUpdate()
	{
		checkInput();
	}

	/*******************************************************
     * gameRefreshScreen event passed by game engine
     ******************************************************/
	void gameRefreshScreen()
	{
		Graphics2D g2d = graphics();

		// The ship is always the first sprite in the linked list
		AnimatedSprite ship = (AnimatedSprite) sprites().get(0);
		
		// Draw the background
		g2d.drawImage(background.getImage(), 0, 0, SCREENWIDTH-1, SCREENHEIGHT-1, this);

		// Print status information on the screen
		g2d.setColor(Color.WHITE);
        g2d.drawString("FPS: " + frameRate(), 5, 10);
        long x = Math.round(ship.position().X());
        long y = Math.round(ship.position().Y());
        g2d.drawString("Ship: " + x + "," + y , 5, 25);
        g2d.drawString("Move angle: " + Math.round(ship.moveAngle())+90, 5, 40);
        g2d.drawString("Face angle: " +  Math.round(ship.faceAngle()), 5, 55);
        if (ship.state()==STATE_NORMAL)
            g2d.drawString("State: NORMAL", 5, 70);
        else if (ship.state()==STATE_COLLIDED)
            g2d.drawString("State: COLLIDED", 5, 70);
        else if (ship.state()==STATE_EXPLODING)
            g2d.drawString("State: EXPLODING", 5, 70);
        g2d.drawString("Sprites: " + sprites().size(), 5, 120);

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
	}

	/*******************************************************
     * gameShutdown event passed by game engine
     ******************************************************/
	void gameShutdown()
	{
		// Oh well, let the garbage collector have at it..
	}

	/*******************************************************
     * spriteUpdate event passed by game engine
     ******************************************************/
	public void spriteUpdate(AnimatedSprite sprite)
	{
		switch(sprite.spriteType())
		{
			case SPRITE_SHIP:
				warp(sprite);
				break;
			case SPRITE_BULLET:
				warp(sprite);
				break;
			case SPRITE_EXPLOSION:
				if (sprite.currentFrame() == sprite.totalFrames()-1) 
				{
					sprite.setAlive(false);
				}
				break;
			case SPRITE_ASTEROID_BIG:
			case SPRITE_ASTEROID_MEDIUM:
			case SPRITE_ASTEROID_SMALL:
			case SPRITE_ASTEROID_TINY:
				warp(sprite);
				break;
		}
	}

	/*****************************************************
     * spriteDraw event passed by game engine
     * called by the game class after each sprite is drawn
     * to give you a chance to manipulate the sprite
     *****************************************************/
    public void spriteDraw(AnimatedSprite sprite) 
	{
        if (showBounds) 
		{
            if (sprite.collided())
                sprite.drawBounds(Color.RED);
            else
                sprite.drawBounds(Color.BLUE);
        }
    }

    /*****************************************************
     * spriteDying event passed by game engine
     * called after a sprite's age reaches its lifespan
     * at which point it will be killed off, and then removed from
     * the linked list. you can cancel the purging process here.
     *****************************************************/
    public void spriteDying(AnimatedSprite sprite) 
	{

    }

    /*****************************************************
     * spriteCollision event passed by game engine
     *****************************************************/
    public void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2) 
	{
        // Jump out quickly if collisions are off
        if (!collisionTesting) return;

        // Figure out what type of sprite has collided
        switch(spr1.spriteType()) 
		{
        	case SPRITE_BULLET:
				// Did bullet hit an asteroid?
				if (isAsteroid(spr2.spriteType())) 
				{
					spr1.setAlive(false);
					spr2.setAlive(false);
					breakAsteroid(spr2);
				}
				break;
        	case SPRITE_SHIP:
				// Did asteroid crash into the ship?
				if (isAsteroid(spr2.spriteType())) 
				{
					if (spr1.state() == STATE_NORMAL)
					{
						collisionTimer = System.currentTimeMillis();
						spr1.setVelocity(new Point2D(0, 0));
						double x = spr1.position().X() - 10;
						double y = spr1.position().Y() - 10;
						startBigExplosion(new Point2D(x, y));
						spr1.setState(STATE_EXPLODING);
						spr2.setAlive(false);
						breakAsteroid(spr2);
					}
					// Make ship temporarily invulnerable
					else if (spr1.state() == STATE_EXPLODING) 
					{
						if (collisionTimer + 3000 < System.currentTimeMillis()) 
						{
							spr1.setState(STATE_NORMAL);
						}
					}
				}
            	break;
        }
    }

    /*****************************************************
     * gameKeyDown event passed by game engine
     *****************************************************/
    public void gameKeyDown(int keyCode) 
	{
        switch(keyCode) 
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
				break;
			case KeyEvent.VK_B:
				// Toggle bounding rectangles
				showBounds = !showBounds;
				break;
			case KeyEvent.VK_C:
				// Toggle collision testing
				collisionTesting = !collisionTesting;
				break;
        }
    }

    /*****************************************************
     * gameKeyUp event passed by game engine
     *****************************************************/
    public void gameKeyUp(int keyCode) 
	{
        switch(keyCode) 
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
				break;
        }
    }

    /*****************************************************
     * Mouse events passed by game engine
     * the game is not currently using mouse input
     *****************************************************/
    public void gameMouseDown() { }
    public void gameMouseUp() { }
    public void gameMouseMove() { }

    /*****************************************************
     * Break up an asteroid into smaller pieces
     *****************************************************/
    private void breakAsteroid(AnimatedSprite sprite) 
	{
        switch(sprite.spriteType()) 
		{
			case SPRITE_ASTEROID_BIG:
				// Spawn medium asteroids over the old one
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				// Draw big explosion
				startBigExplosion(sprite.position());
				break;
			case SPRITE_ASTEROID_MEDIUM:
				// Spawn small asteroids over the old one
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				// Draw small explosion
				startSmallExplosion(sprite.position());
				break;
			case SPRITE_ASTEROID_SMALL:
				// Spawn tiny asteroids over the old one
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				spawnAsteroid(sprite);
				// Draw small explosion
				startSmallExplosion(sprite.position());
				break;
			case SPRITE_ASTEROID_TINY:
				// Spawn a random powerup
				spawnPowerup(sprite);
				// Draw small explosion
				startSmallExplosion(sprite.position());
				break;
        }
    }

    /*****************************************************
     * Spawn a smaller asteroid based on passed sprite
     *****************************************************/
    private void spawnAsteroid(AnimatedSprite sprite) 
	{
        // Create a new asteroid sprite
        AnimatedSprite ast = new AnimatedSprite(this, graphics());
        ast.setAlive(true);

        // Set pseudo-random position around source sprite
        int w = sprite.getBounds().width;
        int h = sprite.getBounds().height;
        double x = sprite.position().X() + w/2 + rand.nextInt(20)-40;
        double y = sprite.position().Y() + h/2 + rand.nextInt(20)-40;
        ast.setPosition(new Point2D(x,y));

        // Set rotation and direction angles
        ast.setFaceAngle(rand.nextInt(360));
        ast.setMoveAngle(rand.nextInt(360));
        ast.setRotationRate(rand.nextDouble());

        // Set velocity based on movement direction
        double ang = ast.moveAngle() - 90;
        double velx = calcAngleMoveX(ang);
        double vely = calcAngleMoveY(ang);
        ast.setVelocity(new Point2D(velx, vely));

        // Set some size-specific properties
        switch(sprite.spriteType()) 
		{
			case SPRITE_ASTEROID_BIG:
				ast.setSpriteType(SPRITE_ASTEROID_MEDIUM);

				// Pick one of the random asteroid images
				int i = rand.nextInt(2);
				ast.setImage(medAsteroids[i].getImage());
				ast.setFrameWidth(medAsteroids[i].width());
				ast.setFrameHeight(medAsteroids[i].height());

				break;
			case SPRITE_ASTEROID_MEDIUM:
				ast.setSpriteType(SPRITE_ASTEROID_SMALL);

				// Pick one of the random asteroid images
				i = rand.nextInt(3);
				ast.setImage(smlAsteroids[i].getImage());
				ast.setFrameWidth(smlAsteroids[i].width());
				ast.setFrameHeight(smlAsteroids[i].height());
				break;

			case SPRITE_ASTEROID_SMALL:
				ast.setSpriteType(SPRITE_ASTEROID_TINY);

				// Pick one of the random asteroid images
				i = rand.nextInt(4);
				ast.setImage(tnyAsteroids[i].getImage());
				ast.setFrameWidth(tnyAsteroids[i].width());
				ast.setFrameHeight(tnyAsteroids[i].height());
				break;
        }

         // Add the new asteroid to the sprite list
        sprites().add(ast);
    }

    /*****************************************************
     * Create a random powerup at the supplied sprite location
     * (this will be implemented in the next chapter)
     *****************************************************/
    private void spawnPowerup(AnimatedSprite sprite) 
	{

    }

    /*****************************************************
     * Create a random "big" asteroid
     *****************************************************/
    public void createAsteroid() 
	{
        // Create a new asteroid sprite
        AnimatedSprite ast = new AnimatedSprite(this, graphics());
        ast.setAlive(true);
        ast.setSpriteType(SPRITE_ASTEROID_BIG);

        // Pick one of the random asteroid images
        int i = rand.nextInt(5);
        ast.setImage(bigAsteroids[i].getImage());
        ast.setFrameWidth(bigAsteroids[i].width());
        ast.setFrameHeight(bigAsteroids[i].height());

        // Set to a random position on the screen
        int x = rand.nextInt(SCREENWIDTH-128);
        int y = rand.nextInt(SCREENHEIGHT-128);
        ast.setPosition(new Point2D(x, y));

        // Set rotation and direction angles
        ast.setFaceAngle(rand.nextInt(360));
        ast.setMoveAngle(rand.nextInt(360));
        ast.setRotationRate(rand.nextDouble());

        // Set velocity based on movement direction
        double ang = ast.moveAngle() - 90;
        double velx = calcAngleMoveX(ang);
        double vely = calcAngleMoveY(ang);
        ast.setVelocity(new Point2D(velx, vely));

        // Add the new asteroid to the sprite list
        sprites().add(ast);
    }

    /*****************************************************
     * Returns true if passed sprite type is an asteroid type
     *****************************************************/
    private boolean isAsteroid(int spriteType) 
	{
        switch(spriteType) 
		{
			case SPRITE_ASTEROID_BIG:
			case SPRITE_ASTEROID_MEDIUM:
			case SPRITE_ASTEROID_SMALL:
			case SPRITE_ASTEROID_TINY:
				return true;
			default:
				return false;
        }
    }

    /*****************************************************
     * Process keys that have been pressed
     *****************************************************/
    public void checkInput() 
	{
        // The ship is always the first sprite in the linked list
        AnimatedSprite ship = (AnimatedSprite)sprites().get(0);
        if (keyLeft) 
		{
            // Left arrow rotates ship left 5 degrees
            ship.setFaceAngle(ship.faceAngle() - SHIPROTATION);
            if (ship.faceAngle() < 0)
                ship.setFaceAngle(360 - SHIPROTATION);

        } 
		else if (keyRight) 
		{
            // Right arrow rotates ship right 5 degrees
            ship.setFaceAngle(ship.faceAngle() + SHIPROTATION);
            if (ship.faceAngle() > 360)
                ship.setFaceAngle(SHIPROTATION);
        }

        if (keyUp) 
		{
            // Up arrow applies thrust to ship
            ship.setImage(shipImage[1].getImage());
            applyThrust();
        }
        else
            // Set ship image to normal non-thrust image
            ship.setImage(shipImage[0].getImage());
    }

    /*****************************************************
     * Increase the thrust of the ship based on facing angle
     *****************************************************/
    public void applyThrust() 
	{
        // The ship is always the first sprite in the linked list
        AnimatedSprite ship = (AnimatedSprite)sprites().get(0);

        // Up arrow adds thrust to ship (1/10 normal speed)
        ship.setMoveAngle(ship.faceAngle() - 90);

        // Calculate the X and Y velocity based on angle
        double velx = ship.velocity().X();
        velx += calcAngleMoveX(ship.moveAngle()) * ACCELERATION;
        if (velx < -10) velx = -10;
        else if (velx > 10) velx = 10;
        double vely = ship.velocity().Y();
        vely += calcAngleMoveY(ship.moveAngle()) * ACCELERATION;
        if (vely < -10) vely = -10;
        else if (vely > 10) vely = 10;
        ship.setVelocity(new Point2D(velx, vely));
    }

    /*****************************************************
     * Fire a bullet from the ship's position and orientation
     *****************************************************/
    public void fireBullet() 
	{
        // The ship is always the first sprite in the linked list
        AnimatedSprite ship = (AnimatedSprite)sprites().get(0);

        // Create the new bullet sprite
        AnimatedSprite bullet = new AnimatedSprite(this,graphics());
        bullet.setImage(bulletImage.getImage());
        bullet.setFrameWidth(bulletImage.width());
        bullet.setFrameHeight(bulletImage.height());
        bullet.setSpriteType(SPRITE_BULLET);
        bullet.setAlive(true);
        bullet.setLifespan(200);
        bullet.setFaceAngle(ship.faceAngle());
        bullet.setMoveAngle(ship.faceAngle() - 90);

        // Set the bullet's starting position
        double x = ship.center().X() - bullet.imageWidth()/2;
        double y = ship.center().Y() - bullet.imageHeight()/2;
        bullet.setPosition(new Point2D(x,y));

        // Set the bullet's velocity
        double angle = bullet.moveAngle();
        double svx = calcAngleMoveX(angle) * BULLET_SPEED;
        double svy = calcAngleMoveY(angle) * BULLET_SPEED;
        bullet.setVelocity(new Point2D(svx, svy));

        // Add bullet to the sprite list
        sprites().add(bullet);
    }

    /*****************************************************
     * Launch a big explosion at the passed location
     *****************************************************/
    public void startBigExplosion(Point2D point) 
	{
        // Create a new explosion at the passed location
        AnimatedSprite expl = new AnimatedSprite(this,graphics());
        expl.setSpriteType(SPRITE_EXPLOSION);
        expl.setAlive(true);
        expl.setAnimImage(explosions[0].getImage());
        expl.setTotalFrames(16);
        expl.setColumns(4);
        expl.setFrameWidth(96);
        expl.setFrameHeight(96);
        expl.setFrameDelay(2);
        expl.setPosition(point);

        // Add the new explosion to the sprite list
        sprites().add(expl);
    }

    /*****************************************************
     * Launch a small explosion at the passed location
     *****************************************************/
    public void startSmallExplosion(Point2D point) 
	{
        // Create a new explosion at the passed location
        AnimatedSprite expl = new AnimatedSprite(this,graphics());
        expl.setSpriteType(SPRITE_EXPLOSION);
        expl.setAlive(true);
        expl.setAnimImage(explosions[1].getImage());
        expl.setTotalFrames(8);
        expl.setColumns(4);
        expl.setFrameWidth(40);
        expl.setFrameHeight(40);
        expl.setFrameDelay(2);
        expl.setPosition(point);

        // Add the new explosion to the sprite list
        sprites().add(expl);
    }

    /*****************************************************
     * Cause sprite to warp around the edges of the screen
     *****************************************************/
    public void warp(AnimatedSprite spr) 
	{
        // Create some shortcut variables
        int w = spr.frameWidth()-1;
        int h = spr.frameHeight()-1;

        // Wrap the sprite around the screen edges
        if (spr.position().X() < 0-w)
            spr.position().setX(SCREENWIDTH);
        else if (spr.position().X() > SCREENWIDTH)
            spr.position().setX(0-w);
        if (spr.position().Y() < 0-h)
            spr.position().setY(SCREENHEIGHT);
        else if (spr.position().Y() > SCREENHEIGHT)
            spr.position().setY(0-h);
    }
}