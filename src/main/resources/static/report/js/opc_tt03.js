app.controller('opc_tt03', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.fromTime = search.fromTime;
        $scope.toTime = search.toTime;
    };

    $scope.setTime = function (event) {
        $scope.fromTime = $(event.currentTarget).data("start");
        $scope.toTime = $(event.currentTarget).data("end");
        setTimeout(function () {
            $("form[name=opc_form]").submit();
        }, 100);
    };


    $scope.getUrl = function (url) {
        return url + "?from_time=" + $scope.fromTime
                + "&to_time=" + $scope.toTime
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
