<%
	if (!config?.config?.answerTo && !config?.config?.options) throw new RuntimeException("Concept field currently only supports config.answerTo and config.options mode (error for formFieldName=${ config.formFieldName })")

	def options = [ [ id: "", name: "" ] ]
	if (config?.config?.options) {
		options.addAll(config.config.options.collect {
			[ id: it.id, name: ui.format(it) ]
		});
	} else {
		options.addAll(config.config.answerTo.answers.collect {
			[ id: it.answerConcept.id, name: ui.format(it.answerConcept) ]
		})
	}
%>

<%= ui.includeFragment("kenyaui", "widget/selectList", [
		id: config.id,
		selected: [ config?.initialValue?.id ],
		formFieldName: config.formFieldName,
		options: options,
		optionsDisplayField: 'name',
		optionsValueField: 'id'
]) %>

<span id="${ config.id }-error" class="error" style="display: none"></span>

<% if (config.parentFormId) { %>
<script type="text/javascript">
	FieldUtils.defaultSubscriptions('${ config.parentFormId }', '${ config.formFieldName }', '${ config.id }');
	jq(function() {
		jq('#${ config.id }').change(function() {
			publish('${ config.parentFormId }/changed');
		});
	});
</script>
<% } %>