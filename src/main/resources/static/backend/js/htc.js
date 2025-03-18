/* global bootbox, app, urlSampleSentTest, loading, urLog, urlLogCreate, utils, urlSendConfirm */
app.service('htcService', function ($uibModal, msg) {
    var elm = this;
    elm.logs = function (oid, code, name) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'htcLogs',
            controller: 'htc_log',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        oid: oid,
                        code: code,
                        name: name
                    };
                }
            }
        });
    };

    elm.send_test = function (items, dialogReport) {
        //lấy ids bên trong items
        var oids = [];

        for (var prop in items) {
            if (items[prop].length > 0) {
                for (var i = 0; i < items[prop].length; i++) {
                    oids.push(items[prop][i].id);
                }
            }
        }

        if (oids == null || oids.length == 0) {
            msg.danger("Lỗi không tìm thấy id khách hàng cần in phiếu");
            return false;
        }
        dialogReport(urlSampleSentTest + "?oid=" + oids.join(","), null, "Phiếu gửi mẫu xét nghiệm HIV");
        $("#pdf-loading").remove();
    };


    elm.agree_test = function (oid, dialogReport) {
        dialogReport(urlAgreeTest + "?oid=" + oid, null, "Phiếu xác nhận đồng ý xét nghiệm HIV");
        $("#pdf-loading").remove();
    };

    elm.agree_test_shift = function (oid, dialogReport) {
        dialogReport(urlAgreeTestShift + "?oid=" + oid, null, "Phiếu xác nhận đồng ý xét nghiệm HIV - SHIFT");
        $("#pdf-loading").remove();
    };

});

app.controller('htc_log', function ($scope, $uibModalInstance, params, msg) {
    $scope.model = {staffID: 0, htcVisitID: params.oid, code: params.code, name: params.name};
    loading.show();
    $scope.list = function () {
        $.ajax({
            url: urLog,
            data: {oid: params.oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        for (var i = 0; i < resp.data.data.length; i++) {
                            resp.data.data[i].staffID = typeof resp.data.staffs[resp.data.data[i].staffID] == undefined ? 'Hệ thống' : resp.data.staffs[resp.data.data[i].staffID];
                        }
                        $scope.logs = resp.data.data;
                    });
                }
            }
        });
    };

    $scope.add = function () {
        console.log($scope.model.content);
        if ($scope.model.content === undefined || $scope.model.content === '') {
            return false;
        }
        loading.show();
        $.ajax({
            url: urlLogCreate,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.list();
                    $scope.$apply(function () {
                        $scope.model.content = null;
                        $scope.errors = null;
                    });
                } else {
                    $scope.$apply(function () {
                        $scope.errors = resp.data;
                    });
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    $scope.list();

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('htc_send_confirm', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.item = params.item;

    $scope.init = function () {
        $('#siteConfirmTest').append($('<option>', {value: '', text: 'Chọn nơi chuyển máu đến'}));
    };

    $scope.model = {
        siteConfirmTest: params.item.siteConfirmTest == null || params.item.siteConfirmTest == '' ? "" : params.item.siteConfirmTest,
        sampleSentDate: utils.timestampToStringDate(params.item.sampleSentDate == null || params.item.sampleSentDate == '' || params.item.sampleSentDate == 0 ? (new Date()) : params.item.sampleSentDate)

    };

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlSendConfirm + "?oid=" + $scope.item.id,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.reload();
                        }, 2000);
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });

            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});


/**
 * @author DSNAnh
 * Tiếp nhận kết quả khẳng định
 * @returns 
 */
