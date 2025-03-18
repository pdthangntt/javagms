app.controller('opc_qr', function ($scope, $timeout, $interval, msg, localStorageService) {
    $scope.code = '';
    $scope.arv_code = '';
    $scope.options = options;
    $scope.autoSec = 30;
    $scope.delay = 0;
    $scope.loading = false;
    $scope.patient = null;
    $scope.currentArv = null;
    $scope.receiveArv = null;
    $scope.arv = null;
    $scope.visit = null;
    $scope.opcVisit = null;
    $scope.error = 'Vui lòng nhập mã QR';
    $scope.opcVisitFrom = null;
    $scope.siteConfirmName = null;
    $scope.siteHtcName = null;
    
    var regimentx = "";

    $scope.init = function () {
        $scope.switchConfig();

        let keyAuto = "opc_qr_auto";
        let vAuto = localStorageService.get(keyAuto);
        $("input#auto").iCheck(vAuto === null || vAuto === '' || vAuto === '0' ? 'uncheck' : 'check');

        let keySec = "opc_qr_sec";
        let vSec = localStorageService.get(keySec);
        $scope.autoSec = vAuto === null || vAuto === '' ? 30 : Number(vSec);

        //check change
        $("#auto").on('ifChanged', function () {
            var checked = $("#auto").is(':checked');
            localStorageService.set(keyAuto, checked ? '1' : '0');
        });
        $("#autoSec").change(function () {
            localStorageService.set(keySec, $(this).val());
        });
    };

    $scope.redirect = function (type, oid) {
        if (type === 'visit') {
            window.open(`/backend/opc-arv/new.html?htc_id=${oid}`);
        } else {
            window.open(`/backend/opc-from-arv/new.html?opc_id=${oid}`);
        }
    };

    $scope.reset = function () {
        $scope.code = '';
        $scope.arv_code = '';
        $scope.loading = false;
        $scope.patient = null;
        $scope.currentArv = null;
        $scope.receiveArv = null;
        $scope.arv = null;
        $scope.visit = null;
        $scope.opcVisit = null;
        $scope.errors = null;
        $scope.error = 'Vui lòng nhập mã QR';
        $scope.errorMsg = '';
    };


    $scope.search = function () {
        if ($scope.promise) {
            $timeout.cancel($scope.promise);
        }
        $scope.promise = $timeout(function () {
            if ($scope.loading && (!$scope.code || $scope.code === '') && (!$scope.arv_code || $scope.arv_code === '')) {
                $scope.error = "Vui lòng nhập mã QR";
                return false;
            }
            let code = $scope.code;
            let arv = $scope.arv_code;
            $scope.loading = true;
            $.ajax({
                url: `/service/opc-qr/scan.json?code=${code}&arv=${arv}`,
                method: 'GET',
                success: function (resp) {
                    $scope.$apply(() => {
                        $scope.loading = false;
                        if (!resp.success) {
                            $scope.error = resp.message;
                        } else {
                            $scope.error = '';
                            $scope.errorMsg = resp.data.error ? resp.data.error : '';
                            $scope.patient = resp.data.patient;
                            $scope.currentArv = resp.data.currentArv;
                            $scope.receiveArv = resp.data.receiveArv;
                            $scope.arv = $scope.currentArv ? $scope.currentArv : $scope.receiveArv;
                            $scope.visit = resp.data.visit;
                            $scope.siteConfirmName = resp.data.siteConfirmName;
                            $scope.siteHtcName = resp.data.siteHtcName;
                            $scope.opcVisit = resp.data.opcVisit;
                            $scope.model = resp.data.opcVisitForm;
                            $scope.options = resp.data.options;
                            //Tự động lưu lượt khám
                            if ($scope.currentArv) {
                                $timeout(() => {
                                    //Init default
                                    $("#stageID option[value='string:']").remove();
                                    $('#stageID').prepend('<option value="string:">Chọn giai đoạn điều trị</option>');

                                    $("#insurance option[value='string:']").remove();
                                    $('#insurance').prepend('<option value="string:">Chọn có bhyt hay không</option>');
                                    $("#treatmentRegimenID option[value='string:']").remove();
                                    if ($scope.model.treatmentRegimenID === null || $scope.model.treatmentRegimenID === '') {
                                        $('#treatmentRegimenID').prepend('<option selected="selected" value="string:">Chọn phác đồ điều trị</option>');
                                    } else {
                                        $('#treatmentRegimenID').prepend('<option value="string:">Chọn phác đồ điều trị</option>');
                                    }
                                    $("#insuranceType option[value='string:']").remove();
                                    if ($scope.model.insuranceType === null || $scope.model.insuranceType === '') {
                                        $('#insuranceType').prepend('<option selected="selected" value="string:">Chọn loại thẻ BHYT</option>');
                                    } else {
                                        $('#insuranceType').prepend('<option value="string:">Chọn loại thẻ BHYT</option>');
                                    }


                                    $("#insurancePay option[value='string:']").remove();
                                    if ($scope.model.insurancePay === null || $scope.model.insurancePay === '') {
                                        $('#insurancePay').prepend('<option selected="selected" value="string:">Chọn tỷ lệ thanh toán BHYT</option>');
                                    } else {
                                        $('#insurancePay').prepend('<option value="string:">Chọn tỷ lệ thanh toán BHYT</option>');
                                    }

                                    $("#route option[value='string:']").remove();
                                    if ($scope.model.route === null || $scope.model.route === '') {
                                        $('#route').prepend('<option selected="selected" value="string:">Chọn tuyến đăng ký bảo hiểm</option>');
                                    } else {
                                        $('#route').prepend('<option value="string:">Chọn tuyến đăng ký bảo hiểm</option>');
                                    }

                                    $("#treatmentRegimenStage option[value='string:']").remove();
                                    if ($scope.model.treatmentRegimenStage === null || $scope.model.treatmentRegimenStage === '') {
                                        $('#treatmentRegimenStage').prepend('<option selected="selected" value="string:">Chọn bậc phác đồ điều trị</option>');
                                    } else {
                                        $('#treatmentRegimenStage').prepend('<option value="string:">Chọn bậc phác đồ điều trị</option>');
                                    }


                                    $("#medicationAdherence option[value='string:']").remove();
                                    if ($scope.model.medicationAdherence === null || $scope.model.medicationAdherence === '') {
                                        $('#medicationAdherence').prepend('<option selected="selected" value="string:">Chọn mức độ tuân thủ</option>');
                                    } else {
                                        $('#medicationAdherence').prepend('<option value="string:">Chọn mức độ tuân thủ</option>');
                                    }

                                    $("#consult option[value='string:']").remove();
                                    if ($scope.model.consult === null || $scope.model.consult === '') {
                                        $('#consult').prepend('<option selected="selected" value="string:">Chọn câu trả lời</option>');
                                    } else {
                                        $('#consult').prepend('<option value="string:">Chọn câu trả lời</option>');
                                    }

                                    $("#objectID option[value='string:']").remove();
                                    if ($scope.model.objectID === null || $scope.model.objectID === '') {
                                        $('#objectID').prepend('<option selected="selected" value="string:">Chọn nhóm đối tượng xét nghiệm</option>');
                                    } else {
                                        $('#objectID').prepend('<option value="string:">Chọn nhóm đối tượng xét nghiệm</option>');
                                    }

                                    $scope.$parent.select_search("#insuranceType", "");
                                    $scope.$parent.select_search("#treatmentRegimenID", "");
                                    $scope.$parent.select_search("#objectID", "");

                                    $("#regimenStageDate").attr("disabled", "disabled");
                                    $("#regimenDate").attr("disabled", "disabled");
                                    $("#oldRegimenStage").attr("disabled", "disabled");
                                    $("#oldTreatmentRegimen").attr("disabled", "disabled");
                                    $("#causesChange").attr("disabled", "disabled");

                                    if ($scope.model.id === null) {
                                        $("#lastExaminationTime").attr("disabled", "disabled");
                                    }

                                    if ($scope.model.oldTreatmentRegimenStage !== null && $scope.model.oldTreatmentRegimenStage !== '') {
                                        $("#oldRegimenStage").val($scope.options['treatment-regimen-stage'][$scope.model.oldTreatmentRegimenStage]);
                                    }
                                    if ($scope.model.oldTreatmentRegimenID !== null && $scope.model.oldTreatmentRegimenID !== '') {
                                        $("#oldTreatmentRegimen").val($scope.options['treatment-regimen'][$scope.model.oldTreatmentRegimenID]);
                                    }
                                    if ($scope.model.statusOfTreatmentID && $scope.model.statusOfTreatmentID === '2') {
                                        $("#treatmentRegimenStage").attr("disabled", "disabled");
                                        $("#treatmentRegimenID").attr("disabled", "disabled");
                                        $("#daysReceived").attr("disabled", "disabled");
                                        $("#daysReceived").val("").change();
                                        $("#supplyMedicinceDate").attr("disabled", "disabled");
                                        $("#receivedWardDate").attr("disabled", "disabled");
                                    }

                                    if ($("#objectID").val() == 'string:5' || $("#objectID").val() == 'string:5.1' || $("#objectID").val() == 'string:5.2') {
                                        $("#pregnantStartDate").removeAttr("disabled").change();
                                        $("#pregnantEndDate").removeAttr("disabled").change();

                                    } else {
                                        $("#pregnantStartDate").attr("disabled", "disabled");
                                        $("#pregnantEndDate").attr("disabled", "disabled");
                                    }

                                    if ($("#insurance").val() == 'string:1') {
                                        $("#insuranceNo").removeAttr("disabled").change();
                                        $("#insuranceType").removeAttr("disabled").change();
                                        $("#insuranceSite").removeAttr("disabled").change();
                                        $("#insuranceExpiry").removeAttr("disabled").change();
                                        $("#insurancePay").removeAttr("disabled").change();
                                        $("#route").removeAttr("disabled").change();
                                    } else {
                                        $("#insuranceNo").attr("disabled", "disabled");
                                        $("#insuranceType").attr("disabled", "disabled");
                                        $("#insuranceSite").attr("disabled", "disabled");
                                        $("#insuranceExpiry").attr("disabled", "disabled");
                                        $("#insurancePay").attr("disabled", "disabled");
                                        $("#route").attr("disabled", "disabled");
                                        $("#insuranceNo").val("").change();
                                        $("#insuranceType").val("string:").change();
                                        $("#insuranceSite").val("").change();
                                        $("#insuranceExpiry").val("").change();
                                        $("#insurancePay").val("string:").change();
                                        $("#route").val("string:").change();
                                    }
                                    if ($scope.model.baseTreatmentRegimenStage !== null && $scope.model.baseTreatmentRegimenStage !== '' &&
                                            $("#treatmentRegimenStage").val() !== null && $("#treatmentRegimenStage").val() !== 'string:' &&
                                            $scope.model.baseTreatmentRegimenStage !== $("#treatmentRegimenStage").val().split(":")[1]) {
                                        $("#regimenStageDate").removeAttr("disabled").change();
                                        $("#regimenStageDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                                        $("#causesChange").removeAttr("disabled").change();
                                        $("#oldRegimenStage").val($scope.options['treatment-regimen-stage'][$scope.model.baseTreatmentRegimenStage]).change();
                                    }

                                    if ($scope.model.baseTreatmentRegimenID !== null && $scope.model.baseTreatmentRegimenID !== '' &&
                                            $("#treatmentRegimenID").val() !== null && $("#treatmentRegimenID").val() !== 'string:' &&
                                            $scope.model.baseTreatmentRegimenID !== $("#treatmentRegimenID").val().split(":")[1]) {
                                        $("#regimenDate").removeAttr("disabled").change();
                                        $("#regimenDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                                        $("#causesChange").removeAttr("disabled").change();
                                        $("#oldTreatmentRegimen").val($scope.options['treatment-regimen'][$scope.model.baseTreatmentRegimenID]).change();
                                    }

                                    //Event
                                    $("#code").keyup(function () {
                                        if ($scope.currentArv) {
                                            $scope.reset();
                                        }
                                    });
                                    $("#insuranceNo").val($("#insuranceNo").val().toUpperCase()).change();
                                    $scope.appointmentTimeChange();
                                    $scope.calculateDay();
                                    $("#objectID").change(function () {
                                        if ($("#objectID").val() == 'string:5' || $("#objectID").val() == 'string:5.1' || $("#objectID").val() == 'string:5.2') {
                                            $("#pregnantStartDate").removeAttr("disabled").change();
                                            $("#pregnantEndDate").removeAttr("disabled").change();

                                        } else {
                                            $("#pregnantStartDate").attr("disabled", "disabled");
                                            $("#pregnantEndDate").attr("disabled", "disabled");
                                        }
                                    });
                                    $scope.insuranceChange();
                                    $("#insuranceNo").change(function () {
                                        $scope.insuranceNoChange();
                                    });
                                    $("#lastExaminationTime, #daysReceived").change(function () {
                                        $scope.appointmentTimeChange();
                                        $scope.calculateDay();
                                    });
                                    $("#appointmentTime").change(function () {
                                        $scope.calculateDay();
                                    });
                                    //phac do thay doi theo bac
                                    $scope.regimenOptions = $scope.options['treatment-regimen'];
                                    $scope.regimenOptions1 = $scope.options['treatment-regimen-level1'];
                                    $scope.regimenOptions2 = $scope.options['treatment-regimen-level2'];
                                    $scope.regimenOptions3 = $scope.options['treatment-regimen-level3'];

                                    var regimenOptions = $scope.regimenOptions;
                                    var regimenOptions1 = $scope.regimenOptions1;
                                    var regimenOptions2 = $scope.regimenOptions2;
                                    var regimenOptions3 = $scope.regimenOptions3;
                                    
                                    


                                    $("#treatmentRegimenStage").change(function () {
                                        if ($scope.model.baseTreatmentRegimenStage != null && $scope.model.baseTreatmentRegimenStage != '' &&
                                                $("#treatmentRegimenStage").val() != null && $("#treatmentRegimenStage").val() != 'string:' &&
                                                $scope.model.baseTreatmentRegimenStage !== $("#treatmentRegimenStage").val().split(":")[1] &&
                                                (($scope.currentArv.treatmentRegimenStage != null && $scope.currentArv.treatmentRegimenStage != '' &&
                                                        $scope.currentArv.treatmentRegimenID != null && $scope.currentArv.treatmentRegimenID != '') || $scope.model.id != null)) {
                                            $("#oldRegimenStage").val($scope.options['treatment-regimen-stage'][$scope.model.baseTreatmentRegimenStage]).change();
                                            $scope.model.oldTreatmentRegimenStage = $scope.model.baseTreatmentRegimenStage;
                                            $("#regimenStageDate").removeAttr("disabled").change();

                                            $("#regimenStageDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                                            $("#causesChange").removeAttr("disabled").change();
                                            $("#causesChange").val("").change();
                                        } else {
                                            $("#oldTreatmentRegimenStage").val('').change();
                                            $("#oldRegimenStage").val('').change();
                                            $("#regimenStageDate").attr("disabled", "disabled").change();
                                            $("#regimenStageDate").val('').change();

                                            if ($scope.model.baseTreatmentRegimenID != null && $scope.model.baseTreatmentRegimenID != '' &&
                                                    $("#treatmentRegimenID").val() != null && $("#treatmentRegimenID").val() != 'string:' &&
                                                    $scope.model.baseTreatmentRegimenID === $("#treatmentRegimenID").val().split(":")[1]) {
                                                $("#causesChange").attr("disabled", "disabled").change();
                                                $("#causesChange").val("").change();
                                            }
                                        }

                                        $('#treatmentRegimenID')
                                                .find('option')
                                                .remove()
                                                .end();

                                        if ($scope.model.treatmentRegimenStage === '1') {
                                            for (const [key, value] of Object.entries(regimenOptions1)) {
                                                $($('#treatmentRegimenID')).append(new Option(value, 'string:' + key, false, false));
                                            }
                                            $($('#treatmentRegimenID')).val("string:");
                                        } else if ($scope.model.treatmentRegimenStage === '2') {
                                            for (const [key, value] of Object.entries(regimenOptions2)) {
                                                $($('#treatmentRegimenID')).append(new Option(value, 'string:' + key, false, false));
                                            }
                                            $($('#treatmentRegimenID')).val("string:");
                                        } else if ($scope.model.treatmentRegimenStage === '3') {
                                            for (const [key, value] of Object.entries(regimenOptions3)) {
                                                $($('#treatmentRegimenID')).append(new Option(value, 'string:' + key, false, false));
                                            }
                                            $($('#treatmentRegimenID')).val("string:");
                                        } else {
                                            for (const [key, value] of Object.entries(regimenOptions)) {
                                                $($('#treatmentRegimenID')).append(new Option(value, 'string:' + key, false, false));
                                            }
                                            $($('#treatmentRegimenID')).val("string:");
                                        }

                                    });

                                    $("#treatmentRegimenID").change(function () {
                                        if ($scope.model.baseTreatmentRegimenID != null && $scope.model.baseTreatmentRegimenID != '' && $("#treatmentRegimenID").val() != '' &&
                                                $("#treatmentRegimenID").val() != null && $("#treatmentRegimenID").val() != 'string:' &&
                                                $scope.model.baseTreatmentRegimenID !== $("#treatmentRegimenID").val().split(":")[1] &&
                                                (($scope.currentArv.treatmentRegimenStage != null && $scope.currentArv.treatmentRegimenStage != '' &&
                                                        $scope.currentArv.treatmentRegimenID != null && $scope.currentArv.treatmentRegimenID != '') || $scope.model.id != null)) {
                                            $("#oldTreatmentRegimen").val($scope.options['treatment-regimen'][$scope.model.baseTreatmentRegimenID]).change();
                                            $scope.model.oldTreatmentRegimenID = $scope.model.baseTreatmentRegimenID;
                                            $("#regimenDate").removeAttr("disabled").change();
                                            $("#regimenDate").val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                                            $("#causesChange").removeAttr("disabled").change();
                                            $("#causesChange").val("").change();
                                        } else {
                                            $("#oldTreatmentRegimen").val('').change();
                                            $("#oldTreatmentRegimenID").val('').change();
                                            $("#regimenDate").attr("disabled", "disabled").change();
                                            $("#regimenDate").val('').change();
                                            if ($scope.model.baseTreatmentRegimenStage != null && $scope.model.baseTreatmentRegimenStage != '' &&
                                                    $("#treatmentRegimenStage").val() != null && $("#treatmentRegimenStage").val() != 'string:' &&
                                                    $scope.model.baseTreatmentRegimenStage === $("#treatmentRegimenStage").val().split(":")[1]) {
                                                $("#causesChange").attr("disabled", "disabled").change();
                                                $("#causesChange").val("").change();
                                            }
                                        }
                                        regimentx = $("#treatmentRegimenID").val();
                                    });

                                    $("#stageID").change(function () {
                                        if ($("#stageID").val() !== 'string:') {

                                            loading.show();
                                            $.ajax({
                                                url: '/service/opc-visit/get-date.json',
                                                data: {oid: $("#stageID").val().split(":")[1]},
                                                method: 'POST',
                                                success: function (resp) {
                                                    loading.hide();
                                                    if (resp.success) {
                                                        $scope.model.statusOfTreatmentID = resp.data.statusOfTreatmentID;
                                                        $scope.model.baseTreatmentRegimenID = resp.data.treatmentRegimenID;
                                                        $scope.model.baseTreatmentRegimenStage = resp.data.treatmentRegimenStage;
                                                        $scope.model.treatmentTime = resp.data.treatmentTime;
                                                        $scope.model.registrationTime = resp.data.registrationTime;
                                                        $scope.model.endTime = resp.data.endTime;
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    $("#stageID").change(function () {
                                        if ($("#stageID").val() !== '') {
                                            if ($scope.model.statusOfTreatmentID && $scope.model.statusOfTreatmentID === '2') {
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
                                }, 200);
                            }
                            if ($scope.currentArv && !$scope.opcVisit) {
                                let checked = $("#auto").is(':checked');
                                if (checked === true && $scope.autoSec && Number($scope.autoSec) > 0) {
                                    let createDelay = $timeout(() => {
                                        $scope.createVisit($scope.currentArv.id);
                                    }, $scope.autoSec * 1000);

                                    $scope.delay = $scope.autoSec;
                                    $interval(() => {
                                        checked = $("#auto").is(':checked');
                                        if (checked === true) {
                                            $scope.delay -= 1;
                                        } else {
                                            $timeout.cancel(createDelay);
                                        }
                                    }, 1000);
                                }
                            }

                        }
                    });
                }
            });
        }, 500);
    };
    
    


    $scope.insuranceChange = function () {
        $("#insurance").change(function () {
            if ($("#insurance").val() != 'string:1') {
                $("#insuranceNo").attr("disabled", "disabled");
                $("#insuranceType").attr("disabled", "disabled");
                $("#insuranceSite").attr("disabled", "disabled");
                $("#insuranceExpiry").attr("disabled", "disabled");
                $("#insurancePay").attr("disabled", "disabled");
                $("#route").attr("disabled", "disabled");
                $("#insuranceNo").val("").change();
                $("#insuranceType").val("string:").change();
                $("#insuranceSite").val("").change();
                $("#insuranceExpiry").val("").change();
                $("#insurancePay").val("string:").change();
                $("#route").val("string:").change();
            } else {
                $("#insuranceNo").removeAttr("disabled").change();
                $("#insuranceType").removeAttr("disabled").change();
                $("#insuranceSite").removeAttr("disabled").change();
                $("#insuranceExpiry").removeAttr("disabled").change();
                $("#insurancePay").removeAttr("disabled").change();
                $("#route").removeAttr("disabled").change();
            }
        });
    };

    $scope.appointmentTimeChange = function () {
        if ($("#lastExaminationTime").val() !== 'dd/mm/yyyy' && $("#lastExaminationTime").val() != null && $("#lastExaminationTime").val() != '' &&
                $("#daysReceived").val() != null && $("#daysReceived").val() != '') {
            var dataSplit = $("#lastExaminationTime").val().split('/');
            var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
            var dayNumer = isNaN(parseInt($("#daysReceived").val())) ? 0 : parseInt($("#daysReceived").val());
            formatedDate.setDate(formatedDate.getDate() + dayNumer);
            $("#appointmentTime").val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
        }
    };
    
    $scope.calculateDay = function () {
        if($("#lastExaminationTime").val() !== 'dd/mm/yyyy' && $("#lastExaminationTime").val() != null && $("#lastExaminationTime").val() != '' && 
                        $("#appointmentTime").val() !== 'dd/mm/yyyy' && $("#appointmentTime").val() != null && $("#appointmentTime").val() != '' && 
                        ($("#daysReceived").val() == null || $("#daysReceived").val() == '')){
            var dataSplit1 = $("#lastExaminationTime").val().split('/');
            var start = new Date(dataSplit1[1] + '/' + dataSplit1[0] + '/' + dataSplit1[2]);
            var dataSplit2 = $("#appointmentTime").val().split('/');
            var end = new Date(dataSplit2[1] + '/' + dataSplit2[0] + '/' + dataSplit2[2]);
            var diff = new Date(end - start);
            var days = diff/1000/60/60/24;
            if(days > 0){
                $("#daysReceived").val(days).change();
            }
        }
    };

    $scope.insuranceNoChange = function () {
        if ($("#insuranceNo").val() != null && $("#insuranceNo").val() != '') {
            if ($("#insuranceNo").val().length === 15) {
                var myMap = $scope.options['insurance-type'];
                for (var s in myMap) {
                    if ($("#insuranceNo").val().substring(0, 2).toUpperCase() == myMap[s].substring(0, 2)) {
                        $("#insuranceType").val('string:' + s).change();
                        break;
                    }
                }
                if ($("#insuranceNo").val().substring(2, 3) == '1' || $("#insuranceNo").val().substring(2, 3) == '2' || $("#insuranceNo").val().substring(2, 3) == '5') {
                    $("#insurancePay").val('string:1').change();
                } else if ($("#insuranceNo").val().substring(2, 3) == '3') {
                    $("#insurancePay").val('string:2').change();
                } else if ($("#insuranceNo").val().substring(2, 3) == '4') {
                    $("#insurancePay").val('string:3').change();
                } else {
                    $("#insurancePay").val('string:').change();
                }
            }

        }
    };

    $scope.createVisit = function (arv_id, print) {
        if ($scope.promise) {
            $timeout.cancel($scope.promise);
        }
        $scope.promise = $timeout(function () {
            loading.show();
            $.ajax({
                url: `/service/opc-qr/visit.json?arv_id=${arv_id}`,
                method: 'POST',
                contentType: "application/json",
                dataType: 'json',
                data: JSON.stringify($scope.model),
                success: function (resp) {
                    loading.hide();
                    $scope.$apply(() => {
                        if (!resp.success) {
                            $scope.errors = resp.data;
                            if (resp.message) {
                                msg.danger(resp.message);
                            }
                        } else {
                            var message = $scope.model.id === null ? 'Thêm mới lượt khám thành công' : 'Cập nhật thông tin lượt khám thành công';
                            $scope.reset();
                            msg.success(message, function () {
                                $scope.reset();
                                $scope.errors = null;
                                if (print) {
                                    $scope.dialogReport('/report/opc-visit/print.html' + "?oid=" + resp.data.id + "&arvid=" + resp.data.arvID, null, "Đơn thuốc");
                                    $("#pdf-loading").remove();
                                }
                            }, 2000);
                        }
                    });
                }
            });
        }, 500);
    };
});
