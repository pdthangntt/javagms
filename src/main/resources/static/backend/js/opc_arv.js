/* global app, $scope */
app.service('arvService', function ($uibModal, msg) {
    var elm = this;
    elm.logs = function (oid, code, name) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'arvLogs',
            controller: 'arv_log',
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

    elm.feedbackReceive = function (oid, type, isPaper) {
        loading.show();
        $.ajax({
            url: urlGetFeedback,
            data: {oid: oid, type: type, isPaper: isPaper},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'opcArvFeedbackReceive',
                        controller: 'opc_arv_feedback_receive',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    type: resp.data.typeSite,
                                    itemOld: resp.data.oldModel,
                                    itemNew: resp.data.newModel,
                                    options: resp.data.options,
                                };
                            }
                        }
                    });
                } else {
//                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };
    
    
});

app.controller('arv_log', function ($scope, $uibModalInstance, params, msg) {
    $scope.model = {staffID: 0, arvID: params.oid, code: params.code, name: params.name};
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

app.controller('opc_arv_new', function ($scope, msg, localStorageService) {
    $scope.pOptions = pOptions;
    $scope.htcID = utils.getContentOfDefault(form.htcID, '');
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.arvID = utils.getContentOfDefault(form.arvID, '');
    $scope.htcSiteID = utils.getContentOfDefault(form.htcSiteID, '');
    $scope.pageRedirect = utils.getContentOfDefault(form.pageRedirect, '');
    $scope.currentSiteID = utils.getContentOfDefault(form.currentSiteID, '');
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;
    $scope.ntch = utils.getContentOfDefault(form.ntch, '');
    $scope.supporterRelation = utils.getContentOfDefault(form.supporterRelation, '');
    $scope.confirmSiteID = utils.getContentOfDefault(form.confirmSiteID, '');
    $scope.confirmSiteName = utils.getContentOfDefault(form.confirmSiteName, '');
    $scope.pcrOneTime = utils.getContentOfDefault(form.pcrOneTime, '');
    $scope.dob = utils.getContentOfDefault(form.dob, '');
    $scope.pcrTwoTime = utils.getContentOfDefault(form.pcrTwoTime, '');
    $scope.pcrOneResult = utils.getContentOfDefault(form.pcrOneResult, '');
    $scope.pcrTwoResult = utils.getContentOfDefault(form.pcrTwoResult, '');
    $scope.therapySiteID = utils.getContentOfDefault(form.therapySiteID, '');
    $scope.registrationType = utils.getContentOfDefault(form.registrationType, '');
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.sourceSiteName = utils.getContentOfDefault(form.sourceSiteName, '');
    $scope.sourceCode = utils.getContentOfDefault(form.sourceCode, '');
    $scope.sourceServiceID = utils.getContentOfDefault(form.sourceServiceID, '');
    $scope.clinicalStage = utils.getContentOfDefault(form.clinicalStage, '');
    $scope.lao = utils.getContentOfDefault(form.lao, '');
    $scope.laoTestTime = utils.getContentOfDefault(form.laoTestTime, '');
    $scope.laoSymptoms = utils.getContentOfDefault(form.laoSymptoms, '');
    $scope.laoOtherSymptom = utils.getContentOfDefault(form.laoOtherSymptom, '');
    $scope.inh = utils.getContentOfDefault(form.inh, '');
    $scope.inhFromTime = utils.getContentOfDefault(form.inhFromTime, '');
    $scope.inhToTime = utils.getContentOfDefault(form.inhToTime, '');
    $scope.inhEndCauses = utils.getContentOfDefault(form.inhEndCauses, '');
    $scope.ntchSymptoms = utils.getContentOfDefault(form.ntchSymptoms, '');
    $scope.ntchOtherSymptom = utils.getContentOfDefault(form.ntchOtherSymptom, '');
    $scope.cotrimoxazoleFromTime = utils.getContentOfDefault(form.cotrimoxazoleFromTime, '');
    $scope.cotrimoxazoleToTime = utils.getContentOfDefault(form.cotrimoxazoleToTime, '');
    $scope.cotrimoxazoleEndCauses = utils.getContentOfDefault(form.cotrimoxazoleEndCauses, '');
    $scope.statusOfTreatmentID = utils.getContentOfDefault(form.statusOfTreatmentID, '');
    $scope.firstTreatmentRegimenID = utils.getContentOfDefault(form.firstTreatmentRegimenID, '');
    $scope.treatmentRegimenStage = utils.getContentOfDefault(form.treatmentRegimenStage, '');
    $scope.treatmentRegimenID = utils.getContentOfDefault(form.treatmentRegimenID, '');
    $scope.medicationAdherence = utils.getContentOfDefault(form.medicationAdherence, '');
    $scope.firstCd4Result = utils.getContentOfDefault(form.firstCd4Result, '');
    $scope.firstCd4Causes = utils.getContentOfDefault(form.firstCd4Causes, '');
    $scope.cd4Result = utils.getContentOfDefault(form.cd4Result, '');
    $scope.cd4Causes = utils.getContentOfDefault(form.cd4Causes, '');
    $scope.firstViralLoadResult = utils.getContentOfDefault(form.firstViralLoadResult, '');
    $scope.firstViralLoadCauses = utils.getContentOfDefault(form.firstViralLoadCauses, '');
    $scope.viralLoadResult = utils.getContentOfDefault(form.viralLoadResult, '');
    $scope.viralLoadCauses = utils.getContentOfDefault(form.viralLoadCauses, '');
    $scope.hbv = utils.getContentOfDefault(form.hbv, '');
    $scope.hbvTime = utils.getContentOfDefault(form.hbvTime, '');
    $scope.hbvResult = utils.getContentOfDefault(form.hbvResult, '');
    $scope.hcv = utils.getContentOfDefault(form.hcv, '');
    $scope.hcvTime = utils.getContentOfDefault(form.hcvTime, '');
    $scope.hcvResult = utils.getContentOfDefault(form.hcvResult, '');
    $scope.endCase = utils.getContentOfDefault(form.endCase, '');
    $scope.transferCase = utils.getContentOfDefault(form.transferCase, '');
    $scope.transferSiteID = utils.getContentOfDefault(form.transferSiteID, '');
    $scope.treatmentStageID = utils.getContentOfDefault(form.treatmentStageID, '');
    $scope.insuranceType = utils.getContentOfDefault(form.insuranceType, '');
    $scope.insuranceSite = utils.getContentOfDefault(form.insuranceSite, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');
    $scope.insurancePay = utils.getContentOfDefault(form.insurancePay, '');
    $scope.insurance = utils.getContentOfDefault(form.insurance, '');
    $scope.insuranceNo = utils.getContentOfDefault(form.insuranceNo, '');
    $scope.genderID = utils.getContentOfDefault(form.genderID, '');
    $scope.raceID = utils.getContentOfDefault(form.raceID, '');
    $scope.jobID = utils.getContentOfDefault(form.jobID, '');
    $scope.objectGroupID = utils.getContentOfDefault(form.objectGroupID, '');
    $scope.isDisplayCopy = utils.getContentOfDefault(form.isDisplayCopy, '');
    $scope.permanentAddressNo = utils.getContentOfDefault(form.permanentAddressNo, '');
    $scope.permanentAddress = utils.getContentOfDefault(form.permanentAddress, '');
    $scope.currentAddress = utils.getContentOfDefault(form.currentAddress, '');
    $scope.permanentAddressGroup = utils.getContentOfDefault(form.permanentAddressGroup, '');
    $scope.permanentAddressStreet = utils.getContentOfDefault(form.permanentAddressStreet, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentAddressNo = utils.getContentOfDefault(form.currentAddressNo, '');
    $scope.currentAddressGroup = utils.getContentOfDefault(form.currentAddressGroup, '');
    $scope.currentAddressStreet = utils.getContentOfDefault(form.currentAddressStreet, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.registrationTime = utils.getContentOfDefault(form.registrationTime, '');
    $scope.firstTreatmentTime = utils.getContentOfDefault(form.firstTreatmentTime, '');
    $scope.treatmentTime = utils.getContentOfDefault(form.treatmentTime, '');
    $scope.lastExaminationTime = utils.getContentOfDefault(form.lastExaminationTime, '');
    $scope.appointmentTime = utils.getContentOfDefault(form.appointmentTime, '');
    $scope.firstCd4Time = utils.getContentOfDefault(form.firstCd4Time, '');
    $scope.cd4Time = utils.getContentOfDefault(form.cd4Time, '');
    $scope.firstViralLoadTime = utils.getContentOfDefault(form.firstViralLoadTime, '');
    $scope.viralLoadTime = utils.getContentOfDefault(form.viralLoadTime, '');
    $scope.endTime = utils.getContentOfDefault(form.endTime, '');
    $scope.treatmentStageTime = utils.getContentOfDefault(form.treatmentStageTime, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.transferSiteName = utils.getContentOfDefault(form.transferSiteName, '');
    $scope.feedbackResultsReceivedTimeOpc = utils.getContentOfDefault(form.feedbackResultsReceivedTimeOpc, '');
    $scope.feedbackResultsReceivedTime = utils.getContentOfDefault(form.feedbackResultsReceivedTime, '');
    $scope.tranferFromTime = utils.getContentOfDefault(form.tranferFromTime, '');
    $scope.tranferToTime = utils.getContentOfDefault(form.tranferToTime, '');
    $scope.dateOfArrival = utils.getContentOfDefault(form.dateOfArrival, '');
    $scope.viralLoadResultNumber = utils.getContentOfDefault(form.viralLoadResultNumber, '');
    $scope.tranferToTimeOpc = utils.getContentOfDefault(form.tranferToTimeOpc, '');
    $scope.laoStartTime = utils.getContentOfDefault(form.laoStartTime, '');
    $scope.laoEndTime = utils.getContentOfDefault(form.laoEndTime, '');
    $scope.qualifiedTreatmentTime = utils.getContentOfDefault(form.qualifiedTreatmentTime, '');
    $scope.receivedWardDate = utils.getContentOfDefault(form.receivedWardDate, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.joinBornDate = utils.getContentOfDefault(form.joinBornDate, '');
    $scope.laoTestDate = utils.getContentOfDefault(form.laoTestDate, '');
    $.validator.addMethod("validCode", function (value) {
        return value.match(/[^x]\d$/);
    }, "Mã khách hàng không đúng định dạng");

    // Custom validate phone Vietname
    $.validator.addMethod("validPhone", function (value, element) {
        return value.match(/^$|([0-9])\b/); // Regex valid for empty string or number only
    }, "Số điện thoại không hợp lệ");


    $scope.items = {
        pregnantStartDate: '#pregnantStartDate',
        pregnantEndDate: '#pregnantEndDate',
        joinBornDate: '#joinBornDate',
        feedbackResultsReceivedTimeOpc: '#feedbackResultsReceivedTimeOpc',
        feedbackResultsReceivedTime: '#feedbackResultsReceivedTime',
        tranferFromTime: '#tranferFromTime',
        tranferToTime: '#tranferToTime',
        dateOfArrival: '#dateOfArrival',
        viralLoadResultNumber: '#viralLoadResultNumber',
        tranferToTimeOpc: '#tranferToTimeOpc',

        ntch: "#ntch",
        firstTreatmentTime: "#firstTreatmentTime",
        treatmentTime: "#treatmentTime",
        qualifiedTreatmentTime: "#qualifiedTreatmentTime",
        supporterRelation: "#supporterRelation",
        confirmSiteID: "#confirmSiteID",
        confirmSiteName: "#confirmSiteName",
        pcrOneTime: "#pcrOneTime",
        dob: "#dob",
        pcrTwoTime: "#pcrTwoTime",
        registrationTime: "#registrationTime",
        treatmentStageTime: "#treatmentStageTime",
        pcrOneResult: "#pcrOneResult",
        pcrTwoResult: "#pcrTwoResult",
        therapySiteID: "#therapySiteID",
        registrationType: "#registrationType",
        sourceSiteID: "#sourceSiteID",
        sourceSiteName: "#sourceSiteName",
        sourceCode: "#sourceCode",
        clinicalStage: "#clinicalStage",
        lao: "#lao",
        laoResult: "#laoResult",
        laoTreatment: "#laoTreatment",
        laoStartTime: "#laoStartTime",
        laoEndTime: "#laoEndTime",
        laoTestTime: "#laoTestTime",
        laoSymptoms: "#laoSymptoms",
        laoOtherSymptom: "#laoOtherSymptom",

        suspiciousSymptoms: "#suspiciousSymptoms",
        examinationAndTest: "#examinationAndTest",
        laoTestDate: "#laoTestDate",
        laoTestResults: "#laoTestResults",
        laoDiagnose: "#laoDiagnose",

        inh: "#inh",
        inhFromTime: "#inhFromTime",
        inhToTime: "#inhToTime",
        inhEndCauses: "#inhEndCauses",
        ntchSymptoms: "#ntchSymptoms",
        ntchOtherSymptom: "#ntchOtherSymptom",
        cotrimoxazoleFromTime: "#cotrimoxazoleFromTime",
        cotrimoxazoleToTime: "#cotrimoxazoleToTime",
        cotrimoxazoleEndCauses: "#cotrimoxazoleEndCauses",
        cotrimoxazoleOtherEndCause: "#cotrimoxazoleOtherEndCause",
        statusOfTreatmentID: "#statusOfTreatmentID",
        passTreatment: "#passTreatment",
        quickByTreatmentTime: "#quickByTreatmentTime",
        quickByConfirmTime: "#quickByConfirmTime",
        firstTreatmentRegimenID: "#firstTreatmentRegimenID",
        treatmentRegimenStage: "#treatmentRegimenStage",
        treatmentRegimenID: "#treatmentRegimenID",
        medicationAdherence: "#medicationAdherence",
        firstCd4Result: "#firstCd4Result",
        firstCd4Causes: "#firstCd4Causes",
        cd4Result: "#cd4Result",
        cd4Causes: "#cd4Causes",
        firstViralLoadResult: "#firstViralLoadResult",
        firstViralLoadCauses: "#firstViralLoadCauses",
        viralLoadResult: "#viralLoadResult",
        route: "#route",
        viralLoadCauses: "#viralLoadCauses",
        hbv: "#hbv",
        hbvTime: "#hbvTime",
        hbvResult: "#hbvResult",
        hcv: "#hcv",
        hcvTime: "#hcvTime",
        hcvResult: "#hcvResult",
        endCase: "#endCase",
        endTime: "#endTime",
        transferCase: "#transferCase",
        transferSiteID: "#transferSiteID",
        treatmentStageID: "#treatmentStageID",
        treatmentStage: "#treatmentStage",
        insuranceType: "#insuranceType",
        insuranceSite: "#insuranceSite",
        insuranceExpiry: "#insuranceExpiry",
        insurancePay: "#insurancePay",
        insurance: "#insurance",
        insuranceNo: "#insuranceNo",
        genderID: "#genderID",
        raceID: "#raceID",
        jobID: "#jobID",
        objectGroupID: "#objectGroupID",
        isDisplayCopy: "#isDisplayCopy",
        permanentAddressNo: "#permanentAddressNo",
        permanentAddress: "#permanentAddress",
        currentAddress: "#currentAddress",
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
        patientPhone: "#patientPhone",
        supporterPhone: "#supporterPhone",
        confirmCode: "#confirmCode",
        confirmTime: "#confirmTime",
        code: "#code",
        transferSiteName: "#transferSiteName",
        lastExaminationTime: "#lastExaminationTime",
        firstCd4Time: "#firstCd4Time",
        cd4Time: "#cd4Time",
        firstViralLoadTime: "#firstViralLoadTime",
        viralLoadTime: "#viralLoadTime",
        daysReceived: "#daysReceived",
        receivedWardDate: "#receivedWardDate",
        appointmentTime: "#appointmentTime",
        pageRedirect: "#pageRedirect"
    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            tranferToTime: {
                required: function () {
                    return $($scope.items.registrationType).val() == '3' || ($($scope.items.registrationType).val() == '1' && $($scope.items.sourceSiteID).val() != null && $($scope.items.sourceSiteID).val() != '');
                }
            },
            endTime: {
                required: function () {
                    return $($scope.items.endCase).val() != null && $($scope.items.endCase).val() != '';
                }
            },
            endCase: {
                required: function () {
                    return $($scope.items.endTime).val() != null && $($scope.items.endTime).val() != '';
                }
            },
            inhFromTime: {
                required: function () {
                    return $($scope.items.inh).val() === '1';
                }
            },
            laoStartTime: {
                required: function () {
                    return $($scope.items.laoTreatment).val() === '1';
                }
            },
            treatmentTime: {
                required: function () {
                    return $($scope.items.statusOfTreatmentID).val() === '3';
                }
            },
            code: {
                required: true,
            },
            confirmTime: {
                required: true,
            },
            registrationTime: {
                required: true,
            },
            fullName: {
                required: true,
            },
            genderID: {
                required: true
            },
            dob: {
                required: true,
            },
            insurance: {
                required: true
            },
            permanentProvinceID: {
                required: true
            },
            permanentDistrictID: {
                required: true
            },
            permanentWardID: {
                required: true
            },
            currentProvinceID: {
                required: true
            },
            currentDistrictID: {
                required: true
            },
            currentWardID: {
                required: true
            },
            pcrOneTime: {
                required: function () {
                    return $($scope.items.pcrOneResult).val() != null && $($scope.items.pcrOneResult).val().length > 0;
                }
            },
            pcrOneResult: {
                required: function () {
                    return $($scope.items.pcrOneTime).val() != null && $($scope.items.pcrOneTime).val().length > 0;
                }
            },
            pcrTwoTime: {
                required: function () {
                    return $($scope.items.pcrTwoResult).val() != null && $($scope.items.pcrTwoResult).val().length > 0;
                }
            },
            pcrTwoResult: {
                required: function () {
                    return $($scope.items.pcrTwoTime).val() != null && $($scope.items.pcrTwoTime).val().length > 0;
                }
            },
            therapySiteID: {
                required: function () {
                    return isOpcManager === true;
                }
            },
            registrationType: {
                required: true
            },
            endCase: {
                required: function () {
                    return $($scope.items.endTime).val().length > 0;
                }
            },
            transferSiteID: {
                required: function () {
                    return $($scope.items.endCase).val() == '3';
                }
            },
            daysReceived: {
                digits: true
            },
            transferSiteName: {
                required: function () {
                    return $($scope.items.transferSiteID).val() == '-1';
                }
            },
            examinationNote: {
                maxlength: 100
            },
            note: {
                maxlength: 500
            },
            statusOfTreatmentID: {
                required: true
            },
            laoTestTime: {
                required: function () {
                    return $($scope.items.lao).val() == '1';
                }
            },
            receivedWard: {
                maxlength: 500
            },
            firstCd4Result: {
                digits: true
            },
            cd4Result: {
                digits: true
            },
            permanentAddress: {
                maxlength: 500
            },
            permanentAddressStreet: {
                maxlength: 500
            },
            permanentAddressGroup: {
                maxlength: 500
            },
            currentAddress: {
                maxlength: 500
            },
            currentAddressStreet: {
                maxlength: 500
            },
            currentAddressGroup: {
                maxlength: 500
            },
            identityCard: {
                maxlength: 100
            },
            confirmCode: {
                maxlength: 100
            }

        },
        messages: {
            tranferToTime: {
                required: "Ngày tiếp nhận không được để trống"
            },
            endTime: {
                required: "Ngày kết thúc không được để trống"
            },
            endCase: {
                required: "Lý do kết thúc không được để trống"
            },
            inhFromTime: {
                required: "Ngày bắt đầu INH không được để trống"
            },
            laoStartTime: {
                required: "Ngày bắt đầu điều trị Lao không được để trống"
            },
            treatmentTime: {
                required: "Ngày điều trị ARV không được để trống"
            },
            code: {
                required: "Mã bệnh án không được để trống",
            },
            confirmTime: {
                required: "Ngày XN khẳng định không được để trống",
            },
            registrationTime: {
                required: "Ngày đăng ký không được để trống",
            },
            fullName: {
                required: "Họ và tên không được để trống",
            },
            genderID: {
                required: "Giới tính không được để trống",
            },
            dob: {
                required: "Ngày sinh không được để trống",
            },
            insurance: {
                required: "BN có thẻ BHYT không được để trống",
            },
            permanentProvinceID: {
                required: "Tỉnh/TP thường trú không được để trống",
            },
            permanentDistrictID: {
                required: "Quận/Huyện thường trú không được để trống",
            },
            permanentWardID: {
                required: "Xã/Phường thường trú không được để trống",
            },
            currentProvinceID: {
                required: "Tỉnh/TP cư trú hiện tại không được để trống",
            },
            currentDistrictID: {
                required: "Quận/Huyện cư trú hiện tại không được để trống",
            },
            currentWardID: {
                required: "Xã/Phường cư trú hiện tại không được để trống",
            },
            pcrOneTime: {
                required: "Ngày XN PCR lần 1 không được để trống",
            },
            pcrOneResult: {
                required: "Kết quả XN PCR lần 1 không được để trống",
            },
            pcrTwoTime: {
                required: "Ngày XN PCR lần 2 không được để trống",
            },
            pcrTwoResult: {
                required: "Kết quả XN PCR lần 2 không được để trống",
            },
            therapySiteID: {
                required: "Cơ sở điều trị không được để trống",
            },
            registrationType: {
                required: "Loại đăng ký không được để trống",
            },
            endCase: {
                required: "Lý do kết thúc không được để trống",
            },
            transferSiteID: {
                required: "Cơ sở chuyển đi không được để trống",
            },
            daysReceived: {
                digits: "Số ngày nhận thuốc không hợp lệ "
            },
            transferSiteName: {
                required: "Cơ sở chuyển đi không được để trống"
            },
            examinationNote: {
                maxlength: "Các vấn đề khác không được quá 255 ký tự"
            },
            note: {
                maxlength: "Ghi chú không được quá 500 ký tự"
            },
            statusOfTreatmentID: {
                required: "Trạng thái điều trị không được để trống"
            },
            laoTestTime: {
                required: "Ngày sàng lọc không được để trống"
            },
            receivedWard: {
                maxlength: "Tên xã không được quá 500 ký tự"
            },
            firstCd4Result: {
                digits: "Kết quả XN CD4 phải là số và không được âm"
            },
            cd4Result: {
                digits: "Kết quả XN CD4 phải là số và không được âm"
            },
            permanentAddress: {
                maxlength: "Số nhà thường trú không được quá 500 ký tự"
            },
            permanentAddressStreet: {
                maxlength: "Đường phố thường trú không được quá 500 ký tự"
            },
            permanentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố thường trú không được quá 500 ký tự"
            },
            currentAddress: {
                maxlength: "Số nhà cư trú hiện tại không được quá 500 ký tự"
            },
            currentAddressStreet: {
                maxlength: "Đường phố cư trú hiện tại không được quá 500 ký tự"
            },
            currentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố cư trú hiện tại không được quá 500 ký tự"
            },
            identityCard: {
                maxlength: "Số CMND không được quá 100 ký tự"
            },
            confirmCode: {
                maxlength: "Mã XN khẳng định không được quá 100 ký tự"
            }
        }
    });

    //Lấy DS cơ sở chuyển đến theo loại đăng ký - DSNAnh
    $scope.getSourceSite = function (type) {
        loading.show();
        $.ajax({
            url: "/service/opc-arv/get-source-site.json?type=" + type,
            type: "GET",
            contentType: "application/json",
            dataType: 'json',
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = null;
                    if (resp.success) {
                        $('#sourceSiteID').children('option:not(:first)').remove();
                        $.each(resp.data, function (k, v) {
                            $('#sourceSiteID').append(new Option(v, k))
                        });
                    }
                });
            }
        });
    };

    $scope.htcReceiveID = $.getQueryParameters().htc_id;
    $scope.opcID = $.getQueryParameters().opc_id;
    $scope.opcNewID = $.getQueryParameters().id;
    $scope.insuranceNoChange = function () {
        if ($($scope.items.insuranceNo).val() != '') {
            if ($($scope.items.insuranceNo).val().length == 15) {
                var myMap = pOptions['insurance-type'];
                for (var s in myMap) {
                    if ($($scope.items.insuranceNo).val().substring(0, 2).toUpperCase() == myMap[s].substring(0, 2)) {
                        $($scope.items.insuranceType).val(s).change();
                        break;
                    }
                }
                if ($($scope.items.insuranceNo).val().substring(2, 3) == '1' || $($scope.items.insuranceNo).val().substring(2, 3) == '2' || $($scope.items.insuranceNo).val().substring(2, 3) == '5') {
                    $($scope.items.insurancePay).val('1').change();
                } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '3') {
                    $($scope.items.insurancePay).val('2').change();
                } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '4') {
                    $($scope.items.insurancePay).val('3').change();
                } else {
                    $($scope.items.insurancePay).val('').change();
                }
            }
        }
    };

    function parseDate(str) {
        var m = str.match(/^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((18|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((18|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/);
        return m;
    }
    function CalculateWeekendDays(fromDate, toDate) {
        var weekendDayCount = 0;
        var diffTime = parseInt(toDate - fromDate);
//                Math.abs(toDate - fromDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        var dayMilliseconds = 1000 * 60 * 60 * 24;

        while (fromDate <= toDate) {
            var day = fromDate.getDay()
            if (day === 0 || day === 6) {
                weekendDayCount++;
            }
            fromDate = new Date(+fromDate + dayMilliseconds);
        }

        var result = diffDays - weekendDayCount;
        return result;
    }
    function quickTimeChange(fromDate, toDate) {
        if ($(toDate).val() != null && $(toDate).val() != '' && $($scope.items.passTreatment).val() == '1' &&
                $(fromDate).val() != null && $(fromDate).val() != '' &&
                parseDate($(fromDate).val()) && parseDate($(toDate).val())) {
            var date1 = new Date(parseInt($(fromDate).val().split("/")[2]), parseInt($(fromDate).val().split("/")[1]) - 1, parseInt($(fromDate).val().split("/")[0]));
            var date2 = new Date(parseInt($(toDate).val().split("/")[2]), parseInt($(toDate).val().split("/")[1]) - 1, parseInt($(toDate).val().split("/")[0]));
            var result = CalculateWeekendDays(date1, date2);
            if (result === 0) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('0').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('0').change();
                }
            } else if (result === 1) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('1').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('1').change();
                }
            } else if (result === 2) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('2').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('2').change();
                }
            } else if (result === 3) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('3').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('3').change();
                }
            } else if (result >= 4 && result <= 7) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('4').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('4').change();
                }
            } else {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('').change();
                }
            }
        }
    }
    
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
        $scope.codeSupport();
        //Dữ liệu địa danh
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        // Trường hợp import excel không có thông tin tinh thành/ quận huyện/ xã phường
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $($scope.items.permanentProvinceID).val('').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $($scope.items.currentProvinceID).val('').change();
        }

        //event copy
        $("#permanentProvinceID, #permanentAddress, #permanentAddressGroup, #permanentAddressStreet, #permanentDistrictID, #permanentWardID").change(function () {
            if ($scope.isCopyPermanentAddress) {
                $($scope.items.currentAddressGroup).val($($scope.items.permanentAddressGroup).val()).change();
                $($scope.items.currentAddressStreet).val($($scope.items.permanentAddressStreet).val()).change();
                $($scope.items.currentAddress).val($($scope.items.permanentAddress).val()).change();
                $($scope.items.currentProvinceID).val($($scope.items.permanentProvinceID).val()).change();
                $($scope.items.currentDistrictID).val($($scope.items.permanentDistrictID).val()).change();
                $($scope.items.currentWardID).val($($scope.items.permanentWardID).val()).change();
            }
        });

        $scope.$parent.select_mutiple($scope.items.laoSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.inhEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.ntchSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.cotrimoxazoleEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.firstCd4Causes, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.cd4Causes, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.firstViralLoadCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.viralLoadCauses, "Chọn lý do");

        $scope.$parent.select_search($scope.items.route, "Chọn tuyến đăng ký bảo hiểm");
        $scope.$parent.select_search($scope.items.viralLoadResult, "");
        $scope.$parent.select_search($scope.items.firstViralLoadResult, "");
        $scope.$parent.select_search($scope.items.ntch, "");
        $scope.$parent.select_search($scope.items.supporterRelation, "");
        $scope.$parent.select_search($scope.items.ntch, "");
        $scope.$parent.select_search($scope.items.confirmSiteID, "");
        $scope.$parent.select_search($scope.items.pcrOneResult, "");
        $scope.$parent.select_search($scope.items.pcrTwoResult, "");
        $scope.$parent.select_search($scope.items.therapySiteID, "");
        $scope.$parent.select_search($scope.items.registrationType, "");
        $scope.$parent.select_search($scope.items.clinicalStage, "");
        $scope.$parent.select_search($scope.items.lao, "");
        $scope.$parent.select_search($scope.items.inh, "");
        $scope.$parent.select_search($scope.items.insuranceType, "");
        $scope.$parent.select_search($scope.items.statusOfTreatmentID, "");
        $scope.$parent.select_search($scope.items.firstTreatmentRegimenID, "");
        $scope.$parent.select_search($scope.items.treatmentRegimenStage, "");
        $scope.$parent.select_search($scope.items.treatmentRegimenID, "");
        $scope.$parent.select_search($scope.items.medicationAdherence, "");
        $scope.$parent.select_search($scope.items.hbv, "");
        $scope.$parent.select_search($scope.items.hbvResult, "");
        $scope.$parent.select_search($scope.items.hcv, "");
        $scope.$parent.select_search($scope.items.hcvResult, "");
        $scope.$parent.select_search($scope.items.hcvResult, "");
        $scope.$parent.select_search($scope.items.endCase, "");
        $scope.$parent.select_search($scope.items.transferSiteID, "");
        $scope.$parent.select_search($scope.items.treatmentStageID, "");
        $scope.$parent.select_search($scope.items.insuranceType, "");
        $scope.$parent.select_search($scope.items.insurancePay, "");
        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.genderID, "Chọn giới tính");
        $scope.$parent.select_search($scope.items.raceID, "Chọn dân tộc");
        $scope.$parent.select_search($scope.items.jobID, "Chọn nghề nghiệp");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn đối tượng");
        $scope.$parent.select_search($scope.items.sourceSiteID, "");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
        $scope.$parent.select_search($scope.items.permanentWardID, "");
        $scope.$parent.select_search($scope.items.currentProvinceID, "");
        $scope.$parent.select_search($scope.items.currentDistrictID, "");
        $scope.$parent.select_search($scope.items.currentWardID, "");

        $('#transferSiteID').children('option:first').text('Chọn cơ sở chuyển tới');
        $('#treatmentStageID').children('option:first').text('Chọn trạng thái biến động');
        $('#statusOfTreatmentID option[value="2"]').text("Đang chờ điều trị (pre-ARV)");

        $($scope.items.treatmentStageID).attr("disabled", "disabled");

        if ($scope.raceID == '') {
            $($scope.items.raceID).val('1').change();
        }

