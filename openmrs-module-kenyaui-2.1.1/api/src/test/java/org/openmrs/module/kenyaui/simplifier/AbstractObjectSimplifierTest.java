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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.ui.framework.SimpleObject;

import static org.hamcrest.Matchers.hasEntry;

/**
 * Tests for {@link AbstractObjectSimplifier}
 */
public class AbstractObjectSimplifierTest {

	/**
	 * @see AbstractObjectSimplifier#simplify(org.openmrs.OpenmrsObject)
	 */
	@Test
	public void simplify_shouldIncludeAllCommonProperties() {
		TestSimplifier simplifier = new TestSimplifier();

		Location location = new Location();
		location.setId(123);
		location.setUuid("location-1");

		SimpleObject result = simplifier.simplify(location);

		Assert.assertThat(result, hasEntry("id", (Object) 123));
		Assert.assertThat(result, hasEntry("uuid", (Object) "location-1"));
	}

	public class TestSimplifier extends AbstractObjectSimplifier<Location> {

		@Override
		protected SimpleObject simplify(Location obj) {
			return super.simplify(obj);
		}
	}
}