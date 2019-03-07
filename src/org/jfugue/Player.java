/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue;  // modified by D G Gray 07-jun-2010

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
//import org.jfugue.*;  // added by D G Gray 07-jun-2010
import javax.swing.*;  // added by D G Gray 14-jul-2011

/**
 * Prepares a pattern to be turned into music by the Renderer.  This class
 * also handles saving the sequence derived from a pattern as a MIDI file.
 *
 *@see MidiRenderer
 *@see Pattern
 *@author David Koelle
 *@version 2.0
 */
public class Player
{
    private Sequencer sequencer;
    private MusicStringParser parser;
    //private MusicStringPulseParser pparser; // added D G Gray 28 Aug 2016
    private MidiRenderer renderer;
    private float sequenceTiming = Sequence.PPQ;
    private int resolution = 32; // modified D G Gray 12th Aug 2017
    private volatile boolean paused = false;
    private volatile boolean started = false;
    private volatile boolean finished = false;

    //private Map rememberedPatterns; // doesn't seem to be required D G Gray Oct 2018

    private JFrame frame = null; // added by D G Gray 22-nov-2013
    private JDialog dialog = null; // added by D G Gray 22-nov-2013

    /**
     * Instantiates a new Player object, which is used for playing music.
     */
    public Player()
    {
        this(true);
    }

    public Player(JFrame aJFrame) // added by D G Gray 22-nov-2013
    {
        this(true);
        frame = aJFrame;
    }

    /**
     * Instantiates a new Player object, which is used for playing music.
     * The <code>connected</code> parameter is passed directly to MidiSystem.getSequencer.
     * Pass false when you do not want to copy a live synthesizer - for example,
     * if your Player is on a server, and you don't want to create new synthesizers every time
     * the constructor is called.
     */
    public Player(boolean connected)
    {
        try {
            // Get default sequencer.
            setSequencer(MidiSystem.getSequencer(connected)); // use non connected sequencer so no copy of live synthesizer will be created.
        } catch (MidiUnavailableException e)
        {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
        }
        initParser();
    }

    /**
     * Creates a new Player instance using a Sequencer that you have provided.
     * @param sequencer The Sequencer to send the MIDI events
     */
    public Player(Sequencer sequencer)
    {
        setSequencer(sequencer);
        initParser();
    }

    /**
     * Creates a new Player instance using a Sequencer obtained from the Synthesizer that you have provided.
     * @param synth The Synthesizer you want to use for this Player.
     */
    public Player(Synthesizer synth) throws MidiUnavailableException
    {
        this(Player.getSequencerConnectedToSynthesizer(synth));
    }

    public Player(Synthesizer synth, JFrame aJFrame) throws MidiUnavailableException // added by D G Gray 22-nov-2013
    {
        this(Player.getSequencerConnectedToSynthesizer(synth));
        frame = aJFrame;
    }

    public Player(Synthesizer synth, JDialog aJDialog) throws MidiUnavailableException // added by D G Gray 22-nov-2013
    {
        this(Player.getSequencerConnectedToSynthesizer(synth));
        dialog = aJDialog;
    }

    private void initParser()
    {
        this.parser = new MusicStringParser();
        this.renderer = new MidiRenderer(sequenceTiming, resolution);
        this.parser.addParserListener(this.renderer);
    }

    private void initSequencer()
    {
        // Close the sequencer and synthesizer
        getSequencer().addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage event)
            {
                if (event.getType() == 47)
                {
                    close();
                }
            }
        });
    }

    private void openSequencer()
    {
        if (getSequencer() == null)
        {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED);
        }

        // Open the sequencer, if it is not already open
        if (!getSequencer().isOpen()) {
            try {
                getSequencer().open();
            } catch (MidiUnavailableException e)
            {
                throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
            }
        }
    }

    /**
     * Returns the instance of the MusicStringParser that the Player uses to parse
     * music strings.  You can attach additional ParserListeners to this Parser to
     * know exactly when musical events are taking place.  (Similarly, you could
     * create an Anticipator with a delay of 0, but this is much more direct).
     *
     * You could also remove the Player's default MidiRenderer from this Parser.
     *
     * @return instance of this Player's MusicStringParser
     * @version 4.1
     */
    public Parser getParser()
    {
        return this.parser;
    }

    /**
     * Allows you to set the MusicStringParser for this Player.  You shouldn't use this unless
     * you definitely want to hijack the Player's MusicStringParser.  You might decide to do this
     * if you're creating a new subclass of MusicStringParser to test out some new functionality.
     * Or, you might have set up a MusicStringParser with an assortment of ParserListeners and
     * you want to use those listeners instead of Player's default listener (although really, you
     * should call getParser() and add your listeners to that parser).
     *
     * @param parser Your new instance of a MusicStringParser
     * @version 4.1
     */
    public void setParser(MusicStringParser parser)
    {
        this.parser = parser;
        this.parser.addParserListener(this.renderer);  // added by D G Gray 07-jun-2010
    }

