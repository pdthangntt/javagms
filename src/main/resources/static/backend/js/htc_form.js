app.controller('htc_new', function ($scope, msg, localStorageService) {

    $scope.pOptions = pOptions;
    $scope.defaultProject = utils.getContentOfDefault(form.defaultProject, '');
    $scope.pepfarProjectID = utils.getContentOfDefault(form.pepfarProjectID, '');
    $scope.code = utils.getContentOfDefault(form.code, '');
    $scope.genderID = utils.getContentOfDefault(form.genderID, '');
    $scope.asanteTest = utils.getContentOfDefault(form.asanteTest, '');
    $scope.isAgreePreTest = utils.getContentOfDefault(form.isAgreePreTest, '');
    $scope.preTestTime = utils.getContentOfDefault(form.preTestTime, '');
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.selectedItem = utils.getContentOfDefault(form.isAgreeTest, '');
    $scope.serviceID = utils.getContentOfDefault(form.serviceID, 'CD');
    $scope.advisoryeTime = utils.getContentOfDefault(form.advisoryeTime,
            ('0' + (new Date()).getDate()).slice(-2) + '/' + ('0' + ((new Date()).getMonth() + 1)).slice(-2) + '/' + (new Date()).getFullYear());
    $scope.resultsTime = utils.getContentOfDefault(form.resultsTime, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.confirmResultsID = utils.getContentOfDefault(form.confirmResultsID, '');
    $scope.confirmTestStatus = utils.getContentOfDefault(form.confirmTestStatus, '');
    $scope.arrivalSite = utils.getContentOfDefault(form.arrivalSite, '');
    $scope.confirmResutl = form.confirmResutl;

    $scope.resultsSiteTime = utils.getContentOfDefault(form.resultsSiteTime, '');
    $scope.resultsPatienTime = utils.getContentOfDefault(form.resultsPatienTime, '');
    $scope.exchangeConsultTime = utils.getContentOfDefault(form.exchangeConsultTime, '');
    $scope.exchangeTime = utils.getContentOfDefault(form.exchangeTime, '');
    $scope.registerTherapyTime = utils.getContentOfDefault(form.registerTherapyTime, '');
    $scope.therapyNo = utils.getContentOfDefault(form.therapyNo, '');
    $scope.exchangeProvinceID = utils.getContentOfDefault(form.exchangeProvinceID, '');
    $scope.exchangeDistrictID = utils.getContentOfDefault(form.exchangeDistrictID, '');
    $scope.confirmTestNo = utils.getContentOfDefault(form.confirmTestNo, '');
    $scope.staffBeforeTestID = utils.getContentOfDefault(form.staffBeforeTestID, current_user_name);
    $scope.testResultsID = utils.getContentOfDefault(form.testResultsID, '');
    $scope.partnerProvideResult = utils.getContentOfDefault(form.partnerProvideResult, '');
    $scope.referralSource = utils.getContentOfDefault(form.referralSource, '');
    $scope.staffAfterID = utils.getContentOfDefault(form.staffAfterID, '');
    $scope.isAgreeTest = typeof (form.isAgreeTest) == 'undefined' || form.isAgreeTest == null || (form.isAgreeTest + '') == '' ? '' : form.isAgreeTest + "";
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;
    $scope.arvExchangeResult = utils.getContentOfDefault(form.arvExchangeResult, '');
    $scope.arrivalSiteID = utils.getContentOfDefault(form.arrivalSiteID, '');
    $scope.siteConfirmTest = utils.getContentOfDefault(form.siteConfirmTest, '');
    $scope.pageRedirect = utils.getContentOfDefault(form.pageRedirect, '');
    $scope.therapyRegistProvinceID = utils.getContentOfDefault(form.therapyRegistProvinceID, '');
    $scope.therapyRegistDistrictID = utils.getContentOfDefault(form.therapyRegistDistrictID, '');
    $scope.hasHealthInsurance = utils.getContentOfDefault(form.hasHealthInsurance, '');
    $scope.healthInsuranceNo = utils.getContentOfDefault(form.healthInsuranceNo, '');
    $scope.patientIDAuthen = utils.getContentOfDefault(form.patientIDAuthen, '');
    $scope.earlyHiv = utils.getContentOfDefault(form.earlyHiv, '');
    $scope.virusLoad = utils.getContentOfDefault(form.virusLoad, '');
    $scope.laytestID = utils.getContentOfDefault(form.laytestID, '');
    $scope.approacherNo = utils.getContentOfDefault(form.approacherNo, '');
    $scope.youInjectCode = utils.getContentOfDefault(form.youInjectCode, '');
    $scope.resultsID = utils.getContentOfDefault(form.resultsID, '');
    $scope.bioName = utils.getContentOfDefault(form.bioName, '');
    $scope.earlyHivDate = utils.getContentOfDefault(form.earlyHivDate, '');
    $scope.virusLoadDate = utils.getContentOfDefault(form.virusLoadDate, '');
    $scope.requestConfirmTime = utils.getContentOfDefault(form.requestConfirmTime, '');
    $scope.updateConfirmTime = utils.getContentOfDefault(form.updateConfirmTime, '');
    $scope.resultPcrHiv = utils.getContentOfDefault(form.resultPcrHiv, '');
    $scope.exchangeServiceConfirm = utils.getContentOfDefault(form.exchangeServiceConfirm, '');
    $scope.resultAnti = utils.getContentOfDefault(form.resultAnti, '');
    $scope.exchangeService = utils.getContentOfDefault(form.exchangeService, '');
    $scope.confirmType = utils.getContentOfDefault(form.confirmType, '');
    $scope.testMethod = utils.getContentOfDefault(form.testMethod, '');
    $scope.pcrHiv = utils.getContentOfDefault(form.pcrHiv, '');
    $scope.cdService = utils.getContentOfDefault(form.cdService, '');
    $scope.disableEarlyHiv = utils.getContentOfDefault(form.disableEarlyHiv, false);
    $scope.disableVirusLoad = utils.getContentOfDefault(form.disableVirusLoad, false);
    $scope.staffKC = utils.getContentOfDefault(form.staffKC, false);
    $scope.laoVariable = utils.getContentOfDefault(form.laoVariable, false);
    $scope.laoVariableName = utils.getContentOfDefault(form.laoVariableName, false);
    $scope.virusLoadNumber = utils.getContentOfDefault(form.virusLoadNumber, "");
    $scope.pacSentDate = utils.getContentOfDefault(form.pacSentDate, "");
    $scope.pacPatientID = utils.getContentOfDefault(form.pacPatientID, "");

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

    // Custom validate giới tính và nhóm đối tượng
    $.validator.addMethod("validGender", function (value, element, params) {
        return this.optional(element) || ((((value === "2" || value === "3") && $("select[name=" + params[1] + "]").val() === "san") || $("select[name=" + params[1] + "]").val() !== "san") &&
                (((value === "2" || value === "3") && ($("select[name=" + params[0] + "]").val() === "31" || $("select[name=" + params[0] + "]").val() === "5.1" || $("select[name=" + params[0] + "]").val() === "5.2"))
                        || ((value === "1" || value === "3") && ($("select[name=" + params[0] + "]").val() === "3")) || ($("select[name=" + params[0] + "]").val() !== "3" && $("select[name=" + params[0] + "]").val() !== "31" && $("select[name=" + params[0] + "]").val() !== "5.1" && $("select[name=" + params[0] + "]").val() !== "5.2")));
    }, "");

    // Khai báo object items
    $scope.items = {
        id: "#ID",
        serviceID: "#serviceID",
        patientPhone: "#patientPhone",
        patientID: "#patientID",
        referralSource: "#referralSource",
        approacherNo: "#approacherNo",
        youInjectCode: "#youInjectCode",
        testResultsID: "#testResultsID",
        staffAfterID: "#staffAfterID",
        staffBeforeTestID: "#staffBeforeTestID",
        reactive_result: "2",
        un_specified: "3",
        criminal: "19",
        non_reactive_result: "1",
        siteConfirmTest: "#siteConfirmTest",
        notExchange: "2",
        raceID: "#raceID",
        jobID: "#jobID",
        isAgreeTest: "#isAgreeTest",
        modeOfTransmission: "#modeOfTransmission",
        partnerProvideResult: "#partnerProvideResult",
        confirmResultsID: "#confirmResultsID",
        arvExchangeResult: "#arvExchangeResult",
        exchangeConsultTime: "#exchangeConsultTime",
        genderID: "#genderID",
        objectGroupID: "#objectGroupID",
        asanteTest: "#asanteTest",
        currentProvinceID: "#currentProvinceID",
        currentAddress: "#currentAddress",
        currentAddressGroup: "#currentAddressGroup",
        currentAddressStreet: "#currentAddressStreet",
        currentDistrictID: "#currentDistrictID",
        currentWardID: "#currentWardID",
        permanentProvinceID: "#permanentProvinceID",
        permanentDistrictID: "#permanentDistrictID",
        permanentAddress: "#permanentAddress",
        permanentAddressGroup: "#permanentAddressGroup",
        permanentAddressStreet: "#permanentAddressStreet",
        yearOfBirth: "#yearOfBirth",
        exchangeProvinceID: "#exchangeProvinceID",
        exchangeDistrictID: "#exchangeDistrictID",
        therapyRegistProvinceID: "#therapyRegistProvinceID",
        therapyRegistDistrictID: "#therapyRegistDistrictID",
        permanentWardID: "#permanentWardID",
        riskBehaviorID: "#riskBehaviorID",
        code: "#code",
        testNoFixSite: "#testNoFixSite",
        confirmTestNo: "#confirmTestNo",
        exchangeTime: "#exchangeTime",
        arrivalSite: "#arrivalSite",
        arrivalSiteID: "#arrivalSiteID",
        registeredTherapySite: "#registeredTherapySite",
        registerTherapyTime: "#registerTherapyTime",
        therapyNo: "#therapyNo",
        isDisplayCopy: "#isDisplayCopy",
        isAgreePreTest: "#isAgreePreTest",
        siteConfirmTestDsp: "#siteConfirmTestDsp",
        preTestTime: "#preTestTime",
        advisoryeTime: "#advisoryeTime",
        resultsTime: "#resultsTime",
        resultsPatienTime: "#resultsPatienTime",
        hasHealthInsurance: "#hasHealthInsurance",
        healthInsuranceNo: "#healthInsuranceNo",
        patientIDAuthen: "#patientIDAuthen",
        patientName: "#patientName",
        earlyHiv: "#earlyHiv",
        virusLoad: "#virusLoad",
        resultsSiteTime: "#resultsSiteTime",
        laytestID: "#laytestID",
        confirmTestStatus: "#confirmTestStatus",
        confirmTime: "#confirmTime",
        resultsID: "#resultsID",
        bioName: "#bioName",
        pepfarProjectID: "#pepfarProjectID",
        earlyHivDate: "#earlyHivDate",
        virusLoadDate: "#virusLoadDate",
        therapyExchangeStatus: "#therapyExchangeStatus",
        cdService: "#cdService",
        testMethod: "#testMethod",
        resultAnti: "#resultAnti",
        confirmType: "#confirmType",
        resultPcrHiv: "#resultPcrHiv",
        exchangeService: "#exchangeService",
        requestConfirmTime: "#requestConfirmTime",
        updateConfirmTime: "#updateConfirmTime",
        exchangeServiceName: "#exchangeServiceName",
        pcrHivCheck: "#pcrHivCheck",
        exchangeServiceConfirm: "#exchangeServiceConfirm",
        exchangeServiceNameConfirm: "#exchangeServiceNameConfirm",
        staffKC: "#staffKC",
        laoVariable: "#laoVariable",
        laoVariableName: "#laoVariableName",
        customerType: "#customerType",
        virusLoadNumber: "#virusLoadNumber", // Nồng độ virus nhiễm mới
        earlyBioName: "#earlyBioName", // Tên sinh phẩm nhiễm mới
        earlyDiagnose: "#earlyDiagnose", // Kết luận chẩn đoán nhiễm mới
        pacSentDate: "#pacSentDate",
        pacPatientID: "#pacPatientID"

    };

    $scope.transferSiteChange = function (provinceID, districtID) {

        loading.show();
        $.ajax({
            url: `/service/htc/get-transfer-site.json?pid=${provinceID}&did=${districtID}`,
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
//                            $('#arrivalSiteID').append(new Option(v,k));
                            if (k == $scope.arrivalSiteID && ($($scope.items.exchangeProvinceID).val() !== "" || $($scope.items.exchangeDistrictID).val() !== "")) {
                                $('#arrivalSiteID').append('<option value="' + k + '" selected="selected">' + v + '</option>');
                            } else {
                                $('#arrivalSiteID').append('<option value="' + k + '">' + v + '</option>');
                            }
                        });
                    }
                });
            }
        });
