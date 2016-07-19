/*
 * SBSPlaySamples.java - Play soundbank samples
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
import javax.sound.midi.*;

public class SBSPlaySamples extends JDialog
{
	/*
	 * version 2.2.22
	 *
	 */

   private JDialog frame = SBSPlaySamples.this;

   private JTable tableField;

   private Connection conn;
   private Integer soundbank_id;

   private Statement stmt;
   private ResultSet rset;

   private int selectedRow;
   private int selectedCol;

   private String instrument;
   private int bank;
   private int number;

   private Soundbank soundbank;
   private Synthesizer synthesizer;
   private Player player;

   private JComboBox<String> titleField;
   private String sample_string;
   private String pattern;

   private SBSPlaySamplesTableModel tableModel;
   private String title = "SBSPlaySamples";

   public SBSPlaySamples(Connection aConnection,
                         Soundbank aSoundbank,
                         String aSoundbankName) throws Exception
   {
      setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  // updated 6 nov 2011
      addWindowListener(new
         WindowAdapter()
         {
				public void windowClosing(WindowEvent e)
				{
					quit();
				}
			});

      setModal(true);
      setSize(620,410);
      //setLocation(100,100);
      setLocation(125,125);
      setTitle(title + " - Soundbank: " + aSoundbankName);

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      conn = aConnection;
      soundbank = aSoundbank;

      synthesizer = MidiSystem.getSynthesizer();
      synthesizer.open();
      synthesizer.loadAllInstruments(soundbank);
      tableModel = new SBSPlaySamplesTableModel(conn, soundbank);
      tableModel.query();

      Container contentPane = getContentPane();
      contentPane.setLayout(new BorderLayout());

      JPanel titlePanel = new JPanel();
      titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      Font titleFont = new Font("Liberation Sans", Font.BOLD, 15);
      Font labelFont = new Font("Liberation Sans", Font.PLAIN+Font.ITALIC, 14);
      Font stringFont = new Font("Liberation Sans", Font.PLAIN, 14);

      JLabel labelField = new JLabel("Sample: ");
      labelField.setFont(labelFont);
      titlePanel.add(labelField);

      titleField = new JComboBox<String>();
      titleField.setEditable(true);
      titleField.setFont(stringFont);

      titleField.addItem("");
      titleField.addItem("[0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11]");
      titleField.addItem("[12] [13] [14] [15] [16] [17] [18] [19] [20] [21] [22] [23]");
      titleField.addItem("[24] [25] [26] [27] [28] [29] [30] [31] [32] [33] [34] [35]");
      titleField.addItem("[36] [37] [38] [39] [40] [41] [42] [43] [44] [45] [46] [47]");
      titleField.addItem("[48] [49] [50] [51] [52] [53] [54] [55] [56] [57] [58] [59]");
      titleField.addItem("[60] [61] [62] [63] [64] [65] [66] [67] [68] [69] [70] [71]");
      titleField.addItem("[72] [73] [74] [75] [76] [77] [78] [79] [80] [81] [82] [83]");
      titleField.addItem("[84] [85] [86] [87] [88] [89] [90] [91] [92] [93] [94] [95]");
      titleField.addItem("[96] [97] [98] [99] [100] [101] [102] [103] [104] [105] [106] [107]");
      titleField.addItem("[108] [109] [110] [111] [112] [113] [114] [115] [116] [117] [118] [119]");
      titleField.addItem("[120] [121] [122] [123] [124] [125] [126] [127]");

      titlePanel.add(titleField);
      titlePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,0));
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

      column0.setPreferredWidth(400);

      column0.setCellRenderer(new TextFieldRenderer());
      column1.setCellRenderer(new IntegerFieldRenderer());
      column2.setCellRenderer(new IntegerFieldRenderer());

      tableField.getSelectionModel().setSelectionMode
         (ListSelectionModel.SINGLE_SELECTION);
      ScrollPane tablePane = new ScrollPane(tableField);
      tablePane.setBorder(BorderFactory.createCompoundBorder(
                          BorderFactory.createEmptyBorder(0,20,0,20),
                          tablePane.getBorder()));
      contentPane.add(tablePane, BorderLayout.CENTER);

      JPanel buttonPanel   = new JPanel();

      JButton instrumentButton = new JButton("Play Instrument");
      JButton percussionButton = new JButton("Play Percussion");
      JButton quitButton       = new JButton("Quit");

      buttonPanel.add(instrumentButton);
      buttonPanel.add(percussionButton);
      buttonPanel.add(quitButton);

      buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
      contentPane.add(buttonPanel, BorderLayout.SOUTH);

      InstrumentAction instrumentAction = new InstrumentAction();
      PercussionAction percussionAction = new PercussionAction();
      QuitAction quitAction             = new QuitAction();

      instrumentButton.addActionListener(instrumentAction);
      percussionButton.addActionListener(percussionAction);
      quitButton.addActionListener(quitAction);
   }

   private class InstrumentAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
				try
				{
               tableField.changeSelection(selectedRow, selectedCol, false, false);
               int i = selectedRow;
               instrument = (String)tableModel.getValueAt(i,0);
               bank       = (Integer)tableModel.getValueAt(i,1);
               number     = (Integer)tableModel.getValueAt(i,2);

               sample_string = (String)titleField.getSelectedItem();

               player = new Player(synthesizer, frame);

               pattern = "V0 I0 I1" +
                         " I" + Integer.toString(number) +
                         " X[Bank_Select]=" + Integer.toString(bank) +
                         " " + sample_string;

               player.play(pattern);
               player.close();
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

   private class PercussionAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         selectedRow = tableField.getSelectedRow();
         selectedCol = tableField.getSelectedColumn();
         if (selectedRow >= 0)
         {
				try
				{
               tableField.changeSelection(selectedRow, selectedCol, false, false);
               int i = selectedRow;
               instrument = (String)tableModel.getValueAt(i,0);
               bank       = (Integer)tableModel.getValueAt(i,1);
               number     = (Integer)tableModel.getValueAt(i,2);

               sample_string = (String)titleField.getSelectedItem();

               player = new Player(synthesizer, frame);

               pattern = "V9 I0 I1" +
                         " I" + Integer.toString(number) +
                         " X[Bank_Select]=" + Integer.toString(bank) +
                         " " + sample_string;

               player.play(pattern);
               player.close();
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

   private void quit()
   {
			synthesizer.close();
         dispose();
	}

   private class QuitAction implements ActionListener
   {
      public void actionPerformed(ActionEvent a)
      {
         quit();
      }
   }
}
