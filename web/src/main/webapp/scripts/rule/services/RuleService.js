'use strict';
define(function(require, exports) {

angular.module('ecgRuleService', [])
    .factory("RuleService", function($rootScope, $http) {
        var uri = PATH + "/api/rule";

        return {
            queryAll: function(params) {
                var params = params || {};
                return $http({
                    method: 'GET',
                    url: uri + '?' + $.param(params)
                }).then(function(res) { // 构造session用户
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
            queryAllGroup: function(params) {
                var params = params || {};
                params.usage = 'group';
                return this.queryAll(params); 
            },
            queryAllFiltersByGroup: function(rule, params) {
                var id = rule.id || rule, params = params || {};
                params.groupdId = id;
                params.usage = 'filter';
                return this.queryAll(params);
            },
            getPlainObject: function() {
                return {
                    "type": 11, // 必填 数字
                    "code": "", // 必填 数字
                    "name": "", // 必填
                    "min": 0, // 必填 数字
                    "max": 1000, // 必填 数字
                    "unit": "单位", // 可填 单位
                    "remark": "", // 可填 说明
                    "level": "success", // 可填 级别
                    "usage": "group",
                    "canReply": true,
                    "user_id": null // 预留字段
                };
            },
            create: function(rule) {
                var rule = $.extend({}, rule || {});
                delete rule.id;
                delete rule.replys;
                delete rule.replyconfigs;
                delete rule.percent;
                delete rule.arrayIdx;
                return $http({
                    method: 'POST',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(rule),
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
            update: function(rule) {
                var rule = $.extend({}, rule);
                delete rule.replys;
                delete rule.replyconfigs;
                delete rule.version;
                delete rule.percent;
                delete rule.arrayIdx;
                return $http({
                    method: 'PUT',
                    headers:{'Content-Type':'application/x-www-form-urlencoded'},
                    data: $.param(rule),
                    url: uri + '/' + rule.id
                });
            },
            remove: function(id) {
                return $http({
                    method: 'DELETE',
                    url: uri + '/' + id
                });
            },
        };
    });
});