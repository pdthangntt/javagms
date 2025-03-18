/* global loading, app, urPatienLog, pOptions */
app.service('pacPatientService', function ($uibModal, msg) {
    var elm = this;
    elm.logs = function (oid, code, name) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'pacPatientLogs',
            controller: 'pac_patient_log',
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

    /**
     * @author pdThang
     * Chuyển gửi
     * @param {type} oid
     * @returns {undefined}
     */
    elm.transfer = function (oid) {
        loading.show();
        $.ajax({
            url: urlPacTransfer,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacTransfer',
                        controller: 'pac_new_transfer',
                        size: 'xs',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    entity: resp.data.entity,
                                    options: resp.data.options
                                };
                            }
                        }
                    });
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    /**
     * @author TrangBN
     * Chuyển gửi
     * @param {type} oid
     * @returns {undefined}
     */
    elm.transferBddt = function (oid, bddt) {
        loading.show();
        $.ajax({
            url: urlPacTransfer,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacTransfer',
                        controller: 'pac_new_transfer',
                        size: 'xs',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    bddt: bddt,
                                    entity: resp.data.entity,
                                    options: resp.data.options
                                };
                            }
                        }
                    });
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    /**
     * @author pdThang
     * Kiểm tra trùng lắp
     * @param {type} oid\
     * @param currentTarget cơ sở hiện tại
     * @returns {undefined}
     * 
     */
    elm.fillter = function (oid, currentTarget) {
        loading.show();
        $.ajax({
            url: urlFilter,
            data: {oid: oid, currenttab: currentTarget},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacFilter',
                        controller: 'pac_filter',
                        size: 'largeWidth',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    model: resp.data.model,
                                    filter: resp.data.filter,
                                    options: resp.data.options,
                                    connectedID: resp.data.connectedID,
                                    currentTarget: currentTarget
                                };
                            }
                        }
                    });
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    /**
     * Service kết nối thông tin sau khi check trùng
     * 
     * @auth TrangBN
     * @returns {undefined}
     */
    elm.pacConnect = function (oid, tid, uiModal, currentTarget, foundTarget) {
        loading.show();

        $.ajax({
            url: urlConnect,
            data: {oid: oid, tid: tid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {

                    // Check trùng tất cả các thông tin
                    var model = resp.data.model;
                    var target = resp.data.target;

                    if (utils.compareString(model.fullname, target.fullname) && target.fullname !== '' &&
                            model.yearOfBirth === target.yearOfBirth && target.yearOfBirth !== '' &&
                            model.genderID === target.genderID && target.genderID !== '' &&
                            model.identityCard === target.identityCard && target.identityCard !== '' &&
                            model.healthInsuranceNo === target.healthInsuranceNo && target.healthInsuranceNo !== '' &&
                            model.permanentProvinceID === target.permanentProvinceID && target.permanentProvinceID !== '' &&
                            model.permanentDistrictID === target.permanentDistrictID && target.permanentDistrictID !== '' &&
                            model.permanentWardID === target.permanentWardID && target.permanentWardID !== '') {

                        bootbox.confirm(
                                {
                                    message: 'Bạn có muốn kết nối thông tin điều trị của người nhiễm được kiểm tra trùng lắp "' + model.fullname
                                            + '" với người nhiễm đang được điều trị ARV này hay không?',
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
                                            // Thực hiện cập nhật thông tin kết nối
                                            $.ajax({
                                                url: urlUpdateConnected,
                                                data: {oid: oid, tid: tid, current: currentTarget},
                                                method: 'GET',
                                                success: function (resp) {
                                                    if (resp.success) {
                                                        // Confirm chuyển sang quản lý ca bệnh
                                                        if (currentTarget !== "quanly" && foundTarget !== "quanly") {
                                                            if (currentTarget === "bddt") {
                                                                elm.transfer(tid);
                                                            } else {
                                                                elm.transfer(oid);
                                                            }
                                                        }
                                                        uiModal.dismiss('cancel');
                                                    } else {
                                                        msg.error(resp.message);
                                                    }
                                                }
                                            });
                                        } else {
                                            // Load lại danh sách lọc trùng
                                            uiModal.dismiss();
                                        }
                                    }
                                }
                        );

                    } else {
                        $uibModal.open({
                            animation: true,
                            backdrop: 'static',
                            templateUrl: 'pacConnect',
                            controller: 'pac_connect',
                            size: 'small',
                            resolve: {
                                params: function () {
                                    return {
                                        oid: oid,
                                        tid: tid,
                                        model: resp.data.model,
                                        target: resp.data.target,
                                        options: resp.data.options,
                                        uiModals: uiModal,
                                        currentTarget: currentTarget,
                                        foundTarget: foundTarget
                                    };
                                }
                            }
                        });
                    }
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    // Yêu cầu rà soát
    elm.requestReview = function (oid, tid) {

        var oids = [oid];
        $.ajax({
            url: urlGet + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'requestReview',
                        controller: 'request_review',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.entities,
                                    options: resp.data.options,
                                    items: resp.data.entities,
                                    tid: tid  // ID của người bệnh từ nguồn dịch vụ điều trị ARV
                                };
                            }
                        }
                    });
                }
            }
        });
    };

    //Kiểm tra thông tin
    elm.pacPatientUpdate = function (oid) {
        loading.show();
        $.ajax({
            url: urlUpdateGet + "?oid=" + oid,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacPatientUpdate',
                        controller: 'pac_patient_update',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    uiModals: $uibModal,
                                    hivInfoCode: resp.data.hivInfoCode,
                                    location: resp.data.location,
                                    oldData: resp.data.oldData,
                                    newData: resp.data.newData,
                                    options: resp.data.options
                                };
                            }
                        }
                    });
                } else {
                    msg.danger(resp.message);
                }
            }
        });
    };

});


