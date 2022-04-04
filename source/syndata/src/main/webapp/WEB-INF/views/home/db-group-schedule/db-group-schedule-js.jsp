<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('groupSchedule', function ($scope, $rootScope, $http) {
        $scope.setting = {
            general: 10,
            totalTask: 11,
            listGroup: 12,
            listService: 13,
            detailService: 14
        }
        $scope.data = [];
        $scope.highcharts = null;
        $scope.categoriesData = [];
        $scope.seriesData = [];
        $scope.page = 1;
        $scope.size = 10; //10 day
        $scope.interval = null;

        this.$onInit = function () {
            //load setting
            if (localStorage.getItem("setting")) {
                $scope.setting = JSON.parse(localStorage.getItem("setting"));
            } else {
                localStorage.setItem("setting", JSON.stringify($scope.setting));
            }
            //get api
            $scope.getData();
            // schedule
            $scope.interval = $scope.refesh($scope.setting.listGroup);
        }

        $scope.changeTime = function (value) {

            clearInterval($scope.interval);
            $scope.page = value;
            $scope.highcharts.destroy();

            //init refesh
            $scope.highcharts = null;
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.listGroup);
        }

        $scope.refesh = function (time) {
            return setInterval(function () {
                $scope.getData();
            }, time * 1000);
        }

        $scope.getData = function () {
            var url = $('#contextPath').val() + '/api/dashboard/group-schedule';
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: { data: JSON.stringify({ PAGE: $scope.page, SIZE: $scope.size }) }
            }).then(function successCallback(response) {
                var date = [];
                for (const item of response.data.ITEMS) {
                    var checkIndex = date.findIndex(x => x === item.DATE_TEXT);
                    if (checkIndex === -1) {
                        date.push(item.DATE_TEXT);
                    }
                }
                var group = [];
                for (const item of response.data.ITEMS) {
                    if (!item.SCHEDULE_SETTING_GROUP) {
                        continue;
                    }
                    var checkIndex = group.findIndex(x => x.name === item.SCHEDULE_SETTING_GROUP);
                    if (checkIndex === -1) {
                        group.push({ name: item.SCHEDULE_SETTING_GROUP, data: [] });
                    }
                }
                for (const groupItem of group) {
                    groupItem.data = [];
                    for (const d of date) {
                        var totalJobInDate = 0;
                        var listData = response.data.ITEMS.filter(x => d === x.DATE_TEXT && x.SCHEDULE_SETTING_GROUP === groupItem.name);
                        for (const item of listData) {
                            var countJob = item.QUANTITY_JOB;
                            if (!countJob) { countJob = 0; }
                            totalJobInDate += countJob;
                        }
                        groupItem.data.push([d, totalJobInDate]);
                    }
                }
                //update UI
                $scope.categoriesData = date;
                $scope.seriesData = group;
                if ($scope.highcharts) {
                    if ($scope.highcharts.series.length === group.length) {
                        for (let index = 0; index < group.length; index++) {
                            const dataUpdate = group[index];
                            $scope.highcharts.series[index].setData(dataUpdate.data);
                        }
                        $scope.highcharts.redraw();
                    } else {
                        //refesh ui
                        $scope.highcharts.destroy();
                        $scope.buildUI();
                    }
                } else {
                    $scope.buildUI();
                }
            });
        }

        $scope.showGroupJobDetail = function (groupName) {
            //call hamf showGroupJobDetail sang ng controler group-detail
            $rootScope.$emit("groupScheduleDetailChange", {page: $scope.page, groupName: groupName});
        }

        //highcharts/examples/line-basic
        $scope.buildUI = function () {
            $scope.highcharts = Highcharts.chart('db-group-schedule', {

                title: {
                    text: 'Danh sách nhóm Schedule'
                },

                // subtitle: {
                //     text: 'Danh sách server online'
                // },

                yAxis: {
                    title: {
                        text: 'Số task đã sử lý'
                    }
                },

                xAxis: {
                    title: {
                        text: 'Thời gian'
                    },
                    categories: $scope.categoriesData
                },

                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle'
                },

                plotOptions: {
                    series: {
                        cursor: 'pointer',
                        label: {
                            connectorAllowed: false
                        },
                        point: {
                            events: {
                                click: function () {
                                    $scope.showGroupJobDetail(this.series.name);
                                }
                            }
                        }
                    }
                },

                series: $scope.seriesData,

                responsive: {
                    rules: [{
                        condition: {
                            maxWidth: 500
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom'
                            }
                        }
                    }]
                }

            });
        }
    });
</script>