window.isMobile = function () {
    if (sessionStorage.desktop) // desktop storage 
        return false;
    else if (localStorage.mobile) // mobile storage
        return true;
    var mobile = ['iphone', 'ipad', 'android', 'blackberry', 'nokia', 'opera mini', 'windows mobile', 'windows phone', 'iemobile'];
    for (var i in mobile)
        if (navigator.userAgent.toLowerCase().indexOf(mobile[i].toLowerCase()) > 0)
            return true;
    return false;
};

var loading = {};
loading.show = function () {
    if ($('#loading').length <= 0) {
        $('body').append('<div id="loading" style="display:none;" class="loading"></div>');
    }
    $('#loading').show();
};

loading.hide = function () {
    $('#loading').hide();
};

jQuery.expr[':'].contains = function (a, i, m) {
    return jQuery(a).text().toUpperCase()
            .indexOf(m[3].toUpperCase()) >= 0;
};

$.ajaxSetup({
    dataType: 'json',
    beforeSend: function (xhr, settings) {
        var url = settings.url.split("?")[0];
        if (url !== '/ping.json' && !url.startsWith('/static/backend/plugin/leaflet/gis') && app_security && !user_action.includes(url)) {
            console.log("Not have access " + settings.url);
            xhr.abort();
            if (loading) {
                loading.hide();
            }
            bootbox.alert("Bạn không có quyền thực hiện chức năng này. Action " + settings.url);
            return false;
        }

    },
    error: function (xhr, status, error) {
        console.log(xhr);
//        $("button").removeAttr("disabled");
        loading.hide();
        var dialog = null;
        if (xhr.status == 403) {
            dialog = bootbox.alert('Bạn không có quyền thực hiện hành động này');
        } else if (xhr.status == 500) {
            dialog = bootbox.alert('<h3>500: Lỗi kỹ thuật trên hệ thống</h3></div>');
        } else if (xhr.status == 404) {
            dialog = bootbox.alert('<h3>Lỗi 404: Không tìm thấy yêu cầu</h3></div>');
        } else if (xhr.status == 405) {
            dialog = bootbox.alert('<h3>Lỗi 405: Lỗi kỹ thuật</h3></div>');
        } else {
            dialog = bootbox.alert(error == '' ? 'Mất kết nối!' : error);
        }
        if (dialog != null) {
            setTimeout(function () {
                dialog.modal('hide');
            }, 5000);
        }
    }
});


var menu = {};
menu.active = function () {
    if (typeof user_action == 'undefined' || user_action.length == 0) {
        user_action = [];
    }
    var bUrl = null;
    var cUrl = null;
    var breadcrumb = $(".breadcrumb a");
    if (breadcrumb.length > 2) {
        bUrl = $(breadcrumb[1]).attr("href").split("?")[0];
        cUrl = $(breadcrumb[2]).attr("href").split("?")[0];
    }

    //active menu
    var lUrl = window.location.pathname;
    $(".sidebar-active-link a").map(function () {
        var aObject = this;
        var aUrl = $(aObject).attr("href");

        if (user_action.includes(aUrl)
                || aUrl == "/backend/index.html"
                || aUrl == "/index.html") {
            $(aObject).parent().css("display", "block");
            $(aObject).parent().parent().parent().css("display", "block");
        }

        if (aUrl == '/backend/site/index.html'
                && (lUrl + window.location.search).indexOf(user_staff_url_301) != -1) {
            $("a[href='/profile/staff.html']").parent().addClass("active");
            $("a[href='/profile/staff.html']").parent().parent().parent().addClass("active");
        } else if (aUrl.indexOf("/import") != -1
                && aUrl == bUrl
                && lUrl.indexOf("/import") != -1 && (lUrl.indexOf("/read.html") != -1 || lUrl.indexOf("/validate.html") != -1)) {
            $(aObject).parent().addClass("active");
            $(aObject).parent().parent().parent().addClass("active");
        } else if ((aUrl == lUrl) || (bUrl != null && aUrl == bUrl && $(".sidebar-active-link a[href='" + cUrl + "']").length == 0)) {
            $(aObject).parent().addClass("active");
            $(aObject).parent().parent().parent().addClass("active");
        }
    });
};

menu.role = function () {
    if (!app_security) {
        return false;
    }
    $("[data-role]").map(function () {
        var aUrl = $(this).attr("data-role");
        aUrl = aUrl.split("?")[0];
        if (!user_action.includes(aUrl)) {
            $(this).addClass("disabled");
            $(this).removeAttr("ng-click");
        } else {
            $(this).removeClass("disabled");
        }
    });
    $("[data-role-if]").map(function () {
        var aUrl = $(this).attr("data-role-if");
        aUrl = aUrl.split("?")[0];
        if (!user_action.includes(aUrl)) {
            $(this).addClass("hidden");
            $(this).removeAttr("ng-click");
        } else {
            $(this).removeClass("hidden");
        }
    });
};

jQuery.extend({
    getQueryParameters: function (str) {
        var q = (str || document.location.search).replace(/(^\?)/, '');
        if (q == null || q == '') {
            return [];
        }
        return q.split("&").map(function (n) {
            return n = n.split("="), this[n[0]] = decodeURIComponent(n[1]), this
        }.bind({}))[0];
    }
});
menu.active();

