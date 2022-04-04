<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- import control vnpt paging -->
<%@ include file="/WEB-INF/_components/vnpt-paging/vnpt-paging-js.jsp" %>
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('listService', function ($scope, $http) {
        $scope.user = null;
        $scope.data = [];
        $scope.searchKey = '';
        $scope.paramTemplate = [
            {
                OUTFILE: "NAME-FILE_${ID}_${PAGE}_${SUM}.INTERNAL.JSON",
                PAGING: true,
                SIZE: 200,
                API: "http://domain.vn/api/xxx"
            },
            {
                INFILE: "NAME-FILE_${ID}_${PAGE}_${SUM}.INTERNAL.JSON",
                OUTFILE: "NAME_FILE_${ID}_${PAGE}_${SUM}.EXTERNAL.JSON",
                HTTPHEADERS: { "serviceName": "DANH_MUC_QUOC_GIA" },
                API: "http://domain.vn/api/xxx"
            },
            {
                GETDATA: {
                    API: "http://domain.vn/api/xxx",
                    SIZE: 200,
                },
                SYNDATA: {
                    API: "http://domain.vn/api/xxx",
                    METHOD: "PUT OR POST",
                    HTTPHEADERS: {
                        "servicename": "DANH_MUC_QUOC_GIA"
                    }
                },
                VERIFYDATA: {
                    API: "http://domain.vn/api/xxx",
                    METHOD: "PUT OR POST",
                    HTTPHEADERS: {
                        "servicename": "DANH_MUC_QUOC_GIA"
                    }
                }
            },
            {
                API: "http://domain.vn/api/xxx",
                METHOD: "GET",
                PARAMS: {
                    "data": "test"
                },
                HTTPHEADERS: {
                    "servicename": "DANH_MUC_QUOC_GIA"
                }
            }
        ];
        $scope.detail = {
            SCHEDULE_SETTING_ID: 0,
            SCHEDULE_SETTING_NAME: '',
            SCHEDULE_SETTING_GROUP: '',
            SCHEDULE_SETTING_LOOP: 1,
            SCHEDULE_SETTING_TURN: null,
            SCHEDULE_SETTING_RETRY: 5,
            SCHEDULE_SETTING_CRON: '',
            SCHEDULE_SETTING_FUNCTION: 'createFileJob',
            STATUS: 0,
            SCHEDULE_SETTING_PARAM: JSON.stringify($scope.paramTemplate[0])
        };
        $scope.isLoadone = false;
        $scope.paging = { page: 1, size: 20, totalPage: 1, totalRecord: 0 };
        $scope.editorParam = null;

        this.$onInit = function () {
            $scope.getData();

            //init role
            if (localStorage.getItem('User')) {
                $scope.user = JSON.parse(localStorage.getItem('User'));
            }

            // init editor jsonParam
            const containerParam = document.getElementById("jsonParam");
            const options = {
                mode: 'code'
            }
            $scope.editorParam = new JSONEditor(containerParam, options);

            $scope.goToTop();
        }

        $scope.changePage = function (value) {
            $scope.getData(value);
            $scope.goToTop();
        }

        $scope.changeFunction = function () {
            if ($scope.detail.SCHEDULE_SETTING_ID === 0) {
                //new set param data temp
                switch ($scope.detail.SCHEDULE_SETTING_FUNCTION) {
                    case "createFileJob":
                        $scope.detail.SCHEDULE_SETTING_PARAM = JSON.stringify($scope.paramTemplate[0]);
                        break;
                    case "receiveFileJob":
                        $scope.detail.SCHEDULE_SETTING_PARAM = JSON.stringify($scope.paramTemplate[1]);
                        break;
                    case "synDataJob":
                        $scope.detail.SCHEDULE_SETTING_PARAM = JSON.stringify($scope.paramTemplate[2]);
                        break;
                    case "executeApiJob":
                        $scope.detail.SCHEDULE_SETTING_PARAM = JSON.stringify($scope.paramTemplate[3]);
                        break;
                }
                $scope.editorParam.set(JSON.parse($scope.detail.SCHEDULE_SETTING_PARAM));
            }
        }

        $scope.submitForm = function () {
            var text = 'Bạn có chắc chắn muốn lưu dữ liệu service này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        // get json
                        $scope.detail.SCHEDULE_SETTING_PARAM = JSON.stringify($scope.editorParam.get());
                        $scope.submitChangeService();
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.changeStatusService = function (item) {
            $scope.detail = item;
            var statusUpdate = item.STATUS === 1 ? 0 : 1;
            var text = item.STATUS === 1 ? 'Bạn có chắc chắn muốn dừng service này không' : 'Bạn có chắc chắn muốn chạy service này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        $scope.detail.STATUS = statusUpdate;
                        $scope.submitChangeService();
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.submitChangeService = function () {
            var url = $('#contextPath').val() + '/api/schedule-setting/update';
            $http({
                url: url,
                method: 'put',
                data: {
                    SCHEDULE_SETTING_ID: $scope.detail.SCHEDULE_SETTING_ID,
                    SCHEDULE_SETTING_NAME: $scope.detail.SCHEDULE_SETTING_NAME,
                    SCHEDULE_SETTING_LOOP: $scope.detail.SCHEDULE_SETTING_LOOP,
                    SCHEDULE_SETTING_TURN: $scope.detail.SCHEDULE_SETTING_TURN,
                    SCHEDULE_SETTING_RETRY: $scope.detail.SCHEDULE_SETTING_RETRY,
                    SCHEDULE_SETTING_CRON: $scope.detail.SCHEDULE_SETTING_CRON,
                    SCHEDULE_SETTING_FUNCTION: $scope.detail.SCHEDULE_SETTING_FUNCTION,
                    STATUS: $scope.detail.STATUS,
                    SCHEDULE_SETTING_GROUP: $scope.detail.SCHEDULE_SETTING_GROUP,
                    SCHEDULE_SETTING_PARAM: JSON.parse($scope.detail.SCHEDULE_SETTING_PARAM)
                }
            }).then(function (response) {
                $.alert({
                    type: 'green',
                    title: '',
                    content: 'Đã update thành công'
                });
                //refesh data;
                $scope.getData();
            }, function (response) {
                $.alert({
                    type: 'red',
                    title: 'Hệ thống gặp lỗi',
                    content: response.data || response.status
                });
            });
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

        $scope.addService = function () {
            //thêm mới
            var itemAddDefault = {
                SCHEDULE_SETTING_ID: 0,
                SCHEDULE_SETTING_NAME: '',
                SCHEDULE_SETTING_GROUP: '',
                SCHEDULE_SETTING_LOOP: 1,
                SCHEDULE_SETTING_TURN: null,
                SCHEDULE_SETTING_RETRY: 5,
                SCHEDULE_SETTING_CRON: '',
                SCHEDULE_SETTING_FUNCTION: 'createFileJob',
                STATUS: 0,
                SCHEDULE_SETTING_PARAM: JSON.stringify($scope.paramTemplate[0])
            };
            $scope.viewDetail(itemAddDefault);
        }

        $scope.viewDetail = function (item) {

            $scope.detail = item;
            // set json
            $scope.editorParam.set(JSON.parse($scope.detail.SCHEDULE_SETTING_PARAM));

            $('#detail_modal').modal('show');
        }

        $scope.closeViewDetail = function () {
            $('#detail_modal').modal('hide');
        }

        $scope.delete = function (id) {
            var text = 'Bạn có chắc chắn muốn xóa dữ liệu này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        var url = $('#contextPath').val() + '/api/schedule-setting/delete';
                        $http({
                            url: url,
                            method: 'delete',
                            data: [{ SCHEDULE_SETTING_ID: id }]
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

        $scope.runJob = function (item) {

            var text = 'Bạn có chắc chắn muốn chạy ngay service này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        var url = $('#contextPath').val() + '/api/service/run?jobId='
                            + item.SCHEDULE_SETTING_ID + '&jobDescription='
                            + item.SCHEDULE_SETTING_NAME + '&jobGroup='
                            + item.SCHEDULE_SETTING_GROUP + '&functionName='
                            + item.SCHEDULE_SETTING_FUNCTION + '&jobParam='
                            + encodeURI(item.SCHEDULE_SETTING_PARAM);
                        window.open(url);
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.getData = function (page = 1) {
            var url = $('#contextPath').val() + '/api/schedule-setting/select';
            $scope.paging.page = page;
            $scope.datas = [];
            $scope.isLoadone = false;
            $http({
                url: url,
                method: 'get',
                params: {
                    data: JSON.stringify({
                        PAGE: $scope.paging.page,
                        SIZE: $scope.paging.size,
                        SCHEDULE_SETTING_NAME: '%' + $scope.searchKey + '%',
                        SCHEDULE_SETTING_GROUP: '%' + $scope.searchKey + '%'
                    })
                }
            }).then(function successCallback(response) {
                $scope.paging = {
                    totalPage: Math.ceil(response.data.TOTAL / $scope.paging.size),
                    totalRecord: response.data.TOTAL,
                    page: $scope.paging.page,
                    size: $scope.paging.size
                };

                //su ly du lieu nhom group
                var listGroup = [];
                var stt = 1;
                for (let index = 0; index < response.data.ITEMS.length; index++) {
                    const item = response.data.ITEMS[index];

                    var checkGroupExist = listGroup.findIndex(x => x === item.SCHEDULE_SETTING_GROUP);
                    if (checkGroupExist === -1) {
                        listGroup.push(item.SCHEDULE_SETTING_GROUP);
                        response.data.ITEMS.splice(index, 0, {
                            ISGROUP: true,
                            STT: listGroup.length,
                            GROUPNAME: item.SCHEDULE_SETTING_GROUP
                        });
                    } else {
                        item['ISGROUP'] = false;
                        item['STT'] = stt;
                        stt += 1;
                    }
                }
                $scope.data = response.data.ITEMS;
            });
        }
    });
</script>