<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- import control vnpt paging -->
<%@ include file="/WEB-INF/_components/vnpt-paging/vnpt-paging-js.jsp" %>
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('listLog', function ($scope, $http) {
        $scope.data = [];
        $scope.detail = {};
        $scope.searchKey = '';
        $scope.isLoadone = false;
        $scope.paging = { page: 1, size: 50, totalPage: 1, totalRecord: 0 };

        this.$onInit = function () {

            //init role
            if (localStorage.getItem('User')) {
                $scope.user = JSON.parse(localStorage.getItem('User'));
            }

            var indexServiceName = location.href.indexOf('?serviceName=');
            if (indexServiceName !== -1) {
                $scope.searchKey = decodeURIComponent(location.href.substring(indexServiceName + '?serviceName='.length));
            }

            $scope.getData();
            $scope.goToTop();
        }

        $scope.changePage = function (value) {
            $scope.paging.page = value;
            //init refesh
            $scope.getData();

            $scope.goToTop();
        }

        $scope.goToTop = function () {
            setTimeout(() => {
                $('html, body').animate({
                    scrollTop: 0
                }, 800, function () {
                    window.location.hash = '#';
                });
            }, 300);
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

        $scope.delete = function (item) {
            var text = 'Bạn có chắc chắn muốn xóa dữ liệu này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        var url = $('#contextPath').val() + '/api/schedule-log/delete';
                        $http({
                            url: url,
                            method: 'delete',
                            data: [{
                                SCHEDULE_LOG_ID: item.SCHEDULE_LOG_ID,
                                SCHEDULE_LOG_FILE: item.SCHEDULE_LOG_RETURN.FILES
                            }]
                        }).then(function (response) {
                            //refesh data trang hiện tại;
                            for (const item of response.data.ITEMS) {
                                if (item.STATUS === 'FAIL') {
                                    $.alert({
                                        type: 'red',
                                        title: '',
                                        content: item.TEXT_ERROR
                                    });
                                }
                            }
                            $scope.getData($scope.paging.page);
                        }, function (response) {
                            $.alert({
                                type: 'red',
                                title: '',
                                content: 'Hệ thống gặp lỗi'
                            });
                        });
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.getData = function () {
            var url = $('#contextPath').val() + '/api/schedule-log/select';
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: {
                    data: JSON.stringify({
                        PAGE: $scope.paging.page,
                        SIZE: $scope.paging.size,
                        SCHEDULE_SETTING_NAME: '%' + $scope.searchKey + '%'
                    })
                }
            }).then(function successCallback(response) {
                $scope.paging = {
                    totalPage: Math.ceil(response.data.TOTAL / $scope.paging.size),
                    totalRecord: response.data.TOTAL,
                    page: $scope.paging.page,
                    size: $scope.paging.size
                };

                //update data json truoc khi tra ve cho data
                var data = response.data.ITEMS.map(x => {
                    x.SCHEDULE_LOG_PARAM = JSON.parse(x.SCHEDULE_LOG_PARAM);
                    x.SCHEDULE_LOG_RETURN = JSON.parse(x.SCHEDULE_LOG_RETURN);
                    x.RETRY_LOG = x.RETRY_LOG ? JSON.parse(x.RETRY_LOG) : [];
                    x.SCHEDULE_LOG_FILE = x.SCHEDULE_LOG_FILE ? JSON.parse(x.SCHEDULE_LOG_FILE) : null;
                    return x;
                });

                //tạo group cho data
                var listServiceName = [];
                var stt = 1;
                for (let index = 0; index < data.length; index++) {
                    const item = data[index];
                    var checkGroupExist = listServiceName.findIndex(x => x === item.SCHEDULE_SETTING_NAME);
                    if (checkGroupExist === -1) {
                        listServiceName.push(item.SCHEDULE_SETTING_NAME);
                        data.splice(index, 0, {
                            ISSERVICE: true,
                            STT: listServiceName.length,
                            SERVICENAME: item.SCHEDULE_SETTING_NAME
                        });
                    } else {
                        item['ISSERVICE'] = false;
                        item['STT'] = stt;
                        stt += 1;
                    }
                }

                $scope.data = data;
            });
        }
    });
</script>