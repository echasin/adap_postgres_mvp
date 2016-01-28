'use strict';

angular.module('adapApp')
        .controller('RouteScoreDetailsController', function ($scope, $resource, $rootScope, $stateParams, entity, Route, Score, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {
            $scope.route = entity;
            $scope.jsonData = [];

            function getAverageScore() {
                console.log("$stateParams " + $stateParams.id);
                var output = $resource('api/averageScore/:id', {
                    id: $stateParams.id
                }, {
                    query: {
                        method: 'GET',
                        transformResponse: function (data) {
                            data = angular.fromJson(data);
                            for (var i = 0; i < data.length; ++i) {
                                if (data[i].details === null) {
                                    $scope.jsonData = null;
                                } else {
                                    var array = JSON.parse(data[i].details);
                                    $scope.jsonData.push(array);
                                }
                            }
                            return $scope.jsonData;
                        },
                        isArray: true
                    }
                });
                $scope.jsonData = output.query()
                return output.query();
            }
            ;

            getAverageScore();


            function getPage() {
                getAverageScore().$promise.then(function (result) {
                	 $('#dataSelector').igPivotDataSelector({
                         dataSource: $scope.jsonData,
                         height: "565px",
                         width: "300px"
                     });
                	$(function () {
                        var $pivotGrid = $("#pivotGrid"),
                            $transposeCheckBox = $("#transpose"),
                            $chart = $("#olapChart"),
                            hasValue = function (value) {
                                return value !== undefined && value !== null && value.count() > 0;
                            },
                            dataSource = new $.ig.OlapFlatDataSource({
                                dataSource: $scope.jsonData,
                                metadata: {
                                    cube: {
                                        name: "Sales",
                                        caption: "Sales",
                                        measuresDimension: {
                                            caption: "Measures",
                                            measures: [
                                            { caption: "Score Value", name: "Value", aggregator: $.ig.OlapUtilities.prototype.sumAggregator('Value') }]
                                       
                                        },
                                        dimensions: [
                                            {
                                                caption: "ScenarioName", name: "ScenarioName", hierarchies:  [{
                                                    name: "ScenarioName", levels: [
                                                                             {
                                                                                 name: "All Scenario Names",
                                                                                 memberProvider: function (item) { return "All Scenario Names"; }
                                                                             },
                                                                             {
                                                                                 name: "ScenarioName",
                                                                                 memberProvider: function (item) { return item.ScenarioName; }
                                                                             }]
                                                                       }]
                                            }
                                        ]
                                    }
                                },
                                rows: "[ScenarioName].[ScenarioName]",
                                measures: "[Measures].[Value]"
                            }),
                            getCellData = function (rowIndex, columnIndex, columnCount, cells) {
                                var cellOrdinal = (rowIndex * columnCount) + columnIndex;
                                if (!hasValue(cells)) {
                                    return 0;
                                }
                                for (var index = 0; index < cells.count() ; index++) {
                                    var cell = cells.item(index);
                                    if (cell.cellOrdinal() == cellOrdinal) {
                                        return new Number(cell.value());
                                    }
                                }
                                return 0;
                            },
                            updateChart = function (tableView, transpose) {
                                var columnHeaders,
                                    rowHeaders,
                                    cells = tableView.resultCells(),
                                    dataArray = [],
                                    series = [],
                                    rowHeaderIndex,
                                    columnHeaderIndex,
                                    ds,
                                    headerCell,
                                    columnCount,
                                    rowCount,
                                    data;

                                if (transpose) {
                                    columnHeaders = tableView.rowHeaders(),
                                        rowHeaders = tableView.columnHeaders()
                                }
                                else {
                                    columnHeaders = tableView.columnHeaders(),
                                        rowHeaders = tableView.rowHeaders()
                                }

                                if (!hasValue(cells) && !hasValue(rowHeaders) && !hasValue(columnHeaders)) {
                                    $chart.igDataChart("destroy");
                                    return;
                                }

                                if (!hasValue(rowHeaders)) {
                                    rowHeaders = [{ caption: function () { return ""; } }];
                                }

                                if (!hasValue(columnHeaders)) {
                                    columnHeaders = [{ caption: function () { return ""; } }];
                                }

                                for (rowHeaderIndex = 0; rowHeaderIndex < rowHeaders.count() ; rowHeaderIndex++) {
                                    headerCell = rowHeaders.item(rowHeaderIndex);
                                    columnCount = columnHeaders.count();
                                    rowCount = rowHeaders.count();
                                    data = { caption: headerCell.caption() };
                                    var value;
                                    for (columnHeaderIndex = 0; columnHeaderIndex < columnCount; columnHeaderIndex++) {
                                        if (transpose) {
                                            value = getCellData(columnHeaderIndex, rowHeaderIndex, rowCount, cells, transpose);
                                        }
                                        else {
                                            value = getCellData(rowHeaderIndex, columnHeaderIndex, columnCount, cells, transpose);
                                        }
                                        data["col" + columnHeaderIndex] = value;
                                    }

                                    dataArray[rowHeaderIndex] = data;
                                };

                                for (columnHeaderIndex = 0; columnHeaderIndex < columnHeaders.count() ; columnHeaderIndex++) {
                                    series[columnHeaderIndex] = {
                                        name: "series" + columnHeaderIndex,
                                        title: columnHeaders.item(columnHeaderIndex).caption(),
                                        type: "column",
                                        xAxis: "xAxis",
                                        yAxis: "yAxis",
                                        valueMemberPath: "col" + columnHeaderIndex
                                    };
                                };

                                ds = new $.ig.DataSource({ dataSource: dataArray });

                                if ($chart.data("igDataChart")) {
                                    $chart.igDataChart("destroy");
                                }
                                $chart.igDataChart({
                                    width: "700px",
                                    height: "500px",
                                    dataSource: ds,
                                    series: series,
                                    legend: { element: "olapChartLegend" },
                                    axes: [{
                                        name: "xAxis",
                                        type: "categoryX",
                                        label: "caption"
                                    },
                                    {
                                        name: "yAxis",
                                        type: "numericY"
                                    }],
                                    horizontalZoomable: true,
                                    verticalZoomable: true,
                                    windowResponse: "immediate"
                                });
                            };

                        $pivotGrid.igPivotGrid({
                            dataSource: dataSource,
                            pivotGridRendered: function () {
                                updateChart($pivotGrid.data("igPivotGrid")._tableView, $transposeCheckBox.is(':checked'));
                            },
                            hideFiltersDropArea: true,
                            disableColumnsDropArea: true,
                            disableRowsDropArea: true,
                            disableMeasuresDropArea: true
                        });
                        $transposeCheckBox.click(function () {
                            updateChart($pivotGrid.data("igPivotGrid")._tableView, $transposeCheckBox.is(':checked'));
                        });
                    });

                });
            }
            getPage();
        });
