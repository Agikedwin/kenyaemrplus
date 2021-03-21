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

import java.util.Locale;

/**
 * Constants used by the Kenya UI
 */
public class KenyaUiConstants {

	/**
	 * Module ID
	 */
	public static final String MODULE_ID = "kenyaui";

	/**
	 * Localization
	 */
	public static final Locale LOCALE = Locale.ENGLISH;
	public static final String DATE_FORMAT = "dd-MMM-yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String DATETIME_FORMAT = "dd-MMM-yyyy HH:mm";

	/**
	 * Request attributes
	 */
	public static final String REQUEST_ATTR_CURRENT_APP = MODULE_ID + ".current-app";

	/**
	 * Page model attributes
	 */
	public static final String MODEL_ATTR_CURRENT_APP = "currentApp";
}