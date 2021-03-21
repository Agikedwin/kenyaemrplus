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

package org.openmrs.module.kenyaui.interceptor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.repository.AllAppDescriptors;
import org.openmrs.ui.framework.fragment.FragmentActionRequest;
import org.openmrs.ui.framework.fragment.FragmentFactory;
import org.openmrs.ui.framework.page.PageContext;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.session.Session;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link AppSecurityInterceptor}
 */
public class AppSecurityInterceptorTest extends BaseModuleWebContextSensitiveTest {

	private AppSecurityInterceptor interceptor;

	private AppDescriptor testApp;

	@Autowired
	private AllAppDescriptors appDescriptorsRepo;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() {
		interceptor = new AppSecurityInterceptor();

		// Create and register a single test app
		testApp = new AppDescriptor("test.app1", "", "Test App #1", null, null, null, 100);
		appDescriptorsRepo.clear();
		appDescriptorsRepo.add(testApp);
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.page.PageContext, String)
	 */
	@Test
	public void setRequestApp_shouldSetNullsIfPageAppIdIsNull() throws Exception {
		PageContext pageContext = mockPageContext("kenyaui", "test");

		interceptor.setRequestApp(pageContext, null);

		Assert.assertThat(pageContext.getRequest().getRequest().getAttribute("kenyaui.current-app"), is(nullValue()));
		Assert.assertThat(pageContext.getModel().getAttribute("currentApp"), is(nullValue()));
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.page.PageContext, String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setRequestApp_shouldThrowExceptionIfPageAppIdIsNotValid() throws Exception {
		PageContext pageContext = mockPageContext("kenyaui", "test");

		interceptor.setRequestApp(pageContext, "xxxxxxx");
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.page.PageContext, String)
	 */
	@Test
	public void setRequestApp_shouldCheckPrivilegeIfPageAppIsValid() throws Exception {
		PageContext pageContext = mockPageContext("kenyaui", "test");

		interceptor.setRequestApp(pageContext, "test.app1");

		Assert.assertThat((AppDescriptor) pageContext.getRequest().getRequest().getAttribute("kenyaui.current-app"), is(testApp));
		Assert.assertThat((AppDescriptor) pageContext.getModel().getAttribute("currentApp"), is(testApp));
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.fragment.FragmentActionRequest, String)
	 */
	@Test
	public void setRequestApp_shouldSetNullsIfActionAppIdIsNull() throws Exception {
		FragmentActionRequest actionRequest = mockFragmentActionRequest("kenyaui", "test", "thing");

		interceptor.setRequestApp(actionRequest, null);

		Assert.assertThat(actionRequest.getHttpRequest().getAttribute("kenyaui.current-app"), is(nullValue()));
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.fragment.FragmentActionRequest, String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void setRequestApp_shouldThrowExceptionIfActionAppIdIsNotValid() throws Exception {
		FragmentActionRequest actionRequest = mockFragmentActionRequest("kenyaui", "test", "thing");

		interceptor.setRequestApp(actionRequest, "xxxxxxx");
	}

	/**
	 * @see AppSecurityInterceptor#setRequestApp(org.openmrs.ui.framework.fragment.FragmentActionRequest, String)
	 */
	@Test
	public void setRequestApp_shouldCheckPrivilegeIfActionAppIsValid() throws Exception {
		FragmentActionRequest actionRequest = mockFragmentActionRequest("kenyaui", "test", "thing");

		interceptor.setRequestApp(actionRequest, "test.app1");

		Assert.assertThat((AppDescriptor) actionRequest.getHttpRequest().getAttribute("kenyaui.current-app"), is(testApp));
	}

	/**
	 * @see AppSecurityInterceptor#countNonNull(Object...)
	 */
	@Test
	public void countNonNull_shouldReturnNumberOfNonNullArguments() {
		Assert.assertThat(AppSecurityInterceptor.countNonNull(), is(0));
		Assert.assertThat(AppSecurityInterceptor.countNonNull("test", null, 123), is(2));
	}

	/**
	 * Creates a mock page context
	 * @param provider the page provider
	 * @param page the page name
	 * @return the page context
	 */
	public static PageContext mockPageContext(String provider, String page) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpSession session = new MockHttpSession();
		PageRequest pageRequest = new PageRequest(provider, page, request, response, new Session(session));
		return new PageContext(pageRequest);
	}

	/**
	 * Creates a mock page context
	 * @param provider the page provider
	 * @param page the page name
	 * @return the page context
	 */
	public static FragmentActionRequest mockFragmentActionRequest(String provider, String page, String action) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/" + provider + "/" + page + "/" + action + ".action");

		FragmentFactory factory = new FragmentFactory();
		return new FragmentActionRequest(factory, request);
	}
}