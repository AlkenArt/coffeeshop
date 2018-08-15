var register = angular.module('register', ['ngRoute','ngLoadingSpinner', 'ui.mask']);
register.controller('registerCtrl', registerCtrl);

function registerCtrl($scope, $http){
	var passRegex = /(?=^.{8,16}$)((?=.*\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))^.*/;
	$scope.updateSuccessful = false;
	$scope.pwd = {};
	$scope.pwd.newPass = "";
	$scope.pwd.confirmPass = "";
	
	function isUndefined(value){
		 return angular.isUndefined(value);
	 }
	
	$scope.Errors = ErrorService[0].PasswordErrors;
	user = {};
	$scope.addUser = function(username, newPassword, confirmPassword, firstName, lastName){
		 if(isUndefined(newPassword)||newPassword==""){
			 document.getElementById('newPassword').focus();
			 $scope.errorMsg = $scope.Errors.newPassEmpty;
		 }else if(!passRegex.test(newPassword)){
			 document.getElementById('newPassword').focus();
			 $scope.errorMsg = $scope.Errors.newPassPolicyError;
		 }else if(isUndefined(confirmPassword)||confirmPassword==""){
			 document.getElementById('confirmPassword').focus();
			 $scope.errorMsg = $scope.Errors.confirmPassEmpty;
		 }else if(newPassword!=confirmPassword){
			 document.getElementById('confirmPassword').focus();
			 $scope.errorMsg = $scope.Errors.newAndConfirmNotSame;
		 }
		 else{
			user.userId = username;
			user.firstName = firstName;
			user.lastName = lastName;
			user.password = confirmPassword;
			user.role = "STAFF";
			$http.post("api/account/addUser", user).success(
					function(data) {
						$scope.result = data.result;
						if($scope.result!="SUCCESS"){
							$scope.errorMsg = $scope.result;
						}
						else
							$scope.updateSuccessful = true;
					}).error(
					function(data, status, headers, config) {
						var errorMessage = "Error: Can't add the user, error # " + status;
						console.log(errorMessage);
					});
		}	 
	 }
};