/*
 * SBSSongs.java - List of Songs
 * Copyright (C) 2010 - 2017  Donald G Gray
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
import java.lang.*;
import java.lang.reflect.*;
import java.sql.*;
import javax.xml.parsers.*;
import javax.swing.border.*;
import java.net.*;
import javax.swing.filechooser.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.basic.*;

public class SBSSongs extends JFrame
{
	/*
	 * version 3.6.0
	 *
	 */

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSSongs.this;
   private Class c = Class.forName("SBSSongs");
   private Field childCountField = c.getField("childCount");
   public  int childCount = 0;

   private JTable tableField;
   private TableColumnModel columnModel;
   private TableColumn column5;

   private Connection conn;
   private String connection_name;
   private ResultSet rset;
   private PreparedStatement updateStmt;
   private PreparedStatement insertStmt;
   private PreparedStatement deleteStmt;
   private PreparedStatement deleteStmt_pattern_component;
   private PreparedStatement deleteStmt_component;
   private PreparedStatement sequenceStmt;
   private Statement stmt;
   private CallableStatement cStmt;
   private CallableStatement cSinsertUser;
   private CallableStatement cScheckUser;
   private CallableStatement cSinsertStmt;
   private CallableStatement cSupdateStmt;
   private CallableStatement cSdeleteStmt;
   private CallableStatement cSdeleteStmt_pattern_component;
   private CallableStatement cSdeleteStmt_component;
   private CallableStatement cSgetOwnerName;

   private int selectedRow;
   private int selectedCol;

   private String  status;
   private Integer song_id;
   private String  song_name;
   private String  owner_name;
   private String  numeric_duration_type;
   private Integer soundbank_id;
   private String  component_type;
   private String  component_name;
   private String  string_value;
   private String  pattern_name;
   private Integer component_position;
   private String  anonymous_string;

   private String  no_voices;
   private String  no_parts;
   private String  no_bars;
   private String  type_of_bar;

   private File templateFile;

   private String  access_type;

   private SBSSongsTableModel tableModel;
   private String title = "Audovia - SBSSongs";

   private JComboBox<String> soundbankComboBox;

   private boolean local;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;

   public SBSSongs(JFrame aParentFrame,
                   Field aSiblingCountField,
                   Connection aConnection, String aConnectionName,
                   boolean localFlag) throws Exception
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

      setSize(700,540); // was 530, 535
      setLocation(50,50);
      setTitle(title + " (" + aConnectionName + ")");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      Image iconImage = icon.getImage();
      ImageIcon scaledIcon = new ImageIcon(iconImage.getScaledInstance(52, 52, Image.SCALE_SMOOTH));

      parentFrame = aParentFrame;
      siblingCountField = aSiblingCountField;
      conn = aConnection;
      connection_name = aConnectionName;
      local = localFlag;

      updateStmt = conn.prepareStatement
                   ("update sbs_song " +
                    "set song_name = ?, " +
                    "    numeric_duration_type = ?, " +
                    "    soundbank_id = ? " +
                    "where song_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_song " +
                       "(song_name, numeric_duration_type, soundbank_id) " +
                       "values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
		}
      else
      {
      insertStmt = conn.prepareStatement
                   ("insert into sbs_song " +
                    "(song_id, song_name, numeric_duration_type, soundbank_id) " +
                    "values (?,?,?,?)");
	   }

      deleteStmt = conn.prepareStatement
                   ("delete from sbs_song " +
                    "where song_id = ? ");

      deleteStmt_pattern_component = conn.prepareStatement
                   ("delete from sbs_pattern_component " +
                    "where pattern_id in (select component_id " +
                    "                     from sbs_component " +
                    "                     where song_id = ? )");

      deleteStmt_component = conn.prepareStatement
                   ("delete from sbs_component " +
                    "where song_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select nextval('sbs_seq_song')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select sbs_seq_song.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequenceStmt = conn.prepareStatement
                        ("select next value for sbs_seq_song from (values 1) v");
		}

		if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
		{
		   cSinsertUser = conn.prepareCall("{call insert_user(?, ?, ?)}");
		   cScheckUser  = conn.prepareCall("{call check_user(?, ?)}");
		   cSinsertStmt  = conn.prepareCall("{call insert_song(?, ?, ?, ?, ?)}");
		   cSupdateStmt  = conn.prepareCall("{call update_song(?, ?, ?, ?, ?, ?)}");
		   cSdeleteStmt = conn.prepareCall("{call delete_song(?, ?, ?)}");
		   cSdeleteStmt_pattern_component = conn.prepareCall("{call delete_pattern_components(?, ?, ?)}");
		   cSdeleteStmt_component = conn.prepareCall("{call delete_components(?, ?, ?)}");
		   cSgetOwnerName = conn.prepareCall("{call get_owner_name(?)}");
		}

      tableModel = new SBSSongsTableModel(conn);
      tableModel.query("");
      conn.commit();

      soundbankComboBox = tableModel.getSoundbankComboBox();

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

      JMenuItem templateItem     = new JMenuItem("Template");
      JMenuItem xmlExportItem    = new JMenuItem("Song Export");
      JMenuItem xmlImportItem    = new JMenuItem("Song Import");
      JMenuItem quitItem     = new JMenuItem("Exit");

      fileMenu.add(templateItem);
      fileMenu.addSeparator();
      fileMenu.add(xmlExportItem);
      fileMenu.add(xmlImportItem);
      fileMenu.addSeparator();
      fileMenu.add(quitItem);

      JMenu soundbanksMenu = new JMenu("Soundbanks");
      menuBar.add(soundbanksMenu);

      JMenuItem soundbanksItem = new JMenuItem("Manage Soundbanks");

      soundbanksMenu.add(soundbanksItem);

      JMenu databaseMenu = new JMenu("Database");
      menuBar.add(databaseMenu);

      JMenuItem connectionsItem    = new JMenuItem("Database Connections");

      databaseMenu.add(connectionsItem);

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         JMenu userMenu = new JMenu("User");
         menuBar.add(userMenu);

         JMenuItem createItem    = new JMenuItem("Create Account");
         userMenu.add(createItem);

         JMenuItem loginItem    = new JMenuItem("Log in");
         userMenu.add(loginItem);

         JMenuItem shareItem    = new JMenuItem("Share Song");
         userMenu.add(shareItem);

         CreateAction createAction = new CreateAction();
         createItem.addActionListener(createAction);

         LoginAction loginAction = new LoginAction();
         loginItem.addActionListener(loginAction);

         ShareAction shareAction = new ShareAction();
         shareItem.addActionListener(shareAction);
	   }

      JMenu documentationMenu = new JMenu("Documentation");
      menuBar.add(documentationMenu);

      JMenuItem documentationItem = new JMenuItem("Audovia Documentation");
      documentationMenu.add(documentationItem);

      DocumentationAction documentationAction = new DocumentationAction();
      documentationItem.addActionListener(documentationAction);

      JMenu helpMenu = new JMenu("About");
      menuBar.add(helpMenu);

      JMenuItem aboutItem    = new JMenuItem("About Audovia");
      JMenuItem tutorialItem = new JMenuItem("Documentation");
      //JMenuItem faqItem      = new JMenuItem("FAQ");
      //JMenuItem checkItem    = new JMenuItem("Installation");
      JMenuItem contactItem  = new JMenuItem("Website");

      helpMenu.add(aboutItem);
      //helpMenu.add(tutorialItem);
      //helpMenu.add(faqItem);
      //helpMenu.add(checkItem);
      //helpMenu.add(contactItem);

      TemplateAction templateAction = new TemplateAction();
      templateItem.addActionListener(templateAction);

      XmlExportAction xmlExportAction = new XmlExportAction();
      xmlExportItem.addActionListener(xmlExportAction);

      XmlImportAction xmlImportAction = new XmlImportAction();
      xmlImportItem.addActionListener(xmlImportAction);

      QuitAction quitAction = new QuitAction();
      quitItem.addActionListener(quitAction);

      SoundbanksAction soundbanksAction = new SoundbanksAction();
      soundbanksItem.addActionListener(soundbanksAction);

      ConnectionsAction connectionsAction = new ConnectionsAction();
      connectionsItem.addActionListener(connectionsAction);

      AboutAction aboutAction = new AboutAction();
      aboutItem.addActionListener(aboutAction);

      TutorialAction tutorialAction = new TutorialAction();
      tutorialItem.addActionListener(tutorialAction);

      //FaqAction faqAction = new FaqAction();
      //faqItem.addActionListener(faqAction);

      //CheckAction checkAction = new CheckAction();
      //checkItem.addActionListener(checkAction);

      WebAction webAction = new WebAction();
      contactItem.addActionListener(webAction);

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel topPanel = new JPanel(); ///////
      topPanel.setLayout(new BorderLayout()); //////////
      //topPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));  ///////////
