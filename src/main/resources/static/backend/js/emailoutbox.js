app.controller('emailoutbox', function ($scope, msg) {

    $scope.init = function () {
//        $scope.switchConfig();
        $scope.sendAt = $.getQueryParameters().send_at;

    };

    $scope.popup1 = {
        opened: false
    };
    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };

    /**
     * @author pdThang
     * thông tin eamil
     * @param {type} oid
     * @returns {undefined}
     */
    $scope.emailDetail = function (oid) {
        loading.show();
        $.ajax({
            url: urlEmailDetail,
            data: {oid: oid},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    bootbox.dialog({
                        title: resp.data.subject,
                        message: "<div class='row'><div class='col-md-12' ><b>Gửi đến</b>: " + resp.data.to + "</div><b>&nbsp;&nbsp;&nbsp;&nbsp;Nội dung:</b> <br/><div class='col-md-12' style='overflow: auto;' >" + resp.data.content + "</div></div>",
                        size: 'large',
                        buttons: {
                            cancel: {
                                label: "Đóng",
                                className: 'btn-danger'
                            }
                        }
                    });
                } else {
                    $scope.errors = resp.data;
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

});

app.controller('emailOutbox', function ($scope) {

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            to: {
                required: true
            },
            subject: {
                required: true
            },
            content: {
                required: true
            }
        },
        messages: {
            to: {
                required: "Người nhận không được để trống"
            },
            subject: {
                required: "Tiêu đề thư không được để trống"
            },
            content: {
                required: "Nội dung thư không được để trống"
            }
        }
    });
});
