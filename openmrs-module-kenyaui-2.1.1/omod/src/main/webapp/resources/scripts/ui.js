/**
 * UI Utils
 *
 * This class mirrors the functionality of UiUtils.java and adds a few extras such as reloadPage() and navigate()
 */
(function(ui, $) {

	/**
	 * Helper method to convert a list of parameters to a query string
	 * @param options
	 * @returns {string}
	 */
	function toQueryString(params) {
		var ret = "?";
		if (params) {
			for (key in params) {
				var val = params[key];
				if (val != null) {
					// If value is an object then serialize it
					if ($.isPlainObject(val)) {
						val = $.param(val);
					}

					ret += key + '=' + encodeURIComponent(val) + '&';
				}
			}
		}
		return ret;
	}
	
	var confirmBeforeNavigationSetup = {
		configured: false,
		enabled: false
	};

	/**
	 * Returns a link to UI framework managed page
	 * @param providerName the page provider
	 * @param pageName the page path
	 * @param params the query string parameters
	 * @returns {string}
	 */
	ui.pageLink = function(providerName, pageName, params) {
		var ret = '/' + OPENMRS_CONTEXT_PATH + '/' + providerName + '/' + pageName + '.page';
		return ret + toQueryString(params);
	};

	/**
	 * Returns a link to a UI framework managed resource
	 * @param providerName the resource provider
	 * @param resourcePath the resource path
	 * @returns {string} the link
	 */
	ui.resourceLink = function(providerName, resourcePath) {
		if (providerName == null) {
			providerName = '*';
		}
		return '/' + OPENMRS_CONTEXT_PATH + '/ms/uiframework/resource/' + providerName + '/' + resourcePath;
	};

	ui.fragmentActionLink = function(providerName, fragmentName, actionName, params) {
		var ret = '/' + OPENMRS_CONTEXT_PATH + '/' + providerName + '/' + fragmentName + '/' + actionName + '.action';
		if (params) {
			ret += toQueryString(params);
		}
		return ret;
	};

	ui.getFragmentActionAsJson = function(providerName, fragmentName, actionName, params, callback) {
		var url = this.fragmentActionLink(providerName, fragmentName, actionName, null);

		$.getJSON(url, params, function(result) {
			if (callback) {
				callback(result);
			}
		});
	};

	/**
	 * Reloads the current page
	 */
	ui.reloadPage = function() {
		location.reload();
	};

	/**
	 * Navigates to a new page
	 * @param providerName the page provider or complete URL if no other args are provided
	 * @param pageName the page name
	 * @param options the page options
	 */
	ui.navigate = function(providerName, pageName, options) {
		// If a single argument, interpret as a complete URL
		location.href = (arguments.length == 1) ? providerName : this.pageLink(providerName, pageName, options);
	};

	ui.escapeHtmlAttribute = function(string) {
		// TODO actually implement this
		string = string.replace("'", "\'");
		string = string.replace('"', '\\"');
		return string;
	};

	ui.confirmBeforeNavigating = function(formSelector) {
		if (!confirmBeforeNavigationSetup.configured) {
			window.onbeforeunload = function() {
				if (confirmBeforeNavigationSetup.enabled) {
					var blockers = $('.confirm-before-navigating').filter(function() {
						return ($(this).data('confirmBeforeNavigating') === 'dirty');
					}).length > 0;

					if (blockers) {
						return "If you leave this page you will lose unsaved changes";
					}
				}
			}
			confirmBeforeNavigationSetup.configured = true;
			confirmBeforeNavigationSetup.enabled = true;
		}

		var form = $(formSelector);

		form.addClass('confirm-before-navigating');
		form.data('confirmBeforeNavigating', 'clean');
		form.find(':input').on('change.confirm-before-navigating', function() {
			$(this).parents('.confirm-before-navigating').data('confirmBeforeNavigating', 'dirty');
		});
	};

	ui.cancelConfirmBeforeNavigating = function(formSelector) {
		var form = $(formSelector);
		form.find(':input').off('change.confirm-before-navigating');
		form.data('confirmBeforeNavigating', null);
		form.removeClass('confirm-before-navigating');
	};

	ui.disableConfirmBeforeNavigating = function() {
		confirmBeforeNavigationSetup.enabled = false;
	};

	ui.enableConfirmBeforeNavigating = function() {
		confirmBeforeNavigationSetup.enabled = true;
	};

}( window.ui = window.ui || {}, jQuery ));