var express = require('express')
var mongojs = require('mongojs');
var bodyParser = require('body-parser')
var app = express()
var http = require('http').Server(app);
var io = require('socket.io')(http);
var db = mongojs('Project', ['person']);


app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json())

app.get('/api/book',function(req,res){
	db.person.find({},function(err,persons){
		res.send(persons);
	});
})

app.post('/api/book',function(req,res){
	db.person.insert((req.body),function(err,persons){
		res.send(persons);	
		io.emit("person:refresh");
	});
});

io.on('connection', function(socket){
  console.log('a user connected');
});

http.listen(8080, function () {
	console.log("server is running")
})