/*******************************************************
 * KeyboardTest program
 *******************************************************/

 import java.awt.*;
 import java.awt.event.*;
 import java.applet.*;

 public class KeyboardTest extends Applet implements KeyListener
 {
    int keyCode;
    char keyChar;

    public void init()
    {
        addKeyListener(this);
    }

    public void paint(Graphics g)
    {
        g.drawString("Press a key...", 20, 20);
        g.drawString("Key code: " + keyCode, 20, 50);
        g.drawString("Key char: " + keyChar, 20, 70);
    }

    public void keyPressed(KeyEvent e)
    {
        keyCode = e.getKeyCode();
        keyChar = ' ';
        repaint();
    }

    // keyReleased is not being used
    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e)
    {
        keyChar = e.getKeyChar();
        repaint();
    }
 }