<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type='text/ng-template' id='high-chart-paging.html'>
	<%@ include file="/WEB-INF/_components/high-chart-paging/high-chart-paging-html.jsp" %>
</script>
<script>
	app.directive('highChartPaging', function($http, $timeout, $parse) {
		return {
			restrict : 'E',
			templateUrl : 'high-chart-paging.html',
			link : function($scope, el, attrs) {
				$scope.curentPage = 1;
                var invoker = $parse(attrs.ngChange);
				
				$scope.$watch(attrs.ngModel, function(value) {
				    if (value == null) { return; }
					$scope.curentPage = value;
			    });
				
				$scope.changePageByHighChart = function(value){
                    $scope.curentPage = value;
                    invoker($scope, {$event: value} );
				}
			}
		};
	});
</script>