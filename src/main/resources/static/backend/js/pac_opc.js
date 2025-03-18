app.controller('pac_opc_index', function ($scope, pacPatientService) {
    $scope.urlExcel = urlExcel;
    if ($.getQueryParameters().full_name != null ||
            $.getQueryParameters().year_of_birth != null ||
            $.getQueryParameters().gender_id != null ||
            $.getQueryParameters().identity_card != null ||
            $.getQueryParameters().health_insurance_no != null ||
            $.getQueryParameters().start_treatment_time != null ||
            $.getQueryParameters().end_treatment_time != null ||
            $.getQueryParameters().permanent_province_id != null ||
            $.getQueryParameters().permanent_district_id != null ||
            $.getQueryParameters().permanent_ward_id != null
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        permanent_district_id: "#permanent_district_id",
        permanent_ward_id: "#permanent_ward_id",
        permanent_province_id: "#permanent_province_id"
    };

    $scope.init = function () {
        $scope.switchConfig();
        $scope.startTreatmentTime = $.getQueryParameters().start_treatment_time;
        $scope.endTreatmentTime = $.getQueryParameters().end_treatment_time;
        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.$parent.select_search($scope.items.permanent_district_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_ward_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_province_id, "Tất cả");
        $scope.$parent.select_search("#facility", "Tất cả");

        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        if (formSearch.addressFilter === 'hokhau') {
            $(".lblProvince").text("Tỉnh/TP thường trú");
            $(".lblDistrict").text("Quận/Huyện thường trú");
            $(".lblWard").text("Xã/Phường thường trú");
        }
        if (formSearch.addressFilter === 'tamtru') {
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

    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.logs = function (oid, name) {
        pacPatientService.logs(oid, name);
    };

    /**
     * Kiểm tra trùng lặp
     * 
     * pdthang
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionFillter = function (oid) {
        pacPatientService.fillter(oid, "bddt");
    };
    $scope.actionTransfer = function (oid) {
        pacPatientService.transferBddt(oid, "bddt");
    };

});