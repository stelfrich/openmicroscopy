/*
 * org.openmicroscopy.shoola.agents.fsimporter.chooser.ImportDialog 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2010 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.fsimporter.chooser;

//Java imports
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

//Third-party libraries
import loci.formats.gui.ComboFileFilter;
import info.clearthought.layout.TableLayout;
import org.jdesktop.swingx.JXTaskPane;

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.fsimporter.IconManager;
import org.openmicroscopy.shoola.agents.fsimporter.ImporterAgent;
import org.openmicroscopy.shoola.agents.fsimporter.view.Importer;
import org.openmicroscopy.shoola.agents.util.SelectionWizard;
import org.openmicroscopy.shoola.agents.util.ViewerSorter;
import org.openmicroscopy.shoola.agents.util.browser.TreeImageDisplay;
import org.openmicroscopy.shoola.agents.util.ui.EditorDialog;
import org.openmicroscopy.shoola.env.LookupNames;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.model.DiskQuota;
import org.openmicroscopy.shoola.env.data.model.ImportableFile;
import org.openmicroscopy.shoola.env.data.model.ImportableObject;
import org.openmicroscopy.shoola.env.rnd.RenderingControl;
import org.openmicroscopy.shoola.util.ui.ClosableTabbedPaneComponent;
import org.openmicroscopy.shoola.util.ui.NumericalTextField;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import pojos.DataObject;
import pojos.DatasetData;
import pojos.ProjectData;
import pojos.ScreenData;
import pojos.TagAnnotationData;

/** 
 * Dialog used to select the files to import.
 *
 * @author Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since 3.0-Beta4
 */
