/*
 * InstrumentFrame.java
 * Copyright (C) 2011  Donald G Gray
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

import javax.swing.*;

public class InstrumentFrame extends JFrame
{
	/*
	 * version 1.1.13
	 *
	 */

   private static final long serialVersionUID = 1L;

	public InstrumentFrame(String soundbank_name, JTextArea output)
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450,400);
      //setLocation(100,100);
      setLocation(125,125);
		setTitle("Soundbank: " + soundbank_name);

      ImageIcon icon = new ImageIcon(System.getProperty("image.dir") + "/" + "SongBuilderColourIcon64.png");
      setIconImage(icon.getImage());

      output.setEditable(false);
		getContentPane().add(new JScrollPane(output));
	}
}