//      topPanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,80));

      topPanel.setBorder(BorderFactory.createEmptyBorder(15,20,10,20));

//      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 14); //////////
//      JPanel headerPanel = new JPanel(); ////////
//      headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); ////////
//      JLabel headerField = new JLabel("Songs"); ///////////
//      headerField.setFont(boldItalic); //////////////////
//      headerPanel.add(headerField); ////////////


//      JPanel middlePanel = new JPanel();
//      JLabel middleField = new JLabel("database application for making music using JFugue MusicStrings");
//      Font middleFont = new Font("Liberation Serif", Font.PLAIN+Font.ITALIC, 12);
//      middlePanel.add(middleField);
//      middleField.setFont(middleFont);
//      middleField.setForeground(new Color(85, 26, 139));


//      JPanel titlePanel = new JPanel();
//      JLabel titleField = new JLabel("Audovia");
//      JLabel blankField = new JLabel("     ");

//      JButton iconLabel     = new JButton(scaledIcon);
//      iconLabel.setBorder(new EmptyBorder(0,0,0,0));
//      iconLabel.setToolTipText("<html><p style=\"background-color:#e8ccff;padding:2px;\">Tree View</p></html>");

      //JLabel iconLabel = new JLabel(scaledIcon); ////////////////
//      JPanel iconPanel = new JPanel();
//      iconPanel.add(iconLabel);

//      JPanel centrePanel = new JPanel();
//      centrePanel.setLayout(new BorderLayout());

//      Font titleFont = new Font("Liberation Serif", Font.PLAIN, 24);
//      titleField.setFont(titleFont);
//      titleField.setForeground(new Color(85, 26, 139));

      //titlePanel.add(iconLabel);

//      titlePanel.add(titleField);
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));

      //topPanel.add(titlePanel, BorderLayout.NORTH); /////////
      //topPanel.add(middlePanel, BorderLayout.CENTER);

//      centrePanel.add(titlePanel, BorderLayout.NORTH); /////////
//      centrePanel.add(middlePanel, BorderLayout.SOUTH);

