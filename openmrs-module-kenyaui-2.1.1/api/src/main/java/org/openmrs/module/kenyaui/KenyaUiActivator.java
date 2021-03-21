/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyaui;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class KenyaUiActivator implements ModuleActivator {
	
	protected static final Log log = LogFactory.getLog(KenyaUiActivator.class);
		
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Kenya UI Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Kenya UI Library refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Kenya UI Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		log.info("Kenya UI Module started");

		setGlobalProperty("uiframework.formatter.dateFormat", KenyaUiConstants.DATE_FORMAT);
		setGlobalProperty("uiframework.formatter.timeFormat", KenyaUiConstants.TIME_FORMAT);
		setGlobalProperty("uiframework.formatter.dateAndTimeFormat", KenyaUiConstants.DATETIME_FORMAT);
		setGlobalProperty("htmlformentry.dateFormat", KenyaUiConstants.DATE_FORMAT);
		setGlobalProperty("htmlformentry.showDateFormat", "false");
		setGlobalProperty("reporting.defaultDateFormat", KenyaUiConstants.DATE_FORMAT);
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Kenya UI Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Kenya UI Module stopped");
	}

	/**
	 * Sets the given global property exists with the given value
	 * @param property the property name
	 * @param value the property value
	 */
	protected void setGlobalProperty(String property, String value) {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(property);
		if (gp == null) {
			gp = new GlobalProperty();
			gp.setProperty(property);
		}

		gp.setPropertyValue(value);

		Context.getAdministrationService().saveGlobalProperty(gp);
	}
}