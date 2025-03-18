var app = angular.module('app', ['ngValidate', 'ngSanitize', 'ui.bootstrap', 'LocalStorageModule', 'ui.select', 'ngMask', 'ui.mask']); //, 'angularjsToast'
app.filter('propsFilter', function () {
    return function (items, props) {
        var out = [];
        if (angular.isArray(items)) {
            items.forEach(function (item) {
                var itemMatches = false;

                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = utils.createAlias(props[prop]);
                    if (utils.createAlias(item[prop].toString()).indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }
        return out;
    };
});

app.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
});

app.service('locations', function (localStorageService) {
    this.useCache = true;
    this.get = function () {
        if (localStorageService.isSupported) {
            if (!this.useCache)
                localStorageService.remove("locations");
            if (localStorageService.get('locations') === null) {
                return this.getLocations(function (locations) {
                    localStorageService.set('locations', locations);
                });
            }
            return localStorageService.get('locations');
        }
        return this.getLocations();
    };

    this.getWards = function (districtID) {
        if (districtID == '') {
            return localStorageService.get('locations').wards;
        }
        if (localStorageService.isSupported) {
            var isExits = false;

            isExits = localStorageService.get('locations').wards.some(function (el) {
                return el.districtID === districtID;
            });

            if (!isExits) {
                this.getWardByDistrictID(districtID, function (wards) {
                    var locations = localStorageService.get('locations');
                    for (var i = 0; i < wards.length; i++) {
                        locations.wards.push(wards[i]);
                    }
                    localStorageService.set('locations', locations);
                });
            }
            return localStorageService.get('locations').wards;
        }
        return this.getWardByDistrictID(districtID);
    };

    this.getLocations = function (fn) {
        loading.show();
        var locations = [];
        $.ajax({
            url: '/service/location.json',
            method: 'GET',
            async: false,
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    locations = {};
                    locations.provinces = resp.data.province;
                    locations.districts = resp.data.district;
                    locations.wards = resp.data.ward == null ? [] : resp.data.ward;

                    locations.provinces.splice(0, 0, {id: '', name: 'Chọn tỉnh thành'});
                    locations.districts.splice(0, 0, {id: '', name: 'Chọn quận huyện'});
                    locations.wards.splice(0, 0, {id: '', name: 'Chọn phường xã'});
                    if (fn) {
                        fn(locations);
                    }
                    return locations;
                }
            }
        });
        return locations;
    };

    this.getWardByDistrictID = function (districtID, fn) {
        loading.show();
        $.ajax({
            url: '/service/location/ward.json?district_id=' + districtID,
            method: 'GET',
            async: false,
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    if (fn) {
                        fn(resp.data);
                    }
                    return resp.data;
                }
            }
        });
        return [];
    };

});

