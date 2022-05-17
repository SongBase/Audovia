/*
 * Messages.java
 * Copyright (C) 2009, 2011, 2017  Donald G Gray
 *
 * http://glass.gray10.com/
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
package com.gray10.glass;

import java.util.*;
import javax.swing.*;

public class Messages
{
	/*
	 * version 1.1.14
	 *
	 */

   public static void exceptionHandler(JFrame frame, String title, Exception e)
   {
      if (e.getMessage().substring(0,9).equals("ORA-00001"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Data not saved - duplicate record.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02291"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Data not saved - parent record has been deleted.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02292"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Can't delete parent record when child records exist.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Exception - " + e.getMessage(),
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.ERROR_MESSAGE);
      }
   }

   public static void exceptionHandler(JDialog frame, String title, Exception e)
   {
      if (e.getMessage().substring(0,9).equals("ORA-00001"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Data not saved - duplicate record.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02291"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Data not saved - parent record has been deleted.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02292"))
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Can't delete parent record when child records exist.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else
      {
         JOptionPane.showConfirmDialog
            (frame,
             "Exception - " + e.getMessage(),
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.ERROR_MESSAGE);
      }
   }

   public static void exceptionHandler(String title, Exception e)
   {
      if (e.getMessage().substring(0,9).equals("ORA-00001"))
      {
         JOptionPane.showConfirmDialog
            (null,
             "Data not saved - duplicate record.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02291"))
      {
         JOptionPane.showConfirmDialog
            (null,
             "Data not saved - parent record has been deleted.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else if (e.getMessage().substring(0,9).equals("ORA-02292"))
      {
         JOptionPane.showConfirmDialog
            (null,
             "Can't delete parent record when child records exist.",
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.WARNING_MESSAGE);
      }
      else
      {
         JOptionPane.showConfirmDialog
            (null,
             "Exception - " + e.getMessage(),
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.ERROR_MESSAGE);
      }
   }

   public static void exceptionHandler(JFrame frame, String title, Exception e, String text)
   {
         JOptionPane.showConfirmDialog
            (frame,
             "Exception - " + e.getMessage() +
             "\n" + text,
             title,
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.ERROR_MESSAGE);
   }

   public static void plainMessage(JFrame frame, String title, String message)
   {
      JOptionPane.showConfirmDialog
         (frame,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.PLAIN_MESSAGE);
   }

   public static void plainMessage(JDialog frame, String title, String message)
   {
      JOptionPane.showConfirmDialog
         (frame,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.PLAIN_MESSAGE);
   }

   public static void plainMessage(String title, String message)
   {
      JOptionPane.showConfirmDialog
         (null,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.PLAIN_MESSAGE);
   }

   public static void warningMessage(JFrame frame, String title, String message)
   {
      JOptionPane.showConfirmDialog
         (frame,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.WARNING_MESSAGE);
   }

   public static void warningMessage(String title, String message)
   {
      JOptionPane.showConfirmDialog
         (null,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.WARNING_MESSAGE);
   }

   public static int plainQuestion(JFrame frame, String title, String question)
   {
      int selection =
      JOptionPane.showConfirmDialog
         (frame,
          question,
          title,
          JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.PLAIN_MESSAGE);
      return selection;
   }

   public static int warningQuestion(JFrame frame, String title, String question)
   {
      int selection =
      JOptionPane.showConfirmDialog
         (frame,
          question,
          title,
          JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.WARNING_MESSAGE);
      return selection;
   }
}