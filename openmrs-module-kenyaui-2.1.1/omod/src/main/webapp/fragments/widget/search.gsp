<%
	config.require("searchType")

	def searchParamsStr = config.searchParams ? (config.searchParams.collect { key, value -> (key + "=" + value) } .join("&amp;")) : ""
%>
<input type="hidden" id="${ config.id }" name="${ config.formFieldName }" class="ke-search" data-searchtype="${ config.searchType }" data-searchparams="${ searchParamsStr }" <% if (config.initialValue) {%> value="${ config.initialValue.id }"<% } %> />