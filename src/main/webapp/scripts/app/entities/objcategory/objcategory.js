'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('objcategory', {
                parent: 'entity',
                url: '/objcategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objcategory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objcategory/objcategorys.html',
                        controller: 'ObjcategoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objcategory');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('objcategory.detail', {
                parent: 'entity',
                url: '/objcategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objcategory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objcategory/objcategory-detail.html',
                        controller: 'ObjcategoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objcategory');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Objcategory', function($stateParams, Objcategory) {
                        return Objcategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('objcategory.new', {
                parent: 'objcategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objcategory/objcategory-dialog.html',
                        controller: 'ObjcategoryDialogController',
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
                        $state.go('objcategory', null, { reload: true });
                    }, function() {
                        $state.go('objcategory');
                    })
                }]
            })
            .state('objcategory.edit', {
                parent: 'objcategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objcategory/objcategory-dialog.html',
                        controller: 'ObjcategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Objcategory', function(Objcategory) {
                                return Objcategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objcategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('objcategory.delete', {
                parent: 'objcategory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objcategory/objcategory-delete-dialog.html',
                        controller: 'ObjcategoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Objcategory', function(Objcategory) {
                                return Objcategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objcategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
