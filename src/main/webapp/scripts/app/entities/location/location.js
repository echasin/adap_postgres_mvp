'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('location', {
                parent: 'entity',
                url: '/locations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.location.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/location/locations.html',
                        controller: 'LocationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('location');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('location.detail', {
                parent: 'entity',
                url: '/location/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.location.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/location/location-detail.html',
                        controller: 'LocationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('location');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Location', function($stateParams, Location) {
                        return Location.get({id : $stateParams.id});
                    }]
                }
            })
            .state('location.new', {
                parent: 'location',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/location/location-dialog.html',
                        controller: 'LocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    isprimary: null,
                                    address1: null,
                                    address2: null,
                                    cityname: null,
                                    citynamealias: null,
                                    countyname: null,
                                    countyfips: null,
                                    countyansi: null,
                                    statename: null,
                                    statecode: null,
                                    statefips: null,
                                    stateiso: null,
                                    stateansi: null,
                                    zippostcode: null,
                                    countryname: null,
                                    countryiso2: null,
                                    countryiso3: null,
                                    latitudedd: null,
                                    longitudedd: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('location', null, { reload: true });
                    }, function() {
                        $state.go('location');
                    })
                }]
            })
            .state('location.edit', {
                parent: 'location',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/location/location-dialog.html',
                        controller: 'LocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Location', function(Location) {
                                return Location.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('location', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('location.delete', {
                parent: 'location',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/location/location-delete-dialog.html',
                        controller: 'LocationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Location', function(Location) {
                                return Location.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('location', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
