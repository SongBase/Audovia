/*
 * SBSSongPickerTableModel.java
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

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class SBSSongPickerTableModel extends AbstractTableModel
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
                              "Song Name"};
   private String  access_type;
   private Integer song_id;
   private String  song_name;

   private Integer parent_id;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSSongPickerTableModel(Connection aConnection, Integer aParentId)
   {
      conn = aConnection;
      parent_id = aParentId;
   }

   public void query(String searchString) throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call song_picker(?, ?)}");
			cStmt.setString(1, searchString);
			cStmt.setInt(2, parent_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select s.song_id, s.song_name " +
                 "from sbs_song s " +
                 "where upper(s.song_name) like upper('%" + searchString + "%') " +
                 "and   s.song_id != " + parent_id.toString() +
                 " order by upper(s.song_name)");
		}
      while (rset.next())
      {
         song_id               = new Integer(rset.getInt(1));
         song_name             = rset.getString(2);

         Object[] record = {"unchanged",
                            song_id,
                            song_name};

         cache.add(record);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

      fireTableDataChanged();
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
   }

   public String getColumnName(int col)
   {
      return names[col];
   }

   public boolean isCellEditable(int r, int c)
   {
      return false;
   }

   public void insertRow(int row)
   {
   }

   public void deleteRow(int row)
   {
   }
}