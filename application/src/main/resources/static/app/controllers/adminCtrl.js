coffeeshopapp.controller('adminCtrl', adminCtrl);
coffeeshopapp.controller('addUserCtrl', addUserCtrl);
coffeeshopapp.controller('editUserCtrl', editUserCtrl);
coffeeshopapp.controller('deleteUserCtrl', deleteUserCtrl);

function adminCtrl($scope, $http, $rootScope, $route, $window, filterFilter, $filter, dataService, user){
		
	$http.get('api/account/getAllUsers')
        .success(function (data) {
			$rootScope.Users=data;
			$rootScope.filteredUser = $filter('orderBy')($rootScope.Users, "firstName", false);
			angular.forEach($rootScope.filteredUser, function(user){
				user.editModal =false;
				user.deleteModal =false;
			});
			$scope.totalItems = $scope.Users.length;
        })
        .error(function (data, status, headers, config) {
				$scope.errorMessage = $rootScope.errorMessagePart
						+ "Admin properties, error # " + status;
		});
	
	$http.get("api/account/getAllRoles")
		.success(function(data) {
			$scope.roles = data;
			$scope.role=$scope.roles[0];
		})
		.error(function(data, status, headers, config) {
			$scope.errorMessage = $rootScope.errorMessagePart+ "Roles properties, error # " + status;
		});
	
	$scope.showAddUserModal = false;
 	$scope.openAddUserModal = function(role){
      $scope.showAddUserModal = true;
 	}
 	
 	$scope.closeAddUserModal = function(){
 		$scope.showAddUserModal = false;
 		$route.reload();
 	}
 	
	$scope.openEditModal = function(user){
		user.editModal = true;
		$rootScope.remainingEditableRealms = [];
		$rootScope.selectedEditableRealms = [];
		angular.forEach(groups.getGroups(), function(userRealm){
			var present = false;
			angular.forEach(user.groups, function(userGroup){
				if(userRealm.groupName==userGroup){
					present = true;
				}
			});
			if(present==true){
				$rootScope.selectedEditableRealms.push(userRealm);
			}
			else{
				$rootScope.remainingEditableRealms.push(userRealm);
			}
		});
	  }
	
	$scope.editPopUpClose = function(user){
		user.editModal = false;
		$route.reload();
	}
	
	$scope.openDeleteModal = function(user){
		user.deleteModal = true;
	  }
	
	$scope.deletePopUpClose = function(user){
		user.deleteModal = false;
		$route.reload();
	}
	
    // Searching
    $scope.searchKey='';
    $scope.searchUser = function(searchKey){
		$rootScope.filteredUser=[];
		$rootScope.filteredUser = filterFilter($rootScope.Users, searchKey);
		$scope.totalItems = $rootScope.filteredUser.length;
	}
    
    // Sorting
    $scope.sortType     = 'lastUpdateDate'
    $scope.sortReverse  = false;
    $scope.orderUser = function (order, sortReverse) {
		$scope.sortType = order;
		$scope.sortReverse  = !$scope.sortReverse;
		$rootScope.filteredUser = $filter('orderBy')($rootScope.filteredUser, order, sortReverse);
    }
	    
    // pagination
    $scope.viewby = 5;
    $scope.currentPage = 4;
    $scope.itemsPerPage = $scope.viewby;
	$scope.setItemsPerPage = function(num) {
		$scope.itemsPerPage = num;
		$scope.currentPage = 1;
	}
	
  	var userPromise = dataService.getUser();
	 userPromise.then(function(result) {  
		 user.setUser(result.data);
	 });
};