app.controller('htc_confirm_receive', function ($scope, $uibModalInstance, params, msg) {

    $scope.init = function () {
        params.addressAutocomplete("#permanentAddress", "#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        params.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");

        $("#permanentProvinceID").val(params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID).change();
        $("#permanentDistrictID").val(params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID).change();
        $("#permanentWardID").val(params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID).change();

        $("#permanentProvinceID, #permanentDistrictID, #permanentWardID").change(function () {
            $scope.model.permanentProvinceID = $("#permanentProvinceID").val();
            $scope.model.permanentDistrictID = $("#permanentDistrictID").val();
            $scope.model.permanentWardID = $("#permanentWardID").val();
        });

        params.select_search("#permanentProvinceID", "Chọn tỉnh thành");
        params.select_search("#permanentDistrictID", "Chọn quận huyện");
        params.select_search("#permanentWardID", "Chọn xã phường");
    };

    $scope.options = params.options;
    $scope.item = params.item;
    $scope.model = {
        patientName: params.item.patientName == null || params.item.patientName == '' ? "" : params.item.patientName,
        yearOfBirth: params.item.yearOfBirth == null || params.item.yearOfBirth == '' ? "" : params.item.yearOfBirth,
        genderID: params.item.genderID == null || params.item.genderID == '' ? "" : params.item.genderID,
        resultsSiteTime: utils.timestampToStringDate((new Date())),
        feedbackMessage: params.item.feedbackMessage == null || params.item.feedbackMessage == '' ? "" : params.item.feedbackMessage,
        permanentAddress: params.item.permanentAddress == null || params.item.permanentAddress == '' ? "" : params.item.permanentAddress,
        permanentAddressStreet: params.item.permanentAddressStreet == null || params.item.permanentAddressStreet == '' ? "" : params.item.permanentAddressStreet,
        permanentAddressGroup: params.item.permanentAddressGroup == null || params.item.permanentAddressGroup == '' ? "" : params.item.permanentAddressGroup,
        permanentProvinceID: params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID,
        permanentDistrictID: params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID,
        permanentWardID: params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID
    };

    $scope.ok = function (confirm) {
        loading.show();
        $.ajax({
            url: urlConfirmReceive + "?oid=" + $scope.item.id + "&confirm=" + confirm,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.reload();
                        }, 2000);
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });

            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('htc_index', function ($scope, msg, $uibModal, htcService) {
    if ($.getQueryParameters().advisorye_time != null ||
            $.getQueryParameters().confirm_time_from != null ||
            $.getQueryParameters().confirm_time_to != null ||
            $.getQueryParameters().exchange_time_to != null ||
            $.getQueryParameters().service != null ||
            $.getQueryParameters().code != null ||
            $.getQueryParameters().name != null ||
            $.getQueryParameters().test_results_id != null ||
            $.getQueryParameters().confirm_results_id != null ||
            $.getQueryParameters().confirm_results_id != null ||
            $.getQueryParameters().confirm_test_status != null ||
            $.getQueryParameters().exchange_time_to != null ||
            $.getQueryParameters().therapy_exchange_status != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.isTransferOPC = false;

    $scope.init = function () {
        $scope.switchConfig();
        $scope.advisorye_time = $.getQueryParameters().advisorye_time;
        $scope.advisorye_time_from = $.getQueryParameters().advisorye_time_from;
        $scope.advisorye_time_to = $.getQueryParameters().advisorye_time_to;
        $scope.confirm_time_from = $.getQueryParameters().confirm_time_from;
        $scope.confirm_time_to = $.getQueryParameters().confirm_time_to;
        $scope.exchange_time_from = $.getQueryParameters().exchange_time_from;
        $scope.exchange_time_to = $.getQueryParameters().exchange_time_to;
        $scope.$parent.select_mutiple("#objects", "Tất cả");
        $scope.$parent.select_mutiple("#service", "Tất cả");
        $("#objects option[value='']").removeAttr('selected');
        $("#service option[value='']").removeAttr('selected');
        //auto print
        if ($.getQueryParameters().pid != null) {
            setTimeout(function () {
                $(".checkbox-switch[value=" + $.getQueryParameters().pid + "]").iCheck('check');
                $scope.transferSampleModal();
            }, 300);
        }

        // In phiếu đồng ý xét nghiệm
        if ($.getQueryParameters().oid !== null && $.getQueryParameters().printable === 'printable') {
            setTimeout(function () {
                url = urlAgreeTest + "?oid=" + $.getQueryParameters().oid;
                $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
            }, 300);
        }
    };

    /**
     * Yêu cầu bổ sung thông tin
     * 
     * @author DSNAnh
     * @returns 
     */
    $scope.additionalRequest = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'additionalRequest',
                        controller: 'additional_request',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    item: resp.data.model,
                                    options: resp.data.options,
                                    initProvince: $scope.initProvince,
                                    select_search: $scope.$parent.select_search
                                };
                            }
                        }
                    });

                }
            }
        });
    };
    
    // Cập nhật ngày khách hàng nhận nhận kết quả khăng định
    $scope.updateReceiveDate = function (oid) {
        loading.show();
        $.ajax({
            url: urlGetUpdateReceiveDate,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'receiveDate',
                        controller: 'receive-date',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    item: resp.data.model,
                                    options: resp.data.options,
                                    initProvince: $scope.initProvince,
                                    select_search: $scope.$parent.select_search
                                };
                            }
                        }
                    });

                }
            }
        });
    };

    /**
     * In phieu phụ lục 02
     * 
     * @author DSNAnh
     * @returns {Boolean}
     */
    $scope.print02Appendix = function () {
        var switches = $scope.getSwitch();
        if (switches == null || switches.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phụ lục 02");
            return false;
        }
//        loading.show();

        if (switches == null || switches.length == 0) {
            msg.danger("Lỗi không tìm thấy id khách hàng cần in phiếu");
            return false;
        }

        loading.show();
        $.ajax({
            url: urlGetAppendix02SentTest + "?oid=" + switches.join(","),
            type: "GET",
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        $scope.dialogReport(urlAppendix02SentTest + "?oid=" + switches.join(","), null, "Phụ lục 02");
                        $("#pdf-loading").remove();
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });
            }
        });
    };

    $scope.logs = function (oid, code, name) {
        htcService.logs(oid, code, name);
    };

    // In phiếu đồng ý tiết lộ thông tin
    $scope.agreeDisclose = function (oid) {
        if (oid == null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
        $scope.dialogReport(urlAgreeDisclose + "?oid=" + oid, null, "Phiếu đồng ý tiết lộ thông tin");
        $("#pdf-loading").remove();
    };

    // In phiếu đồng ý xét nghiệm
    $scope.agreeTest = function (oid) {
        if (oid == null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
        htcService.agree_test(oid, $scope.dialogReport);
    };

    // In phiếu xét nghiệm SHIFT
    $scope.agreeTestShift = function (oid) {
        if (oid == null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
//        htcService.agree_test_shift(oid, $scope.dialogReport);
        $scope.print(urlAgreeTestShift + "?oid=" + oid);
    };

    $scope.sendConfirm = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    if (resp.data.checkExist == true) {
                        msg.danger("Khách hàng đã tồn tại ở cơ sở khẳng định.");
                    } else {
                        $uibModal.open({
                            animation: true,
                            backdrop: 'static',
                            templateUrl: 'htcSendConfirm',
                            controller: 'htc_send_confirm',
//                        size: 'lg',
                            resolve: {
                                params: function () {
                                    return {
                                        oid: oid,
                                        item: resp.data.model,
                                        options: resp.data.options
                                    };
                                }
                            }
                        });
                    }
                }
            }
        });
    };

    /**
     * In phiếu trả kết quả khách sàng không phản ứng sàng lọc
     * 
     * @author TrangBN
     * @returns {Boolean}
     */
    $scope.answerResult = function () {
        var oid = $scope.getSwitch();
        if (oid === null || oid.length === 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phiếu trả kết quả");
            return false;
        }

        $scope.dialogReport(urlVisitAnswerResult + "?oid=" + oid, null, "Phiếu trả lời kết quả xét nghiệm kháng thể kháng HIV");
        $("#pdf-loading").remove();
    };

    /**
     * @author DSNAnh
     * Tiếp nhận kết quả khẳng định
     * @param {type} oid
     * @param {type} confirm
     * @returns {undefined}
     */
    $scope.confirmReceive = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    if (resp.data.confirmModel == null) {
                        msg.danger("Cơ sở không đối chiếu được kết quả do đã thay đổi nơi xét nghiệm khẳng định.");
                    } else {
                        $uibModal.open({
                            animation: true,
                            backdrop: 'static',
                            templateUrl: 'htcConfirmReceive',
                            controller: 'htc_confirm_receive',
                            size: 'lg',
                            resolve: {
                                params: function () {
                                    return {
                                        oid: oid,
                                        item: resp.data.model,
                                        options: resp.data.options,
                                        addressAutocomplete: $scope.addressAutocomplete,
                                        initProvince: $scope.initProvince,
                                        select_search: $scope.$parent.select_search
                                    };
                                }
                            }
                        });
                    }
                }
            }
        });
    };

    //In phiếu chuyển gửi
    $scope.transferOPC = function (oid) {
        if (oid == null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
//        $scope.dialogReport(urlTransferOPC + "?oid=" + oid, null, "Phiếu chuyển gửi cơ sở điều trị");
//        $("#pdf-loading").remove();

        let url = urlTransferOPC + "?oid=" + oid;
        if ($('#frm-print').length > 0) {
            $('#frm-print').remove();
        }
        $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
    };

    // Chuyển gửi giám sát phát hiện
    $scope.transferGSPH = function (oid) {
        if (oid === null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }

        bootbox.confirm(
                {
                    message: 'Bạn chắc chắn muốn chuyển khách hàng sang báo cáo giám sát phát hiện không?',
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
                    callback: function (confirmed) {
                        if (confirmed) {
                            $.ajax({
                                url: urlTransferGSPH + "?oid=" + oid,
                                method: 'GET',
                                success: function (resp) {
                                    if (resp.success) {
                                        msg.success(resp.message, function () {
                                            location.reload();
                                        }, 2000);
                                    } else {
                                        msg.warning(resp.message, function () {
                                            location.reload();
                                        }, 3000);
                                    }
                                }
                            });
                        } else {
                        }
                    }
                }
        );
    };
    
    // Xác nhận khách hàng không nhận kết quả 
    $scope.confirmNotReceive = function (oid) {
        if (oid === null) {
            msg.warning("Chưa chọn khách hàng cần xác nhận");
            return false;
        }

        bootbox.confirm(
                {
                    message: "Bạn có chắc chắn xác nhận khách hàng <span class='text-bold text-danger'>#" + $("#codePopUp").val() + " - " + $("#patientNamePopUp").val() + "</span> không nhận kết quả?",
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
                    callback: function (confirmed) {
                        if (confirmed) {
                            $.ajax({
                                url: urlConfirmNoReceive + "?oid=" + oid,
                                method: 'GET',
                                success: function (resp) {
                                    if (resp.success) {
                                        msg.success(resp.message, function () {
                                            location.reload();
                                        }, 2000);
                                    } else {
                                        msg.warning(resp.message, function () {
                                            location.reload();
                                        }, 3000);
                                    }
                                }
                            });
                        } else {
                        }
                    }
                }
        );
    };

    //Chuyển gửi điều trị
    $scope.transferTreatment = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet + "?oid=" + oid,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'htcTransferTreatment',
                        controller: 'htc_transfer_treatment',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.model,
                                    options: resp.data.options,
                                    transferStaff: resp.data.transferStaff,
                                    dialogReport: $scope.dialogReport,
                                    selectSearch: $scope.$parent.select_search,
                                    initProvince: $scope.initProvince
                                };
                            }
                        }
                    });
                }
            }
        });
    };

    //in phiếu hẹn
    $scope.ticketResult = function (oid) {
        if (oid == null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
        $scope.print(urlTicketResult + "?oid=" + oid);
//        $scope.dialogReport(urlTicketResult + "?oid=" + oid, null, "Phiếu hẹn nhận kết quả");
//        $("#pdf-loading").remove();
    };

    /**
     * In phieu gui mau xet nghiem
     * 
     * @author pdThang
     * @returns {Boolean}
     */
    $scope.transferSampleModal = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phiếu gửi mẫu xét nghiệm");
            return false;
        }
        loading.show();
        $.ajax({
            url: urlDataModal + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {

                    if (resp.data.items.keyNot.length == 0) {
                        htcService.send_test(resp.data.items, $scope.dialogReport);
                        return false;
                    }

                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'transferSampleModal',
                        controller: 'htc_sent_modal',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.items,
                                    options: resp.data.options,
                                    dialogReport: $scope.dialogReport

                                };
                            }
                        }
                    });
                }
            }
        });

    };
});

