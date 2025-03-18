app.controller('pac_outprovince_index', function ($scope, pacPatientService, msg) {
    if ($.getQueryParameters().year_of_birth != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }

    $scope.urlPdf = urlPdf;
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.formSearch = formSearch;


    $scope.items = {
        province_id: "#province_id",
        detect_province_id: "#detect_province_id",
        permanent_province_id: "#permanent_province_id",
        current_province_id: "#current_province_id"
    };

    $scope.init = function () {
        $scope.switchConfig();

        $scope.select_search("#province_id");
        $scope.select_search("#detect_province_id");
        $scope.select_search("#permanent_province_id");
        $scope.select_search("#current_province_id");
        
        $scope.select_search("#test_object");
        $scope.select_search("#transmision");
        $scope.select_search("#status_resident");
        $scope.select_search("#status_treatment");

        $scope.confirm_time_from = $.getQueryParameters().confirm_time_from;
        $scope.confirm_time_to = $.getQueryParameters().confirm_time_to;
        $scope.year_of_birth = $.getQueryParameters().year_of_birth;

        $scope.select_search("#blood_base", "");
        $scope.select_search("#service", "");
        $scope.select_search("#site_confirm", "");
        $scope.select_search("#siteTreatmentFacilityID", "");

        //tỉnh thành
        $scope.initProvince("#province_id");
        $scope.initProvince("#detect_province_id");
        $scope.initProvince("#permanent_province_id");
        $scope.initProvince("#current_province_id");

        $("#province_id").val($scope.formSearch.provinceID).change();
        $("#detect_province_id").val($scope.formSearch.detectProvinceID).change();
        $("#permanent_province_id").val($scope.formSearch.permanentProvinceID).change();
        $("#current_province_id").val($scope.formSearch.currentProvinceID).change();


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

    $scope.logs = function (oid, code, name) {
        pacPatientService.logs(oid, code, name);
    };
});

app.controller('pac_outprovince_manager', function ($scope) {
    $scope.pOptions = pOptions;
    $scope.id = utils.getContentOfDefault(form.id, '');

    $scope.init = function () {
        $scope.initProvince("#provinceID", "#districtID", "#wardID");
        $scope.$parent.select_search("#provinceID", "Chọn tỉnh thành");
        $scope.$parent.select_search("#districtID", "Chọn quận huyện");
        $scope.$parent.select_search("#wardID", "Chọn xã phường");
    };
});

app.controller('pac_outprovince_filter', function ($scope) {
    $scope.init = function () {
        $scope.switchConfig();
        let config = {
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'icheckbox_minimal-blue'
        };
        $(".radio-cust").iCheck(config);
    };

    $scope.source = function (acceptTime, reviewWardTime, reviewProvinceTime, sourceServiceID) {
        if (sourceServiceID === "103") {
            return "opc";
        } else if (acceptTime != null && reviewWardTime == null && reviewProvinceTime == null) {
            return "review";
        } else if (acceptTime != null && reviewWardTime != null && reviewProvinceTime == null) {
            return "accept";
        } else if (acceptTime != null && reviewWardTime != null && reviewProvinceTime != null) {
            return "patient";
        } else if (acceptTime == null && reviewWardTime == null && reviewProvinceTime == null && sourceServiceID !== "103") {
            return "new";
        }
    };

    $scope.view = function (id, acceptTime, reviewWardTime, reviewProvinceTime, sourceServiceID) {
        let source = $scope.source(acceptTime, reviewWardTime, reviewProvinceTime, sourceServiceID);
        if (source === 'review') {
            window.open(urlReviewView.replace("0", id), '_blank');
        } else if (source === 'accept') {
            window.open(urlAcceptView.replace("0", id), '_blank');
        } else if (source === 'patient') {
            window.open(urlPatientView.replace("0", id), '_blank');
        } else if (source === 'new') {
            window.open(urlNewView.replace("0", id), '_blank');
        } else if (source === 'opc') {
            window.open(urlOpcView.replace("0", id), '_blank');
        }
    };
    
    $scope.addHistory = function (id) {
        
        // A
//        let treatmentInfoSource = patient.siteTreatmentFacilityID !== 'null' && patient.siteTreatmentFacilityID !== null && patient.siteTreatmentFacilityID !== '' && patient.statusOfTreatmentID !== '0' && patient.statusOfTreatmentID !== '1' && patient.statusOfTreatmentID !== 'null' && patient.statusOfTreatmentID !== null && patient.statusOfTreatmentID !== '';
        // B
//        let treatmentInfoTarget = facilityPlace !== 'null' && facilityPlace !== null && facilityPlace !== '' && statusTreatment !== '0' && statusTreatment !== '1' && statusTreatment !== 'null' && statusTreatment !== null && statusTreatment !== '';
        
        bootbox.confirm(
                {
                    message:'Bạn có muốn đánh dấu người nhiễm được kiểm tra trùng lắp \"' + patient.fullname + '\" là thông tin con của người nhiễm đang được chọn này không?',
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
                                url: urlAddHistory + '?source-id=' + patient.id + '&target-id=' + id,
                                type: "GET",
                                contentType: "application/json",
                                dataType: 'json',
                                success: function (resp) {
                                    loading.hide();
                                    $scope.$apply(function () {
                                        $scope.errors = null;
                                        if (resp.success) {
                                            msg.success(resp.message);
                                        } else {
                                            $scope.errors = resp.data;
                                            if (resp.message) {
                                                msg.danger(resp.message);
                                            }
                                        }
                                    });

                                }
                            });


                        } else {
                            
                        }
                    }
                }
        );
    }
});