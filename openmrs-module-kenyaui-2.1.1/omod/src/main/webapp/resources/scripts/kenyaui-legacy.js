/**
 * Everything in this file needs replaced with something else
 */

//// Validation ///////////////////

function isValidNumber(val) {
	var asNum = Number(val);
	return !isNaN(asNum);
}

function isValidInteger(val) {
	var asNum = Number(val);
	return !isNaN(asNum) && (asNum == Math.round(asNum));
}

//// Event Bus support //////////////////

function publish(message, payload) {
	window.PageBus.publish(message, payload);
}

function subscribe(message, callback) {
	return window.PageBus.subscribe(message, null, callback, null);
}

//// Utils //////////////////

function escapeJs(string) {
	string = string.replace(/'/g, "\\'");
	string = string.replace(/"/g, '\\"');
	return string;
}

function escapeHtml(string) {
	string = string.replace(/</g, "&lt;");
	string = string.replace(/>/g, "&gt;");
	return string;
}

var FieldUtils = {
	defaultSubscriptions: function(formId, formFieldName, fieldId) {

		// Save default field value
		$('#' + fieldId).data('default-value', $('#' + fieldId).val());

		// On form reset, set field to it's default value
		subscribe(formId + '.reset', function() {
		    $('#' + fieldId).val($('#' + fieldId).data('default-value'));
		    $('#' + fieldId + '-error').html("").hide();
		});
	}
}