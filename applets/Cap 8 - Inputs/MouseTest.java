/*******************************************************
 * MouseTest program
 *******************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class MouseTest extends Applet implements MouseListener, MouseMotionListener 
{
    // Declaro some mouse event variables
    int clickx, clicky;
    int pressx, pressy;
    int releasex, releasey;
    int enterx, entery;
    int exitx, exity;
    int dragx, dragy;
    int movex, movey;
    int mousebutton;

    // Initialize the applet
    public void init()
    {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Redraw the applet window
    public void paint(Graphics g)
    {
        g.drawString("Mouse clicked " + mousebutton + " at " + clickx + "," + clicky, 10, 10);
        g.drawString("Mouse entered at " + enterx + "," + entery, 10, 25);
        g.drawString("Mouse exited at " + exitx + "," + exity, 10, 40);
        g.drawString("Mouse pressed " + mousebutton + " at " + pressx + "," + pressy, 10, 55);
        g.drawString("Mouse released " + mousebutton + "at " + releasex + "," + releasey, 10, 70);
        g.drawString("Mouse dragged at " + dragx + "," + dragy, 10, 85);
        g.drawString("Mouse moved at " + movex + "," + movey, 10, 100);
    }

    // Custom method called by mouse events to report button status
    private void checkButton(MouseEvent e)
    {
        // Check the mouse buttons
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                mousebutton = 1;
                break;
            case MouseEvent.BUTTON2:
                mousebutton = 2;
                break;
            case MouseEvent.BUTTON3:
                mousebutton = 3;
                break;
            default:
                mousebutton = 0;
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        // Save the mouse position values
        clickx = e.getX();
        clicky = e.getY();

        // Get an update on buttons
        checkButton(e);

        // Refresh the screen (call the paint event)
        repaint();
    }

    public void mouseEntered(MouseEvent e)
    {
        enterx = e.getX();
        entery = e.getY();
        repaint();
    }

    public void mouseExited(MouseEvent e)
    {
        exitx = e.getX();
        exity = e.getY();
        repaint();
    }

    public void mousePressed(MouseEvent e)
    {
        pressx = e.getX();
        pressy = e.getY();
        checkButton(e);
        repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        releasex = e.getX();
        releasey = e.getY();
        checkButton(e);
        repaint();
    }

    public void mouseDragged(MouseEvent e)
    {
        dragx = e.getX();
        dragy = e.getY();
        repaint();
    }

    public void mouseMoved(MouseEvent e)
    {
        movex = e.getX();
        movey = e.getY();
        repaint();
    }
}
