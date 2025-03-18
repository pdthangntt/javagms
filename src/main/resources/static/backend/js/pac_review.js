app.controller('pac_review_index', function ($scope, msg, pacPatientService, $uibModal) {
    if ($.getQueryParameters().fullname != null ||
            $.getQueryParameters().confirm_time_from != null || $.getQueryParameters().confirm_time_to != null || $.getQueryParameters().year_of_birth != null
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        detect_province_id: "#detect_province_id",
        permanent_district_id: "#permanent_district_id",
        permanent_ward_id: "#permanent_ward_id",
        permanent_province_id: "#permanent_province_id"

    };

    $scope.init = function () {

        $scope.switchConfig();
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.$parent.select_search($scope.items.detect_province_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_district_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_ward_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_province_id, "Tất cả");
        $scope.select_search("#siteConfirmID", "Tất cả");
        $scope.select_search("#siteTreatmentFacilityID", "Tất cả");

        $scope.addressFilter = utils.getContentOfDefault(addressFilter, '');

        if ($scope.addressFilter === 'hokhau') {
            $(".lblProvince").text("Tỉnh/TP thường trú");
            $(".lblDistrict").text("Quận/Huyện thường trú");
            $(".lblWard").text("Xã/Phường thường trú");
        }
        if ($scope.addressFilter === 'tamtru') {
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
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        if (search.permanentProvinceID !== '') {
            $("#permanent_province_id").val(search.permanentProvinceID).change();
        }
        if (search.permanentDistrictID !== '') {
            $("#permanent_district_id").val(search.permanentDistrictID).change();
        }
        if (search.permanentWardID !== '') {
            $("#permanent_ward_id").val(search.permanentWardID).change();
        }


        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        if (!isVAAC) {
            $scope.isPrintf = true;
        } else {
            $scope.isPrintf = false;
            $scope.isPrintf = $($scope.items.permanent_province_id).val() != null && $($scope.items.permanent_province_id).val() != '';
            $($scope.items.permanent_province_id).change(function () {
                $scope.$apply(function () {
                    $scope.isPrintf = $($scope.items.permanent_province_id).val() != null && $($scope.items.permanent_province_id).val() != '';
                });
            });
        }
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
        pacPatientService.fillter(oid, "");
    };

    //DSNAnh Cập nhật thông tin rà soát
    $scope.updateReview = function (oid, tab) {
        loading.show();
        $.ajax({
            url: urlReviewGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'updateReview',
                        controller: 'update_review',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    tab: tab,
                                    item: resp.data.model,
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
        if (switches == null || switches.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phụ lục 02");
            return false;
        }

        if (switches == null || switches.length == 0) {
            msg.danger("Lỗi không tìm thấy id khách hàng cần in phiếu");
            return false;
        }

        $scope.dialogReport(urlAppendix02SentTest + "?oid=" + switches.join(","), null, "Phụ lục 02");
        $("#pdf-loading").remove();

    };

});

app.controller('update_review', function ($scope, $uibModalInstance, params, msg) {
    $scope.tab = params.tab;
    $scope.item = params.item;
    $scope.options = params.options;
    $scope.model = {
        fullname: params.item.fullname == null || params.item.fullname == '' ? "" : params.item.fullname,
        yearOfBirth: params.item.yearOfBirth == null || params.item.yearOfBirth == '' ? "" : params.item.yearOfBirth,
        genderID: params.item.genderID == null || params.item.genderID == '' ? "" : params.item.genderID,
        raceID: params.item.raceID == null || params.item.raceID == '' ? "" : params.item.raceID,
        identityCard: params.item.identityCard == null || params.item.identityCard == '' ? "" : params.item.identityCard,
        healthInsuranceNo: params.item.healthInsuranceNo == null || params.item.healthInsuranceNo == '' ? "" : params.item.healthInsuranceNo,
        permanentAddressFull: params.item.permanentAddressFull == null || params.item.permanentAddressFull == '' ? "" : params.item.permanentAddressFull,
        currentAddressFull: params.item.currentAddressFull == null || params.item.currentAddressFull == '' ? "" : params.item.currentAddressFull,
        jobID: params.item.jobID == null || params.item.jobID == '' ? "" : params.item.jobID,
        objectGroupID: params.item.objectGroupID == null || params.item.objectGroupID == '' ? "" : params.item.objectGroupID,
        modeOfTransmissionID: params.item.modeOfTransmissionID == null || params.item.modeOfTransmissionID == '' ? "" : params.item.modeOfTransmissionID,
        riskBehaviorID: params.item.riskBehaviorID == null || params.item.riskBehaviorID == '' ? "" : params.item.riskBehaviorID,
        causeOfDeath: params.item.causeOfDeath == null || params.item.causeOfDeath == '' ? "" : params.item.causeOfDeath,
        confirmTime: utils.timestampToStringDate(params.item.confirmTime == null || params.item.confirmTime == '' ? "" : params.item.confirmTime),
        deathTime: utils.timestampToStringDate(params.item.deathTime == null || params.item.deathTime == '' ? "" : params.item.deathTime),
        requestDeathTime: utils.timestampToStringDate(params.item.requestDeathTime == null || params.item.requestDeathTime == '' ? "" : params.item.requestDeathTime),
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

        params.select_search("#siteConfirmID", "Chọn cơ sở XN khẳng định");
        params.select_search("#bloodBaseID", "Chọn nơi lấy mẫu XN");
        params.select_search("#siteTreatmentFacilityID", "Chọn nơi điều trị");

        params.select_search("#currentProvinceID", "Chọn tỉnh thành");
        params.select_search("#currentDistrictID", "Chọn quận huyện");
        params.select_search("#currentWardID", "Chọn xã phường");

        params.select_mutiple("#causeOfDeath", "Chọn nguyên nhân");
        params.select_mutiple("#riskBehaviorID", "Chọn hành vi nguy cơ");
        $("#genderID").append(new Option("Chọn giới tính", ""));
        $("#raceID").append(new Option("Chọn dân tộc", ""));
        $("#jobID").append(new Option("Chọn nghề nghiệp", ""));
        $("#objectGroupID").append(new Option("Chọn nhóm đối tượng", ""));
        $("#modeOfTransmissionID").append(new Option("Chọn đường lây nhiễm", ""));
        $("#siteConfirmID").append(new Option("Chọn cơ sở XN khẳng định", ""));
        $("#bloodBaseID").append(new Option("Chọn nơi lấy mẫu XN", ""));
        $("#siteTreatmentFacilityID").append(new Option("Chọn nơi điều trị", ""));
        $("#treatmentRegimenID").append(new Option("Chọn phác đồ điều trị", ""));
        $("#statusOfResidentID").append(new Option("Chọn kết quả xác minh hiện trạng cư trú", ""));
    };

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlUpdateReview + "?oid=" + $scope.item.id + "&tab=" + $scope.tab,
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



