app.controller('pac_patient_index', function ($scope, pacPatientService, $uibModal, msg) {
    $scope.formSearch = formSearch;
    $scope.isQueryString = ($.getQueryParameters().fullname != null
            || $.getQueryParameters().confirm_time_from != null
            || $.getQueryParameters().confirm_time_to != null
            || $.getQueryParameters().year_of_birth != null);

    $scope.init = function () {


        $scope.switchConfig();
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.request_death_time_from = $.getQueryParameters().request_death_time_from;
        $scope.request_death_time_to = $.getQueryParameters().request_death_time_to;
        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.select_search("#gender_id", "Tất cả", null, false);
        $scope.select_mutiple("#status_of_resident_id", "Tất cả");
        $scope.select_search("#status_of_treatment_id", "Tất cả", null, false);
        $scope.select_search("#detect_province_id", "Tất cả");
        $scope.select_search("#permanent_district_id", "Tất cả");
        $scope.select_search("#permanent_ward_id", "Tất cả");
        $scope.select_search("#permanent_province_id", "Tất cả");
        $scope.select_search("#siteConfirmID", "Tất cả");
        $scope.select_search("#siteTreatmentFacilityID", "Tất cả");
        if ($scope.formSearch.statusOfResidentIDs !== '') {
            $("#status_of_resident_id").val($scope.formSearch.statusOfResidentIDs).change();
        }
        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        if ($("#permanent_province_id").val() === '' && formSearch.isVAAC) {
            $("#actionButton").hide();
        } else {
            $("#actionButton").show();
        }
        $("#permanent_province_id").on("change", function () {
            if ($("#permanent_province_id").val() === '' && formSearch.isVAAC) {
                $("#actionButton").hide();
            } else {
                $("#actionButton").show();
            }
        });

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        if (formSearch.addressFilter === 'hokhau') {
            $(".lblProvince").text("Tỉnh/TP thường trú");
            $(".lblDistrict").text("Quận/Huyện thường trú");
            $(".lblWard").text("Xã/Phường thường trú");
        }
        if (formSearch.addressFilter === 'tamtru') {
            $(".lblProvince").text("Tỉnh/TP tạm trú");
            $(".lblDistrict").text("Quận/Huyện tạm trú");
            $(".lblWard").text("Xã/Phường tạm trú");
        }

        $("#thuongtru").click(function () {
            $(".lblProvince").text("Tỉnh/TP thường trú");
            $(".lblDistrict").text("Quận/Huyện thường trú");
            $(".lblWard").text("Xã/Phường thường trú");
        });

        $("#tamtru").click(function () {
            $(".lblProvince").text("Tỉnh/TP tạm trú");
            $(".lblDistrict").text("Quận/Huyện tạm trú");
            $(".lblWard").text("Xã/Phường tạm trú");
        });


        $("input[type='radio']").on('ifChanged', function (e) {
            if ($(this).val() === 'hokhau') {
                $(".lblProvince").text("Tỉnh/TP thường trú");
                $(".lblDistrict").text("Quận/Huyện thường trú");
                $(".lblWard").text("Xã/Phường thường trú");
            }
            if ($(this).val() === 'tamtru') {
                $(".lblProvince").text("Tỉnh/TP tạm trú");
                $(".lblDistrict").text("Quận/Huyện tạm trú");
                $(".lblWard").text("Xã/Phường tạm trú");
            }
        });
    };

    $scope.logs = function (oid, code, name) {
        pacPatientService.logs(oid, code, name);
    };

    /**
     * Kiểm tra trùng lặp
     * 
     * pdthang
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionFillter = function (oid) {
        pacPatientService.fillter(oid, "quanly");
    };

    /**
     * Kiểm tra thông tin
     * 
     * DSNAnh
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionUpdate = function (oid) {
        pacPatientService.pacPatientUpdate(oid);
    };

    $scope.updateReport = function (oid) {
        loading.show();
        $.ajax({
            url: urlPacUpdateReport,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacUpdateReport',
                        controller: 'pac_update_report',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    item: resp.data.model,
                                    warning: resp.data.warning,
                                    options: resp.data.options,
                                    select_mutiple: $scope.select_mutiple,
                                    addressAutocomplete: $scope.addressAutocomplete,
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
     * @author TrangBN
     * @returns {Boolean}
     */
    $scope.print02Appendix = function () {
        var switches = $scope.getSwitch();
        if (switches === null || switches.length === 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phụ lục 02");
            return false;
        }

        if (switches === null || switches.length === 0) {
            msg.danger("Lỗi không tìm thấy id khách hàng cần in phiếu");
            return false;
        }

        $scope.dialogReport(urlAppendix02 + "?oid=" + switches.join(","), null, "Phụ lục 02");
        $("#pdf-loading").remove();

    };
});

