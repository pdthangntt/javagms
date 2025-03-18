app.controller('report_pac_patient', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
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

app.controller('pac_dead_report', function ($scope) {

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
                $(".titleTable").text("Báo cáo tử vong");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo tử vong chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo tử vong cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo tử vong đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo tử vong được quản lý");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo tử vong");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo tử vong chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo tử vong cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo tử vong đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo tử vong được quản lý");
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
        
        
        $scope.request_death_time_from = $.getQueryParameters().request_death_time_from;
        $scope.request_death_time_to = $.getQueryParameters().request_death_time_to;
        $scope.deathTimeFrom = $.getQueryParameters().death_time_from;
        $scope.deathTimeTo = $.getQueryParameters().death_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#gender", "Tất cả");
        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");

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

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
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

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});

app.controller('pac_aids_report', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.initInput();
        $scope.request_death_time_from = $.getQueryParameters().request_death_time_from;
        $scope.request_death_time_to = $.getQueryParameters().request_death_time_to;
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
                $(".titleTable").text("Báo cáo AIDS");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo AIDS chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo AIDS cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo AIDS đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo AIDS được quản lý");
            }
        }
        
        $("#manage_status").on('change', function () {
            if ($("#manage_status").val() === "-1") {
                $(".titleTable").text("Báo cáo AIDS");
            }
            if ($("#manage_status").val() === "1") {
                $(".titleTable").text("Báo cáo AIDS chưa rà soát");
            }
            if ($("#manage_status").val() === "2") {
                $(".titleTable").text("Báo cáo AIDS cần rà soát");
            }
            if ($("#manage_status").val() === "3") {
                $(".titleTable").text("Báo cáo AIDS đã rà soát");
            }
            if ($("#manage_status").val() === "4") {
                $(".titleTable").text("Báo cáo AIDS được quản lý");
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
        
        
        $scope.deathTimeFrom = $.getQueryParameters().death_time_from;
        $scope.deathTimeTo = $.getQueryParameters().death_time_to;
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
        $scope.update_time_from = $.getQueryParameters().update_time_from;
        $scope.update_time_to = $.getQueryParameters().update_time_to;

        $scope.select_search("#gender", "Tất cả");
        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#status_treatment", "Tất cả");
        $scope.select_search("#status_resident", "Tất cả");

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

        if (formSearch.manageStatus !== '') {
            $("#manage_status").val(formSearch.manageStatus).change();
        }
        if (formSearch.gender !== '') {
            $("#gender").val(formSearch.gender).change();
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

    $scope.email = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };

    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
});


app.controller('pac_local_report', function ($scope) {

    $scope.init = function () {
        
        $scope.patientStatus = utils.getContentOfDefault(formSearch.patientStatus, '');
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        
        $scope.fromTime = $.getQueryParameters().from_time;
        $scope.toTime = $.getQueryParameters().to_time;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");

        $scope.select_mutiple("#permanent_district_id", "Tất cả");
        $scope.select_mutiple("#permanent_ward_id", "Tất cả");
        $scope.select_mutiple("#permanent_province_id", "Tất cả");
        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");

        // Load lại giá trị cho điều kiện search
        if (formSearch.ttyt) {
            $("#permanent_district_id").val(formSearch.district).change();
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
            $("#permanent_district_id").attr('disabled', 'disabled');
        }

        if (formSearch.tyt) {
            $("#permanent_district_id").val(formSearch.district).change();
            $("#permanent_ward_id").val(formSearch.ward).change();
            $("#permanent_district_id").attr('disabled', 'disabled');
            $("#permanent_ward_id").attr('disabled', 'disabled');
        }
        
        if (form.permanent_province_id != null && form.permanent_province_id !== '') {
            $("#permanent_province_id").val(form.permanent_province_id).change();
        }
        
        if (form.permanent_district_id != null && form.permanent_district_id !== '') {
            $("#permanent_district_id").val(form.permanent_district_id).change();
        }
        
        $("#permanent_ward_id option[value='']").remove();
        if (form.permanent_ward_id != null && form.permanent_ward_id !== '') {
            $("#permanent_ward_id").val(form.permanent_ward_id).change();
        }
    }

    /**
     * Hiển thị chi tiết repory
     * 
     * @auth TrangBN
     * @param {type} params
     * @returns {undefined}
     */
    $scope.detailLocalReportLine = function (params, $event) {

        if ($event.target.textContent === "") {
            return false;
        }

        params.permanentDistrictID = params.permanentDistrictID === 'null' ? "" : params.permanentDistrictID;
        params.permanentWardID = params.permanentWardID === 'null' ? "" : params.permanentWardID;
        params.fromTime = $scope.from_time === '' ? params.fromTime : $scope.from_time;
        params.toTime = $scope.to_time === '' ? params.toTime : $scope.to_time;

        loading.show();
        $.ajax({
            url: '/service/pac-local/search.json',
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
                        (typeof options['risk-behavior'][item.riskBehaviorID] == 'undefined' ? '' : options['risk-behavior'][item.riskBehaviorID]),
                        // Nơi lấy máu
                        (typeof options['blood-base'][item.bloodBaseID] == 'undefined' ? '' : options['blood-base'][item.bloodBaseID]),
                        utils.timestampToStringDate(item.confirmTime),
                        (typeof options['place-test'][item.siteConfirmID] == 'undefined' ? '' : options['place-test'][item.siteConfirmID]),
                        (typeof options['status-of-treatment'][item.statusOfTreatmentID] == 'undefined' ? '' : options['status-of-treatment'][item.statusOfTreatmentID]),
                        utils.timestampToStringDate(item.startTreatmentTime),
                        (typeof options['treatment-facility'][item.siteTreatmentFacilityID] == 'undefined' ? '' : options['treatment-facility'][item.siteTreatmentFacilityID]),
                        utils.timestampToStringDate(item.deathTime)
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách người nhiễm còn sống và tử vong theo huyện xã",
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
                            {title: "Số BHYT"},
                            {title: "Địa chỉ thường trú", className: "text-center dt-body-left"},
                            {title: "Địa chỉ hiện tại", className: "text-center dt-body-left"},
                            {title: "Hiện trạng cư trú", className: "text-center"},
                            {title: "Nghề nghiệp", className: "text-center dt-body-left"},
                            {title: "Nhóm đối tượng", className: "text-center dt-body-left"},
                            {title: "Nguy cơ lây nhiễm", className: "text-center dt-body-left"},
                            {title: "Nơi lấy máu" , className: "text-center dt-body-left"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Nơi XN khẳng định", className: "text-center dt-body-left"},
                            {title: "Trạng thái điều trị", className: "text-center dt-body-left"},
                            {title: "Ngày BĐ điều trị", className: "text-center"},
                            {title: "Nơi điều trị", className: "text-center dt-body-left"},
                            {title: "Ngày tử vong" , className: "text-center"}
                        ]
                    });
                }, 300);
            }
        });
    };

});

app.controller('report_pac_dead_local', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

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

app.controller('report_pac_local', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

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
/**
 * 
 * TrangBN
 */
app.controller('report_pac_early_hiv', function ($scope) {

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

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

app.controller('pac_patient_a10', function ($scope) {
    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;

    $scope.init = function () {
        $scope.confirmTimeFrom = $.getQueryParameters().confirm_time_from;
        $scope.confirmTimeTo = $.getQueryParameters().confirm_time_to;
        $scope.yearOfBirth = $.getQueryParameters().year_of_birth;
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#gender", "Tất cả");
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

app.controller('pac_aids', function ($scope) {
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
        
        $scope.select_search("#blood", "Tất cả");

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
        $scope.aids_from = $.getQueryParameters().aids_from;
        $scope.aids_to = $.getQueryParameters().aids_to;
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