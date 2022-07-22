//* Validate Register *//

// get form 
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
username.addEventListener('textInput', username_verify);
password.addEventListener('textInput', password_verify);
fullname.addEventListener('textInput', fullname_verify);
phone.addEventListener('textInput', phone_verify);
dob.addEventListener('change', dob_verify);
email.addEventListener('textInput', email_verify);
identitycard.addEventListener('textInput', identitycard_verify);

function validateRegister(){
    // user name
    if(username.value.length < 6 || username.value.length > 25){
        username.style.border = "1px solid #dc3545";
        username_error.style.display = "block";
        username.focus();
        return false;
    }
    // password 
    if(password.value.length < 6 || password.value.length > 12){
        password.style.border = "1px solid #dc3545";
        password_error.style.display = "block";
        password.focus();
        return false;
    }
    // fullname
    if(fullname.value.length < 1){
        fullname.style.border = "1px solid #dc3545";
        fullname_error.style.display = "block";
        fullname.focus();
        return false;
    }
    // phone
    if(phone.value.length < 9 || phone.value.length > 11){
        phone.style.border = "1px solid #dc3545";
        phone_error.style.display = "block";
        phone.focus();
        return false;
    }
    // dob 
    if(!dob.value){
        dob.style.border = "1px solid #dc3545";
        dob_error.style.display = "block";
        dob.focus();
        return false;
    }
    // identitycard 
    if(identitycard.value.length < 9 || identitycard.value.length > 11){
        identitycard.style.border = "1px solid #dc3545";
        identitycard_error.style.display = "block";
        identitycard.focus();
        return false;
    }
    // email
    if(email.value.length < 9){
        email.style.border = "1px solid #dc3545";
        email_error.style.display = "block";
        email.focus();
        return false;
    }
}

function username_verify(){
     if(username.value.length > 6 && username.value.length < 25){
        username.style.border = "1px solid #d4d4d4";
        username_error.style.display = "none";
        return true;
    }
}

function password_verify(){
    if(password.value.length > 6 && password.value.length < 12){
        password.style.border = "1px solid #d4d4d4";
        password_error.style.display = "none";
        return true;
    }
}

function fullname_verify(){
     if(fullname.value.length >= 2){
        fullname.style.border = "1px solid #d4d4d4";
        fullname_error.style.display = "none";
        return true;
    }
}

function dob_verify(){
    if(dob.value){
        dob.style.border = "1px solid #d4d4d4";
        dob_error.style.display = "none";
        return true;
    }
}

function phone_verify(){
     if(phone.value.length = 10){
        phone.style.border = "1px solid #d4d4d4";
        phone_error.style.display = "none";
        return true;
    }
}

function identitycard_verify(){
     if(identitycard.value.length = 10){
        identitycard.style.border = "1px solid #d4d4d4";
        identitycard_error.style.display = "none";
        return true;
    }
}

function email_verify(){
    if(email.value.length >= 10){
        email.style.border = "1px solid #d4d4d4";
        email_error.style.display = "none";
        return true;
    }
}