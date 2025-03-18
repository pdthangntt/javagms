app.service('msg', function (toast) {
    var elm = this;

    elm.toast = function (cls, message, fn, duration) {
        toast({
//            masterClass: 'masterClass',
            className: cls,
            duration: typeof duration != 'undefined' ? duration : 5000,
            message: message,
            maxToast: 4,
            insertFromTop: true,
            fn: fn
        });
    };

    elm.success = function (message, fn, duration) {
        elm.toast("alert-success", message, fn, duration);
    };

    elm.danger = function (message, fn, duration) {
        elm.toast("alert-danger", message, fn, duration);
    };

    elm.primary = function (message, fn, duration) {
        elm.toast("alert-primary", message, fn, duration);
    };

    elm.info = function (message, fn, duration) {
        elm.toast("alert-info", message, fn, duration);
    };

    elm.warning = function (message, fn, duration) {
        elm.toast("alert-warning", message, fn, duration);
    };
});
