/*******************************************************
 * RandomPolygons Program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;

public class RandomPolygons extends Applet 
{
	private static final long serialVersionUID = 1L;
	
//	private int[] xPoints = {   0, -10, -7,  7, 10 };
//	private int[] yPoints = { -10,  -2, 10, 10, -2 };
	
	private int[] xPoints = { -20, -13, 0, 20, 22, 20, 12, 2, -10, -22, -16 };
	private int[] yPoints = { 20, 23, 17, 20, 16, -20, -22, -14, -17, -20, -5 };
	
	//here's the shape used for drawing
	private Polygon poly;
	
	//applet init event
	@Override
	public void init()
	{
		poly = new Polygon(xPoints, yPoints, xPoints.length);
	}
	
	//applet paint event
	@Override
	public void paint(Graphics g)
	{
		//create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D)g;
		
		//save the identity transform
		AffineTransform identity = new AffineTransform();
		
		//create a random number generator
		Random rand = new Random();
		
		//save the window width/height
		int width = getSize().width;
		int height = getSize().height;
		
		//fill the banckground with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		for(int n = 0; n < 300; n++)
		{
			//reset Graphics2D to the identity transform
			g2d.setTransform(identity);
			
			//move, rotate, and scale the shape randomly
			g2d.translate(rand.nextInt() % width, rand.nextInt() % height);
			g2d.rotate(Math.toRadians(360 * rand.nextDouble()));
			g2d.scale(5 * rand.nextDouble(), 5 * rand.nextDouble());
			
			//draw the shape with a random color
			g2d.setColor(new Color(rand.nextInt()));
			g2d.fill(poly);
		}
	}
}
