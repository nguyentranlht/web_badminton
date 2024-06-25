$(document).ready(function() {
    $('#searchForm').on('submit', function(e) {
        e.preventDefault(); // Prevent the default form submission
        var formData = {
            province: $('#province').val() != "Province"? $('#province').val() : null,
            district: $('#district').val() != "District"? $('#district').val() : null,
            ward: $('#ward').val() != "Ward"? $('#ward').val() : null,
            day: $('#date1').val() ? $('#date1').val() : new Date().toISOString().split('T')[0],
            startTime: $('#startTime').val() ? $('#startTime').val() : null,
            endTime: $('#endTime').val() ? $('#endTime').val() : null
        };

        $.ajax({
            url: '/badmintons/search',
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

function updateBadmintonCards(badmintons) {
    var listContainer = $('#listBadminton');
    var header = $('#header');
    listContainer.empty(); // Clear previous results
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
                                "<img class='img-fluid' src='" + badminton.imageUrl + "' alt='Image' style='border-radius: 20px 20px 0 0;'>" +
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
