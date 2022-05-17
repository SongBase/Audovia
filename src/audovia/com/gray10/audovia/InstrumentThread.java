/*
 * InstrumentThread.java - Display instruments in separate thread
 * Copyright (C) 2011, 2015  Donald G Gray
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

import java.io.*;
import javax.swing.*;
import java.sql.*;
import javax.sound.midi.*;

public class InstrumentThread extends Thread
{
	/*
	 * version 3.0.0
	 *
	 */

	private Integer soundbank_id;
	private String  soundbank_name;

   private Connection conn;

   private ResultSet rset;
   private PreparedStatement viewStmt;
   private CallableStatement cSviewStmt;

   private InstrumentFrame instrumentFrame;

   private String title = "InstrumentThread";

   private InputStream inputStream = null;

   private JDialog pleaseWait;

   public InstrumentThread(Connection aConnection, Integer aSoundbankId, String aSoundbankName) throws Exception
   {
		conn = aConnection;
		soundbank_id = aSoundbankId;
		soundbank_name = aSoundbankName;

      viewStmt = conn.prepareStatement
                   ("select soundbank from sbs_soundbank " +
                    "where soundbank_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSviewStmt = conn.prepareCall("{call select_soundbank(?)}");
   }

   public void run()
   {
      try
      {
         JTextArea output = new JTextArea();
         instrumentFrame = new InstrumentFrame(soundbank_name, output);
         instrumentFrame.setVisible(true);

         pleaseWait = new JDialog(instrumentFrame, "Please wait...", false);
         pleaseWait.setSize(200,0);
         pleaseWait.setLocation(225,165);
         pleaseWait.setVisible(true);

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

            output.append("Name: " + (soundbank.getName() != null ? soundbank.getName() : "") + "\n" +
                          "Version: " + (soundbank.getVersion() != null ? soundbank.getVersion() : "") + "\n" +
                          "Description: " + (soundbank.getDescription() != null ? soundbank.getDescription() : "") + "\n" +
                          "Vendor: " + (soundbank.getVendor() != null ? soundbank.getVendor() : "") + "\n\n");

            Instrument[] instruments = soundbank.getInstruments();
            for (int ii = 0; ii < instruments.length; ii++)
              output.append(instruments[ii].toString() + " (Patch: " +
                Integer.toString(instruments[ii].getPatch().getBank()) + ", " +
                Integer.toString(instruments[ii].getPatch().getProgram()) + ")\n");

            inputStream.close();
            conn.commit();

            pleaseWait.setVisible(false);
         }
         else
         {
            conn.commit();

            pleaseWait.setVisible(false);

            Messages.warningMessage(instrumentFrame, title, "Soundbank not loaded.");
            instrumentFrame.dispose();
         }
      }
      catch (Exception e)
      {
         try
         {
            conn.rollback();

            pleaseWait.setVisible(false);
         }
         catch (Exception e1)
         {
            Messages.exceptionHandler(instrumentFrame, title, e1);
         }

         Messages.exceptionHandler(instrumentFrame, title, e);
      }
	}
}
