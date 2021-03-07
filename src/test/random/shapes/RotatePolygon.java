package test.random.shapes;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;

public class RotatePolygon extends Applet implements KeyListener, MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int[] xPoints = {   0, -10, -7,  7, 10 };
	private int[] yPoints = { -10,  -2, 10, 10, -2 };
	
	private Polygon poly;
	
	int rotation = 0;
	
	@Override
	public void init()
	{
		poly = new Polygon(xPoints, yPoints, xPoints.length);
		
		addKeyListener(this);
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform identity = new AffineTransform();
		
		int width = getSize().width;
		int height = getSize().height;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setTransform(identity);
		
		g2d.translate(width / 2, height / 2);
		g2d.scale(10, 10);
		g2d.rotate(Math.toRadians(rotation));
		
		g2d.setColor(Color.RED);
		g2d.fill(poly);
		g2d.setColor(Color.BLUE);
		g2d.draw(poly);
	}
	
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
