/*******************************************************
 * RotatePolygon Program
 *******************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;

public class RotatePolygon extends Applet implements KeyListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private int[] xPoints = {   0, -10, -7,  7, 10 };
	private int[] yPoints = { -10,  -2, 10, 10, -2 };
	
	//here's the shape used for drawing
	private Polygon poly;
	
	//polygon rotation variable
	int rotation = 0;
	
	//applet init event
	@Override
	public void init()
	{
		//create the polygon
		poly = new Polygon(xPoints, yPoints, xPoints.length);
		
		//initialize the listeners
		addKeyListener(this);
		addMouseListener(this);
	}
	
	//applet paint event
	@Override
	public void paint(Graphics g)
	{
		//create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D)g;
		
		//save the identity transform
		AffineTransform identity = new AffineTransform();
		
		//save the window width/height
		int width = getSize().width;
		int height = getSize().height;
		
		//fill the background with black
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setTransform(identity);
		
		//move, rotate, and scale the shape randomly
		g2d.translate(width / 2, height / 2);
		g2d.scale(10, 10);
		g2d.rotate(Math.toRadians(rotation));
		
		//draw the shape with random color
		g2d.setColor(Color.RED);
		g2d.fill(poly);
		g2d.setColor(Color.BLUE);
		g2d.draw(poly);
	}
	
	//handle keyboard events
	@Override
	public void keyReleased(KeyEvent k) { }
	@Override
	public void keyTyped(KeyEvent k) { }
	@Override
	public void keyPressed(KeyEvent k) 
	{ 
		switch (k.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			rotation--;
			if(rotation < 0) rotation = 359;
			repaint();
			break;
		case KeyEvent.VK_RIGHT:
			rotation++;
			if(rotation > 360) rotation = 0;
			repaint();
			break;
		}
	}
	
	//handle mouse envents
	@Override
	public void mouseEntered(MouseEvent m) { }
	@Override
	public void mouseExited(MouseEvent m) { }
	@Override
	public void mouseReleased(MouseEvent m) { }
	@Override
	public void mouseClicked(MouseEvent m) { }
	@Override
	public void mousePressed(MouseEvent m) 
	{ 
		switch (m.getButton()) {
		case MouseEvent.BUTTON1:
			rotation--;
			if(rotation < 0) rotation = 359;
			repaint();
			break;
		case MouseEvent.BUTTON3:
			rotation++;
			if(rotation > 360) rotation = 0;
			repaint();
			break;
		}
	}
}
