<%
	def visibleFieldId = config.visibleFieldId ?: ui.randomId("field")
	config.visibleFieldId = visibleFieldId
	
	ui.decorateWith("kenyaui", "labeled", config)
%>

${ ui.includeFragment("kenyaui", "widget/field", config) }

<% if (config.afterField) { %>
	${ config.afterField }
<% } %>