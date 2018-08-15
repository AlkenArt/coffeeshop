coffeeshopapp.controller('placeOrderCtrl', placeOrderCtrl);

function placeOrderCtrl($scope, $http, $location,dataService,categories){
	$scope.orderError = false;
	$scope.orderInfo = {}
	 var categoryPromise = dataService.getCategory();
	 categoryPromise.then(function(result) {  
		 categories.setCategories(result.data);
	 });
	 
	$scope.allCategories = categories.getCategories();
	$scope.orderInfo.order=$scope.allCategories[0];
	
	$scope.productDetails = [{'productCode':1}];
	$scope.productCode=$scope.productDetails[0].productCode;
	
	$scope.calculatePrice = function(quantity){
		$scope.orderInfo.price=quantity*250;
	}
	
	$scope.placeOrder = function(orderInfo){
		if(angular.isUndefined(orderInfo.order)||orderInfo.order == ''){
			$scope.orderError = true;
			$scope.orderErrorMsg = "Please select valid coffee type"
		}
		else if(angular.isUndefined(orderInfo.quantity)||orderInfo.quantity < 1){
			$scope.orderError = true;
			$scope.orderErrorMsg = "Quantity Selection is wrong"
		}
		else if(angular.isUndefined(orderInfo.customerName)||orderInfo.customerName==''){
			$scope.orderError = true;
			$scope.orderErrorMsg = "Please enter customer name"
		}
		else{
			$scope.orderError = false;
			$http.post("api/order/placeOrder", orderInfo)
	        .success(function (data) {
	            if(data.result!="SUCCESS"){
	            	$scope.orderError = true;
	    			$scope.orderErrorMsg = "Error in placing order Please retry after some time";
	            }
	            else
	            	$location.path('/home');
	            })
	        .error(function (data, status, headers, config) {
	            $scope.errorMessage = "Not able to generate the access codes, error # " + status;
	        });
		}
	}
};