/**
 * 
 * @author pdThang
 */
app.controller('htc_sent_modal', function ($scope, $uibModalInstance, params, htcService, msg) {
    $scope.items = params.item;
    $scope.options = params.options;
    $scope.isPrint = !(Object.keys($scope.items).length == 1 && typeof $scope.items.keyNot != undefined);

    $scope.ok = function () {
        htcService.send_test($scope.items, params.dialogReport);
        $scope.cancel();
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('htc_transfer_treatment', function ($scope, $uibModalInstance, params, htcService, msg) {
    $scope.item = params.item;
    $scope.options = params.options;
    $scope.selectSearch = params.selectSearch;
    $scope.initProvince = params.initProvince;
    $scope.transferStaff = params.transferStaff;
    $scope.init = function () {
        $scope.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");

        $("#permanentProvinceID").val(params.item.exchangeProvinceID == null || params.item.exchangeProvinceID == '' ? "" : params.item.exchangeProvinceID).change();
        $("#permanentDistrictID").val(params.item.exchangeDistrictID == null || params.item.exchangeDistrictID == '' ? "" : params.item.exchangeDistrictID).change();
//        $("#permanentWardID").val(params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID).change();

        $("#permanentProvinceID, #permanentDistrictID, #permanentWardID").change(function () {
            $scope.model.permanentProvinceID = $("#permanentProvinceID").val();
            $scope.model.permanentDistrictID = $("#permanentDistrictID").val();
//            $scope.model.permanentWardID = $("#permanentWardID").val();
            $scope.provinceChange();
        });

        $scope.selectSearch("#partnerProvideResult", "");
        $scope.selectSearch("#permanentProvinceID", "");
        $scope.selectSearch("#permanentDistrictID", "");
        $scope.selectSearch("#arrivalSiteID", "");
        $scope.selectSearch("#arvExchangeResult", "");

        $("#arvExchangeResult").prepend("<option value=''>Chọn kết quả tv cgđt arv</option>");
        $("#arrivalSiteID").prepend("<option value=''>Chọn cơ sở điều trị</option>");
        $("#partnerProvideResult").prepend("<option value=''>Chọn kết quả tvxn theo dấu</option>");
    };

    $scope.model = {
        arvExchangeResult: '1',
        confirmTime: utils.timestampToStringDate(params.item.confirmTime),
        exchangeConsultTime: utils.timestampToStringDate(new Date()),
        partnerProvideResult: params.item.partnerProvideResult == null || params.item.partnerProvideResult == '' ? "" : params.item.partnerProvideResult,
        permanentProvinceID: params.item.exchangeProvinceID == null || params.item.exchangeProvinceID == '' ? "" : params.item.exchangeProvinceID,
        permanentDistrictID: params.item.exchangeDistrictID == null || params.item.exchangeDistrictID == '' ? "" : params.item.exchangeDistrictID,
        arrivalSiteID: '',
        arrivalSite: params.item.arrivalSite == null || params.item.arrivalSite == '' ? "" : params.item.arrivalSite,
        exchangeTime: utils.timestampToStringDate(new Date()),
    };

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: "/service/htc/transfer-treatment.json?oid=" + $scope.item.id,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.reload();
                        }, 2000);
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.arrivalSiteIDChange = function () {
        if ($("#arrivalSiteID").val() != 'string:-1') {
            if ($("#arrivalSiteID").val() != null && $("#arrivalSiteID").val() != '') {
                $scope.model.arrivalSite = $("#arrivalSiteID option:selected").text();
                $scope.model.arrivalSiteID = $("#arrivalSiteID option:selected").val();
            }
            if ($("#arrivalSiteID").val() == '') {
                $scope.model.arrivalSite = '';
            }
        } else {
            $scope.model.arrivalSite = '';
        }
    };

    $scope.provinceChange = function () {
        loading.show();
        $.ajax({
            url: "/service/htc/get-transfer-site.json?pid=" + $scope.model.permanentProvinceID +
                    "&did=" + $scope.model.permanentDistrictID,
            type: "GET",
            contentType: "application/json",
            dataType: 'json',
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        $('#arrivalSiteID').children('option:not(:first)').remove();
                        $.each(resp.data, function (k, v) {
                            $('#arrivalSiteID').append(new Option(v, 'string:' + k))
                        });
                    }
                });
            }
        });
        $scope.model.arrivalSiteID = '';
        $scope.model.arrivalSite = '';
    };
});

