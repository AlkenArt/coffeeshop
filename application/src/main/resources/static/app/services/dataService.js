coffeeshopapp.service('dataService', function($http) {
	return {
			getUser: function(){
				return $http.get("api/account/getCurrentUser").success(
						function(data) {
							return data;
						}).error(
						function(data, status, headers, config) {
							$scope.errorMessage = $rootScope.errorMessagePart
									+ "Can't load user details, error # " + status;
						});
			},
			getCategory: function(){
				return $http.get("api/order/getAllCategories").success(
						function(data) {
							return data;
						}).error(
						function(data, status, headers, config) {
							$scope.errorMessage = $rootScope.errorMessagePart
									+ "Can't load group details, error # " + status;
						});
			}
		};
});