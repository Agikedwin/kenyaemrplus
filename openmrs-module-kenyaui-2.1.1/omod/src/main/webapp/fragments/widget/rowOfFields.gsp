<%
	config.require("fields") // will be passed to labeledField widget
%>
<table>
	<tr>
	<% config.fields.each { %>
		<% if (it instanceof String) { %>
			<td valign="top">${ it }</td>
		<% } else if (it.hiddenInputName) { %>
			<input type="hidden" name="${ it.hiddenInputName }" value="${ ui.convert(it.value, java.lang.String) }"/>
		<% } else { %>
			<td valign="top"><%= ui.includeFragment("kenyaui", "widget/labeledField", it) %></td>
		<% } %>
	<% } %>
	</tr>
</table>
