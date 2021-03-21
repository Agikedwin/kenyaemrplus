<%
	def onblur = "kenyaui.clearFieldErrors('" + config.id + "');"

	if (config.required) {
		onblur += " kenyaui.validateRequiredField('" + config.id + "');"
	}

	onblur += " kenyaui.validateIntegerField('" + config.id + "');"
%>
<input id="${ config.id }" type="text" size="5" name="${ config.formFieldName }" value="${ config.initialValue ?: "" }" onblur="${ onblur }"/>

<span id="${ config.id }-error" class="error" style="display: none"></span>

<% if (config.parentFormId) { %>
<script>
	FieldUtils.defaultSubscriptions('${ config.parentFormId }', '${ config.formFieldName }', '${ config.id }');
	jq(function() {
		jq('#${ config.id }').keyup(function() {
			publish('${ config.parentFormId }/changed');
		});
	});
</script>
<% } %>