/*
 * SBSPatternComponents.java - Manage Pattern Components
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

import org.jfugue.*;

public class SBSPatternComponents extends JFrame
{
	/*
	 * version 3.0.3
	 *
	 */

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSPatternComponents.this;
   private Class c = Class.forName("SBSPatternComponents");
   private Field childCountField = c.getField("childCount");
   public  int childCount = 0;

   private JTable tableField;

   private Connection conn;
   private String connection_name;
   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private PreparedStatement bulkDeleteStmt;
   private PreparedStatement sequenceStmt;
   private PreparedStatement drillStmt;
   private PreparedStatement updateString;
   private PreparedStatement getConstants;
   private CallableStatement cSdrillStmt;
   private CallableStatement cSgetConstants;
   private CallableStatement cSupdateStmt;
   private CallableStatement cSinsertStmt;
   private CallableStatement cSdeleteStmt;
   private CallableStatement cSbulkDeleteStmt;
   private CallableStatement cSupdateString;

   private int selectedRow;
   private int selectedCol;
   private String status;
   private Integer song_id;
   private String  song_name;
   private Integer pattern_id;
   private String  pattern_name;
   private Integer pattern_component_id;
   private Integer component_position;
   private Integer component_id;
   private String  component;
   private String  anonymous_string;
	private String	 drill_component_type;
	private String	 drill_component_name;
	private String  drill_string_value;
   private String  constants;
   private String  parse_value;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;

   private SBSPatternComponentsTableModel tableModel;
   private String title = "SBSPatternComponents";

   private JComboBox<String> componentComboBox;

   public SBSPatternComponents(JFrame aParentFrame,
                               Field aSiblingCountField,
                               Connection aConnection, String aConnectionName,
                               Integer aSongId,
                               String  aSongName,
                               Integer aPatternId,
                               String  aPatternName,
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

      setSize(700,466);
      //setLocation(100,100);
      setLocation(125,125);
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
      connection_name = aConnectionName;
      song_id = aSongId;
      song_name = aSongName;
      pattern_id = aPatternId;
      pattern_name = aPatternName;
      session_user = aSessionUser;
      session_password = aSessionPassword;

      updateStmt = conn.prepareStatement
                   ("update sbs_pattern_component " +
                    "set component_position = ?, " +
                    "    component_id = ?, " +
                    "    anonymous_string = ? " +
                    "where pattern_component_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_pattern_component " +
                       "(pattern_id, component_position, component_id, anonymous_string) " +
                       "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
      insertStmt = conn.prepareStatement
                   ("insert into sbs_pattern_component " +
                    "(pattern_component_id, pattern_id, component_position, component_id, anonymous_string) " +
                    "values (?,?,?,?,?)");
		}

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_pattern_component " +
                    "where pattern_component_id = ? ");

      bulkDeleteStmt = conn.prepareStatement
                       ("delete from sbs_pattern_component " +
                        "where pattern_id = " + pattern_id.toString());

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
        sequenceStmt = conn.prepareStatement
                       ("select nextval('sbs_seq_pattern_component')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select sbs_seq_pattern_component.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select next value for sbs_seq_pattern_component from (values 1) v");
		}

      drillStmt = conn.prepareStatement
                  ("select component_type, component_name, string_value " +
                   "from sbs_component " +
                   "where component_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSdrillStmt = conn.prepareCall("{call select_component(?)}");
		}

      updateString = conn.prepareStatement
                    ("update sbs_component " +
                     "set string_value = ? " +
                     "where component_id = ? ");

      getConstants = conn.prepareStatement
                     ("select string_value " +
                      "from sbs_component " +
                      "where song_id = ? " +
                      "and   component_type = 'string' " +
                      "and   string_value like '%$%'");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSgetConstants = conn.prepareCall("{call get_constants(?)}");
         cSupdateStmt = conn.prepareCall("{call update_pattern_component(?, ?, ?, ?, ?, ?)}");
         cSinsertStmt = conn.prepareCall("{call insert_pattern_component(?, ?, ?, ?, ?, ?)}");
         cSdeleteStmt = conn.prepareCall("{call delete_selected_pattern_component(?, ?, ?)}");
         cSbulkDeleteStmt = conn.prepareCall("{call delete_components_from_pattern(?, ?, ?)}");
         cSupdateString = conn.prepareCall("{call update_string(?, ?, ?, ?)}");
		}

      tableModel = new SBSPatternComponentsTableModel(conn, song_id, pattern_id);
      tableModel.query();
      conn.commit();

      componentComboBox = tableModel.getComponentComboBox();

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
	   dim1.width = 75;
      songField.setPreferredSize(dim1);
      newPanelN.add(songField, BorderLayout.WEST);

      JLabel songNameField = new JLabel(song_name);
      songNameField.setFont(titleFont);
      newPanelN.add(songNameField, BorderLayout.CENTER);

      JLabel patternField = new JLabel("Pattern: ");
      patternField.setFont(labelFont);
      Dimension dim2 = patternField.getPreferredSize();
	   dim2.width = 75;
      patternField.setPreferredSize(dim2);
      newPanelS.add(patternField, BorderLayout.WEST);

      JLabel patternNameField = new JLabel(pattern_name);
      patternNameField.setFont(titleFont);
      newPanelS.add(patternNameField, BorderLayout.CENTER);

      //JPanel titlePanel = new JPanel();
      //titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

      //Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      //Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);
      //Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      //JLabel labelField = new JLabel("Pattern: ");
      //labelField.setFont(labelFont);
      //titlePanel.add(labelField);

      //JLabel titleField = new JLabel(pattern_name);
      //titleField.setEditable(false);
      //titleField.setFont(titleFont);
      //Dimension dim1 = titleField.getPreferredSize();
		//dim1.width = 400;
      //titleField.setPreferredSize(dim1);
      //titlePanel.add(titleField);

      topPanel.add(newPanel, BorderLayout.NORTH);

      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      JLabel headerField = new JLabel("Pattern Components");
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
      TableColumn column4 = columnModel.getColumn(4);
      TableColumn column5 = columnModel.getColumn(5);

      tableField.removeColumn(column0);
      tableField.removeColumn(column1);
      tableField.removeColumn(column3);

      column4.setPreferredWidth(400);
      column5.setPreferredWidth(150);

      column2.setCellRenderer(new IntegerFieldRenderer());

      IntegerFieldEditor positionFieldEditor = new IntegerFieldEditor();
      positionFieldEditor.setFilter("[0-9]*");
      column2.setCellEditor(positionFieldEditor);

      column4.setCellRenderer(new TextFieldRenderer());

      column4.setCellEditor(new DefaultCellEditor(componentComboBox));

      column5.setCellRenderer(new StringFieldRenderer());

      column5.setCellEditor(new StringFieldEditor());

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton insertButton     = new JButton("Insert");
      JButton renumberButton   = new JButton("Renumber");
      JButton deleteButton     = new JButton("Delete");
      JButton saveButton       = new JButton("Save");
      JButton drillButton      = new JButton("Drill Down");
      JButton quitButton       = new JButton("Quit");

      buttonPanel.add(insertButton);
      buttonPanel.add(renumberButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(drillButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InsertAction insertAction         = new InsertAction();
      RenumberAction renumberAction     = new RenumberAction();
      DeleteAction deleteAction         = new DeleteAction();
      SaveAction saveAction             = new SaveAction();
      DrillAction drillAction           = new DrillAction();
      QuitAction quitAction             = new QuitAction();

      insertButton.addActionListener(insertAction);
      renumberButton.addActionListener(renumberAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
      drillButton.addActionListener(drillAction);
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

   private class RenumberAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
			int selection = 0;
			selection = Messages.plainQuestion(frame, title, "OK to Renumber?");
			if (selection == 0)
			{
            int i;
            selectedRow = 0;
            try
            {

               for (i=0; i<tableModel.getRowCount(); i++)
               {
                  selectedRow          = i;
                  status               = (String)tableModel.getValueAt(i,0);
                  pattern_component_id = (Integer)tableModel.getValueAt(i,1);
                  component_position   = (Integer)tableModel.getValueAt(i,2);
                  component_id         = (Integer)tableModel.getValueAt(i,3);
                  anonymous_string     = (String)tableModel.getValueAt(i,5);

               if (status.equals("changed") || status.equals("inserted"))
               {
						parse_value = "" + anonymous_string;
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

               }

               if (session_user == null)
               {
   					bulkDeleteStmt.execute();
					}
					else
					{
                        cSbulkDeleteStmt.setInt(1,pattern_id.intValue());
                        cSbulkDeleteStmt.setString(2, session_user);
                        cSbulkDeleteStmt.setString(3, session_password);
                        cSbulkDeleteStmt.execute();

                        rset = cSbulkDeleteStmt.getResultSet();

							   while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

								if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only update your own or shared songs.");
								}

                        rset.close();
					}

               for (i=0; i<tableModel.getRowCount(); i++)
               {
                  component_id = (Integer)tableModel.getValueAt(i,3);
                  anonymous_string = (String)tableModel.getValueAt(i,5);
                  if (component_id != null || anonymous_string != null)
                  {
							tableModel.setValueAt("inserted", i, 0);
							tableModel.setValueAt(i+1, i, 2);
						}
						else
						{
							status  = (String)tableModel.getValueAt(i,0);
							if (!status.equals("new"))
							{
							   tableModel.setValueAt(null, i, 2);
							   tableModel.setValueAt("new", i, 0);
                     }
						}
               }

               for (i=0; i<tableModel.getRowCount(); i++)
               {
                  selectedRow          = i;
                  status               = (String)tableModel.getValueAt(i,0);
                  pattern_component_id = (Integer)tableModel.getValueAt(i,1);
                  component_position   = (Integer)tableModel.getValueAt(i,2);
                  component_id         = (Integer)tableModel.getValueAt(i,3);
                  anonymous_string     = (String)tableModel.getValueAt(i,5);

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
               Messages.plainMessage(frame, title, "Components renumbered.");
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
               status               = (String)tableModel.getValueAt(selectedRow, 0);
               pattern_component_id = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     if (session_user == null)
                     {
                        deleteStmt.setInt(1,pattern_component_id.intValue());
                        deleteStmt.execute();
                        conn.commit();
							}
							else
							{
                        cSdeleteStmt.setInt(1,pattern_component_id.intValue());
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
               selectedRow          = i;
               status               = (String)tableModel.getValueAt(i,0);
               pattern_component_id = (Integer)tableModel.getValueAt(i,1);
               component_position   = (Integer)tableModel.getValueAt(i,2);
               component_id         = (Integer)tableModel.getValueAt(i,3);
               anonymous_string     = (String)tableModel.getValueAt(i,5);

               if (status.equals("changed") || status.equals("inserted"))
               {
						parse_value = "" + anonymous_string;
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

   private class DrillAction implements ActionListener
   {
		public void actionPerformed(ActionEvent a)
		{
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
               status               = (String)tableModel.getValueAt(i,0);
               pattern_component_id = (Integer)tableModel.getValueAt(i,1);
               component_position   = (Integer)tableModel.getValueAt(i,2);
               component_id         = (Integer)tableModel.getValueAt(i,3);

            if (! status.equals("new"))
            {
					if (component_id != null)
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
			               cSdrillStmt.setInt(1, component_id.intValue());
			               cSdrillStmt.execute();
			               rset = cSdrillStmt.getResultSet();
		               }
		               else
		               {
                        drillStmt.setInt(1,component_id.intValue());
                        rset = drillStmt.executeQuery();
						   }

                     while (rset.next())
                     {
			  	            drill_component_type = rset.getString(1);
				            drill_component_name = rset.getString(2);
				            drill_string_value   = rset.getString(3);
			            }
                     rset.close();

                     conn.commit();

                     if (drill_component_type.equals("pattern"))
                     {
                        SBSPatternComponents patternComponents = new SBSPatternComponents(frame, childCountField,
                                                                 conn, connection_name, song_id, song_name, component_id, drill_component_name,
                                                                 session_user, session_password);
                        patternComponents.setVisible(true);
                        childCount++;
					   	}
					   	else
					   	{
                        SBSPopupEditor editor = new SBSPopupEditor(conn, song_id, song_name, drill_component_name, drill_string_value, "Save", "Cancel");
                        editor.setVisible(true);
                        while (editor.getSelection() == 0)
                        {
									try
									{
                              drill_string_value = editor.getStringValue();

						            parse_value = "" + drill_string_value;
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
                                 conn.commit();
						            }
						            Pattern pattern = new Pattern(parse_value);
						            MusicStringParser parser = new MusicStringParser();
                              parser.parse(pattern);

                              break;
									}
									catch (Exception e2)
									{
										Messages.exceptionHandler(frame, "Popup Editor", e2);
										editor.setVisible(true);
									}
								}
								if (editor.getSelection() == 0)
								{
                           if (session_user == null)
                           {
                              updateString.setString(1, drill_string_value);
                              updateString.setInt(2, component_id.intValue());

                              updateString.execute();
                              conn.commit();
									}
									else
									{
                                 cSupdateString.setString(1, drill_string_value);
                                 cSupdateString.setInt(2, component_id.intValue());
                                 cSupdateString.setString(3, session_user);
                                 cSupdateString.setString(4, session_password);
                                 cSupdateString.execute();

	   		                  	rset = cSupdateString.getResultSet();

	   	                        while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

	   		                  	if (returned_user_id.intValue() == -1)
	   		                  	{
	   		                  	   rset.close();
	   		                  	   conn.rollback();
	   		                  		throw new Exception("You can only update your own or shared songs.");
	   		                  	}

                                 rset.close(); //need a commit
                                 conn.commit();
									}
				          	}
				         	editor.dispose();
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
						Messages.plainMessage(frame, title, "Null component selected.");
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
         if (session_user == null)
         {
            if (component_position != null)
               updateStmt.setInt(1, component_position.intValue());
            else
               updateStmt.setNull(1, Types.INTEGER);

            if (component_id != null)
               updateStmt.setInt(2, component_id.intValue());
            else
               updateStmt.setNull(2, Types.INTEGER);

            updateStmt.setString(3, anonymous_string);

            updateStmt.setInt(4, pattern_component_id.intValue());

            updateStmt.execute();
			}
			else
			{
            if (component_position != null)
               cSupdateStmt.setInt(1, component_position.intValue());
            else
               cSupdateStmt.setNull(1, Types.INTEGER);

            if (component_id != null)
               cSupdateStmt.setInt(2, component_id.intValue());
            else
               cSupdateStmt.setNull(2, Types.INTEGER);

            cSupdateStmt.setString(3, anonymous_string);

            cSupdateStmt.setInt(4, pattern_component_id.intValue());
            cSupdateStmt.setString(5, session_user);
            cSupdateStmt.setString(6, session_password);
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
			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertStmt.setInt(1,pattern_id.intValue());

               if (component_position != null)
                  insertStmt.setInt(2, component_position.intValue());
               else
                  insertStmt.setNull(2, Types.INTEGER);

               if (component_id != null)
                  insertStmt.setInt(3, component_id.intValue());
               else
                  insertStmt.setNull(3, Types.INTEGER);

               insertStmt.setString(4, anonymous_string);
               insertStmt.execute();

               rset = insertStmt.getGeneratedKeys();
               while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));
               rset.close();

               tableModel.setValueAt(pattern_component_id, selectedRow, 1);
				}
				else
				{
               cSinsertStmt.setInt(1,pattern_id.intValue());

               if (component_position != null)
                  cSinsertStmt.setInt(2, component_position.intValue());
               else
                  cSinsertStmt.setNull(2, Types.INTEGER);

               if (component_id != null)
                  cSinsertStmt.setInt(3, component_id.intValue());
               else
                  cSinsertStmt.setNull(3, Types.INTEGER);

               cSinsertStmt.setString(4, anonymous_string);
               cSinsertStmt.setString(5, session_user);
               cSinsertStmt.setString(6, session_password);

               cSinsertStmt.execute();

				   rset = cSinsertStmt.getResultSet();

					while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));

				   if (pattern_component_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("You can only update your own or shared songs.");
					}

               rset.close();

               tableModel.setValueAt(pattern_component_id, selectedRow, 1);
				}
			}
			else
         {
         rset = sequenceStmt.executeQuery();
         while (rset.next()) pattern_component_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(pattern_component_id, selectedRow, 1);

         insertStmt.setInt(1,pattern_component_id.intValue());
         insertStmt.setInt(2,pattern_id.intValue());

         if (component_position != null)
            insertStmt.setInt(3, component_position.intValue());
         else
            insertStmt.setNull(3, Types.INTEGER);

         if (component_id != null)
            insertStmt.setInt(4, component_id.intValue());
         else
            insertStmt.setNull(4, Types.INTEGER);

         insertStmt.setString(5, anonymous_string);

         insertStmt.execute();
		   }
      }
   }
}
