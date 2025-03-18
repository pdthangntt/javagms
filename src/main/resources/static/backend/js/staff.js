app.controller('staff', function ($scope) {

    $.validator.addMethod("username", function (value, element) {
        return this.optional(element) || /^[a-z0-9_-]{6,30}$/i.test(value);
    }, "Tên đăng nhập không đúng định dạng. Chỉ chưa các kí tự 'a-z', '0-9', '_' và '-'");

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            name: {
                required: true
            },
            phone: {
                required: true
            },
            email: {
                required: false,
                email: true
            },
            isActive: {
                required: false
            },
            roleID: {
                required: false
            },
            pwd: {
                required: true,
                minlength: 6
            },
            confirmPwd: {
                required: true,
                minlength: 6,
                equalTo: "#pwd"
            }
        },
        messages: {
            name: {
                required: "Họ và tên không được để trống"
            },
            phone: {
                required: "Số điện thoại không được để trống"
            },
            email: {
                email: "Email không đúng định dạng"
            },
            pwd: {
                required: "Mật khẩu không được để trống",
                minlength: "Mật khẩu có độ dài tối thiểu 6 kí tự"
            },
            confirmPwd: {
                required: "Nhập lại mật khẩu không được để trống",
                minlength: "Nhập lại mật khẩu có độ dài tối thiểu 6 kí tự",
                equalTo: "Mật khẩu nhập lại không chính xác."
            }
        }
    });

    $scope.init = function () {
        $scope.$parent.select_mutiple("#roleID", "Chọn quyền truy cập");
    };
});

app.controller('staff_admin', function ($scope, pacPatientService) {
    $scope.isQueryString = ($.getQueryParameters().sid != null);

    $scope.init = function () {
        $scope.switchConfig();
        $scope.sid = $.getQueryParameters().sid;
        $scope.select_search("#sid", "Tất cả");

//        $scope.initProvince("#detect_province_id"); //Chỉ có tỉnh thành
//        $scope.initProvince("#permanent_province_id", "#permanent_district_id", "#permanent_ward_id");
    };
    
});

app.controller('staff_admin_update', function ($scope) {

    $.validator.addMethod("username", function (value, element) {
        return this.optional(element) || /^[a-z0-9_-]{6,30}$/i.test(value);
    }, "Tên đăng nhập không đúng định dạng. Chỉ chưa các kí tự 'a-z', '0-9', '_' và '-'");

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            name: {
                required: true
            },
            phone: {
                required: true
            },
            email: {
                required: false,
                email: true
            },
            isActive: {
                required: false
            },
            roleID: {
                required: false
            },
            pwd: {
                minlength: 6
            },
            confirmPwd: {
                minlength: 6,
                equalTo: "#pwd"
            }
        },
        messages: {
            name: {
                required: "Họ và tên không được để trống"
            },
            phone: {
                required: "Số điện thoại không được để trống"
            },
            email: {
                email: "Email không đúng định dạng"
            },
            pwd: {
                minlength: "Mật khẩu có độ dài tối thiểu 6 kí tự"
            },
            confirmPwd: {
                minlength: "Nhập lại mật khẩu có độ dài tối thiểu 6 kí tự",
                equalTo: "Mật khẩu nhập lại không chính xác."
            }
        }
    });

    $scope.init = function () {
        $scope.$parent.select_mutiple("#roleID", "Chọn quyền truy cập");
    };
});

app.controller('log_index', function ($scope, msg, $uibModal, htcConfirmService) {
    if ($.getQueryParameters().id != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.init = function () {
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;

    };

});

app.controller('log_api_arvindex', function ($scope, msg, $uibModal, htcConfirmService) {
    if ($.getQueryParameters().status != null) {
        $scope.isQueryString = true;
    } else {
        $scope.isQueryString = false;
    }
    $scope.init = function () {
        $scope.from = $.getQueryParameters().from;
        $scope.to = $.getQueryParameters().to;

    };

});