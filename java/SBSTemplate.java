/*
 * SBSTemplate.java - Template Generator for Audovia
 * Copyright (C) 2011 - 2016  Donald G Gray
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
import javax.swing.*;
import java.sql.*;
import java.io.*;
import javax.swing.UIManager.*;
import javax.xml.parsers.*;

public class SBSTemplate extends JDialog
{
	/*
	 * version 3.1.1
	 *
	 */

	//private static String userName = System.getProperty("user.name");

   private int selection = 1;

   private JComboBox<String> voicesBox;
   private JComboBox<String> partsBox;
   private JComboBox<String> barsBox;
   private JComboBox<String> typeBox;
   private static String  no_voices;
   private static String  no_parts;
   private static String  no_bars;
   private static String  type_of_bar;

   private JDialog frame = SBSTemplate.this;

   private static String title = "Song Template";

   public SBSTemplate()
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

      setModal(true);
      setSize(335,260);
      setLocation(100,100);
      setTitle(title);

      ImageIcon icon = new ImageIcon("SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JLabel voicesLabel = new JLabel("Number of Voices");
      voicesLabel.setBounds(25,20,185,30);
      contentPane.add(voicesLabel);

      String[] voices = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
      voicesBox = new JComboBox<String>(voices);
      voicesBox.setSelectedIndex(0);
      voicesBox.setBounds(210,20,65,30);
      contentPane.add(voicesBox);

      JLabel partsLabel = new JLabel("Number of Parts per Voice");
      partsLabel.setBounds(25,50,185,30);
      contentPane.add(partsLabel);

      String[] parts = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
      partsBox = new JComboBox<String>(parts);
      partsBox.setSelectedIndex(0);
      partsBox.setBounds(210,50,65,30);
      contentPane.add(partsBox);

      JLabel barsLabel = new JLabel("Number of Bars per Part");
      barsLabel.setBounds(25,80,185,30);
      contentPane.add(barsLabel);

      String[] bars = {"8","12","16","24","32","48","64","96","128","192","256"};
      barsBox = new JComboBox<String>(bars);
      barsBox.setSelectedIndex(0);
      barsBox.setBounds(210,80,65,30);
      contentPane.add(barsBox);

      JLabel typeLabel = new JLabel("Type of Bar");
      typeLabel.setBounds(25,110,185,30);
      contentPane.add(typeLabel);

      String[] types = {"string","pattern"};
      typeBox = new JComboBox<String>(types);
      typeBox.setSelectedIndex(0);
      typeBox.setBounds(210,110,85,30); // was 75
      contentPane.add(typeBox);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Continue");
      buttonPanel.add(goButton);
      buttonPanel.setBounds(25,170,240,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();

      goButton.addActionListener(goAction);
	}

   public int getSelection()
   {
      return selection;
   }

   public String getVoices()
   {
      return (String)voicesBox.getSelectedItem();
   }

   public String getParts()
   {
      return (String)partsBox.getSelectedItem();
   }

   public String getBars()
   {
      return (String)barsBox.getSelectedItem();
   }

   public String getTypeOfBar()
   {
      return (String)typeBox.getSelectedItem();
   }

   private class GoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selection = 0;
         setVisible(false);
      }
   }

   private void quit()
   {
         selection = 1;
         setVisible(false);
	}
}

