'use strict';

describe('Objtype Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockObjtype, MockObjcategory, MockAsset, MockLocation, MockScore;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockObjtype = jasmine.createSpy('MockObjtype');
        MockObjcategory = jasmine.createSpy('MockObjcategory');
        MockAsset = jasmine.createSpy('MockAsset');
        MockLocation = jasmine.createSpy('MockLocation');
        MockScore = jasmine.createSpy('MockScore');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Objtype': MockObjtype,
            'Objcategory': MockObjcategory,
            'Asset': MockAsset,
            'Location': MockLocation,
            'Score': MockScore
        };
        createController = function() {
            $injector.get('$controller')("ObjtypeDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adapApp:objtypeUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
