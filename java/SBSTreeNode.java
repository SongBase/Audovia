/*
 * SBSTreeNode.java - Tree Node Object
 * Copyright (C) 2011, 2016  Donald G Gray
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

public class SBSTreeNode
{
	/*
	 * version 3.3.1
	 *
	 */

   private String component_name;
   private Integer component_position;
   private String string_value;
   private Integer component_id;
   private Integer pattern_component_id;

   public SBSTreeNode(String aComponentName,
                      Integer aComponentPosition,
                      String aStringValue,
                      Integer aComponentId,
                      Integer aPatternComponentId)
   {
		component_name     = aComponentName;
		component_position = aComponentPosition;
		string_value       = aStringValue;
		component_id         = aComponentId;
		pattern_component_id = aPatternComponentId;
	}

	public String toString()
	{
		if (component_position == null)
		{
			return component_name;
		}
		else
		{
	   	if (string_value == null)
		   {
		      return "<html>" + component_position.toString() + ":&nbsp;&nbsp;" + component_name + "</html>";
	      }
	      else
	      {
		      String content = string_value.replaceAll("\n", "&nbsp;&nbsp;");
		      return "<html>" + component_position.toString() + ":&nbsp;&nbsp;" + component_name + ":" + "<font face=\"Liberation Mono\" style=\"font-size: 14pt;\">&nbsp;&nbsp;" + content + "</font></html>";
	      }
		}
	}

   public String getComponentName()
   {
		return component_name;
	}

	public String getStringValue()
	{
		return string_value;
	}

	public void setStringValue(String s)
	{
		string_value = s;
	}

	public Integer getComponentId()
	{
		return component_id;
	}

	public Integer getPatternComponentId()
	{
		return pattern_component_id;
	}
}
