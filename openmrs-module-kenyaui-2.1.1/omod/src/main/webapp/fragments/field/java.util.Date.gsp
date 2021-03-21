<%
	// Supports id, formFieldName, initialValue
	// Supports showTime

	def initialDatetime, initialHour, initialMinute

	if (config.initialValue) {
		if (config.showTime) {
			initialDatetime = config.initialValue
			initialHour = config.initialValue.hours
			initialMinute = config.initialValue.minutes
		}
		else {
			initialDatetime = new Date(config.initialValue.year, config.initialValue.month, config.initialValue.date, 0, 0, 0)
			initialHour = 0
			initialMinute = 0
		}
	}
%>
<script type="text/javascript">
	jq(function() {
		jq('#${ config.id }_date').datepicker({
			dateFormat: 'dd-M-yy',
			changeMonth: true,
			changeYear: true,
			showButtonPanel: true,
			yearRange: '-120:+5',
			autoSize: true
			<% if (config.maxDate) { %>
			, maxDate: '${ config.maxDate }'
			<% } %>
			<% if (config.minDate) { %>
			, minDate: '${ config.minDate }'
			<% } %>
		});

		jq('#${ config.id }_date, #${ config.id }_hour, #${ config.id }_minute').change(function() {
			kenyaui.updateDateTimeFromDisplay('${ config.id }', ${ config.showTime ? "true" : "false" });
		});
	});
</script>
<input id="${ config.id }" type="hidden" name="${ config.formFieldName }" <% if (initialDatetime) { %>value="${ ui.dateToString(initialDatetime) }"<% } %>/>
<input id="${ config.id }_date" type="text" <% if (initialDatetime) { %>value="${ kenyaui.formatDate(initialDatetime) }"<% } %>/>
<% if (config.showTime) { %>
<select id="${ config.id }_hour"><% for (def h in 0..23) { %><option ${ initialHour == h ? "selected" : "" }>${ String.format('%02d', h) }</option><% } %></select>&nbsp;<strong>:</strong>&nbsp;<select id="${ config.id }_minute"><% for (def m in 0..59) { %><option ${ initialMinute == m ? "selected" : "" }>${ String.format('%02d', m) }</option><% } %></select>
<% } %>
<span id="${ config.id }-error" class="error" style="display: none"></span>

<% if (config.parentFormId) { %>
<script type="text/javascript">
	jq(function() {
		// Save default input values
		jq('#${ config.id }').data('default-value', jq('#${ config.id }').val());
		jq('#${ config.id }_date').data('default-value', jq('#${ config.id }_date').val());
		<% if (config.showTime) { %>
		jq('#${ config.id }_hour').data('default-value', jq('#${ config.id }_hour').val());
		jq('#${ config.id }_minute').data('default-value', jq('#${ config.id }_minute').val());
		<% } %>

		subscribe('${ config.parentFormId }.reset', function() {
			jq('#${ config.id }').val(jq('#${ config.id }').data('default-value'));
			jq('#${ config.id }_date').val(jq('#${ config.id }_date').data('default-value'));
			<% if (config.showTime) { %>
			jq('#${ config.id }_hour').val(jq('#${ config.id }_hour').data('default-value'));
			jq('#${ config.id }_minute').val(jq('#${ config.id }_minute').data('default-value'));
			<% } %>
			jq('#${ config.id }-error').html("").hide();
		});

		jq('#${ config.id }_date, #${ config.id }_hour, #${ config.id }_minute').change(function() {
			publish('${ config.parentFormId }/changed');
		});
	});
</script>
<% } %>