/*
 * SBSConnectionsTableModel.java
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
import java.sql.Date;

public class SBSConnectionsTableModel extends AbstractTableModel
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
                              "Connection Id",
                              "Connection Name"};
   private Integer connection_id;
   private String  connection_name;
   private String  description;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSConnectionsTableModel(Connection aConnection)
   {
      conn = aConnection;
   }

   public void query() throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_connections()}");
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
      else
      {
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select connection_id, connection_name " +
                 "from sbs_connection " +
                 "order by upper(connection_name)");
		}
      while (rset.next())
      {
         connection_id    = Integer.valueOf(rset.getInt(1));
         connection_name  = rset.getString(2);

         Object[] record = {"unchanged",
                            connection_id,
                            connection_name};

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
      return c == 2;
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