/*
 * SBSSongPicker.java
 * Copyright (C) 2010, 2011, 2014  Donald G Gray
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
import javax.xml.parsers.*;
import javax.swing.border.*;
import java.net.*;

public class SBSSongPicker extends JDialog
{
	/*
	 * version 2.2.22
	 *
	 */

   private JDialog frame = SBSSongPicker.this;

   private JTable tableField;

   private Connection conn;

   private Integer parent_id;
   private String  parent_name;

   private int selectedRow;
   private int selectedCol;

   private String  status;
   private Integer song_id;
   private String  song_name;

   private String  access_type;

   private SBSStringsTableModel parentTableModel;

   private SBSSongPickerTableModel tableModel;
   private String title = "SBSSongPicker";

   public SBSSongPicker(Connection aConnection,
                        Integer aParentId, String aParentName,
                        SBSStringsTableModel aTableModel) throws Exception
   {
      setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
      addWindowListener(new
         WindowAdapter()
         {
				public void windowClosing(WindowEvent e)
				{
					quit();
				}
			});

      setModal(true);
		setSize(600,444);
      //setLocation(100,100);
      setLocation(125,125);

      setTitle(title + " - Import to: " + aParentName);

      ImageIcon icon = new ImageIcon("SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      conn = aConnection;
      parent_id = aParentId;
      parent_name = aParentName;
      parentTableModel = aTableModel;

      tableModel = new SBSSongPickerTableModel(conn, parent_id);
      tableModel.query("");
      conn.commit();

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.setBackground(new Color(232, 204, 255));

      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BorderLayout());
      topPanel.setBackground(new Color(232, 204, 255));
      topPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));

      JPanel newPanel = new JPanel();
      newPanel.setLayout(new BorderLayout());
      newPanel.setBackground(new Color(232, 204, 255));
      newPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
      JPanel newPanelN = new JPanel();
      newPanelN.setLayout(new BorderLayout());
      newPanelN.setBackground(new Color(232, 204, 255));
      newPanelN.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
      //JPanel newPanelS = new JPanel();
      //newPanelS.setLayout(new BorderLayout());
      //newPanelS.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
      newPanel.add(newPanelN, BorderLayout.NORTH);
      //newPanel.add(newPanelS, BorderLayout.SOUTH);

      Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      JLabel songField = new JLabel("Song: ");
      songField.setFont(labelFont);
      Dimension dim1 = songField.getPreferredSize();
	   dim1.width = 55;
      songField.setPreferredSize(dim1);
      newPanelN.add(songField, BorderLayout.WEST);

      JLabel songNameField = new JLabel(parent_name);
      songNameField.setFont(titleFont);
      newPanelN.add(songNameField, BorderLayout.CENTER);

      //JPanel titlePanel = new JPanel();
      //titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

      //Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      //Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      //Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      //JLabel labelField = new JLabel("Song: ");
      //labelField.setFont(labelFont);
      //titlePanel.add(labelField);

      //JTextField titleField = new JTextField(song_name, 35); // was 40
      //JLabel titleField = new JLabel(song_name);
      //titleField.setFont(titleFont);
      //titleField.setHorizontalAlignment(JTextField.TRAILING);
      //titleField.setEditable(false);
      //titleField.setFont(titleFont);

      //Dimension dim1 = titleField.getPreferredSize();
      //dim1.width = 400;
      //titleField.setPreferredSize(dim1);

      //titlePanel.add(titleField);

      topPanel.add(newPanel, BorderLayout.NORTH);

      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      headerPanel.setBackground(new Color(232, 204, 255));
      JLabel headerField = new JLabel("Import Strings from:");
      headerField.setFont(boldItalic);
      headerField.setForeground(new Color(85, 26, 139));
      headerPanel.add(headerField);
      topPanel.add(headerPanel, BorderLayout.SOUTH);

      //JPanel titlePanel = new JPanel();
      //titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      //titlePanel.setBackground(new Color(232, 204, 255));
      //JLabel titleField = new JLabel("Import Strings from:");

      //Font titleFont = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);
      //titleField.setFont(titleFont);
      //titlePanel.add(titleField);
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,0));
      contentPane.add(topPanel, BorderLayout.NORTH);

      tableField = new JTable(tableModel);
      tableField.setShowGrid(true);
      tableField.setGridColor(Color.gray);
      tableField.setSurrendersFocusOnKeystroke(true);
      tableField.setRowHeight(30);
      tableField.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

      JTableHeader header = tableField.getTableHeader();
      Font headerFont = new Font("Liberation Sans", Font.BOLD, 15);
      header.setFont(headerFont);
      Dimension dim = header.getPreferredSize();
      dim.height = 34;
      header.setPreferredSize(dim);

      TableColumnModel columnModel = tableField.getColumnModel();
      TableColumn column0 = columnModel.getColumn(0);
      TableColumn column1 = columnModel.getColumn(1);
      TableColumn column2 = columnModel.getColumn(2);

      tableField.removeColumn(column0);
      tableField.removeColumn(column1);

      column2.setCellRenderer(new TextFieldRenderer());

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);

      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();
      buttonPanel.setBackground(new Color(232, 204, 255));

      JButton searchButton   = new JButton("Search");
      JButton stringsButton  = new JButton("Select Strings");
      JButton quitButton     = new JButton("Quit");

      buttonPanel.add(searchButton);
      buttonPanel.add(stringsButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      SearchAction searchAction     = new SearchAction();
      StringsAction stringsAction   = new StringsAction();
      QuitAction quitAction         = new QuitAction();

      searchButton.addActionListener(searchAction);
      stringsButton.addActionListener(stringsAction);
      quitButton.addActionListener(quitAction);
   }

   private class SearchAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
            Search search = new Search(title);
            search.setVisible(true);
            int selection1 = search.getSelection();
            if (selection1 == 0)
            {
               try
               {
                  String text = search.getSearch();
                  tableModel.query(text);
                  conn.commit();
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
                  Messages.exceptionHandler(frame, title, e);
               }
            }
            search.dispose();
            tableField.requestFocusInWindow();
      }
   }

   private class StringsAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status                = (String)tableModel.getValueAt(i,0);
            song_id               = (Integer)tableModel.getValueAt(i,1);
            song_name             = (String)tableModel.getValueAt(i,2);

            try
            {
               SBSStringPicker strings = new SBSStringPicker(conn,
                                                             parent_name,
                                                             song_id,
                                                             song_name,
                                                             parentTableModel);
               strings.setVisible(true);

               tableField.requestFocusInWindow();
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
				   Messages.exceptionHandler(frame, title, e);
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private void quit()
   {
         dispose();
	}

   private class QuitAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         dispose();
      }
   }
}
