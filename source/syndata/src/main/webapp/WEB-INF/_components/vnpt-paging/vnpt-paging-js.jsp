<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type='text/ng-template' id='vnpt-paging.html'>
	<%@ include file="/WEB-INF/_components/vnpt-paging/vnpt-paging-html.jsp" %>
</script>
<script>
	app.directive('vnptPaging', function ($http, $timeout, $parse) {
		return {
			restrict: 'E',
			templateUrl: 'vnpt-paging.html',
			link: function ($scope, el, attrs) {
				var invoker = $parse(attrs.ngChange);
				$scope.lstPage = [];
				$scope.curentPage = 1;
				$scope.totalPage = 0;
				$scope.totalItem = 0;
				$scope.itemsPerPage = 10;
				$scope.view = 5;

				$scope.$watch(attrs.ngModel, function (value) {
					$scope.lstPage = [];
					if (value == null) { return; }

					$scope.curentPage = value.page;
					$scope.totalPage = value.totalPage;
					$scope.totalItem = value.totalRecord;
					$scope.itemsPerPage = value.size;

					if ($scope.totalPage > 1) {
						let showItem = $scope.view;
						if ($scope.totalPage < $scope.view) { showItem = $scope.totalPage; }
						// index slot curent page
						let index = $scope.curentPage;
						if (showItem === $scope.view) {
							// tslint:disable-next-line:radix
							index = showItem % 2 === 0 ? (showItem / 2) : parseInt((showItem / 2).toString()) + 1;
						}

						let fix = $scope.curentPage < index ? (index - $scope.curentPage) : 0;
						if ($scope.curentPage > ($scope.totalPage - index) && showItem === $scope.view) { fix = ($scope.totalPage - index) - $scope.curentPage + 1; }
						for (let i = 1; i <= showItem; i++) {
							$scope.lstPage.push($scope.curentPage - index + i + fix);
						}
					}
				});

				$scope.changePageByVNPTPaging = function (value) {
					if (invoker) {
						$scope.curentPage = value;
						invoker($scope, { $event: value });
					} else {
						var url = lib.replaceUrlParam(location.href, 'page', value);
						location.href = url;
					}
				}
			}
		};
	});
</script>