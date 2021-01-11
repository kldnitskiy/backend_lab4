
let y, r,x = undefined;
let points = [];
let menu_clicker = false;
let active_screen = 0;
function optimizeTable(){
  reDraw();
  if($(window).width() > 852){
    $('.menu_mobile').hide();
    menu_clicker = false;
    let table_height = $(window).height() - $('.graphics').height() - $('.header').height();
    $('.mainTable').css('max-height', table_height-160);
     $('.app-graphform').show();
     $('.app-table').show();
     $('.app-graph').show();
    console.log($(window).height());
  }else{
    reStoreView();
    let table_height = 150;
    $('.mainTable').css('max-height', $(window).height() -250);
  }
}

function reStoreView(){
  if(active_screen === 0 || active_screen === 1){
    $('.app-graphform').hide();
    $('.app-table').hide();
    $('.app-graph').show();
  }else if(active_screen === 2){
    $('.app-graphform').show();
    $('.app-table').hide();
    $('.app-graph').hide();
  }else{
    $('.app-graphform').hide();
    $('.app-table').show();
    $('.app-graph').hide();
  }
}

window.onresize = function(event) {
  optimizeTable();
};
$(window).load(function (){
  if(window.location.href.includes("main")){
    clearCanvas();
    //Обрабатываем клики



  }
})
function drawGraph(){
  let canvas = document.getElementById('render');
  let image = new Image();
  //image.src = 'assets/images/areas.png';
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

    canvasContext.drawImage(image,
      canvas.width / 2 - image.width / 2,
      canvas.height / 2 - image.height / 2
    );
    // canvasContext.drawImage(image, xOffset, yOffset, newWidth, newHeight);
  };
}
function drawDot(x,y,r, mode){
  var canvas = document.getElementById('render');
  var canvasContext = canvas.getContext('2d');
  let xCenter = 148;
  let yCenter = 74;
  let multiplyX = 93 / r;
  let multiplyY = 44 / r;
  if($(window).width() < 851 && $(window).width() > 575){
    multiplyX = 108 / r;
    multiplyY = 52 / r;
  }
  let xCoor = xCenter + x*multiplyX;
  let yCoor = yCenter - y*multiplyY;
  canvasContext.fillStyle = 'red';
  if(checkArea(x,y,r)){
    canvasContext.fillStyle = 'blue';
  }
  canvasContext.fillRect(xCoor,yCoor,5,3);
  let m = Math.pow(10,2);
  canvasContext.fillText(Math.round(x*m)/m + ':' + Math.round(y*m)/m,xCoor ,yCoor, 40);
  if(mode){
    sendClickPoints(Math.round(x*m)/m,Math.round(y*m)/m,r);
  }

}

function sendClickPoints(x,y,r){
  $.ajax({
    url: "rest/points/save",
    type: "POST",
    dataType: "json",
    crossDomain: true,
    contentType: "application/x-www-form-urlencoded; charset=utf-8",
    data: { x: x, y: y, r:r, username: localStorage.getItem("username") },
    headers: {
      Authorization: "ENIGMA " + localStorage.getItem('token'),
    },
    cache: false,
    beforeSend: function (xhr) {
      /* Authorization header */
      xhr.setRequestHeader("Authorization", "ENIGMA " + localStorage.getItem('token'));
      xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

    },
    success: function (data) {
      console.log(data);
    $('#resultTableBody').append("<tr><td>"+x+"</td><td>"+y+"</td><td>"+r+"</td><td>"+data.point_status+"</td></tr>");
      tableIconShow();
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(data);
    }
  });


  function onAjaxSuccess(data)
  {
    // Здесь мы получаем данные, отправленные сервером и выводим их на экран.
    console.log(data);
  }
}
function checkArea(x,y,r){
  if((x <= 0 && y >= 0) && (Math.abs(x) <= r/2) && (Math.abs(y) <= r)){
    return true;
  }
  else if(x >= 0 && y >= 0 && y <= (-x+(r/2))){
    return true;
  }
  else return x <= 0 && y <= 0 && r/2 >= Math.sqrt(y * y + x * x);
}

