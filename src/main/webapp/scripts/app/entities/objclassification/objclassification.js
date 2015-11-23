'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('objclassification', {
                parent: 'entity',
                url: '/objclassifications',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objclassification.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objclassification/objclassifications.html',
                        controller: 'ObjclassificationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objclassification');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('objclassification.detail', {
                parent: 'entity',
                url: '/objclassification/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objclassification.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objclassification/objclassification-detail.html',
                        controller: 'ObjclassificationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objclassification');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Objclassification', function($stateParams, Objclassification) {
                        return Objclassification.get({id : $stateParams.id});
                    }]
                }
            })
            .state('objclassification.new', {
                parent: 'objclassification',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objclassification/objclassification-dialog.html',
                        controller: 'ObjclassificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                        $state.go('objclassification', null, { reload: true });
                    }, function() {
                        $state.go('objclassification');
                    })
                }]
            })
            .state('objclassification.edit', {
                parent: 'objclassification',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objclassification/objclassification-dialog.html',
                        controller: 'ObjclassificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Objclassification', function(Objclassification) {
                                return Objclassification.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objclassification', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('objclassification.delete', {
                parent: 'objclassification',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objclassification/objclassification-delete-dialog.html',
                        controller: 'ObjclassificationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Objclassification', function(Objclassification) {
                                return Objclassification.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objclassification', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
