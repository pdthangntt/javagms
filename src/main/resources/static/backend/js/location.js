app.controller('province', function ($scope) {

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            ID: {
                required: true,
                maxlength: 5
            },
            name: {
                required: true,
                maxlength: 100
            },
            type: {
                required: true,
                maxlength: 30
            },
            position: {
                required: true,
                number: true
            },
            elogCode: {
                number: true,
                maxlength: 50
            },
            hivInfoCode: {
                number: true,
                maxlength: 50
            }
        },
        messages: {
            ID: {
                required: "Mã tỉnh thành không được để trống",
                maxlength: "Mã tỉnh thành có độ dài tối đa 5 ký tự."
            },
            name: {
                required: "Tên tỉnh thành không được để trống",
                maxlength: "Tên tỉnh thành có độ dài tối đa 100 ký tự."
            },
            type: {
                required: "Cấp tỉnh thành không được để trống",
                maxlength: "Cấp tỉnh thành có độ dài tối đa 30 ký tự."
            },
            position: {
                required: "Thứ tự không được để trống",
                number: "Thứ tự phải nhập số"
            },
            elogCode: {
                number: "Mã HTC Elog phải nhập số",
                maxlength: "Mã HTC Elog không quá 50 kí tự"
            },
            hivInfoCode: {
                number: "Mã HIV Info phải nhập số",
                maxlength: "Mã HIV Info không quá 50 kí tự"
            }
        }
    });

});

app.controller('district', function ($scope) {

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            ID: {
                required: true,
                maxlength: 5
            },
            name: {
                required: true,
                maxlength: 100
            },
            type: {
                required: true,
                maxlength: 30
            },
            position: {
                required: true,
                number: true
            },
            elogCode: {
                number: true,
                maxlength: 50
            },
            hivInfoCode: {
                number: true,
                maxlength: 50
            }
        },
        messages: {
            ID: {
                required: "Mã quận huyện không được để trống",
                maxlength: "Mã quận huyện có độ dài tối đa 5 ký tự."
            },
            name: {
                required: "Tên quận huyện không được để trống",
                maxlength: "Tên quận huyện có độ dài tối đa 100 ký tự."
            },
            type: {
                required: "Cấp quận huyện không được để trống",
                maxlength: "Cấp quận huyện có độ dài tối đa 30 ký tự."
            },
            position: {
                required: "Thứ tự không được để trống",
                number: "Thứ tự phải nhập số"
            },
            elogCode: {
                number: "Mã HTC Elog phải nhập số",
                maxlength: "Mã HTC Elog không quá 50 kí tự"
            },
            hivInfoCode: {
                number: "Mã HIV Info phải nhập số",
                maxlength: "Mã HIV Info không quá 50 kí tự"
            }
        }
    });

});

app.controller('ward', function ($scope) {

    $scope.validationOptions = angular.extend($scope.$parent.validationOptions, {
        rules: {
            ID: {
                required: true,
                maxlength: 5
            },
            name: {
                required: true,
                maxlength: 100
            },
            type: {
                required: true,
                maxlength: 30
            },
            position: {
                required: true,
                number: true
            },
            elogCode: {
                number: true,
                maxlength: 50
            },
            hivInfoCode: {
                number: true,
                maxlength: 50
            }
        },
        messages: {
            ID: {
                required: "Mã phường xã không được để trống",
                maxlength: "Mã phường xã có độ dài tối đa 5 ký tự."
            },
            name: {
                required: "Tên phường xã không được để trống",
                maxlength: "Tên phường xã có độ dài tối đa 100 ký tự."
            },
            type: {
                required: "Cấp phường xã không được để trống",
                maxlength: "Cấp phường xã có độ dài tối đa 30 ký tự."
            },
            position: {
                required: "Thứ tự không được để trống",
                number: "Thứ tự phải nhập số"
            },
            elogCode: {
                number: "Mã HTC Elog phải nhập số",
                maxlength: "Mã HTC Elog không quá 50 kí tự"
            },
            hivInfoCode: {
                number: "Mã HIV Info phải nhập số",
                maxlength: "Mã HIV Info không quá 50 kí tự"
            }
        }
    });

});


