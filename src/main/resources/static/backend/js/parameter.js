app.controller('parameter_new', function ($scope) {
    $scope.init = function () {
        $scope.initProvince("#provinceID", null, null);
        $scope.$parent.select_search("#parentID", "Chọn cấp cha");
        $scope.$parent.select_search("#siteID", "Chọn sở sở");
    };

});

app.controller('parameter_index', function ($scope) {
    $scope.init = function () {
        $scope.$parent.select_search("#siteID", "Chọn cơ sở");
    };

});


app.controller('parameter', function ($scope, msg) {
    $scope.event = null;
    $scope.delayTime = 0;

    $scope.init = function () {
        $scope.eventChange();
    };
    $scope.eventChange = function () {
        $('#config-form').find('input, textarea').blur(function () {
            var object = this;

            var type = $(object).attr("data-type");
            var name = $(object).attr("name");
            $scope.loading(true, '', object);

            clearTimeout($scope.event);
            $scope.event = setTimeout(function () {
                var val = $(object).val();
                if (val == '') {
                    $scope.loading(false, 'fa fa-times-circle text-danger', object);
                    return false;
                }
                var data = {
                    key: name,
                    type: type,
                    value: val
                };
                $scope.save(data, object);
            }, $scope.delayTime);
        });

        $('#config-form').find('select').change(function () {
            var object = this;

            var type = $(object).attr("data-type");
            var name = $(object).attr("name");
            $scope.loading(true, '', object);

            var val = $(object).val();
            if (val == '') {
                $scope.loading(false, 'fa fa-times-circle text-danger', object);
                return false;
            }
            var data = {
                key: name,
                type: type,
                value: val
            };
            $scope.save(data, object);
        });
    };

    $scope.save = function (data, object) {
        $.ajax({
            url: '/service/parameter/save-fast.json',
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