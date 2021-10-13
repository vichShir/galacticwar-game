/*******************************************************
 * SoundClip class
 *******************************************************/

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class SoundClip
{
    // The source for audio data
    private AudioInputStream sample;

    // Sound clip property is read-only here
    private Clip clip;
    public Clip getClup() { return clip; }

    // Looping property for continuous playback
    private boolean looping = false;
    public void setLooping(boolean _looping) { looping = _looping; }
    public boolean getLooping() { return looping; }

    // Repeat property used to play sound multiple times
    private int repeat = 0;
    public void setRepeat(int _repeat) { repeat = _repeat; }
    public int getRepeat() { return repeat; }

    // Filename property
    private String filename = "";
    public void setFilename(String _filename) { filename = _filename; }

    // Property to verify when sample is ready
    public boolean isLoaded()
    {
        return (boolean)(sample != null);
    }

    // Constructor
    public SoundClip()
    {
        try
        {
            // Create a sound buffer
            clip = AudioSystem.getClip();
        }
        catch(LineUnavailableException e) { }
    }

    // This overloaded constructor takes a sound file as a parameter
    public SoundClip(String audiofile)
    {
        this(); // Call default constructor first
        load(audiofile); // Now load the audio file
    }

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

    // Load sound file
    public boolean load(String audiofile)
    {
        try
        {
            // Prepare the input stream for an audio file
            setFilename(audiofile);
            // Set the audio stream source
            sample = AudioSystem.getAudioInputStream(getURL(filename));
            // Load the audio file
            clip.open(sample);
            return true;
        }
        catch(IOException e)
        {
            return false;
        }
        catch(UnsupportedAudioFileException e)
        {
            return false;
        }
        catch(LineUnavailableException e)
        {
            return false;
        }
    }

    public void play()
    {
        // Exit if the samples isn't been loaded
        if(!isLoaded()) return;

        // Reset the sound clip
        clip.setFramePosition(0);

        // Play sample with optional looping
        if(looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(repeat);
    }

    public void stop()
    {
        clip.stop();
    }
}
