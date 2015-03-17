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
        $scope.loadPatients = loadPatients;
        $scope.loadObservations = loadObservations;
        $scope.loadConditions = loadConditions;
        $scope.allAvailablePatients = [];
        $scope.allAvailableObservations = [];
        $scope.allAvailableConditions = [];


//Dictionary
        $scope.myDictionary = {
            'ICD9System':'http://hl7.org/fhir/sid/icd-9',
            'LOINCSystem':'http://loinc.org',
            'PatientLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient',
            'ObservationLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Observation',
            'ConditionLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition'
        };



//Get All Available Condition Codes and Count





//Loading Data
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

        function loadPatients(){
            return dashboardService.getAllPatients().
                then(function(result){
                    $scope.allAvailablePatients = result.data;
                    console.log("allAvailablePatients: ", $scope.allAvailablePatients);

                });
        }
        $scope.patientData = function(idlink){
            return dashboardService.getPatientDetail(idlink).
                then(function(result){
                    console.log("patient Data: ", result.data);
                });
        }
        function loadObservations(){
            return dashboardService.getAllObservations().
                then(function(result){
                    $scope.allAvailableObservations = result.data;
                    console.log("allAvailableObservations: ", $scope.allAvailableObservations);

                });
        }
        $scope.observationData = function(idlink){
            return dashboardService.getObservationDetail(idlink).
                then(function(result){
                    console.log("observation Data: ", result.data);
                });
        }
        function loadConditions(){
            return dashboardService.getAllConditions().
                then(function(result){
                    //$scope.allAvailableConditions = result.data;
                    //console.log("allAvailableConditions: ", $scope.allAvailableConditions);
                    angular.forEach(result.data["entry"], function(condition){

                    });

                });
        }
        $scope.conditionData = function(idlink){
            return dashboardService.getConditionDetail(idlink).
                then(function(result){
                    console.log("condition Data: ", result.data);
                });
        }

        $scope.loadConditionsByCode = function(system,code){
            return dashboardService.getAllConditionsByCode(system,code).
                then(function(result){
                    console.log("conditions by code: ", result.data);
                });
        }





    }
})();