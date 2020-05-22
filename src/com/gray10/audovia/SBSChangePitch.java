/*
 * SBSChangePitch.java - Change Pitch popup
 * Copyright (C) 2017  Donald G Gray
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
package com.gray10.audovia;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SBSChangePitch extends JDialog
{
	/*
	 * version 3.4.0
	 *
	 */
   private static final long serialVersionUID = 1L;

   private JComboBox<String>      pitchBox;

   private int selection = 1;

   public SBSChangePitch()
   {
      setTitle("SBSChangePitch");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,270);
      setLocation(250,160);
      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      JLabel titleField = new JLabel("Change Pitch");
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

      JLabel pitchLabel = new JLabel("semitones:");
      pitchLabel.setBounds(25,30,75,30);
      inputPanel.add(pitchLabel);

      String[] semitones = {"12","11","10","9","8","7","6","5","4","3","2","1","0","-1","-2","-3","-4","-5","-6","-7","-8","-9","-10","-11","-12"};
      pitchBox = new JComboBox<String>(semitones);
      pitchBox.setSelectedItem("0");
      pitchBox.setBounds(100,30,165,30);
      inputPanel.add(pitchBox);

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

   public String getPitch()
   {
      return (String)pitchBox.getSelectedItem();
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