
function login(){
    $.ajax({
        type: "POST",
        url: "rest/auth/login",
        data: "name=user&pwd=pwd",
        success: function(msg){
            console.log( msg );
        }
    });
}

function register(){
    $.ajax({
        type: "POST",
        url: "rest/auth/register",
        data: "name=user&pwd=pwd",
        success: function(msg){
            console.log(  msg );
        }
    });
}

function openLoginForm() {
    document.getElementById("loginForm").style.display = "block";
    closeRegisterForm();
}

function closeLoginForm() {
    document.getElementById("loginForm").style.display = "none";
}

function openRegisterForm() {
    document.getElementById("registerForm").style.display = "block";
    closeLoginForm();
}

function closeRegisterForm() {
    document.getElementById("registerForm").style.display = "none";
}