angular.module("myApp", ['btford.socket-io'])
.factory('socketIO', function(socketFactory){
	 return socketFactory({
    	ioSocket: io.connect("http://localhost:8080")
    });
})
.controller('mainCtrl', function($scope, $http, socketIO){
	$scope.persons = [];
	$scope.personInstance = {};
	$scope.logs = [];
	$scope.selectedModel = {};
	refreshPersons();
	refreshLogs();

	function refreshPersons(){
		$http.get('/api/book').success(function(data){
			$scope.persons = data;
		})
	}

	$scope.signup = function(){
		$scope.createLogin.RFID = $scope.createUser.RFID;
		$http.post('/api/createuser',$scope.createUser).success(function(data){
	    	$scope.createUser = {};
	  	});
	  	$http.post('/api/signup',$scope.createLogin).success(function(data){
	    	$scope.createLogin = {};
	  	});
	}
 


	function refreshLogs(){
		$http.get('/api/log').success(function(data){
			$scope.logs = data;
			console.log(data);
		})
	}

	

	$scope.edituser = function(){
		$http.post('/api/edit', $scope.selectedModel).success(function(data){
			$scope.persons.push(data);
			$scope.selectedModel = {};
		});
	}

	$scope.ID_searching = function (){
		console.log($scope.ID_search.StudentID);
		console.log($scope.ID_search);
		$http.post('/api/ID_search',$scope.ID_search).success(function(data){
			$scope.ID_searchAll = data;			
			console.log("Searching Success");
		})
	}

	socketIO.on('person:refresh', function(){
		refreshPersons();
	});

	socketIO.on('log:refresh', function(){
		refreshLogs();
	});

})