/*
 * org.openmicroscopy.shoola.agents.datamng.editors.DatasetGeneralPane
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.datamng.editors;

//Java imports
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.datamng.DataManager;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.model.DatasetData;
import org.openmicroscopy.shoola.util.ui.TableComponent;
import org.openmicroscopy.shoola.util.ui.TableComponentCellEditor;
import org.openmicroscopy.shoola.util.ui.TableComponentCellRenderer;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class DatasetGeneralPane
	extends JPanel
{

	private static final Dimension	H_DIM = new Dimension(10, 0);
	
	private DatasetEditorManager	manager;
	
	private Registry				registry;
	
	private JButton					saveButton, reloadButton, annotateButton;
	
	private JTextArea				nameField;
	
	private JTextArea				descriptionArea;
	
	DatasetGeneralPane(DatasetEditorManager manager, Registry registry)
	{
		this.manager = manager;
		this.registry = registry;
		buildGUI();
	}
	
	/** Returns the annotate button. */
	JButton getAnnotateButton()
	{
		return annotateButton;
	}
	
	/** Returns the save button. */
	JButton getSaveButton()
	{ 
		return saveButton;
	}

	/** Returns the reload button. */
	JButton getReloadButton()
	{ 
		return reloadButton;
	}
	
	/** Returns the TextArea with the project's description. */
	JTextArea getDescriptionArea()
	{
		return descriptionArea;
	}

	/** Returns the textfield with project's name. */
	JTextArea getNameField()
	{
		return nameField;
	}

	/** Build and layout the GUI. */
	private void buildGUI()
	{
		setLayout(new GridLayout(1, 1));
		add(buildSummaryPanel());
		Border b = BorderFactory.createEmptyBorder(0, 0, 10, 10);
		setBorder(b);
	}
	
	private JPanel buildSummaryPanel() 
	{	
		JPanel  p = new JPanel();
		//save button
		saveButton = new JButton("Save");
		saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//make panel transparent
		saveButton.setOpaque(false);
		//suppress button press decoration
		saveButton.setContentAreaFilled(false); 
		saveButton.setToolTipText("Save data to the DB.");
		saveButton.setEnabled(false);
		
		//reload button
		reloadButton = new JButton("Reload");
		reloadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//make panel transparent
		reloadButton.setOpaque(false);
		//suppress button press decoration
		reloadButton.setContentAreaFilled(false); 
		reloadButton.setToolTipText("Reload data from the DB.");
		
		//annotate button
		annotateButton = new JButton("Annotate");
		annotateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  		//make panel transparent
		annotateButton.setOpaque(false);
  		//suppress button press decoration
		annotateButton.setContentAreaFilled(false); 
		annotateButton.setToolTipText("Annotate the dataset.");
		
		JPanel controls = new JPanel(), all = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		all.setLayout(gridbag);  
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		
		controls.add(annotateButton);
		controls.add(Box.createRigidArea(H_DIM));
		controls.add(reloadButton);
		controls.add(Box.createRigidArea(H_DIM));
		controls.add(saveButton);
		controls.setOpaque(false); //make panel transparent
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(controls,c); 
		all.add(controls);
		all.setOpaque(false); //make panel transparent
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(buildTable());
		p.add(all);
		//make panel transparent
		p.setOpaque(false);
		
		return p;
	}
	
	/** 
	 * A <code>3x2</code> table model to view dataset summary.
	 * The first column contains the property names (id, name, description)
	 * and the second column holds the corresponding values. 
	 * <code>name</code> and <code>description</code> values
	 * are marked as editable. 
	 */
	private JTable buildTable()
	{
		JTable table = new TableComponent(3, 2);
		table.setTableHeader(null);
		table.setRowHeight(2, DataManager.ROW_TABLE_HEIGHT);
		table.setRowHeight(1, DataManager.ROW_NAME_FIELD);
		
		// Labels
		table.setValueAt(new JLabel(" ID"), 0, 0);
		table.setValueAt(new JLabel(" Name"), 1, 0);
		table.setValueAt(new JLabel(" Description"), 2, 0);

		DatasetData pd = manager.getDatasetData();
		table.setValueAt(new JLabel(""+pd.getID()), 0, 1);
		
		//textfields
		nameField = new JTextArea(pd.getName());
		nameField.setForeground(DataManager.STEELBLUE);
		nameField.setEditable(true);
		nameField.setLineWrap(true);
		nameField.setWrapStyleWord(true);
		JScrollPane scrollPaneName  = new JScrollPane(nameField);
		scrollPaneName.setPreferredSize(DataManager.DIM_SCROLL_NAME);
		table.setValueAt(scrollPaneName, 1, 1); 

		descriptionArea = new JTextArea(pd.getDescription());
		descriptionArea.setForeground(DataManager.STEELBLUE);
		descriptionArea.setEditable(true);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		JScrollPane scrollPane  = new JScrollPane(descriptionArea);
		scrollPane.setPreferredSize(DataManager.DIM_SCROLL_TABLE);
		table.setValueAt(scrollPane, 2, 1);

		table.setDefaultRenderer(JComponent.class, 
								new TableComponentCellRenderer());
		table.setDefaultEditor(JComponent.class, 
								new TableComponentCellEditor());
								
		return table;
	}
	
}
