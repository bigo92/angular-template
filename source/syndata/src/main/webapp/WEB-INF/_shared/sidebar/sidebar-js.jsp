<script>
    app.controller('sideBar', function ($scope, $http) {
        $scope.user = null;

        this.$onInit = function () {
            if (localStorage.getItem('User')) {
                $scope.user = JSON.parse(localStorage.getItem('User'));
            }
        }

    });
</script>