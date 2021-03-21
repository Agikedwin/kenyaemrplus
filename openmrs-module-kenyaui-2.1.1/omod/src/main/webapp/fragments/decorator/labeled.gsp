<% if (config.label) { %>
    <label <% if (config.visibleFieldId) { %>for="${ config.visibleFieldId }"<% } %> class="ke-field-label">
        ${config.label}
    </label>
    <span class="ke-field-content">
<% } %>

${ config.content }

<% if (config.label) { %>
    </span>
<% } %>