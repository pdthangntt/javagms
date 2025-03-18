app.controller('opc_treatment_fluctuations', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {

        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
        $scope.treatmentStageTimeFrom = $.getQueryParameters().treatment_stage_time_from;
        $scope.treatmentStageTimeTo = $.getQueryParameters().treatment_stage_time_to;
        $scope.keywords = $.getQueryParameters().keywords;
        $scope.select_mutiple("#status_of_treatment_id", "Tất cả");
        $scope.select_mutiple("#treatment_stage_id", "Tất cả");
//        $scope.select_search("#treatment_stage_id", "Tất cả");
        $scope.select_search("#permanent_province_id", "Tất cả");
        $scope.select_search("#permanent_district_id", "Tất cả");
        $scope.select_search("#site_id", "Tất cả");
//        $("#permanent_district_id option:first").remove();
//        $("#permanent_district_id").prepend("<option value=''>Tất cả</option>");
        $scope.initInput();
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
    $scope.initInput = function () {
        if (f.search.statusOfTreatmentID !== null && f.search.treatmentStageID !== '') {
            $("#status_of_treatment_id").val(f.search.statusOfTreatmentID.split(",")).change();
        }
        if (f.search.treatmentStageID !== null && f.search.treatmentStageID !== '') {
            $("#treatment_stage_id").val(f.search.treatmentStageID.split(",")).change();
        }
    };
});
app.controller('opc_patient', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
        $scope.registrationTimeFrom = $.getQueryParameters().registration_time_from;
        $scope.registrationTimeTo = $.getQueryParameters().registration_time_to;
        $scope.treatmentTimeFrom = $.getQueryParameters().treatment_time_from;
        $scope.treatmentTimeTo = $.getQueryParameters().treatment_time_to;
        $scope.inhFrom = $.getQueryParameters().inhFrom;
        $scope.inhTo = $.getQueryParameters().inhTo;
        $scope.cotriFrom = $.getQueryParameters().cotriFrom;
        $scope.cotriTo = $.getQueryParameters().cotriTo;
        $scope.laoFrom = $.getQueryParameters().laoFrom;
        $scope.laoTo = $.getQueryParameters().laoTo;
        $scope.select_search("#treatment_regimen_id", "Tất cả");
        $scope.select_search("#status_of_treatment_id", "Tất cả");
        $scope.select_search("#permanent_district_id", "Tất cả");
        $scope.select_search("#permanent_province_id", "Tất cả");
        $scope.select_search("#site_id", "Tất cả");
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
app.controller('regimen_report', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {
        $scope.$parent.select_mutiple("#sites", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + "?month=" + $("select[name=month]").val()
                + "&year=" + $("select[name=year]").val()
                + "&sites=" + ($("#sites").val() == null ? '' : $("#sites").val());
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
app.controller('qpm_indicators_report', function ($scope) {
    $scope.urlPdf = "urlPdf";
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = "urlEmail";
    $scope.init = function () {
        $scope.$parent.select_mutiple("#sites", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + "?month=" + $("select[name=month]").val()
                + "&year=" + $("select[name=year]").val()
                + "&sites=" + ($("#sites").val() == null ? '' : $("#sites").val());
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
app.controller('qpm__report', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlExcel = urlExcel;
    $scope.urlExcelData = urlExcelData;
    $scope.urlPqmApiHub = urlPqmApiHub;
    $scope.urlMonth = urlMonth;
    $scope.urlSend = urlSend;
    $scope.urlSynthetic = urlSynthetic;
    $scope.urlSynthetics = urlSynthetics;
    $scope.urlSyntheticsALL = urlSyntheticsALL;
    $scope.hub = hub;
    $scope.mon = mon;
    $scope.mapSites = mapSites;

    $scope.isSend = isSend;
    $scope.isUpdate = isUpdate;
    $scope.urlEmail = "urlEmail";
    $scope.false_key = false_key;


    if ($scope.false_key) {
        let text = "<h4>Đã xảy ra lỗi khi gửi dữ liệu lên Provincial Dashboards:</h4></br>";
        for (var item in $scope.false_key) {
            text = text + $scope.false_key[item] + "</br>"
        }

        bootbox.alert(text);
    }

    $scope.init = function () {
        $scope.$parent.select_search("#sites", "Tất cả");
        $scope.$parent.select_mutiple("#siteSend", "Tất cả");
        $scope.$parent.select_search("#year", "Tất cả");

        $("#service").val($.getQueryParameters().service);


//        $(".e").each(function () {
//            var elm = this;
////                console.log(elm);
//            -$(elm).find("option").each(function () {
//                var input = this;
//                $(input).remove();
//            });
//        });

//        $("#sites").append(new Option("Chọn cơ sở", ""));
        if ($("#service").val() == '1') {
            for (const property in mapSites['100']) {
                $("#sites").append(new Option(mapSites['100'][property], property));
            }
        }
        if ($("#service").val() == '2') {
            for (const property in mapSites['500']) {
                $("#sites").append(new Option(mapSites['500'][property], property));
            }
        }
        if ($("#service").val() == '3') {
            for (const property in mapSites['103']) {
                $("#sites").append(new Option(mapSites['103'][property], property));
            }
        }
        if ($("#service").val() == '4') {
            for (const property in mapSites['prep']) {
                $("#sites").append(new Option(mapSites['prep'][property], property));
            }
        }


        $("#sites").val($.getQueryParameters().sites);


        $("#service").on('change', function () {
            $(".e").each(function () {
                var elm = this;
//                console.log(elm);
                -$(elm).find("option").each(function () {
                    var input = this;
                    $(input).remove();
                });
            });

            $("#sites").append(new Option("Chọn cơ sở", ""));
            if ($("#service").val() == '1') {
                for (const property in mapSites['100']) {
                    $("#sites").append(new Option(mapSites['100'][property], property));
                }
            }
            if ($("#service").val() == '2') {
                for (const property in mapSites['500']) {
                    $("#sites").append(new Option(mapSites['500'][property], property));
                }
            }
            if ($("#service").val() == '3') {
                for (const property in mapSites['103']) {
                    $("#sites").append(new Option(mapSites['103'][property], property));
                }
            }
            if ($("#service").val() == '4') {
                for (const property in mapSites['prep']) {
                    $("#sites").append(new Option(mapSites['prep'][property], property));
                }
            }

        });


    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        bootbox.confirm({
            message: "Bạn có chắc chắn tổng hợp cho tháng " + $scope.mon + " năm " + $("#year").val() + " không?",
            buttons: {
                confirm: {
                    label: '<i class="fa fa-check" ></i> Đồng ý',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<i class="fa fa-remove" ></i> Không',
                    className: 'btn-danger'
                }

            },
            callback: function (result) {
                if (result) {
                    window.location.href = $scope.getUrl($scope.urlSynthetics);
                } else {
                }
            }
        });

    };
    $scope.syntheticAll = function () {
        bootbox.confirm({
            message: "Bạn có chắc chắn tổng hợp lại toàn bộ dữ liệu không?",
            buttons: {
                confirm: {
                    label: '<i class="fa fa-check" ></i> Đồng ý',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<i class="fa fa-remove" ></i> Không',
                    className: 'btn-danger'
                }

            },
            callback: function (result) {
                if (result) {
                    window.location.href = $scope.getUrl($scope.urlSyntheticsALL);
                } else {
                }
            }
        });

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
    $scope.excelHub = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData);
    };
    $scope.apiHub = function () {
        window.location.href = $scope.getUrl($scope.urlPqmApiHub);
    };
    $scope.send = function () {
        if ($scope.isSend) {
            msg.warning("Báo cáo đã được tỉnh tổng hợp, không thể gửi lại");
        } else {
            window.location.href = $scope.getUrl($scope.urlSend);
        }
    };
    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    $scope.actionUpdate = function (oid, name, indicator, month, year) {

        if ($scope.hub == '0') {
            if ($scope.isSend) {
                msg.warning("Báo cáo đã được tỉnh tổng hợp, không thể chỉnh sửa");
            } else {
                if ($scope.isUpdate) {
                    loading.show();
                    $.ajax({
                        url: urlGet + '?oid=' + oid +
                                '&indicator=' + indicator +
                                '&month=' + month +
                                '&year=' + year,
                        data: {oid: oid},
                        method: 'GET',
                        success: function (resp) {
                            loading.hide();
                            if (resp.success) {
                                $uibModal.open({
                                    animation: true,
                                    backdrop: 'static',
                                    templateUrl: 'pqm-update',
                                    controller: 'pqm_controllerr',
                                    size: 'lg',
                                    resolve: {
                                        params: function () {
                                            return {
                                                oid: oid,
                                                name: name,
                                                month: month,
                                                year: year,
                                                indicator: indicator,
                                                items: resp.data.items,
                                                formUpdate: resp.data.formUpdate,
                                                uiModals: $uibModal,
                                                parent: $scope.$parent,
                                                dialogReport: $scope.dialogReport,
                                                labels: resp.data.labels
                                            };
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        } else if ($scope.hub == '') {
            msg.warning("Không được phép sửa do cơ sở chưa cấu hình HUB. Vui lòng liên hệ tuyến tỉnh để cấu hình!");
        } else if ($scope.hub == '1') {
            msg.warning("Không được phép sửa chỉ số báo cáo");
        }


    };

    $scope.actionSynthetic = function (oid, name, indicator, month, year) {
        window.location.href = $scope.getUrl($scope.urlSynthetic);
    };
});


app.controller('qpm_report_plan_arv', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcelData = urlExcelData;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";

    $scope.false_key = false_key;


    if ($scope.false_key) {
        let text = "<h4>Đã xảy ra lỗi khi gửi dữ liệu lên Provincial Dashboards:</h4></br>";
        for (var item in $scope.false_key) {
            text = text + $scope.false_key[item] + "</br>"
        }

        bootbox.alert(text);
    }

    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        bootbox.confirm({
            message: "Bạn có chắc chắn tổng hợp cho tháng " + $scope.mon + " năm " + $("#year").val() + " không?",
            buttons: {
                confirm: {
                    label: '<i class="fa fa-check" ></i> Đồng ý',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<i class="fa fa-remove" ></i> Không',
                    className: 'btn-danger'
                }

            },
            callback: function (result) {
                if (result) {
                    window.location.href = $scope.getUrl($scope.urlSynthetic);
                } else {
                }
            }
        });

    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData);
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

});

app.controller('qpm_report_plan_arv_view', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";




    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        window.location.href = $scope.getUrl($scope.urlSynthetic);
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData);
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

});

app.controller('qpm_drug_new', function ($scope, $uibModal, msg) {
    $scope.urlExcel = urlExcel;
    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

});

app.controller('qpm_site', function ($scope, $uibModal, msg) {
    $scope.urlHub = urlHub;
    $scope.items = items;
    $scope.formUpdate = formUpdate;

    $scope.init = function () {
        for (const property in $scope.items) {
            let keyIDhub = "#" + $scope.items[property].id + "hub";
            let valueHub = $scope.items[property].hub;
            $(keyIDhub).val(valueHub).change();

            let keyhuHSiteCode = "#" + $scope.items[property].id + "hubSiteCode";
            let valueHubSiteCode = $scope.items[property].hubSiteCode;
            $(keyhuHSiteCode).val(valueHubSiteCode).change();

            let keyPqmSiteCode = "#" + $scope.items[property].id + "pqmSiteCode";
            let valuePqmSiteCode = $scope.items[property].pqmSiteCode;
            $(keyPqmSiteCode).val(valuePqmSiteCode).change();

            $("#" + $scope.items[property].id + "elogSiteCode").val($scope.items[property].elogSiteCode).change();
            $("#" + $scope.items[property].id + "arvSiteCode").val($scope.items[property].arvSiteCode).change();
            $("#" + $scope.items[property].id + "prepSiteCode").val($scope.items[property].prepSiteCode).change();
            $("#" + $scope.items[property].id + "hmedSiteCode").val($scope.items[property].hmedSiteCode).change();
        }

    };
    $scope.ok = function () {
        var arr = [];
        $(".x").each(function () {
            var elm = this;
            var item = {};

            -$(elm).find("input, select").each(function () {
                var input = this;
                console.log(input);
                var val = $(input).val();
                var name = $(input).attr("name");
                if (val != '' && name != null) {
                    item[name] = val;
                }
            });
            arr.push(item);
        });
        console.log(arr);
        $scope.formUpdate.items = arr;

        $.ajax({
            url: urlHub,
            data: JSON.stringify($scope.formUpdate),
            contentType: "application/json",
            dataType: 'json',
            method: 'POST',
            success: function (resp) {
                if (resp.success) {
                    msg.success("Đã nhập chỉ số báo cáo thành công. Vui lòng chờ trong giây lát.");
                    setTimeout(function () {
                        location.reload();
                    }, 4000);
                } else {
                    for (const element of resp.data.siteIDs) {
                        let keyy = "#y" + element;
                        let keyx = "#x" + element;
                        $(keyy).text("");
                        $(keyx).text("");
                    }
                    msg.danger("Đã xảy ra lỗi. Vui lòng kiểm tra lại!");
                    for (const element of resp.data.doulicateID) {
                        let keyc = "#x" + element;
                        $(keyc).text("Mã bị trùng, kiểm tra lại.");
                    }
                    for (const element of resp.data.doulicateHubID) {
                        let keyy = "#y" + element;
                        $(keyy).text("Mã bị trùng, kiểm tra lại.");
                    }
                }
            },
            error: function (request, status, error) {
                console.log(error);
            }
        });
    };
});

app.controller('qpm_proportion', function ($scope, $uibModal, msg) {
    $scope.urlUpdate = urlUpdate;
    $scope.items = items;
    $scope.formUpdate = formUpdate;

    $scope.init = function () {
        for (const property in $scope.items) {
            let keyID = "#" + $scope.items[property].id + "value";
            let value = $scope.items[property].value;
            $(keyID).val(value).change();
        }
    };
    $scope.ok = function () {
        var arr = [];
        $(".x").each(function () {
            var elm = this;
            var item = {};
            -$(elm).find("input").each(function () {
                var input = this;
                var val = $(input).val();
                var name = $(input).attr("name");
                if (val != '' && name != null) {
                    item[name] = val;
                }
            });
            arr.push(item);
        });
        $scope.formUpdate.items = arr;
        debugger;
        $.ajax({
            url: "/service/pqm-proportion/update.json",
            data: JSON.stringify($scope.formUpdate),
            contentType: "application/json",
            dataType: 'json',
            method: 'POST',
            success: function (resp) {
                if (resp.success) {
                    msg.success("Cập nhật thành. Vui lòng chờ trong giây lát.");
                    setTimeout(function () {
                        location.reload();
                    }, 4000);
                } else {
                    msg.danger("Đã xảy ra lỗi. Vui lòng kiểm tra lại!");
                }
            },
            error: function (request, status, error) {
                console.log(error);
            }
        });
    };
});

app.controller('pqm_controllerr', function ($scope, $uibModalInstance, msg, params) {
    $scope.options = params.options;
    $scope.uiModals = params.uiModals;
    $scope.dialogReport = params.dialogReport;
    $scope.parent = params.parent;
    $scope.name = params.name;
    $scope.month = params.month;
    $scope.year = params.year;
    $scope.indicator = params.indicator;
    $scope.items = params.items;
    $scope.formUpdate = params.formUpdate;
    $scope.labels = params.labels;
    $scope.init = function () {
        for (const property in $scope.items) {
            let keyID = $scope.items[property].key;
            let keyIDx = "#" + $scope.items[property].key;
            let quantity = $scope.items[property].quantity;
            $(keyIDx).val(quantity);
        }
    };

    $scope.ok = function () {
        var validateInputs = 0;
        var validateError = "";
        for (const property in $scope.items) {
            let keyID = $scope.items[property].key;
            let keyIDx = "#" + $scope.items[property].key;
            let reg = /^\d+$/;
            if (!reg.test($(keyIDx).val())) {
                validateInputs = validateInputs + 1;
                validateError = validateError + $scope.labels[keyID] + ', \n';
            }
        }
        if (validateInputs !== 0) {
            msg.warning("Dữ liệu chỉ được nhập số nguyên dương. \n" + validateError);
        } else {
            var arr = [];
            console.log($(".input").length);
            $(".input").each(function () {
                var elm = this;
//                console.log(elm);
                var item = {};
                item["indicator"] = $scope.indicator;

                -$(elm).find("input").each(function () {

                    var input = this;
                    console.log(input);
                    var val = $(input).val();
                    var name = $(input).attr("name");
                    if (val != '' && name != null) {
                        item[name] = val;
                    }
                });
                arr.push(item);
            });
            $scope.formUpdate.items = arr;

            $.ajax({
                url: "/service/pqm-report/update.json",
                data: JSON.stringify($scope.formUpdate),
                contentType: "application/json",
                dataType: 'json',
                method: 'POST',
                success: function (resp) {
                    if (resp.success) {
                        msg.success("Đã nhập chỉ số báo cáo thành công. Vui lòng chờ trong giây lát.");
                        setTimeout(function () {
                            location.reload();
                        }, 4000);
                    } else {
                        msg.danger("Cơ sở đã được tỉnh tổng hợp, không thể nhập.");
                    }
                },
                error: function (request, status, error) {
                    console.log(error);
                }
            });



//            window.location.href = "/report/pqm-report/index.html";
        }
    };


    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
app.controller('htc_confirm_qpm_indicators_report', function ($scope) {
    $scope.urlPdf = "urlPdf";
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = "urlEmail";
    $scope.fromz = from;
    $scope.toz = to;
    $scope.init = function () {


        $scope.$parent.select_mutiple("#sites", "Tất cả");
        $scope.from = $scope.fromz;
        $scope.to = $scope.toz;
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
app.controller('htc_confirm_early_report', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.fromz = from;
    $scope.toz = to;
    $scope.init = function () {
        $scope.$parent.select_mutiple("#sites", "Tất cả");
        $scope.from = $scope.fromz;
        $scope.to = $scope.toz;
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
app.controller('viralload_site_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {
        $scope.initInput();
    };
    $scope.initInput = function () {

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
//        $scope.select_search("#status_treatment", "Tất cả");
//        $scope.select_search("#status_resident", "Tất cả");
//        $scope.fromTime = $.getQueryParameters().from_time;
//        $scope.toTime = $.getQueryParameters().to_time;

//        $scope.select_search("#month", "");
//        $scope.select_search("#year", "");

        if (formSearch.month !== '') {
            $("#month").val(formSearch.month).change();
        }
        if (formSearch.year !== '') {
            $("#year").val(formSearch.year).change();
        }



    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#searchDetect");
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
    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});
app.controller('opc_mer_six-month', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {
        $scope.initInput();
        if (siteOPC != null) {
            $("#site_id").val(siteOPC).change();
        }

    };
    $scope.initInput = function () {

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
//        $scope.select_search("#status_treatment", "Tất cả");
//        $scope.select_search("#status_resident", "Tất cả");
//        $scope.fromTime = $.getQueryParameters().from_time;
//        $scope.toTime = $.getQueryParameters().to_time;

//        $scope.select_search("#month", "");
//        $scope.select_search("#year", "");

        if (formSearch.month !== '') {
            $("#month").val(formSearch.month).change();
        }
        if (formSearch.year !== '') {
            $("#year").val(formSearch.year).change();
        }



    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#searchDetect");
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
    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});
app.controller('thuantap_report', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.init = function () {
        $scope.$parent.select_search("#sites", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + "?month=" + $("select[name=month]").val()
                + "&year=" + $("select[name=year]").val()
                + "&sites=" + ($("#sites").val() == null ? '' : $("#sites").val());
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
app.controller('opc_arv_book', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.treatmentTimeFrom = typeof ($.getQueryParameters().treatment_time_from) == 'undefined' ? f.from : $.getQueryParameters().treatment_time_from;
    $scope.treatmentTimeTo = typeof ($.getQueryParameters().treatment_time_to) == 'undefined' ? f.to : $.getQueryParameters().treatment_time_to;
    $scope.getUrl = function (url) {
        return url + "?word=" + f.word
                + "&treatment_time_from=" + $scope.treatmentTimeFrom
                + "&treatment_time_to=" + $scope.treatmentTimeTo;
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
app.controller('opc_pre_arv', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.word = $.getQueryParameters().word;
    $scope.year = $.getQueryParameters().year;
    $scope.getUrl = function (url) {
        return url + "?word=" + f.word
                + "&year=" + f.year;
    };
    $scope.init = function () {
        $("td").css("min-height", "57px");
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
app.controller('opc_book_viral', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.getUrl = function (url) {
        return url + "?start=" + f.startDate
                + "&end=" + f.endDate
                + "&keywords=" + f.keywords;
    };
    $scope.init = function () {
        $scope.startDate = f.startDate;
        $scope.endDate = f.endDate;
        $scope.keywords = f.keywords;
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



app.controller('qpm_report_estimate_arv', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";
    $scope.urlExcelData = urlExcelData;


    $scope.false_key = false_key;


    if ($scope.false_key) {
        let text = "<h4>Đã xảy ra lỗi khi gửi dữ liệu lên Provincial Dashboards:</h4></br>";
        for (var item in $scope.false_key) {
            text = text + $scope.false_key[item] + "</br>"
        }

        bootbox.alert(text);
    }


    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        bootbox.confirm({
            message: "Bạn có chắc chắn tổng hợp cho quý " + $scope.mon + " năm " + $("#year").val() + " không?",
            buttons: {
                confirm: {
                    label: '<i class="fa fa-check" ></i> Đồng ý',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<i class="fa fa-remove" ></i> Không',
                    className: 'btn-danger'
                }

            },
            callback: function (result) {
                if (result) {
                    window.location.href = $scope.urlSynthetic;
                } else {
                }
            }
        });

    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
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

});

app.controller('qpm_report_drug_new', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";
    $scope.urlExcelData = urlExcelData;


    $scope.false_key = false_key;


    $("#year").change(function () {
        window.location.href = $scope.urlMonth + "?year=" + $("#year").val() + '&month=' + $scope.mon + '&sites=' + $("#sites").val();
    });
    $("#sites").change(function () {
        window.location.href = $scope.urlMonth + "?year=" + $("#year").val() + '&month=' + $scope.mon + '&sites=' + $("#sites").val();
    });


    if ($scope.false_key) {
        let text = "<h4>Đã xảy ra lỗi khi gửi dữ liệu lên Provincial Dashboards:</h4></br>";
        for (var item in $scope.false_key) {
            text = text + $scope.false_key[item] + "</br>"
        }

        bootbox.alert(text);
    }


    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
        $scope.$parent.select_search("#sites", "Tất cả");

        $scope.synthetic = function (quarter) {
            bootbox.confirm({
                message: "Bạn có chắc chắn tổng hợp cho quý " + quarter + " không?",
                buttons: {
                    confirm: {
                        label: '<i class="fa fa-check" ></i> Đồng ý',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: '<i class="fa fa-remove" ></i> Không',
                        className: 'btn-danger'
                    }

                },
                callback: function (result) {
                    if (result) {
                        window.location.href = $scope.urlSynthetic;
                    } else {
                    }
                }
            });
        };



    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };


});

app.controller('qpm_report_estimate_arv_view', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";





    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        window.location.href = $scope.urlSynthetic;
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
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

});


app.controller('qpm_shi_report', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlExcelData = urlExcelData;
    $scope.urlEmail = "urlEmail";

    $scope.false_key = false_key;


    if ($scope.false_key) {
        let text = "<h4>Đã xảy ra lỗi khi gửi dữ liệu lên Provincial Dashboards:</h4></br>";
        for (var item in $scope.false_key) {
            text = text + $scope.false_key[item] + "</br>"
        }

        bootbox.alert(text);
    }


    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        bootbox.confirm({
            message: "Bạn có chắc chắn tổng hợp cho tháng " + $scope.mon + " năm " + $("#year").val() + " không?",
            buttons: {
                confirm: {
                    label: '<i class="fa fa-check" ></i> Đồng ý',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<i class="fa fa-remove" ></i> Không',
                    className: 'btn-danger'
                }

            },
            callback: function (result) {
                if (result) {
                    window.location.href = $scope.urlSynthetic;
                } else {
                }
            }
        });

    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
    };
    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
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

});

app.controller('qpm_shi_report_view', function ($scope, $uibModal, msg) {
    $scope.urlPdf = "urlPdf";
    $scope.urlSynthetic = urlSynthetic;
    $scope.mon = mon;
    $scope.urlExcel = urlExcel;
    $scope.urlMonth = urlMonth;
    $scope.urlEmail = "urlEmail";


    $scope.init = function () {
        $scope.$parent.select_search("#year", "Tất cả");
    };
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.month = function (month) {
        window.location.href = $scope.getUrl($scope.urlMonth).replace("&month=" + $scope.mon, "") + '&month=' + month;
    };
    $scope.synthetic = function () {
        window.location.href = $scope.urlSynthetic;
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
    };
    $scope.excel_data = function () {
        window.location.href = $scope.getUrl($scope.urlExcelData).replace("&month=" + $scope.mon, "") + '&month=' + $scope.mon;
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

});
app.controller('pqm_exprotssss', function ($scope, $uibModal, msg) {


    $scope.init = function () {
        $scope.$parent.select_search("#month1a", "");
        $scope.$parent.select_search("#month2a", "");
        $scope.$parent.select_search("#month3a", "");
        $scope.$parent.select_search("#month4a", "");
        $scope.$parent.select_search("#month5a", "");
        $scope.$parent.select_search("#month6a", "");
        $scope.$parent.select_search("#year1a", "");
        $scope.$parent.select_search("#year2a", "");
        $scope.$parent.select_search("#year3a", "");
        $scope.$parent.select_search("#year4a", "");
        $scope.$parent.select_search("#year5a", "");
        $scope.$parent.select_search("#year6a", "");
        $scope.$parent.select_search("#month1b", "");
        $scope.$parent.select_search("#month2b", "");
        $scope.$parent.select_search("#month3b", "");
        $scope.$parent.select_search("#month4b", "");
        $scope.$parent.select_search("#month5b", "");
        $scope.$parent.select_search("#month6b", "");
        $scope.$parent.select_search("#year1b", "");
        $scope.$parent.select_search("#year2b", "");
        $scope.$parent.select_search("#year3b", "");
        $scope.$parent.select_search("#year4b", "");
        $scope.$parent.select_search("#year5b", "");
        $scope.$parent.select_search("#year6b", "");
    };
    $scope.excel1 = function (url) {
        if (!checkDate($("#month1a").val(), $("#year1a").val(), $("#month1b").val(), $("#year1b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }

        window.location.href = url + getParam($("#month1a").val(), $("#year1a").val(), $("#month1b").val(), $("#year1b").val());
    };
    $scope.excel2 = function (url) {
        if (!checkDate($("#month2a").val(), $("#year2a").val(), $("#month2b").val(), $("#year2b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }
        window.location.href = url + getParam($("#month2a").val(), $("#year2a").val(), $("#month2b").val(), $("#year2b").val());
    };
    $scope.excel3 = function (url) {
        if (!checkDate($("#month3a").val(), $("#year3a").val(), $("#month3b").val(), $("#year3b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }
        window.location.href = url + getParam($("#month3a").val(), $("#year3a").val(), $("#month3b").val(), $("#year3b").val());
    };
    $scope.excel4 = function (url) {
        if (!checkDate($("#month4a").val(), $("#year4a").val(), $("#month4b").val(), $("#year4b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }
        window.location.href = url + getParam($("#month4a").val(), $("#year4a").val(), $("#month4b").val(), $("#year4b").val());
    };
    $scope.excel5 = function (url) {
        if (!checkDate($("#month5a").val(), $("#year5a").val(), $("#month5b").val(), $("#year5b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }
        window.location.href = url + getParam($("#month5a").val(), $("#year5a").val(), $("#month5b").val(), $("#year5b").val());
    };
    $scope.excel6 = function (url) {
        if (!checkDate($("#month6a").val(), $("#year6a").val(), $("#month6b").val(), $("#year6b").val())) {
            return bootbox.alert('Thời gian từ không được lớn hơn Thời gian đến. Vui lòng kiểm tra lại!');
        }
        window.location.href = url + getParam($("#month6a").val(), $("#year6a").val(), $("#month6b").val(), $("#year6b").val());
    };

    function getParam(a, b, c, d) {
        return "&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d;
    }
    function checkDate(a, b, c, d) {
        a = (parseInt(a) > 0 && parseInt(a) < 10) ? "0" + a : a;
        c = (parseInt(c) > 0 && parseInt(c) < 10) ? "0" + c : c;
        let from = b + "" + a;
        let to = d + "" + c;
        if (parseInt(from) > parseInt(to)) {
            return false;
        }
        return true;
    }


});


