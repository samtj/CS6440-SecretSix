/**
 * Created by Ali on 2/25/2015.
 */
(function (){
    'use strict';

angular.module('app',[
    'ngSanitize',
    'ngAnimate',
    'ngRoute',
    'ui.bootstrap',
    'toastr'

]);
    // define additional triggers on Tooltip and Popover
    app.config(['$tooltipProvider', function($tooltipProvider){
        $tooltipProvider.setTriggers({
            'show': 'hide'
        });
    }]);
})();