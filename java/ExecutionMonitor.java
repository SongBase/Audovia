/*
 * ExecutionMonitor.java
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
import java.io.*;

public class ExecutionMonitor
{
	/*
	 * version 2.3.18
	 *
	 */

   JFrame frame;
   String title;

   public ExecutionMonitor(String aTitle)
   {
      title = aTitle;
   }

   public void init()
   {
      frame = new JFrame();
      JPanel buttonPanel = new JPanel();
      JButton exitButton = new JButton("Exit");
      frame.setTitle(title);

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      frame.setIconImage(icon.getImage());

      buttonPanel.add(exitButton);
      final JTextArea output = new JTextArea();
      output.setEditable(false);
      frame.getContentPane().add(new JScrollPane(output));
      frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
      frame.setSize(WIDTH, HEIGHT);
      frame.setLocation(LEFT, TOP);
      frame.setVisible(true);

      ExitAction exitAction = new ExitAction();
      exitButton.addActionListener(exitAction);

      PrintStream consoleStream = new PrintStream(new
         OutputStream()
         {
            public void write(int b) {} // never called
            public void write(byte[] b, int off, int len)
            {
               output.append(new String(b, off, len));
            }
         });
      System.setOut(consoleStream);
      System.setErr(consoleStream);
   }

   public static final int WIDTH = 500;
   public static final int HEIGHT = 350;
   public static final int LEFT = 0;
   public static final int TOP = 0;

   public void close()
   {
      frame.setVisible(false);
   }

   private class ExitAction implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         int selection = Messages.warningQuestion(frame, title,
                           "OK to Exit?");
         if (selection == 0)
         {
            System.exit(0);
         }
      }
   }
}