public class ImportDialog 
	extends ClosableTabbedPaneComponent//JDialog
	implements ActionListener, PropertyChangeListener
{

	/** Bound property indicating to load the tags. */
	public static final String	LOAD_TAGS_PROPERTY = "loadTags";
	
	/** Bound property indicating that the cancel button is pressed. */
	public static final String	CANCEL_SELECTION_PROPERTY = "cancelSelection";
	
	/** Bound property indicating that the cancel button is pressed. */
	public static final String	CANCEL_ALL_IMPORT_PROPERTY = "cancelAllImport";
	
	/** Bound property indicating to import the selected files. */
	public static final String	IMPORT_PROPERTY = "import";

	/** Bound property indicating to refresh the location. */
	public static final String	REFRESH_LOCATION_PROPERTY = "refreshLocation";

	/** The default text. */
	private static final String	PROJECT_TXT = "Project";
	
	/** The default text. */
	private static final String	SCREEN_TXT = "Screen";
	
	/** The default text. */
	private static final String	DATASET_TXT = "Dataset";
	
	/** Action id indicating to import the selected files. */
	private static final int	IMPORT = 0;
	
	/** Action id indicating to close the dialog. */
	private static final int	CANCEL = 1;
	
	/** Action id indicating to refresh the file view. */
	private static final int	REFRESH = 2;
	
	/** Action id indicating to reset the names. */
	private static final int	RESET = 3;
	
	/** Action id indicating to apply the partial names to all.*/
	private static final int	APPLY_TO_ALL = 4;
	
	/** Action id indicating to add tags to the file. */
	private static final int	TAG = 5;
	
	/** Action id indicating to create a new dataset. */
	private static final int	CREATE_DATASET = 6;
	
	/** Action id indicating to create a new project. */
	private static final int	CREATE_PROJECT = 7;
	
	/** Action id indicating to refresh the containers. */
	private static final int	REFRESH_LOCATION = 8;
	
	/** Action id indicating to select the Project/Dataset or Screen.*/
	private static final int	LOCATION = 9;
	
	/** Action id indicating to select the Project/Dataset or Screen.*/
	private static final int	CANCEL_ALL_IMPORT = 10;
	
	/** The title of the dialog. */
	private static final String TITLE = "Select Data to Import";
	
	/** The message to display in the header. */
	private static final String MESSAGE_LOCATION = "Select where to import " +
			"the data";

	/** Warning when de-selecting the name overriding option. */
	private static final List<String> WARNING;
	
	/** The length of a column. */
	private static final int		COLUMN_WIDTH = 200;
	
	/** String used to retrieve if the value of the archived flag. */
	private static final String LOAD_THUMBNAIL = "/options/LoadThumbnail";
	
	static {
		WARNING = new ArrayList<String>();
		WARNING.add("NOTE: Some file formats do not include the file name " +
				"in their metadata, ");
		WARNING.add("and disabling this option may result in files being " +
				"imported without a ");
		WARNING.add("reference to their file name e.g. " +
				"'myfile.lsm [image001]'");
		WARNING.add("would show up as 'image001' with this optioned " +
				"turned off.");
	}
	
	/** The approval option the user chose. */
	private int					option;

	/** The table hosting the file to import. */
	private FileSelectionTable  table;
	
	/** The file chooser. */
	private JFileChooser	    chooser;
	
	/** Button to close the dialog. */
	private JButton				cancelButton;
	
	/** Button to cancel all imports. */
	private JButton				cancelImportButton;
	
	/** Button to import the files. */
	private JButton				importButton;
	
	/** Button to import the files. */
	private JButton				refreshButton;
	
	/** Button to reload the containers where to import the files. */
	private JToggleButton		reloadContainerButton;
	
	/** Button used to select the project as the location.*/
	private JButton				projectLocationButton;
	
	/** Button used to select the screen as the location.*/
	private JButton				screenLocationButton;
	
	/** 
	 * Resets the name of all files to either the full path
	 * or the partial name if selected. 
	 */
	private JButton				resetButton;
	
	/** Apply the partial name to all files. */
	private JButton				applyToAllButton;
	
	/** Indicates to use a partial name. */
	private JRadioButton		partialName;
	
	/** Indicates to use a full name. */
	private JRadioButton		fullName;
	
	/** Button indicating to override the name if selected. */
	private JCheckBox			overrideName;
	
	/** Text field indicating how many folders to include. */
	private NumericalTextField	numberOfFolders;
	
	/** The collection of supported filters. */
	private FileFilter[]	filters;

	/** Button to bring up the tags wizard. */
	private JButton						tagButton;
	
	/** The fields hosting the pixels size. First
	 * is for the size along the X-axis, then Y-axis, finally Z-axis
	 */
	private List<NumericalTextField>	pixelsSize;
	
	/** Components hosting the tags. */
	private JPanel						tagsPane;
	
	/** Map hosting the tags. */
	private Map<JButton, TagAnnotationData> tagsMap;
	
	/** The action listener used to handle tag selection. */
	private ActionListener				listener;
	
	/** The selected container where to import the data. */
	private TreeImageDisplay		selectedContainer;
	
	/** The possible node. */
	private Collection<TreeImageDisplay> 		objects;
	
	/** The component displaying the table, options etc. */
	private JTabbedPane 				tabbedPane;

	/** The collection of datasets to use by default. */
	private List<DataNode>				datasets;
	
	/** Component used to select the default dataset. */
	private JComboBox					datasetsBox;
	
	/** Component used to select the default dataset. */
	private JComboBox					parentsBox;
	
	/** The component displaying where the data will be imported. */
	private JPanel						locationPane;
	
	/** The type associated to the import. */
	private int							type;
	
	/** Button to create a new dataset or screen. */
	private JButton						addButton;
	
	/** Button to create a new dataset or screen. */
	private JButton						addProjectButton;
	
	/** Sorts the objects from the display. */
	private ViewerSorter				sorter;
	
	/** The class of reference for the container. */
	private Class						reference;
	
	/** Indicates to show thumbnails in import tab. */
	private JCheckBox					showThumbnails;
	
	/** The listener linked to the parents box. */
	private ActionListener				parentsBoxListener;
	
	/** The collection of <code>HCS</code> filters. */
	private List<FileFilter> 			hcsFilters;
	
	/** The collection of general filters. */
	private List<FileFilter> 			generalFilters;
	
	/** The combine filter. */
	private FileFilter					combinedFilter;
	
	/** The component displaying the available and used disk space. */
	private JPanel						diskSpacePane;

	/** Displays the amount of free and used space. */
	private QuotaCanvas 				canvas;
	
	/** The size of the import. */
	private JLabel						sizeImportLabel;
	
	/** 
	 * Used to create a dataset using the folder containing the selected images. 
	 */
	private JCheckBox					folderAsDatasetBox;
	
	/** The owner related to the component. */
	private JFrame						owner;
	
	/** 
	 * Creates the dataset.
	 * 
	 * @param dataset The dataset to create.
	 */
	private void createDataset(DatasetData dataset)
	{
		if (dataset == null || dataset.getName().trim().length() == 0) return;
		List<DataNode> nodes = new ArrayList<DataNode>();
		DataNode n;
		for (int i = 0; i < datasetsBox.getItemCount(); i++) {
			n = (DataNode) datasetsBox.getItemAt(i);
			if (!n.isDefaultNode()) 
				nodes.add(n);
		}
		DataNode node = (DataNode) parentsBox.getSelectedItem();
		n = new DataNode(dataset, node);
		nodes.add(n);
		datasetsBox.removeAllItems();
		List l = sorter.sort(nodes);
		Iterator i = l.iterator();
		while (i.hasNext()) {
			datasetsBox.addItem((DataNode) i.next());
		}
		datasetsBox.setSelectedItem(n);
		repaint();
	}

	/**
	 * Creates a project.
	 * 
	 * @param project The project to create.
	 */
	private void createProject(ProjectData project)
	{
		if (project == null || project.getName().trim().length() == 0) return;
		List<DataNode> nodes = new ArrayList<DataNode>();
		DataNode n;
		DataNode defaultNode = null;
		for (int i = 0; i < parentsBox.getItemCount(); i++) {
			n = (DataNode) parentsBox.getItemAt(i);
			if (!n.isDefaultNode()) 
				nodes.add(n);
			else defaultNode = n;
		}
		n = new DataNode(project);
		n.addNode(new DataNode(DataNode.createDefaultDataset()));
		nodes.add(n);
		List l = sorter.sort(nodes);
		if (defaultNode != null) l.add(defaultNode);
		parentsBox.removeActionListener(parentsBoxListener);
		parentsBox.removeAllItems();
		parentsBox.addActionListener(parentsBoxListener);
		Iterator i = l.iterator();
		while (i.hasNext()) {
			parentsBox.addItem((DataNode) i.next());
		}
		parentsBox.setSelectedItem(n);
		repaint();
	}
	
	/**
	 * Creates a screen.
	 * 
	 * @param screen The screen to create.
	 */
	private void createScreen(ScreenData screen)
	{
		if (screen == null || screen.getName().trim().length() == 0) return;
		List<DataNode> nodes = new ArrayList<DataNode>();
		DataNode n;
		DataNode defaultNode = null;
		for (int i = 0; i < parentsBox.getItemCount(); i++) {
			n = (DataNode) parentsBox.getItemAt(i);
			if (!n.isDefaultNode()) 
				nodes.add(n);
			else defaultNode = n;
		}
		n = new DataNode(screen);
		nodes.add(n);
		List l = sorter.sort(nodes);
		if (defaultNode != null) l.add(defaultNode);
		parentsBox.removeAllItems();
		Iterator i = l.iterator();
		while (i.hasNext()) {
			parentsBox.addItem((DataNode) i.next());
		}
		parentsBox.setSelectedItem(n);
		repaint();
	}
	
	
	/** Adds the files to the selection. */
	private void addFiles()
	{
		File[] files = chooser.getSelectedFiles();
		if (files == null || files.length == 0) return;
		List<File> l = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) 
			checkFile(files[i], l);
		table.addFiles(l);
		importButton.setEnabled(table.hasFilesToImport());
	}

	/** 
	 * Handles <code>Enter</code> key pressed. 
	 * 
	 * @param source The source of the mouse pressed.
	 */
	private void handleEnterKeyPressed(Object source)
	{
		if (source instanceof JList || source instanceof JTable) {
			JComponent c = (JComponent) source;
			if (c.isFocusOwner()) addFiles();
		}
	}
	
	/**
	 * Handles the selection of tags.
	 * 
	 * @param tags The selected tags.
	 */
	private void handleTagsSelection(Collection tags)
	{
		Collection<TagAnnotationData> set = tagsMap.values();
		Map<String, TagAnnotationData> 
			newTags = new HashMap<String, TagAnnotationData>();
		TagAnnotationData tag;
		Iterator i = set.iterator();
		while (i.hasNext()) {
			tag = (TagAnnotationData) i.next();
			if (tag.getId() < 0)
				newTags.put(tag.getTagValue(), tag);
		}
		List<TagAnnotationData> toKeep = new ArrayList<TagAnnotationData>();
		i = tags.iterator();
		while (i.hasNext()) {
			tag = (TagAnnotationData) i.next();
			if (tag.getId() < 0) {
				if (!newTags.containsKey(tag.getTagValue())) {
					toKeep.add(tag);
				}
			} else toKeep.add(tag);
		}
		toKeep.addAll(newTags.values());
		
		//layout the tags
		tagsMap.clear();
		tagsPane.removeAll();
		i = toKeep.iterator();
		IconManager icons = IconManager.getInstance();
		JPanel entry;
		JPanel p = initRow();
		int width = 0;
		while (i.hasNext()) {
			tag = (TagAnnotationData) i.next();
			entry = buildTagEntry(tag, icons.getIcon(IconManager.MINUS_11));
			if (width+entry.getPreferredSize().width >= COLUMN_WIDTH) {
		    	tagsPane.add(p);
		    	p = initRow();
				width = 0;
		    } else {
		    	width += entry.getPreferredSize().width;
		    	width += 2;
		    }
			p.add(entry);
		}
		if (p.getComponentCount() > 0) tagsPane.add(p);
		tagsPane.validate();
		tagsPane.repaint();
	}
	
	/**
	 * Creates a row.
	 * 
	 * @return See above.
	 */
	private JPanel initRow()
	{
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		return p;
	}
	
	/** 
	 * Builds and lays out a tag.
	 * 
	 * @param tag The tag to display.
	 * @param icon The icon used to remove the tag from the display.
	 * @return See above.
	 */
	private JPanel buildTagEntry(TagAnnotationData tag, Icon icon)
	{
		JButton b = new JButton(icon);
		UIUtilities.unifiedButtonLookAndFeel(b);
		//add listener
		b.addActionListener(listener);
		tagsMap.put(b, tag);
		JPanel p = new JPanel();
		JLabel l = new JLabel();
		l.setText(tag.getTagValue());
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.add(l);
		p.add(b);
		return p;
	}

	/**
	 * Shows the selection wizard.
	 * 
	 * @param type			The type of objects to handle.
	 * @param available 	The available objects.
	 * @param selected  	The selected objects.
	 * @param addCreation	Pass <code>true</code> to add a component
	 * 						allowing creation of object of the passed type,
	 * 						<code>false</code> otherwise.
	 */
	private void showSelectionWizard(Class type, Collection available, 
									Collection selected, boolean addCreation)
	{
		IconManager icons = IconManager.getInstance();
		Registry reg = ImporterAgent.getRegistry();
		String title = "";
		String text = "";
		Icon icon = null;
		if (TagAnnotationData.class.equals(type)) {
			title = "Tags Selection";
			text = "Select the Tags to add or remove, \nor Create new Tags";
			icon = icons.getIcon(IconManager.TAGS_48);
		} 
		long userID = ImporterAgent.getUserDetails().getId();
		SelectionWizard wizard = new SelectionWizard(
				reg.getTaskBar().getFrame(), available, selected, type,
				addCreation, userID);
		wizard.setAcceptButtonText("Save");
		wizard.setTitle(title, text, icon);
		wizard.addPropertyChangeListener(this);
		UIUtilities.centerAndShow(wizard);
	}
	
	/** Sets the properties of the dialog. */
	private void setProperties()
	{
		/*
		setTitle(TITLE);
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        */
	}

	/** Installs the listeners. */
	private void installListeners()
	{
        //addWindowListener(new WindowAdapter() {
    		
			/** 
			 * Cancels the selection.
			 * @see WindowAdapter#windowClosing(WindowEvent)
			 */
			//public void windowClosing(WindowEvent e) { cancelSelection(); }
		
		//});
	}
	
	/** Formats the {@link #projectLocationButton}.*/
	private void formatSwitchButton()
	{
		projectLocationButton.setSelected(getType() == Importer.PROJECT_TYPE);
		screenLocationButton.setSelected(getType() == Importer.SCREEN_TYPE);
	}
	
	/**
	 * Handles the switch of containers either <code>Project</code>
	 * or <code>Screen</code>.
	 * 
	 * @param src The source component.
	 */
	private void handleLocationSwitch(Object src)
	{
		int t = Importer.PROJECT_TYPE;
		if (src == projectLocationButton) {
			if (projectLocationButton.isSelected()) {
				screenLocationButton.setSelected(true);
				projectLocationButton.setSelected(false);
				t = Importer.SCREEN_TYPE;
			} else {
				screenLocationButton.setSelected(false);
				projectLocationButton.setSelected(true);
			}
		} else if (src == screenLocationButton) {
			if (screenLocationButton.isSelected()) {
				projectLocationButton.setSelected(true);
				screenLocationButton.setSelected(false);
			} else {
				t = Importer.SCREEN_TYPE;
				projectLocationButton.setSelected(false);
				screenLocationButton.setSelected(true);
			}
		}
		firePropertyChange(REFRESH_LOCATION_PROPERTY, getType(), t);
	}
	
	/** 
	 * Initializes the components composing the display. 
	 * 
	 * @param filters The filters to handle.
	 */
	private void initComponents(FileFilter[] filters)
	{
    	folderAsDatasetBox = new JCheckBox();
    	folderAsDatasetBox.setText("New Dataset from folder's name");
    	folderAsDatasetBox.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				boolean b = !folderAsDatasetBox.isSelected();
				datasetsBox.setEnabled(b);
				addButton.setEnabled(b);
			}
		});
    	canvas = new QuotaCanvas();
		sizeImportLabel = new JLabel();
		//sizeImportLabel.setText(UIUtilities.formatFileSize(0));
		diskSpacePane = new JPanel();
		diskSpacePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		//diskSpacePane.setBackground(UIUtilities.BACKGROUND);
		diskSpacePane.add(UIUtilities.setTextFont("Space Usage"));
		diskSpacePane.add(canvas);
		
		showThumbnails = new JCheckBox("Show Thumbnails when imported");
		showThumbnails.setVisible(false);
		Boolean b = (Boolean) ImporterAgent.getRegistry().lookup(
    			LOAD_THUMBNAIL);
    	if (b != null) {
    		if (b.booleanValue()) {
    			showThumbnails.setVisible(true);
    			showThumbnails.setSelected(true);
    		}
    	}
    	if (!isFastConnection()) //slow connection
    		showThumbnails.setSelected(false);
		reference = null;
		parentsBox = new JComboBox();
		parentsBoxListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				populateDatasetsBox();
			}
		};
		parentsBox.addActionListener(parentsBoxListener);
		datasetsBox = new JComboBox();
		sorter = new ViewerSorter();
		datasets = new ArrayList<DataNode>();
		addProjectButton = new JButton("New...");
		addProjectButton.setBackground(UIUtilities.BACKGROUND);
		addProjectButton.setToolTipText("Create a new Project.");
		if (type == Importer.SCREEN_TYPE) {
			addProjectButton.setToolTipText("Create a new Screen.");
		}
		
		addProjectButton.setActionCommand(""+CREATE_PROJECT);
		addProjectButton.addActionListener(this);
		
		addButton = new JButton("New...");
		addButton.setBackground(UIUtilities.BACKGROUND);
		addButton.setToolTipText("Create a new Dataset.");
		addButton.setActionCommand(""+CREATE_DATASET);
		addButton.addActionListener(this);

		IconManager icons = IconManager.getInstance();
		reloadContainerButton = new JToggleButton(
				icons.getIcon(IconManager.REFRESH));
		reloadContainerButton.setBackground(UIUtilities.BACKGROUND);
		reloadContainerButton.setToolTipText("Reloads the container where to " +
				"import the data.");
		reloadContainerButton.setActionCommand(""+REFRESH_LOCATION);
		reloadContainerButton.addActionListener(this);
		UIUtilities.unifiedButtonLookAndFeel(reloadContainerButton);
		
		projectLocationButton = new JButton(icons.getIcon(IconManager.PROJECT));
		projectLocationButton.setBackground(UIUtilities.BACKGROUND);
		projectLocationButton.setToolTipText("Select the Project/Dataset " +
				"where to import the data.");
		
		screenLocationButton = new JButton(icons.getIcon(IconManager.SCREEN));
		screenLocationButton.setBackground(UIUtilities.BACKGROUND);
		screenLocationButton.setToolTipText("Select the Screen " +
				"where to import the data.");
		ButtonGroup group = new ButtonGroup();
		//group.add(projectLocationButton);
		//group.add(screenLocationButton);
		//UIUtilities.unifiedButtonLookAndFeel(switchLocationButton);
		formatSwitchButton();
		projectLocationButton.addActionListener(this);
		screenLocationButton.addActionListener(this);

		screenLocationButton.setActionCommand(""+LOCATION);
		projectLocationButton.setActionCommand(""+LOCATION);
		listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				if (src instanceof JButton) {
					TagAnnotationData tag = tagsMap.get(src);
					if (tag != null) {
						tagsMap.remove(src);
						handleTagsSelection(tagsMap.values());
					}
				}
			}
		};
		locationPane = new JPanel();
		locationPane.setLayout(new BoxLayout(locationPane, BoxLayout.Y_AXIS));
		locationPane.setBackground(UIUtilities.BACKGROUND);
		
		tabbedPane = new JTabbedPane();
		numberOfFolders = new NumericalTextField();
		numberOfFolders.setMinimum(0);
		numberOfFolders.setText("0");
		numberOfFolders.setColumns(3);
		//numberOfFolders.setEnabled(false);
		numberOfFolders.addPropertyChangeListener(this);
		tagsMap = new LinkedHashMap<JButton, TagAnnotationData>();

		tagButton = new JButton(icons.getIcon(IconManager.PLUS_12));
		UIUtilities.unifiedButtonLookAndFeel(tagButton);
		tagButton.addActionListener(this);
		tagButton.setActionCommand(""+TAG);
		tagButton.setToolTipText("Add Tags.");
		tagsPane = new JPanel();
		tagsPane.setLayout(new BoxLayout(tagsPane, BoxLayout.Y_AXIS));

		overrideName = new JCheckBox("Override default File naming. " +
				"Instead use");
		overrideName.setToolTipText(UIUtilities.formatToolTipText(WARNING));
		overrideName.setSelected(true);
		group = new ButtonGroup();
		fullName = new JRadioButton("Full Path+File's name");
		group.add(fullName);
		partialName = new JRadioButton();
		partialName.setText("Partial Path+File's name with");
		partialName.setSelected(true);
		group.add(partialName);

		chooser = new JFileChooser();
		JList list = (JList) UIUtilities.findComponent(chooser, JList.class);
		KeyAdapter ka = new KeyAdapter() {
			
			/**
			 * Adds the files to the import queue.
			 * @see KeyListener#keyPressed(KeyEvent)
			 */
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleEnterKeyPressed(e.getSource());
				}
			}
		};
		if (list != null) list.addKeyListener(ka);
		if (list == null) {
			JTable t = (JTable) 
				UIUtilities.findComponent(chooser, JTable.class);
			if (t != null) t.addKeyListener(ka);
		}

		try {
			File f = UIUtilities.getDefaultFolder();
			if (f != null) chooser.setCurrentDirectory(f);
		} catch (Exception e) {
			//Ignore: could not set the default container
		}
		
		chooser.addPropertyChangeListener(this);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setControlButtonsAreShown(false);
		chooser.setApproveButtonText("Import");
		chooser.setApproveButtonToolTipText("Import the selected files " +
				"or directories");
		hcsFilters = new ArrayList<FileFilter>();
		generalFilters = new ArrayList<FileFilter>();
		if (filters != null) {
			chooser.setAcceptAllFileFilterUsed(false);
			FileFilter filter;
			for (int i = 0; i < filters.length; i++) {
				filter = filters[i];
				if (filter instanceof ComboFileFilter) {
					combinedFilter = filter;
				} else {
					if (ImportableObject.isHCSFormat(filter.toString())) {
						hcsFilters.add(filter);
					} else {
						generalFilters.add(filter);
					}
				}
			}
			Iterator<FileFilter> j;
			if (type == Importer.SCREEN_TYPE) {
				j = hcsFilters.iterator();
				while (j.hasNext())
					chooser.addChoosableFileFilter(j.next());
				chooser.setFileFilter(hcsFilters.get(0));
			} else {
				chooser.addChoosableFileFilter(combinedFilter);
				j = generalFilters.iterator();
				while (j.hasNext())
					chooser.addChoosableFileFilter(j.next());
				chooser.setFileFilter(combinedFilter);
			}
			while (j.hasNext())
				chooser.addChoosableFileFilter(j.next());
			//chooser.setFileFilter(filters[0]);
		} else chooser.setAcceptAllFileFilterUsed(true);
		
		
		table = new FileSelectionTable(this);
		table.addPropertyChangeListener(this);
		cancelButton = new JButton("Close");
		cancelButton.setToolTipText("Close the dialog and do not import.");
		cancelButton.setActionCommand(""+CANCEL);
		cancelButton.addActionListener(this);
		
		cancelButton = new JButton("Close");
		cancelButton.setToolTipText("Close the dialog and do not import.");
		cancelButton.setActionCommand(""+CANCEL);
		cancelButton.addActionListener(this);
		
		cancelImportButton = new JButton("Cancel All");
		cancelImportButton.setToolTipText("Cancel all ongoing imports.");
		cancelImportButton.setActionCommand(""+CANCEL_ALL_IMPORT);
		cancelImportButton.addActionListener(this);
		
		importButton = new JButton("Import");
		importButton.setToolTipText("Import the selected files or" +
				" directories.");
		importButton.setActionCommand(""+IMPORT);
		importButton.addActionListener(this);
		importButton.setEnabled(false);
		refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("Reloads the files view.");
		refreshButton.setActionCommand(""+REFRESH);
		refreshButton.addActionListener(this);
		resetButton = new JButton("Reset");
		resetButton.setToolTipText("Reset the name of all files to either " +
				"the full path or the partial name if selected.");
		resetButton.setActionCommand(""+RESET);
		resetButton.addActionListener(this);
		applyToAllButton = new JButton("Apply Partial Name");
		applyToAllButton.setToolTipText("Apply the partial name to " +
				"all files in the queue.");
		applyToAllButton.setActionCommand(""+APPLY_TO_ALL);
		applyToAllButton.addActionListener(this);
		applyToAllButton.setEnabled(false);

		//getRootPane().setDefaultButton(cancelButton);
		
		pixelsSize = new ArrayList<NumericalTextField>();
		NumericalTextField field;
		for (int i = 0; i < 3; i++) {
			field = new NumericalTextField();
			field.setNumberType(Double.class);
			field.setColumns(2);
			pixelsSize.add(field);
		}
		initializeLocationBoxes();
		
		List<Component> boxes = 
			UIUtilities.findComponents(chooser, JComboBox.class);
		if (boxes != null) {
			JComboBox box;
			JComboBox filerBox = null;
			Iterator<Component> i = boxes.iterator();
			while (i.hasNext()) {
				box = (JComboBox) i.next();
				Object o = box.getItemAt(0);
				if (o instanceof FileFilter) {
					filerBox = box;
					break;
				}
			}
			if (filerBox != null) {
				filerBox.addKeyListener(new KeyAdapter() {
					
					public void keyPressed(KeyEvent e) {
						String value = KeyEvent.getKeyText(e.getKeyCode());
						JComboBox box = (JComboBox) e.getSource();
						int n = box.getItemCount();
						FileFilter filter;
						FileFilter selectedFilter = null;
						String d;
						for (int j = 0; j < n; j++) {
							filter = (FileFilter) box.getItemAt(j);
							d = filter.getDescription();
							if (d.startsWith(value)) {
								selectedFilter = filter;
								break;
							}
						}
						if (selectedFilter != null)
							box.setSelectedItem(selectedFilter);
					}
				});
			}
		}
	}
	
	/** 
	 * Builds and lays out the tool bar. 
	 * 
	 * @return See above.
	 */
	private JPanel buildToolBarRight()
	{
		JPanel bar = new JPanel();
		bar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		//bar.add(resetButton);
		//bar.add(Box.createHorizontalStrut(20));
		bar.add(cancelImportButton);
		bar.add(Box.createHorizontalStrut(5));
		bar.add(cancelButton);
		bar.add(Box.createHorizontalStrut(5));
		bar.add(importButton);
		bar.add(Box.createHorizontalStrut(10));
		return bar;
	}
	
	/** 
	 * Builds and lays out the tool bar. 
	 * 
	 * @return See above.
	 */
	private JPanel buildToolBarLeft()
	{
		JPanel bar = new JPanel();
		bar.setLayout(new FlowLayout(FlowLayout.LEFT));
		//bar.add(refreshButton);
		//bar.add(Box.createHorizontalStrut(5));
		bar.add(showThumbnails);
		return bar;
	}
	
	/**
	 * Builds and lays out the components.
	 * 
	 * @return See above
	 */
	private JPanel buildPathComponent()
	{
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(numberOfFolders);
		JLabel l = new JLabel();
		l.setText("Directories before File");
		p.add(l);
		return p;
	}
	
	/**
	 * Builds and lays out the component displaying the options for the 
	 * metadata.
	 * 
	 * @return See above.
	 */
	private JXTaskPane buildMetadataComponent()
	{
		JXTaskPane pane = new JXTaskPane();
		Font font = pane.getFont();
		pane.setFont(font.deriveFont(font.getStyle(), font.getSize()-2));
		pane.setCollapsed(true);
		pane.setTitle("Metadata Defaults");
		pane.add(buildPixelSizeComponent());
		return pane;
	}
	
	/**
	 * Builds and lays out the pixels size options.
	 * 
	 * @return See above.
	 */
	private JPanel buildPixelSizeComponent()
	{
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createTitledBorder("Pixels Size Defaults"));
		JLabel l = new JLabel();
		l.setText("Used if no values included in the file:");
		p.add(UIUtilities.buildComponentPanel(l));
		JPanel row = new JPanel();
		row.setLayout(new FlowLayout(FlowLayout.LEFT));
		l = new JLabel();
		l.setText("X: ");
		row.add(l);
		row.add(pixelsSize.get(0));
		l = new JLabel();
		l.setText("Y: ");
		row.add(l);
		row.add(pixelsSize.get(1));
		l = new JLabel();
		l.setText("Z: ");
		row.add(l);
		row.add(pixelsSize.get(2));
		p.add(row);
		return UIUtilities.buildComponentPanel(p);
	}
	
	/**
	 * Builds and lays out the components displaying the naming options.
	 * 
	 * @return See above.
	 */
	private JComponent buildNamingComponent()
	{
		JPanel content = new JPanel();
		content.setBorder(BorderFactory.createTitledBorder("File Naming"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(fullName);
		panel.add(partialName);
		JPanel pp = new JPanel();
		pp.setLayout(new BoxLayout(pp, BoxLayout.Y_AXIS));
		pp.add(UIUtilities.buildComponentPanel(panel));
		pp.add(buildPathComponent());
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		content.add(overrideName, c);
		c.gridwidth = 1;
		c.gridy++;
		content.add(Box.createHorizontalStrut(15), c);
		c.gridx++;
		content.add(pp, c);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(content);
		p.add(buildAnnotationComponent());
		return UIUtilities.buildComponentPanel(p);
	}
	
	/**
	 * Builds the component hosting the controls to add annotations.
	 * 
	 * @return See above.
	 */
	private JPanel buildAnnotationComponent()
	{
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JLabel l = new JLabel();
		l.setText("Add Tag");
		JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tagPanel.add(l);
		tagPanel.add(tagButton);
		l = new JLabel();
		l.setText(": ");
		tagPanel.add(l);
		tagPanel.add(tagsPane);
		
		p.add(tagPanel);
		return UIUtilities.buildComponentPanel(p);
	}
	
	/**
	 * Builds and lays out the import options available.
	 * 
	 * @param container Container where to import the image.
	 * @return See above.
	 */
	private JPanel buildOptionsPane()
	{
		//Lays out the options
		JPanel options = new JPanel();
		double[][] size = {{TableLayout.FILL}, 
				{TableLayout.PREFERRED, TableLayout.PREFERRED, 
			TableLayout.PREFERRED}};
		options.setLayout(new TableLayout(size));
		options.add(buildNamingComponent(), "0, 1");
		options.add(buildMetadataComponent(), "0, 2");
		return options;
	}
	
	/**
	 * Creates a row.
	 * 
	 * @return See above.
	 */
	private JPanel createRow()
	{
		JPanel row = new JPanel();
		row.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		row.setBackground(UIUtilities.BACKGROUND);
		row.setBorder(null);
		return row;
	}
	
	/** Initializes the selection boxes. */
	private void initializeLocationBoxes()
	{
		parentsBox.removeActionListener(parentsBoxListener);
		parentsBox.removeAllItems();
		parentsBox.addActionListener(parentsBoxListener);
		datasetsBox.removeAllItems();
		List<DataNode> topList = new ArrayList<DataNode>();
		List<DataNode> datasetsList = new ArrayList<DataNode>();
		DataNode n;
		Object ho;
		TreeImageDisplay node;
		if (objects != null && objects.size() > 0) {
			Iterator<TreeImageDisplay> i = objects.iterator();
			while (i.hasNext()) {
				node = i.next();
				ho = node.getUserObject();
				if (ho instanceof ProjectData || ho instanceof ScreenData) {
					n = new DataNode((DataObject) ho);
					n.setRefNode(node);
					topList.add(n); 
				} else if (ho instanceof DatasetData) {
					n = new DataNode((DataObject) ho);
					n.setRefNode(node);
					datasetsList.add(n);
				}
			}
		}
		List sortedList = new ArrayList();
		if (topList.size() > 0) {
			sortedList = sorter.sort(topList);
		}
		int size;
		if (type == Importer.PROJECT_TYPE) {
			//sort the node
			if (datasetsList.size() > 0) { //orphaned datasets.
				n = new DataNode(datasetsList);
			} else {
				List<DataNode> list = new ArrayList<DataNode>();
				list.add(new DataNode(DataNode.createDefaultDataset()));
				n = new DataNode(list);
			}
			sortedList.add(n);
			
			parentsBox.removeActionListener(parentsBoxListener);
			parentsBox.setModel(new DefaultComboBoxModel(sortedList.toArray()));
			parentsBox.addActionListener(parentsBoxListener);
			//Determine the node to select.
			size = parentsBox.getItemCount();
			if (selectedContainer != null) {
				ho = selectedContainer.getUserObject();
				ProjectData p = null;
				if (ho instanceof ProjectData) {
					p = (ProjectData) ho;
				} else if (ho instanceof DatasetData) {
					node = selectedContainer.getParentDisplay();
					if (node.getUserObject() instanceof ProjectData) {
						p = (ProjectData) node.getUserObject();
					}
				}
				if (p != null) {
					long id = p.getId();
					int index = 0;
					for (int i = 0; i < size; i++) {
						n = (DataNode) parentsBox.getItemAt(i);
						if (n.getDataObject().getId() == id) {
							index = i;
							break;
						}
					}
					parentsBox.setSelectedIndex(index);
				} else { //orphaned dataset
					parentsBox.setSelectedIndex(size-1);
				}
			} else { //nothing selected so we will select the first item
				if (size > 0)
					parentsBox.setSelectedIndex(0);
			}
		} else if (type == Importer.SCREEN_TYPE) {
			sortedList.add(new DataNode(DataNode.createDefaultScreen()));
			parentsBox.removeActionListener(parentsBoxListener);
			parentsBox.setModel(new DefaultComboBoxModel(sortedList.toArray()));
			parentsBox.addActionListener(parentsBoxListener);
			size = sortedList.size();
			if (selectedContainer != null) {
				ho = selectedContainer.getUserObject();
				if (ho instanceof ScreenData) {
					long id = ((ScreenData) ho).getId();
					for (int i = 0; i < size; i++) {
						n = (DataNode) parentsBox.getItemAt(i);
						if (n.getDataObject().getId() == id) {
							parentsBox.setSelectedIndex(i);
							break;
						}
					}
				}
			}
		}
	}
	
	/** Populates the datasets box depending on the selected project. */
	private void populateDatasetsBox()
	{
		if (type == Importer.SCREEN_TYPE) return;
		DataNode n = (DataNode) parentsBox.getSelectedItem();
		List<DataNode> list = n.getDatasetNodes();
		List l = sorter.sort(list);
		datasetsBox.removeAllItems();
		datasetsBox.setModel(new DefaultComboBoxModel(l.toArray()));
		if (selectedContainer != null) {
			Object o = selectedContainer.getUserObject();
			if (o instanceof DatasetData) {
				DatasetData d = (DatasetData) o;
				Iterator<DataNode> i = l.iterator();
				while (i.hasNext()) {
					n = i.next();
					if (n.getDataObject().getId() == d.getId()) {
						datasetsBox.setSelectedItem(n);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Builds and lays out the controls for the location.
	 * 
	 * @return See above.
	 */
	private JComponent buildLocationBar()
	{
		JPanel bar = new JPanel();
		//bar.setBackground(UIUtilities.BACKGROUND);
		bar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		bar.add(projectLocationButton);
		bar.add(screenLocationButton);
		bar.add(Box.createHorizontalStrut(5));
		bar.add(reloadContainerButton);
		return bar;
	}
	
	/**
	 * Returns the file queue and indicates where the files will be imported.
	 * 
	 * @return See above.
	 */
	private void buildLocationPane()
	{
		locationPane.removeAll();
		//locationPane.add(buildLocationBar());
		//locationPane.add(new JSeparator());
		JPanel row = createRow();
		String message = PROJECT_TXT;
		if (type == Importer.SCREEN_TYPE) message = SCREEN_TXT;
		row.add(UIUtilities.setTextFont(MESSAGE_LOCATION));
		locationPane.add(row);
		locationPane.add(Box.createVerticalStrut(2));
		row = createRow();
		row.add(UIUtilities.setTextFont(message));
		row.add(parentsBox);
		row.add(addProjectButton);
		locationPane.add(row);
		if (type == Importer.PROJECT_TYPE) {
			locationPane.add(Box.createVerticalStrut(8));
			JPanel p = UIUtilities.buildComponentPanel(folderAsDatasetBox, 0, 0);
			p.setBackground(UIUtilities.BACKGROUND);
			JPanel right = new JPanel();
			right.setBackground(UIUtilities.BACKGROUND);
			right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
			right.add(p);
			row = createRow();
			row.add(datasetsBox);
			row.add(addButton);
			right.add(row);
			row = createRow();
			row.add(UIUtilities.setTextFont(DATASET_TXT));
			row.add(right);
			locationPane.add(row);
		}
	}

	/** 
	 * Lays out the quota.
	 * 
	 * @return See above.
	 */
	private JPanel buildQuotaPane()
	{
		JPanel row = new JPanel();
		row.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		row.add(UIUtilities.buildComponentPanelRight(diskSpacePane, 0, 0, true));
		row.add(UIUtilities.setTextFont(QuotaCanvas.IMPORT_SIZE_TEXT));
		row.add(UIUtilities.buildComponentPanel(sizeImportLabel, 0, 0, true));
		row.setBorder(null);
		return row;
	}
	
	/** Builds and lays out the UI. */
	private void buildGUI()
	{
		setLayout(new BorderLayout(0, 0));
		add(buildLocationBar(), BorderLayout.NORTH);
		JPanel p = new JPanel();
		p.setBorder(null);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(buildQuotaPane());
		p.add(table);
		tabbedPane.add("Files to import", p);
		tabbedPane.add("Options", buildOptionsPane());
		
		p = new JPanel();
		double[][] size = {{TableLayout.PREFERRED, 10, 5, TableLayout.FILL}, 
				{TableLayout.PREFERRED, TableLayout.FILL}};
		p.setLayout(new TableLayout(size));
		p.add(table.buildControls(), "0, 1, LEFT, CENTER");
		
		buildLocationPane();
		p.add(locationPane, "3, 0");
		p.add(tabbedPane, "2, 1, 3, 1");
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chooser, 
				p);
		JPanel body = new JPanel();
		double[][] ss = {{TableLayout.FILL}, 
				{TableLayout.PREFERRED, TableLayout.FILL}};
		body.setLayout(new TableLayout(ss));
		body.setBackground(UIUtilities.BACKGROUND);

		body.add(pane, "0, 1");
		add(body, BorderLayout.CENTER);
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		
		//Lays out the buttons.
		JPanel bar = new JPanel();
		bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
		bar.add(buildToolBarLeft());
		bar.add(buildToolBarRight());
		controls.add(new JSeparator());
		controls.add(bar);
		
		//c.add(controls, BorderLayout.SOUTH);
		add(controls, BorderLayout.SOUTH);
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = 
				UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations)
				getRootPane().setWindowDecorationStyle(
						JRootPane.FILE_CHOOSER_DIALOG);
		}
	}

	/**
	 * Helper method returning <code>true</code> if the connection is fast,
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	private boolean isFastConnection()
	{
		int value = (Integer) ImporterAgent.getRegistry().lookup(
				LookupNames.CONNECTION_SPEED);
		return value == RenderingControl.UNCOMPRESSED;
	}
	
    /** Imports the selected files. */
    private void importFiles()
    {
    	option = IMPORT;
    	//Set the current directory as the defaults
    	File dir = chooser.getCurrentDirectory();
    	if (dir != null) UIUtilities.setDefaultFolder(dir.toString());
    	List<ImportableFile> files = table.getFilesToImport();
    	ImportableObject object = new ImportableObject(files,
    			overrideName.isSelected());
    	object.setScanningDepth(ImporterAgent.getScanningDepth());
    	Boolean b = (Boolean) ImporterAgent.getRegistry().lookup(
    			LOAD_THUMBNAIL);
    	if (b != null)
    		object.setLoadThumbnail(b.booleanValue());
    	//if slow connection 
    	if (!isFastConnection())
    		object.setLoadThumbnail(false);
    	if (showThumbnails.isVisible()) {
    		object.setLoadThumbnail(showThumbnails.isSelected());
    	}
    	//tags
    	if (tagsMap.size() > 0) object.setTags(tagsMap.values());
    	if (partialName.isSelected()) {
    		Integer number = (Integer) numberOfFolders.getValueAsNumber();
        	if (number != null && number >= 0) object.setDepthForName(number);
    	} 
    	NumericalTextField nf;
    	Iterator<NumericalTextField> i = pixelsSize.iterator();
    	Number n;
    	double[] size = new double[3];
    	int index = 0;
    	int count = 0;
    	while (i.hasNext()) {
			nf = i.next();
			n = nf.getValueAsNumber();
			if (n != null) {
				count++;
				size[index] = n.doubleValue();
			} else size[index] = 1;
			index++;
		}
    	if (count > 0) object.setPixelsSize(size);	
    	firePropertyChange(IMPORT_PROPERTY, null, object);
    	table.removeAllFiles();
    	//sizeImportLabel.setText(UIUtilities.formatFileSize(0));
    	//setVisible(false);
    	//dispose();
    }

	/**
	 * Checks if the file can be added to the passed list.
	 * 
	 * @param f The file to handle.
	 * @param l The list to populate.
	 */
	private void checkFile(File f, List<File> l)
	{
		if (f == null || f.isHidden()) return;
		if (f.isFile()) {
			if (isFileImportable(f)) l.add(f);
		} else if (f.isDirectory()) {
			File[] list = f.listFiles();
			if (list != null && list.length > 0) l.add(f);
		}
	}
	
	/**
	 * Returns <code>true</code> if the file can be imported, 
	 * <code>false</code> otherwise.
	 * 
	 * @param f The file to check.
	 * @return See above.
	 */
	private boolean isFileImportable(File f)
	{
		if (f == null || f.isHidden()) return false;
		return true;
	}

	/** 
     * Creates a new instance.
     * 
     * @param owner 	The owner of the dialog.
     * @param filters 	The list of filters.
     * @param containers The container where to import the files.
     * @param objects    The possible objects.
     * @param type 		One of the type constants.
     */
    public ImportDialog(JFrame owner, FileFilter[] filters, 
    		TreeImageDisplay selectedContainer, 
    		Collection<TreeImageDisplay> objects, int type)
    {
    	//super(owner);
    	super(0, TITLE, TITLE);
    	this.owner = owner;
    	setClosable(false);
    	setCloseVisible(false);
    	this.objects = objects;
    	this.type = type;
    	this.selectedContainer = selectedContainer;
    	setProperties();
    	initComponents(filters);
    	installListeners();
    	buildGUI();
    	//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//setSize(7*(screenSize.width/10), 7*(screenSize.height/10));
    }

    /** 
     * Returns the type of the import.
     * 
     * @return See above.
     */
    public int getType() { return type; }
    
    /** Display the size of files to add. */
    void onSelectionChanged()
    {
		if (canvas != null) {
			long size = table.getSizeFilesInQueue();
			canvas.setSizeInQueue(size);
			String v = (int) Math.round(canvas.getPercentageToImport()*100)
			+"% of Remaining Space";
			sizeImportLabel.setText(UIUtilities.formatFileSize(size));
			sizeImportLabel.setToolTipText(v);
		}
    }

    /**
     * Returns <code>true</code> if the folder containing an image has to be
     * used as a dataset, <code>false</code> otherwise.
     * 
     * @return See above.
     */
    boolean isParentFolderAsDataset()
    {
    	return folderAsDatasetBox.isSelected();
    }
    /**
	 * Returns the name to display for a file.
	 * 
	 * @param fullPath The file's absolute path.
	 * @return See above.
	 */
	String getDisplayedFileName(String fullPath)
	{
		if (fullPath == null || !partialName.isSelected()) return fullPath;
		Integer number = (Integer) numberOfFolders.getValueAsNumber();
		return UIUtilities.getDisplayedFileName(fullPath, number);
	}
	
	/**
	 * Returns <code>true</code> if the folder can be used as a container,
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	boolean useFolderAsContainer()
	{
		return (type != Importer.SCREEN_TYPE);
	}

	/**
	 * Returns where to import the file when selected.
	 * 
	 * @return See above.
	 */
	DataNode getImportLocation()
	{
		if (type == Importer.SCREEN_TYPE) {
			if (parentsBox.getItemCount() > 0) 
				return (DataNode) parentsBox.getSelectedItem();
			return null;
		}
		if (datasetsBox.getItemCount() > 0)
			return (DataNode) datasetsBox.getSelectedItem();
		return null;
	}
	
    /**
     * Resets the text and remove all the files to import.
     * 
     * @param selectedContainer The container where to import the files.
     * @param objects    The possible objects.
     * @param type       One of the constants used to identify the type of 
     * 					 import.
     */
	public void reset(TreeImageDisplay selectedContainer, 
			Collection<TreeImageDisplay> objects, int type)
	{
		canvas.setVisible(false);
		this.selectedContainer = selectedContainer;
		this.objects = objects;
		int oldType = this.type;
		this.type = type;
		formatSwitchButton();
		if (oldType != this.type) { 
			//change filters.
			//reset name
			FileFilter[] filters = chooser.getChoosableFileFilters();
			for (int i = 0; i < filters.length; i++) {
				chooser.removeChoosableFileFilter(filters[i]);
			}
			Iterator<FileFilter> j;
			if (type == Importer.SCREEN_TYPE) {
				j = hcsFilters.iterator();
				while (j.hasNext()) {
					chooser.addChoosableFileFilter(j.next());
				}
				chooser.setFileFilter(hcsFilters.get(0));
			} else {
				chooser.addChoosableFileFilter(combinedFilter);
				j = generalFilters.iterator();
				while (j.hasNext()) {
					chooser.addChoosableFileFilter(j.next());
				}
				chooser.setFileFilter(combinedFilter);
			}
			//File[] files = chooser.getSelectedFiles();
			//table.reset(files != null && files.length > 0);
		}
		File[] files = chooser.getSelectedFiles();
		table.allowAddition(files != null && files.length > 0);
		handleTagsSelection(new ArrayList());
		tabbedPane.setSelectedIndex(0);
		FileFilter[] filters = chooser.getChoosableFileFilters();
		if (filters != null && filters.length > 0)
			chooser.setFileFilter(filters[0]);
		initializeLocationBoxes();
		buildLocationPane();
		locationPane.repaint();
		tagsPane.removeAll();
		tagsMap.clear();
	}
	
    /**
     * Shows the chooser dialog. 
     * 
     * @return The option selected.
     */
    public int showDialog()
    {
	    UIUtilities.setLocationRelativeToAndShow(getParent(), this);
	    return option;
    }

    /**
     * Shows the chooser dialog. 
     * 
     * @return The option selected.
     */
    public int centerDialog()
    {
	    UIUtilities.centerAndShow(this);
	    return option;
    }
    
	/**
	 * Sets the collection of existing tags.
	 * 
	 * @param tags The collection of existing tags.
	 */
	public void setTags(Collection tags)
	{
		if (tags == null) return;
		Collection<TagAnnotationData> set = tagsMap.values();
		List<Long> ids = new ArrayList<Long>();
		List available = new ArrayList();
		List selected = new ArrayList();
		TagAnnotationData tag;
		Iterator i = set.iterator();
		while (i.hasNext()) {
			tag = (TagAnnotationData) i.next();
			if (tag.getId() > 0)
				ids.add(tag.getId());
		}
		i = tags.iterator();
		while (i.hasNext()) {
			tag = (TagAnnotationData) i.next();
			if (ids.contains(tag.getId())) 
				selected.add(tag);
			else available.add(tag);
		}
		//show the selection wizard
		showSelectionWizard(TagAnnotationData.class, available, selected, true);
	}
	
	/**
	 * Displays the used and available disk space.
	 * 
	 * @param quota The value to set.
	 */
	public void setDiskSpace(DiskQuota quota)
	{
		if (quota == null) return;
		long free = quota.getAvailableSpace();
		long used = quota.getUsedSpace();
		if (free <= 0 || used < 0) return;
		canvas.setPercentage(quota);
		canvas.setVisible(true);
	}

	/**
	 * Reacts to property fired by the table.
	 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		String name = evt.getPropertyName();
		if (FileSelectionTable.ADD_PROPERTY.equals(name)) {
			addFiles();
		} else if (FileSelectionTable.REMOVE_PROPERTY.equals(name)) {
			importButton.setEnabled(table.hasFilesToImport());
		} else if (JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(name)) {
			File[] files = chooser.getSelectedFiles();
			table.allowAddition(files != null && files.length > 0);
		} else if (NumericalTextField.TEXT_UPDATED_PROPERTY.equals(name)) {
			if (partialName.isSelected()) {
		    	Integer number = (Integer) numberOfFolders.getValueAsNumber();
		    	if (number != null && number >= 0) table.applyToAll();
			}
		} else if (SelectionWizard.SELECTED_ITEMS_PROPERTY.equals(name)) {
			Map m = (Map) evt.getNewValue();
			if (m == null || m.size() != 1) return;
			Set set = m.entrySet();
			Entry entry;
			Iterator i = set.iterator();
			Class type;
			while (i.hasNext()) {
				entry = (Entry) i.next();
				type = (Class) entry.getKey();
				if (TagAnnotationData.class.getName().equals(type.getName()))
					handleTagsSelection((Collection) entry.getValue());
			}
		} else if (EditorDialog.CREATE_NO_PARENT_PROPERTY.equals(name)) {
			Object ho = evt.getNewValue();
			if (ho instanceof DatasetData)
				createDataset((DatasetData) ho);
			else if (ho instanceof ProjectData)
				createProject((ProjectData) ho);
			else if (ho instanceof ScreenData)
				createScreen((ScreenData) ho);
		}
	}

	/**
	 * Cancels or imports the files.
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt)
	{
		int index = Integer.parseInt(evt.getActionCommand());
		EditorDialog d;
		switch (index) {
			case IMPORT:
				importFiles();
				break;
			case CANCEL:
				firePropertyChange(CANCEL_SELECTION_PROPERTY,
						Boolean.valueOf(false), Boolean.valueOf(true));
				break;
			case REFRESH:
				chooser.rescanCurrentDirectory();
				chooser.repaint();
				break;
			case RESET: 
				partialName.setSelected(false);
				table.resetFilesName();
				break;
			case APPLY_TO_ALL:
				table.applyToAll();
				break;
			case TAG:
				firePropertyChange(LOAD_TAGS_PROPERTY, Boolean.valueOf(false), 
						Boolean.valueOf(true));
				break;
			case CREATE_DATASET:
				d = new EditorDialog(owner, new DatasetData(), false);
				d.addPropertyChangeListener(this);
				d.setModal(true);
				UIUtilities.centerAndShow(d);
				break;
			case CREATE_PROJECT:
				if (type == Importer.PROJECT_TYPE)
					d = new EditorDialog(owner, new ProjectData(), false);
				else d = new EditorDialog(owner, new ScreenData(), false);
				d.addPropertyChangeListener(this);
				d.setModal(true);
				UIUtilities.centerAndShow(d);
				break;
			case REFRESH_LOCATION:
				chooser.rescanCurrentDirectory();
				chooser.repaint();
				firePropertyChange(REFRESH_LOCATION_PROPERTY, 
						-1, getType());
				break;
			case LOCATION:
				handleLocationSwitch(evt.getSource());
				break;
			case CANCEL_ALL_IMPORT:
				firePropertyChange(CANCEL_ALL_IMPORT_PROPERTY,
						Boolean.valueOf(false), Boolean.valueOf(true));
		}
	}
	
}
