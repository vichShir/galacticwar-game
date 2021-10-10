/*******************************************************
 * RandomImages program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;
import java.net.*;

public class RandomImages extends Applet
{
    // Image variable
    private Image image;

    // Identify transformation
    AffineTransform identify = new AffineTransform();

    private URL getURL(String filename)
    {
        URL url = null;
        try
        {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }
        System.out.println(url);
        return url; 
    }

    // Applet init event
    public void init()
    {
        image = getImage(getURL("spaceship.png"));
    }

    // Applet paint event
    public void paint(Graphics g)
    {
        // Create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // Working transform object
        AffineTransform trans = new AffineTransform();

        // Random number generator
        Random rand = new Random();

        // Applet window width/height
        int width = getSize().width;
        int height = getSize().height;

        // Fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        // Draw the image multiple times
        for(int n = 0; n < 50; n++)
        {
            trans.setTransform(identify);
            // Move, rotate, scale the image randomly
            trans.translate(rand.nextInt()%width, rand.nextInt()%height);
            trans.rotate(Math.toRadians(360 * rand.nextDouble()));
            double scale = rand.nextDouble() + 1;
            trans.scale(scale, scale);
            // Draw the image
            g2d.drawImage(image, trans, this);
        }
        
    }
}
