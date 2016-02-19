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
            .state('asset.newlocation', {
                parent: 'asset',
                url: '/newlocation/{id}',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/asset/location-asset-dialog.html',
                        controller: 'LocationAssetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {address1: null, address2: null, cityname: null, cityaliasname: null, countyname: null, countyfips: null, statename: null, statecode: null, statefips: null, stateiso: null, stateansi: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('asset.detail', {id: $stateParams.id});
                    }, function() {
                        $state.go('asset.detail', {id: $stateParams.id});
                    })
                }]
            }).state('asset.newevent', {
                parent: 'asset',
                url: '/newevent/{id}',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/event/event-dialog.html',
                        controller: 'EventDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                            	return {
                                    name: null,
                                    description: null,
                                    details: null,
                                    eventdate: null,
                                    severity: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null,
                                    asset:$stateParams
                                };             
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('asset.detail', {id: $stateParams.id});
                    }, function() {
                        $state.go('asset.detail', {id: $stateParams.id});
                    })
                }]
            });
            
    });
