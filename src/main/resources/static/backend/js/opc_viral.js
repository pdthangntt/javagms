app.controller('opc_viral', function ($scope, msg, $uibModal) {
    $scope.arv = arv;
    $scope.tab = tab;
    $scope.options = options;
    $scope.stages = stages;
    $scope.stagesLast = stagesLast;

    $scope.currentSiteID = currentSiteID;
    $scope.init = function () {
        $scope.switchConfig();
    };

    $scope.create = function () {
        $uibModal.open({
            animation: true,
            backdrop: 'static',
            templateUrl: 'opcViral',
            controller: 'opc_viral_form',
            size: 'lg',
            resolve: {
                params: function () {
                    return {
                        action: "create",
                        currentSiteID: $scope.currentSiteID,
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
                        templateUrl: 'opcViral',
                        controller: 'opc_viral_form',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    action: "update",
                                    model: resp.data,
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
                        templateUrl: 'opcViral',
                        controller: 'opc_viral_form',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    action: "view",
                                    model: resp.data,
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
            templateUrl: 'opcViralLogs',
            controller: 'opc_viral_log',
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
    if (tab == "new") {
        $scope.create();
    }
});

app.controller('opc_viral_form', function ($scope, $uibModalInstance, params, msg) {

    $scope.arv = arv;
    $scope.options = options;
    $scope.stages = stages;
    $scope.item = form;
    $scope.oid = params.oid;
    $scope.action = params.action;
    $scope.currentSiteID = currentSiteID;
    $scope.stagesLast = stagesLast;


    $scope.viralloadSymtom = $scope.options["viralload-symtom"];
    $scope.siteConfirm = $scope.options["siteCd4"];
    $scope.results = $scope.options["virus-load"];
    $scope.stage = $scope.stages;

//    delete $scope.viralloadSymtom[""];
    delete $scope.siteConfirm[""];
    delete $scope.results[""];

    $scope.init = function () {
        if ($scope.action === "create" && $scope.item.testSiteID == null) {
            $scope.item.testSiteID = '' + $scope.currentSiteID;
        }
        if ($scope.action === "create" && $scope.item.stageID == null) {
            $scope.item.stageID = '' + $scope.stagesLast;
        }

        if ($scope.action === "view") {
            $('#opcViralForm input, #opcViralForm select').attr('disabled', 'disabled');
        }

        if (params.model) {
            $scope.item = params.model;
        }
        setTimeout(() => {
            params.select_search("#testSiteID", "Chọn nơi xét nghiệm");
            params.select_mutiple("#causes", "Chọn câu trả lời");
            $("#causes").val($scope.item.causes).trigger('change');
        }, 100);

        if ($scope.action !== "view") {
            $('#testSiteID').append($('<option>', {value: '', text: 'Chọn nơi xét nghiệm'}));
            $('#result').append($('<option>', {value: '', text: 'Chọn kết quả xn tải lượng virus'}));
            $('#causes').append($('<option>', {value: '', text: 'Chọn lý do xét nghiệm'}));
            $('#stageID').append($('<option>', {value: '', text: 'Chọn giai đoạn điều trị'}));
        }

        $("#causes").on("change", () => {
            $("#causes option[value='']").remove();
        })

        if ($scope.item !== null && $scope.item.testSiteID === "-1" && $scope.action !== "view") {
            $("#testSiteName").removeAttr("disabled").val('').change();
        }

        if ($("#testSiteID").on("change", () => {
            if ($("#testSiteID").val() === "string:-1") {
                $("#testSiteName").removeAttr("disabled").val('').change();
            } else {
                $("#testSiteName").attr('disabled', 'disabled').val('').change();
            }
        }))
            ;
        if ($("#result").on("change", () => {

            if ($("#testTime").val() != null) {
                if (($("#result").val() === "string:3" || $("#result").val() === "string:4") && $("#testTime").val() !== "") {

                    var from = $("#testTime").val().split("/")
                    var date = new Date(from[2], from[1] - 1, from[0])

                    date.setDate(date.getDate() + 84);

                    var month = date.getMonth() + 1;
                    var day = date.getDate();
                    var year = date.getFullYear();
                    var final = (day < 10 ? "0" + day : day) + "/" + (month < 10 ? "0" + month : month) + "/" + year;

                    $("#retryTime").val(final).change();



                } else {
                    $("#retryTime").val('').change();
                }
            }


        }))
            ;
        if ($("#testTime").on("change", () => {

            if ($("#result").val() != null) {
                if (($("#result").val() === "string:3" || $("#result").val() === "string:4") && $("#testTime").val() !== "") {

                    var from = $("#testTime").val().split("/")
                    var date = new Date(from[2], from[1] - 1, from[0])

                    date.setDate(date.getDate() + 84);

                    var month = date.getMonth() + 1;
                    var day = date.getDate();
                    var year = date.getFullYear();
                    var final = (day < 10 ? "0" + day : day) + "/" + (month < 10 ? "0" + month : month) + "/" + year;

                    $("#retryTime").val(final).change();



                } else {
                    $("#retryTime").val('').change();
                }
            }


        }))
            ;
    };

    $scope.ok = function () {

        let stageID = "";
        stageID = typeof $scope.item == 'undefined' || $scope.item == null || $scope.item === '' ? "" : $scope.item.id;

        let url = urlCreate + "?arvid=" + $scope.arv.id;
        if (stageID !== null && stageID !== "") {
            url = urlUpdate + "?arvid=" + $scope.arv.id + "&id=" + stageID;
        }

        // Lấy giá trị chọn nhiều
        $scope.item.causes = $("#causes").val();

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
                    $scope.errors = null;
                    if (resp.success) {
                        msg.success(resp.message, function () {
                            location.href = "/backend/opc-viralload/update.html?arvid=" + $scope.arv.id;
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

app.controller('opc_viral_log', function ($scope, $uibModalInstance, params) {
    $scope.model = {staffID: 0, viralLoadID: params.oid, code: params.code};
    $scope.oid = params.oid;

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
