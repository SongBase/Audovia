/*
 * SBSTimeSignature.java - Time Signature popup for MusicXML export
 * Copyright (C) 2010  Donald G Gray
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

public class SBSTimeSignature extends JDialog
{
	/*
	 * version 1.0
	 *
	 */

   private JComboBox<String>      beatsBox;
   private JComboBox<String>      beat_typeBox;
   private JTextField     partField;

   private int selection = 1;

   public SBSTimeSignature(String part)
   {
      setTitle("SBSTimeSignature");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,270);
      setLocation(250,160);
      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      JLabel titleField = new JLabel("Time Signature");
      Font titleFont = new Font("Liberation Sans", Font.BOLD, 18);
      titleField.setFont(titleFont);
      titleField.setForeground(new Color(85, 26, 139));
      titlePanel.add(titleField);
      titlePanel.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
      contentPane.add(titlePanel, BorderLayout.NORTH);

      JPanel inputPanel = new JPanel();
      inputPanel.setSize(200,90);
      inputPanel.setLayout(null);

      Font partFont = new Font("Liberation Sans", Font.BOLD, 15);

      JLabel partLabel = new JLabel("part:");
      partLabel.setFont(partFont);
      partLabel.setBounds(25,0,75,30);
      inputPanel.add(partLabel);

      partField = new JTextField(part);
      partField.setEditable(false);
      partField.setFont(partFont);
      partField.setBounds(100,0,165,30);
      inputPanel.add(partField);

      JLabel beatsLabel = new JLabel("beats:");
      beatsLabel.setBounds(25,30,75,30);
      inputPanel.add(beatsLabel);

      String[] beats_types = {"2", "3", "4", "6", "9", "12"};
      beatsBox = new JComboBox<String>(beats_types);
      beatsBox.setSelectedItem("4");
      beatsBox.setBounds(100,30,165,30);
      inputPanel.add(beatsBox);

      JLabel beat_typeLabel = new JLabel("beat-type:");
      beat_typeLabel.setBounds(25,60,75,30);
      inputPanel.add(beat_typeLabel);

      String[] beat_type_types = {"2", "4", "8"};
      beat_typeBox = new JComboBox<String>(beat_type_types);
      beat_typeBox.setSelectedItem("4");
      beat_typeBox.setBounds(100,60,165,30);
      inputPanel.add(beat_typeBox);

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