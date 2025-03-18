app.controller('pac_early_hiv_report', function ($scope) {

    $scope.year_old              = utils.getContentOfDefault($.getQueryParameters().year_old, '');
    $scope.gender_id             = utils.getContentOfDefault($.getQueryParameters().gender_id, '');
    $scope.job                   = utils.getContentOfDefault($.getQueryParameters().job, '');
    $scope.test_object           = utils.getContentOfDefault($.getQueryParameters().test_object, '');
    $scope.risk                  = utils.getContentOfDefault($.getQueryParameters().risk, '');
    $scope.blood                 = utils.getContentOfDefault($.getQueryParameters().blood, '');
    $scope.status_treatment      = utils.getContentOfDefault($.getQueryParameters().status_treatment, '');
    $scope.status_resident       = utils.getContentOfDefault($.getQueryParameters().status_resident , '');
    $scope.from_time             = utils.getContentOfDefault($.getQueryParameters().from_time, '');
    $scope.to_time               = utils.getContentOfDefault($.getQueryParameters().to_time, '');

    $scope.init = function () {
        $scope.select_mutiple("#permanent_district_id", "Tất cả");
        $scope.select_mutiple("#permanent_ward_id", "Tất cả");
        $scope.select_mutiple("#permanent_province_id", "Tất cả");
        $scope.fromTime = $.getQueryParameters().from_time;
        $scope.toTime = $.getQueryParameters().to_time;

        $scope.select_search("#job", "Tất cả");
        $scope.select_search("#test_object", "Tất cả");
        $scope.select_search("#blood", "Tất cả");
        $scope.select_search("#risk", "Tất cả");

        $scope.initProvinceMultiple("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
    };
    
    /**
     * Hiển thị chi tiết repory
     * @param {type} params
     * @returns {undefined}
     */
    $scope.detailEarlyReportLine = function (params, $event) {
        
        if ($event.target.textContent === "") {
            return false;
        }
        
        params["permanentDistrictID"]  = params.permanentDistrictID === 'null' ? "" : params.permanentDistrictID;
        params["permanentWardID"]      = params.permanentWardID  === 'null' ? "" : params.permanentWardID;
        params["genderID"]             = $scope.gender_id !== '' ? $scope.gender_id : params.genderID === 'null' ? "" : params.genderID;
        params["yearOld"]              = $scope.year_old;
        params["job"]                  = $scope.job;
        params["testObject"]           = $scope.test_object;
        params["blood"]                = $scope.blood;
        params["statusTreatment"]      = $scope.status_treatment;
        params["statusResident"]       = $scope.status_resident;
        params["fromTime"]             = $scope.from_time;
        params["toTime"]               = $scope.to_time;

        loading.show();
        $.ajax({
            url: '/service/pac-early-hiv/search.json',
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
                        (typeof options['treatment-facility'][item.siteTreatmentFacilityID] == 'undefined' ? '' : options['treatment-facility'][item.siteTreatmentFacilityID])
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách người nhiễm mới phát hiện",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover table-full"></table> Tổng số '+ resp.data.models.length +' người nhiễm.',
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
                        scrollX: true,
                        scrollCollapse: true,
                        processing: true,
                        language: {
                            emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
                        },
                        data: dTable,
                        columns: [
                            {title: "TT", className: "text-center"},
                            {title: "ID", className: "text-center"},
                            {title: "Họ tên", className: "text-center"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Số CMND", className: "text-center"},
                            {title: "Số BHYT"},
                            {title: "Địa chỉ thường trú"},
                            {title: "Địa chỉ hiện tại"},
                            {title: "Hiện trạng cư trú", className: "text-center"},
                            {title: "Nghề nghiệp", className: "text-center"},
                            {title: "Nhóm đối tượng", className: "text-center"},
                            {title: "Nguy cơ lây nhiễm", className: "text-center"},
                            {title: "Nơi lấy máu"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Nơi XN khẳng định", className: "text-center"},
                            {title: "Trạng thái điều trị", className: "text-center"},
                            {title: "Ngày BĐ điều trị", className: "text-center"},
                            {title: "Nơi điều trị"}
                        ]
                    });
                }, 300);
            }
        });
    };
    
});