app.controller('pac_patient_new', function ($scope) {
    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.statusOfTreatmentID = utils.getContentOfDefault(form.statusOfTreatmentID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.firstTreatmentTime = utils.getContentOfDefault(form.firstTreatmentTime, '');
    $scope.registrationTime = utils.getContentOfDefault(form.registrationTime, '');
    $scope.endTime = utils.getContentOfDefault(form.endTime, '');
    $scope.startTreatmentTime = utils.getContentOfDefault(form.startTreatmentTime, '');
    $scope.deathTime = utils.getContentOfDefault(form.deathTime, '');
    $scope.requestDeathTime = utils.getContentOfDefault(form.requestDeathTime, '');
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;

    $scope.earlyHivTime = utils.getContentOfDefault(form.earlyHivTime, '');
    $scope.virusLoadConfirmDate = utils.getContentOfDefault(form.virusLoadConfirmDate, '');
    $scope.cdFourResultDate = utils.getContentOfDefault(form.cdFourResultDate, '');
    $scope.virusLoadArvDate = utils.getContentOfDefault(form.virusLoadArvDate, '');
    $scope.clinicalStageDate = utils.getContentOfDefault(form.clinicalStageDate, '');
    $scope.aidsStatusDate = utils.getContentOfDefault(form.aidsStatusDate, '');
    $scope.aidsStatus = utils.getContentOfDefault(form.aidsStatus, '');
    $scope.cdFourResultLastestDate = utils.getContentOfDefault(form.cdFourResultLastestDate, '');
    $scope.virusLoadArvLastestDate = utils.getContentOfDefault(form.virusLoadArvLastestDate, '');
    $scope.statusOfChangeTreatmentID = utils.getContentOfDefault(form.statusOfChangeTreatmentID, '');
    $scope.acceptPermanentTime = utils.getContentOfDefault(form.acceptPermanentTime, '');
    $scope.sourceServiceID = utils.getContentOfDefault(form.sourceServiceID, '');
    $scope.changeTreatmentDate = utils.getContentOfDefault(form.changeTreatmentDate, '');
    $scope.virusLoadNumber = utils.getContentOfDefault(form.virusLoadNumber, '');
    $scope.earlyBioName = utils.getContentOfDefault(form.earlyBioName, '');
    $scope.earlyHiv = utils.getContentOfDefault(form.earlyHiv, '');
    $scope.earlyDiagnose = utils.getContentOfDefault(form.earlyDiagnose, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');

    $scope.items = {
        statusOfChangeTreatmentID: "#statusOfChangeTreatmentID",
        changeTreatmentDate: "#changeTreatmentDate",
        riskBehaviorID: "#riskBehaviorID",
        causeOfDeath: "#causeOfDeath",
        isDisplayCopy: "#isDisplayCopy",
        permanentAddressNo: "#permanentAddressNo",
        permanentAddressGroup: "#permanentAddressGroup",
        permanentAddressStreet: "#permanentAddressStreet",
        permanentProvinceID: "#permanentProvinceID",
        permanentDistrictID: "#permanentDistrictID",
        permanentWardID: "#permanentWardID",
        currentAddressNo: "#currentAddressNo",
        currentAddressGroup: "#currentAddressGroup",
        currentAddressStreet: "#currentAddressStreet",
        currentProvinceID: "#currentProvinceID",
        currentDistrictID: "#currentDistrictID",
        currentWardID: "#currentWardID",
        provinceID: "#provinceID",
        districtID: "#districtID",
        wardID: "#wardID",
        siteConfirmID: "#siteConfirmID",
        bloodBaseID: "#bloodBaseID",
        siteTreatmentFacilityID: "#siteTreatmentFacilityID",
        treatmentRegimenID: "#treatmentRegimenID",
        symptomID: "#symptomID",
        objectGroupID: "#objectGroupID",
        sourceServiceID: "#sourceServiceID",
        opcCode: "#opcCode",
        hasHealthInsurance: "#hasHealthInsurance",
        earlyBioName: "#earlyBioName",
        virusLoadNumber: "#virusLoadNumber",
        earlyDiagnose: "#earlyDiagnose",
        earlyHiv: "#earlyHiv",
        healthInsuranceNo: "#healthInsuranceNo",
        insuranceExpiry: "#insuranceExpiry",
        firstTreatmentTime: "#firstTreatmentTime",
        firstTreatmentRegimenId: "#firstTreatmentRegimenId",
        registrationTime: "#registrationTime",
        registrationType: "#registrationType",
        endTime: "#endTime",
        endCase: "#endCase"

    };

    $scope.init = function () {
        $scope.pOptions = pOptions;
        $scope.$parent.select_mutiple($scope.items.riskBehaviorID, "Chọn hành vi nguy cơ lây nhiễm");
        $scope.$parent.select_mutiple($scope.items.causeOfDeath, "Chọn nguyên nhân tử vong");
        $scope.$parent.select_search($scope.items.siteConfirmID, "Chọn cơ sở XN khẳng định");
        $scope.$parent.select_search($scope.items.bloodBaseID, "Chọn nơi lấy mẫu XN:");
        $scope.$parent.select_search($scope.items.siteTreatmentFacilityID, "Chọn cơ sở điều trị:");
        $scope.$parent.select_search($scope.items.treatmentRegimenID, "Chọn phác đồ điều trị:");
        $scope.$parent.select_search($scope.items.firstTreatmentRegimenId, "Chọn phác đồ điều trị:");
        $scope.$parent.select_search($scope.items.symptomID, "Chọn triệu chứng:");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn đối tượng XN");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
        $scope.$parent.select_search($scope.items.permanentWardID, "");
        $scope.$parent.select_search($scope.items.currentProvinceID, "");
        $scope.$parent.select_search($scope.items.currentDistrictID, "");
        $scope.$parent.select_search($scope.items.currentWardID, "");
        $scope.$parent.select_search($scope.items.earlyBioName, "");

        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);
        $scope.initProvince($scope.items.provinceID, $scope.items.districtID, $scope.items.wardID);


        if ($scope.id != "" && $($scope.items.isDisplayCopy).val() == "") {
            $scope.isCopyPermanentAddress = false;
            $($scope.items.isDisplayCopy).val(false);
        }

        $($scope.items.opcCode).change(function () {
            if ($($scope.items.opcCode).val() != '') {
                setTimeout(function () {
                    $($scope.items.opcCode).val($($scope.items.opcCode).val().toUpperCase()).change();
                }, 10);
            }
        });

        $("#cdFourResultCurrent, #cdFourResultLastestDate, #clinicalStageDate").on('blur', function () {
            $scope.aidsChange();
        });

        $("#clinicalStage").on('change', function () {
            $scope.aidsChange();
        });

        $("#aidsStatus").on('change', function () {
            if ($("#aidsStatus").val() === "1") {
                $("#aidsStatusDate").val("").change();
            }
        });

        $($scope.items.statusOfChangeTreatmentID).on('change', function () {
            if ($($scope.items.statusOfChangeTreatmentID).val() === "") {
                $($scope.items.changeTreatmentDate).val("").change();
            }
        });

        if ($scope.acceptPermanentTime !== '') {
            $($scope.items.permanentProvinceID).attr({disabled: "disable"})
        }

        $("#virusLoadNumber").on('change', function () {
            if ($scope.virusLoadNumber > 0 && $scope.virusLoadNumber < 200) {
                $("#virusLoadConfirm").val('2').change();
            } else if ($scope.virusLoadNumber >= 200 && $scope.virusLoadNumber <= 1000) {
                $("#virusLoadConfirm").val('3').change();
            } else if ($scope.virusLoadNumber > 1000) {
                $("#virusLoadConfirm").val('4').change();
            } else {
                $("#virusLoadConfirm").val('').change();
            }
        });

        // Ràng buộc kết quả XN tải lượng virus
//        if ($("#virusLoadConfirm").val() === "5") {
//            $("#virusLoadConfirmDate").attr({disabled: "disable"}).val('').change();
//        } else {
//            $("#virusLoadConfirmDate").removeAttr("disabled").val('').change();
//        }
//
//        $("#virusLoadConfirm").on('change', function () {
//            if ($("#virusLoadConfirm").val() === "5") {
//                $("#virusLoadConfirmDate").attr({disabled: "disable"}).val('').change();
//            } else {
//                $("#virusLoadConfirmDate").removeAttr("disabled").val('').change();
//            }
//        });

        // Ràng buộc kết quả XN mới nhiễm
//        if ($("#earlyHiv").val() === "3") {
//            $("#earlyHivTime").attr({disabled: "disable"}).val('').change();
//        } else {
//            $("#earlyHivTime").removeAttr("disabled").val('').change();
//        }
//
//        $("#earlyHiv").on('change', function () {
//            if ($("#earlyHiv").val() === "3") {
//                $("#earlyHivTime").attr({disabled: "disable"}).val('').change();
//            } else {
//                $("#earlyHivTime").removeAttr("disabled").val('').change();
//            }
//        });

        // Ràng buộc kết quả XN tải lượng virus lần đầu tiên
        if ($("#virusLoadArv").val() === "5") {
            $("#virusLoadArvDate").attr({disabled: "disable"}).val('').change();
        } else {
            $("#virusLoadArvDate").removeAttr("disabled").val('').change();
        }

        $("#virusLoadArv").on('change', function () {
            if ($("#virusLoadArv").val() === "5") {
                $("#virusLoadArvDate").attr({disabled: "disable"}).val('').change();
            } else {
                $("#virusLoadArvDate").removeAttr("disabled").val('').change();
            }
        });

        // Ràng buộc KQ XN tải lượng virus hiện tại
        if ($("#virusLoadArvCurrent").val() === "5") {
            $("#virusLoadArvLastestDate").attr({disabled: "disable"}).val('').change();
        } else {
            $("#virusLoadArvLastestDate").removeAttr("disabled").val('').change();
        }

        $("#virusLoadArvCurrent").on('change', function () {
            if ($("#virusLoadArvCurrent").val() === "5") {
                $("#virusLoadArvLastestDate").attr({disabled: "disable"}).val('').change();
            } else {
                $("#virusLoadArvLastestDate").removeAttr("disabled").val('').change();
            }
        });

        // Kiểm tra nếu cập nhật không cho update nguồn dịch vụ
        if ($scope.id !== "") {
            $($scope.items.sourceServiceID).attr({disabled: "disable"});
        } else {
            $($scope.items.sourceServiceID).removeAttr("disabled");
        }

        if ($($scope.items.hasHealthInsurance).val() === "1") {
            $($scope.items.healthInsuranceNo).removeAttr("disabled");
            $($scope.items.insuranceExpiry).removeAttr("disabled");
        } else {
            $($scope.items.healthInsuranceNo).val("");
            $($scope.items.healthInsuranceNo).attr("disabled", "disabled");
            $($scope.items.insuranceExpiry).val("");
            $($scope.items.insuranceExpiry).attr("disabled", "disabled");
        }
    };

    $scope.aidsChange = function () {
        var cd4Value = $("#cdFourResultCurrent").val();
        var stageValue = $("#clinicalStage").val();

        var cd4TimeValue = $("#cdFourResultLastestDate").val() === "dd/mm/yyyy" ? "" : $("#cdFourResultLastestDate").val();
        var stageTimeValue = $("#clinicalStageDate").val() === "dd/mm/yyyy" ? "" : $("#clinicalStageDate").val();


        if ((cd4Value !== "" && cd4Value >= 0 && cd4Value < 250) || (cd4Value === "" && (stageValue === "3" || stageValue === "4"))) {
            $("#aidsStatus").val("2").change();
            $("#aidsStatusDate").val(stageTimeValue > cd4TimeValue ? stageTimeValue : cd4TimeValue).change();
        } else {
            $("#aidsStatus").val("1").change();
            $("#aidsStatusDate").val("").change();
        }
        if (stageValue == "" && cd4Value == "") {
            $("#aidsStatus").val("").change();
        }
        if (cd4TimeValue != "" && stageTimeValue == "") {
            $("#aidsStatusDate").val(cd4TimeValue).change();
        }
        if (cd4TimeValue == "" && stageTimeValue != "") {
            $("#aidsStatusDate").val(stageTimeValue).change();
        }

    };

    $scope.copyAddress = function () {
        $scope.isCopyPermanentAddress = !$scope.isCopyPermanentAddress;
        if (!$scope.isCopyPermanentAddress) {
            $($scope.items.currentAddressNo).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressGroup).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressStreet).removeAttr("disabled").val('').change();
            $($scope.items.currentProvinceID).removeAttr("disabled").val('').change();
            $($scope.items.currentDistrictID).removeAttr("disabled").val('').change();
            $($scope.items.currentWardID).removeAttr("disabled").val('').change();
            $($scope.items.isDisplayCopy).val(false);
        } else {
            $($scope.items.currentAddressNo).attr({disabled: "disable"}).val($($scope.items.permanentAddressNo).val()).change();
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($($scope.items.permanentAddressGroup).val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($($scope.items.permanentAddressStreet).val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($($scope.items.permanentProvinceID).val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($($scope.items.permanentDistrictID).val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($($scope.items.permanentWardID).val()).change();
            $($scope.items.isDisplayCopy).val(true);
        }
    };

    $("#causeOfDeath").change(
            function () {
                if ($("#causeOfDeath").val() !== null && $("#causeOfDeath").val().toString().charAt(0) === ',') {
                    $("#causeOfDeath option[value='']").removeAttr("selected");
                }
            }
    );
    $("#riskBehaviorID").change(
            function () {
                if ($("#riskBehaviorID").val() !== null && $("#riskBehaviorID").val().toString().charAt(0) === ',') {
                    $("#riskBehaviorID option[value='']").removeAttr("selected");
                }
            }
    );

    $("#hasHealthInsurance").change(
            function () {
                if ($($scope.items.hasHealthInsurance).val() === "1") {
                    $($scope.items.healthInsuranceNo).removeAttr("disabled");
                    $($scope.items.insuranceExpiry).removeAttr("disabled");
                } else {
                    $($scope.items.healthInsuranceNo).val("");
                    $($scope.items.healthInsuranceNo).attr("disabled", "disabled");
                    $($scope.items.insuranceExpiry).val("");
                    $($scope.items.insuranceExpiry).attr("disabled", "disabled");
                }
            }
    );

    $("#healthInsuranceNo").on("blur", function () {
        $($scope.items.healthInsuranceNo).val($($scope.items.healthInsuranceNo).val().toUpperCase());
    });

});


app.controller('pqm_vct_update', function ($scope, msg) {
    $scope.scanUrl = scanUrl;

    $scope.earlyHivDate = utils.getContentOfDefault(form.earlyHivDate, '');
    $scope.registerTherapyTime = utils.getContentOfDefault(form.registerTherapyTime, '');
    $scope.exchangeTime = utils.getContentOfDefault(form.exchangeTime, '');

    var keyW = form.earlyHivDate == null || form.earlyHivDate == '' || form.earlyDiagnose == null || earlyDiagnose == '';
    var keyExchangeTime = form.exchangeTime == null || form.exchangeTime == '';


    $scope.items = {
//        statusOfChangeTreatmentID: "#statusOfChangeTreatmentID",

    };

    $scope.init = function () {
        $scope.pOptions = pOptions;
        $("#scan").hide();
    };

    $scope.actionScan = function (ID, identityCard) {
        var item = {};
        $.ajax({
            url: scanUrl + "?oid=" + ID,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {

                    msg.success("Đã thực hiện lấy dữ liệu thành công.");
                    $("#registerTherapyTime").val(resp.data.registerTherapyTime).change();
                    $("#registeredTherapySite").val(resp.data.registeredTherapySite).change();
                    $("#therapyNo").val(resp.data.therapyNo).change();

                    if (keyW) {
                        $("#earlyHivDate").val(resp.data.earlyHivDate).change();
                        $("#earlyDiagnose").val(resp.data.earlyDiagnose).change();
                    }
                    if (keyExchangeTime) {
                        $("#exchangeTime").val(resp.data.exchangeTime).change();
                    }
                    $("#scan").show();
                    $("#registerTherapyTime_view").html(resp.data.registerTherapyTime);
                    $("#registeredTherapySite_view").html(resp.data.registeredTherapySite);
                    $("#therapyNo_view").html(resp.data.therapyNo);
                    $("#exchangeTime_view").html(resp.data.exchangeTime);
                    $("#earlyDiagnose_view").html(resp.data.earlyDiagnose == '1' ? 'Nhiễm mới' : resp.data.earlyDiagnose == '2' ? 'Nhiễm lâu' : '');
                    $("#earlyHivDate_view").html(resp.data.earlyHivDate);
                } else {
                    msg.danger(resp.message);
                }
            }
        });



    };


});

app.controller('pac_new_index', function ($scope, $uibModal, pacPatientService, msg) {
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
        $scope.select_search("#status", "Tất cả");
        $scope.select_search("#gender_id", "Tất cả");
        $scope.select_search("#blood_base", "Tất cả");
        $scope.select_search("#service", "Tất cả");
        $scope.$parent.select_search($scope.items.detect_province_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_district_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_ward_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_province_id, "Tất cả");
        $scope.select_search("#siteConfirmID", "Tất cả");
        $scope.select_search("#siteTreatmentFacilityID", "Tất cả");
        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
        //        $scope.initProvince(null, "#permanent_district_id", "#permanent_ward_id"); // Có quận huyện/phường xã. Dùng cho tìm kiếm tab Phát hiện trong tỉnh và Tỉnh khác phát hiện

        if ($("#permanent_province_id").val() === '' && isVAAC) {
            $("#actionButton").hide();
        } else {
            $("#actionButton").show();
        }

        $("#permanent_province_id").on("change", function () {
            if ($("#permanent_province_id").val() === '') {
                $("#actionButton").hide();
            } else {
                $("#actionButton").show();
            }
        });

    };

    $scope.logs = function (oid, name) {
        pacPatientService.logs(oid, name);
    };
    $scope.actionTransfer = function (oid) {
        pacPatientService.transfer(oid);
    };

    /**
     * Kiểm tra trùng lặp
     * 
     * pdthang
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionFillter = function (oid) {
        pacPatientService.fillter(oid);
    };

    /**
     * Yêu cầu ra soát
     * 
     * @author DSNAnh
     * @returns {Boolean}
     */
    $scope.requestReview = function (tid) {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để yêu cầu rà soát");
            return false;
        }
        loading.show();
        pacPatientService.requestReview(oids, tid);
    };

    /**
     * Chuyển gửi tỉnh khác
     * 
     * @author DSNAnh
     * @returns {Boolean}
     */
    $scope.transferOtherProvince = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi cần chuyển gửi tỉnh khác.");
            return false;
        }
        loading.show();
        $.ajax({
            url: urlGet + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    if (resp.data.dataTransfer.length == 0) {
                        msg.warning("Khách hàng đã được chuyển gửi");
                    } else {
                        $uibModal.open({
                            animation: true,
                            backdrop: 'static',
                            templateUrl: 'transferOtherProvince',
                            controller: 'transfer_other_province',
                            size: 'lg',
                            resolve: {
                                params: function () {
                                    return {
                                        item: resp.data.dataTransfer,
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
});


app.controller('pac_new_vaac_index', function ($scope, $uibModal, pacPatientService, msg) {
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
        $scope.select_search("#status", "Tất cả");
        $scope.select_search("#gender_id", "Tất cả");
        $scope.select_search("#blood_base", "Tất cả");
        $scope.select_search("#service", "Tất cả");
        $scope.$parent.select_search($scope.items.detect_province_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_district_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_ward_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_province_id, "Tất cả");
        $scope.select_search("#siteConfirmID", "Tất cả");
        $scope.select_search("#siteTreatmentFacilityID", "Tất cả");
        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
        //        $scope.initProvince(null, "#permanent_district_id", "#permanent_ward_id"); // Có quận huyện/phường xã. Dùng cho tìm kiếm tab Phát hiện trong tỉnh và Tỉnh khác phát hiện

        if ($("#permanent_province_id").val() === '' && isVAAC) {
            $("#actionButton").hide();
        } else {
            $("#actionButton").show();
        }

        $("#permanent_province_id").on("change", function () {
            if ($("#permanent_province_id").val() === '') {
                $("#actionButton").hide();
            } else {
                $("#actionButton").show();
            }
        });

    };

    $scope.logs = function (oid, name) {
        pacPatientService.logs(oid, name);
    };
    $scope.actionTransfer = function (oid) {
        pacPatientService.transfer(oid);
    };

    /**
     * Kiểm tra trùng lặp
     * 
     * pdthang
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionFillter = function (oid) {
        pacPatientService.fillter(oid);
    };

    /**
     * Yêu cầu ra soát
     * 
     * @author DSNAnh
     * @returns {Boolean}
     */
    $scope.requestReview = function (tid) {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để yêu cầu rà soát");
            return false;
        }
        loading.show();
        pacPatientService.requestReview(oids, tid);
    };

    /**
     * Chuyển gửi tỉnh khác
     * 
     * @author DSNAnh
     * @returns {Boolean}
     */
    $scope.transferOtherProvince = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi cần chuyển gửi tỉnh khác.");
            return false;
        }
        loading.show();
        $.ajax({
            url: urlGet + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    if (resp.data.dataTransfer.length == 0) {
                        msg.warning("Khách hàng đã được chuyển gửi");
                    } else {
                        $uibModal.open({
                            animation: true,
                            backdrop: 'static',
                            templateUrl: 'transferOtherProvince',
                            controller: 'transfer_other_province',
                            size: 'lg',
                            resolve: {
                                params: function () {
                                    return {
                                        item: resp.data.dataTransfer,
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

    $scope.updateNewVaac = function (oid) {
        loading.show();
        $.ajax({
            url: urlPacNewVaacGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'updateNewVaac',
                        controller: 'update_new_vaac',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
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


});

app.controller('update_new_vaac', function ($scope, $uibModalInstance, params, msg) {
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
            url: urlPacUpdateReview + "?oid=" + $scope.item.id,
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
 * @author pdThang
 * Kiểm tra trùng lắp
 * @returns 
 */
app.controller('pac_filter', function ($scope, $uibModalInstance, params, pacPatientService, msg) {
    $scope.options = params.options;
    $scope.model = params.model;
    $scope.filter = params.filter;
    $scope.currentTarget = params.currentTarget;
    $scope.connectedID = params.connectedID;

    $scope.view = function (oid) {
        for (var i = 0; i < $scope.filter.length; i++) {
            var item = $scope.filter[i];
            if (item.id == oid) {
                if (item.acceptTime != null && item.reviewWardTime == null && item.reviewProvinceTime == null) {
                    window.open(urlReviewView.replace("0", item.id), '_blank');
                } else if (item.acceptTime != null && item.reviewWardTime != null && item.reviewProvinceTime == null) {
                    window.open(urlAcceptView.replace("0", item.id), '_blank');
                } else if (item.acceptTime != null && item.reviewWardTime != null && item.reviewProvinceTime != null) {
                    window.open(urlPatientView.replace("0", item.id), '_blank');
                } else if (item.acceptTime == null && item.reviewWardTime == null && item.reviewProvinceTime == null && item.sourceServiceID != "103") {
                    window.open(urlNewView.replace("0", item.id), '_blank');
                } else if (item.sourceServiceID == "103") {
                    window.open(urlOpcView.replace("0", item.id), '_blank');
                }
            }
        }
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.connect = function (oid, tid, bddt, foundTarget) {
        pacPatientService.pacConnect(oid, tid, $uibModalInstance, $scope.currentTarget !== '' ? $scope.currentTarget : bddt, foundTarget);
    };

    $scope.deleteConfirm = function (oid) {
        bootbox.confirm({
            message: "Bạn chắc chắn muốn xóa người nhiễm này ?",
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
                    window.location.href = urlDelete + oid;
                    window.onpopstate = null;
                } else {
                    history.pushState(null, null, null);
                }
            }
        });
    };
});

app.controller('request_review', function ($scope, $uibModalInstance, params, msg) {
    $scope.items = params.item;
    $scope.options = params.options;
    $scope.tid = params.tid;

    $scope.ok = function () {
        var oids = [];
        for (var i = 0; i < $scope.items.length; i++) {
            oids.push($scope.items[i].id);
        }
        $.ajax({
            url: urlRequest + "?oid=" + oids.join(",") + "&tid=" + $scope.tid,
            success: function (resp) {
                if (resp.success) {
                    msg.success("Người nhiễm phát hiện đã chọn được chuyển sang chờ rà soát thành công", function () {
                        location.reload();
                    }, 2000);
                }
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

//Kiểm tra thông tin
app.controller('pac_patient_update', function ($scope, $uibModalInstance, params, msg) {
    $scope.uiModals = params.uiModals;
    $scope.oldData = params.oldData;
    $scope.newData = params.newData;
    $scope.options = params.options;
    $scope.location = params.location;
    $scope.hivInfoCode = params.hivInfoCode;
    var caueOfDeathsOld = [];
    var caueOfDeathsNew = [];
    var caueOfDeathOld = '';
    var caueOfDeathNew = '';

    for (var s in params.newData.causeOfDeath) {
        caueOfDeathsNew.push(params.options['cause-of-death'][params.newData.causeOfDeath[s]]);
    }
    caueOfDeathNew = caueOfDeathsNew.join(',');

    for (var s in params.oldData.causeOfDeath) {
        caueOfDeathsOld.push(params.options['cause-of-death'][params.oldData.causeOfDeath[s]]);
    }
    caueOfDeathOld = caueOfDeathsOld.join(',');
    $scope.model = {
        confirmTimeOld: utils.timestampToStringDate(params.oldData.confirmTime == null || params.oldData.confirmTime == '' || params.oldData.confirmTime == 0 ? '' : params.oldData.confirmTime),
        startTreatmentTimeOld: utils.timestampToStringDate(params.oldData.startTreatmentTime == null || params.oldData.startTreatmentTime == '' || params.oldData.startTreatmentTime == 0 ? '' : params.oldData.startTreatmentTime),
        deathTimeOld: utils.timestampToStringDate(params.oldData.deathTime == null || params.oldData.deathTime == '' || params.oldData.deathTime == 0 ? '' : params.oldData.deathTime),
        causeOfDeathOld: caueOfDeathOld == null ? '' : caueOfDeathOld,
        caueOfDeathNew: caueOfDeathNew == null ? '' : caueOfDeathNew,
        confirmTimeNew: utils.timestampToStringDate(params.newData.confirmTime == null || params.newData.confirmTime == '' || params.newData.confirmTime == 0 ? '' : params.newData.confirmTime),
        startTreatmentTimeNew: utils.timestampToStringDate(params.newData.startTreatmentTime == null || params.newData.startTreatmentTime == '' || params.newData.startTreatmentTime == 0 ? '' : params.newData.startTreatmentTime),
        deathTimeNew: utils.timestampToStringDate(params.newData.deathTime == null || params.newData.deathTime == '' || params.newData.deathTime == 0 ? '' : params.newData.deathTime),
        requestDeathTimeNew: utils.timestampToStringDate(params.newData.requestDeathTime == null || params.newData.requestDeathTime == '' || params.newData.requestDeathTime == 0 ? '' : params.newData.requestDeathTime),
        requestDeathTimeOld: utils.timestampToStringDate(params.oldData.requestDeathTime == null || params.oldData.requestDeathTime == '' || params.oldData.requestDeathTime == 0 ? '' : params.oldData.requestDeathTime),

    };
    $scope.ok = function (oid) {

        $.ajax({
            url: urlPatientUpdate + "?oid=" + oid,
            success: function (resp) {
                if (resp.success) {
                    msg.success(resp.message, function () {
                        location.reload();
                    }, 2000);
                } else {
                    msg.danger(resp.message);
                }
            }
        });
    };

    $scope.cancel = function (oid) {
        $uibModalInstance.dismiss('cancel');
        loading.show();
        $.ajax({
            url: urlUpdateCancelGet + "?oid=" + oid,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.uiModals.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pacPatientUpdateCancel',
                        controller: 'pac_patient_update_cancel',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.entity,
                                    options: resp.data.options
                                };
                            }
                        }
                    });
                } else {
                    msg.danger(resp.message);
                }
            }
        });
    };

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

//Hủy yêu cầu cập nhật thông tin
app.controller('pac_patient_update_cancel', function ($scope, $uibModalInstance, params, msg) {
    $scope.item = params.item;
    $scope.options = params.options;
    $scope.content = '';

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlUpdateCancel + "?oid=" + $scope.item.id,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.content),
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

app.controller('transfer_other_province', function ($scope, $uibModalInstance, params, msg) {
    $scope.items = params.item;
    $scope.options = params.options;

    $scope.ok = function () {
        //lấy ids bên trong items
        var oids = [];
        for (var i = 0; i < $scope.items.length; i++) {
            oids.push($scope.items[i].id);
        }
        $.ajax({
            url: urlTransferProvince + "?oid=" + oids.join(","),
            success: function (resp) {
                if (resp.success) {
                    msg.success("Người nhiễm đã được chuyển sang tỉnh khác thành công", function () {
                        location.reload();
                    }, 2000);
                }
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});


app.controller('pac_patient_view', function ($scope) {
    $scope.pOptions = pOptions;

    $scope.items = {
        riskBehaviorID: "#riskBehaviorID",
        causeOfDeath: "#causeOfDeath",
        permanentAddressNo: "#permanentAddressNo",
        permanentAddressGroup: "#permanentAddressGroup",
        permanentAddressStreet: "#permanentAddressStreet",
        permanentProvinceID: "#permanentProvinceID",
        permanentDistrictID: "#permanentDistrictID",
        permanentWardID: "#permanentWardID",
        currentAddressNo: "#currentAddressNo",
        currentAddressGroup: "#currentAddressGroup",
        currentAddressStreet: "#currentAddressStreet",
        currentProvinceID: "#currentProvinceID",
        currentDistrictID: "#currentDistrictID",
        currentWardID: "#currentWardID",
        siteConfirmID: "#siteConfirmID",
        bloodBaseID: "#bloodBaseID",
        siteTreatmentFacilityID: "#siteTreatmentFacilityID",
        treatmentRegimenID: "#treatmentRegimenID",
        symptomID: "#symptomID",
        statusOfResidentID: "#statusOfResidentID",
        statusOfTreatmentID: "#statusOfTreatmentID",
        deathTime: "#deathTime",
        note: "#note",
        sourceServiceID: "#sourceServiceID"

    };

    $scope.init = function () {
        $scope.pOptions = pOptions;
        $scope.$parent.select_mutiple($scope.items.riskBehaviorID, "Chọn hành vi nguy cơ lây nhiễm");

        $scope.$parent.select_mutiple($scope.items.causeOfDeath, "Chọn nguyên nhân tử vong");

        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);
        $("#permanentProvinceID").attr("disabled", "disabled");
        $("#causeOfDeath").attr("disabled", "disabled");
        $("#permanentDistrictID").attr("disabled", "disabled");
        $("#permanentWardID").attr("disabled", "disabled");
        $("#currentProvinceID").attr("disabled", "disabled");
        $("#currentDistrictID").attr("disabled", "disabled");
        $("#currentWardID").attr("disabled", "disabled");
        $("#riskBehaviorID").attr("disabled", "disabled");
        $("#permanentAddressNo").attr("disabled", "disabled");
        $("#permanentAddressGroup").attr("disabled", "disabled");
        $("#permanentAddressStreet").attr("disabled", "disabled");
        $("#currentAddressNo").attr("disabled", "disabled");
        $("#currentAddressGroup").attr("disabled", "disabled");
        $("#currentAddressStreet").attr("disabled", "disabled");
        $("#note").attr("disabled", "disabled");
    };
});

app.controller('pac_patient_log', function ($scope, $uibModalInstance, params) {
    $scope.model = {staffID: 0, patientID: params.oid, code: params.code, name: params.name};
    loading.show();
    $scope.list = function () {
        $.ajax({
            url: urPatienLog,
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
        if ($scope.model.content === undefined || $scope.model.content === '') {
            return false;
        }
        loading.show();
        $.ajax({
            url: urlPatienLogCreate,
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
                        bootbox.alert(resp.message);
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


/**
 * @author pdThang
 * Chuyển gửi
 * @returns 
 */
app.controller('pac_new_transfer', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.entity = params.entity;
    $scope.bddt = params.bddt;
    $scope.statusOfResidentID = utils.getContentOfDefault(params.entity.statusOfResidentID, '');

    $scope.ok = function () {
        loading.show();
        console.log($scope.statusOfResidentID);
        $.ajax({
            url: urlPacDoTransfer + "?oid=" + $scope.entity.id + "&src=" + $scope.bddt + "&stt=" + $scope.statusOfResidentID,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.entity),
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

app.controller('pac_new_export', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.deathTimeFrom = $.getQueryParameters().death_time_from;
        $scope.deathTimeTo = $.getQueryParameters().death_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;

        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(f.addressFilter, '');
        $scope.ageFilter = utils.getContentOfDefault(f.ageFilter, '');
        $scope.dateFilter = utils.getContentOfDefault(f.dateFilter, '');
        $scope.select_search("#blood", "Tất cả");

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

        $("#ngayxn").click(function () {
            $("#fromTime").text("Ngày xét nghiệm từ");
            $("#toTime").text("Ngày xét nghiệm đến");
        });

        $("#ngayquanly").click(function () {
            $("#fromTime").text("Ngày quản lý từ");
            $("#toTime").text("Ngày quản lý đến");
        });
        $("#ngaytuvong").click(function () {
            $("#fromTime").text("Ngày tử vong từ");
            $("#toTime").text("Ngày tử vong đến");
        });


//        if (f.type == 102) {
//            $("#permanent_district_id").attr("disabled", 'disabled');
//            $("#permanent_district_id").val(f.districtID).change();
//            if (f.searchWard != null) {
//                $("#permanent_ward_id").val(f.search.wIDs).change();
//            }
//        }
//        if (f.type == 103) {
//            $("#permanent_district_id").attr("disabled", 'disabled');
//            $("#permanent_ward_id").attr("disabled", 'disabled');
//            $("#permanent_district_id").val(f.districtID).change();
//            $("#permanent_ward_id").val(f.wardID).change();
//        }

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

        if (!f.isVAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text(" chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text(" cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text(" đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text(" được quản lý");
            }
            if ($("#manage_status").val() === "-1") {
                $(".titleTotal").text(":");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTotal").text(" chưa rà soát:");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTotal").text(" cần rà soát:");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTotal").text(" đã rà soát:");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTotal").text(" được quản lý:");
            }
        }

        $("#manage_status").on('change', function () {

            if (!f.isVAAC) {
                if ($("#manage_status").val() === "-1") {
                    $(".titleTable").text("");
                }
                if ($("#manage_status").val() === "1") {
                    $(".titleTable").text(" chưa rà soát");
                }
                if ($("#manage_status").val() === "2") {
                    $(".titleTable").text(" cần rà soát");
                }
                if ($("#manage_status").val() === "3") {
                    $(".titleTable").text(" đã rà soát");
                }
                if ($("#manage_status").val() === "4") {
                    $(".titleTable").text(" được quản lý");
                }
                if ($("#manage_status").val() === "-1") {
                    $(".titleTotal").text(":");
                }
                if ($("#manage_status").val() === "1") {
                    $(".titleTotal").text(" chưa rà soát:");
                }
                if ($("#manage_status").val() === "2") {
                    $(".titleTotal").text(" cần rà soát:");
                }
                if ($("#manage_status").val() === "3") {
                    $(".titleTotal").text(" đã rà soát:");
                }
                if ($("#manage_status").val() === "4") {
                    $(".titleTotal").text(" được quản lý:");
                }
            }
        });

    };

    $scope.initInput = function () {

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.deathTimeFrom = $.getQueryParameters().death_time_from;
        $scope.deathTimeTo = $.getQueryParameters().death_time_to;
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.input_time_from = $.getQueryParameters().input_time_from;
        $scope.input_time_to = $.getQueryParameters().input_time_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

        $scope.select_mutiple("#permanent_province_id", "Tất cả");
        $scope.select_mutiple("#permanent_district_id", "Tất cả");
        $scope.select_mutiple("#permanent_ward_id", "Tất cả");
        $scope.select_mutiple("#test_object", "Tất cả");
        $scope.select_mutiple("#race", "Tất cả");
        $scope.select_mutiple("#gender", "Tất cả");
        $scope.select_mutiple("#transmision", "Tất cả");
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_mutiple("#status_treatment", "Tất cả");
        $scope.select_mutiple("#status_resident", "Tất cả");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (f.searchProvince !== '' && f.searchProvince !== null) {
            $("#permanent_province_id").val(f.searchProvince.split(",")).change();
        }
        if (f.searchDistrict !== '') {
            $("#permanent_district_id").val(f.searchDistrict.split(",")).change();
        }
        if (f.searchWard !== '') {
            $("#permanent_ward_id").val(f.searchWard.split(",")).change();
        }
        if (f.testObject !== '') {
            $("#test_object").val(f.testObject.split(",")).change();
        }
        if (f.manageStatus !== '') {
            $("#manage_status").val(f.manageStatus).change();
        }
        if (f.race !== '') {
            $("#race").val(f.race.split(",")).change();
        }
        if (f.gender !== '') {
            $("#gender").val(f.gender.split(",")).change();
        }
        if (f.transmision !== '') {
            $("#transmision").val(f.transmision.split(",")).change();
        }
        if (f.alive !== '') {
            $("#status_alive").val(f.alive.split(",")).change();
        }
        if (f.treatment !== '') {
            $("#status_treatment").val(f.treatment.split(",")).change();
        }
        if (f.resident !== '') {
            $("#status_resident").val(f.resident.split(",")).change();
        }

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

/**
 * Controler kết nối ca nhiễm phát hiện với người đang điều trị
 * 
 * @auth TrangBN
 */
app.controller('pac_connect', function ($scope, $uibModalInstance, pacPatientService, params, msg) {

    $scope.options = params.options;
    $scope.model = params.model;
    $scope.target = params.target;
    $scope.oid = params.oid;
    $scope.tid = params.tid;
    $scope.uiModals = params.uiModals;
    $scope.currentTarget = params.currentTarget;
    $scope.foundTarget = params.foundTarget;

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.connect = function (optionsUpdate) {

        if (typeof (optionsUpdate) === 'undefined' || optionsUpdate === null) {
            msg.warning("Bạn cần lựa chọn thông tin để kết nối");
            return false;
        }

        // Thực hiện cập nhật thông tin kết nối nguồn dịch vụ ARV
        $.ajax({
            url: urlUpdateConnectedArv,
            data: {option_update: optionsUpdate, oid: $scope.oid, tid: $scope.tid, current: $scope.currentTarget},
            method: 'GET',
            success: function (resp) {
                if (resp.success) {
                    // Confirm chuyển sang quản lý ca bệnh
                    $uibModalInstance.dismiss('cancel');
                    $scope.uiModals.dismiss('cancel');
                    if ($scope.currentTarget !== "quanly" && $scope.foundTarget !== "quanly") {
                        if ($scope.currentTarget === "bddt") {
                            pacPatientService.transfer($scope.tid);
                        } else {
                            pacPatientService.transfer($scope.oid);
                        }

                    }
                } else {
                    msg.error(resp.message);
                }
            }
        });
    };

    $scope.request = function () {
        $uibModalInstance.dismiss('cancel');
        $scope.uiModals.dismiss('cancel');
        pacPatientService.requestReview($scope.oid, $scope.tid);
    };

});

app.controller('pac_patient_manager_new', function ($scope) {
    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.createAT = utils.getContentOfDefault(form.createAT, '');
    $scope.updateAt = utils.getContentOfDefault(form.updateAt, '');
    $scope.reviewProvinceTimeForm = utils.getContentOfDefault(form.reviewProvinceTimeForm, '');

    $scope.init = function () {
        $scope.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        $scope.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");
        $scope.initProvince("#provinceID", "#districtID", "#wardID");
    };
});


