/*
 * NewMonarch.java - Script Generator for Glass Shutters program
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
import java.awt.*;
import javax.swing.*;

public class NewMonarch
{
   private static double a = 0.0;
   private static double b = 0.0;
   private static double c = 0.0;
   private static double d = 0.0;
   private static double e = 0.0;
   private static double f = 0.0;
   private static double g = 0.0;
   private static double h = 0.0;
   private static double lh = 0.0;
   private static double rh = 0.0;
   private static double l2;
   private static double m2;
   private static double p;
   private static double q;
   private static double xoff;
   private static double yoff;
   private static String w;
   private static String s;

   private static double endGap  = Constants.endGap;
   private static double sideGap = Constants.sideGap;
   private static double intGap  = Constants.intGap;

   private static double endCentre  = Constants.endCentre;
   private static double sideCentre = Constants.sideCentre;

   private static double sideSpace = Constants.sideSpace;

   private static double endCatch  = Constants.endCatch;
   private static double sideCatch = Constants.sideCatch;

   private static String extDia   = Constants.extDia;
   private static String catchDia = Constants.catchDia;

   public static void run(String window, String style) throws Exception
   {
	  w = window;
	  s = style;

      FormStyleD template = new FormStyleD();
	  template.setVisible(true);

      a = template.getA();
      b = template.getB();
      c = template.getC();
      d = template.getD();
      e = template.getE();
      f = template.getF();
      g = template.getG();
      h = template.getH();
      lh = template.getLH();
      rh = template.getRH();

      template.dispose();

      l2 = a + (g - a)*(c - lh)/(c + e);
      m2 = b + (h - b)*(d - rh)/(d + f);

      p = c + (d - c)*a/(a + b);
      q = e + (f - e)*g/(g + h);

      JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
      chooser.setPreferredSize(new Dimension(600,300));

      chooser.setSelectedFile(new File("NewMonarch-" + Constants.today + "-" + w + ".scr"));
      chooser.setDialogTitle("Double for " + w);

      int result = chooser.showDialog(null, "Save");
      if (result == JFileChooser.APPROVE_OPTION)
      {
         File file = chooser.getSelectedFile();

         FileWriter fileWriter = new FileWriter(file);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

         // begin panel 1

         xoff = -50.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, -g + sideGap + xoff, -e + endGap + yoff);
         Constants.cornerTL(bufferedWriter, -a + sideGap + xoff, c - endGap + yoff);
         Constants.cornerTR(bufferedWriter, 0.0 - intGap/2.0 + xoff, p - endGap + yoff);
         Constants.cornerBR(bufferedWriter, 0.0 - intGap/2.0 + xoff, -q + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-g + sideGap + sideCentre + xoff, -e + endGap + endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-g + sideGap + sideCentre + xoff, -e + endGap + endCentre + sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2 + sideGap + sideCentre + xoff, lh + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2 + sideGap + sideCentre + xoff, lh - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-a + sideGap + sideCentre + xoff, c - endGap - endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-a + sideGap + sideCentre + xoff, c - endGap - endCentre - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(0.0 - intGap/2.0 - sideCatch + xoff, -q + endGap + endCatch + yoff) + catchDia);
         bufferedWriter.newLine();

         // end panel 1

         // begin panel 2

         xoff = 50.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, 0.0 + intGap/2.0 + xoff, -q + endGap + yoff);
         Constants.cornerTL(bufferedWriter, 0.0 + intGap/2.0 + xoff, p - endGap + yoff);
         Constants.cornerTR(bufferedWriter, b - sideGap + xoff, d - endGap + yoff);
         Constants.cornerBR(bufferedWriter, h - sideGap + xoff, -f + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(b - sideGap - sideCentre + xoff, d - endGap - endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(b - sideGap - sideCentre + xoff, d - endGap - endCentre - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2 - sideGap - sideCentre + xoff, rh + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2 - sideGap - sideCentre + xoff, rh - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(h - sideGap - sideCentre + xoff, -f + endGap + endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(h - sideGap - sideCentre + xoff, -f + endGap + endCentre + sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(0.0 + intGap/2.0 + sideCatch + xoff, -q + endGap + endCatch + yoff) + catchDia);
         bufferedWriter.newLine();

         // end panel 2

         // flip

         bufferedWriter.write("flip all");
         bufferedWriter.newLine();
         bufferedWriter.newLine();
         bufferedWriter.write("0,-1000 0,1000");
         bufferedWriter.newLine();

         // end flip

         // save

         bufferedWriter.write("saveas");
         bufferedWriter.newLine();
         bufferedWriter.write("R27");
         bufferedWriter.newLine();
         bufferedWriter.write(Constants.userHome + "\\Documents\\Glass Shutters\\dwg\\NewMonarch-" + Constants.today + "-" + w);
         bufferedWriter.newLine();

         // end save

         bufferedWriter.close();
         fileWriter.close();
         Messages.plainMessage(Constants.title, "Script saved to: " + file.getPath());

         // write audit file

         File auditFile = new File(Constants.userHome + "\\Documents\\Glass Shutters\\audit\\NewMonarch-" + Constants.today + "-" + w + ".txt");
         FileWriter auditFileWriter = new FileWriter(auditFile);
         BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

         writeAudit(auditFile, auditBufferedWriter);

         auditBufferedWriter.close();
         auditFileWriter.close();

         // end write audit file
      }
   }

   public static void writeAudit(File auditFile, BufferedWriter auditBufferedWriter) throws Exception
   {
      auditBufferedWriter.newLine();
      auditBufferedWriter.write(auditFile.getPath());
      auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

      auditBufferedWriter.write("A (top left)      " + Double.toString(a));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("B (top right)     " + Double.toString(b));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("C (left top)      " + Double.toString(c));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("D (right top)     " + Double.toString(d));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("E (left bottom)   " + Double.toString(e));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("F (right bottom)  " + Double.toString(f));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("G (bottom left)   " + Double.toString(g));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("H (bottom right)  " + Double.toString(h));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("LH (left hinge)   " + Double.toString(lh));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("RH (right hinge)  " + Double.toString(rh));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("Window            " + w);
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("Style             " + s);
	  auditBufferedWriter.newLine();
   }
}