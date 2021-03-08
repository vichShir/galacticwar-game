/*******************************************************
 * DrawImage Program
 *******************************************************/

package test.bitmaps;

import java.awt.*;
import java.applet.*;
import java.net.*;

public class DrawImage extends Applet 
{
	private static final long serialVersionUID = 1L;
	
	//image variable
	private Image image;
	
	private URL getURL(String filename)
	{
		URL url = null;
		try
		{
			url = new URL(filename);
		}
		catch(Exception e) { }
				
		return url;
	}

	//applet init event
	@Override
	public void init()
	{
		try 
		{
			image = getImage(getURL("file:/C:/Users/victo/eclipse-workspace/GamesJava/bin/resources/castle.png"));
		}
		catch(Exception e)
		{
			image = getImage(getCodeBase(), "resources/castle.png");
		}
	}
	
	//applet paint event
	@Override
	public void paint(Graphics g)
	{
		//create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D)g;
		
		//fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		
		//draw the image
		g2d.drawImage(image, 0, 0, this);
	}
}
