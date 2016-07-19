/*
 * SBSPopupEditor.java - Popup Editor for Music Strings
 * Copyright (C) 2010, 2012, 2014, 2015, 2016  Donald G Gray
 *
 * http://gray10.com/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.lang.reflect.*;
import java.sql.*;
import java.net.*;
import java.awt.font.*;

import javax.swing.plaf.basic.*;

public class SBSPopupEditor extends JDialog
{
	/*
	 * version 3.2.8
	 *
	 */

   private JTextArea editor;
   private int selection = 1;

   private String string_name;
   private String string_value;
   private String song_name;

   private Connection conn;
   private Integer song_id;

   private PreparedStatement getNumericConstants;
   private PreparedStatement getStringConstants;
   private CallableStatement cSgetNumericConstants;
   private CallableStatement cSgetStringConstants;
   private ResultSet rset;

   private JComboBox<String> numericconstants;
   private JComboBox<String> constants;

   private JComboBox<String> tempo;
   private JComboBox<String> instrument;
   private JComboBox<String> percussion;
   private JComboBox<String> controller;

   private String title = "SBSPopupEditor";
   private JDialog frame = SBSPopupEditor.this;

   public SBSPopupEditor(Connection aConnection, Integer aSongId, String aSongName, String aStringName, String aStringValue,
                         String okButtonText, String cancelButtonText) throws Exception
   {
      setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      addWindowListener(new
         WindowAdapter()
         {
				public void windowClosing(WindowEvent e)
				{
					quit();
				}
			});

      MyFocusListener myFocusListener = new MyFocusListener();

      setSize(600,414); // was 600,396

      //Color background = getBackground();
      //System.out.println(background.getRed());
      //System.out.println(background.getGreen());
      //System.out.println(background.getBlue());

      //setLocation(100,100);
      setLocation(125,125);
      setTitle(title + " - " + aSongName);

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);

      string_name  = aStringName;
      string_value = aStringValue;
      conn         = aConnection;
      song_id      = aSongId;
      song_name    = aSongName;

      getNumericConstants = conn.prepareStatement
                     ("select string_value " +
                      "from sbs_component " +
                      "where song_id = ? " +
                      "and   component_type = 'string' " +
                      "and   string_value like '%$%'");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSgetNumericConstants = conn.prepareCall("{call get_constants(?)}");

      getStringConstants = conn.prepareStatement
                     ("select string_value " +
                      "from sbs_component " +
                      "where song_id = ? " +
                      "and   component_type = 'string' " +
                      "and   string_value like '%~%'");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSgetStringConstants = conn.prepareCall("{call get_string_constants(?)}");

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      menuBar.setUI ( new BasicMenuBarUI ()
		    {
		        public void paint ( Graphics g, JComponent c )
		        {
		            g.setColor ( new Color(232, 204, 255) );
		            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
		        }
          } );

      JMenu fileMenu = new JMenu("Insert");
      menuBar.add(fileMenu);

      JMenuItem numericconstantsItem = new JMenuItem("Numeric Constants");
      JMenuItem constantsItem        = new JMenuItem("String Constants");

      JMenuItem tempoItem      = new JMenuItem("Tempo");
      JMenuItem instrumentItem = new JMenuItem("Instrument");
      JMenuItem percussionItem = new JMenuItem("Percussion (V9)");
      JMenuItem controllerItem = new JMenuItem("Controller");
      JMenuItem cancelInsertItem = new JMenuItem("Cancel");

      NumericconstantsAction numericconstantsAction = new NumericconstantsAction();
      numericconstantsItem.addActionListener(numericconstantsAction);

      ConstantsAction constantsAction = new ConstantsAction();
      constantsItem.addActionListener(constantsAction);

      TempoAction tempoAction = new TempoAction();
      tempoItem.addActionListener(tempoAction);

      InstrumentAction instrumentAction = new InstrumentAction();
      instrumentItem.addActionListener(instrumentAction);

      PercussionAction percussionAction = new PercussionAction();
      percussionItem.addActionListener(percussionAction);

      ControllerAction controllerAction = new ControllerAction();
      controllerItem.addActionListener(controllerAction);

      CancelInsertAction cancelInsertAction = new CancelInsertAction();
      cancelInsertItem.addActionListener(cancelInsertAction);


      // start of numeric constants

      ArrayList<String> numconstantsList = new ArrayList<String>();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cSgetNumericConstants.setInt(1, song_id.intValue());
			cSgetNumericConstants.execute();
			rset = cSgetNumericConstants.getResultSet();
		}
		else
      {
         getNumericConstants.setInt(1,song_id.intValue());
	   	rset = getNumericConstants.executeQuery();
	   }
	   while (rset.next())
		{
			String numconstants = rset.getString(1);
			String[] tokens = numconstants.split("\\s");
			for (int i=0; i<tokens.length; i++)
			{
				if (tokens[i].indexOf("$") > -1 &&
				    tokens[i].indexOf("=") > -1 &&
				    tokens[i].indexOf("~") == -1)
				    {
						 String substring = tokens[i].substring(tokens[i].indexOf("$") + 1, tokens[i].indexOf("="));
						 numconstantsList.add("[" + substring + "]");
					 }
			}
		}
      rset.close();
      conn.commit();

      Collections.sort(numconstantsList);

      numericconstants = new JComboBox<String>();

      if (numconstantsList.size() > 0)
      {
			for (int i=0; i<numconstantsList.size(); i++)
			{
				String numitem = numconstantsList.get(i);
				numericconstants.addItem(numitem);
			}
		}

      Dimension numericconstantsDimension = numericconstants.getPreferredSize();
      double numericconstantsWidth = numericconstantsDimension.getWidth();
      double numericconstantsHeight = numericconstantsDimension.getHeight();
      numericconstantsDimension.setSize(numericconstantsWidth + 3.0, numericconstantsHeight);
      numericconstants.setMaximumSize(numericconstantsDimension);

      numericconstants.addActionListener (new
         ActionListener()
         {
				public void actionPerformed(ActionEvent event)
				{
					String numconstant = (String)numericconstants.getSelectedItem();
					if (editor.getSelectedText() == null)
					{
					   editor.insert(numconstant, editor.getCaretPosition());
					}
					else
					{
						editor.replaceSelection(numconstant);
					}
					//numericconstants.setVisible(false);
					editor.requestFocusInWindow();
				}
			});

	   //buttonPanel.add(numericconstants);

      // end of numeric constants

      // start of string constants

      ArrayList<String> constantsList = new ArrayList<String>();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cSgetStringConstants.setInt(1, song_id.intValue());
			cSgetStringConstants.execute();
			rset = cSgetStringConstants.getResultSet();
		}
		else
      {
         getStringConstants.setInt(1,song_id.intValue());
		   rset = getStringConstants.executeQuery();
	   }
	   while (rset.next())
		{
			String stringconstants = rset.getString(1);
			String[] tokens = stringconstants.split("\\s");
			for (int i=0; i<tokens.length; i++)
			{
				if (tokens[i].indexOf("$") > -1 &&
				    tokens[i].indexOf("=") > -1 &&
				    tokens[i].indexOf("~") > -1)
				    {
						 String substring = tokens[i].substring(tokens[i].indexOf("$") + 1, tokens[i].indexOf("="));
						 constantsList.add("{" + substring + "}");
					 }
			}
		}
      rset.close();
      conn.commit();

      Collections.sort(constantsList);

      constants = new JComboBox<String>();

      if (constantsList.size() > 0)
      {
			for (int i=0; i<constantsList.size(); i++)
			{
				String item = constantsList.get(i);
				constants.addItem(item);
			}
		}

      Dimension constantsDimension = constants.getPreferredSize();
      double constantsWidth = constantsDimension.getWidth();
      double constantsHeight = constantsDimension.getHeight();
      constantsDimension.setSize(constantsWidth + 3.0, constantsHeight);
      constants.setMaximumSize(constantsDimension);

      constants.addActionListener (new
         ActionListener()
         {
				public void actionPerformed(ActionEvent event)
				{
					String constant = (String)constants.getSelectedItem();
					if (editor.getSelectedText() == null)
					{
					   editor.insert(" " + constant + " ", editor.getCaretPosition());
				   }
				   else
				   {
						editor.replaceSelection(" " + constant + " ");
				   }
				   //constants.setVisible(false);
				   editor.requestFocusInWindow();
				}
			});

	   //buttonPanel.add(constants);

      // end of string constants

      tempo           = new JComboBox<String>();
      tempo.addItem("T[grave]");
      tempo.addItem("T[largo]");
      tempo.addItem("T[larghetto]");
      tempo.addItem("T[lento]");
      tempo.addItem("T[adagio]");
      tempo.addItem("T[adagietto]");

      tempo.addItem("T[andante]");
      tempo.addItem("T[andantino]");
      tempo.addItem("T[moderato]");
      tempo.addItem("T[allegretto]");

      tempo.addItem("T[allegro]");
      tempo.addItem("T[vivace]");
      tempo.addItem("T[presto]");
      tempo.addItem("T[pretissimo]");

      Dimension tempoDimension = tempo.getPreferredSize();
      double tempoWidth = tempoDimension.getWidth();
      double tempoHeight = tempoDimension.getHeight();
      tempoDimension.setSize(tempoWidth + 3.0, tempoHeight);
      tempo.setMaximumSize(tempoDimension);

      tempo.addActionListener (new
		   ActionListener()
		      {
				   public void actionPerformed(ActionEvent event)
				   {
					   String tempoconstant = (String)tempo.getSelectedItem();
					   if (editor.getSelectedText() == null)
					   {
					   	editor.insert(" " + tempoconstant + " ", editor.getCaretPosition());
					   }
					   else
					   {
							editor.replaceSelection(" " + tempoconstant + " ");
						}
						tempo.setVisible(false);
					}
			});

      instrument      = new JComboBox<String>();
      instrument.addItem("I[piano]");
      instrument.addItem("I[acoustic_grand]");
      instrument.addItem("I[bright_acoustic]");
      instrument.addItem("I[electric_grand]");
      instrument.addItem("I[honkey_tonk]");
      instrument.addItem("I[electric_piano]");
      instrument.addItem("I[electric_piano_1]");
      instrument.addItem("I[electric_piano_2]");
      instrument.addItem("I[harpsichord]");
      instrument.addItem("I[clavinet]");
      instrument.addItem("I[celesta]");
      instrument.addItem("I[glockenspiel]");

      instrument.addItem("I[music_box]");
      instrument.addItem("I[vibraphone]");
      instrument.addItem("I[marimba]");
      instrument.addItem("I[xylophone]");
      instrument.addItem("I[tubular_bells]");
      instrument.addItem("I[dulcimer]");
      instrument.addItem("I[drawbar_organ]");
      instrument.addItem("I[percussive_organ]");
      instrument.addItem("I[rock_organ]");
      instrument.addItem("I[church_organ]");

      instrument.addItem("I[reed_organ]");
      instrument.addItem("I[accordian]");
      instrument.addItem("I[harmonica]");
      instrument.addItem("I[tango_accordian]");
      instrument.addItem("I[guitar]");
      instrument.addItem("I[nylon_string_guitar]");
      instrument.addItem("I[steel_string_guitar]");
      instrument.addItem("I[electric_jazz_guitar]");
      instrument.addItem("I[electric_clean_guitar]");
      instrument.addItem("I[electric_muted_guitar]");
      instrument.addItem("I[overdriven_guitar]");

      instrument.addItem("I[distortion_guitar]");
      instrument.addItem("I[guitar_harmonics]");
      instrument.addItem("I[acoustic_bass]");
      instrument.addItem("I[electric_bass_finger]");
      instrument.addItem("I[electric_bass_pick]");
      instrument.addItem("I[fretless_bass]");
      instrument.addItem("I[slap_bass_1]");
      instrument.addItem("I[slap_bass_2]");
      instrument.addItem("I[synth_bass_1]");
      instrument.addItem("I[synth_bass_2]");

      instrument.addItem("I[violin]");
      instrument.addItem("I[viola]");
      instrument.addItem("I[cello]");
      instrument.addItem("I[contrabass]");
      instrument.addItem("I[tremolo_strings]");
      instrument.addItem("I[pizzicato_strings]");
      instrument.addItem("I[orchestral_strings]");
      instrument.addItem("I[timpani]");
      instrument.addItem("I[string_ensemble_1]");
      instrument.addItem("I[string_ensemble_2]");

      instrument.addItem("I[synth_strings_1]");
      instrument.addItem("I[synth_strings_2]");
      instrument.addItem("I[choir_aahs]");
      instrument.addItem("I[voice_oohs]");
      instrument.addItem("I[synth_voice]");
      instrument.addItem("I[orchestra_hit]");
      instrument.addItem("I[trumpet]");
      instrument.addItem("I[trombone]");
      instrument.addItem("I[tuba]");
      instrument.addItem("I[muted_trumpet]");

      instrument.addItem("I[french_horn]");
      instrument.addItem("I[brass_section]");
      instrument.addItem("I[synthbrass_1]");
      instrument.addItem("I[synth_brass_1]");
      instrument.addItem("I[synthbrass_2]");
      instrument.addItem("I[synth_brass_2]");
      instrument.addItem("I[soprano_sax]");
      instrument.addItem("I[alto_sax]");
      instrument.addItem("I[tenor_sax]");
      instrument.addItem("I[baritone_sax]");
      instrument.addItem("I[oboe]");
      instrument.addItem("I[english_horn]");

      instrument.addItem("I[bassoon]");
      instrument.addItem("I[clarinet]");
      instrument.addItem("I[piccolo]");
      instrument.addItem("I[flute]");
      instrument.addItem("I[recorder]");
      instrument.addItem("I[pan_flute]");
      instrument.addItem("I[blown_bottle]");
      instrument.addItem("I[skakuhachi]");
      instrument.addItem("I[whistle]");
      instrument.addItem("I[ocarina]");

      instrument.addItem("I[lead_square]");
      instrument.addItem("I[square]");
      instrument.addItem("I[lead_sawtooth]");
      instrument.addItem("I[sawtooth]");
      instrument.addItem("I[lead_calliope]");
      instrument.addItem("I[calliope]");
      instrument.addItem("I[lead_chiff]");
      instrument.addItem("I[chiff]");
      instrument.addItem("I[lead_charang]");
      instrument.addItem("I[charang]");
      instrument.addItem("I[lead_voice]");
      instrument.addItem("I[voice]");
      instrument.addItem("I[lead_fifths]");
      instrument.addItem("I[fifths]");
      instrument.addItem("I[lead_basslead]");
      instrument.addItem("I[basslead]");
      instrument.addItem("I[pad_new_age]");
      instrument.addItem("I[new_age]");
      instrument.addItem("I[pad_warm]");
      instrument.addItem("I[warm]");

      instrument.addItem("I[pad_polysynth]");
      instrument.addItem("I[polysynth]");
      instrument.addItem("I[pad_choir]");
      instrument.addItem("I[choir]");
      instrument.addItem("I[pad_bowed]");
      instrument.addItem("I[bowed]");
      instrument.addItem("I[pad_metallic]");
      instrument.addItem("I[metallic]");
      instrument.addItem("I[pad_halo]");
      instrument.addItem("I[halo]");
      instrument.addItem("I[pad_sweep]");
      instrument.addItem("I[sweep]");
      instrument.addItem("I[fx_rain]");
      instrument.addItem("I[rain]");
      instrument.addItem("I[fx_soundtrack]");
      instrument.addItem("I[soundtrack]");
      instrument.addItem("I[fx_crystal]");
      instrument.addItem("I[crystal]");
      instrument.addItem("I[fx_atmosphere]");
      instrument.addItem("I[atmosphere]");

      instrument.addItem("I[fx_brightness]");
      instrument.addItem("I[brightness]");
      instrument.addItem("I[fx_goblins]");
      instrument.addItem("I[goblins]");
      instrument.addItem("I[fx_echoes]");
      instrument.addItem("I[echoes]");
      instrument.addItem("I[fx_sci-fi]");
      instrument.addItem("I[sci-fi]");
      instrument.addItem("I[sitar]");
      instrument.addItem("I[banjo]");
      instrument.addItem("I[shamisen]");
      instrument.addItem("I[koto]");
      instrument.addItem("I[kalimba]");
      instrument.addItem("I[bagpipe]");

      instrument.addItem("I[fiddle]");
      instrument.addItem("I[shanai]");
      instrument.addItem("I[tinkle_bell]");
      instrument.addItem("I[agogo]");
      instrument.addItem("I[steel_drums]");
      instrument.addItem("I[woodblock]");
      instrument.addItem("I[taiko_drum]");
      instrument.addItem("I[melodic_tom]");
      instrument.addItem("I[synth_drum]");
      instrument.addItem("I[reverse_cymbal]");

      instrument.addItem("I[guitar_fret_noise]");
      instrument.addItem("I[breath_noise]");
      instrument.addItem("I[seashore]");
      instrument.addItem("I[bird_tweet]");
      instrument.addItem("I[telephone_ring]");
      instrument.addItem("I[helicopter]");
      instrument.addItem("I[applause]");
      instrument.addItem("I[gunshot]");

      Dimension instrumentDimension = instrument.getPreferredSize();
      double instrumentWidth = instrumentDimension.getWidth();
      double instrumentHeight = instrumentDimension.getHeight();
      instrumentDimension.setSize(instrumentWidth + 3.0, instrumentHeight);
      instrument.setMaximumSize(instrumentDimension);

      instrument.addActionListener (new
		   ActionListener()
		      {
				   public void actionPerformed(ActionEvent event)
				   {
					   String instrumentconstant = (String)instrument.getSelectedItem();
					   if (editor.getSelectedText() == null)
					   {
						   editor.insert(" " + instrumentconstant + " ", editor.getCaretPosition());
					   }
					   else
					   {
						   editor.replaceSelection(" " + instrumentconstant + " ");
						}
						instrument.setVisible(false);
					}
			});

      percussion      = new JComboBox<String>();
      percussion.addItem("[acoustic_bass_drum]");
      percussion.addItem("[bass_drum]");
      percussion.addItem("[side_stick]");
      percussion.addItem("[acoustic_snare]");
      percussion.addItem("[hand_clap]");

      percussion.addItem("[electric_snare]");
      percussion.addItem("[low_floor_tom]");
      percussion.addItem("[closed_hi_hat]");
      percussion.addItem("[high_floor_tom]");
      percussion.addItem("[pedal_hi_hat]");
      percussion.addItem("[low_tom]");
      percussion.addItem("[open_hi_hat]");
      percussion.addItem("[low_mid_tom]");
      percussion.addItem("[hi_mid_tom]");
      percussion.addItem("[crash_cymbal_1]");

      percussion.addItem("[high_tom]");
      percussion.addItem("[ride_cymbal_1]");
      percussion.addItem("[chinese_cymbal]");
      percussion.addItem("[ride_bell]");
      percussion.addItem("[tambourine]");
      percussion.addItem("[splash_cymbal]");
      percussion.addItem("[cowbell]");
      percussion.addItem("[crash_cymbal_2]");
      percussion.addItem("[vibraslap]");
      percussion.addItem("[ride_cymbal_2]");

      percussion.addItem("[hi_bongo]");
      percussion.addItem("[low_bongo]");
      percussion.addItem("[mute_hi_conga]");
      percussion.addItem("[open_hi_conga]");
      percussion.addItem("[low_conga]");
      percussion.addItem("[high_timbale]");
      percussion.addItem("[low_timbale]");
      percussion.addItem("[high_agogo]");
      percussion.addItem("[low_agogo]");
      percussion.addItem("[cabasa]");

      percussion.addItem("[maracas]");
      percussion.addItem("[short_whistle]");
      percussion.addItem("[long_whistle]");
      percussion.addItem("[short_guiro]");
      percussion.addItem("[long_guiro]");
      percussion.addItem("[claves]");
      percussion.addItem("[hi_wood_block]");
      percussion.addItem("[low_wood_block]");
      percussion.addItem("[mute_cuica]");
      percussion.addItem("[open_cuica]");

      percussion.addItem("[mute_triangle]");
      percussion.addItem("[open_triangle]");

      Dimension percussionDimension = percussion.getPreferredSize();
      double percussionWidth = percussionDimension.getWidth();
      double percussionHeight = percussionDimension.getHeight();
      percussionDimension.setSize(percussionWidth + 3.0, percussionHeight);
      percussion.setMaximumSize(percussionDimension);

      percussion.addActionListener (new
		   ActionListener()
		      {
				   public void actionPerformed(ActionEvent event)
				   {
					   String percussionconstant = (String)percussion.getSelectedItem();
					   if (editor.getSelectedText() == null)
					   {
						   editor.insert(percussionconstant, editor.getCaretPosition());
					   }
					   else
					   {
						   editor.replaceSelection(percussionconstant);
					   }
						//percussion.setVisible(false);
						editor.requestFocusInWindow();
					}
			});

      controller = new JComboBox<String>();
      controller.addItem("X[bank_select_coarse]=");
      controller.addItem("X[mod_wheel_coarse]=");
      controller.addItem("X[breath_coarse]=");
      controller.addItem("X[foot_pedal_coarse]=");
      controller.addItem("X[portamento_time_coarse]=");
      controller.addItem("X[data_entry_coarse]=");
      controller.addItem("X[volume_coarse]=");
      controller.addItem("X[balance_coarse]=");
      controller.addItem("X[pan_position_coarse]=");
      controller.addItem("X[expression_coarse]=");
      controller.addItem("X[effect_control_1_coarse]=");
      controller.addItem("X[effect_control_2_coarse]=");

      controller.addItem("X[slider_1]=");
      controller.addItem("X[slider_2]=");
      controller.addItem("X[slider_3]=");
      controller.addItem("X[slider_4]=");

      controller.addItem("X[bank_select_fine]=");
      controller.addItem("X[mod_wheel_fine]=");
      controller.addItem("X[breath_fine]=");
      controller.addItem("X[foot_pedal_fine]=");
      controller.addItem("X[portamento_time_fine]=");
      controller.addItem("X[data_entry_fine]=");
      controller.addItem("X[volume_fine]=");
      controller.addItem("X[balance_fine]=");
      controller.addItem("X[pan_position_fine]=");
      controller.addItem("X[expression_fine]=");
      controller.addItem("X[effect_control_1_fine]=");
      controller.addItem("X[effect_control_2_fine]=");

      controller.addItem("X[hold_pedal]=");
      controller.addItem("X[hold]=");
      controller.addItem("X[portamento]=");
      controller.addItem("X[sustenuto_pedal]=");
      controller.addItem("X[sustenuto]=");
      controller.addItem("X[soft_pedal]=");
      controller.addItem("X[soft]=");
      controller.addItem("X[legato_pedal]=");
      controller.addItem("X[legato]=");
      controller.addItem("X[hold_2_pedal]=");
      controller.addItem("X[hold_2]=");

      controller.addItem("X[sound_variation]=");
      controller.addItem("X[variation]=");
      controller.addItem("X[sound_timbre]=");
      controller.addItem("X[timbre]=");
      controller.addItem("X[sound_release_time]=");
      controller.addItem("X[release_time]=");
      controller.addItem("X[sound_attack_time]=");
      controller.addItem("X[attack_time]=");
      controller.addItem("X[sound_brightness]=");
      controller.addItem("X[brightness]=");
      controller.addItem("X[sound_control_6]=");
      controller.addItem("X[control_6]=");
      controller.addItem("X[sound_control_7]=");
      controller.addItem("X[control_7]=");
      controller.addItem("X[sound_control_8]=");
      controller.addItem("X[control_8]=");
      controller.addItem("X[sound_control_9]=");
      controller.addItem("X[control_9]=");
      controller.addItem("X[sound_control_10]=");
      controller.addItem("X[control_10]=");

      controller.addItem("X[general_purpose_button_1]=");
      controller.addItem("X[general_button_1]=");
      controller.addItem("X[button_1]=");
      controller.addItem("X[general_purpose_button_2]=");
      controller.addItem("X[general_button_2]=");
      controller.addItem("X[button_2]=");
      controller.addItem("X[general_purpose_button_3]=");
      controller.addItem("X[general_button_3]=");
      controller.addItem("X[button_3]=");
      controller.addItem("X[general_purpose_button_4]=");
      controller.addItem("X[general_button_4]=");
      controller.addItem("X[button_4]=");

      controller.addItem("X[effects_level]=");
      controller.addItem("X[effects]=");
      controller.addItem("X[tremulo_level]=");
      controller.addItem("X[tremulo]=");
      controller.addItem("X[chorus_level]=");
      controller.addItem("X[chorus]=");
      controller.addItem("X[celeste_level]=");
      controller.addItem("X[celeste]=");
      controller.addItem("X[phaser_level]=");
      controller.addItem("X[phaser]=");

      controller.addItem("X[data_button_increment]=");
      controller.addItem("X[data_button_inc]=");
      controller.addItem("X[button_inc]=");
      controller.addItem("X[data_button_decrement]=");
      controller.addItem("X[data_button_dec]=");
      controller.addItem("X[button_dec]=");

      controller.addItem("X[non_registered_coarse]=");
      controller.addItem("X[non_registered_fine]=");
      controller.addItem("X[registered_coarse]=");
      controller.addItem("X[registered_fine]=");

      controller.addItem("X[all_sound_off]=");
      controller.addItem("X[all_controllers_off]=");
      controller.addItem("X[local_keyboard]=");
      controller.addItem("X[all_notes_off]=");
      controller.addItem("X[omni_mode_off]=");
      controller.addItem("X[omni_off]=");
      controller.addItem("X[omni_mode_on]=");
      controller.addItem("X[omni_on]=");
      controller.addItem("X[mono_operation]=");
      controller.addItem("X[mono]=");
      controller.addItem("X[poly_operation]=");
      controller.addItem("X[poly]=");

 //
 // combined controller names
 // (index = coarse_controller_index * 128 + fine_controller_index)
 //
      controller.addItem("X[bank_select]=");
      controller.addItem("X[mod_wheel]=");
      controller.addItem("X[breath]=");
      controller.addItem("X[foot_pedal]=");
      controller.addItem("X[portamento_time]=");
      controller.addItem("X[data_entry]=");
      controller.addItem("X[volume]=");
      controller.addItem("X[balance]=");
      controller.addItem("X[pan_position]=");
      controller.addItem("X[expression]=");
      controller.addItem("X[effect_control_1]=");
      controller.addItem("X[effect_control_2]=");
      controller.addItem("X[non_registered]=");
      controller.addItem("X[registered]=");

      Dimension controllerDimension = controller.getPreferredSize();
      double controllerWidth = controllerDimension.getWidth();
      double controllerHeight = controllerDimension.getHeight();
      controllerDimension.setSize(controllerWidth + 3.0, controllerHeight);
      controller.setMaximumSize(controllerDimension);

      controller.addActionListener (new
		   ActionListener()
		      {
				   public void actionPerformed(ActionEvent event)
				   {
					   String controllerconstant = (String)controller.getSelectedItem();
					   if (editor.getSelectedText() == null)
					   {
						   editor.insert(" " + controllerconstant, editor.getCaretPosition());
					   }
					   else
					   {
							editor.replaceSelection(" " + controllerconstant);
					   }
						controller.setVisible(false);
					}
			});

      fileMenu.add(numericconstantsItem);
      fileMenu.add(constantsItem);
      fileMenu.addSeparator();

      fileMenu.add(tempoItem);
      fileMenu.add(instrumentItem);
      fileMenu.add(percussionItem);
      fileMenu.add(controllerItem);
      fileMenu.addSeparator();
      fileMenu.add(cancelInsertItem);

      menuBar.add(numericconstants);
      numericconstants.setVisible(false);
      menuBar.add(constants);
      constants.setVisible(false);

      menuBar.add(tempo);
      tempo.setVisible(false);
      menuBar.add(instrument);
      instrument.setVisible(false);
      menuBar.add(percussion );
      percussion.setVisible(false);
      menuBar.add(controller);
      controller.setVisible(false);

      JMenu helpMenu = new JMenu("Pick");
      menuBar.add(helpMenu);

      //JMenuItem jfugueDocumentationItem    = new JMenuItem("Documentation");
      //helpMenu.add(jfugueDocumentationItem);

      //helpMenu.addSeparator();

      JMenu clefsMenu = new JMenu("Pick from staves");
      helpMenu.add(clefsMenu);

      JMenuItem sopranoItem = new JMenuItem("Treble");
      clefsMenu.add(sopranoItem);
      JMenuItem treble8Item = new JMenuItem("Treble-8");
      clefsMenu.add(treble8Item);
      JMenuItem altoItem = new JMenuItem("Alto");
      clefsMenu.add(altoItem);
      JMenuItem tenorItem = new JMenuItem("Tenor");
      clefsMenu.add(tenorItem);
      JMenuItem bassItem = new JMenuItem("Bass");
      clefsMenu.add(bassItem);

      helpMenu.addSeparator();

      JMenuItem durationsItem = new JMenuItem("Pick durations");
      helpMenu.add(durationsItem);

      //JFugueDocumentationAction jfugueDocumentationAction = new JFugueDocumentationAction();
      //jfugueDocumentationItem.addActionListener(jfugueDocumentationAction);

      SopranoAction sopranoAction = new SopranoAction();
      sopranoItem.addActionListener(sopranoAction);

      Treble8Action treble8Action = new Treble8Action();
      treble8Item.addActionListener(treble8Action);

      AltoAction altoAction = new AltoAction();
      altoItem.addActionListener(altoAction);

      TenorAction tenorAction = new TenorAction();
      tenorItem.addActionListener(tenorAction);

      BassAction bassAction = new BassAction();
      bassItem.addActionListener(bassAction);

      DurationsAction durationsAction = new DurationsAction();
      durationsItem.addActionListener(durationsAction);

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      //JPanel titlePanel = new JPanel();
      //titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,0));

      JPanel newPanel = new JPanel();
      newPanel.setLayout(new BorderLayout());
      newPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
      JPanel newPanelN = new JPanel();
      newPanelN.setLayout(new BorderLayout());
      newPanelN.setBorder(BorderFactory.createEmptyBorder(5,5,0,0));
      JPanel newPanelS = new JPanel();
      newPanelS.setLayout(new BorderLayout());
      newPanelS.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
      newPanel.add(newPanelN, BorderLayout.NORTH);
      newPanel.add(newPanelS, BorderLayout.SOUTH);

      Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      JLabel songField = new JLabel("Song: ");
      songField.setFont(labelFont);
      Dimension dim1 = songField.getPreferredSize();
	   dim1.width = 65;
      songField.setPreferredSize(dim1);
      newPanelN.add(songField, BorderLayout.WEST);

      JLabel songNameField = new JLabel(song_name);
      songNameField.setFont(titleFont);
      newPanelN.add(songNameField, BorderLayout.CENTER);

      JLabel stringField = new JLabel("String: ");
      stringField.setFont(labelFont);
      Dimension dim2 = stringField.getPreferredSize();
	   dim2.width = 65;
      stringField.setPreferredSize(dim2);
      newPanelS.add(stringField, BorderLayout.WEST);

      JLabel stringNameField = new JLabel(string_name);
      stringNameField.setFont(titleFont);
      newPanelS.add(stringNameField, BorderLayout.CENTER);

      //Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      //Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);

      //JLabel labelField = new JLabel("String: ");
      //labelField.setFont(labelFont);
      //titlePanel.add(labelField);

      //JLabel titleField = new JLabel(string_name);
      //titleField.setEditable(false);
      //titleField.setFont(titleFont);
      //Dimension dim1 = titleField.getPreferredSize();
		//dim1.width = 400;
      //titleField.setPreferredSize(dim1);
      //titlePanel.add(titleField);
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(newPanel, BorderLayout.NORTH);

      editor = new JTextArea(string_value);

      editor.addFocusListener(myFocusListener);

      int blinkrate = editor.getCaret().getBlinkRate();

      editor.setCaret(new DefaultCaret() {
		   @Override
		   public void setSelectionVisible(boolean visible) {
		      super.setSelectionVisible(true);
		   }
      });

      editor.getCaret().setBlinkRate(blinkrate);

      Font stringFont = new Font("Liberation Mono", Font.PLAIN, 14);
      editor.setFont(stringFont);

 //     Font font = editor.getFont();
 //     font = font.deriveFont(Collections.singletonMap(TextAttribute.BACKGROUND, new Color(238,238,238)));
 //     editor.setFont(font);

 //     editor.setSelectedTextColor(Color.RED);

      JScrollPane editorPane = new JScrollPane(editor);
      editorPane.setBorder(BorderFactory.createCompoundBorder(
                           BorderFactory.createEmptyBorder(0,20,0,20),
                           editorPane.getBorder()));

      contentPane.add(editorPane, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();

      //JButton okButton     = new JButton("OK");
      JButton okButton     = new JButton(okButtonText);
      //JButton cancelButton = new JButton("Cancel");
      JButton cancelButton = new JButton(cancelButtonText);

      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      OKAction okAction         = new OKAction();
      CancelAction cancelAction = new CancelAction();

      okButton.addActionListener(okAction);
      cancelButton.addActionListener(cancelAction);
   }

   public String getStringValue()
   {
      return editor.getText();
   }

   public int getSelection()
   {
      return selection;
   }

   private class OKAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selection = 0;
         setVisible(false);
      }
   }

   private class CancelAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selection = 1;
         setVisible(false);
      }
   }

   private void quit()
   {
         selection = 1;
         setVisible(false);
	}

   private class NumericconstantsAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {

         constants.setVisible(false);
         instrument.setVisible(false);
         percussion.setVisible(false);
         controller.setVisible(false);
			tempo.setVisible(false);
			if (numericconstants.getItemCount() > 0)
			numericconstants.setVisible(true);
			else
			Messages.plainMessage(frame, title, "No numeric constants defined.");

      }
   }

   private class ConstantsAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {

         numericconstants.setVisible(false);

         instrument.setVisible(false);
         percussion.setVisible(false);
         controller.setVisible(false);
			tempo.setVisible(false);
			if (constants.getItemCount() > 0)
			constants.setVisible(true);
			else
			Messages.plainMessage(frame, title, "No string constants defined.");
      }
   }

   private class TempoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {

         numericconstants.setVisible(false);
         constants.setVisible(false);
         instrument.setVisible(false);
         percussion.setVisible(false);
         controller.setVisible(false);
			tempo.setVisible(true);

      }
   }

   private class InstrumentAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         numericconstants.setVisible(false);
         constants.setVisible(false);
         tempo.setVisible(false);

         percussion.setVisible(false);
         controller.setVisible(false);
			instrument.setVisible(true);

      }
   }

   private class PercussionAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         numericconstants.setVisible(false);
         constants.setVisible(false);
         tempo.setVisible(false);
         instrument.setVisible(false);

         controller.setVisible(false);
			percussion.setVisible(true);

      }
   }

   private class ControllerAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         numericconstants.setVisible(false);
         constants.setVisible(false);
         tempo.setVisible(false);
         instrument.setVisible(false);
         percussion.setVisible(false);

			controller.setVisible(true);

      }
   }

   private class CancelInsertAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         numericconstants.setVisible(false);
         constants.setVisible(false);
         tempo.setVisible(false);
         instrument.setVisible(false);
         percussion.setVisible(false);
         controller.setVisible(false);
         editor.requestFocusInWindow();
      }
   }

