/*
 * Constants.java - constants for Glass Shutters program
 * Copyright (C) 2017, 2018  Donald G Gray
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

import java.io.*;
import java.util.*;
import java.text.*;

public class Constants
{
   private static String[] months = {"01", "02", "03", "04", "05", "06",
                                     "07", "08", "09", "10", "11", "12"};
   private static String dayMask = "00";

   private static GregorianCalendar calendar = new GregorianCalendar();

   private static String dayValue = Integer.toString(calendar.get(Calendar.DATE));
   private static String dayNumber = dayMask.substring(0, dayMask.length()-dayValue.length()) + dayValue;

   public static String today = calendar.get(Calendar.YEAR) + months[calendar.get(Calendar.MONTH)] + dayNumber;

   public static String userHome = System.getProperty("user.home");

   public static String title = "Glass Shutters 3.3.7";

   public static double endGap = 3.0;
   public static double sideGap = 4.0;
   public static double intGap = 3.0;

   public static double endCentre = 17.0;
   public static double sideCentre = 17.0;
   public static double intCentre = 17.0;

   public static double sideSpace = 32.0;
   public static double intSpace = 21.0;

   public static double endCatch = 19.0;
   public static double sideCatch = 32.0;

   public static String extDia   = " d 8";
   public static String intDia   = " d 8";
   public static String catchDia = " d 14";

   public static String point(double x, double y)
   {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(6);
		formatter.setGroupingUsed(false);
		String s = formatter.format(x) + "," + formatter.format(y);
		return s;
	}

   public static void cornerBL(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("pl " + point(x + 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x + 4.0, y + 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerTL(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x, y - 4.0));
	   w.newLine();
	   w.write("a ce " + point(x + 4.0, y - 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerTR(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x - 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x - 4.0, y - 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerBR(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x, y + 4.0));
	   w.newLine();
	   w.write("a ce " + point(x - 4.0, y + 4.0) + " a -90");
	   w.newLine();
   }
}