app.controller('report_tt03_quarter', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initProvince(null, "#district", null);
        $scope.select_mutiple("#service", "Tất cả");

        if ($("#district").length > 0) {
            let dID = $("#district").val();
            if (dID === '') {
                $("#sites option").removeAttr("disabled");
            } else {
                $("#sites option").attr({disabled: "disabled"});
                $("#sites option[district-id=" + dID + "]").removeAttr("disabled");
            }
            $("#district").change(() => {
                $("#sites option").removeAttr("selected");
                $("#sites").val("").trigger("change");
                let dID = $("#district").val();
                if (dID === '') {
                    $("#sites option").removeAttr("disabled");
                } else {
                    $("#sites option").attr({disabled: "disabled"});
                    $("#sites option[district-id=" + dID + "]").removeAttr("disabled");
                }
                $scope.select_mutiple("#sites", "Tất cả");
            });
        }
        $scope.select_mutiple("#sites", "Tất cả");
    };


    $scope.getUrl = function (url) {
        return url + "?quarter=" + $("#quarter").val()
                + "&year=" + $("#year").val()
                + "&district=" + $("#district").val()
                + "&services=" + ($("#service").val() === null ? '' : $("#service").val())
                + "&sites=" + ($("#sites").val() === null ? '' : $("#sites").val())
    };

    $scope.pdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.phuluc02Excel();
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

app.controller('report_tt09', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initProvince(null, "#district", null);
        $scope.select_mutiple("#service", "Tất cả");

        if ($("#district").length > 0) {
            let dID = $("#district").val();
            if (dID === '') {
                $("#sites option").removeAttr("disabled");
            } else {
                $("#sites option").attr({disabled: "disabled"});
                $("#sites option[district-id=" + dID + "]").removeAttr("disabled");
            }
            $scope.select_mutiple("#sites", "Tất cả");

            $("#district").change(() => {
                $("#sites option").removeAttr("selected");
                $("#sites").val("").trigger("change");
                let dID = $("#district").val();
                if (dID === '') {
                    $("#sites option").removeAttr("disabled");
                } else {
                    $("#sites option").attr({disabled: "disabled"});
                    $("#sites option[district-id=" + dID + "]").removeAttr("disabled");
                }
                $scope.select_mutiple("#sites", "Tất cả");
            });
        }
        $scope.select_mutiple("#sites", "Tất cả");
    };


    $scope.getUrl = function (url) {
        return url + "?month=" + $("#month").val()
                + "&year=" + $("#year").val()
                + "&district=" + $("#district").val()
                + "&services=" + ($("#service").val() === null ? '' : $("#service").val())
                + "&sites=" + ($("#sites").val() === null ? '' : $("#sites").val())
    };

    $scope.pdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.phuluc02Excel();
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


app.controller('qpm_report_index', function ($scope) {

    $scope.init = function () {
        $scope.fromTime = search.fromTime;
        $scope.toTime = search.toTime;
    };




});
