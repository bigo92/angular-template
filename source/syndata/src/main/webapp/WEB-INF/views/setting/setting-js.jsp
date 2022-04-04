<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('setting', function ($scope, $http) {
        $scope.setting = {
            general: 10,
            totalTask: 11,
            listGroup: 12,
            listService: 13,
            detailService: 14
        }

        this.$onInit = function () {
            if (localStorage.getItem("setting")) {
                $scope.setting = JSON.parse(localStorage.getItem("setting"));
            } else {
                localStorage.setItem("setting", JSON.stringify($scope.setting));
            }
        }

        $scope.submitForm = function () {
            localStorage.setItem("setting", JSON.stringify($scope.setting));
            $.alert({
                type: 'green',
                title: '',
                content: 'Đã update thành công'
            });
        }
    });
</script>