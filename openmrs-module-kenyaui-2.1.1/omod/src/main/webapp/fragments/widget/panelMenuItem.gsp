<%
	/* Creates a menu item for inclusion in panel widget */

	config.require("label");

	def styles = [ "ke-menu-item" ]
	if (config.active) {
		styles << "ke-menu-item-active"
	}

	def onClick = config.href ? """ui.navigate('${ config.href }')""" : config.onClick
%>

<div class="${ styles.join(" ") }" onclick="${ onClick }">
	<% if (config.icon && config.iconProvider) { %>
		${ ui.includeFragment("kenyaui", "widget/icon", [ iconProvider: config.iconProvider, icon: config.icon ]) }
	<% } %>

	<span class="ke-label" <% if (!config.extra) { %>style="margin-top: 7px"<% } %> >${ config.label }</span>

	<% if (config.extra) { %>
		<br />
		<span class="ke-extra">${ config.extra }</span>
	<% } %>
</div>