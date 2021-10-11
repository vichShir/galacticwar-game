/*******************************************************
 * BitmapTest program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.net.*;

public class BitmapTest extends Applet
{
    final int screenWidth = 640;
    final int screenHeight = 480;

    Image image;
    Thread gameloop;
    Random rand = new Random();

    private URL getURL(String filename)
    {
        URL url = null;
        try
        {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }
        return url; 
    }

    // Applet init event
    public void init()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(getURL("images/asteroid1.png"));
    }

    public void start()
    {
        gameloop = new Thread();
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
            repaint();
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    // Applet paint event
    public void paint(Graphics g)
    {
        // Create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        int width = screenWidth - image.getWidth(this) - 1;
        int height = screenHeight - image.getHeight(this) - 1;
        g2d.drawImage(image, rand.nextInt(width), rand.nextInt(height), this);
    }
}
