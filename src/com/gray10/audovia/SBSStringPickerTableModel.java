/*
 * SBSStringPickerTableModel.java
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
package com.gray10.audovia;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class SBSStringPickerTableModel extends AbstractTableModel
{
	/*
	 * version 3.0.0
	 *
	 */

   private static final long serialVersionUID = 1L;

   private Connection conn;
   private Statement stmt;
   private CallableStatement cStmt;
   private ResultSet rset;
   private ArrayList<Object[]> cache = new ArrayList<Object[]>(200);
   private String[]  names = {"Status",
                              "String Id",
                              "String Name",
                              "String Value"};
   private Integer song_id;
   private Integer string_id;
   private String  string_name;
   private String  string_value;

   public SBSStringPickerTableModel(Connection aConnection, Integer aSongId)
   {
      conn = aConnection;
      song_id = aSongId;
   }

   public void query() throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call string_picker(?)}");
			cStmt.setInt(1, song_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select component_id, component_name, string_value " +
                 "from sbs_component " +
                 "where song_id = " + song_id.toString() +
                 " and  component_type = 'string' " +
                 " order by upper(component_name)");
		}
      while (rset.next())
      {
         string_id    = Integer.valueOf(rset.getInt(1));
         string_name  = rset.getString(2);
         string_value = rset.getString(3);
         Object[] record = {"unchanged",
                            string_id,
                            string_name,
                            string_value};
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
      return (cache.get(row))[col];
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