app.controller('body', function ($scope, msg, locations, localStorageService) {
    $scope.locations = [];
    $scope.notifications = [];

    $scope.selected = {
        province: null,
        district: null,
        ward: null
    };

    $scope.select_options = {
        maximumInputLength: 20,
        minimumResultsForSearch: 20,
        placeholder: "Select a state",
        dropdownAutoWidth: true,
        width: '100%'
    };

    $scope.select_mutiple = function (_id, placeholder) {
        if (typeof placeholder != 'undefined' && placeholder != null && placeholder != '') {
            $scope.select_options.placeholder = placeholder;
        }
        var options = $scope.select_options;
        options.closeOnSelect = false;
        $(_id).css({display: "none"});
        $(_id).attr({multiple: "multiple"});
        $(_id).select2(options);
        var values = [];
        $(_id).find("option[selected]").each(function () {
            values.push($(this).attr("value"));
        });
        $(_id).val(values).trigger('change');
    };

    $scope.select_search = function (_id, placeholder, ajax, search) {
        if (typeof placeholder !== 'undefined' && placeholder !== null && placeholder !== '') {
            $scope.select_options.placeholder = placeholder;
        }
        var options = {
            width: '100%',
        };
        if (typeof search !== 'undefined') {
            options.minimumResultsForSearch = -1;
        }
        if (typeof ajax !== 'undefined') {
            options.ajax = ajax;
        }
        if ($(_id).length > 0) {
            $(_id).select2(options);
        }
    };

    $scope.validationOptions = {
        rules: {
        },
        messages: {
        },
        errorElement: "em",
        errorPlacement: function (error, element) {
            error.addClass("help-block");
            element.parents().addClass("has-feedback");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            }
            if (element.prop("class").indexOf('select2-hidden-accessible') != -1) {
                error.insertAfter($(element.parent("div")).find("span.select2"));
            } else {
                error.insertAfter(element);
            }
        },
        success: function (label, element) {

        },
        highlight: function (element, errorClass, validClass) {
            $(element).parent().addClass("has-error").removeClass("has-success");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parent().addClass("has-success").removeClass("has-error");
        }
    };


    $scope.ping = function () {
        $.ajax({
            url: '/ping.json',
            method: 'GET',
            success: function (resp) {
                if (resp.success) {
                    $scope.$apply(function () {
                        $scope.notifications = resp.data.notifications;
                    });
                }
            }
        });
    };

    $scope.read = function (oid, url, title, time, content) {
        $.ajax({
            url: "/read.json?oid=" + (typeof oid == 'undefined' ? '' : oid),
            success: function (resp) {
                if (resp.success) {
                    if (typeof oid == 'undefined' || url == null || typeof url == 'undefined' || url == '') {
                        msg.success(url == null || typeof url == 'undefined' || url == '' ? "Đánh dấu đã đọc" : "Đã đánh dấu toàn bộ là đã đọc", function () {
                            $scope.ping();
                        }, 2000);
                    } else {
                        window.open(url, '_blank');
                        $scope.ping();
                    }

                }
            }
        });
    };

    $scope.init = function () {
        $(document).ajaxStart(function () {
            Pace.restart();
//            $("button").attr("disabled", true);
        });

        $(document).ajaxComplete(function () {
//            $("button").removeAttr("disabled");
        });

        $scope.ping();
        setInterval(function () {
            $scope.ping();
//        }, 1000);
        }, 5 * 60000);

        $scope.initNav();

        //Click table
        if ($("table.table").length > 0) {
            var choose_many = false;
            var choose_table = false;
            if ($("#tableFunction").length > 0) {
                choose_many = $("#tableFunction").parent().find(".pull-left button, .pull-left a").length > 0;
            }
            if (!choose_many) {
                $("#checkbox-switch-all").attr({disabled: "disabled"});
            }

            if ($("table.table-full").find(".table-function").length > 0) {
                var s_function = site_config != null && typeof site_config.s_function != 'undefined' ? site_config.s_function : 0;
                angular.forEach($("table.table-full").find('th:last-child, td:last-child'), function (elm) {
                    if (s_function == '0') {
                        $(elm).css({display: "none"});
                    }
                });
            }

            if ($("table.table").find("td:first input.checkbox-switch").length > 0) {
                choose_table = true;
                $("#page-footer").find("img").remove();
                $("#page-footer #table-selected").removeClass("display-none");
                $("#page-footer").addClass("box-footer text-center navbar-fixed-bottom main-footer");
            }

            var htmlDisable = '<a class="btn btn-default btn-xs disabled"><i class="fa fa-eye"></i> Xem</a> <a class="btn btn-default btn-xs disabled" href="javascript:;"><i class="fa fa-edit"></i> Sửa</a> <a class="btn btn-danger btn-xs disabled" href="javascript:;"><i class="fa fa-remove"></i> Xoá</a> <button type="button" class="btn btn-default btn-xs disabled table-function"><i class="fa fa-comment"></i> Lịch sử</button>';
//            var htmlDisable = '';
            $("input.checkbox-switch").on('ifChanged', function () {
                $(this).parent().parent().click();
            });

            $("table.table").find("td").on('click', function () {
                let isCP = true;
                let elm = this;
                let checkbox = $(elm).parent("tr").find("td:first input.checkbox-switch");

                if (checkbox !== 'undefined' && checkbox.length === 1) {
                    let isChecked = checkbox.is(":checked");
                    let tr = $(elm).parent("tr");
                    if (isChecked && !tr.hasClass('active')) {

                    } else if (!isChecked && tr.hasClass('active')) {

                    } else {
                        $(checkbox).iCheck(isChecked ? 'uncheck' : 'check');
                    }

                    isCP = checkbox.is(":checked");
                    if (isCP) {
                        if (!choose_many) {
                            angular.forEach($("input.checkbox-switch:checked"), function (elm) {
                                if ($(elm).attr("value") !== $(checkbox).attr("value")) {
//                                    $(elm).trigger('click');
                                    let check = $(elm).is(":checked");
                                    $(elm).iCheck(check ? 'uncheck' : 'check');
                                }
                            });
                        }
                        $(elm).parent("tr").addClass('active');
                    } else {
                        $(elm).parent("tr").removeClass('active');
                    }
                }

                $("#tableFunction").html("");
                var inputchecked = $("input.checkbox-switch:checked");
                if (inputchecked.length > 1) {
                    isCP = false;
                }
                if (isCP) {
                    angular.forEach($(elm).parent("tr").find("td:last .table-function"), function (e) {
                        if ($(e).find("ul").length > 0 && $(e).find("ul li").length === 0) {
                            return false;
                        }
                        var elmFunction = $(e).clone(true, true);
                        $("#tableFunction").append($(elmFunction));
                    });
                } else {
                    $("#tableFunction").html(htmlDisable);
                }

                if (inputchecked.length === 1 && inputchecked.val() !== $(checkbox).val()) {
                    $("#tableFunction").html("");
                    angular.forEach(inputchecked.parent().parent().parent("tr").find("td:last .table-function"), function (elm) {
                        var elmFunction = $(elm).clone(true, true);
                        $("#tableFunction").append($(elmFunction));
                    });

                }
                if (inputchecked.length === 0) {
                    $("#tableFunction").html(htmlDisable);
                }

                if (choose_table) {
                    let comment = $("#table-comment").html();
                    let html = "<i class='fa fa-check-square-o' ></i> Đã chọn " + inputchecked.length + " mục";
                    if(typeof comment !== 'undefined') {
                        html += " | " + comment;
                    }
                    $("#page-footer #table-selected").html(html);
                }
            });
        }
    };

    $scope.initNav = function () {
        if (localStorageService.isSupported && !window.isMobile()) {
            $('[data-toggle="offcanvas"]').click(function () {
                localStorageService.set('menu_nav', $('body').hasClass('sidebar-collapse'));
            });
            if (localStorageService.get('menu_nav') != null && !localStorageService.get('menu_nav')) {
                $('[data-toggle="offcanvas"]').click();
            }
        }

    };

    $scope.register = function (form, $event) {
        if (!form.validate()) {
            msg.danger("Thông tin chưa chính xác. Vui lòng kiểm tra lại");
            $event.preventDefault();
        }
    };

    $scope.print = function (urlPrint) {
        if ($('#frm-print').length > 0) {
            $('#frm-print').remove();
        }
        $('body').append('<iframe id="frm-print" src="' + urlPrint + '" width="0" frameborder="0" scrolling="no" height="0"></iframe>');
    };

    /**
     * Hiển thị Pop pdf
     * @param {type} pdfUrl
     * @param {type} fnok
     * @param {type} title
     * @returns {undefined}
     */
    $scope.dialogReport = function (pdfUrl, fnok, title) {
        var height = String($(window).height() - 200) + 'px';
        var object = "<object id='object-pdf' style=\"background: transparent url(/static/backend/images/loading.gif) no-repeat center;\" data=\"" + pdfUrl + "\" type=\"application/pdf\" width=\"100%\" height='" + height + "'>";
        object += "If you are unable to view file, you can download from";
        object += " or download <a target = \"_blank\" href = \"http://get.adobe.com/reader/\">Adobe PDF Reader</a> to view the file.";
        object += "</object>";

        var buttons = {
            cancel: {
                label: "Đóng",
                className: 'btn-default',
                callback: function () {
                }
            }
        };

        if (typeof (urlPrint) != 'undefined') {
            var u = pdfUrl.split("?");
            buttons.noclose = {
                label: '<i class="fa fa-print"></i> In',
                className: 'btn-primary',
                callback: function () {
                    $scope.print(urlPrint + "?" + u[1]);
                    return false;
                }
            };
        }

        if (fnok) {
            buttons.ok = fnok;
        }

        bootbox.dialog({
            title: title ? title : $("title").text().split("|")[0],
            message: object,
            size: 'large',
            buttons: buttons
        }).find("div.modal-dialog").addClass("largeWidth").find("div.bootbox-body").css({height: height});
    };

    /**
     * Hiển thị chi tiết repory
     * @param {type} params
     * @returns {undefined}
     */
    $scope.detailReportLine = function (params, type) {
        if (typeof type == 'undefined') {
            type = "";
        }
        if (typeof params.objectGroupID != 'undefined') {
            if (params.objectGroupID.indexOf(",") == -1) {
                params.objectGroupID = [params.objectGroupID];
            } else {
                params.objectGroupID = params.objectGroupID.split(",");
            }
        }

        loading.show();
        $.ajax({
            url: '/service/htc/search.json?type=' + type,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(params),
            success: function (resp) {
                loading.hide();
                var dTable = [];
                var options = resp.data.options;
                $.each(resp.data.models, function () {
                    var item = this;
                    dTable.push([
                        item.code, item.patientName, item.yearOfBirth,
                        (typeof options.gender[item.genderID] == 'undefined' ? '' : options.gender[item.genderID]),
                        item.permanentAddressFull,
                        utils.timestampToStringDate(item.advisoryeTime),
                        utils.timestampToStringDate(item.preTestTime),
                        utils.timestampToStringDate(item.resultsTime),
                        (typeof options['test-results'][item.testResultsID] == 'undefined' ? '' : ('<span class="' + (item.testResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-results'][item.testResultsID] + '</span>')),
                        utils.timestampToStringDate(item.confirmTime),
                        utils.timestampToStringDate(item.resultsSiteTime),
                        utils.timestampToStringDate(item.resultsPatienTime),
                        (typeof options['test-result-confirm'][item.confirmResultsID] == 'undefined' ? '' : ('<span class="' + (item.confirmResultsID == 2 ? 'text-danger' : '') + '" >' + options['test-result-confirm'][item.confirmResultsID] + '</span>')),
                        (typeof options['test-object-group'][item.objectGroupID] == 'undefined' ? '' : options['test-object-group'][item.objectGroupID]),
                        item.staffBeforeTestID,
                        item.staffAfterID
                    ]);
                });
                if (dTable.length == 0) {
                    return false;
                }

                bootbox.dialog({
                    title: "Danh sách khách hàng",
                    message: '<table id="grid-report" class="table-report-view table-sm table-report table table-bordered table-hover table-full"></table>',
                    size: 'large',
                    buttons: {
                        cancel: {
                            label: "Đóng",
                            className: 'btn-default',
                            callback: function () {
                            }
                        }
                    }
                }).find("div.modal-dialog").addClass("largeWidth");

                setTimeout(function () {
                    $("#grid-report").addClass('nowrap').DataTable({
                        paging: false,
                        searching: false,
                        ordering: false,
                        info: false,
                        scrollX: true,
                        scrollCollapse: true,
                        processing: true,
                        language: {
                            emptyTable: "<b class='text-red text-center' >Không có thông tin</b>"
                        },
                        data: dTable,
                        columns: [
                            {title: "Mã KH", className: "text-center"},
                            {title: "Họ và tên"},
                            {title: "Năm sinh", className: "text-center"},
                            {title: "Giới tính", className: "text-center"},
                            {title: "Địa chỉ thường trú"},
                            {title: "Ngày tư vấn", className: "text-center"},
                            {title: "Ngày XN sàng lọc", className: "text-center"},
                            {title: "Ngày nhận KQ sàng lọc", className: "text-center"},
                            {title: "Kết quả XN sàng lọc", className: "text-center"},
                            {title: "Ngày XN khẳng định", className: "text-center"},
                            {title: "Ngày CS nhận KQKĐ", className: "text-center"},
                            {title: "Ngày KH nhận KQKĐ", className: "text-center"},
                            {title: "Kết quả XN khẳng định", className: "text-center"},
                            {title: "Nhóm đối tượng"},
                            {title: "Tư vấn viên trước XN"},
                            {title: "Tư vấn viên sau XN"}
                        ]
                    });
                }, 300);
            }
        });
    };

    /**
     * google auto suggets địa chỉ
     * @param {type} inputID
     * @param {type} provinceID
     * @param {type} districtID
     * @param {type} wardID
     * @returns {Boolean}
     */
    $scope.addressAutocomplete = function (inputID, provinceID, districtID, wardID) {
        if ($(inputID).length == 0) {
            return false;
        }
        var autocomplete = new google.maps.places.Autocomplete(document.getElementById(inputID.replace("#", "")), {types: ['geocode']});
        autocomplete.setComponentRestrictions({'country': ['VN', 'vi']});
        autocomplete.setFields(['address_components', 'name', 'formatted_address']);
        autocomplete.addListener('place_changed', function () {
            var place = autocomplete.getPlace();
            var provinceName = districtName = wardName = null;
            for (var i = 0; i < place.address_components.length; i++) {
                if (place.address_components[i].types['0'] == "administrative_area_level_1") {
                    provinceName = place.address_components[i].long_name;
                } else if (place.address_components[i].types['0'] == "administrative_area_level_2" || place.address_components[i].types['0'] == "locality") {
                    districtName = place.address_components[i].long_name;
                } else if (place.address_components[i].types['0'] == "sublocality_level_1") {
                    wardName = place.address_components[i].long_name.replace("tt.", "");
                }
            }
            if (wardName == null) {
                let regexp = /phường.*,|xã.*,/ig;
                var s = place.formatted_address.match(regexp);
                if (s != null && s.length > 0) {
                    wardName = s[0].split(",")[0];
                }
            }

            if (wardName != null && wardName.search(/.*\d{1}/ig) != -1) {
                var cs = wardName.split(" ");
                wardName = cs[0] + " 0" + cs[1];
            }

            if (provinceName != null) {
                var elm = $(provinceID + ' option:contains(' + provinceName + ')');
                $(provinceID).val(elm.attr("value")).change();
            }
            if (districtName != null) {
                var elm = $(districtID + ' option:contains(' + districtName + ')');
                $(districtID).val(elm.attr("value")).change();
            }
            setTimeout(function () {
                if (wardName != null) {
                    var elm = $(wardID + ' option:contains(' + wardName + ')');
                    $(wardID).val(elm.attr("value")).change();
                }
            }, 500);
        });
    };

    $scope.initProvinceMultiple = function (provinceID, districtID, ward) {
        if ($(districtID).length > 0) {
            $(districtID).append($('<option>', {value: '', text: typeof $(districtID).attr("data-title") != 'undefined' ? $(districtID).attr("data-title") : "Chọn quận huyện"}));
        }
        if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
            $(ward).append($('<option>', {value: '', text: typeof $(ward).attr("data-title") != 'undefined' ? $(ward).attr("data-title") : "Chọn phường xã"}));
        }

        $scope.locations = locations.get();
        var pID = null;
        var dID = null;
        var wID = null;
        //vẽ tỉnh
        if (typeof provinceID != 'undefined' && provinceID != null && $(provinceID).length > 0) {
            pID = form[provinceID.replace("#", '')];
            angular.forEach($scope.locations.provinces, function (value, key) {
                if (value.id == '' && typeof $(provinceID).attr("data-title") != 'undefined') {
                    value.name = $(provinceID).attr("data-title");
                }
                if (value.id == '' || $(provinceID + " option[value=" + value.id + "]").length == 0) {
                    $(provinceID).append($('<option>', {value: value.id, text: value.name}));
                }
            });
        }

        //Huyện
        if (typeof districtID != 'undefined' && districtID != null && $(districtID).length > 0) {
            dID = form[districtID.replace("#", '')];
            if (typeof provinceID != 'undefined' && provinceID != null && $(provinceID).length > 0) {
                $(provinceID).change(function () {
                    var pValue = $(provinceID).val() == null ? [] : $(provinceID).val();
                    $(districtID).find("option").remove();
                    if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
                        $(ward).find("option").remove();
                        $(ward).append($('<option>', {value: '', text: typeof $(ward).attr("data-title") != 'undefined' ? $(ward).attr("data-title") : "Chọn phường xã"}));
                    }
                    angular.forEach($scope.locations.districts, function (value, key) {
                        if (pValue.indexOf(value.provinceID) != -1 || value.id == '') {
                            if (value.id == '' && typeof $(districtID).attr("data-title") != 'undefined') {
                                value.name = $(ward).attr("data-title");
                            }
                            var pName = $(provinceID + " option[value=" + value.provinceID + "]").text();
                            $(districtID).append($('<option>', {value: value.id, text: (value.name + (pValue.length > 1 ? ", " + pName : ""))}));
                        }
                    });
                });
            } else if ($(districtID).attr("data-province-id") != 'undefined') {
                $(districtID).find("option").remove();
                angular.forEach($scope.locations.districts, function (value, key) {
                    if ($(districtID).attr("data-province-id") == value.provinceID || value.id == '') {
                        if (value.id == '' && typeof $(districtID).attr("data-title") != 'undefined') {
                            value.name = $(ward).attr("data-title");
                        }
                        $(districtID).append($('<option>', {value: value.id, text: value.name}));
                    }
                });
            }
        }

        //Vẽ xã
        if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
            wID = form[ward.replace("#", '')];
            if (typeof districtID != 'undefined' && districtID != null && $(districtID).length > 0) {
                $(districtID).change(function () {
                    var dID = $(districtID).val() == null ? [] : $(districtID).val();
                    $(ward).find("option").remove();
                    if (dID.length > 0) {
                        angular.forEach(dID, function (value, key) {
                            $scope.locations.wards = locations.getWards(value);
                        });
                    }
                    angular.forEach($scope.locations.wards, function (value, key) {
                        if (dID.indexOf(value.districtID) != -1 || value.id == '') {
                            if (value.id == '' && typeof $(ward).attr("data-title") != 'undefined') {
                                value.name = $(ward).attr("data-title");
                            }
                            var wName = $(districtID + " option[value=" + value.districtID + "]").text();
                            $(ward).append($('<option>', {value: value.id, text: (value.name + (dID.length > 1 ? ", " + wName : ""))}));
                        }
                    });
                });
            } else if ($(ward).attr("data-district-id") != 'undefined') {
                $(ward).find("option").remove();
                angular.forEach($scope.locations.wards, function (value, key) {
                    if ($(ward).attr("data-district-id") == value.districtID || value.id == '') {
                        if (value.id == '' && typeof $(ward).attr("data-title") != 'undefined') {
                            value.name = $(ward).attr("data-title");
                        }
                        $(ward).append($('<option>', {value: value.id, text: value.name}));
                    }
                });
            }
        }

        //selected value
        if (utils.getContentOfDefault(pID, null) != null) {
            angular.forEach(pID, function (value, key) {
                $(provinceID).val(value).change();
            });
        }

        if (utils.getContentOfDefault(dID, null) != null) {
            $(districtID).val(dID).change();
        }

        if (typeof (wID) != 'undefined' && utils.getContentOfDefault(wID, null) != null) {
            $(ward).val(wID).change();
        }

    };

    $scope.initProvince = function (provinceID, districtID, ward) {
        if ($(districtID).length > 0) {
            $(districtID).append($('<option>', {value: '', text: typeof $(districtID).attr("data-title") !== 'undefined' ? $(districtID).attr("data-title") : "Chọn quận huyện"}));
        }
        if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
            $(ward).append($('<option>', {value: '', text: typeof $(ward).attr("data-title") != 'undefined' ? $(ward).attr("data-title") : "Chọn phường xã"}));
        }

        $scope.locations = locations.get();
        var pID = null;
        var dID = null;
        var wID = null;
        //vẽ option
        if (typeof provinceID != 'undefined' && provinceID != null && $(provinceID).length > 0) {
            pID = form[provinceID.replace("#", '')];
            angular.forEach($scope.locations.provinces, function (value, key) {
                if (value.id == '' && typeof $(provinceID).attr("data-title") != 'undefined') {
                    value.name = $(provinceID).attr("data-title");
                }
                if (value.id == '' || $(provinceID + " option[value=" + value.id + "]").length == 0) {
                    $(provinceID).append($('<option>', {value: value.id, text: value.name}));
                }
            });
        }

        if (typeof districtID != 'undefined' && districtID != null && $(districtID).length > 0) {
            dID = form[districtID.replace("#", '')];
            if (typeof provinceID != 'undefined' && provinceID != null && $(provinceID).length > 0) {
                $(provinceID).change(function () {
                    $(districtID).find("option").remove();
                    if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
                        $(ward).find("option").remove();
                        $(ward).append($('<option>', {value: '', text: typeof $(ward).attr("data-title") != 'undefined' ? $(ward).attr("data-title") : "Chọn phường xã"}));
                    }
                    angular.forEach($scope.locations.districts, function (value, key) {
                        if ($(provinceID).val() == value.provinceID || value.id == '') {
                            if (value.id == '' && typeof $(districtID).attr("data-title") != 'undefined') {
                                value.name = $(ward).attr("data-title");
                            }
                            $(districtID).append($('<option>', {value: value.id, text: value.name}));
                        }
                    });
                });
            } else if ($(districtID).attr("data-province-id") != 'undefined') {
                $(districtID).find("option").remove();
                angular.forEach($scope.locations.districts, function (value, key) {
                    if ($(districtID).attr("data-province-id") === value.provinceID || value.id === '') {
                        if (value.id === '' && typeof $(districtID).attr("data-title") !== 'undefined') {
                            value.name = $(districtID).attr("data-title");
                        }
                        $(districtID).append($('<option>', {value: value.id, text: value.name}));
                    }
                });
            }
        }

        if (typeof ward != 'undefined' && ward != null && $(ward).length > 0) {
            wID = form[ward.replace("#", '')];
            if (typeof districtID != 'undefined' && districtID != null && $(districtID).length > 0) {
                $(districtID).change(function () {
                    var dID = $(districtID).val();
                    $(ward).find("option").remove();
                    if (dID != '') {
                        $scope.locations.wards = locations.getWards(dID);
                    }
                    angular.forEach($scope.locations.wards, function (value, key) {
                        if (dID == value.districtID || value.id == '') {
                            if (value.id == '' && typeof $(ward).attr("data-title") != 'undefined') {
                                value.name = $(ward).attr("data-title");
                            }
                            $(ward).append($('<option>', {value: value.id, text: value.name}));
                        }
                    });
                });
            } else if ($(ward).attr("data-district-id") != 'undefined') {
                $(ward).find("option").remove();
                angular.forEach($scope.locations.wards, function (value, key) {
                    if ($(ward).attr("data-district-id") == value.districtID || value.id == '') {
                        if (value.id == '' && typeof $(ward).attr("data-title") != 'undefined') {
                            value.name = $(ward).attr("data-title");
                        }
                        $(ward).append($('<option>', {value: value.id, text: value.name}));
                    }
                });
            }
        }

        //selected value
        if (utils.getContentOfDefault(pID, null) != null) {
            $(provinceID).val(pID).change();
        } else if (provinceID === "#permanentProvinceID" && typeof province_id != 'undefined') {
            $(provinceID).val(province_id).change();
        }

        if (utils.getContentOfDefault(dID, null) != null) {
            $(districtID).val(dID).change();
        } else if (districtID === "#permanentDistrictID" && typeof district_id != 'undefined') {
            $(districtID).val(district_id).change();
        }

        if (typeof (wID) != 'undefined' && utils.getContentOfDefault(wID, null) != null) {
            $(ward).val(wID).change();
        } else if (ward === "#permanentWardID") {
            $(ward).val('').change();
        }
    };

    /**
     * Lấy id selected từ table
     * @param {type} elm
     * @returns {unresolved}
     */
    $scope.getIDFromTable = function (elm) {
        if (typeof elm == 'undefined') {
            elm = $("table.table");
        }
        if (typeof $("div.dataTables_scrollBody table").length > 0) {
            elm = $("div.dataTables_scrollBody table");
        }
        return elm.find("tr.active").length == 0 ? null : elm.find("tr.active").attr("data-id");
    };

    $scope.changeSwitchery = function (element, checked) {
        if ((element.is(':checked') && checked == false) || (!element.is(':checked') && checked == true)) {
            element.trigger('click');
        }
    };

    $scope.getSwitch = function (elm) {
        var elm = $(typeof (elm) === 'undefined' ? 'input.checkbox-switch' : elm);
        var ids = [];
        angular.forEach($(elm), function (elm) {
            if ($(elm).is(':checked')) {
                ids.push($(elm).val());
            }
        });
        return ids;
    };

    $scope.switchConfig = function (elm, elmAll) {
        var elm = $(typeof (elm) === 'undefined' ? 'input.checkbox-switch' : elm);
        var elmCheckboxAll = document.querySelector(typeof (elmAll) === 'undefined' ? 'input#checkbox-switch-all' : elmAll);

        let config = {
            checkboxClass: 'icheckbox_square-blue',
//            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_square-blue'
//            radioClass: 'iradio_minimal-blue'
        };

        $(elm).iCheck(config);

        if (elmCheckboxAll !== null) {
            $(elmCheckboxAll).iCheck(config);

            $(elmCheckboxAll).on('ifChanged', function () {
                var checked = $(elmCheckboxAll).is(':checked');
                angular.forEach(elm, function (child) {
                    if (($(child).is(':checked') && checked === false) || (!$(child).is(':checked') && checked === true)) {
                        if (checked) {
                            $(child).iCheck('check');
                        } else {
                            $(child).iCheck('uncheck');
                        }
                    }
                });
            });
        }
    };

    $scope.getQueryStringInForm = function (_id) {
        var params = "?";
        angular.forEach($(_id).find("input, select"), function (elm) {
            var tagName = $(elm).prop("tagName");
            var name = $(elm).attr("name");
            var val = '';
            if (tagName == 'INPUT' && ($(elm).attr("type") == 'radio' || $(elm).attr("type") == 'checkbox')) {
                if ($(elm).is(":checked")) {
                    val = $(elm).val();
                }
            } else {
                val = $(elm).val();
            }

            if (typeof name != 'undefined' && val != null && val != '') {
                params += (params == '?' ? '' : '&') + name + "=" + val;
            }
        });
        return params;
    };

    $scope.clearCache = function (fn) {
        if (localStorageService.isSupported) {
            localStorageService.remove("locations");
            console.log("Clear store cache");
        }
        $.ajax({
            url: '/clear-cache.json',
            method: 'GET',
            success: function (resp) {
                if (resp.success) {
                    if (fn) {
                        fn();
                        return false;
                    } else {
                        window.location.reload();
                        return false;
                    }
                }
            }
        });
    };

    $scope.sortGrid = function (sortName, sortType) {
        let attrName = $("#sortName");
        let attrType = $("#sortType");
        if (attrName.length > 0) {
            attrName.val(sortName);
        }
        if (attrType.length > 0) {
            attrType.val(sortType);
        }
        $("button[type=submit]").click();
    };

});

app.controller('index', function ($scope, amchart, msg) {
    $scope.init = function () {
        var data = [
            {
                name: "HIV IMS",
                children: [
                    {name: "Tư vấn\nvà\nxét nghiệm", value: 5000},
                    {name: "Quản lý\nngười nhiễm", value: 3000},
                    {name: "Điều trị\nngoại trú", value: 1000},
                    {name: "Báo cáo\nquốc gia", value: 1000},
                ]
            }
        ];
        amchart.forceDirectedTree("index-chart", data, function (chart, networkSeries) {
            networkSeries.nodes.template.tooltipText = "{name}";
            networkSeries.minRadius = am4core.percent(6);
        });
    };
});