//        $($scope.items.dob).val($scope.dob).change();
        if ($($scope.items.dob).val() === null || $($scope.items.dob).val() === '' || typeof ($($scope.items.dob)) == 'undefined') {
            $($scope.items.pcrOneTime).attr("disabled", "disabled");
            $($scope.items.pcrOneResult).attr("disabled", "disabled");
            $($scope.items.pcrTwoTime).attr("disabled", "disabled");
            $($scope.items.pcrTwoResult).attr("disabled", "disabled");
        } else {
            var dateOfBirth18 = $scope.getDateFromString($($scope.items.dob).val());
            var dateBefore18mon = $scope.getDateFromString(form.dateBefore18mon);
            if (dateBefore18mon > dateOfBirth18) {
                $($scope.items.pcrOneTime).attr("disabled", "disabled");
                $($scope.items.pcrOneResult).attr("disabled", "disabled");
                $($scope.items.pcrTwoTime).attr("disabled", "disabled");
                $($scope.items.pcrTwoResult).attr("disabled", "disabled");
            }
        }

        if ($($scope.items.confirmSiteID).val() != '-1') {
            $($scope.items.confirmSiteName).attr("disabled", "disabled");
        }


        if ($($scope.items.insurance).val() != '1') {
            $($scope.items.insuranceNo).attr("disabled", "disabled");
            $($scope.items.insuranceType).attr("disabled", "disabled");
            $($scope.items.insuranceSite).attr("disabled", "disabled");
            $($scope.items.insuranceExpiry).attr("disabled", "disabled");
            $($scope.items.insurancePay).attr("disabled", "disabled");
            $($scope.items.route).attr("disabled", "disabled");
        }

        if (isARV && !isOpcManager) {
            $($scope.items.therapySiteID).attr("disabled", "disabled");
            $($scope.items.therapySiteID).val($scope.currentSiteID).change();
        }

        if (isOpcManager) {
            $($scope.items.therapySiteID).removeAttr("disabled").change();
        }

        $($scope.items.sourceCode).attr("disabled", "disabled");

        if ($($scope.items.registrationType).val() != '3') {
            $($scope.items.sourceSiteID).attr("disabled", "disabled");
            $($scope.items.sourceSiteName).attr("disabled", "disabled");
            $($scope.items.sourceCode).attr("disabled", "disabled");
            $($scope.items.tranferToTime).attr("disabled", "disabled");
            $($scope.items.dateOfArrival).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");

        } else {
            $($scope.items.sourceSiteID).removeAttr("disabled").change();
            $($scope.items.sourceCode).removeAttr("disabled").change();
            $($scope.items.tranferToTime).removeAttr("disabled").change();
            $($scope.items.dateOfArrival).removeAttr("disabled").change();
            $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
        }

        if ($($scope.items.sourceSiteID).val() != '-1') {
            $($scope.items.sourceSiteName).attr("disabled", "disabled");
        } else {
            $($scope.items.sourceSiteName).removeAttr("disabled").change();
        }

        if ($($scope.items.lao).val() != '1') {
            $($scope.items.laoTestTime).attr("disabled", "disabled");
            $($scope.items.laoSymptoms).attr("disabled", "disabled");
            $($scope.items.laoOtherSymptom).attr("disabled", "disabled");

            $($scope.items.suspiciousSymptoms).attr("disabled", "disabled");
            $($scope.items.examinationAndTest).attr("disabled", "disabled");
            $($scope.items.laoTestDate).attr("disabled", "disabled");
            $($scope.items.laoDiagnose).attr("disabled", "disabled");
        }

        if ($($scope.items.inh).val() != '1') {
            $($scope.items.inhFromTime).attr("disabled", "disabled");
            $($scope.items.inhToTime).attr("disabled", "disabled");
            $($scope.items.inhEndCauses).attr("disabled", "disabled");
        }

        if ($($scope.items.ntch).val() != '1') {
            $($scope.items.ntchSymptoms).attr("disabled", "disabled");
            $($scope.items.ntchOtherSymptom).attr("disabled", "disabled");
        }
        if ($scope.statusOfTreatmentID === '') {
            $($scope.items.statusOfTreatmentID).val('0').change();
        }

        if ($($scope.items.statusOfTreatmentID).val() === '2') {
            $($scope.items.qualifiedTreatmentTime).attr("disabled", "disabled");
            $($scope.items.treatmentTime).attr("disabled", "disabled");
            $("#treatmentRegimenStage").attr("disabled", "disabled");
            $($scope.items.treatmentRegimenID).attr("disabled", "disabled");
            $($scope.items.daysReceived).attr("disabled", "disabled");
            $($scope.items.receivedWardDate).attr("disabled", "disabled");
        }

        if ($($scope.items.hbv).val() != '1') {
            $($scope.items.hbvTime).attr("disabled", "disabled");
            $($scope.items.hbvResult).attr("disabled", "disabled");
        }

        if ($($scope.items.hcv).val() != '1') {
            $($scope.items.hcvTime).attr("disabled", "disabled");
            $($scope.items.hcvResult).attr("disabled", "disabled");
        }

        if ($($scope.items.endCase).val() != '3') {
            $($scope.items.transferSiteID).attr("disabled", "disabled");
            $($scope.items.transferCase).attr("disabled", "disabled");

            $($scope.items.tranferFromTime).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
            $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
        }

        if ($($scope.items.transferSiteID).val() != '-1') {
            $($scope.items.transferSiteName).attr("disabled", "disabled");
            $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
        }

        if ($($scope.items.endCase).val() != '') {
            if ($($scope.items.endCase).val() == '1') {
                $($scope.items.treatmentStageID).val('6').change();
            }
            if ($($scope.items.endCase).val() == '2') {
                $($scope.items.treatmentStageID).val('7').change();
            }
            if ($($scope.items.endCase).val() == '3') {
                $($scope.items.treatmentStageID).val('3').change();
            }
            if ($($scope.items.endCase).val() == '4') {
                $($scope.items.treatmentStageID).val('5').change();
            }
            if ($($scope.items.endCase).val() == '5') {
                $($scope.items.treatmentStageID).val('8').change();
            }
        }
        if (($($scope.items.registrationType).val() != '') && ($($scope.items.endCase).val() == '')) {
            if ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '4' ||
                    $($scope.items.registrationType).val() == '5') {
                $($scope.items.treatmentStageID).val('1').change();
            }
            if ($($scope.items.registrationType).val() == '2') {
                $($scope.items.treatmentStageID).val('2').change();
            }
            if ($($scope.items.registrationType).val() == '3') {
                $($scope.items.treatmentStageID).val('4').change();
            }
        }

        if ($scope.htcID != '') {
            $($scope.items.registrationType).attr("disabled", "disabled");
            $($scope.items.sourceSiteID).attr("disabled", "disabled");
            $($scope.items.firstTreatmentTime).attr("disabled", "disabled");
            $($scope.items.firstTreatmentRegimenID).attr("disabled", "disabled");

        }


        if ($($scope.items.laoTreatment).val() != '1') {
            $($scope.items.laoStartTime).attr("disabled", "disabled");
            $($scope.items.laoEndTime).attr("disabled", "disabled");
        }

        if ($($scope.items.registrationType).val() == '1' && $($scope.items.sourceSiteID).val() != null && $($scope.items.sourceSiteID).val() != '') {
            $($scope.items.sourceSiteID).removeAttr("disabled").change();
            $($scope.items.tranferToTime).removeAttr("disabled").change();
            $($scope.items.dateOfArrival).removeAttr("disabled").change();
            $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
        }
        ;
        if ($($scope.items.registrationType).val() == '1') {
            if ($scope.htcID === '') {
                $($scope.items.sourceSiteID).removeAttr("disabled").change();
            }

            $($scope.items.firstTreatmentTime).attr("disabled", "disabled");
            $($scope.items.firstTreatmentRegimenID).attr("disabled", "disabled");

        }

        if ($($scope.items.objectGroupID).val() == '5' || $($scope.items.objectGroupID).val() == '5.1' || $($scope.items.objectGroupID).val() == '5.2') {
            $($scope.items.pregnantStartDate).removeAttr("disabled").change();
            $($scope.items.pregnantEndDate).removeAttr("disabled").change();
            $($scope.items.joinBornDate).removeAttr("disabled").change();
        } else {
            $($scope.items.pregnantStartDate).attr("disabled", "disabled");
            $($scope.items.pregnantEndDate).attr("disabled", "disabled");
            $($scope.items.joinBornDate).attr("disabled", "disabled");
        }

        if ($($scope.items.laoSymptoms).val() != null &&
                ($($scope.items.laoSymptoms).val().indexOf("2") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("3") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("4") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("5") != '-1')) {
            $($scope.items.suspiciousSymptoms).val("1").change();
        }

        if ($($scope.items.suspiciousSymptoms).val() == '1') {
            $($scope.items.examinationAndTest).removeAttr("disabled").change();
        } else {
            $($scope.items.examinationAndTest).attr("disabled", "disabled").change();
        }
        if ($($scope.items.examinationAndTest).val() == '1') {
            $($scope.items.laoTestDate).removeAttr("disabled").change();
            $($scope.items.laoResult).removeAttr("disabled").change();
            $($scope.items.laoDiagnose).removeAttr("disabled").change();
        } else {
            $($scope.items.laoTestDate).attr("disabled", "disabled").change();
            $($scope.items.laoResult).attr("disabled", "disabled").change();
            $($scope.items.laoDiagnose).attr("disabled", "disabled").change();

            $($scope.items.laoTestDate).val("").change();
            $($scope.items.laoResult).val("").change();
            $($scope.items.laoDiagnose).val("").change();
        }
        if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
            var count = 0;
            for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                    count++;
                }
            }
            if (count > 0) {
                $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
            } else {
                $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
            }
        } else {
            $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
            $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
        }
        if ($($scope.items.statusOfTreatmentID).val() === '3') {
            $($scope.items.passTreatment).removeAttr("disabled").change();
        } else {
            $($scope.items.passTreatment).attr("disabled", "disabled");
        }
        if ($($scope.items.passTreatment).val() == '1') {
            $($scope.items.quickByTreatmentTime).removeAttr("disabled").change();
            $($scope.items.quickByConfirmTime).removeAttr("disabled").change();
        } else {
            $($scope.items.quickByTreatmentTime).attr("disabled", "disabled").change();
            $($scope.items.quickByTreatmentTime).val("").change();
            $($scope.items.quickByConfirmTime).attr("disabled", "disabled").change();
            $($scope.items.quickByConfirmTime).val("").change();
        }
        var dateBefore18mon = $scope.getDateFromString(form.dateBefore18mon);
        if ($scope.htcID != null && $scope.htcID != '') {
            $scope.insuranceNoChange();
        }

        $($scope.items.insuranceNo).val($($scope.items.insuranceNo).val().toUpperCase()).change();


        $('form[name="opc_arv_form"]').ready(function () {

            $($scope.items.sourceCode).change(function () {
                if ($($scope.items.sourceCode).val() != '') {
                    setTimeout(function () {
                        $($scope.items.sourceCode).val($($scope.items.sourceCode).val().toUpperCase()).change();
                    }, 10);
                }
            });
            $($scope.items.code).change(function () {
                if ($($scope.items.code).val() != '') {
                    setTimeout(function () {
                        $($scope.items.code).val($($scope.items.code).val().toUpperCase()).change();
                    }, 10);
                }
            });

            $($scope.items.treatmentTime).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });
            $($scope.items.registrationTime).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
            });
            $($scope.items.confirmTime).change(function () {
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });
            $($scope.items.passTreatment).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });

            $($scope.items.passTreatment).change(function () {
                if ($($scope.items.passTreatment).val() == '1') {
                    $($scope.items.quickByTreatmentTime).removeAttr("disabled").change();
                    $($scope.items.quickByConfirmTime).removeAttr("disabled").change();
                } else {
                    $($scope.items.quickByTreatmentTime).attr("disabled", "disabled").change();
                    $($scope.items.quickByTreatmentTime).val("").change();
                    $($scope.items.quickByConfirmTime).attr("disabled", "disabled").change();
                    $($scope.items.quickByConfirmTime).val("").change();
                }
            });
            $($scope.items.cotrimoxazoleEndCauses).change(function () {

                if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
                    var count = 0;
                    for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                        if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
                    } else {
                        $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                        $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
                    }
                } else {
                    $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                    $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
                }
            });
            $($scope.items.laoSymptoms).change(function () {
                var laoSymptoms = $($scope.items.laoSymptoms).val();
                if (laoSymptoms != null && (laoSymptoms.indexOf("2") != '-1' ||
                        laoSymptoms.indexOf("3") != '-1' ||
                        laoSymptoms.indexOf("4") != '-1' ||
                        laoSymptoms.indexOf("5") != '-1')) {
                    $($scope.items.suspiciousSymptoms).val("1").change();
                } else {
                    $($scope.items.suspiciousSymptoms).val("").change();
                }
            });

            $($scope.items.suspiciousSymptoms).change(function () {
                if ($($scope.items.suspiciousSymptoms).val() == '1') {
                    $($scope.items.examinationAndTest).removeAttr("disabled").change();
                } else {
                    $($scope.items.examinationAndTest).attr("disabled", "disabled").change();
                    $($scope.items.examinationAndTest).val("").change();
                }
            });

            $($scope.items.examinationAndTest).change(function () {
                if ($($scope.items.examinationAndTest).val() == '1') {
                    $($scope.items.laoTestDate).removeAttr("disabled").change();
                    $($scope.items.laoResult).removeAttr("disabled").change();
                    $($scope.items.laoDiagnose).removeAttr("disabled").change();
                } else {
                    $($scope.items.laoTestDate).attr("disabled", "disabled").change();
                    $($scope.items.laoResult).attr("disabled", "disabled").change();
                    $($scope.items.laoDiagnose).attr("disabled", "disabled").change();

                    $($scope.items.laoTestDate).val("").change();
                    $($scope.items.laoResult).val("").change();
                    $($scope.items.laoDiagnose).val("").change();
                }
            });

            $($scope.items.confirmCode).change(function () {
                if ($($scope.items.confirmCode).val() != '') {
                    setTimeout(function () {
                        $($scope.items.confirmCode).val($($scope.items.confirmCode).val().toUpperCase()).change();
                    }, 10);
                }
            });
            $($scope.items.insuranceNo).change(function () {
                $scope.insuranceNoChange();
            });
            $($scope.items.objectGroupID).change(function () {
                if ($($scope.items.objectGroupID).val() == '5' || $($scope.items.objectGroupID).val() == '5.1' || $($scope.items.objectGroupID).val() == '5.2') {
                    $($scope.items.pregnantStartDate).removeAttr("disabled").change();
                    $($scope.items.pregnantEndDate).removeAttr("disabled").change();
                    $($scope.items.joinBornDate).removeAttr("disabled").change();
                    $scope.objectGroupID = $($scope.items.objectGroupID).val();
                } else {
                    $($scope.items.pregnantStartDate).attr("disabled", "disabled");
                    $($scope.items.pregnantEndDate).attr("disabled", "disabled");
                    $($scope.items.joinBornDate).attr("disabled", "disabled");
                    $($scope.items.pregnantStartDate).val('').change();
                    $($scope.items.pregnantEndDate).val('').change();
                    $($scope.items.joinBornDate).val('').change();
                    $scope.objectGroupID = $($scope.items.objectGroupID).val();
                }
            });
            if ($($scope.items.registrationType).val() == '1') {
                $($scope.items.firstTreatmentTime).val($($scope.items.treatmentTime).val()).change();
                $($scope.items.firstTreatmentRegimenID).val($($scope.items.treatmentRegimenID).val()).change();
            }
            $($scope.items.treatmentTime).change(function () {
                if ($($scope.items.registrationType).val() == '1' || $scope.registrationType == '1') {
                    $($scope.items.firstTreatmentTime).val($($scope.items.treatmentTime).val()).change();

                }
            });
            $($scope.items.treatmentRegimenID).change(function () {
                if ($($scope.items.registrationType).val() == '1' || $scope.registrationType == '1') {
                    $($scope.items.firstTreatmentRegimenID).val($($scope.items.treatmentRegimenID).val()).change();

                }
            });

            if ($scope.htcID != '') {
                $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
                $($scope.items.tranferToTime).removeAttr("disabled").change();
            }
            if ($($scope.items.endCase).val() == '' && ($($scope.items.registrationTime).val() != '' && $($scope.items.registrationTime).val() != null && $($scope.items.registrationTime).val() != 'dd/mm/yyyy') && $scope.htcID != '' &&
                    ($($scope.items.treatmentStageTime).val() == '' || $($scope.items.treatmentStageTime).val() == null || $($scope.items.treatmentStageTime).val() == 'dd/mm/yyyy')) {
                $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
            }

            $($scope.items.statusOfTreatmentID).change(function () {
                if ($($scope.items.statusOfTreatmentID).val() === '3') {
                    $($scope.items.passTreatment).removeAttr("disabled").change();
                } else {
                    $($scope.items.passTreatment).attr("disabled", "disabled");
                    $($scope.items.passTreatment).val('').change();
                }
            });

            $($scope.items.statusOfTreatmentID).change(function () {
                if ($($scope.items.statusOfTreatmentID).val() === '2') {
                    $($scope.items.qualifiedTreatmentTime).attr("disabled", "disabled");
                    $($scope.items.treatmentTime).attr("disabled", "disabled");
                    $("#treatmentRegimenStage").attr("disabled", "disabled");
                    $($scope.items.treatmentRegimenID).attr("disabled", "disabled");
                    $($scope.items.daysReceived).attr("disabled", "disabled");
                    $($scope.items.receivedWardDate).attr("disabled", "disabled");

                    $($scope.items.qualifiedTreatmentTime).val("").change();
                    $($scope.items.treatmentTime).val("").change();
                    $("#treatmentRegimenStage").val("string:").change();
                    $($scope.items.treatmentRegimenID).val("").change();
                    $($scope.items.daysReceived).val("").change();
                    $($scope.items.receivedWardDate).val("").change();
                } else {
                    $($scope.items.qualifiedTreatmentTime).removeAttr("disabled").change();
                    $($scope.items.treatmentTime).removeAttr("disabled").change();
                    $("#treatmentRegimenStage").removeAttr("disabled").change();
                    $($scope.items.treatmentRegimenID).removeAttr("disabled").change();
                    $($scope.items.daysReceived).removeAttr("disabled").change();
                    $($scope.items.receivedWardDate).removeAttr("disabled").change();
                }
            });

            $($scope.items.dob).change(function () {
                var dateOfBirth18 = $scope.getDateFromString($($scope.items.dob).val());
                if ($($scope.items.dob).val() != null && $($scope.items.dob).val() != '') {
                    if (dateBefore18mon < dateOfBirth18) {
                        $($scope.items.pcrOneTime).removeAttr("disabled").change();
                        $($scope.items.pcrOneResult).removeAttr("disabled").change();
                        $($scope.items.pcrTwoTime).removeAttr("disabled").change();
                        $($scope.items.pcrTwoResult).removeAttr("disabled").change();
                    } else {
                        $($scope.items.pcrOneTime).attr("disabled", "disabled");
                        $($scope.items.pcrOneResult).attr("disabled", "disabled");
                        $($scope.items.pcrTwoTime).attr("disabled", "disabled");
                        $($scope.items.pcrTwoResult).attr("disabled", "disabled");
                        $($scope.items.pcrOneTime).val("").change();
                        $($scope.items.pcrOneResult).val("").change();
                        $($scope.items.pcrTwoTime).val("").change();
                        $($scope.items.pcrTwoResult).val("").change();
                    }
                }
            });

            $($scope.items.insurance).change(function () {
                if ($($scope.items.insurance).val() != '1') {
                    $($scope.items.insuranceNo).attr("disabled", "disabled");
                    $($scope.items.insuranceType).attr("disabled", "disabled");
                    $($scope.items.insuranceSite).attr("disabled", "disabled");
                    $($scope.items.insuranceExpiry).attr("disabled", "disabled");
                    $($scope.items.insurancePay).attr("disabled", "disabled");
                    $($scope.items.route).attr("disabled", "disabled");
                    $($scope.items.insuranceNo).val("").change();
                    $($scope.items.insuranceType).val("").change();
                    $($scope.items.insuranceSite).val("").change();
                    $($scope.items.insuranceExpiry).val("").change();
                    $($scope.items.insurancePay).val("").change();
                    $($scope.items.route).val("").change();
                } else {
                    $($scope.items.insuranceNo).removeAttr("disabled").change();
                    $($scope.items.insuranceType).removeAttr("disabled").change();
                    $($scope.items.insuranceSite).removeAttr("disabled").change();
                    $($scope.items.insuranceExpiry).removeAttr("disabled").change();
                    $($scope.items.insurancePay).removeAttr("disabled").change();
                    $($scope.items.route).removeAttr("disabled").change();
                }
            });

            $($scope.items.confirmSiteID).change(function () {
                if ($($scope.items.confirmSiteID).val() == '-1') {
                    $($scope.items.confirmSiteName).removeAttr("disabled").change();
                } else {
                    $($scope.items.confirmSiteName).attr("disabled", "disabled");
                    $($scope.items.confirmSiteName).val("").change();
                }
            });

            $($scope.items.registrationType).change(function () {
                if ($($scope.items.registrationType).val() == '3') {
                    $($scope.items.sourceSiteID).removeAttr("disabled").change();
                    $($scope.items.sourceCode).removeAttr("disabled").change();
                    $($scope.items.tranferToTime).removeAttr("disabled").change();
                    $($scope.items.dateOfArrival).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
                    $($scope.items.tranferToTime).val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                    $($scope.items.feedbackResultsReceivedTime).val('').change();
                    $scope.getSourceSite($($scope.items.registrationType).val());
                } else {
                    $($scope.items.sourceCode).attr("disabled", "disabled");
                    $($scope.items.sourceCode).val("").change();
                    if ($($scope.items.registrationType).val() != '1') {
                        $($scope.items.sourceSiteName).val("").change();

                        $($scope.items.sourceSiteID).attr("disabled", "disabled");
                        $($scope.items.sourceSiteID).val("").change();

                        $($scope.items.tranferToTime).attr("disabled", "disabled");
                        $($scope.items.tranferToTime).val("").change();

                        $($scope.items.dateOfArrival).attr("disabled", "disabled");
                        $($scope.items.dateOfArrival).val("").change();

                        $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
                        $($scope.items.feedbackResultsReceivedTime).val("").change();
                    }
                }
            });
            $($scope.items.registrationType).change(function () {
                if ($($scope.items.registrationType).val() == '1' || $scope.registrationType == '1') {
                    $($scope.items.firstTreatmentTime).attr("disabled", "disabled");
                    $($scope.items.firstTreatmentRegimenID).attr("disabled", "disabled");
                    $($scope.items.firstTreatmentTime).val("").change();
                    $($scope.items.firstTreatmentRegimenID).val("").change();
                    $($scope.items.sourceSiteID).removeAttr("disabled").change();

                    $($scope.items.sourceSiteName).val("").change();
                    $($scope.items.tranferToTime).attr("disabled", "disabled");
                    $($scope.items.tranferToTime).val("").change();
                    $($scope.items.dateOfArrival).attr("disabled", "disabled");
                    $($scope.items.dateOfArrival).val("").change();
                    $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTime).val("").change();
                    $scope.getSourceSite($($scope.items.registrationType).val());

                } else {
                    $($scope.items.firstTreatmentTime).removeAttr("disabled").change();
                    $($scope.items.firstTreatmentRegimenID).removeAttr("disabled").change();
                    if ($($scope.items.registrationType).val() != '3') {

                        $($scope.items.sourceSiteID).attr("disabled", "disabled");
                        $($scope.items.dateOfArrival).attr("disabled", "disabled");
                        $($scope.items.sourceSiteID).val("").change();
                    }
                }
            });

            $($scope.items.sourceSiteID).change(function () {
                if ($($scope.items.sourceSiteID).val() != null && $($scope.items.sourceSiteID).val() != '') {
                    if ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '3') {
                        $($scope.items.tranferToTime).removeAttr("disabled").change();
                        $($scope.items.dateOfArrival).removeAttr("disabled").change();
                        $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
                        $($scope.items.tranferToTime).val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                        $($scope.items.feedbackResultsReceivedTime).val($($scope.items.registrationType).val() == '1' ? $.datepicker.formatDate('dd/mm/yy', new Date()) : '').change();
                        $($scope.items.dateOfArrival).val("").change();
                    }
                } else {
                    if ($($scope.items.registrationType).val() == '1') {
                        $($scope.items.tranferToTime).attr("disabled", "disabled");
                        $($scope.items.dateOfArrival).attr("disabled", "disabled");
                        $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
                        $($scope.items.tranferToTime).val("").change();
                        $($scope.items.dateOfArrival).val("").change();
                        $($scope.items.feedbackResultsReceivedTime).val("").change();
                    }
                }
            });

            $($scope.items.sourceSiteID).change(function () {
                if ($($scope.items.sourceSiteID).val() == '-1') {
                    $($scope.items.sourceSiteName).removeAttr("disabled").change();
                } else {

                    $($scope.items.sourceSiteName).attr("disabled", "disabled");
                    $($scope.items.sourceSiteName).val("").change();
                    $($scope.items.sourceCode).val("").change();
                }
            });

            $($scope.items.lao).change(function () {
                if ($($scope.items.lao).val() == '1') {
                    $($scope.items.laoTestTime).removeAttr("disabled").change();
                    $($scope.items.laoSymptoms).removeAttr("disabled").change();
                    $($scope.items.laoOtherSymptom).removeAttr("disabled").change();
                    $($scope.items.suspiciousSymptoms).removeAttr("disabled").change();
                } else {
                    $($scope.items.laoTestTime).attr("disabled", "disabled");
                    $($scope.items.laoSymptoms).attr("disabled", "disabled");
                    $($scope.items.laoOtherSymptom).attr("disabled", "disabled");
                    $($scope.items.laoTestTime).val("").change();
                    $($scope.items.laoSymptoms).val("").change();
                    $($scope.items.laoOtherSymptom).val("").change();

                    $($scope.items.suspiciousSymptoms).attr("disabled", "disabled");
                    $($scope.items.examinationAndTest).attr("disabled", "disabled");
                    $($scope.items.laoTestDate).attr("disabled", "disabled");
                    $($scope.items.laoDiagnose).attr("disabled", "disabled");
                    $($scope.items.suspiciousSymptoms).val("").change();
                    $($scope.items.examinationAndTest).val("").change();
                    $($scope.items.laoTestDate).val("").change();
                    $($scope.items.laoDiagnose).val("").change();
                }
            });

