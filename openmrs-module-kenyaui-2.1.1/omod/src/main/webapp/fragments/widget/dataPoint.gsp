<%
	config.require("label")

	def formatValue = { val ->
		if (val instanceof Date) {
			return config.showTime ? kenyaui.formatDateTime(val): kenyaui.formatDate(val)
		}
		else if (val instanceof org.openmrs.ui.framework.Link) {
			if (val.icon) {
				return """<a href="${ val.link }"><img src="${ val.icon }" class="ke-glyph" /> ${ val.label }</a>"""
			} else {
				return """<a href="${ val.link }">${ val.label }</a>"""
			}
		}
		else {
			return ui.format(val)
		}
	}

	def formattedValues = []
	def formattedExtra = null

	if (config.value instanceof Collection) {
		formattedValues = config.value.collect({ val -> formatValue(val) })
	}
	else {
		formattedValues = [ formatValue(config.value) ]
	}

	if (config.extra instanceof Date) {
		formattedExtra = kenyaui.formatDate(config.extra)
	}
	else if (config.value instanceof Date && config.showDateInterval) {
		formattedExtra = kenyaui.formatInterval(config.value)
	}
%>
<div class="ke-datapoint">
	<span class="ke-label">${ config.label }</span>:
	<%=  formattedValues.collect({ """<span class="ke-value">${ it }</span>""" }).join(", ") %>
	<% if (formattedExtra) { %><span class="ke-extra">(${ formattedExtra })</span><% } %>
</div>