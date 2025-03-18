app.controller('site', function ($scope) {

    $.validator.addMethod("code", function (value, element) {
        return this.optional(element) || /^[a-zA-Z0-9\-]+$/i.test(value);
    }, "Mã cơ sở chỉ chứa kí tự chữ, số và dấu -");
    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            code: 'required code',
            name: {
                required: true,
                minlength: 3,
                maxlength: 100
            },
            description: {required: false, maxlength: 500},
            provinceID: {required: true},
            districtID: {required: true},
            wardID: {required: true},
            address: {required: true},
            email: {required: true, email: true},
            phone: {required: true},
            service: {required: true},
            shortName: {required: true},
            contactPosition: {required: true},
            isActive: {required: false},
            parentID: {required: false},
            type: {required: false}
        },
        messages: {
            code: {
                required: "Mã cơ sở không được để trống."
            },
            name: {
                required: "Tên cơ sở không được để trống.",
                minlength: "Tên cơ sở có độ dài tối thiểu 3 kí tự.",
                maxlength: "Tên cơ sở có độ dàu tối đa 100 ký tự."
            },
            description: {maxlength: "Mô tả có độ dài tối đa 500 kí tự."},
            provinceID: {required: "Tỉnh thành không được để trống."},
            districtID: {required: "Quận huyện không được để trống."},
            wardID: {required: "Phường xã không được để trống."},
            address: {required: "Địa chỉ cơ sở không được để trống."},
            email: {
                required: "Email không được để trống.",
                email: "Email không đúng định dạng."
            },
            phone: {required: "Số điện thoại không được để trống."},
            service: {required: "Dịch vụ không được để trống."},
            shortName: {required: "Tên viết tắt không được để trống."},
            contactPosition: {required: "Chức vụ không được để trống."}
        }
    });
    $scope.isShow = true;
    $scope.init = function () {
        $scope.treetable();
        $scope.treetableShow();
        $scope.initProvince("#provinceID", "#districtID", "#wardID");
        $scope.addressAutocomplete("#address", "#provinceID", "#districtID", "#wardID");
        $scope.select_search("#provinceID", "Chọn tỉnh thành");
        $scope.select_search("#districtID", "Chọn quận huyện");
        $scope.select_search("#wardID", "Chọn phường xã");
        
        $scope.select_mutiple("#service", "Chọn dịch vụ");
        $scope.select_search("#parentID", "Chọn cơ sở quản lý trực tiếp");
    };
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

app.controller('site_config', function ($scope, msg) {

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

        $scope.$parent.select_mutiple("#l_s_biology", "Chọn sinh phẩm");
        $scope.$parent.select_mutiple("#s_biology", "Chọn sinh phẩm");
        $scope.$parent.select_mutiple("#h_s_biology", "Chọn sinh phẩm");
        $scope.$parent.select_mutiple("#opc_regimen", "Chọn phác đồ");
        $("#s_biology, #h_s_biology, #l_s_biology, #opc_regimen").on('change', function () {
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
            url: '/service/site-config/save.json',
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