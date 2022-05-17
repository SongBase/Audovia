/*
 * GlassShuttersNTR.java - Script Generator for Glass Shutters to NT specifications, all corners rounded.
 * Copyright (C) 2012, 2013, 2014, 2015, 2016, 2017, 2017, 2018  Donald G Gray
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.sql.*;
import java.io.*;
import javax.swing.UIManager.*;
import java.text.*;
import static java.lang.Math.*;

import javax.swing.plaf.basic.*;

public class GlassShuttersNTR extends JDialog
{
	/*
	 * version 3.3
	 *
	 */

	private static String userName = System.getProperty("user.name");
	private static String userHome = System.getProperty("user.home");

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
   private static double k = 0.0;
   private static double lh = 0.0;
   private static double innerlh1 = 0.0;
   private static double innerlh2 = 0.0;
   private static double innerlh3 = 0.0;
   private static double lh2;
   private static double lh2_1;
   private static double lh2_2;
   private static double rh = 0.0;
   private static double innerrh1 = 0.0;
   private static double innerrh2 = 0.0;
   private static double innerrh3 = 0.0;
   private static double apex = 0.0;
   private static double reveal = 0.0;
   private static double radius;
   private static double innerradius;
   private static double inset;
   private static double rh2;
   private static double rh2_1;
   private static double rh2_2;
   private static double l;
   private static double m;
   private static double l2;
   private static double l2_1;
   private static double l2_2;
   private static double l2_3;
   private static double m2;
   private static double m2_1;
   private static double m2_2;
   private static double m2_3;
   private static double n;
   private static double o;
   private static double p;
   private static double q;
   private static double r;
   private static double t;
   private static double xoff;
   private static double yoff;

   private static double notchT;
   private static double notchM;
   private static double notchM1;
   private static double notchM2;
   private static double notchM3;
   private static double notchB;
   private static double lead;
   private static String w;
   private static String s;
   private static String today;

   private JTextField wField = new JTextField();

   private JComboBox<String> styleComboBox = new JComboBox<String>();

   private JDialog frame = GlassShuttersNTR.this;

   private static String title = "Glass Shutters 3.3";

   public GlassShuttersNTR()
   {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      setModal(true);

      setSize(450,260);
      setLocation(100,80);
      setTitle(title);

      String[] months = {"01", "02", "03", "04", "05", "06",
                         "07", "08", "09", "10", "11", "12"};
      String dayMask = "00";
      String dayValue;
      String dayNumber;

      GregorianCalendar calendar = new GregorianCalendar();

      dayValue = Integer.toString(calendar.get(Calendar.DATE));
      dayNumber = dayMask.substring(0, dayMask.length()-dayValue.length()) + dayValue;

      today = calendar.get(Calendar.YEAR) + months[calendar.get(Calendar.MONTH)] + dayNumber;

      Container contentPane = getContentPane();
      contentPane.setLayout(null);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      menuBar.setUI ( new BasicMenuBarUI ()
		    {
		        public void paint ( Graphics g, JComponent c )
		        {
		            g.setColor ( new Color(232, 204, 255) );
		            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
		        }
          } );

      JMenu helpMenu = new JMenu("Help");
      menuBar.add(helpMenu);

      JMenuItem aboutItem    = new JMenuItem("Licence");

      helpMenu.add(aboutItem);

      AboutAction aboutAction = new AboutAction();
      aboutItem.addActionListener(aboutAction);

      JLabel wLabel = new JLabel("Window:");
      wLabel.setBounds(25,50,75,30);
      contentPane.add(wLabel);
      wField.setBounds(100,50,290,30);
      contentPane.add(wField);

      JLabel sLabel = new JLabel("Style:");
      sLabel.setBounds(25,80,75,30);
      contentPane.add(sLabel);

        styleComboBox.addItem(null);
		styleComboBox.addItem("New Standard Left");
		styleComboBox.addItem("New Standard Right");
		styleComboBox.addItem("New Monarch");
		styleComboBox.addItem("New Monarch bi-folding hinged on left");
		styleComboBox.addItem("New Monarch bi-folding hinged on right");
		styleComboBox.addItem("New Majestic Left");
		styleComboBox.addItem("New Majestic Right");
		styleComboBox.addItem("New Regent (3 hinges)");
		styleComboBox.addItem("New Regent (4 hinges)");
		styleComboBox.addItem("New Regent (5 hinges)");
		styleComboBox.addItem(null);
		styleComboBox.addItem("Standard hinged left");
		styleComboBox.addItem("Standard hinged right");
		styleComboBox.addItem("Sterling hinged left");
		styleComboBox.addItem("Sterling hinged right");
		styleComboBox.addItem("Monarch");
		styleComboBox.addItem("Regal");
		styleComboBox.addItem("Majestic");
		styleComboBox.addItem("New Majestic Special");
		styleComboBox.addItem("Majestic right bi-folding");
		styleComboBox.addItem("Imperial");
		styleComboBox.addItem("Imperial right bi-folding");
		styleComboBox.addItem("Regent");
		styleComboBox.addItem("Regent (4 hinges)");
		styleComboBox.addItem("Viceroy");
		styleComboBox.addItem("Roman");
		styleComboBox.addItem("Roman (4 hinges)");
		styleComboBox.addItem("Roman (5 hinges)");
		styleComboBox.addItem("Roman (inset)");
		styleComboBox.addItem("Roman (inset) (4 hinges)");
		styleComboBox.addItem("Roman (inset) (5 hinges)");
		styleComboBox.addItem("Armagh (inset) (4 hinges)");

      styleComboBox.setBounds(100,80,290,30);
      contentPane.add(styleComboBox);

      JPanel buttonPanel = new JPanel();
      JButton goButton = new JButton("Continue");
      buttonPanel.add(goButton);
      buttonPanel.setBounds(40,140,240,50);
      contentPane.add(buttonPanel);

      GoAction goAction = new GoAction();

      goButton.addActionListener(goAction);
	}

   public String getW()
   {
      return wField.getText();
   }

   public String getS()
   {
      return (String)styleComboBox.getSelectedItem();
   }

   public static String point(double x, double y)
   {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(6);
		formatter.setGroupingUsed(false);
		String s = formatter.format(x) + "," + formatter.format(y);
		return s;
	}

   public static String charvalue(double x)
   {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(6);
		formatter.setGroupingUsed(false);
		String s = formatter.format(x);
		return s;
	}

   public static void cornerBL(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("pl " + point(x + 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x + 4.0, y + 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerNotchBL(BufferedWriter w, double x, double y, double depth) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("pl " + point(x + depth + 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x + depth + 4.0, y + 4.0) + " a -90");
	   w.newLine();
	   w.write("l " + point(x + depth, y + 5.0 + 29.0));
	   w.newLine();
      w.write("a ce " + point(x + depth - 8.0, y + 5.0 + 29.0) + " " + point(x, y + 5.0 + 29.0 + lead));
      w.newLine();
   }

   public static void cornerTL(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x, y - 4.0));
	   w.newLine();
	   w.write("a ce " + point(x + 4.0, y - 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerNotchTL(BufferedWriter w, double x, double y, double depth) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
		w.write("l " + point(x, y - 5.0 - 29.0 - lead));
		w.newLine();
		w.write("a ce " + point(x + depth - 8.0, y - 5.0 - 29.0) + " " + point(x + depth, y - 5.0 - 29.0));
		w.newLine();
	   w.write("l " + point(x + depth, y - 4.0));
	   w.newLine();
	   w.write("a ce " + point(x + depth + 4.0, y - 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerTR(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x - 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x - 4.0, y - 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerNotchTR(BufferedWriter w, double x, double y, double depth) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("l " + point(x - depth - 4.0, y));
	   w.newLine();
	   w.write("a ce " + point(x - depth - 4.0, y - 4.0) + " a -90");
	   w.newLine();
	   w.write("l " + point(x - depth, y - 5.0 - 29.0));
	   w.newLine();
	   w.write("a ce " + point(x - depth + 8.0, y - 5.0 - 29.0) + " " + point(x, y - 5.0 - 29.0 - lead));
	   w.newLine();
   }

   public static void cornerBR(BufferedWriter w, double x, double y) throws Exception
   {
	   w.write("l " + point(x, y + 4.0));
	   w.newLine();
	   w.write("a ce " + point(x - 4.0, y + 4.0) + " a -90");
	   w.newLine();
   }

   public static void cornerNotchBR(BufferedWriter w, double x, double y, double depth) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("l " + point(x, y + 5.0 + 29.0 + lead));
	   w.newLine();
	   w.write("a ce " + point(x - depth + 8.0, y + 5.0 + 29.0) + " " + point(x - depth, y + 5.0 + 29.0));
	   w.newLine();
	   w.write("l " + point(x - depth, y + 4.0));
	   w.newLine();
	   w.write("a ce " + point(x - depth - 4.0, y + 4.0) + " a -90");
	   w.newLine();
   }

   public static void leftNotch(BufferedWriter w, double x, double y, double depth, double length) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("l " + point(x, y - length/2.0 - lead));
	   w.newLine();
	   w.write("a ce " + point(x + depth - 8.0, y - length/2.0) + " " + point(x + depth, y - length/2.0));
	   w.newLine();
	   w.write("l " + point(x + depth, y + length/2.0));
	   w.newLine();
	   w.write("a ce " + point(x + depth - 8.0, y + length/2.0) + " " + point(x, y + length/2.0 + lead));
	   w.newLine();
   }

   public static void rightNotch(BufferedWriter w, double x, double y, double depth, double length) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("l " + point(x, y + length/2.0 + lead));
	   w.newLine();
	   w.write("a ce " + point(x - depth + 8.0, y + length/2.0) + " " + point(x - depth, y + length/2.0));
	   w.newLine();
	   w.write("l " + point(x - depth, y - length/2.0));
	   w.newLine();
	   w.write("a ce " + point(x - depth + 8.0, y - length/2.0) + " " + point(x, y - length/2.0 - lead));
	   w.newLine();
   }

   public static void topNotch(BufferedWriter w, double x, double y, double depth, double length) throws Exception
   {
	   if (depth == 2.0) lead = 5.292;
	      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	   w.write("l " + point(x - length/2.0 - lead, y));
	   w.newLine();
	   w.write("a ce " + point(x - length/2.0, y - depth + 8.0) + " " + point(x - length/2.0, y - depth));
	   w.newLine();
	   w.write("l " + point(x + length/2.0, y - depth));
	   w.newLine();
	   w.write("a ce " + point(x + length/2.0, y - depth + 8.0) + " " + point(x + length/2.0 + lead, y));
	   w.newLine();
   }

   //public static void bottomNotch(BufferedWriter w, double x, double y, double depth, double length) throws Exception
   //{
	//   if (depth == 2.0) lead = 5.292;
	//      else lead = Math.sqrt(64.0 - (8.0 - depth)*(8.0 - depth));
	//   w.write("l " + point(x + length/2.0 + lead, y));
	//   w.newLine();
	//   w.write("a ce " + point(x + length/2.0, y + depth - 8.0) + " " + point(x + length/2.0, y + depth));
	//   w.newLine();
	//   w.write("l " + point(x - length/2.0, y + depth));
	//   w.newLine();
	//   w.write("a ce " + point(x - length/2.0, y + depth - 8.0) + " " + point(x - length/2.0 - lead, y));
	//   w.newLine();
   //}

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

		auditBufferedWriter.write("K (top flap)      " + Double.toString(k));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("LH (left hinge)   " + Double.toString(lh));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("RH (right hinge)  " + Double.toString(rh));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("Apex              " + Double.toString(apex));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("Reveal            " + Double.toString(reveal));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("LH1               " + Double.toString(innerlh1));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("LH2               " + Double.toString(innerlh2));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("LH3               " + Double.toString(innerlh3));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("RH1               " + Double.toString(innerrh1));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("RH2               " + Double.toString(innerrh2));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("RH3               " + Double.toString(innerrh3));
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("Window            " + w);
		auditBufferedWriter.newLine();
		auditBufferedWriter.newLine();

		auditBufferedWriter.write("Style             " + s);
		auditBufferedWriter.newLine();
	}

   public static void topFlap(BufferedWriter bufferedWriter) throws Exception
   {
            xoff = 0.0;
            yoff = 100.0;

            cornerBL(bufferedWriter, -l + 3.0 + xoff, k + 1.5 + yoff);
            // leftNotch(bufferedWriter, -l + 3.0 + xoff, k + 1.5 + 50.0 + yoff, 2.0, 40.0);
            cornerTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff);
            topNotch(bufferedWriter, -a + 3.0 + 59.0 + 14.5 + xoff, c - 3.0 + yoff, 2.0, 29.0);
            topNotch(bufferedWriter, b - 3.0 - 59.0 - 14.5 + xoff, d - 3.0 + yoff, 2.0, 29.0);
            cornerTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff);
            // rightNotch(bufferedWriter, m - 3.0 + xoff, k + 1.5 + 50.0 + yoff, 2.0, 40.0);
            cornerBR(bufferedWriter, m - 3.0 + xoff, k + 1.5 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + 59.0 + 14.5 + xoff, c - 3.0 - 2.0 - 29.0 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - 59.0 - 14.5 + xoff, d - 3.0 - 2.0 - 29.0 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + 16.0 + xoff, k + 1.5 + 30.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - 16.0 + xoff, k + 1.5 + 30.0 + yoff) + " d 7");
            bufferedWriter.newLine();
	}

   public static void wings(BufferedWriter bufferedWriter) throws Exception
   {
         double topzpoint = sqrt((radius - 7.0)*(radius - 7.0) - 30.25);
         double botzpoint = sqrt((innerradius + 4.0)*(innerradius + 4.0) - 30.25);
         double topcornerangle = -90.0 - toDegrees(asin(5.5/(radius - 7.0)));
         double botcornerangle = -90.0 + toDegrees(asin(5.5/(innerradius + 4.0)));
         double toparchangle = -90.0 + toDegrees(asin(5.5/(radius - 7.0)));
         double botarchangle = 90.0 - toDegrees(asin(5.5/(innerradius + 4.0)));

         double opp = (radius - 22.0 - 3.0) * sin(70.0/radius); // was 24.0
         double adj = (radius - 22.0 - 3.0) * cos(70.0/radius);
         double opp45 = (radius - 22.0 - 3.0) * sin(PI/4.0);
         double adj45 = (radius - 22.0 - 3.0) * cos(PI/4.0);

		// start left wing

		xoff = -100.0;
		yoff = 100.0;

		cornerBL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + 3.0 + yoff);
      bufferedWriter.write("l " + point(-a + 3.0 + xoff, c + yoff));
	   bufferedWriter.newLine();
	   bufferedWriter.write("arc ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(toparchangle));
	   bufferedWriter.newLine();

	   bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + topzpoint + yoff) + " a " + charvalue(topcornerangle));
	   bufferedWriter.newLine();

      bufferedWriter.write("l " + point((b - a)/2.0 - 1.5 + xoff, apex - radius + botzpoint + yoff));
	   bufferedWriter.newLine();

	   bufferedWriter.write("arc ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + botzpoint + yoff) + " a " + charvalue(botcornerangle));
	   bufferedWriter.newLine();

	   bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(botarchangle));
	   bufferedWriter.newLine();

	   //bufferedWriter.write("ce " + point(-a + 3.0 + inset - 25.0 + xoff, c + yoff) + " a -90");
	   bufferedWriter.write("ce " + point((b - a)/2.0 - innerradius - 25.0 + 3.0 + xoff, c + yoff) + " a -90");
	   bufferedWriter.newLine();

	   bufferedWriter.write("l cl");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 - adj + xoff, c + opp + yoff) + " d 16");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 - adj45 + xoff, c + opp45 + yoff) + " d 16");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 - opp + xoff, c + adj + yoff) + " d 16");
      bufferedWriter.newLine();

		// end left wing

		// start right wing

		xoff = 100.0;
		yoff = 100.0;

	   bufferedWriter.write("pl " + point((b - a)/2.0 + innerradius + 25.0 - 3.0 + xoff, d - 25.0 + 3.0 + yoff));
	   bufferedWriter.newLine();
	   bufferedWriter.write("a ce " + point((b - a)/2.0 + innerradius + 25.0 - 3.0 + xoff, d + yoff) + " a -90");
	   bufferedWriter.newLine();

	   bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(botarchangle));
	   bufferedWriter.newLine();

	   bufferedWriter.write("ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + botzpoint + yoff) + " a " + charvalue(botcornerangle));
	   bufferedWriter.newLine();

      bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + topzpoint + yoff));
	   bufferedWriter.newLine();

	   bufferedWriter.write("arc ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + topzpoint + yoff) + " a " + charvalue(topcornerangle));
	   bufferedWriter.newLine();

	   bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(toparchangle));
	   bufferedWriter.newLine();

      bufferedWriter.write("l " + point(b - 3.0 + xoff, d - 25.0 + 3.0 + 4.0 + yoff)); ////////////// d was c
	   bufferedWriter.newLine();

	   bufferedWriter.write("a ce " + point(b - 3.0 - 4.0 + xoff, d - 25.0 + 3.0 + 4.0 + yoff) + " a -90"); //////////// d was c
	   bufferedWriter.newLine();

	   bufferedWriter.write("l cl");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 + adj + xoff, d + opp + yoff) + " d 16");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 + adj45 + xoff, d + opp45 + yoff) + " d 16");
      bufferedWriter.newLine();

      bufferedWriter.write("c " + point((b - a)/2.0 + opp + xoff, d + adj + yoff) + " d 16");
      bufferedWriter.newLine();

		// end right wing
	}

   private class GoAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         setVisible(false);
      }
   }

   private class AboutAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            JFrame aboutFrame = new JFrame();
            aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            aboutFrame.setTitle("About Glass Shutters");
            JTextArea output = new JTextArea();
            output.setEditable(false);
            aboutFrame.getContentPane().add(new JScrollPane(output));
            aboutFrame.setSize(450,350);
            aboutFrame.setLocation(475,100);
            aboutFrame.setVisible(true);

            output.append("Glass Shutters - Application for creating CAD scripts for drawing \n" +
                          "Glass Shutters version 3.3  Copyright (C) 2012 - 2018  Donald G Gray \n" +
                          "\n" +
                          "http://glass.gray10.com/ \n" +
                          "\n" +
                          "This program is free software: you can redistribute it and/or modify \n" +
                          "it under the terms of the GNU General Public License as published by \n" +
                          "the Free Software Foundation, either version 3 of the License, or \n" +
                          "(at your option) any later version. \n" +
                          "\n" +
                          "This program is distributed in the hope that it will be useful, \n" +
                          "but WITHOUT ANY WARRANTY; without even the implied warranty of \n" +
                          "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the \n" +
                          "GNU General Public License for more details. \n" +
                          "\n" +
                          "You should have received a copy of the GNU General Public License \n" +
                          "along with this program.  If not, see http://www.gnu.org/licenses/. \n");
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
      }
   }

   public static void main(String[] args)
   {
      try
      {
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               UIManager.setLookAndFeel(info.getClassName());
               UIManager.put("ScrollBar.minimumThumbSize", new Dimension(32,32));
               break;
            }
         }
         ExecutionMonitor console = new ExecutionMonitor
                                        ("GS - Execution Monitor");
         console.init();
         console.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

         System.out.println("Glass Shutters version 3.3");
         System.out.println("Hello " + userName);

         File file = new File(System.getProperty("user.home"),"Documents");
         if (! file.exists()) file.mkdir();

         file = new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters");
         if (! file.exists()) file.mkdir();

         file = new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr");
         if (! file.exists()) file.mkdir();

         file = new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"dwg");
         if (! file.exists()) file.mkdir();

         file = new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"audit");
         if (! file.exists()) file.mkdir();

         GlassShuttersNTR template = new GlassShuttersNTR();
         template.setVisible(true);

         w = template.getW();
         s = template.getS();

         template.dispose();

         //  Call method for chosen Style

         if (s == null)
         {
				Messages.warningMessage(title, "Style not chosen.");
				System.exit(0);
			}

			if      (s.equals("Standard hinged left"))   styleS();
			else if (s.equals("Standard hinged right"))  styleSR();
			else if (s.equals("New Standard Left"))  NewStandardLeft.run(w,s);
			else if (s.equals("New Standard Right"))  NewStandardRight.run(w,s);
			else if (s.equals("Sterling hinged left"))   styleSF();
			else if (s.equals("Sterling hinged right"))  styleSRF();
			else if (s.equals("Monarch"))  styleD();
			else if (s.equals("New Monarch"))  NewMonarch.run(w,s);
			else if (s.equals("New Monarch bi-folding hinged on left"))  NewMonarchBiLeft.run(w,s);
			else if (s.equals("New Monarch bi-folding hinged on right"))  NewMonarchBiRight.run(w,s);
			else if (s.equals("Regal"))    styleDF();
			else if (s.equals("Majestic")) styleT();
			//else if (s.equals("New Majestic")) styleNewT();
			else if (s.equals("New Majestic Left")) NewMajesticLeft.run(w,s);
			else if (s.equals("New Majestic Right")) NewMajesticRight.run(w,s);
			else if (s.equals("New Majestic Special")) styleNewTS();
			else if (s.equals("Majestic right bi-folding")) styleTR();
			else if (s.equals("Imperial")) styleTF();
			else if (s.equals("Imperial right bi-folding")) styleTRF();
			else if (s.equals("Regent"))   styleQ();
			else if (s.equals("New Regent (3 hinges)"))   NewRegent3.run(w,s);
			else if (s.equals("New Regent (4 hinges)"))   NewRegent4.run(w,s);
			else if (s.equals("New Regent (5 hinges)"))   NewRegent5.run(w,s);
			else if (s.equals("Regent (4 hinges)"))   styleQ4();
			else if (s.equals("Viceroy"))  styleQF();
			else if (s.equals("Roman"))    styleRoman();
			else if (s.equals("Roman (4 hinges)"))    styleRoman4();
			else if (s.equals("Roman (5 hinges)"))    styleRoman5();
			else if (s.equals("Roman (inset)"))    styleRomanInset();
			else if (s.equals("Roman (inset) (4 hinges)"))    styleRomanInset4();
			else if (s.equals("Roman (inset) (5 hinges)"))    styleRomanInset5();
			else if (s.equals("Armagh (inset) (4 hinges)"))    styleArmaghInset4();

         //  End of Call method for chosen Style

         System.exit(0);
      }
      catch (Exception e)
      {
         Messages.exceptionHandler("Script Generator", e);
      }
   }

   private static void styleS() throws Exception
   {
		   FormStyleS template = new FormStyleS();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         c = template.getC();
         d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         lh = template.getLH();
         //rh = template.getRH();

         template.dispose();

		   l2 = a + (g - a)*(c - lh)/(c + e);

         if (Math.abs(a - g) > 6.0)
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Standard-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Single hinged left for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1

            xoff = 0.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff);
            cornerBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff);
            // bottomNotch(bufferedWriter, h - 3.0 - 50.0 + xoff, -f + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - 30.0 + xoff, -f + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Standard-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Standard-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

   private static void styleSR() throws Exception
   {

		   FormStyleSR template = new FormStyleSR();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         c = template.getC();
         d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         //lh = template.getLH();
         rh = template.getRH();

         template.dispose();

		   m2 = b + (h - b)*(d - rh)/(d + f);

         if (Math.abs(b - h) > 6.0)
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Standard-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Single hinged right for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1

            xoff = 0.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff);
            cornerTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, -g + 3.0 + 50.0 + xoff, -e + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + 30.0 + xoff, -e + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Standard-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Standard-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleSF() throws Exception
	{

		   FormStyleSF template = new FormStyleSF();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         c = template.getC();
         d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         k = template.getK();
         lh = template.getLH();
         //rh = template.getRH();

         template.dispose();

         l = a + (g - a)*(c - k)/(c + e);
         m = b + (h - b)*(d - k)/(d + f);

         l2 = a + (g - a)*(c - lh)/(c + e);

         if (Math.abs(l - g) > 6.0)
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Sterling-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Single hinged left with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel 1

            xoff = 0.0;
            yoff = 0.0;

            if ( l > g)
            {
					notchB = 2.0;
					notchT = 2.0 + l - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - l;
					notchM = notchT + (notchB - notchT)*(k - lh)/(k + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff, notchT);
            cornerTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff);
            // bottomNotch(bufferedWriter, h - 3.0 - 50.0 + xoff, -f + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + notchT + 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - 30.0 + xoff, -f + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Sterling-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Sterling-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

   private static void styleSRF() throws Exception
   {

		   FormStyleSRF template = new FormStyleSRF();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         c = template.getC();
         d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         k = template.getK();
         //lh = template.getLH();
         rh = template.getRH();

         template.dispose();

		   l = a + (g - a)*(c - k)/(c + e);
		   m = b + (h - b)*(d - k)/(d + f);

		   m2 = b + (h - b)*(d - rh)/(d + f);

         if (Math.abs(m - h) > 6.0)  // should be (m - h)  corrected 4th May 2014
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Sterling-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Single hinged right with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel 1

            xoff = 0.0;
            yoff = 0.0;

            if ( m > h)
            {
					notchB = 2.0;
					notchT = 2.0 + m - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - m;
					notchM = notchT + (notchB - notchT)*(k - rh)/(k + f);
				}

            cornerBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff);
            cornerTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff);
            cornerNotchTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, -g + 3.0 + 50.0 + xoff, -e + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - notchT - 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + 30.0 + xoff, -e + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Sterling-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Sterling-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleD() throws Exception
	{

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
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Monarch-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Double for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Monarch-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Monarch-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleDF() throws Exception
	{

		   FormStyleDF template = new FormStyleDF();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         c = template.getC();
         d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l = a + (g - a)*(c - k)/(c + e);
         l2 = a + (g - a)*(c - lh)/(c + e);
         m = b + (h - b)*(d - k)/(d + f);
         m2 = b + (h - b)*(d - rh)/(d + f);

         q = e + (f - e)*g/(g + h);

         if ((Math.abs(l - g) > 6.0) || (Math.abs(m - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Regal-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Double with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( l > g)
            {
					notchB = 2.0;
					notchT = 2.0 + l - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - l;
					notchM = notchT + (notchB - notchT)*(k - lh)/(k + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff, notchT);
            cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + notchT + 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( m > h)
            {
					notchB = 2.0;
					notchT = 2.0 + m - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - m;
					notchM = notchT + (notchB - notchT)*(k - rh)/(k + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, k - 1.5 + yoff);
            cornerNotchTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - notchT - 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Regal-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Regal-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleT() throws Exception
	{

		   FormStyleT template = new FormStyleT();
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
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Majestic-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Majestic-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Majestic-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleNewT() throws Exception
	{

		   FormStyleT template = new FormStyleT();
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
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);

         //if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         //{
		 //		Messages.warningMessage(title, "More than 6 mm out of plumb.");
		 //		System.exit(0);
		 //}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Majestic-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            //if ( a > g)
            //{
			//		notchB = 2.0;
			//		notchT = 2.0 + a - g;
			//		notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
			//	}
			//	else
			//	{
			//		notchT = 2.0;
			//		notchB = 2.0 + g - a;
			//		notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
			//	}

            //cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            //leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerBL(bufferedWriter, -g + 4.0 + xoff, -e + 3.0 + yoff);
            cornerTL(bufferedWriter, -a + 4.0 + xoff, c - 3.0 + yoff);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 4.0 + 21.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 4.0 + 21.0 + xoff, lh + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 4.0 + 21.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            //if ( b > h)
            //{
			//		notchB = 2.0;
			//		notchT = 2.0 + b - h;
			//		notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
			//	}
			//	else
			//	{
			//		notchT = 2.0;
			//		notchB = 2.0 + h - b;
			//		notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
			//	}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            //rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            //cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            cornerTR(bufferedWriter, b - 4.0 + xoff, d - 3.0 + yoff);
            cornerBR(bufferedWriter, h - 4.0 + xoff, -f + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 4.0 - 21.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 4.0 - 21.0 + xoff, rh + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 4.0 - 21.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Majestic-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Majestic-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleNewTS() throws Exception
	{

		   FormStyleTF template = new FormStyleTF();
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
         k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);

         //if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         //{
		 //		Messages.warningMessage(title, "More than 6 mm out of plumb.");
		 //		System.exit(0);
		 //}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Majestic-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            //if ( a > g)
            //{
			//		notchB = 2.0;
			//		notchT = 2.0 + a - g;
			//		notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
			//	}
			//	else
			//	{
			//		notchT = 2.0;
			//		notchB = 2.0 + g - a;
			//		notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
			//	}

            //cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            //leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerBL(bufferedWriter, -g + 4.0 + xoff, -e + 3.0 + yoff);
            cornerTL(bufferedWriter, -a + 4.0 + xoff, c - 3.0 + yoff);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 4.0 + 21.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 4.0 + 21.0 + xoff, lh + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 4.0 + 21.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, k + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, k + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            //if ( b > h)
            //{
			//		notchB = 2.0;
			//		notchT = 2.0 + b - h;
			//		notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
			//	}
			//	else
			//	{
			//		notchT = 2.0;
			//		notchB = 2.0 + h - b;
			//		notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
			//	}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            //rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            //cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            cornerTR(bufferedWriter, b - 4.0 + xoff, d - 3.0 + yoff);
            cornerBR(bufferedWriter, h - 4.0 + xoff, -f + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 4.0 - 21.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 4.0 - 21.0 + xoff, rh + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 4.0 - 21.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Majestic-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Majestic-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleTR() throws Exception
	{

		   FormStyleTR template = new FormStyleTR();
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
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         //lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);
         rh2 = rh + (lh - rh)*(m2 - j)/(m2 + l2);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Majestic-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple right bi-folding for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            //bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 30.0 + xoff, -n + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 30.0 + xoff, -n + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Majestic-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Majestic-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleTF() throws Exception
	{

		   FormStyleTF template = new FormStyleTF();
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
         k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l = a + (g - a)*(c - k)/(c + e);
         l2 = a + (g - a)*(c - lh)/(c + e);
         m = b + (h - b)*(d - k)/(d + f);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);

         if ((Math.abs(l - g) > 6.0) || (Math.abs(m - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Imperial-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            if ( l > g)
            {
					notchB = 2.0;
					notchT = 2.0 + l - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - l;
					notchM = notchT + (notchB - notchT)*(k - lh)/(k + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + notchT + 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, k - 1.5 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            if ( m > h)
            {
					notchB = 2.0;
					notchT = 2.0 + m - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - m;
					notchM = notchT + (notchB - notchT)*(k - rh)/(k + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, k - 1.5 + yoff);
            cornerNotchTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - notchT - 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Imperial-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Imperial-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleTRF() throws Exception
	{

		   FormStyleTRF template = new FormStyleTRF();
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
         k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l = a + (g - a)*(c - k)/(c + e);
         l2 = a + (g - a)*(c - lh)/(c + e);
         m = b + (h - b)*(d - k)/(d + f);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         //lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);
         rh2 = rh + (lh - rh)*(m2 - j)/(m2 + l2);

         if ((Math.abs(l - g) > 6.0) || (Math.abs(m - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Imperial-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Triple right bi-folding with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel 1 (left)

            xoff = -100.0;
            yoff = 0.0;

            if ( l > g)
            {
					notchB = 2.0;
					notchT = 2.0 + l - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - l;
					notchM = notchT + (notchB - notchT)*(k - lh)/(k + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + notchT + 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            //bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 30.0 + xoff, -n + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre)

            xoff = 0.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, k - 1.5 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, j - 1.5 - 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 30.0 + xoff, -n + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (right)

            xoff = 100.0;
            yoff = 0.0;

            if ( m > h)
            {
					notchB = 2.0;
					notchT = 2.0 + m - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - m;
					notchM = notchT + (notchB - notchT)*(k - rh)/(k + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, k - 1.5 + yoff);
            cornerNotchTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, j + 1.5 + 50.0 + xoff, -o + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - notchT - 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(j + 1.5 + 30.0 + xoff, -o + 3.0 + 16.0 + yoff) + " d 7");
            //bufferedWriter.newLine();

            // end panel 3

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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Imperial-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Imperial-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleQ() throws Exception
	{

		   FormStyleQ template = new FormStyleQ();
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
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);
         rh2 = rh + (lh - rh)*(m2 - j)/(m2 + l2);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Regent-" + today + "-" + w + ".scr"));
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

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre left)

            xoff = -50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // start panel 3 (centre right)

            xoff = 50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

            // begin panel 4 (right)

            xoff = 150.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Regent-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Regent-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleQ4() throws Exception
	{

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
         //k = template.getK();
         //lh = template.getLH();
         //rh = template.getRH();

         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         //innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         //innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerrh1 > innerrh2))
         {
				Messages.warningMessage(title, "LH1 - LH2 and RH1 - RH2 must be in ascending order.");
				System.exit(0);
			}

         template.dispose();

         //l2 = a + (g - a)*(c - lh)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         //l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         //m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         r = c + (d - c)*(a - i)/(a + b);
         t = d + (c - d)*(b - j)/(b + a);

         //lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);
         //rh2 = rh + (lh - rh)*(m2 - j)/(m2 + l2);

         lh2_1 = innerlh1 + (innerrh1 - innerlh1)*(l2_1 - i)/(l2_1 + m2_1);
         lh2_2 = innerlh2 + (innerrh2 - innerlh2)*(l2_2 - i)/(l2_2 + m2_2);
         rh2_1 = innerrh1 + (innerlh1 - innerrh1)*(m2_1 - j)/(m2_1 + l2_1);
         rh2_2 = innerrh2 + (innerlh2 - innerrh2)*(m2_2 - j)/(m2_2 + l2_2);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Regent4-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Quad4 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -150.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					//notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					//notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            //leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, r - 3.0 + yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            //bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            //bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2_1 + yoff) + " d 13");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2_2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre left)

            xoff = -50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, r - 3.0 + yoff);
            cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, r - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2_1 + yoff) + " d 13");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2_2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // start panel 3 (centre right)

            xoff = 50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, t - 3.0 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2_1 + yoff) + " d 13");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2_2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

            // begin panel 4 (right)

            xoff = 150.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					//notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					//notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, t - 3.0 + yoff);
            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            //rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 3.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            //bufferedWriter.newLine();
            //bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            //bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, t - 3.0 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2 + yoff) + " d 13");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2_1 + yoff) + " d 13");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2_2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Regent4-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Regent4-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleQF() throws Exception
	{

		   FormStyleQF template = new FormStyleQF();
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
         k = template.getK();
         lh = template.getLH();
         rh = template.getRH();

         template.dispose();

         l = a + (g - a)*(c - k)/(c + e);
         l2 = a + (g - a)*(c - lh)/(c + e);
         m = b + (h - b)*(d - k)/(d + f);
         m2 = b + (h - b)*(d - rh)/(d + f);

         n = e + (f - e)*(g - i)/(g + h);
         o = f + (e - f)*(h - j)/(h + g);

         lh2 = lh + (rh - lh)*(l2 - i)/(l2 + m2);
         rh2 = rh + (lh - rh)*(m2 - j)/(m2 + l2);

         q = e + (f - e)*g/(g + h);

         if ((Math.abs(l - g) > 6.0) || (Math.abs(m - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Viceroy-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Quad with top flap for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin top flap

            topFlap(bufferedWriter);

            // end top flap

            // begin panel 1 (left)

            xoff = -150.0;
            yoff = 0.0;

            if ( l > g)
            {
					notchB = 2.0;
					notchT = 2.0 + l - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - l;
					notchM = notchT + (notchB - notchT)*(k - lh)/(k + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -l + 3.0 + xoff, k - 1.5 + yoff, notchT);
            cornerTR(bufferedWriter, -i - 1.5 + xoff, k - 1.5+ yoff);
            cornerBR(bufferedWriter, -i - 1.5 + xoff, -n + 3.0 + yoff);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l + 3.0 + notchT + 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i - 1.5 - 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (centre left)

            xoff = -50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, -i + 1.5 + xoff, -n + 3.0 + yoff);
            cornerTL(bufferedWriter, -i + 1.5 + xoff, k - 1.5 + yoff);
            cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, lh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-i + 1.5 + 16.0 + xoff, -n + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 2

            // begin panel 3 (centre right)

            xoff = 50.0;
            yoff = 0.0;

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, k - 1.5 + yoff);
            cornerTR(bufferedWriter, j - 1.5 + xoff, k - 1.5 + yoff);
            cornerBR(bufferedWriter, j - 1.5 + xoff, -o + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j - 1.5 - 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 3

            // begin panel 4 (right)

            xoff = 150.0;
            yoff = 0.0;

            if ( m > h)
            {
					notchB = 2.0;
					notchT = 2.0 + m - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + k);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - m;
					notchM = notchT + (notchB - notchT)*(k - rh)/(k + f);
				}

            cornerBL(bufferedWriter, j + 1.5 + xoff, -o + 3.0 + yoff);
            cornerTL(bufferedWriter, j + 1.5 + xoff, k - 1.5 + yoff);
            cornerNotchTR(bufferedWriter, m - 3.0 + xoff, k - 1.5 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m - 3.0 - notchT - 29.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, k - 1.5 - 5.0 - 14.5 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, rh2 + yoff) + " d 13");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(j + 1.5 + 16.0 + xoff, -o + 3.0 + 5.0 + 14.5 + yoff) + " d 13");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Viceroy-" + today + "-" + w);
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Viceroy-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRoman() throws Exception
	{

		   FormStyleRoman template = new FormStyleRoman();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();
         apex = template.getApex();

         template.dispose();

         radius = (a + b)/2.0;

         double zpoint = sqrt((radius - 7.0)*(radius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(radius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(radius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Roman-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Roman for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for Roman

            bufferedWriter.write("l " + point(-a + 3.0 + xoff, c + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("arc ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();
            rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Roman-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Roman-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRoman5() throws Exception
	{

		   FormStyleRoman5 template = new FormStyleRoman5();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerlh2 > innerlh3) || (innerrh1 > innerrh2) || (innerrh2 > innerrh3))
         {
				Messages.warningMessage(title, "LH1 - LH3 and RH1 - RH3 must be in ascending order.");
				System.exit(0);
			}

         apex = template.getApex();

         template.dispose();

         radius = (a + b)/2.0;

         double zpoint = sqrt((radius - 7.0)*(radius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(radius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(radius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Roman5-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Roman5 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            //leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            leftNotch(bufferedWriter, -l2_3 + 3.0 + xoff, innerlh3 + yoff, notchM3, 29.0);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for Roman5

            bufferedWriter.write("l " + point(-a + 3.0 + xoff, c + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("arc ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();
            rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            //rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            rightNotch(bufferedWriter, m2_3 - 3.0 + xoff, innerrh3 + yoff, notchM3, 29.0);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Roman5-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Roman5-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRoman4() throws Exception
	{

		   FormStyleRoman4 template = new FormStyleRoman4();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         //innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         //innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerrh1 > innerrh2))
         {
				Messages.warningMessage(title, "LH1 - LH2 and RH1 - RH2 must be in ascending order.");
				System.exit(0);
			}

         apex = template.getApex();

         template.dispose();

         radius = (a + b)/2.0;

         double zpoint = sqrt((radius - 7.0)*(radius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(radius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(radius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         //l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         //m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("Roman4-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("Roman4 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					//notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					//notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            //leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            //leftNotch(bufferedWriter, -l2_3 + 3.0 + xoff, innerlh3 + yoff, notchM3, 29.0);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 3.0 + yoff, notchT);
            leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for Roman4

            bufferedWriter.write("l " + point(-a + 3.0 + xoff, c + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("arc ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            //bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					//notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					//notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();
            rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 3.0 + yoff, notchT);
            //rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            //rightNotch(bufferedWriter, m2_3 - 3.0 + xoff, innerrh3 + yoff, notchM3, 29.0);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 10.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            //bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            //bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\Roman4-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\Roman4-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRomanInset() throws Exception
	{

		   FormStyleRomanInset template = new FormStyleRomanInset();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         lh = template.getLH();
         rh = template.getRH();
         apex = template.getApex();
         reveal = template.getReveal();
         inset = reveal + 15.0;
         if (inset < 39.0) inset = 39.0;

         template.dispose();

         radius = (a + b)/2.0;

         innerradius = radius - inset;

         double zpoint = sqrt((innerradius - 7.0)*(innerradius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(innerradius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(innerradius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         l2 = a + (g - a)*(c - lh)/(c + e);
         m2 = b + (h - b)*(d - rh)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("RomanInset-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("RomanInset for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin wings

            if (reveal >= 36.0) wings(bufferedWriter);

            // end wings

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2 + 3.0 + xoff, lh + yoff, notchM, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + yoff, notchT);
            //leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for RomanInset

            bufferedWriter.write("l " + point(-a + 3.0 + inset - 25.0 + xoff, c - 25.0 + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("a ce " + point(-a + 3.0 + inset - 25.0 + xoff, c + yoff) + " a 90");
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point(b - 3.0 - inset + 25.0 + xoff, d + yoff) + " a 90"); //////////////
	         bufferedWriter.newLine();


            //rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 25.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2 - 3.0 + xoff, rh + yoff, notchM, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\RomanInset-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\RomanInset-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRomanInset5() throws Exception
	{

		   FormStyleRomanInset5 template = new FormStyleRomanInset5();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerlh2 > innerlh3) || (innerrh1 > innerrh2) || (innerrh2 > innerrh3))
         {
				Messages.warningMessage(title, "LH1 - LH3 and RH1 - RH3 must be in ascending order.");
				System.exit(0);
			}

         apex = template.getApex();
         reveal = template.getReveal();
         inset = reveal + 15.0;
         if (inset < 39.0) inset = 39.0;

         template.dispose();

         radius = (a + b)/2.0;

         innerradius = radius - inset;

         double zpoint = sqrt((innerradius - 7.0)*(innerradius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(innerradius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(innerradius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("RomanInset5-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("RomanInset5 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin wings

            if (reveal >= 36.0) wings(bufferedWriter);

            // end wings

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            leftNotch(bufferedWriter, -l2_3 + 3.0 + xoff, innerlh3 + yoff, notchM3, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + yoff, notchT);
            //leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for RomanInset

            bufferedWriter.write("l " + point(-a + 3.0 + inset - 25.0 + xoff, c - 25.0 + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("a ce " + point(-a + 3.0 + inset - 25.0 + xoff, c + yoff) + " a 90");
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point(b - 3.0 - inset + 25.0 + xoff, d + yoff) + " a 90"); ///////////
	         bufferedWriter.newLine();


            //rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 25.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2_3 - 3.0 + xoff, innerrh3 + yoff, notchM3, 29.0);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();


            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\RomanInset5-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\RomanInset5-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleRomanInset4() throws Exception
	{

		   FormStyleRomanInset4 template = new FormStyleRomanInset4();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         //innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         //innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerrh1 > innerrh2))
         {
				Messages.warningMessage(title, "LH1 - LH2 and RH1 - RH2 must be in ascending order.");
				System.exit(0);
			}

         apex = template.getApex();
         reveal = template.getReveal();
         inset = reveal + 15.0;
         if (inset < 39.0) inset = 39.0;

         template.dispose();

         radius = (a + b)/2.0;

         innerradius = radius - inset;

         double zpoint = sqrt((innerradius - 7.0)*(innerradius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(innerradius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(innerradius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         //l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         //m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("RomanInset4-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("RomanInset4 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin wings

            if (reveal >= 36.0) wings(bufferedWriter);

            // end wings

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					//notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					//notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + yoff, notchT);
            //leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for RomanInset

            bufferedWriter.write("l " + point(-a + 3.0 + inset - 25.0 + xoff, c - 25.0 + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("a ce " + point(-a + 3.0 + inset - 25.0 + xoff, c + yoff) + " a 90");
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            //bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					//notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					//notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point(b - 3.0 - inset + 25.0 + xoff, d + yoff) + " a 90"); ////////////////
	         bufferedWriter.newLine();


            //rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);

            cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 25.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();


            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 25.0 - 5.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            //bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            //bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 5.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\RomanInset4-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\RomanInset4-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}

	private static void styleArmaghInset4() throws Exception
	{

		   FormStyleArmaghInset4 template = new FormStyleArmaghInset4();
		   template.setVisible(true);

         a = template.getA();
         b = template.getB();
         //c = template.getC();
         //d = template.getD();
         e = template.getE();
         f = template.getF();
         g = template.getG();
         h = template.getH();
         //i = template.getI();
         //j = template.getJ();
         //k = template.getK();
         innerlh1 = template.getLH1();
         innerlh2 = template.getLH2();
         //innerlh3 = template.getLH3();
         innerrh1 = template.getRH1();
         innerrh2 = template.getRH2();
         //innerrh3 = template.getRH3();

         if ((innerlh1 > innerlh2) || (innerrh1 > innerrh2))
         {
				Messages.warningMessage(title, "LH1 - LH2 and RH1 - RH2 must be in ascending order.");
				System.exit(0);
			}

         apex = template.getApex();
         reveal = template.getReveal();
         inset = reveal + 15.0;
         if (inset < 31.0) inset = 31.0;

         template.dispose();

         radius = (a + b)/2.0;

         innerradius = radius - inset;

         double zpoint = sqrt((innerradius - 7.0)*(innerradius - 7.0) - 30.25);
         double cornerangle = -90.0 - toDegrees(asin(5.5/(innerradius - 7.0)));
         double archangle = -90.0 + toDegrees(asin(5.5/(innerradius - 7.0)));

         c = apex - radius;
         d = apex - radius;

         //l2 = a + (g - a)*(c - lh)/(c + e);
         l2_1 = a + (g - a)*(c - innerlh1)/(c + e);
         l2_2 = a + (g - a)*(c - innerlh2)/(c + e);
         //l2_3 = a + (g - a)*(c - innerlh3)/(c + e);
         //m2 = b + (h - b)*(d - rh)/(d + f);
         m2_1 = b + (h - b)*(d - innerrh1)/(d + f);
         m2_2 = b + (h - b)*(d - innerrh2)/(d + f);
         //m2_3 = b + (h - b)*(d - innerrh3)/(d + f);

         p = c + (d - c)*a/(a + b);
         q = e + (f - e)*g/(g + h);

         if ((Math.abs(a - g) > 6.0) || (Math.abs(b - h) > 6.0))
         {
				Messages.warningMessage(title, "More than 6 mm out of plumb.");
				System.exit(0);
			}

         JFileChooser chooser = new JFileChooser(new File(new File(new File(System.getProperty("user.home"),"Documents"),"Glass Shutters"),"scr"));
         chooser.setPreferredSize(new Dimension(600,300));

         chooser.setSelectedFile(new File("ArmaghInset4-" + today + "-" + w + ".scr"));
         chooser.setDialogTitle("ArmaghInset4 for " + w);

         int result = chooser.showDialog(null, "Save");
         if (result == JFileChooser.APPROVE_OPTION)
         {
            File file = chooser.getSelectedFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // begin wings

            if (reveal >= 36.0) wings(bufferedWriter);

            // end wings

            // begin panel 1 (left)

            xoff = -50.0;
            yoff = 0.0;

            if ( a > g)
            {
					notchB = 2.0;
					notchT = 2.0 + a - g;
					//notchM = notchB + (notchT - notchB)*(e + lh)/(e + c);
					notchM1 = notchB + (notchT - notchB)*(e + innerlh1)/(e + c);
					notchM2 = notchB + (notchT - notchB)*(e + innerlh2)/(e + c);
					//notchM3 = notchB + (notchT - notchB)*(e + innerlh3)/(e + c);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + g - a;
					//notchM = notchT + (notchB - notchT)*(c - lh)/(c + e);
					notchM1 = notchT + (notchB - notchT)*(c - innerlh1)/(c + e);
					notchM2 = notchT + (notchB - notchT)*(c - innerlh2)/(c + e);
					//notchM3 = notchT + (notchB - notchT)*(c - innerlh3)/(c + e);
				}

            //cornerNotchBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff, notchB);
            cornerBL(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + yoff);
            leftNotch(bufferedWriter, -g + 3.0 + xoff, -e + 3.0 + 50.0 + 14.5 + yoff, notchB, 29.0);
            leftNotch(bufferedWriter, -l2_1 + 3.0 + xoff, innerlh1 + yoff, notchM1, 29.0);
            leftNotch(bufferedWriter, -l2_2 + 3.0 + xoff, innerlh2 + yoff, notchM2, 29.0);
            leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 25.0 - 50.0 - 14.5 + yoff, notchT, 29.0);
            cornerTL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + yoff);
            //cornerNotchTL(bufferedWriter, -a + 3.0 + xoff, c - 25.0 + yoff, notchT); ///////////////////////////////////////
            //leftNotch(bufferedWriter, -a + 3.0 + xoff, c - 10.0 - 14.5 + yoff, notchT, 29.0);  // new for ArmaghInset

            bufferedWriter.write("l " + point(-a + 3.0 + inset - 25.0 + xoff, c - 25.0 + yoff));
			   bufferedWriter.newLine();

	         bufferedWriter.write("a ce " + point(-a + 3.0 + inset - 25.0 + xoff, c + yoff) + " a 90");
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 - 1.5 - 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

            //cornerTR(bufferedWriter, 0.0 - 1.5 + xoff, p - 3.0 + yoff);
            cornerBR(bufferedWriter, 0.0 - 1.5 + xoff, -q + 3.0 + yoff);
            // bottomNotch(bufferedWriter, 0.0 - 1.5 - 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-g + 3.0 + notchB + 29.0 + xoff, -e + 3.0 + 50.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(-l2 + 3.0 + notchM + 29.0 + xoff, lh + yoff) + " d 14");
            bufferedWriter.write("c " + point(-l2_1 + 3.0 + notchM1 + 29.0 + xoff, innerlh1 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(-l2_2 + 3.0 + notchM2 + 29.0 + xoff, innerlh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            //bufferedWriter.write("c " + point(-l2_3 + 3.0 + notchM3 + 29.0 + xoff, innerlh3 + yoff) + " d 14");
            //bufferedWriter.newLine();

            bufferedWriter.write("c " + point(-a + 3.0 + notchT + 29.0 + xoff, c - 25.0 - 50.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 - 1.5 - 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
            bufferedWriter.newLine();

            // end panel 1

            // begin panel 2 (right)

            xoff = 50.0;
            yoff = 0.0;

            if ( b > h)
            {
					notchB = 2.0;
					notchT = 2.0 + b - h;
					//notchM = notchB + (notchT - notchB)*(f + rh)/(f + d);
					notchM1 = notchB + (notchT - notchB)*(f + innerrh1)/(f + d);
					notchM2 = notchB + (notchT - notchB)*(f + innerrh2)/(f + d);
					//notchM3 = notchB + (notchT - notchB)*(f + innerrh3)/(f + d);
				}
				else
				{
					notchT = 2.0;
					notchB = 2.0 + h - b;
					//notchM = notchT + (notchB - notchT)*(d - rh)/(d + f);
					notchM1 = notchT + (notchB - notchT)*(d - innerrh1)/(d + f);
					notchM2 = notchT + (notchB - notchT)*(d - innerrh2)/(d + f);
					//notchM3 = notchT + (notchB - notchT)*(d - innerrh3)/(d + f);
				}

            cornerBL(bufferedWriter, 0.0 + 1.5 + xoff, -q + 3.0 + yoff);
            //cornerTL(bufferedWriter, 0.0 + 1.5 + xoff, p - 3.0 + yoff);

            bufferedWriter.write("l " + point((b - a)/2.0 + 1.5 + xoff, apex - radius + zpoint + yoff));
			   bufferedWriter.newLine();
	         bufferedWriter.write("a ce " + point((b - a)/2.0 + 1.5 + 4.0 + xoff, apex - radius + zpoint + yoff) + " a " + charvalue(cornerangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point((b - a)/2.0 + xoff, apex - radius + yoff) + " a " + charvalue(archangle));
	         bufferedWriter.newLine();

	         bufferedWriter.write("ce " + point(b - 3.0 - inset + 25.0 + xoff, d + yoff) + " a 90"); ////////////////
	         bufferedWriter.newLine();


            //rightNotch(bufferedWriter, b - 3.0 + xoff, d - 10.0 - 14.5 + yoff, notchT, 29.0);
            cornerTR(bufferedWriter, b - 3.0 + xoff, d - 25.0 + yoff);
            rightNotch(bufferedWriter, b - 3.0 + xoff, d - 25.0 - 50.0 - 14.5 + yoff, notchT, 29.0);
            //cornerNotchTR(bufferedWriter, b - 3.0 + xoff, d - 25.0 + yoff, notchT);
            rightNotch(bufferedWriter, m2_2 - 3.0 + xoff, innerrh2 + yoff, notchM2, 29.0);
            rightNotch(bufferedWriter, m2_1 - 3.0 + xoff, innerrh1 + yoff, notchM1, 29.0);
            rightNotch(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + 50.0 + 14.5 + yoff, notchB, 29.0); /////////////////////
            cornerBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff);
            //cornerNotchBR(bufferedWriter, h - 3.0 + xoff, -f + 3.0 + yoff, notchB);
            // bottomNotch(bufferedWriter, 0.0 + 1.5 + 50.0 + xoff, -q + 3.0 + yoff, 2.0, 40.0);
            bufferedWriter.write("l cl");
            bufferedWriter.newLine();


            bufferedWriter.write("c " + point(b - 3.0 - notchT - 29.0 + xoff, d - 25.0 - 50.0 - 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            //bufferedWriter.write("c " + point(m2 - 3.0 - notchM - 29.0 + xoff, rh + yoff) + " d 14");
            //bufferedWriter.write("c " + point(m2_3 - 3.0 - notchM3 - 29.0 + xoff, innerrh3 + yoff) + " d 14");
            //bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_2 - 3.0 - notchM2 - 29.0 + xoff, innerrh2 + yoff) + " d 14");
            bufferedWriter.newLine();
            bufferedWriter.write("c " + point(m2_1 - 3.0 - notchM1 - 29.0 + xoff, innerrh1 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(h - 3.0 - notchB - 29.0 + xoff, -f + 3.0 + 50.0 + 14.5 + yoff) + " d 14");
            bufferedWriter.newLine();

            bufferedWriter.write("c " + point(0.0 + 1.5 + 30.0 + xoff, -q + 3.0 + 16.0 + yoff) + " d 7");
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
            bufferedWriter.write(userHome + "\\Documents\\Glass Shutters\\dwg\\ArmaghInset4-" + today + "-" + w);
            //bufferedWriter.newLine();
            bufferedWriter.newLine();

            // end save

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(title, "Script saved to: " + file.getPath());

            // write audit file

            File auditFile = new File(userHome + "\\Documents\\Glass Shutters\\audit\\ArmaghInset4-" + today + "-" + w + ".txt");
            FileWriter auditFileWriter = new FileWriter(auditFile);
            BufferedWriter auditBufferedWriter = new BufferedWriter(auditFileWriter);

            writeAudit(auditFile, auditBufferedWriter);

            auditBufferedWriter.close();
            auditFileWriter.close();

            // end write audit file
         }
	}
}

