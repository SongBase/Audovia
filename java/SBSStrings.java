/*
 * SBSStrings.java - Manage Strings
 * Copyright (C) 2010, 2011, 2012, 2014, 2015, 2016  Donald G Gray
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

import javax.swing.plaf.basic.*;

public class SBSStrings extends JFrame
{
	/*
	 * version 3.1.2
	 *
	 */

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSStrings.this;
   private Class c = Class.forName("SBSStrings");
   private Field keyField = c.getField("keychar");
   public  char keychar;

   private JTable tableField;

   private Connection conn;
   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private PreparedStatement sequenceStmt;
   private PreparedStatement getConstants;
   private CallableStatement cSgetConstants;
   private CallableStatement cSupdateStmt;
   private CallableStatement cSinsertStmt;
   private CallableStatement cSdeleteStmt;

   private int selectedRow;
   private int selectedCol;
   private String status;
   private Integer song_id;
   private String  song_name;
   private Integer string_id;
   private String  string_name;
   private String  string_value;
   private String  constants;
   private String  parse_value;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;

   private SBSStringsTableModel tableModel;
   private String title = "SBSStrings";

   public SBSStrings(JFrame aParentFrame,
                     Field aSiblingCountField,
                     Connection aConnection, String aConnectionName,
                     Integer aSongId,
                     String aSongName,
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

      setSize(600,458);
      setLocation(100,100);
      if (aSessionUser == null)
      {
			setTitle(title + " (" + aConnectionName + ")");
		}
		else
		{
         setTitle(title + " (" + aConnectionName + ") " + aSessionUser);
		}

      ImageIcon icon = new ImageIcon("SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      parentFrame = aParentFrame;
      siblingCountField = aSiblingCountField;
      conn = aConnection;
      song_id = aSongId;
      song_name = aSongName;
      session_user = aSessionUser;
      session_password = aSessionPassword;

      updateStmt = conn.prepareStatement
                   ("update sbs_component " +
                    "set component_name = ?, " +
                    "    string_value = ? " +
                    "where component_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_component " +
                       "(song_id, component_type, component_name, string_value) " +
                       "values (?,'string',?,?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
      insertStmt = conn.prepareStatement
                   ("insert into sbs_component " +
                    "(component_id, song_id, component_type, component_name, string_value) " +
                    "values (?,?,'string',?,?)");
		}

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_component " +
                    "where component_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
        sequenceStmt = conn.prepareStatement
                       ("select nextval('sbs_seq_component')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select sbs_seq_component.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select next value for sbs_seq_component from (values 1) v");
		}

      getConstants = conn.prepareStatement
                     ("select string_value " +
                      "from sbs_component " +
                      "where song_id = ? " +
                      "and   component_type = 'string' " +
                      "and   string_value like '%$%'");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSgetConstants = conn.prepareCall("{call get_constants(?)}");
         cSupdateStmt = conn.prepareCall("{call update_component(?, ?, ?, ?, ?)}");
         cSinsertStmt = conn.prepareCall("{call insert_component(?, ?, ?, ?, ?, ?)}");
         cSdeleteStmt = conn.prepareCall("{call delete_selected_component(?, ?, ?)}");
		}

      tableModel = new SBSStringsTableModel(conn, song_id);
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

      JMenuItem importItem    = new JMenuItem("Import Strings");

      fileMenu.add(importItem);

      ImportAction importAction = new ImportAction();
      importItem.addActionListener(importAction);

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

      JLabel songNameField = new JLabel(song_name);
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

      TextFieldEditor stringFieldEditor = new TextFieldEditor();
      stringFieldEditor.setFilter("[\\w !£$%^&\\*()\\-+={\\[}\\]:;@'~#\\\\<,>\\.?/]*");
      column2.setCellEditor(stringFieldEditor);

      column3.setCellRenderer(new TextAreaRenderer());

      TextAreaEditor valueAreaEditor = new TextAreaEditor(frame, keyField);
      //valueAreaEditor.setFilter("[\\w !£$%^&\\*()\\-+={\\[}\\]:;@'~#\\\\<,>\\.?/]*");
      column3.setCellEditor(valueAreaEditor);

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
      JButton popupButton  = new JButton("Editor");
      JButton quitButton   = new JButton("Quit");

      buttonPanel.add(insertButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(popupButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InsertAction insertAction = new InsertAction();
      DeleteAction deleteAction = new DeleteAction();
      SaveAction saveAction     = new SaveAction();
      PopupAction popupAction   = new PopupAction();
      QuitAction quitAction     = new QuitAction();

      insertButton.addActionListener(insertAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
      popupButton.addActionListener(popupAction);
      quitButton.addActionListener(quitAction);

      KeyAction keyAction = new KeyAction();
      tableField.addKeyListener(keyAction);
   }

   private class KeyAction implements KeyListener
   {
      public void keyPressed(KeyEvent e)
      {
         keychar = e.getKeyChar();
      }
      public void keyReleased(KeyEvent e)
      {
      }
      public void keyTyped(KeyEvent e)
      {
      }
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
               status    = (String)tableModel.getValueAt(selectedRow, 0);
               string_id = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     if (session_user == null)
                     {
                        deleteStmt.setInt(1,string_id.intValue());
                        deleteStmt.execute();
                        conn.commit();
							}
							else
							{
                        cSdeleteStmt.setInt(1,string_id.intValue());
                        cSdeleteStmt.setString(2, session_user);
                        cSdeleteStmt.setString(3, session_password);
                        cSdeleteStmt.execute();

                        rset = cSdeleteStmt.getResultSet();

							   while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

								if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only update your own or shared songs.");
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
               selectedRow  = i;
               status       = (String)tableModel.getValueAt(i,0);
               string_id    = (Integer)tableModel.getValueAt(i,1);
               string_name  = (String)tableModel.getValueAt(i,2);
               string_value = (String)tableModel.getValueAt(i,3);

               if (status.equals("changed") || status.equals("inserted"))
               {
						parse_value = "" + string_value;
						if (parse_value.indexOf("[") > -1 || parse_value.indexOf("{") > -1)
						{
                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
			               cSgetConstants.setInt(1, song_id.intValue());
			               cSgetConstants.execute();
			               rset = cSgetConstants.getResultSet();
		               }
		               else
		               {
						   	getConstants.setInt(1,song_id.intValue());
							   rset = getConstants.executeQuery();
							}
							while (rset.next())
							{
								constants = rset.getString(1);
								parse_value = constants + " " + parse_value;
						   }
                     rset.close();
						}
						Pattern pattern = new Pattern(parse_value);
						MusicStringParser parser = new MusicStringParser();
                  parser.parse(pattern);
					}

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

   private class PopupAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);

            string_name  = (String)tableModel.getValueAt(selectedRow,2);
            string_value = (String)tableModel.getValueAt(selectedRow,3);

            try
            {
               SBSPopupEditor editor = new SBSPopupEditor(conn, song_id, song_name, string_name, string_value, "OK", "Cancel");
               editor.setVisible(true);
               int selection1 = editor.getSelection();
               if (selection1 == 0)
               {
                  string_value = editor.getStringValue();
                  tableModel.setValueAt(string_value, selectedRow, 3);
					}
					editor.dispose();
					tableField.requestFocusInWindow();
			   }
			   catch (Exception e)
			   {
					Messages.exceptionHandler(frame, title, e);
				}
         }
         else
         {
            Messages.plainMessage(frame, title, "No selection made.");
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
		   if (string_name != null) string_name = string_name.trim();

         if (session_user == null)
         {
            updateStmt.setString(1, string_name);
            updateStmt.setString(2, string_value);
            updateStmt.setInt(3, string_id.intValue());
            updateStmt.execute();
			}
			else
			{
            cSupdateStmt.setString(1, string_name);
            cSupdateStmt.setString(2, string_value);
            cSupdateStmt.setInt(3, string_id.intValue());
            cSupdateStmt.setString(4, session_user);
            cSupdateStmt.setString(5, session_password);
            cSupdateStmt.execute();

				rset = cSupdateStmt.getResultSet();

		      while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

				if (returned_user_id.intValue() == -1)
				{
				   rset.close();
					throw new Exception("You can only update your own or shared songs.");
				}

            rset.close();
			}
      }
      if (status.equals("inserted"))
      {
		   if (string_name != null) string_name = string_name.trim();

			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertStmt.setInt(1,song_id.intValue());
               insertStmt.setString(2,string_name);
               insertStmt.setString(3,string_value);
               insertStmt.execute();

               rset = insertStmt.getGeneratedKeys();
               while (rset.next()) string_id = new Integer(rset.getInt(1));
               rset.close();

               tableModel.setValueAt(string_id, selectedRow, 1);
				}
				else
				{
               cSinsertStmt.setInt(1,song_id.intValue());
               cSinsertStmt.setString(2, "string");
               cSinsertStmt.setString(3, string_name);
               cSinsertStmt.setString(4, string_value);
               cSinsertStmt.setString(5, session_user);
               cSinsertStmt.setString(6, session_password);

               cSinsertStmt.execute();

				   rset = cSinsertStmt.getResultSet();

					while (rset.next()) string_id = new Integer(rset.getInt(1));

				   if (string_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("You can only update your own or shared songs.");
					}

               rset.close();

               tableModel.setValueAt(string_id, selectedRow, 1);
				}
			}
			else
         {
         rset = sequenceStmt.executeQuery();
         while (rset.next()) string_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(string_id, selectedRow, 1);

         insertStmt.setInt(1,string_id.intValue());
         insertStmt.setInt(2,song_id.intValue());
         insertStmt.setString(3,string_name);
         insertStmt.setString(4,string_value);
         insertStmt.execute();
		   }
      }
   }

   private class ImportAction implements ActionListener
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

         int i;
         selectedRow = 0;
         try
         {
            for (i=0; i<tableModel.getRowCount(); i++)
            {
               selectedRow  = i;
               status       = (String)tableModel.getValueAt(i,0);
               string_id    = (Integer)tableModel.getValueAt(i,1);
               string_name  = (String)tableModel.getValueAt(i,2);
               string_value = (String)tableModel.getValueAt(i,3);

               if (status.equals("changed") || status.equals("inserted"))
               {
						parse_value = "" + string_value;
						if (parse_value.indexOf("[") > -1 || parse_value.indexOf("{") > -1)
						{
                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
			               cSgetConstants.setInt(1, song_id.intValue());
			               cSgetConstants.execute();
			               rset = cSgetConstants.getResultSet();
		               }
		               else
		               {
							   getConstants.setInt(1,song_id.intValue());
							   rset = getConstants.executeQuery();
							}
							while (rset.next())
							{
								constants = rset.getString(1);
								parse_value = constants + " " + parse_value;
						   }
                     rset.close();
						}
						Pattern pattern = new Pattern(parse_value);
						MusicStringParser parser = new MusicStringParser();
                  parser.parse(pattern);
					}

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
            Messages.plainMessage(frame, title, "Data saved.  Press OK to continue.");

            try
            {
               int location = tableModel.getRowCount() - 1;

               SBSSongPicker songPicker = new SBSSongPicker(conn, song_id, song_name, tableModel);
               songPicker.setVisible(true);

               tableField.changeSelection(location, 0, false, false);
               tableField.requestFocusInWindow();
				}
				catch (Exception e2)
				{
               try
               {
                  conn.rollback();
               }
               catch (Exception e3)
               {
                  Messages.exceptionHandler(frame, title, e3);
               }
               Messages.exceptionHandler(frame, title, e2);
				}
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
}
