/*******************************************************
 * RandomImages Program
 *******************************************************/

package test.bitmaps;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;

public class RandomImages extends Applet
{
	private static final long serialVersionUID = 1L;
	
	//image variable
	private Image image;
	
	//identity transformation
	AffineTransform identity = new AffineTransform();

	//applet init event
	@Override
	public void init()
	{
		image = getImage(getCodeBase(), "resources/spaceship.png");
	}
	
	//applet paint event
	@Override
	public void paint(Graphics g)
	{
		//create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D)g;
		
		//working transform object
		AffineTransform trans = new AffineTransform();
		
		//random number generator
		Random rand = new Random();
		
		//applet window width/height
		int width = getSize().width;
		int height = getSize().height;
		
		//fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		//draw the image multiple times
		for(int n = 0; n < 50; n++)
		{
			trans.setTransform(identity);
			
			//move, rotate, scale the image randomly
			trans.translate(rand.nextInt() % width, rand.nextInt() % height);
			trans.rotate(Math.toRadians(360 * rand.nextDouble()));
			double scale = rand.nextDouble() + 1;
			trans.scale(scale, scale);
			
			//draw the image
			g2d.drawImage(image, trans, this);
		}
	}
}
