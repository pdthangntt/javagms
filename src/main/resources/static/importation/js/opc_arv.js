app.controller('import_opc_arv', function ($scope) {

    $scope.addError = function (elm, message) {
        elm.parent().addClass('has-error');
        if (typeof elm.parent().find("div.help-block") != 'undefined' && elm.parent().find("div.help-block").length == 0) {
            elm.after('<div class="help-block help-block-error">' + message + '</div>');
        }
    };

    $scope.removeError = function (elm) {
        elm.parent().removeClass('has-error');
        elm.parent().find("div.help-block").remove();
    };

    $scope.init = function () {
        angular.forEach($("tr[data-row]"), function (elm) {
            var index = $(elm).attr("data-row");
            $scope.initProvince('#permanentProvinceID_' + index, '#permanentDistrictID_' + index, '#permanentWardID_' + index);
            $scope.initProvince('#currentProvinceID_' + index, '#currentDistrictID_' + index, '#currentWardID_' + index);

        });

        setTimeout(function () {
            $("select[data-content]").each(function () {
                var elm = this;
                var ehead = $(elm).attr("data-content");
                $(elm).val($(elm).find("option:contains(" + ehead + ")").attr("value")).change();
            });
            $("select[data-value]").each(function () {
                var elm = this;
                var evalue = $(elm).attr("data-value");
                $(elm).val(evalue).change();
            });
        }, 300);
    };

    $scope.loading = function (_show, tr, _icon) {
        if (_show === true) {
            $(tr).find("td").first().html('<i class="fa fa-sun-o fa-spin" for="loading" style="border-right:none;" ></i>');
        } else {
            $(tr).find("td").first().html('<i class="' + _icon + '" ></i> ');
        }
    };

    $scope.save = function () {
        $("tr[data-row]").each(function () {
            var elm = this;
            var attrId = $(elm).attr('data-db-id');
            if (typeof attrId !== typeof undefined && attrId !== false) {
                console.log("Saved!");
            } else {
                $scope.loading(true, elm);
                var item = {};
                $(elm).find("input, select, textarea").each(function () {
                    var input = this;
                    var val = $(input).val();
                    var name = $(input).attr("name");
                    if (val != '' && name != null) {
                        item[name] = val;
                    }
                });

                $.ajax({
                    async: false,
                    url: saveUrl,
                    data: JSON.stringify(item),
                    contentType: "application/json",
                    dataType: 'json',
                    method: 'POST',
                    success: function (resp) {
                        $(elm).find("input, select").each(function (i, elmInput) {
                            $scope.removeError($(elmInput));
                        });
                        if (resp.success) {
                            $(elm).find("input, select").attr("disabled", "disabled");
                            $(elm).attr("data-db-id", resp.data.id);
                            $scope.loading(false, elm, "fa fa-check-circle text-success");
                        } else {
                            $scope.loading(false, elm, "fa fa-times-circle text-danger");
                            $.each(resp.data, function (i, error) {
                                var el = $(elm).find("[name=" + error.field + "]");
                                $scope.addError(el, error.defaultMessage);
                            });
                        }
                    },
                    error: function (request, status, error) {
                        console.log(error);
                        $scope.loading(false, elm, "fa fa-times-circle text-danger");
                    }
                });
            }
        });
    };
});