'use strict';

describe('Objcategory Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockObjcategory, MockObjclassification, MockObjtype, MockAsset, MockLocation, MockScore;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockObjcategory = jasmine.createSpy('MockObjcategory');
        MockObjclassification = jasmine.createSpy('MockObjclassification');
        MockObjtype = jasmine.createSpy('MockObjtype');
        MockAsset = jasmine.createSpy('MockAsset');
        MockLocation = jasmine.createSpy('MockLocation');
        MockScore = jasmine.createSpy('MockScore');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Objcategory': MockObjcategory,
            'Objclassification': MockObjclassification,
            'Objtype': MockObjtype,
            'Asset': MockAsset,
            'Location': MockLocation,
            'Score': MockScore
        };
        createController = function() {
            $injector.get('$controller')("ObjcategoryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adapApp:objcategoryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
