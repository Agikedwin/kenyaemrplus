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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ValidatingCommandObject}
 */
public class ValidatingCommandObjectTest {

	/**
	 * @see ValidatingCommandObject#require(org.springframework.validation.Errors, String)
	 */
	@Test
	public void require() {
		TestCommandObject command = new TestCommandObject();
		command.setField1("test");
		Errors errors = new BeanPropertyBindingResult(command, "");
		command.require(errors, "field1");
		command.require(errors, "field2");

		Assert.assertThat(errors.hasErrors(), is(true));
		Assert.assertThat(errors.getFieldError("field1"), is(nullValue()));
		Assert.assertThat(errors.getFieldError("field2").getCode(), is("kenyaui.error.required"));
	}

	/**
	 * Testing implementation of base class
	 */
	public static class TestCommandObject extends ValidatingCommandObject {

		private String field1;

		private String field2;

		@Override
		public void validate(Object o, Errors errors) {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}
	}
}