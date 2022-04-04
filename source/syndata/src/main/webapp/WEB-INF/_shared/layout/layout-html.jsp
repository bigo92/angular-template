<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Schedule Manager</title>

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

	<!-- Core JS files -->
	<script src="${pageContext.request.contextPath}/resources/global_assets/js/main/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/global_assets/js/main/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/loaders/blockui.min.js"></script>
	<!-- /core JS files -->

	<!-- Theme JS files -->
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/visualization/d3/d3.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/visualization/d3/d3_tooltip.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/forms/styling/switchery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/forms/selects/bootstrap_multiselect.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/ui/moment/moment.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/global_assets/js/plugins/pickers/daterangepicker.js"></script>

	<script src="${pageContext.request.contextPath}/resources/assets/js/app.js"></script>
	<!-- /theme JS files -->

	<link href="${pageContext.request.contextPath}/resources/jquery-confirm/jquery-confirm.min.css" rel="stylesheet"
		type="text/css">
	<script src="${pageContext.request.contextPath}/resources/jquery-confirm/jquery-confirm.min.js"></script>

	<link href="${pageContext.request.contextPath}/resources/jsoneditor/jsoneditor.min.css" rel="stylesheet"
		type="text/css">
	<script src="${pageContext.request.contextPath}/resources/jsoneditor/jsoneditor.min.js"></script>

	<style>
		.table-responsive thead th {
			font-size: 14px;
		}

		.table-responsive tbody td {
			font-size: 13px;
		}

		vnpt-paging{
			display: contents;
		}

		/* .content-dark {
			background-color: #262d3c;
		} */
	</style>

</head>

<body ng-app="myApp">
	<tiles:insertAttribute name="navbar"></tiles:insertAttribute>

	<tiles:insertAttribute name="sidebar.html"></tiles:insertAttribute>

	<div class="page-content pt-0">

		<div class="content-wrapper">

			<tiles:insertAttribute name="page.html"></tiles:insertAttribute>

			<tiles:insertAttribute name="footer"></tiles:insertAttribute>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/resources/angular.min.js"></script>
	
	<!-- import myHttpInterceptor -->
	<%@ include file="/WEB-INF/_components/http-interceptor/http-interceptor-js.jsp" %>
	<script>
		var app = angular.module('myApp', []).config(function ($httpProvider) {
			//register interceptor
			$httpProvider.interceptors.push(myHttpInterceptor);
		});
	</script>
	<tiles:insertAttribute name="layout.js"></tiles:insertAttribute>
	<tiles:insertAttribute name="page.js"></tiles:insertAttribute>
	<tiles:insertAttribute name="sidebar.js"></tiles:insertAttribute>
</body>

</html>