/*
 * Search.java
 * Copyright (C) 2009  Donald G Gray
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

public class Search extends JDialog
{
	/*
	 * version 1.0
	 *
	 */

   private JTextField searchField;
   private int selection = 1;

   public Search(String title)
   {
      setTitle(title);

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      setModal(true);
      setSize(300,145);
      setLocation(250,235);
      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JLabel searchLabel = new JLabel("Find:");
      searchLabel.setBounds(25,20,50,30);
      contentPane.add(searchLabel);

      searchField = new JTextField();
      searchField.setBounds(75,20,190,30);
      contentPane.add(searchField);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Go");
      JButton exitButton = new JButton("Cancel");
      buttonPanel.add(goButton);
      buttonPanel.add(exitButton);
      buttonPanel.setBounds(50,60,200,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();
      ExitAction exitAction = new ExitAction();

      goButton.addActionListener(goAction);
      exitButton.addActionListener(exitAction);
   }

   public String getSearch()
   {
      return searchField.getText();
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