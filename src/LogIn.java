/*
 * LogIn.java
 * Copyright (C) 2015  Donald G Gray
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LogIn extends JDialog
{
	/*
	 * version 3.0.0
	 *
	 */

   private JTextField     userField;
   private JPasswordField passwordField;
   //private JTextField     emailField;

   private int selection = 1;

   public LogIn(String user, String password)
   {
      setTitle("Log in");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,180);
      setLocation(250,160);
      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JLabel userLabel = new JLabel("username:");
      userLabel.setBounds(25,20,75,30);
      contentPane.add(userLabel);

      userField = new JTextField(user);
      userField.setBounds(100,20,165,30);
      contentPane.add(userField);

      JLabel passwordLabel = new JLabel("password:");
      passwordLabel.setBounds(25,50,75,30);
      contentPane.add(passwordLabel);

      passwordField = new JPasswordField(password);
      passwordField.setBounds(100,50,165,30);
      contentPane.add(passwordField);

      //JLabel emailLabel = new JLabel("e-mail:");
      //emailLabel.setBounds(25,80,75,30);
      //contentPane.add(emailLabel);

      //emailField = new JTextField(email);
      //emailField.setBounds(100,80,165,30);
      //contentPane.add(emailField);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Log in");
      JButton exitButton = new JButton("Quit");
      buttonPanel.add(goButton);
      buttonPanel.add(exitButton);
      buttonPanel.setBounds(50,90,200,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();
      ExitAction exitAction = new ExitAction();

      goButton.addActionListener(goAction);
      exitButton.addActionListener(exitAction);
   }

   public String getUser()
   {
      return userField.getText();
   }

   public char[] getPassword()
   {
      return passwordField.getPassword();
   }

   //public String getEmail()
   //{
   //   return emailField.getText();
   //}

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