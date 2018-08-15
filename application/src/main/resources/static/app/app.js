var coffeeshopapp = angular.module('coffeeshopapp', ['ngRoute', 'ui.mask', 'ngResource','ngAnimate','ui.bootstrap','ngLoadingSpinner','directive.contextMenu','ngTagsInput',"ngSanitize", "ngCsv"]);
coffeeshopapp.controller('viewController', viewController);
coffeeshopapp.controller('datePickerCtrl', datePickerCtrl);
coffeeshopapp.controller('helpCtrl', helpCtrl);

coffeeshopapp.config(['$routeProvider',function($routeProvider) {
    $routeProvider      
    .when("/createAC", { templateUrl: "view/startACG.html"})  
    .when("/generateAC", { templateUrl: "view/generatedAC.html"})
    .when("/accessCodes", { templateUrl: "view/accessCodes.html"})
    .when("/reports", { templateUrl: "view/reports.html"})
    .when("/admin", { templateUrl: "view/adminmanagement.html"})
    .when("/help", { templateUrl: "view/help.html"})
      .otherwise( {redirectTo: '/accessCodes'});
  }]);

function viewController($scope, $location, $rootScope, $http, $route, dataService , groups, user, products) {
	 $scope.header="view/header.html";
	 $scope.footer="view/footer.html";
	 $scope.modal="view/modal/addUserModal.html";
	 $scope.addGroup="view/modal/addGroup.html";
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
	 
	 var groupPromise = dataService.getGroups();
	 groupPromise.then(function(result) {  
		 groups.setGroups(result.data);
	 });
	 
	 var productPromise = dataService.getProducts();
	 productPromise.then(function(result) {  
		 products.setProducts(result.data);
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
	 
	 $http.get("api/subscriber/getAllMasterAccounts")
     .success(function (data) {
         $scope.masterAccounts = data;
         $http.post("api/subscriber/updateAccountsUsername",$scope.masterAccounts)
         .success(function (data) {
             $scope.updatedMasterAccounts = data;
         })
         .error(function (data, status, headers, config) {
             $scope.errorMessage = "Not able to update the master account details, error # " + status;
         });
     })
     .error(function (data, status, headers, config) {
         $scope.errorMessage = "Not able to fetch the master account details, error # " + status;
     });
};

function datePickerCtrl($scope){
	$scope.dateInitiator = function() {
	    $scope.startDate = new Date();
	    $scope.endDate = new Date();
	    $scope.endDate.setDate($scope.endDate.getDate() + 1);
	  };
	  $scope.dateInitiator();

	  $scope.clear = function() {
	    $scope.startDate = null;
	    $scope.endDate = null;
	  };

	  $scope.inlineOptions = {
	    customClass: getDayClass,
	    minDate: new Date(),
	    showWeeks:true
	  };

	  $scope.dateOptions = {
	  // dateDisabled: disabled, // uncomment to disable the weekends
	    formatYear: 'yyyy',
	    maxDate: new Date(2020, 5, 22),
	    minDate: new Date(),
	    startingDay: 1
	  };

	  // Disable weekend selection
	  function disabled(data) {
	    var date = data.date,
	      mode = data.mode;
	    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
	  }

	  $scope.setMin = function(date) {
		    $scope.inlineOptions.minDate = date;
		    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
		  };

	  $scope.open1 = function() {
	    $scope.popup1.opened = true;
	    $scope.inlineOptions.minDate = new Date();
	    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
	  };

	  $scope.open2 = function(startDate) {
		  $scope.popup2.opened = true;
		  $scope.endDate = new Date(startDate);
		  $scope.endDate.setDate($scope.endDate.getDate() + 1);
		  $scope.setMin($scope.endDate);
	  };
	  
	  $scope.format = 'yyyy-MM-dd HH:mm:ss';

	  $scope.popup1 = {
	    opened: false
	  };

	  $scope.popup2 = {
	    opened: false
	  };

	  function getDayClass(data) {
	    var date = data.date,
	      mode = data.mode;
	    if (mode === 'day') {
	      var dayToCheck = new Date(date).setHours(0,0,0,0);

	      for (var i = 0; i < $scope.events.length; i++) {
	        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

	        if (dayToCheck === currentDay) {
	          return $scope.events[i].status;
	        }
	      }
	    }

	    return '';
	  }
};

function helpCtrl($scope, $http, $rootScope) {
	$http.get("data/faq.property").success(
			function(data) {
				$scope.faqs = data;
			}).error(
			function(data, status, headers, config) {
				$scope.errorMessage = $rootScope.errorMessagePart
						+ "faq properties, error # " + status;
			});
	  
	};