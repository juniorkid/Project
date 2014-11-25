angular.module("myApp", [])
.controller('mainCtrl', function($scope, $http){
	$scope.persons = [];

	$http.get('/api/books').success(function(data){
		$scope.persons = data;
	})

})