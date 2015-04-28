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
            getPatient:getPatient,
            getAllPatients:getAllPatients,
            getAllStudyPatients:getAllStudyPatients,
            getPatientDetail:getPatientDetail,
            getAllObservations:getAllObservations,
            getObservationDetail:getObservationDetail,
            getAllConditions:getAllConditions,
            getConditionDetail:getConditionDetail,
            getAllConditionsByCode:getAllConditionsByCode,
            getObservationByPatientID:getObservationByPatientID,
            getConditionsByPatientID:getConditionsByPatientID,
            getMedicationByPatientID:getMedicationByPatientID,
            createPatient:createPatient,
            getCount:getCount,
            getStudy:getStudy,
            getAllStudies:getAllStudies,
            addStudy:addStudy,
            updateStudy: updateStudy,
            updateStudyPatient: updateStudyPatient,
            getTodos:getTodos,
            getAllTodos:getAllTodos,
            getLocalObservationByPatientID:getLocalObservationByPatientID,
            getUser:getUser,
            authenticationWithRole:authenticationWithRole,
            addObservation:addObservation
        };
        return service;

        function authenticationWithRole(user){
            return $http.post('authenticateUser', user);
        }

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
        function getPatient(id){
            return $http.get('patient/' + id);
        }
        //'https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Patient?_count=50'
        function getAllPatients(link){
            return $http.get(link);

        }
        function getAllStudies(){
            return $http.get('study');
        }
        function getStudy(id){
            return $http.get('study/'+id);
        }
        function addStudy(studyJson){
            return $http.post('study', studyJson);
        }
        function updateStudy(studyJson){
            return $http.put('study/' + studyJson.studyId, studyJson);
        }

        function updateStudyPatient(patientJson){
            return $http.put('patient/' + patientJson.patientId, patientJson);
        }

        function getAllStudyPatients(){
            return $http.get('patient');
        }
        function getTodos(){
            return $http.get('todo');
        }
        function getAllTodos(){
            return $http.get('todoall');
        }
        function getUser(id){
            return $http.get('user/'+id);
        }
        function getPatientDetail(idlink){
            return $http.get(idlink);
        }
        function getCount(countLink){
            return $http.get(countLink);
        }
        function getAllObservations(){
            return $http.get('https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Observation?_count=50');
        }
        function addObservation(obsJson){
            return $http.post('observation', obsJson);
        }
        function getObservationDetail(idlink){
            return $http.get(idlink);
        }
        function getObservationByPatientID(id){
            return $http.get("https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Observation?subject:Patient="+id)
        }
        function getLocalObservationByPatientID(id){
            return $http.get('observationsByPatientId/'+id)
        }
        function getAllConditions(){
            return $http.get('https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Condition?_count=200');
        }
        function getConditionDetail(idlink){
            return $http.get(idlink);
        }
        //https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Condition?code=http://hl7.org/fhir/sid/icd-9|590.80
        function getAllConditionsByCode(system,code){
            return $http.get('https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Condition?code='+system+'|'+code);
        }
        function getConditionsByPatientID(id) {
            return $http.get("https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Condition?subject:Patient=" + id);
        }
        function getMedicationByPatientID(id){
            return $http.get("https://healthport.i3l.gatech.edu:8443/dstu1/fhir/MedicationPrescription?subject:Patient=" + id);
        }
    }
})();