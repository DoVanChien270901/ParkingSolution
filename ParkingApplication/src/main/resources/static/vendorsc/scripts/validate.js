// Regex 
let regex_email = new RegExp('[a-z0-9]+@gmail.com');
let regex_only_text = new RegExp('^[a-zA-Z_ ]*$');
// Query Selector 
const qs_username = document.querySelector('.error-username');
const qs_password = document.querySelector('.error-password');
const qs_fullname = document.querySelector('.error-fullname');
const qs_phone = document.querySelector('.error-phone');
const qs_dob = document.querySelector('.error-dob');
const qs_identitycard = document.querySelector('.error-identitycard');
const qs_email = document.querySelector('.error-email');
// Span Create Text
const span_username = document.createElement('span');
const span_password = document.createElement('span');
const span_fullname = document.createElement('span');
const span_phone = document.createElement('span');
const span_dob = document.createElement('span');
const span_identitycard = document.createElement('span');
const span_email = document.createElement('span');
// Get Form
var username = document.forms['form-register']['username'];
var password = document.forms['form-register']['password'];
var fullname = document.forms['form-register']['fullname'];
var phone = document.forms['form-register']['phone'];
var dob = document.forms['form-register']['dob'];
var email = document.forms['form-register']['email'];
var identitycard = document.forms['form-register']['identitycard'];
// error
var username_error = document.getElementById('error-username');
var password_error = document.getElementById('error-password');
var fullname_error = document.getElementById('error-fullname');
var phone_error = document.getElementById('error-phone');
var dob_error = document.getElementById('error-dob');
var email_error = document.getElementById('error-email');
var identitycard_error = document.getElementById('error-identitycard');
// addEvent
username.addEventListener('change', username_verify);
password.addEventListener('change', password_verify);
fullname.addEventListener('change', fullname_verify);
phone.addEventListener('change', phone_verify);
dob.addEventListener('change', dob_verify);
email.addEventListener('change', email_verify);

