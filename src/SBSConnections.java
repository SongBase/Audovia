/*
 * SBSConnections.java
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

import javax.swing.plaf.basic.*;

public class SBSConnections extends JFrame
{
	/*
	 * version 3.0.12
	 *
	 */

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSConnections.this;

   private Class c = Class.forName("SBSConnections");
   private Field childCountField = c.getField("childCount");
   public  int childCount = 0;

   private JTable tableField;

   private Connection conn;

   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement updateStmt2;
   private PreparedStatement selectStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private PreparedStatement sequenceStmt;
   private CallableStatement cSselectStmt;

   private Statement stmt;

   private int selectedRow;
   private int selectedCol;
   private String status;
   private Integer connection_id;
   private String connection_name;
   private String description;
   private String db_type;
   private String host;
   private String port;
   private String database;
   private String user;
   private String passwordString;

   private SBSConnectionsTableModel tableModel;
   private String title = "SBSConnections";

   public SBSConnections(JFrame aParentFrame,
                        Field aSiblingCountField,
                        Connection aConnection, String aConnectionName) throws Exception
   {
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new
         WindowAdapter()
         {
				public void windowClosing(WindowEvent e)
				{
               if (tableField.isEditing())
               {
                  int editingRow = tableField.getEditingRow();
                  int editingCol = tableField.getEditingColumn();
                  TableCellEditor tableEditor = tableField.getCellEditor();
                  tableEditor.stopCellEditing();
                  tableField.setValueAt(tableEditor.getCellEditorValue(),
                     editingRow, editingCol);
                  tableField.requestFocusInWindow();
               }
					quit();
				}
			});

      setSize(600,418);
      setLocation(100,100);
      setTitle(title + " (" + aConnectionName + ")");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      parentFrame = aParentFrame;
      siblingCountField = aSiblingCountField;
      conn = aConnection;

      updateStmt = conn.prepareStatement
                   ("update sbs_connection " +
                    "set connection_name = ? " +
                    "where connection_id = ? ");

      updateStmt2 = conn.prepareStatement
                   ("update sbs_connection " +
                    "set db_type = ?, " +
                    "    host_name = ?, " +
                    "    port = ?, " +
                    "    db_name = ?, " +
                    "    user_name = ?, " +
                    "    password = ? " +
                    "where connection_id = ? ");

      selectStmt = conn.prepareStatement
                   ("select connection_name, db_type, host_name, port, db_name, user_name, password " +
                    "from sbs_connection " +
                    "where connection_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSselectStmt = conn.prepareCall("{call select_connection(?)}");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_connection " +
                       "(connection_name) " +
                       "values (?)", Statement.RETURN_GENERATED_KEYS);
		}
      else
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_connection " +
                       "(connection_id, connection_name) " +
                       "values (?,?)");
	   }

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_connection " +
                    "where connection_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select nextval('sbs_seq_connection')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select sbs_seq_connection.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select next value for sbs_seq_connection from (values 1) v");
		}

      tableModel = new SBSConnectionsTableModel(conn);
      tableModel.query();
      conn.commit();

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      menuBar.setUI ( new BasicMenuBarUI ()
		    {
		        public void paint ( Graphics g, JComponent c )
		        {
		            g.setColor ( new Color(232, 204, 255) );
		            g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
		        }
          } );


      JMenu fileMenu = new JMenu("File");
      menuBar.add(fileMenu);

      JMenuItem createItem    = new JMenuItem("Create Tables");

      fileMenu.add(createItem);

      CreateAction createAction = new CreateAction();
      createItem.addActionListener(createAction);

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      JLabel titleField = new JLabel("Database Connections");
      Font titleFont = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);
      titleField.setFont(titleFont);
      titleField.setForeground(new Color(85, 26, 139));
      titlePanel.add(titleField);
      titlePanel.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
      contentPane.add(titlePanel, BorderLayout.NORTH);

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

      TextFieldEditor connectionFieldEditor = new TextFieldEditor();
      connectionFieldEditor.setFilter("[\\w !£$%^&\\*()\\-+={\\[}\\]:;@'~#\\\\<,>\\.?/]*");
      column2.setCellEditor(connectionFieldEditor);

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton insertButton = new JButton("Insert");
      JButton deleteButton = new JButton("Delete");
      JButton saveButton   = new JButton("Save");
      JButton connectionButton = new JButton("Connection Details");
      JButton connectToButton  = new JButton("Connect");
      JButton quitButton   = new JButton("Quit");

      buttonPanel.add(insertButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(connectionButton);
      buttonPanel.add(connectToButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InsertAction insertAction = new InsertAction();
      DeleteAction deleteAction = new DeleteAction();
      SaveAction saveAction     = new SaveAction();
      ConnectionAction connectionAction = new ConnectionAction();
      ConnectToAction  connectToAction  = new ConnectToAction();
      QuitAction quitAction     = new QuitAction();

      insertButton.addActionListener(insertAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
      connectionButton.addActionListener(connectionAction);
      connectToButton.addActionListener(connectToAction);
      quitButton.addActionListener(quitAction);
   }

   private class InsertAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableModel.insertRow(selectedRow);
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            tableField.requestFocusInWindow();
         }
         else
         {
            Messages.plainMessage(frame, title, "No selection made.");
         }
      }
   }

   private class DeleteAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int selection = Messages.plainQuestion(frame, title, "OK to Delete?");
            if (selection == 0)
            {
               status     = (String)tableModel.getValueAt(selectedRow, 0);
               connection_id = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     deleteStmt.setInt(1,connection_id.intValue());
                     deleteStmt.execute();
                     conn.commit();

                     if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
                     {
                        CallableStatement cs = conn.prepareCall
                                               ("CALL SYSCS_UTIL.SYSCS_COMPRESS_TABLE(?, ?, ?)");
                        cs.setString(1, "APP");
                        cs.setString(2, "SBS_CONNECTION");
                        cs.setShort(3, (short)1);
                        cs.execute();
                        conn.commit();
                     }
                  }
                  tableModel.deleteRow(selectedRow);
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
         }
         else
         {
            Messages.plainMessage(frame, title, "No selection made.");
         }
      }
   }

   private class SaveAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         int i;
         selectedRow = 0;
         try
         {
            for (i=0; i<tableModel.getRowCount(); i++)
            {
               selectedRow        = i;
               status             = (String)tableModel.getValueAt(i,0);
               connection_id      = (Integer)tableModel.getValueAt(i,1);
               connection_name    = (String)tableModel.getValueAt(i,2);

               save();
            }
            conn.commit();
            for (i=0; i<tableModel.getRowCount(); i++)
            {
               status  = (String)tableModel.getValueAt(i,0);
               if (status.equals("changed") || status.equals("inserted"))
               {
                  tableModel.setValueAt("unchanged", i, 0);
               }
            }
            Messages.plainMessage(frame, title, "Data saved.");
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
            tableField.changeSelection(selectedRow, 0, false, false);
            Messages.exceptionHandler(frame, title, e);
         }
      }
   }

   private class ConnectionAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status   = (String)tableModel.getValueAt(i,0);
            connection_id  = (Integer)tableModel.getValueAt(i,1);
            connection_name = (String)tableModel.getValueAt(i,2);
            if (! status.equals("new"))
            {
               int selection = 0;
               if (status.equals("changed") || status.equals("inserted"))
               {
                  selection = Messages.plainQuestion(frame, title, "OK to Save current row?");
               }
               if (selection == 0)
               {
                  try
                  {
                     save();
                     conn.commit();
                     if (status.equals("changed") || status.equals("inserted"))
                     {
                        tableModel.setValueAt("unchanged", i, 0);
                     }
                     db_type = null;
                     host = null;
                     port = null;
                     database = null;
                     user = null;
                     passwordString = null;

                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
			               cSselectStmt.setInt(1, connection_id.intValue());
			               cSselectStmt.execute();
			               rset = cSselectStmt.getResultSet();
		               }
		               else
		               {
                        selectStmt.setInt(1, connection_id.intValue());
                        rset = selectStmt.executeQuery();
							}
                     while (rset.next())
                     {
                        db_type = rset.getString(2);
                        host = rset.getString(3);
                        port = rset.getString(4);
                        database = rset.getString(5);
                        user = rset.getString(6);
                        passwordString = rset.getString(7);
                     }
                     rset.close();
                     conn.commit();
                     ConnectionDetails connectionDetails = new ConnectionDetails(db_type, host, port, database, user, passwordString);
                     connectionDetails.setVisible(true);
                     while (connectionDetails.getSelection() == 0)
                     {
                        try
                        {
                           db_type = connectionDetails.getDbType();
                           host = connectionDetails.getHost();
                           port = connectionDetails.getPort();
                           database  = connectionDetails.getDatabase();
                           user = connectionDetails.getUser();
                           char[] password = connectionDetails.getPassword();
                           passwordString = "";
                           for (int ii = 0; ii < password.length; ii++)
                             passwordString += password[ii];

                           if (db_type == null)
                           {
                              Messages.plainMessage(frame, title, "Select a connection type.");
                              connectionDetails.setVisible(true);
                              continue;
                           }

                           if (db_type.equals("Oracle"))
                           {
                              Class.forName("oracle.jdbc.OracleDriver");
                              Connection conn1 = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + database,
                                                                             user, passwordString);
                              conn1.close();
                              break;
                           }

                           if (db_type.equals("Postgres"))
                           {
                              Class.forName("org.postgresql.Driver");
                              Connection conn1 = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database,
                                                                            user, passwordString);
                              conn1.close();
                              break;
                           }

                           if (db_type.equals("MySQL"))
                           {
                              Class.forName("com.mysql.jdbc.Driver");
                              Connection conn1 = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                                                                             database + "?user=" + user + "&password=" + passwordString + "&noAccessToProcedureBodies=true");

                              conn1.close();
                              break;
                           }

                           Messages.plainMessage(frame, title, "Invalid connection type.");
                           connectionDetails.setVisible(true);
                        }
                        catch (Exception e2)
                        {
                           Messages.exceptionHandler(frame, "Connection Details", e2);
                           connectionDetails.setVisible(true);
                        }
                     }
                     if (connectionDetails.getSelection() == 0)
                     {
                        updateStmt2.setString(1, db_type);
                        updateStmt2.setString(2, host);
                        updateStmt2.setString(3, port);
                        updateStmt2.setString(4, database);
                        updateStmt2.setString(5, user);
                        updateStmt2.setString(6, passwordString);
                        updateStmt2.setInt(7, connection_id.intValue());
                        updateStmt2.execute();
                        conn.commit();
                        Messages.plainMessage(frame, "Connection Details", "Data saved.");
                     }
                     connectionDetails.dispose();  // added 27/07/2010
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
            }
            else
            {
               Messages.plainMessage(frame, title, "Null record selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private class ConnectToAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status   = (String)tableModel.getValueAt(i,0);
            connection_id  = (Integer)tableModel.getValueAt(i,1);
            connection_name = (String)tableModel.getValueAt(i,2);
            if (! status.equals("new"))
            {
               int selection = 0;
               if (status.equals("changed") || status.equals("inserted"))
               {
                  selection = Messages.plainQuestion(frame, title, "OK to Save current row?");
               }
               if (selection == 0)
               {
                  try
                  {
                     save();
                     conn.commit();
                     if (status.equals("changed") || status.equals("inserted"))
                     {
                        tableModel.setValueAt("unchanged", i, 0);
                     }
                     connection_name = null;
                     db_type = null;
                     host = null;
                     port = null;
                     database = null;
                     user = null;
                     passwordString = null;

                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
			               cSselectStmt.setInt(1, connection_id.intValue());
			               cSselectStmt.execute();
			               rset = cSselectStmt.getResultSet();
		               }
		               else
		               {
                        selectStmt.setInt(1, connection_id.intValue());
                        rset = selectStmt.executeQuery();
						   }
                     while (rset.next())
                     {
								connection_name = rset.getString(1);
                        db_type = rset.getString(2);
                        host = rset.getString(3);
                        port = rset.getString(4);
                        database = rset.getString(5);
                        user = rset.getString(6);
                        passwordString = rset.getString(7);
                     }
                     rset.close();
                     conn.commit();

                     if (db_type == null)
                     {
                        Messages.plainMessage(frame, title, "Database connection details not defined.");
                     }
                     else if (db_type.equals("Oracle"))
                     {
                        Class.forName("oracle.jdbc.OracleDriver");
                        Connection conn1 = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + database,
                                                                       user, passwordString);
                        conn1.setAutoCommit(false);
                        SBSSongs songs = new SBSSongs(frame, childCountField, conn1, connection_name, false);
                        songs.setVisible(true);
                        childCount++;
                     }

                     else if (db_type.equals("MySQL"))
                     {
                        Class.forName("com.mysql.jdbc.Driver");
								Connection conn1 = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                                                                        database + "?user=" + user + "&password=" + passwordString + "&noAccessToProcedureBodies=true");
                        conn1.setAutoCommit(false);
                        SBSSongs songs = new SBSSongs(frame, childCountField, conn1, connection_name, false);
                        songs.setVisible(true);
                        childCount++;
                     }

                     else if (db_type.equals("Postgres"))
                     {
                        Class.forName("org.postgresql.Driver");
                        Connection conn1 = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database,
                                                                      user, passwordString);
                        conn1.setAutoCommit(false);
                        SBSSongs songs = new SBSSongs(frame, childCountField, conn1, connection_name, false);
                        songs.setVisible(true);
                        childCount++;
                     }
                     else
                     {
                        Messages.plainMessage(frame, title, "Invalid connection type.");
					      }
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
            }
            else
            {
               Messages.plainMessage(frame, title, "Null record selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private class QuitAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         quit();
      }
   }

   private void quit()
   {
         int i;
         int updated = 0;
         int selection = 0;
         for (i=0; i<tableModel.getRowCount(); i++)
         {
            status = (String)tableModel.getValueAt(i,0);
            if (status.equals("changed") || status.equals("inserted"))
            {
               updated = 1;
               break;
            }
         }
         if (updated == 1)
         {
         selection = Messages.warningQuestion(frame, title, "OK to Quit? - Data not saved.");
         }
         else
         {
            if (childCount > 0)
            {
               selection = Messages.warningQuestion(frame, title,
                           "OK to Quit? - Data in popup windows may not be saved.");
            }
         }
         if (selection == 0)
         {
            try
            {
               int siblingCount = siblingCountField.getInt(parentFrame);
               siblingCount--;
               siblingCountField.setInt(parentFrame, siblingCount);
               conn.rollback();
               dispose();
            }
            catch (Exception e)
            {
               Messages.exceptionHandler(frame, title, e);
               dispose();
            }
         }
	}

   private void save() throws Exception
   {
      if (status.equals("changed"))
      {
			if (connection_name != null) connection_name = connection_name.trim();

         updateStmt.setString(1, connection_name);
         updateStmt.setInt(2, connection_id.intValue());
         updateStmt.execute();
      }
      if (status.equals("inserted"))
      {
			if (connection_name != null) connection_name = connection_name.trim();

			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
         insertStmt.setString(1,connection_name);
         insertStmt.execute();

         rset = insertStmt.getGeneratedKeys();
         while (rset.next()) connection_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(connection_id, selectedRow, 1);
			}
			else
         {
         rset = sequenceStmt.executeQuery();
         while (rset.next()) connection_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(connection_id, selectedRow, 1);

         insertStmt.setInt(1,connection_id.intValue());
         insertStmt.setString(2,connection_name);
         insertStmt.execute();
		   }
      }
   }

   private class CreateAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         if (tableField.isEditing())
         {
            int editingRow = tableField.getEditingRow();
            int editingCol = tableField.getEditingColumn();
            TableCellEditor tableEditor = tableField.getCellEditor();
            tableEditor.stopCellEditing();
            tableField.setValueAt(tableEditor.getCellEditorValue(),
               editingRow, editingCol);
            tableField.requestFocusInWindow();
         }
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status   = (String)tableModel.getValueAt(i,0);
            connection_id  = (Integer)tableModel.getValueAt(i,1);
            connection_name = (String)tableModel.getValueAt(i,2);
            if (! status.equals("new"))
            {
               int selection = 0;
               if (status.equals("changed") || status.equals("inserted"))
               {
                  selection = Messages.plainQuestion(frame, title, "OK to Save current row?");
               }
               if (selection == 0)
               {
                  try
                  {
                     save();
                     conn.commit();
                     if (status.equals("changed") || status.equals("inserted"))
                     {
                        tableModel.setValueAt("unchanged", i, 0);
                     }
                     connection_name = null;
                     db_type = null;
                     host = null;
                     port = null;
                     database = null;
                     user = null;
                     passwordString = null;

                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
			               cSselectStmt.setInt(1, connection_id.intValue());
			               cSselectStmt.execute();
			               rset = cSselectStmt.getResultSet();
		               }
		               else
		               {
                        selectStmt.setInt(1, connection_id.intValue());
                        rset = selectStmt.executeQuery();
							}
                     while (rset.next())
                     {
								connection_name = rset.getString(1);
                        db_type = rset.getString(2);
                        host = rset.getString(3);
                        port = rset.getString(4);
                        database = rset.getString(5);
                        user = rset.getString(6);
                        passwordString = rset.getString(7);
                     }
                     rset.close();
                     conn.commit();

                     if (db_type == null)
                     {
                        Messages.plainMessage(frame, title, "Database connection details not defined.");
                     }
                     else if (db_type.equals("Oracle"))
                     {
                        Class.forName("oracle.jdbc.OracleDriver");
                        Connection conn1 = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + database,
                                                                       user, passwordString);
                        conn1.setAutoCommit(true);

            stmt = conn1.createStatement();
            stmt.execute("create table sbs_soundbank " +
                         "(" +
                         "soundbank_id	  integer," +
                         "soundbank_name varchar2(100) not null," +
                         "soundbank		  blob," +
                         "primary key (soundbank_id)," +
                         "unique (soundbank_name), check(length(soundbank_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_soundbank");
            stmt.execute("create table sbs_song " +
                         "(" +
                         "song_id			      integer," +
                         "song_name		         varchar2(200) not null," +
                         "numeric_duration_type	varchar2(10)," +
                         "soundbank_id          integer," +
                         "primary key (song_id)," +
                         "foreign key (soundbank_id) references sbs_soundbank," +
                         "unique (song_name), check(length(song_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_song");
            stmt.execute("create table sbs_component " +
                         "(" +
                         "component_id		integer," +
                         "song_id			integer," +
                         "component_type		varchar2(10)," +
                         "component_name		varchar2(100)	not null," +
                         "string_value		varchar2(4000)," +
                         "primary key (component_id)," +
                         "foreign key (song_id) references sbs_song," +
                         "unique (song_id, component_type, component_name)," +
                         "check(length(component_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_component");
            stmt.execute("create table sbs_pattern_component " +
                         "(" +
                         "pattern_component_id	integer," +
                         "pattern_id		integer," +
                         "component_position	integer		not null," +
                         "component_id		integer," +
                         "anonymous_string varchar2(4000)," +
                         "primary key (pattern_component_id)," +
                         "foreign key (pattern_id) references sbs_component," +
                         "foreign key (component_id) references sbs_component," +
                         "unique (pattern_id, component_position)," +
                         "check(pattern_id != component_id)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_pattern_component");
            stmt.execute("create table sbs_connection " +
                         "(" +
                         "connection_id    integer," +
                         "connection_name  varchar2(200) not null," +
                         "db_type          varchar2(20)," +
                         "host_name        varchar2(400)," +
                         "port             varchar2(8)," +
                         "db_name         varchar2(100)," +
                         "user_name        varchar2(100)," +
                         "password         varchar2(100)," +
                         "primary key (connection_id)," +
                         "unique (connection_name), check(length(connection_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_connection");
            stmt.close();

                        Messages.plainMessage(frame, title, "Database tables created.");
                     }

                     else if (db_type.equals("MySQL"))
                     {
                        Class.forName("com.mysql.jdbc.Driver");
								Connection conn1 = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" +
                                                                        database + "?user=" + user + "&password=" + passwordString + "&noAccessToProcedureBodies=true");
                        conn1.setAutoCommit(true);

            stmt = conn1.createStatement();
            stmt.execute("create table sbs_soundbank " +
                         "(" +
                         "soundbank_id	  integer unsigned auto_increment," +
                         "soundbank_name varchar(100) not null," +
                         "soundbank		  longblob," +
                         "primary key (soundbank_id)," +
                         "unique (soundbank_name), check(length(soundbank_name)>0)" +
                         ")engine = 'InnoDB'"
                        );
            //stmt.execute("create sequence sbs_seq_soundbank");

            stmt.execute("create table sbs_user " +
                         "(" +
                         "user_id	  integer unsigned auto_increment," +
                         "user_name   varchar(100) not null," +
                         "password    varchar(100) not null," +
                         "e_mail      varchar(100) not null," +
                         "primary key (user_id)," +
                         "unique (user_name), check(length(user_name)>0)" +
                         ")engine = 'InnoDB'"
                        );

            stmt.execute("create table sbs_song " +
                         "(" +
                         "song_id			      integer unsigned auto_increment," +
                         "song_name		         varchar(200) not null," +
                         "numeric_duration_type	varchar(10)," +
                         "soundbank_id          integer unsigned," +
                         "owner_id              integer unsigned," +
                         "primary key (song_id)," +
                         "foreign key (soundbank_id) references sbs_soundbank(soundbank_id)," +
                         "foreign key (owner_id) references sbs_user(user_id)," +
                         "unique (song_name), check(length(song_name)>0)" +
                         ")engine = 'InnoDB'"
                        );
            //stmt.execute("create sequence sbs_seq_song");

            stmt.execute("create table sbs_share " +
                         "(" +
                         "share_id			      integer unsigned auto_increment," +
                         "song_id			      integer unsigned," +
                         "shared_with_id			integer unsigned," +
                         "primary key (share_id)," +
                         "unique (song_id, shared_with_id)," +
                         "foreign key (song_id) references sbs_song(song_id)," +
                         "foreign key (shared_with_id) references sbs_user(user_id)" +
                         ")engine = 'InnoDB'"
                        );

            stmt.execute("create table sbs_component " +
                         "(" +
                         "component_id		integer unsigned auto_increment," +
                         "song_id			integer unsigned," +
                         "component_type		varchar(10)," +
                         "component_name		varchar(100)	not null," +
                         "string_value		varchar(4000)," +
                         "primary key (component_id)," +
                         "foreign key (song_id) references sbs_song(song_id)," +
                         "unique (song_id, component_type, component_name)," +
                         "check(length(component_name)>0)" +
                         ")engine = 'InnoDB'"
                        );
            //stmt.execute("create sequence sbs_seq_component");
            stmt.execute("create table sbs_pattern_component " +
                         "(" +
                         "pattern_component_id	integer unsigned auto_increment," +
                         "pattern_id		integer unsigned," +
                         "component_position	integer		not null," +
                         "component_id		integer unsigned," +
                         "anonymous_string varchar(4000)," +
                         "primary key (pattern_component_id)," +
                         "foreign key (pattern_id) references sbs_component(component_id)," +
                         "foreign key (component_id) references sbs_component(component_id)," +
                         "unique (pattern_id, component_position)," +
                         "check(pattern_id != component_id)" +
                         ")engine = 'InnoDB'"
                        );
            //stmt.execute("create sequence sbs_seq_pattern_component");
            stmt.execute("create table sbs_connection " +
                         "(" +
                         "connection_id    integer unsigned auto_increment," +
                         "connection_name  varchar(200) not null," +
                         "db_type          varchar(20)," +
                         "host_name        varchar(400)," +
                         "port             varchar(8)," +
                         "db_name         varchar(100)," +
                         "user_name        varchar(100)," +
                         "password         varchar(100)," +
                         "primary key (connection_id)," +
                         "unique (connection_name), check(length(connection_name)>0)" +
                         ")engine = 'InnoDB'"
                        );
            //stmt.execute("create sequence sbs_seq_connection");

stmt.execute(
"create procedure get_songs(IN searchString VARCHAR(200)) " +
"begin " +
"select s.song_id, s.song_name, s.numeric_duration_type, " +
"       s.soundbank_id, sb.soundbank_name " +
"from sbs_song s left join sbs_soundbank sb on s.soundbank_id = sb.soundbank_id " +
"where upper(s.song_name) like upper(concat('%', searchString, '%')) " +
"order by upper(s.song_name); " +
"end"
);

stmt.execute(
"create procedure get_soundbanks() " +
"begin " +
"select s.soundbank_id, " +
"       s.soundbank_name " +
"from sbs_soundbank s " +
"order by upper(s.soundbank_name); " +
"end"
);

stmt.execute(
"create procedure get_patterns(IN songId INT) " +
"begin " +
"select component_id, component_name " +
"from sbs_component " +
"where song_id = songId " +
"  and  component_type = 'pattern' " +
"order by upper(component_name); " +
"end"
);

stmt.execute(
"create procedure get_pattern_components(IN patternId INT) " +
"begin " +
"select p.pattern_component_id, " +
"       p.component_position, " +
"       p.component_id, " +
"       concat('<',  c.component_type, '> ', c.component_name), " +
"       p.anonymous_string " +
"from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
"where p.pattern_id = patternId " +
"order by p.component_position; " +
"end"
);

stmt.execute(
"create procedure get_components(IN songId INT, IN patternId INT) " +
"begin " +
"select c.component_id, " +
"       concat('<',  c.component_type, '> ', c.component_name) " +
"from sbs_component c " +
"where c.song_id =  songId " +
"  and c.component_id !=  patternId " +
"order by c.component_type, c.component_name; " +
"end"
);

stmt.execute(
"create procedure get_strings(IN songId INT) " +
"begin " +
"select component_id, component_name, string_value " +
"from sbs_component " +
"where song_id = songId " +
"  and component_type = 'string' " +
"order by upper(component_name); " +
"end"
);

stmt.execute(
"create procedure get_top_level_patterns(IN songId INT) " +
"begin " +
"select component_id, component_name " +
"from sbs_component " +
"where song_id =  songId " +
"  and component_type = 'pattern' " +
"  and component_id not in (select component_id from sbs_pattern_component where component_id is not null) " +
"order by upper(component_name); " +
"end"
);

stmt.execute(
"create procedure get_count_components(IN patternId INT) " +
"begin " +
"select count(*) " +
"from sbs_pattern_component p " +
"where p.pattern_id =  patternId; " +
"end"
);

stmt.execute(
"create procedure get_tree_components(IN patternId INT) " +
"begin " +
"select p.component_id, " +
"       c.component_type, " +
"       c.component_name, " +
"       c.string_value, " +
"       p.component_position, " +
"       p.anonymous_string, " +
"       p.pattern_component_id " +
"from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
"where p.pattern_id = patternId " +
"order by p.component_position; " +
"end"
);

stmt.execute(
"create procedure song_picker(IN searchString VARCHAR(200), IN parentId INT) " +
"begin " +
"select s.song_id, s.song_name " +
"from sbs_song s " +
"where upper(s.song_name) like upper(concat('%', searchString, '%')) " +
"  and s.song_id != parentId " +
"order by upper(s.song_name); " +
"end"
);

stmt.execute(
"create procedure string_picker(IN songId INT) " +
"begin " +
"select component_id, component_name, string_value " +
"from sbs_component " +
"where song_id = songId " +
"  and component_type = 'string' " +
"order by upper(component_name); " +
"end"
);

stmt.execute(
"create procedure get_connections() " +
"begin " +
"select connection_id, connection_name " +
"from sbs_connection " +
"order by upper(connection_name); " +
"end"
);

stmt.execute(
"create procedure select_soundbank(IN soundbankId INT) " +
"begin " +
"select soundbank from sbs_soundbank " +
"where soundbank_id = soundbankId; " +
"end"
);

stmt.execute(
"create procedure select_connection(IN connectionId INT) " +
"begin " +
"select connection_name, db_type, host_name, port, db_name, user_name, password " +
"from sbs_connection " +
"where connection_id = connectionId; " +
"end"
);

stmt.execute(
"create procedure get_component_id(IN songId INT, IN componentType VARCHAR(10), IN componentName VARCHAR(100)) " +
"begin " +
"select component_id from sbs_component " +
"where song_id = songId and component_type = componentType and component_name = componentName; " +
"end"
);

stmt.execute(
"create procedure select_component(IN componentId INT) " +
"begin " +
"select component_type, component_name, string_value " +
"from sbs_component " +
"where component_id = componentId; " +
"end"
);

stmt.execute(
"create procedure get_constants(IN songId INT) " +
"begin " +
"select string_value " +
"from sbs_component " +
"where song_id = songId " +
"and   component_type = 'string' " +
"and   string_value like '%$%'; " +
"end"
);

stmt.execute(
"create procedure get_string_constants(IN songId INT) " +
"begin " +
"select string_value " +
"from sbs_component " +
"where song_id = songId " +
"and   component_type = 'string' " +
"and   string_value like '%~%'; " +
"end"
);

stmt.execute(
"create procedure get_xml_components(IN songId INT) " +
"begin " +
"select component_type, component_name, string_value " +
"from sbs_component " +
"where song_id =  songId " +
"order by component_type, component_name; " +
"end"
);

stmt.execute(
"create procedure get_xml_pattern_components(IN songId INT) " +
"begin " +
"select p.component_name, pc.component_position, c.component_type, c.component_name, pc.anonymous_string " +
"from (sbs_pattern_component pc join sbs_component p on pc.pattern_id = p.component_id) " +
"     left join sbs_component c on pc.component_id = c.component_id " +
"where p.song_id = songId " +
"order by p.component_name, pc.component_position; " +
"end"
);

stmt.execute(
"create procedure get_component_values(IN patternId INT) " +
"begin " +
"select p.component_id, " +
"       c.component_type, " +
"       c.component_name, " +
"       c.string_value, " +
"       p.anonymous_string " +
"from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
"where p.pattern_id = patternId " +
"order by p.component_position; " +
"end"
);

stmt.execute(
"create procedure get_cloned_components(IN patternId INT) " +
"begin " +
"select p.component_position, " +
"       p.component_id, " +
"       p.anonymous_string " +
"from sbs_pattern_component p " +
"where p.pattern_id = patternId; " +
"end"
);

stmt.execute(
"create procedure update_pattern_component(IN componentPosition INT, IN componentID INT, IN stringValue VARCHAR(4000), IN patternComponentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c, sbs_pattern_component p " +
"   where p.pattern_component_id = patternComponentID " +
"   and   p.pattern_id = c.component_id " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_pattern_component " +
"set component_position = componentPosition, " +
"    component_id = componentID, " +
"    anonymous_string = stringValue " +
"where pattern_component_id = patternComponentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure check_user(IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"  select count(*) from sbs_user " +
"  where user_name = userName " +
"  and   password = passwordString; " +
"end"
);

stmt.execute(
"create procedure delete_components(IN songID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"delete from sbs_component " +
"where song_id = songID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_components_from_pattern(IN patternID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = patternID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
" delete from sbs_pattern_component " +
" where pattern_id = patternID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_pattern_components(IN songID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"delete from sbs_pattern_component  " +
"where pattern_id in (select component_id  " +
"                     from sbs_component  " +
"                     where song_id = songID ); " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_selected_component(IN componentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = componentID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"   delete from sbs_component " +
"   where component_id = componentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_selected_pattern_component(IN patternComponentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c, sbs_pattern_component p " +
"   where p.pattern_component_id = patternComponentID " +
"   and   p.pattern_id = c.component_id " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"delete from sbs_pattern_component " +
"where pattern_component_id = patternComponentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_share(IN shareID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_share sh " +
"   where sh.share_id = shareID " +
"   and   s.song_id = sh.song_id " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"   delete from sbs_share  " +
"   where share_id = shareID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure delete_song(IN songID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"delete from sbs_song " +
"where song_id = songID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure get_shares(IN songId INT) " +
"begin " +
"  select s.share_id, " +
"         s.shared_with_id, " +
"         u.user_name " +
"  from sbs_share s, sbs_user u " +
"  where s.song_id = songId " +
"  and   s.shared_with_id = u.user_id " +
"  order by u.user_name; " +
"end"
);

stmt.execute(
"create procedure get_users() " +
"begin " +
"  select u.user_id, " +
"         u.user_name " +
"  from sbs_user u " +
"  order by u.user_name; " +
"end"
);

stmt.execute(
"create procedure insert_component(IN songID INT, IN componentType VARCHAR(10), IN componentName VARCHAR(100), IN stringValue VARCHAR(4000), " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"declare lastInsertId INT; " +
"SET userID = -1; " +
"SET lastInsertId = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = songID))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"insert into sbs_component " +
"   (song_id, component_type, component_name, string_value) " +
"values " +
"   (songID, componentType, componentName, stringValue); " +
"select LAST_INSERT_ID() into lastInsertId; " +
"end if; " +
"select lastInsertId; " +
"end"
);

stmt.execute(
"create procedure insert_pattern_component(IN patternID INT, IN componentPosition INT, IN componentID INT, IN anonymousString VARCHAR(4000), " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"declare lastInsertId INT; " +
"SET userID = -1; " +
"SET lastInsertId = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = patternID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"insert into sbs_pattern_component " +
"   (pattern_id, component_position, component_id, anonymous_string) " +
"values " +
"   (patternID, componentPosition, componentID, anonymousString); " +
"select LAST_INSERT_ID() into lastInsertId; " +
"end if; " +
"select lastInsertId; " +
"end"
);

stmt.execute(
"create procedure insert_share(IN songID INT, IN sharedWithId INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"declare lastInsertId INT; " +
"SET userID = -1; " +
"SET lastInsertId = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"   insert into sbs_share " +
"     (song_id, shared_with_id) " +
"   values (songID, sharedWithId); " +
"select LAST_INSERT_ID() into lastInsertId; " +
"end if; " +
"select lastInsertId; " +
"end"
);

stmt.execute(
"create procedure insert_song(IN songName VARCHAR(200), IN numericDurationType VARCHAR(10), IN soundbankId INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"declare lastInsertId INT; " +
"SET userID = -1; " +
"SET lastInsertId = -1; " +
"select user_id into userID " +
"from sbs_user " +
"where user_name = userName " +
"and   password = passwordString; " +
"if userID != -1 THEN " +
"insert into sbs_song " +
"   (song_name, numeric_duration_type, soundbank_id, owner_id) " +
"values " +
"   (songName, numericDurationType, soundbankId, userID); " +
"select LAST_INSERT_ID() into lastInsertId; " +
"end if; " +
"select lastInsertId; " +
"end"
);

stmt.execute(
"create procedure insert_user(IN userName VARCHAR(100), IN passwordString VARCHAR(100), IN eMail VARCHAR(100)) " +
"begin " +
"  insert into sbs_user " +
"  (user_name, password, e_mail) " +
"  values " +
"  (userName, passwordString, eMail); " +
"end"
);

stmt.execute(
"create procedure update_anonymous_string(IN stringValue VARCHAR(4000), IN patternComponentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c, sbs_pattern_component p " +
"   where p.pattern_component_id = patternComponentID " +
"   and   p.pattern_id = c.component_id " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_pattern_component " +
"set anonymous_string = stringValue " +
"where pattern_component_id = patternComponentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_component(IN componentName VARCHAR(100), IN stringValue VARCHAR(4000), IN componentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = componentID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_component " +
"set component_name = componentName, " +
"    string_value = stringValue " +
"where component_id = componentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_component_name(IN componentName VARCHAR(100), IN componentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = componentID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_component " +
"set component_name = componentName " +
"where component_id = componentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_numeric_duration_type(IN numericDurationType VARCHAR(10), IN songID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_song " +
"set " +
"numeric_duration_type = numericDurationType " +
"where song_id = songID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_share(IN sharedWithId INT, IN shareID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_share sh " +
"   where sh.share_id = shareID " +
"   and   s.song_id = sh.song_id " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"   update sbs_share  " +
"   set shared_with_id = sharedWithId " +
"   where share_id = shareID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_song(IN songName VARCHAR(200), IN numericDurationType VARCHAR(10), IN soundbankId INT, IN songID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s " +
"   where s.song_id = songID " +
"   and   s.owner_id = u.user_id " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_song " +
"set song_name = songName, " +
"    numeric_duration_type = numericDurationType, " +
"    soundbank_id = soundbankId " +
"where song_id = songID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure update_string(IN stringValue VARCHAR(4000), IN componentID INT, " +
"  IN userName VARCHAR(100), IN passwordString VARCHAR(100)) " +
"begin " +
"declare userID INT; " +
"SET userID = -1; " +
"   select u.user_id into userID " +
"   from sbs_user u, sbs_song s, sbs_component c " +
"   where c.component_id = componentID " +
"   and   s.song_id = c.song_id " +
"   and   (s.owner_id = u.user_id or (u.user_id in(select shared_with_id from sbs_share sh where sh.song_id = c.song_id))) " +
"   and   u.user_name = userName " +
"   and   u.password = passwordString; " +
"if userID != -1 THEN " +
"update sbs_component " +
"set string_value = stringValue " +
"where component_id = componentID; " +
"end if; " +
"select userID; " +
"end"
);

stmt.execute(
"create procedure get_owner_name(IN songID INT) " +
"begin " +
"declare userName VARCHAR(100); " +
"SET userName = NULL; " +
   "select u.user_name into userName " +
   "from sbs_user u, sbs_song s " +
   "where s.song_id = songID " +
   "and   s.owner_id = u.user_id; " +
"select userName; " +
"end"
);

            stmt.close();

                        Messages.plainMessage(frame, title, "Database tables created.");
                     }

                     else if (db_type.equals("Postgres"))
                     {
                        Class.forName("org.postgresql.Driver");
                        Connection conn1 = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database,
                                                                      user, passwordString);
                        conn1.setAutoCommit(true);

            stmt = conn1.createStatement();
            stmt.execute("create table sbs_soundbank " +
                         "(" +
                         "soundbank_id	  integer," +
                         "soundbank_name varchar(100) not null," +
                         "soundbank		  bytea," +
                         "primary key (soundbank_id)," +
                         "unique (soundbank_name), check(length(soundbank_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_soundbank");
            stmt.execute("create table sbs_song " +
                         "(" +
                         "song_id			      integer," +
                         "song_name		         varchar(200) not null," +
                         "numeric_duration_type	varchar(10)," +
                         "soundbank_id          integer," +
                         "primary key (song_id)," +
                         "foreign key (soundbank_id) references sbs_soundbank," +
                         "unique (song_name), check(length(song_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_song");
            stmt.execute("create table sbs_component " +
                         "(" +
                         "component_id		integer," +
                         "song_id			integer," +
                         "component_type		varchar(10)," +
                         "component_name		varchar(100)	not null," +
                         "string_value		varchar(4000)," +
                         "primary key (component_id)," +
                         "foreign key (song_id) references sbs_song," +
                         "unique (song_id, component_type, component_name)," +
                         "check(length(component_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_component");
            stmt.execute("create table sbs_pattern_component " +
                         "(" +
                         "pattern_component_id	integer," +
                         "pattern_id		integer," +
                         "component_position	integer		not null," +
                         "component_id		integer," +
                         "anonymous_string varchar(4000)," +
                         "primary key (pattern_component_id)," +
                         "foreign key (pattern_id) references sbs_component," +
                         "foreign key (component_id) references sbs_component," +
                         "unique (pattern_id, component_position)," +
                         "check(pattern_id != component_id)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_pattern_component");
            stmt.execute("create table sbs_connection " +
                         "(" +
                         "connection_id    integer," +
                         "connection_name  varchar(200) not null," +
                         "db_type          varchar(20)," +
                         "host_name        varchar(400)," +
                         "port             varchar(8)," +
                         "db_name         varchar(100)," +
                         "user_name        varchar(100)," +
                         "password         varchar(100)," +
                         "primary key (connection_id)," +
                         "unique (connection_name), check(length(connection_name)>0)" +
                         ")"
                        );
            stmt.execute("create sequence sbs_seq_connection");
            stmt.close();

                        Messages.plainMessage(frame, title, "Database tables created.");
                     }
                     else
                     {
                        Messages.plainMessage(frame, title, "Invalid connection type.");
					      }
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
            }
            else
            {
               Messages.plainMessage(frame, title, "Null record selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }
}
