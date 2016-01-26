'use strict';

angular.module('adapApp')
        .controller('RouteScoreDetailsController', function ($scope, $location,$resource, $rootScope, $stateParams, entity, Route, Score, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {
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


            function getData() {
                getAverageScore().$promise.then(function (result) {
                	var dataSource = new $.ig.OlapFlatDataSource({
                        dataSource: $scope.jsonData,
                        metadata: {
                            cube: {
                                name: "Sales",
                                caption: "Route Scores",
                                measuresDimension: {
                                    caption: "Measures",
                                    measures: [
                                        {
                                            caption: "Score Value", name: "Value",
                                            aggregator: $.ig.OlapUtilities.prototype.sumAggregator('Value')
                                        }]
                                },
                                dimensions: [
                                    {
                                        caption: "Rules", name: "ScoreCategory", hierarchies: [{
                                                caption: "Score Category", name: "ScoreCategory", levels: [
                                                    {
                                                        name: "AllCategories", caption: "All Categories",
                                                        memberProvider: function (item) {
                                                            return "All Categories";
                                                        }
                                                    },
                                                    {
                                                        name: "CategoryName", caption: "Category",
                                                        memberProvider: function (item) {
                                                            return item.ScoreCategoryName;
                                                        }
                                                    }]
                                            }]
                                    },
                                    {
                                        caption: "Scenarios", name: "Scenario", hierarchies: [{
                                                caption: "Scenario Category", name: "Scenario", levels: [
                                                    {
                                                        name: "AllScenarios", caption: "All Scenarios",
                                                        memberProvider: function (item) {
                                                            return "All Scenarios";
                                                        }
                                                    },
                                                    {
                                                        name: "ScenarioName", caption: "Scenario",
                                                        memberProvider: function (item) {
                                                            return item.ScenarioName;
                                                        }
                                                    }]
                                            }]
                                    }
                                ]
                            }
                        },
                        rows: "[Scenario].[Scenario]",
                        columns: "[ScoreCategory].[ScoreCategory]",
                        measures: "[Measures].[Value]"
                    });

                    $('#dataSelector').igPivotDataSelector({
                        dataSource: dataSource,
                        height: "565px",
                        width: "300px"
                    });


                    if ($scope.jsonData === null) {
                    	$location.path("noscore");
                    } else {
                        $("#pivotGrid").igPivotGrid({
                            dataSource: dataSource,
                            height: "100%",
                            width: "100%"
                        });
                    
                    $("#chart").igDataChart({
                        width: "100%",
                        height: "300px",
                        title: "Route Score Details",
                        subtitle: "",
                        dataSource: $scope.jsonData,
                        axes: [
                            {
                                name: "NameAxis",
                                type: "categoryX",
                                title: "Scenario Name",
                                label: "ScenarioName"
                            },
                            {
                                name: "PopulationAxis",
                                type: "numericY",
                                minimumValue: 0,
                                title: "Value",
                            }
                        ],
                        series: [
                            {
                                name: "value",
                                type: "column",
                                isHighlightingEnabled: true,
                                isTransitionInEnabled: true,
                                xAxis: "NameAxis",
                                yAxis: "PopulationAxis",
                                valueMemberPath: "Value"
                            }
                        ]
                    });
                }
                });
            }
            getData();
        });
