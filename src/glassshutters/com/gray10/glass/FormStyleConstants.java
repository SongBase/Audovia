/*
 * FormStyleConstants.java - Script Generator for Glass Shutters to NT specifications, all corners rounded.
 * Copyright (C) 2018  Donald G Gray
 *
 * http://glass.gray10.com/
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
package com.gray10.glass;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.sql.*;
import java.io.*;
import javax.swing.UIManager.*;
import java.text.*;

public class FormStyleConstants extends JDialog
{
	/*
	 * version 3.3
	 *
	 */

   private JTextField endGapField = new JTextField();
   private JTextField sideGapField = new JTextField();
   private JTextField intGapField = new JTextField();
   private JTextField endCentreField = new JTextField();
   private JTextField sideCentreField = new JTextField();
   private JTextField intCentreField = new JTextField();
   private JTextField sideSpaceField = new JTextField();
   private JTextField intSpaceField = new JTextField();
   private JTextField endCatchField = new JTextField();
   private JTextField sideCatchField = new JTextField();
   private JTextField extDiaField = new JTextField();
   private JTextField intDiaField = new JTextField();
   private JTextField catchDiaField = new JTextField();

   private JDialog frame = FormStyleConstants.this;

   private static String title = "Constants";

   public FormStyleConstants()
   {
   endGapField.setText(Double.toString(Constants.endGap));
   sideGapField.setText(Double.toString(Constants.sideGap));
   intGapField.setText(Double.toString(Constants.intGap));
   endCentreField.setText(Double.toString(Constants.endCentre));
   sideCentreField.setText(Double.toString(Constants.sideCentre));
   intCentreField.setText(Double.toString(Constants.intCentre));
   sideSpaceField.setText(Double.toString(Constants.sideSpace));
   intSpaceField.setText(Double.toString(Constants.intSpace));
   endCatchField.setText(Double.toString(Constants.endCatch));
   sideCatchField.setText(Double.toString(Constants.sideCatch));
   extDiaField.setText(Constants.extDia);
   intDiaField.setText(Constants.intDia);
   catchDiaField.setText(Constants.catchDia);

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      setModal(true);
      setSize(320,620);
      setLocation(100,80);
      setTitle(title);

      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JLabel endGapLabel = new JLabel("endGap");
      endGapLabel.setBounds(25,20,175,30);
      contentPane.add(endGapLabel);
      endGapField.setBounds(200,20,65,30);
      contentPane.add(endGapField);

      JLabel sideGapLabel = new JLabel("sideGap");
      sideGapLabel.setBounds(25,50,175,30);
      contentPane.add(sideGapLabel);
      sideGapField.setBounds(200,50,65,30);
      contentPane.add(sideGapField);

      JLabel intGapLabel = new JLabel("intGap");
      intGapLabel.setBounds(25,80,175,30);
      contentPane.add(intGapLabel);
      intGapField.setBounds(200,80,65,30);
      contentPane.add(intGapField);

      JLabel endCentreLabel = new JLabel("endCentre");
      endCentreLabel.setBounds(25,110,175,30);
      contentPane.add(endCentreLabel);
      endCentreField.setBounds(200,110,65,30);
      contentPane.add(endCentreField);

      JLabel sideCentreLabel = new JLabel("sideCentre");
      sideCentreLabel.setBounds(25,140,175,30);
      contentPane.add(sideCentreLabel);
      sideCentreField.setBounds(200,140,65,30);
      contentPane.add(sideCentreField);

      JLabel intCentreLabel = new JLabel("intCentre");
      intCentreLabel.setBounds(25,170,175,30);
      contentPane.add(intCentreLabel);
      intCentreField.setBounds(200,170,65,30);
      contentPane.add(intCentreField);

      JLabel sideSpaceLabel = new JLabel("sideSpace");
      sideSpaceLabel.setBounds(25,200,175,30);
      contentPane.add(sideSpaceLabel);
      sideSpaceField.setBounds(200,200,65,30);
      contentPane.add(sideSpaceField);

      JLabel intSpaceLabel = new JLabel("intSpace");
      intSpaceLabel.setBounds(25,230,175,30);
      contentPane.add(intSpaceLabel);
      intSpaceField.setBounds(200,230,65,30);
      contentPane.add(intSpaceField);

      JLabel endCatchLabel = new JLabel("endCatch");
      endCatchLabel.setBounds(25,260,175,30);
      contentPane.add(endCatchLabel);
      endCatchField.setBounds(200,260,65,30);
      contentPane.add(endCatchField);

      JLabel sideCatchLabel = new JLabel("sideCatch");
      sideCatchLabel.setBounds(25,290,175,30);
      contentPane.add(sideCatchLabel);
      sideCatchField.setBounds(200,290,65,30);
      contentPane.add(sideCatchField);

      JLabel extDiaLabel = new JLabel("extDia");
      extDiaLabel.setBounds(25,320,175,30);
      contentPane.add(extDiaLabel);
      extDiaField.setBounds(200,320,65,30);
      contentPane.add(extDiaField);

      JLabel intDiaLabel = new JLabel("intDia");
      intDiaLabel.setBounds(25,350,175,30);
      contentPane.add(intDiaLabel);
      intDiaField.setBounds(200,350,65,30);
      contentPane.add(intDiaField);

      JLabel catchDiaLabel = new JLabel("catchDia");
      catchDiaLabel.setBounds(25,380,175,30);
      contentPane.add(catchDiaLabel);
      catchDiaField.setBounds(200,380,65,30);
      contentPane.add(catchDiaField);

      JLabel format = new JLabel("<html><center>Format must be as shown including leading and embedded spaces.</center></html>");
      format.setBounds(25,440,240,60);
      format.setVerticalAlignment(JLabel.TOP);
      contentPane.add(format);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Continue");
      buttonPanel.add(goButton);
      buttonPanel.setBounds(25,500,240,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();

      goButton.addActionListener(goAction);
	}

   public double getendGap()
   {
      return Double.parseDouble(endGapField.getText());
   }

   public double getsideGap()
   {
      return Double.parseDouble(sideGapField.getText());
   }

   public double getintGap()
   {
      return Double.parseDouble(intGapField.getText());
   }

   public double getendCentre()
   {
      return Double.parseDouble(endCentreField.getText());
   }

   public double getsideCentre()
   {
      return Double.parseDouble(sideCentreField.getText());
   }

   public double getintCentre()
   {
      return Double.parseDouble(intCentreField.getText());
   }

   public double getsideSpace()
   {
      return Double.parseDouble(sideSpaceField.getText());
   }

   public double getintSpace()
   {
      return Double.parseDouble(intSpaceField.getText());
   }

   public double getendCatch()
   {
      return Double.parseDouble(endCatchField.getText());
   }

   public double getsideCatch()
   {
      return Double.parseDouble(sideCatchField.getText());
   }

   public String getextDia()
   {
      return extDiaField.getText();
   }

   public String getintDia()
   {
      return intDiaField.getText();
   }

   public String getcatchDia()
   {
      return catchDiaField.getText();
   }

   private class GoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         setVisible(false);
      }
   }
}