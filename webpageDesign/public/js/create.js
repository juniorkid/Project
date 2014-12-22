angular.module("myCreate", ['angular-md5'])
.controller('mainCtrl', function($scope, $http, md5){
	$scope.createUser = {
		late : 0 ,
		all : 0
	};
	$scope.createLogin = {};
	$scope.signup = function(){
		$scope.createLogin.RFID = $scope.createUser.RFID;
		$scope.createUser.late = 0;
		$scope.createUser.all = 0;
		$scope.createLogin.pass = md5.createHash($scope.createLogin.pass || '')
		$http.post('/api/createuser',$scope.createUser).success(function(data){
	    	$scope.createUser = {};
	  	});
	  	$http.post('/api/signup',$scope.createLogin).success(function(data){
	    	$scope.createLogin = {};
	  	});
	}

})