<%
	/* Deprecated - use <button type="button"> instead */

	// Supports icon, iconProvider, iconOverlay, label, extra
	// Supports onClick (javascript snippet)
	// Supports href (link url)
	// Supports cssClass (applied to button)

	def id = config.id ?: ui.randomId("button")

	def cssClass = config.cssClass ?: "ke-button"
%>

<% if (config.extra) { %>

<div id="${ id }" class="${ cssClass }">
	<% if (config.icon && config.iconProvider) { %>
		${ ui.includeFragment("kenyaui", "widget/icon", [ iconProvider: config.iconProvider, icon: config.icon, iconOverlay: config.iconOverlay, iconOverlayProvider: config.iconOverlayProvider ]) }
	<% } %>
	<div class="ke-button-text">
	<% if (config.label) { %>
		<div class="ke-label">${ config.label }</div>
	<% } %>
	<% if (config.extra) { %>
		<div class="ke-extra">${ config.extra }</div>
	<% } %>
	</div>
</div>

<% } else { %>

<button id="${ id }" type="button">
	<% if (config.icon && config.iconProvider) { %><img src="${ ui.resourceLink(config.iconProvider, "images/" + config.icon) }" /><% } %>
	${ config.label }
</button>

<% } %>

<% if (config.href || config.onClick) { %>
<script type="text/javascript">
jQuery(function() {
	jQuery('#${ id }').click(function() {
		<% if (config.onClick) { %>
			${ config.onClick }
		<% } else if (config.href) { %>
			ui.navigate('${ ui.escapeJs(config.href) }');
		<% } %>
	});
});
</script>
<% } %>