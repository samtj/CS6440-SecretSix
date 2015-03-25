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
        $scope.action = action;
        $scope.allAvailablePatients = [];
        $scope.allAvailableObservations = [];
        $scope.allAvailableConditions = [];
        $scope.allAvailableConditionCodes = [];
        $scope.objectOfConditionswithPatientData = {};
        $scope.patientObservations=[];
        action();
//Dictionary
        $scope.myDictionary = {
            'ICD9System':'http://hl7.org/fhir/sid/icd-9',
            'LOINCSystem':'http://loinc.org',
            'PatientLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient',
            'ObservationLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Observation',
            'ConditionLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition'
        };



//Get All Available Condition Codes and Count

        function action(){
            loadPatients();
        }



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
        $scope.observationByPatientID = function(id){
            return dashboardService.getObservationByPatientID(id).
                then(function(result){
                    $scope.patientObservations = result.data;
                    console.log("patientObservations: ", $scope.patientObservations);
                });
        }
        function loadConditions(){
            var exists = false;
            return dashboardService.getAllConditions().
                then(function(result){
                    $scope.allAvailableConditions = result.data;
                    console.log("allAvailableConditions: ", $scope.allAvailableConditions);
                    angular.forEach(result.data["entry"], function(condition){
                        //Make a unique list of all available codes
                        if(condition.content.code.coding[0].code != null) {
                            if ($scope.allAvailableConditionCodes.indexOf(condition.content.code.coding[0].code) == -1)
                                $scope.allAvailableConditionCodes.push(condition.content.code.coding[0].code);

                            //Make a list of unique Codes with Display, System, and number of Patients, and PatientIDs
                            if (Object.keys($scope.objectOfConditionswithPatientData).length == 0)
                                $scope.objectOfConditionswithPatientData[Object.keys($scope.objectOfConditionswithPatientData).length] = {
                                    'code': condition.content.code.coding[0].code,
                                    'display': condition.content.code.coding[0].display,
                                    'system': condition.content.code.coding[0].system,
                                    'patientList': [condition.content.subject.reference],
                                    'patientCount': 1
                                };
                            else {
                                for (var i = 0; i < Object.keys($scope.objectOfConditionswithPatientData).length; i++) {
                                    if ($scope.objectOfConditionswithPatientData[i].code == condition.content.code.coding[0].code) {
                                        $scope.objectOfConditionswithPatientData[i].patientCount++;
                                        $scope.objectOfConditionswithPatientData[i].patientList.push(condition.content.subject.reference);
                                        exists = true;
                                    }
                                }
                                if (!exists) {
                                    $scope.objectOfConditionswithPatientData[Object.keys($scope.objectOfConditionswithPatientData).length] = {
                                        'code': condition.content.code.coding[0].code,
                                        'display': condition.content.code.coding[0].display,
                                        'system': condition.content.code.coding[0].system,
                                        'patientList': [condition.content.subject.reference],
                                        'patientCount': 1
                                    };
                                }
                                exists = false;
                            }
                        }

                    });
                    console.log("allAvailableConditionCode: ", $scope.allAvailableConditionCodes);
                    console.log("objectOfConditionswithPatientData: ", $scope.objectOfConditionswithPatientData);
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