/*
 * IntegerFieldRenderer.java
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
package com.gray10.audovia;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.*;

public class IntegerFieldRenderer implements TableCellRenderer
{
	/*
	 * version 1.0
	 *
	 */

   private JTextField textfield;

   public IntegerFieldRenderer()
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
//         textfield.setBackground(table.getSelectionBackground());
         textfield.setBackground(new Color(128,0,255));
//         textfield.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
//         textfield.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),2));
         textfield.setBorder(BorderFactory.createLineBorder(new Color(128,0,255), 2));
      }
      else
      {
         textfield.setForeground(table.getForeground());
         if (row % 2 == 0)
         {
            textfield.setBackground(new Color(238,238,238));
            textfield.setBorder(BorderFactory.createLineBorder(new Color(238,238,238),2));
         }
         else
         {
            //textfield.setBackground(table.getBackground());
            textfield.setBackground(new Color(255,255,255));
            textfield.setBorder(BorderFactory.createLineBorder(new Color(255,255,255),2));
         }
//         textfield.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
         if (hasFocus)
         {
//            textfield.setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),2));
            textfield.setBorder(BorderFactory.createLineBorder(new Color(128,0,255), 2));
         }
      }
      textfield.setFont(table.getFont());
      textfield.setText((value != null) ? ((Integer)value).toString() : "");
      textfield.setCaretPosition(0);
      return textfield;
   }
}
