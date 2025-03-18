app.service('laytestService', function ($uibModal) {
    var elm = this;
    elm.logs = function (oid, code, name) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'laytestLogs',
            controller: 'laytest_log',
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
app.controller('laytest_view', function ($scope) {
    $scope.riskBehaviorID = utils.getContentOfDefault(form.riskBehaviorID, '');
    $scope.advisoryeTime = utils.getContentOfDefault(form.advisoryeTime, '');
    $scope.testResultsID = utils.getContentOfDefault(form.testResultsID, '');
    $scope.isAgreeTest = utils.getContentOfDefault(form.isAgreeTest, '');
    $scope.siteVisitID = utils.getContentOfDefault(form.siteVisitID, '');
    $scope.siteConfirmTest = utils.getContentOfDefault(form.siteConfirmTest, '');
    $scope.controlLine = utils.getContentOfDefault(form.controlLine, '');
    $scope.testLine = utils.getContentOfDefault(form.testLine, '');
    $scope.confirmResultsID = utils.getContentOfDefault(form.confirmResultsID, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.exchangeStatus = utils.getContentOfDefault(form.exchangeStatus, '');
    $scope.referralSource = utils.getContentOfDefault(form.referralSource, '');
    $scope.patientIDAuthen = utils.getContentOfDefault(form.patientIDAuthen, '');
    $scope.exchangeTime = utils.getContentOfDefault(form.exchangeTime, '');
    $scope.registerTherapyTime = utils.getContentOfDefault(form.registerTherapyTime, '');

    $scope.items = {
        riskBehaviorID: "#riskBehaviorID",
        siteName: "#siteName",
        siteVisitTooltip: "#siteVisitTooltip",
        siteVisitID: "#siteVisitID",
        siteConfirmTest: "#siteConfirmTest"
    };
    $scope.init = function () {
        $scope.$parent.select_mutiple($scope.items.riskBehaviorID, "Chọn hành vi nguy cơ lây nhiễm");
        $scope.initProvince("#permanentProvinceID", "#permanentDistrictID", "#permanentWardID");
        $("#permanentProvinceID").attr("disabled", "disabled");
        $("#permanentDistrictID").attr("disabled", "disabled");
        $("#permanentWardID").attr("disabled", "disabled");
        $("#siteVisitTooltip").attr("data-original-title", $("#siteVisitID").val());
        $scope.initProvince("#currentProvinceID", "#currentDistrictID", "#currentWardID");
        $("#currentProvinceID").attr("disabled", "disabled");
        $("#currentDistrictID").attr("disabled", "disabled");
        $("#currentWardID").attr("disabled", "disabled");
        $("#riskBehaviorID").attr("disabled", "disabled");
        $("#siteVisitID").attr("disabled", "disabled");
        $("#siteConfirmTest").attr("disabled", "disabled");

        // Trường hợp hiển thị cập nhật không có thông tin địa danh
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $($scope.items.permanentProvinceID).val('').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $($scope.items.currentProvinceID).val('').change();
        }

        $scope.initSiteVisitTest();
        $scope.initSiteConfirmTest();
        $scope.customerCount($(".fieldwrapper")); // Đếm tổng số khách hàng

        // Disable bảng thông tin người được giới thiệu
        $(".tbl_agency").each(function () {
            $(this).attr("disabled", "disabled");
        });
        
        $scope.emptyNotify();
        
    };

    $scope.emptyNotify = function () {
        if (typeof $(".fieldwrapper") === 'undefined' || $(".fieldwrapper").size() === 0) {
            $("#listPartner").append("<tr class=\"text-center empty-notify text-danger\"><td colspan=\"6\">Chưa có thông tin bạn tình bạn chích được giới thiệu</td></tr>");
        } else {
            $(".empty-notify").remove();
        }
    }

    // Đếm tổng số người được giới thiệu
    $scope.customerCount = function (a) {
        if (typeof a !== 'undefined' && a.length > 0) {
            $("#total-customer").text("Tổng số bạn tình, bạn chích được giới thiệu: " + a.length);
        } else {
            $("#total-customer").text("");
        }
    };

    // Khởi tạo cơ sở xn khẳng định
    $scope.initSiteConfirmTest = function () {
        $.ajax({
            url: '/service/laytest/site-confirmatory-test.json',
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
            if (result.data.length > 0) {
                $($scope.items.siteConfirmTest).append(new Option("Chọn cơ sở xét nghiệm", "", i == 0, false));
            }

            for (var i = 0; i < result.data.length; i++) {
                var option = new Option(result.data[i].name, result.data[i].id, false, false);
                $($scope.items.siteConfirmTest).append(option);
            }
            $($scope.items.siteConfirmTest).val(utils.getContentOfDefault(form.siteConfirmTest, '')).change();
            $($scope.items.siteConfirmTest).trigger('change');
        });
    };

    $scope.initSiteVisitTest = function () {
        $.ajax({
            url: '/service/laytest/get-site-htc.json',
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
            if (result.data.length > 0) {
                $($scope.items.siteVisitID).append(new Option("Chọn cơ sở chuyển đến", "", i == 0, false));
            }

            for (var i = 0; i < result.data.length; i++) {
                var option = new Option(result.data[i].name, result.data[i].id, false, false);
                $($scope.items.siteVisitID).append(option);
            }
            $($scope.items.siteVisitID).val(utils.getContentOfDefault(form.siteVisitID, '')).change();
            $($scope.items.siteVisitID).trigger('change');
        });
    };

});

app.controller('laytest_log', function ($scope, $uibModalInstance, params, msg) {
    $scope.model = {staffID: 0, htcLaytestID: params.oid, code: params.code, patientName: params.name};
    loading.show();
    $scope.list = function () {
        $.ajax({
            url: '/service/laytest/log.json',
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
            url: '/service/laytest/log-create.json',
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

app.controller('htc_laytest_new', function ($scope, msg) {

    $scope.isCopyPermanentAddress = typeof (form.isDisplayCopy) == 'undefined' || form.isDisplayCopy == null ? true : form.isDisplayCopy;
    $scope.siteVisitID = utils.getContentOfDefault(form.siteVisitID, '');
    $scope.isAgreeTest = utils.getContentOfDefault(form.isAgreeTest, '');
    $scope.testResultsID = utils.getContentOfDefault(form.testResultsID, '');
    $scope.hasTestBefore = utils.getContentOfDefault(form.hasTestBefore, '');
    $scope.serviceID = utils.getContentOfDefault(form.serviceID, '');
    $scope.mostRecentTest = utils.getContentOfDefault(form.mostRecentTest, '');
    $scope.jobID = utils.getContentOfDefault(form.jobID, '');
    $scope.objectGroupID = utils.getContentOfDefault(form.objectGroupID, '');
    $scope.id = utils.getContentOfDefault(form.id, '');
    $scope.sampleSentDate = utils.getContentOfDefault(form.sampleSentDate, '');
    $scope.hasHealthInsurance = utils.getContentOfDefault(form.hasHealthInsurance, '');
    $scope.riskBehaviorID = utils.getContentOfDefault(form.riskBehaviorID, '');
    $scope.permanentProvinceID = utils.getContentOfDefault(form.permanentProvinceID, '');
    $scope.permanentDistrictID = utils.getContentOfDefault(form.permanentDistrictID, '');
    $scope.permanentWardID = utils.getContentOfDefault(form.permanentWardID, '');
    $scope.patientName = utils.getContentOfDefault(form.patientName, '');
    $scope.patientIDAuthen = utils.getContentOfDefault(form.patientIDAuthen, '');
    $scope.code = utils.getContentOfDefault(form.code, '');
    $scope.controlLine = utils.getContentOfDefault(form.controlLine, '');
    $scope.testLine = utils.getContentOfDefault(form.testLine, '');
    $scope.exchangeTime = utils.getContentOfDefault(form.exchangeTime, '');
    $scope.registerTherapyTime = utils.getContentOfDefault(form.registerTherapyTime, '');
    $scope.referralSource = utils.getContentOfDefault(form.referralSource, '');
    $scope.note = utils.getContentOfDefault(form.note, '');
    $scope.exchangeStatus = utils.getContentOfDefault(form.exchangeStatus, '');
    $scope.confirmTime = utils.getContentOfDefault(form.confirmTime, '');
    $scope.confirmResultsID = utils.getContentOfDefault(form.confirmResultsID, '');
    $scope.advisoryeTime = utils.getContentOfDefault(form.advisoryeTime,
            ('0' + (new Date()).getDate()).slice(-2) + '/' + ('0' + ((new Date()).getMonth() + 1)).slice(-2) + '/' + (new Date()).getFullYear());

    // Thêm custom validate date
    $.validator.addMethod("validDate", function (value, element) {
        return value.match(/(?:0[1-9]|[12][0-9]|3[01])\/(?:0[1-9]|1[0-2])\/(?:19|20\d{2})/);
    }, "Nhập đúng định dạng theo dd/mm/yyyy");

    $.validator.addMethod("validCode", function (value) {
        return value.match(/[^x]\d$/);
    }, "Mã khách hàng không đúng định dạng");

    $scope.items = {
        ID: "#ID",
        serviceID: "#serviceID",
        raceID: "#raceID",
        patientName: "#patientName",
        patientIDAuthen: "#patientIDAuthen",
        genderID: "#genderID",
        hasHealthInsurance: "#hasHealthInsurance",
        healthInsuranceNo: "#healthInsuranceNo",
        currentProvinceID: "#currentProvinceID",
        code: "#code",
        currentAddress: "#currentAddress",
        currentAddressGroup: "#currentAddressGroup",
        currentAddressStreet: "#currentAddressStreet",
        permanentAddressGroup: "#permanentAddressGroup",
        permanentAddressStreet: "#permanentAddressStreet",
        currentDistrictID: "#currentDistrictID",
        currentWardID: "#currentWardID",
        isDisplayCopy: "#isDisplayCopy",
        permanentProvinceID: "#permanentProvinceID",
        permanentDistrictID: "#permanentDistrictID",
        permanentAddress: "#permanentAddress",
        permanentWardID: "#permanentWardID",
        yearOfBirth: "#yearOfBirth",
        objectGroupID: "#objectGroupID",
        jobID: "#jobID",
        riskBehaviorID: "#riskBehaviorID",
        isAgreeTest: "#isAgreeTest",
        testResultsID: "#testResultsID",
        advisoryeTime: "#advisoryeTime",
        siteVisitID: "#siteVisitID",
        hasTestBefore: "#hasTestBefore",
        mostRecentTest: "#mostRecentTest",
        sampleSentDate: "#sampleSentDate",
        siteConfirmTest: "#siteConfirmTest",
        exchangeTime: "#exchangeTime",
        arrivalSite: "#arrivalSite",
        controlLine: "#controlLine",
        testLine: "#testLine",
        referralSource: "#referralSource",
        youInjectCode: "#youInjectCode",
        registerTherapyTime: "#registerTherapyTime",
        registeredTherapySite: "#registeredTherapySite",
        exchangeStatus: "#exchangeStatus",
        confirmResultsID: "#confirmResultsID",
        confirmTime: "#confirmTime",
        bioName: "#bioName"
        
    };

    $scope.init = function () {
        // Style cho dropdown
        $scope.$parent.select_search($scope.items.raceID, "Chọn dân tộc");
        $scope.$parent.select_search($scope.items.genderID, "Chọn giới tính");
        $scope.$parent.select_search($scope.items.serviceID, "Chọn dịch vụ");
        $scope.$parent.select_search($scope.items.hasHealthInsurance, "Chọn câu trả lời");
        $scope.$parent.select_search($scope.items.currentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.currentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.currentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.permanentProvinceID, "Chọn tỉnh thành");
        $scope.$parent.select_search($scope.items.patientIDAuthen, "Lựa chọn");
        $scope.$parent.select_search($scope.items.permanentDistrictID, "Chọn quận huyện");
        $scope.$parent.select_search($scope.items.permanentWardID, "Chọn phường xã");
        $scope.$parent.select_search($scope.items.objectGroupID, "Chọn nhóm đối tượng");
        $scope.$parent.select_search($scope.items.jobID, "Chọn công việc");
        $scope.$parent.select_search($scope.items.testResultsID, "Kết quả xét nghiệm sàng lọc");
        $scope.$parent.select_search($scope.items.isAgreeTest, "Đồng ý tiếp tục xét nghiệm");
        $scope.$parent.select_search($scope.items.siteVisitID, "Đồng ý tiếp tục xét nghiệm");
        $scope.$parent.select_search($scope.items.hasTestBefore, "Lựa chọn");
        $scope.$parent.select_search($scope.items.mostRecentTest, "Lựa chọn");
        $scope.$parent.select_search($scope.items.siteConfirmTest, "Lựa chọn cơ sở");
        $scope.$parent.select_search($scope.items.bioName, "Lựa chọn sinh phẩm");

        $scope.$parent.select_mutiple($scope.items.riskBehaviorID, "Chọn hành vi nguy cơ lây nhiễm");
        
        // Ẩn giá trị kết quả xét nghiệm 
        $("#testResultsID option[value='4']").remove();

        //Dữ liệu địa danh
        $scope.initProvince($scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
        $scope.initProvince($scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);
        $scope.initProvince($scope.items.exchangeProvinceID, $scope.items.exchangeDistrictID, null);
        $scope.initProvince($scope.items.therapyRegistProvinceID, $scope.items.therapyRegistDistrictID, null);

        // Trường hợp hiển thị cập nhật không có thông tin địa danh
        if ($scope.id !== '' && $scope.permanentProvinceID === '') {
            $($scope.items.permanentProvinceID).val('').change();
        }

        if ($scope.id !== '' && $scope.currentProvinceID === '') {
            $($scope.items.currentProvinceID).val('').change();
        }

//        $scope.addressAutocomplete($scope.items.permanentAddress, $scope.items.permanentProvinceID, $scope.items.permanentDistrictID, $scope.items.permanentWardID);
//        $scope.addressAutocomplete($scope.items.currentAddress, $scope.items.currentProvinceID, $scope.items.currentDistrictID, $scope.items.currentWardID);

        //Load cơ sở xét nghiệm cố định
        $scope.initSiteVisitTest();
        $($scope.items.serviceID).attr("disabled", "disabled");


        //event copy
        $("#permanentProvinceID, #permanentAddress, #currentAddressGroup, #currentAddressStreet, #permanentDistrictID, #permanentWardID").change(function () {
            if ($scope.isCopyPermanentAddress) {
                $($scope.items.currentAddress).val($($scope.items.permanentAddress).val());
                $($scope.items.currentAddressGroup).val($($scope.items.permanentAddressGroup).val());
                $($scope.items.currentAddressStreet).val($($scope.items.permanentAddressStreet).val());
                $($scope.items.currentProvinceID).val($($scope.items.permanentProvinceID).val());
                $($scope.items.currentDistrictID).val($($scope.items.permanentDistrictID).val());
                $($scope.items.currentWardID).val($($scope.items.permanentWardID).val());
            }
        });

        // Set UPPERCASE sau khi nhập xong mã khách hàng
        $("#code").blur(function () {
            $($scope.items.code).val($($scope.items.code).val().toUpperCase());
        }
        );

        if ($scope.id !== "" && $($scope.items.isDisplayCopy).val() === "") {
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
        if ($($scope.items.isAgreeTest).val() === '' || $($scope.items.testResultsID).val() !== '2') {
            $($scope.items.confirmTime).val("").change();
            $($scope.items.confirmTime).attr("disabled", "disabled");

            $($scope.items.confirmResultsID).val("").change();
            $($scope.items.confirmResultsID).attr("disabled", "disabled");

            $($scope.items.siteConfirmTest).val("").change();
            $($scope.items.siteConfirmTest).attr("disabled", "disabled");
            
            $($scope.items.exchangeTime).val("").change();
            $($scope.items.exchangeTime).attr("disabled", "disabled");

            $($scope.items.arrivalSite).val("").change();
            $($scope.items.arrivalSite).attr("disabled", "disabled");

            $($scope.items.exchangeStatus).val("").change();
            $($scope.items.exchangeStatus).attr("disabled", "disabled");
        } 
        if ($($scope.items.testResultsID).val() !== '2') {
            $($scope.items.isAgreeTest).attr("disabled", "disabled");
            $($scope.items.siteVisitID).attr("disabled", "disabled");
            $($scope.items.confirmTime).attr("disabled", "disabled");
            $($scope.items.confirmResultsID).attr("disabled", "disabled");
            $($scope.items.siteConfirmTest).attr("disabled", "disabled");
        }

        // set max length
        $($scope.items.code).attr('maxlength', $scope.code.length);

        // Clear data theo dropdown
        $($scope.items.testResultsID).on("change", function () {
            if ($($scope.items.testResultsID).val() !== '2') {
                $($scope.items.isAgreeTest).val("").change();
                $($scope.items.siteVisitID).val("").change();
                
                $($scope.items.confirmTime).val("").change();
                $($scope.items.confirmTime).attr("disabled", "disabled");
                
                $($scope.items.confirmResultsID).val("").change();
                $($scope.items.confirmResultsID).attr("disabled", "disabled");
                
                $($scope.items.siteConfirmTest).val("").change();
                $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                
                $($scope.items.exchangeTime).val("").change();
                $($scope.items.exchangeTime).attr("disabled", "disabled");

                $($scope.items.arrivalSite).val("").change();
                $($scope.items.arrivalSite).attr("disabled", "disabled");

                $($scope.items.exchangeStatus).val("").change();
                $($scope.items.exchangeStatus).attr("disabled", "disabled");
            }
        });

        $($scope.items.isAgreeTest).on("change", function () {
            if ($($scope.items.isAgreeTest).val() === '' && $($scope.items.testResultsID).val() !== '') {
                $($scope.items.siteVisitID).val("").change();
                $($scope.items.siteVisitID).attr("disabled", "disabled");
                
                $($scope.items.confirmTime).val("").change();
                $($scope.items.confirmTime).attr("disabled", "disabled");
                
                $($scope.items.confirmResultsID).val("").change();
                $($scope.items.confirmResultsID).attr("disabled", "disabled");
                
                $($scope.items.siteConfirmTest).val("").change();
                $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                
                $($scope.items.exchangeTime).val("").change();
                $($scope.items.exchangeTime).attr("disabled", "disabled");

                $($scope.items.arrivalSite).val("").change();
                $($scope.items.arrivalSite).attr("disabled", "disabled");

                $($scope.items.exchangeStatus).val("").change();
                $($scope.items.exchangeStatus).attr("disabled", "disabled");
            }
        });

        //Load cơ sở xét nghiệm khẳng định
        $scope.initSiteConfirmTest();

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));

        if ($($scope.items.controlLine).val() !== null && $($scope.items.controlLine).val() === '1') {
            $($scope.items.testLine).removeAttr("disabled").change();
        }

        // Thông báo nếu không có người được giới thiệu
        $scope.emptyNotify();
        
        // Hiện thông tin bạn tình bạn chích theo kết quả xét nghiệm dương tính
        if ($($scope.items.confirmResultsID).val() !== "2") {
           $(".tbl_agency").attr("disabled", "disabled");
        } else {
            $(".tbl_agency").removeAttr()("disabled");
        }
    };

    // Phần BHYT
    $($scope.items.hasHealthInsurance).on("change", function () {
        if ($($scope.items.hasHealthInsurance).val() !== "1") {
            $($scope.items.healthInsuranceNo).val("").change();
        }
    });

    // Phần nguồn tu vấn xét nghiệm
    $($scope.items.referralSource).on("change", function () {
        if ($($scope.items.referralSource).val() !== "2") {
            $($scope.items.youInjectCode).val("").change();
        }
    });

    // Phần hỏi đã test trước đó
    $($scope.items.hasTestBefore).on("change", function () {
        if ($($scope.items.hasTestBefore).val() !== "1") {
            $($scope.items.mostRecentTest).val("").change();
            $($scope.items.mostRecentTest).attr("disabled", "disabled");
        } else {
            $($scope.items.mostRecentTest).removeAttr("disabled");
        }
    });

    // Phần hỏi đã test trước đó
    $($scope.items.exchangeStatus).on("change", function () {
        if ($($scope.items.exchangeStatus).val() !== "1") {
            $($scope.items.registerTherapyTime).val("").change();
            $($scope.items.registeredTherapySite).val("").change();
        }
    });
    
    // Clear data phần bạn tình bạn chích theo kqxn kđ
    $($scope.items.confirmResultsID).on("change", function () {
        if ($($scope.items.confirmResultsID).val() !== "2") {
           $(".tbl_agency").val('').change();
           $(".tbl_agency").attr("disabled", "disabled");
        } else {
            $(".tbl_agency").removeAttr("disabled");
        }
    });

    // Thêm dòng động đối tượng bạn tình bạn chích
    $("#add").click(function () {

        // Get row index
        var numberCustomer = $(".fieldwrapper").length;
        var rowIndex = numberCustomer;

        var lastField = $("#listPartner tr:last");
        var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
        var fieldWrapper = $("<tr class=\"fieldwrapper\" id=\"field" + intId + "\"/>");
        fieldWrapper.data("idx", intId);
        var fOrder = $("<td class=\"text-center order vertical-align-middle\">" + (numberCustomer + 1) + "</td>");
        var fName = $("<td class=\"text-center vertical-align-top\"><input type=\"text\" class=\"form-control tbl_agency\" id=\"laytestAgencies" + rowIndex + ".fullname \" name=\"laytestAgencies[" + rowIndex + "].fullname\"/></td>");
        var fAddress = $("<td class=\"text-center vertical-align-top\"><input type=\"text\" class=\"form-control tbl_agency\" id=\"laytestAgencies" + rowIndex + ".address \" name=\"laytestAgencies[" + rowIndex + "].address\"/></td>");
        var fPhoneNumer = $("<td class=\"text-center vertical-align-top\"><input type=\"text\" class=\"form-control tbl_agency\" id=\"laytestAgencies" + rowIndex + ".phone \" name=\"laytestAgencies[" + rowIndex + "].phone\"/></td>");
        var fNotify = $("<td class=\"text-center notifyListTD vertical-align-top\"><select class=\"notifyList form-control tbl_agency\" id=\"laytestAgencies" + rowIndex + ".alertType \" name=\"laytestAgencies[" + rowIndex + "].alertType\"></select></td>");
        var fAgreeTest = $("<td class=\"text-center notifyListTD vertical-align-top\"><select class=\"notifyList form-control tbl_agency\" id=\"laytestAgencies" + rowIndex + ".isAgreePreTest \" name=\"laytestAgencies[" + rowIndex + "].isAgreePreTest\"></select></td>");
        var removeButton = $("<td class=\"text-center vertical-align-middle\"><button type=\"button\" class=\"btn btn-danger btn-xs delete-agency\" > <i class=\"fa fa fa-trash\" ></i>&nbsp;Xóa</button></td>");

        removeButton.click(function () {
            $(this).parent().remove();
            // Đếm tổng số khách hàng
            $scope.customerCount($(".fieldwrapper"));
            $scope.indexForAgency($(".fieldwrapper"));
            $scope.emptyNotify();
        });

        fieldWrapper.append(fOrder);
        fieldWrapper.append(fName);
        fieldWrapper.append(fAddress);
        fieldWrapper.append(fPhoneNumer);
        fieldWrapper.append(fNotify);
        fieldWrapper.append(fAgreeTest);
        fieldWrapper.append(removeButton);
        $("#listPartner").append(fieldWrapper);

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));
        $scope.emptyNotify();

        $scope.initNotification($("[name='" + "laytestAgencies[" + rowIndex + "].alertType" + "']"));
        $scope.initAgreePreTest($("[name='" + "laytestAgencies[" + rowIndex + "].isAgreePreTest" + "']"));
    });

    $scope.emptyNotify = function () {
        if (typeof $(".fieldwrapper") === 'undefined' || $(".fieldwrapper").size() === 0) {
            if ($(".empty-notify") === 'undefined' || $(".empty-notify").size() === 0) {
                $("#listPartner").append("<tr class=\"text-center empty-notify text-danger\"><td colspan=\"7\">Chưa có thông tin bạn tình bạn chích được giới thiệu</td></tr>");
            }
        } else {
            $(".empty-notify").remove();
        }
    };

    $(".delete-agency").click(function () {
        $(this).parent().parent().remove();

        // Đếm tổng số khách hàng
        $scope.customerCount($(".fieldwrapper"));
        $scope.indexForAgency($(".fieldwrapper"));
        $scope.emptyNotify();
    });

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

    // Khởi tạo đồng ý xét nghiệm
    $scope.initAgreePreTest = function (a) {
        a.empty();
        a.append(new Option("Chọn câu trả lời", ""));
        a.append(new Option("Có", 1));
        a.append(new Option("Không", 0));

    }

    // Đếm tổng số người được giới thiệu
    $scope.customerCount = function (a) {
        if (typeof a !== 'undefined' && a.length > 0) {
            $("#total-customer").text("Tổng số bạn tình, bạn chích được giới thiệu: " + a.length);
        } else {
            $("#total-customer").text("");
        }
    };

    // Đánh lại thứ tự bạn tình bạn chích
    $scope.indexForAgency = function (a) {
        var index = 1;
        a.each(function () {
            $(this).children(".order").text(index);
            index += 1;
        });
    };

    // Khởi tạo cơ sở xn khẳng định
    $scope.initSiteConfirmTest = function () {
        $.ajax({
            url: '/service/laytest/site-confirmatory-test.json',
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
            if (result.data.length > 0) {
                $($scope.items.siteConfirmTest).append(new Option("Chọn cơ sở xét nghiệm", "", i == 0, false));
            }

            for (var i = 0; i < result.data.length; i++) {
                var option = new Option(result.data[i].name, result.data[i].id, false, false);
                $($scope.items.siteConfirmTest).append(option);
            }
            $($scope.items.siteConfirmTest).val(utils.getContentOfDefault(form.siteConfirmTest, '')).change();
            $($scope.items.siteConfirmTest).trigger('change');
        });
    };

    $($scope.items.code).keyup(function () {

        var code = $scope.code;
        var codes = code.split("-");
        var prefix = code.substring(0, code.indexOf(codes[codes.length - 1]));

        if (this.value.indexOf(prefix) !== 0) {
            this.value = prefix;
        }
    });

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
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
            code: {
                required: true,
                validCode: function () {
                    return $($scope.items.ID).val().length > 0;
                }
            },
            mostRecentTest: {
                required: function () {
                    return $($scope.items.hasTestBefore).val() === '1';
                }
            },
            testResultsID: {
                required: true
            },
            healthInsuranceNo: {
                required: function () {
                    return $($scope.items.hasHealthInsurance).val() === '1';
                }
            },
            advisoryeTime: {
                required: true,
                validDate: function () {
                    return $($scope.items.advisoryeTime).val() !== '' &&
                            $($scope.items.advisoryeTime).val() !== 'undefined';
                }
            },
            patientName: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2' &&
                            $($scope.items.isAgreeTest).val() === 'true';
                }
            },
            yearOfBirth: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2' &&
                            $($scope.items.isAgreeTest).val() === 'true';
                }
            },
//            permanentAddress: {
//                required: function () {
//                    return $($scope.items.testResultsID).val() === '2' &&
//                            $($scope.items.isAgreeTest).val() === 'true';
//                }
//            },
            permanentProvinceID: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2' &&
                            $($scope.items.isAgreeTest).val() === 'true';
                }
            },
            permanentDistrictID: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2' &&
                            $($scope.items.isAgreeTest).val() === 'true';
                }
            },
            isAgreeTest: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2';
                }
            },
            permanentWardID: {
                required: function () {
                    return $($scope.items.testResultsID).val() === '2' &&
                            $($scope.items.isAgreeTest).val() === 'true';
                }
            }
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
                required: "Mã khách hàng không được để trống"
            },
            mostRecentTest: {
                required: "Lần xét nghiệm gần nhất không được để trống"
            },
            testResultsID: {
                required: "Kết quả xét nghiệm không được để trống"
            },
            healthInsuranceNo: {
                required: "Mã số thẻ BHYT không được để trống"
            },
            advisoryeTime: {
                required: "Ngày XN không được để trống",
                validDate: "Ngày XN phải đúng định dạng ngày/tháng/năm"
            },
            patientName: {
                required: "Họ tên khách hàng không được để trống"
            },
            yearOfBirth: {
                required: "Năm sinh không được để trống"
            },
