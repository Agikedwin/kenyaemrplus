<%
    String err = session.getAttribute(org.openmrs.ui.framework.WebConstants.OPENMRS_ERROR_ATTR, String.class);
    if (err) {
        session.setAttribute(org.openmrs.ui.framework.WebConstants.OPENMRS_ERROR_ATTR, null);
    }
    String msg = session.getAttribute(org.openmrs.ui.framework.WebConstants.OPENMRS_MSG_ATTR, String.class);
    if (msg) {
        session.setAttribute(org.openmrs.ui.framework.WebConstants.OPENMRS_MSG_ATTR, null);
    }
%>
<script type="text/javascript">
	<% if (err) { %>kenyaui.notifyError('${ ui.message(err).replace("'", "\\'") }');<% } %>
	<% if (msg) { %>kenyaui.notifySuccess('${ ui.message(msg).replace("'", "\\'") }');<% } %>
</script>