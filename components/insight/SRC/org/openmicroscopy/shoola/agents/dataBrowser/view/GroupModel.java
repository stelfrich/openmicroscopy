/*
 * org.openmicroscopy.shoola.agents.dataBrowser.view.GroupModel 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2013 University of Dundee. All rights reserved.
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
 */
package org.openmicroscopy.shoola.agents.dataBrowser.view;


//Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.dataBrowser.DataBrowserLoader;
import org.openmicroscopy.shoola.agents.dataBrowser.DataBrowserTranslator;
import org.openmicroscopy.shoola.agents.dataBrowser.ThumbnailLoader;
import org.openmicroscopy.shoola.agents.dataBrowser.browser.BrowserFactory;
import org.openmicroscopy.shoola.agents.dataBrowser.browser.ImageDisplay;
import org.openmicroscopy.shoola.agents.dataBrowser.browser.ImageNode;
import omero.gateway.SecurityContext;

import pojos.DataObject;
import pojos.ExperimenterData;
import pojos.ImageData;

/** 
 * A concrete Model for a collection of Groups consisting of a single 
 * tree rooted by given Group.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since 3.0-Beta4
 */
class GroupModel 
	extends DataBrowserModel
{

	/**
	 * Creates a new instance.
	 *
	 * @param ctx The security context.
	 * @param parent	The parent of the experimenters.
	 * @param datasets 	The collection to experimenters the model is for.
	 */
	GroupModel(SecurityContext ctx, Object parent,
			Collection<ExperimenterData> experimenters)
	{
		super(ctx);
		if (experimenters  == null) 
			throw new IllegalArgumentException("No experimenters.");
		this.parent = parent;
		Set visTrees = DataBrowserTranslator.transformExperimenters(
				experimenters);
		numberOfImages = experimenters.size();
        browser = BrowserFactory.createBrowser(visTrees);
        //layoutBrowser();
	}
	
	/**
	 * Creates a concrete loader.
	 * @see DataBrowserModel#createDataLoader(boolean, Collection)
	 */
	protected List<DataBrowserLoader> createDataLoader(boolean refresh, 
			Collection ids)
	{
		if (refresh) imagesLoaded = 0;
		if (imagesLoaded != 0 && ids != null)
			imagesLoaded = imagesLoaded-ids.size();
		if (imagesLoaded == numberOfImages) return null;
		//only load thumbnails not loaded.
		List<ImageNode> nodes = browser.getVisibleImageNodes();
		if (nodes == null || nodes.size() == 0) return null;
		Iterator<ImageNode> i = nodes.iterator();
		ImageNode node;
		List<DataObject> imgs = new ArrayList<DataObject>();
		DataObject data;
		List<Long> loaded = new ArrayList<Long>();
		if (ids != null) {
			while (i.hasNext()) {
				node = i.next();
				if (node.getThumbnail().getFullScaleThumb() == null) {
					data = (DataObject) node.getHierarchyObject();
					if (ids.contains(data.getId())) {
						if (!loaded.contains(data.getId())) {
							imgs.add(data);
							loaded.add(data.getId());
							imagesLoaded++;
						}
					}
				}
			}
		} else {
			long id;
			List<ImageData> list;
			while (i.hasNext()) {
				node = i.next();
				if (node.getThumbnail().getFullScaleThumb() == null) {
					data = (DataObject) node.getHierarchyObject();
					id = data.getId();
					if (id > 0) {
						if (!loaded.contains(id)) {
							imgs.add(data);
							loaded.add(id);
							imagesLoaded++;
						}
					}
				}
			}
		}
		if (imgs.size() == 0) return null;
		List<DataBrowserLoader> loaders = new ArrayList<DataBrowserLoader>();
		loaders.add(new ThumbnailLoader(component, ctx, sorter.sort(imgs),
				ThumbnailLoader.EXPERIMENTER, imgs.size()));
		return loaders;
	}
	
	/**
	 * Returns the type of this model.
	 * @see DataBrowserModel#getType()
	 */
	protected int getType() { return DataBrowserModel.GROUP; }

	/**
	 * No-operation implementation in our case.
	 * @see DataBrowserModel#getNodes()
	 */
	protected List<ImageDisplay> getNodes() { return null; }
	
}
