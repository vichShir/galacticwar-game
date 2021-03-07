package test.random.shapes;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.geom.*;

public class RandomPolygons extends Applet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private int[] xPoints = {   0, -10, -7,  7, 10 };
//	private int[] yPoints = { -10,  -2, 10, 10, -2 };
	
	private int[] xPoints = { -20, -13, 0, 20, 22, 20, 12, 2, -10, -22, -16 };
	private int[] yPoints = { 20, 23, 17, 20, 16, -20, -22, -14, -17, -20, -5 };
	

	private Polygon poly;
	
	@Override
	public void init()
	{
		poly = new Polygon(xPoints, yPoints, xPoints.length);
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform identity = new AffineTransform();
		
		Random rand = new Random();
		
		int width = getSize().width;
		int height = getSize().height;
		
		for(int n = 0; n < 300; n++)
		{
			g2d.setTransform(identity);
			
			g2d.translate(rand.nextInt() % width, rand.nextInt() % height);
			g2d.rotate(Math.toRadians(360 * rand.nextDouble()));
			g2d.scale(5 * rand.nextDouble(), 5 * rand.nextDouble());
			
			g2d.setColor(new Color(rand.nextInt()));
			g2d.fill(poly);
		}
	}
}
