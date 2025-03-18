app.controller('pqm_shi_art', function ($scope, $uibModal, msg) {
    
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    if ($.getQueryParameters().month != null ||
            $.getQueryParameters().year != null 
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.mon = mon;
    
    $scope.init = function () {
        console.log($("#month").val() + " xxx " + $scope.mon);
        $("#month").val($scope.mon).change();
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        
        
        

//        $scope.select_search("#objectGroupID", "Tất cả");
//        $scope.select_search("#statusOfTreatmentID", "Tất cả");
//        $scope.select_mutiple("#sites", "Tất cả");
//        $scope.treatmentTimeFrom = $.getQueryParameters().treatmentTimeFrom;
//        $scope.treatmentTimeTo = $.getQueryParameters().treatmentTimeTo;
    };
});


app.controller('pqm_shi_mmd', function ($scope, $uibModal, msg) {
    
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

//        $scope.select_search("#objectGroupID", "Tất cả");
//        $scope.select_search("#statusOfTreatmentID", "Tất cả");
//        $scope.select_mutiple("#sites", "Tất cả");
//        $scope.treatmentTimeFrom = $.getQueryParameters().treatmentTimeFrom;
//        $scope.treatmentTimeTo = $.getQueryParameters().treatmentTimeTo;
    };
});

