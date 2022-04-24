/*
 * TextAreaRenderer.java
 * Copyright (C) 2009, 2012  Donald G Gray
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

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class TextAreaRenderer implements TableCellRenderer
{
	/*
	 * version 1.2.12
	 *
	 */

   private JTextArea textarea;
   private JScrollPane scrollpane;

   public TextAreaRenderer()
   {
      textarea = new JTextArea();
      textarea.setLineWrap(true);
      textarea.setWrapStyleWord(true);
      textarea.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));  // why?
      scrollpane = new JScrollPane();
      scrollpane.getViewport().add(textarea);
   }

   public Component getTableCellRendererComponent(JTable table,
                                                  Object value,
                                                  boolean isSelected,
                                                  boolean hasFocus,
                                                  int row,
                                                  int column)
   {
      if (isSelected && ! hasFocus)
      {
         scrollpane.setForeground(table.getSelectionForeground());
         scrollpane.setBackground(table.getSelectionBackground());
         textarea.setForeground(table.getSelectionForeground());
         textarea.setBackground(table.getSelectionBackground());
//         scrollpane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
         scrollpane.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),2));
      }
      else
      {
         scrollpane.setForeground(table.getForeground());
         scrollpane.setBackground(table.getBackground());
         textarea.setForeground(table.getForeground());
         if (row % 2 == 0)
         {
            textarea.setBackground(new Color(238,238,238));
            scrollpane.setBorder(BorderFactory.createLineBorder(new Color(238,238,238),2));
         }
         else
         {
	     textarea.setBackground(new Color(255,255,255));
	     scrollpane.setBorder(BorderFactory.createLineBorder(new Color(255,255,255),2));
            //textarea.setBackground(table.getBackground());
         }
//         scrollpane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
         if (hasFocus)
         {
            scrollpane.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),2));
         }
      }

      Font stringFont = new Font("Liberation Mono", Font.PLAIN, 14);
      textarea.setFont(stringFont);
      //textarea.setFont(table.getFont());
      textarea.setText((String)value);
      textarea.setCaretPosition(0);
      return scrollpane;
   }
}