//            $($scope.items.laoResult).change(function () {
//                if ($($scope.items.laoResult).val() == '2') {
//                    $($scope.items.laoTreatment).removeAttr("disabled").change();
//                } else {
//                    $($scope.items.laoTreatment).attr("disabled", "disabled");
//                    $($scope.items.laoTreatment).val("").change();
//                }
//            });

            $($scope.items.laoTreatment).change(function () {
                if ($($scope.items.laoTreatment).val() == '1') {
                    $($scope.items.laoStartTime).removeAttr("disabled").change();
                    $($scope.items.laoEndTime).removeAttr("disabled").change();

                } else {
                    $($scope.items.laoStartTime).attr("disabled", "disabled");
                    $($scope.items.laoStartTime).val("").change();
                    $($scope.items.laoEndTime).attr("disabled", "disabled");
                    $($scope.items.laoEndTime).val("").change();
                }
            });

            $($scope.items.inh).change(function () {
                if ($($scope.items.inh).val() == '1') {
                    $($scope.items.inhFromTime).removeAttr("disabled").change();
                    $($scope.items.inhToTime).removeAttr("disabled").change();
                    $($scope.items.inhEndCauses).removeAttr("disabled").change();
                } else {
                    $($scope.items.inhFromTime).attr("disabled", "disabled");
                    $($scope.items.inhToTime).attr("disabled", "disabled");
                    $($scope.items.inhEndCauses).attr("disabled", "disabled");
                    $($scope.items.inhFromTime).val("").change();
                    $($scope.items.inhToTime).val("").change();
                    $($scope.items.inhEndCauses).val("").change();
                }
            });

            $($scope.items.ntch).change(function () {
                if ($($scope.items.ntch).val() == '1') {
                    $($scope.items.ntchSymptoms).removeAttr("disabled").change();
                    $($scope.items.ntchOtherSymptom).removeAttr("disabled").change();
                } else {
                    $($scope.items.ntchSymptoms).attr("disabled", "disabled");
                    $($scope.items.ntchOtherSymptom).attr("disabled", "disabled");
                    $($scope.items.ntchSymptoms).val("").change();
                    $($scope.items.ntchOtherSymptom).val("").change();
                }
            });
            $($scope.items.hbv).change(function () {
                if ($($scope.items.hbv).val() == '1') {
                    $($scope.items.hbvTime).removeAttr("disabled").change();
                    $($scope.items.hbvResult).removeAttr("disabled").change();
                } else {
                    $($scope.items.hbvTime).attr("disabled", "disabled");
                    $($scope.items.hbvResult).attr("disabled", "disabled");
                    $($scope.items.hbvTime).val("").change();
                    $($scope.items.hbvResult).val("").change();
                }
            });
            $($scope.items.hcv).change(function () {
                if ($($scope.items.hcv).val() == '1') {
                    $($scope.items.hcvTime).removeAttr("disabled").change();
                    $($scope.items.hcvResult).removeAttr("disabled").change();
                } else {
                    $($scope.items.hcvTime).attr("disabled", "disabled");
                    $($scope.items.hcvResult).attr("disabled", "disabled");
                    $($scope.items.hcvTime).val("").change();
                    $($scope.items.hcvResult).val("").change();
                }
            });
            $($scope.items.endCase).change(function () {
                if ($($scope.items.endCase).val() == '3') {
                    $($scope.items.transferSiteID).removeAttr("disabled").change();
                    $($scope.items.transferCase).removeAttr("disabled").change();

                    $($scope.items.tranferFromTime).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).removeAttr("disabled").change();
                    $($scope.items.tranferToTimeOpc).removeAttr("disabled").change();
                    $($scope.items.tranferFromTime).val($.datepicker.formatDate('dd/mm/yy', new Date())).change();

                } else {
                    $($scope.items.transferSiteID).attr("disabled", "disabled");
                    $($scope.items.transferCase).attr("disabled", "disabled");
                    $($scope.items.transferSiteName).attr("disabled", "disabled");
                    $($scope.items.tranferFromTime).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
                    $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");


                    $($scope.items.transferSiteID).val("").change();
                    $($scope.items.transferCase).val("").change();
                    $($scope.items.transferSiteName).val("").change();
                    $($scope.items.tranferFromTime).val("").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).val("").change();
                    $($scope.items.tranferToTimeOpc).val("").change();
                }
            });

            $($scope.items.transferSiteID).change(function () {
                if ($($scope.items.transferSiteID).val() == '-1') {
                    $($scope.items.transferSiteName).removeAttr("disabled").change();
                    $($scope.items.tranferToTimeOpc).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).removeAttr("disabled").change();
                } else {
                    $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
                    $($scope.items.transferSiteName).attr("disabled", "disabled");
                    $($scope.items.tranferToTimeOpc).val("").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).val("").change();
                    $($scope.items.transferSiteName).val("").change();
                }
            });
            
            
            $($scope.items.lastExaminationTime).change(function () {
                if ($($scope.items.lastExaminationTime).val() !== 'dd/mm/yyyy' && $($scope.items.lastExaminationTime).val() != null && $($scope.items.lastExaminationTime).val() != '' &&
                        $($scope.items.daysReceived).val() != null && $($scope.items.daysReceived).val() != '') {
                    var dataSplit = $($scope.items.lastExaminationTime).val().split('/');
                    var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                    var dayNumer = isNaN(parseInt($($scope.items.daysReceived).val())) ? 0 : parseInt($($scope.items.daysReceived).val());
                    formatedDate.setDate(formatedDate.getDate() + dayNumer);
                    $($scope.items.appointmentTime).val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
                }
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });
            $($scope.items.appointmentTime).change(function () {
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });
            $($scope.items.daysReceived).change(function () {
                if ($($scope.items.lastExaminationTime).val() !== 'dd/mm/yyyy' && $($scope.items.lastExaminationTime).val() != null && $($scope.items.lastExaminationTime).val() != '' &&
                        $($scope.items.daysReceived).val() != null && $($scope.items.daysReceived).val() != '') {
                    var dataSplit = $($scope.items.lastExaminationTime).val().split('/');
                    var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                    var dayNumer = isNaN(parseInt($($scope.items.daysReceived).val())) ? 0 : parseInt($($scope.items.daysReceived).val());
                    formatedDate.setDate(formatedDate.getDate() + dayNumer);
                    $($scope.items.appointmentTime).val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
                }
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });

            $($scope.items.registrationType).change(function () {
                if ($($scope.items.endCase).val() == null || $($scope.items.endCase).val() == '') {
                    if ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '4' ||
                            $($scope.items.registrationType).val() == '5') {
                        $($scope.items.treatmentStageID).val('1').change();
                    }
                    if ($($scope.items.registrationType).val() == '2') {
                        $($scope.items.treatmentStageID).val('2').change();
                    }
                    if ($($scope.items.registrationType).val() == '3') {
                        $($scope.items.treatmentStageID).val('4').change();
                    }
                }
            });

            $($scope.items.registrationType).change(function () {
                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
                        ($($scope.items.registrationType).val() != null && $($scope.items.registrationType).val() != '') &&
                        ($($scope.items.registrationTime).val() != null && $($scope.items.registrationTime).val() != '' && $($scope.items.registrationTime).val() != 'dd/mm/yyyy')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                }
            });

            $($scope.items.registrationTime).change(function () {
                if ($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') {
                    if ($($scope.items.registrationType).val() != null && $($scope.items.registrationType).val() != '') {
                        $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                    }
                }
            });

//            $($scope.items.registrationTime).change(function () {
//                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
//                        ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '2' || $($scope.items.registrationType).val() == '3')) {
//                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
//                }
//            });

            $($scope.items.endCase).change(function () {
                if ($($scope.items.endCase).val() == '1') {
                    $($scope.items.treatmentStageID).val('6').change();
                }
                if ($($scope.items.endCase).val() == '2') {
                    $($scope.items.treatmentStageID).val('7').change();
                }
                if ($($scope.items.endCase).val() == '3') {
                    $($scope.items.treatmentStageID).val('3').change();
                }
                if ($($scope.items.endCase).val() == '4') {
                    $($scope.items.treatmentStageID).val('5').change();
                }
                if ($($scope.items.endCase).val() == '5') {
                    $($scope.items.treatmentStageID).val('8').change();
                }
                if (($($scope.items.endCase).val() == null || $($scope.items.endCase).val() == '') &&
                        ($($scope.items.registrationType).val() != null && $($scope.items.registrationType).val() != '')) {
                    $($scope.items.treatmentStageID).val($($scope.items.registrationType).val()).change();
                }
                if (($($scope.items.endTime).val() != null && $($scope.items.endTime).val() != '' && $($scope.items.endTime).val() != 'dd/mm/yyyy') &&
                        ($($scope.items.endCase).val() != '')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.endTime).val()).change();
                }
            });
            $($scope.items.endTime).change(function () {
                if (($($scope.items.endTime).val() != null || $($scope.items.endTime).val() != '' || $($scope.items.endTime).val() != 'dd/mm/yyyy') &&
                        ($($scope.items.endCase).val() != '')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.endTime).val()).change();
                }
                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
                        ($($scope.items.registrationType).val() != null && $($scope.items.registrationType).val() != '') &&
                        $($scope.items.registrationTime).val() != null && $($scope.items.registrationTime).val() != '' && $($scope.items.registrationTime).val() != 'dd/mm/yyyy') {
                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                }
            });
        });

        // Hiển thị popup confirm phản hổi tiếp nhận từ cơ sở HTC
        if (($scope.htcReceiveID != null && $scope.htcReceiveID != '' && $scope.opcNewID != null && $scope.opcNewID != '') || feedback === 'htc') {
            bootbox.confirm(
                    {
                        message: 'Bạn có chắc chắn gửi phản hồi tiếp nhận khách hàng này cho cơ sở tư vấn xét nghiệm không?',
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
                                    url: '/service/opc-from-htc/feedback-htc.json',
                                    data: {newID: $scope.opcNewID},
                                    method: 'GET',
                                    success: function (resp) {
                                        if (resp.success) {
                                            if (resp.message) {
                                                msg.success(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html?id=' + $scope.opcNewID + '&page_redirect=printable';
                                                }, 2000);
                                            }
                                        } else {
                                            if (resp.message) {
                                                msg.danger(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html';
                                                }, 2000);
                                            }
                                        }
                                    }
                                });
                            } else {
                                msg.success('Khách hàng chuyển gửi điều trị được tiếp nhận thành công', function () {
                                    location.href = '/backend/opc-arv/index.html?receive_notification=1&id=' + $scope.opcNewID;
                                }, 2000);
                            }
                        }
                    }
            );
        }

        if (($scope.opcID != null && $scope.opcID != '' && $scope.opcNewID != null && $scope.opcNewID != '') || feedback === 'opc') {
            bootbox.confirm(
                    {
                        message: 'Bạn có chắc chắn gửi phản hồi tiếp nhận bệnh nhân này cho cơ sở điều trị chuyển gửi không?',
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
                                    url: '/service/opc-from-arv/feedback-opc.json',
                                    data: {oldID: $scope.opcID, newID: $scope.opcNewID},
                                    method: 'GET',
                                    success: function (resp) {
                                        if (resp.success) {
                                            if (resp.message) {
                                                msg.success(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html?id=' + $scope.opcNewID + '&page_redirect=printable';
                                                }, 2000);
                                            }
                                        } else {
                                            if (resp.message) {
                                                msg.danger(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html';
                                                }, 2000);
                                            }
                                        }
                                    }
                                });
                            } else {
                                msg.success('Bệnh nhân chuyển gửi điều trị được tiếp nhận thành công', function () {
                                    location.href = '/backend/opc-arv/index.html';
                                }, 2000);
                            }
                        }
                    }
            );
        }

        // Disable C4. Cơ sở chuyển gửi, C10. Ngày chuyển gửi theo phiếu khi tiếp nhận
        if ($scope.sourceServiceID != null && $scope.sourceServiceID != '') {
            $($scope.items.sourceSiteID).attr({disabled: "disable"});
            $($scope.items.sourceSiteName).attr({disabled: "disable"});
            $($scope.items.dateOfArrival).attr({disabled: "disable"});
        }


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

//        $('#treatmentRegimenID')
//                .find('option')
//                .remove()
//                .end()
//                ;
//        var ix = 0;
//        for (const [key, value] of Object.entries(regimenOptions)) {
//            $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
//        }
//        $('#treatmentRegimenID').append($('<option>', {value: '', text: 'Chọn phác đồ điều trị'}));
//        $("#treatmentRegimenID option[value='string:']").remove();
//        $("#treatmentRegimenID option[value='? string: ?']").remove();

