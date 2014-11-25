var express = require('express')
var mongojs = require('mongojs');
var app = express()
var db = mongojs('Project', ['person']);


app.use(express.static(__dirname + '/public'));


app.get('/api/books',function(req,res){
	db.person.find({},function(err,persons){
		res.send(persons);
	})	
})

app.post('/api/books',function(req,res){
	db.person.insert((req.body),function(err,persons){
		res.send(persons);	
	});
});

var server = app.listen(3000, function () {

  	console.log("server is running")

})