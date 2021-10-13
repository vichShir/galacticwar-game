/*******************************************************
 * PlayMusic program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import javax.sound.midi.*;

public class PlayMusic extends Applet
{
    String filename = "sounds/titlemusic.mid";
    Sequence song;

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

    // Initialize the applet
    public void init()
    {
        try
        {
            song = MidiSystem.getSequence(getURL(filename));
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.setSequence(song);
            sequencer.open();
            sequencer.start();
        }
        catch(InvalidMidiDataException e) { }
        catch(MidiUnavailableException e) { }
        catch(IOException e) { }
    }

    // Repaint the applet window
    public void paint(Graphics g)
    {
        int x= 10, y = 1;
        if(song != null)
        {
            g.drawString("Midi file: " + filename, x, 15 * y++);
            g.drawString("Resolution: " + song.getResolution(), x, 15 * y++);
            g.drawString("Tick length: " + song.getTickLength(), x, 15 * y++);
            g.drawString("Tracks: " + song.getTracks().length, x, 15 * y++);
            g.drawString("Patches: " + song.getPatchList().length, x, 15 * y++);
        }
        else
        {
            g.drawString("Error loading sequence file " + filename, x, 15 * y++);
        }
    }
}
