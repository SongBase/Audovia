//package org.jfugue.examples; // modified by D G Gray 2nd Sep 2014

/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  Karl Helgason and David Koelle
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.jfugue.JFugueException;
import org.jfugue.Pattern;
import org.jfugue.*; //modified D G Gray 28th Aug 2016

import org.sun.media.sound.*;
//import com.sun.media.sound.*; // D G Gray 30th July 2017

public class Midi2WavRenderer
{
    private Synthesizer synth;
    private int pad;  // added D G Gray 10th Dec 2014

    public Midi2WavRenderer(int aPad) throws MidiUnavailableException, InvalidMidiDataException, IOException  // modified D G Gray 10th Dec 2014
    {
        pad = aPad;  // added D G Gray 10th Dec 2014
        //this.synth = MidiSystem.getSynthesizer(); // D G Gray 30th July 2017
    }

    private Soundbank loadSoundbank(File soundbankFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        Soundbank soundbank = MidiSystem.getSoundbank(soundbankFile);
        if (!synth.isSoundbankSupported(soundbank)) {
            throw new JFugueException("Soundbank not supported by synthesizer");
        }
        return soundbank;
    }

    /**
     * Creates a WAV file based on the Pattern, using the sounds from the specified soundbank;
     * to prevent memory problems, this method asks for an array of patches (instruments) to load.
     *
     * @param soundbankFile
     * @param patches
     * @param pattern
     * @param outputFile
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void createWavFile(File soundbankFile, int[] patches, Pattern pattern, File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        // Create a Player with this Synthesizer, and get a Sequence
        Sequencer sequencer = Player.getSequencerConnectedToSynthesizer(synth);
        Player player = new Player(sequencer);
        Sequence sequence = player.getSequence(pattern);

        createWavFile(soundbankFile, patches, sequence, outputFile);
    }

    /**
     * Creates a WAV file based on the Sequence, using the sounds from the specified soundbank;
     * to prevent memory problems, this method asks for an array of patches (instruments) to load.
     *
     * @param soundbankFile
     * @param patches
     * @param sequence
     * @param outputFile
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void createWavFile(File soundbankFile, int[] patches, Sequence sequence, File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        // Load soundbank
        Soundbank soundbank = loadSoundbank(soundbankFile);

        // Open the Synthesizer and load the requested instruments
        this.synth.open();
        this.synth.unloadAllInstruments(soundbank);
        for (int patch : patches) {
            this.synth.loadInstrument(soundbank.getInstrument(new Patch(0, patch)));
        }

        createWavFile(sequence, outputFile);
    }

    /**
     * Creates a WAV file based on the Pattern, using the default soundbank.
     *
     * @param pattern
     * @param outputFile
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void createWavFile(Pattern pattern, File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        // Create a Player with this Synthesizer, and get a Sequence
        Sequencer sequencer = Player.getSequencerConnectedToSynthesizer(synth);
        Player player = new Player(sequencer);
        Sequence sequence = player.getSequence(pattern);

        createWavFile(sequence, outputFile);
    }

    /**
     * Creates a WAV file based on the Sequence, using the default soundbank.
     *
     * @param sequence
     * @param outputFile
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void createWavFile(Sequence sequence, File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        AudioSynthesizer synth = findAudioSynthesizer();
        if (synth == null) {
            throw new JFugueException("No AudioSynthesizer was found!");
        }

        //AudioFormat format = new AudioFormat(96000, 24, 2, true, false);
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);  // modified D G Gray 3rd May 2015
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("interpolation", "sinc");
        p.put("max polyphony", "1024");
        AudioInputStream stream = synth.openStream(format, p);

        // Play Sequence into AudioSynthesizer Receiver.
        double total = send(sequence, synth.getReceiver());

        // Calculate how long the WAVE file needs to be.
        //long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
        long len = (long) (stream.getFormat().getFrameRate() * (total + pad));  // modified D G Gray 10th Dec 2014
        stream = new AudioInputStream(stream, stream.getFormat(), len);

        // Write WAVE file to disk.
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);

        this.synth.close();
    }


    /**
     * Creates a WAV file based on the Pattern, using a supplied soundbank - by D G Gray 28th Aug 2016.
     *
     * @param sequence
     * @param outputFile
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public void createWavFile(Pattern pattern, String numeric_duration_type, Soundbank soundbank, File outputFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
    {
        //AudioSynthesizer synth = findAudioSynthesizer();
        AudioSynthesizer synth = new SoftSynthesizer(); // D G Gray 30th July 2017

        if (synth == null) {
            throw new JFugueException("No AudioSynthesizer was found!");
        }

        //AudioFormat format = new AudioFormat(96000, 24, 2, true, false);
        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);  // modified D G Gray 3rd May 2015
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("interpolation", "sinc");
        p.put("max polyphony", "1024");
        AudioInputStream stream = synth.openStream(format, p);

        // D G Gray 30th July 2017

        if (soundbank != null)
        {
			try
			{
               synth.sf2LoadAllInstruments((SF2Soundbank)soundbank);
		    }
		    catch(Exception es)
			{
			   synth.dlsLoadAllInstruments((DLSSoundbank)soundbank);
			}

	    }

        Sequencer sequencer = Player.getSequencerConnectedToSynthesizer(synth);
        Player player = new Player(sequencer);

        if (numeric_duration_type != null && numeric_duration_type.equals("pulses"))
        {
           MusicStringParser parser = new MusicStringParser();
           parser.setNumeric_duration_type("pulses");
           player.setParser(parser);
        }

        Sequence sequence = player.getSequence(pattern);

        // Play Sequence into AudioSynthesizer Receiver.
        double total = send(sequence, synth.getReceiver());

        // Calculate how long the WAVE file needs to be.
        //long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
        long len = (long) (stream.getFormat().getFrameRate() * (total + pad));  // modified D G Gray 10th Dec 2014
        stream = new AudioInputStream(stream, stream.getFormat(), len);

        // Write WAVE file to disk.
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);

        player.close();
        synth.close();
    }


	/**
	 * Find available AudioSynthesizer.
	 */
	private AudioSynthesizer findAudioSynthesizer() throws MidiUnavailableException
	{
		// First check if default synthesizer is AudioSynthesizer.
		Synthesizer synth = MidiSystem.getSynthesizer();
		//System.out.println(synth.toString());
		if (synth instanceof AudioSynthesizer) {
			return (AudioSynthesizer)synth;
		}

		// If default synthesizer is not AudioSynthesizer, check others.
		MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < midiDeviceInfo.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(midiDeviceInfo[i]);
			//System.out.println(dev.toString());
			if (dev instanceof AudioSynthesizer) {
				return (AudioSynthesizer) dev;
			}
		}

