$(document).ready(function () {
    function updateClock() {
        let currentTime = new Date();
        let time = currentTime.getHours() + ':' + currentTime.getMinutes() + ':' + currentTime.getSeconds();
        let day = currentTime.getDate();
        let month = currentTime.getMonth()+1;
        let year = currentTime.getFullYear();
        $('#time').html(time + ' ' + day + '.' + month + '.' + year);
        setTimeout(updateClock, 5000);
    }
    updateClock();
});