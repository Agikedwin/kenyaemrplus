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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.test.TestUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.util.*;

import static org.hamcrest.Matchers.*;

/**
 * Test for {@link KenyaUiUtils}
 */
public class KenyaUiUtilsTest extends BaseModuleContextSensitiveTest {

	private static final String TIME_REGEX = "\\d{2}:\\d{2}";

	private static final String DATE_REGEX = "\\d{2}-\\w{3}-\\d{4}";

	@Autowired
	private KenyaUiUtils kenyaUi;

	/**
	 * @see KenyaUiUtils#notifySuccess(javax.servlet.http.HttpSession, String)
	 */
	@Test
	public void notifySuccess_shouldSetMessageSessionAttribute() {
		MockHttpSession session = new MockHttpSession();
		kenyaUi.notifySuccess(session, "test");

		Assert.assertThat((String) session.getAttribute(WebConstants.OPENMRS_MSG_ATTR), is("test"));
	}

	/**
	 * @see KenyaUiUtils#notifyError(javax.servlet.http.HttpSession, String) )
	 */
	@Test
	public void notifyError_shouldSetErrorSessionAttribute() {
		MockHttpSession session = new MockHttpSession();
		kenyaUi.notifyError(session, "test");

		Assert.assertThat((String) session.getAttribute(WebConstants.OPENMRS_ERROR_ATTR), is("test"));
	}

	/**
	 * @see KenyaUiUtils#formatDateTime(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateTime_shouldFormatDateAndTime() throws Exception {
		DateTime date = new DateTime(1981, 5, 28, 7, 30, 12);

		Assert.assertThat(kenyaUi.formatDateTime(date.toDate()), is("28-May-1981 07:30"));
	}

	/**
	 * @see KenyaUiUtils#formatDateTime(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateTime_shouldFormatNullDateAsEmptyString() throws Exception {
		Assert.assertThat(kenyaUi.formatDateTime(null), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatDate(java.util.Date)
	 * @verifies format date as a string without time information
	 */
	@Test
	public void formatDate_shouldFormatDateWithoutTime() throws Exception {
		DateTime date = new DateTime(1981, 5, 28, 7, 30, 12);

		Assert.assertThat(kenyaUi.formatDate(date.toDate()), is("28-May-1981"));
	}

	/**
	 * @see KenyaUiUtils#formatDate(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDate_shouldFormatNullDateAsEmptyString() throws Exception {
		Assert.assertThat(kenyaUi.formatDate(null), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatDateParam(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateParam_shouldFormatDateWithTimeAsFullISO8601() throws Exception {
		DateTime date = new DateTime(1981, 5, 28, 7, 30, 12);

		Assert.assertThat(kenyaUi.formatDateParam(date.toDate()), startsWith("1981-05-28T07:30:12.000"));
	}

	/**
	 * @see KenyaUiUtils#formatDateParam(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateParam_shouldFormatDateWithoutTimeAsISO8601() throws Exception {
		DateTime date = new DateTime(1981, 5, 28, 0, 0);

		Assert.assertThat(kenyaUi.formatDateParam(date.toDate()), is("1981-05-28"));
	}

	/**
	 * @see KenyaUiUtils#formatDateParam(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateParam_shouldFormatNullDateAsEmptyString() throws Exception {
		Assert.assertThat(kenyaUi.formatDateParam(null), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatDateAuto(java.util.Date)
	 */
	@Test
	public void formatDateAuto_shouldChooseSuitableFormat() {
		Assert.assertThat(kenyaUi.formatDateParam(null), is(""));

		// Check today's date formats as just time
		Assert.assertThat(kenyaUi.formatDateAuto(new Date()).matches(TIME_REGEX), is(true));

		// Check date without time formats as just date
		Assert.assertThat(kenyaUi.formatDateAuto(TestUtils.date(2013, 5, 4)).matches(DATE_REGEX), is(true));

		// Check past date with time formats as both date and time
		Assert.assertThat(kenyaUi.formatDateAuto(TestUtils.date(2013, 5, 4, 11, 0, 0)).matches(DATE_REGEX + " " + TIME_REGEX), is(true));
	}