function addUserCtrl($scope, $rootScope, $http, $route){
	$scope.addUserError = false;
	
	$scope.selectGroup = function(group) {
		$rootScope.remainingRealms.splice($rootScope.remainingRealms.indexOf(group), 1);
		$rootScope.selectedrealms.push(group);
	};
	
	$scope.removeGroup = function(group) {
		$rootScope.selectedrealms.splice($rootScope.selectedrealms.indexOf(group), 1);
		$rootScope.remainingRealms.push(group);
	};
	
 	$scope.addUser=function(email, password, firstname, lastname, role, selectedrealms){
 		$scope.realmList = [];
    	angular.forEach(selectedrealms, function(realm){
    		$scope.realmList.push(realm.groupName);
    	})
    	
    	 $('form').submit(function(){
	       var options = selectedrealms;
	       if(options== "")
	    	   {
	         	 document.getElementById("demo").innerHTML = "Select atleast one group";
	              return false;
	    	   }
	       else
	    	  {
		          $http.post("api/account/addUser", {"userId":email,"password":password,"role":role,"firstName":firstname,"lastName":lastname,"groups":$scope.realmList})
	              .success(function (data) {
	                  $scope.result = data.result;
	                  if($scope.result=='SUCCESS'){
	                	  $route.reload();
	                  }
	                  else{
	                	  $scope.addUserError = true;
	                  }
	                })
	              .error(function (data, status, headers, config) {
	                  $scope.errorMessage = "Not able to add user details, error # " + status;
	              	});
	    	   }
	     });
 	};
}

function editUserCtrl($scope, $rootScope, $http, $route){
	$scope.modifyUserError = false;
	$scope.password = '';
	$scope.password1 = '';
	
	$scope.selectEditableGroup = function(group) {
		$rootScope.remainingEditableRealms.splice($rootScope.remainingEditableRealms.indexOf(group), 1);
		$rootScope.selectedEditableRealms.push(group);
	};
	
	$scope.removeEditableGroup = function(group) {
		$rootScope.selectedEditableRealms.splice($rootScope.selectedEditableRealms.indexOf(group), 1);
		$rootScope.remainingEditableRealms.push(group);
	};
	
	$scope.updateUser = function(user, password, password1, selectedEditableRealms){
		$scope.updateError = false;
    	 $scope.updateErrorMsg = "";
		
		$scope.updatedRealmList = [];
    	angular.forEach(selectedEditableRealms, function(realm){
    		$scope.updatedRealmList.push(realm.groupName);
    	})
    
    	if(password==""&&password1==""){
    		$scope.password = user.password;
    		$scope.password1 = user.password;
    	}
    	
    if(password!=password1){
	    	$scope.updateError = true;
	   		$scope.updateErrorMsg = "Password update failed due to mismatch";
    	}
    else if(selectedEditableRealms<=0)
	   {
	   		$scope.updateError = true;
	   		$scope.updateErrorMsg = "Select Atleast One Group";
	   }
   else
	  {
	$http.post("api/account/updateUser", {"userId":user.userId,"password":$scope.password,"firstName":user.firstName,"lastName":user.lastName,"role":user.role,"groups":$scope.updatedRealmList})
    .success(function (data) {
        $scope.modifyUserResult = data.result;
        if($scope.modifyUserResult=="SUCCESS"){
      	  $scope.editPopUpClose(user);
        }
        else{
      	  $scope.modifyUserError = true;
        }
      })
    .error(function (data, status, headers, config) {
        $scope.errorMessage = "Not able to add user details, error # " + status;
    	});
	  }
	};
}

function deleteUserCtrl($scope, $http, $route){
	$scope.deleteUserError = false;
	
	$scope.deleteUser = function(user){
		user.deleteModal = true;
		
    	$http.post("api/account/deleteUser?userId="+user.userId)
	    	.success(function (data) {
	    		$scope.deleteUserResult = data.result;
	    		if($scope.deleteUserResult=='SUCCESS'){
	    			$scope.deletePopUpClose(user);
	    		}else{
	    			$scope.deleteUserError = true;
	    		}
	         })
	        .error(function (data, status, headers, config) {
	        	$scope.errorMessage = "Not able to delete user details, error # " + status;
	        });
    };	
}