app.controller('htc_target', function ($scope) {
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            years: {
                required: true,
                digits: true,
                maxlength: 9
            },
            numberCustomer: {
                required: true,
                digits: true,
                maxlength: 9
            },
            numberPositive: {
                required: true,
                digits: true,
                maxlength: 9
            },
            numberGetResult: {
                required: true,
                digits: true,
                maxlength: 9
            },
            positiveAndGetResult: {
                required: true,
                digits: true,
                maxlength: 9
            },
            careForTreatment: {
                required: true,
                number: true,
                maxlength: 9
            }
        },
        messages: {
            years: {
                required: "Năm không được để trống",
                digits: "Vui lòng nhập giá trị nguyên dương",
                maxlength: "Năm không quá 10 kí tự"
            },
            numberCustomer: {
                required: "Số khách hàng được xét nghiệm HIV không được để trống",
                digits: "Vui lòng nhập giá trị nguyên dương",
                maxlength: "Số khách hàng được xét nghiệm HIV không quá 10 kí tự"
            },
            numberPositive: {
                required: "Số khách hàng có kết quả xét nghiệm HIV (+) không được để trống",
                digits: "Vui lòng nhập giá trị nguyên dương",
                maxlength: "Số khách hàng có kết quả xét nghiệm HIV (+) không quá 10 kí tự"
            },
            numberGetResult: {
                required: " Số khách hàng xét nghiệm và quay lại nhận kết quả không được để trống",
                digits: "Vui lòng nhập giá trị nguyên dương",
                maxlength: " Số khách hàng xét nghiệm và quay lại nhận kết quả không quá 10 kí tự"
            },
            positiveAndGetResult: {
                required: "Số khách hàng dương tính và quay lại nhận kết quả không được để trống",
                digits: "Vui lòng nhập giá trị nguyên dương",
                maxlength: "Số khách hàng dương tính và quay lại nhận kết quả không quá 10 kí tự"
            },
            careForTreatment: {
                required: "Tỷ lệ khách hàng dương tính được chuyển gửi thành công vào CSĐT không được để trống",
                number: "Vui lòng nhập số",
                maxlength: "Tỷ lệ khách hàng được chuyển gửi thành công vào CSĐT không quá 10 kí tự"
            }
        }
    });
});


app.controller('pqm_api_token_new', function ($scope) {
    
    $scope.init = function () {
        $scope.select_search("#siteID", "Tất cả" );
    };
    
    
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            siteID: {
                required: true,
            },
            keyToken: {
                required: true,
                maxlength: 500
            },
            secret: {
                required: true,
                maxlength: 500
            },
            info: {
                required: true,
                maxlength: 500
            }
        },
        messages: {
            siteID: {
                required: "Cơ sơ không được để trống",
            },
            keyToken: {
                required: "Key không được để trống",
                maxlength: "Key không quá 500 kí tự"
            },
            secret: {
                required: "Secret không được để trống",
                maxlength: "Secret không quá 500 kí tự"
            },
            info: {
                required: "Mô tả không được để trống",
                maxlength: "Mô tả không quá 500 kí tự"
            }
        }
    });
});

