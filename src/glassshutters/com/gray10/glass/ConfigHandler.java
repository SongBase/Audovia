/*
 * ConfigHandler.java - Handler for XML config file
 * Copyright (C) 2018  Donald G Gray
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
package com.gray10.glass;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
//import java.sql.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class ConfigHandler extends DefaultHandler
{
	/*
	 * version 3.3
	 *
	 */

   private String string_buffer;

   private double  endGap;
   private double  sideGap;
   private double  intGap;
   private double  endCentre;
   private double  sideCentre;
   private double  intCentre;
   private double  sideSpace;
   private double  intSpace;
   private double  endCatch;
   private double  sideCatch;
   private String  extDia;
   private String  intDia;
   private String  catchDia;

   public ConfigHandler() throws Exception
   {
	}

   public void characters(char[] ch,
                          int start,
                          int length) throws SAXException
   {
      string_buffer = new String(ch, start, length);
	}

   public void startElement(String uri,
                            String localName,
                            String qName,
                            Attributes attributes) throws SAXException
   {
	}

	public void endElement(String uri,
	                       String localName,
	                       String qName) throws SAXException
	{
		try
		{
         if (qName.equals("endGap"))
         {
				endGap = Double.parseDouble(string_buffer);
			}

         if (qName.equals("sideGap"))
         {
				sideGap = Double.parseDouble(string_buffer);
			}

         if (qName.equals("intGap"))
         {
				intGap = Double.parseDouble(string_buffer);
			}

         if (qName.equals("endCentre"))
         {
				endCentre = Double.parseDouble(string_buffer);
			}

         if (qName.equals("sideCentre"))
         {
				sideCentre = Double.parseDouble(string_buffer);
			}

         if (qName.equals("intCentre"))
         {
				intCentre = Double.parseDouble(string_buffer);
			}

         if (qName.equals("sideSpace"))
         {
				sideSpace = Double.parseDouble(string_buffer);
			}

         if (qName.equals("intSpace"))
         {
				intSpace = Double.parseDouble(string_buffer);
			}

         if (qName.equals("endCatch"))
         {
				endCatch = Double.parseDouble(string_buffer);
			}

         if (qName.equals("sideCatch"))
         {
				sideCatch = Double.parseDouble(string_buffer);
			}

         if (qName.equals("extDia"))
         {
				extDia = string_buffer;
			}

         if (qName.equals("intDia"))
         {
				intDia = string_buffer;
			}

         if (qName.equals("catchDia"))
         {
				catchDia = string_buffer;
			}

		}
		catch (Exception e)
		{
         Messages.exceptionHandler("XML config file", e);
         throw new SAXException("XML config file Exception");
		}
	}

   public double getendGap()
   {
      return endGap;
   }

   public double getsideGap()
   {
      return sideGap;
   }

   public double getintGap()
   {
      return intGap;
   }

   public double getendCentre()
   {
      return endCentre;
   }

   public double getsideCentre()
   {
      return sideCentre;
   }

   public double getintCentre()
   {
      return intCentre;
   }

   public double getsideSpace()
   {
      return sideSpace;
   }

   public double getintSpace()
   {
      return intSpace;
   }

   public double getendCatch()
   {
      return endCatch;
   }

   public double getsideCatch()
   {
      return sideCatch;
   }

   public String getextDia()
   {
      return extDia;
   }

   public String getintDia()
   {
      return intDia;
   }

   public String getcatchDia()
   {
      return catchDia;
   }

}