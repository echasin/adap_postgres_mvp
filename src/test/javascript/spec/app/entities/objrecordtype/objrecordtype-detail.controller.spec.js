'use strict';

describe('Objrecordtype Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockObjrecordtype, MockObjclassification, MockAsset, MockLocation, MockScore;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockObjrecordtype = jasmine.createSpy('MockObjrecordtype');
        MockObjclassification = jasmine.createSpy('MockObjclassification');
        MockAsset = jasmine.createSpy('MockAsset');
        MockLocation = jasmine.createSpy('MockLocation');
        MockScore = jasmine.createSpy('MockScore');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Objrecordtype': MockObjrecordtype,
            'Objclassification': MockObjclassification,
            'Asset': MockAsset,
            'Location': MockLocation,
            'Score': MockScore
        };
        createController = function() {
            $injector.get('$controller')("ObjrecordtypeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adapApp:objrecordtypeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
