/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2007 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 *	author Will Moore will@lifesci.dundee.ac.uk
 */

package util;

import javax.swing.Icon;
import javax.swing.ImageIcon;

// for returning icons.
// Icons come from Nuvola. http://icon-king.com/?p=15

public class ImageFactory {
	
	// singleton
	private static ImageFactory uniqueInstance = new ImageFactory();
	// private constructor
	private ImageFactory() {};
	// return uniqueInstance
	public static ImageFactory getInstance() {
		return uniqueInstance;
	}
	
	public static final String ICONS_FILE = "/graphx/";
	
	public static final String OPEN_FILE_ICON = ICONS_FILE + "folder_open.png";
	public static final String SAVE_ICON = ICONS_FILE + "filesave.png";
	public static final String PRINT_ICON = ICONS_FILE + "fileExportPrint.png";
	public static final String LOAD_DEFAULTS_ICON = ICONS_FILE + "bookmarks_list_add.png";
	public static final String NEW_FILE_ICON = ICONS_FILE + "filenew.png";
	public static final String SAVE_FILE_AS_ICON = ICONS_FILE + "filesaveas.png";
	public static final String EDIT_ICON = ICONS_FILE + "package_editors.png";
	public static final String ADD_ICON = ICONS_FILE + "edit_add.png";
	public static final String DELETE_ICON = ICONS_FILE + "cancel.png";
	public static final String MOVE_UP_ICON = ICONS_FILE + "up.png";
	public static final String MOVE_DOWN_ICON = ICONS_FILE + "down.png";
	public static final String DEMOTE_ICON = ICONS_FILE + "demote.png";
	public static final String PROMOTE_ICON = ICONS_FILE + "promote.png";
	public static final String DUPLICATE_ICON = ICONS_FILE + "window_list.png";
	public static final String COPY_ICON = ICONS_FILE + "eclipse_copy_edit.png";
	public static final String PASTE_ICON = ICONS_FILE + "eclipse_paste_edit.png";
	public static final String IMPORT_ICON = ICONS_FILE + "importXml.png";
	public static final String IMPORT_TEXT_ICON = ICONS_FILE + "eclipse_copy_import.png";
	public static final String IMPORT_TABLE_ICON = ICONS_FILE + "import_table.png";
	public static final String BIG_PROTOCOL_ICON = ICONS_FILE + "omeroEditor128.png";
	public static final String PROTOCOL_ICON = ICONS_FILE + "omeroEditor16.png";
	public static final String COLLAPSED_ICON = ICONS_FILE + "1rightarrow.png";
	public static final String NOT_COLLAPSED_ICON = ICONS_FILE + "1downarrow.png";
	public static final String SPACER_ICON = ICONS_FILE + "spacer16x16.png";
	public static final String WWW_ICON = ICONS_FILE + "www.png";
	public static final String MORE_LIKE_THIS_ICON = ICONS_FILE + "moreLikeThis.png";
	public static final String TEMPLATE_SEARCH_ICON = ICONS_FILE + "templateSearch.png";
	public static final String INFO_ICON = ICONS_FILE + "messagebox_info.png";
	public static final String SEARCH_ICON = ICONS_FILE + "xmag.png";
	public static final String TWO_LEFT_ARROW = ICONS_FILE + "2leftarrow.png";
	public static final String N0 = ICONS_FILE + "no.png";
	public static final String EDU_MATHS = ICONS_FILE + "edu_mathematics.png";
	public static final String NOTE_PAD = ICONS_FILE + "kwrite.png";
	public static final String VALIDATION_ICON = ICONS_FILE + "spellcheck.png";
	public static final String RED_BALL_ICON = ICONS_FILE + "krec_record.png";
	public static final String UNDO_ICON = ICONS_FILE + "undo.png";
	public static final String REDO_ICON = ICONS_FILE + "redo.png";
	public static final String CLEAR_FIELDS_ICON = ICONS_FILE + "news_unsubscribe.png";
	public static final String WWW_FILE_ICON = ICONS_FILE + "folder_http.png";
	public static final String FIND_ICON = ICONS_FILE + "find.png";
	public static final String FILE_CLOSE_ICON = ICONS_FILE + "fileclose.png";
	public static final String PREVIOUS_UP_ICON = ICONS_FILE + "previousUp.png";
	public static final String NEXT_DOWN_ICON = ICONS_FILE + "nextDown.png";
	public static final String OLS_LOGO_SMALL = ICONS_FILE + "ols-logo-small.jpg";
	public static final String NEW_ROW_ICON = ICONS_FILE + "view_bottom.png";
	public static final String CLEAR_ROW_ICON = ICONS_FILE + "view_clear.png";
	public static final String COLOUR_SELECTION_ICON = ICONS_FILE + "colorize.png";
	public static final String BOLD_ICON = ICONS_FILE + "bold.png";
	public static final String BULLET_POINTS_ICON = ICONS_FILE + "bulletPoints.png";
	public static final String UNDERLINE_ICON = ICONS_FILE + "underline.png";
	public static final String ENTER_ICON = ICONS_FILE + "enter.png";
	public static final String ONTOLOGY_METADATA_ICON = ICONS_FILE + "kdict.png";
	public static final String CONFIGURE_ICON = ICONS_FILE + "package_utilities.png";
	public static final String OPEN_IMAGE_ICON = ICONS_FILE + "folder_image.png";
	public static final String SEND_COMMENT_ICON = ICONS_FILE + "mail_send.png";
	public static final String ZOOM_ICON = ICONS_FILE + "zoom.png";
	public static final String INDEX_FILES_ICON = ICONS_FILE + "bookcase.png";
	public static final String ROTATE_HORIZONTAL_ICON = ICONS_FILE + "rotate_right_up.png";
	public static final String ROTATE_VERTICAL_ICON = ICONS_FILE + "rotate_down_left.png";
	public static final String CALENDAR_ICON = ICONS_FILE + "date.png";
	public static final String CALENDAR_EXPORT_ICON = ICONS_FILE + "date-export.png";
	public static final String ALARM_ICON_64 = ICONS_FILE + "kalarm64.png";
	public static final String ALARM_GIF_64 = ICONS_FILE + "kalarmAnimated64.gif";
	public static final String RELOAD_ICON = ICONS_FILE + "reload.png";
	public static final String LOCKED_ICON = ICONS_FILE + "encrypted.png";
	public static final String LOCKED_RED_ICON = ICONS_FILE + "encrypted_red.png";
	public static final String LOCKED_ICON_48 = ICONS_FILE + "encrypted48.png";
	public static final String UNLOCKED_ICON = ICONS_FILE + "decrypted.png";
	public static final String NO_IMAGE_ICON = ICONS_FILE + "file_broken32.png";
	public static final String KORGANIZER_ICON = ICONS_FILE + "korganizer64.png";
	public static final String NETWORK_LOCAL_ICON = ICONS_FILE + "network_local.png";
	public static final String LINK_LOCAL_ICON = ICONS_FILE + "link_local.png";
	public static final String LINK_RELATIVE_ICON = ICONS_FILE + "link_relative.png";
	public static final String LINK_SCIENCE_ICON = ICONS_FILE + "link_science.png";
	public static final String LINK_SCIENCE_RELATIVE_ICON = ICONS_FILE + "link_science_relative.png";
	public static final String WRENCH_ICON = ICONS_FILE + "configure.png";
	public static final String RED_ASTERISK_ICON = ICONS_FILE + "red_asterisk.png";
	public static final String RED_ASTERISK_WARNING_ICON = ICONS_FILE + "red_asterisk_warning.png";
	public static final String DEFAULT_ICON = ICONS_FILE + "default-d14.png";
	public static final String TIMER_START_ICON = ICONS_FILE + "timerStart.png";
	public static final String TIMER_STOP_ICON = ICONS_FILE + "timerStop.png";
	
	
	public static final String BORDER_TOP_LEFT = ICONS_FILE + "BorderImages/topLeft.gif";
	public static final String BORDER_TOP = ICONS_FILE + "BorderImages/top.gif";
	public static final String BORDER_TOP_RIGHT = ICONS_FILE + "BorderImages/topRight.gif";
	public static final String BORDER_LEFT = ICONS_FILE + "BorderImages/left.gif";
	public static final String BORDER_RIGHT = ICONS_FILE + "BorderImages/right.gif";
	public static final String BORDER_BOTTOM_LEFT = ICONS_FILE + "BorderImages/bottomLeft.gif";
	public static final String BORDER_BOTTOM = ICONS_FILE + "BorderImages/bottom.gif";
	public static final String BORDER_BOTTOM_RIGHT = ICONS_FILE + "BorderImages/bottomRight.gif";
	
