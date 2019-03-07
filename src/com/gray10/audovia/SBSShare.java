/*
 * SBSShare.java - Manage shared songs.
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
package com.gray10.audovia;

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

public class SBSShare extends JDialog
{
	/*
	 * version 3.0.3
	 *
	 */

   private static final long serialVersionUID = 1L;

   private JDialog frame = SBSShare.this;

   private JTable tableField;

   private Connection conn;
   private String connection_name;
   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private CallableStatement cSupdateStmt;
   private CallableStatement cSinsertStmt;
   private CallableStatement cSdeleteStmt;

   private int selectedRow;
   private int selectedCol;
   private String status;
   private Integer song_id;
   private String  song_name;
   private String  owner_name;
   private Integer share_id;
   private Integer shared_with_id;
   private String  shared_with_name;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;

   private SBSShareTableModel tableModel;
   private String title = "SBSShare";

   private JComboBox<String> shared_withComboBox;

   public SBSShare(
                               Connection aConnection, String aConnectionName,
                               Integer aSongId,
                               String  aSongName,
                               String  aOwnerName,
                               String aSessionUser, String aSessionPassword) throws Exception
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

      setSize(600,436);
      setLocation(100,100);
      //setSize(700,466);
      setModal(true);
      //setLocation(100,100);
      //setLocation(125,125);
      if (aSessionUser == null)
      {
			setTitle(title + " (" + aConnectionName + ")");
		}
		else
		{
         setTitle(title + " (" + aConnectionName + ") " + aSessionUser);
		}

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      conn = aConnection;
      connection_name = aConnectionName;
      song_id = aSongId;
      song_name = aSongName;
      owner_name = aOwnerName;
      session_user = aSessionUser;
      session_password = aSessionPassword;

      updateStmt = conn.prepareStatement
                   ("update sbs_share " +
                    "set shared_with_id = ? " +
                    "where share_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) // will only be accessed from MySQL
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_share " +
                       "(song_id, shared_with_id) " +
                       "values (?, ?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
		}

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_share " +
                    "where share_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSupdateStmt = conn.prepareCall("{call update_share(?, ?, ?, ?)}");
         cSinsertStmt = conn.prepareCall("{call insert_share(?, ?, ?, ?)}");
         cSdeleteStmt = conn.prepareCall("{call delete_share(?, ?, ?)}");
		}

      tableModel = new SBSShareTableModel(conn, song_id);
      tableModel.query();
      conn.commit();

      shared_withComboBox = tableModel.getShared_withComboBox();

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BorderLayout());
      topPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));

      JPanel newPanel = new JPanel();
      newPanel.setLayout(new BorderLayout());
      newPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
      JPanel newPanelN = new JPanel();
      newPanelN.setLayout(new BorderLayout());
      newPanelN.setBorder(BorderFactory.createEmptyBorder(5,5,0,0));
      JPanel newPanelS = new JPanel();
      newPanelS.setLayout(new BorderLayout());
      newPanelS.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));
      newPanel.add(newPanelN, BorderLayout.NORTH);
      newPanel.add(newPanelS, BorderLayout.SOUTH);

      Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      JLabel songField = new JLabel("Song: ");
      songField.setFont(labelFont);
      Dimension dim1 = songField.getPreferredSize();
	   dim1.width = 65;
      songField.setPreferredSize(dim1);
      newPanelN.add(songField, BorderLayout.WEST);

      JLabel songNameField = new JLabel(song_name);
      songNameField.setFont(titleFont);
      newPanelN.add(songNameField, BorderLayout.CENTER);

      if (owner_name != null)
      {
         JLabel ownerField = new JLabel("Owner: ");
         ownerField.setFont(labelFont);
         Dimension dim2 = ownerField.getPreferredSize();
	      dim2.width = 65;
         ownerField.setPreferredSize(dim2);
         newPanelS.add(ownerField, BorderLayout.WEST);

         JLabel ownerNameField = new JLabel(owner_name);
         ownerNameField.setFont(titleFont);
         newPanelS.add(ownerNameField, BorderLayout.CENTER);
   	}

      topPanel.add(newPanel, BorderLayout.NORTH);

      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      JLabel headerField = new JLabel("Shared with");
      headerField.setFont(boldItalic);
      headerField.setForeground(new Color(85, 26, 139));
      headerPanel.add(headerField);
      topPanel.add(headerPanel, BorderLayout.SOUTH);

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
      TableColumn column3 = columnModel.getColumn(3);

      tableField.removeColumn(column0);
      tableField.removeColumn(column1);
      tableField.removeColumn(column2);

      //column3.setPreferredWidth(400);

      column3.setCellRenderer(new TextFieldRenderer());

      column3.setCellEditor(new DefaultCellEditor(shared_withComboBox));

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton insertButton     = new JButton("Insert");
      JButton deleteButton     = new JButton("Delete");
      JButton saveButton       = new JButton("Save");
      JButton quitButton       = new JButton("Quit");

      buttonPanel.add(insertButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InsertAction insertAction         = new InsertAction();
      DeleteAction deleteAction         = new DeleteAction();
      SaveAction saveAction             = new SaveAction();
      QuitAction quitAction             = new QuitAction();

      insertButton.addActionListener(insertAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
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
               status   = (String)tableModel.getValueAt(selectedRow, 0);
               share_id = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     if (session_user == null)
                     {
                        deleteStmt.setInt(1,share_id.intValue());
                        deleteStmt.execute();
                        conn.commit();
							}
							else
							{
                        cSdeleteStmt.setInt(1,share_id.intValue());
                        cSdeleteStmt.setString(2, session_user);
                        cSdeleteStmt.setString(3, session_password);
                        cSdeleteStmt.execute();

                        rset = cSdeleteStmt.getResultSet();

							   while (rset.next()) returned_user_id = Integer.valueOf(rset.getInt(1));

								if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only share your own songs.");
								}

                        rset.close();

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
               selectedRow          = i;
               status               = (String)tableModel.getValueAt(i,0);
               share_id             = (Integer)tableModel.getValueAt(i,1);
               shared_with_id       = (Integer)tableModel.getValueAt(i,2);

               //System.out.println(shared_with_id);

               if (shared_with_id != null) save();
            }
            conn.commit();
            for (i=0; i<tableModel.getRowCount(); i++)
            {
               status         = (String)tableModel.getValueAt(i,0);
               shared_with_id = (Integer)tableModel.getValueAt(i,2);

               if ((status.equals("changed") || status.equals("inserted")) && (shared_with_id != null))
               {
                  tableModel.setValueAt("unchanged", i, 0);
               }
            }
            Messages.plainMessage(frame, title, "Data saved.");
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
            tableField.changeSelection(selectedRow, 0, false, false);
            Messages.exceptionHandler(frame, title, e);
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
            status         = (String)tableModel.getValueAt(i,0);
            shared_with_id = (Integer)tableModel.getValueAt(i,2);

            if ((status.equals("changed") || status.equals("inserted")) &&  (shared_with_id != null))
            {
               updated = 1;
               break;
            }
         }

         if (updated == 1)
         {
            selection = Messages.warningQuestion(frame, title, "OK to Quit? - Data not saved.");
         }

         if (selection == 0)
         {
            try
            {
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
         if (session_user == null)
         {
            updateStmt.setInt(1, shared_with_id.intValue());
            updateStmt.setInt(2, share_id.intValue());

            updateStmt.execute();
			}
			else
			{
            cSupdateStmt.setInt(1, shared_with_id.intValue());
            cSupdateStmt.setInt(2, share_id.intValue());
            cSupdateStmt.setString(3, session_user);
            cSupdateStmt.setString(4, session_password);
            cSupdateStmt.execute();

				rset = cSupdateStmt.getResultSet();

		      while (rset.next()) returned_user_id = Integer.valueOf(rset.getInt(1));

				if (returned_user_id.intValue() == -1)
				{
				   rset.close();
					throw new Exception("You can only share your own songs.");
				}

            rset.close();
			}
      }
      if (status.equals("inserted"))
      {
			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertStmt.setInt(1, song_id.intValue());
               insertStmt.setInt(2, shared_with_id.intValue());
               insertStmt.execute();

               rset = insertStmt.getGeneratedKeys();
               while (rset.next()) share_id = Integer.valueOf(rset.getInt(1));
               rset.close();

               tableModel.setValueAt(share_id, selectedRow, 1);
				}
				else
				{
               cSinsertStmt.setInt(1, song_id.intValue());
               cSinsertStmt.setInt(2, shared_with_id.intValue());
               cSinsertStmt.setString(3, session_user);
               cSinsertStmt.setString(4, session_password);

               cSinsertStmt.execute();

				   rset = cSinsertStmt.getResultSet();

					while (rset.next()) share_id = Integer.valueOf(rset.getInt(1));

				   if (share_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("You can only share your own songs.");
					}

               rset.close();

               tableModel.setValueAt(share_id, selectedRow, 1);
				}
			}
			else
         {
		   }
      }
   }
}
