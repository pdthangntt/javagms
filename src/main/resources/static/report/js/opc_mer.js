app.controller('opc_mer', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.form = form;

    $scope.init = function () {
        $scope.quarter = form.quarter;
        $scope.year = form.year;
        
    };

    $scope.setTime = function (event) {
        $scope.fromTime = $(event.currentTarget).data("start");
        $scope.toTime = $(event.currentTarget).data("end");
        setTimeout(function () {
            $("form[name=mer_form]").submit();
        }, 100);
    };

    $scope.getUrl = function (url) {
        return url + "?quarter=" + $scope.quarter
                + "&year=" + $scope.year 
                + "&site_id=" + $.getQueryParameters().site_id 
                + "&tab=" + ($("#tab").val() == null ? '' : $("#tab").val());
    };

    $scope.pdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                window.location.href = $scope.getUrl($scope.urlExcel);
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
