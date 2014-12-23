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
var hour = [];
var min = [];
var sec  = [];
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

app.post('/api/stat',function(req,res){
	console.log(req.body.all);
	db_person.person.update({RFID : req.body.RFID},{RFID : req.body.RFID , StudentID : req.body.StudentID, 
		First_Name : req.body.First_Name,Last_name : req.body.Last_name,Role : req.body.Role , 
		late : req.body.late , all : req.body.all},function(err,persons){
		console.log(persons);
		res.send(persons);	
		io.emit("person:refresh");
	});
});

app.post('/api/createuser',function(req,res){
	db_person.person.insert((req.body),function(err,books){
		console.log(req);
		console.log(req.body);
		res.send(books);
		io.emit("book:refresh")	
	});
});

app.post('/api/signup',function(req,res){
	db_login.login.insert((req.body),function(err,books){
		console.log(req);
		console.log(req.body);
		res.send(books);
		io.emit("book:refresh")	
	});
});

app.post('/api/log',function(req,res){
	now = new Date();
	//date = dateFormat(now, "dddd, mmmm dS, yyyy, h:MM:ss TT");
	hours = now.getHours()+"";
	min  = now.getMinutes()+"";
	sec  = now.getSeconds()+"";



	var year = now.getFullYear();

    var month = now.getMonth() + 1;
    month = (month < 10 ? "0" : "") + month;

    var day  = now.getDate();
    day = (day < 10 ? "0" : "") + day;

    date = day + "/" + month + "/" + year;
	console.log(date);
	lastlog = { 
				RFID: req.body.RFID,
				Name: req.body.First_Name,
				StudentID: req.body.StudentID,
				Date: date,
				Hour: hours,
				Min : min,
				Sec : sec
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

//======================= SEARCH =============================
app.post('/api/log_search',function(req,res){
	console.log("Searching...");
	console.log(req.body.StudentID)
	db_logs.log.find(({StudentID:req.body.StudentID}),function(err,logs){
		res.send(logs);
	});
});

app.post('/api/person_search',function(req,res){
	console.log("Searching...");
	console.log(req.body.StudentID);
	db_person.person.find(({StudentID:req.body.StudentID}),function(err,logs){
		res.send(logs);

	});
});
//============================================================

//======================= DATE_SEARCH =============================
app.post('/api/date_search',function(req,res){
	console.log("DATE Searching...");
	console.log(req.body.date)
	db_logs.log.find(({Date:req.body.date}),function(err,logs){
		console.log(logs);
		res.send(logs);
	});
});
//============================================================

app.get('/api/login',function(req,res){
	db_login.login.find({},function(err,logs){
		res.send(logs);
	});
})


app.post('/api/edituser',function(req,res){
	console.log(req.body.all);
	db_person.person.update({RFID : req.body.RFID},{RFID : req.body.RFID , StudentID : req.body.StudentID, 
		First_Name : req.body.First_Name,Last_name : req.body.Last_name,Role : req.body.Role , 
		late : req.body.late , all : req.body.all},function(err,persons){
		console.log(persons);
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