/*******************************************************
 * Bullet class derives from BaseVectorShape
 *******************************************************/

import java.awt.*;

/*
 * Bullet class - polygonal shape of a bullet
 */

public class Bullet extends VectorEntity 
{
	// Bounding rectangle
	public Rectangle getBounds()
	{
		Rectangle r;
		r = new Rectangle((int)getX(), (int)getY(), 1, 1);
		return r;
	}
	
	// Default constructor
	Bullet()
	{
		// Create the bullet shape
		setShape(new Rectangle(0, 0, 1, 1));
		setAlive(false);
	}
}
