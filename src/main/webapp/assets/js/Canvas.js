/**
 * Created by Богдана on 29.09.2017.
 */
var app = angular.module("myApp",[]);

angular.element(document).ready(function () {

    var paint = document.getElementById('graphen');
    var context = paint.getContext("2d");
        var width = document.body.clientWidth;
    function load(x,y) {
        context.moveTo(x/2, 1);
        context.lineTo(x/2, y);
        context.moveTo(1, y/2);
        context.lineTo(x, y/2);
        context.strokeStyle = "#ffffff";
        context.stroke();
    }
       if (width >= 1187) {
           load(650,650);
        }
        if ((width >= 672)&&(width<1187)) {

            paint=document.getElementById('graphen_tabl');
            context = paint.getContext("2d");
            load(500,600);
        }
    if (width <672) {
        paint=document.getElementById('graphen_mob');
        context = paint.getContext("2d");
    load(350,350);
    }

});

app.controller('canvasController',['$scope','$window', '$http','$httpParamSerializer',function ($scope, $window, $http, $httpParamSerializer) {


    $scope.paint=function (r){
        $scope.rad=r;
      var width =document.body.clientWidth;
  function paint_dep(x,y,k) {
      context.clearRect(0, 0, x, y);
      context.beginPath();
      context.moveTo(x/2, y/2);
      context.lineTo(x/2, y/2 - r * k);
      context.lineTo(x/2 + r * k, y/2);
      context.closePath();
      context.fillStyle = "#ffa500";
      context.fill();
      context.rect(x/2 - r * k, y/2 - r * k, r * k, r * k);
      context.fill();
      context.moveTo(x/2, y/2);
      context.arc(x/2, y/2, r * k / 2, (1 / 2) * Math.PI, Math.PI);
      context.fill();
      context.moveTo(x/2, 1);
      context.lineTo(x/2, y);
      context.moveTo(1, y/2);
      context.lineTo(x, y/2);
      context.strokeStyle = "#ffffff";
      context.stroke();
  }

            var paint = document.getElementById('graphen');
            var context = paint.getContext("2d");
            if (width >= 1187) {
            paint_dep(650,650,60);
            }
            if((width>=672)&&(width<1187)){
                paint = document.getElementById('graphen_tabl');
                 context = paint.getContext("2d");
                paint_dep(500,600,50);

            }
        if (width < 672) {
            paint = document.getElementById('graphen_mob');
            context = paint.getContext("2d");
     paint_dep(350,350,30);
        }

    };
    $scope.listen=function (r) {
         var rad = document.forms.checker.r;
         for(var i =0;i<rad.length;i++) {
             if (i !== r) {
                 rad[i].checked = false;
             }
         }
         if(rad[r].checked){
             $scope.paint(r);
         }if(!rad[r].checked){
            $scope.paint(0);

        }
    };
 $scope.listenX=function (n) {
     var xfield =document.forms.checker.x;
     for(var i = 0;i<xfield.length;i++){
         if(i!==n){
             xfield[i].checked=false;
         }
     }
 };

 $scope.checkText=function () {
   var text = document.forms.checker.y;

     if((text.value<=-5)||(text.value>=3)||(isNaN(text.value))||(text.value==='')){
         text.style.backgroundColor="#f80040";

     }else{ text.style.backgroundColor="#ffa500";

    }
 };

$scope.paintPoint= function (x,y,color) {
    var width =document.body.clientWidth;
    var paint = document.getElementById('graphen');
    var context = paint.getContext("2d");
     function paint_dep_point(x,y,w,h,k,color) {
         context.fillStyle=color;
         context.beginPath();
         context.arc(w+x*k,h-y*k,4,0,2*Math.PI);
         context.fill();
     }


    if (width >= 1187) {

        paint_dep_point(x,y,325,325,60,color);

    }
    if((width>=672)&&(width<1187)){
        paint = document.getElementById('graphen_tabl');
        context = paint.getContext("2d");
        paint_dep_point(x,y,250,300,50,color);

    }
    if (width < 672) {
        paint = document.getElementById('graphen_mob');
        context = paint.getContext("2d");
        paint_dep_point(x,y,175,175,30,color);
    }
 };





$scope.onClickCanvas=function () {

};
 $window.onload = function () {
     $http.get('rest/usr/secur').then(function success(resp) {
        if(resp.data.length===0){
            $window.location.replace('http://localhost:3377/laba4-1.0/error_page.html');
        }
     });

     var amq = org.activemq.Amq;
     amq.init({
         uri: 'amq',
         logging: true,
         timeout: 20
     });
     var myHandler = {
         rcvMessage: function(message)
         {
            alert(message.textContent);
         }
     };

     amq.addListener('in_out','topic://in_out',myHandler.rcvMessage,{});
var x=0;
var y=0;
  var color = '#0000ff';


  $http.get('rest/point/getpoints').
     then(function success(response) {
         $scope.shots = response.data;
        var len =$scope.shots.length-1;
         document.forms.checker.r[$scope.shots[len].r].checked=true;
         $scope.paint($scope.shots[len].r);
         for(var i = 0; i < len; i++){

              x = $scope.shots[i].x;
             y = $scope.shots[i].y;

             $scope.paintPoint(x, y, color);
         }
       var fit = $scope.shots[len].fit;

          if (fit===true) {
          color = '#ffff00';
          } if(fit===false){
          color = '#ff0000';
          }
          $scope.paintPoint($scope.shots[len].x, $scope.shots[len].y, color);

     });

 };
 $scope.click=function clicked(arg){


     var elem = document.getElementById(arg);
     var br = elem.getBoundingClientRect();
     var left = br.left;
     var top = br.top;
     var event = window.event;
     var x = event.clientX-left;
     var y = event.clientY-top;
     var form = document.forms.checker;
     var r = form.elements.r;
     var rad=-1;
     var newx;
     var newy;
     for(var i=0;i<r.length;i++){
         if(r[i].checked===true){rad = r[i].value;}
     }
     var width = document.body.clientWidth;
     if(rad!==-1){
      if(width>=1187){
          newx=(x-325)/60;
          newy=(325-y)/60;
      }if((width<1187)&&(width>=672)){
             newx=(x-250)/50;
             newy=(300-y)/50;
         }
         if(width<672){
             newx=(x-175)/30;
             newy=(175-y)/30;
         }
         var data={
            'x': newx,
             'y': newy,
            'r': rad
         };
         console.log(x);
         console.log(y);
         console.log(data);
     $http.post('rest/point/check',$httpParamSerializer({x:newx,y:newy,r:rad}));
$window.location.reload(true);
      }else{
      alert("Выберите радиус :)");
      }
 };

}]);

app.controller('resultController', ['$scope','$http', function ($scope,$http) {

    $scope.show_table =function () {
        var btn = document.getElementById('show');
        var text=btn.value;
        if(text==='Показать таблицу результатов') {

          $http.get('rest/point/getpoints').then(function success(response) {
              $scope.shots=response.data;
              for(var i=0;i<$scope.shots.length;i++){
                  if($scope.shots[i].fit===true){
                      $scope.shots[i].fit='Попадание!';
                  }else {$scope.shots[i].fit='Мимо!';}
              }
              btn.value ='Скрыть таблицу результатов';
          });

        }else{
            $scope.shots=[];
            btn.value ='Показать таблицу результатов';

        }
    };


}]);


function validate() {
    var r = document.forms.checker.r;
    var x = document.forms.checker.x;
    var flagr=false;
    var flagx=false;
    for(var i =0;i<r.length;i++){
        if (r[i].checked===true){flagr=true;}
    }
    for( i =0;i<x.length;i++){
        if (x[i].checked===true){flagx=true;}
    }
  if(isNaN( document.forms.checker.y.value) ||(document.forms.checker.y.value==='')||(flagx===false)||(flagr===false)) {
        alert('Проверьте введенные данные :)');
      return false;
  }else{

      return true;}
}



