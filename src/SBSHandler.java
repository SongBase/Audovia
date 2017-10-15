/*
 * SBSHandler.java - Handler for XML import
 * Copyright (C) 2010, 2011, 2012, 2015  Donald G Gray
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

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class SBSHandler extends DefaultHandler
{
	/*
	 * version 3.0.0
	 *
	 */

   private Connection conn;
   private Statement stmt;
   private ResultSet rset;
   private PreparedStatement insertSong;
   private PreparedStatement sequenceSong;
   private PreparedStatement updateSong;
   private PreparedStatement insertComponent;
   private PreparedStatement sequenceComponent;
   private PreparedStatement insertPatternComponent;
   private PreparedStatement sequencePatternComponent;
   private PreparedStatement getComponentId;
   private CallableStatement cSgetComponentId;
   private CallableStatement cSinsertSong;
   private CallableStatement cSupdateSong;
   private CallableStatement cSinsertComponent;
   private CallableStatement cSinsertPatternComponent;

   private String string_buffer;
   private String today;

   private Integer song_id;
   private Integer imported_song;
   private String  song_name;
   private String  numeric_duration_type;
   private Integer component_id;
   private String  component_type;
   private String  component_name;
   private String  string_value;
   private String  pattern_name;
   private Integer component_position;
   private Integer pattern_component_id;
   private Integer pattern_id;
   private String  anonymous_string;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;


   public SBSHandler(Connection aConnection, String aSessionUser, String aSessionPassword) throws Exception
   {
      conn = aConnection;
      session_user = aSessionUser;
      session_password = aSessionPassword;

      imported_song = null;

      String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                         "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
      GregorianCalendar calendar = new GregorianCalendar();
      today = calendar.get(Calendar.DATE) + " " +
              months[calendar.get(Calendar.MONTH)] + " " +
              calendar.get(Calendar.YEAR);

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertSong = conn.prepareStatement
                      ("insert into sbs_song " +
                       "(song_name) " +
                       "values (?)", Statement.RETURN_GENERATED_KEYS);
		}
      else
      {
      insertSong = conn.prepareStatement
                   ("insert into sbs_song " +
                    "(song_id, song_name) " +
                    "values (?,?)");
	   }

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
         sequenceSong = conn.prepareStatement
                        ("select nextval('sbs_seq_song')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceSong = conn.prepareStatement
                        ("select sbs_seq_song.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceSong = conn.prepareStatement
                        ("select next value for sbs_seq_song from (values 1) v");
		}

      updateSong = conn.prepareStatement
                   ("update sbs_song " +
                    "set numeric_duration_type = ? " +
                    "where song_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertComponent = conn.prepareStatement
                      ("insert into sbs_component " +
                       "(song_id, component_type, component_name, string_value) " +
                       "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		}
      else
      {
      insertComponent = conn.prepareStatement
                        ("insert into sbs_component " +
                         "(component_id, song_id, component_type, component_name, string_value) " +
                         "values (?,?,?,?,?)");
		}

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
        sequenceComponent = conn.prepareStatement
                            ("select nextval('sbs_seq_component')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceComponent = conn.prepareStatement
                             ("select sbs_seq_component.nextval " +
                              "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceComponent = conn.prepareStatement
                             ("select next value for sbs_seq_component from (values 1) v");
		}

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertPatternComponent = conn.prepareStatement
                      ("insert into sbs_pattern_component " +
                       "(pattern_id, component_position, component_id, anonymous_string) " +
                       "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		}
      else
      {
      insertPatternComponent = conn.prepareStatement
                               ("insert into sbs_pattern_component " +
                                "(pattern_component_id, pattern_id, component_position, component_id, anonymous_string) " +
                                "values (?,?,?,?,?)");
		}

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
        sequencePatternComponent = conn.prepareStatement
                                   ("select nextval('sbs_seq_pattern_component')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequencePatternComponent = conn.prepareStatement
                                    ("select sbs_seq_pattern_component.nextval " +
                                     "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequencePatternComponent = conn.prepareStatement
                                    ("select next value for sbs_seq_pattern_component from (values 1) v");
		}

      getComponentId = conn.prepareStatement
                       ("select component_id from sbs_component " +
                        "where song_id = ? and component_type = ? and component_name = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSgetComponentId = conn.prepareCall("{call get_component_id(?, ?, ?)}");
         cSinsertSong  = conn.prepareCall("{call insert_song(?, ?, ?, ?, ?)}");
         cSupdateSong  = conn.prepareCall("{call update_numeric_duration_type(?, ?, ?, ?)}");
         cSinsertComponent  = conn.prepareCall("{call insert_component(?, ?, ?, ?, ?, ?)}");
         cSinsertPatternComponent  = conn.prepareCall("{call insert_pattern_component(?, ?, ?, ?, ?, ?)}");
		}
   }

   public void characters(char[] ch,
                          int start,
                          int length) throws SAXException
   {
      string_buffer = new String(ch, start, length);
      //if (string_buffer.trim().length() == 0) string_buffer = null;
      //System.out.println(start + "   " + length + "   " + string_buffer);
	}

   public void startElement(String uri,
                            String localName,
                            String qName,
                            Attributes attributes) throws SAXException
   {
      if (qName.equals("component"))
      {
			string_value = null;
		}
		if (qName.equals("pattern_component"))
		{
			component_type = null;
			component_name = null;
			anonymous_string = null;
		}
	}

	public void endElement(String uri,
	                       String localName,
	                       String qName) throws SAXException
	{
		try
		{
         if (qName.equals("song_name"))
         {
	   		song_name = string_buffer + " (imported " + today + ")";

			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertSong.setString(1,song_name);
               insertSong.execute();

               rset = insertSong.getGeneratedKeys();
               while (rset.next()) song_id = new Integer(rset.getInt(1));
               rset.close();
				}
				else
				{
               cSinsertSong.setString(1, song_name);
               cSinsertSong.setNull(2, Types.VARCHAR);
               cSinsertSong.setNull(3, Types.INTEGER);
               cSinsertSong.setString(4, session_user);
               cSinsertSong.setString(5, session_password);

               cSinsertSong.execute();

				   rset = cSinsertSong.getResultSet();

					while (rset.next()) song_id = new Integer(rset.getInt(1));

				   if (song_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("Incorrect user name or password.");
					}

               rset.close();
				}
			}
			else
         {
            rset = sequenceSong.executeQuery();
            while (rset.next()) song_id = new Integer(rset.getInt(1));
            rset.close();

            insertSong.setInt(1, song_id.intValue());
            insertSong.setString(2, song_name);

            insertSong.execute();
			}

            imported_song = song_id;
	   	}

         if (qName.equals("numeric_duration_type"))
         {
	   		numeric_duration_type = string_buffer;

            if (session_user == null)
            {
               updateSong.setString(1, numeric_duration_type);
               updateSong.setInt(2, song_id.intValue());

               updateSong.execute();
	   		}
	   		else
	   		{
               cSupdateSong.setString(1, numeric_duration_type);
               cSupdateSong.setInt(2, song_id.intValue());
               cSupdateSong.setString(3, session_user);
               cSupdateSong.setString(4, session_password);
               cSupdateSong.execute();

	   			rset = cSupdateSong.getResultSet();

	   	      while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

	   			if (returned_user_id.intValue() == -1)
	   			{
	   			   rset.close();
	   				throw new Exception("You can only update your own songs.");
	   			}

               rset.close();
	   	   }
	   	}

         if (qName.equals("component_type"))
         {
				component_type = string_buffer;
			}

         if (qName.equals("component_name"))
         {
				component_name = string_buffer;
			}

         if (qName.equals("string_value"))
         {
				string_value = string_buffer;
			}

         if (qName.equals("pattern_name"))
         {
				pattern_name = string_buffer;
			}

         if (qName.equals("component_position"))
         {
				component_position = new Integer(string_buffer);
			}

			if (qName.equals("anonymous_string"))
			{
				anonymous_string = string_buffer;
			}

         if (qName.equals("component"))
         {
			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertComponent.setInt(1, song_id.intValue());
               insertComponent.setString(2, component_type);
               insertComponent.setString(3, component_name);
               insertComponent.setString(4, string_value);
               insertComponent.execute();

               rset = insertComponent.getGeneratedKeys();
               while (rset.next()) component_id = new Integer(rset.getInt(1));
               rset.close();
				}
				else
				{
               cSinsertComponent.setInt(1, song_id.intValue());
               cSinsertComponent.setString(2, component_type);
               cSinsertComponent.setString(3, component_name);
               cSinsertComponent.setString(4, string_value);
               cSinsertComponent.setString(5, session_user);
               cSinsertComponent.setString(6, session_password);

               cSinsertComponent.execute();

				   rset = cSinsertComponent.getResultSet();

					while (rset.next()) component_id = new Integer(rset.getInt(1));

				   if (component_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("You can only update your own or shared songs.");
					}

               rset.close();
				}
			}
			else
			{
            rset = sequenceComponent.executeQuery();
            while (rset.next()) component_id = new Integer(rset.getInt(1));
            rset.close();

            insertComponent.setInt(1, component_id.intValue());
            insertComponent.setInt(2, song_id.intValue());
            insertComponent.setString(3, component_type);
            insertComponent.setString(4, component_name);
            insertComponent.setString(5, string_value);

            insertComponent.execute();
			}
			}

         if (qName.equals("pattern_component"))
         {
            if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
            {
			      cSgetComponentId.setInt(1, song_id.intValue());
			      cSgetComponentId.setString(2, "pattern");
				   cSgetComponentId.setString(3, pattern_name);
			      cSgetComponentId.execute();
			      rset = cSgetComponentId.getResultSet();
		      }
		      else
		      {
				   getComponentId.setInt(1, song_id.intValue());
				   getComponentId.setString(2, "pattern");
				   getComponentId.setString(3, pattern_name);

               rset = getComponentId.executeQuery();
		   	}
            while (rset.next()) pattern_id = new Integer(rset.getInt(1));
            rset.close();

            if (component_name != null)
            {
					if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
					{
					    cSgetComponentId.setInt(1, song_id.intValue());
						 cSgetComponentId.setString(2, component_type);
						 cSgetComponentId.setString(3, component_name);
						 cSgetComponentId.execute();
						 rset = cSgetComponentId.getResultSet();
					}
					else
		         {
				      getComponentId.setInt(1, song_id.intValue());
				      getComponentId.setString(2, component_type);
				      getComponentId.setString(3, component_name);

                  rset = getComponentId.executeQuery();
				   }
               while (rset.next()) component_id = new Integer(rset.getInt(1));
               rset.close();
			   }
			   else component_id = null;

			   if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			   {
               if (session_user == null)
               {
                  insertPatternComponent.setInt(1, pattern_id.intValue());
                  insertPatternComponent.setInt(2, component_position.intValue());

                  if (component_id != null)
                     insertPatternComponent.setInt(3, component_id.intValue());
                  else
                     insertPatternComponent.setNull(3, Types.INTEGER);

                  insertPatternComponent.setString(4, anonymous_string);
                  insertPatternComponent.execute();

                  rset = insertPatternComponent.getGeneratedKeys();
                  while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));
                  rset.close();
					}
					else
					{
                  cSinsertPatternComponent.setInt(1, pattern_id.intValue());
                  cSinsertPatternComponent.setInt(2, component_position.intValue());

                  if (component_id != null)
                     cSinsertPatternComponent.setInt(3, component_id.intValue());
                  else
                     cSinsertPatternComponent.setNull(3, Types.INTEGER);

                  cSinsertPatternComponent.setString(4, anonymous_string);
                  cSinsertPatternComponent.setString(5, session_user);
                  cSinsertPatternComponent.setString(6, session_password);

                  cSinsertPatternComponent.execute();

			   	   rset = cSinsertPatternComponent.getResultSet();

			   		while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));

			   	   if (pattern_component_id.intValue() == -1)
			   	   {
			   			rset.close();
			   			throw new Exception("You can only update your own or shared songs.");
			   		}

                  rset.close();
					}
		   	}
		   	else
			   {
               rset = sequencePatternComponent.executeQuery();
               while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));
               rset.close();

               insertPatternComponent.setInt(1, pattern_component_id.intValue());
               insertPatternComponent.setInt(2, pattern_id.intValue());
               insertPatternComponent.setInt(3, component_position.intValue());

               if (component_id != null)
                  insertPatternComponent.setInt(4, component_id.intValue());
               else
                  insertPatternComponent.setNull(4, Types.INTEGER);

               insertPatternComponent.setString(5, anonymous_string);

               insertPatternComponent.execute();
			   }
			}
		}
		catch (Exception e)
		{
         Messages.exceptionHandler("XML Import", e);
         throw new SAXException("Database Exception");
		}
	}

	public Integer getImportedSong()
	{
		return imported_song;
	}
}