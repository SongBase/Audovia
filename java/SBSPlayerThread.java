/*
 * SBSPlayerThread.java - Play Pattern in separate thread
 * Copyright (C) 2010, 2011, 2015  Donald G Gray
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
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.lang.reflect.*;
import java.sql.*;
import org.jfugue.*;

import javax.sound.midi.*;
import javax.swing.filechooser.*;

public class SBSPlayerThread extends Thread
{
	/*
	 * version 3.0.0
	 *
	 */

   private String  numeric_duration_type;
   private Integer soundbank_id;
   private Pattern pattern;

   private Connection conn;
   private ResultSet rset;
   private PreparedStatement viewStmt;
   private CallableStatement cSviewStmt;

   private InputStream inputStream = null;

   private Player player;
   private Synthesizer synthesizer = null;

   private String title = "SBSPlayerThread";

   private JDialog pleaseWait;

   private JFrame frame;

   public SBSPlayerThread(Connection aConnection,
                          String aNumericDurationType,
                          Integer aSoundbankId,
                          Pattern aPattern,
                          JFrame aJFrame) throws Exception
   {
		conn                  = aConnection;
      numeric_duration_type = aNumericDurationType;
      soundbank_id          = aSoundbankId;
      pattern               = aPattern;
      frame                 = aJFrame;

      viewStmt = conn.prepareStatement
                   ("select soundbank from sbs_soundbank " +
                    "where soundbank_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSviewStmt = conn.prepareCall("{call select_soundbank(?)}");

      pleaseWait = new JDialog(frame); //////////
      pleaseWait.setTitle("Player started...");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      pleaseWait.setIconImage(icon.getImage());

		pleaseWait.setModal(false); //////////
		pleaseWait.setSize(200,0);
      pleaseWait.setLocation(300,165);
   }

   public void run()
   {
      try
      {
	      String[] options = {"Java Sound Synthesizer", "Gervill"};

//	      int choice = JOptionPane.showOptionDialog(null,
//	                                                "Choose a synthesizer.\n\n" +
//	                                                "Choose Java Sound Synthesizer to use the Java Media Soundbank\n" +
//	                                                "or choose Gervill to use the attached soundbank.",
//	                                                title,
//	                                                JOptionPane.YES_NO_OPTION,
//	                                                JOptionPane.PLAIN_MESSAGE,
//	                                                null,
//	                                                options,
//	                                                null);

         int choice = 1;

	      if (choice != JOptionPane.CLOSED_OPTION)
	      {
	         if (choice == 0)
	         {
			   	MidiDevice.Info synthInfo;
			   	MidiDevice device;
			   	MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

			   	for (int i = 0; i < infos.length; i++)
			   	{
			   		device = MidiSystem.getMidiDevice(infos[i]);
			   		if (device instanceof Synthesizer)
			   		{

							//System.out.println(infos[i].getName()+" "+infos[i].getVersion());

			   			if (infos[i].getName().equals("Java Sound Synthesizer"))
			   			{
                        synthesizer = (Synthesizer)MidiSystem.getMidiDevice(infos[i]);
			   			}
			   		}
		      	}

					if (synthesizer != null)
					{
                  player = new Player(synthesizer, frame);

                  if (numeric_duration_type != null && numeric_duration_type.equals("pulses"))
                  {
		               MusicStringPulseParser parser = new MusicStringPulseParser();
			         	player.setParser(parser);
			         }

			         player.play(pattern);
                  player.close();
                  synthesizer.close();
					}
					else
					{
						Messages.warningMessage(frame, title, "The Java Sound Synthesizer is not available in this version of Java.");
					}
			   }
			   else
			   {
  			      pleaseWait.setVisible(true);

			      if (soundbank_id != null)
			      {
			         synthesizer = MidiSystem.getSynthesizer();

			         synthesizer.open();

                  if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                  {
 			            cSviewStmt.setInt(1, soundbank_id.intValue());
 			            cSviewStmt.execute();
 			            rset = cSviewStmt.getResultSet();
 		            }
 		            else
		            {
                     viewStmt.setInt(1,soundbank_id.intValue());
                     rset = viewStmt.executeQuery();
		            }
                  rset.next();

                  if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL") ||
                      conn.getMetaData().getDatabaseProductName().equals("Oracle"))
                  {
                     inputStream = rset.getBinaryStream(1);
					   }
                  else
                  {
                     Blob soundbank_blob = rset.getBlob(1);

                     if (soundbank_blob != null)
                     {
                        byte[] bytes = soundbank_blob.getBytes(1L, (int)soundbank_blob.length());
                        inputStream = new ByteArrayInputStream(bytes);
						   }
						   else
						   {
								inputStream = null;
							}
						}

                  rset.close();

                  if (inputStream != null)
                  {
			      		Soundbank soundbank = MidiSystem.getSoundbank(inputStream);
	                  synthesizer.loadAllInstruments(soundbank);
	                  inputStream.close();
	                  conn.commit();
		         	}
		         	else
		         	{
                     conn.commit();
                     Messages.warningMessage(frame, title, "Soundbank not loaded - default soundbank will be used.");
			      	}

                  player = new Player(synthesizer, frame);
		         }
		         else
		         {
	               String[] soundbank_options = {"Default Soundbank", "Soundbank from File"};

	               JOptionPane optionPane = new JOptionPane();

	               int soundbank_choice = optionPane.showOptionDialog(frame,
	                              "No soundbank attached to song.\n\n" +
	                              "Choose the Default Soundbank or a Soundbank from File.",
	                               title,
	                               JOptionPane.YES_NO_OPTION,
	                               JOptionPane.PLAIN_MESSAGE,
	                               null,
	                               soundbank_options,
	                               null);

	               if (soundbank_choice != JOptionPane.CLOSED_OPTION)
	               {
	                  if (soundbank_choice == 0)
	                  {
                        player = new Player(frame);
						   }
						   else
						   {
								FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                        JFileChooser chooser = new JFileChooser("SF2", fsv);
                        chooser.setPreferredSize(new Dimension(600,300));
                        chooser.setDialogTitle("Get Soundbank from File");

                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Soundbank files *.sf2 and *.dls", "sf2", "dls");
                        chooser.addChoosableFileFilter(filter);
                        chooser.setFileFilter(filter);

                        int result = chooser.showDialog(frame, "Get Soundbank"); ///////////////
                        if (result == JFileChooser.APPROVE_OPTION)
                        {
                           File file = chooser.getSelectedFile();

                           if (file.exists())
                           {
			                     synthesizer = MidiSystem.getSynthesizer();

			                     synthesizer.open();

			      		         Soundbank soundbank = MidiSystem.getSoundbank(file);
	                           synthesizer.loadAllInstruments(soundbank);

                              player = new Player(synthesizer, frame);
                           }
                           else
                           {
                              throw new Exception("Soundbank not found.");
                           }
                        }
                        else
                        {
									throw new Exception("Get Soundbank Aborted.");
								}
							}
						}
						else
						{
							throw new Exception("Play Aborted.");
						}
	    	      }

               if (numeric_duration_type != null && numeric_duration_type.equals("pulses"))
               {
		            MusicStringPulseParser parser = new MusicStringPulseParser();
			      	player.setParser(parser);
			      }

               pleaseWait.setVisible(false);

			      player.play(pattern);
               player.close();

               if (synthesizer != null) synthesizer.close();
			   }
		   }
      }
      catch (Exception e)
      {
         try
         {
            conn.rollback();
         }
         catch (Exception e1)
         {
            Messages.exceptionHandler(frame, title, e1);
         }
         pleaseWait.setVisible(false);

         Messages.exceptionHandler(frame, title, e);
      }
   }
}
