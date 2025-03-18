app.controller('htc_tt03', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    
    $scope.init = function () {
        $scope.fromTime = search.fromTime;
        $scope.toTime = search.toTime;
        $scope.$parent.select_mutiple("#service", "Tất cả");
    };
    
    $scope.setTime = function (event) {
        $scope.fromTime = $(event.currentTarget).data("start");
        $scope.toTime = $(event.currentTarget).data("end");
        setTimeout(function(){$( "form[name=htc_form]" ).submit();}, 100);
    };
    
    
    $scope.getUrl = function (url) {
        return url + "?from_time=" + $scope.fromTime
                + "&to_time=" + $scope.toTime
                + "&service=" + ($("#service").val() == null ? '' : $("#service").val())
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
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
});

app.controller('htc_tt09', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.$parent.select_mutiple("#service", "Tất cả");
    };

    $scope.getUrl = function (url) {
//        var service = $("#service option:selected").val();
        return url + "?month=" + $("select[name=month]").val()
                + "&year=" + $("select[name=year]").val()
                + "&service=" + ($("#service").val() == null ? '' : $("#service").val())
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
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
});

app.controller('htc_positive', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');

    $scope.init = function () {
        $scope.$parent.select_mutiple("#services", "Tất cả");
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };

    $scope.positivePdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.positiveExcel();
            }
        });
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&services=" + ($("#services").val() == null ? '' : $("#services").val())
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val())
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
    };

    $scope.positiveExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.positiveEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});

app.controller('htc_transfer_success', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');

    $scope.init = function () {
        $scope.$parent.select_mutiple("#services", "Tất cả");
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };

    $scope.transferSuccessPdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.transferSuccessExcel();
            }
        });
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&services=" + ($("#services").val() == null ? '' : $("#services").val())
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val())
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
    };

    $scope.transferSuccessExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.transferSuccessEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});

app.controller('htc_book', function ($scope) {

    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.urlPdf = urlPdf;

    $scope.init = function () {
        $scope.$parent.select_mutiple("#services", "Tất cả");
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&services=" + ($("#services").val() == null ? '' : $("#services").val())
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val())
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
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

app.controller('htc_mer', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.fromTime = search.fromTime;
        $scope.toTime = search.toTime;
        $scope.$parent.select_mutiple("#service", "Tất cả");
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };

    $scope.merExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.getUrl = function (url) {
        return url + "?from_time=" + $scope.fromTime
                + "&to_time=" + $scope.toTime
                + "&service=" + ($("#service").val() == null ? '' : $("#service").val())
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val());
    };
    
    $scope.merEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    
    $scope.detailReportLine = function (params, type) {
        if (typeof type == 'undefined') {
            type = "";
        }
//        if(params.flag == '3' || params.flag == '5'){
//            if (typeof params.objectGroupIDs != 'undefined') {
//                if (params.objectGroupIDs.indexOf(",") == -1) {
//                    params.objectGroupIDs = [params.objectGroupIDs];
//                } else {
//                    params.objectGroupIDs = params.objectGroupIDs.split(",");
//                }
//            }
//        }

        loading.show();
        $.ajax({
            url: '/service/htc-mer/search.json?type=' + type,
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
                        (typeof options.gender[item.genderID] == 'undefined' ? '' : options.gender[item.genderID]),
                        item.permanentAddressFull,
                        utils.timestampToStringDate(item.advisoryeTime),
                        utils.timestampToStringDate(item.preTestTime),
                        utils.timestampToStringDate(item.resultsTime),
                        (typeof options['test-results'][item.testResultsID] == 'undefined' ? '' : ('<span class="' + (item.testResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-results'][item.testResultsID] + '</span>')),
                        utils.timestampToStringDate(item.confirmTime),
                        utils.timestampToStringDate(item.resultsSiteTime),
                        utils.timestampToStringDate(item.resultsPatienTime),
                        (typeof options['test-result-confirm'][item.confirmResultsID] == 'undefined' ? '' : ('<span class="' + (item.confirmResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-result-confirm'][item.confirmResultsID] + '</span>')),
                        (typeof options['test-object-group'][item.objectGroupID] == 'undefined' ? '' : options['test-object-group'][item.objectGroupID]),
                        item.staffBeforeTestID,
                        item.staffAfterID
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách khách hàng",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover table-full"></table>',
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
                    $("#grid-report").addClass('nowrap').DataTable({
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
                            {title: "Họ và tên"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Địa chỉ thường trú"},
                            {title: "Ngày tư vấn", className: "text-center"},
                            {title: "Ngày XN sàng lọc", className: "text-center"},
                            {title: "Ngày nhận KQ sàng lọc", className: "text-center"},
                            {title: "Kết quả XN sàng lọc", className: "text-center"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Ngày CS nhận KQKĐ", className: "text-center"},
                            {title: "Ngày KH nhận KQKĐ", className: "text-center"},
                            {title: "Kết quả XN khẳng định", className: "text-center"},
                            {title: "Nhóm đối tượng"},
                            {title: "Tư vấn viên trước XN"},
                            {title: "Tư vấn viên sau XN"}
                        ]
                    });
                }, 300);
            }
        });
    };
});

app.controller('test_book', function ($scope) {

    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.urlPdf = urlPdf;

    $scope.init = function () {
        $scope.$parent.select_mutiple("#services", "Tất cả");
        $scope.$parent.select_mutiple("#objects", "Tất cả");
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&services=" + ($("#services").val() == null ? '' : $("#services").val())
                + "&objects=" + ($("#objects").val() == null ? '' : $("#objects").val());
                + "&code=" + ($("#code").val() == null ? '' : $("#code").val());
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