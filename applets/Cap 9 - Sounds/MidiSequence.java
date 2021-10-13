/*******************************************************
 * MidiSequence class
 *******************************************************/

import java.io.*;
import java.net.*;
import javax.sound.midi.*;

public class MidiSequence 
{
    // Primary midi sequencer object
    private Sequencer sequencer;

    // Provide Sequence as a read-only property
    private Sequence song;
    public Sequence getSong() { return song; }

    // Filename property is read-only
    private String filename;
    public String getFilename() { return filename; }

    // Looping property for looping continuously
    private boolean looping = false;
    public boolean getLooping() { return looping; }
    public void setLooping(boolean _looping) { looping = _looping; }

    // Repeat property for looping a fixed number of times
    private int repeat = 0;
    public void setRepeat(int _repeat) { repeat = _repeat; }
    public int getRepeat() { return repeat; }

    // Returns whether the sequence is ready for action
    public boolean isLoaded()
    {
        return (boolean)(sequencer.isOpen());
    }

    // Primary constructor
    public MidiSequence()
    {
        try
        {
            // Fire up the sequencer
            sequencer = MidiSystem.getSequencer();
        }
        catch(MidiUnavailableException e) { }
    }

    // Overloaded constructor accepts midi filename
    public MidiSequence(String midifile)
    {
        this(); // Call default constructor first
        load(midifile); // Load the midi file
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

    // Load a midi file into a sequence
    public boolean load(String midifile)
    {
        try
        {
            // Load the midi file into the sequencer
            filename = midifile;
            song = MidiSystem.getSequence(getURL(filename));
            sequencer.setSequence(song);
            sequencer.open();
            return true;
        }
        catch(InvalidMidiDataException e)
        {
            return false;
        }
        catch(MidiUnavailableException e)
        {
            return false;
        }
        catch(IOException e)
        {
            return false;
        }
    }

    // Play the midi sequence
    public void play()
    {
        if(!sequencer.isOpen()) return;
        if(looping)
        {
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
        }
        else
        {
            sequencer.setLoopCount(repeat);
            sequencer.start();
        }
    }

    // Stop the midi sequence
    public void stop()
    {
        sequencer.stop();
    }
}
