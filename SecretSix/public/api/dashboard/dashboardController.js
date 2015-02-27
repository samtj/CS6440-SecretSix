/**
 * Created by Ali on 2/25/2015.
 */
(function(){
    'use strict';

    angular.module('app').controller('dashboardController',['$scope','$log','dashboardService',dashboardController]);
    function dashboardController($scope,$log,dashboardService){
        $scope.test = "testing to see this";
        $scope.loadSamplejsonWP = loadSamplejsonWP;
        $scope.loadSamplejson = loadSamplejson;

        function loadSamplejsonWP(input){
            console.log("in here");
            return dashboardService.getDataWithInput({text:input}).
                then(function(result){
                    $scope.passedtext = result.data;

                });
        }
        function loadSamplejson(){
            return dashboardService.getData().
                then(function(result){
                    $scope.passedtext = result.data;

                    console.log("in here 2",result.data);
                });
        }






    }
})();