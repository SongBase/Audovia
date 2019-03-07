/*
 * SBSTimeSignatureClef.java - Time Signature snd Clef popup for MusicXML export
 * Copyright (C) 2010, 2017  Donald G Gray
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

package org.jfugue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SBSTimeSignatureClef extends JDialog
{
	/*
	 * version 3.6.3
	 *
	 */

   private static final long serialVersionUID = 1L;

   private JComboBox<String>      beatsBox;
   private JComboBox<String>      beat_typeBox;
   private JComboBox<String>      signBox;
   private JComboBox<String>      lineBox;
   private JTextField             voiceField;

   private int selection = 1;

   public SBSTimeSignatureClef(String voice)
   {
      setTitle("SBSTimeSignatureClef");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,370);
      setLocation(250,160);
      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      JLabel titleField = new JLabel("Time Signature and Clef");
      Font titleFont = new Font("Liberation Sans", Font.BOLD, 18);
      titleField.setFont(titleFont);
      titleField.setForeground(new Color(85, 26, 139));
      titlePanel.add(titleField);
      titlePanel.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
      contentPane.add(titlePanel, BorderLayout.NORTH);

      JPanel inputPanel = new JPanel();
      inputPanel.setSize(200,190);
      inputPanel.setLayout(null);

      Font voiceFont = new Font("Liberation Sans", Font.BOLD, 15);

      JLabel voiceLabel = new JLabel("voice:");
      voiceLabel.setFont(voiceFont);
      voiceLabel.setBounds(25,0,75,30);
      inputPanel.add(voiceLabel);

      voiceField = new JTextField(voice);
      voiceField.setEditable(false);
      voiceField.setFont(voiceFont);
      voiceField.setBounds(100,0,165,30);
      inputPanel.add(voiceField);

      JLabel beatsLabel = new JLabel("beats:");
      beatsLabel.setBounds(25,50,75,30);
      inputPanel.add(beatsLabel);

      String[] beats_types = {"2", "3", "4", "6", "9", "12"};
      beatsBox = new JComboBox<String>(beats_types);
      beatsBox.setSelectedItem(SBSConstants.beats);
      beatsBox.setBounds(100,50,165,30);
      inputPanel.add(beatsBox);

      JLabel beat_typeLabel = new JLabel("beat-type:");
      beat_typeLabel.setBounds(25,80,75,30);
      inputPanel.add(beat_typeLabel);

      String[] beat_type_types = {"2", "4", "8"};
      beat_typeBox = new JComboBox<String>(beat_type_types);
      beat_typeBox.setSelectedItem(SBSConstants.beat_type);
      beat_typeBox.setBounds(100,80,165,30);
      inputPanel.add(beat_typeBox);

      JLabel signLabel = new JLabel("sign:");
      signLabel.setBounds(25,130,75,30);
      inputPanel.add(signLabel);

      String[] sign_types = {"G", "C", "F"};
      signBox = new JComboBox<String>(sign_types);
      signBox.setSelectedItem(SBSConstants.sign);
      signBox.setBounds(100,130,165,30);
      inputPanel.add(signBox);

      JLabel lineLabel = new JLabel("line:");
      lineLabel.setBounds(25,160,75,30);
      inputPanel.add(lineLabel);

      String[] line_types = {"5", "4", "3", "2", "1"};
      lineBox = new JComboBox<String>(line_types);
      lineBox.setSelectedItem(SBSConstants.line);
      lineBox.setBounds(100,160,165,30);
      inputPanel.add(lineBox);

      inputPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
      contentPane.add(inputPanel, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("OK");

      buttonPanel.add(goButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      GoAction goAction = new GoAction();

      goButton.addActionListener(goAction);
   }

   public String getBeats()
   {
      return (String)beatsBox.getSelectedItem();
   }

   public String getBeatType()
   {
      return (String)beat_typeBox.getSelectedItem();
   }

   public String getSign()
   {
      return (String)signBox.getSelectedItem();
   }

   public String getLine()
   {
      return (String)lineBox.getSelectedItem();
   }

   public int getSelection()
   {
      return selection;
   }

   private class GoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selection = 0;
         setVisible(false);
      }
   }
}