$(document).ready(function () {
    console.log("%cDừng lại", "color: red; font-size:35px;");
    console.log("%cĐây là tính năng cho cán bộ Gimasys sử dụng. Xin vui lòng không sử dụng", "color: green; font-size:14px;");

    menu.role();

    //active link
    var cUrl = window.location.pathname;
    $(".header-tabs a").each(function () {
        var aUrl = $(this).attr("href");
        if (aUrl !== undefined && aUrl !== 'undefined' && aUrl.indexOf(cUrl) !== -1) {
            $(this).parent().addClass("active");
        }
    });

    //Bắt sự kiện khi click nút back trình duyệt
    if ($("[data-confirm]").length > 0
            && $("[data-confirm]").attr("data-confirm-browsers") !== undefined
            && window.history && history.pushState) {
        history.pushState(null, null, null);
        window.onpopstate = function (event) {
            bootbox.confirm({
                message: "Bạn chắc chắn muốn rời khỏi trang này ?",
                buttons: {
                    confirm: {
                        label: '<i class="fa fa-check" ></i> Đồng ý',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: '<i class="fa fa-remove" ></i> Ở lại',
                        className: 'btn-danger'
                    }

                },
                callback: function (result) {
                    if (result) {
                        window.onpopstate = null;
                        history.back();
                    } else {
                        history.pushState(null, null, null);
                    }
                }
            });
        };
    }

    if ($("[data-confirm]").length > 0) {
        $("[data-confirm]").on('click', function (event) {
            event.preventDefault();
            var elm = this;
            bootbox.confirm({
                message: this.getAttribute('data-confirm'),
                buttons: {
                    confirm: {
                        label: '<i class="fa fa-check" ></i> Đồng ý',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: '<i class="fa fa-remove" ></i> Không',
                        className: 'btn-danger'
                    }

                },
                callback: function (result) {
                    if (result) {
                        window.location.href = elm.getAttribute('href');
                    }
                }
            });
        });
    }

    //input file
    $(document).on('change', '.file-custom:file', function () {
        var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');

        if (label != '') {
            $(".file-custom-name").html("Tệp tin được chọn: " + label);
        }
        var btn = $(".btn-file-custom-name");
        if (btn.length > 0 && label != '') {
            btn.fadeIn();
        }

        input.trigger('fileselect', [numFiles, label]);
    });

    //table customer
    var tableElm = $('.table-full');
    if (typeof tableElm != 'undefined' && tableElm.length > 0) {
        var tableOption = {
            language: {
                emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
            },
            responsive: true,
            scrollX: true,
            paging: false,
            searching: false,
            ordering: false,
            info: false,
            scrollCollapse: true,
            processing: true,
            autoWidth: (tableElm.find("tbody > tr").length > 0)
        };
        if (tableElm.attr("data-rowsGroup") != undefined && tableElm.attr("data-rowsGroup") != 'undefined' && tableElm.attr("data-rowsGroup") != '-1') {
            tableOption.rowsGroup = tableElm.attr("data-rowsGroup").split(",");
        }
        if (tableElm.attr("data-scrollY") != undefined && tableElm.attr("data-scrollY") == 'auto') {
            tableOption.scrollY = String($(window).height() - 200) + 'px';
        }
        if (tableElm.attr("data-rightColumns") !== undefined && tableElm.attr("data-rightColumns") !== '0' && !isNaN(Number(tableElm.attr("data-rightColumns")))) {
            if (typeof tableOption.fixedColumns === 'undefined') {
                tableOption.fixedColumns = {};
            }
            tableOption.fixedColumns.rightColumns = Number(tableElm.attr("data-rightColumns"));
        }
        if (tableElm.attr("data-leftColumns") !== undefined && tableElm.attr("data-leftColumns") !== '0' && !isNaN(Number(tableElm.attr("data-leftColumns")))) {
            if (typeof tableOption.fixedColumns === 'undefined') {
                tableOption.fixedColumns = {};
            }
            tableOption.fixedColumns.leftColumns = Number(tableElm.attr("data-leftColumns"));
        }

        if (tableElm.attr("data-details") !== undefined && tableElm.attr("data-details") !== 'false') {
            var sall = $("#checkbox-switch-all");
            if (sall.length > 0) {
                sall.parent().html('<div class="header-control" ></div>');
            }
            var elm = $('input.checkbox-switch');
            angular.forEach(elm, function (child) {
                $(child).parent().addClass("details-control");
                $(child).parent().html("");
            });
        }
        var table = tableElm.addClass('nowrap').DataTable(tableOption); //.columns.adjust();
        if (tableElm.attr("data-details") !== undefined && tableElm.attr("data-details") !== 'false') {
            tableElm.find('tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table.row(tr);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                } else {
                    if ($("#child_" + row.id()).length > 0) {
                        row.child($("#child_" + row.id()).html()).show();
                        tr.addClass('shown');
                    }
                }
            });
            var detailDefault = tableElm.attr("data-details");
            if (detailDefault === 'show') {
                tableElm.find('tbody td.details-control').each(function () {
                    $(this).click();
                });
            }
        }
    }
});