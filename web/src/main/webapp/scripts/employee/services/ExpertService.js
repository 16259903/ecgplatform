'use strict';
define(function(require, exports) {

angular.module('ecgExpertService', [])
    .factory("ExpertService", function($rootScope, $http) {
        var uri = "/api/expert";

        return {
            queryAll: function() {
                return $http({
                    method: 'GET',
                    url: uri
                }).then(function(res) { // 构造session用户
                    if (res.data.datas && res.data.datas.length > 0) {
                        return res.data.datas;
                    } else {
                        return [];    
                    }
                }, function() {
                    $rootScope.popup.error('服务器异常,无法获取数据');
                    return [];
                });
            },
            getTotal: function() {
                return 0;
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
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
                    hospital: "AINIA健康中心",
                    enabled: true,
                    dismissed: false,
                    expire: '2099-01-01',
                    roles: "expert"
                };
            },
            create: function(expert) {
                return $http({
                    method: 'POST',
                    data: expert,
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
                    $rootScope.popup.error('服务器异常,无法获取标识为' + id + '的数据.');
                    return null;
                });
            },
            update: function(expert) {
                return $http({
                    method: 'PUT',
                    data: expert,
                    url: uri + '/' + expert.id
                });
            },
            getRules: function(id) {
                return [];
            },
            getOperators: function(id) {
                return [];
            }
        };
    });
});