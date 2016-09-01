/*
 * SBSUserMain.java - Main program for Audovia System
 * Copyright (C) 2010, 2011, 2012, 2013, 2014, 2015, 2016  Donald G Gray
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

import java.util.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import javax.swing.UIManager.*;
import javax.xml.parsers.*;
import java.awt.*;

import javax.swing.plaf.basic.*;

public class SBSUserMain
{
	/*
	 * version 3.3.8
	 *
	 */

	private static String userName = System.getProperty("user.name");
   private static String access_type;

   private static String  db_type   = "Postgres";
   private static String  host_name = "localhost";
   private static String  port      = "5432";
   private static String  database  = "SBS";
   private static String  user_name = "SBS";
   private static String  password  = "sbs";

   private static File  derby_directory;

   private static Statement stmt;

   public static void main(String[] args)
   {
      try
      {
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               UIManager.setLookAndFeel(info.getClassName());
               UIManager.put("ScrollBar.minimumThumbSize", new Dimension(32,32)); // added 8 Sep 2015
               break;
            }
         }

         UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Liberation Sans", Font.PLAIN, 14));

  //       BasicMenuBarUI MyMenuBarUI = new BasicMenuBarUI ()
	//		    {
	//		        public void paint ( Graphics g, JComponent c )
	//		        {
	//		            g.setColor ( Color.RED );
	//		            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
	//		        }
  //           } ;

  //           UIManager.put ( "MenuBarUI", MyMenuBarUI.getClass().getCanonicalName() );

         System.setProperty("image.dir", args[1]);

         ExecutionMonitor console = new ExecutionMonitor
                                        ("SBS - Execution Monitor");
         console.init();

          //System.out.println("menu bar test");

         System.out.println("Audovia version 3.3.8 for Ubuntu Snappy");
         System.out.println("Work directory: " + System.getProperty("user.dir"));
         System.out.println("Java directory: " + System.getProperty("java.home"));

         File config_file = new File(args[0]);
         if (config_file.exists())
         {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();

				SBSConfigHandler handler = new SBSConfigHandler();

            parser.parse(config_file, handler);

            db_type   = handler.getdb_type();
            host_name = handler.gethost_name();
            port      = handler.getport();
            database  = handler.getdatabase();
            user_name = handler.getuser_name();
            password  = handler.getpassword();
			}
			else
			{
            System.out.println("XML config file: " + config_file.getPath() + " not found.");
			}

         System.out.println("Connecting to database...");

         Connection conn = null;

         if (db_type.equals("Postgres"))
         {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                     "jdbc:postgresql://" + host_name + ":" + port + "/" + database,
                     user_name, password);
		   }
         else if (db_type.equals("MySQL"))
         {
            Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://" + host_name + ":" + port + "/" +
                     database + "?user=" + user_name + "&password=" + password + "&noAccessToProcedureBodies=true");
         }
		   else if (db_type.equals("Oracle"))
		   {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(
                     "jdbc:oracle:thin:@" + host_name + ":" + port + ":" + database,
                     user_name, password);
		   }
		   else
		   {
				// check if database exists

				//File database_directory = new File(database);
				//derby_directory = new File(new File(new File(System.getProperty("user.home"),"Documents"),"Audovia"),database);
            derby_directory = new File(database);
				if (! derby_directory.exists()) createDatabase();

				// end of check if database exists

				Properties p = System.getProperties();
				p.setProperty("derby.stream.error.field", "DerbyUtil.DEV_NULL");

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(
                     "jdbc:derby:" + derby_directory.getPath());
			}

         conn.setAutoCommit(false);

         System.out.println(conn.getMetaData().getDatabaseProductName() + " " +
                            conn.getMetaData().getDatabaseProductVersion());

         System.out.println("Connection established.");

         System.out.println("Hello " + userName);

         SBSSongs frame = new SBSSongs(null, null, conn, "local", true);
         frame.setVisible(true);
         //console.close();
      }
      catch (Exception e)
      {
         Messages.exceptionHandler("SBS User", e);
      }
   }

   private static void createDatabase() throws Exception
	{
						Properties p = System.getProperties();
						p.setProperty("derby.stream.error.field", "DerbyUtil.DEV_NULL");

						//derby_directory = new File(new File(new File(System.getProperty("user.home"),"Documents"),"Audovia"),database);

		            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		            Connection conn = DriverManager.getConnection(
		                     "jdbc:derby:" + derby_directory.getPath() + ";create=true");

		            conn.setAutoCommit(true);

		            System.out.println(conn.getMetaData().getDatabaseProductName() + " " +
		                               conn.getMetaData().getDatabaseProductVersion());

		            System.out.println("Connection established.");

		            System.out.println("Audovia database created.");

		            stmt = conn.createStatement();
		            stmt.execute("create table sbs_soundbank " +
		                         "(" +
		                         "soundbank_id	  integer," +
		                         "soundbank_name varchar(100) not null," +
		                         "soundbank		  blob," +
		                         "primary key (soundbank_id)," +
		                         "unique (soundbank_name), check(length(soundbank_name)>0)" +
		                         ")"
		                        );
		            stmt.execute("create sequence sbs_seq_soundbank");
		            stmt.execute("create table sbs_song " +
		                         "(" +
		                         "song_id			      integer," +
		                         "song_name		         varchar(200) not null," +
		                         "numeric_duration_type	varchar(10)," +
		                         "soundbank_id          integer," +
		                         "primary key (song_id)," +
		                         "foreign key (soundbank_id) references sbs_soundbank," +
		                         "unique (song_name), check(length(song_name)>0)" +
		                         ")"
		                        );
		            stmt.execute("create sequence sbs_seq_song");
		            stmt.execute("create table sbs_component " +
		                         "(" +
		                         "component_id		integer," +
		                         "song_id			integer," +
		                         "component_type		varchar(10)," +
		                         "component_name		varchar(100)	not null," +
		                         "string_value		varchar(4000)," +
		                         "primary key (component_id)," +
		                         "foreign key (song_id) references sbs_song," +
		                         "unique (song_id, component_type, component_name)," +
		                         "check(length(component_name)>0)" +
		                         ")"
		                        );
		            stmt.execute("create sequence sbs_seq_component");
		            stmt.execute("create table sbs_pattern_component " +
		                         "(" +
		                         "pattern_component_id	integer," +
		                         "pattern_id		integer," +
		                         "component_position	integer		not null," +
		                         "component_id		integer," +
		                         "anonymous_string varchar(4000)," +
		                         "primary key (pattern_component_id)," +
		                         "foreign key (pattern_id) references sbs_component," +
		                         "foreign key (component_id) references sbs_component," +
		                         "unique (pattern_id, component_position)," +
		                         "check(pattern_id != component_id)" +
		                         ")"
		                        );
		            stmt.execute("create sequence sbs_seq_pattern_component");
		            stmt.execute("create table sbs_connection " +
		                         "(" +
		                         "connection_id    integer," +
		                         "connection_name  varchar(200) not null," +
		                         "db_type          varchar(20)," +
		                         "host_name        varchar(400)," +
		                         "port             varchar(8)," +
		                         "db_name         varchar(100)," +
		                         "user_name        varchar(100)," +
		                         "password         varchar(100)," +
		                         "primary key (connection_id)," +
		                         "unique (connection_name), check(length(connection_name)>0)" +
		                         ")"
		                        );
		            stmt.execute("create sequence sbs_seq_connection");
		            stmt.close();
		            //conn.commit();

		            System.out.println("Audovia database objects created.");

		            conn.close();

		            //try
		            //{
		            //   DriverManager.getConnection("jdbc:derby:;shutdown=true");
					   //}
					   //catch (Exception e1)
					   //{
						//	System.out.println(e1.getMessage());
						//}

		            System.out.println("Audovia installation complete.");
   }
}

