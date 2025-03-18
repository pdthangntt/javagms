app.service('htcConfirmService', function ($uibModal) {
    var elm = this;
    elm.logs = function (oid, code, name) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'htcConfirmLogs',
            controller: 'htc_confirm_log',
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
});

app.controller('htc_confirm_log', function ($scope, $uibModalInstance, params, msg) {
    $scope.model = {staffID: 0, htcConfirmID: params.oid, code: params.code, name: params.name};
    loading.show();
    $scope.list = function () {
        $.ajax({
            url: urConfirmLog,
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
            url: urlConfirmLogCreate,
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

app.controller('htc_confirm_new', function ($scope, locations) {

    $scope.pOptions = pOptions;
    $scope.disabledEarlyHiv = utils.getContentOfDefault(form.disabledEarlyHiv, '');
    $scope.disabledVirusLoad = utils.getContentOfDefault(form.disabledVirusLoad, '');
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.code = utils.getContentOfDefault(form.code, '');
    $scope.sourceID = utils.getContentOfDefault(form.sourceID, '');
    $scope.sourceReceiveSampleTime = utils.getContentOfDefault(form.sourceReceiveSampleTime, '');
    $scope.testResultsID = utils.getContentOfDefault(form.testResultsID, '');
    $scope.sampleQuality = utils.getContentOfDefault(form.sampleQuality, '');
    $scope.sourceSampleDate = utils.getContentOfDefault(form.sourceSampleDate, '');
    $scope.siteName = utils.getContentOfDefault(form.siteName, currentSideName);
    $scope.sampleReceiveTime = utils.getContentOfDefault(form.sampleReceiveTime, '');
    $scope.acceptDate = utils.getContentOfDefault(form.acceptDate, '');
    $scope.bioName1 = utils.getContentOfDefault(form.bioName1, '');
    $scope.bioName2 = utils.getContentOfDefault(form.bioName2, '');
    $scope.bioName3 = utils.getContentOfDefault(form.bioName3, '');
    $scope.bioNameResult1 = utils.getContentOfDefault(form.bioNameResult1, '');
    $scope.bioNameResult2 = utils.getContentOfDefault(form.bioNameResult2, '');
    $scope.bioNameResult3 = utils.getContentOfDefault(form.bioNameResult3, '');
    $scope.resultsID = utils.getContentOfDefault(form.resultsID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.resultsReturnTime = utils.getContentOfDefault(form.resultsReturnTime, '');
    $scope.testStaffID = utils.getContentOfDefault(form.testStaffID, '');
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.genderID = utils.getContentOfDefault(form.genderID, '');
    $scope.year = utils.getContentOfDefault(form.year, '');
    $scope.testStaffID = utils.getContentOfDefault(form.testStaffID, current_user_name);
    $scope.provinceID = utils.getContentOfDefault(form.provinceID, '');
    $scope.districtID = utils.getContentOfDefault(form.districtID, '');
    $scope.wardId = utils.getContentOfDefault(form.wardId, '');
    $scope.sourceServiceID = utils.getContentOfDefault(form.sourceServiceID, '');
    $scope.earlyHiv = utils.getContentOfDefault(form.earlyHiv, '');
    $scope.virusLoad = utils.getContentOfDefault(form.virusLoad, '');
    $scope.firstBioDate = utils.getContentOfDefault(form.firstBioDate, '');
    $scope.secondBioDate = utils.getContentOfDefault(form.secondBioDate, '');
    $scope.thirdBioDate = utils.getContentOfDefault(form.thirdBioDate, '');
    $scope.earlyHivDate = utils.getContentOfDefault(form.earlyHivDate, '');
    $scope.virusLoadDate = utils.getContentOfDefault(form.virusLoadDate, '');
    $scope.serviceID = utils.getContentOfDefault(form.serviceID, '');
    $scope.virusLoadNumber = utils.getContentOfDefault(form.virusLoadNumber, '');
    $scope.earlyBioName = utils.getContentOfDefault(form.earlyBioName, '');
    $scope.earlyDiagnose = utils.getContentOfDefault(form.earlyDiagnose, '');
    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;

    // Thêm custom validate date
    $.validator.addMethod("validDate", function (value, element) {
        return value.match(/(?:0[1-9]|[12][0-9]|3[01])\/(?:0[1-9]|1[0-2])\/(?:19|20\d{2})/);
    }, "Nhập đúng định dạng theo dd/mm/yyyy");

    $scope.items = {
        id: "#ID",
        code: "#code",
        year: "#year",
        fullname: "#fullname",
        address: "#address",
        permanentAddressStreet: "#permanentAddressStreet",
        permanentAddressGroup: "#permanentAddressGroup",
        provinceID: "#provinceID",
        districtID: "#districtID",
        wardID: "#wardID",
        genderID: "#genderID",
        objectGroupID: "#objectGroupID",
        sourceSiteID: "#sourceSiteID",
        sourceServiceID: "#sourceServiceID",
        testResultsID: "#testResultsID",
        sampleQuality: "#sampleQuality",
        bioNameResult1: "#bioNameResult1",
        bioNameResult2: "#bioNameResult2",
        bioNameResult3: "#bioNameResult3",
        resultsID: "#resultsID",
        siteName: "#siteName",
        bioName1: "#bioName1",
        bioName2: "#bioName2",
        bioName3: "#bioName3",
        sourceID: "#sourceID",
        acceptDate: "#acceptDate",
        sourceSampleDate: "#sourceSampleDate",
        sourceReceiveSampleTime: "#sourceReceiveSampleTime",
        virusLoad: "#virusLoad",
        earlyHiv: "#earlyHiv",
        sampleSaveCode: "#sampleSaveCode",
        firstBioDate: "#firstBioDate",
        secondBioDate: "#secondBioDate",
        thirdBioDate: "#thirdBioDate",
        virusLoadDate: "#virusLoadDate",
        sampleSentSource: "#sampleSentSource",
        earlyHivDate: "#earlyHivDate",
        patientID: "#patientID",
        otherTechnical: "#otherTechnical",
        resultsReturnTime: "#resultsReturnTime",
        confirmTime: "#confirmTime",
        testStaffID: "#testStaffID",
        ID: "#ID",
        sampleReceiveTime: "#sampleReceiveTime",
        error: "#error",
        serviceID: "#serviceID",
        currentProvinceID: "#currentProvinceID",
        currentDistrictID: "#currentDistrictID",
        currentWardID: "#currentWardID",
        currentAddress: "#currentAddress",
        currentAddressStreet: "#currentAddressStreet",
        currentAddressGroup: "#currentAddressGroup",
        modeOfTransmission: "#modeOfTransmission",
        isDisplayCopy: "#isDisplayCopy",
        insurance: "#insurance",
        earlyBioName: "#earlyBioName",
        virusLoadNumber: "#virusLoadNumber",
        earlyDiagnose: "#earlyDiagnose",
        insuranceNo: "#insuranceNo"
    };

    $scope.init = function () {

        $scope.$parent.select_search($scope.items.provinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.districtID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.wardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.genderID, "Chọn giới tính");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn đối tượng");
        $scope.$parent.select_search($scope.items.testResultsID, "Chọn KQ XN sàng lọc");
        $scope.$parent.select_search($scope.items.sampleQuality, "Chọn chất lượng mẫu");
        $scope.$parent.select_search($scope.items.bioNameResult1, "Chọn kết quả");
        $scope.$parent.select_search($scope.items.bioNameResult2, "Chọn kết quả");
        $scope.$parent.select_search($scope.items.bioNameResult3, "Chọn kết quả");
        $scope.$parent.select_search($scope.items.resultsID, "Chọn kết quả XN khẳng định");
        $scope.$parent.select_search($scope.items.sourceSiteID, "Chọn cơ sở gửi mẫu");
        $scope.$parent.select_search($scope.items.bioName1, "Chọn tên sinh phẩm");
        $scope.$parent.select_search($scope.items.bioName2, "Chọn tên sinh phẩm");
        $scope.$parent.select_search($scope.items.bioName3, "Chọn tên sinh phẩm");
        $scope.$parent.select_search($scope.items.currentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.currentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.currentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.modeOfTransmission, "Chọn đường lây nhiễm");
        $scope.$parent.select_search($scope.items.earlyBioName, "Chọn sinh phẩm xét nghiệm");

        $scope.initProvince($scope.items.provinceID, $scope.items.districtID, $scope.items.wardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        // Auto complete address
//        $scope.addressAutocomplete($scope.items.address, $scope.items.provinceID, $scope.items.districtID, $scope.items.wardID);
//        $scope.addressAutocomplete($scope.items.currentAddress, $scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        // Set disable for confirm test site
        $($scope.items.siteName).attr("disabled", 'disabled');
        // Set disable for code in case updating

        // Mặc định có xét nghiệm sàng lọc
        $($scope.items.sourceID).ready(function () {
            if (($scope.code == 'undefined' || $scope.code == '') && ($scope.sourceID != '')) {
                $($scope.items.code).val($($scope.items.sourceID).val()).change();
            }
        });
        //set tu dong dia chi co so hien tai
        if (utils.getContentOfDefault($scope.provinceID, null) == null && $scope.items.provinceID == "#provinceID") {
            $($scope.items.provinceID).val(province_id).change();
        }
        if (utils.getContentOfDefault($scope.districtID, null) == null && $scope.items.districtID == "#districtID") {
            $($scope.items.districtID).val(district_id).change();
        }
        if (utils.getContentOfDefault($scope.wardId, null) == null && $scope.items.wardId == "#wardId") {
            $($scope.items.wardId).val(ward_id).change();
        }

        // set max length
//        $($scope.items.code).attr('maxlength', $scope.code !== '' ? $scope.code.length : $($scope.items.sourceID).val().length);

        // Clear data theo kết quả xét nghiệm khẳng định
        if ($($scope.items.resultsID).val() !== '2') {
            $($scope.items.virusLoad).val('').change();
            $($scope.items.virusLoadDate).val('').change();
            $($scope.items.earlyHiv).val('').change();
            $($scope.items.earlyHivDate).val('').change();
//            $($scope.items.sampleSaveCode).val('').change();
//            $($scope.items.sampleSaveCode).attr("disabled", "disabled");
        }
//        else {
//            $($scope.items.sampleSaveCode).removeAttr("disabled");
//        }

        $($scope.items.resultsID).change(function () {
            if ($($scope.items.resultsID).val() !== '2') {
                $($scope.items.virusLoad).val('').change();
                $($scope.items.virusLoadDate).val('').change();
                $($scope.items.earlyHiv).val('').change();
                $($scope.items.earlyHivDate).val('').change();
//                $($scope.items.sampleSaveCode).val('').change();
//                $($scope.items.sampleSaveCode).attr("disabled", "disabled").change();
            }
//            else {
//                $($scope.items.sampleSaveCode).removeAttr("disabled");
//            }
            if ($($scope.items.resultsID).val() == '') {
                $($scope.items.resultsReturnTime).val('').change();
                $($scope.items.resultsReturnTime).attr("disabled", "disabled").change();
            } else {
                $($scope.items.resultsReturnTime).removeAttr("disabled");
            }

        });

//        $($scope.items.earlyHiv).change(function () {
//            if ($scope.resultsID !== '1') {
//                $($scope.items.virusLoad).val('').change();
//            }
//        });

        // Check hiển thị nơi gửi mẫu bệnh phẩm
        if ($($scope.items.objectGroupID).val() !== '19') {
            $($scope.items.sampleSentSource).attr("disabled", "disabled").change();
        } else {
            $($scope.items.sampleSentSource).removeAttr("disabled");
        }
        if ($($scope.items.resultsReturnTime).val() != '' && $($scope.items.ID).val() != '' && (error == null || (errorDetail != null && errorDetail.includes('virusLoadDate')) || (errorDetail != null && errorDetail.includes('earlyHivDate')))) {
            $($scope.items.code).attr("disabled", 'disabled');
            $($scope.items.resultsReturnTime).attr("disabled", 'disabled');
            $($scope.items.fullname).attr("disabled", 'disabled');
            $($scope.items.genderID).attr("disabled", 'disabled');
            $($scope.items.patientID).attr("disabled", 'disabled');
            $($scope.items.year).attr("disabled", 'disabled');
            $($scope.items.objectGroupID).attr("disabled", 'disabled');
            $($scope.items.sampleSentSource).attr("disabled", 'disabled');
            $($scope.items.address).attr("disabled", 'disabled');
            $($scope.items.permanentAddressGroup).attr("disabled", 'disabled');
            $($scope.items.permanentAddressStreet).attr("disabled", 'disabled');
            $($scope.items.provinceID).attr("disabled", 'disabled');
            $($scope.items.districtID).attr("disabled", 'disabled');
            $($scope.items.wardID).attr("disabled", 'disabled');
            $($scope.items.sourceSiteID).attr("disabled", 'disabled');
            $($scope.items.sourceID).attr("disabled", 'disabled');
            $($scope.items.testResultsID).attr("disabled", 'disabled');
            $($scope.items.sourceReceiveSampleTime).attr("disabled", 'disabled');
            $($scope.items.sourceSampleDate).attr("disabled", 'disabled');
            $($scope.items.sampleReceiveTime).attr("disabled", 'disabled');

            $($scope.items.sampleQuality).attr("disabled", 'disabled');
            $($scope.items.bioName1).attr("disabled", 'disabled');
            $($scope.items.bioName2).attr("disabled", 'disabled');
            $($scope.items.bioName3).attr("disabled", 'disabled');
            $($scope.items.bioNameResult1).attr("disabled", 'disabled');
            $($scope.items.bioNameResult2).attr("disabled", 'disabled');
            $($scope.items.bioNameResult3).attr("disabled", 'disabled');
            $($scope.items.firstBioDate).attr("disabled", 'disabled');
            $($scope.items.secondBioDate).attr("disabled", 'disabled');
            $($scope.items.thirdBioDate).attr("disabled", 'disabled');
            $($scope.items.otherTechnical).attr("disabled", 'disabled');
            $($scope.items.resultsID).attr("disabled", 'disabled');
            $($scope.items.sampleSaveCode).attr("disabled", 'disabled');
            $($scope.items.confirmTime).attr("disabled", 'disabled');
            $($scope.items.testStaffID).attr("disabled", 'disabled');
            $($scope.items.serviceID).attr("disabled", 'disabled');

            $($scope.items.modeOfTransmission).attr("disabled", 'disabled');
            $($scope.items.currentAddress).attr("disabled", 'disabled');
            $($scope.items.currentAddressGroup).attr("disabled", 'disabled');
            $($scope.items.currentAddressStreet).attr("disabled", 'disabled');
            $($scope.items.currentProvinceID).attr("disabled", 'disabled');
            $($scope.items.currentDistrictID).attr("disabled", 'disabled');
            $($scope.items.currentWardID).attr("disabled", 'disabled');

            $($scope.items.insurance).attr("disabled", 'disabled');
            $($scope.items.insuranceNo).attr("disabled", 'disabled');
            $($scope.items.earlyBioName).attr("disabled", 'disabled');
            $($scope.items.virusLoadNumber).attr("disabled", 'disabled');
            $($scope.items.earlyDiagnose).attr("disabled", 'disabled');
            $($scope.items.earlyHivDate).attr("disabled", 'disabled');
            $($scope.items.earlyHiv).attr("disabled", 'disabled');
            $($scope.items.virusLoadDate).attr("disabled", 'disabled');
            $($scope.items.virusLoad).attr("disabled", 'disabled');
        }

//        if ($scope.disabledEarlyHiv) {
//            $($scope.items.earlyHiv).attr("disabled", 'disabled');
//            $($scope.items.earlyHivDate).attr("disabled", 'disabled');
//        }
//
//        if ($scope.disabledVirusLoad) {
//            $($scope.items.virusLoad).attr("disabled", 'disabled');
//            $($scope.items.virusLoadDate).attr("disabled", 'disabled');
//        }

        if ($($scope.items.resultsID).val() == '') {
            $($scope.items.resultsReturnTime).attr("disabled", 'disabled');
        }

        if ($scope.sourceServiceID !== '' && $scope.sourceServiceID !== '500') {
            $($scope.items.serviceID).attr("disabled", "disabled");
        }

        //event copy
        $("#provinceID, #address, #permanentAddressStreet, #permanentAddressGroup, #districtID, #wardID").change(function () {
            if ($scope.isCopyPermanentAddress) {
                $($scope.items.currentAddress).val($($scope.items.address).val()).change();
                $($scope.items.currentAddressStreet).val($($scope.items.permanentAddressStreet).val()).change();
                $($scope.items.currentAddressGroup).val($($scope.items.permanentAddressGroup).val()).change();
                $($scope.items.currentProvinceID).val($($scope.items.provinceID).val()).change();
                $($scope.items.currentDistrictID).val($($scope.items.districtID).val()).change();
                $($scope.items.currentWardID).val($($scope.items.wardID).val()).change();
            }
        });

        // Ràng buộc kết quả nhiễm mới HIV:
        if ($scope.earlyHiv === "3") {
            $("#earlyHivDate").attr({disabled: "disable"}).val('').change();
        }

        $("#earlyHiv").on('change', function () {
            if ($("#earlyHiv").val() === "3") {
                $("#earlyHivDate").attr({disabled: "disable"}).val('').change();
            } else {
//                $("#earlyHivDate").removeAttr("disabled").val('').change();
            }
        });

        // Ràng buộc kết quả XN tải lượng virus
        if ($scope.virusLoad === "5") {
            $("#virusLoadDate").attr({disabled: "disable"}).val('').change();
        }

        $("#virusLoad").on('change', function () {
            if ($("#virusLoad").val() === "5") {
                $("#virusLoadDate").attr({disabled: "disable"}).val('').change();
            } else {
//                $("#virusLoadDate").removeAttr("disabled").val('').change();
            }
        });

        $('form[name="test_form"]').ready(function () {
            if ($($scope.items.resultsReturnTime).val() != '' && $($scope.items.ID).val() != '') {
                $("#copyButton").attr("disabled", 'disabled');
            }
        });

        if ($($scope.items.insurance).val() !== '1') {
            $($scope.items.insuranceNo).attr("disabled", "disabled");
        }

        $("#virusLoadNumber").on('change', function () {
            if ($scope.virusLoadNumber > 0 && $scope.virusLoadNumber < 200) {
                $("#virusLoad").val('2').change();
            } else if ($scope.virusLoadNumber >= 200 && $scope.virusLoadNumber <= 1000) {
                $("#virusLoad").val('3').change();
            } else if ($scope.virusLoadNumber > 1000) {
                $("#virusLoad").val('4').change();
            } else {
                $("#virusLoad").val('').change();
            }
        });

        //an hien thong tin nhiem moi
        if ((($($scope.items.earlyDiagnose).val() == null || $($scope.items.earlyDiagnose).val() == '') &&
                ($($scope.items.resultsReturnTime).val() != null && $($scope.items.resultsReturnTime).val() != '')) ||
                error !== null) {
            $($scope.items.earlyDiagnose).removeAttr("disabled");
            $($scope.items.earlyBioName).removeAttr("disabled");
            $($scope.items.earlyHivDate).removeAttr("disabled");
            $($scope.items.earlyHiv).removeAttr("disabled");
            $($scope.items.virusLoadDate).removeAttr("disabled");
            $($scope.items.virusLoadNumber).removeAttr("disabled");
            $($scope.items.virusLoad).removeAttr("disabled");
        }

    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            address: {
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
            code: {
                required: true
            },
            fullname: {
                required: true
            },
//            address: {
//                required: true
//            },
            genderID: {
                required: true
            },
            objectGroupID: {
                required: true
            },
            provinceID: {
                required: true
            },
            districtID: {
                required: true
            },
            wardID: {
                required: true
            },
            resultsID: {
                required: false
            },
            confirmTime: {
                required: false
            },
            testStaffID: {
                required: function () {
                    return $($scope.items.resultsID).val().length > 0;
                }
            },
            sourceSiteID: {
                required: true
            },
            sourceID: {
                required: true
            },
            sourceReceiveSampleTime: {
                required: true,
                validDate: true
            },
            testResultsID: {
                required: true
            },
            sourceSampleDate: {
                required: true,
                validDate: true
            },
            sampleReceiveTime: {
                required: true,
                validDate: true
            },
            acceptDate: {
//                validDate: $($scope.items.acceptDate).val().length > 0
            },
            bioNameResult1: {
                required: function () {
                    return $($scope.items.bioName1).val().length > 0;
                }
            },
            bioNameResult2: {
                required: function () {
                    return $($scope.items.bioName2).val().length > 0;
                }
            },
            bioNameResult3: {
                required: function () {
                    return $($scope.items.bioName3).val().length > 0;
                }
            },
            bioName1: {
                required: function () {
                    return $($scope.items.bioNameResult1).val().length > 0 || $($scope.items.firstBioDate).val().length > 0;
                }
            },
            bioName2: {
                required: function () {
                    return $($scope.items.bioNameResult2).val().length > 0 || $($scope.items.secondBioDate).val().length > 0;
                }
            },
            bioName3: {
                required: function () {
                    return $($scope.items.bioNameResult3).val().length > 0 || $($scope.items.thirdBioDate).val().length > 0;
                }
            },
            earlyHivDate: {
                required: function () {
                    return $($scope.items.earlyHiv).val().length > 0 && $($scope.items.virusLoad).val() !== "3";
                },
            },
            earlyHiv: {
                required: function () {
                    return $($scope.items.earlyHivDate).val().length > 0;
                }
            },
            virusLoadDate: {
                required: function () {
                    return $($scope.items.virusLoad).val().length > 0 && $($scope.items.virusLoad).val() !== "5";
                },
            },
            virusLoad: {
                required: function () {
                    return $($scope.items.virusLoadDate).val().length > 0;
                }
            },
            sampleSentSource: {
                maxlength: 500
            },
            serviceID: {
                required: false
            },
            virusLoadNumber: {
                digits: true,
            },
            year: {
                required: function () {
                    return $($scope.items.resultsID).val() === '2';
                }
            }
        },
        messages: {
            address: {
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
                required: "Mã XN khẳng định không được để trống"
            },
            fullname: {
                required: "Họ tên khách hàng không được để trống"
            },
//            address: {
//                required: "Số nhà/tổ/thôn/xóm không được để trống"
//            },
            genderID: {
                required: "Giới tính không được để trống"
            },
            objectGroupID: {
                required: "Đối tượng không được để trống"
            },
            provinceID: {
                required: "Tỉnh/Thành phố không được để trống"
            },
            districtID: {
                required: "Quận/Huyện không được để trống"
            },
            wardID: {
                required: "Xã/Phường không được để trống"
            },
            resultsID: {
                required: "Kết quả XN khẳng định không được để trống"
            },
            confirmTime: {
                required: "Ngày XN khẳng định không được để trống"
            },
            testStaffID: {
                required: "Cán bộ XN khẳng định không được để trống"
            },
            sourceSiteID: {
                required: "Tên cơ sở gửi mẫu không được để trống"
            },
            sourceID: {
                required: "Mã khách hàng XN sàng lọc không được để trống"
            },
            sourceReceiveSampleTime: {
                required: "Ngày lấy mẫu không được để trống",
                validDate: "Ngày phải đúng định dạng ngày/tháng/năm"
            },
            testResultsID: {
                required: "Kết quả XN sàng lọc không được để trống"
            },
            sourceSampleDate: {
                required: "Ngày gửi mẫu không được để trống",
                validDate: "Ngày phải đúng định dạng ngày/tháng/năm"
            },
            sampleReceiveTime: {
                required: "Ngày nhận mẫu không được để trống",
                validDate: "Ngày phải đúng định dạng ngày/tháng/năm"
            },
            acceptDate: {
//                validDate: "Ngày phải đúng định dạng ngày/tháng/năm"
            },
            bioNameResult1: {
                required: "Kết quả XN sinh phẩm 1 không được để trống"
            },
            bioNameResult2: {
                required: "Kết quả XN sinh phẩm 2 không được để trống"
            },
            bioNameResult3: {
                required: "Kết quả XN sinh phẩm 3 không được để trống"
            },
            bioName1: {required: "Tên sinh phẩm không được để trống"},
            bioName2: {required: "Tên sinh phẩm không được để trống"},
            bioName3: {required: "Tên sinh phẩm không được để trống"},
            earlyHivDate: {required: "Ngày XN nhiễm mới HIV không được để trống"},
            virusLoadDate: {required: "Ngày XN tải lượng virus không được để trống"},
            virusLoad: {required: "KQXN tải lượng virus không được để trống"},
            earlyHiv: {required: "KQXN nhiễm mới HIV không được để trống"},
            sampleSentSource: {maxlength: "Nơi gửi bệnh phẩm không được quá 500 ký tự"},
            serviceID: {required: "Loại dich vụ không được để trống"},
            virusLoadNumber: {digits: "Nồng độ virus phải là số nguyên dương"},
            year: {required: "Năm sinh không được để trống"}
        }

    });
    // Set UPPERCASE sau khi nhập xong mã khách hàng
    $("#code, #sourceID, #insuranceNo").blur(function () {
        $($scope.items.code).val($($scope.items.code).val().toUpperCase());
        $($scope.items.sourceID).val($($scope.items.sourceID).val().toUpperCase());
        $($scope.items.insuranceNo).val($($scope.items.insuranceNo).val().toUpperCase());
    }
    );

    $($scope.items.insurance).on("change", function () {
        if ($($scope.items.insurance).val() !== '1') {
            $($scope.items.insuranceNo).attr("disabled", "disabled").val('');
        } else {
            $($scope.items.insuranceNo).removeAttr("disabled");
        }
    });

    // Hiển thị nơi gửi mẫu bệnh phẩm theo đối tượng phạm nhân
    $($scope.items.objectGroupID).change(function () {
        if ($($scope.items.objectGroupID).val() !== '19') {
            $($scope.items.sampleSentSource).attr("disabled", "disabled").change();
            $($scope.items.sampleSentSource).val('').change();
        } else {
            $($scope.items.sampleSentSource).removeAttr("disabled").change();
        }
    });

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
            $($scope.items.currentAddressGroup).attr({disabled: "disable"}).val($("#permanentAddressGroup").val()).change();
            $($scope.items.currentAddressStreet).attr({disabled: "disable"}).val($("#permanentAddressStreet").val()).change();
            $($scope.items.currentProvinceID).attr({disabled: "disable"}).val($("#provinceID").val()).change();
            $($scope.items.currentDistrictID).attr({disabled: "disable"}).val($("#districtID").val()).change();
            $($scope.items.currentWardID).attr({disabled: "disable"}).val($("#wardID").val()).change();
            $($scope.items.isDisplayCopy).val(true);
        }
    };
});

app.controller('htc_confirm_redirect', function ($scope, $uibModalInstance, params, msg) {
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
            url: urlConfirm + "?oid=" + oids.join(","),
            success: function (resp) {
                if (resp.success) {
                    msg.success("Xác nhận trả kết quả cho cơ sở gửi mẫu thành công", function () {
//                        location.reload();
//                        $uibModalInstance.dismiss('cancel');
//                        $scope.dialogReport(urlAnswerResult + "?oid=" + oids.join(","), null, "Phiếu trả lời kết quả xét nghiệm HIV");
                        window.location = "/backend/htc-confirm/index.html?tab=result&pid=" + oids.join(",");
                    }, 2000);
                }
            }
        });

    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});


app.controller('htc_confirm_transfer', function ($scope, $uibModalInstance, params, msg) {
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
            url: urlTransferConfirm + "?oid=" + oids.join(","),
            success: function (resp) {
                if (resp.success) {
                    msg.success("Chuyển gửi GSPH thành công", function () {
//                        location.reload();
//                        $uibModalInstance.dismiss('cancel');
//                        $scope.dialogReport(urlAnswerResult + "?oid=" + oids.join(","), null, "Phiếu trả lời kết quả xét nghiệm HIV");
                        window.location = "/backend/htc-confirm/index.html";
                    }, 2000);
                }
            }
        });

    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('htc_confirm_index', function ($scope, msg, $uibModal, htcConfirmService) {
    if ($.getQueryParameters().code != null || $.getQueryParameters().fullname != null ||
            $.getQueryParameters().request_htc_time_from != null || $.getQueryParameters().request_htc_time_to != null ||
            $.getQueryParameters().confirm_time_from != null || $.getQueryParameters().confirm_time_to != null ||
            $.getQueryParameters().results_id != null || $.getQueryParameters().source_site_id != null ||
            $.getQueryParameters().source_sample_date_from != null || $.getQueryParameters().source_sample_date_to != null ||
            $.getQueryParameters().accept_date_from != null || $.getQueryParameters().accept_date_to != null ||
            $.getQueryParameters().confirm_status != null || $.getQueryParameters().early_hiv_date_from != null || $.getQueryParameters().early_hiv_date_to != null
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.isTransferOPC = false;
    $scope.init = function () {
        $scope.switchConfig();
        $scope.acceptDateFrom = $.getQueryParameters().accept_date_from;
        $scope.acceptDateTo = $.getQueryParameters().accept_date_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.earlyHivDateFrom = $.getQueryParameters().early_hiv_date_from;
        $scope.earlyHivDateTo = $.getQueryParameters().early_hiv_date_to;
        $scope.sourceSampleDateFrom = $.getQueryParameters().source_sample_date_from;
        $scope.sourceSampleDateTo = $.getQueryParameters().source_sample_date_to;
        $scope.request_htc_time_from = $.getQueryParameters().request_htc_time_from;
        $scope.request_htc_time_to = $.getQueryParameters().request_htc_time_to;
        $scope.sourceSiteId = $.getQueryParameters().source_site_id;
        $scope.$parent.select_search("#source_site_id", "Tất cả");
        if ($.getQueryParameters().pid != null) {
            setTimeout(function () {
                url = urlAnswerResult + "?oid=" + $.getQueryParameters().pid;
                $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
            }, 300);
        }

    };
    //Kiểm tra thông tin
    $scope.verifyRequest = function (oid) {
        loading.show();
        $.ajax({
            url: urlGetVerifyRequest + "?oid=" + oid,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'verifyRequest',
                        controller: 'verify_request',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    uiModals: $uibModal,
                                    location: resp.data.location,
                                    confirm: resp.data.confirm,
                                    visit: resp.data.visit,
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
    $scope.logs = function (oid, code, name) {
        htcConfirmService.logs(oid, code, name);
    };

    /**
     * //In phiếu trả kết quả xn hiv
     * 
     * @author pdThang
     * @returns {Boolean}
     */
    $scope.comfirmTicketResult = function (oid) {
        $scope.dialogReport(urlTicketConfirmResult + "?oid=" + oid, null, "Phiếu trả kết quả XN");
        $("#pdf-loading").remove();
    };

    /**
     * In phiếu gui mau xet nghiem
     * 
     * @author pdThang
     * @returns {Boolean}
     */
    $scope.answerResult = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phiếu trả kết quả");
            return false;
        }
        var isPrint = false;
        $(oids).each(function (k, v) {
            if ($("tr[id=" + v + "]").attr("data-print") == 'true') {
                isPrint = true;
            }
        });
        if (!isPrint) {
            msg.warning("Bản ghi đã chọn không thỏa mãn điều kiện in phiếu");
            return false;
        }
        let url = urlAnswerResult + "?oid=" + oids.join(",");
        if ($('#frm-print').length > 0) {
            $('#frm-print').remove();
        }
        $('body').append('<iframe id="frm-print" src="' + url + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
//        $scope.dialogReport(urlAnswerResult + "?oid=" + oids.join(","), null, "Phiếu trả lời kết quả xét nghiệm HIV");
//        $("#pdf-loading").remove();
    };

    /**
     * 
     * @returns {Boolean}
     */
    $scope.answerResultList = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để in phiếu trả kết quả");
            return false;
        }

        $scope.dialogReport(urlAnswerResultList + "?oid=" + oids.join(","), null, "Phiếu trả lời kết quả xét nghiệm HIV");
        $("#pdf-loading").remove();
    };

    /**
     * Ttra ket qua co so 
     * 
     * @author pdThang
     * @returns {Boolean}
     */
    $scope.redirectConfirm = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi để xác nhận trả kết quả");
            return false;
        }
        loading.show();
        $.ajax({
            url: urlRedirectConfirm + "?oid=" + oids.join(","),
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'htcConfirmRedirect',
                        controller: 'htc_confirm_redirect',
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

    /**
     * Auto in phiếu trả kết quả cho cơ sở
     * 
     * @author TrangBN
     * @returns {Boolean}
     */
    $scope.redirectConfirmPrint = function (oids) {
        loading.show();
        $.ajax({
            url: urlRedirectConfirm + "?oid=" + oids,
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'htcConfirmRedirect',
                        controller: 'htc_confirm_redirect',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.entities,
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

    /**
     * Tiếp nhận khách hàng 
     * 
     * @author pdThang
     * @returns {Boolean}
     */
    $scope.receivedConfirm = function () {
        var oids = $scope.getSwitch();
        if (oids == null || oids.length == 0) {
            msg.warning("Bạn chưa chọn bản ghi cần tiếp nhận");
            return false;
        }
        bootbox.confirm(
                {
                    message: "Bạn chắc chắn muốn tiếp nhận khách hàng không?",
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
                            $.ajax({
                                url: urlReceivedConfirm + "?oid=" + oids.join(","),
                                success: function (resp) {
                                    if (resp.success) {
                                        msg.success("Khách hàng đã được tiếp nhận thành công!", function () {
                                            window.location.reload();
                                        }, 2000);
                                        return false;
                                    }
                                }
                            });
                        }
                    }
                }
        );
    };

    /**
     * @author DSNAnh
     * Cập nhật kết quả
     * @param {type} oid
     * @returns {undefined}
     */
    $scope.updateResult = function (oid) {
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
                        templateUrl: 'htcConfirmUpdateResult',
                        controller: 'htc_confirm_update_result',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    staffName: resp.data.staffName,
                                    item: resp.data.model,
                                    options: resp.data.options,
                                    uiModals: $uibModal,
                                    parent: $scope.$parent,
                                    dialogReport: $scope.dialogReport
                                };
                            }
                        }
                    });
                }
            }
        });
    };

    /**
     * @author pdThang
     * Cập nhật thong tin
     * @param {type} oid
     * @returns {undefined}
     */
    $scope.actionReceivedInfo = function (oid) {
        loading.show();
        $.ajax({
            url: urlActionReceivedInfo,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'htcConfirmReceivedInfo',
                        controller: 'htc_confirm_received_info',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    dataConfirm: resp.data.dataConfirm,
                                    dataVisit: resp.data.dataVisit,
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
                        templateUrl: 'htcConfirmTransfer',
                        controller: 'htc_confirm_transfer',
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

});


/**
 * @auth pdThắng
 */
app.controller('htc_confirm_view', function ($scope, locations) {
    $scope.siteName = utils.getContentOfDefault(form.siteName, currentSideName);
    $scope.sourceSiteID = utils.getContentOfDefault(form.sourceSiteID, '');
    $scope.items = {
        siteName: "#siteName",
        sourceSiteID: "#sourceSiteID",
        sourceSiteTooltip: "#sourceSiteTooltip"
    };
    $scope.init = function () {
        //Dữ liệu địa danh
        $scope.initProvince("#provinceID", "#districtID", "#wardID");
        $scope.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");
        $("#provinceID").attr("disabled", "disabled");
        $("#districtID").attr("disabled", "disabled");
        $("#wardID").attr("disabled", "disabled");
        $("#currentProvinceID").attr("disabled", "disabled");
        $("#currentDistrictID").attr("disabled", "disabled");
        $("#currentWardID").attr("disabled", "disabled");
        $("#sourceSiteID").attr("disabled", "disabled");//hàm này dùng để sét thuộc tính bên html
        $("#visitTestCode").attr("disabled", "disabled");
        $("#testResultsID").attr("disabled", "disabled");
        // Set disable for confirm test site
        $($scope.items.siteName).attr("disabled", 'disabled');
        $($scope.items.sourceSiteTooltip).attr("data-original-title", $("#sourceSiteID option:selected").text());
        $("#bioName1").attr("disabled", "disabled");
        $("#bioName2").attr("disabled", "disabled");
        $("#bioName3").attr("disabled", "disabled");
    };

});

/**
 * @auth DSNAnh
 */
app.controller('htc_confirm_positive', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');

    $scope.init = function () {
//        $scope.$parent.select_mutiple("#services", "Tất cả");
    };

    $scope.htcConfirmPositivePdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.htcConfirmPositiveExcel();
            }
        });
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val();
    };

    $scope.htcConfirmPositiveExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.htcConfirmPositiveEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});
