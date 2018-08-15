coffeeshopapp.controller('ordersCtrl', ordersCtrl);

function ordersCtrl($scope, $http, $rootScope, filterFilter, $filter, categories, user){
	$scope.sortType     = 'orderDate'
	$scope.sortReverse  = false;
	$scope.searchFish='';	
	
	$scope.allCategories = categories.getCategories();
	$scope.category='All Orders';
	
	$http.get("api/order/getAllOrders")
		.success(function(data) {
			$rootScope.allOrders = data;
			$rootScope.filteredOrders = $rootScope.allOrders;
			$rootScope.filteredOrders = $filter('orderBy')($rootScope.filteredOrders, $scope.sortType, true);
			$scope.totalItems = $rootScope.filteredOrders.length;
		}).error(function(data, status, headers, config) {
			$scope.errorMessage = $rootScope.errorMessagePart
					+ "all Access Codes properties, error # " + status;
		});
	
	$scope.filterByCategory = function(category){
		$rootScope.filteredOrders=[];
		
		if(category=="All Orders"){
			$rootScope.filteredOrders = $rootScope.allOrders;
		}
		else {
			angular.forEach($scope.allOrders, function(value){
				if(value.order==category){
					$rootScope.filteredOrders.push(value);
				}
			});
		}
		$scope.totalItems = $rootScope.filteredOrders.length;
	}
		
	$scope.searchByText = function(searchText){
		$rootScope.filteredOrders=[];
		$rootScope.filteredOrders = filterFilter($rootScope.allOrders, searchText);
		$scope.totalItems = $rootScope.filteredOrders.length;
	}
		
// Sorting
	$scope.order = function (order, sortReverse) {
		$scope.sortType = order;
		$scope.sortReverse  = !$scope.sortReverse;
		$rootScope.filteredOrders = $filter('orderBy')($rootScope.filteredOrders, order, sortReverse);
    }
// pagination code
	  $scope.viewby = 5;
	  $scope.currentPage = 4;
	  $scope.itemsPerPage = $scope.viewby;
	  $scope.maxSize = 3; // Number of pager buttons to show

	$scope.setItemsPerPage = function(num) {
	  $scope.itemsPerPage = num;
	  $scope.currentPage = 1; // reset to first page
	}
// Context-Menu start
	  $scope.edit = function() {
	  }
	  
	  $scope.deleteAC = function() {
	  }
};