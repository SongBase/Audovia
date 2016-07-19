/*
 * StringFieldRenderer.java
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

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.*;

public class StringFieldRenderer implements TableCellRenderer
{
	/*
	 * version 2.0.0
	 *
	 */

   private JTextField textfield;

   public StringFieldRenderer()
   {
      textfield = new JTextField();
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
         textfield.setForeground(table.getSelectionForeground());
         textfield.setBackground(table.getSelectionBackground());
         textfield.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
      }
      else
      {
         textfield.setForeground(table.getForeground());
         if (row % 2 == 0)
         {
            textfield.setBackground(new Color(238,238,238));
         }
         else
         {
            //textfield.setBackground(table.getBackground());
            textfield.setBackground(new Color(255,255,255));
         }
         textfield.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
         if (hasFocus)
         {
            textfield.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),2));
         }
      }
      Font stringFont = new Font("Liberation Mono", Font.PLAIN, 14);
      textfield.setFont(stringFont);
      //textfield.setFont(table.getFont());
      textfield.setText((String)value);
      textfield.setCaretPosition(0);
      return textfield;
   }
}