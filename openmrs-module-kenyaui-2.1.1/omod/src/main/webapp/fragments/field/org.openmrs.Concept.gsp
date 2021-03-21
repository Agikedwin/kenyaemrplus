<% if (config?.config?.style == "search") { %>
	${ ui.includeFragment("kenyaui", "field/org.openmrs.Concept.search", config) }
<% } else if (config?.config?.style == "list") { %>
	${ ui.includeFragment("kenyaui", "field/org.openmrs.Concept.list", config) }
<% } else {
	throw new RuntimeException("Concept field " + config.formFieldName + " style config must be 'list' or 'search'")
} %>