angular.module("myApp", [])
.controller('mainCtrl', function($scope, $http){
	$scope.members = [];
	$http.get('/api/member').success(function(data){
		$scope.members = data;
	})
})