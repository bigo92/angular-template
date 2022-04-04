<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('scheduleDetail', function ($scope, $rootScope, $http) {
        $scope.setting = {
            general: 10,
            totalTask: 11,
            listGroup: 12,
            listService: 13,
            detailService: 14
        }
        $scope.data = [];
        $scope.page = 1;
        $scope.size = 10; //10 day
        $scope.interval = null;
        $scope.serviceName = null;
        $scope.detail = {};

        //đăng ký lắng nghe 1 function để chờ ng group job gọi xem chi tiết 1 service
        $rootScope.$on("scheduleDetailChange", function (scope, value) {
            $scope.page = value.page;
            $scope.serviceName = value.serviceName;

            //init
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.detailService);

            //animation focus
            setTimeout(() => {
                $('html, body').animate({
                    scrollTop: $('#scheduleDetail').offset().top
                }, 800, function () {

                    // Add hash (#) to URL when done scrolling (default click behavior)
                    window.location.hash = '#scheduleDetail';
                })
            }, 500);
        });

        this.$onInit = function () {
            //load setting
            if (localStorage.getItem("setting")) {
                $scope.setting = JSON.parse(localStorage.getItem("setting"));
            } else {
                localStorage.setItem("setting", JSON.stringify($scope.setting.detailService));
            }
        }

        $scope.viewDetail = function (id) {
            var url = $('#contextPath').val() + '/api/schedule-log/find-one';
            $http({
                url: url,
                method: 'get',
                params: {
                    data: JSON.stringify({
                        SCHEDULE_LOG_ID: id
                    })
                }
            }).then(function successCallback(response) {
                $scope.detail = response.data.ITEMS;

                // create the editor
                const containerParam = document.getElementById("jsonParam");
                const containerResult = document.getElementById("jsonResult");
                const containerRetry = document.getElementById("jsonRetry");
                const options = {
                    mode: 'code'
                }
                const editorParam = new JSONEditor(containerParam, options);
                const editorResult = new JSONEditor(containerResult, options);
                const editorRetry = new JSONEditor(containerRetry, options);

                // set json
                editorParam.set(JSON.parse($scope.detail.SCHEDULE_LOG_PARAM));
                editorResult.set(JSON.parse($scope.detail.SCHEDULE_LOG_RETURN));
                editorRetry.set(JSON.parse($scope.detail.RETRY_LOG ? $scope.detail.RETRY_LOG : '[]'));

                $('#detail_modal').modal('show');
            });
        }

        $scope.closeViewDetail = function () {
            $('#detail_modal').modal('hide');
        }

        $scope.changeTime = function (value) {
            clearInterval($scope.interval);
            $scope.page = value;

            //init refesh
            $scope.getData();
            $scope.interval = $scope.refesh($scope.setting.detailService);
        }

        $scope.refesh = function (time) {
            return setInterval(function () {
                $scope.getData();
            }, time * 1000);
        }

        $scope.getData = function () {
            var url = $('#contextPath').val() + '/api/dashboard/schedule-detail';
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: {
                    data: JSON.stringify({
                        PAGE: $scope.page,
                        SIZE: $scope.size,
                        SERVICE_NAME: $scope.serviceName
                    })
                }
            }).then(function successCallback(response) {
                //group date time data
                var listGroup = [];
                var stt = 1;
                var data = [];

                for (let index = 0; index < response.data.ITEMS.length; index++) {
                    const item = response.data.ITEMS[index];
                    var checkGroupExist = listGroup.findIndex(x => x === item.DATE_TEXT);
                    if (checkGroupExist === -1) {
                        listGroup.push(item.DATE_TEXT);
                        data.push({
                            ISGROUP: true,
                            GROUPNAME: item.DATE_TEXT
                        });
                    }
                    //các data status null sẽ ko hiển thị
                    if (item.STATUS) {
                        item['ISGROUP'] = false;
                        item['STT'] = stt;
                        data.push(item);
                        stt++;
                    }
                }
                $scope.data = data;
            });
        }
    });
</script>