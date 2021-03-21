<%
	// Supports heading
%>
<div class="ke-panel-frame">
	<% if (config.heading) { %>
	<div class="ke-panel-heading">${ config.heading }</div>
	<% } %>
	<% if (config.frameOnly) { %>
	<%= config.content %>
	<% } else { %>
	<div class="ke-panel-content"><%= config.content %></div>
	<% } %>
</div>