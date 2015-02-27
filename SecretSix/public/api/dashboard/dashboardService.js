/**
 * Created by Ali on 2/26/2015.
 */
(function(){
    'use strict';
    angular
        .module('app').factory('dashboardService',dashboardService);
    dashboardService.$inject = ['$http'];

    function dashboardService($http){
        var service = {
            getData: getData,
            getDataWithInput: getDataWithInput
        };
        return service;
        function getData(){
            return $http.get('/mysamplejson');
        }
        function getDataWithInput(input){
            return $http.get('/mysamplejsonWithParms',
                {
                    params: input
                });
        }
    }
})();