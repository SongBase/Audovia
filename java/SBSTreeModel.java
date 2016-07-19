/*
 * SBSTreeModel.java - Tree Model
 * Copyright (C) 2011, 2012, 2015  Donald G Gray
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
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.lang.reflect.*;
import java.sql.*;

import org.jfugue.*;

public class SBSTreeModel extends DefaultTreeModel
{
	/*
	 * version 3.0.0
	 *
	 */

   private Connection conn;
   private Statement stmt;
   private CallableStatement cStmt;
   private ResultSet rset;

   private Integer song_id;
   private Integer pattern_id;
   private String  pattern_name;
   private DefaultMutableTreeNode root;

   public SBSTreeModel(Connection aConnection,
                       Integer aSongId,
                       DefaultMutableTreeNode aRoot) throws Exception
   {
		super(aRoot);
		conn = aConnection;
		song_id = aSongId;
		root = aRoot;
	}

	void update() throws Exception
	{
		root.removeAllChildren();

      if (conn.getMetaData().getDatabaseProductName().equals("MySQL"))
      {
			cStmt = conn.prepareCall("{call get_top_level_patterns(?)}");
			cStmt.setInt(1, song_id.intValue());
			cStmt.execute();
			rset = cStmt.getResultSet();
		}
		else
		{
         stmt = conn.createStatement();
         rset = stmt.executeQuery
                ("select component_id, component_name " +
                 "from sbs_component " +
                 "where song_id = " + song_id.toString() +
                 " and  component_type = 'pattern' " +
                 " and  component_id not in (select component_id from sbs_pattern_component where component_id is not null) " +
                 " order by upper(component_name)");
		}
      while (rset.next())
      {
         pattern_id    = new Integer(rset.getInt(1));
         pattern_name  = rset.getString(2);

         SBSTreeNode treeNode = new SBSTreeNode(pattern_name, null, null, pattern_id, null);

         DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeNode); //// needs to be SBSTreeNode
         node.setAllowsChildren(true);
         root.add(node);

         ArrayList<Integer> ancestors = new ArrayList<Integer>();
         ancestors.add(pattern_id);
         addNodes(node, pattern_id, pattern_name, ancestors);
      }
      rset.close();
      if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt.close(); else stmt.close();

		reload();
	}

   private void addNodes(DefaultMutableTreeNode parentNode, Integer pattern_id, String pattern_name, ArrayList<Integer> ancestors) throws Exception
   {
		Integer pattern_component_id;
		Integer component_id;
		String  component_type;
		String  component_name;
		Integer component_position;
		String  string_value;
		String  anonymous_string;
		SBSTreeNode treeNode;

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
		   	cStmt2 = conn.prepareCall("{call get_tree_components(?)}");
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
		                      "       p.component_position, " +
		                      "       p.anonymous_string, " +
		                      "       p.pattern_component_id " +
		                      "from sbs_pattern_component p left join sbs_component c on p.component_id = c.component_id " +
		                      "where   p.pattern_id = " + pattern_id.toString() +
		                      " order by p.component_position");
			}
		   while (rset2.next())
		   {
				component_id   = new Integer(rset2.getInt(1));
				if (rset2.wasNull()) component_id = null;
				component_type = rset2.getString(2);
				component_name = rset2.getString(3);
				string_value   = rset2.getString(4);
				component_position = new Integer(rset2.getInt(5));
				anonymous_string   = rset2.getString(6);
				pattern_component_id = new Integer(rset2.getInt(7));

			   if (component_id == null)
				{
				   component_type = "string";
					component_name = "";
					string_value = anonymous_string;
					treeNode = new SBSTreeNode(component_name, component_position, string_value, component_id, pattern_component_id);
				}
				else
				{
			   	treeNode = new SBSTreeNode(component_name, component_position, string_value, component_id, pattern_component_id);
			   }

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

				if (component_type.equals("string"))
				{
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeNode);
					node.setAllowsChildren(false);

					parentNode.add(node);
				}
				else
				{
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(treeNode);
					node.setAllowsChildren(true);

					parentNode.add(node);

					ArrayList<Integer> myAncestors = new ArrayList<Integer>(ancestors);
					myAncestors.add(component_id);
					addNodes(node, component_id, component_name, myAncestors);
				}
			}
			rset2.close();
		   if (conn.getMetaData().getDatabaseProductName().equals("MySQL")) cStmt2.close(); else stmt2.close();
		}
	}
}
