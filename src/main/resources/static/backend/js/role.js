app.controller('role_form', function ($scope, msg) {
    $scope.event = null;
    $scope.delayTime = 0;

    $scope.init = function () {
        $scope.switchConfig();

        var elmService = $("input.checkbox-switch-service");
        var elmItem = $("input.checkbox-switch");
        elmService.change(function () {
            elmItem.parent().hide();
            angular.forEach(elmService, function (elm) {
                var code = $(elm).attr("value");
                if ($(elm).is(':checked') && typeof assignmentServices[code] !== 'undefined') {
                    angular.forEach(elmItem, function (child) {
                        var val = Number($(child).attr("value"));
                        if (assignmentServices[code].indexOf(val) !== -1) {
                            $(child).parent().show();
                        } else if ($(child).is(':checked')) {
                            $(child).trigger('click');
                        }
                    });
                }
            });
        });

        $("input[data-key-p]").each(function () {
            new Switchery(this, {
                size: 'small',
                secondaryColor: '#a94442',
                jackColor: '#fff',
                offJackColor: '#fff'
            });
        });
        $("input[data-key-p]").change(function () {
            let key = $(this).attr("data-key-p");
            let check = $(this).is(':checked');
            $('input[data-key=' + key + ']').each(function () {
                $scope.changeSwitchery($(this), check);
            });
        });
    };

    $scope.checkAll = function () {
        $('input.checkbox-switch').each(function () {
            $scope.changeSwitchery($(this), true);
        });
    };

    $scope.unCheckAll = function () {
        $('input.checkbox-switch').each(function () {
            $scope.changeSwitchery($(this), false);
        });
    };

    $scope.switchConfig = function () {
        var options = {
            size: 'small',
            secondaryColor: '#a94442',
            jackColor: '#fff',
            offJackColor: '#fff'
        };

        $('input.checkbox-switch').each(function (_index, o) {
            var value = Number($(this).val());
            if (typeof roles != 'undefined' && roles != null && roles.length > 0 && roles.includes(value)) {
                $(this).attr("checked", "checked");
            }
            new Switchery(this, options);
        });

        if ($('input.checkbox-switch-service').length > 0) {
            $('input.checkbox-switch-service').each(function (_index, o) {
                var value = String($(this).val());
                if (typeof services != 'undefined' && services != null && services.length > 0 && services.includes(value)) {
                    $(this).attr("checked", "checked");
                }
                new Switchery(this, options);
            });
        }
    };
});

app.controller('role', function ($scope, msg) {
    $scope.event = null;
    $scope.delayTime = 0;

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            name: {
                required: true,
                minlength: 3,
                maxlength: 100
            },
            action: {
                required: false
            }
        },
        messages: {
            name: {
                required: "Tên quyền không được để trống.",
                minlength: "Tên quyền có độ dài tối thiểu 3 kí tự.",
                maxlength: "Tên quyền có độ dàu tối đa 100 ký tự."
            }
        }
    });

    $scope.init = function () {
        $scope.switchConfig();
        $scope.eventChange();
        $("input[data-key-p]").each(function () {
            new Switchery(this, {
                size: 'small',
                secondaryColor: '#a94442',
                jackColor: '#fff',
                offJackColor: '#fff'
            });
        });
        $("input[data-key-p]").change(function () {
            let key = $(this).attr("data-key-p");
            let check = $(this).is(':checked');
            $('input[data-key=' + key + ']').each(function () {
                $scope.changeSwitchery($(this), check);
            });
        });
    };

    $scope.changeSwitchery = function (element, checked) {
        if ((element.is(':checked') && checked == false) || (!element.is(':checked') && checked == true)) {
            element.parent().find('.switchery').trigger('click');
        }
    };

    $scope.checkAll = function () {
        $('input.checkbox-switch').each(function () {
            $scope.changeSwitchery($(this), true);
        });
    };

    $scope.unCheckAll = function () {
        $('input.checkbox-switch').each(function () {
            $scope.changeSwitchery($(this), false);
        });
    };

    $scope.switchConfig = function () {
        var options = {
            size: 'small',
            secondaryColor: '#a94442',
            jackColor: '#fff',
            offJackColor: '#fff'
        };

        $('input.checkbox-switch').each(function (_index, o) {
            var value = Number($(this).val());
            if (typeof roles != 'undefined' && roles != null && roles.length > 0 && roles.includes(value)) {
//                $scope.switchs[_index].on();
                $(this).attr("checked", "checked");
            }
            new Switchery(this, options);
        });

        $('input.checkbox-switch-service').each(function (_index, o) {
            var value = String($(this).val());
            if (typeof services != 'undefined' && services != null && services.length > 0 && services.includes(value)) {
                $(this).attr("checked", "checked");
            }
            new Switchery(this, options);

        });
    };

    $scope.eventChange = function () {
        $('#role-form').find('input').blur(function () {
            var object = this;

            var name = $(object).attr("data-name");
            $scope.loading(true, '', object);

            clearTimeout($scope.event);
            $scope.event = setTimeout(function () {
                var val = $(object).val();
                if (val == '') {
                    $scope.loading(false, 'fa fa-times-circle text-danger', object);
                    return false;
                }
                var data = {
                    name: name,
                    title: val
                };
                $scope.save(data, object);
            }, $scope.delayTime);
        });
    };

    $scope.save = function (data, object) {
        $.ajax({
            url: '/service/role/save-action-define.json',
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