app.service('msg', function () {
    var elm = this;

    elm.success = function (message, fn, duration) {
        let time = typeof duration !== 'undefined' ? duration : 5000;
        Swal.fire({
            title: message,
            icon: 'success',
            toast: true,
            position: 'top-end',
            timer: time
        }).then((result) => {
            if (fn) {
                fn();
            }
        });
    };

    elm.danger = function (message, fn, duration) {
        let time = typeof duration !== 'undefined' ? duration : 5000;
        Swal.fire({
            title: message,
            icon: 'error',
            toast: true,
            position: 'top-end',
            timer: time
        }).then((result) => {
            if (fn) {
                fn();
            }
        });
    };

    elm.primary = function (message, fn, duration) {
        let time = typeof duration !== 'undefined' ? duration : 5000;
        Swal.fire({
            title: message,
            icon: 'info',
            toast: true,
            position: 'top-end',
            timer: time
        }).then((result) => {
            if (fn) {
                fn();
            }
        });
    };

    elm.info = function (message, fn, duration) {
        let time = typeof duration !== 'undefined' ? duration : 5000;
        Swal.fire({
            title: message,
            icon: 'info',
            toast: true,
            position: 'top-end',
            timer: time
        }).then((result) => {
            if (fn) {
                fn();
            }
        });
    };

    elm.warning = function (message, fn, duration) {
        let time = typeof duration !== 'undefined' ? duration : 5000;
        Swal.fire({
            title: message,
            icon: 'warning',
            toast: true,
            position: 'top-end',
            timer: time
        }).then((result) => {
            if (fn) {
                fn();
            }
        });
    };
});