//            permanentAddress: {
//                required: "Địa chỉ thường trú không được để trống"
//            },
            permanentProvinceID: {
                required: "Tỉnh/Thành phố không được để trống"
            },
            permanentDistrictID: {
                required: "Quận/Huyện không được để trống"
            },
            permanentWardID: {
                required: "Phường/xã không được để trống"
            },
            isAgreeTest: {
                required: "Câu trả lời không được để trống"
            }
        }

    });

    $("#riskBehaviorID").change(
            function () {
                if ($("#riskBehaviorID").val() !== null && $("#riskBehaviorID").val().toString().charAt(0) === ',') {
                    $("#riskBehaviorID option[value='']").removeAttr("selected");
                }
            }
    );

    // Sự kiện change kết quả xét nghiệm
    $($scope.items.testResultsID).change(
            function () {
                // Chưa chọn kết quả xét nghiệm
                if ($($scope.items.testResultsID).val() === '') {
                    $($scope.items.isAgreeTest).attr("disabled", "disabled");
                    $($scope.items.siteVisitID).attr("disabled", "disabled");
                    return false;
                }

                // Kết quả có phản ứng
                if ($($scope.items.testResultsID).val() === '2') {
                    $($scope.items.isAgreeTest).removeAttr("disabled").val('').change();
                    return false;
                }

                // Kết quả xét nghiệm âm tính
                if ($($scope.items.testResultsID).val() === '1') {
                    $($scope.items.isAgreeTest).hide();
                    $($scope.items.siteVisitID).hide();
                    $($scope.items.isAgreeTest).attr("disabled", "disabled");
                    $($scope.items.siteVisitID).attr("disabled", "disabled");
                    return false;
                }
            }
    );

    // Sự kiện change khách hàng đồng ý xét nghiệm KĐ
    $($scope.items.isAgreeTest).change(
            function () {
                // Chưa chọn câu trả lời đồng ý xét nghiệm
                if ($($scope.items.isAgreeTest).val() === 'false' && $($scope.items.testResultsID).val() === '2') {
                    $($scope.items.siteVisitID).attr("disabled", "disabled");
                    $($scope.items.confirmTime).val("").change();
                    $($scope.items.confirmTime).attr("disabled", "disabled");

                    $($scope.items.confirmResultsID).val("").change();
                    $($scope.items.confirmResultsID).attr("disabled", "disabled");

                    $($scope.items.siteConfirmTest).val("").change();
                    $($scope.items.siteConfirmTest).attr("disabled", "disabled");
                    
                    $($scope.items.exchangeTime).val("").change();
                    $($scope.items.exchangeTime).attr("disabled", "disabled");

                    $($scope.items.arrivalSite).val("").change();
                    $($scope.items.arrivalSite).attr("disabled", "disabled");

                    $($scope.items.exchangeStatus).val("").change();
                    $($scope.items.exchangeStatus).attr("disabled", "disabled");
                    return false;
                }

                // Đồng ý xét nghiệm 
                if ($($scope.items.testResultsID).val() === '2' && $($scope.items.isAgreeTest).val() === 'true') {
                    $($scope.items.siteVisitID).removeAttr("disabled").val('').change();
                    $($scope.items.confirmTime).removeAttr("disabled").val('').change();
                    $($scope.items.confirmResultsID).removeAttr("disabled").val('').change();
                    $($scope.items.siteConfirmTest).removeAttr("disabled").val('').change();
                    
                    $($scope.items.exchangeTime).removeAttr("disabled").val('').change();
                    $($scope.items.arrivalSite).removeAttr("disabled").val('').change();
                    $($scope.items.exchangeStatus).removeAttr("disabled").val('').change();
                    
                    return false;
                }
            }
    );

    $scope.initSiteVisitTest = function () {
        $.ajax({
            url: '/service/laytest/get-site-htc.json',
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
            if (result.data.length > 0) {
                $($scope.items.siteVisitID).append(new Option("Chọn cơ sở chuyển đến", "", i == 0, false));
            }

            for (var i = 0; i < result.data.length; i++) {
                var option = new Option(result.data[i].name, result.data[i].id, false, false);
                $($scope.items.siteVisitID).append(option);
            }
            $($scope.items.siteVisitID).val(utils.getContentOfDefault(form.siteVisitID, '')).change();
            $($scope.items.siteVisitID).trigger('change');
        });
    };

    // Submit method
    $scope.customSubmit = function (form, $event) {
        var elm = $event.currentTarget;
        $event.preventDefault();
        let flagCheck;
        let flagCheckAgency;
        bootbox.hideAll();
        
        $(".help-block-error").remove();
        flagCheck = form.validate();    // Check for all other form
        flagCheckAgency = $scope.validateAgency(form); // Check for agency only
        $scope.customerCount($(".fieldwrapper")); // Đếm tổng số khách hàng
        $scope.emptyNotify() // Thông báo nếu không có thông tin bạn tình bạn chích

        if (flagCheck && flagCheckAgency) {
            if ($scope.testResultsID === '2' && $scope.isAgreeTest === 'true'
                    && $scope.siteVisitID !== '' && $scope.sampleSentDate === '') {
                bootbox.confirm(
                        {
                            message: 'Bạn có muốn gửi thông tin khách hàng tới cơ sở xét nghiệm sàng lọc không ?',
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
                                    $("#pageRedirect").val("save-sent-sample");
                                    elm.form.submit();
                                } else {
                                    $("#pageRedirect").val("save");
                                    elm.form.submit();
                                }
                            }
                        }
                );
            } else {
                $("#pageRedirect").val("save");
                elm.form.submit();
            }
        } else {
            msg.danger("Lưu thông tin thất bại vì thông tin chưa đầy đủ. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }

    };

    // Validate người được giới thiệu
    $.validator.addMethod("validateName", function (value) {
        return value.match(/^[\w\s\-\_]+$/);
    }, "Họ tên không được chứa ký tự đặc biệt");
    
    $.validator.addMethod("validateAddress", function (value) {
        return value.match(/^[\w\s\-\_\,]+$/);
    }, "Địa chỉ không được chứa ký tự đặc biệt");
    
    
    $scope.validateAgency = function (form) {

        // Validate người được giới thiệu
        var rules = new Object();
        var messages = new Object();
        
        if(typeof $('.tbl_agency') === 'undefined' || $('.tbl_agency').size() <= 0){
            return true;
        }
        
        $('.tbl_agency').each(function () {
            let name = $(this).attr('name');
            if (name.indexOf('fullname') !== -1) {
                
                let reg = /^[\w\s\-]+$/;
                let enName = utils.removeDiacritical($("input[name ='"+this.name+"']").val());
                rules[this.name] = {maxlength: 100, 
                                    required: true, 
                                    validateName: !reg.test(enName)
                                    }
                messages[this.name] = {maxlength: "Họ tên không được nhập quá 100 ký tự", 
                                        required: "Họ tên không được để trống",
                                        validateName: "Họ tên không được chứa ký tự đặc biệt"};
            }

            if (name.indexOf('address') !== -1) {
                
                let reg = /^[\w\s\-\_\,]+$/;
                let enAdress = utils.removeDiacritical($("input[name ='"+this.name+"']").val());
                
                rules[this.name] = {maxlength: 500,
                                    validateAddress: enAdress!=='' && !reg.test(enAdress)
                                };
                messages[this.name] = {maxlength: "Địa chỉ không được nhập quá 500 ký tự",
                                    validateAddress: "Địa chỉ không được chứa ký tự đặc biệt"
                                };
            }

            if (name.indexOf('phone') !== -1) {
                rules[this.name] = {required: true, digits: true, maxlength: 50};
                messages[this.name] = {required: "Số điện thoại không được để trống", digits: "Số điện thoại không được quá 50 ký tự và phải là số", 
                                        maxlength: "Số điện thoại không được quá 50 ký tự và phải là số"};
            }
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
        return validateOk;
    };
    
    // Load trạng thái lần gần nhất xét nghiệm
    if ($($scope.items.hasTestBefore).val() !== "1") {
        $($scope.items.mostRecentTest).val("").change();
        $($scope.items.mostRecentTest).attr("disabled", "disabled");
    } else {
        $($scope.items.mostRecentTest).removeAttr("disabled");
    }
});

app.controller('laytest_index', function ($scope, $uibModal, laytestService) {
    if ($.getQueryParameters().code != null ||
            $.getQueryParameters().full_name != null ||
            $.getQueryParameters().advisorye_time != null ||
            $.getQueryParameters().send_status != null ||
            $.getQueryParameters().fullname != null ||
            $.getQueryParameters().status != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.init = function () {
        $scope.advisorye_time = $.getQueryParameters().advisorye_time;
        $scope.switchConfig();

    };

    $scope.logs = function (oid, code, name) {
        laytestService.logs(oid, code, name);
    };

    $scope.transferred = function (oid) {
        loading.show();
        $.ajax({
            url: '/service/laytest/get.json',
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'transferred',
                        controller: 'laytest_transferred',
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
        });
    };
});
app.controller('laytest_transferred', function ($scope, $uibModalInstance, params, msg) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.model = {
        siteVisitID: params.item.siteVisitID == null || params.item.siteVisitID == '' ? "" : params.item.siteVisitID,
        sampleSentDate: utils.timestampToStringDate(new Date())

    };

    $scope.ok = function () {
        loading.show();
        $.ajax({
            url: '/service/laytest/transferred.json' + "?oid=" + $scope.item.id,
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


app.controller('laytest_dashboard', function ($scope, msg, amchart) {
    $scope.init = function () {
        $scope.chart01();
        $scope.chart02();
        $scope.chart03();
        $scope.chart04();
    };

    /**
     * KC1.7a_TQ_XN theo nhóm
     * @auth TrangBN
     * @returns {undefined}
     */
    $scope.chart01 = function () {
        var today = new Date();
        var toDay = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
        var fromDay = new Date(new Date().getFullYear(), 0, 1);
        $.ajax({
            url: '/service/laytest-dashboard/chart01.json',
            success: function (resp) {
                if (resp.success) {
                    amchart.pie("chart01", resp.data, function (chart, title, pieSeries) {
                        title.text = 'Phân bổ số khách hàng phát hiện dương tính theo nhóm đối tượng (' + fromDay.toLocaleDateString("en-US") + " - " + toDay + ")";
                        pieSeries.dataFields.value = "so_xn";
                        pieSeries.dataFields.category = "danhmuc";
                    });
                }
            }
        });
    };


    /**
     * KC1.7b_TQ_XN (+) theo nhóm
     * @returns {undefined}
     */
    $scope.chart02 = function () {
        var today = new Date();
        var toDay = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
        var fromDay = new Date(new Date().getFullYear(), 0, 1);
        $.ajax({
            url: '/service/laytest-dashboard/chart02.json',
            success: function (resp) {
                if (resp.success) {
                    amchart.pie("chart02", resp.data, function (chart, title, pieSeries) {
                        title.text = 'Phân bổ số XN theo nhóm đối tượng (' + fromDay.toLocaleDateString("en-US") + " - " + toDay + ")";
                        pieSeries.dataFields.value = "so_xn";
                        pieSeries.dataFields.category = "danhmuc";
                    });
                }
            }
        });
    };

    /**
     * KC1.7c_TQ_XN &(+) theo tháng
     * @returns {undefined}
     */
    $scope.chart03 = function () {
        $.ajax({
            url: '/service/laytest-dashboard/chart03.json',
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = 'Số lượt xét nghiệm và số người khẳng định dương tính theo tháng';
                        categoryAxis.dataFields.category = "rule";

                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "so_xn", "Số người XN HIV", false, valueAxis);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "so_duongtinh", "Số người XN HIV dương tính", false, valueAxisLine);
                    });
                }
            }
        });
    };


    /**
     * KC1.7d_TQ_Chuyển gửi
     * @returns {undefined}
     */
    $scope.chart04 = function () {
        $.ajax({
            url: '/service/laytest-dashboard/chart04.json',
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
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
});