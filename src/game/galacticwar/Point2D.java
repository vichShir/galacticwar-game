/*******************************************************
 * Point2D Program
 *******************************************************/

package game.galacticwar;

public class Point2D extends Object
{
	private double x, y;
	
	//int constructor
	public Point2D(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	//float constructor
	public Point2D(float x, float y)
	{
		setX(x);
		setY(y);
	}
	
	//double constructor
	public Point2D(double x, double y)
	{
		setX(x);
		setY(y);
	}
	
	//X property
	public double getX() { return x; }
	
	public void setX(int x) { this.x = (double)x; }
	public void setX(float x) { this.x = (double)x; }
	public void setX(double x) { this.x = x; }
	
	//Y property
	public double getY() { return y; }
	
	public void setY(int y) { this.y = (double)y; }
	public void setY(float y) { this.y = (double)y; }
	public void setY(double y) { this.y = y; }
}
