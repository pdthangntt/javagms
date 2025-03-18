app.controller('pqm_vct', function ($scope, $uibModal, msg) {
    
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#objectGroupID", "Tất cả");
        $scope.select_mutiple("#sites", "Tất cả");
        $scope.confirmTimeFrom = $.getQueryParameters().confirmTimeFrom;
        $scope.confirmTimeTo = $.getQueryParameters().confirmTimeTo;
        $scope.earlyHivDateFrom = $.getQueryParameters().earlyHivDateFrom;
        $scope.earlyHivDateTo = $.getQueryParameters().earlyHivDateTo;
        $scope.exchangeTimeFrom = $.getQueryParameters().exchangeTimeFrom;
        $scope.exchangeTimeTo = $.getQueryParameters().exchangeTimeTo;
        $scope.registerTherapyTimeFrom = $.getQueryParameters().registerTherapyTimeFrom;
        $scope.registerTherapyTimeTo = $.getQueryParameters().registerTherapyTimeTo;
    };

    $scope.view = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pqm-vct-view',
                        controller: 'pqm_vct_controller',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    item: resp.data.item,
                                    options: resp.data.options,
                                    uiModals: $uibModal,
                                    parent: $scope.$parent,
                                    dialogReport: $scope.dialogReport
                                };
                            }
                        }
                    });
                }
            }
        });
    };


});

app.controller('pqm_vctss', function ($scope, $uibModal, msg) {
    
     $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#objectGroupID", "Tất cả");
        $scope.select_mutiple("#sites", "Tất cả");
        $scope.confirmTimeFrom = $.getQueryParameters().confirmTimeFrom;
        $scope.confirmTimeTo = $.getQueryParameters().confirmTimeTo;
        $scope.earlyHivDateFrom = $.getQueryParameters().earlyHivDateFrom;
        $scope.earlyHivDateTo = $.getQueryParameters().earlyHivDateTo;
        $scope.exchangeTimeFrom = $.getQueryParameters().exchangeTimeFrom;
        $scope.exchangeTimeTo = $.getQueryParameters().exchangeTimeTo;
        $scope.registerTherapyTimeFrom = $.getQueryParameters().registerTherapyTimeFrom;
        $scope.registerTherapyTimeTo = $.getQueryParameters().registerTherapyTimeTo;
    };

});

app.controller('pqm_logs', function ($scope, $uibModal, msg) {
    if ($.getQueryParameters().fullname != null ||
            $.getQueryParameters().site != null 
            ) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#site", "Tất cả");
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;
    };



});

app.controller('pqm_vct_controller', function ($scope, $uibModalInstance, msg, params) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.init = function () {
        $("#code").val($scope.item.code);
        $("#patientName").val($scope.item.patientName);
        $("#yearOfBirth").val($scope.item.yearOfBirth);
        $("#genderID").val($scope.options["gender"][$scope.item.genderID]);
        $("#identityCard").val($scope.item.identityCard);
        $("#objectGroupID").val($scope.options['test-object-group'][$scope.item.objectGroupID]);
        $("#permanentAddress").val($scope.item.permanentAddress);
        $("#currentAddress").val($scope.item.currentAddress);
        $("#advisoryeTime").val($scope.getDateFormat($scope.item.advisoryeTime));
        $("#preTestTime").val($scope.getDateFormat($scope.item.preTestTime));
        $("#testResultsID").val($scope.options['test-results'][$scope.item.testResultsID + '']);
        $("#confirmTime").val($scope.getDateFormat($scope.item.confirmTime));
        $("#confirmTestNo").val($scope.item.confirmTestNo);
        $("#confirmResultsID").val($scope.options['test-result-confirm'][$scope.item.confirmResultsID + '']);
        $("#earlyHivDate").val($scope.getDateFormat($scope.item.earlyHivDate));
        $("#earlyDiagnose").val($scope.options['early-diagnose'][$scope.item.earlyDiagnose]);
        $("#resultsSiteTime").val($scope.getDateFormat($scope.item.resultsSiteTime));
        $("#resultsTime").val($scope.getDateFormat($scope.item.resultsTime));
        $("#exchangeTime").val($scope.getDateFormat($scope.item.exchangeTime));
        $("#registerTherapyTime").val($scope.getDateFormat($scope.item.registerTherapyTime));
        $("#therapyNo").val($scope.item.therapyNo);
        $("#registeredTherapySite").val($scope.item.registeredTherapySite);
        $("#insuranceNo").val($scope.item.insuranceNo);
        $("#patientPhone").val($scope.item.patientPhone);
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.getDateFormat = function (date) {
        if (date == null || date == '') {
            return '';
        }

        var today = new Date(date);
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = today.getFullYear();

        today = dd + '/' + mm + '/' + yyyy;

        return today;
    };
});


