app.controller('pac_ward', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');

    };

    $scope.initInput = function () {
        $scope.patientStatus = utils.getContentOfDefault(formSearch.patientStatus, '');

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_mutiple("#permanent_district_id", "Tất cả");
        $scope.select_mutiple("#permanent_ward_id", "Tất cả");
        $scope.select_mutiple("#permanent_province_id", "Tất cả");
        $scope.select_mutiple("#status_alive", "");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        $scope.fromTime = $.getQueryParameters().from_time;
        $scope.toTime = $.getQueryParameters().to_time;

        $scope.select_search("#race", "Tất cả");
        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#gender", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");
        $scope.select_search("#manage_status", "");
//        $scope.select_search("#month", "");
//        $scope.select_search("#year", "");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (form.permanent_province_id != null && form.permanent_province_id !== '') {
            console.log(form.permanent_province_id);
            $("#permanent_province_id").val(form.permanent_province_id).change();
        }
        if (form.permanent_district_id != null && form.permanent_district_id !== '') {
            console.log(form.permanent_district_id);
            $("#permanent_district_id").val(form.permanent_district_id).change();
        }
        if (form.permanent_ward_id != null && form.permanent_ward_id !== '') {
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
        }
        if (formSearch.patientStatus != null) {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.genderID !== '') {
            $("#gender").val(formSearch.genderID).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
        if (formSearch.month !== '') {
            $("#month").val(formSearch.month).change();
        }
        if (formSearch.year !== '') {
            $("#year").val(formSearch.year).change();
        }

        if ($("#manage_status").val() === "-1") {
            $(".titleTable").text("Báo cáo số liệu HIV");
        }
        if ($("#manage_status").val() === "1") {
            $(".titleTable").text("Báo cáo số liệu HIV chưa rà soát");
        }
        if ($("#manage_status").val() === "2") {
            $(".titleTable").text("Báo cáo số liệu HIV cần rà soát");
        }
        if ($("#manage_status").val() === "3") {
            $(".titleTable").text("Báo cáo số liệu HIV đã rà soát");
        }
        if ($("#manage_status").val() === "4") {
            $(".titleTable").text("Báo cáo số liệu HIV được quản lý");
        }

        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo số liệu HIV");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo số liệu HIV chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo số liệu HIV cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo số liệu HIV đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo số liệu HIV được quản lý");
            }
        });

    };

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#searchDetect");
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

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});