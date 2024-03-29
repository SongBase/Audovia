/*
 * ConnectionDetails.java
 * Copyright (C) 2010, 2012  Donald G Gray
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

public class ConnectionDetails extends JDialog
{
	/*
	 * version 2.0.4
	 *
	 */

   private JComboBox<String>      db_typeBox;
   private JTextField     hostField;
   private JTextField     portField;
   private JTextField     databaseField;
   private JTextField     userField;
   private JPasswordField passwordField;

   private int selection = 1;

   private static final long serialVersionUID = 1L;

   public ConnectionDetails(String db_type, String host, String port, String database, String user, String password)
   {
      setTitle("Connection Details");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,300);
      setLocation(250,160);
      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JLabel db_typeLabel = new JLabel("type:");
      db_typeLabel.setBounds(25,20,75,30);
      contentPane.add(db_typeLabel);

      String[] db_types = {"Postgres", "Oracle", "MySQL"};
      //String[] db_types = {"Postgres"};
      db_typeBox = new JComboBox<String>(db_types);
      //db_typeBox.setRenderer(new MyComboBoxRenderer());
      db_typeBox.setSelectedItem(db_type);
      db_typeBox.setBounds(100,20,165,30);
      contentPane.add(db_typeBox);

      JLabel hostLabel = new JLabel("hostname:");
      hostLabel.setBounds(25,50,75,30);
      contentPane.add(hostLabel);

      hostField = new JTextField(host);
      hostField.setBounds(100,50,165,30);
      contentPane.add(hostField);

      JLabel portLabel = new JLabel("port:");
      portLabel.setBounds(25,80,75,30);
      contentPane.add(portLabel);

      portField = new JTextField(port);
      portField.setBounds(100,80,165,30);
      contentPane.add(portField);

      JLabel databaseLabel = new JLabel("database:");
      databaseLabel.setBounds(25,110,75,30);
      contentPane.add(databaseLabel);

      databaseField = new JTextField(database);
      databaseField.setBounds(100,110,165,30);
      contentPane.add(databaseField);

      JLabel userLabel = new JLabel("username:");
      userLabel.setBounds(25,140,75,30);
      contentPane.add(userLabel);

      userField = new JTextField(user);
      userField.setBounds(100,140,165,30);
      contentPane.add(userField);

      JLabel passwordLabel = new JLabel("password:");
      passwordLabel.setBounds(25,170,75,30);
      contentPane.add(passwordLabel);

      passwordField = new JPasswordField(password);
      passwordField.setBounds(100,170,165,30);
      contentPane.add(passwordField);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Save");
      JButton exitButton = new JButton("Quit");
      buttonPanel.add(goButton);
      buttonPanel.add(exitButton);
      buttonPanel.setBounds(50,210,200,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();
      ExitAction exitAction = new ExitAction();

      goButton.addActionListener(goAction);
      exitButton.addActionListener(exitAction);
   }

   public String getDbType()
   {
      return (String)db_typeBox.getSelectedItem();
   }

   public String getHost()
   {
      return hostField.getText();
   }

   public String getPort()
   {
      return portField.getText();
   }

   public String getDatabase()
   {
      return databaseField.getText();
   }

   public String getUser()
   {
      return userField.getText();
   }

   public char[] getPassword()
   {
      return passwordField.getPassword();
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

   private class ExitAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selection = 1;
         setVisible(false);
      }
   }
}
