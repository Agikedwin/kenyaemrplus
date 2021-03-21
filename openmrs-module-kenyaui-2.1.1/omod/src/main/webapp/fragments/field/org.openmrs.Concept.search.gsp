<%
	def searchParams = [:]

	if (config?.config?.conceptClass) {
		def conceptClass = config.config.conceptClass
		searchParams.put("class", conceptClass instanceof org.openmrs.ConceptClass ? conceptClass.id : conceptClass)
	}
	if (config?.config?.answerTo) {
		def answerTo = config.config.answerTo
		searchParams.put("answerTo", answerTo instanceof org.openmrs.Concept ? answerTo.id : answerTo)
	}
%>
${ ui.includeFragment("kenyaui", "widget/search", config.mergeAttributes([ searchType: "concept", searchParams: searchParams ])) }

<span id="${ config.id }-error" class="error" style="display: none"></span>
<% if (config.parentFormId) { %>
<script type="text/javascript">
	jq(function() {
		jq('#${ config.id }').data('default-value', jq('#${ config.id }').val());

		subscribe('${ config.parentFormId }.reset', function() {
			kenyaui.setSearchField('${ config.id }', jq('#${ config.id }').data('default-value'));
		});

		jq('#${ config.id }').on('change', function() {
			publish('${ config.parentFormId }/changed');
		});
	});
</script>
<% } %>