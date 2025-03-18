app.controller('report_pac_accept', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    
    $scope.init = function () {
    };
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.pdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.excel();
            }
        });
    };
    
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});