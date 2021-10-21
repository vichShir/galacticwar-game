/*******************************************************
 * Applet Game Framework class
 *******************************************************/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.System;

abstract class Game extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener
{
    // The main thread becomes the game loop
	Thread gameloop;

    // Internal list of sprites
    private LinkedList<AnimatedSprite> _sprites;
    public LinkedList<AnimatedSprite> sprites() { return _sprites; }

    // Screen and double buffer related variables
	private BufferedImage backbuffer;
	private Graphics2D g2d;
    private int screenWidth, screenHeight;

    // Keep track of mouse position and buttons
    private Point2D mousePos = new Point2D(0, 0);
    private boolean mouseButtons[] = new boolean[4];

    // Frame rate counters and other timing variables
	private int _frameCount = 0;
    private int _frameRate = 0;
    private int desiredRate;
	private long startTime = System.currentTimeMillis();

    // Declare the game event methods that sub-class must implement
    abstract void gameStartup();
    abstract void gameTimedUpdate();
    abstract void gameRefreshScreen();
    abstract void gameShutdown();
    abstract void gameKeyDown(int keyCode);
    abstract void gameKeyUp(int keyCode);
    abstract void gameMouseDown();
    abstract void gameMouseUp();
    abstract void gameMouseMove();
    abstract void spriteUpdate(AnimatedSprite sprite);
    abstract void spriteDraw(AnimatedSprite sprite);
    abstract void spriteDying(AnimatedSprite sprite);
    abstract void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2);

    /*******************************************************
     * Constructor
     *******************************************************/
    public Game(int frameRate, int width, int height)
    {
        desiredRate = frameRate;
        screenWidth = width;
        screenHeight = height;
    }

    // Return g2d object so sub-class can draw things
    public Graphics2D graphics() { return g2d; }

    // Current frame rate
    public int frameRate() { return _frameRate; }

    // Mouse buttons and movement
    public boolean mouseButton(int btn) { return mouseButtons[btn]; }
    public Point2D mousePosition() { return mousePos; }

    /*******************************************************
     * Applet init event method
     *******************************************************/
    public void init()
    {
        // Create the back buffer and drawing surface
		backbuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();

        // Create the internal sprite list
        _sprites = new LinkedList<AnimatedSprite>();

        // Start the input listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // This method implemented by sub-class
        gameStartup();
    }

    /*******************************************************
     * Applet update event method
     *******************************************************/
    public void update(Graphics g)
    {
        // Calculate frame rate
		_frameCount++;
		if(System.currentTimeMillis() > startTime + 1000)
		{
			startTime = System.currentTimeMillis();
			_frameRate = _frameCount;
			_frameCount = 0;

            // Once every second all dead sprites are deleted
            purgeSprites();
		}

        // This method implemented by sub-class
        gameRefreshScreen();

        // Draw the internal list of sprites
        drawSprites();

        // Redraw the screen
        paint(g);
    }

    /*******************************************************
     * Applet window paint event method
     *******************************************************/
    public void paint(Graphics g)
    {
        g.drawImage(backbuffer, 0, 0, this);
    }

    /*******************************************************
     * Thread start event - start the game loop running
     *******************************************************/
    public void start()
    {
        gameloop = new Thread(this);
        gameloop.start();
    }

    /*******************************************************
     * Thread run event(game loop)
     ******************************************************/
    public void run()
    {
        // Acquire the current thread
		Thread t = Thread.currentThread();
		
		// Process the main game loop thread
		while(t == gameloop)
		{
			try
			{
				// Set a consistent frame rate
				Thread.sleep(1000 / desiredRate);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}

            // Update the internal list of sprites
            updateSprites();
            testCollisions();

			// Allow main game to update if needed
			gameTimedUpdate();

            // Refresh the screen
			repaint();
		}
    }

    /*******************************************************
     * Thread stop event
     ******************************************************/
    public void stop()
    {
        // Kill the gameloop thread
		gameloop = null;

        // This method implemented by sub-class
        gameShutdown();
    }

    /*******************************************************
     * Key listener events
     ******************************************************/
    public void keyTyped(KeyEvent k) { }
    public void keyPressed(KeyEvent k)
    {
        gameKeyDown(k.getKeyCode());
    }
    public void keyReleased(KeyEvent k)
    {
        gameKeyUp(k.getKeyCode());
    }

    /*******************************************************
     * checkButtons stores the state of the mouse buttons
     ******************************************************/
    private void checkButtons(MouseEvent e)
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                mouseButtons[1] = true;
                mouseButtons[2] = false;
                mouseButtons[3] = false;
                break;
            case MouseEvent.BUTTON2:
                mouseButtons[1] = false;
                mouseButtons[2] = true;
                mouseButtons[3] = false;
                break;
            case MouseEvent.BUTTON3:
                mouseButtons[1] = false;
                mouseButtons[2] = false;
                mouseButtons[3] = true;
                break;
            default:
        }
    }

    /*******************************************************
     * Mouse listener events
     ******************************************************/
    public void mousePressed(MouseEvent e)
    {
        checkButtons(e);
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseDown();
    }

    public void mouseReleased(MouseEvent e)
    {
        checkButtons(e);
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseUp();
    }

    public void mouseMoved(MouseEvent e)
    {
        checkButtons(e);
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseMove();
    }

    public void mouseDragged(MouseEvent e)
    {
        checkButtons(e);
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseDown();
        gameMouseMove();
    }

    public void mouseEntered(MouseEvent e)
    {
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseMove();
    }

    public void mouseExited(MouseEvent e)
    {
        mousePos.setX(e.getX());
        mousePos.setY(e.getY());
        gameMouseMove();
    }

    // This event is not needed
    public void mouseClicked(MouseEvent e) { }

    /*******************************************************
     * X and Y velocity calculation functions
     ******************************************************/
    protected double calcAngleMoveX(double angle)
	{
		return (double)(Math.cos(angle * Math.PI / 180));
	}
	
	protected double calcAngleMoveY(double angle)
	{
		return (double) (Math.sin(angle * Math.PI / 180));
	}

    /*******************************************************
     * Update the sprite list from the game loop thread
     ******************************************************/
    protected void updateSprites()
    {
        for(int n = 0; n < _sprites.size(); n++)
        {
            AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
            if(spr.alive())
            {
                spr.updatePosition();
                spr.updateRotation();
                spr.updateAnimation();
                spriteUpdate(spr);
                spr.updateLifetime();
                if(!spr.alive())
                {
                    spriteDying(spr);
                }
            }
        }
    }

    /*******************************************************
     * Perform collision testing of all active sprites
     ******************************************************/
    protected void testCollisions()
    {
        // Iterate through the sprite list, test each sprite against every other sprite in the list
        for(int first = 0; first < _sprites.size(); first++)
        {
            // Get the first to test for collision
            AnimatedSprite spr1 = (AnimatedSprite) _sprites.get(first);
            if(spr1.alive())
            {
                // Look through all sprites again for collisions
                for(int second = 0; second < _sprites.size(); second++)
                {
                    if(first != second)
                    {
                        // Get the seconds sprite to test for collision
                        AnimatedSprite spr2 = (AnimatedSprite) _sprites.get(second);
                        if(spr2.alive())
                        {
                            if(spr2.collidesWith(spr1))
                            {
                                spriteCollision(spr1, spr2);
                                break;
                            }
                            else
                                spr1.setCollided(false);
                        }
                    }
                }
            }
        }
    }

    /*******************************************************
     * Draw all active sprites in the sprite list
     * Sprites lower in the list are drawn on top
     ******************************************************/
    protected void drawSprites()
    {
        // Draw sprites in reverse order (reverse priority)
        for(int n = 0; n < _sprites.size(); n++)
        {
            AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
            if(spr.alive())
            {
                spr.updateFrame();
                spr.transform();
                spr.draw();
                spriteDraw(spr); // Notify player
            }
        }
    }

    /*******************************************************
     * Once every second during the frame update, this method
     * is called to remove all dead sprites from the linked list
     ******************************************************/
    private void purgeSprites()
    {
        for(int n = 0; n < _sprites.size(); n++)
        {
            AnimatedSprite spr = (AnimatedSprite) _sprites.get(n);
            if(!spr.alive())
            {
                _sprites.remove(n);
            }
        }
    }
}
