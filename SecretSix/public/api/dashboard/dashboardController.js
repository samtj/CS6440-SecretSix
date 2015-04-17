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
        $scope.loadStudies = loadStudies;
        $scope.loadObservations = loadObservations;
        $scope.loadConditions = loadConditions;
        $scope.createNewPatient = createNewPatient;
        $scope.loadStudyPatients = loadStudyPatients;
        $scope.testAddStudy = testAddStudy;

        $scope.action = action;
        $scope.allAvailablePatients = [];
        $scope.allStudies = [];
        $scope.allStudyPatients = [];
        $scope.allAvailableObservations = [];
        $scope.allAvailableConditions = [];
        $scope.allAvailableConditionCodes = [];
        $scope.allAvailableobservationCodes = [];
        $scope.objectOfConditionswithPatientData = {};
        $scope.objectOfobservationswithPatientData = {};
        $scope.patientObservations=[];
        $scope.patientConditions=[];
        $scope.patientCount;
        $scope.loggedin = false;
        action();
//Dictionary
        $scope.myDictionary = {
            'ICD9System':'http://hl7.org/fhir/sid/icd-9',
            'LOINCSystem':'http://loinc.org',
            'PatientLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Patient',
            'ObservationLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Observation',
            'ConditionLink':'https://taurus.i3l.gatech.edu:8443/HealthPort/fhir/Condition'
        };
        $scope.showList = {
            'availablePatients':false,
            'availablePatientsObservations':false,
            'availablePatientsConditions':false,
            'availablePatientsMedications':false,
            'studyPatients':false,
            'availableTodos':false,
            'availableStudies':false
        };
        $scope.observationCodesDictionary = {
            'Diastolic BP':'8462-4',
            'Systolic BP':'8480-6',
            'Heart Beat':'8867-4',
            'Respiration Rate':'9279-1',
            'Cholesterol':'2093-3',
            'Body Height':'8302-2',
            'Body Temperature':'8310-5'
        };
        $scope.setShowListDefault = function(incoming){
            $scope.showList = {
                'availablePatients':false,
                'availablePatientsObservations':false,
                'availablePatientsConditions':false,
                'availablePatientsMedications':false,
                'studyPatients':false,
                'availableTodos':false,
                'availableStudies':false
            };
            $scope.showList[incoming] = true;

        };


//Get All Available Condition Codes and Count

        function action(){
            loadPatients();
            patientCount();
            loadStudies();
            loadObservations();
        }

//post/put data
        function testAddStudy()
        {
            var newStudy = {
                'studyId': 3,
                'description': "Test Add Study",
                'assignedTo': 1,
                'observationCodes': "123ABC",
                'frequency': 30,
                'active': 1,
                'status': 0
            };

            return dashboardService.addStudy(newStudy).then(function(result){
                console.log(result);
                loadStudies();
            });
        }


        function createNewPatient(patientData){
            console.log("creating new patient");
            return dashboardService.createPatient({'patientId':patientData.identifier[0].value,'firstName':patientData.name[0].given[0],'lastName':patientData.name[0].family[0],'type':1})
                .then(function(result){
                    if(result.data.content){
                        patientCount();
                    }else{
                        //not successful toast
                    }


                    //
                });
        }
//post/put data END

//Loading Data
        function patientCount(){
            return dashboardService.getCount('patientCount').then(function(result){
                $scope.patientCount = result.data;
                console.log($scope.patientCount);
            });

        }
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

        function loadStudies(){
            return dashboardService.getAllStudies().
                then(function(result){
                    $scope.allStudies = result.data;
                    //console.log("allStudies: ", $scope.allStudies[0]);
            })
        }

        function loadStudyPatients(){
            return dashboardService.getAllStudyPatients().
                then(function(result){
                    $scope.allStudyPatients = result.data;
                    console.log("allStudyPatients: ", $scope.allStudyPatients);

                });
        }
        $scope.patientData = function(idlink){
            return dashboardService.getPatientDetail(idlink).
                then(function(result){
                    console.log("patient Data: ", result.data);
                });
        };
        function loadObservations(){
            var exists = false;
            return dashboardService.getAllObservations().
                then(function(result){
                    $scope.allAvailableObservations = result.data;
                    console.log("allAvailableObservations: ", $scope.allAvailableObservations);
                    angular.forEach(result.data["entry"], function(observation){
                        //Make a unique list of all available codes
                        if(observation.content.name.coding[0].code != null) {
                            if ($scope.allAvailableobservationCodes.indexOf(observation.content.name.coding[0].code) == -1)
                                $scope.allAvailableobservationCodes.push(observation.content.name.coding[0].code);

                            //Make a list of unique Codes with Display, System, and number of Patients, and PatientIDs
                            if (Object.keys($scope.objectOfobservationswithPatientData).length == 0)
                                $scope.objectOfobservationswithPatientData[Object.keys($scope.objectOfobservationswithPatientData).length] = {
                                    'code': observation.content.name.coding[0].code,
                                    'display': observation.content.name.coding[0].display,
                                    'system': observation.content.name.coding[0].system,
                                    'patientList': [observation.content.subject.reference],
                                    'patientCount': 1
                                };
                            else {
                                for (var i = 0; i < Object.keys($scope.objectOfobservationswithPatientData).length; i++) {
                                    if ($scope.objectOfobservationswithPatientData[i].code == observation.content.name.coding[0].code) {
                                        $scope.objectOfobservationswithPatientData[i].patientCount++;
                                        $scope.objectOfobservationswithPatientData[i].patientList.push(observation.content.subject.reference);
                                        exists = true;
                                    }
                                }
                                if (!exists) {
                                    $scope.objectOfobservationswithPatientData[Object.keys($scope.objectOfobservationswithPatientData).length] = {
                                        'code': observation.content.name.coding[0].code,
                                        'display': observation.content.name.coding[0].display,
                                        'system': observation.content.name.coding[0].system,
                                        'patientList': [observation.content.subject.reference],
                                        'patientCount': 1
                                    };
                                }
                                exists = false;
                            }
                        }

                    });
                    console.log("allAvailableobservationCode: ", $scope.allAvailableobservationCodes);
                    console.log("objectOfobservationswithPatientData: ", $scope.objectOfobservationswithPatientData);
                });
        }
        $scope.observationData = function(idlink){
            return dashboardService.getObservationDetail(idlink).
                then(function(result){
                    console.log("observation Data: ", result.data);
                });
        };
        $scope.observationByPatientID = function(id){
            return dashboardService.getObservationByPatientID(id).
                then(function(result){
                    $scope.patientObservations = result.data;
                    console.log("patientObservations: ", $scope.patientObservations);
                });
        };
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
        };

        $scope.loadConditionsByCode = function(system,code){
            return dashboardService.getAllConditionsByCode(system,code).
                then(function(result){
                    console.log("conditions by code: ", result.data);
                });
        };

        $scope.conditionsByPatientID = function(id){
            return dashboardService.getConditionsByPatientID(id).
                then(function(result) {
                    $scope.patientConditions = result.data;
                    console.log("conditions by id", result.data);
                });
        };
        $scope.medicationByPatientID = function(id){
            return dashboardService.getMedicationByPatientID(id).
                then(function(result) {
                    $scope.patientMedicationPrescription = result.data;
                    console.log("medication by id", result.data);
                });
        };

        $scope.selectPatientData = function(id){
            $scope.conditionsByPatientID(id);
            $scope.observationByPatientID(id);
            $scope.medicationByPatientID(id);

        };
    }
})();