app.controller('api_pqm_token', function ($scope, pacPatientService, $uibModal, msg) {
//    $scope.isQueryString = ($.getQueryParameters().sites != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.select_search("#sites", "Tất cả");
        $scope.select_search("#siteID", "Tất cả" );
    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            siteID: {
                required: true
            },
            info: {
                maxlength: 500
            }
        },
        messages: {
            siteID: {
                required: "Cơ sở không được để trống",
            },
            info: {
                maxlength: "Mô tả không quá 500 kí tự"
            }
        }
    });



});

app.controller('pac_hiv_index_export', function ($scope, pacPatientService, $uibModal) {
    $scope.isQueryString = ($.getQueryParameters().from != null
            || $.getQueryParameters().to != null
            || $.getQueryParameters().year_of_birth != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;
    };
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});

app.controller('pac_patient_export_hivinfo_index', function ($scope) {
//    $scope.isQueryString = ($.getQueryParameters().from != null
//            || $.getQueryParameters().to != null);
//
//    $scope.init = function () {
////        $scope.initInput();
////        $scope.dateFilter = utils.getContentOfDefault(formSearch.dateFilter, '');
//        
//        $scope.from = $.getQueryParameters().from;
//        $scope.to = $.getQueryParameters().to;
//
////        if ($scope.dateFilter === 'ngayxn') {
////            $("#fromTime").text("Ngày xét nghiệm từ");
////            $("#toTime").text("Ngày xét nghiệm đến");
////        }
////        if ($scope.dateFilter === 'chuyenquanly') {
////            $("#fromTime").text("Ngày quản lý từ");
////            $("#toTime").text("Ngày quản lý đến");
////        }
////
////
////        $("#ngayxn").click(function () {
////            $("#fromTime").text("Ngày xét nghiệm từ");
////            $("#toTime").text("Ngày xét nghiệm đến");
////        });
////
////        $("#ngayquanly").click(function () {
////            $("#fromTime").text("Ngày quản lý từ");
////            $("#toTime").text("Ngày quản lý đến");
////        });
//    };

    $scope.isQueryString = ($.getQueryParameters().from != null
            || $.getQueryParameters().to != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;

        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
    };

    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});


app.controller('api_pqm_log', function ($scope, pacPatientService, $uibModal, msg) {

    $scope.init = function () {
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;
    };
});


app.controller('pac_hiv_index_export', function ($scope, pacPatientService, $uibModal) {
    $scope.isQueryString = ($.getQueryParameters().from != null
            || $.getQueryParameters().to != null
            || $.getQueryParameters().year_of_birth != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;
    };
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});

app.controller('pac_patient_export_hivinfo_index', function ($scope) {
//    $scope.isQueryString = ($.getQueryParameters().from != null
//            || $.getQueryParameters().to != null);
//
//    $scope.init = function () {
////        $scope.initInput();
////        $scope.dateFilter = utils.getContentOfDefault(formSearch.dateFilter, '');
//        
//        $scope.from = $.getQueryParameters().from;
//        $scope.to = $.getQueryParameters().to;
//
////        if ($scope.dateFilter === 'ngayxn') {
////            $("#fromTime").text("Ngày xét nghiệm từ");
////            $("#toTime").text("Ngày xét nghiệm đến");
////        }
////        if ($scope.dateFilter === 'chuyenquanly') {
////            $("#fromTime").text("Ngày quản lý từ");
////            $("#toTime").text("Ngày quản lý đến");
////        }
////
////
////        $("#ngayxn").click(function () {
////            $("#fromTime").text("Ngày xét nghiệm từ");
////            $("#toTime").text("Ngày xét nghiệm đến");
////        });
////
////        $("#ngayquanly").click(function () {
////            $("#fromTime").text("Ngày quản lý từ");
////            $("#toTime").text("Ngày quản lý đến");
////        });
//    };

    $scope.isQueryString = ($.getQueryParameters().from != null
            || $.getQueryParameters().to != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;

        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
    };

    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});