//        $('#arrivalSiteID option:selected').removeAttr('selected');
        $($scope.items.arrivalSiteID).val('').change();
        $($scope.items.arrivalSite).val('').change();
    };


    $.validator.addMethod("riskBehavior", function (value, element) {

        console.log('xxxxx2 ' + value);
//                    return $($scope.items.testResultsID).val() === '2';

        return value.match(/^$|([0-9])\b/); // Regex valid for empty string or number only
    }, "Hành vi nguy cơ không được để trống");

    // Validate for fields on add new customer for testing HIV
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            jobID: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2';
                }
            },
            modeOfTransmission: {required: function () {
                    return $($scope.items.testResultsID).val() === '2';
                }},
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
            advisoryeTime: {
                validDate: true,
                invalidFutureDate: true,
                required: true
            },
            patientName: {
                required: function () {
                    return $($scope.items.testResultsID).val() === $scope.items.reactive_result || $($scope.items.testResultsID).val() === $scope.items.un_specified || $($scope.items.testResultsID).val() === '4';
                },
                maxlength: 100
            },
            serviceID: {
                required: true
            },
            code: {
                required: true,
                maxlength: 100,
                validCode: function () {
                    return $($scope.items.id).val().length > 0;
                }
            },
            patientPhone: {
                validPhone: function () {
                    return $($scope.items.patientPhone).val().length > 0;
                }
            },
            raceID: {
                required: function () {
                    return $($scope.items.testResultsID).val() === $scope.items.reactive_result;
                }
            },
            genderID: {
                required: true,
                validGender: ["objectGroupID", "serviceID"]
            },
            yearOfBirth: {
                required: function () {
                    return $($scope.items.testResultsID).val() === $scope.items.reactive_result || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4';
                },
                digits: true,
                maxlength: 4
            },
            permanentProvinceID: {
                required: function () {
                    return (($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4');
                }
            },
            permanentDistrictID: {
                required: function () {
                    return (($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4');
                }
            },
            permanentWardID: {
                required: function () {
                    return (($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4');
                }
            },
            currentProvinceID: {
                required: function () {
                    return ($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4';
                }
            },
            currentDistrictID: {
                required: function () {
                    return ($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4';
                }
            },
            currentWardID: {
                required: function () {
                    return ($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4';
                }
            },
            objectGroupID: {
                required: true
            },
            approacherNo: {
                required: function () {
                    return $($scope.items.referralSource).val().includes("1");
                },
                maxlength: 50
            },
            youInjectCode: {
                required: function () {
                    return $($scope.items.referralSource).val().includes("2");
                },
                maxlength: 50
            },
            isAgreeTest: {
                required: function () {
                    return $($scope.items.testResultsID).val() === $scope.items.reactive_result || $($scope.items.testResultsID).val() === $scope.items.un_specified || $($scope.items.testResultsID).val() === "4";
                }
            },
            staffBeforeTestID: {
                required: true
            },
            isAgreePreTest: {
                required: false
            },
            patientIDAuthen: {
                required: function () {
                    return (($($scope.items.testResultsID).val() === $scope.items.reactive_result) || ($($scope.items.testResultsID).val() === $scope.items.un_specified) || $($scope.items.testResultsID).val() === '4');
                }
            },
            staffAfterID: {
                required: function () {
                    if ($($scope.items.resultsTime).val() != '' && $($scope.items.resultsTime).val() != null) {
                        return $($scope.items.resultsTime).val().length > 0;
                    }

                    if ($($scope.items.resultsPatienTime).val() != '' && $($scope.items.resultsPatienTime).val() != null) {
                        return $($scope.items.resultsPatienTime).val().length > 0;
                    }
                    return false;
                }
            },
            siteConfirmTest: {
                required: function () {
                    return ($($scope.items.testResultsID).val() === '2' || $($scope.items.testResultsID).val() === '3' || $($scope.items.testResultsID).val() === '4') && $($scope.items.isAgreeTest).val() === 'true';
                }
            },
            exchangeConsultTime: {
                required: function () {
                    return $($scope.items.partnerProvideResult).val() !== '' || $($scope.items.arvExchangeResult).val() !== '';
                }
            },
            cdService: {
                required: function () {
                    return $($scope.items.serviceID).val() === "CD";
                }
            },
            resultAnti: {
                required: function () {
                    return $($scope.items.testMethod).val() === '2' && $($scope.items.testResultsID).val() === '2';
                }
            },
            exchangeServiceName: {
                required: function () {
                    return $($scope.items.exchangeService).val() === '5';
                },
                maxlength: 100
            },
            confirmType: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '4' || ($($scope.items.testResultsID).val() === '2' && $($scope.items.testResultsID).val() !== null && $($scope.items.testResultsID).val() !== '');
                }
            },
            confirmResultsID: {
                required: function () {
                    return $($scope.items.confirmTime).val() !== '' && $($scope.items.confirmTime).val() !== null;
                }
            },
            confirmTime: {
                required: function () {
                    return $($scope.items.confirmResultsID).val() !== '' && $($scope.items.confirmResultsID).val() !== null;
                }
            },
            resultsSiteTime: {
                required: function () {
                    return $($scope.items.confirmResultsID).val() !== '' && $($scope.items.confirmResultsID).val() !== null;
                }
            },
            resultPcrHiv: {
                required: function () {
                    return $($scope.items.pcrHivCheck).iCheck('update')[0].checked;
                }
            },
            exchangeServiceNameConfirm: {
                required: function () {
                    return $($scope.items.exchangeServiceConfirm).val() === '5';
                },
                maxlength: 100
            },
            testNoFixSite: {
                maxlength: 50
            },
            patientID: {
                maxlength: 50
            },
            confirmTestNo: {
                maxlength: 100
            },
            staffKC: {
                maxlength: 50,
                required: function () {
                    return $($scope.items.serviceID).val() === 'KC';
                }
            },
            laoVariableName: {
                maxlength: 100
            },
            earlyHiv: {
                required: function () {
                    return $($scope.items.earlyHivDate).val() !== null && $($scope.items.earlyHivDate).val() !== ""
                }
            },
            earlyHivDate: {
                required: function () {
                    return $($scope.items.earlyHiv).val() !== null && $($scope.items.earlyHiv).val() !== ""
                }
            },
            virusLoad: {
                required: function () {
                    return $($scope.items.virusLoadDate).val() !== null && $($scope.items.virusLoadDate).val() !== ""
                }
            },
            virusLoadDate: {
                required: function () {
                    return $($scope.items.virusLoad).val() !== null && $($scope.items.virusLoad).val() !== ""
                }
            },
            virusLoadNumber: {
                digits: true,
                maxlength: 9
            },
        },
        messages: {
            jobID: {required: "Nghề nghiệp không được để trống"},
            modeOfTransmission: {required: "Đường lây truyền không được để trống"},
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
            advisoryeTime: {
                validDate: "Nhập đúng định dạng theo dd/mm/yyyy",
                invalidFutureDate: "Không được nhập ngày tương lai",
                required: "Ngày tư vấn trước xét nghiệm không được để trống"
            },
            patientName: {required: "Tên khách hàng không được để trống", maxlength: "Tên khách hàng không được quá 100 ký tự"},
            serviceID: {required: "Loại hình dịch vụ không được để trống"},
            code: {required: "Mã khách hàng không được để trống",
                validCode: "Mã khách hàng không đúng định dạng",
                maxlength: "Mã khách hàng không được quá 50 ký tự"},
            patientPhone: {validPhone: "Số điện thoại không hợp lệ"},
            raceID: {required: "Dân tộc không được để trống"},
            genderID: {required: "Giới tính không được để trống",
                validGender: function () {
                    return ((($("select[name=genderID]").val() === "2" || $("select[name=genderID]").val() === "3") && $("select[name=serviceID]").val() === "san") ? "Giới tính không phù hợp với nhóm đối tượng" : $("select[name=serviceID]").val() !== "san" ? "Giới tính không phù hợp với nhóm đối tượng" : "Giới tính không phù hợp với loại dịch vụ");
                }},
            yearOfBirth: {required: "Năm sinh không được để trống",
                digits: "Năm sinh phải là số",
                maxlength: "Năm sinh không được quá 4 ký tự số"},
            permanentProvinceID: {required: "Tỉnh thành không được để trống"},
            permanentDistrictID: {required: "Quận huyện không được để trống"},
            permanentWardID: {required: "Phường xã không được để trống"},
            currentProvinceID: {required: "Tỉnh thành không được để trống"},
            currentDistrictID: {required: "Quận huyện không được để trống"},
            currentWardID: {required: "Phường xã không được để trống"},
            objectGroupID: {required: "Nhóm đối tượng không được để trống"},
            approacherNo: {required: "Mã số tiếp cận cộng đồng không được để trống",
                maxlength: "Mã số tiếp cận cộng đồng không được quá 50 ký tự"},
            youInjectCode: {required: "Mã số bạn tình/bạn chích không được để trống",
                maxlength: "Mã số bạn tình/bạn chích không được quá 50 ký tự"},
            isAgreeTest: {required: "Câu trả lời không được để trống"},
            patientIDAuthen: {required: "Câu trả lời không được để trống"},
            staffBeforeTestID: {required: "Nhân viên không được để trống"},
            isAgreePreTest: {required: "Câu trả lời không được để trống"},
            staffAfterID: {required: "Nhân viên không được để trống"},
            siteConfirmTest: {required: "Nơi chuyển máu đến để XN khẳng định không được để trống"},
            exchangeConsultTime: {required: "Ngày tư vấn chuyển gửi điều trị ARV không được trống"},
            cdService: {required: "Khách hàng nhận dịch vụ không được để trống"},
            resultAnti: {required: "Kết quả xét nghiệm kháng nguyên/kháng thể không được để trống"},
            exchangeServiceName: {required: "Dịch vụ tư vấn chuyển gửi không được để trống",
                maxlength: "Dịch vụ tư vấn chuyển gửi không được quá 100 ký tự"},
            confirmType: {required: "Loại hình XN khẳng định không được để trống"},
            confirmResultsID: {required: "Kết quả xét nghiệm khẳng định không được để trống"},
            confirmTime: {required: "Ngày xét nghiệm khẳng định không được để trống"},
            resultsSiteTime: {required: "Ngày cơ sở nhận kết quả khẳng định không được để trống"},
            resultPcrHiv: {required: "Kết quả xét nghiệm PCR HIV không được để trống"},
            exchangeServiceNameConfirm: {required: "Dịch vụ tư vấn chuyển gửi không được để trống",
                maxlength: "Dịch vụ tư vấn chuyển gửi không được quá 100 ký tự"},
            testNoFixSite: {maxlength: "Mã do cơ sở XN sàng lọc cấp không được quá 50 ký tự"},
            patientID: {maxlength: "Số CMND/Giấy tờ khác không được quá 50 ký tự"},
            confirmTestNo: {maxlength: "Mã xét nghiệm khẳng định không được quá 100 ký tự"},
            staffKC: {required: "Mã nhân viên không chuyên không được để trống", maxlength: "Mã nhân viên không chuyên không được quá 50 ký tự"},
            laoVariableName: {maxlength: "Thể lao không được quá 100 ký tự"},
            earlyHiv: {required: "KQXN nhiễm mới ban đầu không được để trống"},
            earlyHivDate: {required: "Ngày XN nhiễm mới HIV không được để trống"},
            virusLoad: {required: "KQXN tải lượng virus không được để trống"},
            virusLoadDate: {required: "Ngày XN tải lượng virus không được để trống"},
            virusLoadNumber: {digits: "Nồng độ virus phải là số nguyên dương", maxlength: "Nồng độ virus không được quá 9 chữ số"}
        }
    });

    // Hiển thị popup confirm / thông báo khi click submit
    $scope.customSubmit = function (form, $event) {

        if ($($scope.items.testResultsID).val() === '2') {
            if ($($scope.items.riskBehaviorID).val() === undefined || $($scope.items.riskBehaviorID).val() === null || $($scope.items.riskBehaviorID).val().length < 1 || $($scope.items.riskBehaviorID).val()[0] === '') {
                var paragraph = document.getElementById("riskBehaviorIDerror");
                var text = document.createTextNode("Hành vi nguy cơ không được để trống");
                if (paragraph.textContent === null || paragraph.textContent === '' || paragraph.textContent.length < 'Hành vi nguy cơ không được để trống'.length) {
                    paragraph.append(text);
                }
            } else {
                var paragraph = document.getElementById("riskBehaviorIDerror");
                var text = document.createTextNode("");
                paragraph.innerHTML = '';
            }
        } else {
            var paragraph = document.getElementById("riskBehaviorIDerror");
            var text = document.createTextNode("");
            paragraph.innerHTML = '';
        }



        var elm = $event.currentTarget;
        let flagCheck = true;
        $event.preventDefault();
        bootbox.hideAll();

        $(".help-block-error").remove();
        flagCheck = form.validate();

        if (flagCheck) {
            if ($scope.testResultsID == "2" && $scope.isAgreeTest == 'false') {
                bootbox.confirm(
                        {
                            message: 'Bạn chắc chắn khách hàng không xét nghiệm khẳng định?',
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
                                    buttonCatch(elm, form, $event);
                                } else {
                                    $event.preventDefault();
                                }
                            }
                        }
                );
            } else if ($($scope.items.arvExchangeResult).val() == "1" && $($scope.items.exchangeTime).val() != null && $($scope.items.exchangeTime).val() != '' &&
                    $($scope.items.arrivalSiteID).val() != null && $($scope.items.arrivalSiteID).val() != '' && $($scope.items.arrivalSiteID).val() != '-1' &&
                    ($($scope.items.registerTherapyTime).val() == null || $($scope.items.registerTherapyTime).val() == '') &&
                    ($($scope.items.therapyNo).val() == null || $($scope.items.therapyNo).val() == '')) {
                bootbox.confirm(
                        {
                            message: 'Bạn có chắc chắn muốn chuyển khách hàng tới cơ sở điều trị ' + $("#arrivalSiteID option:selected").text() + ' không?',
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
                                    $($scope.items.therapyExchangeStatus).val("1");

                                    elm.form.submit();
                                } else {
//                                $($scope.items.arvExchangeResult).val("2");
                                    $($scope.items.therapyExchangeStatus).val("0");
                                    elm.form.submit();
                                }
                            }
                        }
                );
            } else {
                buttonCatch(elm, form, $event);
            }
        } else {
            $scope.validationOptions;
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };

    // In phiếu đồng ý xét nghiệm
    $scope.customSubmitPrint = function (form, $event) {

        var elm = $event.currentTarget;
        let flagCheck = true;
        $event.preventDefault();
        bootbox.hideAll();
        $(".help-block-error").remove();

        flagCheck = form.validate();

        if (flagCheck) {
            if ($scope.isAgreePreTest === '1') {
                $("#pageRedirect").val("printable");
            }
            elm.form.submit();
        } else {
            $scope.validationOptions;
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    }

    // Bắt dự kiện các nút
    var buttonCatch = function (elm, form, $event) {
        switch (elm.value) {
            case "save-send-sample":
                bootbox.confirm(
                        {
                            message: 'Bạn có muốn gửi mẫu xét nghiệm của khách hàng tới cơ sở khẳng định không?',
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
                                if (confirmed && validatePopUp(form)) {
                                    elm.form.submit();
                                } else {
                                    $("#pageRedirect").val("save-send-sample-no");
                                    elm.form.submit();
                                }
                            }
                        }
                );
                break;
            case "save-print":
                if (validatePopUp(form)) {
                    elm.form.submit();
                } else {
                    $event.preventDefault();
                }
                break;
            case "return-list":
                if (validatePopUp(form)) {
                    elm.form.submit();
                } else {
                    $event.preventDefault();
                }
                break;
            case "update_customer":
                if (validatePopUp(form)) {
                    elm.form.submit();
                } else {
                    $event.preventDefault();
                }
                break;
            default:
                ;

        }
    };

    // Validate dữ liệu trước khi nhập
    var validatePopUp = function (form) {
        if (!form.validate()) {
            form.keys();
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            return false;
        }
        return true;
    };

    // Toggle hiển ẩn dấu sao
    var toggleStarKey = function (item, flag) {
        if (flag) {
            $(item).parent().find(".text-danger").text("*");
        } else {
            $(item).parent().find(".text-danger").text("");
        }
    };

    $scope.init = function () {
        $scope.switchConfig();
        if ($scope.laytestID === '') {
            $scope.$parent.select_search($scope.items.serviceID, "Chọn dịch vụ");
            $scope.$parent.select_mutiple($scope.items.referralSource, "Chọn nguồn giới thiệu");
        }

        if (($scope.requestConfirmTime === null || $scope.requestConfirmTime !== '') && ($scope.updateConfirmTime !== null && $scope.updateConfirmTime !== '')) {
            $scope.$parent.select_search($scope.items.siteConfirmTest, "Chọn cơ sở");
        }

        $scope.$parent.select_search($scope.items.raceID, "Chọn dân tộc");
        $scope.$parent.select_search($scope.items.jobID, "Chọn công việc");
        $scope.$parent.select_search($scope.items.modeOfTransmission, "Chọn đường lây nhiễm");
        $scope.$parent.select_search($scope.items.partnerProvideResult, "Thông tin tư vấn xét nghiệm ban tình/bạn chích");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn nhóm đối tượng");
        $scope.$parent.select_search($scope.items.asanteTest, "Chọn kết quả");

        $scope.$parent.select_search($scope.items.currentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.currentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.currentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.permanentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.exchangeProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.exchangeDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.therapyRegistProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.therapyRegistDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.hasHealthInsurance, "Chọn câu trả lời");
        $scope.$parent.select_search($scope.items.bioName, "Chọn tên sinh phẩm sàng lọc");
        $scope.$parent.select_search($scope.items.earlyBioName, "Chọn sinh phẩm xét nghiệm");

        // Chỉnh sửa validate theo phiếu SHIFT
        $($scope.items.testMethod).attr("disabled", "disabled");
        $($scope.items.bioName).attr("disabled", "disabled");
        $($scope.items.resultAnti).attr("disabled", "disabled");
        $($scope.items.exchangeService).attr("disabled", "disabled");
        $($scope.items.exchangeServiceConfirm).attr("disabled", "disabled");
        $($scope.items.exchangeServiceName).attr("disabled", "disabled");
        $($scope.items.exchangeServiceNameConfirm).attr("disabled", "disabled");
        $($scope.items.confirmType).attr("disabled", "disabled");
        $($scope.items.isAgreeTest).attr("disabled", "disabled");
        $($scope.items.siteConfirmTest).attr("disabled", "disabled");
        $($scope.items.resultsTime).attr("disabled", "disabled");
        $($scope.items.pcrHivCheck).attr("disabled", "disabled");
        $($scope.items.resultPcrHiv).attr("disabled", "disabled");
        $($scope.items.earlyHiv).attr("disabled", "disabled");
        $($scope.items.earlyHivDate).attr("disabled", "disabled");
        $($scope.items.virusLoad).attr("disabled", "disabled");
        $($scope.items.virusLoadDate).attr("disabled", "disabled");

        // Chỉnh sửa cập nhật thông tin xét nghiệm nhiễm mới
        $($scope.items.virusLoadNumber).attr("disabled", "disabled");
        $($scope.items.earlyDiagnose).attr("disabled", "disabled");
        $($scope.items.earlyBioName).attr("disabled", "disabled");

        if (($scope.testResultsID === $scope.items.reactive_result ||
                $scope.testResultsID === $scope.items.un_specified || $scope.testResultsID === "4")
                && $scope.isAgreeTest === "true") {
            $($scope.items.siteConfirmTest).removeAttr("disabled");
        } else {
            $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
            $($scope.items.confirmTestNo).val("").change();
            $($scope.items.confirmTime).val("").change();
            $($scope.items.confirmResultsID).val("").change();
            $($scope.items.earlyHiv).val("");
            $($scope.items.virusLoad).val("");
            $($scope.items.resultsSiteTime).val("").change();
            $($scope.items.resultsPatienTime).val("").change();
        }

        if ($($scope.items.testMethod).val() === '1') {
            $("#testResultsID option[value='3']").hide();
            $("#testResultsID option[value='4']").hide();
        } else {
            $("#testResultsID option[value='4']").show();
            $("#testResultsID option[value='3']").hide();
        }
        ;

        if ($scope.resultsTime === '' || !($scope.resultsTime.includes("d") || $scope.resultsTime.includes("m") || $scope.resultsTime.includes("y"))) {
            $($scope.items.exchangeService).val('').change();
        }

        if ($scope.exchangeServiceConfirm !== "5") {
            $($scope.items.exchangeServiceNameConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceNameConfirm).val('').change();
        } else {
            $($scope.items.exchangeServiceNameConfirm).removeAttr("disabled");
        }

        if ($($scope.items.resultsTime).val() !== '' && $($scope.items.resultsTime).val() !== null && $($scope.items.testResultsID).val() !== '2') {
            $("#exchangeService option[value='1']").hide();
        } else {
            $("#exchangeService option[value='1']").show();
        }

        if ($($scope.items.testMethod).val() === '' || $($scope.items.testMethod).val() === null) {
            $($scope.items.testResultsID).attr("disabled", "disabled");
            $($scope.items.testResultsID).val('').change();
        } else {
            $($scope.items.testResultsID).removeAttr("disabled");
        }

        $scope.$parent.select_mutiple($scope.items.riskBehaviorID, "Chọn hành vi nguy cơ lây nhiễm");

        //Load cơ sở xét nghiệm khẳng định
        $scope.initSiteConfirmTest();

        // Tiếp tục đồng ý xét nghiệm khẳng định
        $($scope.items.testResultsID).on("change", function () {
            if ($($scope.items.testResultsID).val() !== "1" && $($scope.items.testResultsID).val() !== "" && $($scope.items.testResultsID).val() !== null) {
                $($scope.items.isAgreeTest).removeAttr("disabled");
                $($scope.items.isAgreeTest).val("true").change();
            }
        });

        // Nguồn giới thiệu và các trường liên quan
        if (($scope.referralSource !== 'undefined' && $scope.referralSource !== null && $scope.referralSource.includes("1") === false && $scope.referralSource.includes("2") === false)
                || ($scope.referralSource === 'undefined' || $scope.referralSource === null)) {
            $($scope.items.pepfarProjectID).attr("disabled", "disabled");
            $($scope.items.pepfarProjectID).val('').change();
        } else {
            $($scope.items.pepfarProjectID).removeAttr("disabled");
        }

        if ($scope.referralSource !== 'undefined' && $scope.referralSource !== null && $scope.referralSource.includes("1")) {
            $($scope.items.approacherNo).removeAttr("disabled");
        } else {
            $($scope.items.approacherNo).attr("disabled", "disabled");
            $($scope.items.approacherNo).val("");
        }

        if ($scope.referralSource !== 'undefined' && $scope.referralSource !== null && $scope.referralSource.includes("2")) {
            $($scope.items.youInjectCode).removeAttr("disabled");
        } else {
            $($scope.items.youInjectCode).attr("disabled", "disabled");
            $($scope.items.youInjectCode).val("");
        }

        // Clear đữ liệu khi chọn nguồn giới thiệu dịch vụ
        if ($("#referralSource").val() !== null) {
            $("#referralSource option[value='']").removeAttr("selected");
        }

        $($scope.items.referralSource).on("change", function () {

            if ($("#referralSource").val() !== null) {
                $("#referralSource option[value='']").removeAttr("selected");
            }

            if (($($scope.items.referralSource).val() !== 'undefined' && $($scope.items.referralSource).val() !== null && $($scope.items.referralSource).val().includes("1") === false && $($scope.items.referralSource).val().includes("2") === false)
                    || ($($scope.items.referralSource).val() === 'undefined' || $($scope.items.referralSource).val() === null)) {
                $($scope.items.pepfarProjectID).attr("disabled", "disabled");
                $($scope.items.pepfarProjectID).val('').change();
            } else {
                $($scope.items.pepfarProjectID).removeAttr("disabled");
                if (($scope.id === null || $scope.id === '') || ($scope.id !== null && $scope.id !== '' && ($scope.pepfarProjectID === '' || $scope.pepfarProjectID === null))) {
                    $($scope.items.pepfarProjectID).val($scope.defaultProject).change();
                }
            }

            if ($($scope.items.referralSource).val() !== 'undefined' && $($scope.items.referralSource).val() !== null && $($scope.items.referralSource).val().includes("1")) {
                $($scope.items.approacherNo).removeAttr("disabled");
            } else {
                $($scope.items.approacherNo).attr("disabled", "disabled");
                $($scope.items.approacherNo).val("");
            }

            if ($($scope.items.referralSource).val() !== 'undefined' && $($scope.items.referralSource).val() !== null && $($scope.items.referralSource).val().includes("2")) {
                $($scope.items.youInjectCode).removeAttr("disabled");
            } else {
                $($scope.items.youInjectCode).attr("disabled", "disabled");
                $($scope.items.youInjectCode).val("");
            }
        });

        // Mặc định có xét nghiệm sàng lọc
        $($scope.items.isAgreePreTest).ready(function () {
            if (($scope.isAgreePreTest === 'undefined' || $scope.isAgreePreTest === '') && $scope.id === '') {
                $($scope.items.siteConfirmTest).removeAttr("disabled");
            }
        });

        $($scope.items.isAgreePreTest).on("change", function () {
            $scope.isAgreePreTestChange();
        });

        $($scope.items.testResultsID).on("change", function () {
            $scope.testResultsIDChange();
        });

        $($scope.items.exchangeProvinceID).on("change", function () {
            $scope.transferSiteChange($($scope.items.exchangeProvinceID).val());
        });

        $($scope.items.exchangeDistrictID).on("change", function () {
            $scope.transferSiteChange($($scope.items.exchangeProvinceID).val(), $($scope.items.exchangeDistrictID).val());
        });

        if ($($scope.items.arrivalSiteID).val() != '-1') {
            $($scope.items.arrivalSite).attr("disabled", 'disabled');
        }

        // Click vào form kiểm hiển thị staffAfterID
        $('form[name="test_form"]').on("click", function () {
            $scope.resultsTimeChange();
        });

        // Sự kiện thay đổi thời gian
        $($scope.items.resultsTime).keyup(function () {
            $scope.resultsTimeChange();
        });

        $($scope.items.resultsPatienTime).keyup(function () {
            $scope.resultsTimeChange();
        });

        // Clear data theo B3. Kết quả xét nghiệm sàng lọc
        $($scope.items.testResultsID).on("change", function () {
            if ($($scope.items.testResultsID).val() === "") {
                $($scope.items.isAgreeTest).val("").change();
                $($scope.items.siteConfirmTest).val("").change();
                $($scope.items.testNoFixSite).val("").change();
            }

            if ($($scope.items.testResultsID).val() === "1") {
                $($scope.items.siteConfirmTest).val(null).change();
                $($scope.items.testNoFixSite).val(null).change();
            }

            if ($($scope.items.testResultsID).val() !== "1") {
                $($scope.items.resultsTime).val("").change();
            }
        });

        // Clear data theo B3.1. Khách hàng đồng ý XN KĐ
        $($scope.items.isAgreeTest).on("change", function () {
            if ($($scope.items.isAgreeTest).val() === "" || $($scope.items.isAgreeTest).val() === "false") {
                $($scope.items.siteConfirmTest).val("").change();
                $($scope.items.testNoFixSite).val("").change();
            } else {
                if ($($scope.items.testMethod).val() === "1" && $($scope.items.testResultsID).val() === "2") {
                    $($scope.items.confirmType).removeAttr("disabled");
                    $($scope.items.confirmType).val("2").change();
                    $scope.confirmType = "2";
                    $("#confirmType option[value='1']").hide();
                    $("#confirmType option[value='']").hide();
                } else if ($($scope.items.testMethod).val() === "2" && $($scope.items.testResultsID).val() !== "4") {
                    $("#confirmType option[value='1']").show();
                    $("#confirmType option[value='']").show();
                    $($scope.items.confirmType).val("").change();
                    $($scope.items.confirmType).attr("disabled", "disabled");
                }
            }
        });

        //Dữ liệu địa danh
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);
        $scope.initProvince($scope.items.exchangeProvinceID, $scope.items.exchangeDistrictID, null);
        $scope.initProvince($scope.items.therapyRegistProvinceID, $scope.items.therapyRegistDistrictID, null);

        // Trường hợp import excel không có thông tin tinh thành/ quận huyện/ xã phường
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $($scope.items.permanentProvinceID).val('').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $($scope.items.currentProvinceID).val('').change();
        }

