/**
 * 
 */

function validateTime() {
	var startTime = Date.parse('20 Aug 2000 '
		+ document.getElementById('lectureStartTime').value);
	var endTime = Date.parse('20 Aug 2000 '
		+ document.getElementById('lectureEndTime').value);
	if (startTime >= endTime) {
		document.getElementById("wrongTimeWarning").removeAttribute(
			"hidden");
	} else {
		document.getElementById("wrongTimeWarning").setAttribute(
			"hidden", "hidden");
	}
}