		// No AudioSynthesizer was found, return null.
		return null;
	}

	/**
	 * Send entry MIDI Sequence into Receiver using timestamps.
	 */
	private double send(Sequence seq, Receiver recv)
	{
		float divtype = seq.getDivisionType();
		assert (seq.getDivisionType() == Sequence.PPQ);
		Track[] tracks = seq.getTracks();
		int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				int trackpos = trackspos[i];
				Track track = tracks[i];
				if (trackpos < track.size()) {
					MidiEvent event = track.get(trackpos);
					if (selevent == null
							|| event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1)
				break;
			trackspos[seltrack]++;
			long tick = selevent.getTick();
			if (divtype == Sequence.PPQ)
				curtime += ((tick - lasttick) * mpq * 4) / seqres; // modified D G Gray 2nd Sep 2014
			else
				curtime = (long) ((tick * 1000000.0 * divtype) / seqres);
			lasttick = tick;
			MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ)
					if (((MetaMessage) msg).getType() == 0x51) {
						byte[] data = ((MetaMessage) msg).getData();
						mpq = ((data[0] & 0x7f) << 14)
								| ((data[1] & 0x7f) << 7) | (data[2] & 0x7f); // modified D G Gray 2nd Sep 2014
					}
			} else {
				if (recv != null)
					recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}

}
