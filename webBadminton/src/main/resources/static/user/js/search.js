$(document).ready(function() {
    $('#searchForm').on('submit', function(e) {
        e.preventDefault(); // Prevent the default form submission
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        startTime = isValidTime(startTime) ? startTime : "03:00";
        endTime = isValidTime(endTime) ? endTime : "03:00";
        var startHour = parseInt(startTime.split(':')[0], 10);
        var endHour = parseInt(endTime.split(':')[0], 10);
        if ((startTime !== '03:00') && (endHour - startHour < 1 || endTime !== '03:00')) {
            $('#errorMessage').hidden = false;
            endHour = startHour + 1;
            if (endHour >= 24) endHour = 23; // Prevent endHour from exceeding 23
            endTime = (endHour < 10 ? '0' : '') + endHour + ':00';
        } else {
            $('#errorMessage').hidden = true;
        }
        var formData = {
            province: $('#province').val() != "Province"? $('#province').val() : null,
            district: $('#district').val() != "District"? $('#district').val() : null,
            ward: $('#ward').val() != "Ward"? $('#ward').val() : null,
            day: $('#date1').find('input').val() ? moment($('#date1').find('input').val(), 'MM/DD/YYYY').format('YYYY-MM-DD') : new Date().toISOString().split('T')[0],
            startTime: startTime,
            endTime: endTime
        };

        $.ajax({
            url: '/api/search',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(badmintons) {
                updateBadmintonCards(badmintons);
            },
            error: function(xhr, status, error) {
                console.error("Error occurred: " + error);
            }
        });
    });
});
function isValidTime(time) {
    return time && time.match(/^\d{2}:\d{2}$/);
}
function updateBadmintonCards(badmintons) {
    var listContainer = $('#listBadminton');
    var header = $('#header');
    listContainer.empty(); // Clear previous results
    header.empty();
    if (badmintons.length === 0) {
        header.append("<div class='text-center mb-3 pb-3'>" +
                            "<h1 style='font-size: 4rem'>Ouch!</h1>" +
                            "<h6 class='text-primary text-uppercase' style='letter-spacing: 2px; color: rgb(100, 100, 100);'>Look like there is no court match with your search</h6>" +
                            "<h6 class='text-primary text-uppercase' style='letter-spacing: 2px; color: rgb(100, 100, 100);'>Try another one?</h6>" +
                       "</div> ");
    } else {
        badmintons.forEach(function(badminton) {
            var cardHtml =
                    "<div class='col-lg-4 col-md-6 mb-4'>" +
                        "<a href='/courts/" + badminton.id + "' class='text-decoration-none text-dark'>" +
                            "<div class='package-item bg-white mb-2'>" +
                                "<img alt='Image' class='img-fluid' src='/user/img/san1.jpg' style='border-radius: 20px 20px 0 0;'>" +
                                "<div class='p-4'>" +
                                    "<div class='d-flex justify-content-between mb-3'>" +
                                        "<small class='m-0'>" +
                                            "<i class='fa fa-map-marker-alt text-primary mr-2'></i>" +
                                            badminton.location.wardName + ", " + badminton.location.districtName + ", " + badminton.location.provinceName +
                                        "</small>" +
                                    "</div>" +
                                    "<div class='d-flex justify-content-between mb-3'>" +
                                        "<small class='m-0'>" +
                                            "<i class='fa fa-clock text-primary mr-2'></i>" +
                                            badminton.openingTime + " - " + badminton.closingTime +
                                        "</small>" +
                                        "<small class='m-0'>" +
                                            "<i class='fas fa-layer-group text-primary mr-2'></i>" +
                                            badminton.courtQuantity + " court</small>"+
                                    "</div>" +
                                    "<a class='h5 text-decoration-none'>" + badminton.badmintonName + "</a>" +
                                    "<div class='border-top mt-4 pt-4'>" +
                                        "<div class='d-flex justify-content-between'>" +
                                            "<h6 class='m-0'><i class='fa fa-star text-primary mr-2'></i>" + badminton.rating + "</h6>" +
                                            "<h5 class='m-0'>" + badminton.rentalPrice + " ₫</h5>" +
                                        "</div>" +
                                    "</div>" +
                                "</div>" +
                            "</div>" +
                        "</a>" +
                    "</div>";
            listContainer.append(cardHtml);
        });
    }
}
function generateTimeOptions() {
    var startTime = document.getElementById('startTime');
    var endTime = document.getElementById('endTime');
    var startOptionZ = document.createElement('option');
    startOptionZ.value = null;
    startOptionZ.text = '--:--';
    startTime.appendChild(startOptionZ);
    var endOptionZ = document.createElement('option');
    endOptionZ.value = null;
    endOptionZ.text = '--:--';
    endTime.appendChild(endOptionZ);
    for (var hour = 0; hour < 24; hour++) {
        var hourStr = hour < 10 ? '0' + hour : hour; // Format hour with leading zero

        // Create start time option
        var startOption = document.createElement('option');
        startOption.value = hourStr + ':00';
        startOption.text = hourStr + ':00';
        startTime.appendChild(startOption);

        // Create end time option
        var endOption = document.createElement('option');
        endOption.value = hourStr + ':00';
        endOption.text = hourStr + ':00';
        endTime.appendChild(endOption);
    }
}
function validateTime() {
    var startTime = document.getElementById('startTime').value;
    var endTime = document.getElementById('endTime').value;
    var errorMessage = document.getElementById('error-message');

    if (startTime && endTime) {
        var startHour = parseInt(startTime.split(':')[0], 10);
        var endHour = parseInt(endTime.split(':')[0], 10);

        if (endHour - startHour < 1) {
            errorMessage.hidden = false;
        } else {
            errorMessage.hidden = true;
        }
    } else {
        errorMessage.hidden = true; // Hide error if either time is not selected
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    generateTimeOptions();

    var startTime = document.getElementById('startTime');
    var endTime = document.getElementById('endTime');

    startTime.addEventListener('change', validateTime);
    endTime.addEventListener('change', validateTime);
});