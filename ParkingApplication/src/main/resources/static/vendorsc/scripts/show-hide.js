$(document).ready(function() {
    $('#list-index').show();
    $('#create').click(function() {
        $('#list-create').show();
        $('#list-index').hide();
    });
    $('#index').click(function() {
        $('#list-create').hide();
        $('#list-index').show();
    });
});