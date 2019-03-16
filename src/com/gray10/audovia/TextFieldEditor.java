/*
 * TextFieldEditor.java
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
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class TextFieldEditor extends DefaultCellEditor
{
	/*
	 * version 1.0
	 *
	 */

   private static final long serialVersionUID = 1L;

   private JTextField textfield;
   private String filter = null;

   public TextFieldEditor()
   {
      super(new JTextField());
      setClickCountToStart(2);
   }

   public Component getTableCellEditorComponent(JTable table,
                                                Object value,
                                                boolean isSelected,
                                                int row,
                                                int column)
   {
      textfield = new JTextField();

      DocumentFilter textFilter = new DocumentFilter()
      {
         public void replace(FilterBypass fb, int offset, int length,
                             String text, AttributeSet attr)
                     throws BadLocationException
         {
            if (filter == null || text.matches(filter))
            {
               super.replace(fb, offset, length, text, attr);
            }
         }
      };

      PlainDocument textDocument = new PlainDocument();
      textDocument.setDocumentFilter(textFilter);

      textfield.setDocument(textDocument);
      textfield.setBackground(Color.yellow);
      textfield.setFont(table.getFont());
      textfield.setText((value != null) ? (String)value : "");

      table.changeSelection(row, column, false, false);

      return textfield;
   }

   public Object getCellEditorValue()
   {
      return textfield.getText();
   }

   public void setFilter(String aFilter)
   {
      filter = aFilter;
   }
}