//    public void setParser(MusicStringPulseParser parser) // added by D G Gray 28th Aug 2016
//    {
//        this.parser = parser;
//        this.parser.addParserListener(this.renderer);  // added by D G Gray 07-jun-2010
//    }

    /**
     * Returns the MidiRenderer that this Player will use to play MIDI events.
     * @return the MidiRenderer that this Player will use to play MIDI events
     * @version 4.1
     */
    public ParserListener getParserListener()
    {
        return this.renderer;
    }

    /**
     * Plays a pattern by setting up a Renderer and feeding the pattern to it.
     * @param pattern the pattern to play
     * @see MidiRenderer
     */
    public void play(Pattern pattern)
    {
        Sequence sequence = getSequence(pattern);
        play(sequence);
    }

    /**
     * Appends together and plays all of the patterns passed in.
     * @param patterns the patterns to play
     * @see MidiRenderer
     */
    public void play(Pattern... patterns)
    {
    	Pattern allPatterns = new Pattern();
    	for (Pattern p : patterns) {
    		allPatterns.add(p);
    	}
    	play(allPatterns);
    }

    /**
     * Replaces pattern identifiers with patterns from the map.
     * @param context A map of pattern identifiers to Pattern objects
     * @param pattern The pattern to play
     */
    public void play(Map<String, Pattern> context, Pattern pattern)
    {
    	Pattern contextPattern = Pattern.createPattern(context, pattern);
    	play(contextPattern);
    }

    /**
     * Appends together and plays all of the patterns passed in, replacing
     * pattern identifiers with actual patterns from the map.
     * @param context A map of pattern identifiers to Pattern objects
     * @param patterns The patterns to play
     */
    public void play(Map<String, Pattern> context, Pattern... patterns)
    {
    	Pattern contextPattern = Pattern.createPattern(context, patterns);
    	play(contextPattern);
    }

    /**
     * Plays a pattern by setting up a Renderer and feeding the pattern to it.
     * @param pattern the pattern to play
     * @see MidiRenderer
     */
    public void play(Rhythm rhythm)
    {
        Pattern pattern = rhythm.getPattern();
        Sequence sequence = getSequence(pattern);
        play(sequence);
    }

    /**
     * Plays a MIDI Sequence
     * @param sequence the Sequence to play
     * @throws JFugueException if there is a problem playing the music
     * @see MidiRenderer
     */
    public void play(Sequence sequence)
    {
        // Open the sequencer
        openSequencer();

        // Set the sequence
        try {
            getSequencer().setSequence(sequence);
        } catch (Exception e)
        {
            throw new JFugueException(JFugueException.ERROR_PLAYING_MUSIC + e.getMessage());
        }

        setStarted(true);

        // Start the sequence
        getSequencer().start();

        // Wait for the sequence to finish
//        while (isOn())
//        {
//            try {
//                Thread.sleep(20L); // don't hog all of the CPU // 20 changed to 20L D G Gray 14-jul-2011
//            } catch (InterruptedException e) {
//                try {
//                    stop();
//                } catch (Exception e2) {
//                    // do nothing
//                }
//                throw new JFugueException(JFugueException.ERROR_SLEEP);
//            }
//        }  //  commented out by D G Gray 14-jul-2011

      Object[] options = {"Quit"};
      //JOptionPane optionPane = new JOptionPane();
      if(dialog != null)
      {
         JOptionPane.showOptionDialog
            (dialog,
             "Playing pattern.",
             "Player",
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.PLAIN_MESSAGE,
             null,
             options,
             options[0]);  // added by D G Gray 14-jul-2011 modified 22-nov-2013
		 }
      else
      {
         JOptionPane.showOptionDialog
            (frame,
             "Playing pattern.",
             "Player",
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.PLAIN_MESSAGE,
             null,
             options,
             options[0]);  // added by D G Gray 22-nov-2013
		}

        // Close the sequencer
        getSequencer().close();

        setStarted(false);
        setFinished(true);
    }

    private synchronized boolean isOn()
    {
    	return isPlaying() || isPaused();
    }

    /**
     * Plays a string of music.  Be sure to call player.close() after play() has returned.
     * @param musicString the MusicString (JFugue-formatted string) to play
     * @version 3.0
     */
    public void play(String musicString)
    {
        if (musicString.indexOf(".mid") > 0)
        {
            // If the user tried to call this method with "filename.mid" or "filename.midi", throw the following exception
            throw new JFugueException(JFugueException.PLAYS_STRING_NOT_FILE_EXC);
        }

        Pattern pattern = new Pattern(musicString);
        play(pattern);
    }

    /**
     * Plays a MIDI file, without doing any conversions to MusicStrings.
     * Be sure to call player.close() after play() has returned.
     * @param file the MIDI file to play
     * @throws IOException
     * @throws InvalidMidiDataException
     * @version 3.0
     */
    public void playMidiDirectly(File file) throws IOException, InvalidMidiDataException
    {
        Sequence sequence = MidiSystem.getSequence(file);
        play(sequence);
    }

    /**
     * Plays a URL that contains a MIDI sequence.  Be sure to call player.close() after play() has returned.
     * @param url the URL to play
     * @throws IOException
     * @throws InvalidMidiDataException
     * @version 3.0
     */
    public void playMidiDirectly(URL url) throws IOException, InvalidMidiDataException
    {
        Sequence sequence = MidiSystem.getSequence(url);
        play(sequence);
    }

    public void play(Anticipator anticipator, Pattern pattern, long offset)
    {
        Sequence sequence = getSequence(pattern);
        Sequence sequence2 = getSequence(pattern);
        play(anticipator, sequence, sequence2, offset);
    }

    public void play(Anticipator anticipator, Sequence sequence, Sequence sequence2, long offset)
    {
        anticipator.play(sequence);

        if (offset > 0)
        {
            try {
                Thread.sleep(offset);
            } catch (InterruptedException e) {
                throw new JFugueException(JFugueException.ERROR_SLEEP);
            }
        }

        play(sequence2);
    }

    /**
     * Convenience method for playing multiple patterns simultaneously.  Assumes that
     * the patterns do not contain Voice tokens (such as "V0").  Assigns each pattern
     * a voice, from 0 through 15, except 9 (which is the percussion track).  For this
     * reason, if more than 15 patterns are passed in, an IllegalArgumentException is thrown.
     *
     * @param patterns The patterns to play in harmony
     * @return The combined pattern, including voice tokens
     */
    public Pattern playInHarmony(Pattern... patterns)
    {
        if (patterns.length > 15) {
            throw new IllegalArgumentException("playInHarmony no more than 15 patterns; "+patterns.length+" were passed in");
        }

        Pattern retVal = new Pattern();
        int voice = 0;
        for (int i=0; i < patterns.length; i++) {
            retVal.add("V"+voice);
            retVal.add(patterns[i]);
            voice++;
            if (voice == 9) {
                voice = 10;
            }
        }
        play(retVal);
        return retVal;
    }

    /**
     * Closes MIDI resources - be sure to call this after play() has returned.
     */
    public void close()
    {
        getSequencer().close();
        try {
            if (MidiSystem.getSynthesizer() != null) {
                MidiSystem.getSynthesizer().close();
            }
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.GENERAL_ERROR + e.getMessage());
        }
    }

    private void setStarted(boolean started)
    {
        this.started = started;
    }

    private void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public boolean isStarted()
    {
        return this.started;
    }

    public boolean isFinished()
    {
        return this.finished;
    }

    public boolean isPlaying()
    {
        return getSequencer().isRunning();
    }

    public boolean isPaused()
    {
        return paused;
    }

    public synchronized void pause()
    {
        paused = true;
        if (isPlaying()) {
            getSequencer().stop();
        }
    }

    public synchronized void resume()
    {
        paused = false;
        getSequencer().start();
    }

    public synchronized void stop()
    {
        paused = false;
        getSequencer().stop();
        getSequencer().setMicrosecondPosition(0);
    }

    public void jumpTo(long microseconds)
    {
        getSequencer().setMicrosecondPosition(microseconds);
    }

    public long getSequenceLength(Sequence sequence)
    {
        return sequence.getMicrosecondLength();
    }

    public long getSequencePosition()
    {
        return getSequencer().getMicrosecondPosition();
    }

    /**
     * Saves the MIDI data from a pattern into a file.
     * @param pattern the pattern to save
     * @param file the File to save the pattern to.  Should include file extension, such as .mid
     */
    public void saveMidi(Pattern pattern, File file) throws IOException
    {
        Sequence sequence = getSequence(pattern);

        int[] writers = MidiSystem.getMidiFileTypes(sequence);
        if (writers.length == 0) return;

        MidiSystem.write(sequence, writers[0], file);
    }

    /**
     * Saves the MIDI data from a MusicString into a file.
     * @param musicString the MusicString to save
     * @param file the File to save the MusicString to.  Should include file extension, such as .mid
     */
    public void saveMidi(String musicString, File file) throws IOException
    {
        Pattern pattern = new Pattern(musicString);
        saveMidi(pattern, file);
    }

    /**
     * Parses a MIDI file and returns a Pattern representing the MIDI file.
     * This is an excellent example of JFugue's Parser-Renderer architecture:
     *
     * <pre>
     *  MidiParser parser = new MidiParser();
     *  MusicStringRenderer renderer = new MusicStringRenderer();
     *  parser.addParserListener(renderer);
     *  parser.parse(sequence);
     * </pre>
     *
     * @param filename The name of the MIDI file
     * @return a Pattern containing the MusicString representing the MIDI music
     * @throws IOException If there is a problem opening the MIDI file
     * @throws InvalidMidiDataException If there is a problem obtaining MIDI resources
     */
    public Pattern loadMidi(File file) throws IOException, InvalidMidiDataException
    {
        MidiFileFormat format = MidiSystem.getMidiFileFormat(file);
        this.sequenceTiming = format.getDivisionType();
        this.resolution = format.getResolution();

        MidiParser parser = new MidiParser();
        MusicStringRenderer renderer = new MusicStringRenderer();
        parser.addParserListener(renderer);
        parser.parse(MidiSystem.getSequence(file));
        Pattern pattern = new Pattern(renderer.getPattern().getMusicString());

        return pattern;
    }

    /**
     * Stops all notes from playing on all MIDI channels.
     * Uses the synthesizer provided by MidiSystem.getSynthesizer().
     */
    public static void allNotesOff()
    {
        try {
            allNotesOff(MidiSystem.getSynthesizer());
        } catch (MidiUnavailableException e)
        {
            throw new JFugueException(JFugueException.GENERAL_ERROR);
        }
    }

    /**
     * Stops all notes from playing on all MIDI channels.
     */
    public static void allNotesOff(Synthesizer synth)
    {
        try {
            if (!synth.isOpen()) {
                synth.open();
            }
            MidiChannel[] channels = synth.getChannels();
            for (int i=0; i < channels.length; i++)
            {
                channels[i].allNotesOff();
            }
        } catch (MidiUnavailableException e)
        {
            throw new JFugueException(JFugueException.GENERAL_ERROR);
        }
    }

    /**
     * Returns the sequencer containing the MIDI data from a pattern that has been parsed.
     * @return the Sequencer from the pattern that was recently parsed
     */
    public Sequencer getSequencer()
    {
        return this.sequencer;
    }

    private void setSequencer(Sequencer sequencer)
    {
        this.sequencer = sequencer;
        initSequencer();
    }

    /**
     * Returns the sequence containing the MIDI data from the given pattern.
     * @return the Sequence from the given pattern
     */
    public Sequence getSequence(Pattern pattern)
    {
        this.renderer.reset();
        this.parser.parse(pattern);
        Sequence sequence = this.renderer.getSequence();
        return sequence;
    }

    /**
     * Returns an instance of a Sequencer that uses the provided Synthesizer as its receiver.
     * This is useful when you have made changes to a specific Synthesizer--for example, you've
     * loaded in new patches--that you want the Sequencer to use.  You can then pass the Sequencer
     * to the Player constructor.
     *
     * @param synth The Synthesizer to use as the receiver for the returned Sequencer
     * @return a Sequencer with the provided Synthesizer as its receiver
     * @throws MidiUnavailableException
     * @version 4.0
     */
    public static Sequencer getSequencerConnectedToSynthesizer(Synthesizer synth) throws MidiUnavailableException
    {
        Sequencer sequencer = MidiSystem.getSequencer(false); // Get Sequencer which is not connected to new Synthesizer.
        sequencer.open();
        if (!synth.isOpen()) {
            synth.open();
        }
        sequencer.getTransmitter().setReceiver(synth.getReceiver()); // Connect the Synthesizer to our synthesizer instance.
        return sequencer;
    }
}