	public static final String BORDER_TOP_LEFT_HLT = ICONS_FILE + "BorderImages/topLeftHlt.gif";
	public static final String BORDER_TOP_HLT = ICONS_FILE + "BorderImages/topHlt.gif";
	public static final String BORDER_TOP_RIGHT_HLT = ICONS_FILE + "BorderImages/topRightHlt.gif";
	public static final String BORDER_LEFT_HLT = ICONS_FILE + "BorderImages/leftHlt.gif";
	public static final String BORDER_RIGHT_HLT = ICONS_FILE + "BorderImages/rightHlt.gif";
	public static final String BORDER_BOTTOM_LEFT_HLT = ICONS_FILE + "BorderImages/bottomLeftHlt.gif";
	public static final String BORDER_BOTTOM_HLT = ICONS_FILE + "BorderImages/bottomHlt.gif";
	public static final String BORDER_BOTTOM_RIGHT_HLT = ICONS_FILE + "BorderImages/bottomRightHlt.gif";
	
	
	
	public Icon getIcon(String iconPathName) {
		try {
			return new ImageIcon(ImageFactory.class.getResource(iconPathName));
		} catch (NullPointerException ex) {
			System.out.println("Could not find Icon at " + iconPathName);
			return null;
		}
	}

	public ImageIcon getImageIcon(String iconPathName) {
		return (ImageIcon)getIcon(iconPathName);
	}
}
