/* global bootbox, app, urlSampleSentTest, loading, urLog, urlLogCreate, utils, urlSendConfirm */
app.service('opcService', function ($uibModal, msg) {
    var elm = this;
    elm.logs = function (stageid, arvid, action, code) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'stageLogs',
            controller: 'stage_log',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        arvid: arvid,
                        stageid: stageid,
                        action: action,
                        code: code
                    };
                }
            }
        });
    };
});

app.controller('stage_log', function ($scope, $uibModalInstance, params, msg) {
    $scope.model = {staffID: 0, arvID: params.arvid, stageID: params.stageid, code: params.code};
    loading.show();
    $scope.list = function () {
        $.ajax({
            url: urlLog,
            data: {arvid: params.arvid, stageid: params.stageid, action: params.action},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        for (var i = 0; i < resp.data.logs.length; i++) {
                            resp.data.logs[i].staffID = typeof resp.data.staffs[resp.data.logs[i].staffID] == undefined ? 'Hệ thống' : resp.data.staffs[resp.data.logs[i].staffID];
                        }
                        $scope.logs = resp.data.logs;
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
            url: urlStageLogCreate,
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

/**
 *  Sửa thông tin điều trị
 */
app.controller('opc_stage', function ($scope, msg, $uibModal, opcService) {

    $scope.arv = arv;
    $scope.form = form;
    $scope.init = function () {
        $scope.switchConfig();
    };

    // Màn hình thêm mới
    $scope.addStage = () => {
        $.ajax({
            url: '/service/opc-stage/new-validate.json',
            data: {arvid: $scope.arv.id},
            method: 'GET',
            success: function (resp) {
                if (resp.success) {
                    window.location.href = "/backend/opc-stage/new.html?arvid=" + $scope.arv.id;
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    $scope.viewStageEvt = () => {
        let updatedStage = $("input:checked").val();
        let rs = updatedStage.split("-", -1);
        $scope.viewStage(rs[0], rs[1], "view");
    }

    $scope.endTreatment = () => {
        let updatedStage = $("input:checked").val();
        let rs = updatedStage.split("-", -1);
        $scope.updateStage(rs[0], rs[1], "update");
    }

    $scope.viewHistoryStageEvt = () => {
        let updatedStage = $("input:checked").val();
        let rs = updatedStage.split("-", -1);
        opcService.logs(rs[0], rs[1], "log", $scope.arv.code);
    }

    // Màn hình cập nhật
    $scope.updateStage = function (stageID, arvID, action) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {arvid: arvID, stageid: stageID, action: action},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'opcStage',
                        controller: 'opc_new_stage',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    model: resp.data.form,
                                    select_search: $scope.$parent.select_search,
                                    action: "update"
                                };
                            }
                        }
                    });
                } else {
                    msg.danger(resp);
                }
            }
        });
    };
});

