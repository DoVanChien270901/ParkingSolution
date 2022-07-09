jQuery(document).ready(function () {
    $('#create').click(function () {
        $('#list-index').hide();
        $('#list-create').show();
    });
    $('#index').click(function () {
        $('#list-create').hide();
        $('#list-index').show();
    });
});