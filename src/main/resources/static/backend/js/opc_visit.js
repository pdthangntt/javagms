app.controller('opc_visit', ($scope, msg, $uibModal) => {
    $scope.arv = arv;
    $scope.tab = tab;
    $scope.options = options;
    $scope.isOpcManager = isOpcManager;
    $scope.id = $.getQueryParameters().id;
    $scope.pageRedirect = $.getQueryParameters().page_redirect;
    $scope.init = function () {
        $scope.switchConfig();

        if ($scope.id != null && $scope.pageRedirect === 'yes') {
            setTimeout(function () {
                $scope.dialogReport(urlPrinted + "?oid=" + $scope.id + "&arvid=" + $scope.arv.id, null, "Đơn thuốc");
                $("#pdf-loading").remove();
            }, 300);
        }
    };

    $scope.logs = function (oid) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'opcVisitLogs',
            controller: 'opc_visit_log',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        oid: oid,
                        code: $scope.arv.code
                    };
                }
            }
        });
    };
});

app.controller('opc_visit_log', function ($scope, $uibModalInstance, params) {
    $scope.model = {staffID: 0, testID: params.oid, code: params.code};

    $scope.list = function () {
        loading.show();
        $.ajax({
            url: urlLogs,
            data: {oid: params.oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        for (var i = 0; i < resp.data.logs.length; i++) {
                            resp.data.logs[i].staffID = typeof resp.data.staffs[resp.data.logs[i].staffID] === 'undefined' ? 'Hệ thống' : resp.data.staffs[resp.data.logs[i].staffID];
                        }
                        $scope.logs = resp.data.logs;
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

app.controller('visit_new', ($scope, msg) => {
    $scope.options = options;
    $scope.stageID = utils.getContentOfDefault(form.stageID, '');
    $scope.insurance = utils.getContentOfDefault(form.insurance, '');
    $scope.insuranceNo = utils.getContentOfDefault(form.insuranceNo, '');
    $scope.insuranceType = utils.getContentOfDefault(form.insuranceType, '');
    $scope.insuranceSite = utils.getContentOfDefault(form.insuranceSite, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');
    $scope.insurancePay = utils.getContentOfDefault(form.insurancePay, '');
    $scope.route = utils.getContentOfDefault(form.route, '');
    $scope.lastExaminationTime = utils.getContentOfDefault(form.lastExaminationTime, '');
    $scope.treatmentRegimenStage = utils.getContentOfDefault(form.treatmentRegimenStage, '');
    $scope.treatmentRegimenID = utils.getContentOfDefault(form.treatmentRegimenID, '');
    $scope.regimenStageDate = utils.getContentOfDefault(form.regimenStageDate, '');
    $scope.regimenDate = utils.getContentOfDefault(form.regimenDate, '');
    $scope.causesChange = utils.getContentOfDefault(form.causesChange, '');
    $scope.circuit = utils.getContentOfDefault(form.circuit, '');
    $scope.bloodPressure = utils.getContentOfDefault(form.bloodPressure, '');
    $scope.bodyTemperature = utils.getContentOfDefault(form.bodyTemperature, '');

    $scope.patientWeight = utils.getContentOfDefault(form.patientWeight, '');
    $scope.patientHeight = utils.getContentOfDefault(form.patientHeight, '');
    $scope.clinical = utils.getContentOfDefault(form.clinical, '');
    $scope.diagnostic = utils.getContentOfDefault(form.diagnostic, '');
    $scope.medicationAdherence = utils.getContentOfDefault(form.medicationAdherence, '');
    $scope.daysReceived = utils.getContentOfDefault(form.daysReceived, '');
    $scope.appointmentTime = utils.getContentOfDefault(form.appointmentTime, '');
    $scope.supplyMedicinceDate = utils.getContentOfDefault(form.supplyMedicinceDate, '');
    $scope.receivedWardDate = utils.getContentOfDefault(form.receivedWardDate, '');
    $scope.msg = utils.getContentOfDefault(form.msg, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.objectID = utils.getContentOfDefault(form.objectID, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.birthPlanDate = utils.getContentOfDefault(form.birthPlanDate, '');
    $scope.oldTreatmentRegimenStage = utils.getContentOfDefault(form.oldTreatmentRegimenStage, '');
    $scope.oldTreatmentRegimenID = utils.getContentOfDefault(form.oldTreatmentRegimenID, '');
    $scope.statusOfTreatmentID = form.stageID == null || form.stageID == '' ? '' : form.stageID.split("-")[3];

    $scope.baseTreatmentRegimenStage = utils.getContentOfDefault(form.baseTreatmentRegimenStage, '');
    $scope.baseTreatmentRegimenID = utils.getContentOfDefault(form.baseTreatmentRegimenID, '');
//    if(form.id != null){
//        $scope.baseTreatmentRegimenStage = utils.getContentOfDefault(form.stageID.split("-")[1] === '0' ? '' : form.stageID.split("-")[1], '');
//        $scope.baseTreatmentRegimenID = utils.getContentOfDefault(form.stageID.split("-")[2] === '0' ? '' : form.stageID.split("-")[2], '');
//    }

    $scope.items = {
        insuranceType: "#insuranceType",
        insurancePay: "#insurancePay",
        insuranceNo: "#insuranceNo"
    };

    $scope.add = function () {};
    
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
            if(days > 0){
                $(daysReceived).val(days).change();
            }
        }
    };
    
    $scope.init = function () {
        $("#note").attr("rows", "3");
        $scope.$parent.select_search("#insuranceType", "");
        $scope.$parent.select_search("#treatmentRegimenID", "");
        $scope.$parent.select_search("#objectID", "");


        if ($("#clinical").val() == null || $("#clinical").val() == '') {
            $("#clinical").val('Bình thường').change();
        }
        if (($("#daysReceived").val() == null || $("#daysReceived").val() == '') && form.id == null && $scope.statusOfTreatmentID != '2') {
            $("#daysReceived").val('30').change();
        }
        if ($("#insurance").val() != '1') {
            $("#insuranceNo").attr("disabled", "disabled");
            $("#insuranceType").attr("disabled", "disabled");
            $("#insuranceSite").attr("disabled", "disabled");
            $("#insuranceExpiry").attr("disabled", "disabled");
            $("#insurancePay").attr("disabled", "disabled");
            $("#route").attr("disabled", "disabled");
        }

        if ($("#objectID").val() != '5' && $("#objectID").val() != '5.1' && $("#objectID").val() != '5.2') {
            $("#pregnantStartDate").attr("disabled", "disabled");
            $("#pregnantEndDate").attr("disabled", "disabled");
            $("#birthPlanDate").attr("disabled", "disabled");
            $(".tbl_child").attr("disabled", "disabled");
        } else {
            $(".tbl_child").removeAttr("disabled");
            $("#pregnantStartDate").removeAttr("disabled");
            $("#pregnantEndDate").removeAttr("disabled");
            $("#birthPlanDate").removeAttr("disabled");
        }

        if ($scope.oldTreatmentRegimenID != '') {
            $("#oldTreatmentRegimen").val(pOptions['treatment-regimen'][$scope.oldTreatmentRegimenID]).change();
        }

        if ($scope.oldTreatmentRegimenStage != '') {
            $("#oldRegimenStage").val(pOptions['treatment-regimen-stage'][$scope.oldTreatmentRegimenStage]).change();
        }

        $("#regimenStageDate").attr("disabled", "disabled");
        $("#regimenDate").attr("disabled", "disabled");
        $("#oldRegimenStage").attr("disabled", "disabled");
        $("#oldTreatmentRegimen").attr("disabled", "disabled");
        $("#causesChange").attr("disabled", "disabled");

        $("#causesChangeHidden").removeAttr("disabled").change();
        if ($scope.baseTreatmentRegimenStage != '' && $scope.treatmentRegimenStage != null && $scope.treatmentRegimenStage != '' && $scope.treatmentRegimenStage != 'string:' &&
                $scope.baseTreatmentRegimenStage !== $scope.treatmentRegimenStage) {
            $("#regimenStageDate").removeAttr("disabled").change();
            $("#regimenStageDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
            $("#causesChange").removeAttr("disabled").change();
            $("#causesChangeHidden").attr("disabled", "disabled").change();
            $("#oldRegimenStage").val(pOptions['treatment-regimen-stage'][$scope.baseTreatmentRegimenStage]).change();
        }

        if ($scope.baseTreatmentRegimenID != '' && $("#treatmentRegimenID").val() != null && $("#treatmentRegimenID").val() != '' &&
                $scope.baseTreatmentRegimenID !== $("#treatmentRegimenID").val()) {
            $("#regimenDate").removeAttr("disabled").change();
            $("#regimenDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
            $("#causesChange").removeAttr("disabled").change();
            $("#causesChangeHidden").attr("disabled", "disabled").change();
            $("#oldTreatmentRegimen").val(pOptions['treatment-regimen'][$scope.baseTreatmentRegimenID]).change();
        }
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
        $("#insurance").change(function () {
            if ($("#insurance").val() != '1') {
                $("#insuranceNo").attr("disabled", "disabled");
                $("#insuranceType").attr("disabled", "disabled");
                $("#insuranceSite").attr("disabled", "disabled");
                $("#insuranceExpiry").attr("disabled", "disabled");
                $("#insurancePay").attr("disabled", "disabled");
                $("#route").attr("disabled", "disabled");
                $("#insuranceNo").val("").change();
                $("#insuranceType").val("").change();
                $("#insuranceSite").val("").change();
                $("#insuranceExpiry").val("").change();
                $("#insurancePay").val("").change();
                $("#route").val("").change();
            } else {
                $("#insuranceNo").removeAttr("disabled").change();
                $("#insuranceType").removeAttr("disabled").change();
                $("#insuranceSite").removeAttr("disabled").change();
                $("#insuranceExpiry").removeAttr("disabled").change();
                $("#insurancePay").removeAttr("disabled").change();
                $("#route").removeAttr("disabled").change();
            }
        });



        $("#treatmentRegimenID").change(function () {
            if ($scope.baseTreatmentRegimenID != '' && $("#treatmentRegimenID").val() != null && $("#treatmentRegimenID").val() != '' &&
                    $scope.baseTreatmentRegimenID !== $("#treatmentRegimenID").val() &&
                    ((arv.treatmentRegimenStage != null && arv.treatmentRegimenStage != '' &&
                            arv.treatmentRegimenID != null && arv.treatmentRegimenID != '') || form.id != null)) {
                $("#oldTreatmentRegimen").val(pOptions['treatment-regimen'][$scope.baseTreatmentRegimenID]).change();
                $("#oldTreatmentRegimenID").val($scope.baseTreatmentRegimenID).change();
                $("#regimenDate").removeAttr("disabled").change();
                $("#regimenDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                $("#causesChange").removeAttr("disabled").change();
                $("#causesChange").val("").change();
                $("#causesChangeHidden").attr("disabled", "disabled").change();
            } else {
                $("#oldTreatmentRegimen").val('').change();
                $("#oldTreatmentRegimenID").val('').change();
                $("#regimenDate").attr("disabled", "disabled").change();
                $("#regimenDate").val('').change();
                if ($scope.baseTreatmentRegimenStage != '' && $scope.treatmentRegimenStage != null && $scope.treatmentRegimenStage != '' &&
                        $scope.baseTreatmentRegimenStage === $scope.treatmentRegimenStage) {
                    $("#causesChange").attr("disabled", "disabled").change();
                    $("#causesChange").val("").change();
                    $("#causesChangeHidden").removeAttr("disabled").change();
                }
            }
        });

        $("#objectID").change(function () {
            if ($("#objectID").val() == '5' || $("#objectID").val() == '5.1' || $("#objectID").val() == '5.2') {
                $("#pregnantStartDate").removeAttr("disabled").change();
                $("#pregnantEndDate").removeAttr("disabled").change();
                $("#birthPlanDate").removeAttr("disabled").change();
                $(".tbl_child").removeAttr("disabled");
            } else {
                $("#pregnantStartDate").attr("disabled", "disabled");
                $("#pregnantEndDate").attr("disabled", "disabled");
                $("#pregnantStartDate").val('').change();
                $("#pregnantEndDate").val('').change();
                $("#birthPlanDate").attr("disabled", "disabled");
                $("#birthPlanDate").val('').change();
                $(".tbl_child").val('').change();
                $(".tbl_child").attr("disabled", "disabled");
            }
        });

        if ($scope.statusOfTreatmentID == '2') {
            $("#treatmentRegimenStage").attr("disabled", "disabled");
            $("#treatmentRegimenID").attr("disabled", "disabled");
            $("#daysReceived").attr("disabled", "disabled");
            $("#supplyMedicinceDate").attr("disabled", "disabled");
            $("#receivedWardDate").attr("disabled", "disabled");
        }

        $("#stageID").change(function () {
            if ($("#stageID").val() !== '') {
//                if(form.id != null){
                $scope.baseTreatmentRegimenStage = $("#stageID").val().split("-")[1] === '0' ? '' : $("#stageID").val().split("-")[1];
                $scope.baseTreatmentRegimenID = $("#stageID").val().split("-")[2] === '0' ? '' : $("#stageID").val().split("-")[2];
                $scope.statusOfTreatmentID = $("#stageID").val().split("-")[3] === '0' ? '' : $("#stageID").val().split("-")[3];

                $("#baseTreatmentRegimenStage").val($scope.baseTreatmentRegimenStage).change();
                $("#baseTreatmentRegimenID").val($scope.baseTreatmentRegimenID).change();
                $("#statusOfTreatmentID").val($scope.statusOfTreatmentID).change();
//                }
                loading.show();
                $.ajax({
                    url: '/service/opc-visit/get-date.json',
                    data: {oid: $("#stageID").val().split("-")[0]},
                    method: 'POST',
                    success: function (resp) {
                        loading.hide();
                        if (resp.success) {
                            $("#treatmentTime").val(resp.data.treatmentTime).change();
                            $("#registrationTime").val(resp.data.registrationTime).change();
                            $("#endTime").val(resp.data.endTime).change();
                        }
                    }
                });
            }
        });

        $("#stageID").change(function () {
            if ($("#stageID").val() !== '') {
                if ($scope.statusOfTreatmentID == '2') {
                    $("#treatmentRegimenStage").attr("disabled", "disabled");
                    $("#treatmentRegimenID").attr("disabled", "disabled");
                    $("#daysReceived").attr("disabled", "disabled");
                    $("#supplyMedicinceDate").attr("disabled", "disabled");
                    $("#receivedWardDate").attr("disabled", "disabled");

                    $("#treatmentRegimenStage").val('').change();
                    $("#treatmentRegimenID").val('').change();
                    $("#daysReceived").val('').change();
                    $("#supplyMedicinceDate").val('').change();
                    $("#receivedWardDate").val('').change();
                } else {
                    $("#treatmentRegimenStage").removeAttr("disabled").change();
                    $("#treatmentRegimenID").removeAttr("disabled").change();
                    $("#daysReceived").removeAttr("disabled").change();
                    $("#supplyMedicinceDate").removeAttr("disabled").change();
                    $("#receivedWardDate").removeAttr("disabled").change();
                }
            }

        });
        // Fill trường BHYT
        $($scope.items.insuranceNo).val($($scope.items.insuranceNo).val().toUpperCase()).change();
        $($scope.items.insuranceNo).change(function () {
            if ($($scope.items.insuranceNo).val() !== '') {
                if ($($scope.items.insuranceNo).val().length === 15) {
                    var myMap = pOptions['insurance-type'];
                    for (var s in myMap) {
                        if ($($scope.items.insuranceNo).val().substring(0, 2).toUpperCase() === myMap[s].substring(0, 2)) {
                            $($scope.items.insuranceType).val(s).change();
                            break;
                        }
                    }
                    if ($($scope.items.insuranceNo).val().substring(2, 3) === '1' || $($scope.items.insuranceNo).val().substring(2, 3) === '2' || $($scope.items.insuranceNo).val().substring(2, 3) === '5') {
                        $($scope.items.insurancePay).val('1').change();
                    } else if ($($scope.items.insuranceNo).val().substring(2, 3) === '3') {
                        $($scope.items.insurancePay).val('2').change();
                    } else if ($($scope.items.insuranceNo).val().substring(2, 3) === '4') {
                        $($scope.items.insurancePay).val('3').change();
                    } else {
                        $($scope.items.insurancePay).val('').change();
                    }
                }

            }
        });
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

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));

        // Thông báo nếu không có người được giới thiệu
        $scope.emptyNotify();

        // Lấy thông tin ID cần phản hồi
        $scope.options = options;

        $scope.regimenOptions = $scope.options['treatment-regimen'];
        $scope.regimenOptions1 = $scope.options['treatment-regimen-level1'];
        $scope.regimenOptions2 = $scope.options['treatment-regimen-level2'];
        $scope.regimenOptions3 = $scope.options['treatment-regimen-level3'];

        var regimenOptions = $scope.regimenOptions;
        var regimenOptions1 = $scope.regimenOptions1;
        var regimenOptions2 = $scope.regimenOptions2;
        var regimenOptions3 = $scope.regimenOptions3;

        $scope.treatmentRegimenStageChange = function () {
            if ($scope.baseTreatmentRegimenStage != '' && $scope.treatmentRegimenStage != null && $scope.treatmentRegimenStage != '' && $scope.treatmentRegimenStage != 'string:' &&
                    $scope.baseTreatmentRegimenStage !== $scope.treatmentRegimenStage &&
                    ((arv.treatmentRegimenStage != null && arv.treatmentRegimenStage != '' &&
                            arv.treatmentRegimenID != null && arv.treatmentRegimenID != '') || form.id != null)) {
                $("#oldRegimenStage").val(pOptions['treatment-regimen-stage'][$scope.baseTreatmentRegimenStage]).change();
                $("#oldTreatmentRegimenStage").val($scope.baseTreatmentRegimenStage).change();
                $("#regimenStageDate").removeAttr("disabled").change();
                $("#regimenStageDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                $("#causesChange").removeAttr("disabled").change();
                $("#causesChange").val("").change();
                $("#causesChangeHidden").attr("disabled", "disabled").change();
            } else {
                $("#oldTreatmentRegimenStage").val('').change();
                $("#oldRegimenStage").val('').change();
                $("#regimenStageDate").attr("disabled", "disabled").change();
                $("#regimenStageDate").val('').change();

                if ($scope.baseTreatmentRegimenID != '' && $("#treatmentRegimenID").val() != null && $("#treatmentRegimenID").val() != '' &&
                        $scope.baseTreatmentRegimenID === $("#treatmentRegimenID").val()) {
                    $("#causesChange").attr("disabled", "disabled").change();
                    $("#causesChange").val("").change();
                    $("#causesChangeHidden").removeAttr("disabled").change();
                }
            }



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


    };

    $(".delete-agency").click(function () {
        $(this).parent().parent().remove();

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));
//        $scope.indexForAgency($(".fieldwrapper"));
        $scope.emptyNotify();
    });

    // Thêm dòng động đối tượng bạn tình bạn chích
    $("#add, #add-title").click(function () {

        // Get row index
        var numberCustomer = $(".fieldwrapper").length;
        var rowIndex = numberCustomer;

        var lastField = $("#listPartner tr:last");
        var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
        var fieldWrapper = $("<tr class=\"fieldwrapper\" id=\"field" + intId + "\"/>");
        fieldWrapper.data("idx", intId);
        var fGear = $("<td class=\"text-center order vertical-align-middle\">" + "<li class=\"fa fa-gear\"> </li>" + "<input type=\"hidden\" class=\"form-control tbl_agency child-date\" id=\"id" + rowIndex + ".dob\" name=\"childs[" + rowIndex + "].ID\"/></td>");
        var fDob = $("<td class=\"text-center vertical-align-top wrap max-width-150\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".dob\" name=\"childs[" + rowIndex + "].dob\"/></td>");
        var fPreventiveDate = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".preventiveDate\" name=\"childs[" + rowIndex + "].preventiveDate\"/></td>");
        var fPreventiveEndDate = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".preventiveEndDate\" name=\"childs[" + rowIndex + "].preventiveEndDate\"/></td>");
        var fCotrimoxazoleFromTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".cotrimoxazoleFromTime\" name=\"childs[" + rowIndex + "].cotrimoxazoleFromTime\"/></td>");
        var fCotrimoxazoleToTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".cotrimoxazoleToTime\" name=\"childs[" + rowIndex + "].cotrimoxazoleToTime\"/></td>");
        var fPcrBloodOneTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".pcrBloodOneTime\" name=\"childs[" + rowIndex + "].pcrBloodOneTime\"/></td>");
        var fPcrOneTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".pcrOneTime\" name=\"childs[" + rowIndex + "].pcrOneTime\"/></td>");
        var fPcrOneResult = $("<td class=\"text-center notifyListTD vertical-align-top wrap\"><select class=\"notifyList form-control tbl_agency\" id=\"childs" + rowIndex + ".pcrOneResult\" name=\"childs[" + rowIndex + "].pcrOneResult\"></select></td>");
        var fPcrBloodTwoTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".pcrBloodTwoTime\" name=\"childs[" + rowIndex + "].pcrBloodTwoTime\"/></td>");
        var fPcrTwoTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".pcrTwoTime\" name=\"childs[" + rowIndex + "].pcrTwoTime\"/></td>");
        var fPcrTwoResult = $("<td class=\"text-center notifyListTD vertical-align-top wrap\"><select class=\"notifyList form-control tbl_agency\" id=\"childs" + rowIndex + ".pcrTwoResult\" name=\"childs[" + rowIndex + "].pcrTwoResult\"></select></td>");
        var fTreatmentTime = $("<td class=\"text-center vertical-align-top child-date wrap\"><input placeholder=\"dd\/mm\/yyyy\"  type=\"text\" class=\"form-control tbl_agency child-date\" id=\"childs" + rowIndex + ".treatmentTime\" name=\"childs[" + rowIndex + "].treatmentTime\"/></td>");
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

        $scope.initPCR($("[name='" + "childs[" + rowIndex + "].pcrOneResult" + "']"));
        $scope.initPCR($("[name='" + "childs[" + rowIndex + "].pcrTwoResult" + "']"));

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
    $scope.customSubmit = function (form, isPrinted, $event) {
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
        if (flagCheckAgency && isPrinted === 'yes') {
            $("#isPrinted").val('yes').change();
            elm.form.submit();
        } else if (flagCheckAgency && isPrinted === 'no') {
            elm.form.submit();
        } else {
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };
});

app.controller('visit_view', ($scope) => {
    $scope.stageID = utils.getContentOfDefault(form.stageID, '');
    $scope.insurance = utils.getContentOfDefault(form.insurance, '');
    $scope.insuranceNo = utils.getContentOfDefault(form.insuranceNo, '');
    $scope.insuranceType = utils.getContentOfDefault(form.insuranceType, '');
    $scope.insuranceSite = utils.getContentOfDefault(form.insuranceSite, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');
    $scope.insurancePay = utils.getContentOfDefault(form.insurancePay, '');
    $scope.route = utils.getContentOfDefault(form.route, '');
    $scope.lastExaminationTime = utils.getContentOfDefault(form.lastExaminationTime, '');
    $scope.treatmentRegimenStage = utils.getContentOfDefault(form.treatmentRegimenStage, '');
    $scope.treatmentRegimenID = utils.getContentOfDefault(form.treatmentRegimenID, '');
    $scope.regimenStageDate = utils.getContentOfDefault(form.regimenStageDate, '');
    $scope.regimenDate = utils.getContentOfDefault(form.regimenDate, '');
    $scope.causesChange = utils.getContentOfDefault(form.causesChange, '');
    $scope.circuit = utils.getContentOfDefault(form.circuit, '');
    $scope.bloodPressure = utils.getContentOfDefault(form.bloodPressure, '');
    $scope.bodyTemperature = utils.getContentOfDefault(form.bodyTemperature, '');

    $scope.patientWeight = utils.getContentOfDefault(form.patientWeight, '');
    $scope.patientHeight = utils.getContentOfDefault(form.patientHeight, '');
    $scope.clinical = utils.getContentOfDefault(form.clinical, '');
    $scope.diagnostic = utils.getContentOfDefault(form.diagnostic, '');
    $scope.medicationAdherence = utils.getContentOfDefault(form.medicationAdherence, '');
    $scope.daysReceived = utils.getContentOfDefault(form.daysReceived, '');
    $scope.appointmentTime = utils.getContentOfDefault(form.appointmentTime, '');
    $scope.supplyMedicinceDate = utils.getContentOfDefault(form.supplyMedicinceDate, '');
    $scope.receivedWardDate = utils.getContentOfDefault(form.receivedWardDate, '');
    $scope.msg = utils.getContentOfDefault(form.msg, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.objectID = utils.getContentOfDefault(form.objectID, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.baseTreatmentRegimenStage = utils.getContentOfDefault(form.baseTreatmentRegimenStage, '');
    $scope.baseTreatmentRegimenID = utils.getContentOfDefault(form.baseTreatmentRegimenID, '');
    $scope.oldTreatmentRegimenStage = utils.getContentOfDefault(form.oldTreatmentRegimenStage, '');
    $scope.oldTreatmentRegimenID = utils.getContentOfDefault(form.oldTreatmentRegimenID, '');
    $scope.id = utils.getContentOfDefault(form.ID, '');
    $scope.init = function () {
        $('input, select, textarea').attr('disabled', 'disabled');
        $(".tbl_child").attr("disabled", "disabled");
        $('select').each(function () {
            var value = $(this).children("option:selected").val();
            if (value === '') {
                $('option:selected', this).text('');
            }
        });
    };
    $scope.print = function () {
        setTimeout(function () {
            $scope.dialogReport(urlPrinted + "?oid=" + visit.id + "&arvid=" + arv.id, null, "Đơn thuốc");
            $("#pdf-loading").remove();
        }, 300);
    };
});
