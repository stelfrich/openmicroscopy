/*
 * org.openmicroscopy.shoola.env.ui.LoginOMEDS
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

package org.openmicroscopy.shoola.env.ui;


//Java imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.Container;

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
public class LoginOMEDS
	extends JDialog
{
	/** The width of the window. */
	private static final int		WIN_W = 200;  
	
	/** The height of the window. */
	private static final int		WIN_H = 150;
	
	/** The font color for the login text fields. */
	private static final Color		FONT_COLOR = new Color(0x4682B4);
	
	/** 
	 * Absolute positioning and size of the label used for text field
	 * and password text field in their panel.
	 */
	private static final Rectangle	LABEL_BOUNDS =
												new Rectangle(10 , 0, 60, 20);
	
	/** 
	* Absolute positioning and size of the label used for text field
	* and password text field in their panel.
	*/
	private static final Rectangle	FIELD_BOUNDS =
												new Rectangle(70 , 0, 80, 20);
	
	/** 
	 * Dimension of the Panel containing a label and text field (user 
	 * or password textfield).
	 */
	private static final Dimension	PANEL_DIM = new Dimension(130, 15);
	
											
	/** Reference to the container. */
	private Container				container;
	
	/** Text field to enter the login user name. */
	JTextField      				user;

	/** Password field to enter login password. */
	JPasswordField 					pass;
	
	/** Login button. */
	JButton     					login;
	
	LoginOMEDS(Container container)
	{
		super((JFrame) container.getRegistry().getTopFrame().getFrame(), true);
		this.container = container;
		LoginOMEDSManager manager = new LoginOMEDSManager(container, this);
		initLoginFields();
		initLoginButton();
		buildGUI();
		manager.initListeners();
		setSize(WIN_W, WIN_H);
	}
		
	/** 
	 * Creates and initializes the login fields.
	 */
	private void initLoginFields()
	{
		Font font = (Font) 
					container.getRegistry().lookup("/resources/fonts/Titles");
		user = new JTextField();
		user.setFont(font);
		user.setForeground(FONT_COLOR);
		user.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		pass = new JPasswordField();
		pass.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		pass.setFont(font);
		pass.setForeground(FONT_COLOR);
	}
	
	/** 
	 * Creates and initializes the login button.
	 */
	private void initLoginButton()
	{
		login = new JButton("Login");
		//TODO: provide icons for login.
		//Next two statements get rid of surrounding border.
		login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	/** Build and layout the GUI. */
	private void buildGUI()
	{
		JPanel p = new JPanel(), buttonPanel = new JPanel(),
		txtPanel = new JPanel();
		JLabel labelText = new JLabel("Please log in");
		txtPanel.add(labelText, BorderLayout.CENTER);
		buttonPanel.add(login);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(txtPanel);
		p.add(buildPanel(user, "name: "));
		p.add(Box.createRigidArea(new Dimension(0,5)));
		p.add(buildPanel(pass, "password: "));
		getContentPane().add(p, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);	
	}
	
	/** Build a panel with label and textfield. */
	private JPanel buildPanel(JTextField field, String txt)
	{
		JPanel p = new JPanel();
		JLabel label = new JLabel(txt);
		p.setLayout(null);
		label.setBounds(LABEL_BOUNDS);
		field.setBounds(FIELD_BOUNDS);
		p.add(label);
		p.add(field);
		p.setSize(PANEL_DIM);
		return p;
	}
	
}
