/*******************************************************
 * ThreadedLoop program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;

public class ThreadedLoop extends Applet implements Runnable
{
    // Random number generator
    Random rand = new Random();

    // The main thred
    Thread thread;

    // Count the number of rectangles drawn
    long count = 0;

    // Applet init event
    public void init()
    {
        // Not needed this time
    }

    // Applet start event
    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }

    // Thread run event
    public void run()
    {
        long start = System.currentTimeMillis();

        // Acquire the current thread
        Thread current = Thread.currentThread();

        // Here's the new game loop
        while(current == thread)
        {
            try
            {
                Thread.sleep(0);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

            // Draw something
            repaint();

            // Figure out how fast it's running
            if(start + 1000 < System.currentTimeMillis())
            {
                start = System.currentTimeMillis();
                showStatus("Rectangles per second: " + count);
                count = 0;
            }
        }
    }

    // Applet stop event
    public void stop()
    {
        thread = null;
    }

    // Applet update event
    public void update(Graphics g)
    {
        paint(g);
    }

    // Applet paint event
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        // Create a random rectangle
        int w = rand.nextInt(100);
        int h = rand.nextInt(100);
        int x = rand.nextInt(getSize().width - w);
        int y = rand.nextInt(getSize().height - h);
        Rectangle rect = new Rectangle(x, y, w, h);

        // Generate a random color
        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);
        g2d.setColor(new Color(red, green, blue));

        // Draw the rectangle
        g2d.fill(rect);

        // Add another to the counter
        count++;
    }
}