app.controller('pac_update_report', function ($scope, $uibModalInstance, params, msg) {
    $scope.item = params.item;
    $scope.options = params.options;
    $scope.warning = params.warning;
    $scope.model = {
        fullname: params.item.fullname == null || params.item.fullname == '' ? "" : params.item.fullname,
        yearOfBirth: params.item.yearOfBirth == null || params.item.yearOfBirth == '' ? "" : params.item.yearOfBirth,
        genderID: params.item.genderID == null || params.item.genderID == '' ? "" : params.item.genderID,
        raceID: params.item.raceID == null || params.item.raceID == '' ? "" : params.item.raceID,
        identityCard: params.item.identityCard == null || params.item.identityCard == '' ? "" : params.item.identityCard,
        healthInsuranceNo: params.item.healthInsuranceNo == null || params.item.healthInsuranceNo == '' ? "" : params.item.healthInsuranceNo,
        permanentAddressFull: params.item.permanentAddressFull == null || params.item.permanentAddressFull == '' ? "" : params.item.permanentAddressFull,
        currentAddressFull: params.item.currentAddressFull == null || params.item.currentAddressFull == '' ? "" : params.item.currentAddressFull,
        causeOfDeath: params.item.causeOfDeath == null || params.item.causeOfDeath == '' ? "" : params.item.causeOfDeath,
        confirmTime: utils.timestampToStringDate(params.item.confirmTime == null || params.item.confirmTime == '' ? "" : params.item.confirmTime),
        requestDeathTime: utils.timestampToStringDate(params.item.requestDeathTime == null || params.item.requestDeathTime == '' ? "" : params.item.requestDeathTime),
        deathTime: utils.timestampToStringDate(params.item.deathTime == null || params.item.deathTime == '' ? "" : params.item.deathTime),
        siteConfirmID: params.item.siteConfirmID == null || params.item.siteConfirmID == '' ? "" : params.item.siteConfirmID,
        bloodBaseID: params.item.bloodBaseID == null || params.item.bloodBaseID == '' ? "" : params.item.bloodBaseID,
        startTreatmentTime: utils.timestampToStringDate(params.item.startTreatmentTime == null || params.item.startTreatmentTime == '' ? "" : params.item.startTreatmentTime),
        siteTreatmentFacilityID: params.item.siteTreatmentFacilityID == null || params.item.siteTreatmentFacilityID == '' ? "" : params.item.siteTreatmentFacilityID,
        treatmentRegimenID: params.item.treatmentRegimenID == null || params.item.treatmentRegimenID == '' ? "" : params.item.treatmentRegimenID,
        statusOfResidentID: params.item.statusOfResidentID == null || params.item.statusOfResidentID == '' ? "" : params.item.statusOfResidentID,
        note: params.item.note == null || params.item.note == '' ? "" : params.item.note,
        permanentAddressNo: params.item.permanentAddressNo == null || params.item.permanentAddressNo == '' ? "" : params.item.permanentAddressNo,
        permanentAddressGroup: params.item.permanentAddressGroup == null || params.item.permanentAddressGroup == '' ? "" : params.item.permanentAddressGroup,
        permanentAddressStreet: params.item.permanentAddressStreet == null || params.item.permanentAddressStreet == '' ? "" : params.item.permanentAddressStreet,
        permanentProvinceID: params.item.permanentProvinceID == null || params.item.permanentProvinceID == '' ? "" : params.item.permanentProvinceID,
        permanentDistrictID: params.item.permanentDistrictID == null || params.item.permanentDistrictID == '' ? "" : params.item.permanentDistrictID,
        permanentWardID: params.item.permanentWardID == null || params.item.permanentWardID == '' ? "" : params.item.permanentWardID,
        currentAddressNo: params.item.currentAddressNo == null || params.item.currentAddressNo == '' ? "" : params.item.currentAddressNo,
        currentAddressGroup: params.item.currentAddressGroup == null || params.item.currentAddressGroup == '' ? "" : params.item.currentAddressGroup,
        currentAddressStreet: params.item.currentAddressStreet == null || params.item.currentAddressStreet == '' ? "" : params.item.currentAddressStreet,
        currentProvinceID: params.item.currentProvinceID == null || params.item.currentProvinceID == '' ? "" : params.item.currentProvinceID,
        currentDistrictID: params.item.currentDistrictID == null || params.item.currentDistrictID == '' ? "" : params.item.currentDistrictID,
        currentWardID: params.item.currentWardID == null || params.item.currentWardID == '' ? "" : params.item.currentWardID,
        objectGroupID: params.item.objectGroupID == null || params.item.objectGroupID == '' ? "" : params.item.objectGroupID,
        modeOfTransmissionID: params.item.modeOfTransmissionID == null || params.item.modeOfTransmissionID == '' ? "" : params.item.modeOfTransmissionID,
        patientPhone: params.item.patientPhone == null || params.item.patientPhone == '' ? "" : params.item.patientPhone,
        statusOfTreatmentID: params.item.statusOfTreatmentID == null || params.item.statusOfTreatmentID == '' ? "" : params.item.statusOfTreatmentID,
        statusOfChangeTreatmentID: params.item.statusOfChangeTreatmentID == null || params.item.statusOfChangeTreatmentID == '' ? "" : params.item.statusOfChangeTreatmentID,
        changeTreatmentDate: utils.timestampToStringDate(params.item.changeTreatmentDate == null || params.item.changeTreatmentDate == '' ? "" : params.item.changeTreatmentDate)

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

        params.select_mutiple("#causeOfDeath", "Chọn nguyên nhân");
        $("#genderID").append(new Option("Chọn giới tính", ""));
        $("#raceID").append(new Option("Chọn dân tộc", ""));
        $("#statusOfResidentID").append(new Option("Chọn kết quả xác minh hiện trạng cư trú", ""));
        $("#objectGroupID").append(new Option("Chọn đối tượng", ""));
        $("#modeOfTransmissionID").append(new Option("Chọn đường lây nhiễm", ""));
        $("#statusOfTreatmentID").append(new Option("Chọn trạng thái điều trị", ""));
        $("#statusOfChangeTreatmentID").append(new Option("Chọn trạng thái biến động điều trị", ""));
        $("#siteTreatmentFacilityID").append(new Option("Chọn nơi điều trị", ""));
        $("#treatmentRegimenID").append(new Option("Chọn phác đồ", ""));

        params.select_search("#objectGroupID", "Chọn đối tượng");
        params.select_search("#siteTreatmentFacilityID", "Chọn nơi điều trị");
        params.select_search("#treatmentRegimenID", "Chọn phác đồ");

        $("#statusOfChangeTreatmentID").val(params.item.statusOfChangeTreatmentID == null || params.item.statusOfChangeTreatmentID == '' ? "" : params.item.statusOfChangeTreatmentID).change();
        $("#changeTreatmentDate").val(params.item.changeTreatmentDate == null || params.item.changeTreatmentDate == '' ? "" : params.item.changeTreatmentDate).change();

        if ($scope.model.statusOfChangeTreatmentID === null || $scope.model.statusOfChangeTreatmentID === "") {
            $("#changeTreatmentDate").attr("disabled", "disabled");
            $("#changeTreatmentDate").val("").change();
        }

        $($("#statusOfChangeTreatmentID")).change(function () {
            if ($(this).val() === null || $(this).val() === "") {
                $("#changeTreatmentDate").attr("disabled", "disabled");
                $("#changeTreatmentDate").val("").change();
            } else {
                $("#changeTreatmentDate").removeAttr("disabled").change();
            }

        });
    };

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlPacUpdateReportPost + "?oid=" + $scope.item.id,
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