//        $($scope.items.treatmentRegimenID).val("");
        $scope.treatmentRegimenStageChange = function () {
            console.log("xx " + $scope.treatmentRegimenStage);



            $('#treatmentRegimenID')
                    .find('option')
                    .remove()
                    .end()
                    ;

            if ($scope.treatmentRegimenStage === '1') {
                for (const [key, value] of Object.entries(regimenOptions1)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else if ($scope.treatmentRegimenStage === '2') {
                for (const [key, value] of Object.entries(regimenOptions2)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else if ($scope.treatmentRegimenStage === '3') {
                for (const [key, value] of Object.entries(regimenOptions3)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else {
                for (const [key, value] of Object.entries(regimenOptions)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            }
        }



    };

    if ($scope.htcID != "" && $($scope.items.isDisplayCopy).val() == "") {
        $scope.isCopyPermanentAddress = false;
        $($scope.items.isDisplayCopy).val(false);
    }


    $scope.copyAddress = function () {

        $scope.isCopyPermanentAddress = !$scope.isCopyPermanentAddress;
        if (!$scope.isCopyPermanentAddress) {
            $($scope.items.currentAddress).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressGroup).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressStreet).removeAttr("disabled").val('').change();
            $($scope.items.currentProvinceID).removeAttr("disabled").val('').change();
            $($scope.items.currentDistrictID).removeAttr("disabled").val('').change();
            $($scope.items.currentWardID).removeAttr("disabled").val('').change();
            $($scope.items.isDisplayCopy).val(false);

        } else {
            $($scope.items.currentAddress).attr({disabled: "disable"}).val($('#permanentAddress').val()).change();
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($('#permanentAddressGroup').val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($('#permanentAddressStreet').val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($('#permanentProvinceID').val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($('#permanentDistrictID').val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($('#permanentWardID').val()).change();
            $($scope.items.isDisplayCopy).val(true);
        }
    };
    $scope.customSubmit = function (form, printable, $event) {
        var elm = $event.currentTarget;
        let flagCheck = true;
        $event.preventDefault();
        bootbox.hideAll();
        $(".help-block-error").remove();

        flagCheck = form.validate();

        // Tiếp nhận khách hàng từ HTC
        if (flagCheck) {
            $(".help-block-error").remove();
            if ($scope.feedbackResultsReceivedTime == null || $scope.feedbackResultsReceivedTime == '' || $scope.feedbackResultsReceivedTime == 'dd/mm/yyyy') {
                $($scope.items.pageRedirect).val("confirm");
                elm.form.submit();
            } else {
                $($scope.items.pageRedirect).val("printable");
                elm.form.submit();
            }
        } else {
            $scope.validationOptions;
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };

    $scope.getDateFromString = function (date) {
        let d = date.split('/')
        let newDate = d[1] + '/' + d[0] + '/' + d[2];
        return new Date(newDate)
    };

    /**
     * Tự động lấy code cuối cùng
     * @returns {undefined}
     */
    $scope.codeSupport = function () {
        $.ajax({
            url: '/service/opc-arv/last-opc.json',
            success: function (resp) {
                if (resp.success && resp.data != null && !isOpcManager) {
                    var html = '<small class="form-text text-muted code-last" >Mã KH cuối cùng: ' + resp.data.code + '</small>';
                    if ($("#code").parent().find(".code-last").length > 0) {
                        $("#code .code-last").html(html);
                    } else {
                        $("#code").after(html);
                    }
                }
            }
        });
    };
});

app.controller('opc_arv_update', function ($scope, locations) {

    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.code = utils.getContentOfDefault(form.code, '');
    $scope.dateOfBirth = $("#dob").val();
    $scope.siteID = utils.getContentOfDefault(form.siteID, '');

    $scope.supporterRelation = utils.getContentOfDefault(form.supporterRelation, '');
    $scope.confirmSiteID = utils.getContentOfDefault(form.confirmSiteID, '');
    $scope.confirmSiteName = utils.getContentOfDefault(form.confirmSiteName, '');
    $scope.pcrOneTime = utils.getContentOfDefault(form.pcrOneTime, '');
    $scope.dob = utils.getContentOfDefault(form.dob, '');
    $scope.pcrTwoTime = utils.getContentOfDefault(form.pcrTwoTime, '');
    $scope.pcrOneResult = utils.getContentOfDefault(form.pcrOneResult, '');
    $scope.pcrTwoResult = utils.getContentOfDefault(form.pcrTwoResult, '');
    $scope.registrationType = utils.getContentOfDefault(form.registrationType, '');
    $scope.sourceServiceID = utils.getContentOfDefault(form.sourceServiceID, '');
    $scope.endCase = utils.getContentOfDefault(form.endCase, '');

    $scope.sourceSiteName = utils.getContentOfDefault(form.sourceSiteName, '');
    $scope.sourceCode = utils.getContentOfDefault(form.sourceCode, '');

    $scope.transferSiteID = utils.getContentOfDefault(form.transferSiteID, '');
    $scope.treatmentStageID = utils.getContentOfDefault(form.treatmentStageID, '');
    $scope.insuranceType = utils.getContentOfDefault(form.insuranceType, '');
    $scope.insuranceSite = utils.getContentOfDefault(form.insuranceSite, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');
    $scope.insurancePay = utils.getContentOfDefault(form.insurancePay, '');
    $scope.insurance = utils.getContentOfDefault(form.insurance, '');
    $scope.insuranceNo = utils.getContentOfDefault(form.insuranceNo, '');
    $scope.genderID = utils.getContentOfDefault(form.genderID, '');
    $scope.raceID = utils.getContentOfDefault(form.raceID, '');
    $scope.jobID = utils.getContentOfDefault(form.jobID, '');
    $scope.objectGroupID = utils.getContentOfDefault(form.objectGroupID, '');
    $scope.isDisplayCopy = utils.getContentOfDefault(form.isDisplayCopy, '');
    $scope.permanentAddressNo = utils.getContentOfDefault(form.permanentAddressNo, '');
    $scope.permanentAddress = utils.getContentOfDefault(form.permanentAddress, '');
    $scope.currentAddress = utils.getContentOfDefault(form.currentAddress, '');
    $scope.permanentAddressGroup = utils.getContentOfDefault(form.permanentAddressGroup, '');
    $scope.permanentAddressStreet = utils.getContentOfDefault(form.permanentAddressStreet, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentAddressNo = utils.getContentOfDefault(form.currentAddressNo, '');
    $scope.currentAddressGroup = utils.getContentOfDefault(form.currentAddressGroup, '');
    $scope.currentAddressStreet = utils.getContentOfDefault(form.currentAddressStreet, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.registrationTime = utils.getContentOfDefault(form.registrationTime, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.transferSiteName = utils.getContentOfDefault(form.transferSiteName, '');
    $scope.firstTreatmentTime = utils.getContentOfDefault(form.firstTreatmentTime, '');
    $scope.firstCd4Time = utils.getContentOfDefault(form.firstCd4Time, '');
    $scope.firstViralLoadTime = utils.getContentOfDefault(form.firstViralLoadTime, '');
    $scope.tranferToTime = utils.getContentOfDefault(form.tranferToTime, '');
    $scope.dateOfArrival = utils.getContentOfDefault(form.dateOfArrival, '');
    $scope.feedbackResultsReceivedTime = utils.getContentOfDefault(form.feedbackResultsReceivedTime, '');
    $scope.tranferFromTime = utils.getContentOfDefault(form.tranferFromTime, '');
    $scope.tranferToTimeOpc = utils.getContentOfDefault(form.tranferToTimeOpc, '');
    $scope.feedbackResultsReceivedTimeOpc = utils.getContentOfDefault(form.feedbackResultsReceivedTimeOpc, '');
    $scope.qualifiedTreatmentTime = utils.getContentOfDefault(form.qualifiedTreatmentTime, '');
    $scope.routeID = utils.getContentOfDefault(form.routeID, '');
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.treatmentStageTime = utils.getContentOfDefault(form.treatmentStageTime, '');
    $scope.tranferFromTime = utils.getContentOfDefault(form.tranferFromTime, '');
    $scope.endTime = utils.getContentOfDefault(form.endTime, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.joinBornDate = utils.getContentOfDefault(form.joinBornDate, '');


    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;

// Thêm custom validate date
    $.validator.addMethod("validDate", function (value) {
        return value.match(/(?:0[1-9]|[12][0-9]|3[01])\/(?:0[1-9]|1[0-2])\/(?:19|20\d{2})/);
    }, "Nhập đúng định dạng theo dd/mm/yyyy");

    $.validator.addMethod("validCode", function (value) {
        return value.match(/[^x]\d$/);
    }, "Mã khách hàng không đúng định dạng");

    // Custom validate phone Vietname
    $.validator.addMethod("validPhone", function (value, element) {
        return value.match(/^$|([0-9])\b/); // Regex valid for empty string or number only
    }, "Số điện thoại không hợp lệ");

    // Check input date with curent date
    $.validator.addMethod("invalidFutureDate", function (value, element) {

        var date = value.substring(0, 2);
        var month = value.substring(3, 5);
        var year = value.substring(6, 10);

        var dateToCompare = new Date(year, month - 1, date);
        var currentDate = new Date();

        return dateToCompare < currentDate;

    }, "Không được nhập ngày tương lai.");

    $scope.items = {
        tranferToTimeOpc: "#tranferToTimeOpc",
        feedbackResultsReceivedTimeOpc: "#feedbackResultsReceivedTimeOpc",
        ntch: "#ntch",
        supporterRelation: "#supporterRelation",
        confirmSiteID: "#confirmSiteID",
        confirmSiteName: "#confirmSiteName",
        pcrOneTime: "#pcrOneTime",
        dob: "#dob",
        tranferToTime: "#tranferToTime",
        dateOfArrival: "#dateOfArrival",
        feedbackResultsReceivedTime: "#feedbackResultsReceivedTime",
        pcrTwoTime: "#pcrTwoTime",
        pcrOneResult: "#pcrOneResult",
        pcrTwoResult: "#pcrTwoResult",
        registrationType: "#registrationType",
        sourceSiteName: "#sourceSiteName",
        sourceCode: "#sourceCode",
        clinicalStage: "#clinicalStage",
        lao: "#lao",
        laoTestTime: "#laoTestTime",
        laoSymptoms: "#laoSymptoms",
        laoOtherSymptom: "#laoOtherSymptom",
        inh: "#inh",
        inhFromTime: "#inhFromTime",
        inhToTime: "#inhToTime",
        inhEndCauses: "#inhEndCauses",
        ntchSymptoms: "#ntchSymptoms",
        ntchOtherSymptom: "#ntchOtherSymptom",
        cotrimoxazoleFromTime: "#cotrimoxazoleFromTime",
        cotrimoxazoleToTime: "#cotrimoxazoleToTime",
        cotrimoxazoleEndCauses: "#cotrimoxazoleEndCauses",
//        statusOfTreatmentID: "#statusOfTreatmentID",
        firstTreatmentRegimenID: "#firstTreatmentRegimenID",
        treatmentRegimenStage: "#treatmentRegimenStage",
        treatmentRegimenID: "#treatmentRegimenID",
        medicationAdherence: "#medicationAdherence",
        firstCd4Result: "#firstCd4Result",
//        firstCd4Causes: "#firstCd4Causes",
        cd4Result: "#cd4Result",
        cd4Causes: "#cd4Causes",
        hbv: "#hbv",
        hbvTime: "#hbvTime",
        hbvResult: "#hbvResult",
        hcv: "#hcv",
        hcvTime: "#hcvTime",
        hcvResult: "#hcvResult",
        endCase: "#endCase",
        endTime: "#endTime",
        transferCase: "#transferCase",
        transferSiteID: "#transferSiteID",
        treatmentStageID: "#treatmentStageID",
        insuranceType: "#insuranceType",
        insuranceSite: "#insuranceSite",
        insuranceExpiry: "#insuranceExpiry",
        insurancePay: "#insurancePay",
        insurance: "#insurance",
        insuranceNo: "#insuranceNo",
        genderID: "#genderID",
        raceID: "#raceID",
        jobID: "#jobID",
        objectGroupID: "#objectGroupID",
        isDisplayCopy: "#isDisplayCopy",
        permanentAddressNo: "#permanentAddressNo",
        permanentAddress: "#permanentAddress",
        currentAddress: "#currentAddress",
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
        patientPhone: "#patientPhone",
        supporterPhone: "#supporterPhone",
        confirmCode: "#confirmCode",
        code: "#code",
        transferSiteName: "#transferSiteName",
        lastExaminationTime: "#lastExaminationTime",
        daysReceived: "#daysReceived",
        appointmentTime: "#appointmentTime",
        sourceSiteID: "#sourceSiteID",
        treatmentStageTime: "#treatmentStageTime",
        tranferFromTime: "#tranferFromTime",
        pregnantStartDate: "#pregnantStartDate",
        pregnantEndDate: "#pregnantEndDate",
        joinBornDate: "#joinBornDate",
        routeID: "#routeID"
    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            permanentAddress: {
                maxlength: 499
            },
            permanentAddressStreet: {
                maxlength: 499
            },
            permanentAddressGroup: {
                maxlength: 499
            },
            currentAddress: {
                maxlength: 499
            },
            currentAddressStreet: {
                maxlength: 499
            },
            currentAddressGroup: {
                maxlength: 499
            },
            code: {
                required: true,
                maxlength: 50,
//                validCode: function () {
//                    return $($scope.items.code).val().length > 0;
//                }
            },
            confirmTime: {
                required: true,
            },
            tranferToTime: {
                required: true,
            },
//            dateOfArrival: {
//                required: true,
//            },
            feedbackResultsReceivedTime: {
//                required: true,
            },
            fullName: {
                required: true,
            },
            genderID: {
                required: true
            },
            dob: {
                required: true,
            },
            insurance: {
                required: true
            },
            permanentProvinceID: {
                required: true
            },
            permanentDistrictID: {
                required: true
            },
            permanentWardID: {
                required: true
            },
            currentProvinceID: {
                required: true
            },
            currentDistrictID: {
                required: true
            },
            currentWardID: {
                required: true
            },
            confirmCode: {
//                required: true,
//                validCode: function () {
//                    return $($scope.items.confirmCode).val().length > 0;
//                }
            },
//            confirmSiteID: {
//                required: true
//            },
//            confirmSiteName: {
//                required: function () {
//                    return $($scope.items.confirmSiteID).val() === '-1';
//                }
//            },
            pcrOneTime: {
                required: function () {
                    return $($scope.items.pcrOneResult).val().length > 0;
                }
            },
            pcrOneResult: {
                required: function () {
                    return $($scope.items.pcrOneTime).val().length > 0;
                }
            },
            pcrTwoTime: {
                required: function () {
                    return $($scope.items.pcrTwoResult).val().length > 0;
                }
            },
            pcrTwoResult: {
                required: function () {
                    return $($scope.items.pcrTwoTime).val().length > 0;
                }
            },
            receivedWard: {
                maxlength: 500
            },
            firstCd4Result: {
                digits: true
            },
        },
        messages: {
            permanentAddress: {
                maxlength: "Số nhà không được quá 500 ký tự"
            },
            permanentAddressStreet: {
                maxlength: "Đường phố không được quá 500 ký tự"
            },
            permanentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố không được quá 500 ký tự"
            },
            currentAddress: {
                maxlength: "Số nhà không được quá 500 ký tự"
            },
            currentAddressStreet: {
                maxlength: "Đường phố không được quá 500 ký tự"
            },
            currentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố không được quá 500 ký tự"
            },
            code: {
                required: "Mã bệnh án không được để trống",
                maxlength: "Mã bệnh án không được quá 50 ký tự",
//                validCode: "Mã bệnh án không đúng định dạng",
            },
            confirmTime: {
                required: "Ngày XN khẳng định không được để trống",
            },
            tranferToTime: {
                required: "Ngày tiếp nhận không được để trống",
            },
//            dateOfArrival: {
//                required: "Ngày chuyển gửi theo phiếu không được để trống",
//            },
            feedbackResultsReceivedTime: {
//                required: "Ngày phản hồi không được để trống",
            },
            fullName: {
                required: "Họ và tên không được để trống",
            },
            genderID: {
                required: "Giới tính không được để trống",
            },
            dob: {
                required: "Ngày tháng năm sinh không được để trống",
            },
            insurance: {
                required: "BN có thẻ BHYT không được để trống",
            },
            permanentProvinceID: {
                required: "Tỉnh/TP thường trú không được để trống",
            },
            permanentDistrictID: {
                required: "Quận/Huyện thường trú không được để trống",
            },
            permanentWardID: {
                required: "Xã/Phường thường trú không được để trống",
            },
            currentProvinceID: {
                required: "Tỉnh/TP tạm trú không được để trống",
            },
            currentDistrictID: {
                required: "Quận/Huyện tạm trú không được để trống",
            },
            currentWardID: {
                required: "Xã/Phường tạm trú không được để trống",
            },
            confirmCode: {
//                required: "Mã XN khẳng định không được để trống",
//                validCode: "Mã không đúng định dạng, bao gồm số, chữ, - và / ",
            },
//            confirmSiteID: {
//                required: "Nơi XN khẳng định không được để trống",
//            },
//            confirmSiteName: {
//                required: "Nơi XN khẳng định không được để trống",
//            },
            pcrOneTime: {
                required: "Ngày XN PCR lần 1 không được để trống",
            },
            pcrOneResult: {
                required: "Kết quả XN PCR lần 1 không được để trống",
            },
            pcrTwoTime: {
                required: "Ngày XN PCR lần 2 không được để trống",
            },
            pcrTwoResult: {
                required: "Kết quả XN PCR lần 2 không được để trống",
            },
            receivedWard: {
                maxlength: "Tên xã không được quá 500 ký tự"
            },
            firstCd4Result: {
                digits: "Kết quả XN CD4 phải là số và không được âm"
            },
        }
    });

    $scope.init = function () {
        $scope.codeSupport();
        $scope.$parent.select_search($scope.items.permanentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.permanentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.currentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.currentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.currentWardID, "Chọn phường xã");

        $scope.$parent.select_search($scope.items.insuranceType, "");
        $scope.$parent.select_search($scope.items.insurancePay, "");
        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.routeID, "");
        $scope.$parent.select_search($scope.items.genderID, "Chọn giới tính");
        $scope.$parent.select_search($scope.items.raceID, "Chọn dân tộc");
        $scope.$parent.select_search($scope.items.jobID, "Chọn công việc");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn nhóm đối tượng");
        $scope.$parent.select_search($scope.items.confirmSiteID, "");
        $scope.$parent.select_search($scope.items.pcrOneResult, "");
        $scope.$parent.select_search($scope.items.pcrTwoResult, "");
        $scope.$parent.select_search($scope.items.firstTreatmentRegimenID, "");

//        $scope.$parent.select_mutiple($scope.items.firstCd4Causes, "Chọn lý do");
        $scope.$parent.select_mutiple("#firstViralLoadCauses", "Chọn lý do");

        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        $("#registrationType").attr("disabled", "disabled");

        if (form.isOtherSite) {
            $("#fullName").attr("disabled", "disabled");
            $("#genderID").attr("disabled", "disabled");
            $("#dob").attr("disabled", "disabled");
            $("#identityCard").attr("disabled", "disabled");
            $("#raceID").attr("disabled", "disabled");
            $("#confirmCode").attr("disabled", "disabled");
            $("#confirmTime").attr("disabled", "disabled");
            $("#confirmSiteID").attr("disabled", "disabled");
            $("#confirmSiteName").attr("disabled", "disabled");
        }

        if ($("#confirmSiteID").val() != '-1') {
            $("#confirmSiteName").attr("disabled", "disabled");
        }

        $($scope.items.confirmCode).change(function () {
            if ($($scope.items.confirmCode).val() != '') {
                setTimeout(function () {
                    $($scope.items.confirmCode).val($($scope.items.confirmCode).val().toUpperCase()).change();
                }, 10);
            }
        });
        $($scope.items.code).change(function () {
            if ($($scope.items.code).val() != '') {
                setTimeout(function () {
                    $($scope.items.code).val($($scope.items.code).val().toUpperCase()).change();
                }, 10);
            }
        });

//        alert($("#objectGroupID").val());

//        if ($("#objectGroupID").val() !== '5' && $("#objectGroupID").val() !== '5.1' && $("#objectGroupID").val() !== '5.2') {
//            $("#pregnantStartDate").attr("disabled", "disabled");
//            $("#pregnantEndDate").attr("disabled", "disabled");
//            $("#joinBornDate").attr("disabled", "disabled");
//            $("#pregnantStartDate").val("").change();
//            $("#pregnantEndDate").val("").change();
//            $("#joinBornDate").val("").change();
//
//        }
//
//        $($("#objectGroupID")).change(function () {
//            if ($(this).val() === '5' || $(this).val() === '5.1' || $(this).val() === '5.2') {
//                $("#pregnantStartDate").removeAttr("disabled").change();
//                $("#pregnantEndDate").removeAttr("disabled").change();
//                $("#joinBornDate").removeAttr("disabled").change();
//            } else {
//                $("#pregnantStartDate").attr("disabled", "disabled");
//                $("#pregnantEndDate").attr("disabled", "disabled");
//                $("#joinBornDate").attr("disabled", "disabled");
//                $("#pregnantStartDate").val("").change();
//                $("#pregnantEndDate").val("").change();
//                $("#joinBornDate").val("").change();
//            }
//
//        });
        $($scope.items.insuranceNo).val($($scope.items.insuranceNo).val().toUpperCase()).change();
        $($scope.items.insuranceNo).change(function () {
            $scope.insuranceNoChange();
        });


        if (form.dob != null) {
            var dateOfBirthForm = $scope.getDateFromString(form.dob);
        }
        var dateBefore18mon = $scope.getDateFromString(form.dateBefore18mon);

        if ($($scope.items.dob).val() != null && $($scope.items.dob).val() != '' && (dateBefore18mon > dateOfBirthForm) || hasErrorDob) {
            $("#pcrOneTime").attr("disabled", "disabled");
            $("#pcrOneResult").attr("disabled", "disabled");
            $("#pcrTwoTime").attr("disabled", "disabled");
            $("#pcrTwoResult").attr("disabled", "disabled");
        }

        $($scope.items.tranferToTime).attr("disabled", "disabled");
        $($scope.items.dateOfArrival).attr("disabled", "disabled");
        $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
//        $($scope.items.statusOfTreatmentID).attr("disabled", "disabled");

        if ($scope.registrationType == '3' || ($scope.registrationType == '1' && $scope.sourceSiteID != '' && form.sourceSiteID != 0)) {
            $($scope.items.tranferToTime).removeAttr("disabled").change();
            $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
            $($scope.items.dateOfArrival).removeAttr("disabled").change();
        }


        //disible
        $($scope.items.endTime).attr("disabled", "disabled");
        $($scope.items.endCase).attr("disabled", "disabled");
        $($scope.items.transferSiteID).attr("disabled", "disabled");
        $($scope.items.transferSiteName).attr("disabled", "disabled");
        $($scope.items.transferCase).attr("disabled", "disabled");
        $($scope.items.treatmentStageID).attr("disabled", "disabled");
        $($scope.items.treatmentStageTime).attr("disabled", "disabled");
        $($scope.items.tranferFromTime).attr("disabled", "disabled");

        $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
        $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");


        if ($scope.transferSiteID == '-1' && $scope.endCase == '3') {
            $($scope.items.tranferToTimeOpc).removeAttr("disabled").change();
            $($scope.items.feedbackResultsReceivedTimeOpc).removeAttr("disabled").change();
        }

//
//        if ($scope.sourceServiceID == '100' || $scope.sourceServiceID == '103') {
//            $($scope.items.tranferToTime).attr("disabled", "disabled");
//            $($scope.items.dateOfArrival).attr("disabled", "disabled");
//            $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
//        }
//
//        if (($scope.sourceServiceID == '100' || $scope.sourceServiceID == '103') && ($scope.feedbackResultsReceivedTime == '' || hasErrorFeedback)) {
//            $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
//        }


        $('form[name="opc_arv_update_form"]').ready(function () {
            $($scope.items.dob).change(function () {
                var dateOfBirth18 = $scope.getDateFromString($($scope.items.dob).val());
                if ($($scope.items.dob).val() != null && $($scope.items.dob).val() != '') {
                    if (dateBefore18mon < dateOfBirth18) {
                        $($scope.items.pcrOneTime).removeAttr("disabled").change();
                        $($scope.items.pcrOneResult).removeAttr("disabled").change();
                        $($scope.items.pcrTwoTime).removeAttr("disabled").change();
                        $($scope.items.pcrTwoResult).removeAttr("disabled").change();
                    } else {
                        $($scope.items.pcrOneTime).attr("disabled", "disabled");
                        $($scope.items.pcrOneResult).attr("disabled", "disabled");
                        $($scope.items.pcrTwoTime).attr("disabled", "disabled");
                        $($scope.items.pcrTwoResult).attr("disabled", "disabled");
                        $($scope.items.pcrOneTime).val("").change();
                        $($scope.items.pcrOneResult).val("").change();
                        $($scope.items.pcrTwoTime).val("").change();
                        $($scope.items.pcrTwoResult).val("").change();
                    }
                }
            });

            if ($($scope.items.insurance).val() != '1') {
                $($scope.items.insuranceNo).val("").change();
                $($scope.items.insuranceType).val("").change();
                $($scope.items.insuranceSite).val("").change();
                $($scope.items.insuranceExpiry).val("").change();
                $($scope.items.insurancePay).val("").change();
                $($scope.items.routeID).val("").change();
                $($scope.items.insuranceNo).attr("disabled", "disabled");
                $($scope.items.insuranceType).attr("disabled", "disabled");
                $($scope.items.insuranceSite).attr("disabled", "disabled");
                $($scope.items.insuranceExpiry).attr("disabled", "disabled");
                $($scope.items.insurancePay).attr("disabled", "disabled");
                $($scope.items.routeID).attr("disabled", "disabled");
            }
            $($scope.items.insurance).change(function () {
                if ($($scope.items.insurance).val() != '1') {
                    $($scope.items.insuranceNo).attr("disabled", "disabled");
                    $($scope.items.insuranceType).attr("disabled", "disabled");
                    $($scope.items.insuranceSite).attr("disabled", "disabled");
                    $($scope.items.insuranceExpiry).attr("disabled", "disabled");
                    $($scope.items.insurancePay).attr("disabled", "disabled");
                    $($scope.items.routeID).attr("disabled", "disabled");
                    $($scope.items.insuranceNo).val("").change();
                    $($scope.items.insuranceType).val("").change();
                    $($scope.items.insuranceSite).val("").change();
                    $($scope.items.insuranceExpiry).val("").change();
                    $($scope.items.insurancePay).val("").change();
                    $($scope.items.routeID).val("").change();
                } else {
                    $($scope.items.insuranceNo).removeAttr("disabled").change();
                    $($scope.items.insuranceType).removeAttr("disabled").change();
                    $($scope.items.insuranceSite).removeAttr("disabled").change();
                    $($scope.items.insuranceExpiry).removeAttr("disabled").change();
                    $($scope.items.insurancePay).removeAttr("disabled").change();
                    $($scope.items.routeID).removeAttr("disabled").change();

                }
            });

            $("#confirmSiteID").change(function () {
                if ($("#confirmSiteID").val() == '-1') {
                    $("#confirmSiteName").removeAttr("disabled").change();
                } else {
                    $("#confirmSiteName").attr("disabled", "disabled");
                }
            });

            $("#confirmSiteID").change(function () {

            });
        });
    };
    $scope.insuranceNoChange = function () {
        if ($($scope.items.insuranceNo).val() != '') {
            if ($($scope.items.insuranceNo).val().length == 15) {
                var myMap = pOptions['insurance-type'];
                for (var s in myMap) {
                    if ($($scope.items.insuranceNo).val().substring(0, 2).toUpperCase() == myMap[s].substring(0, 2)) {
                        $($scope.items.insuranceType).val(s).change();
                        break;
                    }
                }
                if ($($scope.items.insuranceNo).val().substring(2, 3) == '1' || $($scope.items.insuranceNo).val().substring(2, 3) == '2' || $($scope.items.insuranceNo).val().substring(2, 3) == '5') {
                    $($scope.items.insurancePay).val('1').change();
                } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '3') {
                    $($scope.items.insurancePay).val('2').change();
                } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '4') {
                    $($scope.items.insurancePay).val('3').change();
                } else {
                    $($scope.items.insurancePay).val('').change();
                }
            }

        }
    };
    if ($scope.id != "" && $($scope.items.isDisplayCopy).val() == "") {
        $scope.isCopyPermanentAddress = false;
        $($scope.items.isDisplayCopy).val(false);
    }

    // Copy địa chỉ
    $scope.copyAddress = function () {

        $scope.isCopyPermanentAddress = !$scope.isCopyPermanentAddress;
        if (!$scope.isCopyPermanentAddress) {
            $($scope.items.currentAddress).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressGroup).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressStreet).removeAttr("disabled").val('').change();
            $($scope.items.currentProvinceID).removeAttr("disabled").val('').change();
            $($scope.items.currentDistrictID).removeAttr("disabled").val('').change();
            $($scope.items.currentWardID).removeAttr("disabled").val('').change();
            $($scope.items.isDisplayCopy).val(false);

        } else {
            $($scope.items.currentAddress).attr({disabled: "disable"}).val($("#address").val()).change();
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($("#address").val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($("#address").val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($("#provinceID").val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($("#districtID").val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($("#wardID").val()).change();
            $($scope.items.isDisplayCopy).val(true);
        }
    };

    /**
     * Tự động lấy code cuối cùng
     * @returns {undefined}
     */
    $scope.codeSupport = function () {
        $.ajax({
            url: '/service/opc-arv/last-opc.json',
            success: function (resp) {
                if (resp.success && resp.data != null) {
                    var html = '<small class="form-text text-muted code-last" >Mã KH cuối cùng: ' + resp.data.code + '</small>';
                    if ($("#code").parent().find(".code-last").length > 0) {
                        $("#code .code-last").html(html);
                    } else {
                        $("#code").after(html);
                    }
                }
            }
        });
    };

    $scope.getDateFromString = function (date) {
        let d = date.split('/')
        let newDate = d[1] + '/' + d[0] + '/' + d[2];
        return new Date(newDate)
    };

});

app.controller('opc_arv_index', function ($scope, msg, $uibModal, arvService) {
    if ($.getQueryParameters().code != null ||
            $.getQueryParameters().name != null ||
            $.getQueryParameters().identityCard != null ||
            $.getQueryParameters().insuranceNo != null ||
            $.getQueryParameters().permanent_district_id != null ||
            $.getQueryParameters().permanent_ward_id != null ||
            $.getQueryParameters().registration_time_from != null ||
            $.getQueryParameters().registration_time_to != null ||
            $.getQueryParameters().treatment_time_from != null ||
            $.getQueryParameters().treatment_time_to != null ||
            $.getQueryParameters().status_of_treatment_id != null ||
            $.getQueryParameters().therapy_site_id != null ||
            $.getQueryParameters().insurance != null ||
            $.getQueryParameters().insurance_type != null ||
            $.getQueryParameters().treatment_stage_time_from != null ||
            $.getQueryParameters().treatment_stage_time_to != null ||
            $.getQueryParameters().treatment_stage_id != null ||
            $.getQueryParameters().insurance_expiry != null
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        insurance: "#insurance",
        insurance_type: "#insurance_type",
        treatment_stage_id: "#treatment_stage_id",
        status_of_treatment_id: "#status_of_treatment_id",
        therapy_site_id: "#therapy_site_id",
        permanentProvinceID: "#permanent_province_id",
        permanentDistrictID: "#permanent_district_id",
        permanentWardID: "#permanent_ward_id",
        treatmentRegimenID: "#treatmentRegimenID",
        insuranceExpiry: "#insurance_expiry"
    };

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };


    $scope.excel = function () {
        window.location.href = $scope.getUrl("/report/opc-arv-grid-inbound/excel.html");
    };
    
    $scope.init = function () {
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);

        $scope.switchConfig();
        $scope.treatment_stage_time_from = $.getQueryParameters().treatment_stage_time_from;
        $scope.treatment_stage_time_to = $.getQueryParameters().treatment_stage_time_to;
        $scope.registration_time_from = $.getQueryParameters().registration_time_from;
        $scope.registration_time_to = $.getQueryParameters().registration_time_to;
        $scope.treatment_time_from = $.getQueryParameters().treatment_time_from;
        $scope.treatment_time_to = $.getQueryParameters().treatment_time_to;
        $scope.insurance_expiry = $.getQueryParameters().insurance_expiry;

        $scope.dateOfArrivalFrom = $.getQueryParameters().dateOfArrivalFrom;
        $scope.dateOfArrivalTo = $.getQueryParameters().dateOfArrivalTo;
        $scope.tranferToTimeFrom = $.getQueryParameters().tranferToTimeFrom;
        $scope.tranferToTimeTo = $.getQueryParameters().tranferToTimeTo;
        $scope.tranferFromTimeFrom = $.getQueryParameters().tranferFromTimeFrom;
        $scope.tranferFromTimeTo = $.getQueryParameters().tranferFromTimeTo;


        $scope.select_mutiple($scope.items.status_of_treatment_id, "Tất cả");
        $scope.select_mutiple($scope.items.treatment_stage_id, "Tất cả");
        $scope.select_mutiple($scope.items.treatmentRegimenID, "Tất cả");
        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.insurance_type, "");
        $scope.$parent.select_search($scope.items.therapy_site_id, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");

        $scope.id = utils.getContentOfDefault($.getQueryParameters().id, '0');
        $scope.pageRedirect = utils.getContentOfDefault($.getQueryParameters().page_redirect, '0');
        
        //In phiếu phản hồi tiếp nhận
        if ($scope.id != null && $scope.pageRedirect === 'printable') {
            setTimeout(function () {
                $scope.dialogReport("/report/opc-arv/print.html?arv_id=" + $scope.id + "&type=feedback", null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }, 300);
        }

        $scope.print = function (oid) {
            if (oid != null) {
                $scope.dialogReport("/report/opc-arv/print.html?arv_id=" + oid, null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }

        };
        

    };

    //In phiếu chuyển gửi điều trị
    $scope.printSend = function (oid) {
        let url = urlPrinted + "?arv_id=" + oid + "&type=transfer";
        $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
//        $scope.dialogReport(urlPrinted + "?arv_id=" + oid + "&type=transfer", null, "Phiếu chuyển gửi điều trị");
//        $("#pdf-loading").remove();
    };

    //In phiếu chuyển gửi Lao
    $scope.printLao = function (oid) {
        $scope.dialogReport(urlTransferLao + "?arv_id=" + oid, null, "Phiếu chuyển gửi Lao");
        $("#pdf-loading").remove();
    };
    
    //In phiếu chuyển tuyến theo bảo hiểm
    $scope.printTransitTemp = function (oid) {
        $scope.dialogReport(urlTransitTemp + "?arv_id=" + oid, null, "Phiếu chuyển tuyến theo bảo hiểm");
        $("#pdf-loading").remove();
    };

    $scope.qr = function (oid) {
        let oids = oid;
        if (!oid) {
            oids = $scope.getSwitch();
            if (oids == null || oids.length == 0) {
                msg.warning("Bạn chưa chọn bản ghi để in mã QR");
                return false;
            }
        }
        let url = urlQr + "?arv_id=" + oids;
        if ($('#frm-print').length > 0) {
            $('#frm-print').remove();
        }
        $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
    };

    $scope.transferGSPHs = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để chuyển gửi GSPH");
            return false;
        }
        loading.show();
        $.ajax({
            url: urlTransfer + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'opcARvTransfer',
                        controller: 'opc_arv_transfer',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.entities,
                                    options: resp.data.options,
                                    i: resp.data.i,
                                    dialogReport: $scope.dialogReport
                                };
                            }
                        }
                    });
                }
            }
        });

    };


    //Chuyển gửi điều trị
    $scope.treatmentTransfer = function (oid) {
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
                        templateUrl: 'opcArvTreatmentTransfer',
                        controller: 'opc_arv_treatment_transfer',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    currentUserName: resp.data.currentUserName,
                                    item: resp.data.model,
                                    stageID: resp.data.stageID,
                                    options: resp.data.options,
                                    uiModals: $uibModal,
                                    dialogReport: $scope.dialogReport,
                                    select_search: $scope.$parent.select_search
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

    $scope.logs = function (oid, code, name) {
        arvService.logs(oid, code, name);
    };

    //Phản hồi tiếp nhận
    $scope.feedbackReceive = function (oid, type, isPaper) {
        arvService.feedbackReceive(oid, type, isPaper);
    };

    // Chuyển gửi GSPH
    $scope.transferGSPH = (oid) => {
        bootbox.confirm(
                {
                    message: 'Bạn chắc chắn muốn chuyển bệnh nhân sang báo cáo giám sát phát hiện không?',
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
    }

});

app.controller('opc_arv_transfer', function ($scope, $uibModalInstance, params, msg) {
    $scope.items = params.item;
    $scope.options = params.options;
    $scope.i = params.i;
    $scope.dialogReport = params.dialogReport;
    $scope.ok = function () {
        //lấy ids bên trong items
        var oids = [];
        for (var i = 0; i < $scope.items.length; i++) {
            oids.push($scope.items[i].id);
        }
        if (oids == null || oids.length == 0) {
            msg.warning("Chưa chọn khách hàng");
            return false;
        }
        $.ajax({
            url: urlTransferOPC + "?oid=" + oids.join(","),
            success: function (resp) {
                if (resp.success) {
                    msg.success("Chuyển gửi GSPH thành công", function () {
//                        location.reload();
//                        $uibModalInstance.dismiss('cancel');
//                        $scope.dialogReport(urlAnswerResult + "?oid=" + oids.join(","), null, "Phiếu trả lời kết quả xét nghiệm HIV");
                        window.location = "/backend/opc-arv/index.html";
                    }, 2000);
                }
            }
        });

    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});


app.controller('opc_viral_index', function ($scope, msg, $uibModal, arvService) {
    $scope.options = options;
    if ($.getQueryParameters().code != null ||
            $.getQueryParameters().name != null ||
            $.getQueryParameters().identityCard != null ||
            $.getQueryParameters().insuranceNo != null ||
            $.getQueryParameters().permanent_district_id != null ||
            $.getQueryParameters().permanent_ward_id != null ||
            $.getQueryParameters().registration_time_from != null ||
            $.getQueryParameters().registration_time_to != null ||
            $.getQueryParameters().treatment_time_from != null ||
            $.getQueryParameters().treatment_time_to != null ||
            $.getQueryParameters().status_of_treatment_id != null ||
            $.getQueryParameters().therapy_site_id != null ||
            $.getQueryParameters().insurance != null ||
            $.getQueryParameters().insurance_type != null ||
            $.getQueryParameters().treatment_stage_time_from != null ||
            $.getQueryParameters().treatment_stage_time_to != null ||
            $.getQueryParameters().treatment_stage_id != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        insurance: "#insurance",
        insurance_type: "#insurance_type",
        treatment_stage_id: "#treatment_stage_id",
        status_of_treatment_id: "#status_of_treatment_id",
        therapy_site_id: "#therapy_site_id",
        permanentProvinceID: "#permanent_province_id",
        permanentDistrictID: "#permanent_district_id",
        permanentWardID: "#permanent_ward_id"

    };

    $scope.init = function () {
        $("#insurance option[value='3']").remove();
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);

        $scope.switchConfig();
        $scope.treatment_stage_time_from = $.getQueryParameters().treatment_stage_time_from;
        $scope.treatment_stage_time_to = $.getQueryParameters().treatment_stage_time_to;
        $scope.registration_time_from = $.getQueryParameters().registration_time_from;
        $scope.registration_time_to = $.getQueryParameters().registration_time_to;
        $scope.treatment_time_from = $.getQueryParameters().treatment_time_from;
        $scope.treatment_time_to = $.getQueryParameters().treatment_time_to;
        $scope.viral_load_time_from = $.getQueryParameters().viral_load_time_from;
        $scope.viral_load_time_to = $.getQueryParameters().viral_load_time_to;
        $scope.viral_load_retry_from = $.getQueryParameters().viral_load_retry_from;
        $scope.viral_load_retry_to = $.getQueryParameters().viral_load_retry_to;


        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.insurance_type, "");
        $scope.$parent.select_search($scope.items.treatment_stage_id, "");
        $scope.$parent.select_search($scope.items.status_of_treatment_id, "");
        $scope.$parent.select_search($scope.items.therapy_site_id, "");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
        $scope.$parent.select_search($scope.items.permanentWardID, "");

    };

    $scope.print = function (oid) {
        $scope.dialogReport(urlPrinted + "?id=" + oid + "&type=transfer", null, "Phiếu chuyển gửi điều trị");
        $("#pdf-loading").remove();
    };

    $scope.treatmentTransfer = function (oid) {
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
                        templateUrl: 'opcArvTreatmentTransfer',
                        controller: 'opc_arv_treatment_transfer',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    currentUserName: resp.data.currentUserName,
                                    item: resp.data.model,
                                    stageID: resp.data.stageID,
                                    options: resp.data.options,
                                    uiModals: $uibModal,
                                    dialogReport: $scope.dialogReport,
                                    select_search: $scope.$parent.select_search
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

    $scope.logs = function (oid, code, name) {
        arvService.logs(oid, code, name);
    };

    $scope.appointment = function (id) {
        $.ajax({
            url: urlGets,
            data: {id: id},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'viralAppointmentForms',
                        controller: 'opc_viral_appointment_form',
                        size: 'xl',
                        resolve: {
                            params: function () {
                                return {
                                    id: id,
                                    form: resp.data.form,
                                    entity: resp.data.entity,
                                    select_search: $scope.$parent.select_search,
                                    select_mutiple: $scope.$parent.select_mutiple
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
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };


    $scope.excel = function () {
        window.location.href = $scope.getUrl("/report/opc-viral-grid/excel.html");
    };
});


app.controller('opc_viral_appointment_form', function ($scope, $uibModalInstance, params, msg) {

    $scope.id = params.id;
    $scope.urlSave = urlSave;
    $scope.options = options;
    $scope.form = params.form;
    $scope.entity = params.entity;
    $scope.errors = {};
    $scope.item = params.form;

    //kiểm tra dữ liệu
    $scope.init = function () {

        setTimeout(() => {

            params.select_mutiple("#viralLoadCauses", "Chọn lý do xét nghiệm");
            $("#viralLoadCauses").val($scope.item.viralLoadCauses).trigger('change');

        }, 100);

    };


    $scope.ok = function () {
        let url = urlSave + "?id=" + $scope.id;

        //Lấy lại giá trị chọn nhiều
        $scope.item.viralLoadCauses = $("#viralLoadCauses").val();

        loading.show();
        $.ajax({
            url: url,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.item),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = {};
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.href = '/backend/opc-viralload/index.html?tab=all';
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

app.controller('opc_visit_index', function ($scope, msg, $uibModal, arvService) {
    if ($.getQueryParameters().code != null ||
            $.getQueryParameters().name != null ||
            $.getQueryParameters().identityCard != null ||
            $.getQueryParameters().insuranceNo != null ||
            $.getQueryParameters().permanent_district_id != null ||
            $.getQueryParameters().permanent_ward_id != null ||
            $.getQueryParameters().registration_time_from != null ||
            $.getQueryParameters().registration_time_to != null ||
            $.getQueryParameters().treatment_time_from != null ||
            $.getQueryParameters().treatment_time_to != null ||
            $.getQueryParameters().status_of_treatment_id != null ||
            $.getQueryParameters().therapy_site_id != null ||
            $.getQueryParameters().insurance != null ||
            $.getQueryParameters().insurance_type != null ||
            $.getQueryParameters().treatment_stage_time_from != null ||
            $.getQueryParameters().treatment_stage_time_to != null ||
            $.getQueryParameters().treatment_stage_id != null ||
            $.getQueryParameters().last_examination_time_from != null ||
            $.getQueryParameters().last_examination_time_to != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        insurance: "#insurance",
        insurance_type: "#insurance_type",
        treatment_stage_id: "#treatment_stage_id",
        status_of_treatment_id: "#status_of_treatment_id",
        therapy_site_id: "#therapy_site_id",
        permanentProvinceID: "#permanent_province_id",
        permanentDistrictID: "#permanent_district_id",
        permanentWardID: "#permanent_ward_id"

    };

    $scope.init = function () {
        $("#insurance option[value='3']").remove();
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);

        $scope.switchConfig();
        $scope.treatment_stage_time_from = $.getQueryParameters().treatment_stage_time_from;
        $scope.treatment_stage_time_to = $.getQueryParameters().treatment_stage_time_to;
        $scope.registration_time_from = $.getQueryParameters().registration_time_from;
        $scope.registration_time_to = $.getQueryParameters().registration_time_to;
        $scope.treatment_time_from = $.getQueryParameters().treatment_time_from;
        $scope.treatment_time_to = $.getQueryParameters().treatment_time_to;
        $scope.viral_load_time_from = $.getQueryParameters().viral_load_time_from;
        $scope.viral_load_time_to = $.getQueryParameters().viral_load_time_to;
        $scope.last_examination_time_from = $.getQueryParameters().last_examination_time_from;
        $scope.last_examination_time_to = $.getQueryParameters().last_examination_time_to;

        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.insurance_type, "");
        $scope.$parent.select_search($scope.items.treatment_stage_id, "");
        $scope.$parent.select_search($scope.items.status_of_treatment_id, "");
        $scope.$parent.select_search($scope.items.therapy_site_id, "");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
//        $scope.$parent.select_search($scope.items.permanentWardID, "");

    };

    $scope.print = function (oid) {
        $scope.dialogReport(urlPrinted + "?id=" + oid + "&type=transfer", null, "Phiếu chuyển gửi điều trị");
        $("#pdf-loading").remove();
    };

    $scope.treatmentTransfer = function (oid) {
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
                        templateUrl: 'opcArvTreatmentTransfer',
                        controller: 'opc_arv_treatment_transfer',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    currentUserName: resp.data.currentUserName,
                                    item: resp.data.model,
                                    stageID: resp.data.stageID,
                                    options: resp.data.options,
                                    uiModals: $uibModal,
                                    dialogReport: $scope.dialogReport,
                                    select_search: $scope.$parent.select_search
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

    $scope.logs = function (oid, code, name) {
        arvService.logs(oid, code, name);
    };
});

app.controller('opc_receive_index', function ($scope, msg, $uibModal, arvService) {

    $scope.confirm_time = $.getQueryParameters().confirm_time;
    $scope.exchange_time = $.getQueryParameters().exchange_time;
    $scope.id = $.getQueryParameters().id;
    $scope.pageRedirect = $.getQueryParameters().page_redirect;
    $scope.isQueryString = ($.getQueryParameters().code != null
            || $.getQueryParameters().name != null
            || $.getQueryParameters().confirm_code != null
            || $.getQueryParameters().tranfer_time != null
            || $.getQueryParameters().source_site_id != null
            || $.getQueryParameters().status != null
            || $.getQueryParameters().confirm_time != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.confirm_time = $.getQueryParameters().confirm_time;
        $scope.tranfer_time = $.getQueryParameters().tranfer_time;

        $scope.$parent.select_search("#source_site_id", "");

        // In phiếu phản hồi tiếp nhận
        if ($scope.id !== null && $scope.pageRedirect === 'printable') {
            setTimeout(function () {
                $scope.dialogReport("/report/opc-arv/print.html?arv_id=" + $scope.id + "&type=feedback", null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }, 300);
        }

        //in phiếu phản hồi tiếp nhận
        $scope.print = function (oid) {
            if (oid != null) {
                $scope.dialogReport("/report/opc-from-arv/print-grid.html?from_arv_id=" + oid, null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }

        };
    };
    $scope.feedbackReceive = function (oid, type, isPaper) {
        arvService.feedbackReceive(oid, type, isPaper);
    };
});

app.controller('opc_arv_treatment_transfer', function ($scope, $uibModalInstance, msg, params) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.stageID = params.stageID === null ? '' : params.stageID;
    $scope.uiModals = params.uiModals;
    $scope.dialogReport = params.dialogReport;
    $scope.selectSearch = params.select_search;

    $scope.init = function () {
        $scope.selectSearch("#transferSiteID", "");
        $scope.selectSearch("#endCase", "");
        $scope.model.endCase = '3';
        $("#transferSiteName").val('').change();
        $("#transferSiteID").prepend("<option value=''>Chọn cơ sở chuyển đi</option>");
        $("#transferSiteID option[value='string:']").remove();
    };

    $scope.model = {
        endCase: params.item.endCase == null || params.item.endCase == '' ? "" : params.item.endCase,
        transferSiteID: params.item.transferSiteID == null || params.item.transferSiteID == '' ? "" : params.item.transferSiteID,
        transferSiteName: '',
        transferCase: '',
        registrationTime: utils.timestampToStringDate(params.item.registrationTime == null || params.item.registrationTime == '' || params.item.registrationTime == 0 ? '' : params.item.registrationTime),
        treatmentTime: utils.timestampToStringDate(params.item.treatmentTime == null || params.item.treatmentTime == '' || params.item.treatmentTime == 0 ? '' : params.item.treatmentTime),
        dob: utils.timestampToStringDate(params.item.patient.dob == null || params.item.patient.dob == '' || params.item.patient.dob == 0 ? '' : params.item.patient.dob),
        confirmTime: utils.timestampToStringDate(params.item.patient.confirmTime == null || params.item.patient.confirmTime == '' || params.item.patient.confirmTime == 0 ? '' : params.item.patient.confirmTime),
        tranferFromTime: utils.timestampToStringDate(new Date()),
        tranferBY: params.currentUserName,
        endTime: utils.timestampToStringDate(new Date())
    };



    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: "/service/opc-arv/treatment-transfer.json?oid=" + $scope.item.id + '&stage_id=' + $scope.stageID,
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

    $scope.transferSiteIDChange = function () {
        if ($scope.model.transferSiteID !== '-1') {
            $scope.model.transferSiteName = '';
        }
    };
});

app.controller('arv_htc_receive', function ($scope, msg, $uibModal, arvService) {
    if ($.getQueryParameters().name != null ||
            $.getQueryParameters().confirm_test_no != null ||
            $.getQueryParameters().confirm_time != null ||
            $.getQueryParameters().exchange_time != null ||
            $.getQueryParameters().transfer_site != null ||
            $.getQueryParameters().receive_status != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.init = function () {
        $scope.switchConfig();
        $scope.confirm_time = $.getQueryParameters().confirm_time;
        $scope.exchange_time = $.getQueryParameters().exchange_time;
        $scope.id = $.getQueryParameters().id;
        $scope.htcid = $.getQueryParameters().htc_id;
        $scope.pageRedirect = $.getQueryParameters().page_redirect;

        $scope.$parent.select_search("#transfer_site", "");
        $scope.$parent.select_search("#receive_status", "");

        if ($scope.id != null && $scope.pageRedirect === 'printable') {
            setTimeout(function () {
                $scope.dialogReport("/report/opc-arv/print.html?arv_id=" + $scope.id + "&htc_id=" + $scope.htcid + "&type=feedback", null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }, 300);
        }
        //in phiếu phản hồi tiếp nhận
        $scope.print = function (oid) {
            if (oid != null) {
                $scope.dialogReport("/report/opc-from-htc/print-grid.html?htc_id=" + oid, null, "Phiếu phản hồi tiếp nhận điều trị");
                $("#pdf-loading").remove();
            }

        };

    };
    $scope.feedbackReceive = function (oid, type, isPaper) {
        arvService.feedbackReceive(oid, type, isPaper);
    };
});

app.controller('opc_arv_feedback_receive', function ($scope, $uibModalInstance, msg, params) {
    $scope.type = params.type;
    $scope.options = params.options;
    $scope.itemOld = params.itemOld;
    $scope.itemNew = params.itemNew;
    $scope.model = {
        code: params.itemNew.code == null || params.itemNew.code == '' ? "" : params.itemNew.code,
        siteID: params.itemNew.sourceSiteID,
        patientName: params.itemNew.patient.fullName,
        arrivalTime: utils.timestampToStringDate(params.itemNew.tranferToTime),
        confirmCode: params.itemNew.patient.confirmCode,
        exchangeTime: params.itemNew.sourceID == null || params.itemNew.sourceID == 0 ? utils.timestampToStringDate(params.itemNew.dateOfArrival) : utils.timestampToStringDate($scope.type == '100' ? params.itemOld.exchangeTime : params.itemOld.tranferFromTime),
//        exchangeTime: $scope.type == '100' ? utils.timestampToStringDate(params.itemOld == null ? params.itemNew.dateOfArrival : params.itemOld.exchangeTime) : $scope.type == '103' ? utils.timestampToStringDate(params.itemOld == null ? params.itemNew.dateOfArrival : params.itemOld.tranferFromTime) : utils.timestampToStringDate(params.itemNew.dateOfArrival),
        yearOfBirth: utils.timestampToStringDate(params.itemNew.patient.dob),
        feedbackReceiveTime: utils.timestampToStringDate(new Date()),
    };

    $scope.ok = function () {
        var idEntity = $scope.itemOld == null ? $scope.itemNew.id : $scope.itemOld.id;
        var isPaper = $scope.itemOld == null;
        loading.show();
        $.ajax({
            url: "/service/opc-arv/feedback-receive.json?oid=" + idEntity +
                    "&type=" + params.type + "&isPaper=" + isPaper,
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

app.controller('opc_receive_new', function ($scope, msg, localStorageService) {
    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.arvID = utils.getContentOfDefault(form.arvID, '');
    $scope.pageRedirect = utils.getContentOfDefault(form.pageRedirect, '');
    $scope.currentSiteID = utils.getContentOfDefault(form.currentSiteID, '');
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;
    $scope.ntch = utils.getContentOfDefault(form.ntch, '');
    $scope.supporterRelation = utils.getContentOfDefault(form.supporterRelation, '');
    $scope.confirmSiteID = utils.getContentOfDefault(form.confirmSiteID, '');
    $scope.confirmSiteName = utils.getContentOfDefault(form.confirmSiteName, '');
    $scope.pcrOneTime = utils.getContentOfDefault(form.pcrOneTime, '');
    $scope.dob = utils.getContentOfDefault(form.dob, '');
    $scope.pcrTwoTime = utils.getContentOfDefault(form.pcrTwoTime, '');
    $scope.pcrOneResult = utils.getContentOfDefault(form.pcrOneResult, '');
    $scope.pcrTwoResult = utils.getContentOfDefault(form.pcrTwoResult, '');
    $scope.therapySiteID = utils.getContentOfDefault(form.therapySiteID, '');
    $scope.registrationType = utils.getContentOfDefault(form.registrationType, '');
    $scope.sourceServiceID = utils.getContentOfDefault(form.sourceServiceID, '');
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.sourceSiteName = utils.getContentOfDefault(form.sourceSiteName, '');
    $scope.sourceCode = utils.getContentOfDefault(form.sourceCode, '');
    $scope.clinicalStage = utils.getContentOfDefault(form.clinicalStage, '');
    $scope.lao = utils.getContentOfDefault(form.lao, '');
    $scope.laoTestTime = utils.getContentOfDefault(form.laoTestTime, '');
    $scope.laoSymptoms = utils.getContentOfDefault(form.laoSymptoms, '');
    $scope.laoOtherSymptom = utils.getContentOfDefault(form.laoOtherSymptom, '');
    $scope.inh = utils.getContentOfDefault(form.inh, '');
    $scope.inhFromTime = utils.getContentOfDefault(form.inhFromTime, '');
    $scope.inhToTime = utils.getContentOfDefault(form.inhToTime, '');
    $scope.inhEndCauses = utils.getContentOfDefault(form.inhEndCauses, '');
    $scope.ntchSymptoms = utils.getContentOfDefault(form.ntchSymptoms, '');
    $scope.ntchOtherSymptom = utils.getContentOfDefault(form.ntchOtherSymptom, '');
    $scope.cotrimoxazoleFromTime = utils.getContentOfDefault(form.cotrimoxazoleFromTime, '');
    $scope.cotrimoxazoleToTime = utils.getContentOfDefault(form.cotrimoxazoleToTime, '');
    $scope.cotrimoxazoleEndCauses = utils.getContentOfDefault(form.cotrimoxazoleEndCauses, '');
    $scope.statusOfTreatmentID = utils.getContentOfDefault(form.statusOfTreatmentID, '');
    $scope.firstTreatmentRegimenID = utils.getContentOfDefault(form.firstTreatmentRegimenID, '');
    $scope.treatmentRegimenStage = utils.getContentOfDefault(form.treatmentRegimenStage, '');
    $scope.treatmentRegimenID = utils.getContentOfDefault(form.treatmentRegimenID, '');
    $scope.medicationAdherence = utils.getContentOfDefault(form.medicationAdherence, '');
    $scope.firstCd4Result = utils.getContentOfDefault(form.firstCd4Result, '');
    $scope.firstCd4Causes = utils.getContentOfDefault(form.firstCd4Causes, '');
    $scope.cd4Result = utils.getContentOfDefault(form.cd4Result, '');
    $scope.cd4Causes = utils.getContentOfDefault(form.cd4Causes, '');
    $scope.firstViralLoadResult = utils.getContentOfDefault(form.firstViralLoadResult, '');
    $scope.firstViralLoadCauses = utils.getContentOfDefault(form.firstViralLoadCauses, '');
    $scope.viralLoadResult = utils.getContentOfDefault(form.viralLoadResult, '');
    $scope.viralLoadCauses = utils.getContentOfDefault(form.viralLoadCauses, '');
    $scope.hbv = utils.getContentOfDefault(form.hbv, '');
    $scope.hbvTime = utils.getContentOfDefault(form.hbvTime, '');
    $scope.hbvResult = utils.getContentOfDefault(form.hbvResult, '');
    $scope.hcv = utils.getContentOfDefault(form.hcv, '');
    $scope.hcvTime = utils.getContentOfDefault(form.hcvTime, '');
    $scope.hcvResult = utils.getContentOfDefault(form.hcvResult, '');
    $scope.endCase = utils.getContentOfDefault(form.endCase, '');
    $scope.transferCase = utils.getContentOfDefault(form.transferCase, '');
    $scope.transferSiteID = utils.getContentOfDefault(form.transferSiteID, '');
    $scope.treatmentStageID = utils.getContentOfDefault(form.treatmentStageID, '');
    $scope.insuranceType = utils.getContentOfDefault(form.insuranceType, '');
    $scope.insuranceSite = utils.getContentOfDefault(form.insuranceSite, '');
    $scope.insuranceExpiry = utils.getContentOfDefault(form.insuranceExpiry, '');
    $scope.insurancePay = utils.getContentOfDefault(form.insurancePay, '');
    $scope.insurance = utils.getContentOfDefault(form.insurance, '');
    $scope.insuranceNo = utils.getContentOfDefault(form.insuranceNo, '');
    $scope.genderID = utils.getContentOfDefault(form.genderID, '');
    $scope.raceID = utils.getContentOfDefault(form.raceID, '');
    $scope.jobID = utils.getContentOfDefault(form.jobID, '');
    $scope.objectGroupID = utils.getContentOfDefault(form.objectGroupID, '');
    $scope.isDisplayCopy = utils.getContentOfDefault(form.isDisplayCopy, '');
    $scope.permanentAddressNo = utils.getContentOfDefault(form.permanentAddressNo, '');
    $scope.permanentAddress = utils.getContentOfDefault(form.permanentAddress, '');
    $scope.currentAddress = utils.getContentOfDefault(form.currentAddress, '');
    $scope.permanentAddressGroup = utils.getContentOfDefault(form.permanentAddressGroup, '');
    $scope.permanentAddressStreet = utils.getContentOfDefault(form.permanentAddressStreet, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentAddressNo = utils.getContentOfDefault(form.currentAddressNo, '');
    $scope.currentAddressGroup = utils.getContentOfDefault(form.currentAddressGroup, '');
    $scope.currentAddressStreet = utils.getContentOfDefault(form.currentAddressStreet, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.registrationTime = utils.getContentOfDefault(form.registrationTime, '');
    $scope.firstTreatmentTime = utils.getContentOfDefault(form.firstTreatmentTime, '');
    $scope.treatmentTime = utils.getContentOfDefault(form.treatmentTime, '');
    $scope.lastExaminationTime = utils.getContentOfDefault(form.lastExaminationTime, '');
    $scope.appointmentTime = utils.getContentOfDefault(form.appointmentTime, '');
    $scope.firstCd4Time = utils.getContentOfDefault(form.firstCd4Time, '');
    $scope.cd4Time = utils.getContentOfDefault(form.cd4Time, '');
    $scope.firstViralLoadTime = utils.getContentOfDefault(form.firstViralLoadTime, '');
    $scope.viralLoadTime = utils.getContentOfDefault(form.viralLoadTime, '');
    $scope.endTime = utils.getContentOfDefault(form.endTime, '');
    $scope.treatmentStageTime = utils.getContentOfDefault(form.treatmentStageTime, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.transferSiteName = utils.getContentOfDefault(form.transferSiteName, '');
    $scope.feedbackResultsReceivedTimeOpc = utils.getContentOfDefault(form.feedbackResultsReceivedTimeOpc, '');
    $scope.feedbackResultsReceivedTime = utils.getContentOfDefault(form.feedbackResultsReceivedTime, '');
    $scope.tranferFromTime = utils.getContentOfDefault(form.tranferFromTime, '');
    $scope.tranferToTime = utils.getContentOfDefault(form.tranferToTime, '');
    $scope.dateOfArrival = utils.getContentOfDefault(form.dateOfArrival, '');
    $scope.viralLoadResultNumber = utils.getContentOfDefault(form.viralLoadResultNumber, '');
    $scope.tranferToTimeOpc = utils.getContentOfDefault(form.tranferToTimeOpc, '');
    $scope.qualifiedTreatmentTime = utils.getContentOfDefault(form.qualifiedTreatmentTime, '');
    $scope.laoStartTime = utils.getContentOfDefault(form.laoStartTime, '');
    $scope.laoEndTime = utils.getContentOfDefault(form.laoEndTime, '');
    $scope.receivedWardDate = utils.getContentOfDefault(form.receivedWardDate, '');
    $scope.pregnantStartDate = utils.getContentOfDefault(form.pregnantStartDate, '');
    $scope.pregnantEndDate = utils.getContentOfDefault(form.pregnantEndDate, '');
    $scope.joinBornDate = utils.getContentOfDefault(form.joinBornDate, '');
    $scope.laoTestDate = utils.getContentOfDefault(form.laoTestDate, '');
    $scope.opcID = $.getQueryParameters().opc_id;
    $scope.opcNewID = $.getQueryParameters().opc_new_id;

    $.validator.addMethod("validCode", function (value) {
        return value.match(/[^x]\d$/);
    }, "Mã khách hàng không đúng định dạng");

    // Custom validate phone Vietname
    $.validator.addMethod("validPhone", function (value, element) {
        return value.match(/^$|([0-9])\b/); // Regex valid for empty string or number only
    }, "Số điện thoại không hợp lệ");


    $scope.items = {
        pregnantStartDate: '#pregnantStartDate',
        pregnantEndDate: '#pregnantEndDate',
        joinBornDate: '#joinBornDate',
        suspiciousSymptoms: "#suspiciousSymptoms",
        examinationAndTest: "#examinationAndTest",
        laoTestDate: "#laoTestDate",
        laoDiagnose: "#laoDiagnose",
        cotrimoxazoleOtherEndCause: "#cotrimoxazoleOtherEndCause",
        passTreatment: "#passTreatment",
        quickByTreatmentTime: "#quickByTreatmentTime",
        quickByConfirmTime: "#quickByConfirmTime",
        feedbackResultsReceivedTimeOpc: '#feedbackResultsReceivedTimeOpc',
        feedbackResultsReceivedTime: '#feedbackResultsReceivedTime',
        tranferFromTime: '#tranferFromTime',
        tranferToTime: '#tranferToTime',
        dateOfArrival: '#dateOfArrival',
        viralLoadResultNumber: '#viralLoadResultNumber',
        tranferToTimeOpc: '#tranferToTimeOpc',

        ntch: "#ntch",
        firstTreatmentTime: "#firstTreatmentTime",
        treatmentTime: "#treatmentTime",
        supporterRelation: "#supporterRelation",
        confirmSiteID: "#confirmSiteID",
        confirmSiteName: "#confirmSiteName",
        pcrOneTime: "#pcrOneTime",
        dob: "#dob",
        pcrTwoTime: "#pcrTwoTime",
        registrationTime: "#registrationTime",
        treatmentStageTime: "#treatmentStageTime",
        pcrOneResult: "#pcrOneResult",
        pcrTwoResult: "#pcrTwoResult",
        therapySiteID: "#therapySiteID",
        registrationType: "#registrationType",
        sourceSiteID: "#sourceSiteID",
        sourceSiteName: "#sourceSiteName",
        sourceCode: "#sourceCode",
        clinicalStage: "#clinicalStage",
        lao: "#lao",
        laoTestTime: "#laoTestTime",
        laoSymptoms: "#laoSymptoms",
        laoOtherSymptom: "#laoOtherSymptom",
        laoResult: "#laoResult",
        laoTreatment: "#laoTreatment",
        laoStartTime: "#laoStartTime",
        laoEndTime: "#laoEndTime",
        inh: "#inh",
        inhFromTime: "#inhFromTime",
        inhToTime: "#inhToTime",
        inhEndCauses: "#inhEndCauses",
        ntchSymptoms: "#ntchSymptoms",
        ntchOtherSymptom: "#ntchOtherSymptom",
        cotrimoxazoleFromTime: "#cotrimoxazoleFromTime",
        cotrimoxazoleToTime: "#cotrimoxazoleToTime",
        cotrimoxazoleEndCauses: "#cotrimoxazoleEndCauses",
        statusOfTreatmentID: "#statusOfTreatmentID",
        qualifiedTreatmentTime: "#qualifiedTreatmentTime",
        firstTreatmentRegimenID: "#firstTreatmentRegimenID",
        treatmentRegimenStage: "#treatmentRegimenStage",
        treatmentRegimenID: "#treatmentRegimenID",
        medicationAdherence: "#medicationAdherence",
        firstCd4Result: "#firstCd4Result",
        firstCd4Causes: "#firstCd4Causes",
        cd4Result: "#cd4Result",
        cd4Causes: "#cd4Causes",
        firstViralLoadResult: "#firstViralLoadResult",
        firstViralLoadCauses: "#firstViralLoadCauses",
        viralLoadResult: "#viralLoadResult",
        viralLoadCauses: "#viralLoadCauses",
        hbv: "#hbv",
        hbvTime: "#hbvTime",
        hbvResult: "#hbvResult",
        hcv: "#hcv",
        hcvTime: "#hcvTime",
        hcvResult: "#hcvResult",
        endCase: "#endCase",
        endTime: "#endTime",
        transferCase: "#transferCase",
        transferSiteID: "#transferSiteID",
        treatmentStageID: "#treatmentStageID",
        treatmentStage: "#treatmentStage",
        insuranceType: "#insuranceType",
        insuranceSite: "#insuranceSite",
        insuranceExpiry: "#insuranceExpiry",
        insurancePay: "#insurancePay",
        insurance: "#insurance",
        insuranceNo: "#insuranceNo",
        route: "#route",
        genderID: "#genderID",
        raceID: "#raceID",
        jobID: "#jobID",
        objectGroupID: "#objectGroupID",
        isDisplayCopy: "#isDisplayCopy",
        permanentAddressNo: "#permanentAddressNo",
        permanentAddress: "#permanentAddress",
        currentAddress: "#currentAddress",
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
        patientPhone: "#patientPhone",
        supporterPhone: "#supporterPhone",
        confirmCode: "#confirmCode",
        confirmTime: "#confirmTime",
        code: "#code",
        transferSiteName: "#transferSiteName",
        lastExaminationTime: "#lastExaminationTime",
        firstCd4Time: "#firstCd4Time",
        cd4Time: "#cd4Time",
        firstViralLoadTime: "#firstViralLoadTime",
        viralLoadTime: "#viralLoadTime",
        daysReceived: "#daysReceived",
        receivedWardDate: "#receivedWardDate",
        appointmentTime: "#appointmentTime",
        identityCard: "#identityCard",
        fullName: "#fullName",
        pageRedirect: "#pageRedirect"
    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            code: {
                required: true,
            },
            registrationTime: {
                required: true,
            },
            fullName: {
                required: true,
            },
            genderID: {
                required: true
            },
            dob: {
                required: true,
            },
            insurance: {
                required: true
            },
            permanentProvinceID: {
                required: true
            },
            permanentDistrictID: {
                required: true
            },
            permanentWardID: {
                required: true
            },
            currentProvinceID: {
                required: true
            },
            currentDistrictID: {
                required: true
            },
            currentWardID: {
                required: true
            },
            confirmSiteID: {
                required: true
            },
            confirmSiteName: {
                required: function () {
                    return $($scope.items.confirmSiteID).val() === '-1';
                }
            },
            pcrOneTime: {
                required: function () {
                    return $($scope.items.pcrOneResult).val().length > 0;
                }
            },
            pcrOneResult: {
                required: function () {
                    return $($scope.items.pcrOneTime).val().length > 0;
                }
            },
            pcrTwoTime: {
                required: function () {
                    return $($scope.items.pcrTwoResult).val().length > 0;
                }
            },
            pcrTwoResult: {
                required: function () {
                    return $($scope.items.pcrTwoTime).val().length > 0;
                }
            },
            therapySiteID: {
                required: function () {
                    return isOpcManager === true;
                }
            },
            registrationType: {
                required: true
            },
            sourceSiteID: {
                required: function () {
                    return $($scope.items.registrationType).val() == '3';
                }
            },
            sourceSiteName: {
                required: function () {
                    return $($scope.items.sourceSiteID).val() == '-1';
                }
            },
            endCase: {
                required: function () {
                    return $($scope.items.endTime).val().length > 0;
                }
            },
            transferSiteID: {
                required: function () {
                    return $($scope.items.endCase).val() == '3';
                }
            },
            daysReceived: {
                digits: true
            },
            transferSiteName: {
                required: function () {
                    return $($scope.items.transferSiteID).val() == '-1';
                }
            },
            examinationNote: {
                maxlength: 100
            },
            note: {
                maxlength: 500
            },
            statusOfTreatmentID: {
                required: true
            },
            receivedWard: {
                maxlength: 500
            },
            laoTestTime: {
                required: function () {
                    return $($scope.items.lao).val() == '1';
                }
            },
            laoStartTime: {
                required: function () {
                    return $($scope.items.laoTreatment).val() == '1';
                }
            },
            inhFromTime: {
                required: function () {
                    return $($scope.items.inh).val() == '1';
                }
            },
            treatmentTime: {
                required: function () {
                    return $($scope.items.statusOfTreatmentID).val() == '3';
                }
            },
            endTime: {
                required: function () {
                    return $($scope.items.endCase).val() != null && $($scope.items.endCase).val() != '';
                }
            },
            tranferToTime: {
                required: true
            },
            firstCd4Result: {
                digits: true
            },
            cd4Result: {
                digits: true
            },
            permanentAddress: {
                maxlength: 500
            },
            permanentAddressStreet: {
                maxlength: 500
            },
            permanentAddressGroup: {
                maxlength: 500
            },
            currentAddress: {
                maxlength: 500
            },
            currentAddressStreet: {
                maxlength: 500
            },
            currentAddressGroup: {
                maxlength: 500
            },
            identityCard: {
                maxlength: 100
            },
            confirmCode: {
                maxlength: 100
            }
        },
        messages: {
            code: {
                required: "Mã bệnh án không được để trống",
            },
            registrationTime: {
                required: "Ngày đăng ký không được để trống",
            },
            fullName: {
                required: "Họ và tên không được để trống",
            },
            genderID: {
                required: "Giới tính không được để trống",
            },
            dob: {
                required: "Ngày sinh không được để trống",
            },
            insurance: {
                required: "BN có thẻ BHYT không được để trống",
            },
            permanentProvinceID: {
                required: "Tỉnh/TP thường trú không được để trống",
            },
            permanentDistrictID: {
                required: "Quận/Huyện thường trú không được để trống",
            },
            permanentWardID: {
                required: "Xã/Phường thường trú không được để trống",
            },
            currentProvinceID: {
                required: "Tỉnh/TP cư trú hiện tại không được để trống",
            },
            currentDistrictID: {
                required: "Quận/Huyện cư trú hiện tại không được để trống",
            },
            currentWardID: {
                required: "Xã/Phường cư trú hiện tại không được để trống",
            },
            confirmSiteID: {
                required: "Nơi XN khẳng định không được để trống",
            },
            confirmSiteName: {
                required: "Nơi XN khẳng định không được để trống",
            },
            pcrOneTime: {
                required: "Ngày XN PCR lần 1 không được để trống",
            },
            pcrOneResult: {
                required: "Kết quả XN PCR lần 1 không được để trống",
            },
            pcrTwoTime: {
                required: "Ngày XN PCR lần 2 không được để trống",
            },
            pcrTwoResult: {
                required: "Kết quả XN PCR lần 2 không được để trống",
            },
            therapySiteID: {
                required: "Cơ sở điều trị không được để trống",
            },
            registrationType: {
                required: "Loại đăng ký không được để trống",
            },
            sourceSiteID: {
                required: "Cơ sở chuyển đến không được để trống",
            },
            sourceSiteName: {
                required: "Cơ sở chuyển đến không được để trống",
            },
            endCase: {
                required: "Lý do kết thúc không được để trống",
            },
            transferSiteID: {
                required: "Cơ sở chuyển đi không được để trống",
            },
            daysReceived: {
                digits: "Số ngày nhận thuốc không hợp lệ "
            },
            transferSiteName: {
                required: "Cơ sở chuyển đi không được để trống"
            },
            examinationNote: {
                maxlength: "Các vấn đề khác không được quá 255 ký tự"
            },
            note: {
                maxlength: "Ghi chú không được quá 500 ký tự"
            },
            statusOfTreatmentID: {
                required: "Trạng thái điều trị không được để trống"
            },
            receivedWard: {
                maxlength: "Tên xã không được quá 500 ký tự"
            },
            laoTestTime: {
                required: "Ngày sàng lọc không được để trống"
            },
            laoStartTime: {
                required: "Ngày bắt đầu điều trị Lao không được để trống"
            },
            inhFromTime: {
                required: "Ngày bắt đầu INH không được để trống"
            },
            treatmentTime: {
                required: "Ngày điều trị ARV không được để trống"
            },
            endTime: {
                required: "Ngày kết thúc không được để trống"
            },
            tranferToTime: {
                required: "Ngày tiếp nhận không được để trống"
            },
            firstCd4Result: {
                digits: "Kết quả XN CD4 phải là số và không được âm"
            },
            cd4Result: {
                digits: "Kết quả XN CD4 phải là số và không được âm"
            },
            permanentAddress: {
                maxlength: "Số nhà thường trú không được quá 500 ký tự"
            },
            permanentAddressStreet: {
                maxlength: "Đường phố thường trú không được quá 500 ký tự"
            },
            permanentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố thường trú không được quá 500 ký tự"
            },
            currentAddress: {
                maxlength: "Số nhà cư trú hiện tại không được quá 500 ký tự"
            },
            currentAddressStreet: {
                maxlength: "Đường phố cư trú hiện tại không được quá 500 ký tự"
            },
            currentAddressGroup: {
                maxlength: "Tổ/ấp/Khu phố cư trú hiện tại không được quá 500 ký tự"
            },
            identityCard: {
                maxlength: "Số CMND không được quá 100 ký tự"
            },
            confirmCode: {
                maxlength: "Mã XN khẳng định không được quá 100 ký tự"
            }
        }
    });

    $scope.errors = {
        code: "Mã không được nhập ký tự đặc biệt"
    }

    function parseDate(str) {
        var m = str.match(/^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((18|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((18|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/);
        return m;
    }
    function CalculateWeekendDays(fromDate, toDate) {
        var weekendDayCount = 0;
        var diffTime = parseInt(toDate - fromDate);
//                Math.abs(toDate - fromDate);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        var dayMilliseconds = 1000 * 60 * 60 * 24;

        while (fromDate <= toDate) {
            var day = fromDate.getDay();
            if (day === 0 || day === 6) {
                weekendDayCount++;
            }
            fromDate = new Date(+fromDate + dayMilliseconds);
        }

        var result = diffDays - weekendDayCount;
        return result;
    }
    function quickTimeChange(fromDate, toDate) {
        if ($(toDate).val() != null && $(toDate).val() != '' && $($scope.items.passTreatment).val() == '1' &&
                $(fromDate).val() != null && $(fromDate).val() != '' &&
                parseDate($(fromDate).val()) && parseDate($(toDate).val())) {
            var date1 = new Date(parseInt($(fromDate).val().split("/")[2]), parseInt($(fromDate).val().split("/")[1]) - 1, parseInt($(fromDate).val().split("/")[0]));
            var date2 = new Date(parseInt($(toDate).val().split("/")[2]), parseInt($(toDate).val().split("/")[1]) - 1, parseInt($(toDate).val().split("/")[0]));
            var result = CalculateWeekendDays(date1, date2);
            if (result === 0) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('0').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('0').change();
                }
            } else if (result === 1) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('1').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('1').change();
                }
            } else if (result === 2) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('2').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('2').change();
                }
            } else if (result === 3) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('3').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('3').change();
                }
            } else if (result >= 4 && result <= 7) {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('4').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('4').change();
                }
            } else {
                if (fromDate == $scope.items.registrationTime) {
                    $($scope.items.quickByTreatmentTime).val('').change();
                } else {
                    $($scope.items.quickByConfirmTime).val('').change();
                }
            }
        }
    }

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
        //disable mặc định
        $($scope.items.fullName).attr("disabled", "disabled");
        $($scope.items.genderID).attr("disabled", "disabled");
        $($scope.items.dob).attr("disabled", "disabled");
        $($scope.items.identityCard).attr("disabled", "disabled");
        $($scope.items.raceID).attr("disabled", "disabled");
        $($scope.items.confirmCode).attr("disabled", "disabled");
        $($scope.items.confirmTime).attr("disabled", "disabled");
        $($scope.items.confirmSiteID).attr("disabled", "disabled");
        $($scope.items.confirmSiteName).attr("disabled", "disabled");
        $($scope.items.sourceSiteName).attr("disabled", "disabled");

        //Dữ liệu địa danh
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        // Trường hợp import excel không có thông tin tinh thành/ quận huyện/ xã phường
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $($scope.items.permanentProvinceID).val('').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $($scope.items.currentProvinceID).val('').change();
        }

        //event copy
        $("#permanentProvinceID, #permanentAddress, #permanentDistrictID, #permanentWardID, #currentAddressGroup, #currentAddressStreet").change(function () {
            if ($scope.isCopyPermanentAddress) {
                $($scope.items.currentAddress).val($($scope.items.permanentAddress).val()).change();
                $($scope.items.currentAddressGroup).val($($scope.items.permanentAddressGroup).val()).change();
                $($scope.items.currentAddressStreet).val($($scope.items.permanentAddressStreet).val()).change();
                $($scope.items.currentProvinceID).val($($scope.items.permanentProvinceID).val()).change();
                $($scope.items.currentDistrictID).val($($scope.items.permanentDistrictID).val()).change();
                $($scope.items.currentWardID).val($($scope.items.permanentWardID).val()).change();
            }
        });

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
            console.log("xx " + $scope.treatmentRegimenStage);



            $('#treatmentRegimenID')
                    .find('option')
                    .remove()
                    .end()
                    ;

            if ($scope.treatmentRegimenStage === '1') {
                for (const [key, value] of Object.entries(regimenOptions1)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else if ($scope.treatmentRegimenStage === '2') {
                for (const [key, value] of Object.entries(regimenOptions2)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else if ($scope.treatmentRegimenStage === '3') {
                for (const [key, value] of Object.entries(regimenOptions3)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            } else {
                for (const [key, value] of Object.entries(regimenOptions)) {
                    $($scope.items.treatmentRegimenID).append(new Option(value, key, false, false));
                }
                $($scope.items.treatmentRegimenID).val("");
            }
        }




        $scope.$parent.select_search($scope.items.viralLoadResult, "");
        $scope.$parent.select_search($scope.items.firstViralLoadResult, "");
        $scope.$parent.select_mutiple($scope.items.laoSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.inhEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.ntchSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.cotrimoxazoleEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.firstCd4Causes, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.cd4Causes, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.firstViralLoadCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.viralLoadCauses, "Chọn lý do");

        $scope.$parent.select_search($scope.items.supporterRelation, "");
        $scope.$parent.select_search($scope.items.ntch, "");
        $scope.$parent.select_search($scope.items.confirmSiteID, "");
        $scope.$parent.select_search($scope.items.pcrOneResult, "");
        $scope.$parent.select_search($scope.items.pcrTwoResult, "");
        $scope.$parent.select_search($scope.items.therapySiteID, "");
        $scope.$parent.select_search($scope.items.registrationType, "");
        $scope.$parent.select_search($scope.items.clinicalStage, "");
        $scope.$parent.select_search($scope.items.lao, "");
        $scope.$parent.select_search($scope.items.inh, "");
        $scope.$parent.select_search($scope.items.insuranceType, "");
        $scope.$parent.select_search($scope.items.statusOfTreatmentID, "");
        $scope.$parent.select_search($scope.items.firstTreatmentRegimenID, "");
        $scope.$parent.select_search($scope.items.treatmentRegimenStage, "");
        $scope.$parent.select_search($scope.items.treatmentRegimenID, "");
        $scope.$parent.select_search($scope.items.medicationAdherence, "");
        $scope.$parent.select_search($scope.items.hbv, "");
        $scope.$parent.select_search($scope.items.hbvResult, "");
        $scope.$parent.select_search($scope.items.hcv, "");
        $scope.$parent.select_search($scope.items.hcvResult, "");
        $scope.$parent.select_search($scope.items.hcvResult, "");
        $scope.$parent.select_search($scope.items.endCase, "");
        $scope.$parent.select_search($scope.items.transferSiteID, "");
        $scope.$parent.select_search($scope.items.treatmentStageID, "");
        $scope.$parent.select_search($scope.items.insuranceType, "");
        $scope.$parent.select_search($scope.items.insurancePay, "");
        $scope.$parent.select_search($scope.items.insurance, "");
        $scope.$parent.select_search($scope.items.genderID, "Chọn giới tính");
        $scope.$parent.select_search($scope.items.raceID, "Chọn dân tộc");
        $scope.$parent.select_search($scope.items.jobID, "Chọn nghề nghiệp");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn đối tượng");
        $scope.$parent.select_search($scope.items.sourceSiteID, "");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "");
        $scope.$parent.select_search($scope.items.permanentWardID, "");
        $scope.$parent.select_search($scope.items.currentProvinceID, "");
        $scope.$parent.select_search($scope.items.currentDistrictID, "");
        $scope.$parent.select_search($scope.items.currentWardID, "");
        $scope.$parent.select_search($scope.items.laoResult, "");
        $scope.$parent.select_search($scope.items.laoTreatment, "");

        $('#transferSiteID').children('option:first').text('Chọn cơ sở chuyển tới');
        $('#treatmentStageID').children('option:first').text('Chọn trạng thái biến động');
        $('#statusOfTreatmentID option[value="2"]').text("Đang chờ điều trị (pre-ARV)");

        $($scope.items.treatmentStageID).attr("disabled", "disabled");

        if ($scope.raceID == '') {
            $($scope.items.raceID).val('1').change();
        }

//        $($scope.items.dob).val($scope.dob).change();
        if ($($scope.items.dob).val() === null || $($scope.items.dob).val() === '' || typeof ($($scope.items.dob)) == 'undefined') {
            $($scope.items.pcrOneTime).attr("disabled", "disabled");
            $($scope.items.pcrOneResult).attr("disabled", "disabled");
            $($scope.items.pcrTwoTime).attr("disabled", "disabled");
            $($scope.items.pcrTwoResult).attr("disabled", "disabled");
        } else {
            var dataSplit = $($scope.items.dob).val().split('/');
            var dt1 = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
            var dt2 = new Date();
            var month = Math.floor((Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()) - Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate())) / (1000 * 60 * 60 * 24 * 30));
            if (month > 18) {
                $($scope.items.pcrOneTime).attr("disabled", "disabled");
                $($scope.items.pcrOneResult).attr("disabled", "disabled");
                $($scope.items.pcrTwoTime).attr("disabled", "disabled");
                $($scope.items.pcrTwoResult).attr("disabled", "disabled");
            }
        }

        if ($($scope.items.confirmSiteID).val() != '-1') {
            $($scope.items.confirmSiteName).attr("disabled", "disabled");
        }

//        $($scope.items.insurance).val($scope.insurance).change();

        if ($($scope.items.insurance).val() != '1') {
            $($scope.items.insuranceNo).attr("disabled", "disabled");
            $($scope.items.insuranceType).attr("disabled", "disabled");
            $($scope.items.insuranceSite).attr("disabled", "disabled");
            $($scope.items.insuranceExpiry).attr("disabled", "disabled");
            $($scope.items.insurancePay).attr("disabled", "disabled");
            $($scope.items.route).attr("disabled", "disabled");
        }

        if (isARV && !isOpcManager) {
            $($scope.items.therapySiteID).attr("disabled", "disabled");
            $($scope.items.therapySiteID).val($scope.currentSiteID).change();
        }

        if (isOpcManager) {
            $($scope.items.therapySiteID).removeAttr("disabled").change();
        }

        $($scope.items.sourceCode).attr("disabled", "disabled");
        if ($scope.sourceServiceID != null && $scope.sourceServiceID != '' && $scope.registrationType != '3') {
            $($scope.items.sourceSiteID).attr("disabled", "disabled");
            $($scope.items.sourceSiteName).attr("disabled", "disabled");
            $($scope.items.sourceCode).attr("disabled", "disabled");
            $($scope.items.tranferToTime).attr("disabled", "disabled");
            $($scope.items.dateOfArrival).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");

        } else {
            $($scope.items.sourceSiteID).removeAttr("disabled").change();
            $($scope.items.sourceCode).removeAttr("disabled").change();
            $($scope.items.tranferToTime).removeAttr("disabled").change();
            $($scope.items.dateOfArrival).removeAttr("disabled").change();
            $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
        }

        if ($($scope.items.sourceSiteID).val() != '-1') {
            $($scope.items.sourceSiteName).attr("disabled", "disabled");
        }

        if ($($scope.items.lao).val() != '1') {
            $($scope.items.laoTestTime).attr("disabled", "disabled");
            $($scope.items.laoSymptoms).attr("disabled", "disabled");
            $($scope.items.laoOtherSymptom).attr("disabled", "disabled");
//            $($scope.items.laoTreatment).val('').change();
            $($scope.items.suspiciousSymptoms).attr("disabled", "disabled");
            $($scope.items.examinationAndTest).attr("disabled", "disabled");
            $($scope.items.laoTestDate).attr("disabled", "disabled");
            $($scope.items.laoDiagnose).attr("disabled", "disabled");
        }

        if ($($scope.items.inh).val() != '1') {
            $($scope.items.inhFromTime).attr("disabled", "disabled");
            $($scope.items.inhToTime).attr("disabled", "disabled");
            $($scope.items.inhEndCauses).attr("disabled", "disabled");
        }

        if ($($scope.items.ntch).val() != '1') {
            $($scope.items.ntchSymptoms).attr("disabled", "disabled");
            $($scope.items.ntchOtherSymptom).attr("disabled", "disabled");
        }
        if ($scope.statusOfTreatmentID === '') {
            $($scope.items.statusOfTreatmentID).val('0').change();
        }

        if ($($scope.items.statusOfTreatmentID).val() === '2') {
            $($scope.items.qualifiedTreatmentTime).attr("disabled", "disabled");
            $($scope.items.treatmentTime).attr("disabled", "disabled");
            $("#treatmentRegimenStage").attr("disabled", "disabled");
            $($scope.items.treatmentRegimenID).attr("disabled", "disabled");
            $($scope.items.daysReceived).attr("disabled", "disabled");
            $($scope.items.receivedWardDate).attr("disabled", "disabled");
        }

        if ($($scope.items.hbv).val() != '1') {
            $($scope.items.hbvTime).attr("disabled", "disabled");
            $($scope.items.hbvResult).attr("disabled", "disabled");
        }

        if ($($scope.items.hcv).val() != '1') {
            $($scope.items.hcvTime).attr("disabled", "disabled");
            $($scope.items.hcvResult).attr("disabled", "disabled");
        }

        if ($($scope.items.endCase).val() != '3') {
            $($scope.items.transferSiteID).attr("disabled", "disabled");
            $($scope.items.transferCase).attr("disabled", "disabled");

            $($scope.items.tranferFromTime).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
            $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
        }

        if ($($scope.items.transferSiteID).val() != '-1') {
            $($scope.items.transferSiteName).attr("disabled", "disabled");
            $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
            $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
        }

        if ($($scope.items.endCase).val() != '') {
            if ($($scope.items.endCase).val() == '1') {
                $($scope.items.treatmentStageID).val('6').change();
            }
            if ($($scope.items.endCase).val() == '2') {
                $($scope.items.treatmentStageID).val('7').change();
            }
            if ($($scope.items.endCase).val() == '3') {
                $($scope.items.treatmentStageID).val('3').change();
            }
            if ($($scope.items.endCase).val() == '4') {
                $($scope.items.treatmentStageID).val('5').change();
            }
            if ($($scope.items.endCase).val() == '5') {
                $($scope.items.treatmentStageID).val('8').change();
            }
        }
        if ($scope.registrationType != '' && ($($scope.items.endCase).val() == '')) {
            if ($scope.registrationType == '1' || $scope.registrationType == '4' ||
                    $scope.registrationType == '5') {
                $($scope.items.treatmentStageID).val('1').change();
            }
            if ($scope.registrationType == '2') {
                $($scope.items.treatmentStageID).val('2').change();
            }
            if ($scope.registrationType == '3') {
                $($scope.items.treatmentStageID).val('4').change();
            }
        }

        if ($($scope.items.laoTreatment).val() != '1') {
            $($scope.items.laoStartTime).attr("disabled", "disabled");
            $($scope.items.laoEndTime).attr("disabled", "disabled");
        }

        if ($($scope.items.objectGroupID).val() == '5' || $($scope.items.objectGroupID).val() == '5.1' || $($scope.items.objectGroupID).val() == '5.2') {
            $($scope.items.pregnantStartDate).removeAttr("disabled").change();
            $($scope.items.pregnantEndDate).removeAttr("disabled").change();
            $($scope.items.joinBornDate).removeAttr("disabled").change();
        } else {
            $($scope.items.pregnantStartDate).attr("disabled", "disabled");
            $($scope.items.pregnantEndDate).attr("disabled", "disabled");
            $($scope.items.joinBornDate).attr("disabled", "disabled");
        }

        if ($($scope.items.laoSymptoms).val() != null &&
                ($($scope.items.laoSymptoms).val().indexOf("2") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("3") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("4") != '-1' ||
                        $($scope.items.laoSymptoms).val().indexOf("5") != '-1')) {
            $($scope.items.suspiciousSymptoms).val("1").change();
        }

        if ($($scope.items.suspiciousSymptoms).val() == '1') {
            $($scope.items.examinationAndTest).removeAttr("disabled").change();
        } else {
            $($scope.items.examinationAndTest).attr("disabled", "disabled").change();
        }
        if ($($scope.items.examinationAndTest).val() == '1') {
            $($scope.items.laoTestDate).removeAttr("disabled").change();
            $($scope.items.laoResult).removeAttr("disabled").change();
            $($scope.items.laoDiagnose).removeAttr("disabled").change();
        } else {
            $($scope.items.laoTestDate).attr("disabled", "disabled").change();
            $($scope.items.laoResult).attr("disabled", "disabled").change();
            $($scope.items.laoDiagnose).attr("disabled", "disabled").change();

            $($scope.items.laoTestDate).val("").change();
            $($scope.items.laoResult).val("").change();
            $($scope.items.laoDiagnose).val("").change();
        }
        if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
            var count = 0;
            for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                    count++;
                }
            }
            if (count > 0) {
                $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
            } else {
                $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
            }
        } else {
            $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
            $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
        }
        if ($($scope.items.statusOfTreatmentID).val() === '3') {
            $($scope.items.passTreatment).removeAttr("disabled").change();
        } else {
            $($scope.items.passTreatment).attr("disabled", "disabled");
        }
        if ($($scope.items.passTreatment).val() == '1') {
            $($scope.items.quickByTreatmentTime).removeAttr("disabled").change();
            $($scope.items.quickByConfirmTime).removeAttr("disabled").change();
        } else {
            $($scope.items.quickByTreatmentTime).attr("disabled", "disabled").change();
            $($scope.items.quickByTreatmentTime).val("").change();
            $($scope.items.quickByConfirmTime).attr("disabled", "disabled").change();
            $($scope.items.quickByConfirmTime).val("").change();
        }
        $($scope.items.insuranceNo).val($($scope.items.insuranceNo).val().toUpperCase()).change();
        $('form[name="opc_receive_form"]').ready(function () {
            $($scope.items.sourceCode).change(function () {
                if ($($scope.items.sourceCode).val() != '') {
                    setTimeout(function () {
                        $($scope.items.sourceCode).val($($scope.items.sourceCode).val().toUpperCase()).change();
                    }, 10);
                }
            });
            $($scope.items.code).change(function () {
                if ($($scope.items.code).val() != '') {
                    setTimeout(function () {
                        $($scope.items.code).val($($scope.items.code).val().toUpperCase()).change();
                    }, 10);
                }
            });
            $($scope.items.treatmentTime).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });
            $($scope.items.registrationTime).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
            });
            $($scope.items.confirmTime).change(function () {
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });
            $($scope.items.passTreatment).change(function () {
                quickTimeChange($scope.items.registrationTime, $scope.items.treatmentTime);
                quickTimeChange($scope.items.confirmTime, $scope.items.treatmentTime);
            });
//            if ($($scope.items.laoResult).val() != '2') {
//                $($scope.items.laoTreatment).attr("disabled", "disabled");
//                $($scope.items.laoTreatment).val('').change();
//            }
            $($scope.items.passTreatment).change(function () {
                if ($($scope.items.passTreatment).val() == '1') {
                    $($scope.items.quickByTreatmentTime).removeAttr("disabled").change();
                    $($scope.items.quickByConfirmTime).removeAttr("disabled").change();
                } else {
                    $($scope.items.quickByTreatmentTime).attr("disabled", "disabled").change();
                    $($scope.items.quickByTreatmentTime).val("").change();
                    $($scope.items.quickByConfirmTime).attr("disabled", "disabled").change();
                    $($scope.items.quickByConfirmTime).val("").change();
                }
            });
            $($scope.items.cotrimoxazoleEndCauses).change(function () {
                if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
                    var count = 0;
                    for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                        if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
                    } else {
                        $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                        $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
                    }
                } else {
                    $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                    $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
                }
            });
            $($scope.items.laoSymptoms).change(function () {
                var laoSymptoms = $($scope.items.laoSymptoms).val();
                if (laoSymptoms != null && (laoSymptoms.indexOf("2") != '-1' ||
                        laoSymptoms.indexOf("3") != '-1' ||
                        laoSymptoms.indexOf("4") != '-1' ||
                        laoSymptoms.indexOf("5") != '-1')) {
                    $($scope.items.suspiciousSymptoms).val("1").change();
                } else {
                    $($scope.items.suspiciousSymptoms).val("").change();
                }
            });

            $($scope.items.suspiciousSymptoms).change(function () {
                if ($($scope.items.suspiciousSymptoms).val() == '1') {
                    $($scope.items.examinationAndTest).removeAttr("disabled").change();
                } else {
                    $($scope.items.examinationAndTest).attr("disabled", "disabled").change();
                    $($scope.items.examinationAndTest).val("").change();
                }
            });

            $($scope.items.examinationAndTest).change(function () {
                if ($($scope.items.examinationAndTest).val() == '1') {
                    $($scope.items.laoTestDate).removeAttr("disabled").change();
                    $($scope.items.laoResult).removeAttr("disabled").change();
                    $($scope.items.laoDiagnose).removeAttr("disabled").change();
                } else {
                    $($scope.items.laoTestDate).attr("disabled", "disabled").change();
                    $($scope.items.laoResult).attr("disabled", "disabled").change();
                    $($scope.items.laoDiagnose).attr("disabled", "disabled").change();

                    $($scope.items.laoTestDate).val("").change();
                    $($scope.items.laoResult).val("").change();
                    $($scope.items.laoDiagnose).val("").change();
                }
            });

            $($scope.items.confirmCode).change(function () {
                if ($($scope.items.confirmCode).val() != '') {
                    setTimeout(function () {
                        $($scope.items.confirmCode).val($($scope.items.confirmCode).val().toUpperCase()).change();
                    }, 10);
                }
            });
            $($scope.items.insuranceNo).change(function () {
                if ($($scope.items.insuranceNo).val() != '') {
                    if ($($scope.items.insuranceNo).val().length == 15) {
                        var myMap = pOptions['insurance-type'];
                        for (var s in myMap) {
                            if ($($scope.items.insuranceNo).val().substring(0, 2).toUpperCase() == myMap[s].substring(0, 2)) {
                                $($scope.items.insuranceType).val(s).change();
                                break;
                            }
                        }
                        if ($($scope.items.insuranceNo).val().substring(2, 3) == '1' || $($scope.items.insuranceNo).val().substring(2, 3) == '2' || $($scope.items.insuranceNo).val().substring(2, 3) == '5') {
                            $($scope.items.insurancePay).val('1').change();
                        } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '3') {
                            $($scope.items.insurancePay).val('2').change();
                        } else if ($($scope.items.insuranceNo).val().substring(2, 3) == '4') {
                            $($scope.items.insurancePay).val('3').change();
                        } else {
                            $($scope.items.insurancePay).val('').change();
                        }
                    }

                }
            });
            $($scope.items.objectGroupID).change(function () {
                if ($($scope.items.objectGroupID).val() == '5' || $($scope.items.objectGroupID).val() == '5.1' || $($scope.items.objectGroupID).val() == '5.2') {
                    $($scope.items.pregnantStartDate).removeAttr("disabled").change();
                    $($scope.items.pregnantEndDate).removeAttr("disabled").change();
                    $($scope.items.joinBornDate).removeAttr("disabled").change();
                    $scope.objectGroupID = $($scope.items.objectGroupID).val();
                } else {
                    $($scope.items.pregnantStartDate).attr("disabled", "disabled");
                    $($scope.items.pregnantEndDate).attr("disabled", "disabled");
                    $($scope.items.joinBornDate).attr("disabled", "disabled");
                    $($scope.items.pregnantStartDate).val('').change();
                    $($scope.items.pregnantEndDate).val('').change();
                    $($scope.items.joinBornDate).val('').change();
                    $scope.objectGroupID = $($scope.items.objectGroupID).val();
                }
            });


            if ($scope.arvID != null) {
                $($scope.items.sourceCode).attr("disabled", "disabled");
                $($scope.items.dateOfArrival).attr("disabled", "disabled");
            }
            $($scope.items.dob).change(function () {
                if ($($scope.items.dob).val() != null && $($scope.items.dob).val() != '') {
                    var dataSplit = $($scope.items.dob).val().split('/');
                    var dt1 = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                    var dt2 = new Date();
                    var month = Math.floor((Date.UTC(dt2.getFullYear(), dt2.getMonth(), dt2.getDate()) - Date.UTC(dt1.getFullYear(), dt1.getMonth(), dt1.getDate())) / (1000 * 60 * 60 * 24 * 30));
                    if (month <= 18) {
                        $($scope.items.pcrOneTime).removeAttr("disabled").change();
                        $($scope.items.pcrOneResult).removeAttr("disabled").change();
                        $($scope.items.pcrTwoTime).removeAttr("disabled").change();
                        $($scope.items.pcrTwoResult).removeAttr("disabled").change();
                    } else {
                        $($scope.items.pcrOneTime).attr("disabled", "disabled");
                        $($scope.items.pcrOneResult).attr("disabled", "disabled");
                        $($scope.items.pcrTwoTime).attr("disabled", "disabled");
                        $($scope.items.pcrTwoResult).attr("disabled", "disabled");
                        $($scope.items.pcrOneTime).val("").change();
                        $($scope.items.pcrOneResult).val("").change();
                        $($scope.items.pcrTwoTime).val("").change();
                        $($scope.items.pcrTwoResult).val("").change();
                    }
                }
            });

            $($scope.items.statusOfTreatmentID).change(function () {
                if ($($scope.items.statusOfTreatmentID).val() === '3') {
                    $($scope.items.passTreatment).removeAttr("disabled").change();
                } else {
                    $($scope.items.passTreatment).attr("disabled", "disabled");
                    $($scope.items.passTreatment).val('').change();
                }
            });

            $($scope.items.statusOfTreatmentID).change(function () {
                if ($($scope.items.statusOfTreatmentID).val() === '2') {
                    $($scope.items.qualifiedTreatmentTime).attr("disabled", "disabled");
                    $($scope.items.treatmentTime).attr("disabled", "disabled");
                    $("#treatmentRegimenStage").attr("disabled", "disabled");
                    $($scope.items.treatmentRegimenID).attr("disabled", "disabled");
                    $($scope.items.daysReceived).attr("disabled", "disabled");
                    $($scope.items.receivedWardDate).attr("disabled", "disabled");

                    $($scope.items.qualifiedTreatmentTime).val("").change();
                    $($scope.items.treatmentTime).val("").change();
                    $("#treatmentRegimenStage").val("string:").change();
                    $($scope.items.treatmentRegimenID).val("").change();
                    $($scope.items.daysReceived).val("").change();
                    $($scope.items.receivedWardDate).val("").change();
                } else {
                    $($scope.items.qualifiedTreatmentTime).removeAttr("disabled").change();
                    $($scope.items.treatmentTime).removeAttr("disabled").change();
                    $("#treatmentRegimenStage").removeAttr("disabled").change();
                    $($scope.items.treatmentRegimenID).removeAttr("disabled").change();
                    $($scope.items.daysReceived).removeAttr("disabled").change();
                    $($scope.items.receivedWardDate).removeAttr("disabled").change();
                }
            });
            $($scope.items.insurance).change(function () {
                if ($($scope.items.insurance).val() != '1') {
                    $($scope.items.insuranceNo).attr("disabled", "disabled");
                    $($scope.items.insuranceType).attr("disabled", "disabled");
                    $($scope.items.insuranceSite).attr("disabled", "disabled");
                    $($scope.items.insuranceExpiry).attr("disabled", "disabled");
                    $($scope.items.insurancePay).attr("disabled", "disabled");
                    $($scope.items.route).attr("disabled", "disabled");
                    $($scope.items.insuranceNo).val("").change();
                    $($scope.items.insuranceType).val("").change();
                    $($scope.items.insuranceSite).val("").change();
                    $($scope.items.insuranceExpiry).val("").change();
                    $($scope.items.insurancePay).val("").change();
                    $($scope.items.route).val("").change();
                } else {
                    $($scope.items.insuranceNo).removeAttr("disabled").change();
                    $($scope.items.insuranceType).removeAttr("disabled").change();
                    $($scope.items.insuranceSite).removeAttr("disabled").change();
                    $($scope.items.insuranceExpiry).removeAttr("disabled").change();
                    $($scope.items.insurancePay).removeAttr("disabled").change();
                    $($scope.items.route).removeAttr("disabled").change();
                }
            });

            $($scope.items.confirmSiteID).change(function () {
                if ($($scope.items.confirmSiteID).val() == '-1') {
                    $($scope.items.confirmSiteName).removeAttr("disabled").change();
                } else {
                    $($scope.items.confirmSiteName).attr("disabled", "disabled");
                    $($scope.items.confirmSiteName).val("").change();
                }
            });

            $($scope.items.registrationType).change(function () {
                if ($($scope.items.registrationType).val() == '3') {
                    $($scope.items.sourceSiteID).removeAttr("disabled").change();
                    $($scope.items.sourceCode).removeAttr("disabled").change();

                    $($scope.items.tranferToTime).removeAttr("disabled").change();
                    $($scope.items.dateOfArrival).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTime).removeAttr("disabled").change();
                } else {
                    $($scope.items.sourceSiteID).attr("disabled", "disabled");
                    $($scope.items.sourceCode).attr("disabled", "disabled");
                    $($scope.items.sourceSiteID).val("").change();
                    $($scope.items.sourceSiteName).val("").change();
                    $($scope.items.sourceCode).val("").change();

                    $($scope.items.tranferToTime).attr("disabled", "disabled");
                    $($scope.items.dateOfArrival).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTime).attr("disabled", "disabled");
                    $($scope.items.tranferToTime).val("").change();
                    $($scope.items.dateOfArrival).val("").change();
                    $($scope.items.feedbackResultsReceivedTime).val("").change();
                }
            });

            $($scope.items.sourceSiteID).change(function () {
                if ($($scope.items.sourceSiteID).val() == '-1') {
                    $($scope.items.sourceSiteName).removeAttr("disabled").change();
                } else {
                    $($scope.items.sourceSiteName).attr("disabled", "disabled");
                    $($scope.items.sourceSiteName).val("").change();
                    $($scope.items.sourceCode).val("").change();
                }
            });

            $($scope.items.lao).change(function () {
                if ($($scope.items.lao).val() == '1') {
                    $($scope.items.laoTestTime).removeAttr("disabled").change();
                    $($scope.items.laoSymptoms).removeAttr("disabled").change();
                    $($scope.items.laoOtherSymptom).removeAttr("disabled").change();
                    $($scope.items.suspiciousSymptoms).removeAttr("disabled").change();
                } else {
                    $($scope.items.laoTestTime).attr("disabled", "disabled");
                    $($scope.items.laoSymptoms).attr("disabled", "disabled");
                    $($scope.items.laoOtherSymptom).attr("disabled", "disabled");
                    $($scope.items.laoTestTime).val("").change();
                    $($scope.items.laoSymptoms).val("").change();
                    $($scope.items.laoOtherSymptom).val("").change();

                    $($scope.items.suspiciousSymptoms).attr("disabled", "disabled");
                    $($scope.items.examinationAndTest).attr("disabled", "disabled");
                    $($scope.items.laoTestDate).attr("disabled", "disabled");
                    $($scope.items.laoDiagnose).attr("disabled", "disabled");
                    $($scope.items.suspiciousSymptoms).val("").change();
                    $($scope.items.examinationAndTest).val("").change();
                    $($scope.items.laoTestDate).val("").change();
                    $($scope.items.laoDiagnose).val("").change();
                }
            });

//            $($scope.items.laoResult).change(function () {
//                if ($($scope.items.laoResult).val() == '2') {
//                    $($scope.items.laoTreatment).removeAttr("disabled").change();
//                } else {
//                    $($scope.items.laoTreatment).attr("disabled", "disabled");
//                    $($scope.items.laoTreatment).val("").change();
//                }
//            });

            $($scope.items.laoTreatment).change(function () {
                if ($($scope.items.laoTreatment).val() == '1') {
                    $($scope.items.laoStartTime).removeAttr("disabled").change();
                    $($scope.items.laoEndTime).removeAttr("disabled").change();

                } else {
                    $($scope.items.laoStartTime).attr("disabled", "disabled");
                    $($scope.items.laoStartTime).val("").change();
                    $($scope.items.laoEndTime).attr("disabled", "disabled");
                    $($scope.items.laoEndTime).val("").change();
                }
            });

            $($scope.items.inh).change(function () {
                if ($($scope.items.inh).val() == '1') {
                    $($scope.items.inhFromTime).removeAttr("disabled").change();
                    $($scope.items.inhToTime).removeAttr("disabled").change();
                    $($scope.items.inhEndCauses).removeAttr("disabled").change();
                } else {
                    $($scope.items.inhFromTime).attr("disabled", "disabled");
                    $($scope.items.inhToTime).attr("disabled", "disabled");
                    $($scope.items.inhEndCauses).attr("disabled", "disabled");
                    $($scope.items.inhFromTime).val("").change();
                    $($scope.items.inhToTime).val("").change();
                    $($scope.items.inhEndCauses).val("").change();
                }
            });

            $($scope.items.ntch).change(function () {
                if ($($scope.items.ntch).val() == '1') {
                    $($scope.items.ntchSymptoms).removeAttr("disabled").change();
                    $($scope.items.ntchOtherSymptom).removeAttr("disabled").change();
                } else {
                    $($scope.items.ntchSymptoms).attr("disabled", "disabled");
                    $($scope.items.ntchOtherSymptom).attr("disabled", "disabled");
                    $($scope.items.ntchSymptoms).val("").change();
                    $($scope.items.ntchOtherSymptom).val("").change();
                }
            });
            $($scope.items.hbv).change(function () {
                if ($($scope.items.hbv).val() == '1') {
                    $($scope.items.hbvTime).removeAttr("disabled").change();
                    $($scope.items.hbvResult).removeAttr("disabled").change();
                } else {
                    $($scope.items.hbvTime).attr("disabled", "disabled");
                    $($scope.items.hbvResult).attr("disabled", "disabled");
                    $($scope.items.hbvTime).val("").change();
                    $($scope.items.hbvResult).val("").change();
                }
            });
            $($scope.items.hcv).change(function () {
                if ($($scope.items.hcv).val() == '1') {
                    $($scope.items.hcvTime).removeAttr("disabled").change();
                    $($scope.items.hcvResult).removeAttr("disabled").change();
                } else {
                    $($scope.items.hcvTime).attr("disabled", "disabled");
                    $($scope.items.hcvResult).attr("disabled", "disabled");
                    $($scope.items.hcvTime).val("").change();
                    $($scope.items.hcvResult).val("").change();
                }
            });
            $($scope.items.endCase).change(function () {
                if ($($scope.items.endCase).val() == '3') {
                    $($scope.items.transferSiteID).removeAttr("disabled").change();
                    $($scope.items.transferCase).removeAttr("disabled").change();

                    $($scope.items.tranferFromTime).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).removeAttr("disabled").change();
                    $($scope.items.tranferToTimeOpc).removeAttr("disabled").change();
                    $($scope.items.tranferFromTime).val($.datepicker.formatDate('dd/mm/yy', new Date())).change();
                } else {
                    $($scope.items.transferSiteID).attr("disabled", "disabled");
                    $($scope.items.transferCase).attr("disabled", "disabled");
                    $($scope.items.transferSiteName).attr("disabled", "disabled");
                    $($scope.items.tranferFromTime).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
                    $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");


                    $($scope.items.transferSiteID).val("").change();
                    $($scope.items.transferCase).val("").change();
                    $($scope.items.transferSiteName).val("").change();
                    $($scope.items.tranferFromTime).val("").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).val("").change();
                    $($scope.items.tranferToTimeOpc).val("").change();
                }
            });

            $($scope.items.transferSiteID).change(function () {
                if ($($scope.items.transferSiteID).val() == '-1') {
                    $($scope.items.transferSiteName).removeAttr("disabled").change();
                    $($scope.items.tranferToTimeOpc).removeAttr("disabled").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).removeAttr("disabled").change();
                } else {
                    $($scope.items.transferSiteName).attr("disabled", "disabled");
                    $($scope.items.tranferToTimeOpc).attr("disabled", "disabled");
                    $($scope.items.feedbackResultsReceivedTimeOpc).attr("disabled", "disabled");
                    $($scope.items.transferSiteName).val("").change();
                    $($scope.items.tranferToTimeOpc).val("").change();
                    $($scope.items.feedbackResultsReceivedTimeOpc).val("").change();
                }
            });
            $($scope.items.lastExaminationTime).change(function () {
                if ($($scope.items.lastExaminationTime).val() !== 'dd/mm/yyyy' && $($scope.items.lastExaminationTime).val() != null && $($scope.items.lastExaminationTime).val() != '' &&
                        $($scope.items.daysReceived).val() != null && $($scope.items.daysReceived).val() != '') {
                    var dataSplit = $($scope.items.lastExaminationTime).val().split('/');
                    var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                    var dayNumer = isNaN(parseInt($($scope.items.daysReceived).val())) ? 0 : parseInt($($scope.items.daysReceived).val());
                    formatedDate.setDate(formatedDate.getDate() + dayNumer);
                    $($scope.items.appointmentTime).val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
                }
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });
            $($scope.items.appointmentTime).change(function () {
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });
            $($scope.items.daysReceived).change(function () {
                if ($($scope.items.lastExaminationTime).val() !== 'dd/mm/yyyy' && $($scope.items.lastExaminationTime).val() != null && $($scope.items.lastExaminationTime).val() != '' &&
                        $($scope.items.daysReceived).val() != null && $($scope.items.daysReceived).val() != '') {
                    var dataSplit = $($scope.items.lastExaminationTime).val().split('/');
                    var formatedDate = new Date(dataSplit[1] + '/' + dataSplit[0] + '/' + dataSplit[2]);
                    var dayNumer = isNaN(parseInt($($scope.items.daysReceived).val())) ? 0 : parseInt($($scope.items.daysReceived).val());
                    formatedDate.setDate(formatedDate.getDate() + dayNumer);
                    $($scope.items.appointmentTime).val($.datepicker.formatDate('dd/mm/yy', formatedDate)).change();
                }
                $scope.calculateDay($scope.items.lastExaminationTime,$scope.items.appointmentTime,$scope.items.daysReceived);
            });
            
            $($scope.items.registrationType).change(function () {
                if ($($scope.items.endCase).val() == null || $($scope.items.endCase).val() == '') {
                    if ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '4' ||
                            $($scope.items.registrationType).val() == '5') {
                        $($scope.items.treatmentStageID).val('1').change();
                    }
                    if ($($scope.items.registrationType).val() == '2') {
                        $($scope.items.treatmentStageID).val('2').change();
                    }
                    if ($($scope.items.registrationType).val() == '3') {
                        $($scope.items.treatmentStageID).val('4').change();
                    }
                }
            });

            $($scope.items.registrationType).change(function () {
                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
                        ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '2' || $($scope.items.registrationType).val() == '3') &&
                        ($($scope.items.registrationTime).val() != null && $($scope.items.registrationTime).val() != '' && $($scope.items.registrationTime).val() != 'dd/mm/yyyy')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                }
            });

            $($scope.items.registrationTime).change(function () {
                if ($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') {
                    if ($($scope.items.registrationType).val() == '1' || $($scope.items.registrationType).val() == '2' ||
                            $($scope.items.registrationType).val() == '3') {
                        $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                    }
                }
            });

            $($scope.items.registrationTime).change(function () {
                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
                        ($scope.registrationType != '')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                }
            });


            $($scope.items.endCase).change(function () {
                if ($($scope.items.endCase).val() == '1') {
                    $($scope.items.treatmentStageID).val('6').change();
                }
                if ($($scope.items.endCase).val() == '2') {
                    $($scope.items.treatmentStageID).val('7').change();
                }
                if ($($scope.items.endCase).val() == '3') {
                    $($scope.items.treatmentStageID).val('3').change();
                }
                if ($($scope.items.endCase).val() == '4') {
                    $($scope.items.treatmentStageID).val('5').change();
                }
                if ($($scope.items.endCase).val() == '5') {
                    $($scope.items.treatmentStageID).val('8').change();
                }
                if (($($scope.items.endCase).val() == null || $($scope.items.endCase).val() == '') &&
                        ($scope.registrationType != '' || $scope.registrationType == '2' || $scope.registrationType == '3')) {
                    $($scope.items.treatmentStageID).val($scope.registrationType).change();
                }
                if (($($scope.items.endTime).val() != null && $($scope.items.endTime).val() != '' && $($scope.items.endTime).val() != 'dd/mm/yyyy') &&
                        ($($scope.items.endCase).val() != '')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.endTime).val()).change();
                }
            });
            $($scope.items.endTime).change(function () {
                if (($($scope.items.endTime).val() != null || $($scope.items.endTime).val() != '' || $($scope.items.endTime).val() != 'dd/mm/yyyy') &&
                        ($($scope.items.endCase).val() != '')) {
                    $($scope.items.treatmentStageTime).val($($scope.items.endTime).val()).change();
                }
                if (($($scope.items.endTime).val() == null || $($scope.items.endTime).val() == '' || $($scope.items.endTime).val() == 'dd/mm/yyyy') &&
                        ($scope.registrationType != '') &&
                        $($scope.items.registrationTime).val() != null && $($scope.items.registrationTime).val() != '' && $($scope.items.registrationTime).val() != 'dd/mm/yyyy') {
                    $($scope.items.treatmentStageTime).val($($scope.items.registrationTime).val()).change();
                }
            });
        });

        // Hiển thị popup confirm phản hổi tiếp nhận cơ sở điều trị
        if ($scope.opcID != null && $scope.opcID != '' && $scope.opcNewID != null && $scope.opcNewID != '') {
            bootbox.confirm(
                    {
                        message: 'Bạn có chắc chắn gửi phản hồi tiếp nhận bệnh nhân này cho cơ sở điều trị chuyển gửi không?',
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
                                    url: '/service/opc-from-arv/feedback-opc.json',
                                    data: {oldID: $scope.opcID, newID: $scope.opcNewID},
                                    method: 'GET',
                                    success: function (resp) {
                                        if (resp.success) {
                                            if (resp.message) {
                                                msg.success(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html?id=' + $scope.opcNewID + '&page_redirect=printable';
                                                }, 2000);
                                            }
                                        } else {
                                            if (resp.message) {
                                                msg.danger(resp.message, function () {
                                                    location.href = '/backend/opc-arv/index.html';
                                                }, 2000);
                                            }
                                        }
                                    }
                                });
                            } else {
                                location.href = '/backend/opc-arv/index.html';
                            }
                        }
                    }
            );
        }
    };

    if ($scope.arvID != "" && $($scope.items.isDisplayCopy).val() == "") {
        $scope.isCopyPermanentAddress = false;
        $($scope.items.isDisplayCopy).val(false);
    }


    $scope.copyAddress = function () {
        $scope.isCopyPermanentAddress = !$scope.isCopyPermanentAddress;
        if (!$scope.isCopyPermanentAddress) {
            $($scope.items.currentAddress).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressGroup).removeAttr("disabled").val('').change();
            $($scope.items.currentAddressStreet).removeAttr("disabled").val('').change();
            $($scope.items.currentProvinceID).removeAttr("disabled").val('').change();
            $($scope.items.currentDistrictID).removeAttr("disabled").val('').change();
            $($scope.items.currentWardID).removeAttr("disabled").val('').change();
            $($scope.items.isDisplayCopy).val(false);

        } else {
            $($scope.items.isDisplayCopy).val(true);
            $($scope.items.currentAddress).attr({disabled: "disable"}).val($("#permanentAddress").val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($("#permanentAddressStreet").val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($("#permanentProvinceID").val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($("#permanentDistrictID").val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($("#permanentWardID").val()).change();
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($("#permanentAddressGroup").val()).change();
        }
    };
    $scope.customSubmit = function (form, printable, $event) {
        var elm = $event.currentTarget;
        let flagCheck = true;
        $event.preventDefault();
        bootbox.hideAll();

        flagCheck = form.validate();

        //Tiếp nhận khách hàng từ OPC
        if (flagCheck) {
            $(".help-block-error").remove();
            if ($scope.feedbackResultsReceivedTime == null || $scope.feedbackResultsReceivedTime == '' || $scope.feedbackResultsReceivedTime == 'dd/mm/yyyy') {
                $($scope.items.pageRedirect).val("confirm");
                elm.form.submit();
            } else {
                $($scope.items.pageRedirect).val("printable");
                elm.form.submit();
            }
        } else {
            $scope.validationOptions;
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };
});

app.controller('htc_receive_view', function ($scope, msg) {

    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');

    $scope.items = {
        siteConfirmTest: "#siteConfirmTest",
        siteConfirmTestDsp: "#siteConfirmTestDsp",
    };

    $scope.init = function () {
        //Dữ liệu địa danh
        $scope.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        $("#permanentProvinceID").attr("disabled", "disabled");
        $("#permanentDistrictID").attr("disabled", "disabled");
        $("#permanentWardID").attr("disabled", "disabled");

        $scope.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");
        $("#currentProvinceID").attr("disabled", "disabled");
        $("#currentDistrictID").attr("disabled", "disabled");
        $("#currentWardID").attr("disabled", "disabled");

        $scope.initProvince("#exchangeProvinceID", "#exchangeDistrictID");
        $("#exchangeProvinceID").attr("disabled", "disabled");
        $("#exchangeDistrictID").attr("disabled", "disabled");

        $scope.initProvince("#therapyRegistProvinceID", "#therapyRegistDistrictID");
        $("#therapyRegistProvinceID").attr("disabled", "disabled");
        $("#therapyRegistDistrictID").attr("disabled", "disabled");

        $("#partnerProvideResultTooltip").attr("data-original-title", $("#partnerProvideResult").val());
        $("#arvExchangeResultTooltip").attr("data-original-title", $("#arvExchangeResult").val());
        $("#exchangeProvinceTooltip").attr("data-original-title", $("#exchangeProvinceID option:selected").text());
        $("#exchangeDistrictTooltip").attr("data-original-title", $("#exchangeDistrictID option:selected").text());
        $("#therapyRegistProvinceTooltip").attr("data-original-title", $("#therapyRegistProvinceID option:selected").text());
        $("#therapyRegistDistrictTooltip").attr("data-original-title", $("#therapyRegistDistrictID option:selected").text());

        // Trường hợp import excel không có thông tin tinh thành/ quận huyện/ xã phường
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $("#permanentProvinceID").val('-1').change();
            $("#permanentDistrictID").val('-1').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $("#currentProvinceID").val('-1').change();
            $("#currentDistrictID").val('-1').change();
        }

        $("#siteConfirmTestTooltip").attr("data-original-title", siteConfirmTestTooltip);
        $("#transferSiteTooltip").attr("data-original-title", siteConfirmTestTooltip);
    };
});


app.controller('opc_arv_treatments', function ($scope, msg, $uibModal, arvService) {
    if ($.getQueryParameters().statusOfTreatmentID != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }


    $scope.init = function () {

//        $scope.switchConfig();

        $scope.timeChangeFrom = $.getQueryParameters().timeChangeFrom;
        $scope.timeChangeTo = $.getQueryParameters().timeChangeTo;
        $scope.registrationTime = $.getQueryParameters().registrationTime;
        $scope.treatmentTime = $.getQueryParameters().treatmentTime;
        $scope.endTime = $.getQueryParameters().endTime;
        $scope.tranferFromTime = $.getQueryParameters().tranferFromTime;

//        $scope.$parent.select_search($scope.items.insurance, "");

    };



});
