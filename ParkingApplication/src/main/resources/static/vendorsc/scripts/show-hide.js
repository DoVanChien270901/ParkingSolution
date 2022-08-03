//$(document).ready(function() {
//    $('#list-index').show();
//    $('#create').click(function() {
//        $('#list-create').show();
//        $('#list-index').hide();
//    });
//    $('#index').click(function() {
//        $('#list-create').hide();
//        $('#list-index').show();
//    });
//});


$(document).ready(function () {
    $('#list-user table tbody tr').click(function () {
        var username = $(this).attr('value');
        window.location.href = "/user-details?id=" + username;
    });
}); 