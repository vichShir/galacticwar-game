/*******************************************************
 * PlaySound program
 *******************************************************/

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class PlaySound extends Applet
{
    String filename = "sounds/gong.wav";
    AudioInputStream sample;

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
            sample = AudioSystem.getAudioInputStream(getURL(filename));
            // Create a sound buffer
            Clip clip = AudioSystem.getClip();
            // Load the audio file
            clip.open(sample);
            // Play the sound clip
            clip.start();
        }
        catch(MalformedURLException e) { }
        catch(IOException e) { }
        catch(LineUnavailableException e) { }
        catch(UnsupportedAudioFileException e) { }
        catch(Exception e) { }
    }

    // The paint event handles the screen refresh
    public void paint(Graphics g)
    {
        int y = 1;
        g.drawString("Sample file: " + filename, 10, 15*y++);
        g.drawString(" " + sample.getFormat().toString(), 10, 15*y++);
        g.drawString(" Sampling rate: " + (int)sample.getFormat().getSampleRate(), 10, 15*y++);
        g.drawString(" Sample channels: " + sample.getFormat().getChannels(), 10, 15*y++);
        g.drawString(" Encoded format: " + sample.getFormat().getEncoding().toString(), 10, 15*y++);
        g.drawString(" Sample size: " + sample.getFormat().getSampleSizeInBits() + "-bit", 10, 15*y++);
        g.drawString(" Frame size: " + sample.getFormat().getFrameSize(), 10, 15*y++);
    }
}