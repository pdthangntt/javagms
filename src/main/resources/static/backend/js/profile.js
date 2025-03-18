app.controller('profile', function ($scope) {

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            username: {
                required: false
            },
            name: {
                required: true,
                minlength: 6,
                maxlength: 30
            },
            phone: {
                required: true
            },
            email: {
                email: true
            }
        },
        messages: {
            name: {
                required: "Họ và tên không được để trống",
                minlength: "Họ tên có độ dài tối thiểu 6 kí tự.",
                maxlength: "Họ tên có độ dài tối đa 30 ký tự."
            },
            phone: {
                required: "Số điện thoại không được để trống"
            },
            email: {
                email: "Email không đúng định dạng"
            }
        }
    });

});

app.controller('profile_pwd', function ($scope) {
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            oldPassword: {
                required: true,
                minlength: 6,
                maxlength: 30
            },
            password: {
                required: true,
                minlength: 6,
                maxlength: 30
            },
            confirmPwd: {
                required: true,
                minlength: 6,
                maxlength: 30,
                equalTo: "#password"
            }
        },
        messages: {
            oldPassword: {
                required: "Mật khẩu cũ không được để trống",
                minlength: "Mật khẩu cũ có độ dài tối thiểu 6 kí tự.",
                maxlength: "Mật khẩu cũ có độ dài tối đa 30 ký tự."
            },
            password: {
                required: "Mật khẩu mới không được để trống",
                minlength: "Mật khẩu mới có độ dài tối thiểu 6 kí tự.",
                maxlength: "Mật khẩu mới có độ dài tối đa 30 ký tự."
            },
            confirmPwd: {
                required: "Nhập lại mật khẩu mới không được thiếu.",
                minlength: "Nhập lại mật khẩu mới có độ dài tối thiểu 6 kí tự.",
                maxlength: "Nhập lại mật khẩu mới có độ dài tối đa 30 ký tự.",
                equalTo: "Nhập lại mật khẩu mới không khớp."
            }
        }
    });
});