	/**
	 * @see KenyaUiUtils#formatDateTime(java.util.Date)
	 * @verifies format date as a string without date information
	 */
	@Test
	public void formatTime_shouldFormatTimeWithoutDate() throws Exception {
		Assert.assertThat(kenyaUi.formatTime(TestUtils.date(2013, 5, 4, 3, 2, 1)), is("03:02"));
	}

	/**
	 * @see KenyaUiUtils#formatTime(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatTime_shouldFormatNullDateAsEmptyString() throws Exception {
		Assert.assertThat(kenyaUi.formatTime(null), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatInterval(java.util.Date)
	 * @verifies return non-empty string
	 */
	@Test
	public void formatInterval_shouldReturnNonEmptyString() throws Exception {
		Assert.assertThat(StringUtils.isNotEmpty(kenyaUi.formatInterval(new Date())), is(true));
	}

	/**
	 * @see KenyaUiUtils#formatDuration(long)
	 */
	@Test
	public void formatDuration() throws Exception {
		Assert.assertThat(kenyaUi.formatDuration(1000), is("00:00:01"));
		Assert.assertThat(kenyaUi.formatDuration(4 * 60 * 60 * 1000 + 3 * 60 * 1000 + 2 * 1000), is("04:03:02"));
		Assert.assertThat(kenyaUi.formatDuration(100 * 60 * 60 * 1000 + 3 * 60 * 1000 + 2 * 1000), is("100:03:02"));
	}

	/**
	 * @see KenyaUiUtils#formatVisitDates(org.openmrs.Visit)
	 */
	@Test
	public void formatVisitDates() {
		// Check a retrospective visit
		Visit visit = new Visit();
		visit.setStartDatetime(OpenmrsUtil.firstSecondOfDay(TestUtils.date(2011, 1, 1)));
		visit.setStopDatetime(OpenmrsUtil.getLastMomentOfDay(TestUtils.date(2011, 1, 1)));
		Assert.assertThat(kenyaUi.formatVisitDates(visit), is("01-Jan-2011"));

		// Check a regular visit on single day
		visit.setStartDatetime(TestUtils.date(2011, 1, 1, 10, 0, 0));
		visit.setStopDatetime(TestUtils.date(2011, 1, 1, 11, 0, 0));
		Assert.assertThat(kenyaUi.formatVisitDates(visit), is("01-Jan-2011 10:00 \u2192 11:00"));

		// Check a regular visit spanning multiple days
		visit.setStartDatetime(TestUtils.date(2011, 1, 1, 10, 0, 0));
		visit.setStopDatetime(TestUtils.date(2011, 1, 2, 11, 0, 0));
		Assert.assertThat(kenyaUi.formatVisitDates(visit), is("01-Jan-2011 10:00 \u2192 02-Jan-2011 11:00"));

		// Check a visit with no end
		visit.setStartDatetime(TestUtils.date(2011, 1, 1, 10, 0, 0));
		visit.setStopDatetime(null);
		Assert.assertThat(kenyaUi.formatVisitDates(visit), is("01-Jan-2011 10:00"));
	}