//        $scope.addressAutocomplete($scope.items.permanentAddress, $scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
//        $scope.addressAutocomplete($scope.items.currentAddress, $scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        //event copy
        $("#permanentProvinceID, #permanentAddress, #permanentAddressGroup, #permanentAddressStreet, #permanentDistrictID, #permanentWardID").change(function () {
            if ($scope.isCopyPermanentAddress) {
                $($scope.items.currentAddress).val($($scope.items.permanentAddress).val()).change();
                $($scope.items.currentAddressGroup).val($($scope.items.permanentAddressGroup).val()).change();
                $($scope.items.currentAddressStreet).val($($scope.items.permanentAddressStreet).val()).change();
                $($scope.items.currentProvinceID).val($($scope.items.permanentProvinceID).val()).change();
                $($scope.items.currentDistrictID).val($($scope.items.permanentDistrictID).val()).change();
                $($scope.items.currentWardID).val($($scope.items.permanentWardID).val()).change();
            }
        });

        //Get button submit value
        $("button").click(function () {
            var fired_button = $(this).val();
            if (fired_button === "continue") {
                $("#pageRedirect").val("htc-new");
            }

            if (fired_button === "save-print") {
                $("#pageRedirect").val("save-print");
                $scope.pageRedirect = "save-print";
            }
            if (fired_button === "save-send-sample") {
                $("#pageRedirect").val("save-send-sample");
                $scope.pageRedirect = "save-send-sample";
            }

            if (fired_button === "return-list") {
                $("#pageRedirect").val("return-list");
            }
        });

        // Confirm submit lưu và gửi mẫu xét nghiệm
        $scope.sendSample = function ($event) {
            var elm = $event.currentTarget;
            bootbox.confirm(
                    {
                        message: 'Bạn có muốn gửi mẫu xét nghiệm của khách hàng tới cơ sở khẳng định không?',
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
                                elm.form.submit();
                                return false;
                            }
                        }
                    }
            );
        };

        $scope.codeSupport();
        setInterval(function () {
            $scope.codeSupport();
        }, 30000);

        if (getURLParameter('printable') === "save-print") {
            url = urlTransferOPC + "?oid=" + $scope.id;
            $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
        }

