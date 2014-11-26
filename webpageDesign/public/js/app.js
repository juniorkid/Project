angular.module("myApp", ['btford.socket-io'])
.factory('socketIO', function(socketFactory){
	 return socketFactory({
    	ioSocket: io.connect("http://localhost:8080")
    });
})
.controller('mainCtrl', function($scope, $http, socketIO){
	$scope.persons = [];
	$scope.personInstance = {};
	refreshPersons();

	function refreshPersons(){
		$http.get('/api/book').success(function(data){
			$scope.persons = data;
		})
	}

	$scope.save = function(){
		$http.post('/api/book', $scope.personInstance).success(function(data){
			$scope.persons.push(data);
			$scope.personInstance = {};
		});
	}

	socketIO.on('person:refresh', function(){
		refreshPersons();
	});

})