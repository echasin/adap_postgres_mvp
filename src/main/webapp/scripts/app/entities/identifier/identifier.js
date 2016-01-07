'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('identifier', {
                parent: 'entity',
                url: '/identifiers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.identifier.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/identifier/identifiers.html',
                        controller: 'IdentifierController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('identifier');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('identifier.detail', {
                parent: 'entity',
                url: '/identifier/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.identifier.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/identifier/identifier-detail.html',
                        controller: 'IdentifierDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('identifier');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Identifier', function($stateParams, Identifier) {
                        return Identifier.get({id : $stateParams.id});
                    }]
                }
            })
            .state('identifier.new', {
                parent: 'identifier',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/identifier/identifier-dialog.html',
                        controller: 'IdentifierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    value: null,
                                    effectivedatetime: null,
                                    enddatetime: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('identifier', null, { reload: true });
                    }, function() {
                        $state.go('identifier');
                    })
                }]
            })
            .state('identifier.edit', {
                parent: 'identifier',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/identifier/identifier-dialog.html',
                        controller: 'IdentifierDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Identifier', function(Identifier) {
                                return Identifier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('identifier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('identifier.delete', {
                parent: 'identifier',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/identifier/identifier-delete-dialog.html',
                        controller: 'IdentifierDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Identifier', function(Identifier) {
                                return Identifier.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('identifier', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
