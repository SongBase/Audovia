/*
 * SBSSoundbanks.java - List of Soundbanks
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
import javax.sound.midi.*;
import javax.swing.filechooser.*;

import javax.swing.plaf.basic.*;

public class SBSSoundbanks extends JFrame
{
	/*
	 * version 3.0.12
	 *
	 */

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSSoundbanks.this;
   private Class c = Class.forName("SBSSoundbanks");
   private Field childCountField = c.getField("childCount");
   public  int childCount = 0;

   private JTable tableField;

   private Connection conn;
   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private PreparedStatement sequenceStmt;
   private PreparedStatement uploadStmt;
   private PreparedStatement viewStmt;
   private CallableStatement cSviewStmt;

   private InputStream inputStream = null;

   private int selectedRow;
   private int selectedCol;

   private String  status;
   private Integer soundbank_id;
   private String  soundbank_name;

   private String  access_type;

   private SBSSoundbanksTableModel tableModel;
   private String title = "SBSSoundbanks";

   private JDialog pleaseWait;

   public SBSSoundbanks(JFrame aParentFrame,
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
                   ("update sbs_soundbank " +
                    "set soundbank_name = ? " +
                    "where soundbank_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_soundbank " +
                       "(soundbank_name) " +
                       "values (?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
      insertStmt = conn.prepareStatement
                   ("insert into sbs_soundbank " +
                    "(soundbank_id, soundbank_name) " +
                    "values (?,?)");
		}

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_soundbank " +
                    "where soundbank_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select nextval('sbs_seq_soundbank')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select sbs_seq_soundbank.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select next value for sbs_seq_soundbank from (values 1) v");
		}

      uploadStmt = conn.prepareStatement
                   ("update sbs_soundbank " +
                    "set soundbank = ? " +
                    "where soundbank_id = ?");

      viewStmt = conn.prepareStatement
                   ("select soundbank from sbs_soundbank " +
                    "where soundbank_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSviewStmt = conn.prepareCall("{call select_soundbank(?)}");

      tableModel = new SBSSoundbanksTableModel(conn);
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

      JMenuItem uploadItem    = new JMenuItem("Upload soundbank");
      JMenuItem viewItem      = new JMenuItem("View instruments");

      fileMenu.add(uploadItem);
      fileMenu.add(viewItem);

      UploadAction uploadAction = new UploadAction();
      uploadItem.addActionListener(uploadAction);
      ViewAction viewAction = new ViewAction();
      viewItem.addActionListener(viewAction);

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      JLabel titleField = new JLabel("Soundbanks");
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

      TextFieldEditor soundbankFieldEditor = new TextFieldEditor();
      soundbankFieldEditor.setFilter("[\\w !£$%^&\\*()\\-+={\\[}\\]:;@'~#\\\\<,>\\.?/]*");

      column2.setCellRenderer(new TextFieldRenderer());
      column2.setCellEditor(soundbankFieldEditor);

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton insertButton   = new JButton("Insert");
      JButton deleteButton   = new JButton("Delete");
      JButton saveButton     = new JButton("Save");
      JButton playButton     = new JButton("Play Samples");
      JButton quitButton     = new JButton("Quit");

      buttonPanel.add(insertButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(playButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InsertAction insertAction     = new InsertAction();
      DeleteAction deleteAction     = new DeleteAction();
      SaveAction saveAction         = new SaveAction();
      PlayAction playAction         = new PlayAction();
      QuitAction quitAction         = new QuitAction();

      insertButton.addActionListener(insertAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
      playButton.addActionListener(playAction);
      quitButton.addActionListener(quitAction);

      pleaseWait = new JDialog(frame, "Please wait...", false);
      pleaseWait.setSize(200,0);
      pleaseWait.setLocation(280,165);
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
               status       = (String)tableModel.getValueAt(selectedRow, 0);
               soundbank_id = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     deleteStmt.setInt(1,soundbank_id.intValue());
                     deleteStmt.execute();
                     conn.commit();

                     if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
                     {
                        CallableStatement cs = conn.prepareCall
                                               ("CALL SYSCS_UTIL.SYSCS_COMPRESS_TABLE(?, ?, ?)");
                        cs.setString(1, "APP");
                        cs.setString(2, "SBS_SOUNDBANK");
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
               selectedRow    = i;
               status         = (String)tableModel.getValueAt(i,0);
               soundbank_id   = (Integer)tableModel.getValueAt(i,1);
               soundbank_name = (String)tableModel.getValueAt(i,2);

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

   private class PlayAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status          = (String)tableModel.getValueAt(i,0);
            soundbank_id    = (Integer)tableModel.getValueAt(i,1);
            soundbank_name  = (String)tableModel.getValueAt(i,2);

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

                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
 			               cSviewStmt.setInt(1, soundbank_id.intValue());
 			               cSviewStmt.execute();
 			               rset = cSviewStmt.getResultSet();
 		               }
 		               else
		               {
                        viewStmt.setInt(1,soundbank_id.intValue());
                        rset = viewStmt.executeQuery();
							}
                     rset.next();

                     if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL") ||
                         conn.getMetaData().getDatabaseProductName().equals("Oracle"))
                     {
                        inputStream = rset.getBinaryStream(1);
			   		   }
                     else
                     {
                        Blob soundbank_blob = rset.getBlob(1);

                        if (soundbank_blob != null)
                        {
                           byte[] bytes = soundbank_blob.getBytes(1L, (int)soundbank_blob.length());
                           inputStream = new ByteArrayInputStream(bytes);
							   }
							   else
							   {
									inputStream = null;
								}
			   			}

                     rset.close();

                     if (inputStream != null)
                     {
								Soundbank soundbank = MidiSystem.getSoundbank(inputStream);
                        inputStream.close();
                        conn.commit();

                        SBSPlaySamples playSamples = new SBSPlaySamples(conn, soundbank, soundbank_name);
                        playSamples.setVisible(true);
                     }
                     else
                     {
                        conn.commit();

                        Messages.warningMessage(frame, title, "Soundbank not loaded.");
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
			if (soundbank_name != null) soundbank_name = soundbank_name.trim();

         updateStmt.setString(1, soundbank_name);
         updateStmt.setInt(2, soundbank_id.intValue());
         updateStmt.execute();
      }
      if (status.equals("inserted"))
      {
			if (soundbank_name != null) soundbank_name = soundbank_name.trim();

			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
         insertStmt.setString(1,soundbank_name);
         insertStmt.execute();

         rset = insertStmt.getGeneratedKeys();
         while (rset.next()) soundbank_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(soundbank_id, selectedRow, 1);
			}
			else
         {
         rset = sequenceStmt.executeQuery();
         while (rset.next()) soundbank_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(soundbank_id, selectedRow, 1);

         insertStmt.setInt(1, soundbank_id.intValue());
         insertStmt.setString(2, soundbank_name);

         insertStmt.execute();
		   }
      }
   }

   private class UploadAction implements ActionListener
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
            status         = (String)tableModel.getValueAt(i,0);
            soundbank_id   = (Integer)tableModel.getValueAt(i,1);
            soundbank_name = (String)tableModel.getValueAt(i,2);

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
                     FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                     JFileChooser chooser = new JFileChooser("SF2", fsv);
                     chooser.setPreferredSize(new Dimension(600,300));
                     chooser.setDialogTitle("Upload Soundbank - " + soundbank_name);

                     FileNameExtensionFilter filter = new FileNameExtensionFilter("Soundbank files *.sf2 and *.dls", "sf2", "dls");
                     chooser.addChoosableFileFilter(filter);
                     chooser.setFileFilter(filter);

                     int result = chooser.showDialog(frame, "Upload soundbank");
                     if (result == JFileChooser.APPROVE_OPTION)
                     {
                        File file = chooser.getSelectedFile();

                        if (file.exists())
                        {
									pleaseWait.setVisible(true);

                           FileInputStream fileInputStream = new FileInputStream(file);
                           uploadStmt.setBinaryStream(1,fileInputStream,(int)file.length());
                           uploadStmt.setInt(2,soundbank_id.intValue());
                           uploadStmt.execute();
                           conn.commit();
                           fileInputStream.close();

                           pleaseWait.setVisible(false);

                           Messages.plainMessage(frame, title, "Soundbank: " + file.getPath() + " uploaded.");
                        }
                        else
                        {
                           Messages.warningMessage(frame, "Upload Soundbank", "Soundbank not found.");
                        }
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
                     pleaseWait.setVisible(false);

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

   private class ViewAction implements ActionListener
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
            status          = (String)tableModel.getValueAt(i,0);
            soundbank_id    = (Integer)tableModel.getValueAt(i,1);
            soundbank_name  = (String)tableModel.getValueAt(i,2);

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

                     InstrumentThread it = new InstrumentThread(conn, soundbank_id, soundbank_name);
                     it.start();
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
