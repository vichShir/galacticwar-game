/*******************************************************
 * AnimationClass program
 *******************************************************/

import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.applet.*;
import java.util.*;

public class AnimationClass extends Applet implements Runnable
{
    static int SCREENWIDTH = 640;
    static int SCREENHEIGHT = 480;
    Thread gameloop;
    Random rand = new Random();

    // Double buffer objects
    BufferedImage backbuffer;
    Graphics2D g2d;

    // Background image
    Image background;

    // Sprite variables
    AnimatedSprite ball;

    int ballX = 300, ballY = 200;
    int speedX, speedY;

    // Animation variables
    int currentFrame = 0;
    int totalFrames = 64;
    int animationDirection = 1;
    int frameCount = 0;
    int frameDelay = 1;

    private URL getURL(String filename)
    {
        URL url = null;
        try
        {
            url = this.getClass().getResource(filename);
        }
        catch(Exception e) { }
        return url;
    }

    public void init()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();

        // Create the back buffer for smooth graphics
        backbuffer = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = backbuffer.createGraphics();

        // Load the background image
        background = tk.getImage(getURL("images/woodgrain.png"));

        // Load the ball animation strip
        ball = new AnimatedSprite(this, g2d);
        ball.load("images/xball.png", 8, 8, 64, 64);
        ball.setPosition(new Point2D(300, 200));
        ball.setFrameDelay(1);
        ball.setVelocity(new Point2D(1, 1));
        ball.setRotationRate(1.0);

        speedX = rand.nextInt(6)+1;
        speedY = rand.nextInt(6)+1;
    }

    public void start()
    {
        gameloop = new Thread(this);
        gameloop.start();
    }

    public void stop()
    {
        gameloop = null;
    }

    public void run()
    {
        Thread t = Thread.currentThread();
        while(t == gameloop)
        {
            try
            {
                Thread.sleep(20);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            gameUpdate();
            repaint();
        }
    }

    public void gameUpdate()
    {
        ball.updateAnimation();

        // Update the ball position
        ballX += speedX;
        if((ballX < 0) || (ballX > SCREENWIDTH - 64))
        {
            speedX *= -1;
            ballX += speedX;
        }
        ballY += speedY;
        if((ballY < 0) || (ballY > SCREENHEIGHT - 64))
        {
            speedY *= -1;
            ballY += speedY;
        }
    }

    public void update(Graphics g)
    {
        // Draw the background
        g2d.drawImage(background, 0, 0, SCREENWIDTH - 1, SCREENHEIGHT - 1, this);

        // Draw the current frame of animation
        ball.pos = new Point2D(ballX, ballY);
        ball.updateAnimation();
        ball.draw();

        g2d.setColor(Color.BLACK);
        g2d.drawString("Position: " + ballX + "," + ballY, 5, 10);
        g2d.drawString("Velocity: " + speedX + "," + speedY, 5, 25);
        g2d.drawString("Animation: " + currentFrame, 5, 40);

        paint(g);
    }

    public void paint(Graphics g)
    {
        // Draw the back buffer to the screen
        g.drawImage(backbuffer, 0, 0, this);
    }
}