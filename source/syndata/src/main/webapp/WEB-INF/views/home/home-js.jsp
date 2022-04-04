<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resources/highcharts/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/resources/highcharts/modules/series-label.js"></script>
<script src="${pageContext.request.contextPath}/resources/highcharts/modules/exporting.js"></script>
<script src="${pageContext.request.contextPath}/resources/highcharts/modules/export-data.js"></script>
<script src="${pageContext.request.contextPath}/resources/highcharts/modules/accessibility.js"></script>

<!-- import control chart paging -->
<%@ include file="/WEB-INF/_components/high-chart-paging/high-chart-paging-js.jsp" %>

<%@ include file="/WEB-INF/views/home/server-online/server-online-js.jsp" %>
<%@ include file="/WEB-INF/views/home/db-total-task/db-total-task-js.jsp" %>

<%@ include file="/WEB-INF/views/home/db-group-schedule/db-group-schedule-js.jsp" %>

<%@ include file="/WEB-INF/views/home/db-group-schedule-detail/db-group-schedule-detail-js.jsp" %>
<%@ include file="/WEB-INF/views/home/db-schedule-detail/db-schedule-detail-js.jsp" %>

<!-- angularjs document-->
<script>
	app.controller('myHome', function ($scope, $rootScope, $http) {
		this.$onInit = function () {
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