//   private class JFugueDocumentationAction implements ActionListener
//   {
//      public void actionPerformed(ActionEvent a)
//      {
//
//         try
//         {
//			   Desktop myDesktop = Desktop.getDesktop();
//            //myDesktop.browse(new URI("http://jfugue.org/jfugue-chapter2.pdf"));
//            //if (System.getProperty("os.name").startsWith("Windows"))
//            myDesktop.open(new File("doc/AudoviaDocumentation.pdf"));
//            //else
//            //myDesktop.open(new File("/opt/SongBase/doc/MusicStringDocumentation.pdf"));
//
//            //editor.requestFocusInWindow();
//         }
//         catch (Exception e)
//         {
//            Messages.exceptionHandler(frame, title, e);
//         }
//         editor.requestFocusInWindow();
//      }
//   }

   private class SopranoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog sopranoFrame = new JDialog(frame);

            sopranoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sopranoFrame.setTitle("Treble");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            sopranoFrame.setIconImage(icon.getImage());

            //sopranoFrame.setSize(730,170);
            sopranoFrame.setLocation(125,550);
            sopranoFrame.setVisible(true);

            //Container contentPane = sopranoFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(710,140)); // added
            contentPane.setLayout(null);

            ImageIcon sopranoIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "TrebleClef.png");

            JLabel sopranoLabel = new JLabel(sopranoIcon);

            sopranoLabel.setBounds(25,25,663,62);

            contentPane.add(sopranoLabel);

            JLabel note1 = new JLabel("C5");
            JLabel note2 = new JLabel("C#5");
            JLabel note3 = new JLabel("D5");
            JLabel note4 = new JLabel("D#5");
            JLabel note5 = new JLabel("E5");
            JLabel note6 = new JLabel("F5");
            JLabel note7 = new JLabel("F#5");
            JLabel note8 = new JLabel("G5");
            JLabel note9 = new JLabel("G#5");
            JLabel note10 = new JLabel("A5");
            JLabel note11 = new JLabel("A#5");
            JLabel note12 = new JLabel("B5");
            JLabel note13 = new JLabel("C6");
            JLabel note14 = new JLabel("C#6");
            JLabel note15 = new JLabel("D6");
            JLabel note16 = new JLabel("D#6");
            JLabel note17 = new JLabel("E6");
            JLabel note18 = new JLabel("F6");
            JLabel note19 = new JLabel("F#6");
            JLabel note20 = new JLabel("G6");
            JLabel note21 = new JLabel("G#6");
            JLabel note22 = new JLabel("A6");

            note1.setVerticalAlignment(JLabel.BOTTOM);
            note2.setVerticalAlignment(JLabel.BOTTOM);
            note3.setVerticalAlignment(JLabel.BOTTOM);
            note4.setVerticalAlignment(JLabel.BOTTOM);
            note5.setVerticalAlignment(JLabel.BOTTOM);
            note6.setVerticalAlignment(JLabel.BOTTOM);
            note7.setVerticalAlignment(JLabel.BOTTOM);
            note8.setVerticalAlignment(JLabel.BOTTOM);
            note9.setVerticalAlignment(JLabel.BOTTOM);
            note10.setVerticalAlignment(JLabel.BOTTOM);
            note11.setVerticalAlignment(JLabel.BOTTOM);
            note12.setVerticalAlignment(JLabel.BOTTOM);
            note13.setVerticalAlignment(JLabel.BOTTOM);
            note14.setVerticalAlignment(JLabel.BOTTOM);
            note15.setVerticalAlignment(JLabel.BOTTOM);
            note16.setVerticalAlignment(JLabel.BOTTOM);
            note17.setVerticalAlignment(JLabel.BOTTOM);
            note18.setVerticalAlignment(JLabel.BOTTOM);
            note19.setVerticalAlignment(JLabel.BOTTOM);
            note20.setVerticalAlignment(JLabel.BOTTOM);
            note21.setVerticalAlignment(JLabel.BOTTOM);
            note22.setVerticalAlignment(JLabel.BOTTOM);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);
            note8.addMouseListener(myMouseListener);
            note9.addMouseListener(myMouseListener);
            note10.addMouseListener(myMouseListener);
            note11.addMouseListener(myMouseListener);
            note12.addMouseListener(myMouseListener);
            note13.addMouseListener(myMouseListener);
            note14.addMouseListener(myMouseListener);
            note15.addMouseListener(myMouseListener);
            note16.addMouseListener(myMouseListener);
            note17.addMouseListener(myMouseListener);
            note18.addMouseListener(myMouseListener);
            note19.addMouseListener(myMouseListener);
            note20.addMouseListener(myMouseListener);
            note21.addMouseListener(myMouseListener);
            note22.addMouseListener(myMouseListener);

            note1.setBounds(67,25,20,95);
            contentPane.add(note1);
            note2.setBounds(88,25,28,95);
            contentPane.add(note2);
            note3.setBounds(122,25,20,95);
            contentPane.add(note3);
            note4.setBounds(144,25,28,95);
            contentPane.add(note4);
            note5.setBounds(178,25,20,95);
            contentPane.add(note5);
            note6.setBounds(206,25,20,95);
            contentPane.add(note6);
            note7.setBounds(228,25,28,95);
            contentPane.add(note7);
            note8.setBounds(263,25,20,95);
            contentPane.add(note8);
            note9.setBounds(285,25,28,95);
            contentPane.add(note9);
            note10.setBounds(319,25,20,95);
            contentPane.add(note10);
            note11.setBounds(341,25,28,95);
            contentPane.add(note11);
            note12.setBounds(375,25,20,95);
            contentPane.add(note12);
            note13.setBounds(403,25,20,95);
            contentPane.add(note13);
            note14.setBounds(426,25,28,95);
            contentPane.add(note14);
            note15.setBounds(458,25,20,95);
            contentPane.add(note15);
            note16.setBounds(482,25,28,95);
            contentPane.add(note16);
            note17.setBounds(515,25,20,95);
            contentPane.add(note17);
            note18.setBounds(544,25,20,95);
            contentPane.add(note18);
            note19.setBounds(566,25,28,95);
            contentPane.add(note19);
            note20.setBounds(600,25,20,95);
            contentPane.add(note20);
            note21.setBounds(623,25,28,95);
            contentPane.add(note21);
            note22.setBounds(657,25,20,95);
            contentPane.add(note22);

            sopranoFrame.add(contentPane); // added
            sopranoFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class Treble8Action implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog sopranoFrame = new JDialog(frame);

            sopranoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sopranoFrame.setTitle("Treble-8");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            sopranoFrame.setIconImage(icon.getImage());

            //sopranoFrame.setSize(730,170);
            sopranoFrame.setLocation(200,550);
            sopranoFrame.setVisible(true);

            //Container contentPane = sopranoFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(710,140)); // added
            contentPane.setLayout(null);

            ImageIcon sopranoIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "TrebleClef.png");

            JLabel sopranoLabel = new JLabel(sopranoIcon);

            sopranoLabel.setBounds(25,25,663,62);

            contentPane.add(sopranoLabel);

            JLabel octave8 = new JLabel("8");
            octave8.setOpaque(true);
            octave8.setBackground(Color.white);

            JLabel note1 = new JLabel("C4");
            JLabel note2 = new JLabel("C#4");
            JLabel note3 = new JLabel("D4");
            JLabel note4 = new JLabel("D#4");
            JLabel note5 = new JLabel("E4");
            JLabel note6 = new JLabel("F4");
            JLabel note7 = new JLabel("F#4");
            JLabel note8 = new JLabel("G4");
            JLabel note9 = new JLabel("G#4");
            JLabel note10 = new JLabel("A4");
            JLabel note11 = new JLabel("A#4");
            JLabel note12 = new JLabel("B4");
            JLabel note13 = new JLabel("C5");
            JLabel note14 = new JLabel("C#5");
            JLabel note15 = new JLabel("D5");
            JLabel note16 = new JLabel("D#5");
            JLabel note17 = new JLabel("E5");
            JLabel note18 = new JLabel("F5");
            JLabel note19 = new JLabel("F#5");
            JLabel note20 = new JLabel("G5");
            JLabel note21 = new JLabel("G#5");
            JLabel note22 = new JLabel("A5");

            octave8.setVerticalAlignment(JLabel.TOP);
            octave8.setHorizontalAlignment(JLabel.CENTER);

            note1.setVerticalAlignment(JLabel.BOTTOM);
            note2.setVerticalAlignment(JLabel.BOTTOM);
            note3.setVerticalAlignment(JLabel.BOTTOM);
            note4.setVerticalAlignment(JLabel.BOTTOM);
            note5.setVerticalAlignment(JLabel.BOTTOM);
            note6.setVerticalAlignment(JLabel.BOTTOM);
            note7.setVerticalAlignment(JLabel.BOTTOM);
            note8.setVerticalAlignment(JLabel.BOTTOM);
            note9.setVerticalAlignment(JLabel.BOTTOM);
            note10.setVerticalAlignment(JLabel.BOTTOM);
            note11.setVerticalAlignment(JLabel.BOTTOM);
            note12.setVerticalAlignment(JLabel.BOTTOM);
            note13.setVerticalAlignment(JLabel.BOTTOM);
            note14.setVerticalAlignment(JLabel.BOTTOM);
            note15.setVerticalAlignment(JLabel.BOTTOM);
            note16.setVerticalAlignment(JLabel.BOTTOM);
            note17.setVerticalAlignment(JLabel.BOTTOM);
            note18.setVerticalAlignment(JLabel.BOTTOM);
            note19.setVerticalAlignment(JLabel.BOTTOM);
            note20.setVerticalAlignment(JLabel.BOTTOM);
            note21.setVerticalAlignment(JLabel.BOTTOM);
            note22.setVerticalAlignment(JLabel.BOTTOM);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);
            note8.addMouseListener(myMouseListener);
            note9.addMouseListener(myMouseListener);
            note10.addMouseListener(myMouseListener);
            note11.addMouseListener(myMouseListener);
            note12.addMouseListener(myMouseListener);
            note13.addMouseListener(myMouseListener);
            note14.addMouseListener(myMouseListener);
            note15.addMouseListener(myMouseListener);
            note16.addMouseListener(myMouseListener);
            note17.addMouseListener(myMouseListener);
            note18.addMouseListener(myMouseListener);
            note19.addMouseListener(myMouseListener);
            note20.addMouseListener(myMouseListener);
            note21.addMouseListener(myMouseListener);
            note22.addMouseListener(myMouseListener);

            octave8.setBounds(25,87,30,30);
            contentPane.add(octave8);

            note1.setBounds(67,25,20,95);
            contentPane.add(note1);
            note2.setBounds(88,25,28,95);
            contentPane.add(note2);
            note3.setBounds(122,25,20,95);
            contentPane.add(note3);
            note4.setBounds(144,25,28,95);
            contentPane.add(note4);
            note5.setBounds(178,25,20,95);
            contentPane.add(note5);
            note6.setBounds(206,25,20,95);
            contentPane.add(note6);
            note7.setBounds(228,25,28,95);
            contentPane.add(note7);
            note8.setBounds(263,25,20,95);
            contentPane.add(note8);
            note9.setBounds(285,25,28,95);
            contentPane.add(note9);
            note10.setBounds(319,25,20,95);
            contentPane.add(note10);
            note11.setBounds(341,25,28,95);
            contentPane.add(note11);
            note12.setBounds(375,25,20,95);
            contentPane.add(note12);
            note13.setBounds(403,25,20,95);
            contentPane.add(note13);
            note14.setBounds(426,25,28,95);
            contentPane.add(note14);
            note15.setBounds(458,25,20,95);
            contentPane.add(note15);
            note16.setBounds(482,25,28,95);
            contentPane.add(note16);
            note17.setBounds(515,25,20,95);
            contentPane.add(note17);
            note18.setBounds(544,25,20,95);
            contentPane.add(note18);
            note19.setBounds(566,25,28,95);
            contentPane.add(note19);
            note20.setBounds(600,25,20,95);
            contentPane.add(note20);
            note21.setBounds(623,25,28,95);
            contentPane.add(note21);
            note22.setBounds(657,25,20,95);
            contentPane.add(note22);

            sopranoFrame.add(contentPane); // added
            sopranoFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class AltoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog altoFrame = new JDialog(frame);

            altoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            altoFrame.setTitle("Alto");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            altoFrame.setIconImage(icon.getImage());

            //altoFrame.setSize(730,170);
            altoFrame.setLocation(150,575);
            altoFrame.setVisible(true);

            //Container contentPane = altoFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(710,140)); // added
            contentPane.setLayout(null);

            ImageIcon altoIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "AltoClef.png");

            JLabel altoLabel = new JLabel(altoIcon);

            altoLabel.setBounds(25,25,663,62);

            contentPane.add(altoLabel);

            JLabel note1 = new JLabel("D4");
            JLabel note2 = new JLabel("D#4");
            JLabel note3 = new JLabel("E4");
            JLabel note4 = new JLabel("F4");
            JLabel note5 = new JLabel("F#4");
            JLabel note6 = new JLabel("G4");
            JLabel note7 = new JLabel("G#4");
            JLabel note8 = new JLabel("A4");
            JLabel note9 = new JLabel("A#4");
            JLabel note10 = new JLabel("B4");
            JLabel note11 = new JLabel("C5");
            JLabel note12 = new JLabel("C#5");
            JLabel note13 = new JLabel("D5");
            JLabel note14 = new JLabel("D#5");
            JLabel note15 = new JLabel("E5");
            JLabel note16 = new JLabel("F5");
            JLabel note17 = new JLabel("F#5");
            JLabel note18 = new JLabel("G5");
            JLabel note19 = new JLabel("G#5");
            JLabel note20 = new JLabel("A5");
            JLabel note21 = new JLabel("A#5");
            JLabel note22 = new JLabel("B5");

            note1.setVerticalAlignment(JLabel.BOTTOM);
            note2.setVerticalAlignment(JLabel.BOTTOM);
            note3.setVerticalAlignment(JLabel.BOTTOM);
            note4.setVerticalAlignment(JLabel.BOTTOM);
            note5.setVerticalAlignment(JLabel.BOTTOM);
            note6.setVerticalAlignment(JLabel.BOTTOM);
            note7.setVerticalAlignment(JLabel.BOTTOM);
            note8.setVerticalAlignment(JLabel.BOTTOM);
            note9.setVerticalAlignment(JLabel.BOTTOM);
            note10.setVerticalAlignment(JLabel.BOTTOM);
            note11.setVerticalAlignment(JLabel.BOTTOM);
            note12.setVerticalAlignment(JLabel.BOTTOM);
            note13.setVerticalAlignment(JLabel.BOTTOM);
            note14.setVerticalAlignment(JLabel.BOTTOM);
            note15.setVerticalAlignment(JLabel.BOTTOM);
            note16.setVerticalAlignment(JLabel.BOTTOM);
            note17.setVerticalAlignment(JLabel.BOTTOM);
            note18.setVerticalAlignment(JLabel.BOTTOM);
            note19.setVerticalAlignment(JLabel.BOTTOM);
            note20.setVerticalAlignment(JLabel.BOTTOM);
            note21.setVerticalAlignment(JLabel.BOTTOM);
            note22.setVerticalAlignment(JLabel.BOTTOM);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);
            note8.addMouseListener(myMouseListener);
            note9.addMouseListener(myMouseListener);
            note10.addMouseListener(myMouseListener);
            note11.addMouseListener(myMouseListener);
            note12.addMouseListener(myMouseListener);
            note13.addMouseListener(myMouseListener);
            note14.addMouseListener(myMouseListener);
            note15.addMouseListener(myMouseListener);
            note16.addMouseListener(myMouseListener);
            note17.addMouseListener(myMouseListener);
            note18.addMouseListener(myMouseListener);
            note19.addMouseListener(myMouseListener);
            note20.addMouseListener(myMouseListener);
            note21.addMouseListener(myMouseListener);
            note22.addMouseListener(myMouseListener);

            note1.setBounds(68,25,20,95);
            contentPane.add(note1);
            note2.setBounds(90,25,28,95);
            contentPane.add(note2);
            note3.setBounds(123,25,20,95);
            contentPane.add(note3);
            note4.setBounds(150,25,20,95);
            contentPane.add(note4);
            note5.setBounds(173,25,28,95);
            contentPane.add(note5);
            note6.setBounds(207,25,20,95);
            contentPane.add(note6);
            note7.setBounds(229,25,28,95);
            contentPane.add(note7);
            note8.setBounds(264,25,20,95);
            contentPane.add(note8);
            note9.setBounds(286,25,28,95);
            contentPane.add(note9);
            note10.setBounds(320,25,20,95);
            contentPane.add(note10);
            note11.setBounds(347,25,20,95);
            contentPane.add(note11);
            note12.setBounds(371,25,28,95);
            contentPane.add(note12);
            note13.setBounds(404,25,20,95);
            contentPane.add(note13);
            note14.setBounds(426,25,28,95);
            contentPane.add(note14);
            note15.setBounds(460,25,20,95);
            contentPane.add(note15);
            note16.setBounds(487,25,20,95);
            contentPane.add(note16);
            note17.setBounds(510,25,28,95);
            contentPane.add(note17);
            note18.setBounds(544,25,20,95);
            contentPane.add(note18);
            note19.setBounds(566,25,28,95);
            contentPane.add(note19);
            note20.setBounds(601,25,20,95);
            contentPane.add(note20);
            note21.setBounds(623,25,28,95);
            contentPane.add(note21);
            note22.setBounds(657,25,20,95);
            contentPane.add(note22);

            altoFrame.add(contentPane); // added
            altoFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class TenorAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog tenorFrame = new JDialog(frame);

            tenorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tenorFrame.setTitle("Tenor");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            tenorFrame.setIconImage(icon.getImage());

            //tenorFrame.setSize(730,170);
            tenorFrame.setLocation(175,600);
            tenorFrame.setVisible(true);

            //Container contentPane = tenorFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(710,140)); // added
            contentPane.setLayout(null);

            ImageIcon tenorIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "TenorClef.png");

            JLabel tenorLabel = new JLabel(tenorIcon);

            tenorLabel.setBounds(25,25,663,62);

            contentPane.add(tenorLabel);

            JLabel note1 = new JLabel("B3");
            JLabel note2 = new JLabel("C4");
            JLabel note3 = new JLabel("C#4");
            JLabel note4 = new JLabel("D4");
            JLabel note5 = new JLabel("D#4");
            JLabel note6 = new JLabel("E4");
            JLabel note7 = new JLabel("F4");
            JLabel note8 = new JLabel("F#4");
            JLabel note9 = new JLabel("G4");
            JLabel note10 = new JLabel("G#4");
            JLabel note11 = new JLabel("A4");
            JLabel note12 = new JLabel("A#4");
            JLabel note13 = new JLabel("B4");
            JLabel note14 = new JLabel("C5");
            JLabel note15 = new JLabel("C#5");
            JLabel note16 = new JLabel("D5");
            JLabel note17 = new JLabel("D#5");
            JLabel note18 = new JLabel("E5");
            JLabel note19 = new JLabel("F5");
            JLabel note20 = new JLabel("F#5");
            JLabel note21 = new JLabel("G5");
            JLabel note22 = new JLabel("G#5");

            note1.setVerticalAlignment(JLabel.BOTTOM);
            note2.setVerticalAlignment(JLabel.BOTTOM);
            note3.setVerticalAlignment(JLabel.BOTTOM);
            note4.setVerticalAlignment(JLabel.BOTTOM);
            note5.setVerticalAlignment(JLabel.BOTTOM);
            note6.setVerticalAlignment(JLabel.BOTTOM);
            note7.setVerticalAlignment(JLabel.BOTTOM);
            note8.setVerticalAlignment(JLabel.BOTTOM);
            note9.setVerticalAlignment(JLabel.BOTTOM);
            note10.setVerticalAlignment(JLabel.BOTTOM);
            note11.setVerticalAlignment(JLabel.BOTTOM);
            note12.setVerticalAlignment(JLabel.BOTTOM);
            note13.setVerticalAlignment(JLabel.BOTTOM);
            note14.setVerticalAlignment(JLabel.BOTTOM);
            note15.setVerticalAlignment(JLabel.BOTTOM);
            note16.setVerticalAlignment(JLabel.BOTTOM);
            note17.setVerticalAlignment(JLabel.BOTTOM);
            note18.setVerticalAlignment(JLabel.BOTTOM);
            note19.setVerticalAlignment(JLabel.BOTTOM);
            note20.setVerticalAlignment(JLabel.BOTTOM);
            note21.setVerticalAlignment(JLabel.BOTTOM);
            note22.setVerticalAlignment(JLabel.BOTTOM);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);
            note8.addMouseListener(myMouseListener);
            note9.addMouseListener(myMouseListener);
            note10.addMouseListener(myMouseListener);
            note11.addMouseListener(myMouseListener);
            note12.addMouseListener(myMouseListener);
            note13.addMouseListener(myMouseListener);
            note14.addMouseListener(myMouseListener);
            note15.addMouseListener(myMouseListener);
            note16.addMouseListener(myMouseListener);
            note17.addMouseListener(myMouseListener);
            note18.addMouseListener(myMouseListener);
            note19.addMouseListener(myMouseListener);
            note20.addMouseListener(myMouseListener);
            note21.addMouseListener(myMouseListener);
            note22.addMouseListener(myMouseListener);

            note1.setBounds(68,25,20,95);
            contentPane.add(note1);
            note2.setBounds(95,25,20,95);
            contentPane.add(note2);
            note3.setBounds(118,25,28,95);
            contentPane.add(note3);
            note4.setBounds(151,25,20,95);
            contentPane.add(note4);
            note5.setBounds(173,25,28,95);
            contentPane.add(note5);
            note6.setBounds(207,25,20,95);
            contentPane.add(note6);
            note7.setBounds(234,25,20,95);
            contentPane.add(note7);
            note8.setBounds(257,25,28,95);
            contentPane.add(note8);
            note9.setBounds(290,25,20,95);
            contentPane.add(note9);
            note10.setBounds(315,25,28,95);
            contentPane.add(note10);
            note11.setBounds(349,25,20,95);
            contentPane.add(note11);
            note12.setBounds(371,25,28,95);
            contentPane.add(note12);
            note13.setBounds(403,25,20,95);
            contentPane.add(note13);
            note14.setBounds(431,25,20,95);
            contentPane.add(note14);
            note15.setBounds(454,25,28,95);
            contentPane.add(note15);
            note16.setBounds(487,25,20,95);
            contentPane.add(note16);
            note17.setBounds(510,25,28,95);
            contentPane.add(note17);
            note18.setBounds(544,25,20,95);
            contentPane.add(note18);
            note19.setBounds(571,25,20,95);
            contentPane.add(note19);
            note20.setBounds(595,25,28,95);
            contentPane.add(note20);
            note21.setBounds(628,25,20,95);
            contentPane.add(note21);
            note22.setBounds(651,25,28,95);
            contentPane.add(note22);

            tenorFrame.add(contentPane); // added
            tenorFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class BassAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog bassFrame = new JDialog(frame);

            bassFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            bassFrame.setTitle("Bass");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            bassFrame.setIconImage(icon.getImage());

            //bassFrame.setSize(730,170);
            bassFrame.setLocation(200,625);
            bassFrame.setVisible(true);

            //Container contentPane = bassFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(710,140)); // added
            contentPane.setLayout(null);

            ImageIcon bassIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "BassClef.png");

            JLabel bassLabel = new JLabel(bassIcon);

            bassLabel.setBounds(25,25,663,62);

            contentPane.add(bassLabel);

            JLabel note1 = new JLabel("E3");
            JLabel note2 = new JLabel("F3");
            JLabel note3 = new JLabel("F#3");
            JLabel note4 = new JLabel("G3");
            JLabel note5 = new JLabel("G#3");
            JLabel note6 = new JLabel("A3");
            JLabel note7 = new JLabel("A#3");
            JLabel note8 = new JLabel("B3");
            JLabel note9 = new JLabel("C4");
            JLabel note10 = new JLabel("C#4");
            JLabel note11 = new JLabel("D4");
            JLabel note12 = new JLabel("D#4");
            JLabel note13 = new JLabel("E4");
            JLabel note14 = new JLabel("F4");
            JLabel note15 = new JLabel("F#4");
            JLabel note16 = new JLabel("G4");
            JLabel note17 = new JLabel("G#4");
            JLabel note18 = new JLabel("A4");
            JLabel note19 = new JLabel("A#4");
            JLabel note20 = new JLabel("B4");
            JLabel note21 = new JLabel("C5");
            JLabel note22 = new JLabel("C#5");

            note1.setVerticalAlignment(JLabel.BOTTOM);
            note2.setVerticalAlignment(JLabel.BOTTOM);
            note3.setVerticalAlignment(JLabel.BOTTOM);
            note4.setVerticalAlignment(JLabel.BOTTOM);
            note5.setVerticalAlignment(JLabel.BOTTOM);
            note6.setVerticalAlignment(JLabel.BOTTOM);
            note7.setVerticalAlignment(JLabel.BOTTOM);
            note8.setVerticalAlignment(JLabel.BOTTOM);
            note9.setVerticalAlignment(JLabel.BOTTOM);
            note10.setVerticalAlignment(JLabel.BOTTOM);
            note11.setVerticalAlignment(JLabel.BOTTOM);
            note12.setVerticalAlignment(JLabel.BOTTOM);
            note13.setVerticalAlignment(JLabel.BOTTOM);
            note14.setVerticalAlignment(JLabel.BOTTOM);
            note15.setVerticalAlignment(JLabel.BOTTOM);
            note16.setVerticalAlignment(JLabel.BOTTOM);
            note17.setVerticalAlignment(JLabel.BOTTOM);
            note18.setVerticalAlignment(JLabel.BOTTOM);
            note19.setVerticalAlignment(JLabel.BOTTOM);
            note20.setVerticalAlignment(JLabel.BOTTOM);
            note21.setVerticalAlignment(JLabel.BOTTOM);
            note22.setVerticalAlignment(JLabel.BOTTOM);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);
            note8.addMouseListener(myMouseListener);
            note9.addMouseListener(myMouseListener);
            note10.addMouseListener(myMouseListener);
            note11.addMouseListener(myMouseListener);
            note12.addMouseListener(myMouseListener);
            note13.addMouseListener(myMouseListener);
            note14.addMouseListener(myMouseListener);
            note15.addMouseListener(myMouseListener);
            note16.addMouseListener(myMouseListener);
            note17.addMouseListener(myMouseListener);
            note18.addMouseListener(myMouseListener);
            note19.addMouseListener(myMouseListener);
            note20.addMouseListener(myMouseListener);
            note21.addMouseListener(myMouseListener);
            note22.addMouseListener(myMouseListener);

            note1.setBounds(68,25,20,95);
            contentPane.add(note1);
            note2.setBounds(95,25,20,95);
            contentPane.add(note2);
            note3.setBounds(118,25,28,95);
            contentPane.add(note3);
            note4.setBounds(151,25,20,95);
            contentPane.add(note4);
            note5.setBounds(173,25,28,95);
            contentPane.add(note5);
            note6.setBounds(207,25,20,95);
            contentPane.add(note6);
            note7.setBounds(232,25,28,95);
            contentPane.add(note7);
            note8.setBounds(263,25,20,95);
            contentPane.add(note8);
            note9.setBounds(290,25,20,95);
            contentPane.add(note9);
            note10.setBounds(315,25,28,95);
            contentPane.add(note10);
            note11.setBounds(347,25,20,95);
            contentPane.add(note11);
            note12.setBounds(371,25,28,95);
            contentPane.add(note12);
            note13.setBounds(404,25,20,95);
            contentPane.add(note13);
            note14.setBounds(431,25,20,95);
            contentPane.add(note14);
            note15.setBounds(454,25,28,95);
            contentPane.add(note15);
            note16.setBounds(487,25,20,95);
            contentPane.add(note16);
            note17.setBounds(510,25,28,95);
            contentPane.add(note17);
            note18.setBounds(544,25,20,95);
            contentPane.add(note18);
            note19.setBounds(568,25,28,95);
            contentPane.add(note19);
            note20.setBounds(599,25,20,95);
            contentPane.add(note20);
            note21.setBounds(628,25,20,95);
            contentPane.add(note21);
            note22.setBounds(651,25,28,95);
            contentPane.add(note22);

            bassFrame.add(contentPane); // added
            bassFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class DurationsAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JDialog durationsFrame = new JDialog(frame);

            durationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            durationsFrame.setTitle("Durations");

            ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
            durationsFrame.setIconImage(icon.getImage());

            //durationsFrame.setSize(184,354); // was 125
            durationsFrame.setLocation(735,150);
            durationsFrame.setVisible(true);

            //Container contentPane = durationsFrame.getContentPane();
            JPanel contentPane = new JPanel(); // added
            contentPane.setPreferredSize(new Dimension(166,324)); // added
            contentPane.setLayout(null);

            ImageIcon durationsIcon = new ImageIcon(System.getProperty("image.dir") + "/" + "NoteDurations.png");

            JLabel durationsLabel = new JLabel(durationsIcon);

            durationsLabel.setBounds(20,25,47,266);

            contentPane.add(durationsLabel);

            JLabel note1 = new JLabel("w");
            JLabel note2 = new JLabel("h");
            JLabel note3 = new JLabel("q");
            JLabel note4 = new JLabel("i");
            JLabel note5 = new JLabel("s");
            JLabel note6 = new JLabel("t");
            JLabel note7 = new JLabel("x");

            note1.setVerticalAlignment(JLabel.CENTER);
            note2.setVerticalAlignment(JLabel.CENTER);
            note3.setVerticalAlignment(JLabel.CENTER);
            note4.setVerticalAlignment(JLabel.CENTER);
            note5.setVerticalAlignment(JLabel.CENTER);
            note6.setVerticalAlignment(JLabel.CENTER);
            note7.setVerticalAlignment(JLabel.CENTER);

            note1.setHorizontalAlignment(JLabel.RIGHT);
            note2.setHorizontalAlignment(JLabel.RIGHT);
            note3.setHorizontalAlignment(JLabel.RIGHT);
            note4.setHorizontalAlignment(JLabel.RIGHT);
            note5.setHorizontalAlignment(JLabel.RIGHT);
            note6.setHorizontalAlignment(JLabel.RIGHT);
            note7.setHorizontalAlignment(JLabel.RIGHT);

            MyMouseListener myMouseListener = new MyMouseListener();
            note1.addMouseListener(myMouseListener);
            note2.addMouseListener(myMouseListener);
            note3.addMouseListener(myMouseListener);
            note4.addMouseListener(myMouseListener);
            note5.addMouseListener(myMouseListener);
            note6.addMouseListener(myMouseListener);
            note7.addMouseListener(myMouseListener);

            note1.setBounds(20,27,67,34);
            contentPane.add(note1);
            note2.setBounds(20,65,67,34);
            contentPane.add(note2);
            note3.setBounds(20,102,67,34);
            contentPane.add(note3);
            note4.setBounds(20,138,67,34);
            contentPane.add(note4);
            note5.setBounds(20,175,67,34);
            contentPane.add(note5);
            note6.setBounds(20,212,67,34);
            contentPane.add(note6);
            note7.setBounds(20,248,67,34);
            contentPane.add(note7);

            JButton plusButton   = new JButton("+");
            JButton minusButton   = new JButton("-");
            JButton spaceButton   = new JButton(" ");
            JButton dotButton   = new JButton(".");
            JButton restButton   = new JButton("R");

            plusButton.setBounds(110,25,40,30);
            contentPane.add(plusButton);

            minusButton.setBounds(110,60,40,30);
            contentPane.add(minusButton);

            spaceButton.setBounds(110,95,40,126);
            contentPane.add(spaceButton);

            dotButton.setBounds(110,226,40,30);
            contentPane.add(dotButton);

            restButton.setBounds(110,261,40,30);
            contentPane.add(restButton);

            ButtonAction buttonAction = new ButtonAction();
            SpaceButtonAction spacebuttonAction = new SpaceButtonAction();

            plusButton.addActionListener(buttonAction);
            minusButton.addActionListener(buttonAction);
            spaceButton.addActionListener(spacebuttonAction);
            dotButton.addActionListener(buttonAction);
            restButton.addActionListener(buttonAction);

            durationsFrame.add(contentPane); // added
            durationsFrame.pack(); // added
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
         editor.requestFocusInWindow(); // doesn't work
      }
   }

   private class MyMouseListener implements MouseListener
   {
      public void mouseClicked(MouseEvent e)
      {
         //JLabel currentLabel = (JLabel)e.getComponent();
         //String note = currentLabel.getText();
			//if (editor.getSelectedText() == null)
			//{
			//	editor.insert(note, editor.getCaretPosition());
			//}
			//else
			//{
			//	editor.replaceSelection(note);
			//}

			//editor.requestFocusInWindow();  // doesn't work

      }

      public void mouseEntered(MouseEvent e)
      {
         JLabel currentLabel = (JLabel)e.getComponent();
         currentLabel.setForeground(Color.RED);
      }
      public void mouseExited(MouseEvent e)
      {
         JLabel currentLabel = (JLabel)e.getComponent();
         currentLabel.setForeground(Color.BLACK);
      }

      public void mousePressed(MouseEvent e)
      {
			editor.getHighlighter().removeAllHighlights();

         JLabel currentLabel = (JLabel)e.getComponent();
         String note = currentLabel.getText();
			if (editor.getSelectedText() == null)
			{
				editor.insert(note, editor.getCaretPosition());
			}
			else
			{
				editor.replaceSelection(note);
			}

			editor.requestFocusInWindow();  // doesn't work
      }

      public void mouseReleased(MouseEvent e)
      {

      }
   }

   private class ButtonAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         String text = a.getActionCommand();

         editor.getHighlighter().removeAllHighlights();

			if (editor.getSelectedText() == null)
			{
				editor.insert(text, editor.getCaretPosition());
			}
			else
			{
				editor.replaceSelection(text);
			}

			editor.requestFocusInWindow();  // doesn't work
      }
   }

   private class SpaceButtonAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         String text = a.getActionCommand();

			if (editor.getSelectedText() == null)
			{
				editor.insert(text, editor.getCaretPosition());

				    try
				    {
				    DefaultHighlighter.DefaultHighlightPainter highlightPainter =
				        new DefaultHighlighter.DefaultHighlightPainter(new Color(238,238,238));
				    editor.getHighlighter().addHighlight(editor.getCaretPosition() - 1, editor.getCaretPosition(),
                highlightPainter);
				    }
                catch (Exception e)
                {
                Messages.exceptionHandler(frame, title, e);
                }
			}
			else
			{
				editor.replaceSelection(text);

				    try
				    {
				    DefaultHighlighter.DefaultHighlightPainter highlightPainter =
				        new DefaultHighlighter.DefaultHighlightPainter(new Color(238,238,238));
				    editor.getHighlighter().addHighlight(editor.getCaretPosition() - 1, editor.getCaretPosition(),
                highlightPainter);
				    }
                catch (Exception e)
                {
                Messages.exceptionHandler(frame, title, e);
                }
			}

			editor.requestFocusInWindow();  // doesn't work
      }
   }

   private class MyFocusListener implements FocusListener
   {
      public void focusGained(FocusEvent e)
      {
         editor.getHighlighter().removeAllHighlights();
      }

      public void focusLost(FocusEvent e)
      {
   	}
   }
}
