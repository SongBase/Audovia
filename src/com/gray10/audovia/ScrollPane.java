/*
 * ScrollPane.java
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrollPane extends JScrollPane
{
	/*
	 * version 1.0
	 *
	 */

   private static final long serialVersionUID = 1L;

   private Component component;

   public ScrollPane(Component aComponent)
   {
      super(aComponent);
      component = aComponent;
      MouseAction mouseAction = new MouseAction();
      getHorizontalScrollBar().addMouseListener(mouseAction);
      getVerticalScrollBar().addMouseListener(mouseAction);
   }

   private class MouseAction implements MouseListener
   {
      public void mouseClicked(MouseEvent e)
      {
      }
      public void mousePressed(MouseEvent e)
      {
      }
      public void mouseReleased(MouseEvent e)
      {
         component.repaint();
      }
      public void mouseEntered(MouseEvent e)
      {
      }
      public void mouseExited(MouseEvent e)
      {
      }
   }
}

