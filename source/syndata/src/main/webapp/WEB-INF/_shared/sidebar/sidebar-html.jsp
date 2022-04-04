<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Secondary navbar -->
<div class="navbar navbar-expand-md navbar-dark" ng-controller="sideBar">
	<div class="text-center d-md-none w-100">
		<button type="button" class="navbar-toggler dropdown-toggle" data-toggle="collapse" data-target="#navbar-navigation">
			<i class="icon-unfold mr-2"></i>
			Navigation
		</button>
	</div>

	<div class="navbar-collapse collapse" id="navbar-navigation">
		<ul class="navbar-nav">
			<li class="nav-item">
				<a href="${pageContext.request.contextPath}/" class="navbar-nav-link active">
					<i class="icon-home4 mr-2"></i>
					Trang chủ
				</a>
			</li>

			<li class="nav-item">
				<a href="${pageContext.request.contextPath}/schedule" class="navbar-nav-link active">
					<i class="icon-database-refresh mr-2"></i>
					Công việc chạy nền
				</a>
			</li>

			<li class="nav-item">
				<a href="${pageContext.request.contextPath}/log" class="navbar-nav-link active">
					<i class="icon-stack-text mr-2"></i>
					Lịch sử
				</a>
			</li>

			<li class="nav-item">
				<a href="${pageContext.request.contextPath}/reset-syn" class="navbar-nav-link active">
					<i class="icon-database-arrow mr-2"></i>
					Reset đồng bộ hệ thống
				</a>
			</li>

			<li class="nav-item">
				<a href="${pageContext.request.contextPath}/setting" class="navbar-nav-link active">
					<i class="icon-gear mr-2"></i>
					Cài đặt
				</a>
			</li>
		</ul>

		<ul class="navbar-nav ml-md-auto">
			
			<li class="nav-item dropdown">
				<a href="#" class="navbar-nav-link dropdown-toggle" data-toggle="dropdown">
					<i class="icon-user"></i>
					<span class="ml-2" ng-bind="user.USER_CODE"></span>
				</a>

				<div class="dropdown-menu dropdown-menu-right">
					<!-- <a href="#" class="dropdown-item"><i class="icon-user-lock"></i> Account security</a>
					<a href="#" class="dropdown-item"><i class="icon-statistics"></i> Analytics</a>
					<a href="#" class="dropdown-item"><i class="icon-accessibility"></i> Accessibility</a>
					<div class="dropdown-divider"></div> -->
					<a href="${pageContext.request.contextPath}/login" class="dropdown-item"><i class="icon-user-lock"></i> Đăng xuất</a>
				</div>
			</li>
		</ul>
	</div>
</div>
<!-- /secondary navbar -->