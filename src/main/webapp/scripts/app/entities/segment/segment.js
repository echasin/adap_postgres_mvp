'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('segment', {
                parent: 'entity',
                url: '/segments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.segment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segment/segments.html',
                        controller: 'SegmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segment');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('segment.detail', {
                parent: 'entity',
                url: '/segment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.segment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/segment/segment-detail.html',
                        controller: 'SegmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('segment');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Segment', function($stateParams, Segment) {
                        return Segment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('segment.new', {
                parent: 'segment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/segment/segment-dialog.html',
                        controller: 'SegmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    origineta: null,
                                    originAta: null,
                                    destinationeta: null,
                                    destinationata: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('segment', null, { reload: true });
                    }, function() {
                        $state.go('segment');
                    })
                }]
            })
            .state('segment.edit', {
                parent: 'segment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/segment/segment-dialog.html',
                        controller: 'SegmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Segment', function(Segment) {
                                return Segment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('segment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('segment.delete', {
                parent: 'segment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/segment/segment-delete-dialog.html',
                        controller: 'SegmentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Segment', function(Segment) {
                                return Segment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('segment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
