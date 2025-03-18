app.controller('pac_dead', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.deathTimeFrom = $.getQueryParameters().death_time_from;
        $scope.deathTimeTo = $.getQueryParameters().death_time_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;
        $scope.request_death_time_from = $.getQueryParameters().request_death_time_from;
        $scope.request_death_time_to = $.getQueryParameters().request_death_time_to;

        $scope.initInput();
        $scope.addressFilter = utils.getContentOfDefault(f.addressFilter, '');
        $scope.dateFilter = utils.getContentOfDefault(f.dateFilter, '');

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
        $scope.inputTimeFrom = $.getQueryParameters().input_time_from;
        $scope.inputTimeTo = $.getQueryParameters().input_time_to;
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;

        $scope.select_search("#place_test", "Tất cả");
        $scope.select_search("#facility", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        
        $scope.select_mutiple("#permanent_province_id", "Tất cả");
        $scope.select_mutiple("#permanent_district_id", "Tất cả");
        $scope.select_mutiple("#permanent_ward_id", "Tất cả");
        $scope.select_mutiple("#test_object", "Tất cả");
        $scope.select_mutiple("#race", "Tất cả");
        $scope.select_mutiple("#gender", "Tất cả");
        $scope.select_mutiple("#transmision", "Tất cả");

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