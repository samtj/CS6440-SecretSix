/**
 * Created by Ali on 2/25/2015.
 */
(function(){
    'use strict';
    angular.module('app').controller('ModalInstanceCtrl', function ($scope, $modalInstance,$timeout, localObservations,remoteObservations) {
        //we get items in here. use that for graphs
        //$timeout(function () {
        //    var temp = Morris.Line({
        //        element: 'chart',
        //        data: [
        //            {y: '2006', a: 100, b: 90},
        //            {y: '2007', a: 75, b: 65},
        //            {y: '2008', a: 50, b: 40},
        //            {y: '2009', a: 75, b: 65},
        //            {y: '2010', a: 50, b: 40},
        //            {y: '2011', a: 75, b: 65},
        //            {y: '2012', a: 100, b: 90}
        //        ],
        //        xkey: 'y',
        //        ykeys: ['a', 'b'],
        //        labels: ['Series A', 'Series B']
        //    });
        //}, 500);
        $scope.viewObservations = {'local':localObservations,'remote':remoteObservations};
        console.log('testing',$scope.viewObservations);

        //$scope.items = items;
        //$scope.selected = {
        //    item: $scope.items[0]
        //};

        $scope.ok = function () {

            $modalInstance.close($scope.selected.item);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

    angular.module('app').controller('dashboardController',['$scope','$modal','$log','dashboardService', 'toastr', dashboardController]);
    function dashboardController($scope,$modal,$log,dashboardService,toastr){

        $scope.items = ['item1', 'item2', 'item3'];
        $scope.open = function (size) {

            var modalInstance = $modal.open({
                templateUrl: 'observationModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                resolve: {
                    items: function () {
                        return $scope.items;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.openPatientObservations = function (patientId) {

            var modalInstance = $modal.open({
                templateUrl: 'patientObservationModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: 'lg',
                resolve: {
                    localObservations: function () {
                        return dashboardService.getLocalObservationByPatientID(patientId).
                            then(function(result){
                                console.log(result.data);
                                return result.data;
                            });
                    },
                    remoteObservations: function () {
                        return dashboardService.getObservationByPatientID(patientId).
                            then(function(result){
                                console.log('anything? ',result.data);
                                return result.data;
                            });
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };


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
        $scope.loadTodos = loadTodos;

        $scope.viewObservations = {};
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
            '8462-4':'Diastolic BP',
            '8480-6':'Systolic BP',
            '8867-4':'Heart Beat',
            '9279-1':'Respiration Rate',
            '2093-3':'Cholesterol',
            '8302-2':'Body Height',
            '8310-5':'Body Temperature'
        };
        $scope.patientStatusEnum = {
            '0':'Open',
            '1':'Active',
            '2':'Inactive'
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
        $scope.getObservationName = function(code){
            if($scope.observationCodesDictionary[code])
                return $scope.observationCodesDictionary[code]
            else
                return code;
        }



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
            return dashboardService.createPatient({
                'patientId':patientData.identifier[0].value,
                'firstName':patientData.name[0].given[0],
                'lastName':patientData.name[0].family[0],
                'type':1,
                'status': 0,
                'studyDescription': 'nostudy',
                'studyId': 0})
                .then(function(result){
                    if(result.data.content){
                        toastr.success('was successfully added to Secret Six database.', patientData.name[0].given[0] + ' ' + patientData.name[0].family[0]);
                        patientCount();
                    }else{
                        toastr.error('Patient ' + patientData.name[0].given[0] + ' ' + patientData.name[0].family[0] + ' already exists in our database','Failure to add');
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
            return dashboardService.getDataWithInput({text:input}).
                then(function(result){
                    $scope.passedtext = result.data;

                });
        }
        function loadSamplejson(){
            return dashboardService.getData().
                then(function(result){
                    $scope.passedtext = result.data;


                });
        }
        function getDistinctStudies(list){
            $scope.distinctStudies = [];
            angular.forEach(list, function(item) {
                if ($scope.distinctStudies.indexOf(item.studyId) == -1)
                    $scope.distinctStudies.push(item.studyId);
            });
            angular.forEach($scope.distinctStudies, function(item,val) {
                var temp = [];
                $scope.distinctStudies[val] = {studyId:item ,count:0,description:''};
                angular.forEach(list, function(listItem) {
                    if(listItem.studyId == $scope.distinctStudies[val].studyId) {
                        $scope.distinctStudies[val].count++;
                        $scope.distinctStudies[val].description = listItem.description;
                        temp.push(listItem);
                    }
                });
                $scope.distinctStudies[val].todos = temp;
                temp = $scope.distinctStudies[val].todos[0].observationCodes.split('|');
                $scope.distinctStudies[val].observationCodesObj = temp;
                temp = '';
                angular.forEach($scope.distinctStudies[val].observationCodesObj, function(item) {
                    temp = temp +" " +  $scope.getObservationName(item);
                });
                angular.forEach($scope.distinctStudies[val].todos, function(item,key) {
                    $scope.distinctStudies[val].todos[key].search = '' + temp;

                    if(item.pastDueDays > 0)
                        $scope.distinctStudies[val].todos[key].search = $scope.distinctStudies[val].todos[key].search + " pastdue";
                    if(!item.lastDateObserved )
                        $scope.distinctStudies[val].todos[key].search = $scope.distinctStudies[val].todos[key].search + " new patient";
                });

            });


            console.log($scope.distinctStudies);
        }
        function loadTodos(all){
            if(all)
                return dashboardService.getAllTodos().then(function(result){
                    $scope.allAvailableTodos = result.data;
                    getDistinctStudies($scope.allAvailableTodos);
                    console.log("in here 2",result.data);
                });
            else{
                return dashboardService.getTodos().then(function(result){
                    $scope.allAvailableTodos = result.data;
                    getDistinctStudies($scope.allAvailableTodos);
                    console.log("in here 3",result.data);
                });
            }
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
            });
        }

        function loadStudyPatients(){
            return dashboardService.getAllStudyPatients().
                then(function(result){
                    $scope.allStudyPatients = result.data;
                    console.log("allStudyPatients: ", $scope.allStudyPatients);
                    angular.forEach($scope.allStudyPatients, function(patient,key){
                        $scope.allStudyPatients[key].status = $scope.patientStatusEnum[$scope.allStudyPatients[key].status];
                    });

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