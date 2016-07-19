/*
 * SBSShareTableModel.java
 * Copyright (C) 2015  Donald G Gray
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

public class SBSShareTableModel extends AbstractTableModel
{
	/*
	 * version 3.0.0
	 *
	 */

   private Connection conn;
   private Statement stmt;
   private CallableStatement cStmt;
   private ResultSet rset;
   private ArrayList<Object[]> cache = new ArrayList<Object[]>(200);
   private String[]  names = {"Status",
                              "Share Id",
                              "Shared with Id",
                              "User Account"
                              };
   private Integer share_id;
   private Integer song_id;
   private Integer shared_with_id;

   private String  shared_with_name;

   private ArrayList<Integer> shared_with_id_cache = new ArrayList<Integer>(200);
   private ArrayList<String>  shared_with_name_cache = new ArrayList<String>(200);
   private JComboBox<String> shared_withComboBox;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSShareTableModel(Connection aConnection,
                                         Integer aSongId
                                         )
   {
      conn       = aConnection;
      song_id    = aSongId;
   }

   public void query() throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) // will only be accessed from MySQL
      {
			cStmt = conn.prepareCall("{call get_shares(?)}");
			cStmt.setInt(1, song_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
		}
      while (rset.next())
      {
         share_id             = new Integer(rset.getInt(1));
         shared_with_id       = new Integer(rset.getInt(2));
         shared_with_name     = rset.getString(3);

         Object[] record = {"unchanged",
                            share_id,
                            shared_with_id,
                            shared_with_name};

         cache.add(record);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

      Object[] newRecord = {"new",
                            nullInt,
                            nullInt,
                            nullStr};

      cache.add(newRecord);
      fireTableDataChanged();

      shared_with_id_cache.clear();
      shared_with_name_cache.clear();
      shared_withComboBox = new JComboBox<String>();

      //shared_with_id_cache.add(nullInt);
      //shared_with_name_cache.add(nullStr);
      //shared_withComboBox.addItem(null);

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_users()}");
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
		}
		while (rset.next())
		{
			shared_with_id_cache.add(new Integer(rset.getInt(1)));
			shared_with_name_cache.add(rset.getString(2));
			shared_withComboBox.addItem(rset.getString(2));
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

      if (col == 3)
      {
			int index = shared_with_name_cache.indexOf(obj);
			if (index != -1)
			{
				shared_with_id = shared_with_id_cache.get(index);
				record[2] = shared_with_id;
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
      return c == 3;
   }

   public void insertRow(int row)
   {
      Object[] newRecord = {"new",
                            nullInt,
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

   public JComboBox<String> getShared_withComboBox()
   {
		return shared_withComboBox;
	}
}