	/**
	 * @see KenyaUiUtils#formatObsValue(org.openmrs.Obs)
	 */
	@Test
	public void formatObsValue_shouldFormatTextObs() {
		Obs obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(19));
		obs.setValueText("Testing");
		Assert.assertThat(kenyaUi.formatObsValue(obs), is("Testing"));
	}

	/**
	 * @see KenyaUiUtils#formatObsValue(org.openmrs.Obs)
	 */
	@Test
	public void formatObsValue_shouldFormatNumericObs() {
		// Check with precise concept
		Obs obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(5089));
		obs.setValueNumeric(123.4);
		Assert.assertThat(kenyaUi.formatObsValue(obs), is("123.4 kg"));

		// Check with inprecise concept
		obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(5497));
		obs.setValueNumeric(123.0);
		Assert.assertThat(kenyaUi.formatObsValue(obs), is("123 cells/mmL"));
	}

	/**
	 * @see KenyaUiUtils#formatObsValue(org.openmrs.Obs)
	 */
	@Test
	public void formatObsValue_shouldFormatDatetimeObs() {
		Obs obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(20));
		obs.setValueDatetime(new DateTime(2012, 5, 4, 3, 2, 1).toDate());
		Assert.assertThat(kenyaUi.formatObsValue(obs), is("04-May-2012 03:02"));
	}

	/**
	 * @see KenyaUiUtils#formatPersonName(org.openmrs.Person)
	 */
	@Test
	public void formatPersonName_shouldFormatPersonName() {
		PersonName pn = new PersonName();
		pn.setFamilyName("fff");
		pn.setGivenName("ggg");
		pn.setMiddleName("mmm");

		Assert.assertThat(kenyaUi.formatPersonName(pn), is("fff, ggg mmm"));

		// Check no middle name
		pn = new PersonName();
		pn.setFamilyName("fff");
		pn.setGivenName("ggg");

		Assert.assertThat(kenyaUi.formatPersonName(pn), is("fff, ggg"));

		// Check with null
		Assert.assertThat(kenyaUi.formatPersonName((PersonName) null), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatPersonBirthdate(org.openmrs.Person)
	 */
	@Test
	public void formatPersonBirthdate() {
		Assert.assertThat(kenyaUi.formatPersonBirthdate(TestUtils.date(2000, 1, 1), false), is("01-Jan-2000"));
		Assert.assertThat(kenyaUi.formatPersonBirthdate(TestUtils.date(1980, 6, 30), true), is("approx 30-Jun-1980"));
		Assert.assertThat(kenyaUi.formatPersonBirthdate(null, true), is(""));
	}

	/**
	 * @see KenyaUiUtils#formatPersonBirthdate(org.openmrs.Person)
	 */
	@Test
	public void formatPersonBirthdate_withPerson() {
		Person p = new Person();
		p.setBirthdate(TestUtils.date(2000, 1, 1));
		Assert.assertThat(kenyaUi.formatPersonBirthdate(p), is("01-Jan-2000"));

		p = new Person();
		p.setBirthdate(TestUtils.date(1980, 6, 30));
		p.setBirthdateEstimated(true);
		Assert.assertThat(kenyaUi.formatPersonBirthdate(p), is("approx 30-Jun-1980"));
	}

	/**
	 * @see KenyaUiUtils#formatPersonGender(org.openmrs.Person)
	 */
	@Test
	public void formatPersonGender() {
		Person p = new Person();
		p.setGender("M");
		Assert.assertThat(kenyaUi.formatPersonGender(p), is("\u2642 Male"));

		p = new Person();
		p.setGender("F");
		Assert.assertThat(kenyaUi.formatPersonGender(p), is("\u2640 Female"));

		p = new Person();
		p.setGender("Unknown");
		Assert.assertThat(kenyaUi.formatPersonGender(p), is("Unknown"));
	}

	/**
	 * @see KenyaUiUtils#formatUser(org.openmrs.User)
	 */
	@Test
	public void formatUser() {
		User user = Context.getAuthenticatedUser();

		Assert.assertThat(kenyaUi.formatUser(user), is("User, Super "));
	}

	/**
	 * @see KenyaUiUtils#formatBytes(long)
	 */
	@Test
	public void formatBytes() {
		Assert.assertThat(kenyaUi.formatBytes(10), is("10 B"));
		Assert.assertThat(kenyaUi.formatBytes(1024), is("1.0 KiB"));
		Assert.assertThat(kenyaUi.formatBytes(10 * 1024), is("10.0 KiB"));
		Assert.assertThat(kenyaUi.formatBytes(10 * 1024 * 1024), is("10.0 MiB"));
		Assert.assertThat(kenyaUi.formatBytes(10 * 1024 * 1024 + 123), is("10.0 MiB"));
	}
}