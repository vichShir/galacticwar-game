/*******************************************************
 * DrawImage program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.net.*;

public class DrawImage extends Applet
{
    // Image variable
    private Image image;

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
        image = getImage(getURL("images/castle.png"));
    }

    // Applet paint event
    public void paint(Graphics g)
    {
        // Create an instance of Graphics2D
        Graphics2D g2d = (Graphics2D) g;

        // Fill the background with black
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getSize().width, getSize().height);

        // Draw the image
        g2d.drawImage(image, 0, 0, this);
    }
}