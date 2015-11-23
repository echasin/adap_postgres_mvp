'use strict';

describe('Score Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockScore, MockObjrecordtype, MockObjclassification, MockObjcategory, MockObjtype, MockAsset;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockScore = jasmine.createSpy('MockScore');
        MockObjrecordtype = jasmine.createSpy('MockObjrecordtype');
        MockObjclassification = jasmine.createSpy('MockObjclassification');
        MockObjcategory = jasmine.createSpy('MockObjcategory');
        MockObjtype = jasmine.createSpy('MockObjtype');
        MockAsset = jasmine.createSpy('MockAsset');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Score': MockScore,
            'Objrecordtype': MockObjrecordtype,
            'Objclassification': MockObjclassification,
            'Objcategory': MockObjcategory,
            'Objtype': MockObjtype,
            'Asset': MockAsset
        };
        createController = function() {
            $injector.get('$controller')("ScoreDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'adapApp:scoreUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
