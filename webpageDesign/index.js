var express = require('express')
var app = express()

app.use(express.static(__dirname + '/public'));

app.get('/api/member',function(req,res){
	var members = [
		{name: 'Dream', id: '5510110132 ',sex: 'male'},
		{name: 'Care', id: '5510110049 ',sex: 'male'},
		{name: 'Ne', id: '5510110438 ',sex: 'male'},
		{name: 'Beer', id: '5510110230 ',sex: 'female'}
	];
	res.send(members);
})
var server = app.listen(3000, function () {

  	console.log("server is running")

})