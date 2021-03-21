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

package org.openmrs.module.kenyaui.simplifier;

import org.openmrs.OpenmrsMetadata;
import org.openmrs.ui.framework.SimpleObject;

/**
 * Abstract simplifier for OpenmrsMetadata subclasses
 */
public abstract class AbstractMetadataSimplifier<T extends OpenmrsMetadata> extends AbstractObjectSimplifier<T> {

	/**
	 * @see AbstractObjectSimplifier#simplify(org.openmrs.OpenmrsObject)
	 */
	protected SimpleObject simplify(T obj) {
		SimpleObject ret = super.simplify(obj);
		ret.put("name", obj.getName());
		ret.put("description", obj.getDescription());
		return ret;
	}
}