app.controller('current_site', function ($scope) {
    $scope.isShow = true;
    $scope.init = function () {
        var htmlSmall = '<small class="form-text text-muted">Liên hệ cơ sở quản lý trực tiếp để thay đổi</small>';
        var province = '<input type="hidden"  id="provinceID" name="provinceID" value="'.concat(form.provinceID,'"/>');
        var district = '<input type="hidden"  id="districtID" name="districtID" value="'.concat(form.districtID,'"/>');
        var ward = '<input type="hidden"  id="wardID" name="wardID" value="'.concat(form.wardID,'"/>');
        $scope.treetable();
        $scope.treetableShow();
        $scope.initProvince("#provinceID", "#districtID", "#wardID");
        $scope.select_search("#provinceID", "Chọn tỉnh thành");
        $scope.select_search("#districtID", "Chọn quận huyện");
        $scope.select_search("#wardID", "Chọn phường xã");
//        $scope.addressAutocomplete("#address", "#provinceID", "#districtID", "#wardID");
        $scope.select_mutiple("#service", "Chọn dịch vụ");
        $("#service").attr("disabled", 'disabled');
        $("#type").attr("disabled", 'disabled');
        $("#parentID").attr("disabled", 'disabled');
        if ($("#type option:selected").val() == "101") {
            $("#provinceID").after(province);
            $("#provinceID").attr("disabled", 'disabled');
            
            
        } else if($("#type option:selected").val() == "102"){
            $("#provinceID").after(province);
            $("#districtID").after(district);
            $("#provinceID").attr("disabled", 'disabled');
            $("#districtID").attr("disabled", 'disabled');
            
        } else if($("#type option:selected").val() == "103") {
            $("#provinceID").after(province);
            $("#districtID").after(district);
            $("#wardID").after(ward);
            $("#provinceID").attr("disabled", 'disabled');
            $("#districtID").attr("disabled", 'disabled');
            $("#wardID").attr("disabled", 'disabled');
            
        }
    };
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            code: {
                required: true,
                minlength: 3,
                maxlength: 30
            },
            type: {required: false},
            isActive: {required: false},
            parentID: {required: false},
            name: {
                required: true,
                minlength: 3,
                maxlength: 100
            },
            description: {
                required: false,
                maxlength: 500
            },
            provinceID: {
                required: true
            },
            districtID: {
                required: true
            },
            wardID: {
                required: true
            },
            address: {
                required: true,
                maxlength: 1000
            },
            email: {
                required: true,
                email: true
            },
            phone: {
                required: true,
                maxlength: 50
            },
            contactName: {
                required: true,
                maxlength: 100
            },
            contactPosition: {
                required: true,
                maxlength: 200
            }

        },
        messages: {
            code: {
                required: "Mã cơ sở không được để trống.",
                minlength: "Mã cơ sở có độ dài tối thiểu 3 kí tự.",
                maxlength: "Mã cơ sở có độ dàu tối đa 50 ký tự."
            },
            name: {
                required: "Tên cơ sở không được để trống.",
                minlength: "Tên cơ sở có độ dài tối thiểu 3 kí tự.",
                maxlength: "Tên cơ sở có độ dàu tối đa 100 ký tự."
            },
            description: {
                maxlength: "Mô tả có độ dài tối đa 500 kí tự."
            },
            provinceID: {
                required: "Tỉnh thành không được để trống."
            },
            districtID: {
                required: "Quận huyện không được để trống."
            },
            wardID: {
                required: "Phường xã không được để trống."
            },
            address: {
                required: "Địa chỉ cơ sở không được để trống.",
                maxlength: "Địa chỉ cơ sở có độ dàu tối đa 1000 ký tự."
            },
            email: {
                required: "Email không được để trống.",
                email: "Email không đúng định dạng."
            },
            phone: {
                required: "Số điện thoại không được để trống.",
                maxlength: "Số điện thoại có độ dài tối đa 50 kí tự."
            },
            contactName: {
                required: "Họ và tên người liên hệ không được để trống.",
                maxlength: "Họ và tên người liên hệ có độ dàu tối đa 100 ký tự."
            },
            contactPosition: {
                required: "Chức vụ người liên hệ không được để trống.",
                maxlength: "Chức vụ người liên hệ có độ dàu tối đa 200 ký tự."
            }
        }
    });

    
    $scope.treetableShow = function () {
        if ($('#table_category').length == 0) {
            return false;
        }
        if ($scope.isShow) {
            jQuery('#table_category').treetable('expandAll');
            $scope.isShow = false;
        } else {
            jQuery('#table_category').treetable('collapseAll');
            $scope.isShow = true;
        }
    };
    $scope.treetable = function () {
        if ($("#table_category").length == 0) {
            return false;
        }

        $("#table_category").treetable({
            expandable: true,
            expanderTemplate: "<a href='#'><i class='fa fa-caret-right'></i></a>",
            iconCollapse: "<i class='fa fa-caret-down'></i>",
            iconExpand: "<i class='fa fa-caret-right'></i>",
            indent: 30
        });
        $("#table_category tbody").on("mousedown", "tr", function () {
            $(".selected").not(this).removeClass("selected");
            $(this).toggleClass("selected");
        });
    };
});

app.controller('profile_config', function ($scope, msg) {

    $scope.init = function () {
        $('#config-form').find('select').each(function () {
            $(this).val($(this).attr("value"));
        });
        $('#config-form').find('input, select, textarea').blur(function () {
            var object = this;
            var val = $(object).val();
            var name = $(object).attr("data-type");
            $scope.loading(true, '', object);

            $scope.save({
                type: "site",
                key: name,
                value: val
            }, object);
        });

        $scope.$parent.select_mutiple("#u_l_objectgroup", "Chọn nhóm đối tượng");
        $scope.$parent.select_mutiple("#u_l_siteconfirm", "Chọn cơ sở");
        $("#u_l_objectgroup, #u_l_siteconfirm").on('change', function () {
            var object = this;
            var val = $(object).val() == null || $(object).val() == '' ? '' : $(object).val().join(",");
            var name = $(object).attr("data-type");
            $scope.loading(true, '', object);

            $scope.save({
                type: "site",
                key: name,
                value: val
            }, object);
        });

    };

    $scope.save = function (data, object) {
        $.ajax({
            url: '/service/profile/save-config.json',
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: 'json',
            method: 'POST',
            success: function (resp) {
                if (!resp.success) {
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                    $scope.loading(false, 'fa fa-ban text-danger', object);
                    return false;
                }
                $scope.loading(false, 'fa fa-check-circle text-success', object);
            }
        });
    };

    $scope.loading = function (_show, _icon, _parent) {
        var _action = '.loading_config';
        if (_show == true) {
            $(_parent).parent().find(_action).html('<i class="fa fa-sun-o fa-spin" for="loading" style="border-right:none;" ></i>');
        } else {
            $(_parent).parent().find(_action).html('<i class="' + _icon + '" ></i> ');
        }
    };
});