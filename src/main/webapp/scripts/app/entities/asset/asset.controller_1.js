'use strict';

angular.module('adapApp')
        .controller('AssetController', function ($scope, $state, $modal, Asset, AssetSearch, ParseLinks,
                $compile, $resource, DTOptionsBuilder, DTColumnDefBuilder, DTColumnBuilder) {

            $scope.assets = [];
            $scope.page = 0;

            $scope.loadAll = function () {
                Asset.query({page: $scope.page, size: 20}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.assets = result;
                });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();


            $scope.search = function () {
                AssetSearch.query({query: $scope.searchQuery}, function (result) {
                    $scope.assets = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAll();
                    }
                });
            };

            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.asset = {
                    name: null,
                    //description: null,
                    //details: null,
                    //status: null,
                    //lastmodifiedby: null,
                    //lastmodifieddate: null,
                    //domain: null,
                    id: null
                };
            };

            $scope.dtOptions = DTOptionsBuilder.newOptions()

                    .withOption('ajax', {
                        url: 'api/assets',
                        type: 'GET'
                    })
                    .withPaginationType('full_numbers')
                    .withDataProp(function (json) {
                        json.draw = 1;
                        json.recordsFiltered = json.length
                        json.recordsTotal = json.length
                        console.log(json)
                        return json;
                    })
                    //.withOption('responsive', false)
                    //.withOption('processing', true)
                    //.withOption("bPaginate", true)
                    //.withOption('order', [[0, 'asc'], [1, 'asc']])
                    //.withOption('bInfo', true)
                    //.withPaginationType('full_numbers')

                    // Add DOM Controls
                    .withDOM('<"pull-right"B>lfrt<"bottom"ip><"clear">')

                    // Add Bootstrap compatibility
                     .withBootstrapOptions({
                     pagination: {
                     classes: {
                     ul: 'pagination  pagination-sm'
                     }
                     }
                     })


                    .withSelect({
                        style: 'os',
                        selector: 'td:first-child'
                    })

                    .withButtons([
                        //'columnsToggle',
                        'colvis',
                        'copy',
                        'pdf',
                        //'csv',
                        'print',
                        'excel'
                                //,
                                // {
                                //    text: 'Some button',
                                //    key: '1',
                                //    action: function (e, dt, node, config) {
                                //        alert('Button activated');
                                //    }
                                //}
                    ])
                    .withColumnFilter({
                        aoColumns: [{
                                type: 'number'
                            }, {
                                type: 'text',
                                bRegex: true,
                                bSmart: true
                            }]
                    }).withOption('initComplete', function (settings) {
                $compile(angular.element('#' + settings.sTableId).contents())($scope);
            });


            $scope.dtColumns =
                    [
                        DTColumnBuilder.newColumn(null).withTitle('')
                                .notSortable()
                                .withClass('select-checkbox')
                                // Need to define the mRender function, otherwise we get a [Object Object]
                                .renderWith(function () {
                                    return '';
                                }),
                        DTColumnBuilder.newColumn('id').withTitle('ID').withClass('text-danger'),
                        DTColumnBuilder.newColumn('name').withTitle('Name').withClass('none'),
                        DTColumnBuilder.newColumn('objclassification.name').withTitle('Classification')
                                /** 
                                 DTColumnBuilder.newColumn(null).withTitle('Actions').notSortable()
                                 .renderWith(function (data, type, full, meta) {
                                 return '<button id="edit" class="btn btn-primary btn-sm" ui-sref="asset.edit({id:' + data.id + '})">' +
                                 '   <i class="fa fa-edit"></i>' +
                                 '</button>&nbsp;' +
                                 '<button class="btn btn-danger btn-sm"  ng-click="delete(' + data.id + ')">' +
                                 '   <i class="fa fa-trash-o"></i>' +
                                 '</button>';
                                 })
                                 **/
                    ];

        });
