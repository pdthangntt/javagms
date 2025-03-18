app.controller('report_pac_new', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.urlExcelAIDS = urlExcelAIDS;
    
    $scope.init = function () {
        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
//        $scope.initProvince(null, "#permanent_district_id", "#permanent_ward_id"); // Có quận huyện/phường xã. Dùng cho tìm kiếm tab Phát hiện trong tỉnh và Tỉnh khác phát hiện
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
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
    $scope.excelAIDS = function () {
        window.location.href = $scope.getUrl($scope.urlExcelAIDS);
    };
    
    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});


app.controller('report_opc_visit_index', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    
    $scope.init = function () {
        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
//        $scope.initProvince(null, "#permanent_district_id", "#permanent_ward_id"); // Có quận huyện/phường xã. Dùng cho tìm kiếm tab Phát hiện trong tỉnh và Tỉnh khác phát hiện
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
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

