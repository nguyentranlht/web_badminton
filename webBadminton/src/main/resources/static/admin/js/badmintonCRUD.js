document.addEventListener('DOMContentLoaded', (event) => {
    generateTimeOptions();

    var openingTime = document.getElementById('openingTime');
    var closingTime = document.getElementById('closingTime');

    openingTime.addEventListener('change', validateTime);
    closingTime.addEventListener('change', validateTime);
});

function generateTimeOptions() {
    var openingTime = document.getElementById('openingTime');
    var closingTime = document.getElementById('closingTime');

    var startOptionZ = document.createElement('option');
    startOptionZ.value = "";
    startOptionZ.text = '--:--';
    openingTime.appendChild(startOptionZ);

    var endOptionZ = document.createElement('option');
    endOptionZ.value = "";
    endOptionZ.text = '--:--';
    closingTime.appendChild(endOptionZ);
    for (var hour = 0; hour < 24; hour++) {
        var hourStr = hour < 10 ? '0' + hour : hour; // Format hour with leading zero

        // Create opening time option
        var startOption = document.createElement('option');
        startOption.value = hourStr + ':00';
        startOption.text = hourStr + ':00';
        openingTime.appendChild(startOption);

        // Create closing time option
        var endOption = document.createElement('option');
        endOption.value = hourStr + ':00';
        endOption.text = hourStr + ':00';
        closingTime.appendChild(endOption);
    }
}

function validateTime() {
    var openingTimeValue = document.getElementById('openingTime').value;
    var closingTimeValue = document.getElementById('closingTime').value;
    var errorMessage = document.getElementById('error-message');

    if (openingTimeValue && closingTimeValue) {
        var startHour = parseInt(openingTimeValue.split(':')[0], 10);
        var endHour = parseInt(closingTimeValue.split(':')[0], 10);

        if (endHour - startHour < 1) {
            errorMessage.hidden = false;
            // Automatically adjust closing time to be at least one hour after opening time
            endHour = startHour + 1;
            if (endHour >= 24) endHour = 23; // Prevent endHour from exceeding 23
            closingTimeValue = (endHour < 10 ? '0' : '') + endHour + ':00';
            document.getElementById('closingTime').value = closingTimeValue;
        } else {
            errorMessage.hidden = true;
        }
    } else {
        errorMessage.hidden = true; // Hide error if either time is not selected
    }
}