function getMousePosition(canvas, event) {
  let rect = canvas.getBoundingClientRect();
  let x = event.clientX - rect.left;
  let y = event.clientY - rect.top;
  let x_bound = $('canvas').width() / 2;
  let y_bound = $('canvas').height() / 2;
  let corrX = $('canvas').width() / 420;
  let corrY = $('canvas').height() / 450;
  let x1 = 75 * corrX;
  let x2 = 342 * corrX;
  let x3 = 208 * corrX;
  let y1 = 96 * corrY;
  //let y1 = 76* corrY;
  let y2 = 360 * corrY;
  //let y2 = 425* corrY;
  let y3 = 228 * corrY;
  //let y3 = 250* corrY;
  console.log(x);
  if($(window).width() < 851 && $(window).width() > 575){
    x1 = 66;
    x2 = 472;
    x3 = 270;
    y1 = 88;
    y3 = 289;
    y2 = 489;
  }
    if(x <= x_bound){
      x = - (1 - ((x - x1) / (x3 - x1)));
    }else{
      x = (1 - ((x - x2) / (x3 - x2)));
    }
    if(y <= y_bound){
      y = (1 - ((y - y1) / (y3 - y1)));
    }else{
      y = ((y - y3) / (y3 - y2));
    }

    $('.canvasNotify').text('');
    if(r <1 || r > 4 || isNaN(r) || r === 0){
      $('.canvasNotify').text('<- Choose radius');
    }else{
      points.push({x,y,r});
      drawDot(r*x,r*y,r, 'point', true);
    }
  }

function showGraph(){
  $('.app-graph').fadeIn();
  $('.app-graphform').fadeOut();
  $('.app-table').fadeOut();
  active_screen = 1;
}
function showForm(){
  $('.app-graphform').fadeIn();
  $('.app-table').fadeOut();
  $('.app-graph').fadeOut();
  active_screen = 2;
}
function showTable(){
  console.log(1);
  $('.app-table').fadeIn();
  $('.app-graphform').fadeOut();
  $('.app-graph').fadeOut();
  active_screen = 3;
}
function emptyCanvas(){
  points = [];
  let canvas = document.getElementById('render');
  let canvasContext = canvas.getContext('2d');
  canvasContext.clearRect(0, 0, canvas.width, canvas.height);
}
function changeR(newR){
  r = newR;
}

function tableIconShow(){
  let times = 1;
  let distance = "3px";
  let speed = 100;
  let element = $('.tableIconShow');
  for(i = 0; i < times; i++) {
    element.animate({marginTop: '-='+distance},speed)
      .animate({marginTop: '+='+distance},speed);
  }
}

function reDraw(){
  clearCanvas();
  for (let i = 0; i < points.length; i++){
    if(!points[i].fromForm){
      drawDot(points[i].x * points[i].r, points[i].y * points[i].r, r, false);
    }else{
      drawDot(points[i].x, points[i].y, r, false);
    }

  }
}

function callAngularFunction() {
  window.angularComponentReference.zone.run(() => { window.angularComponentReference.loadAngularFunction(); });
}

function exitApp(){
  localStorage.clear();
  clearCanvas();
  window.location.href = "";
}


function showMobileMenu(){
  if(!menu_clicker){
    $('.menu_mobile').fadeIn();
    menu_clicker = true;
  }else{
    $('.menu_mobile').fadeOut();
    menu_clicker = false;
  }
}

function clearCanvas(){
  let canvas = document.getElementById('render');
  let canvasContext = canvas.getContext('2d');
  canvasContext.clearRect(0, 0, canvas.width, canvas.height);
}

function savePoints(x,y,r, fromForm){
  points.push({x,y,r, fromForm});
}