$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip({
    });
});

app.controller('htc_dashboard', function ($scope, amchart) {
    $scope.init = function () {
        $('form input').on('keypress', function (e) {
            return e.which !== 13;
        });
        $scope.search();
    };

    $scope.search = function () {
        $scope.visit();
        $scope.positiveTrans();
        $scope.objectGroup();
        $scope.targetTest();
        $scope.positiveObjectGroup();
        $scope.positiveObjectGroupPercent();
    };

    //TrangBN - update
    $scope.targetTest = function () {
        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() !== '') {
            service = [$("#service").val()];
        }

        // Set display for TVXN
        $.ajax({
            url: '/service/htc-dashboard/target-test.json',
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    $.each(resp.data, function (k, v) {
                        if (k == '5') {
                            data.push({
                                quarter: 'Từ 1/1 đến hiện tại',
                                col1: v[5],
                                col2: v[3],
                            });
                            return true;
                        }
                        data.push({
                            quarter: 'Quý 0' + k,
                            col1: v[4],
                            col2: v[2],
                        });
                    });

                    amchart.bar("targetChartTest", data, function (chart, title, categoryAxis) {
//                        chart.colors.step = 3;

                        title.text = 'So sánh chỉ tiêu số lượt xét nghiệm với kết quả thực tế';
                        categoryAxis.dataFields.category = "quarter";
                        amchart.createColumnSeries(chart, "quarter", "col1", "Chỉ tiêu", false);
                        amchart.createColumnSeries(chart, "quarter", "col2", "Thực tế", false);
                    });
                }
            }
        });

        $.ajax({
            url: '/service/htc-dashboard/target-positive.json',
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    $.each(resp.data, function (k, v) {
                        if (k == '5') {
                            data.push({
                                quarter: 'Từ 1/1 đến hiện tại',
                                col1: v[5],
                                col2: v[3],
                            });
                            return true;
                        }
                        data.push({
                            quarter: 'Quý 0' + k,
                            col1: v[4],
                            col2: v[2],
                        });
                    });

                    amchart.bar("targetChartPositive", data, function (chart, title, categoryAxis) {
//                        chart.colors.step = 3;

                        title.text = 'So sánh chỉ tiêu số người dương tính với kết quả thực tế';
                        categoryAxis.dataFields.category = "quarter";
                        amchart.createColumnSeries(chart, "quarter", "col1", "Chỉ tiêu", false);
                        amchart.createColumnSeries(chart, "quarter", "col2", "Thực tế", false);
                    });
                }
            }
        });
    };
    //AnhDSN

    /**
     * Dowload hình ảnh bản đồ
     * @param {type} id
     * @param {type} chartWrapper
     * @returns {undefined}
     */
    $scope.getChart = function (id, chart) {
        $("a#dowload-" + id).attr("href", chart.getImageURI());
        $("a#dowload-" + id).attr("download", id + ".png");
        $("a#dowload-" + id).fadeIn();

    };

    $scope.getChartImage = function (id, linkElement) {
        var myDiv = document.getElementById(id);
        var myImage = myDiv.children[0];
        linkElement.href = myImage.src;
    };

    /**
     * Thống kê nhóm đối tượng TVXN từ đầu năm đến hiện tại
     * Sửa lại NTGiang
     * 
     */
    $scope.objectGroup = function () {
        var today = new Date();
        var toDay = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
        var fromDay = new Date(new Date().getFullYear(), 0, 1);

        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() != '') {
            service = [$("#service").val()];
        }
        $.ajax({
            url: '/service/htc-dashboard/objectgroup.json',
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("objectQuarterChart", data, function (chart, title) {
                        title.text = 'Phân bổ số XN theo nhóm đối tượng (' + fromDay.toLocaleDateString("en-US") + " - " + toDay + ")";
                    });
                }
            }
        });
    };

    /**
     * Thống kê khách hàng dương tính chuyển gửi điều trị theo tháng
     * @author TrangBN
     * 
     * Sửa lại NTGiang
     * 
     */
    $scope.positiveTrans = function () {
        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() !== '') {
            service = [$("#service").val()];
        }

        // Set display for TVXN
        $.ajax({
            url: '/service/htc-dashboard/positive-trans.json',
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    $.each(resp.data, function (k, v) {
                        data.push({
                            month: k,
                            col1: v[0],
                            col2: v[1]
                        });
                    });

                    amchart.bar("positiveTrans", data, function (chart, title, categoryAxis, valueAxis) {
//                        chart.colors.step = 1;
                        title.text = 'Khách hàng dương tính chuyển gửi điều trị thành công';
                        categoryAxis.dataFields.category = "month";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "col1", "Số lượng dương tính", false, valueAxis);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "col2", "Số lượng CGĐT thành công", false, valueAxisLine);
                    });
                }
            }
        });
    };

    $scope.visit = function () {
        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() !== '') {
            service = [$("#service").val()];
        }

        // Set display for TVXN
        $.ajax({
            url: '/service/htc-dashboard/visit.json',
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    $.each(resp.data, function (k, v) {
                        data.push({
                            month: 'T' + (k < 10 ? '0' + k : k),
                            col1: v[1],
                            col2: v[0],
                        });
                    });

                    amchart.bar("visitChart", data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = 'Số lượt xét nghiệm & số người khẳng định dương tính theo tháng';
                        categoryAxis.dataFields.category = "month";

                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "col1", "Số người XN HIV", false, valueAxis);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "col2", "Số người XN HIV dương tính", false, valueAxisLine);
                    });
                }
            }
        });
    };

    //Tỷ lệ dương tính của từng nhóm đối tượng theo quý
    $scope.positiveObjectGroupPercent = function () {
        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() !== '') {
            service = [$("#service").val()];
        }
        $.ajax({
            url: '/service/htc-dashboard/object-group-percent.json',
            data: {service: service.join(",")},
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    $.each(resp.data.data, function (k, v) {
                        var item = {
                            month: k
                        };
                        for (var i = 0; i < v.length; i++) {
                            item['col' + i] = v[i];
                        }
                        data.push(item);
                    });
                    amchart.bar("positiveObjectGroupPercent", data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = 'Tỷ lệ dương tính của từng nhóm đối tượng theo quý';
                        categoryAxis.dataFields.category = "month";
                        chart.legend.position = "right";

                        for (var i = 0; i < resp.data.cols.length; i++) {
                            amchart.createLineSeries(chart, categoryAxis.dataFields.category, "col" + i, resp.data.cols[i], false, valueAxis, "{valueY}%");
                        }
                    });
                }
            }
        });
    };

    // Tỷ lệ khách hàng dương tính theo nhóm đối tượng
    $scope.positiveObjectGroup = function () {
        var today = new Date();
        var toDay = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
        var fromDay = new Date(new Date().getFullYear(), 0, 1);

        var service = $("#service option").map(function () {
            return $(this).val();
        }).get();

        if ($("#service").val() != '') {
            service = [$("#service").val()];
        }
        $.ajax({
            url: '/service/htc-dashboard/positive-object.json',
            dataType: "json",
            data: {
                service: service.join(",")
            },
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("positiveObjectGroup", data, function (chart, title) {
                        title.text = 'Phân bổ số KH phát hiện dương tính theo nhóm đối tượng (' + fromDay.toLocaleDateString("en-US") + " - " + toDay + ")";
                    });
                }
            }
        });
    };
});

