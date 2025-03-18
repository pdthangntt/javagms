app.controller('pac_detecthiv_gender_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
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
        
        if (!formSearch.isVAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo giới tính");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo giới tính");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo giới tính");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo giới tính");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo giới tính");
            }
        });
        
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
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (form.permanent_province_id != null && form.permanent_province_id !== '') {
            $("#permanent_province_id").val(form.permanent_province_id).change();
        }
        if (form.permanent_district_id != null && form.permanent_district_id !== '') {
            $("#permanent_district_id").val(form.permanent_district_id).change();
        }
        if (form.permanent_ward_id != null && form.permanent_ward_id !== '') {
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
        }
        if ($scope.patientStatus.includes(',')) {
            $("#alive").attr('checked', 'checked').change();
            $("#die").attr('checked', 'checked').change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }
        
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

app.controller('pac_detecthiv_object_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
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
        
        if (!formSearch.VAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo đối tượng");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo đối tượng");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo đối tượng");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo đối tượng");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo đối tượng");
            }
        });
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
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

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
        if ($scope.patientStatus.includes(',')) {
            $("#alive").attr('checked', 'checked').change();
            $("#die").attr('checked', 'checked').change();
        }
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
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


app.controller('pac_out_province_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
       $scope.patientStatus = utils.getContentOfDefault(formSearch.patientStatus, '');

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

//        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

        // Load lại giá trị cho điều kiện search
        if ($scope.patientStatus.includes(',')) {
            $("#alive").attr('checked', 'checked').change();
            $("#die").attr('checked', 'checked').change();
        }
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
        
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
app.controller('pac_detecthiv_resident_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
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
        
        if (!formSearch.VAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo hiện trạng cư trú");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo hiện trạng cư trú");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo hiện trạng cư trú");
            }
        });
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
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

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
        if ($scope.patientStatus.includes(',')) {
            $("#alive").attr('checked', 'checked').change();
            $("#die").attr('checked', 'checked').change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }
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


