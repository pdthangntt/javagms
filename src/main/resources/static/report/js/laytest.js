app.controller('laytest_tt03', function ($scope) {

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
        setTimeout(function(){$( "form[name=laytest_form]" ).submit();}, 100);
    };
    
    
    $scope.getUrl = function (url) {
        return url + "?from_time=" + $scope.fromTime
                + "&to_time=" + $scope.toTime;
    };
    $scope.phuluc02Pdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.phuluc02Excel();
            }
        });
    };
    
    $scope.phuluc02Excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.phuluc02Email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    $scope.detailReportLineLaytest = function (params, type) {
        if (typeof type == 'undefined') {
            type = "";
        }
        if (typeof params.objectGroupID != 'undefined') {
            if (params.objectGroupID.indexOf(",") == -1) {
                params.objectGroupID = [params.objectGroupID];
            } else {
                params.objectGroupID = params.objectGroupID.split(",");
            }
        }

        loading.show();
        $.ajax({
            url: '/service/laytest/search.json?type=' + type,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(params),
            success: function (resp) {
                loading.hide();
                var dTable = [];
                var options = resp.data.options;
                $.each(resp.data.models, function () {
                    var item = this;
                    dTable.push([
                        item.code, item.patientName, item.yearOfBirth,
                        (typeof options['gender'][item.genderID] == 'undefined' ? '' : options['gender'][item.genderID]),
                        (typeof options['test-object-group'][item.objectGroupID] == 'undefined' ? '' : options['test-object-group'][item.objectGroupID]),
                        item.permanentAddressFull,
                        utils.timestampToStringDate(item.advisoryeTime),
                        (typeof options['test-results'][item.testResultsID] == 'undefined' ? '' : ('<span class="' + (item.testResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-results'][item.testResultsID] + '</span>')),
                        utils.timestampToStringDate(item.confirmTime),
                        (typeof options['test-result-confirm'][item.confirmResultsID] == 'undefined' ? '' : ('<span class="' + (item.confirmResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-result-confirm'][item.confirmResultsID] + '</span>')),
                        (typeof options['siteConfirms'][item.siteConfirmTest] == 'undefined' ? '' : options['siteConfirms'][item.siteConfirmTest])
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách khách hàng",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover"></table>',
                    size: 'large',
                    buttons: {
                        cancel: {
                            label: "Đóng",
                            className: 'btn-default',
                            callback: function () {
                            }
                        }
                    }
                }).find("div.modal-dialog").addClass("largeWidth");

                setTimeout(function () {
                    $("#grid-report").DataTable({
                        paging: false,
                        searching: false,
                        ordering: false,
                        info: false,
                        scrollX: true,
                        scrollCollapse: true,
                        processing: true,
                        language: {
                            emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
                        },
                        data: dTable,
                        columns: [
                            {title: "Mã KH", className: "text-center"},
                            {title: "Họ và tên", className: "text-center"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Đối tượng", className: "text-center"},
                            {title: "Địa chỉ thường trú", className: "text-center"},
                            {title: "Ngày xét nghiệm", className: "text-center"},
                            {title: "Kết quả XN sàng lọc", className: "text-center"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Kết quả XN khẳng định", className: "text-center"},
                            {title: "Nơi XN khẳng định", className: "text-center"}
                        ]
                    });
                }, 300);
            }
        });
    };
});

app.controller('laytest_book', function ($scope) {

    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.urlPdf = urlPdf;
    
    $scope.init = function () {
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };
    
    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val());
    };
    
    $scope.bookPdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.bookExcel();
            }
        });
    };

    $scope.bookExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.bookEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});
app.controller('laytest_mer', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.fromTime = search.fromTime;
        $scope.toTime = search.toTime
    };

    $scope.merExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.getUrl = function (url) {
        return url + "?from_time=" + $scope.fromTime
                + "&to_time=" + $scope.toTime
    };
    
    $scope.merEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    
    $scope.detailReportLineLaytest = function (params, type) {
        if (typeof type == 'undefined') {
            type = "";
        }
        if (typeof params.objectGroupID != 'undefined') {
            if (params.objectGroupID.indexOf(",") == -1) {
                params.objectGroupID = [params.objectGroupID];
            } else {
                params.objectGroupID = params.objectGroupID.split(",");
            }
        }
        if (typeof params.confirmResultsID != 'undefined') {
            if (params.confirmResultsID.indexOf(",") == -1) {
                params.confirmResultsID = [params.confirmResultsID];
            } else {
                params.confirmResultsID = params.confirmResultsID.split(",");
            }
        }
        if (typeof params.genderID != 'undefined') {
            if (params.genderID.indexOf(",") == -1) {
                params.genderID = [params.genderID];
            } else {
                params.genderID = params.genderID.split(",");
            }
        }

        loading.show();
        $.ajax({
            url: '/service/laytest-mer/search.json?type=' + type,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(params),
            success: function (resp) {
                loading.hide();
                var dTable = [];
                var options = resp.data.options;
                $.each(resp.data.models, function () {
                    var item = this;
                    dTable.push([
                        item.code, item.patientName, item.yearOfBirth,
                        (typeof options['gender'][item.genderID] == 'undefined' ? '' : options['gender'][item.genderID]),
                        (typeof options['test-object-group'][item.objectGroupID] == 'undefined' ? '' : options['test-object-group'][item.objectGroupID]),
                        item.permanentAddressFull,
                        utils.timestampToStringDate(item.advisoryeTime),
                        (typeof options['test-results'][item.testResultsID] == 'undefined' ? '' : ('<span class="' + (item.testResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-results'][item.testResultsID] + '</span>')),
                        utils.timestampToStringDate(item.confirmTime),
                        (typeof options['test-result-confirm'][item.confirmResultsID] == 'undefined' ? '' : ('<span class="' + (item.confirmResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-result-confirm'][item.confirmResultsID] + '</span>')),
                        (typeof options['siteConfirms'][item.siteConfirmTest] == 'undefined' ? '' : options['siteConfirms'][item.siteConfirmTest])
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách khách hàng",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover"></table>',
                    size: 'large',
                    buttons: {
                        cancel: {
                            label: "Đóng",
                            className: 'btn-default',
                            callback: function () {
                            }
                        }
                    }
                }).find("div.modal-dialog").addClass("largeWidth");

                setTimeout(function () {
                    $("#grid-report").DataTable({
                        paging: false,
                        searching: false,
                        ordering: false,
                        info: false,
                        scrollX: true,
                        scrollCollapse: true,
                        processing: true,
                        language: {
                            emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
                        },
                        data: dTable,
                        columns: [
                            {title: "Mã KH", className: "text-center"},
                            {title: "Họ và tên", className: "text-center"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Đối tượng", className: "text-center"},
                            {title: "Địa chỉ thường trú", className: "text-center"},
                            {title: "Ngày xét nghiệm", className: "text-center"},
                            {title: "Kết quả XN sàng lọc", className: "text-center"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Kết quả XN khẳng định", className: "text-center"},
                            {title: "Nơi XN khẳng định", className: "text-center"}
                        ]
                    });
                }, 300);
            }
        });
    };
});
