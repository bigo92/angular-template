<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
    app.controller('myForm', function ($scope, $http) {
        $scope.message = '';
        $scope.fromData = {
            username: '',
            password: ''
        }

        this.$onInit = function () {
            $('.form-input-styled').uniform();
            //xoa thong tin dang nhap cu
            localStorage.removeItem("User");
            //set het han cookie
            document.cookie = 'Authorization=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        }

        $scope.onSubmit = function () {
            if (!$scope.fromData.username || !$scope.fromData.password) {
                return;
            }

            $scope.message = '';
            var url = $('#contextPath').val() + '/public/aut/generate-token';
            $http({
                url: url,
                method: 'post',
                data: $scope.fromData
            }).then(function (response) {
                var user = response.data.user;
                localStorage.setItem("User", user);
                var urlBack = $scope.getParameterByName('urlback');
                if (!urlBack) { urlBack = $('#contextPath').val(); }
                location.href = $('#contextPath').val() + "/public/add-token?token=" + encodeURI(response.data.token) + "&urlback=" + urlBack;
            }, function (response) {
                $scope.message = 'Sai tài khoản hoặc mật khẩu';
            });
        }

        $scope.getParameterByName = function (name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }
    });
</script>