app.controller('pac_detecthiv_transmission', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;
        
        
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
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

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
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
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

app.controller('pac_detecthiv_age_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
        $scope.dateFilter = utils.getContentOfDefault(formSearch.dateFilter, '');
        $scope.group1 = utils.getContentOfDefault(formSearch.searchAge1, '');
        $scope.group2 = utils.getContentOfDefault(formSearch.searchAge2, '');
        $scope.group3 = utils.getContentOfDefault(formSearch.searchAge3, '');
        $scope.group4 = utils.getContentOfDefault(formSearch.searchAge4, '');
        $scope.group5 = utils.getContentOfDefault(formSearch.searchAge5, '');

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

        if ($("#age1").val() === "<15") {
            $("age-group").val("");
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }

        $('.age-group').blur(function () {
            var inputValue = $(this).val();
            if (inputValue === '__-__') {
                return;
            }

            var ages = inputValue.split('-');
            if (parseInt(ages[1]) < parseInt(ages[0])) {
                $(this).val('');
            }
        });

        // Xử lý phần nhập nhóm tuổi
        $("#age1, #age2, #age3, #age4, #age5").attr("disabled", 'disabled');
        if ($scope.group1 === '' && $scope.group2 === '' && $scope.group3 === ''
                && $scope.group4 === '' && $scope.group5 === '') {
            $("#age1").removeAttr("disabled").change();
        }

        if ($scope.group1 !== '') {
            $("#age1").removeAttr("disabled").change();
        }
        if ($scope.group2 !== '') {
            $("#age2").removeAttr("disabled").change();
        }
        if ($scope.group3 !== '') {
            $("#age3").removeAttr("disabled").change();
        }
        if ($scope.group4 !== '') {
            $("#age4").removeAttr("disabled").change();
        }
        if ($scope.group5 !== '') {
            $("#age5").removeAttr("disabled").change();
        }

        $("#age1").blur(function () {
            if ($("#age1").val() !== '' && !$("#age1").val().includes("_")) {
                $("#age2").removeAttr("disabled").change();
            } else {
                $("#age2, #age3, #age4, #age5").val("");
                $("#age2, #age3, #age4, #age5").attr("disabled", 'disabled');
            }
            
            if ((parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age2").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age3").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0])) 
               ) {
                $("#age2, #age3, #age4, #age5").val("");
                $("#age2,#age3, #age4, #age5").attr("disabled", 'disabled');
            }
        });

        $("#age2").blur(function () {
            if ($("#age2").val() !== '' && !$("#age2").val().includes("_")) {
                $("#age3").removeAttr("disabled").change();
            } else {
                $("#age3, #age4, #age5").val("");
                $("#age3, #age4, #age5").attr("disabled", 'disabled');
            }

            if ((parseInt($("#age2").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age3").val().split("-")[0])) 
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0])) 
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0])) 
               ) {
                $("#age2, #age3, #age4, #age5").val("");
                $("#age3, #age4, #age5").attr("disabled", 'disabled');
            }
        });
        $("#age3").blur(function () {
            if ($("#age3").val() !== '' && !$("#age3").val().includes("_")) {
                $("#age4").removeAttr("disabled").change();
            } else {
                $("#age4, #age5").val("");
                $("#age4, #age5").attr("disabled", 'disabled');
            }

            if ((parseInt($("#age3").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age3").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age3").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0]))
                || (parseInt($("#age3").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0]))
                ) {
                $("#age3, #age4, #age5").val("");
                $("#age4, #age5").attr("disabled", 'disabled');
            }
        });
        $("#age4").blur(function () {
            if ($("#age4").val() !== '' && !$("#age4").val().includes("_")) {
                $("#age5").removeAttr("disabled").change();
            } else {
                $("#age5").val("");
                $("#age5").attr("disabled", 'disabled');
            }

            if ((parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age3").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0]))
                ) {
                $("#age4, #age5").val("");
                $("#age5").attr("disabled", 'disabled');
            }
        });
        $("#age5").blur(function () {
            if ((parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age4").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age3").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                ) {
                $("#age5").val("");
            }
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
        
        if (!formSearch.isVAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo nhóm tuổi");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo nhóm tuổi");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo nhóm tuổi");
            }
        });
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
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (form.permanent_province_id != null && form.permanent_province_id !== '') {
            $("#permanent_province_id").val(form.permanent_province_id).change();
        }
        if (form.permanent_district_id != null && form.permanent_district_id !== '') {
            $("#permanent_district_id").val(form.permanent_district_id).change();
        }
        if (form.permanent_ward_id != null && form.permanent_ward_id !== '') {
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
        }
        
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        
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
    };

    // Bắt sự kiện nhấn Enter
    document.addEventListener("keypress", function onEvent(event) {
        
        let result = false;
        if (event.key === "Enter") {
            // Validate nhập tuổi
            // Age1
            if (($("#age1").val() === '' || $("#age1").val().includes('_')) && 
                    (($("#age2").val() !== '' && !$("#age2").val().includes('_'))
                    || ($("#age3").val() !== '' && !$("#age3").val().includes('_'))
                    || ($("#age4").val() !== '' && !$("#age4").val().includes('_'))
                    || ($("#age5").val() !== '' && !$("#age5").val().includes('_'))) 
                ) {
                result = true;
                $("#age1").removeAttr("disabled").change();
                $("#age2, #age3, #age4, #age5").val("");
                $("#age2, #age3, #age4, #age5").attr("disabled", 'disabled');
            }
            
            if ((parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age2").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age3").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0])) 
                || (parseInt($("#age1").val().split("-")[1]) < parseInt($("#age1").val().split("-")[0])) 
               ) {
                result = true;
                $("#age2, #age3, #age4, #age5").val("");
                $("#age2,#age3, #age4, #age5").attr("disabled", 'disabled');
            }
            
            // Age2
            if (($("#age2").val() === '' || $("#age2").val().includes('_')) && 
                    ( ($("#age3").val()  !== ''  && !$("#age3").val().includes('_'))
                    || ($("#age4").val() !== ''  && !$("#age4").val().includes('_')) 
                    || ($("#age5").val() !== ''  && !$("#age5").val().includes('_')))
                    ) {
                result = true;
                $("#age2").removeAttr("disabled").change();
                $("#age3, #age4, #age5").val("");
            }
            
            if ((parseInt($("#age2").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age3").val().split("-")[0])) 
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0])) 
                || (parseInt($("#age2").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0])) 
                || (parseInt($("#age2").val().split("-")[1]) < parseInt($("#age2").val().split("-")[0])) 
               ) {
                result = true;
                $("#age2, #age3, #age4, #age5").val("");
                $("#age3, #age4, #age5").attr("disabled", 'disabled');
            }
            
            // Age 3
            if (($("#age3").val() === '' || $("#age3").val().includes('_')) && 
                    ( ($("#age4").val() !== '' && !$("#age4").val().includes('_'))
                    || ($("#age5").val() !== '' && !$("#age5").val().includes('_')))
                    ) {
                result = true;
                $("#age3").removeAttr("disabled").change();
                $("#age4, #age5").val("");
            }
            
            if ((parseInt($("#age3").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age3").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age3").val().split("-")[1]) >= parseInt($("#age4").val().split("-")[0]))
                || (parseInt($("#age3").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0]))
                || (parseInt($("#age3").val().split("-")[1]) < parseInt($("#age3").val().split("-")[0]))
                ) {
                result = true;
                $("#age3, #age4, #age5").val("");
                $("#age4, #age5").attr("disabled", 'disabled');
            }
            
            // Age 4
            if (($("#age4").val() === '' || $("#age4").val().includes('_')) && ($("#age5").val() !== '' && !$("#age5").val().includes('_')) ) {
                result = true;
                $("#age4").removeAttr("disabled").change();
                $("#age5").val("");
            }
            
            if ((parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age3").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age4").val().split("-")[1]) >= parseInt($("#age5").val().split("-")[0]))
                || (parseInt($("#age4").val().split("-")[1]) < parseInt($("#age4").val().split("-")[0]))
                ) {
                result = true;
                $("#age4, #age5").val("");
                $("#age5").attr("disabled", 'disabled');
            }
            
            // Age 5
            if ((parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age4").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age3").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age2").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) <= parseInt($("#age1").val().split("-")[1]))
                || (parseInt($("#age5").val().split("-")[0]) > parseInt($("#age5").val().split("-")[1]))
                ) {
                result = true;
                $("#age5").val("");
            }
            
            if (result) {
                event.preventDefault();
            }
        }
    });


    /**
     * Hiển thị chi tiết report
     * 
     * @param {type} params
     * @returns {undefined}
     */
    $scope.detectHivAgeReportLine = function (params, $event) {

        if ($event.target.textContent === "") {
            return false;
        }

        loading.show();
        $.ajax({
            url: '/service/pac-detecthiv-age/search.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(params),
            success: function (resp) {
                loading.hide();
                var dTable = [];
                var options = resp.data.options;
                let i = 0;
                $.each(resp.data.models, function () {
                    i++;
                    var item = this;
                    dTable.push([
                        i, item.id, item.fullname, item.yearOfBirth,
                        (typeof options.gender[item.genderID] === 'undefined' ? '' : options.gender[item.genderID]),
                        item.identityCard,
                        item.healthInsuranceNo,
                        item.permanentAddressFull,
                        item.currentAddressFull,
                        (typeof options['status-of-resident'][item.statusOfResidentID] === 'undefined' ? '' : options['status-of-resident'][item.statusOfResidentID]),
                        (typeof options['job'][item.jobID] === 'undefined' ? '' : options['job'][item.jobID]),
                        (typeof options['test-object-group'][item.objectGroupID] == 'undefined' ? '' : options['test-object-group'][item.objectGroupID]),
                        (typeof options['modes-of-transmision'][item.modeOfTransmissionID] == 'undefined' ? '' : options['modes-of-transmision'][item.modeOfTransmissionID]),
                        // Nơi lấy máu
                        (typeof options['blood-base'][item.bloodBaseID] == 'undefined' ? '' : options['blood-base'][item.bloodBaseID]),
                        utils.timestampToStringDate(item.confirmTime),
                        (typeof options['place-test'][item.siteConfirmID] == 'undefined' ? '' : options['place-test'][item.siteConfirmID]),
                        (typeof options['status-of-treatment'][item.statusOfTreatmentID] == 'undefined' ? '' : options['status-of-treatment'][item.statusOfTreatmentID]),
                        utils.timestampToStringDate(item.reviewProvinceTime),
                        utils.timestampToStringDate(item.startTreatmentTime),
                        (typeof options['treatment-facility'][item.siteTreatmentFacilityID] == 'undefined' ? '' : options['treatment-facility'][item.siteTreatmentFacilityID])
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách người nhiễm mới phát hiện",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover table-full"></table> Tổng số ' + resp.data.models.length + ' người nhiễm.',
                    size: 'large',
                    buttons: {
                        cancel: {
                            label: "Đóng",
                            className: 'btn-default',
                            callback: function () {
                            }
                        }
                    }
                }).find("div.modal-dialog").addClass("largeWidth");

                setTimeout(function () {
                    $("#grid-report").addClass('nowrap').DataTable({
                        paging: false,
                        searching: false,
                        ordering: false,
                        info: false,
                        scrollCollapse: true,
                        processing: true,
                        language: {
                            emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
                        },
                        data: dTable,
                        columns: [
                            {title: "TT", className: "text-center"},
                            {title: "ID", className: "text-center"},
                            {title: "Họ tên", className: "text-center dt-body-left"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Số CMND", className: "text-center"},
                            {title: "Số BHYT", className: "text-center"},
                            {title: "Địa chỉ thường trú", className: "text-center dt-body-left"},
                            {title: "Địa chỉ hiện tại", className: "text-center dt-body-left"},
                            {title: "Hiện trạng cư trú", className: "text-center dt-body-left"},
                            {title: "Nghề nghiệp", className: "text-center dt-body-left"},
                            {title: "Nhóm đối tượng", className: "text-center dt-body-left"},
                            {title: "Đường lây nhiễm", className: "text-center dt-body-left"},
                            {title: "Nơi lấy máu", className: "text-center dt-body-left"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Nơi XN khẳng định", className: "text-center dt-body-left"},
                            {title: "Trạng thái điều trị", className: "text-center dt-body-left"},
                            {title: "Ngày chuyển sang quản lý", className: "text-center"},
                            {title: "Ngày BĐ điều trị", className: "text-center"},
                            {title: "Nơi điều trị", className: "text-center dt-body-left"}
                        ]
                    });
                }, 300);
            }
        });
    };

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#searchDetect");
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

});
app.controller('pac_detecthiv_treatment_export', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;
        
        
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
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

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
//        if (formSearch.province != null && formSearch.province !== '') {
//            $("#permanent_province_id").val(formSearch.province.split(",")).change();
//        }
//        if (formSearch.district != null && formSearch.district !== '') {
//            $("#permanent_district_id").val(formSearch.district.split(",")).change();
//        }
//        if (formSearch.ward != null && formSearch.ward !== '') {
//            $("#permanent_ward_id").val(formSearch.ward.split(",")).change();
//        }
        
        if (formSearch.patientStatus != null) {
//            $("#alive").attr('checked', 'checked').change();
//            $("#die").attr('checked', 'checked').change();
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
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

// Báo cáo người nhiễm phát hiện theo BHYT
app.controller('pac_detecthiv_insurance_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(formSearch.addressFilter, '');
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
        
        if (!formSearch.isVAAC) {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo bảo hiểm y tế");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện chưa rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện cần rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện đã rà soát theo bảo hiểm y tế");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo người nhiễm phát hiện được quản lý theo bảo hiểm y tế");
            }
        });
        
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
        $scope.select_mutiple("#status_alive", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");
        
        $scope.manageTimeFrom = $.getQueryParameters().manage_time_from;
        $scope.manageTimeTo = $.getQueryParameters().manage_time_to;
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");
        $scope.select_search("#transmision", "Tất cả");
        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (form.permanent_province_id != null && form.permanent_province_id !== '') {
            $("#permanent_province_id").val(form.permanent_province_id).change();
        }
        if (form.permanent_district_id != null && form.permanent_district_id !== '') {
            $("#permanent_district_id").val(form.permanent_district_id).change();
        }
        if (form.permanent_ward_id != null && form.permanent_ward_id !== '') {
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
        }
        if ($scope.patientStatus.includes(',')) {
            $("#alive").attr('checked', 'checked').change();
            $("#die").attr('checked', 'checked').change();
        }

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.race !== '') {
            $("#race").val(formSearch.race).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
        }
        if (formSearch.transmision !== '') {
            $("#transmision").val(formSearch.transmision).change();
        }
        if (formSearch.patientStatus !== '') {
            $("#status_alive").val(formSearch.patientStatus.split(",")).change();
        }
        
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