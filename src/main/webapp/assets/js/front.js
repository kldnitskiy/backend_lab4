
(function () {
    import mymodule from 'https://cdnjs.cloudflare.com/ajax/libs/primeng/11.0.0/esm2015/primeng.js';
    var app = angular.module('myApp', ['ngRoute']);
    app.controller('canvasController', function($scope, $http){
        let r = 1;
        let points = [];
        const canvas = document.querySelector('canvas');
        canvas.addEventListener('mousedown', function(e) {
            getMousePosition(canvas, e);
        });

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

        function checkArea(x,y,r){
            if((x <= 0 && y >= 0) &&  (Math.abs(y) <= r) && -x <= (r/2)){
                return true;
            }
            else if(x >= 0 && y >= 0 && y <= (-x+(r/2))){
                return true;
            }
            else return x <= 0 && y <= 0 && r/2 >= Math.sqrt(y * y + x * x);
        }

        function sendPoints(x,y,r){
            $.ajax({
                type: "POST",
                url: "rest/points/renderPoint",
                data: {x: x, y: y, r: r},
                success: function(msg){
                    console.log(  msg );
                }
            });
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
                    sendPoints(x,y,r);
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

    });




})();