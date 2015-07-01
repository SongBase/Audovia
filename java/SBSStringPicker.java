/*
 * SBSStringPicker.java
 * Copyright (C) 2010, 2011, 2014, 2015  Donald G Gray
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

import org.jfugue.*;

public class SBSStringPicker extends JDialog
{
	/*
	 * version 3.0.0
	 *
	 */

   private JDialog frame = SBSStringPicker.this;

   private JTable tableField;

   private Connection conn;

   private int[] selectedRows;

   private String status;
   private Integer song_id;
   private String  song_name;
   private String  parent_name;
   private Integer string_id;
   private String  string_name;
   private String  string_value;
   private String  constants;
   private String  parse_value;

   private SBSStringsTableModel parentTableModel;

   private SBSStringPickerTableModel tableModel;
   private String title = "SBSStringPicker";

   public SBSStringPicker(Connection aConnection, String aParentName,
                     Integer aSongId,
                     String aSongName,
                     SBSStringsTableModel aTableModel) throws Exception
   {
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new
         WindowAdapter()
         {
				public void windowClosing(WindowEvent e)
				{
					quit();
				}
			});

      setModal(true);
      setSize(600,454);
      setLocation(150,150);
      setTitle(title + " - Import to: " + aParentName);

      ImageIcon icon = new ImageIcon("SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      conn = aConnection;
      song_id = aSongId;
      song_name = aSongName;
      parent_name = aParentName;
      parentTableModel = aTableModel;

      tableModel = new SBSStringPickerTableModel(conn, song_id);
      tableModel.query();
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
      newPanelN.setBorder(BorderFactory.createEmptyBorder(5,5,0,0));
      JPanel newPanelS = new JPanel();
      newPanelS.setLayout(new BorderLayout());
      newPanelS.setBackground(new Color(232, 204, 255));
      newPanelS.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
      newPanel.add(newPanelN, BorderLayout.NORTH);
      newPanel.add(newPanelS, BorderLayout.SOUTH);

      Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      JLabel songField = new JLabel("Song: ");
      songField.setFont(labelFont);
      Dimension dim1 = songField.getPreferredSize();
	   dim1.width = 100;
      songField.setPreferredSize(dim1);
      newPanelN.add(songField, BorderLayout.WEST);

      JLabel songNameField = new JLabel(parent_name);
      songNameField.setFont(titleFont);
      newPanelN.add(songNameField, BorderLayout.CENTER);

      JLabel importField = new JLabel("Import from: ");
      importField.setFont(labelFont);
      Dimension dim2 = importField.getPreferredSize();
	   dim2.width = 100;
      importField.setPreferredSize(dim2);
      newPanelS.add(importField, BorderLayout.WEST);

      JLabel importNameField = new JLabel(song_name);
      importNameField.setFont(titleFont);
      newPanelS.add(importNameField, BorderLayout.CENTER);

      topPanel.add(newPanel, BorderLayout.NORTH);

      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      headerPanel.setBackground(new Color(232, 204, 255));
      JLabel headerField = new JLabel("Strings");
      headerField.setFont(boldItalic);
      headerField.setForeground(new Color(85, 26, 139));
      headerPanel.add(headerField);
      topPanel.add(headerPanel, BorderLayout.SOUTH);

      contentPane.add(topPanel, BorderLayout.NORTH);

      tableField = new JTable(tableModel);
      tableField.setShowGrid(true);
      tableField.setGridColor(Color.gray);
      tableField.setSurrendersFocusOnKeystroke(true);
      tableField.setRowHeight(40);
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
      TableColumn column3 = columnModel.getColumn(3);

      tableField.removeColumn(column0);
      tableField.removeColumn(column1);

      column3.setPreferredWidth(300);

      column2.setCellRenderer(new TextFieldRenderer());

      column3.setCellRenderer(new TextAreaRenderer());

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();
      buttonPanel.setBackground(new Color(232, 204, 255));

      JButton importButton = new JButton("Import");
      JButton quitButton   = new JButton("Quit");

      buttonPanel.add(importButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      ImportAction importAction = new ImportAction();
      QuitAction quitAction     = new QuitAction();

      importButton.addActionListener(importAction);
      quitButton.addActionListener(quitAction);
   }

   private class ImportAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRows = tableField.getSelectedRows();

         if (selectedRows.length > 0)
         {
            for (int i = 0; i < selectedRows.length; i++)
            {
               string_name  = (String)tableModel.getValueAt(selectedRows[i],2);
               string_value = (String)tableModel.getValueAt(selectedRows[i],3);

               int location = parentTableModel.getRowCount() - 1;
               parentTableModel.insertRow(location);
               parentTableModel.setValueAt(string_name, location, 2);
               parentTableModel.setValueAt(string_value, location, 3);
				}
            Messages.plainMessage(frame, title, "String(s) imported.");
         }
         else
         {
            Messages.plainMessage(frame, title, "No selection made.");
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
