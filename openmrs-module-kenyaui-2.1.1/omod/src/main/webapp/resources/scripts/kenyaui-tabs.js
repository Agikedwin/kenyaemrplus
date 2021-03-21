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

/**
 * Tabs widget
 */
(function(kenyatab, $) {

	/**
	 * DOM initialization
	 */
	$(function() {
		$('.ke-tabs').each(function() {
			var tabs = $(this);

			// Make first tab active
			var firstTabId = tabs.find('.ke-tabmenu-item:first').data('tabid');
			kenyatab.activateTab(tabs.attr('id'), firstTabId);

			// Configure click handlers for each tab button
			tabs.find('.ke-tabmenu-item').click(function() {
				// Get tabid from data attribute
				var tabId = $(this).data('tabid');

				kenyatab.activateTab(tabs.attr('id'), tabId);
			});
		});
	});

	/**
	 * Activates a tab
	 * @param tabsId the tabs widget id
	 * @param tabId the specific tab id
	 */
	kenyatab.activateTab = function(tabsId, tabId) {
		var tabs = $('#' + tabsId);

		// Make only this tab's menu item active
		tabs.find('.ke-tabmenu-item').each(function() {
			var menuItem = $(this);

			if (menuItem.data('tabid') == tabId) {
				menuItem.addClass('ke-tabmenu-item-active');
			} else {
				menuItem.removeClass('ke-tabmenu-item-active');
			}
		});

		// Make only this tab's content visible
		tabs.find('.ke-tab').each(function() {
			var tab = $(this);

			if (tab.data('tabid') == tabId) {
				tab.show();
			} else {
				tab.hide();
			}
		});
	};

}( window.kenyatab = window.kenyatab || {}, jQuery ));