'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('asset', {
                parent: 'entity',
                url: '/assets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.asset.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/asset/assets.html',
                        controller: 'AssetController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('asset');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('asset.detail', {
                parent: 'entity',
                url: '/asset/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.asset.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/asset/asset-detail.html',
                        controller: 'AssetDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('asset');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Asset', function($stateParams, Asset) {
                        return Asset.get({id : $stateParams.id});
                    }]
                }
            })
            .state('asset.new', {
                parent: 'asset',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/asset/asset-dialog.html',
                        controller: 'AssetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
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
                        $state.go('asset', null, { reload: true });
                    }, function() {
                        $state.go('asset');
                    })
                }]
            })
            .state('asset.edit', {
                parent: 'asset',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/asset/asset-dialog.html',
                        controller: 'AssetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Asset', function(Asset) {
                                return Asset.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('asset', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('asset.delete', {
                parent: 'asset',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/asset/asset-delete-dialog.html',
                        controller: 'AssetDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Asset', function(Asset) {
                                return Asset.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('asset', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
