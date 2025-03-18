/* global msg */

app.controller('pac_accept_index', function ($scope, pacPatientService, msg) {
    if ($.getQueryParameters().fullname != null ||
            $.getQueryParameters().confirm_time_from != null || $.getQueryParameters().confirm_time_to != null || $.getQueryParameters().year_of_birth != null
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.items = {
        detect_province_id: "#detect_province_id",
        permanent_district_id: "#permanent_district_id",
        permanent_ward_id: "#permanent_ward_id",
        permanent_province_id: "#permanent_province_id"

    };

    $scope.init = function () {
        $scope.switchConfig();
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.$parent.select_search($scope.items.detect_province_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_district_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_ward_id, "Tất cả");
        $scope.$parent.select_search($scope.items.permanent_province_id, "Tất cả");
        $scope.select_search("#siteConfirmID", "Tất cả");
        $scope.select_search("#siteTreatmentFacilityID", "Tất cả");
        
        $scope.addressFilter = utils.getContentOfDefault(addressFilter, '');

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
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        if (search.permanentProvinceID !== '') {
            $("#permanent_province_id").val(search.permanentProvinceID).change();
        }
        if (search.permanentDistrictID !== '') {
            $("#permanent_district_id").val(search.permanentDistrictID).change();
        }
        if (search.permanentWardID !== '') {
            $("#permanent_ward_id").val(search.permanentWardID).change();
        }


        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
        
        if (!isVAAC) {
            $scope.isPrintf = true;
        } else {
            $scope.isPrintf = false;
            $scope.isPrintf = $($scope.items.permanent_province_id).val() != null && $($scope.items.permanent_province_id).val() != '';
            $($scope.items.permanent_province_id).change(function () {
                $scope.$apply(function () {
                    $scope.isPrintf = $($scope.items.permanent_province_id).val() != null && $($scope.items.permanent_province_id).val() != '';
                });
            });
        }
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
        pacPatientService.fillter(oid, "");
    };
    
    /**
     * In phieu phụ lục 02
     * 
     * @author pdThang
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
        
        $scope.dialogReport(urlAppendix02SentTest + "?oid=" + switches.join(","), null, "Phụ lục 02");
        $("#pdf-loading").remove();

    };
    
    /**
     * Chuyển sang quản lý
     * 
     * pdthang
     * @param {type} oid
     * @return {undefined}
     */
    $scope.actionTransfer = function (oid) {
        pacPatientService.transfer(oid);
    };
    
    //DSNAnh Rà soát lại
    $scope.reviewCheck = function (oid, tab) {
        bootbox.confirm(
            {
                message: "Xác nhận chuyển người nhiễm về địa phương rà soát lại?",
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
                            url: urlReviewCheck,
                            data: {oid: oid, tab: tab},
                            method: 'GET',
                            success: function (resp) {
                                if (resp.success) {
                                    msg.success("Khách hàng đã được yêu cầu rà soát lại thành công!", function () {
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

});