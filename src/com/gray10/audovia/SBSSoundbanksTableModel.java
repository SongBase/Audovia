/*
 * SBSSoundbanksTableModel.java
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
package com.gray10.audovia;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class SBSSoundbanksTableModel extends AbstractTableModel
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
   private ArrayList<Object[]> cache = new ArrayList<Object[]>(100);
   private String[]  names = {"Status",
                              "Soundbank Id",
                              "Soundbank Name"};
   private String  access_type;
   private Integer soundbank_id;
   private String  soundbank_name;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSSoundbanksTableModel(Connection aConnection)
   {
      conn = aConnection;
   }

   public void query() throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_soundbanks()}");
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
      else
      {
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select s.soundbank_id, s.soundbank_name " +
                 "from sbs_soundbank s " +
                 "order by upper(s.soundbank_name)");
		}
      while (rset.next())
      {
         soundbank_id   = Integer.valueOf(rset.getInt(1));
         soundbank_name = rset.getString(2);

         Object[] record = {"unchanged",
                            soundbank_id,
                            soundbank_name};

         cache.add(record);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

      Object[] newRecord = {"new",
                            nullInt,
                            nullStr};

      cache.add(newRecord);
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
      Object[] record = cache.get(row);
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
      return (c == 2);
   }

   public void insertRow(int row)
   {
      Object[] newRecord = {"new",
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
}