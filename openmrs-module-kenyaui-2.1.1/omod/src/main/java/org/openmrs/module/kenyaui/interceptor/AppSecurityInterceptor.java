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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.kenyaui.KenyaUiConstants;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.annotation.AppAction;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.kenyaui.annotation.PublicAction;
import org.openmrs.module.kenyaui.annotation.PublicPage;
import org.openmrs.module.kenyaui.annotation.SharedAction;
import org.openmrs.module.kenyaui.annotation.SharedPage;
import org.openmrs.ui.framework.fragment.FragmentActionRequest;
import org.openmrs.ui.framework.interceptor.FragmentActionInterceptor;
import org.openmrs.ui.framework.interceptor.PageRequestInterceptor;
import org.openmrs.ui.framework.page.PageAction;
import org.openmrs.ui.framework.page.PageContext;
import org.openmrs.ui.framework.page.Redirect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Permissions checking interceptor for all UI framework page and action requests
 */
@Component
public class AppSecurityInterceptor implements PageRequestInterceptor, FragmentActionInterceptor {

	protected static final Log log = LogFactory.getLog(AppSecurityInterceptor.class);

	@Autowired
	private KenyaUiUtils kenyaUi;

	/**
	 * @see PageRequestInterceptor#beforeHandleRequest(org.openmrs.ui.framework.page.PageContext)
	 */
	@Override
	public void beforeHandleRequest(PageContext context) throws PageAction {
		Class<?> controllerClazz = context.getController().getClass();

		if (log.isDebugEnabled()) {
			log.debug("Intercepted page request to " + controllerClazz.getCanonicalName());
		}

		PublicPage publicPage = controllerClazz.getAnnotation(PublicPage.class);
		AppPage appPage = controllerClazz.getAnnotation(AppPage.class);
		SharedPage sharedPage = controllerClazz.getAnnotation(SharedPage.class);

		if (countNonNull(publicPage, appPage, sharedPage) > 1) {
			throw new RuntimeException("Page controller should have only one of the @PublicPage, @AppPage and @SharedPage annotations");
		}

		// Start by checking if a login is required
		if (publicPage == null && !Context.isAuthenticated()) {
			redirectToLogin(context, "Login is required");
		}

		String requestAppId = null;

		// Set the current request app based on @AppPage or @SharedPage
		if (appPage != null) {
			// Read app id from annotation
			requestAppId = appPage.value();
		}
		else if (sharedPage != null) {
			// Read app id from request
			requestAppId = context.getRequest().getRequest().getParameter("appId");

			if (requestAppId == null) {
				throw new RuntimeException("Shared page controller requires the appId request parameter");
			}

			List<String> allowedAppIds = Arrays.asList(sharedPage.value());

			if (allowedAppIds != null && allowedAppIds.size() > 0 && !allowedAppIds.contains(requestAppId)) {
				throw new RuntimeException("Shared page accessed with invalid appId: " + requestAppId);
			}
		}

		setRequestApp(context, requestAppId);
	}

	/**
	 * @see FragmentActionInterceptor#beforeHandleRequest(org.openmrs.ui.framework.fragment.FragmentActionRequest, java.lang.reflect.Method)
	 */
	@Override
	public void beforeHandleRequest(FragmentActionRequest request, Method controllerMethod) {
		if (log.isDebugEnabled()) {
			log.debug("Intercepted action request to " + controllerMethod.getDeclaringClass().getCanonicalName() + "." + controllerMethod.getName());
		}

		PublicAction publicAction = controllerMethod.getAnnotation(PublicAction.class);
		AppAction appAction = controllerMethod.getAnnotation(AppAction.class);
		SharedAction sharedAction = controllerMethod.getAnnotation(SharedAction.class);

		if (countNonNull(publicAction, appAction, sharedAction) > 1) {
			throw new RuntimeException("Fragment action method should have only one of the @PublicAction, @AppAction and @SharedAction annotations");
		}

		// Start by checking if a login is required
		if (publicAction == null && !Context.isAuthenticated()) {
			throw new APIAuthenticationException("Must be logged in");
		}

		String requestAppId = null;

		// Set the current request app based on @AppPage or @SharedPage
		if (appAction != null) {
			// Read app id from annotation
			requestAppId = appAction.value();
		}
		else if (sharedAction != null) {
			// Read app id from request
			requestAppId = request.getHttpRequest().getParameter("appId");

			if (requestAppId == null) {
				throw new RuntimeException("Shared action method requires the appId request parameter");
			}

			List<String> allowedAppIds = Arrays.asList(sharedAction.value());

			if (allowedAppIds != null && allowedAppIds.size() > 0 && !allowedAppIds.contains(requestAppId)) {
				throw new RuntimeException("Shared page accessed with invalid appId: " + requestAppId);
			}
		}

		setRequestApp(request, requestAppId);
	}

	/**
	 * Sets the app associated with the given page request
	 * @param pageContext the page context
	 * @param appId the app id (may be null)
	 */
	public void setRequestApp(PageContext pageContext, String appId) throws Redirect {
		AppDescriptor app = null;

		if (appId != null) {
			app = Context.getService(AppFrameworkService.class).getApp(appId);

			if (app == null) {
				throw new IllegalArgumentException("No such app with appId " + appId);
			}

			// Check logged in user has require privilege for this app
			if (!Context.hasPrivilege(app.getRequiredPrivilege())) {
				redirectToLogin(pageContext, "Insufficient privileges for " + app.getLabel() + " app");
			}
		}

		// Important to add these attributes even if null
		pageContext.getRequest().getRequest().setAttribute(KenyaUiConstants.REQUEST_ATTR_CURRENT_APP, app);
		pageContext.getModel().addAttribute(KenyaUiConstants.MODEL_ATTR_CURRENT_APP, app);
	}

	/**
	 * Sets the app associated with the given action request
	 * @param request the action request
	 * @param appId the app id (may be null)
	 */
	public void setRequestApp(FragmentActionRequest request, String appId) {
		AppDescriptor app = null;

		if (appId != null) {
			app = Context.getService(AppFrameworkService.class).getApp(appId);

			if (app == null) {
				throw new IllegalArgumentException("No such app with appId " + appId);
			}

			// Check logged in user has require privilege for this app
			if (!Context.hasPrivilege(app.getRequiredPrivilege())) {
				throw new APIAuthenticationException("Insufficient privileges for " + app.getLabel() + " app");
			}
		}

		// Important to add these attributes even if null
		request.getHttpRequest().setAttribute(KenyaUiConstants.REQUEST_ATTR_CURRENT_APP, app);
	}

	/**
	 * Performs a redirect to the login page
	 * @param pageContext the page context
	 * @param message the message to display to the user
	 */
	public void redirectToLogin(PageContext pageContext, String message) throws Redirect {
		HttpServletRequest request = pageContext.getRequest().getRequest();

		kenyaUi.notifyError(request.getSession(), message);

		String loginUrl = "login.htm";

		// Logout servlet redirects to index.htm?noredirect=true
		if (! "true".equals(request.getParameter("noredirect"))) {
			loginUrl += "?redirect=" + URLEncoder.encode(pageContext.getUrl(false));
		}

		throw new Redirect(loginUrl);
	}

	/**
	 * Counts the number of non-null arguments
	 * @param args the arguments
	 * @return the number which are non-null
	 */
	protected static int countNonNull(Object... args) {
		int count = 0;
		for (Object arg : args) {
			if (arg != null) {
				++count;
			}
		}
		return count;
	}
}