/**
 * @auth DSNAnh
 */
app.controller('htc_confirm_positive_distinct', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');

    $scope.init = function () {
//        $scope.$parent.select_mutiple("#services", "Tất cả");
    };

    $scope.htcConfirmPositivePdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.htcConfirmPositiveExcel();
            }
        });
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val();
    };

    $scope.htcConfirmPositiveExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.htcConfirmPositiveEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

});
/**
 * @author DSNAnh
 * Cập nhật kết quả
 * @returns 
 */
app.controller('htc_confirm_update_result', function ($scope, $uibModalInstance, msg, params) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.uiModals = params.uiModals;
    $scope.dialogReport = params.dialogReport;
    $scope.parent = params.parent;

    $scope.init = function () {
        $('#virusLoad').append($('<option>', {value: '', text: 'Chọn kết quả XN tải lượng virus'}));
        $('#earlyHiv').append($('<option>', {value: '', text: 'Chọn kết quả XN nhiễm mới'}));
        $('#resultsID').append($('<option>', {value: '', text: 'Chọn kết quả XN khẳng định'}));
        $('#bioNameResult1').append($('<option>', {value: '', text: 'Chọn kết quả XN SP1'}));
        $('#bioName1').append($('<option>', {value: '', text: 'Chọn tên SP1'}));
        $('#bioNameResult2').append($('<option>', {value: '', text: 'Chọn kết quả XN SP2'}));
        $('#bioName2').append($('<option>', {value: '', text: 'Chọn tên SP2'}));
        $('#bioNameResult3').append($('<option>', {value: '', text: 'Chọn kết quả XN SP3'}));
        $('#bioName3').append($('<option>', {value: '', text: 'Chọn tên SP3'}));
        $('#earlyBioName').append($('<option>', {value: '', text: 'Chọn tên sinh phẩm nhiễm mới'}));
        $('#earlyDiagnose').append($('<option>', {value: '', text: 'Chọn kết luận chẩn đoán nhiễm mới'}));

        $scope.parent.select_search("#earlyBioName", "Chọn tên sinh phẩm nhiễm mới");

    };

    $scope.model = {
        sampleSentSource: params.item.sampleSentSource == null || params.item.sampleSentSource == '' ? "" : params.item.sampleSentSource,
        objectGroupID: params.item.objectGroupID == null || params.item.objectGroupID == '' ? "" : params.item.objectGroupID,
        earlyHivDate: utils.timestampToStringDate(params.item.earlyHivDate == null || params.item.earlyHivDate == '' || params.item.earlyHivDate == 0 ? '' : params.item.earlyHivDate),
        virusLoadDate: utils.timestampToStringDate(params.item.virusLoadDate == null || params.item.virusLoadDate == '' || params.item.virusLoadDate == 0 ? '' : params.item.virusLoadDate),
        sampleReceiveTime: utils.timestampToStringDate(params.item.sampleReceiveTime == null || params.item.sampleReceiveTime == '' || params.item.sampleReceiveTime == 0 ? '' : params.item.sampleReceiveTime),
        sourceSampleDate: utils.timestampToStringDate(params.item.sourceSampleDate == null || params.item.sourceSampleDate == '' || params.item.sourceSampleDate == 0 ? '' : params.item.sourceSampleDate),
        testStaffId: params.staffName == null || params.staffName == '' ? "" : params.staffName,
        bioName1: params.item.bioName1 == null || params.item.bioName1 == '' ? "" : params.item.bioName1,
        bioName2: params.item.bioName2 == null || params.item.bioName2 == '' ? "" : params.item.bioName2,
        bioName3: params.item.bioName3 == null || params.item.bioName3 == '' ? "" : params.item.bioName3,
        bioNameResult1: params.item.bioNameResult1 == null || params.item.bioNameResult1 == '' ? "" : params.item.bioNameResult1,
        bioNameResult2: params.item.bioNameResult2 == null || params.item.bioNameResult2 == '' ? "" : params.item.bioNameResult2,
        bioNameResult3: params.item.bioNameResult3 == null || params.item.bioNameResult3 == '' ? "" : params.item.bioNameResult3,
        code: params.item.code == null || params.item.code == '' ? params.item.sourceID : params.item.code,
        confirmTime: utils.timestampToStringDate(params.item.confirmTime == null || params.item.confirmTime == '' || params.item.confirmTime == 0 ? '' : params.item.confirmTime),
        resultsID: params.item.resultsID == null || params.item.resultsID == '' ? "" : params.item.resultsID,
        virusLoad: params.item.virusLoad == null || params.item.virusLoad == '' ? "" : params.item.virusLoad,
        earlyHiv: params.item.earlyHiv == null || params.item.earlyHiv == '' ? "" : params.item.earlyHiv,
        earlyBioName: params.item.earlyBioName == null || params.item.earlyBioName == '' ? "" : params.item.earlyBioName,
        virusLoadNumber: params.item.virusLoadNumber == null || params.item.virusLoadNumber == '' ? "" : params.item.virusLoadNumber,
        earlyDiagnose: params.item.earlyDiagnose == null || params.item.earlyDiagnose == '' ? "" : params.item.earlyDiagnose,
        sampleSaveCode: params.item.sampleSaveCode == null || params.item.sampleSaveCode == '' ? "" : params.item.sampleSaveCode
    };

    $scope.answerResult = function () {

        loading.show();

        $.ajax({
            url: urlUpdateResult + "?oid=" + $scope.item.id,
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
//                            location.reload();
                        }, 2000);
                        $uibModalInstance.dismiss('cancel');
                        $.ajax({
                            url: urlRedirectConfirm + "?oid=" + $scope.item.id,
                            method: 'GET',
                            success: function (resp) {
                                loading.hide();
                                if (resp.success) {
                                    $scope.uiModals.open({
                                        animation: true,
                                        backdrop: 'static',
                                        templateUrl: 'htcConfirmRedirect',
                                        controller: 'htc_confirm_redirect',
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

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: urlUpdateResult + "?oid=" + $scope.item.id,
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

    // Clear data khi chọn kết quả xét nghiệm khẳng định (popup cập nhật kết quả KĐ)
    $scope.changeResultID = function () {
        if ($scope.model.resultsID !== '2') {
            $scope.model.earlyHiv = "";
            $scope.model.virusLoad = "";
            $scope.model.earlyHivDate = "";
            $scope.model.virusLoadDate = "";
        }
    };

    $scope.changeVirusLoadNumber = function () {
        console.log("xx" + $scope.model.virusLoadNumber);
        if ($scope.model.virusLoadNumber > 0 && $scope.model.virusLoadNumber < 200) {
            $scope.model.virusLoad = "2";
        } else if ($scope.model.virusLoadNumber >= 200 && $scope.model.virusLoadNumber <= 1000) {
            $scope.model.virusLoad = "3";
        } else if ($scope.model.virusLoadNumber > 1000) {
            $scope.model.virusLoad = "4";
        } else {
            $scope.model.virusLoad = "";
        }
    };
//    $scope.changeEarlyHiv = function () {
//        if ($scope.model.earlyHiv === '3' || $scope.model.earlyHiv === '') {
//            $scope.model.earlyHivDate = "";
//        }
//    };
//
//    $scope.changeVirusLoad = function () {
//        if ($scope.model.virusLoad === '5' || $scope.model.virusLoad === '') {
//            $scope.model.virusLoadDate = "";
//        }
//    };
});

/**
 * @author pdThang
 * Tiếp nhận thông tin
 * @returns 
 */
app.controller('htc_confirm_received_info', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.dataConfirm = params.dataConfirm;
    $scope.dataVisit = params.dataVisit;
    $scope.errors = {};

    $scope.model = {//khai báo cả 1 đối tượng mới trong js
        feedbackMessage: params.feedbackMessage == null || params.feedbackMessage == '' ? "" : params.feedbackMessage,
    };

    $scope.ok = function (accept) {
        $scope.errors = {};
        if ($scope.model.feedbackMessage == '') {
            $scope.errors.feedbackMessage = "Nội dung không được để trống";
            return false;
        }
        loading.show();
        // khai báo 1 thuộc tính mới mới trong js
        $scope.model.accept = accept;
        $.ajax({
            url: urlActionReceivedInfoUpdate + "?oid=" + $scope.dataConfirm.id,
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

app.controller('verify_request', function ($scope, $uibModalInstance, params, msg) {
    $scope.uiModals = params.uiModals;
    $scope.confirm = params.confirm;
    $scope.location = params.location;
    $scope.visit = params.visit;
    $scope.options = params.options;
    var riskBehaviorID = [];
    var riskBehavior = '';

    for (var s in params.visit.riskBehaviorID) {
        riskBehaviorID.push(params.options['risk-behavior'][params.visit.riskBehaviorID[s]]);
    }
    riskBehavior = riskBehaviorID.join(',');

    $scope.model = {
        cause: '',
        riskBehavior: riskBehavior,
        sourceSampleDate: utils.timestampToStringDate(params.confirm.sourceSampleDate == null || params.confirm.sourceSampleDate == '' || params.confirm.sourceSampleDate == 0 ? '' : params.confirm.sourceSampleDate),
        sampleSentDate: utils.timestampToStringDate(params.visit.sampleSentDate == null || params.visit.sampleSentDate == '' || params.visit.sampleSentDate == 0 ? '' : params.visit.sampleSentDate),
        confirmTimeVisit: utils.timestampToStringDate(params.visit.confirmTime == null || params.visit.confirmTime == '' || params.visit.confirmTime == 0 ? '' : params.visit.confirmTime),
        confirmTime: utils.timestampToStringDate(params.confirm.confirmTime == null || params.confirm.confirmTime == '' || params.confirm.confirmTime == 0 ? '' : params.confirm.confirmTime),
    };
    $scope.ok = function (confirmID, htcID, verify) {
        $scope.errors = {};
        if ($scope.model.cause == '') {
            $scope.errors.cause = "Lý do phản hồi không được để trống";
            return false;
        }
        if ($scope.model.cause != '' && $scope.model.cause.length > 255) {
            $scope.errors.cause = "Lý do phản hồi không được quá 255 ký tự";
            return false;
        }
        loading.show();
        $.ajax({
            url: urlVerifyRequest + "?confirm_id=" + confirmID + "&htc_id=" + htcID + "&verify=" + verify + "&cause=" + $scope.model.cause,
            method: 'POST',
            success: function (resp) {
                loading.hide();
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
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
});


app.controller('htc_confirm_early', function ($scope, locations) {

    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.earlyHiv = utils.getContentOfDefault(form.earlyHiv, '');
    $scope.virusLoad = utils.getContentOfDefault(form.virusLoad, '');
    $scope.earlyHivDate = utils.getContentOfDefault(form.earlyHivDate, '');
    $scope.virusLoadDate = utils.getContentOfDefault(form.virusLoadDate, '');
    $scope.virusLoadNumber = utils.getContentOfDefault(form.virusLoadNumber, '');
    $scope.earlyBioName = utils.getContentOfDefault(form.earlyBioName, '');
    $scope.earlyDiagnose = utils.getContentOfDefault(form.earlyDiagnose, '');



    $scope.items = {
        id: "#ID",
        earlyHiv: "#earlyHiv",
        virusLoad: "#virusLoad",
        earlyHivDate: "#earlyHivDate",
        virusLoadDate: "#virusLoadDate",
        virusLoadNumber: "#virusLoadNumber",
        earlyBioName: "#earlyBioName",
        earlyDiagnose: "#earlyDiagnose"

    };

    $scope.init = function () {
        $("#virusLoadNumber").on('change', function () {
            if ($scope.virusLoadNumber > 0 && $scope.virusLoadNumber < 200) {
                $("#virusLoad").val('2').change();
            } else if ($scope.virusLoadNumber >= 200 && $scope.virusLoadNumber <= 1000) {
                $("#virusLoad").val('3').change();
            } else if ($scope.virusLoadNumber > 1000) {
                $("#virusLoad").val('4').change();
            } else {
                $("#virusLoad").val('').change();
            }
        });
    };

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            earlyHivDate: {
                required: function () {
                    return $($scope.items.earlyHiv).val().length > 0 && $($scope.items.virusLoad).val() !== "3";
                },
            },
            earlyHiv: {
                required: function () {
                    return $($scope.items.earlyHivDate).val().length > 0;
                }
            },
            virusLoadDate: {
                required: function () {
                    return $($scope.items.virusLoad).val().length > 0 && $($scope.items.virusLoad).val() !== "5";
                },
            },
            virusLoad: {
                required: function () {
                    return $($scope.items.virusLoadDate).val().length > 0;
                }
            },
            virusLoadNumber: {
                digits: true,
            }
        },
        messages: {
            earlyHivDate: {required: "Ngày XN nhiễm mới HIV không được để trống"},
            virusLoadDate: {required: "Ngày XN tải lượng virus không được để trống"},
            virusLoad: {required: "KQXN tải lượng virus không được để trống"},
            earlyHiv: {required: "KQXN nhiễm mới HIV không được để trống"},
            virusLoadNumber: {digits: "Nồng độ virus phải là số nguyên dương"}
        }
    });
});