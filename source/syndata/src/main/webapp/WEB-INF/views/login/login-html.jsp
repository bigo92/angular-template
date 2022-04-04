<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng nhập</title>

    <!-- Global stylesheets -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet"
        type="text/css">
    <link href="${pageContext.request.contextPath}/resources/global_assets/css/icons/icomoon/styles.min.css"
        rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/assets/css/bootstrap.min.css" rel="stylesheet"
        type="text/css">
    <link href="${pageContext.request.contextPath}/resources/assets/css/bootstrap_limitless.min.css" rel="stylesheet"
        type="text/css">
    <link href="${pageContext.request.contextPath}/resources/assets/css/layout.min.css" rel="stylesheet"
        type="text/css">
    <link href="${pageContext.request.contextPath}/resources/assets/css/components.min.css" rel="stylesheet"
        type="text/css">
    <link href="${pageContext.request.contextPath}/resources/assets/css/colors.min.css" rel="stylesheet"
        type="text/css">
    <!-- /global stylesheets -->

    <!-- Core JS files -->
    <script src="${pageContext.request.contextPath}/resources/global_assets/js/main/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/global_assets/js/main/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/loaders/blockui.min.js"></script>
    <!-- /core JS files -->

    <!-- Theme JS files -->
    <script
        src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/forms/styling/uniform.min.js"></script>

    <script src="${pageContext.request.contextPath}/resources/assets/js/app.js"></script>
    <!-- /theme JS files -->

    <style>
        body {
            background-image: url('${pageContext.request.contextPath}/resources/upload/login-backgroud.jpg');

            background-color: #030408;
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center;
        }
    </style>
</head>

<body ng-app="myApp">

    <!-- Page content -->
    <div class="page-content" ng-controller="myForm">

        <!-- Main content -->
        <div class="content-wrapper">

            <!-- Content area -->
            <div class="content d-flex justify-content-center align-items-center">

                <!-- Login card -->
                <form class="login-form" ng-submit="onSubmit()">
                    <div class="card mb-0">
                        <div class="card-body">
                            <div class="text-center mb-3">
                                <h5 class="mb-0">HỆ THỐNG TÁC VỤ CHẠY NỀN</h5>
                                <span class="d-block text-muted">Đăng nhập bằng tài khoản</span>
                                <span ng-if="message" class="text-danger">{{message}}</span>
                            </div>

                            <div class="form-group form-group-feedback form-group-feedback-left">
                                <input type="text" ng-model="fromData.username" class="form-control" placeholder="Nhập tài khoản" required oninvalid="this.setCustomValidity('Không được để trống')" oninput="this.setCustomValidity('')">
                                <div class="form-control-feedback">
                                    <i class="icon-user text-muted"></i>
                                </div>
                            </div>

                            <div class="form-group form-group-feedback form-group-feedback-left">
                                <input type="password" ng-model="fromData.password" class="form-control" placeholder="Nhập mật khẩu" required oninvalid="this.setCustomValidity('Không được để trống')" oninput="this.setCustomValidity('')">
                                <div class="form-control-feedback">
                                    <i class="icon-lock2 text-muted"></i>
                                </div>
                            </div>

                            <div class="form-group d-flex align-items-center">
                                <div class="form-check mb-0">
                                    <label class="form-check-label">
                                        <input type="checkbox" name="remember" class="form-input-styled" required checked
                                            data-fouc>
                                        Nhớ mật khẩu
                                    </label>
                                </div>

                                <!-- <a href="login_password_recover.html" class="ml-auto">Forgot password?</a> -->
                            </div>

                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">Đăng nhập <i
                                        class="icon-circle-right2 ml-2"></i></button>
                            </div>

                        </div>
                    </div>
                </form>
                <!-- /login card -->

            </div>
            <!-- /content area -->

        </div>
        <!-- /main content -->

    </div>
    <!-- /page content -->

</body>

<script src="${pageContext.request.contextPath}/resources/angular.min.js"></script>
<script>
    var app = angular.module('myApp', []);
</script>
<tiles:insertAttribute name="login.js"></tiles:insertAttribute>

</html>