<%
config.require("forms")

if (config.forms && config.forms.size() > 0) {
	config.forms.each { form ->
		def onClick = config.onFormClick instanceof Closure ? config.onFormClick(form) : config.onFormClick
%>
<div class="ke-stack-item ke-navigable" onclick="${ onClick }">
	${ ui.includeFragment("kenyaui", "widget/icon", [ iconProvider: form.iconProvider, icon: form.icon, useEditOverlay: true, tooltip: "Enter form" ]) }
	<b>${ form.name }</b>
	<div style="clear: both"></div>
</div>
<%
		}
	} else {
%>
<i>None</i>
<% } %>