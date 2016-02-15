'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scorefactor', {
                parent: 'entity',
                url: '/scorefactors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.scorefactor.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scorefactor/scorefactors.html',
                        controller: 'ScorefactorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scorefactor');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('scorefactor.detail', {
                parent: 'entity',
                url: '/scorefactor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.scorefactor.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scorefactor/scorefactor-detail.html',
                        controller: 'ScorefactorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('scorefactor');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Scorefactor', function($stateParams, Scorefactor) {
                        return Scorefactor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('scorefactor.new', {
                parent: 'scorefactor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scorefactor/scorefactor-dialog.html',
                        controller: 'ScorefactorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    matchattribute: null,
                                    matchvalue: null,
                                    scorevalue: null,
                                    scoretext: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('scorefactor', null, { reload: true });
                    }, function() {
                        $state.go('scorefactor');
                    })
                }]
            })
            .state('scorefactor.edit', {
                parent: 'scorefactor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scorefactor/scorefactor-dialog.html',
                        controller: 'ScorefactorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Scorefactor', function(Scorefactor) {
                                return Scorefactor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scorefactor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('scorefactor.delete', {
                parent: 'scorefactor',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scorefactor/scorefactor-delete-dialog.html',
                        controller: 'ScorefactorDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Scorefactor', function(Scorefactor) {
                                return Scorefactor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scorefactor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
