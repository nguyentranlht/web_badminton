$(document).ready(function() {
    // Button to trigger modal
    $('.addCourtButton').on('click', function() {
        console.log("Clicked")
        var modal = new bootstrap.Modal(document.getElementById('modalAddCourt'));
        modal.show();
    });



});

function submitAddCourtForm() {
    document.getElementById('addCourtForm').submit();
}