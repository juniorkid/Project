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
	$scope.log_searchAll = [];
	$scope.person_searchAll = [];
	$scope.date_search = {};
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
		$http.post('/api/edituser', $scope.selectedModel).success(function(data){
			$scope.selectedModel = {};
		});
	}

	$scope.ID_searching = function (){
		$http.post('/api/log_search',$scope.ID_search).success(function(data){
			$scope.log_searchAll = data;			
			console.log("Searching Success");
		})

		$http.post('/api/person_search',$scope.ID_search).success(function(data){
			$scope.person_searchAll = data;			
			console.log("Searching Success");
		})
	}
	//============================ DATE SEARCH ====================================
	$scope.date_searching = function (){
		console.log($scope.date_search.date);
		console.log($scope.date_search);
		$http.post('/api/date_search',$scope.date_search).success(function(data){
			$scope.date_search = data;		
			console.log($scope.date_search);	
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