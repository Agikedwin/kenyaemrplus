<%
	config.require("itemFormatter") // name of a function(item), will be used to paint each result
	// supports numResultsFormatter, name of a function(items), should return something like "5 patient(s) found"
	// supports clickFunction, name of a function() whose this will be the panel, if clicked
%>

<div id="${ config.id }">
	<div class="ke-panel-controls status"></div>
	<div class="ke-panel-content results"></div>
</div>

<script type="text/javascript">
subscribe("${ config.id }/show", function(event, data) {
	jq('#${ config.id } > .results').html('');

	if (data.length > 0) {
		jq('#${ config.id } > .status').html(${ config.numResultsFormatter }(data));
		jq('#${ config.id } > .results').show();

		for (var i = 0; i < data.length; ++i) {
			var html = ${ config.itemFormatter }(data[i]);
			jq(html)
					.appendTo(jq('#${ config.id } > .results'))
					<% if (config.clickFunction) { %> .click(${ config.clickFunction }) <% } %>
		}

	} else {
		jq('#${ config.id } > .status').html('${ ui.message("general.none") }');
		jq('#${ config.id } > .results').hide();
	}
});
</script>