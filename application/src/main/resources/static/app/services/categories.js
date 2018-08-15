coffeeshopapp.service('categories', function() {
	this.categories = [];
    this.addCategory = function (category) {
    	this.categories.push(category);
    }
    this.getCategories = function () {
        return this.categories;
    }
    this.setCategories = function (categories) {
    	this.categories = categories;
    }
});