app.controller('pqm_prep', function ($scope, $uibModal, msg) {
    
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#objectGroupID", "Tất cả");
        $scope.select_mutiple("#sites", "Tất cả");
        $scope.startTimeFrom = $.getQueryParameters().startTimeFrom;
        $scope.startTimeTo = $.getQueryParameters().startTimeTo;
        $scope.examinationTimeFrom = $.getQueryParameters().examinationTimeFrom;
        $scope.examinationTimeTo = $.getQueryParameters().examinationTimeTo;
    };

    $scope.view = function (oid) {
        loading.show();
        $.ajax({
            url: urlGet,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'pqm-prep-view',
                        controller: 'pqm_prep_controller',
                        size: 'lg',
                        resolve: {
                            params: function () {
                                return {
                                    oid: oid,
                                    item: resp.data.item,
                                    options: resp.data.options,
                                    mapStage: resp.data.mapStage,
                                    mapStageType: resp.data.mapStageType,
                                    visit: resp.data.itemVisit,
                                    uiModals: $uibModal,
                                    parent: $scope.$parent,
                                    dialogReport: $scope.dialogReport
                                };
                            }
                        }
                    });
                }
            }
        });
    };


});

app.controller('pqm_arv', function ($scope, $uibModal, msg) {
    
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#objectGroupID", "Tất cả");
        $scope.select_search("#statusOfTreatmentID", "Tất cả");
        $scope.select_mutiple("#sites", "Tất cả");
        $scope.treatmentTimeFrom = $.getQueryParameters().treatmentTimeFrom;
        $scope.treatmentTimeTo = $.getQueryParameters().treatmentTimeTo;
    };
});




app.controller('pqm_drug', function ($scope, $uibModal, msg) {
        
    $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#sites", "Tất cả");
        $scope.select_search("#month", "");
        $scope.select_search("#year", "");
    };
});
app.controller('pqm_drug_elmise', function ($scope, $uibModal, msg) {
        $scope.urlExcel = urlExcel;
    
    
    $scope.getUrl = function (url) {
        return url + $scope.getQueryStringInForm("#search");
    };
    
    $scope.excel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };
    
    
    
    $scope.init = function () {
        $scope.switchConfig();
        $(".radio-cust, .checkbox-cust").iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        $scope.select_search("#sites", "Tất cả");
        $scope.select_search("#quarter", "");
        $scope.select_search("#year", "");
    };
});

app.controller('pqm_prep_controller', function ($scope, $uibModalInstance, msg, params) {
    $scope.options = params.options;
    $scope.item = params.item;
    $scope.visit = params.visit;
    $scope.mapStage = params.mapStage;
    $scope.mapStageType = params.mapStageType;
    $scope.init = function () {
        $("#code").val($scope.item.code);
        $("#fullName").val($scope.item.fullName);
        $("#insuranceNo").val($scope.item.insuranceNo);
        $("#patientPhone").val($scope.item.patientPhone);
        $("#yearOfBirth").val($scope.item.yearOfBirth);
        $("#genderID").val($scope.options["gender"][$scope.item.genderID]);
        $("#identityCard").val($scope.item.identityCard);
        $("#objectGroupID").val($scope.options['test-object-group'][$scope.item.objectGroupID]);
        $("#startTime").val($scope.getDateFormat($scope.item.startTime));
        $("#endTime").val($scope.getDateFormat($scope.item.endTime));
        $("#type").val($scope.options['type'][$scope.item.type]);
        
        $("#source").val($scope.item.source);
        $("#otherSite").val($scope.item.otherSite);
        $("#projectSupport").val($scope.item.projectSupport);


    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.getDateFormat = function (date) {
        if (date == null || date == '') {
            return '';
        }

        var today = new Date(date);
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = today.getFullYear();

        today = dd + '/' + mm + '/' + yyyy;

        return today;
    };
});