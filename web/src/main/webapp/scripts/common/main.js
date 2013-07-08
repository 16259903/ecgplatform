'use strict';
define(function(require, exports) {

require("./Nav");
require("./Header");
require("./Message");
require("./Footer");
require("./Dialog");

angular.module('ecgCommon', ["angular-table", "ecgNav", "ecgHeader", "ecgMessage", "ecgFooter", "ecgDialog"])
.factory("EnumService", function() {
    var levels = {
        'danger': '危险或数据异常',
        'warning': '值得关注',
        'success': '正常'
    };
    return {
        getGenders: function() {
            return [{label: '男', value: 1}, {label: '女', value: 0}];
        },
        getGenderLabel: function(gender) {
            return gender ?  '男': '女';
        },
        getDismissedStates: function() {
            return [{label: '离职', value: true}, {label: '在职', value: false}];
        },
        getDismissedLabel: function(dismissed) {
            return !dismissed ? '在职' : '离职';
        },
        getLevelLabel: function(level) {
            return levels[level] || '未知';
        }
    };
});

});
