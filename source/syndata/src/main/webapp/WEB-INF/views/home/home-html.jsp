<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Content area -->
<div class="content" ng-controller="myHome">

	<%@ include file="/WEB-INF/views/home/server-online/server-online-html.jsp" %>
	<%@ include file="/WEB-INF/views/home/db-total-task/db-total-task-html.jsp" %>

	<%@ include file="/WEB-INF/views/home/db-group-schedule/db-group-schedule-html.jsp" %>

	<%@ include file="/WEB-INF/views/home/db-group-schedule-detail/db-group-schedule-detail-html.jsp" %>
	<%@ include file="/WEB-INF/views/home/db-schedule-detail/db-schedule-detail-html.jsp" %>

</div>
<!-- /content area -->