//        if (getURLParameter('printable') === "save-print") {
//            $scope.print(urlTicketResult + "?oid=" + $scope.id);
//            $("#pdf-loading").remove();
//        }

        // In phiếu đồng ý xét nghiệm
        if ($.getQueryParameters().oid != null && $.getQueryParameters().printable === 'printable') {
            setTimeout(function () {
                $scope.dialogReport(urlAgreeTest + "?oid=" + $.getQueryParameters().oid, null, "Phiếu xác nhận đồng ý xét nghiệm HIV");
                $("#pdf-loading").remove();
            }, 300);
        }

        // Ràng buộc thời gian với các trường B3 B4
        if ($($scope.items.preTestTime).val() === '') {
            $($scope.items.bioName).attr("disabled", 'disabled');
            $($scope.items.bioName).val('').change();
        }

        //Init save
        if (($scope.id == null || $scope.id == '') && ($scope.laytestID === null || $scope.laytestID === "") && localStorageService.isSupported) {
            let key_service = "htc_cookie_serviceID";
            if ((v = localStorageService.get(key_service)) != null && v != '') {
                $scope.serviceID = v;

                // Click chọn khoa sản -> fill giới tính nữ
                if (v === 'san' && $scope.id === '' && $scope.genderID === '') {
                    $('#genderID option:selected').removeAttr('selected').change();
                    $('#genderID').val('2').change();
                }

                if (v === 'CD' && $scope.id === '') {
                    $('#serviceID').val('CD').change();
                    $('#cdService').removeAttr('disabled');
                }
            } else {
                $('#serviceID').val('CD').change();
            }


            $("#serviceID").change(function () {
                localStorageService.set(key_service, $(this).val());
            });

            let key_group = "htc_cookie_objectGroupID";
            if ((v = localStorageService.get(key_group)) != null && v != '' && (t = localStorageService.get("htc_cookie_serviceID")) != null && t != 'san') {
                $("#objectGroupID").find("option").removeAttr("selected");
                $("#objectGroupID").val(v).change();
                $("#objectGroupID").find(`option[value='${v}']`).attr("selected", "selected");

                if ((v === '31' || v === '5.1' || v === '5.2') && $scope.id === '') {
                    $("#genderID").val("2").change();
                    $('#genderID option[value=2]').attr('selected', 'selected');
                }
                if ((v === '3') && $scope.id === '') {
                    $("#genderID").val("1").change();
                    $('#genderID option[value=1]').attr('selected', 'selected');
                }
            }
            $("#objectGroupID").change(function () {
                localStorageService.set(key_group, $(this).val());
            });

            let key_job = "htc_cookie_jobID";
            if ((v = localStorageService.get(key_job)) != null && v != '') {
                $("#jobID").find("option").removeAttr("selected");
                $("#jobID").val(v).change();
                $("#jobID").find("option[value='" + v + "']").attr("selected", "selected");
            }
            $("#jobID").change(function () {
                localStorageService.set(key_job, $(this).val());
            });

            let key_ref = "htc_cookie_referralSource";
            if ((v = localStorageService.get(key_ref)) != null && v != '') {
                $scope.referralSource = v;
            }
            $("#referralSource").change(function () {
                localStorageService.set(key_ref, $(this).val());
            });

            let key_is_new = "htc_cookie_isNew";
            if ((v = localStorageService.get(key_is_new)) != null && v != '' && v == true) {
                $("#isNew1").click();
            }
            $("#isNew1").change(function () {
                localStorageService.set(key_is_new, $(this).is(":checked"));
            });
        }

        if ($("#objectGroupID").val() === "6") {
            $("#lao_block").show();
        } else {
            $("#lao_block").hide();
            $("#laoVariable").val("").change();
        }

        $("#objectGroupID").change(function () {
            if ($("#objectGroupID").val() === "5.1" || $("#objectGroupID").val() === "5.2" || $("#objectGroupID").val() === "31") {
                $("#genderID").val("2").change();
            }

            if ($("#objectGroupID").val() === "3") {
                $("#genderID").val("1").change();
            }

            if ($("#objectGroupID").val() === "19") {
                $($scope.items.jobID).val("i9").change();
            }

            if ($("#objectGroupID").val() === "6") {
                $("#lao_block").show();
            } else {
                $("#lao_block").hide();
                $("#laoVariable").val("").change();
            }
        });

        if ($scope.laoVariable === "-1") {
            $($scope.items.laoVariableName).removeAttr("disabled");
        } else {
            $($scope.items.laoVariableName).attr("disabled", "disabled");
            $($scope.items.laoVariableName).val("").change();
        }

        $($scope.items.laoVariable).change(function () {
            if ($($scope.items.laoVariable).val() === "-1") {
                $($scope.items.laoVariableName).removeAttr("disabled");
            } else {
                $($scope.items.laoVariableName).attr("disabled", "disabled");
                $($scope.items.laoVariableName).val("").change();
            }
        });

        if ($scope.pcrHiv === "1") {
            $('#pcrHivCheck').removeAttr("disabled");
            $('#pcrHivCheck').removeAttr("readonly");
            $($scope.items.pcrHivCheck).attr("readonly", false);
            $('#pcrHivCheck').iCheck('check');
        }

        if ($($scope.items.testResultsID).val() === "1" && $($scope.items.resultsTime).val() !== "" && $($scope.items.resultsTime).val() !== null) {
            $($scope.items.exchangeService).removeAttr("disabled");
        }

        if ($($scope.items.preTestTime).val() === '' || $($scope.items.preTestTime).val().indexOf('m') > -1 || $($scope.items.preTestTime).val().indexOf('d') > -1
                || $($scope.items.preTestTime).val().indexOf('y') > -1) {
            $($scope.items.bioName).attr("disabled", 'disabled');
            $($scope.items.testMethod).attr("disabled", 'disabled');
            $($scope.items.bioName).val('').change();
            $($scope.items.testMethod).val('').change();
            $($scope.items.testResultsID).val('').change();
        } else {
            $($scope.items.bioName).removeAttr("disabled").change();
            $($scope.items.testMethod).removeAttr("disabled");
        }

        // TH Loại hình XN khẳng định: huyết thanh + Dương tính
        if ($($scope.items.confirmResultsID).val() === "2" && $($scope.items.confirmType).val() === "2") {
            $($scope.items.earlyHivDate).removeAttr("disabled");
            $($scope.items.earlyHiv).removeAttr("disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if (typeof $($scope.items.pcrHivCheck).iCheck('update')[0] !== 'undefined' && $($scope.items.pcrHivCheck).iCheck('update')[0].checked) {
            $($scope.items.resultPcrHiv).removeAttr("disabled");
        } else {
            $($scope.items.resultPcrHiv).attr("disabled", "disabled");
            $($scope.items.resultPcrHiv).val('').change();
        }

        // TH Loại hình XN khẳng định: huyết thanh + Âm tính/Không xác định
        if ($($scope.items.confirmResultsID).val() !== "2" && $($scope.items.confirmResultsID).val() !== ""
                && $($scope.items.confirmResultsID).val() !== null && $($scope.items.confirmType).val() === "2") {
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        // TH Loại hình XN khẳng định: PCR HIV + Âm tính/Không xác định
        if ($($scope.items.confirmResultsID).val() !== "3" && $($scope.items.confirmResultsID).val() !== ""
                && $($scope.items.confirmResultsID).val() !== null && $($scope.items.confirmType).val() === "1") {
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.id === '' && ($($scope.items.resultsPatienTime).val() !== '' && $($scope.items.resultsPatienTime).val() !== null && !($($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("m") || $($scope.items.resultsPatienTime).val().includes("y")))) {
            $($scope.items.exchangeServiceConfirm).removeAttr("disabled");
        } else if ($scope.id === '') {
            $($scope.items.exchangeServiceConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceConfirm).val('').change();
        }

        if ($scope.id !== '' && ($scope.resultsPatienTime !== '' && $scope.resultsPatienTime !== null && !($scope.resultsPatienTime.includes("d") || $scope.resultsPatienTime.includes("m") || $scope.resultsPatienTime.includes("y")))) {
            $($scope.items.exchangeServiceConfirm).removeAttr("disabled");
        } else if ($scope.id !== '') {
            $($scope.items.exchangeServiceConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceConfirm).val('').change();
        }

        if ($scope.id === '' && ($($scope.items.resultsPatienTime).val() !== '' && $($scope.items.resultsPatienTime).val() !== null)) {
            if ($($scope.items.confirmResultsID).val() !== '2') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            } else {
                $("#exchangeServiceConfirm option[value='1']").show();
            }
        }

        if ($scope.id !== '' && ($scope.resultsPatienTime !== '' && $scope.resultsPatienTime !== null)) {
            if ($scope.confirmResultsID !== '2') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            } else {
                $("#exchangeServiceConfirm option[value='1']").show();
            }
        }

        if ($($scope.items.testResultsID).val() === "1") {
            $($scope.items.resultsTime).removeAttr("disabled");
        } else {
            $($scope.items.resultsTime).attr("disabled", "disabled");
            $($scope.items.resultsTime).val("disabled");
        }

        if ($scope.testResultsID !== "1" && $scope.testResultsID !== "") {
            $($scope.items.isAgreeTest).removeAttr("disabled");
        } else {
            $($scope.items.isAgreeTest).attr("disabled", "disabled");
            $($scope.items.isAgreeTest).val('').change();
        }

        if ($scope.testResultsID !== "1" && $scope.testResultsID !== "" && $scope.isAgreeTest === "true") {
            $($scope.items.siteConfirmTest).removeAttr("disabled");
        } else {
            $($scope.items.siteConfirmTest).attr("disabled", "disabled");
            $($scope.items.siteConfirmTest).val("disabled");
        }

        if (($($scope.items.testResultsID).val() === "2" && $($scope.items.testMethod).val() === "1") || $($scope.items.testResultsID).val() === "4" || ($($scope.items.testResultsID).val() === "2" && $($scope.items.resultAnti).val() !== "" && $($scope.items.resultAnti).val() !== null)) {
            $($scope.items.confirmType).removeAttr("disabled");
        } else {
            $($scope.items.confirmType).attr("disabled", "disabled");
            $($scope.items.confirmType).val('').change();
        }

        if ($scope.exchangeService !== "5") {
            $($scope.items.exchangeServiceName).attr("disabled", "disabled");
            $($scope.items.exchangeServiceName).val('').change();
        } else {
            $($scope.items.exchangeServiceName).removeAttr("disabled");
        }

        if ($($scope.items.serviceID).val() !== "CD" && ($scope.laytestID === "" || $scope.laytestID !== null)) {
            $($scope.items.cdService).attr("disabled", "disabled");
        } else {
            $($scope.items.cdService).removeAttr("disabled");
        }

        if ($($scope.items.testMethod).val() === '2' && $($scope.items.testResultsID).val() === '2') {
            $($scope.items.resultAnti).removeAttr("disabled");
        } else {
            $($scope.items.resultAnti).attr("disabled", "disabled");
            $($scope.items.resultAnti).val('').change();
        }

        $($scope.items.serviceID).on("change", function () {
            if ($($scope.items.serviceID).val() !== "CD") {
                $($scope.items.cdService).attr("disabled", "disabled");
                $($scope.items.cdService).val('').change();
            } else {
                $($scope.items.cdService).removeAttr("disabled");
            }

            if ($($scope.items.serviceID).val() !== "KC") {
                $($scope.items.staffKC).val('');
            }
        });

        $($scope.items.exchangeService).on("change", function () {
            if ($($scope.items.exchangeService).val() !== "5") {
                $($scope.items.exchangeServiceName).attr("disabled", "disabled");
                $($scope.items.exchangeServiceName).val('').change();
            } else {
                $($scope.items.exchangeServiceName).removeAttr("disabled");
            }
        });

        $($scope.items.exchangeServiceConfirm).on("change", function () {
            if ($($scope.items.exchangeServiceConfirm).val() !== "5") {
                $($scope.items.exchangeServiceNameConfirm).attr("disabled", "disabled");
                $($scope.items.exchangeServiceNameConfirm).val('').change();
            } else {
                $($scope.items.exchangeServiceNameConfirm).removeAttr("disabled");
            }

            if ($($scope.items.exchangeServiceConfirm).val() !== "1" && ($($scope.items.arvExchangeResult).val() !== "" || $($scope.items.exchangeConsultTime).val() !== "")) {
                $($scope.items.arvExchangeResult).val('').change();
                $($scope.items.exchangeConsultTime).val('').change();
                $($scope.items.partnerProvideResult).val('').change();
                $scope.arvExchangeResult = "";
            }
        });

        $($scope.items.resultAnti).on("change", function (e) {

            if (e.originalEvent) {
                $($scope.items.confirmResultsID).val('').change();
            }

            $scope.disableNhiemMoi();

            if (($($scope.items.testResultsID).val() === "2" && $($scope.items.testMethod).val() === "1") || $($scope.items.testResultsID).val() === "4" || ($($scope.items.testResultsID).val() === "2" && $($scope.items.resultAnti).val() !== "" && $($scope.items.resultAnti).val() !== null)) {
                $($scope.items.confirmType).removeAttr("disabled");

                if ($($scope.items.resultAnti).val() === "1" || $($scope.items.testResultsID).val() === "4") {
                    $("#confirmType option[value='1']").show();
                    $($scope.items.confirmType).val("1").change();
                    $("#confirmType option[value='2']").hide();
                    $("#confirmType option[value='']").hide();
                } else {
                    $("#confirmType option[value='2']").show();
                    $($scope.items.confirmType).val("2").change();
                    $("#confirmType option[value='1']").hide();
                    $("#confirmType option[value='']").hide();
                }
            } else {
                $($scope.items.confirmType).attr("disabled", "disabled");
                $($scope.items.confirmType).val('').change();
            }
        });

        $($scope.items.testMethod).on("change", function (e) {

            if (e.originalEvent) {
                $($scope.items.confirmResultsID).val('').change();
            }

            $scope.disableNhiemMoi();

            if ($($scope.items.testMethod).val() === '2' && $($scope.items.testResultsID).val() === '2') {
                $($scope.items.resultAnti).removeAttr("disabled");
            } else {
                $($scope.items.resultAnti).attr("disabled", "disabled");
                $($scope.items.resultAnti).val('').change();
            }

            if ($($scope.items.testMethod).val() === '' || $($scope.items.testMethod).val() === null) {
                $($scope.items.testResultsID).attr("disabled", "disabled");
                $($scope.items.testResultsID).val('').change();
            } else {
                $($scope.items.testResultsID).removeAttr("disabled");
            }

            if ($($scope.items.testMethod).val() === '1') {
                $("#testResultsID option[value='3']").hide();
                $("#testResultsID option[value='4']").hide();
            } else {
                $("#testResultsID option[value='4']").show();
                $("#testResultsID option[value='3']").hide();
            }
            ;
        });

        $($scope.items.arrivalSiteID).on("change", function () {
            let value = $($scope.items.arrivalSiteID).val();
            let label = $("#arrivalSiteID option:selected").text();
//            let label = $($scope.items.arrivalSiteID + ` option[value=${value}]`).text();
            if (value === '-1') {
                $($scope.items.arrivalSite).removeAttr("disabled");
                $($scope.items.arrivalSite).val("");
            } else if (value != null && value != '') {
                $($scope.items.arrivalSite).attr("disabled", 'disabled');
                $($scope.items.arrivalSite).val(label);
            } else {
                $($scope.items.arrivalSite).attr("disabled", 'disabled');
                $($scope.items.arrivalSite).val('');
            }

        });

        // Form ready 
        $('form[name="test_form"]').ready(function () {
            $($scope.items.arrivalSite).val($scope.arrivalSite).change();
            $scope.isAgreePreTestChange();
            $scope.testResultsIDChange();
            $scope.resultsTimeChange();
            if ($scope.confirmTestStatus === '2' && $scope.confirmTime !== '') {
                if ($scope.confirmResutl) {
                    $scope.updateAfterReceiveResult();
                }
                $("#copyButton").attr("disabled", 'disabled');
            }

            if ($($scope.items.confirmType).val() === '2' && ($($scope.items.confirmResultsID).val() === '1' || $($scope.items.confirmResultsID).val() === '3')) {
                $($scope.items.pcrHivCheck).removeAttr("disabled");
            }

            if ($($scope.items.confirmResultsID).val() === "2") {
                $($scope.items.earlyHivDate).removeAttr("disabled");
                $($scope.items.earlyBioName).removeAttr("disabled");
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.virusLoadNumber).removeAttr("disabled");
                $($scope.items.earlyDiagnose).removeAttr("disabled");
            }

            if ($scope.id !== null && $scope.id !== "" && $scope.pacPatientID !== null && $scope.pacPatientID !== "" && $scope.pacSentDate !== null && $scope.pacSentDate !== "") {
                $scope.updateAfterGsph();
            }
        });

        // Hiển thị C7
        if ($scope.resultsPatienTime !== '' && $scope.resultsPatienTime !== null &&
                !($scope.resultsPatienTime.includes("d") || $scope.resultsPatienTime.includes("m")
                        || $scope.resultsPatienTime.includes("y"))) {
            $($scope.items.exchangeServiceConfirm).removeAttr("disabled");

            if ($scope.id === '' && ($($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2")) {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id === '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

            if ($scope.id !== '' && ($scope.confirmResultsID === "2" || $scope.resultPcrHiv === "2")) {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id !== '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

        } else {
            $($scope.items.exchangeServiceConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceConfirm).val('').change();
        }

//        $($scope.items.earlyHiv).on("change", function () {
//            if ($($scope.items.earlyHiv).val() === null || $($scope.items.earlyHiv).val() === '') {
//                $($scope.items.earlyHivDate).val('');
//            }
//            if ($($scope.items.earlyHiv).val() === "3") {
//                $($scope.items.earlyHivDate).val("");
//                $($scope.items.earlyHivDate).attr("disabled", "disabled");
//            } else if(!$($scope.items.earlyHiv).attr("disabled")){
//                $($scope.items.earlyHivDate).removeAttr("disabled", "disabled");
//            }
//            if ($($scope.items.earlyHiv).val() !== "3" && $($scope.items.earlyHiv).val() !== "") {
//                $($scope.items.earlyHivDate).val($($scope.items.confirmTime).val());
//                $scope.earlyHivDate = $($scope.items.confirmTime).val();
//            }
//        });

//        $($scope.items.virusLoad).on("change", function () {
//            if ($($scope.items.virusLoad).val() === null || $($scope.items.virusLoad).val() === '') {
//                $($scope.items.virusLoadDate).val('');
//            }
//            if ($($scope.items.virusLoad).val() === "5") {
//                $($scope.items.virusLoadDate).val('');
//                $($scope.items.virusLoadDate).attr("disabled", "disabled");
//            } else if($($scope.items.virusLoad).val() !== "" && $($scope.items.virusLoad).val() !== null){
//                $($scope.items.virusLoadDate).removeAttr("disabled");
//            }
//        });

        // Click chọn khoa sản -> fill giới tính nữ
        $($scope.items.serviceID).on("change", function () {
            if ($($scope.items.serviceID).val() === "san") {
                $('#genderID option:selected').removeAttr('selected').change();
                $('#genderID').val('2').change();
            }
        });

        // Ràng buộc với trường ngày xét nghiệm sàng lọc
        $($scope.items.preTestTime).on("blur", function () {
            if ($($scope.items.preTestTime).val() === '' || $($scope.items.preTestTime).val().indexOf('m') > -1 || $($scope.items.preTestTime).val().indexOf('d') > -1
                    || $($scope.items.preTestTime).val().indexOf('y') > -1) {
                $($scope.items.bioName).attr("disabled", 'disabled');
                $($scope.items.testMethod).attr("disabled", 'disabled');
                $($scope.items.bioName).val('').change();
                $($scope.items.testMethod).val('').change();
//                $($scope.items.testResultsID).val('').change();
            } else {
                $($scope.items.bioName).removeAttr("disabled");
                $($scope.items.testMethod).removeAttr("disabled");
                if ($($scope.items.testMethod).val() === '') {
                    $($scope.items.testMethod).val('1').change();
                }
            }
        });

        // A14. Phạm nhân không bắt buộc nhập A8
        $($scope.items.objectGroupID).change(function () {
            if ($($scope.items.objectGroupID).val() === '19') {
                toggleStarKey($scope.items.permanentProvinceID, false);
                toggleStarKey($scope.items.permanentDistrictID, false);
                toggleStarKey($scope.items.permanentWardID, false);
            } else {
                toggleStarKey($scope.items.permanentProvinceID, true);
                toggleStarKey($scope.items.permanentDistrictID, true);
                toggleStarKey($scope.items.permanentWardID, true);
            }
        });

        if (($($scope.items.testResultsID).val() === "2" && $($scope.items.testMethod).val() === "1") || $($scope.items.testResultsID).val() === "4" || ($($scope.items.testResultsID).val() === "2" && $($scope.items.resultAnti).val() !== "" && $($scope.items.resultAnti).val() !== null)) {
            $($scope.items.confirmType).removeAttr("disabled");

            if ($($scope.items.resultAnti).val() === "1" || $($scope.items.testResultsID).val() === "4") {
                $($scope.items.confirmType).val("1").change();
                $("#confirmType option[value='2']").hide();
                $("#confirmType option[value='']").hide();
            } else {
                $($scope.items.confirmType).val("2").change();
                $scope.confirmType = "2";
                $("#confirmType option[value='1']").hide();
                $("#confirmType option[value='']").hide();
            }

        } else if ($($scope.items.testResultsID).val() !== "2" && $($scope.items.testMethod).val() !== "1") {
            $($scope.items.confirmType).attr("disabled", "disabled");
            $($scope.items.confirmType).val('').change();
        }

        if ($($scope.items.confirmType).val() === '1') {
            $("#confirmResultsID option[value='3']").hide();
        } else if ($($scope.items.confirmType).val() === '2') {
            $("#confirmResultsID option[value='3']").show();
        }

        $($scope.items.confirmType).on("change", function () {

            if ($($scope.items.confirmType).val() === '1') {
                $("#confirmResultsID option[value='3']").hide();
                if ($($scope.items.confirmResultsID).val() === "3") {
                    $($scope.items.confirmResultsID).val("").change();
                }
            } else if ($($scope.items.confirmType).val() !== '') {
                $("#confirmResultsID option[value='3']").show();
            }

            // C4. Loại hình XN khẳng định PCR HIV + confirmResultsID != '' -> mở C4.3 ->  C4.6
            if (($($scope.items.confirmResultsID).val() === "1" || $($scope.items.confirmResultsID).val() === "2") && $($scope.items.confirmType).val() === "1") {
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.virusLoadNumber).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === "1" && $($scope.items.confirmResultsID).val() === "") {
                $($scope.items.virusLoadNumber).attr("disabled", "disabled");
                $($scope.items.virusLoadNumber).val("").change();
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.virusLoad).val("").change();
                $($scope.items.virusLoadDate).val("").change();
            }

            // Xét nghiệm huyết thanh + KQ Xn KĐ Dương tính
            if ($($scope.items.confirmResultsID).val() === "2" && $($scope.items.confirmType).val() === "2") {
                $($scope.items.earlyHivDate).removeAttr("disabled");
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
                $($scope.items.virusLoadNumber).removeAttr("disabled");
            }
            // Xét nghiệm huyết thanh + KQ XN KĐ Không xác định/ Âm tính
            else if ($($scope.items.confirmType).val() === "2" && $($scope.items.confirmType).val() !== "") {
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
            } else if ($($scope.items.confirmType).val() === "2" && $($scope.items.confirmType).val() === "") {
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoad).val('').change();
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.resultsSiteTime).attr("disabled", "disabled");
                $($scope.items.resultsSiteTime).val('').change();
                $($scope.items.resultsPatienTime).attr("disabled", "disabled");
                $($scope.items.resultsPatienTime).val('').change();
                $($scope.items.earlyHiv).attr("disabled", "disabled");
                $($scope.items.earlyHivDate).attr("disabled", "disabled");
                $($scope.items.earlyHiv).val('').change();
            }

            // c4b
            if ($($scope.items.confirmType).val() === '2' && ($($scope.items.confirmResultsID).val() === '1' || $($scope.items.confirmResultsID).val() === '3')) {
                $($scope.items.pcrHivCheck).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === '' || $($scope.items.confirmType).val() === null) {
                $($scope.items.pcrHivCheck).iCheck("uncheck");
                $($scope.items.pcrHivCheck).attr("disabled", "disabled");
                $($scope.items.pcrHivCheck).val("0").change();
            }

        });

        $($scope.items.pcrHivCheck).on('ifToggled', function (event) {
            if ($($scope.items.pcrHivCheck).iCheck('update')[0].checked) {
                $($scope.items.resultPcrHiv).removeAttr("disabled");
                $("#pcrHiv").val("1");
            } else {
                $($scope.items.resultPcrHiv).attr("disabled", "disabled");
                $($scope.items.resultPcrHiv).val('').change();
                $("#pcrHiv").val("0");
            }
        });

        // Clear data theo C3 Kết quả xét nghiệm khẳng định:
        $($scope.items.confirmResultsID).on("change", function () {

            if ($($scope.items.confirmResultsID).val() === null || $($scope.items.confirmResultsID).val() === "") {
                $scope.disableNhiemMoi();
            }

            if ($($scope.items.confirmResultsID).val() !== '2') {
                $($scope.items.exchangeServiceConfirm).val('').change();
            }

            $($scope.items.pcrHivCheck).iCheck("uncheck");
            $($scope.items.pcrHivCheck).val("0").change();

            // C4. Loại hình XN khẳng định null ( luồng bình thường)
            if ($($scope.items.confirmResultsID).val() === "2" && $($scope.items.confirmType).val() === "") {
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.earlyHivDate).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
                $($scope.items.virusLoadNumber).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === "") {
                $($scope.items.earlyHiv).attr("disabled", "disabled");
                $($scope.items.earlyHiv).val('').change();
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoad).val('').change();
                $($scope.items.earlyHivDate).attr("disabled", "disabled");
                $($scope.items.earlyHivDate).val('').change();
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).val('').change();
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
            }

            // trường hợp là  C4. Loại hình XN khẳng định PCR HIV
            if (($($scope.items.confirmResultsID).val() !== "" && $($scope.items.confirmResultsID).val() !== null) && $($scope.items.confirmType).val() === "1") {
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === "1") {
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoad).val('').change();
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).val('').change();
                $($scope.items.resultsSiteTime).attr("disabled", "disabled");
                $($scope.items.resultsSiteTime).val('').change();
                $($scope.items.resultsPatienTime).attr("disabled", "disabled");
                $($scope.items.resultsPatienTime).val('').change();
                $($scope.items.earlyBioName).val('').change();
                $($scope.items.earlyBioName).attr("disabled", "disabled");
            }

            // C4. Loại hình XN khẳng định PCR HIV + confirmResultsID != '' -> mở C4.3 ->  C4.6
            if (($($scope.items.confirmResultsID).val() === "1") && $($scope.items.confirmType).val() === "1") {
                $($scope.items.virusLoadNumber).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");

                $($scope.items.earlyBioName).attr("disabled", "disabled");
                $($scope.items.earlyBioName).val("").change();
                $($scope.items.earlyHivDate).attr("disabled", "disabled");
                $($scope.items.earlyHivDate).val("").change();
                $($scope.items.earlyHiv).attr("disabled", "disabled");
                $($scope.items.earlyHiv).val("").change();
                $($scope.items.earlyDiagnose).val("").change();
                $($scope.items.earlyDiagnose).attr("disabled", "disabled");

            } else if (($($scope.items.confirmResultsID).val() === "2") && $($scope.items.confirmType).val() === "1") {
                $($scope.items.virusLoadNumber).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.earlyBioName).removeAttr("disabled");
                $($scope.items.earlyHivDate).removeAttr("disabled");
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.earlyDiagnose).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === "1" && $($scope.items.confirmResultsID).val() === "") {
                $($scope.items.virusLoadNumber).attr("disabled", "disabled");
                $($scope.items.virusLoadNumber).val("").change();
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.virusLoad).val("").change();
                $($scope.items.virusLoadDate).val("").change();
            }

            // Xét nghiệm huyết thanh + KQ XN KĐ Dương tính
            if ($($scope.items.confirmResultsID).val() === "2" && $($scope.items.confirmType).val() === "2") {
                $($scope.items.earlyHivDate).removeAttr("disabled");
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadNumber).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
                $($scope.items.earlyDiagnose).removeAttr("disabled");
                $($scope.items.earlyBioName).removeAttr("disabled");
            }
            // Xét nghiệm huyết thanh + KQ XN KĐ Không xác định/ Âm tính
            else if ($($scope.items.confirmType).val() === "2" && $($scope.items.confirmResultsID).val() !== "2" && $($scope.items.confirmResultsID).val() !== "") {
                $($scope.items.resultsSiteTime).removeAttr("disabled");
                $($scope.items.resultsPatienTime).removeAttr("disabled");
                $($scope.items.virusLoadNumber).attr("disabled", "disabled");
                $($scope.items.virusLoadNumber).val("");
                $($scope.items.earlyDiagnose).attr("disabled", "disabled");
                $($scope.items.earlyDiagnose).val("");
                $($scope.items.earlyBioName).attr("disabled", "disabled");
                $($scope.items.earlyBioName).val('').change();
                $($scope.items.virusLoad).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).attr("disabled", "disabled");
                $($scope.items.virusLoadDate).val("").change();
                $($scope.items.earlyHiv).attr("disabled", "disabled");
                $($scope.items.earlyHivDate).attr("disabled", "disabled");
                $($scope.items.earlyHivDate).val("");
            } else if ($($scope.items.confirmType).val() === "2" && $($scope.items.confirmResultsID).val() !== "") {
                $($scope.items.virusLoad).removeAttr("disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled");
                $($scope.items.earlyHiv).removeAttr("disabled");
                $($scope.items.earlyHivDate).removeAttr("disabled");
            } else if ($($scope.items.confirmType).val() === "2" && $($scope.items.confirmResultsID).val() === "") {
                $($scope.items.virusLoadNumber).attr("disabled", "disabled");
                $($scope.items.virusLoadNumber).val("");
                $($scope.items.earlyDiagnose).attr("disabled", "disabled");
                $($scope.items.earlyDiagnose).val("");
                $($scope.items.earlyBioName).attr("disabled", "disabled");
                $($scope.items.earlyBioName).val('').change();
            }

            // c4b
            if ($($scope.items.confirmType).val() === '2' && ($($scope.items.confirmResultsID).val() === '1' || $($scope.items.confirmResultsID).val() === '3')) {
                $($scope.items.pcrHivCheck).removeAttr("disabled");
            } else if ($($scope.items.confirmResultsID).val() === '' || $($scope.items.confirmResultsID).val() === null || $($scope.items.confirmResultsID).val() === '2') {
                $($scope.items.pcrHivCheck).iCheck("uncheck");
                $($scope.items.pcrHivCheck).attr("disabled", "disabled");
                $($scope.items.pcrHivCheck).val("0").change();
            }

            if ($scope.id === '' && (($($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2") && $($scope.items.resultsPatienTime).val() !== "" && $($scope.items.resultsPatienTime).val() !== null &&
                    !($($scope.items.resultsPatienTime).val().includes("m") || $($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("y")))) {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id === '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

            if ($scope.id !== '' && (($($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2") && $($scope.items.resultsPatienTime).val() !== "" && $($scope.items.resultsPatienTime).val() !== null &&
                    !($($scope.items.resultsPatienTime).val().includes("m") || $($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("y")))) {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id !== '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

            if (!$($scope.items.pcrHivCheck).iCheck('update')[0].checked && $($scope.items.pcrHivCheck).val() !== "") {
                $($scope.items.earlyHiv).val('').change();
                $($scope.items.virusLoad).val('').change();
            }

            if ($($scope.items.pcrHivCheck).iCheck('update')[0].checked && $($scope.items.pcrHivCheck).val() !== "" && $($scope.items.pcrHivCheck).val() !== null && $($scope.items.resultPcrHiv).val() !== "") {
                $($scope.items.virusLoad).removeAttr("disabled", "disabled");
                $($scope.items.virusLoadDate).removeAttr("disabled", "disabled");
            }

        });

        if ($($scope.items.resultPcrHiv).val() !== "" && $($scope.items.resultPcrHiv).val() !== null) {
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
        } else if ($($scope.items.confirmResultsID).val() !== "2") {
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoad).val('').change();
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).val('').change();
        }

        if ($scope.exchangeServiceConfirm !== "5") {
            $($scope.items.exchangeServiceNameConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceNameConfirm).val('').change();
        } else {
            $($scope.items.exchangeServiceNameConfirm).removeAttr("disabled");
        }

        // End init
        // Xử lý enable/ disable C4.1 4.2, 4.3, 4.4
//        if( $scope.confirmType !== "1" &&  $scope.resultPcrHiv === "" && $scope.confirmResultsID === "2"){
//            $($scope.items.earlyHiv).removeAttr("disabled");
//            $($scope.items.earlyHivDate).removeAttr("disabled");
//        }

        if ($scope.resultsTime === "") {
            $($scope.items.exchangeService).val('').change();
            $scope.exchangeService = "";
        }

        // Luồng binh thường
        if ($scope.confirmType === "" && ($scope.confirmResultsID === "" || $scope.confirmResultsID === "1" || $scope.confirmResultsID === "3")) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.confirmType === "" && $scope.confirmResultsID === "2") {
            $($scope.items.earlyHiv).removeAttr("disabled");
            $($scope.items.earlyHivDate).removeAttr("disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        // C4 PCR
        if ($scope.confirmType === "1" && ($scope.confirmResultsID === "" || $scope.confirmResultsID === null)) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.confirmType === "1" && ($scope.confirmResultsID === "1")) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.confirmType === "1" && ($scope.confirmResultsID === "2")) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
            $($scope.items.earlyBioName).removeAttr("disabled");
            $($scope.items.earlyHivDate).removeAttr("disabled");
            $($scope.items.earlyHiv).removeAttr("disabled");
            $($scope.items.earlyDiagnose).removeAttr("disabled");
        }

        // C4 Huyết thanh
        if ($scope.confirmType === "2" && ($scope.confirmResultsID === "" || $scope.confirmResultsID === null || $scope.confirmResultsID === "1" || $scope.confirmResultsID === "3")) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.confirmType === "2" && $scope.confirmResultsID === "2") {
            $($scope.items.earlyHiv).removeAttr("disabled");
            $($scope.items.earlyHivDate).removeAttr("disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.earlyDiagnose).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.confirmType === "2" && ($scope.confirmResultsID === "1" || $scope.confirmResultsID === "3") && ($scope.resultPcrHiv === "1" || $scope.resultPcrHiv === "2")) {
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.resultsSiteTime).removeAttr("disabled");
            $($scope.items.resultsPatienTime).removeAttr("disabled");
        }

        if ($scope.id !== '' && ($scope.resultsPatienTime !== "" && $scope.resultsPatienTime !== null &&
                !($scope.resultsPatienTime.includes("m") || $scope.resultsPatienTime.includes("d")
                        || $scope.resultsPatienTime.includes("y")))) {
            $($scope.items.exchangeServiceConfirm).removeAttr("disabled");
        }

        if ($scope.earlyHiv === "3") {
            $($scope.items.earlyHivDate).val("");
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
        }

        if ($scope.virusLoad === "5") {
            $($scope.items.virusLoadDate).val("");
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
        }

        if ($scope.id === "") {
            $("#testResultsID option[value='3']").hide();
        } else if ($scope.testResultsID === "3") {
            $("#testResultsID option[value='3']").show();
        } else {
            $("#testResultsID option[value='3']").hide();
        }

        // c4b
        if ($($scope.items.confirmType).val() === '2' && ($scope.confirmResultsID === '1' || $scope.confirmResultsID === '3')) {
            $($scope.items.pcrHivCheck).removeAttr("disabled");
            $($scope.items.pcrHivCheck).attr("readonly", false);
        }

        if ($($scope.items.pcrHivCheck).iCheck('update')[0].checked && $scope.resultPcrHiv !== "") {
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
        }

        if ($($scope.items.pcrHivCheck).iCheck('update')[0].checked) {
            $($scope.items.resultPcrHiv).removeAttr("disabled");
        }

        if ($($scope.items.virusLoad).val() === "5") {
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
        }

        if ($scope.confirmResultsID === "2" && $scope.confirmType === "2") {
            $($scope.items.earlyBioName).removeAttr("disabled");
        }

        if ($($scope.items.confirmType).val() === '2' && ($($scope.items.confirmResultsID).val() === '1' || $($scope.items.confirmResultsID).val() === '3')) {
            $($scope.items.pcrHivCheck).removeAttr("disabled");
        }
    }

    // disable các trường nhiễm mới
    $scope.disableNhiemMoi = function () {
        if ($($scope.items.confirmResultsID).val() === '') {
            $($scope.items.resultPcrHiv).val('').change();
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoad).val('').change();
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).val("");
            $($scope.items.resultsSiteTime).attr("disabled", "disabled");
            $($scope.items.resultsSiteTime).val('').change();
            $($scope.items.resultsPatienTime).attr("disabled", "disabled");
            $($scope.items.resultsPatienTime).val('').change();
            $($scope.items.earlyHiv).attr("disabled", "disabled");
            $($scope.items.earlyHiv).val('').change();
            $($scope.items.earlyHivDate).attr("disabled", "disabled");
            $($scope.items.earlyDiagnose).val("");
            $($scope.items.earlyDiagnose).attr("disabled", "disabled");
            $($scope.items.earlyBioName).val("");
            $($scope.items.earlyBioName).attr("disabled", "disabled");
        }
    }

    // Disable các trường màn hình cập nhật KH sau trả kết quả
    $scope.updateAfterReceiveResult = function () {
        $($scope.items.advisoryeTime).attr("disabled", 'disabled');
        $($scope.items.serviceID).attr("disabled", 'disabled');
        $($scope.items.code).attr("disabled", 'disabled');
        $($scope.items.patientName).attr("disabled", 'disabled');
        $($scope.items.yearOfBirth).attr("disabled", 'disabled');
        $($scope.items.genderID).attr("disabled", 'disabled');
        $($scope.items.patientIDAuthen).attr("disabled", 'disabled');
        $($scope.items.patientID).attr("disabled", 'disabled');
        $($scope.items.permanentAddress).attr("disabled", 'disabled');
        $($scope.items.permanentAddressGroup).attr("disabled", 'disabled');
        $($scope.items.permanentAddressStreet).attr("disabled", 'disabled');
        $($scope.items.permanentProvinceID).attr("disabled", 'disabled');
        $($scope.items.permanentDistrictID).attr("disabled", 'disabled');
        $($scope.items.permanentWardID).attr("disabled", 'disabled');
        $($scope.items.currentAddress).attr("disabled", 'disabled');
        $($scope.items.currentAddressGroup).attr("disabled", 'disabled');
        $($scope.items.currentAddressStreet).attr("disabled", 'disabled');
        $($scope.items.currentProvinceID).attr("disabled", 'disabled');
        $($scope.items.currentDistrictID).attr("disabled", 'disabled');
        $($scope.items.currentWardID).attr("disabled", 'disabled');
        $($scope.items.raceID).attr("disabled", 'disabled');
        $($scope.items.hasHealthInsurance).attr("disabled", 'disabled');
        $($scope.items.healthInsuranceNo).attr("disabled", 'disabled');
        $($scope.items.jobID).attr("disabled", 'disabled');
        $($scope.items.objectGroupID).attr("disabled", 'disabled');
        $($scope.items.riskBehaviorID).attr("disabled", 'disabled');
        $($scope.items.modeOfTransmission).attr("disabled", 'disabled');
        $($scope.items.referralSource).attr("disabled", 'disabled');
        $($scope.items.approacherNo).attr("disabled", 'disabled');
        $($scope.items.youInjectCode).attr("disabled", 'disabled');
        $($scope.items.patientPhone).attr("disabled", 'disabled');
        $($scope.items.isAgreePreTest).attr("disabled", 'disabled');
        $($scope.items.preTestTime).attr("disabled", 'disabled');
        $($scope.items.testResultsID).attr("disabled", 'disabled');
        $($scope.items.isAgreeTest).attr("disabled", 'disabled');
        $($scope.items.testNoFixSite).attr("disabled", 'disabled');
        $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
        $($scope.items.resultsTime).attr("disabled", 'disabled');
        $($scope.items.bioName).attr("disabled", 'disabled');
        $($scope.items.confirmTestNo).attr("disabled", 'disabled');
        $($scope.items.cdService).attr("disabled", 'disabled');

        if ($($scope.items.resultPcrHiv).val() !== "" && $($scope.items.resultPcrHiv).val() !== null) {
            $($scope.items.pcrHivCheck).attr("disabled", 'disabled');
        }

        $($scope.items.confirmType).attr("disabled", 'disabled');
        $($scope.items.testMethod).attr("disabled", 'disabled');
        $($scope.items.resultAnti).attr("disabled", 'disabled');
        $($scope.items.pepfarProjectID).attr("disabled", 'disabled');
        $($scope.items.staffKC).attr("disabled", 'disabled');
        $($scope.items.customerType).attr("disabled", 'disabled');
        $($scope.items.laoVariable).attr("disabled", 'disabled');
        $($scope.items.laoVariableName).attr("disabled", 'disabled');
    }

    // Cập nhật sau khi chuyển sang GSPH
    $scope.updateAfterGsph = function () {
        $($scope.items.advisoryeTime).attr("disabled", 'disabled');
        $($scope.items.serviceID).attr("disabled", 'disabled');
        $($scope.items.code).attr("disabled", 'disabled');
        $($scope.items.patientName).attr("disabled", 'disabled');
        $($scope.items.yearOfBirth).attr("disabled", 'disabled');
        $($scope.items.genderID).attr("disabled", 'disabled');
        $($scope.items.patientIDAuthen).attr("disabled", 'disabled');
        $($scope.items.patientID).attr("disabled", 'disabled');
        $($scope.items.permanentAddress).attr("disabled", 'disabled');
        $($scope.items.permanentAddressGroup).attr("disabled", 'disabled');
        $($scope.items.permanentAddressStreet).attr("disabled", 'disabled');
        $($scope.items.permanentProvinceID).attr("disabled", 'disabled');
        $($scope.items.permanentDistrictID).attr("disabled", 'disabled');
        $($scope.items.permanentWardID).attr("disabled", 'disabled');
        $($scope.items.currentAddress).attr("disabled", 'disabled');
        $($scope.items.currentAddressGroup).attr("disabled", 'disabled');
        $($scope.items.currentAddressStreet).attr("disabled", 'disabled');
        $($scope.items.currentProvinceID).attr("disabled", 'disabled');
        $($scope.items.currentDistrictID).attr("disabled", 'disabled');
        $($scope.items.currentWardID).attr("disabled", 'disabled');
        $($scope.items.raceID).attr("disabled", 'disabled');
        $($scope.items.hasHealthInsurance).attr("disabled", 'disabled');
        $($scope.items.healthInsuranceNo).attr("disabled", 'disabled');
        $($scope.items.jobID).attr("disabled", 'disabled');
        $($scope.items.objectGroupID).attr("disabled", 'disabled');
        $($scope.items.riskBehaviorID).attr("disabled", 'disabled');
        $($scope.items.modeOfTransmission).attr("disabled", 'disabled');
        $($scope.items.referralSource).attr("disabled", 'disabled');
        $($scope.items.approacherNo).attr("disabled", 'disabled');
        $($scope.items.youInjectCode).attr("disabled", 'disabled');
        $($scope.items.patientPhone).attr("disabled", 'disabled');
        $($scope.items.isAgreePreTest).attr("disabled", 'disabled');
        $($scope.items.preTestTime).attr("disabled", 'disabled');
        $($scope.items.testResultsID).attr("disabled", 'disabled');
        $($scope.items.isAgreeTest).attr("disabled", 'disabled');
        $($scope.items.testNoFixSite).attr("disabled", 'disabled');
        $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
        $($scope.items.resultsTime).attr("disabled", 'disabled');
        $($scope.items.bioName).attr("disabled", 'disabled');
        $($scope.items.confirmTestNo).attr("disabled", 'disabled');
        $($scope.items.cdService).attr("disabled", 'disabled');

        if ($($scope.items.resultPcrHiv).val() !== "" && $($scope.items.resultPcrHiv).val() !== null) {
            $($scope.items.pcrHivCheck).attr("disabled", 'disabled');
        }

        $($scope.items.confirmType).attr("disabled", 'disabled');
        $($scope.items.testMethod).attr("disabled", 'disabled');
        $($scope.items.resultAnti).attr("disabled", 'disabled');
        $($scope.items.pepfarProjectID).attr("disabled", 'disabled');
        $($scope.items.staffKC).attr("disabled", 'disabled');
        $($scope.items.customerType).attr("disabled", 'disabled');
        $($scope.items.laoVariable).attr("disabled", 'disabled');
        $($scope.items.laoVariableName).attr("disabled", 'disabled');

        //C. Xét nghiệm khẳng định
        $($scope.items.siteConfirmTestDsp).attr("disabled", 'disabled');
        $($scope.items.confirmTestNo).attr("disabled", 'disabled');
        $($scope.items.confirmTime).attr("disabled", 'disabled');
        $($scope.items.confirmType).attr("disabled", 'disabled');
        $($scope.items.confirmResultsID).attr("disabled", 'disabled');
        $($scope.items.pcrHivCheck).attr("disabled", 'disabled');
        $($scope.items.earlyBioName).attr("disabled", 'disabled');
        $($scope.items.earlyHivDate).attr("disabled", 'disabled');
        $($scope.items.earlyHiv).attr("disabled", 'disabled');
        $($scope.items.virusLoadDate).attr("disabled", 'disabled');
        $($scope.items.virusLoadNumber).attr("disabled", 'disabled');
        $($scope.items.virusLoad).attr("disabled", 'disabled');
        $($scope.items.earlyDiagnose).attr("disabled", 'disabled');
        $($scope.items.resultsSiteTime).attr("disabled", 'disabled');
        $($scope.items.resultsPatienTime).removeAttr("disabled");
    }

    // ==============================================================================
    // = Clear data theo các nút change
    // = Hiển thị dấu sao theo điều kiện
    // ==============================================================================

    // B1. Đồng ý XN sàng lọc *
    $scope.isAgreePreTestChange = function () {
        if ($($scope.items.isAgreePreTest).val() === "" || $($scope.items.isAgreePreTest).val() === "0") {
            $($scope.items.preTestTime).val("");
            $scope.preTestTime = "";
            $($scope.items.preTestTime).attr("disabled", 'disabled').blur();
//            $($scope.items.isAgreeTest).val("");
//            $($scope.items.testNoFixSite).val("");
//            $($scope.items.resultsTime).val("");
            $($scope.items.bioName).attr("disabled", 'disabled');
            $($scope.items.bioName).val("");
//            $($scope.items.testResultsID).val("")
//            $($scope.items.testResultsID).attr("disabled", 'disabled');
            $($scope.items.testMethod).attr("disabled", 'disabled');
            $($scope.items.testMethod).val('');

            // Set hiển thị phần hiển thị dấu sao
            toggleStarKey($scope.items.preTestTime, false);
            toggleStarKey($scope.items.testResultsID, false);
            toggleStarKey($scope.items.staffAfterID, false);
        } else if ($($scope.items.isAgreePreTest).val() === "1") {
            if ($scope.laytestID === '') {
                $($scope.items.preTestTime).removeAttr("disabled");
                $($scope.items.preTestTime).val($($scope.items.advisoryeTime).val());
                $($scope.items.preTestTime).blur();
                $scope.preTestTime = ($($scope.items.advisoryeTime).val());
            }
        }

        if ($($scope.items.isAgreePreTest).val() === "1") {
            toggleStarKey($scope.items.preTestTime, true);
            toggleStarKey($scope.items.testResultsID, true);
            toggleStarKey($scope.items.staffAfterID, true);
        }

//        if ($($scope.items.isAgreePreTest).val() !== "1") {
//            $($scope.items.preTestTime).attr("disabled", 'disabled').blur();
//            $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
//        } else if ($($scope.items.isAgreePreTest).val() === "1") {
//            if ($scope.laytestID === '') {
//                $($scope.items.preTestTime).removeAttr("disabled").blur();
//            }
//        }
    };

    // B3. Kết quả xét nghiệm sàng lọc
    $scope.testResultsIDChange = function () {

        $scope.disableNhiemMoi();

        if ($($scope.items.testResultsID).val() === "" || $($scope.items.testResultsID).val() !== "2") {
            toggleStarKey($scope.items.patientName, false);
            toggleStarKey($scope.items.patientIDAuthen, false);
            toggleStarKey($scope.items.yearOfBirth, false);
            toggleStarKey($scope.items.raceID, false);
            toggleStarKey($scope.items.permanentProvinceID, false);
            toggleStarKey($scope.items.permanentDistrictID, false);
            toggleStarKey($scope.items.permanentWardID, false);
            toggleStarKey($scope.items.currentProvinceID, false);
            toggleStarKey($scope.items.currentDistrictID, false);
            toggleStarKey($scope.items.currentWardID, false);
        }

        if ($($scope.items.testResultsID).val() === "2") {
            toggleStarKey($scope.items.patientName, true);
            toggleStarKey($scope.items.patientIDAuthen, true);
            toggleStarKey($scope.items.yearOfBirth, true);
            toggleStarKey($scope.items.raceID, true);
            toggleStarKey($scope.items.permanentProvinceID, true);
            toggleStarKey($scope.items.permanentDistrictID, true);
            toggleStarKey($scope.items.permanentWardID, true);
            toggleStarKey($scope.items.currentProvinceID, true);
            toggleStarKey($scope.items.currentDistrictID, true);
            toggleStarKey($scope.items.currentWardID, true);
        }
    };

    // Có ngày nhận kết quả mới nhập nhân viên sau xét nghiệm
    $scope.resultsTimeChange = function () {
        if (($($scope.items.resultsTime).val() === ""
                || $($scope.items.resultsTime).val() === "dd/mm/yyyy")
                && ($($scope.items.resultsPatienTime).val() === ""
                        || $($scope.items.resultsPatienTime).val() === "dd/mm/yyyy")) {
            toggleStarKey($scope.items.staffAfterID, false);

            // Disable tư vấn viên sau xét nghiệm
            $($scope.items.staffAfterID).attr("disabled", 'disabled');
            return;
        }

        if (($($scope.items.resultsTime).val() !== ""
                && $($scope.items.resultsTime).val() !== "dd/mm/yyyy")
                || ($($scope.items.resultsPatienTime).val() !== ""
                        && $($scope.items.resultsPatienTime).val() !== "dd/mm/yyyy")) {

            toggleStarKey($scope.items.staffAfterID, true);
            $($scope.items.staffAfterID).removeAttr("disabled");
            return;
        }
    };

    $($scope.items.resultsTime).on("blur", function () {
        if ($($scope.items.testResultsID).val() === "" && $($scope.items.resultsTime).val() !== "" && $($scope.items.resultsTime).val().includes("d") || !($($scope.items.resultsTime).val().includes("m") || $($scope.items.resultsTime).val().includes("y"))) {
            $($scope.items.exchangeService).removeAttr("disabled");
            $("#exchangeService option[value='1']").hide();
        } else {
            $("#exchangeService option[value='1']").show();
            $($scope.items.exchangeService).attr("disabled", "disabled");
            $($scope.items.exchangeService).val("").change();
        }

        if ($($scope.items.resultsTime).val() === '' || !($($scope.items.resultsTime).val().includes("d") || $($scope.items.resultsTime).val().includes("m") || $($scope.items.resultsTime).val().includes("y"))) {
            $($scope.items.exchangeService).val('').change();
        }

    });

    // Tạo prefix cho code
    $($scope.items.code).keyup(function () {

        var code = $scope.code;
        var codes = code.split("-");
        var prefix = code.substring(0, code.indexOf(codes[codes.length - 1]));

        if (this.value.indexOf(prefix) !== 0) {
//            this.value = prefix;
        }
    });

    // Set UPPERCASE sau khi nhập xong mã khách hàng
    $("#code, #testNoFixSite, #confirmTestNo").blur(function () {
        $($scope.items.code).val($($scope.items.code).val().toUpperCase());
        $($scope.items.testNoFixSite).val($($scope.items.testNoFixSite).val().toUpperCase());
        $($scope.items.confirmTestNo).val($($scope.items.confirmTestNo).val().toUpperCase());
    }
    );

    $("#hasHealthInsurance").on('change', () => {
        if ($("#hasHealthInsurance").val() !== '1') {
            $("#healthInsuranceNo").val('').change();
        }
    });

    $("#healthInsuranceNo").blur(function () {
        $($scope.items.healthInsuranceNo).val($($scope.items.healthInsuranceNo).val().toUpperCase());
    });

    // Ràng buộc hiển thị đống ý XN sàng lọc và các trường liên quan
    if ($($scope.items.isAgreePreTest).val() !== "1") {
        if ($scope.laytestID === '') {
            $($scope.items.preTestTime).attr("disabled", 'disabled');
        }
        $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
        $($scope.items.resultsTime).attr("disabled", 'disabled');
//        $($scope.items.testNoFixSite).attr("disabled", 'disabled');
    } else if ($($scope.items.isAgreePreTest).val() === "1") {
        if ($scope.laytestID === '') {
            $($scope.items.preTestTime).removeAttr("disabled");
        }
        $($scope.items.resultsTime).removeAttr("disabled");
        $($scope.items.siteConfirmTest).removeAttr("disabled");
//        $($scope.items.testNoFixSite).removeAttr("disabled");
    }

    $("#resultsPatienTime").change(function () {
        if ($($scope.items.resultsPatienTime).val() !== '' && $($scope.items.resultsPatienTime).val() !== null &&
                !($($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("m")
                        || $($scope.items.resultsPatienTime).val().includes("y"))) {

            if ($($scope.items.testResultsID).val() === "1" && $($scope.items.resultsTime).val() !== "" && $($scope.items.resultsTime).val() !== null
                    && !($($scope.items.resultsTime).val().includes("d") || $($scope.items.resultsTime).val().includes("m") || $($scope.items.resultsTime).val().includes("y"))) {
                $($scope.items.exchangeService).removeAttr("disabled");
                $("#exchangeService option[value='1']").hide();
            } else {
                $("#exchangeService option[value='1']").show();
                $($scope.items.exchangeService).attr("disabled", "disabled");
                $($scope.items.exchangeService).val('').change();
            }
        }

        // Copy tên nhân viên sau xét nghiệm
        if ($($scope.items.resultsPatienTime).val() !== '' && $($scope.items.resultsPatienTime).val().indexOf('y') <= -1
                && (typeof $($scope.items.staffAfterID) === 'undefined' || $($scope.items.staffAfterID).val() === '')) {
            $($scope.items.staffAfterID).val($($scope.items.staffBeforeTestID).val()).change();
        } else {
            $($scope.items.staffAfterID).val('').change();
        }

        if ($($scope.items.resultsPatienTime).val() !== '' && $($scope.items.resultsPatienTime).val() !== null &&
                !($($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("m")
                        || $($scope.items.resultsPatienTime).val().includes("y"))) {
            $($scope.items.exchangeServiceConfirm).removeAttr("disabled");

            if ($scope.id === '' && $($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2") {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id === '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

            if ($scope.id !== '' && ($scope.confirmResultsID === "2" || $scope.resultPcrHiv === "2")) {
                $("#exchangeServiceConfirm option[value='1']").show();
            } else if ($scope.id !== '') {
                $("#exchangeServiceConfirm option[value='1']").hide();
            }

        } else {
            $($scope.items.exchangeServiceConfirm).attr("disabled", "disabled");
            $($scope.items.exchangeServiceConfirm).val('').change();
        }
    });

    $($scope.items.resultPcrHiv).on("change", function () {

        $($scope.items.exchangeServiceConfirm).val('').change();

        if ($scope.id === '' && (($($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2") && $($scope.items.resultsPatienTime).val() !== "" && $($scope.items.resultsPatienTime).val() !== null &&
                !($($scope.items.resultsPatienTime).val().includes("m") || $($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("y")))) {
            $("#exchangeServiceConfirm option[value='1']").show();
        } else if ($scope.id === '') {
            $("#exchangeServiceConfirm option[value='1']").hide();
        }

        if ($scope.id !== '' && (($($scope.items.confirmResultsID).val() === "2" || $($scope.items.resultPcrHiv).val() === "2") && $($scope.items.resultsPatienTime).val() !== "" && $($scope.items.resultsPatienTime).val() !== null &&
                !($($scope.items.resultsPatienTime).val().includes("m") || $($scope.items.resultsPatienTime).val().includes("d") || $($scope.items.resultsPatienTime).val().includes("y")))) {
            $("#exchangeServiceConfirm option[value='1']").show();
        } else if ($scope.id !== '') {
            $("#exchangeServiceConfirm option[value='1']").hide();
        }

        if ($scope.id === '' && (!$($scope.items.pcrHivCheck).iCheck('update')[0].checked || $($scope.items.resultPcrHiv).val() !== "2")) {
            $("#exchangeServiceConfirm option[value='1']").hide();
        } else if ($scope.id === '') {
            $("#exchangeServiceConfirm option[value='1']").show();
        }

        if ($scope.id !== '' && (!$($scope.items.pcrHivCheck).iCheck('update')[0].checked || $($scope.items.resultPcrHiv).val() !== "2")) {
            $("#exchangeServiceConfirm option[value='1']").hide();
        } else if ($scope.id !== '') {
            $("#exchangeServiceConfirm option[value='1']").show();
        }

        if ($($scope.items.resultPcrHiv).val() !== "" && $($scope.items.resultPcrHiv).val() !== null) {
            $($scope.items.virusLoad).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
        } else if ($($scope.items.confirmResultsID).val() !== "2") {
            $($scope.items.virusLoad).attr("disabled", "disabled");
            $($scope.items.virusLoad).val('').change();
            $($scope.items.virusLoadDate).attr("disabled", "disabled");
            $($scope.items.virusLoadDate).val('').change();
        }

        if (($($scope.items.resultPcrHiv).val() !== "" && $($scope.items.resultPcrHiv).val() !== null) || $($scope.items.confirmResultsID).val() === "2") {
            $($scope.items.virusLoadNumber).removeAttr("disabled");
        } else {
            $($scope.items.virusLoadNumber).attr("disabled", "disabled");
            $($scope.items.virusLoadNumber).val("");
        }
    });

    $("#resultsPatienTime").keyup(function () {
        if ($($scope.items.resultsPatienTime).val() !== '' && (typeof $($scope.items.staffAfterID) === 'undefined' || $($scope.items.staffAfterID).val() === '')) {
            $($scope.items.staffAfterID).val($($scope.items.staffBeforeTestID).val()).change();
        }
    });

    $("#riskBehaviorID").change(function () {
        if ($("#riskBehaviorID").val() !== null && $("#riskBehaviorID").val().toString().charAt(0) === ',') {
            $("#riskBehaviorID option[value='']").removeAttr("selected");
        }
    }
    );

    // Đồng ý xn và có phản ứng mới cho nhập nơi đăng ký xn khẳng định
    $("#testResultsID").change(
            function () {
                if (($($scope.items.testResultsID).val() === $scope.items.reactive_result ||
                        $($scope.items.testResultsID).val() === $scope.items.un_specified)
                        && $($scope.items.isAgreeTest).val() === "true") {
                    $($scope.items.siteConfirmTest).removeAttr("disabled");
                } else {
                    $($scope.items.siteConfirmTest).attr("disabled", 'disabled');
                    $($scope.items.confirmTestNo).val("").change();
                    $($scope.items.confirmTime).val("").change();
                    $($scope.items.confirmResultsID).val("").change();
                    $($scope.items.earlyHiv).val("").change();
                    $($scope.items.virusLoad).val("").change();
                    $($scope.items.resultsSiteTime).val("").change();
                    $($scope.items.resultsPatienTime).val("").change();
                }

                if ($($scope.items.testMethod).val() === '2' && $($scope.items.testResultsID).val() === '2') {
                    $($scope.items.resultAnti).removeAttr("disabled");
                } else {
                    $($scope.items.resultAnti).attr("disabled", "disabled");
                    $($scope.items.resultAnti).val('').change();
                }

                if (($($scope.items.testResultsID).val() === "2" && $($scope.items.testMethod).val() === "1") || $($scope.items.testResultsID).val() === "4" || ($($scope.items.testResultsID).val() === "2" && $($scope.items.resultAnti).val() !== "" && $($scope.items.resultAnti).val() !== null)) {
                    $($scope.items.confirmType).removeAttr("disabled");

                    if ($($scope.items.resultAnti).val() === "1" || $($scope.items.testResultsID).val() === "4") {
                        $($scope.items.confirmType).val("1").change();
                        $("#confirmType option[value='2']").hide();
                        $("#confirmType option[value='']").hide();
                    } else {
                        $($scope.items.confirmType).val("2").change();
                        $("#confirmType option[value='1']").hide();
                        $("#confirmType option[value='']").hide();
                    }

                } else if ($($scope.items.testResultsID).val() !== "2" && $($scope.items.testMethod).val() !== "1") {
                    $($scope.items.confirmType).attr("disabled", "disabled");
                    $($scope.items.confirmType).val('').change();
                }

                if ($($scope.items.testResultsID).val() === "2" || ($($scope.items.testResultsID).val() === "4" && $($scope.items.testMethod).val() === "2")
                        || ($($scope.items.testResultsID).val() === "3" && $($scope.items.testMethod).val() === "1")) {
                    $($scope.items.isAgreeTest).removeAttr("disabled");
                    $($scope.items.isAgreeTest).val('true').change();
                } else {
                    $($scope.items.isAgreeTest).attr("disabled", "disabled");
                    $($scope.items.isAgreeTest).val('').change();
                }

                if ($($scope.items.testResultsID).val() === "1" && $($scope.items.resultsTime).val() !== "" && $($scope.items.resultsTime).val() !== null
                        && !($($scope.items.resultsTime).val().includes("d") || $($scope.items.resultsTime).val().includes("m") || $($scope.items.resultsTime).val().includes("y"))) {
                    $($scope.items.exchangeService).removeAttr("disabled");
                } else {
                    $($scope.items.exchangeService).attr("disabled", "disabled");
                    $($scope.items.exchangeService).val('').change();
                }

                if ($($scope.items.testResultsID).val() !== "1" && $($scope.items.isAgreeTest).val() === "true") {
                    $($scope.items.siteConfirmTest).removeAttr("disabled");
                } else {
                    $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                    $($scope.items.siteConfirmTest).val("");
                }

                if ($($scope.items.testResultsID).val() === "1") {
                    $($scope.items.resultsTime).removeAttr("disabled");
                } else {
                    $($scope.items.resultsTime).attr("disabled", "disabled");
                    $($scope.items.resultsTime).val('').change();
                }

                // Khách hàng đồng ý xét nghiệm
                if ($($scope.items.isAgreeTest).val() === "true") {
                    $($scope.items.siteConfirmTest).removeAttr("disabled");
                } else {
                    $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                    $($scope.items.siteConfirmTest).val('').change();
                }
            });

    $("#isAgreeTest").change(
            function () {

                if ($($scope.items.testResultsID).val() !== "1" && $($scope.items.isAgreeTest).val() === "true") {
                    $($scope.items.siteConfirmTest).removeAttr("disabled");
                } else {
                    $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                    $($scope.items.siteConfirmTest).val("");
                }

                if ($($scope.items.testResultsID).val() === "1") {
                    $($scope.items.resultsTime).removeAttr("disabled");
                } else {
                    $($scope.items.resultsTime).attr("disabled", "disabled");
                    $($scope.items.resultsTime).val('').change();
                }

                // Khách hàng đồng ý xét nghiệm
                if ($($scope.items.isAgreeTest).val() === "true") {
                    $($scope.items.siteConfirmTest).removeAttr("disabled");

                    if ($($scope.items.testResultsID).val() !== "4" && $($scope.items.testMethod).val() === "1") {
                        $("#confirmType option[value='1']").hide();
                        $("#confirmType option[value='']").hide();
                    }
                } else {
                    $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                    $($scope.items.siteConfirmTest).val('').change();
                }
            });

    // Ràng buộc hiển thị chuyển gửi điều trị 
    if (typeof (form.arvExchangeResult) == 'undefined' || form.arvExchangeResult == null
            || form.arvExchangeResult == '' || form.arvExchangeResult == $scope.items.notExchange) {
        $($scope.items.exchangeTime).attr("disabled", 'disabled');
    } else {
        $($scope.items.exchangeTime).removeAttr("disabled");
    }

    if (typeof (form.exchangeTime) == 'undefined' || form.exchangeTime == null || form.exchangeTime == '') {
        $($scope.items.exchangeProvinceID).attr("disabled", 'disabled');
        $($scope.items.exchangeDistrictID).attr("disabled", 'disabled');
        $($scope.items.arrivalSite).attr("disabled", 'disabled');
        $($scope.items.arrivalSiteID).attr("disabled", 'disabled');
        $($scope.items.registerTherapyTime).attr("disabled", 'disabled');
    } else {
        $($scope.items.exchangeProvinceID).removeAttr("disabled");
        $($scope.items.exchangeDistrictID).removeAttr("disabled");
        $($scope.items.arrivalSite).removeAttr("disabled");
        $($scope.items.arrivalSiteID).removeAttr("disabled");
        $($scope.items.registerTherapyTime).removeAttr("disabled");
    }

    if (typeof (form.registerTherapyTime) === 'undefined' || form.registerTherapyTime === null || form.registerTherapyTime === '') {
        $($scope.items.therapyRegistProvinceID).attr("disabled", 'disabled');
        $($scope.items.therapyRegistDistrictID).attr("disabled", 'disabled');
        $($scope.items.registeredTherapySite).attr("disabled", 'disabled');
        $($scope.items.therapyNo).attr("disabled", 'disabled');
    } else {
        $($scope.items.therapyRegistProvinceID).removeAttr("disabled");
        $($scope.items.therapyRegistDistrictID).removeAttr("disabled");
        $($scope.items.registeredTherapySite).removeAttr("disabled");
        $($scope.items.therapyNo).removeAttr("disabled");
    }

    $($scope.items.arvExchangeResult).change(
            function () {
                if (typeof ($($scope.items.arvExchangeResult).val()) === 'undefined' || $($scope.items.arvExchangeResult).val() === null
                        || $($scope.items.arvExchangeResult).val() === '' || $($scope.items.arvExchangeResult).val() === $scope.items.notExchange) {

                    $($scope.items.exchangeTime).val("").change();
                    $($scope.items.exchangeProvinceID).val("").change();
                    $($scope.items.exchangeDistrictID).val("").change();
                    $($scope.items.arrivalSite).val("").change();
                    $($scope.items.registerTherapyTime).val("").change();
                    $($scope.items.therapyRegistProvinceID).val("").change();
                    $($scope.items.therapyRegistDistrictID).val("").change();
                    $($scope.items.registeredTherapySite).val("").change();
                    $($scope.items.therapyNo).val("").change();
                    $($scope.items.arrivalSiteID).val("").change();

                    $($scope.items.exchangeTime).attr("disabled", 'disabled');
                    $($scope.items.exchangeProvinceID).attr("disabled", 'disabled');
                    $($scope.items.exchangeDistrictID).attr("disabled", 'disabled');
                    $($scope.items.arrivalSite).attr("disabled", 'disabled');
                    $($scope.items.arrivalSiteID).attr("disabled", 'disabled');
                    $($scope.items.registerTherapyTime).attr("disabled", 'disabled');
                    $($scope.items.therapyRegistProvinceID).attr("disabled", 'disabled');
                    $($scope.items.therapyRegistDistrictID).attr("disabled", 'disabled');
                    $($scope.items.registeredTherapySite).attr("disabled", 'disabled');
                    $($scope.items.therapyNo).attr("disabled", 'disabled');

                } else {
                    $($scope.items.exchangeTime).removeAttr("disabled");
                }
            }
    );

    // Hiển thị tên cơ sở xét nghiệm khẳng định
    $($scope.items.siteConfirmTest).change(
            function () {
                $("#siteConfirmTest option:selected").text() == 'Chọn cơ sở xét nghiệm' ? $($scope.items.siteConfirmTestDsp).val('').change() : $($scope.items.siteConfirmTestDsp).val($("#siteConfirmTest option:selected").text()).change();
                //DSNAnh 17/09/2019
                $($scope.items.siteConfirmTestDsp).attr("data-original-title", $("#siteConfirmTest option:selected").text() == 'Chọn cơ sở xét nghiệm' ? "" : $("#siteConfirmTest option:selected").text());
            }
    );

    $($scope.items.exchangeTime).on("change", function () {
        var regex = new RegExp('[dmy]');
        if ($($scope.items.exchangeTime).val() == null || $($scope.items.exchangeTime).val() == ''
                || regex.test($($scope.items.exchangeTime).val())) {
            $($scope.items.exchangeProvinceID).attr("disabled", 'disabled');
            $($scope.items.exchangeDistrictID).attr("disabled", 'disabled');
            $($scope.items.arrivalSite).attr("disabled", 'disabled');
            $($scope.items.arrivalSiteID).attr("disabled", 'disabled');
            $($scope.items.registerTherapyTime).attr("disabled", 'disabled');
            $($scope.items.therapyRegistProvinceID).attr("disabled", 'disabled');
            $($scope.items.therapyRegistDistrictID).attr("disabled", 'disabled');
            $($scope.items.registeredTherapySite).attr("disabled", 'disabled');
            $($scope.items.therapyNo).attr("disabled", 'disabled');
        } else {
            $($scope.items.exchangeProvinceID).removeAttr("disabled");
            $($scope.items.exchangeDistrictID).removeAttr("disabled");
            $($scope.items.arrivalSite).removeAttr("disabled");
            $($scope.items.arrivalSiteID).removeAttr("disabled");
            $($scope.items.registerTherapyTime).removeAttr("disabled");
        }
    });

    $($scope.items.registerTherapyTime).on("change", function () {
        var regex = new RegExp('[dmy]');
        if ($($scope.items.registerTherapyTime).val() == null || $($scope.items.registerTherapyTime).val() == ''
                || regex.test($($scope.items.registerTherapyTime).val())) {
            $($scope.items.therapyRegistProvinceID).attr("disabled", 'disabled');
            $($scope.items.therapyRegistDistrictID).attr("disabled", 'disabled');
            $($scope.items.registeredTherapySite).attr("disabled", 'disabled');
            $($scope.items.therapyNo).attr("disabled", 'disabled');
        } else {
            $($scope.items.therapyRegistProvinceID).removeAttr("disabled");
            $($scope.items.therapyRegistDistrictID).removeAttr("disabled");
            $($scope.items.registeredTherapySite).removeAttr("disabled");
            $($scope.items.therapyNo).removeAttr("disabled");
        }
    });

    // Mapping nồng độ virus và kq xntlvr
    $("#virusLoadNumber").on('blur', function () {
        if (parseInt($($scope.items.virusLoadNumber).val()) > 0 && parseInt($($scope.items.virusLoadNumber).val()) < 200) {
            $("#virusLoad").val('2').change();
        } else if (parseInt($($scope.items.virusLoadNumber).val()) >= 200 && parseInt($($scope.items.virusLoadNumber).val()) <= 1000) {
            $("#virusLoad").val('3').change();
        } else if (parseInt($($scope.items.virusLoadNumber).val()) > 1000) {
            $("#virusLoad").val('4').change();
        } else {
            $("#virusLoad").val('').change();
        }
    });

    $scope.initSiteConfirmTest = function () {
        $.ajax({
            url: '/service/htc/site-confirmatory-test.json',
            processResults: function (result) {
                var items = [];
                for (var i = 0; i < result.data.length; i++) {
                    items[i] = {id: result.data[i].id, text: result.data[i].name};
                }
                return {
                    results: items
                };
            }
//            cache: true
        }).then(function (result) {
            if ($scope.requestConfirmTime !== null && $scope.requestConfirmTime !== '' && ($scope.updateConfirmTime === null || $scope.updateConfirmTime === '')) {
                for (var i = 0; i < result.data.length; i++) {
                    if ($scope.siteConfirmTest === result.data[i].id + '') {
                        $("#siteConfirmTestNameDsp").val(result.data[i].name);
                        $("#siteConfirmTestDsp").val(result.data[i].name);
                        return;
                    }
                }
            } else {
                if (result.data.length > 0) {
                    $($scope.items.siteConfirmTest).append(new Option("Chọn cơ sở xét nghiệm", "", i == 0, false));
                }

                for (var i = 0; i < result.data.length; i++) {
                    var option = new Option(result.data[i].name, result.data[i].id, false, false);
                    $($scope.items.siteConfirmTest).append(option);
                }
                $($scope.items.siteConfirmTest).val(utils.getContentOfDefault(form.siteConfirmTest, '')).change();
                $($scope.items.siteConfirmTest).trigger('change');
            }
        });
    };

    if ($scope.id != "" && $($scope.items.isDisplayCopy).val() == "") {
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
            $($scope.items.currentAddress).attr({disabled: "disable"}).val($("#permanentAddress").val()).change();
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($("#permanentAddressGroup").val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($("#permanentAddressStreet").val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($("#permanentProvinceID").val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($("#permanentDistrictID").val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($("#permanentWardID").val()).change();
            $($scope.items.isDisplayCopy).val(true);
        }
    };

    /**
     * Tự động lấy visit cuối cùng để sugget cho người dùng
     * @auth vvThành
     * @returns {undefined}
     */
    $scope.codeSupport = function () {
        $.ajax({
            url: '/service/htc/last-visit.json',
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

    var getURLParameter = function (sParam) {
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');
        for (var i = 0; i < sURLVariables.length; i++)
        {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam)
            {
                return sParameterName[1];
            }
        }
    };

});

/**
 * @auth pdThắng
 */
app.controller('htc_view', function ($scope, msg) {

    $scope.pOptions = pOptions;
    $scope.code = utils.getContentOfDefault(form.code, '');
    $scope.asanteTest = utils.getContentOfDefault(form.asanteTest, '');
    $scope.isAgreePreTest = utils.getContentOfDefault(form.isAgreePreTest, '');
    $scope.preTestTime = utils.getContentOfDefault(form.preTestTime, '');
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.selectedItem = utils.getContentOfDefault(form.isAgreeTest, '');
    $scope.serviceID = utils.getContentOfDefault(form.serviceID, 'CD');
    $scope.advisoryeTime = utils.getContentOfDefault(form.advisoryeTime,
            ('0' + (new Date()).getDate()).slice(-2) + '/' + ('0' + ((new Date()).getMonth() + 1)).slice(-2) + '/' + (new Date()).getFullYear());
    $scope.resultsTime = utils.getContentOfDefault(form.resultsTime, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.currentProvinceID = utils.getContentOfDefault(form.currentProvinceID, '');
    $scope.currentDistrictID = utils.getContentOfDefault(form.currentDistrictID, '');
    $scope.currentWardID = utils.getContentOfDefault(form.currentWardID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.confirmResultsID = utils.getContentOfDefault(form.confirmResultsID, '');
    $scope.confirmTestStatus = utils.getContentOfDefault(form.confirmTestStatus, '');
    $scope.arrivalSite = utils.getContentOfDefault(form.arrivalSite, '');

    $scope.resultsSiteTime = utils.getContentOfDefault(form.resultsSiteTime, '');
    $scope.resultsPatienTime = utils.getContentOfDefault(form.resultsPatienTime, '');
    $scope.exchangeConsultTime = utils.getContentOfDefault(form.exchangeConsultTime, '');
    $scope.exchangeTime = utils.getContentOfDefault(form.exchangeTime, '');
    $scope.registerTherapyTime = utils.getContentOfDefault(form.registerTherapyTime, '');
    $scope.therapyNo = utils.getContentOfDefault(form.therapyNo, '');
    $scope.exchangeProvinceID = utils.getContentOfDefault(form.exchangeProvinceID, '');
    $scope.exchangeDistrictID = utils.getContentOfDefault(form.exchangeDistrictID, '');
    $scope.confirmTestNo = utils.getContentOfDefault(form.confirmTestNo, '');
    $scope.testResultsID = utils.getContentOfDefault(form.testResultsID, '');
    $scope.partnerProvideResult = utils.getContentOfDefault(form.partnerProvideResult, '');
    $scope.referralSource = utils.getContentOfDefault(form.referralSource, '');
    $scope.staffAfterID = utils.getContentOfDefault(form.staffAfterID, '');
    $scope.isAgreeTest = typeof (form.isAgreeTest) == 'undefined' || form.isAgreeTest == null || (form.isAgreeTest + '') == '' ? '' : form.isAgreeTest + "";
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;
    $scope.arvExchangeResult = utils.getContentOfDefault(form.arvExchangeResult, '');
    $scope.arrivalSiteID = utils.getContentOfDefault(form.arrivalSiteID, '');
    $scope.siteConfirmTest = utils.getContentOfDefault(form.siteConfirmTest, '');
    $scope.pageRedirect = utils.getContentOfDefault(form.pageRedirect, '');
    $scope.therapyRegistProvinceID = utils.getContentOfDefault(form.therapyRegistProvinceID, '');
    $scope.therapyRegistDistrictID = utils.getContentOfDefault(form.therapyRegistDistrictID, '');
    $scope.hasHealthInsurance = utils.getContentOfDefault(form.hasHealthInsurance, '');
    $scope.healthInsuranceNo = utils.getContentOfDefault(form.healthInsuranceNo, '');
    $scope.patientIDAuthen = utils.getContentOfDefault(form.patientIDAuthen, '');
    $scope.earlyHiv = utils.getContentOfDefault(form.earlyHiv, '');
    $scope.virusLoad = utils.getContentOfDefault(form.virusLoad, '');
    $scope.laytestID = utils.getContentOfDefault(form.laytestID, '');
    $scope.approacherNo = utils.getContentOfDefault(form.approacherNo, '');
    $scope.youInjectCode = utils.getContentOfDefault(form.youInjectCode, '');
    $scope.resultsID = utils.getContentOfDefault(form.resultsID, '');
    $scope.bioName = utils.getContentOfDefault(form.bioName, '');
    $scope.earlyHivDate = utils.getContentOfDefault(form.earlyHivDate, '');
    $scope.virusLoadDate = utils.getContentOfDefault(form.virusLoadDate, '');
    $scope.requestConfirmTime = utils.getContentOfDefault(form.requestConfirmTime, '');
    $scope.updateConfirmTime = utils.getContentOfDefault(form.updateConfirmTime, '');
    $scope.resultPcrHiv = utils.getContentOfDefault(form.resultPcrHiv, '');
    $scope.exchangeServiceConfirm = utils.getContentOfDefault(form.exchangeServiceConfirm, '');
    $scope.resultAnti = utils.getContentOfDefault(form.resultAnti, '');
    $scope.exchangeService = utils.getContentOfDefault(form.exchangeService, '');
    $scope.confirmType = utils.getContentOfDefault(form.confirmType, '');
    $scope.testMethod = utils.getContentOfDefault(form.testMethod, '');
    $scope.pcrHiv = utils.getContentOfDefault(form.pcrHiv, '');
    $scope.cdService = utils.getContentOfDefault(form.cdService, '');

    $scope.items = {
        siteConfirmTest: "#siteConfirmTest",
        siteConfirmTestDsp: "#siteConfirmTestDsp",
        confirmResultsID: "#confirmResultsID",
        confirmType: "#confirmType"
    };

    $scope.initSiteConfirmTest = function () {
        $.ajax({
            url: '/service/htc/site-confirmatory-test.json',
            processResults: function (result) {
                var items = [];
                for (var i = 0; i < result.data.length; i++) {
                    items[i] = {id: result.data[i].id, text: result.data[i].name};
                }
                return {
                    results: items
                };
            }
//            cache: true
        }).then(function (result) {
            if ($scope.requestConfirmTime !== null && $scope.requestConfirmTime !== '' && ($scope.updateConfirmTime === null || $scope.updateConfirmTime === '')) {
                for (var i = 0; i < result.data.length; i++) {
                    if ($scope.siteConfirmTest === result.data[i].id + '') {
                        $("#siteConfirmTestNameDsp").val(result.data[i].name);
                        $("#siteConfirmTestDsp").val(result.data[i].name);
                        return;
                    }
                }
            } else {
                if (result.data.length > 0) {
                    $($scope.items.siteConfirmTest).append(new Option("Chọn cơ sở xét nghiệm", "", i == 0, false));
                }

                for (var i = 0; i < result.data.length; i++) {
                    var option = new Option(result.data[i].name, result.data[i].id, false, false);
                    $($scope.items.siteConfirmTest).append(option);
                    if ($scope.siteConfirmTest === result.data[i].id + '') {
                        $("#siteConfirmTestDsp").val(result.data[i].name);
                    }
                }
                $($scope.items.siteConfirmTest).val(utils.getContentOfDefault(form.siteConfirmTest, '')).change();
                $($scope.items.siteConfirmTest).trigger('change');
            }
        });
    };

    //In phiếu chuyển gửi
    $scope.transferOPC = function (oid) {
        if (oid === null) {
            msg.warning("Chưa chọn khách hàng cần in phiếu.");
            return false;
        }
        url = urlTransferOPC + "?oid=" + oid;
        $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
    };

    $scope.init = function () {
        $scope.switchConfig();

        //Dữ liệu địa danh
        $scope.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        $("#permanentProvinceID").attr("disabled", "disabled");
        $("#permanentDistrictID").attr("disabled", "disabled");
        $("#permanentWardID").attr("disabled", "disabled");

        $scope.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");
        $("#currentProvinceID").attr("disabled", "disabled");
        $("#currentDistrictID").attr("disabled", "disabled");
        $("#currentWardID").attr("disabled", "disabled");

        $scope.initSiteConfirmTest();
        $("#siteConfirmTest").attr("disabled", "disabled");

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

        if ($scope.pcrHiv === "1") {
            $('#pcrHivCheck').iCheck('check');
        }

        $("#pcrHivCheck").attr("readonly", true);
        $("#pcrHivCheck").attr("disabled", "disabled");
        $("#cdService").attr("disabled", "disabled");

        if ($("#objectGroupID").val() === "Bệnh nhân lao") {
            $("#lao_block").show();
        } else {
            $("#lao_block").hide();
            $("#laoVariable").val("").change();
        }
    };

});