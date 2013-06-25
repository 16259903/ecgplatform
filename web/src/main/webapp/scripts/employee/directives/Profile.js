'use strict';
define(function(require, exports) {

var profileEditTemp = require("../templates/profile/edit.html");
var profilePasswordTemp = require("../templates/profile/password.html");

angular.module('ecgProfile', [])
.controller('ProfileController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService) {
    $scope.subheader.title = "个人设置";

    $scope.profile = {};
    $scope.profile.tab = 1; // 默认为基本页面

}])
// 基本信息
.controller('ProfileEditController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService) {

   	$scope.profile.user = null;
   	ProfileService.get().then(function(user) {
        $scope.profile.user = user;
    });
    $scope.profile.genders = EnumService.getGenders();
    $scope.profile.dismissedStates = EnumService.getDismissedStates();

    $('#profile-birthday').datetimepicker({
        format: "yyyy-MM-dd",
        language: "zh-CN",
        pickTime: false,
    }).on('changeDate', function(e) {
        $scope.profile.user.birthday = $('#profile-birthday input').val();
    });

    $scope.profile.showDatePicker = function() {
        $('#profile-birthday').datetimepicker('show');
    };

    $scope.profile.update = function() {
        $scope.dialog.showStandby();
        $scope.profile.user.birthday = $('#profile-birthday input').val();
        ProfileService.update($scope.profile.user)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.popup.success("编辑成功!");
        }, function() {
            $scope.dialog.hideStandby();
            $scope.popup.error("编辑失败!");
        });;
    };
}])
.directive("ecgProfileEdit", [ '$location', function($location) {
    return {
        restrict : 'E',
        replace : false,
        template : profileEditTemp,
        controller : "ProfileEditController",
        link : function($scope, $element, $attrs) {
        }
    };
}])
// 修改密码
.controller('ProfilPasswordController', ['$scope', '$routeParams', '$timeout', '$location', 'EnumService', 'ProfileService',
    function ($scope, $routeParams, $timeout, $location, EnumService, ProfileService) {

   	$scope.profile.user = null;

    $scope.profile.updatePassword = function() {
        $scope.dialog.showStandby();
        ProfileService.update($scope.profile.user)
        .then(function(result) {
            $scope.dialog.hideStandby();
            $scope.popup.success("修改密码成功!");
        }, function() {
            $scope.dialog.hideStandby();
            $scope.popup.error("修改密码失败!");
        });;
    };
}])
.directive("ecgProfilePasword", [ '$location', function($location) {
    return {
        restrict : 'E',
        replace : false,
        template : profilePasswordTemp,
        controller : "ProfilPasswordController",
        link : function($scope, $element, $attrs) {
        }
    };
}]);


});