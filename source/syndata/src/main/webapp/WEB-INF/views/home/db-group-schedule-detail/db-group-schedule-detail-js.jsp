<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('groupScheduleDetail', function ($scope, $rootScope, $http) {
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
        $scope.groupServiceName = null;

        //đăng ký lắng nghe 1 function để chờ ng group job gọi xem chi tiết 1 service
        $rootScope.$on("groupScheduleDetailChange", function (scope, value) {
            $scope.page = value.page;
            $scope.groupServiceName = value.groupName;

            //init
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.listService);

            //animation focus
            setTimeout(() => {
                $('html, body').animate({
                    scrollTop: $('#groupScheduleDetail').offset().top
                }, 800, function () {

                    // Add hash (#) to URL when done scrolling (default click behavior)
                    window.location.hash = '#groupScheduleDetail';
                })
            }, 500);
        });

        this.$onInit = function () {
            //load setting
            if (localStorage.getItem("setting")) {
                $scope.setting = JSON.parse(localStorage.getItem("setting"));
            } else {
                localStorage.setItem("setting", JSON.stringify($scope.setting.listService));
            }
        }

        $scope.showJobDetail = function (serviceName) {
            //call ham jobDetailChange sang ng controler job-detail
            $rootScope.$emit("scheduleDetailChange", { page: $scope.page, serviceName: serviceName });
        }

        $scope.changeTime = function (value) {

            clearInterval($scope.interval);
            $scope.page = value;
            $scope.highcharts.destroy();

            //init refesh
            $scope.highcharts = null;
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.listService);
        }

        $scope.refesh = function (time) {
            return setInterval(function () {
                $scope.getData();
            }, time * 1000);
        }

        $scope.getData = function () {
            var url = $('#contextPath').val() + '/api/dashboard/group-schedule-detail';
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: {
                    data: JSON.stringify({
                        PAGE: $scope.page,
                        SIZE: $scope.size,
                        GROUP_NAME: $scope.groupServiceName
                    })
                }
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
                    if (!item.SCHEDULE_SETTING_NAME) {
                        continue;
                    }
                    var checkIndex = group.findIndex(x => x.name === item.SCHEDULE_SETTING_NAME);
                    if (checkIndex === -1) {
                        group.push({ name: item.SCHEDULE_SETTING_NAME, data: [] });
                    }
                }
                for (const groupItem of group) {
                    groupItem.data = [];
                    for (const d of date) {
                        var totalJobInDate = 0;
                        var listData = response.data.ITEMS.filter(x => d === x.DATE_TEXT && x.SCHEDULE_SETTING_NAME === groupItem.name);
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

        $scope.buildUI = function () {
            $scope.highcharts = Highcharts.chart('db-group-schedule-detail', {

                title: {
                    text: 'Danh sách job của nhóm ' + $scope.groupServiceName
                },

                // subtitle: {
                //     text: 'Danh sách server online'
                // },

                yAxis: {
                    title: {
                        text: 'Số job đã sử lý'
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
                                    $scope.showJobDetail(this.series.name);
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