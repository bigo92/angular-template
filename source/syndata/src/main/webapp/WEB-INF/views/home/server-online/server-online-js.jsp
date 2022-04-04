<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
<script>
	app.controller('serverOnline', function ($scope, $http) {
		$scope.setting = {
			general: 10,
			totalTask: 11,
			listGroup: 12,
			listService: 13,
			detailService: 14
		}
		$scope.data = [];
		$scope.isLoadone = false;

		this.$onInit = function () {
			//load setting
			if (localStorage.getItem("setting")) {
				$scope.setting = JSON.parse(localStorage.getItem("setting"));
			} else {
				localStorage.setItem("setting", JSON.stringify($scope.setting));
			}

			$scope.getData();
			$scope.refesh($scope.setting.general);
		}

		$scope.refesh = function (time) {
			setInterval(function () {
				$scope.getData();
			}, time * 1000);
		}

		$scope.getData = function () {
			var url = $('#contextPath').val() + '/api/dashboard/server-online';
			$scope.datas = [];
			$scope.isLoadone = false;
			$http({
				url: url,
				method: 'get'
			}).then(function successCallback(response) {
				$scope.data = response.data.ITEMS;
				$scope.isLoadone = $scope.data.length > 0 ? true : null;
			});
		}
	});
</script>