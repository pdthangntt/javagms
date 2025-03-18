app.controller('opc_test', function ($scope, msg, $uibModal) {
    $scope.arv = arv;
    $scope.options = options;
    $scope.currentSiteID = currentSiteID;
    $scope.init = function () {
        $scope.switchConfig();

        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const id = urlParams.get('id')
        const print = urlParams.get('print')
        console.log('arvID ' + id);
        console.log('print ' + print);

        if (print === '1') {
            setTimeout(function () {
                $scope.dialogReport("/report/opc-arv/transfer-lao.html?arv_id=" + id, null, "Phiếu chuyển gửi Lao");
                $("#pdf-loading").remove();
            }, 300);
        }

        

    };
    $scope.create = function () {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'opcTest',
            controller: 'opc_test_form',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        formTitle: "Thêm mới lượt xét nghiệm",
                        select_search: $scope.$parent.select_search,
                        select_mutiple: $scope.$parent.select_mutiple
                    };
                }
            }
        });
    };
    $scope.update = function (id) {
        $.ajax({
            url: urlGet,
            data: {arvid: $scope.arv.id, id: id},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    resp.data.id = id;
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'opcTest',
                        controller: 'opc_test_form',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    model: resp.data,
                                    update: true,
                                    formTitle: "Cập nhật lượt xét nghiệm",
                                    select_search: $scope.$parent.select_search,
                                    select_mutiple: $scope.$parent.select_mutiple
                                };
                            }
                        }
                    });
                } else {
                    msg.danger(resp);
                }
            }
        });
    };
    $scope.view = function (id) {
        $.ajax({
            url: urlGet,
            data: {arvid: $scope.arv.id, id: id},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    resp.data.id = id;
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'opcTest',
                        controller: 'opc_test_form',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    readonly: true,
                                    model: resp.data,
                                    formTitle: "Xem lượt xét nghiệm bệnh án",
                                    select_search: $scope.$parent.select_search,
                                    select_mutiple: $scope.$parent.select_mutiple
                                };
                            }
                        }
                    });
                } else {
                    msg.danger(resp);
                }
            }
        });
    };
    $scope.logs = function (oid) {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'opcTestLogs',
            controller: 'opc_test_log',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        oid: oid,
                        code: $scope.arv.code
                    };
                }
            }
        });
    };
});
app.controller('opc_test_form', function ($scope, $uibModalInstance, params, msg) {
    $scope.readonly = !params.readonly ? false : true;
    $scope.update = !params.update ? false : true;
    $scope.arv = arv;
    $scope.options = options;
    $scope.isOpcManager = isOpcManager;
    $scope.formTitle = params.formTitle;
    $scope.currentSiteID = currentSiteID;
    $scope.errors = {};
    $scope.item = params.model ? params.model : {
        id: null,
        lao: '',
        inh: '',
        ntch: '',
        hbv: '',
        hbvResult: '',
        hcv: '',
        hcvResult: '',
        cd4TestSiteID: '',
        laoSymptoms: null,
        inhEndCauses: null,
        ntchSymptoms: null,
        cotrimoxazoleEndCauses: null,
        cd4Causes: null,
        laoResult: '',
        laoTreatment: '',
        laoStartTime: '',
        laoEndTime: ''
    };
    if (!$scope.readonly && !$scope.update) {
        $scope.item.cd4TestSiteID = $scope.currentSiteID + '';
    }

    //kiểm tra dữ liệu

    $scope.init = function () {

        setTimeout(() => {
            params.select_search("#cd4TestSiteID", "Chọn nơi xét nghiệm");
            params.select_search("#hcvResult", $scope.readonly && $scope.item.hcvResult === null ? "  " : "Chọn kết quả");
            params.select_search("#hbvResult", "Chọn kết quả");
            params.select_mutiple("#laoSymptoms", $scope.readonly && $scope.item.laoSymptoms === null ? "  " : "Chọn biểu hiện");
            $("#laoSymptoms").val($scope.item.laoSymptoms).trigger('change');
            params.select_mutiple("#ntchSymptoms", $scope.readonly && $scope.item.ntchSymptoms === null ? "  " : "Chọn biểu hiện");
            $("#ntchSymptoms").val($scope.item.ntchSymptoms).trigger('change');
            params.select_mutiple("#inhEndCauses", $scope.readonly && $scope.item.inhEndCauses === null ? "  " : "Chọn lý do");
            $("#inhEndCauses").val($scope.item.inhEndCauses).trigger('change');
            params.select_mutiple("#cotrimoxazoleEndCauses", $scope.readonly && $scope.item.cotrimoxazoleEndCauses === null ? "  " : "Chọn lý do");
            $("#cotrimoxazoleEndCauses").val($scope.item.cotrimoxazoleEndCauses).trigger('change');
            params.select_mutiple("#cd4Causes", $scope.readonly && $scope.item.cd4Causes === null ? "  " : "Chọn lý do");
            $("#cd4Causes").val($scope.item.cd4Causes).trigger('change');
        }, 100);
        if ($scope.readonly) {
            $("#testForm input, #testForm select, #testForm textarea").attr({disabled: "disabled"});
        }
        //Lao disible
        if ($scope.item.lao != "1") {
            $("#laoTestTime").attr("disabled", "disabled");
            $("#laoSymptoms").attr("disabled", "disabled");
            $("#laoOtherSymptom").attr("disabled", "disabled");
            $("#laoResult").attr("disabled", "disabled");
        }

        if ($scope.item.laoResult != '2') {
            $("#laoTreatment").attr("disabled", "disabled");
        }

        if ($scope.item.laoTreatment != '1') {
            $("#laoStartTime").attr("disabled", "disabled");
            $("#laoEndTime").attr("disabled", "disabled");
        }

        $("#lao").on("change", function () {
            if ($("#lao").val() == "string:1") {
                $("#laoTestTime").removeAttr("disabled").change();
                $("#laoSymptoms").removeAttr("disabled").change();
                $("#laoOtherSymptom").removeAttr("disabled").change();
                $("#laoResult").removeAttr("disabled").change();
            } else {
                $("#laoTestTime").attr("disabled", "disabled");
                $("#laoSymptoms").attr("disabled", "disabled");
                $("#laoOtherSymptom").attr("disabled", "disabled");
                $("#laoResult").attr("disabled", "disabled");
                $("#laoSymptoms").val("").change();
                $("#laoTestTime").val("").change();
                $("#laoOtherSymptom").val("").change();
                $("#laoResult").val("string:").change();
            }
        });
        $("#laoResult").on("change", function () {
            if ($("#laoResult").val() == "string:2") {
                $("#laoTreatment").removeAttr("disabled").change();
            } else {
                $("#laoTreatment").attr("disabled", "disabled");
                $("#laoTreatment").val("string:").change();
            }
        });
        $("#laoTreatment").on("change", function () {
            if ($("#laoTreatment").val() == "string:1") {
                $("#laoStartTime").removeAttr("disabled").change();
                $("#laoEndTime").removeAttr("disabled").change();
            } else {
                $("#laoStartTime").attr("disabled", "disabled");
                $("#laoStartTime").val("").change();
                $("#laoEndTime").attr("disabled", "disabled");
                $("#laoEndTime").val("").change();
            }
        });
        //inh dissible
        if ($scope.item.inh != "1") {
            $("#inhFromTime").attr("disabled", "disabled");
            $("#inhEndCauses").attr("disabled", "disabled");
            $("#inhToTime").attr("disabled", "disabled");
        }
        $("#inh").on("change", function () {
            if ($("#inh").val() == "string:1") {
                $("#inhFromTime").removeAttr("disabled").change();
                $("#inhToTime").removeAttr("disabled").change();
                $("#inhEndCauses").removeAttr("disabled").change();
            } else {
                $("#inhFromTime").attr("disabled", "disabled");
                $("#inhEndCauses").attr("disabled", "disabled");
                $("#inhToTime").attr("disabled", "disabled");
                $("#inhEndCauses").val("").change();
                $("#inhFromTime").val("").change();
                $("#inhToTime").val("").change();
            }
        });
        //ntch dissible
        if ($scope.item.ntch != "1") {
            $("#ntchSymptoms").attr("disabled", "disabled");
            $("#ntchOtherSymptom").attr("disabled", "disabled");
            $("#cotrimoxazoleFromTime").attr("disabled", "disabled");
            $("#cotrimoxazoleToTime").attr("disabled", "disabled");
            $("#cotrimoxazoleEndCauses").attr("disabled", "disabled");
        }
        $("#ntch").on("change", function () {
            if ($("#ntch").val() == "string:1") {
                $("#ntchSymptoms").removeAttr("disabled").change();
                $("#ntchOtherSymptom").removeAttr("disabled").change();
                $("#cotrimoxazoleFromTime").removeAttr("disabled").change();
                $("#cotrimoxazoleToTime").removeAttr("disabled").change();
                $("#cotrimoxazoleEndCauses").removeAttr("disabled").change();
            } else {
                $("#ntchSymptoms").attr("disabled", "disabled");
                $("#ntchOtherSymptom").attr("disabled", "disabled");
                $("#cotrimoxazoleFromTime").attr("disabled", "disabled");
                $("#cotrimoxazoleToTime").attr("disabled", "disabled");
                $("#cotrimoxazoleEndCauses").attr("disabled", "disabled");
                $("#ntchSymptoms").val("").change();
                $("#ntchOtherSymptom").val("").change();
                $("#cotrimoxazoleFromTime").val("").change();
                $("#cotrimoxazoleToTime").val("").change();
                $("#cotrimoxazoleEndCauses").val("").change();
            }
        });
        if ($scope.item.cd4TestSiteID != "-1") {
            $("#cd4TestSiteName").attr("disabled", "disabled");
        }

        $("#cd4TestSiteID").on("change", function () {
            if ($("#cd4TestSiteID").val() == "string:-1") {
                $("#cd4TestSiteName").removeAttr("disabled").change();
            } else {
                $("#cd4TestSiteName").attr("disabled", "disabled");
                $("#cd4TestSiteName").val("").change();
            }
        });
        if ($scope.item.hbv != "1") {
            $("#hbvTime").attr("disabled", "disabled");
            $("#hbvResult").attr("disabled", "disabled");
            $("#hbvCase").attr("disabled", "disabled");
        }
        $("#hbv").on("change", function () {
            if ($("#hbv").val() == "string:1") {
                $("#hbvTime").removeAttr("disabled").change();
                $("#hbvResult").removeAttr("disabled").change();
                $("#hbvCase").removeAttr("disabled").change();
            } else {
                $("#hbvTime").attr("disabled", "disabled");
                $("#hbvResult").attr("disabled", "disabled");
                $("#hbvCase").attr("disabled", "disabled");
                $("#hbvTime").val("").change();
                $("#hbvResult").val("string:").change();
                $("#hbvCase").val("").change();
            }
        });
        if ($scope.item.hcv != "1") {
            $("#hcvTime").attr("disabled", "disabled");
            $("#hcvResult").attr("disabled", "disabled");
            $("#hcvCase").attr("disabled", "disabled");
        }
        $("#hcv").on("change", function () {
            if ($("#hcv").val() == "string:1") {
                $("#hcvTime").removeAttr("disabled").change();
                $("#hcvResult").removeAttr("disabled").change();
                $("#hcvCase").removeAttr("disabled").change();
            } else {
                $("#hcvTime").attr("disabled", "disabled");
                $("#hcvResult").attr("disabled", "disabled");
                $("#hcvCase").attr("disabled", "disabled");
                $("#hcvTime").val("").change();
                $("#hcvResult").val("string:").change();
                $("#hcvCase").val("").change();
            }
        });
    };
    $scope.ok = function () {
        let url = urlNew + "?arvid=" + $scope.arv.id;
        if ($scope.item.id) {
            url = urlUpdate + "?arvid=" + $scope.arv.id + "&id=" + $scope.item.id;
        }

        //Lấy lại giá trị chọn nhiều
        $scope.item.laoSymptoms = $("#laoSymptoms").val();
        $scope.item.inhEndCauses = $("#inhEndCauses").val();
        $scope.item.ntchSymptoms = $("#ntchSymptoms").val();
        $scope.item.cotrimoxazoleEndCauses = $("#cotrimoxazoleEndCauses").val();
        $scope.item.cd4Causes = $("#cd4Causes").val();
        loading.show();
        $.ajax({
            url: url,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.item),
            success: function (resp) {
                loading.hide();
                $scope.$apply(function () {
                    $scope.errors = {};
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.href = urlIndex;
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
        if ($scope.readonly) {
            $uibModalInstance.dismiss('cancel');
        } else {
            bootbox.confirm({
                message: "Bạn chắc chắn muốn hủy không?",
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
                        $uibModalInstance.dismiss('cancel');
                    } else {
                    }
                }
            });
        }

    };
});
app.controller('opc_test_log', function ($scope, $uibModalInstance, params) {
    $scope.model = {staffID: 0, testID: params.oid, code: params.code};
    $scope.list = function () {
        loading.show();
        $.ajax({
            url: urlLogs,
            data: {oid: params.oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        for (var i = 0; i < resp.data.logs.length; i++) {
                            resp.data.logs[i].staffID = typeof resp.data.staffs[resp.data.logs[i].staffID] === 'undefined' ? 'Hệ thống' : resp.data.staffs[resp.data.logs[i].staffID];
                        }
                        $scope.logs = resp.data.logs;
                    });
                }
            }
        });
    };
    $scope.add = function () {
        if ($scope.model.content === undefined || $scope.model.content === '') {
            return false;
        }
        loading.show();
        $.ajax({
            url: urlLogCreate,
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
                        bootbox.alert(resp.message);
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
app.controller('opc_test_new_form', function ($scope, msg, localStorageService) {

    $scope.stageID = utils.getContentOfDefault(form.stageID, '');
    $scope.lao = utils.getContentOfDefault(form.lao, '');
    $scope.laoTestTime = utils.getContentOfDefault(form.laoTestTime, '');
    $scope.laoStartTime = utils.getContentOfDefault(form.laoStartTime, '');
    $scope.laoEndTime = utils.getContentOfDefault(form.laoEndTime, '');
    $scope.inhFromTime = utils.getContentOfDefault(form.inhFromTime, '');
    $scope.inhToTime = utils.getContentOfDefault(form.inhToTime, '');
    $scope.cotrimoxazoleFromTime = utils.getContentOfDefault(form.cotrimoxazoleFromTime, '');
    $scope.cotrimoxazoleToTime = utils.getContentOfDefault(form.cotrimoxazoleToTime, '');
    $scope.cd4SampleTime = utils.getContentOfDefault(form.cd4SampleTime, '');
    $scope.cd4TestTime = utils.getContentOfDefault(form.cd4TestTime, '');

    $scope.cd4ResultTime = utils.getContentOfDefault(form.cd4ResultTime, '');
    $scope.cd4RetryTime = utils.getContentOfDefault(form.cd4RetryTime, '');
    $scope.hbvTime = utils.getContentOfDefault(form.hbvTime, '');
    $scope.hcvTime = utils.getContentOfDefault(form.hcvTime, '');
    $scope.laoTestDate = utils.getContentOfDefault(form.laoTestDate, '');
    $scope.cotrimoxazoleEndCauses = utils.getContentOfDefault(form.cotrimoxazoleEndCauses, '');
    $scope.examinationAndTest = utils.getContentOfDefault(form.examinationAndTest, '');
    $scope.statusOfTreatmentID = utils.getContentOfDefault(form.statusOfTreatmentID, '');
    
//    $scope.options = options;
    $scope.items = {
        ID: "#ID",
        siteID: "#siteID",
        lao: "#lao",
        laoTestTime: "#laoTestTime",
        laoOtherSymptom: "#laoOtherSymptom",
        inh: "#inh",
        inhFromTime: "#inhFromTime",
        inhToTime: "#inhToTime",
        ntch: "#ntch",
        ntchOtherSymptom: "#ntchOtherSymptom",
        cotrimoxazoleFromTime: "#cotrimoxazoleFromTime",
        cotrimoxazoleToTime: "#cotrimoxazoleToTime",
        cd4SampleTime: "#cd4SampleTime",
        cd4TestTime: "#cd4TestTime",
        firstCd4Time: "#firstCd4Time",
        cd4TestSiteID: "#cd4TestSiteID",
        cd4TestSiteName: "#cd4TestSiteName",
        cd4Result: "#cd4Result",
        cd4ResultTime: "#cd4ResultTime",
        cd4RetryTime: "#cd4RetryTime",
        hbv: "#hbv",
        hbvTime: "#hbvTime",
        hbvResult: "#hbvResult",
        hbvCase: "#hbvCase",
        hcv: "#hcv",
        hcvTime: "#hcvTime",
        hcvResult: "#hcvResult",
        hcvCase: "#hcvCase",
        note: "#note",
        laoResult: "#laoResult",
        laoTreatment: "#laoTreatment",
        laoStartTime: "#laoStartTime",
        laoEndTime: "#laoEndTime",
        stageID: "#stageID",
        laoSymptoms: "#laoSymptoms",
        inhEndCauses: "#inhEndCauses",
        ntchSymptoms: "#ntchSymptoms",
        cotrimoxazoleEndCauses: "#cotrimoxazoleEndCauses",
        suspiciousSymptoms: "#suspiciousSymptoms",
        examinationAndTest: "#examinationAndTest",
        laoTestDate: "#laoTestDate",
        laoDiagnose: "#laoDiagnose",
        cotrimoxazoleOtherEndCause: "#cotrimoxazoleOtherEndCause",
        cd4Causes: "#cd4Causes"
    };

    //kiểm tra dữ liệu
    $scope.init = function () {
        $("#note").attr("rows", "3");
        $scope.$parent.select_mutiple($scope.items.laoSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.ntchSymptoms, "Chọn biểu hiện");
        $scope.$parent.select_mutiple($scope.items.inhEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.cotrimoxazoleEndCauses, "Chọn lý do");
        $scope.$parent.select_mutiple($scope.items.cd4Causes, "Chọn lý do");
        $scope.$parent.select_search($scope.items.stageID, "Chọn giai đoạn điều trị");
        $scope.$parent.select_search($scope.items.cd4TestSiteID, "Chọn nơi xét nghiệm");


        //Lao disible
        if ($($scope.items.lao).val() != "1") {
            $("#laoTestTime").attr("disabled", "disabled");
            $("#laoSymptoms").attr("disabled", "disabled");
            $("#laoOtherSymptom").attr("disabled", "disabled");
            $("#suspiciousSymptoms").attr("disabled", "disabled");
        }

//        if ($($scope.items.laoResult).val() != '2') {
//            $("#laoTreatment").attr("disabled", "disabled");
//        }

        if ($($scope.items.laoTreatment).val() != '1') {
            $("#laoStartTime").attr("disabled", "disabled");
            $("#laoEndTime").attr("disabled", "disabled");
        }

        $("#lao").on("change", function () {
            if ($("#lao").val() == "1") {
                $("#laoTestTime").removeAttr("disabled").change();
                $("#laoSymptoms").removeAttr("disabled").change();
                $("#laoOtherSymptom").removeAttr("disabled").change();
                $("#suspiciousSymptoms").removeAttr("disabled").change();
            } else {
                $("#laoTestTime").attr("disabled", "disabled");
                $("#laoSymptoms").attr("disabled", "disabled");
                $("#laoOtherSymptom").attr("disabled", "disabled");
                $("#suspiciousSymptoms").attr("disabled", "disabled");

//                $("#examinationAndTest").attr("disabled", "disabled");
//                $("#laoTestDate").attr("disabled", "disabled");
//                $("#laoResult").attr("disabled", "disabled");
//                
//                $("#examinationAndTest").val("").change();
//                $("#laoTestDate").val("").change();
//                $("#laoResult").val("").change();

                $("#laoSymptoms").val("").change();
                $("#laoTestTime").val("").change();
                $("#laoOtherSymptom").val("").change();
                $("#suspiciousSymptoms").val("").change();
            }
        });

        //suspiciousSymptoms disibale
        if ($($scope.items.suspiciousSymptoms).val() != "1") {
            $("#examinationAndTest").attr("disabled", "disabled");
        }

        $("#suspiciousSymptoms").on("change", function () {
            if ($("#suspiciousSymptoms").val() == "1") {
                $("#examinationAndTest").removeAttr("disabled").change();
            } else {
                $("#examinationAndTest").attr("disabled", "disabled");
                $("#examinationAndTest").val("").change();
            }
        });

        //examinationAndTest disibale
        if ($($scope.items.examinationAndTest).val() != "1") {
            $("#laoTestDate").attr("disabled", "disabled");
            $("#laoResult").attr("disabled", "disabled");
            $("#laoDiagnose").attr("disabled", "disabled");
        }

        $("#examinationAndTest").on("change", function () {
            if ($("#examinationAndTest").val() == "1") {
                $("#laoTestDate").removeAttr("disabled").change();
                $("#laoResult").removeAttr("disabled").change();
                $("#laoDiagnose").removeAttr("disabled").change();
            } else {
                $("#laoTestDate").attr("disabled", "disabled");
                $("#laoResult").attr("disabled", "disabled");
                $("#laoDiagnose").attr("disabled", "disabled");
                $("#laoTestDate").val("").change();
                $("#laoResult").val("").change();
                $("#laoDiagnose").val("").change();
            }
        });

        $($scope.items.laoSymptoms).change(function () {
            var laoSymptoms = $($scope.items.laoSymptoms).val();
            if (laoSymptoms != null && (laoSymptoms.indexOf("2") != '-1' ||
                    laoSymptoms.indexOf("3") != '-1' ||
                    laoSymptoms.indexOf("4") != '-1' ||
                    laoSymptoms.indexOf("5") != '-1')) {
                $($scope.items.suspiciousSymptoms).val("1").change();
            } else {
                $($scope.items.suspiciousSymptoms).val("").change();
            }
        });



//        $("#laoResult").on("change", function () {
//            if ($("#laoResult").val() == "2") {
//                $("#laoTreatment").removeAttr("disabled").change();
//            } else {
//                $("#laoTreatment").attr("disabled", "disabled");
//                $("#laoTreatment").val("").change();
//            }
//        });
        $("#laoTreatment").on("change", function () {
            if ($("#laoTreatment").val() == "1") {
                $("#laoStartTime").removeAttr("disabled").change();
                $("#laoEndTime").removeAttr("disabled").change();
            } else {
                $("#laoStartTime").attr("disabled", "disabled");
                $("#laoStartTime").val("").change();
                $("#laoEndTime").attr("disabled", "disabled");
                $("#laoEndTime").val("").change();
            }
        });
        //inh dissible
        if ($($scope.items.inh).val() != "1") {
            $("#inhFromTime").attr("disabled", "disabled");
            $("#inhEndCauses").attr("disabled", "disabled");
            $("#inhToTime").attr("disabled", "disabled");
        }
        $("#inh").on("change", function () {
            if ($("#inh").val() == "1") {
                $("#inhFromTime").removeAttr("disabled").change();
                $("#inhToTime").removeAttr("disabled").change();
                $("#inhEndCauses").removeAttr("disabled").change();
            } else {
                $("#inhFromTime").attr("disabled", "disabled");
                $("#inhEndCauses").attr("disabled", "disabled");
                $("#inhToTime").attr("disabled", "disabled");
                $("#inhEndCauses").val("").change();
                $("#inhFromTime").val("").change();
                $("#inhToTime").val("").change();
            }
        });
        if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
            var count = 0;
            for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                    count++;
                }
            }
            if (count > 0) {
                $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
            } else {
                $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
            }
        } else {
            $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
            $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
        }
        $($scope.items.cotrimoxazoleEndCauses).change(function () {
            if ($($scope.items.cotrimoxazoleEndCauses).val() != null) {
                var count = 0;
                for (var s in $($scope.items.cotrimoxazoleEndCauses).val()) {
                    if ('-1' == $($scope.items.cotrimoxazoleEndCauses).val()[s]) {
                        count++;
                    }
                }
                if (count > 0) {
                    $($scope.items.cotrimoxazoleOtherEndCause).removeAttr("disabled").change();
                } else {
                    $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                    $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
                }
            } else {
                $($scope.items.cotrimoxazoleOtherEndCause).attr("disabled", "disabled").change();
                $($scope.items.cotrimoxazoleOtherEndCause).val("").change();
            }
        });
//        //ntch dissible
        if ($($scope.items.ntch).val() != "1") {
            $("#ntchSymptoms").attr("disabled", "disabled");
            $("#ntchOtherSymptom").attr("disabled", "disabled");
        }
        $("#ntch").on("change", function () {
            if ($("#ntch").val() == "1") {
                $("#ntchSymptoms").removeAttr("disabled").change();
                $("#ntchOtherSymptom").removeAttr("disabled").change();
            } else {
                $("#ntchSymptoms").attr("disabled", "disabled");
                $("#ntchOtherSymptom").attr("disabled", "disabled");
                $("#ntchSymptoms").val("").change();
                $("#ntchOtherSymptom").val("").change();
            }
        });
        if ($($scope.items.cd4TestSiteID).val() != "-1") {
            $("#cd4TestSiteName").attr("disabled", "disabled");
        }

        $("#cd4TestSiteID").on("change", function () {
            if ($("#cd4TestSiteID").val() == "-1") {
                $("#cd4TestSiteName").removeAttr("disabled").change();
            } else {
                $("#cd4TestSiteName").attr("disabled", "disabled");
                $("#cd4TestSiteName").val("").change();
            }
        });
        if ($($scope.items.hbv).val() != "1") {
            $("#hbvTime").attr("disabled", "disabled");
            $("#hbvResult").attr("disabled", "disabled");
            $("#hbvCase").attr("disabled", "disabled");
        }
        $("#hbv").on("change", function () {
            if ($("#hbv").val() == "1") {
                $("#hbvTime").removeAttr("disabled").change();
                $("#hbvResult").removeAttr("disabled").change();
                $("#hbvCase").removeAttr("disabled").change();
            } else {
                $("#hbvTime").attr("disabled", "disabled");
                $("#hbvResult").attr("disabled", "disabled");
                $("#hbvCase").attr("disabled", "disabled");
                $("#hbvTime").val("").change();
                $("#hbvResult").val("").change();
                $("#hbvCase").val("").change();
            }
        });
        if ($($scope.items.hcv).val() != "1") {
            $("#hcvTime").attr("disabled", "disabled");
            $("#hcvResult").attr("disabled", "disabled");
            $("#hcvCase").attr("disabled", "disabled");
        }
        $("#hcv").on("change", function () {
            if ($("#hcv").val() == "1") {
                $("#hcvTime").removeAttr("disabled").change();
                $("#hcvResult").removeAttr("disabled").change();
                $("#hcvCase").removeAttr("disabled").change();
            } else {
                $("#hcvTime").attr("disabled", "disabled");
                $("#hcvResult").attr("disabled", "disabled");
                $("#hcvCase").attr("disabled", "disabled");
                $("#hcvTime").val("").change();
                $("#hcvResult").val("").change();
                $("#hcvCase").val("").change();
            }
        });

        if ($scope.statusOfTreatmentID !== '0') {
            $("#statusOfTreatmentID option[value='0']").hide();
        }
    };
});
app.controller('opc_test_view', function ($scope, msg, localStorageService) {

    $scope.stageID = utils.getContentOfDefault(form.stageID, '');
    $scope.lao = utils.getContentOfDefault(form.lao, '');
    $scope.laoTestTime = utils.getContentOfDefault(form.laoTestTime, '');
    $scope.laoStartTime = utils.getContentOfDefault(form.laoStartTime, '');
    $scope.laoEndTime = utils.getContentOfDefault(form.laoEndTime, '');
    $scope.inhFromTime = utils.getContentOfDefault(form.inhFromTime, '');
    $scope.inhToTime = utils.getContentOfDefault(form.inhToTime, '');
    $scope.cotrimoxazoleFromTime = utils.getContentOfDefault(form.cotrimoxazoleFromTime, '');
    $scope.cotrimoxazoleToTime = utils.getContentOfDefault(form.cotrimoxazoleToTime, '');
    $scope.cd4SampleTime = utils.getContentOfDefault(form.cd4SampleTime, '');
    $scope.cd4TestTime = utils.getContentOfDefault(form.cd4TestTime, '');

    $scope.cd4ResultTime = utils.getContentOfDefault(form.cd4ResultTime, '');
    $scope.cd4RetryTime = utils.getContentOfDefault(form.cd4RetryTime, '');
    $scope.hbvTime = utils.getContentOfDefault(form.hbvTime, '');
    $scope.hcvTime = utils.getContentOfDefault(form.hcvTime, '');

    $scope.form = form;
    $scope.items = {
        ID: "#ID",
        siteID: "#siteID",
        lao: "#lao",
        laoTestTime: "#laoTestTime",
        laoOtherSymptom: "#laoOtherSymptom",
        inh: "#inh",
        inhFromTime: "#inhFromTime",
        inhToTime: "#inhToTime",
        ntch: "#ntch",
        ntchOtherSymptom: "#ntchOtherSymptom",
        cotrimoxazoleFromTime: "#cotrimoxazoleFromTime",
        cotrimoxazoleToTime: "#cotrimoxazoleToTime",
        cd4SampleTime: "#cd4SampleTime",
        cd4TestTime: "#cd4TestTime",
        firstCd4Time: "#firstCd4Time",
        cd4TestSiteID: "#cd4TestSiteID",
        cd4TestSiteName: "#cd4TestSiteName",
        cd4Result: "#cd4Result",
        cd4ResultTime: "#cd4ResultTime",
        cd4RetryTime: "#cd4RetryTime",
        hbv: "#hbv",
        hbvTime: "#hbvTime",
        hbvResult: "#hbvResult",
        hbvCase: "#hbvCase",
        hcv: "#hcv",
        hcvTime: "#hcvTime",
        hcvResult: "#hcvResult",
        hcvCase: "#hcvCase",
        note: "#note",
        laoResult: "#laoResult",
        laoTreatment: "#laoTreatment",
        laoStartTime: "#laoStartTime",
        laoEndTime: "#laoEndTime",
        stageID: "#stageID",
        laoSymptoms: "#laoSymptoms",
        inhEndCauses: "#inhEndCauses",
        ntchSymptoms: "#ntchSymptoms",
        cotrimoxazoleEndCauses: "#cotrimoxazoleEndCauses",
        cd4Causes: "#cd4Causes"
    };


    //kiểm tra dữ liệu
    $scope.init = function () {

        $scope.$parent.select_mutiple($scope.items.laoSymptoms, "  ");
        $scope.$parent.select_mutiple($scope.items.ntchSymptoms, "   ");
        $scope.$parent.select_mutiple($scope.items.inhEndCauses, "   ");
        $scope.$parent.select_mutiple($scope.items.cotrimoxazoleEndCauses, "   ");
        $scope.$parent.select_mutiple($scope.items.cd4Causes, "   ");
        $scope.$parent.select_search($scope.items.stageID, "   ");

        $("#testFormView input, #testFormView select, #testFormView textarea").attr({disabled: "disabled"});



        $("#testFormView option").text(function () {
            return $(this).text().replace("Chọn kết quả", "    ");
        });
        $("#testFormView option").text(function () {
            return $(this).text().replace("Chọn nơi xét nghiệm", "    ");
        });
        $("#suspiciousSymptoms option").text(function () {
            return $(this).text().replace("Chọn triệu chứng", "    ");
        });
        $("#laoDiagnose option").text(function () {
            return $(this).text().replace("Chọn loại hình Lao", "    ");
        });


    };


});
