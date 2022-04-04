<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('totalTask', function ($scope, $rootScope, $http) {
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
            if (localStorage.getItem("setting") && $scope.setting.totalTask) {
                $scope.setting = JSON.parse(localStorage.getItem("setting"));
            } else {
                localStorage.setItem("setting", JSON.stringify($scope.setting));
            }

            //get api
            $scope.getData();
            // schedule
            $scope.interval = $scope.refesh($scope.setting.totalTask);
        }

        $scope.changeTime = function (value) {

            clearInterval($scope.interval);
            $scope.page = value;
            $scope.highcharts.destroy();

            //init refesh
            $scope.highcharts = null;
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.totalTask);
        }

        $scope.refesh = function (time) {
            return setInterval(function () {
                $scope.getData();
            }, time * 1000);
        }

        $scope.getData = function () {
            var url = $('#contextPath').val() + '/api/dashboard/total-task';
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: { data: JSON.stringify({ PAGE: $scope.page, SIZE: $scope.size }) }
            }).then(function successCallback(response) {
                var date = [];
                for (const item of response.data.ITEMS) {
                    var checkIndex = date.findIndex(x => x === item.DATE);
                    if (checkIndex === -1) {
                        date.push(item.DATE);
                    }
                }
                var group = [
                    { name: 'Tổng task', color: '#03a9f4', data: [] },
                    { name: 'Thành công', color: '#4caf50', data: [] },
                    { name: 'Thất bại', color: '#f44336', data: [] }
                ];

                for (const d of date) {
                    //du lieu da duoc tong hop tren server nen chi can find dung ngay la dc
                    var dataOfDate = response.data.ITEMS.find(x => d === x.DATE);
                    if (dataOfDate) {
                        group.find(x => x.name === 'Tổng task').data.push(dataOfDate.SUCCESS + dataOfDate.ERROR);
                        group.find(x => x.name === 'Thành công').data.push(dataOfDate.SUCCESS);
                        group.find(x => x.name === 'Thất bại').data.push(dataOfDate.ERROR);
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

        //highcharts/examples/line-basic
        $scope.buildUI = function () {
            $scope.highcharts = Highcharts.chart('db-total-task', {

                title: {
                    text: 'Thống kê task đang chạy trong hệ thống'
                },

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

                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true
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