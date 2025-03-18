app.controller('his_hiv', function ($scope, msg, $uibModal) {
    if ($.getQueryParameters().id != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
//    $scope.arv = arv;
    
    $scope.init = function () {
        $scope.switchConfig();
        $scope.$parent.select_search($("#site"), "");
        $scope.$parent.select_search($("#status"), "");
        
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;
        
    };
});

app.controller('vnpt_config', function ($scope, msg) {

    $scope.init = function () {
         $scope.$parent.select_search($('#ID'), "");
         $scope.$parent.select_search($('#active'), "");
    };
});