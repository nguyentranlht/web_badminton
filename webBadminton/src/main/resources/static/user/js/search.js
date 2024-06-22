$(document).ready(function() {
    $('#searchForm').on('submit', function(e) {
        e.preventDefault(); // Prevent the default form submission

        var formData = {
            province: $('#province').val(),
            district: $('#district').val(),
            ward: $('#ward').val(),
            day: $('#date1').val(),
            startTime: $('#startTime').val(),
            endTime: $('#endTime').val()
        };

        $.ajax({
            url: '/badminton/search',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(data) {
                // Assuming 'data' is the HTML to be displayed
                $('#results').html(data);
            },
            error: function(xhr, status, error) {
                console.error("Error occurred: " + error);
            }
        });
    });
});