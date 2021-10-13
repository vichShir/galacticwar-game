/*******************************************************
 * SoundClass program
 *******************************************************/

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class SoundClass extends Applet implements KeyListener
{
    private int keyCode;
    private String filename;

    public void init()
    {
        addKeyListener(this);
    }

    public void paint(Graphics g)
    {
        int y = 1;
        g.drawString("Key code (49) to play gong.wav", 20, 15 * y++);
        g.drawString("Key code (50) to play titlemusic.mid", 20, 15 * y++);
        g.drawString("Press a key...", 20, 15 * y++);
        g.drawString("Key code: " + keyCode, 20, 15 * y++);

        // Sound Clip class
        filename = "sounds/gong.wav";
        SoundClip clip = new SoundClip(filename);

        // Midi Sequence class
        filename = "sounds/titlemusic.mid";
        MidiSequence midi = new MidiSequence(filename);

        switch(keyCode)
        {
            case 49:
                clip.play();
                break;
            case 50:
                midi.play();
                break;
        }
    }

    public void keyPressed(KeyEvent e)
    {
        keyCode = e.getKeyCode();
        repaint();
    }

    // keyReleased is not being used
    public void keyReleased(KeyEvent e) { }

    // keyTyped is not being used
    public void keyTyped(KeyEvent e) { }
}
