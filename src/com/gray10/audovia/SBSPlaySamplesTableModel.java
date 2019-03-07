/*
 * SBSPlaySamplesTableModel.java
 * Copyright (C) 2010, 2011  Donald G Gray
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
import java.io.*;
import javax.sound.midi.*;

public class SBSPlaySamplesTableModel extends AbstractTableModel
{
	/*
	 * version 1.1.12
	 *
	 */
	 private static final long serialVersionUID = 1L;

   private Connection conn;
   private Integer soundbank_id;
   private Statement stmt;
   private ResultSet rset;
   private ArrayList<Object[]> cache = new ArrayList<Object[]>(1000);
   private String[]  names = {"Instrument",
                              "Bank",
                              "Number"};
   private String  instrument;
   private int     bank;
   private int     number;

   private Soundbank soundbank;

   public SBSPlaySamplesTableModel(Connection aConnection, Soundbank aSoundbank)
   {
      conn = aConnection;
      soundbank = aSoundbank;
   }

   public void query() throws Exception
   {
      cache.clear();

      Instrument[] instruments = soundbank.getInstruments();

      for (int ii = 0; ii < instruments.length; ii++)
      {
	      instrument = instruments[ii].toString();
	   	bank       = instruments[ii].getPatch().getBank();
	   	number     = instruments[ii].getPatch().getProgram();

         Object[] record = {instrument,
                            bank,
                            number};

         cache.add(record);
	   }

	   fireTableDataChanged();  // added for safety 6 nov 2011
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
      //return ((Object[])cache.get(row))[col];
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