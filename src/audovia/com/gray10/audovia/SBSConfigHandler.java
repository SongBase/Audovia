/*
 * SBSConfigHandler.java - Handler for XML config file
 * Copyright (C) 2011  Donald G Gray
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

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class SBSConfigHandler extends DefaultHandler
{
	/*
	 * version 1.1.14
	 *
	 */

   private String string_buffer;

   private String  db_type;
   private String  host_name;
   private String  port;
   private String  database;
   private String  user_name;
   private String  password;

   public SBSConfigHandler() throws Exception
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
         if (qName.equals("db_type"))
         {
				db_type = string_buffer;
			}

         if (qName.equals("host_name"))
         {
				host_name = string_buffer;
			}

         if (qName.equals("port"))
         {
				port = string_buffer;
			}

         if (qName.equals("database"))
         {
				database = string_buffer;
			}

         if (qName.equals("user_name"))
         {
				user_name = string_buffer;
			}

         if (qName.equals("password"))
         {
				password = string_buffer;
			}
		}
		catch (Exception e)
		{
         Messages.exceptionHandler("XML config file", e);
         throw new SAXException("XML config file Exception");
		}
	}

   public String getdb_type()
   {
      return db_type;
   }

   public String gethost_name()
   {
      return host_name;
   }

   public String getport()
   {
      return port;
   }

   public String getdatabase()
   {
      return database;
   }

   public String getuser_name()
   {
      return user_name;
   }

   public String getpassword()
   {
      return password;
   }
}