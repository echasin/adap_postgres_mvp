'use strict';

describe('Asset Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockAsset, MockObjrecordtype, MockObjclassification, MockObjcategory, MockObjtype, MockLocation, MockScore;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockAsset = jasmine.createSpy('MockAsset');
        MockObjrecordtype = jasmine.createSpy('MockObjrecordtype');
        MockObjclassification = jasmine.createSpy('MockObjclassification');
        MockObjcategory = jasmine.createSpy('MockObjcategory');
        MockObjtype = jasmine.createSpy('MockObjtype');
        MockLocation = jasmine.createSpy('MockLocation');
        MockScore = jasmine.createSpy('MockScore');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Asset': MockAsset,
            'Objrecordtype': MockObjrecordtype,
            'Objclassification': MockObjclassification,
            'Objcategory': MockObjcategory,
            'Objtype': MockObjtype,
            'Location': MockLocation,
            'Score': MockScore
        };
        createController = function() {
            $injector.get('$controller')("AssetDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adapApp:assetUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
