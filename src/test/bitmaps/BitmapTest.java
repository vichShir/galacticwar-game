/*******************************************************
 * BitmapTest Program
 *******************************************************/

package test.bitmaps;

import java.awt.*;
import java.net.URL;
import java.applet.*;
import java.util.*;

public class BitmapTest extends Applet implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	int screenWidth = 640;
	int screenHeight = 480;
	
	Image image;
	Thread gameLoop;
	Random rand = new Random();
	
	private URL getURL(String filename)
	{
		URL url = null;
		try
		{
			url = this.getClass().getResource(filename);
		}
		catch(Exception e) { }
				
		return url;
	}
	
	//applet init event
	@Override
	public void init()
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		image = tk.getImage(getURL("spaceship2.png"));
	}
	
	@Override
	public void start()
	{
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	@Override
	public void stop()
	{
		gameLoop = null;
	}
	
	@Override
	public void run()
	{
		Thread t = Thread.currentThread();
		while(t == gameLoop)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	@Override
	public void update(Graphics g)
	{
		paint(g);
	}
	
	//applet paint event
	@Override
	public void paint(Graphics g)
	{
		//create an instance of Graphics2D
		Graphics2D g2d = (Graphics2D)g;
		int width = screenWidth - image.getWidth(this) - 1;
		int height = screenHeight - image.getHeight(this) - 1;
		g2d.drawImage(image, rand.nextInt(width), rand.nextInt(height), this);
	}
}
