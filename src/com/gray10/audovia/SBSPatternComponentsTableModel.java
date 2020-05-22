/*
 * SBSPatternComponentsTableModel.java
 * Copyright (C) 2010, 2011, 2012, 2014, 2015  Donald G Gray
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

public class SBSPatternComponentsTableModel extends AbstractTableModel
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
                              "Pattern Component Id",
                              "Position",
                              "Component Id",
                              "Component",
                              "Anonymous String"};
   private Integer song_id;
   private Integer pattern_id;

   private Integer pattern_component_id;
   private Integer position;
   private Integer component_id;
   private String  component;
   private String  anonymous_string;

   private ArrayList<Integer> component_id_cache = new ArrayList<Integer>(200);
   private ArrayList<String>  component_cache = new ArrayList<String>(200);
   private JComboBox<String> componentComboBox;

   private Integer nullInt = null;
   private String  nullStr = null;

   public SBSPatternComponentsTableModel(Connection aConnection,
                                         Integer aSongId,
                                         Integer aPatternId)
   {
      conn       = aConnection;
      song_id    = aSongId;
      pattern_id = aPatternId;
   }

   public void query() throws Exception
   {
      cache.clear();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_pattern_components(?)}");
			cStmt.setInt(1, pattern_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
	   	stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select p.pattern_component_id, " +
                 "       p.component_position, " +
                 "       p.component_id, " +
                 "       '<' || c.component_type || '> ' || c.component_name, " +
                 "       p.anonymous_string " +
                 "from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
                 " where   p.pattern_id = " + pattern_id.toString() +
                 " order by p.component_position");
		}
      while (rset.next())
      {
         pattern_component_id = Integer.valueOf(rset.getInt(1));
         position             = Integer.valueOf(rset.getInt(2));
         component_id         = Integer.valueOf(rset.getInt(3));
         if (rset.wasNull()) component_id = null;
         component            = rset.getString(4);
         if (component_id == null) component = null;
         anonymous_string     = rset.getString(5);

         Object[] record = {"unchanged",
                            pattern_component_id,
                            position,
                            component_id,
                            component,
                            anonymous_string};

         cache.add(record);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

      Object[] newRecord = {"new",
                            nullInt,
                            nullInt,
                            nullInt,
                            nullStr,
                            nullStr};

      cache.add(newRecord);
      fireTableDataChanged();

      component_id_cache.clear();
      component_cache.clear();
      componentComboBox = new JComboBox<String>();

      component_id_cache.add(nullInt);
      component_cache.add(nullStr);
      componentComboBox.addItem(null);

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_components(?, ?)}");
			cStmt.setInt(1,song_id.intValue());
			cStmt.setInt(2,pattern_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
	   	stmt = conn.createStatement();
	      rset  = stmt.executeQuery("select c.component_id, " +
                                   "       '<' || c.component_type || '> ' || c.component_name " +
                                   "from sbs_component c " +
                                   "where c.song_id = " + song_id.toString() +
                                   " and  c.component_id != " + pattern_id.toString() +
                                   " order by c.component_type, c.component_name");
		}
		while (rset.next())
		{
			component_id_cache.add(Integer.valueOf(rset.getInt(1)));
			component_cache.add(rset.getString(2));
			componentComboBox.addItem(rset.getString(2));
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

      if (col == 4)
      {
			int index = component_cache.indexOf(obj);
			if (index != -1)
			{
				component_id = component_id_cache.get(index);
				record[3] = component_id;
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
      return c == 2 || c == 4 || c == 5;
   }

   public void insertRow(int row)
   {
      Object[] newRecord = {"new",
                            nullInt,
                            nullInt,
                            nullInt,
                            nullStr,
                            nullStr};

      cache.add(row, newRecord);
      fireTableRowsInserted(row, row);
   }

   public void deleteRow(int row)
   {
      cache.remove(row);
      fireTableRowsDeleted(row, row);
   }

   public JComboBox<String> getComponentComboBox()
   {
		return componentComboBox;
	}
}