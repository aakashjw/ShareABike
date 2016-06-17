var express = require("express");
var app = express();
var redis = require('redis');
var client = redis.createClient(); //creates a new client
client.on('connect', function() {
    console.log('redis connected');
});

function getParameter(name,fullUrl){
   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(fullUrl))
      return decodeURIComponent(name[1]);
}

app.get('/saveLocation', function (req, res) {
	var fullUrl=req.protocol + '://' + req.get('host') + req.originalUrl;
	console.log("url",req.protocol + '://' + req.get('host') + req.originalUrl);
	var userId=getParameter("userid",fullUrl);
	var longitude=getParameter("longitude",fullUrl);
	var latitude = getParameter("latitude",fullUrl);

   if(userId && latitude && longitude){
   	var latlng={}
   	latlng.latitude=latitude;
   	latlng.longitude=longitude;
   	client.hset('location',userId, JSON.stringify(latlng));
   	console.log("save the informtion");
   res.send("it has been saved");
   }else{
   	console.log("no user id or latitude or longitude");
   }
})
app.get('/', function (req, res) {
   console.log("/ has been hit")
   res.send("welcome to the broadcast app")
})

app.get('/getAllClients', function (req, res) {
	/*var keys=client.keys("location*");*/
	client.hgetall("location" , function(err, location) {
    console.log(location);
    res.send(location);
})
});

var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("Example app listening at http://%s:%s", host, port)

})
