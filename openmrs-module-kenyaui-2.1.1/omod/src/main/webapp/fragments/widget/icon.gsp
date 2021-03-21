<%
    // Supports icon, iconProvider, iconOverlay, iconOverlayProvider
	// Supports useEditOverlay, useViewOverlay
	// Supports tooltip

	config.require("iconProvider")
	config.require("icon")

	def iconOverlay, iconOverlayProvider

	if (config.useEditOverlay) {
		iconOverlayProvider = "kenyaui"
		iconOverlay = "buttons/_overlay_edit.png"
	}
	else if (config.useViewOverlay) {
		iconOverlayProvider = "kenyaui"
		iconOverlay = "buttons/_overlay_view.png"
	}
	else {
		iconOverlay = config.iconOverlay
		iconOverlayProvider = config.iconOverlayProvider
	}

	def tooltip = config.tooltip ?: ""
%>
<div class="ke-icon">
	<div style="position: relative">
		<img src="${ ui.resourceLink(config.iconProvider, "images/" + config.icon) }" alt="${ tooltip }" />
		<% if (iconOverlay && iconOverlayProvider) { %>
		<img src="${ ui.resourceLink(config.iconOverlayProvider, "images/" + iconOverlay) }" style="position: absolute; top: 0; right: 0; z-index: 1" alt="${ tooltip }" />
		<% } %>
	</div>
</div>