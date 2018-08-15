var coffeeshopapp = angular.module('coffeeshopapp', ['ngRoute', 'ngResource','ngAnimate','ui.bootstrap','ngLoadingSpinner','directive.contextMenu','ngTagsInput',"ngSanitize", "ngCsv"]);
coffeeshopapp.controller('viewController', viewController);

coffeeshopapp.config(['$routeProvider',function($routeProvider) {
    $routeProvider      
    .when("/placeOrder", { templateUrl: "view/placeOrder.html"})  
    .when("/home", { templateUrl: "view/orders.html"})
    .when("/admin", { templateUrl: "view/adminmanagement.html"})
      .otherwise( {redirectTo: '/home'});
  }]);

function viewController($scope, $location, $rootScope, $http, $route, dataService , categories, user) {
	 $scope.header="view/header.html";
	 $scope.footer="view/footer.html";
	 $scope.modal="view/modal/addUserModal.html";
	 $scope.editUser="view/modal/editUser.html";
	 $scope.deleteUser="view/modal/deleteUserModal.html";
	 $rootScope.errorMessagePart = "Couldn't load the"
     $scope.adminAccess = false;
	 $scope.click = function(pathurl)
	 {
	 	$location.path(pathurl);	 	
	 }
	 
	 $scope.refresh = function()
	 {
	 	$route.reload();	 	
	 }
	 
	 var groupPromise = dataService.getCategory();
	 groupPromise.then(function(result) {  
		 categories.setCategories(result.data);
	 });
	 
	 var userPromise = dataService.getUser();
	 userPromise.then(function(result) {  
		 user.setUser(result.data);
		 $scope.currentUser = user.getUser();
		if($scope.currentUser.role == 'ADMIN'){
			$scope.adminAccess = true;
		}
		else{
			$scope.adminAccess = false;
		}
	 });
};