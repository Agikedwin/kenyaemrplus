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

package org.openmrs.module.kenyaui.form;

import org.openmrs.OpenmrsData;
import org.openmrs.api.context.Context;

import java.util.Date;

/**
 * Abstract base class for web forms
 */
public abstract class AbstractWebForm extends ValidatingCommandObject {

	/**
	 * Saves the form
	 */
	public abstract Object save();

	/**
	 * Voids the specified data object
	 * @param data the data
	 */
	public void voidData(OpenmrsData data) {
		if (!data.isVoided()) {
			data.setVoided(true);
			data.setDateVoided(new Date());
			data.setVoidedBy(Context.getAuthenticatedUser());
		}
	}
}