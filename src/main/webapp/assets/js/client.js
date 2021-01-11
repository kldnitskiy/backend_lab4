$( document ).ready(function() {


    //Обрабатываем клики
    const canvas = document.querySelector('canvas');
    canvas.addEventListener('mousedown', function(e) {
        getMousePosition(canvas, e);
    })
    //Адаптивная таблица
    let table_height = $('.wrapper').height() - $('.graphics').height() - $('.header').height();
    $('.mainTable').css('max-height', table_height-80);
    //Рисуем график
    drawGraph();
});
let y, r,x = undefined;
let points = [];
//Получение переменных
function sendY(obj){
    y = parseFloat($(obj).val());
    console.log(y);
}
function getVars() {
    x = parseFloat($('input[type="radio"]:checked').val());
    document.getElementById("formId:r").value = r;
    if(validate()){
        drawDot(x,y,r);
        return true;
    }else{
        return false;
    }
}

function drawDot(x,y,r){
    var canvas = document.getElementById('render');
    var canvasContext = canvas.getContext('2d');
    let xCenter = 148;
    let yCenter = 74;
    let multiplyX = 93 / r;
    let multiplyY = 44 / r
    let xCoor = xCenter + x*multiplyX;
    let yCoor = yCenter - y*multiplyY;
    canvasContext.fillStyle = 'red';
    if(checkArea(x,y,r)){
        canvasContext.fillStyle = 'blue';
    }
    canvasContext.fillRect(xCoor,yCoor,5,3);
    let m = Math.pow(10,2);

    canvasContext.fillText(Math.round(x*m)/m + ':' + Math.round(y*m)/m,xCoor ,yCoor, 40);
}

function validate(){
    console.log(x);
    console.log(y);
    console.log(r);
    if(typeof x !== "undefined" && typeof y !== "undefined" && typeof r !== "undefined"){
        if(x <-4 || x > 4 || y < -5 || y > 3 || r < 1 || r > 3 || isNaN(y)){
            $('.canvasNotify').text("Проверьте правильность значений");
            console.log(false);
            return false;
        }else{
            console.log(true);
            $('.canvasNotify').text("");
            if((points.length) > 0){
                if(points[points.length-1].x == x && points[points.length-1].y == y && points[points.length-1].r ==r){
                    $('.canvasNotify').text("Эта точка была отправлена только что");
                    return false;
                }
            }

            points.push({x,y,r});
            return true;
        }
    }else{
        $('.canvasNotify').text("Некоторые значения отсутствуют");
        return false;
    }

}



function clearCanvas(){
    let canvas = document.getElementById('render');
    let canvasContext = canvas.getContext('2d');
    canvasContext.clearRect(0, 0, canvas.width, canvas.height);
}

function emptyCanvas(){
    points = [];
    let canvas = document.getElementById('render');
    let canvasContext = canvas.getContext('2d');
    canvasContext.clearRect(0, 0, canvas.width, canvas.height);
}

function reDraw(){
    clearCanvas();
    for (let i = 0; i < points.length; i++){
        drawDot(points[i].x, points[i].y, r);
    }
}

function checkArea(x,y,r){
    if((x >= 0 && y <= 0) && (x <= r) && (Math.abs(y) <= r)){
        return true;
    }
    else if(x >= 0 && y >= 0 && y <= (-x+(r/2))){
        return true;
    }
    else return x <= 0 && y >= 0 && r >= Math.sqrt(y * y + x * x);
}

function getMousePosition(canvas, event) {
    let rect = canvas.getBoundingClientRect();
    let x = event.clientX - rect.left;
    let y = event.clientY - rect.top;
    if((y >=96 && y <=360) && (x >=75 && x <=342) ){
        if(x <= 208){
            x = - (1 - ((x - 75) / (208 - 75)));
        }else{
            x = (1 - ((x - 342) / (208 - 342)));
        }
        if(y <= 228){
            y = (1 - ((y - 96) / (228 - 96)));
        }else{
            y = ((y - 228) / (228 - 360));
        }
        console.log("Coordinate x: " + x);
        console.log("Coordinate y: " + y);
        $('.canvasNotify').text('');
        if(r <1 || r > 4 || isNaN(r) || r === 0){
            $('.canvasNotify').text('Choose radius');
        }else{
            points.push({x,y,r});
            drawDot(r*x,r*y,r, 'point');
        }
    }else{
        $('.canvasNotify').text('This area is blocked');
    }
}
function drawGraph(){
    let canvas = document.getElementById('render');
    let image = new Image();
    image.src = 'images/areas.png';
    image.onload = function () {
        let canvasContext = canvas.getContext('2d');
        let wrh = image.width / image.height;
        let newWidth = canvas.width;
        let newHeight = newWidth / wrh;
        if (newHeight > canvas.height) {
            newHeight = canvas.height;
            newWidth = newHeight * wrh;
        }
        let xOffset = newWidth < canvas.width ? ((canvas.width - newWidth) / 2) : 0;
        let yOffset = newHeight < canvas.height ? ((canvas.height - newHeight) / 2) : 0;

        canvasContext.drawImage(image, xOffset, yOffset, newWidth, newHeight);
    };
}