//      topPanel.add(iconPanel, BorderLayout.WEST);
//      topPanel.add(centrePanel, BorderLayout.CENTER);

      //topPanel.add(headerPanel, BorderLayout.SOUTH); /////////

      Font boldItalic = new Font("Liberation Sans", Font.BOLD+Font.ITALIC, 18);

      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      JLabel headerField = new JLabel("Songs");
      headerField.setFont(boldItalic);
      headerField.setForeground(new Color(85, 26, 139));
      //headerField.setForeground(new Color(102, 0, 153));
      headerPanel.add(headerField);
      topPanel.add(headerPanel, BorderLayout.SOUTH);

      contentPane.add(topPanel, BorderLayout.NORTH); ///////////

      //contentPane.add(titlePanel, BorderLayout.NORTH);

      tableField = new JTable(tableModel);
      tableField.setShowGrid(true);
      tableField.setGridColor(Color.gray);
      tableField.setSurrendersFocusOnKeystroke(true);
      tableField.setRowHeight(30);
      tableField.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

      JTableHeader header = tableField.getTableHeader();
      Font headerFont = new Font("Liberation Sans", Font.BOLD, 15); ///////////// font experiment
      header.setFont(headerFont);
      Dimension dim = header.getPreferredSize();
      dim.height = 40; // was 26, 34
      header.setPreferredSize(dim);

      columnModel = tableField.getColumnModel();
      TableColumn column0 = columnModel.getColumn(0);
      TableColumn column1 = columnModel.getColumn(1);
      TableColumn column2 = columnModel.getColumn(2);
      TableColumn column3 = columnModel.getColumn(3);
      TableColumn column4 = columnModel.getColumn(4);
      column5 = columnModel.getColumn(5);

      tableField.removeColumn(column0);
      tableField.removeColumn(column1);
      tableField.removeColumn(column4);

      column2.setPreferredWidth(400);
      column3.setPreferredWidth(50);
      column5.setPreferredWidth(100);

      TextFieldEditor songFieldEditor = new TextFieldEditor();
      songFieldEditor.setFilter("[\\w !£$%^&\\*()\\-+={\\[}\\]:;@'~#\\\\<,>\\.?/]*");

      column2.setCellRenderer(new TextFieldRenderer());
      column2.setCellEditor(songFieldEditor);

      column3.setCellRenderer(new TextFieldRenderer());

		JComboBox<String> typeComboBox = new JComboBox<String>();
		typeComboBox.addItem(null);
		typeComboBox.addItem("decimal");
		typeComboBox.addItem("pulses");

      column3.setCellEditor(new DefaultCellEditor(typeComboBox));

      column5.setCellRenderer(new TextFieldRenderer());

      column5.setCellEditor(new DefaultCellEditor(soundbankComboBox));

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      //JButton iconButton     = new JButton(new ImageIcon("iconButton.jpg"));
      //iconButton.setBorder(new EmptyBorder(0,0,0,0));

      JButton searchButton   = new JButton("Search");
      JButton insertButton   = new JButton("Insert");
      JButton deleteButton   = new JButton("Delete");
      JButton saveButton     = new JButton("Save");
      JButton stringsButton  = new JButton("Strings");
      JButton patternsButton = new JButton("Patterns");
      JButton treeviewButton     = new JButton("Tree View");

      //buttonPanel.add(iconButton);
      //buttonPanel.add(blankField);

      buttonPanel.add(searchButton);
      buttonPanel.add(insertButton);
      buttonPanel.add(deleteButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(stringsButton);
      buttonPanel.add(patternsButton);
      buttonPanel.add(treeviewButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      //IconAction   iconAction         = new IconAction();

      SearchAction searchAction     = new SearchAction();
      InsertAction insertAction     = new InsertAction();
      DeleteAction deleteAction     = new DeleteAction();
      SaveAction saveAction         = new SaveAction();
      StringsAction stringsAction   = new StringsAction();
      PatternsAction patternsAction = new PatternsAction();
      TreeViewAction treeviewAction         = new TreeViewAction();

//      iconLabel.addActionListener(treeViewAction);

      //iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
	//	    public void mouseEntered(java.awt.event.MouseEvent evt) {
	//	        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	//	    }
	//	    public void mouseExited(java.awt.event.MouseEvent evt) {
	//	        setCursor(Cursor.getDefaultCursor());
	//	    }
      //});

      searchButton.addActionListener(searchAction);
      insertButton.addActionListener(insertAction);
      deleteButton.addActionListener(deleteAction);
      saveButton.addActionListener(saveAction);
      stringsButton.addActionListener(stringsAction);
      patternsButton.addActionListener(patternsAction);
      treeviewButton.addActionListener(treeviewAction);
   }

   private class WebAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
			try
			{
			   Desktop myDesktop = Desktop.getDesktop();
            myDesktop.browse(new URI("http://gray10.com/"));
			}
			catch (Exception e)
			{
				Messages.exceptionHandler(frame, title, e);
			}
			tableField.requestFocusInWindow();
      }
   }

   private class SearchAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
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
            selection = Messages.warningQuestion(frame, title, "OK to Search? - Data not saved.");
         }
         if (selection == 0)
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

                  soundbankComboBox = tableModel.getSoundbankComboBox();
                  column5.setCellEditor(new DefaultCellEditor(soundbankComboBox));
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
            int selection = Messages.plainQuestion(frame, title,
                              "OK to Delete?  This will delete all the Strings and Patterns in the Song.");
            if (selection == 0)
            {
               status     = (String)tableModel.getValueAt(selectedRow, 0);
               song_id    = (Integer)tableModel.getValueAt(selectedRow, 1);
               try
               {
                  if (status.equals("unchanged") || status.equals("changed"))
                  {
                     if (session_user == null)
                     {
                        deleteStmt_pattern_component.setInt(1,song_id.intValue());
                        deleteStmt_pattern_component.execute();
                        deleteStmt_component.setInt(1,song_id.intValue());
                        deleteStmt_component.execute();
                        deleteStmt.setInt(1,song_id.intValue());
                        deleteStmt.execute();

                        conn.commit();
                     }
                     else
                     {
                        cSdeleteStmt_pattern_component.setInt(1,song_id.intValue());
                        cSdeleteStmt_pattern_component.setString(2, session_user);
                        cSdeleteStmt_pattern_component.setString(3, session_password);
                        cSdeleteStmt_pattern_component.execute();

                        rset = cSdeleteStmt_pattern_component.getResultSet();

								while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

								if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only delete your own songs.");
								}

                        rset.close();

                        cSdeleteStmt_component.setInt(1,song_id.intValue());
                        cSdeleteStmt_component.setString(2, session_user);
                        cSdeleteStmt_component.setString(3, session_password);
                        cSdeleteStmt_component.execute();

                        rset = cSdeleteStmt_component.getResultSet();

								while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

							   if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only delete your own songs.");
								}

                        rset.close();

                        cSdeleteStmt.setInt(1,song_id.intValue());
                        cSdeleteStmt.setString(2, session_user);
                        cSdeleteStmt.setString(3, session_password);
                        cSdeleteStmt.execute();

                        rset = cSdeleteStmt.getResultSet();

							   while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

								if (returned_user_id.intValue() == -1)
								{
									rset.close();
									throw new Exception("You can only delete your own songs.");
								}

                        rset.close();

                        conn.commit();
                     }

                     if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
                     {
                        CallableStatement cs = conn.prepareCall
                                               ("CALL SYSCS_UTIL.SYSCS_COMPRESS_TABLE(?, ?, ?)");
                        cs.setString(1, "APP");
                        cs.setString(2, "SBS_SONG");
                        cs.setShort(3, (short)1);
                        cs.execute();
                        conn.commit();
                        cs.setString(2, "SBS_COMPONENT");
                        cs.execute();
                        conn.commit();
                        cs.setString(2, "SBS_PATTERN_COMPONENT");
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
               selectedRow           = i;
               status                = (String)tableModel.getValueAt(i,0);
               song_id               = (Integer)tableModel.getValueAt(i,1);
               song_name             = (String)tableModel.getValueAt(i,2);
               numeric_duration_type = (String)tableModel.getValueAt(i,3);
               soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
            numeric_duration_type = (String)tableModel.getValueAt(i,3);
            soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
                     SBSStrings strings = new SBSStrings(frame, childCountField,
                                                         conn, connection_name, song_id, song_name, session_user, session_password);
                     strings.setVisible(true);
                     childCount++;

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

   private class PatternsAction implements ActionListener
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
            numeric_duration_type = (String)tableModel.getValueAt(i,3);
            soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
                     SBSPatterns patterns = new SBSPatterns(frame, childCountField,
                                                            conn, connection_name, song_id, song_name, numeric_duration_type,
                                                            soundbank_id, session_user, session_password);
                     patterns.setVisible(true);
                     childCount++;

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
					if (! local)
					{
						int siblingCount = siblingCountField.getInt(parentFrame);
				   	siblingCount--;
                  siblingCountField.setInt(parentFrame, siblingCount);
					}
               conn.rollback();

               boolean apache;
               if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
                  apache = true;
               else
                  apache = false;

               conn.close();
               if (local)
               {
                  if (apache)
						{
                     try
                     {
                        DriverManager.getConnection("jdbc:derby:;shutdown=true");
			            }
			            catch (Exception e1)
			            {
			      	   	System.out.println(e1.getMessage());
			      	   }
					   }
						System.exit(0);
					}
               else dispose();
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
			if (song_name != null) song_name = song_name.trim();

         if (session_user == null)
         {
            updateStmt.setString(1, song_name);
            updateStmt.setString(2, numeric_duration_type);

            if (soundbank_id != null)
               updateStmt.setInt(3, soundbank_id.intValue());
            else
               updateStmt.setNull(3, Types.INTEGER);

            updateStmt.setInt(4, song_id.intValue());
            updateStmt.execute();
		   }
		   else
		   {
            cSupdateStmt.setString(1, song_name);
            cSupdateStmt.setString(2, numeric_duration_type);

            if (soundbank_id != null)
               cSupdateStmt.setInt(3, soundbank_id.intValue());
            else
               cSupdateStmt.setNull(3, Types.INTEGER);

            cSupdateStmt.setInt(4, song_id.intValue());
            cSupdateStmt.setString(5, session_user);
            cSupdateStmt.setString(6, session_password);
            cSupdateStmt.execute();

				rset = cSupdateStmt.getResultSet();

		      while (rset.next()) returned_user_id = new Integer(rset.getInt(1));

				if (returned_user_id.intValue() == -1)
				{
				   rset.close();
					throw new Exception("You can only update your own songs.");
				}

            rset.close();
			}
      }
      if (status.equals("inserted"))
      {
			if (song_name != null) song_name = song_name.trim();

			if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
			{
            if (session_user == null)
            {
               insertStmt.setString(1, song_name);
               insertStmt.setString(2, numeric_duration_type);

               if (soundbank_id != null)
                  insertStmt.setInt(3, soundbank_id.intValue());
               else
                  insertStmt.setNull(3, Types.INTEGER);
               insertStmt.execute();

               rset = insertStmt.getGeneratedKeys();
               while (rset.next()) song_id = new Integer(rset.getInt(1));
               rset.close();

               tableModel.setValueAt(song_id, selectedRow, 1);
				}
				else
				{
               cSinsertStmt.setString(1, song_name);
               cSinsertStmt.setString(2, numeric_duration_type);

               if (soundbank_id != null)
                  cSinsertStmt.setInt(3, soundbank_id.intValue());
               else
                  cSinsertStmt.setNull(3, Types.INTEGER);

               cSinsertStmt.setString(4, session_user);
               cSinsertStmt.setString(5, session_password);

               cSinsertStmt.execute();

				   rset = cSinsertStmt.getResultSet();

					while (rset.next()) song_id = new Integer(rset.getInt(1));

				   if (song_id.intValue() == -1)
				   {
						rset.close();
						throw new Exception("Incorrect user name or password.");
					}

               rset.close();

               tableModel.setValueAt(song_id, selectedRow, 1);
				}
			}
			else
         {
         rset = sequenceStmt.executeQuery();
         while (rset.next()) song_id = new Integer(rset.getInt(1));
         rset.close();

         tableModel.setValueAt(song_id, selectedRow, 1);

         insertStmt.setInt(1, song_id.intValue());
         insertStmt.setString(2, song_name);
         insertStmt.setString(3, numeric_duration_type);

         if (soundbank_id != null)
            insertStmt.setInt(4, soundbank_id.intValue());
         else
            insertStmt.setNull(4, Types.INTEGER);

         insertStmt.execute();
		   }
      }
   }

   private class TemplateAction implements ActionListener
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
               selectedRow           = i;
               status                = (String)tableModel.getValueAt(i,0);
               song_id               = (Integer)tableModel.getValueAt(i,1);
               song_name             = (String)tableModel.getValueAt(i,2);
               numeric_duration_type = (String)tableModel.getValueAt(i,3);
               soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
            SBSTemplate template = new SBSTemplate();
            template.setVisible(true); /////////////////////////////////////////////////////////

            if (template.getSelection() == 0)
            {

            no_voices = template.getVoices();
            int voices = Integer.parseInt(no_voices);

            no_parts = template.getParts();
            int parts = Integer.parseInt(no_parts);

            no_bars = template.getBars();
            int bars = Integer.parseInt(no_bars);

            type_of_bar = template.getTypeOfBar();

            //template.dispose(); //////////////////////////////////////////////////

            if (type_of_bar.equals("string"))
            {
   				templateFile = new File(new File("XML"),
   				                        "SongTemplate" + no_voices + "voices" + no_parts + "parts" + no_bars + "stringbars.sbxml");
   		   }
   		   else
   		   {
   				templateFile = new File(new File("XML"),
   				                        "SongTemplate" + no_voices + "voices" + no_parts + "parts" + no_bars + "patternbars.sbxml");
   			}

            {
            FileWriter fileWriter = new FileWriter(templateFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("<?xml version=\"1.0\"?>");
            bufferedWriter.newLine();
            bufferedWriter.write("<songs>");
            bufferedWriter.newLine();

            bufferedWriter.write("  <song>");
            bufferedWriter.newLine();

            if (type_of_bar.equals("string"))
            {
               bufferedWriter.write("    <song_name><![CDATA[Template with " + no_voices + " voices, " + no_parts + " parts and " + no_bars + " string bars]]></song_name>");
			   }
			   else
			   {
					bufferedWriter.write("    <song_name><![CDATA[Template with " + no_voices + " voices, " + no_parts + " parts and " + no_bars + " pattern bars]]></song_name>");
				}
            bufferedWriter.newLine();

				bufferedWriter.write("    <components>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>pattern</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>Song</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </component>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>pattern</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>Voices</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </component>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <component>"); // new
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>pattern</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>Constants</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </component>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>string</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>constants</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </component>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>string</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>tempo</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <string_value>T120</string_value>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </component>");
				bufferedWriter.newLine();

				if (Integer.parseInt(no_parts) > 1)
				{
				   for (int part_no = 1; part_no <= Integer.parseInt(no_parts); part_no++)
				   {
			      	bufferedWriter.write("      <component>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_type>pattern</component_type>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_name>Part " + Integer.toString(part_no) + "</component_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("      </component>");
			      	bufferedWriter.newLine();
				   }
			   }

				String voiceMask = "00";
				String barMask   = "000";
				String numberi;
				String numberp;
				String numberj;
				String voiceNumber;
				String barNumber;

				for (int v=0; v<=Integer.parseInt(no_voices)-1; v++)
				{
					bufferedWriter.write("      <component>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_type>string</component_type>");
					bufferedWriter.newLine();
					numberi = Integer.toString(v);
					voiceNumber = voiceMask.substring(0,voiceMask.length()-numberi.length()) + numberi;
					if (v == 9) bufferedWriter.write("        <component_name>start voice " + voiceNumber + " (percussion)</component_name>");
					else bufferedWriter.write("        <component_name>start voice " + voiceNumber + "</component_name>");
					bufferedWriter.newLine();
					if (v == 9) bufferedWriter.write("        <string_value>V" + numberi + "</string_value>");
					else bufferedWriter.write("        <string_value>V" + numberi + " KCmaj I[piano]</string_value>");
					bufferedWriter.newLine();
					bufferedWriter.write("      </component>");
					bufferedWriter.newLine();

					bufferedWriter.write("      <component>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_type>pattern</component_type>");
					bufferedWriter.newLine();
					if (v == 9) bufferedWriter.write("        <component_name>Voice " + voiceNumber + " (Percussion)</component_name>");
					else bufferedWriter.write("        <component_name>Voice " + voiceNumber + "</component_name>");
					bufferedWriter.newLine();
					bufferedWriter.write("      </component>");
					bufferedWriter.newLine();

               for (int p=1; p<=Integer.parseInt(no_parts); p++)
               {
						numberp = Integer.toString(p);

				   	bufferedWriter.write("      <component>");
				   	bufferedWriter.newLine();
				   	bufferedWriter.write("        <component_type>pattern</component_type>");
				   	bufferedWriter.newLine();
				   	bufferedWriter.write("        <component_name>Voice " + voiceNumber + " Part " + numberp + "</component_name>");
				   	bufferedWriter.newLine();
				   	bufferedWriter.write("      </component>");
				   	bufferedWriter.newLine();

				   	for (int j=1; j<=Integer.parseInt(no_bars); j++)
				   	{
				   		bufferedWriter.write("      <component>");
				   		bufferedWriter.newLine();
				   		if (type_of_bar.equals("string"))
				   		{
				   	   	bufferedWriter.write("        <component_type>string</component_type>");
						   }
						   else
						   {
								bufferedWriter.write("        <component_type>pattern</component_type>");
							}
				   		bufferedWriter.newLine();
				   		numberj = Integer.toString(j);
				   		barNumber = barMask.substring(0,barMask.length()-numberj.length()) + numberj;
				   		if (type_of_bar.equals("string"))
				   		{
				   		   bufferedWriter.write("        <component_name>voice " + voiceNumber + " part " + numberp +" bar " + barNumber + "</component_name>");
						   }
						   else
						   {
								bufferedWriter.write("        <component_name>Voice " + voiceNumber + " Part " + numberp +" Bar " + barNumber + "</component_name>");
							}
				   		bufferedWriter.newLine();
				   		bufferedWriter.write("      </component>");
				   		bufferedWriter.newLine();
				   	}
				   }
				}

				bufferedWriter.write("    </components>");
				bufferedWriter.newLine();

				bufferedWriter.write("    <pattern_components>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <pattern_component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <pattern_name>Song</pattern_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_position>1</component_position>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>string</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>tempo</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </pattern_component>");
				bufferedWriter.newLine();

				bufferedWriter.write("      <pattern_component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <pattern_name>Song</pattern_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_position>2</component_position>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>pattern</component_type>"); // changed
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>Constants</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </pattern_component>");
				bufferedWriter.newLine();

			      	bufferedWriter.write("      <pattern_component>"); //new
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <pattern_name>Constants</pattern_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_position>1</component_position>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_type>string</component_type>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_name>constants</component_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("      </pattern_component>");
			      	bufferedWriter.newLine();

				bufferedWriter.write("      <pattern_component>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <pattern_name>Song</pattern_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_position>3</component_position>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_type>pattern</component_type>");
				bufferedWriter.newLine();
				bufferedWriter.write("        <component_name>Voices</component_name>");
				bufferedWriter.newLine();
				bufferedWriter.write("      </pattern_component>");
				bufferedWriter.newLine();

				for (int v=0; v<=Integer.parseInt(no_voices)-1; v++)
				{
					numberi = Integer.toString(v);
					voiceNumber = voiceMask.substring(0,voiceMask.length()-numberi.length()) + numberi;
					bufferedWriter.write("      <pattern_component>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <pattern_name>Voices</pattern_name>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_position>" + numberi + "</component_position>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_type>pattern</component_type>");
					bufferedWriter.newLine();
					if (v == 9) bufferedWriter.write("        <component_name>Voice " + voiceNumber + " (Percussion)</component_name>");
					else bufferedWriter.write("        <component_name>Voice " + voiceNumber + "</component_name>");
					bufferedWriter.newLine();
					bufferedWriter.write("      </pattern_component>");
					bufferedWriter.newLine();

					bufferedWriter.write("      <pattern_component>");
					bufferedWriter.newLine();
					if (v == 9) bufferedWriter.write("        <pattern_name>Voice " + voiceNumber + " (Percussion)</pattern_name>");
					else bufferedWriter.write("        <pattern_name>Voice " + voiceNumber + "</pattern_name>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_position>0</component_position>");
					bufferedWriter.newLine();
					bufferedWriter.write("        <component_type>string</component_type>");
					bufferedWriter.newLine();
					if (v == 9) bufferedWriter.write("        <component_name>start voice " + voiceNumber + " (percussion)</component_name>");
					else bufferedWriter.write("        <component_name>start voice " + voiceNumber + "</component_name>");
					bufferedWriter.newLine();
					bufferedWriter.write("      </pattern_component>");
					bufferedWriter.newLine();

               for (int p=1; p<=Integer.parseInt(no_parts); p++)
               {
						numberp = Integer.toString(p);

		   			bufferedWriter.write("      <pattern_component>");
		   			bufferedWriter.newLine();
		   			if (v == 9) bufferedWriter.write("        <pattern_name>Voice " + voiceNumber + " (Percussion)</pattern_name>");
		   			else bufferedWriter.write("        <pattern_name>Voice " + voiceNumber + "</pattern_name>");
		   			bufferedWriter.newLine();
		   			bufferedWriter.write("        <component_position>" + numberp + "</component_position>");
		   			bufferedWriter.newLine();
		   			bufferedWriter.write("        <component_type>pattern</component_type>");
		   			bufferedWriter.newLine();
		   			bufferedWriter.write("        <component_name>Voice " + voiceNumber + " Part " + numberp + "</component_name>");
		   			bufferedWriter.newLine();
		   			bufferedWriter.write("      </pattern_component>");
		   			bufferedWriter.newLine();

		   			for (int j=1; j<=Integer.parseInt(no_bars); j++)
		   			{
		   				numberj = Integer.toString(j);
		   				barNumber = barMask.substring(0,barMask.length()-numberj.length()) + numberj;

		   				bufferedWriter.write("      <pattern_component>");
		   				bufferedWriter.newLine();
		   				bufferedWriter.write("        <pattern_name>Voice " + voiceNumber + " Part " + numberp + "</pattern_name>");
		   				bufferedWriter.newLine();
		   				bufferedWriter.write("        <component_position>" + numberj + "</component_position>");
		   				bufferedWriter.newLine();
		   				if (type_of_bar.equals("string"))
		   				{
		   				   bufferedWriter.write("        <component_type>string</component_type>");
						   }
						   else
						   {
								bufferedWriter.write("        <component_type>pattern</component_type>");
							}
		   				bufferedWriter.newLine();
		   				if (type_of_bar.equals("string"))
		   				{
		   			   	bufferedWriter.write("        <component_name>voice " + voiceNumber + " part " + numberp + " bar " + barNumber + "</component_name>");
						   }
						   else
						   {
								bufferedWriter.write("        <component_name>Voice " + voiceNumber + " Part " + numberp + " Bar " + barNumber + "</component_name>");
							}
		   				bufferedWriter.newLine();
		   				bufferedWriter.write("      </pattern_component>");
		   				bufferedWriter.newLine();
		   			}
				   }
				}

            if (Integer.parseInt(no_parts) > 1)
            {
               for (int part_no = 1; part_no <= Integer.parseInt(no_parts); part_no++)
               {
			      	bufferedWriter.write("      <pattern_component>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <pattern_name>Part " + Integer.toString(part_no) + "</pattern_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_position>1</component_position>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_type>string</component_type>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_name>tempo</component_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("      </pattern_component>");
			      	bufferedWriter.newLine();

			      	bufferedWriter.write("      <pattern_component>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <pattern_name>Part " + Integer.toString(part_no) + "</pattern_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_position>2</component_position>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_type>pattern</component_type>"); //changed
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("        <component_name>Constants</component_name>");
			      	bufferedWriter.newLine();
			      	bufferedWriter.write("      </pattern_component>");
			      	bufferedWriter.newLine();

                  for (int v=0; v<=Integer.parseInt(no_voices)-1; v++)
                  {
							numberi = Integer.toString(v);
					      voiceNumber = voiceMask.substring(0,voiceMask.length()-numberi.length()) + numberi;

				      	bufferedWriter.write("      <pattern_component>");
					      bufferedWriter.newLine();
					      bufferedWriter.write("        <pattern_name>Part " + Integer.toString(part_no) + "</pattern_name>");
				      	bufferedWriter.newLine();
					      bufferedWriter.write("        <component_position>" + Integer.toString(v*2 + 3) + "</component_position>");
					      bufferedWriter.newLine();
					      bufferedWriter.write("        <component_type>string</component_type>");
 				         bufferedWriter.newLine();
					      if (v == 9) bufferedWriter.write("        <component_name>start voice " + voiceNumber + " (percussion)</component_name>");
					      else bufferedWriter.write("        <component_name>start voice " + voiceNumber + "</component_name>");
					      bufferedWriter.newLine();
					      bufferedWriter.write("      </pattern_component>");
					      bufferedWriter.newLine();

		   	   		bufferedWriter.write("      <pattern_component>");
		   	   		bufferedWriter.newLine();
		   	   		bufferedWriter.write("        <pattern_name>Part " + Integer.toString(part_no) + "</pattern_name>");
		   	   		bufferedWriter.newLine();
		   	   		bufferedWriter.write("        <component_position>" + Integer.toString(v*2 + 4) + "</component_position>");
		   	   		bufferedWriter.newLine();
		   	   		bufferedWriter.write("        <component_type>pattern</component_type>");
		   	   		bufferedWriter.newLine();
		   	   		bufferedWriter.write("        <component_name>Voice " + voiceNumber + " Part " + Integer.toString(part_no) + "</component_name>");
		   	   		bufferedWriter.newLine();
		   	   		bufferedWriter.write("      </pattern_component>");
		   	   		bufferedWriter.newLine();
						}
			      }
				}

				bufferedWriter.write("    </pattern_components>");
				bufferedWriter.newLine();

            bufferedWriter.write("  </song>");
            bufferedWriter.newLine();

            bufferedWriter.write("</songs>");
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileWriter.close();
            Messages.plainMessage(frame, title, "Template exported to: " + templateFile.getPath());
            }
               {
                  if (templateFile.exists())
                  {
                     SAXParserFactory factory = SAXParserFactory.newInstance();
                     SAXParser parser = factory.newSAXParser();

                     SBSHandler handler = new SBSHandler(conn, session_user, session_password);

                     parser.parse(templateFile, handler);

                     tableModel.query("");
                     conn.commit();

                     Integer importedSong = handler.getImportedSong();
                     if (importedSong != null)
                     {
								for (i=0; i<tableModel.getRowCount(); i++)
								{
									selectedRow = i;
									song_id = (Integer)tableModel.getValueAt(i,1);
									if (song_id.equals(importedSong))
									{
										tableField.changeSelection(selectedRow,0,false,false);
										break;
									}
								}
							}

                     Messages.plainMessage(frame, title, "Template imported from: " + templateFile.getPath());
                  }
                  else
                  {
                     Messages.warningMessage(frame, title, "Template file: " + templateFile.getPath() + " not found.");
                  }
               } /////////////////////////////////////////////////

			   }
			      template.dispose(); //////////////////////
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

   private class XmlExportAction implements ActionListener
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
            status                = (String)tableModel.getValueAt(i,0);
            song_id               = (Integer)tableModel.getValueAt(i,1);
            song_name             = (String)tableModel.getValueAt(i,2);
            numeric_duration_type = (String)tableModel.getValueAt(i,3);
            soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
                     JFileChooser chooser = new JFileChooser(new File("XML"), fsv);
                     chooser.setSelectedFile(new File(song_name + ".sbxml"));
                     chooser.setPreferredSize(new Dimension(600,300));
                     chooser.setDialogTitle("Song Export - " + song_name);

                     int result = chooser.showDialog(frame, "Export Song");
                     if (result == JFileChooser.APPROVE_OPTION)
                     {
                        File file = chooser.getSelectedFile();

                        FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        bufferedWriter.write("<?xml version=\"1.0\"?>");
                        bufferedWriter.newLine();
                        bufferedWriter.write("<songs>");
                        bufferedWriter.newLine();

                        bufferedWriter.write("  <song>");
                        bufferedWriter.newLine();

                        bufferedWriter.write("    <song_name><![CDATA[" + song_name + "]]></song_name>");
                        bufferedWriter.newLine();
                        if (numeric_duration_type != null)
                        {
                           bufferedWriter.write("    <numeric_duration_type>" + numeric_duration_type + "</numeric_duration_type>");
                           bufferedWriter.newLine();
							   }

							   bufferedWriter.write("    <components>");
							   bufferedWriter.newLine();

                        if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                        {
		                   	cStmt = conn.prepareCall("{call get_xml_components(?)}");
		                  	cStmt.setInt(1, song_id.intValue());
			                  cStmt.execute();
			                  rset = cStmt.getResultSet();
		                  }
		                  else
		                  {
                        stmt = conn.createStatement();
                        rset = stmt.executeQuery
                               ("select component_type, component_name, string_value " +
                                "from sbs_component " +
                                "where song_id = " + song_id.toString() +
                                " order by component_type, component_name");
								}
                        while (rset.next())
                        {
                           component_type = rset.getString(1);
                           component_name = rset.getString(2);
                           string_value   = rset.getString(3);
							      bufferedWriter.write("      <component>");
							      bufferedWriter.newLine();
							      bufferedWriter.write("        <component_type>" + component_type + "</component_type>");
							      bufferedWriter.newLine();
							      bufferedWriter.write("        <component_name><![CDATA[" + component_name + "]]></component_name>");
							      bufferedWriter.newLine();
							      if (string_value != null)
							      {
							         bufferedWriter.write("        <string_value><![CDATA[" + string_value + "]]></string_value>");
							         bufferedWriter.newLine();
									}
							      bufferedWriter.write("      </component>");
							      bufferedWriter.newLine();
                        }
                        rset.close();
                        if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

							   bufferedWriter.write("    </components>");
							   bufferedWriter.newLine();

							   bufferedWriter.write("    <pattern_components>");
							   bufferedWriter.newLine();

                        if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                        {
		                   	cStmt = conn.prepareCall("{call get_xml_pattern_components(?)}");
		                  	cStmt.setInt(1, song_id.intValue());
			                  cStmt.execute();
			                  rset = cStmt.getResultSet();
		                  }
		                  else
		                  {
                           stmt = conn.createStatement();
                           rset = stmt.executeQuery
                                  ("select p.component_name, pc.component_position, c.component_type, c.component_name, pc.anonymous_string " +
                                   "from (sbs_pattern_component pc join sbs_component p on pc.pattern_id = p.component_id) " +
                                   "     left join sbs_component c on pc.component_id = c.component_id " +
                                   "where   p.song_id = " + song_id.toString() +
                                   " order by p.component_name, pc.component_position");
								}
                        while (rset.next())
                        {
                           pattern_name       = rset.getString(1);
                           component_position = new Integer(rset.getInt(2));
                           component_type     = rset.getString(3);
                           component_name     = rset.getString(4);
                           anonymous_string   = rset.getString(5);
							      bufferedWriter.write("      <pattern_component>");
							      bufferedWriter.newLine();
							      bufferedWriter.write("        <pattern_name><![CDATA[" + pattern_name + "]]></pattern_name>");
							      bufferedWriter.newLine();
							      bufferedWriter.write("        <component_position>" + component_position.toString() + "</component_position>");
							      bufferedWriter.newLine();
							      if (component_type != null)
							      {
							      bufferedWriter.write("        <component_type>" + component_type + "</component_type>");
							      bufferedWriter.newLine();
								   }
								   if (component_name != null)
								   {
							      bufferedWriter.write("        <component_name><![CDATA[" + component_name + "]]></component_name>");
							      bufferedWriter.newLine();
								   }
							      if (anonymous_string != null)
							      {
							         bufferedWriter.write("        <anonymous_string><![CDATA[" + anonymous_string + "]]></anonymous_string>");
							         bufferedWriter.newLine();
									}
							      bufferedWriter.write("      </pattern_component>");
							      bufferedWriter.newLine();
                        }
                        rset.close();
                        if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

							   bufferedWriter.write("    </pattern_components>");
							   bufferedWriter.newLine();

                        bufferedWriter.write("  </song>");
                        bufferedWriter.newLine();

                        bufferedWriter.write("</songs>");
                        bufferedWriter.newLine();

                        conn.commit();
                        bufferedWriter.close();
                        fileWriter.close();
                        Messages.plainMessage(frame, title, "Song exported to: " + file.getPath());
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

   private class XmlImportAction implements ActionListener
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
               selectedRow           = i;
               status                = (String)tableModel.getValueAt(i,0);
               song_id               = (Integer)tableModel.getValueAt(i,1);
               song_name             = (String)tableModel.getValueAt(i,2);
               numeric_duration_type = (String)tableModel.getValueAt(i,3);
               soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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
               FileSystemView fsv = new SingleRootFileSystemView(new File("."));
               JFileChooser chooser = new JFileChooser(new File("XML"), fsv);
               chooser.setPreferredSize(new Dimension(600,300));
               chooser.setDialogTitle("Song Import");

               FileNameExtensionFilter filter = new FileNameExtensionFilter("Audovia Song files *.sbxml", "sbxml");
               chooser.addChoosableFileFilter(filter);
               chooser.setFileFilter(filter);

               int result = chooser.showDialog(frame, "Import Song");
               if (result == JFileChooser.APPROVE_OPTION)
               {
                  File file = chooser.getSelectedFile();

                  if (file.exists())
                  {
                     SAXParserFactory factory = SAXParserFactory.newInstance();
                     SAXParser parser = factory.newSAXParser();

                     SBSHandler handler = new SBSHandler(conn, session_user, session_password);

                     parser.parse(file, handler);

                     tableModel.query("");
                     conn.commit();

                     Integer importedSong = handler.getImportedSong();
                     if (importedSong != null)
                     {
								for (i=0; i<tableModel.getRowCount(); i++)
								{
									selectedRow = i;
									song_id = (Integer)tableModel.getValueAt(i,1);
									if (song_id.equals(importedSong))
									{
										tableField.changeSelection(selectedRow,0,false,false);
										break;
									}
								}
							}

                     Messages.plainMessage(frame, title, "Song file: " + file.getPath() + " imported.");
                  }
                  else
                  {
                     Messages.warningMessage(frame, title, "Song file: " + file.getPath() + " not found.");
                  }
               }
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

   private class TreeViewAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
//         if (tableField.isEditing())
//         {
//            int editingRow = tableField.getEditingRow();
//            int editingCol = tableField.getEditingColumn();
//            TableCellEditor tableEditor = tableField.getCellEditor();
//            tableEditor.stopCellEditing();
//            tableField.setValueAt(tableEditor.getCellEditorValue(),
//               editingRow, editingCol);
//            tableField.requestFocusInWindow();
//         }
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
            tableField.changeSelection(selectedRow, selectedCol, false, false);
            int i = selectedRow;
            status                = (String)tableModel.getValueAt(i,0);
            song_id               = (Integer)tableModel.getValueAt(i,1);
            song_name             = (String)tableModel.getValueAt(i,2);
            numeric_duration_type = (String)tableModel.getValueAt(i,3);
            soundbank_id          = (Integer)tableModel.getValueAt(i,4);

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

                     SBSTree tree = new SBSTree(frame, childCountField,
                                                conn, connection_name, song_id, song_name, numeric_duration_type,
                                                soundbank_id, session_user, session_password);
                     tree.setVisible(true);
                     childCount++;

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

   private class SoundbanksAction implements ActionListener
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
         try
         {
            SBSSoundbanks soundbanks = new SBSSoundbanks(frame, childCountField, conn, connection_name);
            soundbanks.setVisible(true);
            childCount++;

            //tableField.requestFocusInWindow();
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

   private class ConnectionsAction implements ActionListener
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
         try
         {
            SBSConnections connections = new SBSConnections(frame, childCountField, conn, connection_name);
            connections.setVisible(true);
            childCount++;

            //tableField.requestFocusInWindow();
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
         try
         {
            String user = null;
            String passwordString = null;
            String email = null;

            CreateUser createUser = new CreateUser(user, passwordString, email);
            createUser.setVisible(true);

            while (createUser.getSelection() == 0)
            {
               try
               {
                   user = createUser.getUser();

                   char[] password = createUser.getPassword();
                   passwordString = "";
                   for (int ii = 0; ii < password.length; ii++)
                       passwordString += password[ii];

                   if (user.length() < 6 || passwordString.length() < 6) throw new Exception("User names and passwords must be six characters or more in length.");

                   email = createUser.getEmail();

					    cSinsertUser.setString(1, user);
						 cSinsertUser.setString(2, passwordString);
						 cSinsertUser.setString(3, email);
						 cSinsertUser.execute(); // need a commit

						 conn.commit();

						 Messages.plainMessage(frame, "Create Account", "User account created.");

                   break;
               }
               catch (Exception e2)
               {
						conn.rollback();
                  Messages.exceptionHandler(frame, "Create Account", e2);
                  createUser.setVisible(true);
               }
            }

            createUser.dispose();
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

   private class LoginAction implements ActionListener
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
         try
         {
            String user = null;
            String passwordString = null;

            LogIn logIn = new LogIn(user, passwordString);
            logIn.setVisible(true);

            while (logIn.getSelection() == 0)
            {
               try
               {
                   user = logIn.getUser();

                   char[] password = logIn.getPassword();
                   passwordString = "";
                   for (int ii = 0; ii < password.length; ii++)
                       passwordString += password[ii];

					    int count = 0;

					    cScheckUser.setString(1, user);
						 cScheckUser.setString(2, passwordString);  // check if credentials ok else throw exception;
						 cScheckUser.execute();
						 rset = cScheckUser.getResultSet();

						 while (rset.next()) count = rset.getInt(1);
		             rset.close();

						 conn.commit();

						 if (count == 0) throw new Exception("Incorrect user name or password.");

						 session_user = user;
						 session_password = passwordString;
						 setTitle(title + " (" + connection_name + ") " + session_user);

						 Messages.plainMessage(frame, "Log in", "Log in successful.");

                   break;
               }
               catch (Exception e2)
               {
						conn.rollback();
                  Messages.exceptionHandler(frame, "Log in", e2);
                  logIn.setVisible(true);
               }
            }

            logIn.dispose();
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

   private class ShareAction implements ActionListener
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
            status                = (String)tableModel.getValueAt(i,0);
            song_id               = (Integer)tableModel.getValueAt(i,1);
            song_name             = (String)tableModel.getValueAt(i,2);

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

			   		   cSgetOwnerName.setInt(1, song_id.intValue());
			   			cSgetOwnerName.execute();
			   			rset = cSgetOwnerName.getResultSet();

			   			while (rset.next()) owner_name = rset.getString(1);
		               rset.close();

			   			conn.commit();

                     SBSShare share = new SBSShare(conn, connection_name,
                               song_id,
                               song_name,
                               owner_name,
                               session_user, session_password);
                     share.setVisible(true);

                     share.dispose();
                     //tableField.requestFocusInWindow();
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

   private class DocumentationAction implements ActionListener
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
         try
         {
            String defaultLF = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(defaultLF);

            ViewerComponentExample viewPDF = new ViewerComponentExample();
            viewPDF.view("doc/AudoviaDocumentation-3-6.pdf");

         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               UIManager.setLookAndFeel(info.getClassName());
               UIManager.put("ScrollBar.minimumThumbSize", new Dimension(32,32)); // added 8 Sep 2015
               break;
            }
         }

         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
      }
   }

   private class AboutAction implements ActionListener
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
         try
         {
            JFrame aboutFrame = new JFrame();
            aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            aboutFrame.setTitle("About Audovia");

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      aboutFrame.setIconImage(icon.getImage());

  //         JTextArea output = new JTextArea();
  //          output.setEditable(false);
            //aboutFrame.getContentPane().add(new JScrollPane(output));
            //aboutFrame.setSize(560,380);

            aboutFrame.setLocation(100,100);
            aboutFrame.setVisible(true);

            JLabel label = new JLabel("<html><p style=\"margin-bottom:8px;\"><b>Audovia</b> - Database application for making music using JFugue " +
                          "MusicStrings&nbsp; version 3.6.0</p>" +

                          "<p style=\"margin-bottom:4px;\">Copyright (C) 2010 - 2017&nbsp; Donald G Gray</p>" +

                          "<p style=\"margin-bottom:4px;\">website: http://audovia.com/</p>" +
                          "<p style=\"margin-bottom:4px;\">e-mail: info@audovia.com</p>" +
                          "<p style=\"margin-bottom:8px;\">documentation: ~/snap/audovia/&lt;version&gt;/doc/</p>" +

                          "<p style=\"margin-bottom:8px;\"><b>Quick Start:<br>" +
                          "&nbsp;&nbsp;&nbsp;&nbsp; 1. File/Song Import, open the Demo folder, then select a song and Import Song.<br>" +
                          "&nbsp;&nbsp;&nbsp;&nbsp; 2. Tree View, select \"Song\", press Play, then Default Soundbank.</b></p>" +

                          "<p style=\"margin-bottom:8px;\">This program is free software: you can redistribute it and/or modify " +
                          "it under the terms of the GNU General Public License as published by " +
                          "the Free Software Foundation, either version 3 of the License, or " +
                          "(at your option) any later version.</p>" +

                          "<p style=\"margin-bottom:8px;\">This program is distributed in the hope that it will be useful, " +
                          "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
                          "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.&nbsp; " +
                          "See the GNU General Public License for more details.</p>" +

                          "<p style=\"margin-bottom:8px;\">You should have received a copy of the GNU General Public License " +
                          "along with this program.&nbsp; If not, see http://www.gnu.org/licenses/.</p>" +

                          "<p style=\"margin-bottom:8px;\">The Documentation PDF Viewer uses the ICEpdf library and is distributed " +
                          "under the terms of the Apache License, version 2.</p> " +

                          "<p style=\"margin-bottom:8px;\">You should have received a copy of the Apache License " +
                          "along with this program.&nbsp; If not, see http://www.apache.org/licenses/.</p>" +

                          "</html>");

            label.setPreferredSize(new Dimension(600,500));
            label.setVerticalAlignment(JLabel.TOP);

            JPanel myPanel = new JPanel();
            myPanel.add(label);

            JScrollPane contentPane = new JScrollPane(myPanel); // added
            contentPane.setPreferredSize(new Dimension(660,400));

            aboutFrame.add(contentPane); // added
            aboutFrame.pack(); // added

				//tableField.requestFocusInWindow();
         }
         catch (Exception e)
         {
            Messages.exceptionHandler(frame, title, e);
         }
      }
   }

   private class TutorialAction implements ActionListener
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
			try
			{
			   Desktop myDesktop = Desktop.getDesktop();
            //myDesktop.browse(new URI("http://www.songbuilder.co.uk/tutorial"));
            //if (System.getProperty("os.name").startsWith("Windows"))
            myDesktop.open(new File("doc/AudoviaDocumentation.pdf"));
            //else
            //myDesktop.open(new File("/opt/SongBase/doc/SongBuilderDocumentation.pdf"));
				//tableField.requestFocusInWindow();
			}
			catch (Exception e)
			{
				Messages.exceptionHandler(frame, title, e);
			}
      }
   }

//   private class FaqAction implements ActionListener
//   {
//      public void actionPerformed(ActionEvent a)
//      {
//         if (tableField.isEditing())
//         {
//            int editingRow = tableField.getEditingRow();
//            int editingCol = tableField.getEditingColumn();
//            TableCellEditor tableEditor = tableField.getCellEditor();
//            tableEditor.stopCellEditing();
//            tableField.setValueAt(tableEditor.getCellEditorValue(),
//               editingRow, editingCol);
//            tableField.requestFocusInWindow();
//         }
//			try
//			{
//			   Desktop myDesktop = Desktop.getDesktop();
//            myDesktop.browse(new URI("http://www.songbuilder.co.uk/faq"));
//
//				//tableField.requestFocusInWindow();
//			}
//			catch (Exception e)
//			{
//				Messages.exceptionHandler(frame, title, e);
//			}
//      }
//   }

//   private class CheckAction implements ActionListener
//   {
//      public void actionPerformed(ActionEvent a)
//      {
//         if (tableField.isEditing())
//         {
//            int editingRow = tableField.getEditingRow();
//            int editingCol = tableField.getEditingColumn();
//            TableCellEditor tableEditor = tableField.getCellEditor();
//            tableEditor.stopCellEditing();
//            tableField.setValueAt(tableEditor.getCellEditorValue(),
//               editingRow, editingCol);
//            tableField.requestFocusInWindow();
//         }
//			try
//			{
//			   Desktop myDesktop = Desktop.getDesktop();
//            //myDesktop.browse(new URI("http://www.songbuilder.co.uk/installation"));
//            myDesktop.open(new File("doc/Song Builder Installation.pdf"));
//
//				//tableField.requestFocusInWindow();
//			}
//			catch (Exception e)
//			{
//				Messages.exceptionHandler(frame, title, e);
//			}
//      }
//   }

//   private class ContactAction implements ActionListener
//   {
//      public void actionPerformed(ActionEvent a)
//      {
//         if (tableField.isEditing())
//         {
//            int editingRow = tableField.getEditingRow();
//            int editingCol = tableField.getEditingColumn();
//            TableCellEditor tableEditor = tableField.getCellEditor();
//            tableEditor.stopCellEditing();
//            tableField.setValueAt(tableEditor.getCellEditorValue(),
//               editingRow, editingCol);
//            tableField.requestFocusInWindow();
//         }
//			try
//			{
//			   Desktop myDesktop = Desktop.getDesktop();
//            myDesktop.mail(new URI("mailto:gray10@gmx.com"));
//
//				//tableField.requestFocusInWindow();
//			}
//			catch (Exception e)
//			{
//				Messages.exceptionHandler(frame, title, e);
//			}
//      }
//   }
}
