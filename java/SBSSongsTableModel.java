/*
 * SBSSongsTableModel.java
 * Copyright (C) 2010, 2015  Donald G Gray
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

public class SBSSongsTableModel extends AbstractTableModel
{
	/*
	 * version 3.0.0
	 *
	 */

   private Connection conn;
   private Statement stmt;
   private CallableStatement cStmt;
   private ResultSet rset;
   private ArrayList<Object[]> cache = new ArrayList<Object[]>(1000);
   private String[]  names = {"Status",
                              "Song Id",
                              "Song Name",
                              "/durations",
                              "Soundbank Id",
                              "Soundbank"};
   private String  access_type;
   private Integer song_id;
   private String  song_name;
   private String  numeric_duration_type;
   private Integer soundbank_id;
   private String  soundbank_name;

   private ArrayList<Integer> soundbank_id_cache = new ArrayList<Integer>(200);
   private ArrayList<String>  soundbank_cache = new ArrayList<String>(200);
   private JComboBox<String> soundbankComboBox;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSSongsTableModel(Connection aConnection)
   {
      conn = aConnection;
   }

   public void query(String searchString) throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_songs(?)}");
			cStmt.setString(1, searchString);
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select s.song_id, s.song_name, s.numeric_duration_type, " +
                 "       s.soundbank_id, sb.soundbank_name " +
                 "from sbs_song s left join sbs_soundbank sb on s.soundbank_id = sb.soundbank_id " +
                 "where upper(s.song_name) like upper('%" + searchString + "%') " +
                 "order by upper(s.song_name)");
	   }
      while (rset.next())
      {
         song_id               = new Integer(rset.getInt(1));
         song_name             = rset.getString(2);
         numeric_duration_type = rset.getString(3);
         soundbank_id          = new Integer(rset.getInt(4));
         if (rset.wasNull()) soundbank_id = null;
         soundbank_name        = rset.getString(5);

         Object[] record = {"unchanged",
                            song_id,
                            song_name,
                            numeric_duration_type,
                            soundbank_id,
                            soundbank_name};

         cache.add(record);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

      Object[] newRecord = {"new",
                            nullInt,
                            nullStr,
                            nullStr,
                            nullInt,
                            nullStr};

      cache.add(newRecord);
      fireTableDataChanged();

      soundbank_id_cache.clear();
      soundbank_cache.clear();
      soundbankComboBox = new JComboBox<String>();

      soundbank_id_cache.add(nullInt);
      soundbank_cache.add(nullStr);
      soundbankComboBox.addItem(null);

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_soundbanks()}");
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
      else
      {
         stmt = conn.createStatement();
	      rset  = stmt.executeQuery("select s.soundbank_id, " +
                                   "       s.soundbank_name " +
                                   "from sbs_soundbank s " +
                                   " order by upper(s.soundbank_name)");
		}
		while (rset.next())
		{
			soundbank_id_cache.add(new Integer(rset.getInt(1)));
			soundbank_cache.add(rset.getString(2));
			soundbankComboBox.addItem(rset.getString(2));
		}
		rset.close();
	   if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();
   }

   public int getRowCount()
   {
      return cache.size();
   }

   public int getColumnCount()
   {
      return names.length;
   }

   public Object getValueAt(int row, int col)
   {
      return ((Object[])cache.get(row))[col];
   }

   public void setValueAt(Object obj, int row, int col)
   {
      Object[] record = (Object[])cache.get(row);
      String status = (String)record[0];
      if (status.equals("unchanged"))
      {
         record[0] = "changed";
      }
      if (status.equals("new"))
      {
         record[0] = "inserted";
      }
      record[col] = obj;

      if (col == 5)
      {
			int index = soundbank_cache.indexOf(obj);
			if (index != -1)
			{
				soundbank_id = soundbank_id_cache.get(index);
				record[4] = soundbank_id;
			}
		}

      fireTableRowsUpdated(row, row);
      if (row == cache.size() - 1)
      {
         insertRow(cache.size());
      }
   }

   public String getColumnName(int col)
   {
      return names[col];
   }

   public boolean isCellEditable(int r, int c)
   {
      return (c == 2 || c == 3 || c == 5);
   }

   public void insertRow(int row)
   {
      Object[] newRecord = {"new",
                            nullInt,
                            nullStr,
                            nullStr,
                            nullInt,
                            nullStr};

      cache.add(row, newRecord);
      fireTableRowsInserted(row, row);
   }

   public void deleteRow(int row)
   {
      cache.remove(row);
      fireTableRowsDeleted(row, row);
   }

   public JComboBox<String> getSoundbankComboBox()
   {
		return soundbankComboBox;
	}
}