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
            getDataWithInput: getDataWithInput,
            getAllPatients:getAllPatients,
            getPatientDetail:getPatientDetail,
            getAllObservations:getAllObservations,
            getObservationDetail:getObservationDetail,
            getAllConditions:getAllConditions,
            getConditionDetail:getConditionDetail,
            getAllConditionsByCode:getAllConditionsByCode,
            getObservationByPatientID:getObservationByPatientID,
            createPatient:createPatient
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
        function createPatient(patientJson){
            return $http.post('patient',patientJson);
        }
        function getAllPatients(){
            return $http.get('https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient?_count=50');
        }
        function getPatientDetail(idlink){
            return $http.get(idlink);
        }
        function getAllObservations(){
            return $http.get('https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Observation?_count=50');
        }
        function getObservationDetail(idlink){
            return $http.get(idlink);
        }
        function getObservationByPatientID(id){
            return $http.get("https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Observation?subject:Patient="+id)
        }
        function getAllConditions(){
            return $http.get('https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition?_count=200');
        }
        function getConditionDetail(idlink){
            return $http.get(idlink);
        }
        //https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition?code=http://hl7.org/fhir/sid/icd-9|590.80
        function getAllConditionsByCode(system,code){
            return $http.get('https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition?code='+system+'|'+code);
        }
    }
})();