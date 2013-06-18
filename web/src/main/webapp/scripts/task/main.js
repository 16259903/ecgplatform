'use strict';
define(function(require, exports) {

require("./services/TaskService");
require("./services/PlotService");
require("./directives/UndoneTaskList");
require("./directives/UndoneTaskView");
require("./directives/UserCard");
require("./directives/TaskCard");
require("./directives/Plot");
require("./directives/Reply");

angular.module('ecgTask', ['ecgTaskService', 'ecgUndoneTaskList', 'ecgUndoneTaskView'])
.controller('UndoneTaskController', ['$scope', function ($scope) {
    // register the inner namespace
    $scope.undone = {};
    $scope.subheader.title = "待办工作";
}])
.controller('DoneTaskController', ['$scope', function ($scope) {
    // register the inner namespace
    $scope.done = {};
    $scope.subheader.title = "已办工作";
}]);
});