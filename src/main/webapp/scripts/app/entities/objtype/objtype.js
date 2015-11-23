'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('objtype', {
                parent: 'entity',
                url: '/objtypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objtype.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objtype/objtypes.html',
                        controller: 'ObjtypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objtype');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('objtype.detail', {
                parent: 'entity',
                url: '/objtype/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.objtype.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/objtype/objtype-detail.html',
                        controller: 'ObjtypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('objtype');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Objtype', function($stateParams, Objtype) {
                        return Objtype.get({id : $stateParams.id});
                    }]
                }
            })
            .state('objtype.new', {
                parent: 'objtype',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objtype/objtype-dialog.html',
                        controller: 'ObjtypeDialogController',
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
                        $state.go('objtype', null, { reload: true });
                    }, function() {
                        $state.go('objtype');
                    })
                }]
            })
            .state('objtype.edit', {
                parent: 'objtype',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objtype/objtype-dialog.html',
                        controller: 'ObjtypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Objtype', function(Objtype) {
                                return Objtype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objtype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('objtype.delete', {
                parent: 'objtype',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/objtype/objtype-delete-dialog.html',
                        controller: 'ObjtypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Objtype', function(Objtype) {
                                return Objtype.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('objtype', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
