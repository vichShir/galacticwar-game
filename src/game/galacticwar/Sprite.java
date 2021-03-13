/*******************************************************
 * Sprite class
 *******************************************************/

package game.galacticwar;

import java.awt.*;
import java.applet.*;

public class Sprite extends Object
{
	private ImageEntity entity;
	protected Point2D pos;
	protected Point2D vel;
	protected double rotRate;
	protected int currentState;
	
	//construtor
	public Sprite(Applet a, Graphics2D g2d)
	{
		entity = new ImageEntity(a);
		entity.setGraphics(g2d);
		entity.setAlive(false);
		pos = new Point2D(0, 0);
		vel = new Point2D(0, 0);
		rotRate = 0.0;
		currentState = 0;
	}
	
	//load bitmap file
	public void load(String filename)
	{
		entity.load(filename);
	}
	
	//perform affine transformations
	public void transform()
	{
		entity.setX(pos.getX());
		entity.setY(pos.getY());
		entity.transform();
	}
	
	//draw the image
	public void draw()
	{
		entity.g2d.drawImage(entity.getImage(), entity.at, entity.applet);
	}
	
	//draw bounding rectangle around sprite
	public void drawBounds(Color c)
	{
		entity.g2d.setColor(c);
		entity.g2d.draw(getBounds());
	}
	
	//update the position based on velocity
	public void updatePosition()
	{
		pos.setX(pos.getX() + vel.getX());
		pos.setY(pos.getY() + vel.getY());
	}
	
	//methods related to automatic rotation factor
	public double rotationRate() { return rotRate; }
	public void setRotationRate(double rate) { rotRate = rate; }
	public void updateRotation()
	{
		setFaceAngle(faceAngle() + rotRate);
		if(faceAngle() < 0)
			setFaceAngle(360 - rotRate);
		else if(faceAngle() > 360)
			setFaceAngle(rotRate);
	}
	
	//generic sprite state variable (alive, dead, collided, etc.)
	public int state() { return currentState; }
	public void setState(int state) { currentState = state; }
	
	//returns a boundinf rectangle
	public Rectangle getBounds() { return entity.getBounds(); }
	
	//sprite position
	public Point2D position() { return pos; }
	public void setPosition(Point2D pos) { this.pos = pos; }
	
	//sprite movement velocity
	public Point2D velocity() { return vel; }
	public void setVelocity(Point2D vel) { this.vel = vel; }
	
	//returns the center of the sprite as a Point2D
	public Point2D center()
	{
		return(new Point2D(entity.getCenterX(), entity.getCenterY()));
	}
	
	//generic variable for selectively using sprites
	public boolean alive() { return entity.isAlive(); }
	public void setAlive(boolean alive) { entity.setAlive(alive); }
	
	//face angle indicates which direction sprite is facing
	public double faceAngle() { return entity.getFaceAngle(); }
	public void setFaceAngle(double angle) 
	{
		entity.setFaceAngle(angle);
	}
	public void setFaceAngle(float angle)
	{
		entity.setFaceAngle(angle);
	}
	public void setFaceAngle(int angle)
	{
		entity.setFaceAngle(angle);
	}
	
	//move angle indicates direction sprite is moving
	public double moveAngle() { return entity.getMoveAngle(); }
	public void setMoveAngle(double angle)
	{
		entity.setMoveAngle(angle);
	}
	public void setMoveAngle(float angle)
	{
		entity.setMoveAngle(angle);
	}
	public void setMoveAngle(int angle)
	{
		entity.setMoveAngle(angle);
	}

	//returns the source image width/height
	public int imageWidth() { return entity.width(); }
	public int imageHeight() { return entity.height(); }
	
	//check for collision with a rectangular shape
	public boolean collidesWith(Rectangle rect)
	{
		return(rect.intersects(getBounds()));
	}
	
	//check for collision with another sprite
	public boolean collidesWith(Sprite sprite)
	{
		return(getBounds().intersects(sprite.getBounds()));
	}
	
	//check for collision with a point
	public boolean collidesWith(Point2D point)
	{
		return(getBounds().contains(point.getX(), point.getY()));
	}
	
	public Applet applet() { return entity.applet; }
	public Graphics2D graphics() { return entity.g2d; }
	public Image image() { return entity.image; }
	public void setImage(Image image) { entity.setImage(image); }
}