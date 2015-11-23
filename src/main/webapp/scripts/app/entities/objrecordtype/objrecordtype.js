'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('objrecordtype', {
                parent: 'entity',
                url: '/objrecordtypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objrecordtype.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objrecordtype/objrecordtypes.html',
                        controller: 'ObjrecordtypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objrecordtype');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('objrecordtype.detail', {
                parent: 'entity',
                url: '/objrecordtype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objrecordtype.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objrecordtype/objrecordtype-detail.html',
                        controller: 'ObjrecordtypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objrecordtype');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Objrecordtype', function($stateParams, Objrecordtype) {
                        return Objrecordtype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('objrecordtype.new', {
                parent: 'objrecordtype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objrecordtype/objrecordtype-dialog.html',
                        controller: 'ObjrecordtypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    objecttype: null,
                                    name: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('objrecordtype', null, { reload: true });
                    }, function() {
                        $state.go('objrecordtype');
                    })
                }]
            })
            .state('objrecordtype.edit', {
                parent: 'objrecordtype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objrecordtype/objrecordtype-dialog.html',
                        controller: 'ObjrecordtypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Objrecordtype', function(Objrecordtype) {
                                return Objrecordtype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objrecordtype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('objrecordtype.delete', {
                parent: 'objrecordtype',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objrecordtype/objrecordtype-delete-dialog.html',
                        controller: 'ObjrecordtypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Objrecordtype', function(Objrecordtype) {
                                return Objrecordtype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objrecordtype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