app.controller('htc_elog_index', function ($scope) {
    $scope.isQueryString = ($.getQueryParameters().advisorye_time_to != null ||
            $.getQueryParameters().advisorye_time_from != null
            || $.getQueryParameters().result_id != null
            || $.getQueryParameters().service != null);

    $scope.init = function () {

        $scope.advisorye_time_from = $.getQueryParameters().advisorye_time_from;
        $scope.advisorye_time_to = $.getQueryParameters().advisorye_time_to;
//        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
//        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.select_search("#result_id", "Tất cả", null, false);
        $scope.select_search("#service", "Tất cả");

    };

});

app.controller('report_htc_elogg', function ($scope) {

    $scope.urlExcel = urlExcel;

    $scope.init = function () {
    };

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});

/**
 * @author DSNAnh
 * Yêu cầu cập nhật thông tin
 * @returns 
 */
app.controller('additional_request', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.model = {
        patientName: params.item.patientName == null || params.item.patientName == '' ? "" : params.item.patientName,
        yearOfBirth: params.item.yearOfBirth == null || params.item.yearOfBirth == '' ? "" : params.item.yearOfBirth,
        genderID: params.item.genderID == null || params.item.genderID == '' ? "" : params.item.genderID,
        patientID: params.item.patientID == null || params.item.patientID == '' ? "" : params.item.patientID,
        modeOfTransmission: params.item.modeOfTransmission == null || params.item.modeOfTransmission == '' ? "" : params.item.modeOfTransmission,
        objectGroupID: params.item.objectGroupID == null || params.item.objectGroupID == '' ? "" : params.item.objectGroupID,
        confirmTestStatus: params.item.confirmTestStatus == null || params.item.confirmTestStatus == '' ? "" : params.item.confirmTestStatus,
        resultsSiteTime: utils.timestampToStringDate(params.item.resultsSiteTime == null || params.item.resultsSiteTime == '' || params.item.resultsSiteTime == 0 ? '' : params.item.resultsSiteTime),
        causeChange: '',
        permanentAddress: params.item.permanentAddress == null || params.item.permanentAddress == '' ? "" : params.item.permanentAddress,
        permanentAddressStreet: params.item.permanentAddressStreet == null || params.item.permanentAddressStreet == '' ? "" : params.item.permanentAddressStreet,
        permanentAddressGroup: params.item.permanentAddressGroup == null || params.item.permanentAddressGroup == '' ? "" : params.item.permanentAddressGroup,
        permanentProvinceID: params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID,
        permanentDistrictID: params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID,
        permanentWardID: params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID,
        currentAddress: params.item.currentAddress == null || params.item.currentAddress == '' ? "" : params.item.currentAddress,
        currentAddressStreet: params.item.currentAddressStreet == null || params.item.currentAddressStreet == '' ? "" : params.item.currentAddressStreet,
        currentAddressGroup: params.item.currentAddressGroup == null || params.item.currentAddressGroup == '' ? "" : params.item.currentAddressGroup,
        currentProvinceID: params.item.currentProvinceID == null || params.item.currentProvinceID == '' ? "" : params.item.currentProvinceID,
        currentDistrictID: params.item.currentDistrictID == null || params.item.currentDistrictID == '' ? "" : params.item.currentDistrictID,
        currentWardID: params.item.currentWardID == null || params.item.currentWardID == '' ? "" : params.item.currentWardID
    };
    $scope.init = function () {
        params.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        params.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");

        $("#permanentProvinceID").val(params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID).change();
        $("#permanentDistrictID").val(params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID).change();
        $("#permanentWardID").val(params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID).change();

        $("#currentProvinceID").val(params.item.currentProvinceID == null || params.item.currentProvinceID == '' ? "" : params.item.currentProvinceID).change();
        $("#currentDistrictID").val(params.item.currentDistrictID == null || params.item.currentDistrictID == '' ? "" : params.item.currentDistrictID).change();
        $("#currentWardID").val(params.item.currentWardID == null || params.item.currentWardID == '' ? "" : params.item.currentWardID).change();

        $("#permanentProvinceID, #permanentDistrictID, #permanentWardID").change(function () {
            $scope.model.permanentProvinceID = $("#permanentProvinceID").val();
            $scope.model.permanentDistrictID = $("#permanentDistrictID").val();
            $scope.model.permanentWardID = $("#permanentWardID").val();
        });

        $("#currentProvinceID, #currentDistrictID, #currentWardID").change(function () {
            $scope.model.currentProvinceID = $("#currentProvinceID").val();
            $scope.model.currentDistrictID = $("#currentDistrictID").val();
            $scope.model.currentWardID = $("#currentWardID").val();
        });

        params.select_search("#permanentProvinceID", "Chọn tỉnh thành");
        params.select_search("#permanentDistrictID", "Chọn quận huyện");
        params.select_search("#permanentWardID", "Chọn xã phường");

        params.select_search("#currentProvinceID", "Chọn tỉnh thành");
        params.select_search("#currentDistrictID", "Chọn quận huyện");
        params.select_search("#currentWardID", "Chọn xã phường");

        params.select_search("#objectGroupID", "Chọn nhóm đối tượng");
        params.select_search("#modeOfTransmission", "Chọn đường lây nhiễm");

        if ($scope.model.confirmTestStatus == '2') {
            $("#resultsSiteTime").removeAttr("disabled");
        } else {
            $("#resultsSiteTime").attr("disabled", "disabled");
        }

        $("#objectGroupID").prepend("<option value=''>Chọn nhóm đối tượng</option>");
        $("#genderID").prepend("<option value=''>Chọn giới tính</option>");
        $("#modeOfTransmission").prepend("<option value=''>Chọn đường lây nhiễm</option>");
    };
    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlAdditionalRequest + "?oid=" + params.oid,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.reload();
                        }, 2000);
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('receive-date', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.model = {
        id: params.item.id,
        resultsPatienTime: params.item.resultsPatienTime == null || params.item.resultsPatienTime == '' ? $.datepicker.formatDate('dd/mm/yy', new Date()) : params.item.resultsPatienTime,
        exchangeServiceConfirm: params.item.exchangeServiceConfirm == null || params.item.exchangeServiceConfirm == '' ? "1" : params.item.exchangeServiceConfirm,
        partnerProvideResult: params.item.partnerProvideResult == null || params.item.partnerProvideResult == '' ? "" : params.item.partnerProvideResult,
        arvExchangeResult: params.item.arvExchangeResult == null || params.item.arvExchangeResult == '' ? "" : params.item.arvExchangeResult,
        arrivalSiteID: params.item.arrivalSiteID == null || params.item.arrivalSiteID == '' ? "" : params.item.arrivalSiteID,
        staffAfterID: params.item.staffAfterID == null || params.item.staffAfterID == '' ? current_user_name : params.item.staffAfterID,
        permanentProvinceID: params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID,
        permanentDistrictID: params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID,
        permanentWardID: params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID,
    };
    $scope.init = function () {
        params.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");

        $("#permanentProvinceID").val(params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID).change();
        $("#permanentDistrictID").val(params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID).change();
        $("#permanentWardID").val(params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID).change();
        
        params.select_search("#exchangeServiceConfirm", "Chọn dịch vụ tư vấn chuyển gửi");
        params.select_search("#partnerProvideResult", "Chọn kết quả TVXN theo dấu");
        params.select_search("#arvExchangeResult", "Chọn kết quả TV CGĐT ARV");
        params.select_search("#arrivalSiteID", "Chọn cơ sở điều trị");
        
        $("#exchangeServiceConfirm").prepend("<option value=''>Chọn dịch vụ tư vấn chuyển gửi</option>");
        $("#partnerProvideResult").prepend("<option value=''>Chọn kết quả TVXN theo dấu</option>");
        $("#arvExchangeResult").prepend("<option value=''>Chọn kết quả TV CGĐT ARV</option>");
        $("#arrivalSiteID").prepend("<option value=''>Chọn cơ sở điều trị</option>");

        $("#permanentProvinceID, #permanentDistrictID, #permanentWardID").change(function () {
            $scope.model.permanentProvinceID = $("#permanentProvinceID").val();
            $scope.model.permanentDistrictID = $("#permanentDistrictID").val();
            $scope.model.permanentWardID = $("#permanentWardID").val();
        });

        params.select_search("#permanentProvinceID", "Chọn tỉnh thành");
        params.select_search("#permanentDistrictID", "Chọn quận huyện");
        params.select_search("#permanentWardID", "Chọn xã phường");

        $scope.provinceChange();
        $("#permanentProvinceID, #permanentDistrictID").change(function () {
            $scope.model.permanentProvinceID = $("#permanentProvinceID").val();
            $scope.model.permanentDistrictID = $("#permanentDistrictID").val();
            $scope.provinceChange();
        });
        
        // Khởi tạo ban đầu
        if ($("#arvExchangeResult").val() !== 'string:1') {
            $("#exchangeTime").attr("disabled", "disabled");
            $("#exchangeTime").val('').change();
        } else {
            $("#exchangeTime").removeAttr("disabled");
        }
        
        if ($("#exchangeServiceConfirm").val() !== 'string:5') {
            $("#exchangeServiceNameConfirm").attr("disabled", "disabled");
            $("#exchangeServiceNameConfirm").val('');
        } else {
            $("#exchangeServiceNameConfirm").removeAttr("disabled");
        }
        
        if ($scope.model.exchangeServiceConfirm !== '1') {
            $("#exchangeConsultTime").attr("disabled", "disabled");
            $("#exchangeConsultTime").val("");
            $("#partnerProvideResult").attr("disabled", "disabled");
            $("#partnerProvideResult").val("");
            $("#arvExchangeResult").attr("disabled", "disabled");
            $("#arvExchangeResult").val("");
        } else {
            $("#exchangeConsultTime").remove("disabled");
            $scope.model.exchangeConsultTime = $.datepicker.formatDate('dd/mm/yy', new Date());
            $("#partnerProvideResult").remove("disabled");
            $scope.model.partnerProvideResult = '';
            $("#arvExchangeResult").remove("disabled");
            $scope.model.arvExchangeResult = '';
        }
        
        if ($("#exchangeTime").val() === '') {
            $("#permanentProvinceID").attr("disabled", "disabled");
                $("#permanentProvinceID").val('').change();
                $("#permanentDistrictID").attr("disabled", "disabled");
                $("#permanentDistrictID").val('').change();
                $("#arrivalSiteID").attr("disabled", "disabled");
                $("#arrivalSiteID").val('');
        } else {
            $("#permanentProvinceID").removeAttr("disabled").change();
            $("#permanentDistrictID").removeAttr("disabled").change();
            $("#arrivalSiteID").removeAttr("disabled").change();
        }
        
        if ($("#arrivalSiteID").val() === '') {
            $("#arrivalSite").attr("disabled", "disabled");
            $("#arrivalSite").val('').change();
        } else {
            $("#arrivalSite").removeAttr("disabled").change();
            $("#arrivalSite").val($("#arrivalSiteID option:selected").text());
        }
        
        // Các sự kiện thay đổi
        $("#exchangeServiceConfirm").change(function () {
            if ($("#exchangeServiceConfirm").val() !== 'string:5') {
                $("#exchangeServiceNameConfirm").attr("disabled", "disabled");
                $("#exchangeServiceNameConfirm").val('');
            } else {
                $("#exchangeServiceNameConfirm").removeAttr("disabled");
            }
            
            if ($("#exchangeServiceConfirm").val() !== 'string:1') {
                $("#exchangeConsultTime").attr("disabled", "disabled");
                $("#exchangeConsultTime").val("");
                $scope.model.exchangeConsultTime = "";
                $("#partnerProvideResult").attr("disabled", "disabled");
                $("#partnerProvideResult").val("");
                $("#arvExchangeResult").attr("disabled", "disabled");
                $("#arvExchangeResult").val("").change();
                $scope.model.arvExchangeResult = "";
            } else {
                $("#exchangeConsultTime").removeAttr("disabled");
                $("#exchangeConsultTime").val($.datepicker.formatDate('dd/mm/yy', new Date()));
                $scope.model.exchangeConsultTime = $.datepicker.formatDate('dd/mm/yy', new Date());
                $("#partnerProvideResult").removeAttr("disabled");
                $("#partnerProvideResult").val("string:");
                $("#arvExchangeResult").removeAttr("disabled");
                $("#arvExchangeResult").val("string:");
            }
        });
        $("#arvExchangeResult").change(function () {
            if ($("#arvExchangeResult").val() !== 'string:1') {
                $("#exchangeTime").attr("disabled", "disabled");
                $("#exchangeTime").val('').blur();
                $scope.model.exchangeTime = "";
            } else {
                $("#exchangeTime").removeAttr("disabled");
            }
        });
        $("#exchangeTime").on('blur',function () {
            if ($("#exchangeTime").val() === '' || $("#exchangeTime").val().includes("d") || $("#exchangeTime").val().includes("m") || $("#exchangeTime").val().includes("y")) {
                $("#permanentProvinceID").attr("disabled", "disabled");
                
                if($("#permanentProvinceID").val() !== ''){
                    $("#permanentProvinceID").val('').change();
                }
                
                $("#permanentDistrictID").attr("disabled", "disabled");
                $("#permanentDistrictID").val('');
                $("#arrivalSiteID").attr("disabled", "disabled");
                $("#arrivalSiteID").val('');
                
                $scope.model.permanentProvinceID = "";
                $scope.model.permanentDistrictID = "";
                $scope.model.arrivalSiteID = "";
                
            } else {
                $("#permanentProvinceID").removeAttr("disabled");
                $("#permanentDistrictID").removeAttr("disabled");
                $("#arrivalSiteID").removeAttr("disabled");
            }
            
            if ($("#arrivalSiteID").val() === "") {
                $("#arrivalSite").attr("disabled", "disabled");
                $("#arrivalSite").val("");
                $scope.model.arrivalSite = "";
            }
        });
        
        $("#arrivalSiteID").change(function () {
            if ($("#arrivalSiteID").val() === '' || $("#arrivalSiteID").val() !== 'string:-1') {
                $("#arrivalSite").attr("disabled", "disabled");
                $("#arrivalSite").val('').change();
                if ($("#arrivalSiteID").val() !== 'string:-1') {
                    $("#arrivalSite").val($("#arrivalSiteID option:selected").text()).change();
                }
            } else {
                $("#arrivalSite").removeAttr("disabled").change();
                $("#arrivalSite").val("");
                $scope.model.arrivalSite = "";
            }
        });
    };
    
    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlUpdReceiveDate + "?oid=" + params.oid,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.model),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.reload();
                        }, 2000);
                    } else {
                        $scope.errors = resp.data;
                        if (resp.message) {
                            msg.danger(resp.message);
                        }
                    }
                });
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    
    $scope.provinceChange = function () {
        loading.show();
        $.ajax({
            url: "/service/htc/get-transfer-site.json?pid=" + $scope.model.permanentProvinceID +
                    "&did=" + $scope.model.permanentDistrictID,
            type: "GET",
            contentType: "application/json",
            dataType: 'json',
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        $('#arrivalSiteID').children('option:not(:first)').remove();
                        $.each(resp.data, function (k, v) {
                            $('#arrivalSiteID').append(new Option(v, 'string:' + k));
                        });
                    }
                });
            }
        });
        
        $scope.model.arrivalSiteID = '';
        $scope.model.arrivalSite = '';
    };
    
    $scope.arrivalSiteIDChange = function () {
        if ($("#arrivalSiteID").val() != 'string:-1') {
            if ($("#arrivalSiteID").val() != null && $("#arrivalSiteID").val() != '') {
                $scope.model.arrivalSite = $("#arrivalSiteID option:selected").text();
                $scope.model.arrivalSiteID = $("#arrivalSiteID option:selected").val();
            }
            if ($("#arrivalSiteID").val() == '') {
                $scope.model.arrivalSite = '';
            }
        } else {
            $scope.model.arrivalSite = '';
        }
    };
});