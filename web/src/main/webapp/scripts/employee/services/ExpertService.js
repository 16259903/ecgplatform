'use strict';
define(function(require, exports) {

angular.module('ecgExpertService', [])
    .factory("ExpertService", function($rootScope, $http) {
        var uri = PATH + "/api/expert";

        return {
            queryAll: function() {
                return $http({
                    method: 'GET',
                    url: uri
                }).then(function(res) {
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            getPlainObject: function() {
                return {
                    name: "",
                    username: "",
                    status: "OFFLINE",
                    gender: 1,
                    birthday: "",
                    idCard: "",
                    title: "",
                    mobile: "",
                    company: "AINIA健康中心",
                    enabled: true,
                    dismissed: false,
                    expire: '2099-01-01',
                    roles: "expert"
                };
            },
            create: function(expert) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(expert),
                    url: uri
                }).then(function(res) {
                    if (res.status === 201) {
                        return true;
                    } else {
                        return false;
                    }
                }, function() {
                    return false;
                });
            },
            get: function(id) {
                return $http({
                    method: 'GET',
                    cache: false,
                    url: uri + '/' + id
                }).then(function(res) {
                    return res.data;
                }, function() {
                    $rootScope.message.error('服务器异常,无法获取标识为' + id + '的数据.');
                    return null;
                });
            },
            update: function(expert) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(expert),
                    url: uri + '/' + expert.id
                });
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
            getRules: function(id) {
                return [];
            },
            linkOperator: function(expert, operator) {
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param({operatorId: operator.id}),
                    url: uri + '/' + expert.id + '/operator'
                }).then(function(res) {
                    if (res.status === 201) {
                        return true;
                    } else {
                        return false;
                    }
                }, function() {
                    return false;
                });
            },
            unlinkOperator: function(expert, operator) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + expert.id + '/operator/' + operator.id
                });
            },
            getOperators: function(id) {
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(expert),
                    url: uri + '/' + expert.id + '/operator'
                });
            }
        };
    });
});