/* Disable Button Settings Profile */
function disable() {
    document.getElementById('fullname-profile').readOnly = false;
    document.getElementById('dob-profile').readOnly = false;
    document.getElementById('phone-profile').readOnly = false;
    document.getElementById('email-profile').readOnly = false;
    document.getElementById('identitycard-profile').readOnly = false;
    document.getElementById('update-profile').style.display = "inline-block";
    document.getElementById('cancel-profile').style.display = "inline-block";
    document.getElementById('settings-profile').style.display = "none";
}

jQuery(document).ready(function () {
    $('#booking table tbody tr').click(function () {
        var id = $(this).attr('value');
        window.location.href = "booking-details?id=" + id;
    });
});

jQuery(document).ready(function () {
    $('#shop-item-name table tbody tr').click(function () {
        var id = $(this).attr('value');
        window.location.href = "booking-details?id=" + id;
    });
});

function changeSelectAmount(value) {
    document.getElementById("amount-dp").value = (10000 * value);
}


