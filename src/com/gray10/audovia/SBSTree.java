/*
 * SBSTree.java - Tree View
 * Copyright (C) 2011, 2014, 2015, 2016, 2017, 2019  Donald G Gray
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
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.lang.reflect.*;
import java.sql.*;
//import nu.xom.*;

import javax.sound.midi.*;
import javax.swing.filechooser.*;

import org.jfugue.*;
import org.jfugue.extras.*;

import javax.swing.plaf.basic.*;

import org.sun.media.sound.SF2Soundbank; ////////////////////
import org.sun.media.sound.DLSSoundbank; ///////////////////

//import com.sun.media.sound.AudioSynthesizer;

public class SBSTree extends JFrame
{
	/*
	 * version 4.0
	 *
	 */

   private static final long serialVersionUID = 1L;

   private JFrame parentFrame;
   private Field siblingCountField;

   private JFrame frame = SBSTree.this;
   private Class<?> c = Class.forName("com.gray10.audovia.SBSTree");
   private Field childCountField = c.getField("childCount");
   public  int childCount = 0;

   private JTree tree;

   private Connection conn;
   private String connection_name;
   private Statement stmt;
   private ResultSet rset;

   private Integer song_id;
   private String  song_name;
   //private String  string_value;
   private String  numeric_duration_type;
   private Integer soundbank_id;

   private SBSTreeModel treeModel;
   private String title = "SBSTree";

   private String  drill_string_value;
	private String  constants;
   private String  parse_value;

   private Integer pattern_to_be_cloned_id;
   private Integer clone_pattern_id;
   private String  clone_pattern_name;

   private Integer component_position;
   private String  anonymous_string;
   private Integer pattern_component_id;

   private PreparedStatement getConstants;
   private CallableStatement cSgetConstants;
   private PreparedStatement updateString;
   private PreparedStatement updateAnonymousString;
   private PreparedStatement insertStmt;
   private PreparedStatement insertPatternComponentStmt;
   private PreparedStatement sequenceStmt;
   private PreparedStatement sequencePatternComponentStmt;

   private PreparedStatement viewStmt;
   private CallableStatement cSviewStmt;
   private CallableStatement cSupdateString;
   private CallableStatement cSupdateAnonymousString;
   private CallableStatement cSinsertStmt;
   private CallableStatement cSinsertPatternComponentStmt;

   private InputStream inputStream = null;

   private Player player;
   private Synthesizer synthesizer = null;

   private Pattern pattern;

   private JDialog pleaseWait;
   private Soundbank soundbank;

   private String session_user = null;
   private String session_password = null;
   private Integer returned_user_id;

   public SBSTree(JFrame aParentFrame,
                     Field aSiblingCountField,
                     Connection aConnection, String aConnectionName,
                     Integer aSongId,
                     String aSongName,
                     String aNumericDurationType,
                     Integer aSoundbankId,
                     String aSessionUser, String aSessionPassword) throws Exception
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

      setSize(600,412);
      setLocation(100,100);
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

      parentFrame = aParentFrame;
      siblingCountField = aSiblingCountField;
      conn = aConnection;
      connection_name = aConnectionName;
      song_id = aSongId;
      song_name = aSongName;
      numeric_duration_type = aNumericDurationType;
      soundbank_id = aSoundbankId;
      session_user = aSessionUser;
      session_password = aSessionPassword;

      getConstants = conn.prepareStatement
                     ("select string_value " +
                      "from sbs_component " +
                      "where song_id = ? " +
                      "and   component_type = 'string' " +
                      "and   string_value like '%$%'");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      cSgetConstants = conn.prepareCall("{call get_constants(?)}");

      updateString = conn.prepareStatement
                    ("update sbs_component " +
                     "set string_value = ? " +
                     "where component_id = ? ");

      updateAnonymousString = conn.prepareStatement
                    ("update sbs_pattern_component " +
                     "set anonymous_string = ? " +
                     "where pattern_component_id = ? ");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertStmt = conn.prepareStatement
                      ("insert into sbs_component " +
                       "(song_id, component_type, component_name) " +
                       "values (?,'pattern',?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
      insertStmt = conn.prepareStatement
                   ("insert into sbs_component " +
                    "(component_id, song_id, component_type, component_name) " +
                    "values (?,?,'pattern',?)");
		}

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         insertPatternComponentStmt = conn.prepareStatement
                      ("insert into sbs_pattern_component " +
                       "(pattern_id, component_position, component_id, anonymous_string) " +
                       "values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
 		}
      else
      {
      insertPatternComponentStmt = conn.prepareStatement
                   ("insert into sbs_pattern_component " +
                    "(pattern_component_id, pattern_id, component_position, component_id, anonymous_string) " +
                    "values (?,?,?,?,?)");
		}

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

      if (conn.getMetaData().getDatabaseProductName().equals("PostgreSQL"))
      {
        sequencePatternComponentStmt = conn.prepareStatement
                       ("select nextval('sbs_seq_pattern_component')");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Oracle"))
      {
         sequencePatternComponentStmt = conn.prepareStatement
                        ("select sbs_seq_pattern_component.nextval " +
                         "from dual");
      }
      else if (conn.getMetaData().getDatabaseProductName().equals("Apache Derby"))
      {
         sequencePatternComponentStmt = conn.prepareStatement
                        ("select next value for sbs_seq_pattern_component from (values 1) v");
		}

      viewStmt = conn.prepareStatement
                   ("select soundbank from sbs_soundbank " +
                    "where soundbank_id = ?");

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
         cSviewStmt = conn.prepareCall("{call select_soundbank(?)}");
         cSupdateString = conn.prepareCall("{call update_string(?, ?, ?, ?)}");
         cSupdateAnonymousString = conn.prepareCall("{call update_anonymous_string(?, ?, ?, ?)}");
         cSinsertStmt = conn.prepareCall("{call insert_component(?, ?, ?, ?, ?, ?)}");
         cSinsertPatternComponentStmt = conn.prepareCall("{call insert_pattern_component(?, ?, ?, ?, ?, ?)}");
		}

      DefaultMutableTreeNode root = new DefaultMutableTreeNode(song_name);

      treeModel = new SBSTreeModel(conn, song_id, root);
      treeModel.setAsksAllowsChildren(true);
      treeModel.update();
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

      JMenuItem exportMidiItem     = new JMenuItem("Export to MIDI");
      JMenuItem exportMusicStringItem = new JMenuItem("Export to MusicString");
      JMenuItem exportMusicXMLItem = new JMenuItem("Export to MusicXML");
      JMenu     exportWavItem      = new JMenu("Export to WAV");
      JMenuItem cloneItem          = new JMenuItem("Clone");

      JMenuItem exportWavpad0Item  = new JMenuItem("No padding");
      JMenuItem exportWavpad1Item  = new JMenuItem("Pad 1 second");
      JMenuItem exportWavpad2Item  = new JMenuItem("Pad 2 seconds");
      JMenuItem exportWavpad3Item  = new JMenuItem("Pad 3 seconds");
      JMenuItem exportWavpad4Item  = new JMenuItem("Pad 4 seconds");

      fileMenu.add(exportMidiItem);
      fileMenu.add(exportMusicStringItem);
      //fileMenu.add(exportMusicXMLItem);
      fileMenu.add(exportWavItem);
      fileMenu.addSeparator();
      fileMenu.add(cloneItem);

      exportWavItem.add(exportWavpad0Item);
      exportWavItem.add(exportWavpad1Item);
      exportWavItem.add(exportWavpad2Item);
      exportWavItem.add(exportWavpad3Item);
      exportWavItem.add(exportWavpad4Item);

      ExportMidiAction exportMidiAction = new ExportMidiAction();
      exportMidiItem.addActionListener(exportMidiAction);

      ExportMusicStringAction exportMusicStringAction = new ExportMusicStringAction();
      exportMusicStringItem.addActionListener(exportMusicStringAction);

      ExportMusicXMLAction exportMusicXMLAction = new ExportMusicXMLAction();
      exportMusicXMLItem.addActionListener(exportMusicXMLAction);

      ExportWavAction exportWavpad0Action = new ExportWavAction(0);
      exportWavpad0Item.addActionListener(exportWavpad0Action);

      ExportWavAction exportWavpad1Action = new ExportWavAction(1);
      exportWavpad1Item.addActionListener(exportWavpad1Action);

      ExportWavAction exportWavpad2Action = new ExportWavAction(2);
      exportWavpad2Item.addActionListener(exportWavpad2Action);

      ExportWavAction exportWavpad3Action = new ExportWavAction(3);
      exportWavpad3Item.addActionListener(exportWavpad3Action);

      ExportWavAction exportWavpad4Action = new ExportWavAction(4);
      exportWavpad4Item.addActionListener(exportWavpad4Action);

      CloneAction cloneAction = new CloneAction();
      cloneItem.addActionListener(cloneAction);

      tree = new JTree(treeModel);
      tree.setRootVisible(false);
      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

      JLabel myCellRenderer = (JLabel)tree.getCellRenderer();
      myCellRenderer.setVerticalTextPosition(JLabel.TOP);

      tree.addTreeSelectionListener
         (new TreeSelectionListener()
         {
		    public void valueChanged(TreeSelectionEvent event)
			{
		       TreePath path = tree.getSelectionPath();
			   if (path == null) return;
			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
			   if (selectedNode.getAllowsChildren() == false)
			   {
				  SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject();
				  String string_name  = treeNode.getComponentName();
				  String string_value = treeNode.getStringValue();
				  Integer component_id = treeNode.getComponentId();
				  Integer pattern_component_id = treeNode.getPatternComponentId();

				  tree.update(tree.getGraphics()); // needed to highlight selection

                  try
                  {
                     SBSPopupEditor viewer = new SBSPopupEditor(conn, song_id, song_name, string_name, string_value, "Save", "Quit");
                     viewer.setVisible(true);
                     while (viewer.getSelection() == 0)
                     {
						try
						{
                           drill_string_value = viewer.getStringValue();
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
						   Messages.exceptionHandler(frame, "Popup Viewer", e2);
						   viewer.setVisible(true);
						}
				     }
					 if (viewer.getSelection() == 0)
				     {
						if (component_id == null)
						{
                           if (session_user == null)
                           {
                              updateAnonymousString.setString(1, drill_string_value);
                              updateAnonymousString.setInt(2, pattern_component_id.intValue());
                              updateAnonymousString.execute();
                              conn.commit();
						   }
						   else
						   {
                              cSupdateAnonymousString.setString(1, drill_string_value);
                              cSupdateAnonymousString.setInt(2, pattern_component_id.intValue());
                              cSupdateAnonymousString.setString(3, session_user);
                              cSupdateAnonymousString.setString(4, session_password);
                              cSupdateAnonymousString.execute();

	   		                  rset = cSupdateAnonymousString.getResultSet();

	   	                      while (rset.next()) returned_user_id = Integer.valueOf(rset.getInt(1));

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
						else
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

	   	                      while (rset.next()) returned_user_id = Integer.valueOf(rset.getInt(1));

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

                        // need to update label!
                        //treeNode.setStringValue(drill_string_value);
                        //selectedNode.setUserObject(treeNode);
                        //treeModel.nodeChanged(selectedNode);

                        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
                        walk(treeModel, root, pattern_component_id, component_id);
				     }
				     viewer.dispose();
					 tree.requestFocusInWindow();
                  }
                  catch (Exception e)
                  {
                     Messages.exceptionHandler(frame, title, e);
                  }
			   }
			}
	     }
		 );

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel newPanel = new JPanel();
      newPanel.setLayout(new BorderLayout());
      newPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
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

      //Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      //Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 15);

      //JLabel labelField = new JLabel("Song: ");
      //labelField.setFont(labelFont);
      //titlePanel.add(labelField);

      //JLabel titleField = new JLabel(song_name);
      //titleField.setEditable(false);
      //titleField.setFont(titleFont);
      //Dimension dim1 = titleField.getPreferredSize();
      //dim1.width = 400;
      //titleField.setPreferredSize(dim1);
      //titlePanel.add(titleField);
      //titlePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,0));
      contentPane.add(newPanel, BorderLayout.NORTH);

      ScrollPane treePane = new ScrollPane(tree);
      treePane.setBorder(BorderFactory.createCompoundBorder(
                         BorderFactory.createEmptyBorder(0,20,0,20),
                         treePane.getBorder()));
      contentPane.add(treePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton refreshButton = new JButton("Refresh");
      JButton componentsButton = new JButton("Components");
      JButton playButton    = new JButton("Play");
      JButton quitButton    = new JButton("Quit");

      //buttonPanel.add(refreshButton);
      buttonPanel.add(componentsButton);
      buttonPanel.add(playButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      RefreshAction refreshAction = new RefreshAction();
      ComponentsAction componentsAction = new ComponentsAction();
      PlayAction playAction       = new PlayAction();
      QuitAction quitAction       = new QuitAction();

      refreshButton.addActionListener(refreshAction);
      componentsButton.addActionListener(componentsAction);
      playButton.addActionListener(playAction);
      quitButton.addActionListener(quitAction);

      pleaseWait = new JDialog(frame, "Please wait...", false);
      pleaseWait.setSize(200,0);
      pleaseWait.setLocation(300,204);
   }

   protected void walk(SBSTreeModel model, DefaultMutableTreeNode n, Integer pattern_component_id, Integer component_id)
   {
	   int count = model.getChildCount(n);
	   for (int i = 0; i < count;  i++)
	   {
		   DefaultMutableTreeNode child = (DefaultMutableTreeNode)model.getChild(n, i);
		   if (model.isLeaf(child))
		   {
			   SBSTreeNode leafNode = (SBSTreeNode)child.getUserObject();
			   Integer leaf_component_id = leafNode.getComponentId();
			   Integer leaf_pattern_component_id = leafNode.getPatternComponentId();

               if (component_id == null)
               {
				   if (leaf_pattern_component_id.intValue() == pattern_component_id.intValue())
				   {
                      leafNode.setStringValue(drill_string_value);
                      child.setUserObject(leafNode);
                      model.nodeChanged(child);
				   }
			   }
			   else
			   {
				   if ((leaf_component_id != null) && (leaf_component_id.intValue() == component_id.intValue()))
				   {
                      leafNode.setStringValue(drill_string_value);
                      child.setUserObject(leafNode);
                      model.nodeChanged(child);
				   }
			   }
		   }
		   else walk(model, child, pattern_component_id, component_id);
	   }
   }

   private class RefreshAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         try
         {
            treeModel.update();
            conn.commit();
			}
			catch (Exception e)
			{
	      	Messages.exceptionHandler(frame, title, e);
	      }
      }
   }

   private class ComponentsAction implements ActionListener
   {
		public void actionPerformed(ActionEvent a)
		{
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   tree.scrollPathToVisible(path); ///////////////////

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {
					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

					//System.out.println(tree.getVisibleRowCount());
					//System.out.println(tree.getRowCount());

                  try
                  {
                     SBSPatternComponents patternComponents = new SBSPatternComponents(frame, childCountField,
                                                              conn, connection_name, song_id, song_name, component_id, component_name,
                                                              session_user, session_password);
                     patternComponents.setVisible(true);
                     childCount++;

                     tree.requestFocusInWindow();
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
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
		}
	}

   private class PlayAction implements ActionListener
   {
		public void actionPerformed(ActionEvent a)
		{
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   tree.scrollPathToVisible(path); ///////////////////

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {
					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

					//System.out.println(tree.getVisibleRowCount());
					//System.out.println(tree.getRowCount());

                  try
                  {
                     pattern = new Pattern();

                     ArrayList<Integer> ancestors = new ArrayList<Integer>();
                     ancestors.add(component_id);
                     addStrings(component_id, component_name, ancestors);
                     conn.commit();

                        SBSChangePitch changePitch = new SBSChangePitch();
							   changePitch.setVisible(true);
								String pitch = changePitch.getPitch();
	    		            changePitch.dispose();

                        if (Integer.parseInt(pitch) != 0)
                        {
	    		            IntervalPatternTransformer patternTransformer = new IntervalPatternTransformer(Integer.parseInt(pitch));
								pattern = patternTransformer.transform(pattern);
								String patternString = pattern.toString();
								patternString = patternString.replaceAll("[Kk][a-zA-Z#]* "," ");
								//System.out.println(patternString); //for testing
								pattern = new Pattern(patternString);
							   }

                     SBSPlayerThread pt = new SBSPlayerThread(conn, numeric_duration_type, soundbank_id, pattern, frame);
                     pt.start();

                     tree.requestFocusInWindow();
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
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
		}
	}

   private void addStrings(Integer pattern_id, String pattern_name, ArrayList<Integer> ancestors) throws Exception
   {
		Integer component_id;
		String  component_type;
		String  component_name;
		String  string_value;
		String  anonymous_string;

      Statement stmt1 = null;
      CallableStatement cStmt1 = null;
      ResultSet rset1 = null;
      Statement stmt2 = null;
	   CallableStatement cStmt2 = null;
      ResultSet rset2 = null;

		int count = 0;

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt1 = conn.prepareCall("{call get_count_components(?)}");
			cStmt1.setInt(1, pattern_id.intValue());
			cStmt1.execute();
			rset1 = cStmt1.getResultSet();
		}
		else
		{
		   stmt1 = conn.createStatement();
		   rset1 = stmt1.executeQuery
		                     ("select count(*) " +
		                      "from sbs_pattern_component p " +
		                      "where   p.pattern_id = " + pattern_id.toString());
		}
		while (rset1.next()) count = rset1.getInt(1);
		rset1.close();
		if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt1.close(); else stmt1.close();

		if (count == 0) return;
		else
		{
         if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
         {
	   		cStmt2 = conn.prepareCall("{call get_component_values(?)}");
	   		cStmt2.setInt(1, pattern_id.intValue());
	   		cStmt2.execute();
	   		rset2 = cStmt2.getResultSet();
	   	}
	   	else
	   	{
	      	stmt2 = conn.createStatement();
	   	   rset2 = stmt2.executeQuery
	   	                     ("select p.component_id, " +
	   	                      "       c.component_type, " +
	   	                      "       c.component_name, " +
	   	                      "       c.string_value, " +
	   	                      "       p.anonymous_string " +
	   	                      "from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
	   	                      "where   p.pattern_id = " + pattern_id.toString() +
	   	                      " order by p.component_position");
			}
		   while (rset2.next())
		   {
				component_id   = Integer.valueOf(rset2.getInt(1));
				if (rset2.wasNull()) component_id = null;
				component_type = rset2.getString(2);
				component_name = rset2.getString(3);

				if (component_id != null)
				{
				for (int i = 0; i < ancestors.size(); i++)
				{
					if (component_id.equals(ancestors.get(i)))
					{
                  throw new Exception("Patterns " + pattern_name + " and " + component_name +
                                      " are ancestors of each other.");
					}
				}
			   }

				string_value   = rset2.getString(4);
				anonymous_string = rset2.getString(5);

				if (component_id == null)
				{
					component_type = "string";
					string_value = anonymous_string;
				}

				if (component_type.equals("string"))
				{
				   if (string_value != null) pattern.add(string_value);
			    }
				else
				{
					ArrayList<Integer> myAncestors = new ArrayList<Integer>(ancestors);
					myAncestors.add(component_id);
					addStrings(component_id, component_name, myAncestors);
				}
			}
			rset2.close();
		   if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt2.close(); else stmt2.close();
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
			int selection = 0;

         if (childCount > 0)
         {
            selection = Messages.warningQuestion(frame, title,
                        "OK to Quit? - Data in popup windows may not be saved.");
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

   private class ExportMidiAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   tree.scrollPathToVisible(path);

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {

					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

                  try
                  {
							FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                     JFileChooser chooser = new JFileChooser(new File("MIDI"), fsv);
                     chooser.setSelectedFile(new File(song_name + "-" + component_name + ".mid"));
                     chooser.setPreferredSize(new Dimension(600,300));
                     chooser.setDialogTitle("Export to MIDI - " + song_name + " - " + component_name);

                     int result = chooser.showDialog(frame, "Export to MIDI");
                     if (result == JFileChooser.APPROVE_OPTION)
                     {
                        File file = chooser.getSelectedFile();

                        pattern = new Pattern();

                        ArrayList<Integer> ancestors = new ArrayList<Integer>();
                        ancestors.add(component_id);
                        addStrings(component_id, component_name, ancestors);
                        conn.commit();

                        SBSChangePitch changePitch = new SBSChangePitch();
							   changePitch.setVisible(true);
								String pitch = changePitch.getPitch();
	    		            changePitch.dispose();

                        if (Integer.parseInt(pitch) != 0)
                        {
	    		            IntervalPatternTransformer patternTransformer = new IntervalPatternTransformer(Integer.parseInt(pitch));
								pattern = patternTransformer.transform(pattern);
								String patternString = pattern.toString();
								patternString = patternString.replaceAll("[Kk][a-zA-Z#]* "," ");
								pattern = new Pattern(patternString);
							   }

                        Player player = new Player();

                        if (numeric_duration_type != null && numeric_duration_type.equals("pulses"))
                        {
	                	       MusicStringParser parser = new MusicStringParser();
                                       parser.setNumeric_duration_type("pulses");
			                   player.setParser(parser);
		                  }

			               player.saveMidi(pattern, file);
                        player.close();

                        Messages.plainMessage(frame, title, "MIDI exported to: " + file.getPath());
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
            else
            {
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private class ExportMusicStringAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   tree.scrollPathToVisible(path);

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {

					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

                  try
                  {
							FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                     JFileChooser chooser = new JFileChooser(new File("MusicString"), fsv);
                     chooser.setSelectedFile(new File(song_name + "-" + component_name + ".txt"));
                     chooser.setPreferredSize(new Dimension(600,300));
                     chooser.setDialogTitle("Export to MusicString - " + song_name + " - " + component_name);

                     int result = chooser.showDialog(frame, "Export to MusicString");
                     if (result == JFileChooser.APPROVE_OPTION)
                     {
                        File file = chooser.getSelectedFile();

                        pattern = new Pattern();

                        ArrayList<Integer> ancestors = new ArrayList<Integer>();
                        ancestors.add(component_id);
                        addStrings(component_id, component_name, ancestors);
                        conn.commit();

                        SBSChangePitch changePitch = new SBSChangePitch();
							   changePitch.setVisible(true);
								String pitch = changePitch.getPitch();
	    		            changePitch.dispose();

                        if (Integer.parseInt(pitch) != 0)
                        {
	    		            IntervalPatternTransformer patternTransformer = new IntervalPatternTransformer(Integer.parseInt(pitch));
								pattern = patternTransformer.transform(pattern);
								String patternString = pattern.toString();
								patternString = patternString.replaceAll("[Kk][a-zA-Z#]* ","KCmaj ");
								pattern = new Pattern(patternString);
							   }

                        FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        String outputString = pattern.toString();
                        outputString = outputString.replace("|","|\n");

                        bufferedWriter.write(outputString);
                        bufferedWriter.newLine();

                        bufferedWriter.close();
                        fileWriter.close();

                        Messages.plainMessage(frame, title, "MusicString exported to: " + file.getPath());
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
            else
            {
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private class ExportMusicXMLAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
      /*
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   tree.scrollPathToVisible(path);

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {

					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

                  try
                  {
							FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                     JFileChooser chooser = new JFileChooser(new File("MusicXML"), fsv);
                     chooser.setSelectedFile(new File(song_name + "-" + component_name + ".xml"));
                     chooser.setPreferredSize(new Dimension(600,300));
                     chooser.setDialogTitle("Export to MusicXML - " + song_name + " - " + component_name);

                     int result = chooser.showDialog(frame, "Export to MusicXML");
                     if (result == JFileChooser.APPROVE_OPTION)
                     {
                        File fileXML = chooser.getSelectedFile();
                        FileOutputStream fosXML = new FileOutputStream(fileXML, false);

                        MusicXmlRenderer MusicXmlOut = new MusicXmlRenderer(song_name);

                        pattern = new Pattern();

                        ArrayList<Integer> ancestors = new ArrayList<Integer>();
                        ancestors.add(component_id);
                        addStrings(component_id, component_name, ancestors);
                        conn.commit();

                        SBSChangePitch changePitch = new SBSChangePitch();
							   changePitch.setVisible(true);
								String pitch = changePitch.getPitch();
	    		            changePitch.dispose();

                        if (Integer.parseInt(pitch) != 0)
                        {
	    		            IntervalPatternTransformer patternTransformer = new IntervalPatternTransformer(Integer.parseInt(pitch));
								pattern = patternTransformer.transform(pattern);
								String patternString = pattern.toString();
								patternString = patternString.replaceAll("[Kk][a-zA-Z#]* "," ");
								pattern = new Pattern(patternString);
							   }

                        if (numeric_duration_type != null && numeric_duration_type.equals("pulses"))
                        {
	                	      MusicStringParser MusicStringIn = new MusicStringParser();
                                      MusicStringIn.setNumeric_duration_type("pulses");
		                     MusicStringIn.addParserListener(MusicXmlOut);
		                     //MusicStringIn.parse(octaveLowerSong);
		                     MusicStringIn.parse(pattern);
		                  }
		                  else
		                  {
	                	      MusicStringParser MusicStringIn = new MusicStringParser();
			                  MusicStringIn.addParserListener(MusicXmlOut);
			                  //MusicStringIn.parse(octaveLowerSong);
			                  MusicStringIn.parse(pattern);
							   }

                        Serializer ser = new Serializer(fosXML, "UTF-8");
                        ser.setIndent(4);
                        ser.write(MusicXmlOut.getMusicXMLDoc());
                        fosXML.flush();
                        fosXML.close();

                        Messages.plainMessage(frame, title, "MusicXML exported to: " + fileXML.getPath());
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
            else
            {
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      */
      }
   }

   private class ExportWavAction implements ActionListener
   {
      private int pad;

      public ExportWavAction(int aPad)
      {
	 pad = aPad;
      }

      public void actionPerformed(ActionEvent a)
      {
         soundbank = null;
         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
	    tree.scrollPathToVisible(path);

	    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
	    if (selectedNode.getAllowsChildren() == true)
            {
	       SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject();

	       Integer pattern_id = treeNode.getComponentId();
	       String pattern_name = treeNode.getComponentName();

                  try
                  {
                     FileSystemView fsv = new SingleRootFileSystemView(new File("."));
                     JFileChooser chooser1 = new JFileChooser(new File("WAV"), fsv);
                     chooser1.setSelectedFile(new File(song_name + "-" + pattern_name + ".wav"));
                     chooser1.setPreferredSize(new Dimension(600,300));
                     chooser1.setDialogTitle("Export to WAV - " + song_name + " - " + pattern_name);

                     int result1 = chooser1.showDialog(frame, "Export to WAV");
                     if (result1 == JFileChooser.APPROVE_OPTION)
                     {
                        File fileOut = chooser1.getSelectedFile();

                        pattern = new Pattern();

                        ArrayList<Integer> ancestors = new ArrayList<Integer>();
                        ancestors.add(pattern_id);
                        addStrings(pattern_id, pattern_name, ancestors);
                        conn.commit();

                        SBSChangePitch changePitch = new SBSChangePitch();
							   changePitch.setVisible(true);
								String pitch = changePitch.getPitch();
	    		            changePitch.dispose();

                        if (Integer.parseInt(pitch) != 0)
                        {
	    		            IntervalPatternTransformer patternTransformer = new IntervalPatternTransformer(Integer.parseInt(pitch));
								pattern = patternTransformer.transform(pattern);
								String patternString = pattern.toString();
								patternString = patternString.replaceAll("[Kk][a-zA-Z#]* "," ");
								pattern = new Pattern(patternString);
							   }

			if (soundbank_id != null)
			{
			   pleaseWait.setVisible(true);

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
			      //soundbank = MidiSystem.getSoundbank(inputStream);

			      try
			      {
			         soundbank = new SF2Soundbank(inputStream); ///////////////////////////////
			      }
			      catch (Exception es)
			      {
					 soundbank = new DLSSoundbank(inputStream); ///////////////////////////////
				  }

	                      inputStream.close();
	                      conn.commit();
		           }
		           else
		           {
                              conn.commit();
                              pleaseWait.setVisible(false);
                              Messages.warningMessage(frame, title, "Soundbank not loaded - default soundbank will be used.");
			   }

		        }
		        else
		        {
	                   String[] soundbank_options = {"Default Soundbank", "Soundbank from File"};

	                   //JOptionPane optionPane = new JOptionPane();

	                   int soundbank_choice = JOptionPane.showOptionDialog(frame,
	                              "No soundbank attached to song.\n\n" +
	                              "Choose the Default Soundbank or a Soundbank from File.",
	                               title,
	                               JOptionPane.YES_NO_OPTION,
	                               JOptionPane.PLAIN_MESSAGE,
	                               null,
	                               soundbank_options,
	                               null);

	                   if (soundbank_choice != JOptionPane.CLOSED_OPTION)
	                   {
	                      if (soundbank_choice == 0)
	                      {
				 pleaseWait.setVisible(true);
			      }
			      else
			      {
				 FileSystemView fsv2 = new SingleRootFileSystemView(new File("."));
                                 JFileChooser chooser = new JFileChooser("SF2", fsv2);
                                 chooser.setPreferredSize(new Dimension(600,300));
                                 chooser.setDialogTitle("Get Soundbank from File");

                                 FileNameExtensionFilter filter = new FileNameExtensionFilter("Soundbank files *.sf2 and *.dls", "sf2", "dls");
                                 chooser.addChoosableFileFilter(filter);
                                 chooser.setFileFilter(filter);

                                 int result = chooser.showDialog(frame, "Get Soundbank"); ///////////////
                                 if (result == JFileChooser.APPROVE_OPTION)
                                 {
                                    File file = chooser.getSelectedFile();

                                    if (file.exists())
                                    {
				       pleaseWait.setVisible(true);

			      	       //soundbank = MidiSystem.getSoundbank(file);

			      try
			      {
			         soundbank = new SF2Soundbank(file); ///////////////////////////////
			      }
			      catch (Exception es)
			      {
					 soundbank = new DLSSoundbank(file); ///////////////////////////////
				  }

                                    }
                                    else
                                    {
                                       throw new Exception("Soundbank not found.");
                                    }
                                 }
                                 else
                                 {
				    throw new Exception("Get Soundbank Aborted.");
				 }
			     }
			   }
			   else
			   {
			      throw new Exception("Export to WAV Aborted.");
			   }
	    	        }

                        pleaseWait.setVisible(true);

                        Midi2WavRenderer renderer = new Midi2WavRenderer(pad);
                        renderer.createWavFile(pattern, numeric_duration_type, soundbank, fileOut);

                        pleaseWait.setVisible(false);

                        Messages.plainMessage(frame, title, "WAV exported to: " + fileOut.getPath());
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
            else
            {
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }

   private class CloneAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         Statement stmt2 = null;
	      CallableStatement cStmt2 = null;
         ResultSet rset2 = null;

         TreePath path = tree.getSelectionPath();
         if (path != null)
         {
			   //tree.scrollPathToVisible(path); /////// may not need this

			   DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getAllowsChildren() == true)
            {

					SBSTreeNode treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					Integer component_id = treeNode.getComponentId();
					String component_name = treeNode.getComponentName();

                  try
                  {
                     pattern_to_be_cloned_id = component_id;
                     clone_pattern_name = component_name + " (clone)";

		            	if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
		            	{
                        if (session_user == null)
                        {
                           insertStmt.setInt(1,song_id.intValue());
                           insertStmt.setString(2,clone_pattern_name);
                           insertStmt.execute();

                           rset = insertStmt.getGeneratedKeys();
                           while (rset.next()) clone_pattern_id = Integer.valueOf(rset.getInt(1));
                           rset.close();
								}
								else
								{
                           cSinsertStmt.setInt(1, song_id.intValue());
                           cSinsertStmt.setString(2, "pattern");
                           cSinsertStmt.setString(3, clone_pattern_name);
                           cSinsertStmt.setNull(4, Types.VARCHAR);
                           cSinsertStmt.setString(5, session_user);
                           cSinsertStmt.setString(6, session_password);

                           cSinsertStmt.execute();

				               rset = cSinsertStmt.getResultSet();

				               while (rset.next()) clone_pattern_id = Integer.valueOf(rset.getInt(1));

				               if (clone_pattern_id.intValue() == -1)
				               {
				             		rset.close();
					             	throw new Exception("You can only update your own or shared songs.");
				             	}

                           rset.close();
								}
		            	}
	            		else
                     {
                     rset = sequenceStmt.executeQuery();
                     while (rset.next()) clone_pattern_id = Integer.valueOf(rset.getInt(1));
                     rset.close();

                     insertStmt.setInt(1,clone_pattern_id.intValue());
                     insertStmt.setInt(2,song_id.intValue());
                     insertStmt.setString(3,clone_pattern_name);
                     insertStmt.execute();
					   	}

                     if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
                     {
		   	            cStmt2 = conn.prepareCall("{call get_cloned_components(?)}");
		   	            cStmt2.setInt(1, pattern_to_be_cloned_id.intValue());
		   	            cStmt2.execute();
		   	            rset2 = cStmt2.getResultSet();
		               }
		               else
		               {
	   	               stmt2 = conn.createStatement();
		                  rset2 = stmt2.executeQuery
		                        ("select p.component_position, " +
		                         "       p.component_id, " +
		                         "       p.anonymous_string " +
		                         "from sbs_pattern_component p " +
		                         "where   p.pattern_id = " + pattern_to_be_cloned_id.toString());
							}

		               while (rset2.next())
		               {
			            	component_position   = Integer.valueOf(rset2.getInt(1));
				            if (rset2.wasNull()) component_position = null;
			            	component_id         = Integer.valueOf(rset2.getInt(2));
				            if (rset2.wasNull()) component_id = null;
				            anonymous_string     = rset2.getString(3);

		            	if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
		            	{
                        if (session_user == null)
                        {
                           insertPatternComponentStmt.setInt(1,clone_pattern_id.intValue());

                           if (component_position != null)
                              insertPatternComponentStmt.setInt(2, component_position.intValue());
                           else
                              insertPatternComponentStmt.setNull(2, Types.INTEGER);

                           if (component_id != null)
                              insertPatternComponentStmt.setInt(3, component_id.intValue());
                           else
                              insertPatternComponentStmt.setNull(3, Types.INTEGER);

                           insertPatternComponentStmt.setString(4,anonymous_string);
                           insertPatternComponentStmt.execute();

                           rset = insertPatternComponentStmt.getGeneratedKeys();
                           while (rset.next()) pattern_component_id = Integer.valueOf(rset.getInt(1));
                           rset.close();
								}
								else
								{
                           cSinsertPatternComponentStmt.setInt(1, clone_pattern_id.intValue());

                           if (component_position != null)
                              cSinsertPatternComponentStmt.setInt(2, component_position.intValue());
                           else
                              cSinsertPatternComponentStmt.setNull(2, Types.INTEGER);

                           if (component_id != null)
                              cSinsertPatternComponentStmt.setInt(3, component_id.intValue());
                           else
                              cSinsertPatternComponentStmt.setNull(3, Types.INTEGER);

                           cSinsertPatternComponentStmt.setString(4, anonymous_string);
                           cSinsertPatternComponentStmt.setString(5, session_user);
                           cSinsertPatternComponentStmt.setString(6, session_password);

                           cSinsertPatternComponentStmt.execute();

				               rset = cSinsertPatternComponentStmt.getResultSet();

				               while (rset.next()) pattern_component_id = Integer.valueOf(rset.getInt(1));

				               if (pattern_component_id.intValue() == -1)
				               {
				             		rset.close();
					             	throw new Exception("You can only update your own or shared songs.");
				             	}

                           rset.close();
								}
		            	}
	            		else
                     {

                        rset = sequencePatternComponentStmt.executeQuery();
                        while (rset.next()) pattern_component_id = Integer.valueOf(rset.getInt(1));
                        rset.close();

                        insertPatternComponentStmt.setInt(1,pattern_component_id.intValue());
                        insertPatternComponentStmt.setInt(2,clone_pattern_id.intValue());

                        if (component_position != null)
                           insertPatternComponentStmt.setInt(3, component_position.intValue());
                        else
                           insertPatternComponentStmt.setNull(3, Types.INTEGER);

                        if (component_id != null)
                           insertPatternComponentStmt.setInt(4, component_id.intValue());
                        else
                           insertPatternComponentStmt.setNull(4, Types.INTEGER);

                        insertPatternComponentStmt.setString(5,anonymous_string);
                        insertPatternComponentStmt.execute();
							}
	            		}
	            		rset2.close();
		               if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt2.close(); else stmt2.close();

                     treeModel.update(); ////////////////
                     conn.commit();

                     if (clone_pattern_id != null)
                     {
								for (int i=0; i<tree.getRowCount(); i++)
								{
                           path = tree.getPathForRow(i);
                           if (path != null)
                           {
										selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
					               treeNode = (SBSTreeNode)selectedNode.getUserObject(); ///////////////////

					               component_id = treeNode.getComponentId();
					               if (component_id.equals(clone_pattern_id))
					               {
											tree.setSelectionPath(path);
											tree.scrollPathToVisible(path);
											break;
										}
									}

//									selectedRow = i;
//									pattern_id = (Integer)tableModel.getValueAt(i,1);
//									if (pattern_id.equals(clone_pattern_id))
//									{
//										tableField.changeSelection(selectedRow,0,false,false);
//										break;
//									}
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
                     Messages.exceptionHandler(frame, title, e);
                  }
            }
            else
            {
               Messages.plainMessage(frame, title, "Pattern not selected.");
            }
         }
         else
         {
            Messages.plainMessage(frame, title, "No Selection made.");
         }
      }
   }
}
