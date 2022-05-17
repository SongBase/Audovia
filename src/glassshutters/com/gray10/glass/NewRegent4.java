/*
 * NewRegent4.java - Script Generator for Glass Shutters program
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

public class NewRegent4
{
   private static double a = 0.0;
   private static double b = 0.0;
   private static double c = 0.0;
   private static double d = 0.0;
   private static double e = 0.0;
   private static double f = 0.0;
   private static double g = 0.0;
   private static double h = 0.0;
   private static double i = 0.0;
   private static double j = 0.0;
   private static double innerlh1 = 0.0;
   private static double innerlh2 = 0.0;
   private static double innerrh1 = 0.0;
   private static double innerrh2 = 0.0;
   private static double l2_1;
   private static double l2_2;
   private static double m2_1;
   private static double m2_2;
   private static double n;
   private static double o;
   private static double r;
   private static double t;
   private static double p;
   private static double q;
   private static double lh2_1;
   private static double lh2_2;
   private static double rh2_1;
   private static double rh2_2;
   private static double xoff;
   private static double yoff;
   private static String w;
   private static String s;

   private static double endGap  = Constants.endGap;
   private static double sideGap = Constants.sideGap;
   private static double intGap  = Constants.intGap;

   private static double endCentre  = Constants.endCentre;
   private static double sideCentre = Constants.sideCentre;
   private static double intCentre  = Constants.intCentre;

   private static double sideSpace = Constants.sideSpace;
   private static double intSpace  = Constants.intSpace;

   private static double endCatch  = Constants.endCatch;
   private static double sideCatch = Constants.sideCatch;

   private static String extDia   = Constants.extDia;
   private static String intDia   = Constants.intDia;
   private static String catchDia = Constants.catchDia;

   public static void run(String window, String style) throws Exception
   {
	  w = window;
	  s = style;

      FormStyleQ4 template = new FormStyleQ4();
	  template.setVisible(true);

      a = template.getA();
      b = template.getB();
      c = template.getC();
      d = template.getD();
      e = template.getE();
      f = template.getF();
      g = template.getG();
      h = template.getH();
      i = template.getI();
      j = template.getJ();
      innerlh1 = template.getLH1();
      innerlh2 = template.getLH2();
      innerrh1 = template.getRH1();
      innerrh2 = template.getRH2();

      if ((innerlh1 > innerlh2) || (innerrh1 > innerrh2))
      {
		  Messages.warningMessage(Constants.title, "LH1 - LH2 and RH1 - RH2 must be in ascending order.");
		  System.exit(0);
	  }

      template.dispose();

      l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
      l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
      m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
      m2_2 = b + (h - b)*(d - innerrh2)/(d + f);

      n = e + (f - e)*(g - i)/(g + h);
      o = f + (e - f)*(h - j)/(h + g);

      p = c + (d - c)*a/(a + b);
      q = e + (f - e)*g/(g + h);

      r = c + (d - c)*(a - i)/(a + b);
      t = d + (c - d)*(b - j)/(b + a);

      lh2_1 = innerlh1 + (innerrh1 - innerlh1)*(l2_1 - i)/(l2_1 + m2_1);
      lh2_2 = innerlh2 + (innerrh2 - innerlh2)*(l2_2 - i)/(l2_2 + m2_2);
      rh2_1 = innerrh1 + (innerlh1 - innerrh1)*(m2_1 - j)/(m2_1 + l2_1);
      rh2_2 = innerrh2 + (innerlh2 - innerrh2)*(m2_2 - j)/(m2_2 + l2_2);

      JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
      chooser.setPreferredSize(new Dimension(600,300));

      chooser.setSelectedFile(new File("NewRegent4-" + Constants.today + "-" + w + ".scr"));
      chooser.setDialogTitle("Quad for " + w);

      int result = chooser.showDialog(null, "Save");
      if (result == JFileChooser.APPROVE_OPTION)
      {
         File file = chooser.getSelectedFile();

         FileWriter fileWriter = new FileWriter(file);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

         // begin panel 1 (left)

         xoff = -150.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, -g + sideGap + xoff, -e + endGap + yoff);
         Constants.cornerTL(bufferedWriter, -a + sideGap + xoff, c - endGap + yoff);
         Constants.cornerTR(bufferedWriter, -i - intGap/2.0 + xoff, r - endGap + yoff);
         Constants.cornerBR(bufferedWriter, -i - intGap/2.0 + xoff, -n + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-g + sideGap + sideCentre + xoff, -e + endGap + endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-g + sideGap + sideCentre + xoff, -e + endGap + endCentre + sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2_1 + sideGap + sideCentre + xoff, innerlh1 - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2_1 + sideGap + sideCentre + xoff, innerlh1 + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2_2 + sideGap + sideCentre + xoff, innerlh2 - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-l2_2 + sideGap + sideCentre + xoff, innerlh2 + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-a + sideGap + sideCentre + xoff, c - endGap - endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-a + sideGap + sideCentre + xoff, c - endGap - endCentre - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre + xoff, r - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre - intSpace + xoff, r - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre + xoff, lh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre - intSpace + xoff, lh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre + xoff, lh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre - intSpace + xoff, lh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre + xoff, -n + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i - intGap/2.0 - intCentre - intSpace + xoff, -n + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         // end panel 1

         // begin panel 2 (centre left)

         xoff = -50.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, -i + intGap/2.0 + xoff, -n + endGap + yoff);
         Constants.cornerTL(bufferedWriter, -i + intGap/2.0 + xoff, r - endGap + yoff);
         Constants.cornerTR(bufferedWriter, 0.0 - intGap/2.0 + xoff, p - endGap + yoff);
         Constants.cornerBR(bufferedWriter, 0.0 - intGap/2.0 + xoff, -q + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + xoff, r - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + intSpace + xoff, r - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + xoff, lh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + intSpace + xoff, lh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + xoff, lh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + intSpace + xoff, lh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + xoff, -n + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(-i + intGap/2.0 + intCentre + intSpace + xoff, -n + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(0.0 - intGap/2.0 - sideCatch + xoff, -q + endGap + endCatch + yoff) + catchDia);
         bufferedWriter.newLine();

         // end panel 2

         // begin panel 3 (centre right)

         xoff = 50.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, 0.0 + intGap/2.0 + xoff, -q + endGap + yoff);
         Constants.cornerTL(bufferedWriter, 0.0 + intGap/2.0 + xoff, p - endGap + yoff);
         Constants.cornerTR(bufferedWriter, j - intGap/2.0 + xoff, t - endGap + yoff);
         Constants.cornerBR(bufferedWriter, j - intGap/2.0 + xoff, -o + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre + xoff, t - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre - intSpace + xoff, t - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre + xoff, rh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre - intSpace + xoff, rh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre + xoff, rh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre - intSpace + xoff, rh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre + xoff, -o + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j - intGap/2.0 - intCentre - intSpace + xoff, -o + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(0.0 + intGap/2.0 + sideCatch + xoff, -q + endGap + endCatch + yoff) + catchDia);
         bufferedWriter.newLine();

         // end panel 3

         // begin panel 4 (right)

         xoff = 150.0;
         yoff = 0.0;

         Constants.cornerBL(bufferedWriter, j + intGap/2.0 + xoff, -o + endGap + yoff);
         Constants.cornerTL(bufferedWriter, j + intGap/2.0 + xoff, t - endGap + yoff);
         Constants.cornerTR(bufferedWriter, b - sideGap + xoff, d - endGap + yoff);
         Constants.cornerBR(bufferedWriter, h - sideGap + xoff, -f + endGap + yoff);
         bufferedWriter.write("l cl");
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(b - sideGap - sideCentre + xoff, d - endGap - endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(b - sideGap - sideCentre + xoff, d - endGap - endCentre - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2_2 - sideGap - sideCentre + xoff, innerrh2 + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2_2 - sideGap - sideCentre + xoff, innerrh2 - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2_1 - sideGap - sideCentre + xoff, innerrh1 + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(m2_1 - sideGap - sideCentre + xoff, innerrh1 - sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(h - sideGap - sideCentre + xoff, -f + endGap + endCentre + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(h - sideGap - sideCentre + xoff, -f + endGap + endCentre + sideSpace + yoff) + extDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + xoff, t - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + intSpace + xoff, t - endGap - endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + xoff, rh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + intSpace + xoff, rh2_1 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + xoff, rh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + intSpace + xoff, rh2_2 + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + xoff, -o + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         bufferedWriter.write("c " + Constants.point(j + intGap/2.0 + intCentre + intSpace + xoff, -o + endGap + endCentre + yoff) + intDia);
         bufferedWriter.newLine();

         // end panel 4

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
         bufferedWriter.write(Constants.userHome + "\\Documents\\Glass Shutters\\dwg\\NewRegent4-" + Constants.today + "-" + w);
         bufferedWriter.newLine();

         // end save

         bufferedWriter.close();
         fileWriter.close();
         Messages.plainMessage(Constants.title, "Script saved to: " + file.getPath());

         // write audit file

         File auditFile = new File(Constants.userHome + "\\Documents\\Glass Shutters\\audit\\NewRegent4-" + Constants.today + "-" + w + ".txt");
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

	  auditBufferedWriter.write("I (left bar)      " + Double.toString(i));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("J (right bar)     " + Double.toString(j));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

      auditBufferedWriter.write("LH1               " + Double.toString(innerlh1));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("LH2               " + Double.toString(innerlh2));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("RH1               " + Double.toString(innerrh1));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("RH2               " + Double.toString(innerrh2));
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("Window            " + w);
	  auditBufferedWriter.newLine();
	  auditBufferedWriter.newLine();

	  auditBufferedWriter.write("Style             " + s);
	  auditBufferedWriter.newLine();
   }
}