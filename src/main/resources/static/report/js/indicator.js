app.controller('indicator', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.$parent.select_mutiple("#month", "Chọn tháng");
        $scope.$parent.select_mutiple("#service", "Tất cả");
    };

    $scope.phuluc02Excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.phuluc02Email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});

