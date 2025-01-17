/**
 * Created by Ali on 2/25/2015.
 */
(function(){
    'use strict';
    angular.module('app').controller('ModalStudy', function ($scope, $modalInstance, study, observationCodesDictionary) {
        $scope.newStudy = study;
        $scope.obsOptions = observationCodesDictionary;

        $scope.ok = function () {
            $modalInstance.close($scope.newStudy);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

    angular.module('app').controller('ModalInstanceCtrl', function ($scope, $modalInstance,$timeout, localObservations,remoteObservations,observationCodesDictionary, userInfo, study, service) {

        resetFields();

        $scope.viewObservations = {'local':localObservations,'remote':remoteObservations, 'obsOptions':observationCodesDictionary, 'userInfo':userInfo};
        console.log('testing',userInfo);

        $scope.add = function(){
            var newObs = {
                code: $scope.observationAdd,
                comment: $scope.commentAdd,
                dateObserved: $scope.dateAdd,
                display: $scope.viewObservations.obsOptions[$scope.observationAdd],
                examId: 1,
                quantity: $scope.quantityAdd,
                status: 0,
                observationId: guid(),
                subject: study.subject,
                system: "http://loinc.org",
                unit: $scope.unitAdd
            };
            $scope.viewObservations.local.push(newObs);

            service.addObservation(newObs);

            resetFields();

        };

        function guid() {
            function s4() {
                return Math.floor((1 + Math.random()) * 0x10000)
                    .toString(16)
                    .substring(1);
            }
            return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                s4() + '-' + s4() + s4() + s4();
        }

        $scope.ok = function(){
            $modalInstance.close($scope.selected.item);
        };

        $scope.cancel = function(){
            $modalInstance.dismiss('cancel');
        };

        function resetFields(){
            $scope.observationAdd = study.observationCodes;
            $scope.quantityAdd = null;
            $scope.unitAdd = null;
            $scope.commentAdd = null;
            $scope.dateAdd = new Date();
        }
    });

    angular.module('app').controller('dashboardController',['$scope','$modal','$log','$timeout','dashboardService', 'toastr', dashboardController]);
    function dashboardController($scope,$modal,$log,$timeout,dashboardService,toastr){

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

        $scope.openEditStudyModal = function(study){
            var studyCopy = {};

            for(var k in study) studyCopy[k]=study[k];

            var modalInstance = $modal.open({
                templateUrl: 'addStudyModal.html',
                controller: 'ModalStudy',
                size: 'lg',
                resolve: {
                    study: function(){
                        return studyCopy;
                    },
                    observationCodesDictionary: function(){
                        return $scope.observationCodesDictionary;
                    }
                }
            });

            modalInstance.result.then(function (study) {
                console.dir(study);
                dashboardService.updateStudy(study).then(function(){
                    loadStudies();
                });
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.openAddStudyModal = function () {
            var newStudy = {
                'assignedTo': 1,
                'active': 1,
                'status': 0
            };

            var modalInstance = $modal.open({
                templateUrl: 'addStudyModal.html',
                controller: 'ModalStudy',
                size: 'lg',
                resolve: {
                    study: function() {
                        return newStudy;
                    },
                    observationCodesDictionary: function(){
                        return $scope.observationCodesDictionary;
                    }
                }
            });

            modalInstance.result.then(function (study) {
                dashboardService.addStudy(study).then(function(){
                    loadStudies();
                });
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
                                //console.log(result.data);
                                return result.data;
                            });
                    },
                    remoteObservations: function () {
                        return dashboardService.getObservationByPatientID(patientId).
                            then(function(result){
                                //console.log('anything? ',result.data);
                                return result.data;
                            });
                    },
                    observationCodesDictionary:function() {
                        return $scope.observationCodesDictionary;
                    },
                    userInfo: function(){
                        return $scope.userInfo;
                    },
                    study: function () {
                        return dashboardService.getPatient(patientId).then(function(patient){
                            if(!patient.data.studyId) {
                                toastr.error('Please assign study before adding observations','Cannot add observations');
                                return null;
                            }

                            return dashboardService.getStudy(patient.data.studyId)
                        }).then(function(study){
                            study.data.subject = patientId;
                            return study.data;
                        })
                    },
                    service: function () {
                        return dashboardService;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

//Dictionary
        $scope.myDictionary = {
            'ICD9System':'http://hl7.org/fhir/sid/icd-9',
            'LOINCSystem':'http://loinc.org',
            'PatientLink':'https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Patient',
            'ObservationLink':'https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Observation',
            'ConditionLink':'https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Condition',
            'selfPatientsLink':'https://healthport.i3l.gatech.edu:8443/dstu1/fhir/Patient?_count=50'

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
            '8310-5':'Body Temperature',
            '3141-9':'Body Weight'

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
                return $scope.observationCodesDictionary[code];
            else
                return code;
        };
        $scope.getTypeEnum = function(typeCode){
            if(typeCode == 0)
                return 'CRC';
            else if(typeCode == 1)
                return 'Sponsor';
            else if(typeCode == 2)
                return 'Admin';
            else(typeCode )
            {
                $scope.loggedin = false
                toastr.error('A role with code: '+typeCode+' does not exist. Please contact your system admin','Undefined Role.');
                return 'Error';
            }
        }


        $scope.test = "testing to see this";
        $scope.loadSamplejsonWP = loadSamplejsonWP;
        $scope.loadSamplejson = loadSamplejson;
        $scope.loadPatients = loadPatients;
        $scope.loadStudies = loadStudies;
        $scope.loadObservations = loadObservations;
        $scope.loadConditions = loadConditions;
        $scope.createNewPatient = createNewPatient;
        $scope.createNewObservation = createNewObservation;
        $scope.loadStudyPatients = loadStudyPatients;
        $scope.testAddStudy = testAddStudy;
        $scope.testUpdateStudy = testUpdateStudy;
        $scope.markStudyComplete = markStudyComplete;
        $scope.loadTodos = loadTodos;
        $scope.testUpdateStudyPatient = testUpdateStudyPatient;
        $scope.testAddObservation = testAddObservation;
        $scope.updateStudyPatient = updateStudyPatient;

        $scope.viewObservations = {};
        $scope.action = action;
        $scope.allAvailablePatients = [];
        $scope.allStudies = [];
        $scope.allStudyPatients = [];
        $scope.allAvailableObservations = [];
        $scope.allAvailableConditions = [];
        $scope.allAvailableConditionCodes = [];
        $scope.allAvailableobservationCodes = [];
        $scope.allAvailableTodos = [];
        $scope.objectOfConditionswithPatientData = {};
        $scope.objectOfobservationswithPatientData = {};
        $scope.patientObservations=[];
        $scope.patientConditions=[];
        $scope.patientCount;
        $scope.loggedin = false;
        action();

//Get All Available Condition Codes and Count

        function action(){

            loadPatients($scope.myDictionary.selfPatientsLink);
            patientCount();
            loadStudies();
            loadObservations();
            loadTodos();
        }

//post/put data
        function markStudyComplete(study) {
            study.status = 1;

            return dashboardService.updateStudy(study).then(function(){
                loadStudies();
            });
        }

        function testAddStudy()
        {
            var newStudy = {
                'description': "Test Add Study",
                'assignedTo': 1,
                'observationCodes': "123ABC",
                'frequency': 30,
                'active': 1,
                'status': 0,
                'note': "Test Notes"
            };

            return dashboardService.addStudy(newStudy).then(function(result){
                console.log(result);
                loadStudies();
            });
        }

        function testAddObservation()
        {
            var newObs = {
                code: "Test Code",
                comment: "Test Comment",
                dateObserved: new Date(),
                display: "Test Display",
                examId: 1,
                quantity: 12,
                status: 0,
                subject: "Test Subject",
                system: "http://loinc.org",
                unit: "kg"
            };

            return dashboardService.addObservation(newObs);
        }

        function testUpdateStudy()
        {
            var updatedStudy = {
                'studyId': 1,
                'description': "Test Updated Study",
                'assignedTo': 1,
                'observationCodes': "123ABC",
                'frequency': 30,
                'active': 1,
                'status': 0,
                'note': "Test Notes"
            };

            return dashboardService.updateStudy(updatedStudy).then(function(result){
                console.log(result);
                loadStudies();
            });
        }

        function testUpdateStudyPatient()
        {
            var updatedStudyPatient = {
                "patientId":"3.568001602-01",
                "firstName":"John",
                "lastName":"Tarr",
                "type":1,
                "studyId":1
            };

            return dashboardService.updateStudyPatient(updatedPatient).then(function(result){
                console.log(result);
                loadStudyPatients();
            });
        }

        function updateStudyPatient(patient)
        {
            var patientJson = jQuery.extend(true, {}, patient);

            for(var name in $scope.patientStatusEnum) {
                var value = $scope.patientStatusEnum[name];
                if (patientJson.status == value) patientJson.status = name;
            }

            patientJson.type = Number(patientJson.type);
            delete patientJson['editMode'];

            return dashboardService.updateStudyPatient(patientJson).then(function(result){
                console.log(result);
                for(var name in $scope.patientStatusEnum) {
                    if (patientJson.status == name) patientJson.status = $scope.patientStatusEnum[name];
                }
                console.log(patientJson);
                patient.status = patientJson.status;
                patient.studyDescription = result.data.studyDescription;
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

        function createNewObservation(data){
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
        $scope.userAuthentication = function(username,password){
            console.log("inputuser: ", username,password);
            var user = {'userName':username,'password':password};

            return dashboardService.authenticationWithRole(user).
                then(function(result){
                    $scope.userInfo = result.data;
                    if($scope.userInfo.userId && $scope.userInfo.active){
                        var temp = $scope.getTypeEnum($scope.userInfo.type);
                        if(temp!='Error') {
                            toastr.success('You are using the Secret Six application as a ' + temp, 'You have been authenticated successfully');
                            $scope.loggedin = true;
                        }
                    }
                    else{
                        toastr.error('This user is not authenticated. Please contact your admin','Username or Password is incorrect');
                    }

                    console.log("userType: ", $scope.userInfo);
                });
        };


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

        function loadPatients(link){

            return dashboardService.getAllPatients(link).
                then(function (result) {
                    $scope.allAvailablePatients = result.data;
                    console.log("allAvailablePatients: ", $scope.allAvailablePatients);

                });

        }

        function loadStudies(){
            return dashboardService.getAllStudies().
                then(function(result){
                    $scope.allStudies = result.data;
                    console.log("allStudies: ", $scope.allStudies);
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

        function addEditMode(arrVal) {
            var len = arrVal.length;
            for(var i = 0; i < len; i++){
                arrVal[i].editMode = false;
            }
        };



//Morrisjs section
        //

        $scope.myLineChart = Morris.Line({
            element: 'chart',
            data: [
                {y: '2015-04-01', a: 100, b: 90, c: 95},
                {y: '2015-04-07', a: 75, b: 90, c: 90},
                {y: '2015-04-14', a: 50, b: 90, c: 75},
                {y: '2015-04-21', a: 50, b: 80, c: 89},
                {y: '2015-04-30', a: 50, b: 75, c: 76},
                {y: '2015-05-06', a: 45, b: 75, c: 90},
                {y: '2015-05-11', a: 31, b: 60, c: 90},
                {y: '2015-05-14', a: 31, b: 60, c: 94},
                {y: '2015-05-22', a: 31, b: 60, c: 98}
            ],
            goals: [100, 95],
            goalLineColors:['#F00'],
            xkey: 'y',
            ykeys: ['a', 'b', 'c'],
            labels: ['Weight Pill Study', 'Height Pill Study', 'Sleeping Pill Study']
        });
        $scope.myLineChartweight = Morris.Line({
            element: 'chartplacebovsnonWeight',
            data: [
                {y: '2015-04-01', a: 290, b: 290},
                {y: '2015-04-07', a: 290, b: 285},
                {y: '2015-04-14', a: 285, b: 285},
                {y: '2015-04-30', a: 290, b: 280},
                {y: '2015-04-21', a: 280, b: 275},
                {y: '2015-05-06', a: 285, b: 276},
                {y: '2015-05-11', a: 290, b: 272},
                {y: '2015-05-14', a: 290, b: 271},
                {y: '2015-05-22', a: 285, b: 268}
            ],
            goals: [270, 250],
            goalLineColors:['#F00'],
            ymin:[245],
            xkey: 'y',
            ykeys: ['a', 'b'],
            labels: ['Placebo', 'Actual']
        });
        $scope.myLineChartsleeping = Morris.Line({
            element: 'chartplacebovsnonSleeping',
            data: [
                {y: '2015-04-01', a: 2, b: 2},
                {y: '2015-04-07', a: 2, b: 5},
                {y: '2015-04-14', a: 2, b: 7},
                {y: '2015-04-30', a: 3, b: 7},
                {y: '2015-04-21', a: 5, b: 7},
                {y: '2015-05-06', a: 3, b: 6},
                {y: '2015-05-11', a: 3, b: 8},
                {y: '2015-05-14', a: 3, b: 8},
                {y: '2015-05-22', a: 4, b: 7}
            ],
            goals: [9.0, 7.0],
            goalLineColors:['#F00'],
            xkey: 'y',
            ykeys: ['a', 'b'],
            labels: ['Placebo', 'Actual']
        });
        $scope.myLineChartheight = Morris.Line({
            element: 'chartplacebovsnonHeight',
            data: [
                {y: '2015-04-01', a: 38, b: 38},
                {y: '2015-05-07', a: 38, b: 39},
                {y: '2015-06-14', a: 39, b: 39},
                {y: '2015-07-30', a: 39, b: 40},
                {y: '2015-08-21', a: 39, b: 40},
                {y: '2015-09-06', a: 39, b: 41},
                {y: '2015-10-11', a: 40, b: 42},
                {y: '2015-11-14', a: 40, b: 43},
                {y: '2015-12-22', a: 40, b: 43}
            ],
            goals: [47, 45],
            goalLineColors:['#F00'],
            xkey: 'y',
            ykeys: ['a', 'b'],
            labels: ['Placebo', 'Actual']
        });
        $scope.myDonutChart = Morris.Donut({
            element: 'mydonutchart',
            data: [
                {label: "Weight Study", value: 13},
                {label: "Sleeping Study", value: 30},
                {label: "Height Study", value: 20}
            ]
        });


        $scope.showChart = function() {
            $timeout(function () {
            $scope.myLineChart.redraw();
                $scope.myLineChartweight.redraw();
                $scope.myLineChartsleeping.redraw();
                $scope.myDonutChart.redraw();
                $scope.myLineChartheight.redraw();
            }, 500);
        };


    }
})();