// Thêm mới sửa xem thông tin điều trị
app.controller('opc_stage_new_pop', function ($scope, msg, localStorageService) {

    $scope.action = $.getQueryParameters().action;
    $scope.arv = arv;
    $scope.item = form;
    $scope.options = options;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.qualifiedTreatmentTime = utils.getContentOfDefault(form.qualifiedTreatmentTime, '');
    $scope.treatmentTime = utils.getContentOfDefault(form.treatmentTime, '');
    $scope.registrationTime = utils.getContentOfDefault(form.registrationTime, '');
    $scope.regimenStageDate = utils.getContentOfDefault(form.regimenStageDate, '');
    $scope.regimenDate = utils.getContentOfDefault(form.regimenDate, '');
    $scope.lastExaminationTime = utils.getContentOfDefault(form.lastExaminationTime, '');
    $scope.appointmentTime = utils.getContentOfDefault(form.appointmentTime, '');
    $scope.supplyMedicinceDate = utils.getContentOfDefault(form.supplyMedicinceDate, '');
    $scope.receivedWardDate = utils.getContentOfDefault(form.receivedWardDate, '');
    $scope.endTime = utils.getContentOfDefault(form.endTime, '');
    $scope.treatmentStageTime = utils.getContentOfDefault(form.treatmentStageTime, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.birthPlanDate = utils.getContentOfDefault(form.birthPlanDate, '');
    $scope.objectGroupID = utils.getContentOfDefault(form.objectGroupID, '');
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.clinicalStage = utils.getContentOfDefault(form.clinicalStage, '');
    $scope.statusOfTreatmentID = utils.getContentOfDefault(form.statusOfTreatmentID, '');
    $scope.treatmentRegimenStage = utils.getContentOfDefault(form.treatmentRegimenStage, '');
    $scope.treatmentRegimenID = utils.getContentOfDefault(form.treatmentRegimenID, '');
    $scope.medicationAdherence = utils.getContentOfDefault(form.medicationAdherence, '');
    $scope.endCase = utils.getContentOfDefault(form.endCase, '');
    $scope.transferSiteID = utils.getContentOfDefault(form.transferSiteID, '');
    $scope.treatmentStageID = utils.getContentOfDefault(form.treatmentStageID, '');
    $scope.oldTreatmentRegimenStage = utils.getContentOfDefault(form.oldTreatmentRegimenStage, '');
    $scope.oldTreatmentRegimenID = utils.getContentOfDefault(form.oldTreatmentRegimenID, '');
    $scope.baseTreatmentRegimenStage = utils.getContentOfDefault(form.baseTreatmentRegimenStage, '');
    $scope.baseTreatmentRegimenID = utils.getContentOfDefault(form.baseTreatmentRegimenID, '');
    $scope.causesChange = utils.getContentOfDefault(form.causesChange, '');

    // Validate for fields on add new customer for testing HIV
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            treatmentTime: {
                required: function () {
                    return $("#statusOfTreatmentID").val() === '3';
                },
            },
        },
        messages: {
            treatmentTime: {
                required: "Ngày điều trị ARV không được để trống"
            },
        }
    });
    
    $scope.calculateDay = function (lastExaminationTime, appointmentTime,daysReceived) {
        if($(lastExaminationTime).val() !== 'dd/mm/yyyy' && $(lastExaminationTime).val() != null && $(lastExaminationTime).val() != '' && 
                        $(appointmentTime).val() !== 'dd/mm/yyyy' && $(appointmentTime).val() != null && $(appointmentTime).val() != '' && 
                        ($(daysReceived).val() == null || $(daysReceived).val() == '')){
            var dataSplit1 = $(lastExaminationTime).val().split('/');
            var start = new Date(dataSplit1[1] + '/' + dataSplit1[0] + '/' + dataSplit1[2]);
            var dataSplit2 = $(appointmentTime).val().split('/');
            var end = new Date(dataSplit2[1] + '/' + dataSplit2[0] + '/' + dataSplit2[2]);
            var diff = new Date(end - start);
            var days = diff/1000/60/60/24;
            if(days  > 0){
                $(daysReceived).val(days).change();
            }
        }
    };

    $scope.init = function () {
        $scope.switchConfig();

        if ($scope.action !== "view") {
            $scope.$parent.select_search("#sourceSiteID", "");
            $scope.$parent.select_search("#treatmentRegimenID", "");
            $scope.$parent.select_search("#objectGroupID", "");
        }

        let rowDataInit = {};
        let resutArray = [];
        $('.tbl_agency').each(function () {

            let name = $(this).attr('name');
            if (name.indexOf('dob') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('isPreventive') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('preventiveCotrimoxazole') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('pcrOneResult') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('pcrTwoResult') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('preventiveDate') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('cotrimoxazoleFromTime') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('cotrimoxazoleToTime') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }
            if (name.indexOf('treatmentTime') !== -1) {
                rowDataInit[name] = $("[name='" + name + "']").val() + '';
            }

            if (Object.keys(rowDataInit).length === 7) {
                resutArray.push(rowDataInit);
                rowDataInit = {};
            }
        });

        $.each(resutArray, function (i, v) {
            let rowDataValidate = {};
            $.each(v, function (key, val) {
                if (key.indexOf("pcrOneResult") !== -1) {
                    rowDataValidate["pcrOneResult"] = key;
                }
                if (key.indexOf("pcrTwoResult") !== -1) {
                    rowDataValidate["pcrTwoResult"] = key;
                }
                if (key.indexOf("treatmentTime") !== -1) {
                    rowDataValidate["treatmentTime"] = key;
                }
            });

            // On change kết quả Kết quả XN PCR lần 1
            if ($("[name = '" + rowDataValidate["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowDataValidate["pcrTwoResult"] + "']").val() !== "1") {
                $("[name = '" + rowDataValidate["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
            } else {
                $("[name = '" + rowDataValidate["treatmentTime"] + "']").removeAttr("disabled").change();
            }

            $("[name = '" + rowDataValidate["pcrOneResult"] + "']").on("change", () => {
                if ($("[name = '" + rowDataValidate["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowDataValidate["pcrTwoResult"] + "']").val() !== "1") {
                    $("[name = '" + rowDataValidate["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
                } else {
                    $("[name = '" + rowDataValidate["treatmentTime"] + "']").removeAttr("disabled").change();
                }
            });

            $("[name = '" + rowDataValidate["pcrTwoResult"] + "']").on("change", () => {
                if ($("[name = '" + rowDataValidate["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowDataValidate["pcrTwoResult"] + "']").val() !== "1") {
                    $("[name = '" + rowDataValidate["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
                } else {
                    $("[name = '" + rowDataValidate["treatmentTime"] + "']").removeAttr("disabled").change();
                }
            });

        });

        $("#siteID").attr('disabled', 'disabled');
        $("#registrationTypeHidden").attr("disabled", "disabled");
        $("#sourceSiteHidden").attr("disabled", "disabled");
        $("#sourceSiteNameHidden").attr("disabled", "disabled");

        if ($scope.item.sourceSiteID === "-1") {
            $("#sourceSiteName").removeAttr("disabled").change();
        } else {
            $("#sourceSiteName").attr('disabled', 'disabled');
        }

        if ($("#registrationType").val() !== "1" && $("#registrationType").val() !== "3") {
            $("#sourceSiteID").attr("disabled", "disabled").change();
        }

        if ($("#registrationType").val() === "3") {
            $("#sourceSiteID").removeAttr("disabled").val('').change();
            $("#sourceSiteHidden").attr('disabled', 'disabled').change();
        } else {
            if ($("#sourceSiteID").val() !== "-1") {
                $("#sourceSiteID").attr('disabled', 'disabled').change();
                $("#sourceSiteHidden").removeAttr("disabled").change();
            }
        }

        if ($("#registrationType").on("change", () => {
            $scope.getSourceSiteID();
        }))
            ;

        if ($("#sourceSiteID").on("change", () => {
            $scope.getSourceSiteName();
        }))
            ;

        // Set mặc định hiển thi mã bệnh án
        if ($scope.item.registrationType === "3") {
            $("#sourceCode").removeAttr("disabled");
        } else {
            $("#sourceCode").attr("disabled", "disabled");
        }

        $scope.getStatusOfTreatmentID();

        if ($("#statusOfTreatmentID").on("change", () => {
            $scope.getStatusOfTreatmentID();
        }))
            ;

        // Mặc định disable Ngày thay đổi phác đồ
        $("#oldTreatmentRegimenStage").attr("disabled", "disabled");
        $("#oldTreatmentRegimenID").attr("disabled", "disabled");
        $("#causesChange").attr("disabled", "disabled");
        $("#hiddenCausesChange").attr("disabled", "disabled");
        $("#regimenStageDate").attr("disabled", "disabled");
        $("#regimenDate").attr("disabled", "disabled");
        $("#hiddenRegimenStageDate").attr("disabled", "disabled");
        $("#hiddenRegimenDate").attr("disabled", "disabled");

        // Mới load phác đồ và bậc phác đồ
        if ($scope.item.baseTreatmentRegimenStage !== '' && $("#treatmentRegimenStage").val() !== null && $("#treatmentRegimenStage").val() !== '' &&
                $scope.item.baseTreatmentRegimenStage !== $("#treatmentRegimenStage").val()) {
            $("#regimenStageDate").removeAttr("disabled").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
            $("#causesChange").removeAttr("disabled").change();
            $("#causesChange").val("");
            $("#hiddenOldTreatmentRegimenStage").removeAttr("disabled").val($("#treatmentRegimenStage").val()).change();
        } else {

            $("#hiddenRegimenStageDate").removeAttr("disabled");
        }

        if ($scope.item.baseTreatmentRegimenID !== '' && $("#treatmentRegimenID").val() !== null && $("#treatmentRegimenID").val() !== '' &&
                $scope.item.baseTreatmentRegimenID !== $("#treatmentRegimenID").val()) {
            $("#regimenDate").removeAttr("disabled").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
            $("#causesChange").removeAttr("disabled").change();
            $("#causesChange").val("");
            $("#hiddenOldTreatmentRegimenID").removeAttr("disabled").val($("#treatmentRegimenID").val()).change();
        } else {
            $("#hiddenRegimenDate").removeAttr("disabled");

        }

        // Sự kiện thay đổi phác đồ bậc phác đồ
        if ($("#treatmentRegimenStage").on("change", () => {
            if ($scope.baseTreatmentRegimenStage !== '' && $("#treatmentRegimenStage").val() !== null && $("#treatmentRegimenStage").val() !== '' && $("#treatmentRegimenStage").val() !== 'string:' &&
                    ($scope.baseTreatmentRegimenStage !== $("#treatmentRegimenStage").val() && 'string:' + $scope.baseTreatmentRegimenStage !== $("#treatmentRegimenStage").val()) ) {
                $("#regimenStageDate").removeAttr("disabled").change();
                $("#regimenStageDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                $("#hiddenRegimenStageDate").attr("disabled", "disabled");
                $("#causesChange").removeAttr("disabled").change();
                $("#causesChange").val("");
                $("#hiddenCausesChange").attr("disabled", "disabled");
                $("#hiddenOldTreatmentRegimenStage").removeAttr("disabled");
                $("#hiddenOldTreatmentRegimenStage").val($scope.baseTreatmentRegimenStage).change();
                $("#oldTreatmentRegimenStage").val($scope.baseTreatmentRegimenStage).change();
            } else {
                $("#hiddenOldTreatmentRegimenStage").attr("disabled", "disabled");
                $("#regimenStageDate").attr("disabled", "disabled");
                $("#regimenStageDate").val('');
                $("#hiddenRegimenStageDate").removeAttr("disabled").change();
                $("#causesChange").val("").attr("disabled", "disabled");
                $("#hiddenCausesChange").val("").removeAttr("disabled").change();
            }
            if ($("#hiddenCausesChange").disabled === false && $("#causesChange").disabled !== false) {
                $("#hiddenCausesChange").attr("disabled", "disabled");
            }
        }))
            ;

        if ($("#treatmentRegimenID").on("change", () => {
            if ($scope.baseTreatmentRegimenID !== '' && $("#treatmentRegimenID").val() !== null && $("#treatmentRegimenID").val() !== '' &&
                    $scope.baseTreatmentRegimenID !== $("#treatmentRegimenID").val()) {
                $("#regimenDate").removeAttr("disabled").change();
                $("#regimenDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                $("#hiddenRegimenDate").attr("disabled", "disabled");
                $("#causesChange").removeAttr("disabled").change();
                $("#causesChange").val("");
                $("#hiddenOldTreatmentRegimenID").removeAttr("disabled");
                $("#hiddenOldTreatmentRegimenID").val($scope.baseTreatmentRegimenID).change();
                $("#oldTreatmentRegimenID").val($scope.baseTreatmentRegimenID).change();
            } else {
                $("#hiddenOldTreatmentRegimenID").attr("disabled", "disabled");
                $("#regimenDate").attr("disabled", "disabled");
                $("#regimenDate").val('');
                $("#hiddenRegimenDate").removeAttr("disabled").change();
                $("#causesChange").val("").attr("disabled", "disabled");
                $("#hiddenCausesChange").val("").removeAttr("disabled").change();
            }
            if ($("#hiddenCausesChange").disabled === false && $("#causesChange").disabled !== false) {
                $("#hiddenCausesChange").attr("disabled", "disabled");
            }
        }))
            ;

        $scope.regimenOptions = $scope.options['treatment-regimen'];
        $scope.regimenOptions1 = $scope.options['treatment-regimen-level1'];
        $scope.regimenOptions2 = $scope.options['treatment-regimen-level2'];
        $scope.regimenOptions3 = $scope.options['treatment-regimen-level3'];

        var regimenOptions = $scope.regimenOptions;
        var regimenOptions1 = $scope.regimenOptions1;
        var regimenOptions2 = $scope.regimenOptions2;
        var regimenOptions3 = $scope.regimenOptions3;

        $scope.treatmentRegimenStageChange = function () {
            console.log("xx " + $scope.treatmentRegimenStage);



            $('#treatmentRegimenID')
                    .find('option')
                    .remove()
                    .end()
                    ;

            if ($scope.treatmentRegimenStage === '1') {
                for (const [key, value] of Object.entries(regimenOptions1)) {
                    $($('#treatmentRegimenID')).append(new Option(value, key, false, false));
                }
                $($('#treatmentRegimenID')).val("");
            } else if ($scope.treatmentRegimenStage === '2') {
                for (const [key, value] of Object.entries(regimenOptions2)) {
                    $($('#treatmentRegimenID')).append(new Option(value, key, false, false));
                }
                $($('#treatmentRegimenID')).val("");
            } else if ($scope.treatmentRegimenStage === '3') {
                for (const [key, value] of Object.entries(regimenOptions3)) {
                    $($('#treatmentRegimenID')).append(new Option(value, key, false, false));
                }
                $($('#treatmentRegimenID')).val("");
            } else {
                for (const [key, value] of Object.entries(regimenOptions)) {
                    $($('#treatmentRegimenID')).append(new Option(value, key, false, false));
                }
                $($('#treatmentRegimenID')).val("");
            }
        }

        // Thông tin biến động điều trị
        $scope.getEndCase();
        if ($("#endCase").on("change", () => {
            $scope.getEndCase();
        }))
            ;

        if ($("#endTime").on("change", () => {
            if ($("#endCase").val() !== '' && $("#endCase").val() !== null && $("#endCase").val() !== '5') {
                $scope.getEndCase();
            }
        }))
            ;

        $scope.getTransferSiteID();
        if ($("#transferSiteID").on("change", () => {
            $scope.getTransferSiteID();
        }))
            ;

        // Mặc định disable trạng thái biến động
        $("#treatmentStageID").attr("disabled", "disabled");

        // Tự động load Trạng thái biến động (ưu tiên lý do kết thúc
        if ($scope.item.endCase === "3") { // Lý do kết thúc
            $scope.item.treatmentStageID = "3";
            $("#treatmentStageID").val("3").change();
        } else if ($scope.item.endCase === "4") { // Mất dấu
            $scope.item.treatmentStageID = "5";
            $("#treatmentStageID").val("5").change();
        } else if ($scope.item.endCase === "1") { // Bỏ trị
            $scope.item.treatmentStageID = "6";
            $("#treatmentStageID").val("6").change();
        } else if ($scope.item.endCase === "2") { // Tử vong
            $scope.item.treatmentStageID = "7";
            $("#treatmentStageID").val("7").change();
        } else if ($scope.item.endCase === "5") { // Kết thúc
            $scope.item.treatmentStageID = "8";
            $("#treatmentStageID").val("8").change();
        } else if ($scope.item.registrationType === "1") { // đăng ký mới
            $scope.item.treatmentStageID = "1";
            $("#treatmentStageID").val("1").change();
        } else if ($scope.item.registrationType === "2") { // điều trị lại
            $scope.item.treatmentStageID = "2";
            $("#treatmentStageID").val("2").change();
        } else if ($scope.item.registrationType === "3") { // chuyển đến
            $scope.item.treatmentStageID = "4";
        } else if ($scope.item.registrationType === "4") { // Trẻ em dưới 18 tuổi
            $scope.item.treatmentStageID = "1";
            $("#treatmentStageID").val("4").change();
        } else if ($scope.item.registrationType === "5") { // Phơi nhiễm
            $scope.item.treatmentStageID = "1";
            $("#treatmentStageID").val("1").change();
        }

        $("#registrationType, #endCase").on("change", () => {
            // Tự động load Trạng thái biến động (ưu tiên lý do kết thúc
            if ($("#endCase").val() === "3") { // Lý do kết thúc
                $("#treatmentStageID").val("3").change();
            } else if ($("#endCase").val() === "4") { // Mất dấu
                $("#treatmentStageID").val("5").change();
            } else if ($("#endCase").val() === "1") { // Bỏ trị
                $("#treatmentStageID").val("6").change();
            } else if ($("#endCase").val() === "2") { // Tử vong
                $("#treatmentStageID").val("7").change();
            } else if ($("#endCase").val() === "5") { // Kết thúc
                $("#treatmentStageID").val("8").change();
            } else if ($("#registrationType").val() === "1") { // đăng ký mới
                $("#treatmentStageID").val("1").change();
            } else if ($("#registrationType").val() === "2") { // điều trị lại
                $("#treatmentStageID").val("2").change();
            } else if ($("#registrationType").val() === "3") { // chuyển đến
                $("#treatmentStageID").val("4").change();
            } else if ($("#registrationType").val() === "4") { // Trẻ em dưới 18 tuổi
                $("#treatmentStageID").val("1").change();
            } else if ($("#registrationType").val() === "5") { // Phơi nhiễm
                $("#treatmentStageID").val("1").change();
            }

            // Set mặc định hiển thi mã bệnh án
            $scope.getSourceCode();
            if ($scope.item.id === null || $scope.item.id === '') {
                if ($("#endTime").val() !== null && $("#endTime").val() !== '' && $("#endCase").val() !== '5') {
                    $("#treatmentStageTime").val($("#endTime").val());
                } else if ($("#registrationTime").val() !== null && $("#registrationTime").val() !== '' && ($("#registrationType").val() !== null && $("#registrationType").val() !== '')) {
                    $("#treatmentStageTime").val($("#registrationTime").val());
                }
            }
        });

        $scope.getObjectGroupID();
        $("#objectGroupID").on("change", () => {
            $scope.getObjectGroupID();
        });

        // Đang chờ điều trị ARV
        if ($("#statusOfTreatmentID").val() === "2") {
            $("#treatmentTime").val('').attr("disabled", "disabled");
            $("#treatmentRegimenStage").val('').attr("disabled", "disabled");
            $("#treatmentRegimenID").val('').attr("disabled", "disabled");
            $("#daysReceived").attr("disabled", "disabled");
            $("#qualifiedTreatmentTime").val('').attr("disabled", "disabled");
            $("#receivedWardDate").val('').attr("disabled", "disabled");
            $("#supplyMedicinceDate").val('').attr("disabled", "disabled");
        } else {
            $("#treatmentTime").removeAttr("disabled");
            $("#treatmentRegimenStage").removeAttr("disabled");
            $("#treatmentRegimenID").removeAttr("disabled");
            $("#daysReceived").removeAttr("disabled");
            $("#qualifiedTreatmentTime").removeAttr("disabled");
            $("#receivedWardDate").removeAttr("disabled");
            $("#supplyMedicinceDate").removeAttr("disabled");
        }

        $("#statusOfTreatmentID").on("change", () => {
            if ($("#statusOfTreatmentID").val() === "2") {
                $("#treatmentTime").val('').attr("disabled", "disabled");
                $("#treatmentRegimenStage").val('').attr("disabled", "disabled");
                $("#treatmentRegimenID").val('').attr("disabled", "disabled");
                $("#daysReceived").val('').attr("disabled", "disabled");
                $("#qualifiedTreatmentTime").val('').attr("disabled", "disabled");
                $("#receivedWardDate").val('').attr("disabled", "disabled");
                $("#supplyMedicinceDate").val('').attr("disabled", "disabled");
            } else {
                $("#treatmentTime").removeAttr("disabled");
                $("#treatmentRegimenStage").removeAttr("disabled");
                $("#treatmentRegimenID").removeAttr("disabled");
                $("#daysReceived").removeAttr("disabled");
                $("#qualifiedTreatmentTime").removeAttr("disabled");
                $("#receivedWardDate").removeAttr("disabled");
                $("#supplyMedicinceDate").removeAttr("disabled");
            }
        });

        if ($scope.action === "view") {
            $("form#opc_stage_new_pop :input").attr({disabled: "disabled"});
            $('select').each(function () {
                if ($(this).children("option:selected").text().includes("Chọn")) {
                    $(this).children("option:selected").text('');
                }
            });
        }

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));

        // Thông báo nếu không có người được giới thiệu
        $scope.emptyNotify();

        // Hiển thị cho dropdown trạng thái điều trị
        if ($scope.id === '' || $scope.statusOfTreatmentID !== '0') {
            $("#statusOfTreatmentID option[value='0']").hide();
        }

        // Uppercase Mã BA chuyển đến
        if ($("#sourceCode").val() !== '' && $("#sourceCode").val() !== null) {
            $("#sourceCode").val($("#sourceCode").val().toUpperCase());
        }

        if ($scope.action === 'view') {
            $("#causesChange").val($scope.causesChange);
        }
    };

    $("#sourceCode").on("blur", function () {
        if ($("#sourceCode").val() !== '' && $("#sourceCode").val() !== null) {
            $("#sourceCode").val($("#sourceCode").val().toUpperCase());
        }
    });

    $(".delete-agency").click(function () {
        $(this).parent().parent().remove();

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));
//        $scope.indexForAgency($(".fieldwrapper"));
        $scope.emptyNotify();
    });

    // Cập nhật ngày biến động điều trị
    $("#registrationTime, #endTime").change(function () {
        $scope.getEndCase();
    });

    $("#lastExaminationTime,#daysReceived").change(function () {
        if ($("#lastExaminationTime").val() != null && $("#lastExaminationTime").val() != '' &&
                $("#daysReceived").val() != null && $("#daysReceived").val() != '') {
            var dataSplit = $("#lastExaminationTime").val().split('/');
            var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
            var dayNumer = isNaN(parseInt($("#daysReceived").val())) ? 0 : parseInt($("#daysReceived").val());
            formatedDate.setDate(formatedDate.getDate() + dayNumer);
            $("#appointmentTime").val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
        }
        $scope.calculateDay("#lastExaminationTime","#appointmentTime","#daysReceived");
    });
    $("#appointmentTime").change(function () {
        $scope.calculateDay("#lastExaminationTime","#appointmentTime","#daysReceived");
    });

    $scope.getObjectGroupID = () => {
        if ($("#objectGroupID").val() === "5.1" || $("#objectGroupID").val() === "5.2" || $("#objectGroupID").val() === "5") {
            $("#pregnantStartDate").removeAttr("disabled");
            $("#pregnantEndDate").removeAttr("disabled");
            $("#birthPlanDate").removeAttr("disabled");
        } else {
            $("#pregnantStartDate").attr("disabled", "disabled").val("");
            $("#pregnantEndDate").attr("disabled", "disabled").val("");
            $("#birthPlanDate").attr("disabled", "disabled").val("");
        }
    }

    $scope.getSourceCode = () => {
        if ($("#registrationType").val() === "3") {
            $("#sourceCode").removeAttr("disabled").val("");
        } else {
            $("#sourceCode").attr("disabled", "disabled").val("");
        }
    }

    $scope.getTransferSiteID = () => {
        if ($("#transferSiteID").val() !== "-1") {
            $("#transferSiteName").attr("disabled", "disabled").val("");
        } else {
            $("#transferSiteName").removeAttr("disabled");
        }
    }

    $scope.getEndCase = () => {

        if ($("#endCase").val() !== '' && $("#endCase").val() !== null) {
            $("#treatmentStageTime").val($("#endTime").val());
        }

        if (($("#endTime").val() === null || $("#endTime").val() === '') && ($("#registrationType").val() !== null && $("#registrationType").val() !== '')
                && $("#registrationTime").val() !== null && $("#registrationTime").val() !== '') {
            $("#treatmentStageTime").val($("#registrationTime").val());
            $scope.item.treatmentStageTime = $("#registrationTime").val();
        }

        if ($("#endCase").val() !== "3") {
            $("#transferSiteID").attr("disabled", "disabled").val("").change();
            $("#transferCase").attr("disabled", "disabled").val("");
        } else {
            $("#transferSiteID").removeAttr("disabled").change();
            $("#transferCase").removeAttr("disabled");
        }
    }

    $scope.getStatusOfTreatmentID = () => {
        if ($("#statusOfTreatmentID").val() === "2") {
            $("#treatmentTime").attr("disabled", "disabled").val("").change();
            $("#treatmentRegimenStage").attr("disabled", "disabled").val("").change();
            $("#treatmentRegimenID").attr("disabled", "disabled").val("").change();
            $("#daysReceived").attr("disabled", "disabled").val("").change();
        } else {
            $("#treatmentTime").removeAttr("disabled");
            $("#treatmentRegimenStage").removeAttr("disabled");
            $("#treatmentRegimenID").removeAttr("disabled");
            $("#daysReceived").removeAttr("disabled");
        }
    }

    $scope.getSourceSiteID = () => {
        if ($("#registrationType").val() === "3" || $("#registrationType").val() === "1") {
            $scope.item.sourceSiteID = "";
            $("#sourceSiteID").removeAttr("disabled").val("").change();
            $("#sourceSiteHidden").attr("disabled", "disabled").change();
        } else {
            $scope.item.sourceSiteID = "";
            $("#sourceSiteID").attr("disabled", "disabled").val("").change();
        }
    }

    $scope.getSourceSiteName = () => {
        if ($("#sourceSiteID").val() === "-1") {
            $scope.item.sourceSiteName = "";
            $("#sourceSiteName").removeAttr("disabled").change();
        } else {
            $scope.item.sourceSiteName = "";
            $("#sourceSiteName").attr("disabled", "disabled").val("").change();
        }
    }

    $scope.getSourceCode = () => {
        if ($("#registrationType").val() === "3") {
            $("#sourceCode").removeAttr("disabled");
        } else {
            $("#sourceCode").attr("disabled", "disabled").val("");
        }
    }

    // Thêm dòng động đối tượng bạn tình bạn chích
    $("#add, #add-title").click(function () {

        // Get row index
        var numberCustomer = $(".fieldwrapper").length;
        var rowIndex = numberCustomer;

        var lastField = $("#listPartner tr:last");
        var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
        var fieldWrapper = $("<tr class=\"fieldwrapper\" id=\"field" + intId + "\"/>");
        fieldWrapper.data("idx", intId);
        var fGear = $("<td class=\"text-center order vertical-align-middle\">" + "<li class=\"fa fa-gear\"> </li>" + "<input type=\"hidden\" class=\"form-control tbl_agency child-date\" id=\"id" + rowIndex + ".dob\" name=\"children[" + rowIndex + "].ID\"/></td>");
        var fDob = $("<td class=\"text-center vertical-align-top wrap max-width-150\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".dob\" name=\"children[" + rowIndex + "].dob\"/></td>");
        var fPreventiveDate = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".preventiveDate\" name=\"children[" + rowIndex + "].preventiveDate\"/></td>");
        var fPreventiveEndDate = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".preventiveEndDate\" name=\"children[" + rowIndex + "].preventiveEndDate\"/></td>");
        var fCotrimoxazoleFromTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".cotrimoxazoleFromTime\" name=\"children[" + rowIndex + "].cotrimoxazoleFromTime\"/></td>");
        var fCotrimoxazoleToTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".cotrimoxazoleToTime\" name=\"children[" + rowIndex + "].cotrimoxazoleToTime\"/></td>");
        var fPcrBloodOneTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".pcrBloodOneTime\" name=\"children[" + rowIndex + "].pcrBloodOneTime\"/></td>");
        var fPcrOneTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".pcrOneTime\" name=\"children[" + rowIndex + "].pcrOneTime\"/></td>");
        var fPcrOneResult = $("<td class=\"text-center notifyListTD vertical-align-top wrap\"><select class=\"notifyList form-control tbl_agency\" id=\"children" + rowIndex + ".pcrOneResult\" name=\"children[" + rowIndex + "].pcrOneResult\"></select></td>");
        var fPcrBloodTwoTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".pcrBloodTwoTime\" name=\"children[" + rowIndex + "].pcrBloodTwoTime\"/></td>");
        var fPcrTwoTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".pcrTwoTime\" name=\"children[" + rowIndex + "].pcrTwoTime\"/></td>");
        var fPcrTwoResult = $("<td class=\"text-center notifyListTD vertical-align-top wrap\"><select class=\"notifyList form-control tbl_agency\" id=\"children" + rowIndex + ".pcrTwoResult\" name=\"children[" + rowIndex + "].pcrTwoResult\"></select></td>");
        var fTreatmentTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"children" + rowIndex + ".treatmentTime\" name=\"children[" + rowIndex + "].treatmentTime\"/></td>");
        var removeButton = $("<td class=\"text-center vertical-align-middle\"><button type=\"button\" class=\"btn btn-danger btn-xs delete-agency\" > <i class=\"fa fa fa-trash\" ></i>&nbsp;Xóa</button></td>");

        removeButton.click(function () {
            $(this).parent().remove();
            // Đếm tổng số khách hàng
            $scope.customerCount($(".fieldwrapper"));
//            $scope.indexForAgency($(".fieldwrapper"));
            $scope.emptyNotify();
        });

        fieldWrapper.append(fGear);
        fieldWrapper.append(fDob);
        fieldWrapper.append(fPreventiveDate);
        fieldWrapper.append(fPreventiveEndDate);
        fieldWrapper.append(fCotrimoxazoleFromTime);
        fieldWrapper.append(fCotrimoxazoleToTime);
        fieldWrapper.append(fPcrBloodOneTime);
        fieldWrapper.append(fPcrOneTime);
        fieldWrapper.append(fPcrOneResult);
        fieldWrapper.append(fPcrBloodTwoTime);
        fieldWrapper.append(fPcrTwoTime);
        fieldWrapper.append(fPcrTwoResult);
        fieldWrapper.append(fTreatmentTime);
        fieldWrapper.append(removeButton);

        $("#listPartnerBody").append(fieldWrapper);
        $(".child-date").attr('maxlength', '10');

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));
        $scope.emptyNotify();

        $scope.initPCR($("[name='" + "children[" + rowIndex + "].pcrOneResult" + "']"));
        $scope.initPCR($("[name='" + "children[" + rowIndex + "].pcrTwoResult" + "']"));

        // Thêm validate enable/disable
        $('.fieldwrapper').each(() => {
            var rowData = {};
            $('.tbl_agency').each(function () {
                let name = $(this).attr('name');

                if (name.indexOf('pcrOneResult') !== -1) {
                    rowData["pcrOneResult"] = name;
                }

                if (name.indexOf('pcrTwoResult') !== -1) {
                    rowData["pcrTwoResult"] = name;
                }

                if (name.indexOf('treatmentTime') !== -1) {
                    rowData["treatmentTime"] = name;
                }
            });

//             On change kết quả Kết quả XN PCR lần 1
            if ($("[name = '" + rowData["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowData["pcrTwoResult"] + "']").val() !== "1") {
                $("[name = '" + rowData["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
            } else {
                $("[name = '" + rowData["treatmentTime"] + "']").removeAttr("disabled").change();
            }

            $("[name = '" + rowData["pcrOneResult"] + "']").on("change", () => {
                if ($("[name = '" + rowData["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowData["pcrTwoResult"] + "']").val() !== "1") {
                    $("[name = '" + rowData["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
                } else {
                    $("[name = '" + rowData["treatmentTime"] + "']").removeAttr("disabled").change();
                }
            });

            $("[name = '" + rowData["pcrTwoResult"] + "']").on("change", () => {
                if ($("[name = '" + rowData["pcrOneResult"] + "']").val() !== "1" && $("[name = '" + rowData["pcrTwoResult"] + "']").val() !== "1") {
                    $("[name = '" + rowData["treatmentTime"] + "']").attr("disabled", "disabled").val("").change();
                } else {
                    $("[name = '" + rowData["treatmentTime"] + "']").removeAttr("disabled").change();
                }
            });
        });
    });

    // Đánh lại thứ tự bạn tình bạn chích
    $scope.indexForAgency = function (a) {
        var index = 1;
        a.each(function () {
            $(this).children(".order").text(index);
            index += 1;
        });
    };

    // Đếm tổng số người được giới thiệu
    $scope.customerCount = function (a) {
        if (typeof a !== 'undefined' && a.length > 0) {
            $("#total-customer").text("Tổng số: " + a.length);
        } else {
            $("#total-customer").text("");
        }
    };

    // Khởi tạo đồng ý xét nghiệm
    $scope.initAnswer = function (a) {
        a.empty();
        a.append(new Option("Chọn câu trả lời", ""));
        a.append(new Option("Có", 1));
        a.append(new Option("Không", 0));

    }

    $scope.initPCR = function (a) {
        a.empty();
        a.append(new Option("Chọn kết quả", ""));
        a.append(new Option("Dương tính", 1));
        a.append(new Option("Âm tính", 2));
        a.append(new Option("Chưa có kết quả", 0));
    }

    $scope.emptyNotify = function () {
        $("table tr td:contains('Không có thông tin')").each(function () {
            $(this).text('');
        });
    }

    // Khởi tạo thông báo
    $scope.initNotification = function (a) {
        a.empty();
        a.append(new Option("Chọn câu trả lời", ""));
        a.append(new Option("Tự thông báo", "1"));
        a.append(new Option("Cùng thông báo", "2"));
        a.append(new Option("NVTV thông báo", "3"));
        a.append(new Option("Được phép tiết lộ danh tính người nhiễm HIV và NVTV thông báo", "4"));
        a.append(new Option("Không được phép tiết lộ danh tính người nhiễm HIV", "5"));
    };

    // Validate định dạng ngày
    $.validator.addMethod("validDate", function (value) {
        return value.match(/(?:0[1-9]|[12][0-9]|3[01])\/(?:0[1-9]|1[0-2])\/(?:19|20\d{2})/);
    }, "Nhập đúng định dạng theo dd/mm/yyyy");

    // Validate ngày tương lai
    $.validator.addMethod("invalidFutureDate", function (value, element) {

        var date = value.substring(0, 2);
        var month = value.substring(3, 5);
        var year = value.substring(6, 10);

        var dateToCompare = new Date(year, month - 1, date);
        var currentDate = new Date();

        return dateToCompare < currentDate;

    }, "Ngày không được lớn hơn ngày hiện tại");

    $.validator.addMethod("compareDate", function (value, element, param) {

        if (param === '' || value === '') {
            return true;
        }

        var date = value.substring(0, 2);
        var month = value.substring(3, 5);
        var year = value.substring(6, 10);

        var dateOther = param.substring(0, 2);
        var monthOther = param.substring(3, 5);
        var yearOther = param.substring(6, 10);

        var originDate = new Date(year, month - 1, date);
        var dateCompared = new Date(yearOther, monthOther - 1, dateOther);

        return this.optional(element) || originDate > dateCompared;
    }, "thứ tự ngày không hợp lệ");

    $.validator.addMethod("compareDatePcr", function (value, element, param) {

        if (param === '' || value === '') {
            return true;
        }

        var date = value.substring(0, 2);
        var month = value.substring(3, 5);
        var year = value.substring(6, 10);

        var dateOther = param.substring(0, 2);
        var monthOther = param.substring(3, 5);
        var yearOther = param.substring(6, 10);

        var originDate = new Date(year, month - 1, date);
        var dateCompared = new Date(yearOther, monthOther - 1, dateOther);

        return this.optional(element) || originDate > dateCompared;
    }, "thứ tự ngày không hợp lệ");

    $.validator.addMethod("compareDateDob", function (value, element, param) {

        if (param === '' || value === '') {
            return true;
        }

        var date = value.substring(0, 2);
        var month = value.substring(3, 5);
        var year = value.substring(6, 10);

        var dateOther = param.substring(0, 2);
        var monthOther = param.substring(3, 5);
        var yearOther = param.substring(6, 10);

        var originDate = new Date(year, month - 1, date);
        var dateCompared = new Date(yearOther, monthOther - 1, dateOther);

        return this.optional(element) || originDate > dateCompared;
    }, "thứ tự ngày không hợp lệ");

    $scope.validateChildren = function (form) {

        // Validate người được giới thiệu
        var rules = new Object();
        var messages = new Object();

        if (typeof $('.tbl_agency') === 'undefined' || $('.tbl_agency').size() <= 0) {
            return true;
        }

        // Validate
        $('.fieldwrapper').each(() => {
            let rowData = {};
            let startCotrima = "";
            let startPreventive = "";
            $('.tbl_agency').each(function () {
                let name = $(this).attr('name');
                if (name.indexOf('dob') !== -1) {
                    rowData["dob"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {maxlength: 10,
                        required: true,
                        validDate: true,
                        invalidFutureDate: true
                    }
                    messages[this.name] = {maxlength: "Ngày sinh không hợp lệ",
                        required: "Ngày sinh không được để trống",
                        validDate: "Ngày sinh không hợp lệ",
                        invalidFutureDate: "Ngày sinh phải nhỏ hơn ngày hiện tại"}
                }

                // Ngày bắt đầu dự phòng ARV
                if (name.indexOf('preventiveDate') !== -1) {
                    rowData["preventiveDate"] = $("[name='" + name + "']").val() + '';
                    startPreventive = this.name;
                    rules[this.name] = {maxlength: 10,
                        validDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null,
                        compareDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null
                    }
                    messages[this.name] = {maxlength: "Ngày BĐ dự phòng ARV không hợp lệ",
                        validDate: "Ngày BĐ dự phòng ARV không hợp lệ ",
                        compareDate: "Ngày BĐ dự phòng ARV phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày BĐ dự phòng ARV phải nhỏ hơn ngày hiện tại"}
                }


                // Ngày kết thúc dự phòng ARV
                if (name.indexOf('preventiveEndDate') !== -1) {
                    rowData["preventiveEndDate"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {maxlength: 10,

                        validDate: rowData["preventiveEndDate"] !== '' && rowData["preventiveEndDate"] !== null,
                        compareDate: rowData["preventiveEndDate"] !== '' && rowData["preventiveEndDate"] !== null && rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null ? rowData["preventiveDate"] : '',
                        invalidFutureDate: rowData["preventiveEndDate"] !== '' && rowData["preventiveEndDate"] !== null
                    }
                    messages[this.name] = {maxlength: "Ngày KT dự phòng ARV không hợp lệ",
                        validDate: "Ngày KT dự phòng ARV không hợp lệ",
                        compareDate: "Ngày KT dự phòng ARV phải lớn hơn ngày BĐ dự phòng ARV",
                        invalidFutureDate: "Ngày KT dự phòng ARV phải nhỏ hơn ngày hiện tại"}

                    // Validate start cotrima
                    rules[startPreventive] = {
                        required: rowData["preventiveEndDate"] !== '' && rowData["preventiveEndDate"] !== null,
                        maxlength: 10,
                        validDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null,
                        compareDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: rowData["preventiveDate"] !== '' && rowData["preventiveDate"] !== null
                    }


                    messages[startPreventive] = {
                        required: "Ngày BĐ dự phòng ARV không được để trống",
                        maxlength: "Ngày BĐ dự phòng ARV không hợp lệ",
                        validDate: "Ngày BĐ dự phòng ARV không hợp lệ ",
                        compareDate: "Ngày BĐ dự phòng ARV phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày BĐ dự phòng ARV phải nhỏ hơn ngày hiện tại"}
                }



                // Ngày bắt đầu Cotrimoxazole
                if (name.indexOf('cotrimoxazoleFromTime') !== -1) {
                    rowData["cotrimoxazoleFromTime"] = $("[name='" + name + "']").val() + '';
                    startCotrima = this.name;
                    rules[this.name] = {

                        validDate: rowData["cotrimoxazoleFromTime"] !== '' && rowData["cotrimoxazoleFromTime"] !== null,
                        compareDate: rowData["cotrimoxazoleFromTime"] !== '' && rowData["cotrimoxazoleFromTime"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {

                        validDate: "Ngày bắt đầu Cotrimoxazole không hợp lệ",
                        compareDate: "Ngày bắt đầu Cotrimoxazole phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày bắt đầu Cotrimoxazole phải nhỏ hơn ngày hiện tại"
                    };
                }

                // Ngày kết thúc Cotrimoxazole
                if (name.indexOf('cotrimoxazoleToTime') !== -1) {
                    rowData["cotrimoxazoleToTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["cotrimoxazoleToTime"] !== '' && rowData["cotrimoxazoleToTime"] !== null,
                        compareDate: rowData["cotrimoxazoleToTime"] !== '' && rowData["cotrimoxazoleToTime"] !== null && rowData["cotrimoxazoleFromTime"] !== '' && rowData["cotrimoxazoleFromTime"] !== null ? rowData["cotrimoxazoleFromTime"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {

                        validDate: "Ngày kết thúc Cotrimoxazole không hợp lệ",
                        compareDate: "Ngày kết thúc Cotrimoxazole phải lớn hơn ngày bắt đầu Cotrimoxazole",
                        invalidFutureDate: "Ngày kết thúc Cotrimoxazole phải nhỏ hơn ngày hiện tại"
                    };

                    // Validate start cotrima
                    rules[startCotrima] = {
                        required: rowData["cotrimoxazoleToTime"] !== '' && rowData["cotrimoxazoleToTime"] !== null,
                        validDate: rowData["cotrimoxazoleFromTime"] !== '' && rowData["cotrimoxazoleFromTime"] !== null,
                        compareDate: rowData["cotrimoxazoleFromTime"] !== '' && rowData["cotrimoxazoleFromTime"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: true
                    };


                    messages[startCotrima] = {
                        required: "Ngày bắt đầu Cotrimoxazole không được để trống",
                        validDate: "Ngày bắt đầu Cotrimoxazole không hợp lệ",
                        compareDate: "Ngày bắt đầu Cotrimoxazole phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày bắt đầu Cotrimoxazole phải nhỏ hơn ngày hiện tại"
                    };

                }

                // Ngày lấy mẫu PCR lần 1
                if (name.indexOf('pcrBloodOneTime') !== -1) {
                    rowData["pcrBloodOneTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["pcrBloodOneTime"] !== '' && rowData["pcrBloodOneTime"] !== null,
                        compareDate: rowData["pcrBloodOneTime"] !== '' && rowData["pcrBloodOneTime"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {
                        validDate: "Ngày lấy mẫu PCR lần 1 không hợp lệ",
                        compareDate: "Ngày lấy mẫu PCR lần 1 phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày lấy mẫu PCR lần 1 phải nhỏ hơn ngày hiện tại"
                    };
                }

                // Ngày XN PCR lần 1
                if (name.indexOf('pcrOneTime') !== -1) {
                    rowData["pcrOneTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["pcrOneTime"] !== '' && rowData["pcrOneTime"] !== null,
                        compareDate: rowData["pcrOneTime"] !== '' && rowData["pcrOneTime"] !== null && rowData["pcrBloodOneTime"] !== '' && rowData["pcrBloodOneTime"] !== null ? rowData["pcrBloodOneTime"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {
                        validDate: "Ngày XN PCR lần 1 không hợp lệ",
                        compareDate: "Ngày XN PCR lần 1 phải lớn hơn ngày lấy mẫu PCR lần 1",
                        invalidFutureDate: "Ngày XN PCR lần 1 phải nhỏ hơn ngày hiện tại"
                    };
                }

                // Ngày lấy mẫu PCR lần 2
                if (name.indexOf('pcrBloodTwoTime') !== -1) {
                    rowData["pcrBloodTwoTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["pcrBloodTwoTime"] !== '' && rowData["pcrBloodTwoTime"] !== null,
                        compareDate: rowData["pcrBloodTwoTime"] !== '' && rowData["pcrBloodTwoTime"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {
                        validDate: "Ngày lấy mẫu PCR lần 2 không hợp lệ",
                        compareDate: "Ngày lấy mẫu PCR lần 2 phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày lấy mẫu PCR lần 2 phải nhỏ hơn ngày hiện tại"
                    };
                }

                // Ngày XN PCR lần 2
                if (name.indexOf('pcrTwoTime') !== -1) {
                    rowData["pcrTwoTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["pcrTwoTime"] !== '' && rowData["pcrTwoTime"] !== null,
                        compareDate: rowData["pcrTwoTime"] !== '' && rowData["pcrTwoTime"] !== null && rowData["pcrBloodTwoTime"] !== '' && rowData["pcrBloodTwoTime"] !== null ? rowData["pcrBloodTwoTime"] : '',
                        compareDatePcr: rowData["pcrTwoTime"] !== '' && rowData["pcrTwoTime"] !== null && rowData["pcrOneTime"] !== '' && rowData["pcrOneTime"] !== null ? rowData["pcrOneTime"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {
                        validDate: "Ngày XN PCR lần 2 không hợp lệ",
                        compareDate: "Ngày XN PCR lần 2 phải lớn hơn ngày lấy mẫu PCR lần 2",
                        compareDatePcr: "Ngày XN PCR lần 2 phải lớn hơn ngày XN PCR lần 1",
                        invalidFutureDate: "Ngày XN PCR lần 2 phải nhỏ hơn ngày hiện tại"
                    };
                }

                // Ngày điều trị ARV cho con
                if (name.indexOf('treatmentTime') !== -1) {
                    rowData["treatmentTime"] = $("[name='" + name + "']").val() + '';
                    rules[this.name] = {
                        validDate: rowData["treatmentTime"] !== '' && rowData["treatmentTime"] !== null,
                        compareDate: rowData["treatmentTime"] !== '' && rowData["treatmentTime"] !== null && rowData["pcrTwoTime"] !== '' && rowData["pcrTwoTime"] !== null ? rowData["pcrTwoTime"] : '',
                        compareDatePcr: rowData["treatmentTime"] !== '' && rowData["treatmentTime"] !== null && rowData["pcrOneTime"] !== '' && rowData["pcrOneTime"] !== null ? rowData["pcrOneTime"] : '',
                        compareDateDob: rowData["treatmentTime"] !== '' && rowData["treatmentTime"] !== null && rowData["dob"] !== '' && rowData["dob"] !== null ? rowData["dob"] : '',
                        invalidFutureDate: true
                    };
                    messages[this.name] = {
                        validDate: "Ngày điều trị ARV không hợp lệ",
                        compareDate: "Ngày điều trị ARV phải lớn hơn ngày XN PCR lần 2",
                        compareDatePcr: "Ngày điều trị ARV phải lớn hơn ngày XN PCR lần 1",
                        compareDateDob: "Ngày điều trị ARV phải lớn hơn ngày sinh",
                        invalidFutureDate: "Ngày điều trị ARV phải nhỏ hơn ngày hiện tại"
                    };
                }
            });
        });

        var validateOk = form.validate({
            rules: rules,
            messages: messages,
            errorElement: "em",
            errorPlacement: function (error, element) {
                error.addClass("help-block");
                element.parents().addClass("has-feedback");

                if (element.prop("type") === "checkbox") {
                    error.insertAfter(element.parent("label"));
                }
                if (element.prop("class").indexOf('select2-hidden-accessible') != -1) {
                    error.insertAfter($(element.parent("div")).find("span.select2"));
                } else {
                    error.insertAfter(element);
                }
            }
        });

        // Hiển thị dòng lỗi
        $("tbody tr td.has-error").each(function () {
            $(this).parent("tr").children("td").first().children('li').addClass('fa fa-times-circle text-danger').removeClass('fa-gear');
            $(this).children("em").addClass('wrap');
        });

        return validateOk;
    };

    $scope.customSubmit = function (form, $event) {
        var elm = $event.currentTarget;
        $event.preventDefault();
        let flagCheck;
        let flagCheckAgency;
        bootbox.hideAll();

        $(".help-block-error").remove();
        flagCheck = form.validate();
        flagCheckAgency = $scope.validateChildren(form);
        $scope.customerCount($(".fieldwrapper")); // Đếm tổng số khách hàng
        $scope.emptyNotify() // Thông báo nếu không có thông tin bạn tình bạn chích

        if (flagCheck && flagCheckAgency) {
            elm.form.submit();
        } else {
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };

});

app.controller('opc_new_stage', function ($scope, $uibModalInstance, params, msg) {

    $scope.options = options;
    $scope.arv = arv;
    $scope.item = params.model;
    $scope.action = params.action;

    $scope.confirmSiteID = $scope.options["siteConfirm"];
    $scope.registrationType = $scope.options["registration-type"];
    $scope.placeTest = $scope.options["place-test"];
    $scope.clinicalStage = $scope.options["clinical-stage"];
    $scope.statusOfTreatment = $scope.options["status-of-treatment"];
    $scope.statusOfChangeTreatment = $scope.options["status-of-change-treatment"];
    $scope.firstTreatmentRegimen = $scope.options["status-of-treatment"];
    $scope.medicationAdherence = $scope.options["medication-adherence"];
    $scope.treatmentRegimen = $scope.options["treatment-regimen"];
    $scope.treatmentRegimenStage = $scope.options["treatment-regimen-stage"];
    $scope.siteOpcFrom = $scope.options["siteOpcFrom"];
    $scope.siteOpcTo = $scope.options["siteOpcTo"];
    $scope.arvEndCase = $scope.options["arv-end-case"];
    $scope.treatmentFacility = $scope.options["treatment-facility"];

    delete $scope.confirmSiteID[""];
    delete $scope.registrationType[""];
    delete $scope.placeTest[""];
    delete $scope.clinicalStage[""];
    delete $scope.statusOfTreatment[""];
    delete $scope.medicationAdherence[""];
    delete $scope.treatmentRegimen[""];
    delete $scope.treatmentRegimenStage[""];
    delete $scope.siteOpcTo[""];
    delete $scope.treatmentFacility[""];
    delete $scope.siteOpcFrom[""];
    delete $scope.statusOfChangeTreatment[""];
    delete $scope.arvEndCase[""];

    $scope.calculateDay = function (lastExaminationTime, appointmentTime,daysReceived) {
        if($(lastExaminationTime).val() !== 'dd/mm/yyyy' && $(lastExaminationTime).val() != null && $(lastExaminationTime).val() != '' && 
                        $(appointmentTime).val() !== 'dd/mm/yyyy' && $(appointmentTime).val() != null && $(appointmentTime).val() != '' && 
                        ($(daysReceived).val() == null || $(daysReceived).val() == '')){
            var dataSplit1 = $(lastExaminationTime).val().split('/');
            var start = new Date(dataSplit1[1] + '/' + dataSplit1[0] + '/' + dataSplit1[2]);
            var dataSplit2 = $(appointmentTime).val().split('/');
            var end = new Date(dataSplit2[1] + '/' + dataSplit2[0] + '/' + dataSplit2[2]);
            var diff = new Date(end - start);
            var days = diff/1000/60/60/24;
            if(days > 0) {
                $(daysReceived).val(days).change();
            }
        }
    };

    $scope.init = function () {

        if ($scope.action === "create") {
            $scope.item["siteID"] = siteIdDefault;
        }

        $scope.model.treatmentStageID = $scope.item.treatmentStageID;
        $scope.model.treatmentStageTime = $scope.item.treatmentStageTime;

        $("#endTime").val($.datepicker.formatDate('dd/mm/yy', new Date()));

        $("#siteID").attr('disabled', 'disabled');

        if ($scope.action === "view") {
            $('#opcStageForm input, #opcStageForm select').attr('disabled', 'disabled');
        }

        if (params.model) {
            $scope.item = params.model;
        }

        if ($scope.action !== "view") {
            $('#siteID').append($('<option>', {value: '', text: 'Chọn cơ sở điều trị'}));
            $('#registrationType').append($('<option>', {value: '', text: 'Chọn loại đăng ký'}));
            $('#sourceSiteID').append($('<option>', {value: '', text: 'Chọn cơ sở'}));
            $('#clinicalStage').append($('<option>', {value: '', text: 'Chọn giai đoạn'}));
            $('#statusOfTreatmentID').append($('<option>', {value: '', text: 'Chọn trạng thái điều trị'}));
            $('#medicationAdherence').append($('<option>', {value: '', text: 'Chọn mức độ tuân thủ'}));
            $('#endCase').append($('<option>', {value: '', text: 'Chọn lý do'}));
            $('#firstTreatmentRegimenID').append($('<option>', {value: '', text: 'Chọn phác đồ'}));
            $('#treatmentRegimenStage').append($('<option>', {value: '', text: 'Chọn bậc phác đồ'}));
            $('#treatmentRegimenID').append($('<option>', {value: '', text: 'Chọn phác đồ'}));
            $('#transferSiteID').append($('<option>', {value: '', text: 'Chọn cơ sở'}));
            $('#treatmentStageID').append($('<option>', {value: '', text: 'Chọn trạng thái biến động'}));

        }

        if ($scope.action === "create") {
            $scope.item.statusOfTreatmentID = "0";
        }

        if ($scope.item.opc && !$scope.item.opcManager) {
            $("#siteID").attr('disabled', 'disabled');
        }

        if ($scope.item.registrationType === "3" && $scope.action !== "view") {
            $("#sourceSiteID").removeAttr("disabled").change();
        }

        if ($("#registrationType").on("change", () => {
            $scope.getSourceSiteID();
        }))
            ;

        if ($scope.item.sourceSiteID === "-1" && $scope.action !== "view") {
            $("#sourceSiteName").removeAttr("disabled").change();
        }

        if ($("#sourceSiteID").on("change", () => {
            $scope.getSourceSiteName();
        }))
            ;

        // Cơ sở chuyển đến phần BDDT
        if ($scope.item.endCase === "3" && $scope.action !== "view") {
            $("#transferSiteID").removeAttr("disabled").change();
        }

        if ($("#endCase").on("change", () => {
            if ($("#endCase").val() === "3") {
                $("#transferSiteID").removeAttr("disabled").change();
            } else {
                $("#transferSiteID").attr('disabled', 'disabled').val('').change();
            }
        }))
            ;

        if ($scope.item.transferSiteID === "-1" && $scope.action !== "view") {
            $("#transferSiteName").removeAttr("disabled").change();
        }

        if ($("#transferSiteID").on("change", () => {
            if ($("#transferSiteID").val() === "-1") {
                $("#transferSiteName").removeAttr("disabled").change();
            } else {
                $("#transferSiteName").attr('disabled', 'disabled').val('').change();
            }
        }))
            ;

        // Cơ sở chuyển đến phần BDDT
        if ($scope.item.endCase === "3" && $scope.action !== "view") {
            $("#transferCase").removeAttr("disabled").val('').change();
        } else {
            $("#transferCase").attr('disabled', 'disabled').val('').change();
        }

        if ($("#endCase").on("change", () => {
            if ($("#endCase").val() === "3") {
                $("#transferCase").removeAttr("disabled").val('').change();
            } else {
                $("#transferCase").attr('disabled', 'disabled').val('').change();
            }
        }))
            ;

        // Tự động load Trạng thái biến động (ưu tiên lý do kết thúc
        if ($scope.item.endCase === "3") { // Lý do kết thúc
            $scope.item.treatmentStageID = "3";
        } else if ($scope.item.endCase === "4") { // Mất dấu
            $scope.item.treatmentStageID = "5";
        } else if ($scope.item.endCase === "1") { // Bỏ trị
            $scope.item.treatmentStageID = "6";
        } else if ($scope.item.endCase === "2") { // Tử vong
            $scope.item.treatmentStageID = "7";
        } else if ($scope.item.endCase === "5") { // Kết thúc
            $scope.item.treatmentStageID = "8";
        } else if ($scope.item.registrationType === "1") { // đăng ký mới
            $scope.item.treatmentStageID = "1";
        } else if ($scope.item.registrationType === "2") { // điều trị lại
            $scope.item.treatmentStageID = "2";
        } else if ($scope.item.registrationType === "3") { // chuyển đến
            $scope.item.treatmentStageID = "4";
        } else if ($scope.item.registrationType === "4") { // Trẻ em dưới 18 tuổi
            $scope.item.treatmentStageID = "1";
        } else if ($scope.item.registrationType === "5") { // Phơi nhiễm
            $scope.item.treatmentStageID = "1";
        }

        $("#registrationType").on("change", () => {
            // Tự động load Trạng thái biến động (ưu tiên lý do kết thúc
            if ($("#endCase").val() === "string:3") { // Lý do kết thúc
                $("#treatmentStageID").val("string:3").change();
            } else if ($("#endCase").val() === "string:4") { // Mất dấu
                $("#treatmentStageID").val("string:5").change();
            } else if ($("#endCase").val() === "string:1") { // Bỏ trị
                $("#treatmentStageID").val("string:6").change();
            } else if ($("#endCase").val() === "string:2") { // Tử vong
                $("#treatmentStageID").val("string:7").change();
            } else if ($("#endCase").val() === "string:5") { // Kết thúc
                $("#treatmentStageID").val("string:8").change();
            } else if ($("#registrationType").val() === "string:1") { // đăng ký mới
                $("#treatmentStageID").val("string:1").change();
            } else if ($("#registrationType").val() === "string:2") { // điều trị lại
                $("#treatmentStageID").val("string:2").change();
            } else if ($("#registrationType").val() === "string:3") { // chuyển đến
                $("#treatmentStageID").val("string:4").change();
            } else if ($("#registrationType").val() === "string:4") { // Trẻ em dưới 18 tuổi
                $("#treatmentStageID").val("string:1").change();
            } else if ($("#registrationType").val() === "string:5") { // Phơi nhiễm
                $("#treatmentStageID").val("string:1").change();
            }

            // Set mặc định hiển thi mã bệnh án
            $scope.getSourceCode();

            if ($scope.item.id === null || $scope.item.id === '') {
                if ($("#endTime").val() !== null && $("#endTime").val() !== '') {
                    $("#treatmentStageTime").val($("#endTime").val());
                } else if ($("#registrationTime").val() !== null && $("#registrationTime").val() !== '') {
                    $("#treatmentStageTime").val($("#registrationTime").val());
                }
            }
        });

        $("#endCase").on("change", () => {
            // Tự động load Trạng thái biến động (ưu tiên lý do kết thúc
            if ($("#endCase").val() === "string:3") { // Lý do kết thúc
                $("#treatmentStageID").val("string:3").change();
            } else if ($("#endCase").val() === "string:4") { // Mất dấu
                $("#treatmentStageID").val("string:5").change();
            } else if ($("#endCase").val() === "string:1") { // Bỏ trị
                $("#treatmentStageID").val("string:6").change();
            } else if ($("#endCase").val() === "string:2") { // Tử vong
                $("#treatmentStageID").val("string:7").change();
            } else if ($("#endCase").val() === "string:5") { // Kết thúc
                $("#treatmentStageID").val("string:8").change();
            } else if ($("#registrationType").val() === "string:1") { // đăng ký mới
                $("#treatmentStageID").val("string:1").change();
            } else if ($("#registrationType").val() === "string:2") { // điều trị lại
                $("#treatmentStageID").val("string:2").change();
            } else if ($("#registrationType").val() === "string:3") { // chuyển đến
                $("#treatmentStageID").val("string:4").change();
            } else if ($("#registrationType").val() === "string:4") { // Trẻ em dưới 18 tuổi
                $("#treatmentStageID").val("string:1").change();
            } else if ($("#registrationType").val() === "string:5") { // Phơi nhiễm
                $("#treatmentStageID").val("string:1").change();
            }

            if ($scope.item.id === null || $scope.item.id === '') {
                if ($("#endTime").val() !== null && $("#endTime").val() !== '') {
                    $("#treatmentStageTime").val($("#endTime").val());
                } else if ($("#registrationTime").val() !== null && $("#registrationTime").val() !== '') {
                    $("#treatmentStageTime").val($("#registrationTime").val());
                }
            }


        });

        $("#lastExaminationTime, #daysReceived").change(function () {
            if ($("#lastExaminationTime").val() !== 'dd/mm/yyyy' && $("#lastExaminationTime").val() != null && $("#lastExaminationTime").val() != '' &&
                    $("#daysReceived").val() != null && $("#daysReceived").val() != '') {
                var dataSplit = $("#lastExaminationTime").val().split('/');
                var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                var dayNumer = isNaN(parseInt($("#daysReceived").val())) ? 0 : parseInt($("#daysReceived").val());
                formatedDate.setDate(formatedDate.getDate() + dayNumer);
                $("#appointmentTime").val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
            }
            $scope.calculateDay("#lastExaminationTime","#appointmentTime","#daysReceived");
        });
        $("#appointmentTime").change(function () {
            $scope.calculateDay("#lastExaminationTime","#appointmentTime","#daysReceived");
        });

        // Cập nhật ngày biến động điều trị
        $("#registrationTime").change(function () {
            if ($("#endTime").val() === null || $("#endTime").val() === '' || $("#endTime").val().includes('d') || $("#endTime").val().includes('m') || $("#endTime").val().includes('y')) {
                if ($("#registrationType").val() !== null && $("#registrationType").val() !== '') {
                    $("#treatmentStageTime").val($("#registrationTime").val());
                    $scope.item.treatmentStageTime = $("#registrationTime").val();
                }
            }
        });

        $("#endTime").change(function () {
            $scope.getTreatmentStageTime();
        });

        $("#endCase").change(function () {
            $scope.getTreatmentStageTime();
        });

        // Set mặc định hiển thi mã bệnh án
        if ($scope.item.registrationType === "3" && $scope.action !== "view") {
            $("#sourceCode").removeAttr("disabled");
        } else {
            $("#sourceCode").attr("disabled", "disabled");
        }

//        $('#statusOfTreatmentID option[value="string:2"]').text("Đang chờ điều trị (pre-ARV)");

        if ($scope.item.statusOfTreatmentID === "2") {
            $scope.item.treatmentTime = "";
            $scope.item.treatmentRegimenStage = "";
            $scope.item.treatmentRegimenID = "";
            $scope.item.daysReceived = "";
            $("#treatmentTime").attr('disabled', 'disabled').val('').change();
            $("#treatmentRegimenStage").attr('disabled', 'disabled').val('').change();
            $("#treatmentRegimenID").attr('disabled', 'disabled').val('').change();
            $("#daysReceived").attr('disabled', 'disabled').val('').change();
        }

        if ($("#statusOfTreatmentID").on("change", () => {
            if ($("#statusOfTreatmentID").val() === "string:2") {
                $("#treatmentTime").attr('disabled', 'disabled').val('').change();
                $("#treatmentRegimenStage").attr('disabled', 'disabled').val('').change();
                $("#treatmentRegimenID").attr('disabled', 'disabled').val('').change();
                $("#daysReceived").attr('disabled', 'disabled').val('').change();
            } else {
                $("#treatmentTime").removeAttr("disabled").change();
                $("#treatmentRegimenStage").removeAttr("disabled").change();
                $("#treatmentRegimenID").removeAttr("disabled").change();
                $("#daysReceived").removeAttr("disabled").change();
            }
        }))
            ;

        // Ngày kết thúc bằng ngày hiện tại cho sửa
        $("#endTime").val($.datepicker.formatDate('dd/mm/yy', new Date()));
        $scope.model.endTime = $.datepicker.formatDate('dd/mm/yy', new Date());
    };

    // Set lại giá trị ( khi ảnh hưởng dữ liệu từ trường khác)
    $scope.getTreatmentStageTime = () => {
        if ($("#endTime").val() !== null && $("#endTime").val() !== '' && !$("#endTime").val().includes('d') && !$("#endTime").val().includes('m') && !$("#endTime").val().includes('y') && $("#endCase").val() !== '') {
            $("#treatmentStageTime").val($("#endTime").val());
            $scope.item.treatmentStageTime = $("#endTime").val();
        }
        if (($("#endTime").val() === null || $("#endTime").val() === '' || $("#endTime").val().includes('d') || $("#endTime").val().includes('m') || $("#endTime").val().includes('y')) &&
                ($("#registrationType").val() !== null && $("#registrationType").val() !== '') &&
                $("#registrationTime").val() !== null && $("#registrationTime").val() !== '' && !$("#registrationTime").val().includes('d') && !$("#registrationTime").val().includes('m') && !$("#registrationTime").val().includes('y')) {
            $("#treatmentStageTime").val($("#registrationTime").val());
            $scope.item.treatmentStageTime = $("#registrationTime").val();
        }
    }

    $scope.getSourceSiteID = () => {
        if ($("#registrationType").val() === "string:3") {
            $scope.item.sourceSiteID = "";
            $("#sourceSiteID").removeAttr("disabled").val("").change();
        } else {
            $scope.item.sourceSiteID = "";
            $("#sourceSiteID").attr("disabled", "disabled").val("").change();
        }
    }

    $scope.getSourceCode = () => {
        if ($("#registrationType").val() === "string:3" && $scope.action !== "view") {
            $scope.item.sourceCode = "";
            $("#sourceCode").removeAttr("disabled").val("");
        } else {
            $scope.item.sourceCode = "";
            $("#sourceCode").attr("disabled", "disabled").val("");
        }
    }

    $scope.getSourceSiteName = () => {
        if ($("#sourceSiteID").val() === "-1") {
            $scope.item.sourceSiteName = "";
            $("#sourceSiteName").removeAttr("disabled").val("").change();
        } else {
            $scope.item.sourceSiteName = "";
            $("#sourceSiteName").attr("disabled", "disabled").val("").change();
            $("#sourceSiteNameHidden").attr("disabled", "disabled").val("").change();
        }
    }

    $scope.statusOfTreatmentIDChange = function () {
        if ($("#statusOfTreatmentID").val() === 'string:2') {
            $scope.item.treatmentTime = "";
            $scope.item.treatmentRegimenStage = '';
            $scope.item.treatmentRegimenID = '';
            $scope.item.daysReceived = "";
            $("#treatmentTime").attr("disabled", "disabled").change();
            $("#treatmentRegimenStage").attr("disabled", "disabled").change();
            $("#treatmentRegimenID").attr("disabled", "disabled").change();
            $("#daysReceived").attr("disabled", "disabled").change();
        } else {
            $("#treatmentTime").removeAttr("disabled").change();
            $("#treatmentRegimenStage").removeAttr("disabled").change();
            $("#treatmentRegimenID").removeAttr("disabled").change();
            $("#daysReceived").removeAttr("disabled").change();
        }
    };

    $scope.model = $scope.item;

    $scope.ok = function () {

        let stageID = $scope.item.id;
        stageID = typeof stageID === 'undefined' || stageID === null || stageID === '' ? "" : stageID;

        url = urlUpdate + "?arvid=" + $scope.arv.id + "&stageid=" + stageID;

        loading.show();
        $.ajax({
            url: url,
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
                            location.href = "/backend/opc-stage/update.html?arvid=" + $scope.arv.id;
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
        if ($scope.action !== "view") {
            bootbox.confirm({
                message: "Bạn chắc chắn muốn hủy không?",
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
                        $uibModalInstance.dismiss('cancel');
                    } else {
                    }
                }
            });
        } else {
            $uibModalInstance.dismiss('cancel');
        }
    };
});