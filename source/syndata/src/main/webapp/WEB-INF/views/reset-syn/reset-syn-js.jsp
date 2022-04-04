<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- import control vnpt paging -->
<%@ include file="/WEB-INF/_components/vnpt-paging/vnpt-paging-js.jsp" %>
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('resetSyn', function ($scope, $http) {
        $scope.data = [
            { STT: 1, NAME: 'Vùng Ngoài', SYSNAME: 'EXTERNAL' },
            { STT: 2, NAME: 'Dân Cư', SYSNAME: 'CITIZEN' },
            { STT: 3, NAME: 'Cư Trú', SYSNAME: 'RESIDENT' },
            { STT: 4, NAME: 'Định Danh', SYSNAME: 'IDENTIFIER' }
        ];

        this.$onInit = function () {
            $scope.goToTop();
        }

        $scope.resetAll = function () {
            var text = 'Bạn có chắc chắn muốn khôi phục đồng bộ tất cả hệ thống này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        $scope.submitChangeService({ SYSNAME: 'ALL' });
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.resetItem = function (item) {
            var text = 'Bạn có chắc chắn muốn khôi phục hệ thống này không';
            $.confirm({
                type: 'red',
                title: '',
                content: text,
                buttons: {
                    'Đồng ý': function () {
                        $scope.submitChangeService(item);
                    },
                    'Hủy bỏ': function () {

                    }
                }
            });
        }

        $scope.submitChangeService = function (item) {
            var url = $('#contextPath').val() + '/api/service/reset';
            
            $http({
                url: url,
                method: 'put',
                params: {
                    SYSNAME : item.SYSNAME
                }
            }).then(function successCallback(response) {
                $.alert({
                    type: 'green',
                    title: '',
                    content: 'Đã update thành công'
                });
            }, function errorCallback() {
                $.alert({
                    type: 'red',
                    title: '',
                    content: 'Cập nhật thất bại'
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

    });
</script>