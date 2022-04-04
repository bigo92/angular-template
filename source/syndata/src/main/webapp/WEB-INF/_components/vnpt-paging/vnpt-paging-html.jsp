<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<ul ng-if="lstPage.length > 0"
	class="pagination pagination-separated pagination-lg mt-3 mb-3 align-self-center">
	
	<li class="page-item" ng-class="curentPage === 1?'disabled': null">
		<a href="javascript:;" ng-click="changePageByVNPTPaging(curentPage-1)" class="page-link" ng-if="curentPage > 1">Trước</a>
		<a href="javascript:;" class="page-link" ng-if="curentPage === 1">Trước</a>
	</li>
	
	<li class="page-item" ng-class="curentPage === x?'active': null" ng-repeat="x in lstPage">
		<a href="javascript:;" ng-click="changePageByVNPTPaging(x)" class="page-link">{{x}}</a>
	</li>
	
	<li class="page-item" ng-class="curentPage === totalPage?'disabled': null">
		<a href="javascript:;" ng-click="changePageByVNPTPaging(curentPage+1)" ng-if="curentPage < totalPage" class="page-link">Sau</a>
		<a href="javascript:;" ng-if="curentPage === totalPage" class="page-link">Sau</a>
	</li>
</ul>