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

package org.openmrs.module.kenyaui.test;

import org.junit.Ignore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility methods for unit tests
 */
@Ignore
public class TestUtils {

	/**
	 * Convenience method to create a new date
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @return the date
	 * @throws IllegalArgumentException if date values are not valid
	 */
	public static Date date(int year, int month, int day) {
		return date(year, month, day, 0, 0, 0);
	}

	/**
	 * Convenience method to create a new date with time
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 * @return the date
	 * @throws IllegalArgumentException if date values are not valid
	 */
	public static Date date(int year, int month, int day, int hour, int minute, int second) {
		Calendar cal = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		cal.setLenient(false);
		return cal.getTime();
	}
}