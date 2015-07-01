/*
 * TextAreaEditor.java
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
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class TextAreaEditor extends DefaultCellEditor
{
	/*
	 * version 1.2.12
	 *
	 */

   private JTextArea textarea;
   private JFrame frame;
   private Field keyField;
   private String filter = null;

   public TextAreaEditor(JFrame aJFrame, Field aKeyField)
   {
      super(new JTextField());
      frame = aJFrame;
      keyField = aKeyField;
      setClickCountToStart(2);
   }

   public Component getTableCellEditorComponent(JTable table,
                                                Object value,
                                                boolean isSelected,
                                                int row,
                                                int column)
   {
      textarea = new JTextArea();

      ScrollPane scrollpane = new ScrollPane(textarea);

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

      textarea.setDocument(textDocument);
      textarea.setLineWrap(true);
      textarea.setWrapStyleWord(true);
      textarea.setBackground(Color.yellow);
      textarea.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

      scrollpane.getViewport().add(textarea);

      FocusAction focusAction = new FocusAction();
      scrollpane.addFocusListener(focusAction);

      Font stringFont = new Font("Liberation Mono", Font.PLAIN, 14);
      textarea.setFont(stringFont);
      //textarea.setFont(table.getFont());
      textarea.setText((value != null) ? (String)value : "");

      table.changeSelection(row, column, false, false);

      return scrollpane;
   }

   public Object getCellEditorValue()
   {
      return textarea.getText();
   }

   public void setFilter(String aFilter)
   {
      filter = aFilter;
   }

   private class FocusAction implements FocusListener
   {
      public void focusGained(FocusEvent fe)
      {
         try
         {
            char c = keyField.getChar(frame);
            Character keyCharacter = new Character(c);
            String keyString = new String(keyCharacter.toString());
            if (filter == null || keyString.matches(filter))
            {
               textarea.append(keyString);
            }
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, "Text Area Editor", e);
         }
         textarea.requestFocusInWindow();
      }
      public void focusLost(FocusEvent fe)
      {
      }
   }
}