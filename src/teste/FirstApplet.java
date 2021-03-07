package teste;
import java.awt.*;
import java.applet.*;

public class FirstApplet extends Applet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vehicle car;
	
	public void init() 
	{
        setSize(640, 480);
        setBackground(Color.yellow);
        
        car = new Vehicle();
        car.setMake("Ford");
    }
	
	public void paint(Graphics g)
	{	
		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		g.drawString("This is my first Java Applet!", 20, 30);
		g.drawLine(5, 5, 6, 30);
		g.drawString("Byte: " + Byte.MIN_VALUE + " to " + Byte.MIN_VALUE, 20, 45);
		g.drawString("Short: " + Short.MIN_VALUE + " to " + Short.MAX_VALUE, 20, 60);
		
		String name = "Victor Shirasuna";
		g.drawString("tamanho: " + name.length(), 20, 200);
		
		g.drawString("Car make: " + car.getMake(), 20, 75);
	}
}
