'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('score', {
                parent: 'entity',
                url: '/scores',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.score.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/score/scores.html',
                        controller: 'ScoreController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('score');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('score.detail', {
                parent: 'entity',
                url: '/score/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.score.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/score/score-detail.html',
                        controller: 'ScoreDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('score');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Score', function($stateParams, Score) {
                        return Score.get({id : $stateParams.id});
                    }]
                }
            })
            .state('score.new', {
                parent: 'score',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/score/score-dialog.html',
                        controller: 'ScoreDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    value: null,
                                    text: null,
                                    rulename: null,
                                    ruleversion: null,
                                    details: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('score', null, { reload: true });
                    }, function() {
                        $state.go('score');
                    })
                }]
            })
            .state('score.edit', {
                parent: 'score',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/score/score-dialog.html',
                        controller: 'ScoreDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Score', function(Score) {
                                return Score.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('score', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('score.delete', {
                parent: 'score',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/score/score-delete-dialog.html',
                        controller: 'ScoreDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Score', function(Score) {
                                return Score.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('score', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