identitycard.addEventListener('change', identitycard_verify);
function validateRegister() {
    var checkYear = new Date(dob.value);
    const getYear = new Date();
    // user name
    if (username.value.length === 0 || username.value.length == '') {
        username_error.style.display = "block";
        span_username.innerHTML = "*Username can not be left blank !";
        username.style.border = "1px solid #FC6F6F";
        qs_username.appendChild(span_username);
        return false;
    }
    if (username.value.length <= 5 || username.value.length >= 26) {
        username_error.style.display = "block";
        span_username.innerHTML = "*Username must be between 6 and 25 characters !";
        username.style.border = "1px solid #FC6F6F";
        qs_username.appendChild(span_username);
        return false;
    }
    // password 
    if (password.value.length === 0 || password.value.length == '') {
        password_error.style.display = "block";
        span_password.innerHTML = "*Password can not be left blank !";
        password.style.border = "1px solid #FC6F6F";
        qs_password.appendChild(span_password);
        return false;
    }
    if (password.value.length <= 5 || password.value.length >= 13) {
        password_error.style.display = "block";
        span_password.innerHTML = "*Password must be between 6 and 12 characters !";
        password.style.border = "1px solid #FC6F6F";
        qs_password.appendChild(span_password);
        return false;
    }
    // fullname
    if (fullname.value.length === 0 || fullname.value.length == '') {
        fullname_error.style.display = "block";
        span_fullname.innerHTML = "*Fullname can not be left blank !";
        fullname.style.border = "1px solid #FC6F6F";
        qs_fullname.appendChild(span_fullname);
        return false;
    }
    if (fullname.value.length <= 3 || fullname.value.length >= 26) {
        fullname_error.style.display = "block";
        span_fullname.innerHTML = "*Fullname must be between 4 and 25 characters !";
        fullname.style.border = "1px solid #FC6F6F";
        qs_fullname.appendChild(span_fullname);
        return false;
    }
    if (regex_only_text.test(fullname.value) == false) {
        fullname_error.style.display = "block";
        span_fullname.innerHTML = "*Fullname without special characters and numbers !";
        fullname.style.border = "1px solid #FC6F6F";
        qs_fullname.appendChild(span_fullname);
        return false;
    }
    // phone
    if (phone.value.length === 0 || phone.value.length == '') {
        phone_error.style.display = "block";
        span_phone.innerHTML = "*Phone can not be left blank !";
        phone.style.border = "1px solid #FC6F6F";
        qs_phone.appendChild(span_phone);
        return false;
    }
    if (phone.value.length <= 9 || phone.value.length >= 11) {
        phone_error.style.display = "block";
        span_phone.innerHTML = "*Phone must enter 10 numbers !";
        phone.style.border = "1px solid #FC6F6F";
        qs_phone.appendChild(span_phone);
        return false;
    }
    // dob 
    if (!dob.value) {
        dob.style.opacity = "1";
        dob_error.style.display = "block";
        span_dob.innerHTML = "*Date can not be left blank !";
        dob.style.border = "1px solid #FC6F6F";
        qs_dob.appendChild(span_dob);
        return false;
    }
    if (checkYear.getFullYear() <= 1969 || checkYear >= getYear) {
        dob.style.opacity = "1";
        dob_error.style.display = "block";
        span_dob.innerHTML = "*Year must be from 1970 to current year !";
        dob.style.border = "1px solid #FC6F6F";
        qs_dob.appendChild(span_dob);
        return false;
    }
    // identitycard 
    if (identitycard.value.length === 0 || identitycard.value.length == '') {
        identitycard_error.style.display = "block";
        span_identitycard.innerHTML = "*Identity can not be left blank !";
        identitycard.style.border = "1px solid #FC6F6F";
        qs_identitycard.appendChild(span_identitycard);
        return false;
    }
    if (identitycard.value.length <= 9 || identitycard.value.length >= 16) {
        identitycard_error.style.display = "block";
        span_identitycard.innerHTML = "*Identity card must between 10 and 15 numbers !";
        identitycard.style.border = "1px solid #FC6F6F";
        qs_identitycard.appendChild(span_identitycard);
        return false;
    }
    // email
    if (email.value.length === 0 || email.value.length == '') {
        email_error.style.display = "block";
        span_email.innerHTML = "*Email can not be left blank !";
        email.style.border = "1px solid #FC6F6F";
        qs_email.appendChild(span_email);
        return false;
    }
    if (regex_email.test(email.value) == false || email.value.length < 9) {
        email_error.style.display = "block";
        span_email.innerHTML = "*Invalid email, only accept email as @gmail.com !";
        email.style.border = "1px solid #FC6F6F";
        qs_email.appendChild(span_email);
        return false;
    }
}

function username_verify() {
    if (username.value.length >= 6 && username.value.length <= 25) {
        username_error.style.display = "none";
        username.style.border = "1px solid #28a745";
        return true;
    }
}

function password_verify() {
    if (password.value.length >= 6 && password.value.length <= 12) {
        password_error.style.display = "none";
        password.style.border = "1px solid #28a745";
        return true;
    }
}

function fullname_verify() {
    if (fullname.value.length >= 4 && fullname.value.length <= 25) {
        fullname_error.style.display = "none";
        fullname.style.border = "1px solid #28a745";
        return true;
    }
}
function dob_verify() {
    var checkYear = new Date(dob.value);
    const getYear = new Date();
    if (dob.value && checkYear.getFullYear() > 1969 && checkYear < getYear) {
        dob.style.opacity = "1";
        dob_error.style.display = "none";
        dob.style.border = "1px solid #28a745";
        return true;
    }
}

function phone_verify() {
    if (phone.value.length == 10) {
        phone_error.style.display = "none";
        phone.style.border = "1px solid #28a745";
        return true;
    }
}

function identitycard_verify() {
    if (identitycard.value.length > 9 && identitycard.value.length < 16) {
        identitycard_error.style.display = "none";
        identitycard.style.border = "1px solid #28a745";
        return true;
    }
}

function email_verify() {
    if (regex_email.test(email.value) == true && email.value.length >= 9) {
        email_error.style.display = "none";
        email.style.border = "1px solid #28a745";
        return true;
    }
}
