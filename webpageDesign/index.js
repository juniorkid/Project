var express = require('express')
var dateFormat = require('dateformat');
var mongojs = require('mongojs');
var bodyParser = require('body-parser')
var app = express()
var http = require('http').Server(app);
var io = require('socket.io')(http);
var db_person = mongojs('Project', ['person']);
var db_logs = mongojs('Project', ['log']);
var db_login = mongojs('Project', ['login']);
var lastlog = [];
var now = [];
var date = [];

app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json())

app.get('/api/book',function(req,res){
	db_person.person.find({},function(err,persons){
		res.send(persons);
	});
})

app.post('/api/book',function(req,res){
	db_person.person.insert((req.body),function(err,persons){
		res.send(persons);	
		io.emit("person:refresh");
	});
});

app.post('/api/log',function(req,res){
	now = new Date();
	date = dateFormat(now, "dddd, mmmm dS, yyyy, h:MM:ss TT");
	console.log(date);
	lastlog = { 
				RFID: req.body.RFID,
				Name: req.body.First_Name,
				Date: date
				}
	db_logs.log.insert((lastlog),function(err,logs){
		res.send(logs);	
		io.emit("log:refresh");
	});
});

app.get('/api/log',function(req,res){
	db_logs.log.find({},function(err,logs){
		res.send(logs);
	});
})

app.get('/api/login',function(req,res){
	db_login.login.find({},function(err,logs){
		res.send(logs);
	});
})

io.on('connection', function(socket){
  console.log('a user connected');
});

http.listen(8080, function () {
	console.log("server is running")
})