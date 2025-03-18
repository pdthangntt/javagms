app.controller('dashboard_htc', function ($scope, msg, amchart) {
    $scope.options = {};
    $scope.filter = {
        districtID: {}
    };

    $scope.init = function () {
        if (window.location.hash != '') {
            $scope.filter.indicatorName = window.location.hash.replace("#", '');
        }
        $scope.report();
    };

    $scope.switchConfig = function (elm, elmAll) {
        var elm = $(typeof (elm) == 'undefined' ? 'input.checkbox-switch' : elm);
        var elmCheckboxAll = document.querySelector(typeof (elmAll) == 'undefined' ? 'input#checkbox-switch-all' : elmAll);

        $(elm).iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });

        if (elmCheckboxAll != null) {
            $(elmCheckboxAll).iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            });

            $(elmCheckboxAll).on('ifChanged', function () {
                var checked = $(elmCheckboxAll).is(':checked');
                angular.forEach(elm, function (child) {
                    if (($(child).is(':checked') && checked == false) || (!$(child).is(':checked') && checked == true)) {
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

    $scope.initFilter = function () {
        var provinceID = "#provinceID";
        $scope.initProvince(provinceID, null, null);
//        $(provinceID).attr("disabled", 'disabled');
//        $scope.select_search(provinceID, "", null);


        $(provinceID).change(function () {
            $scope.$apply(function () {
                $scope.options.districts = [];
                var pID = $(provinceID).val();
                angular.forEach($scope.locations.districts, function (value, key) {
                    if (pID == value.provinceID) {
                        $scope.options.districts.push({value: value.id, text: value.name});
                    }
                });
                $scope.filter.provinceID = $("#provinceID").val();
            });
            $scope.switchConfig("input.switch-districtID", "input.switch-all-districtID");

            $('.switch-districtID').on("ifChanged", function () {
                var id = $(this).attr("id");
                var checked = $(this).is(":checked");
                $scope.$apply(function () {
                    if ($scope.filter.districtID == null) {
                        $scope.filter.districtID = {}
                    }
                    $scope.filter.districtID[id] = checked;

                    $.each($scope.options.sites, function (k, v) {
                        $scope.options.sites[k].show = false;
                        if ($scope.filter.districtID[v.districtID] == true) {
                            $scope.options.sites[k].show = true;
                        }
                    });

                    setTimeout(function () {
                        $scope.switchConfig("input.switch-siteID", "input.switch-all-siteID");
                    }, 50);
                });
            });

            var cd = 0;
            $('.switch-districtID').each(function () {
                if ($scope.filter.district == null || $scope.filter.district.length == 0 || $scope.filter.district.includes($(this).attr("id"))) {
                    cd += 1;
                    $(this).iCheck('check');
                }
                if (cd == $scope.options.districts.length) {
                    $("input.switch-all-districtID").iCheck('check');
                } else {
//                    $("input.switch-all-districtID").iCheck('uncheck');
                }
            });

            var csite = 0;
            $('.switch-siteID').each(function () {
                if ($scope.filter.site == null || $scope.filter.site.length == 0 || $scope.filter.site.includes($(this).attr("id"))) {
                    $(this).iCheck('check');
                    csite += 1;
                }
                if (csite == Object.keys($scope.options.sites).length) {
                    $("input.switch-all-siteID").iCheck('check');
                } else {
//                    $("input.switch-all-siteID").iCheck('uncheck');
                }
            });
        });

        $scope.switchConfig("input.switch-serviceTest", "input.switch-all-serviceTest");
        var cst = 0;
        $('.switch-serviceTest').each(function () {
            if ($scope.filter.serviceTest == null || $scope.filter.serviceTest.length == 0 || $scope.filter.serviceTest.includes($(this).attr("id"))) {
                $(this).iCheck('check');
                cst += 1;
            }
            if (cst == Object.keys($scope.options['service-test']).length) {
                $("input.switch-all-serviceTest").iCheck('check');
            } else {
//                $("input.switch-all-serviceTest").iCheck('uncheck');
            }
        });

        $scope.switchConfig("input.switch-quarter", "input.switch-all-quarter");
        var cq = 0;
        $('.switch-quarter').each(function () {
            if ($scope.filter.quarter == null || $scope.filter.quarter.length == 0 || $scope.filter.quarter.includes($(this).attr("id"))) {
                $(this).iCheck('check');
                cq += 1;
            }
            if ($scope.options.quarter && cq == Object.keys($scope.options.quarter).length) {
                $("input.switch-all-quarter").iCheck('check');
            } else {
//                $("input.switch-all-quarter").iCheck('uncheck');
            }
        });

        $(provinceID).val($scope.filter.provinceID).change();
    };

    $scope.getFilter = function () {
        var districtID = [];
        var siteID = [];
        var serviceTest = [];
        var quarter = [];
        $.each($scope.filter.districtID, function (k, v) {
            if (v) {
                districtID.push(k);
            }
        });
        $(".switch-siteID").each(function (k, v) {
            if ($(this).is(":checked") && $(this).attr("data-show") == 'true') {
                siteID.push($(this).attr("id"));
            }
        });
        $(".switch-serviceTest").each(function (k, v) {
            if ($(this).is(":checked")) {
                serviceTest.push($(this).attr("id"));
            }
        });
        $(".switch-quarter").each(function (k, v) {
            if ($(this).is(":checked")) {
                quarter.push($(this).attr("id"));
            }
        });

        if (serviceTest.length == 0 && $scope.options['service-test']) {
            $.each($scope.options['service-test'], function (k, v) {
                serviceTest.push(k);
            });
        }
        if (quarter.length == 0 && $scope.options.quarter) {
            $.each($scope.options.quarter, function (k, v) {
                quarter.push(k);
            });
        }

        $scope.filter.district = districtID;
        $scope.filter.site = siteID;
        $scope.filter.serviceTest = serviceTest;
        $scope.filter.quarter = quarter;
        return $scope.filter;
    };

    $scope.report = function () {
        $(".dashboard-height").each(function () {
            $(this).html('<i class="fa fa-sun-o fa-spin"></i>');
        });
        loading.show();
        $.ajax({
            url: urlIndicator,
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        $scope.options = resp.data.options;
                        $scope.options.sites = resp.data.siteOptions;
                        $scope.filter = resp.data.form;
                    });
                    $scope.initFilter();
                    setTimeout(function () {
                        $scope.getFilter();
                        if ($scope.filter.site.length == 0) {
                            msg.warning("Vui lòng chọn cơ sở");
                            return false;
                        }
                        $scope.router();
                    }, 50);
                } else {
                    $scope.$apply(function () {
                        $scope.errors = resp.data;
                    });
                    if (resp.message) {
                        msg.danger(resp.message);
                    }
                }
            }
        });
    };

    $scope.router = function () {
        window.location.hash = $scope.filter.indicatorName;
        if ($scope.filter.indicatorName === 'htc') {
            $scope.htcChar01();
            $scope.htcChar02();
            $scope.htcChar03();
            $scope.htcChar04();
        } else if ($scope.filter.indicatorName === 'htc-01') {
            //D2.1_TV&nhận KQ
            $scope.d21Char01();
            $scope.d21Char02();
            $scope.d21Char03();
            $scope.d21Char04();
            $scope.d21Char05();
            $scope.d21Char06();
        } else if ($scope.filter.indicatorName === 'htc-02') {
            //D2.2
            $scope.d22Char01();
            $scope.d22Char02();
            $scope.d22Char03();
            $scope.d22Char04();
            $scope.d22Char05();
            $scope.d22Char06();
        } else if ($scope.filter.indicatorName === 'htc-03') {
            //D2.3
            $scope.d23Char01();
            $scope.d23Char02();
            $scope.d23Char03();
            $scope.d23Char04();
            $scope.d23Char05();
            $scope.d23Char06();
        } else if ($scope.filter.indicatorName === 'htc-04') {
            //D2.4
            $scope.d24Char01();
            $scope.d24Char02();
            $scope.d24Char04();
            $scope.d24Char03();
            $scope.d24Char05();
            $scope.d24Char06();
        } else if ($scope.filter.indicatorName === 'htc-05') {
            //D2.5
            $scope.d25Char01();
            $scope.d25Char02();
            $scope.d25Char03();
            $scope.d25Char04();
            $scope.d25Char05();
            $scope.d25Char06();
        }
    };

    $scope.htcChar01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/htc-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    $.each(resp.data, function (k, v) {
                        resp.data[k].percent = v.soxn == 0 ? 0 : ((v.nhanketqua * 100) / v.soxn);
                    });
                    amchart.bar("dashboard-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số xét nghiệm và số nhận kết quả";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soxn", "Số xét nghiệm", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhanketqua", "Số nhận kết quả", false, valueAxis, function (series, valueLabel) {
                            var pLabel = series.bullets.push(new am4charts.LabelBullet());
                            pLabel.label.text = "{percent.formatNumber('#.0')}%";
                            pLabel.locationX = 1;
                            pLabel.label.truncate = false;
                            pLabel.label.hideOversized = false;
                            pLabel.label.fontSize = 12;
                            pLabel.label.horizontalCenter = "left";
                            pLabel.label.dx = 5;
                            pLabel.locationY = 1;
                            pLabel.rotation = -90;
                            pLabel.label.fill = am4core.color("#fff");
                        });
                    });
                }
            }
        });
    };
    $scope.htcChar02 = function () {
        $.ajax({
            url: '/service/dashboard-htc/htc-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    $.each(resp.data, function (k, v) {
                        resp.data[k].percent = v.xetnghiemduongtinh == 0 ? 0 : ((v.duongtinhnhanketqua * 100) / v.xetnghiemduongtinh);
                    });
                    amchart.bar("dashboard-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số xét nghiệm dương tính và số dương tính nhận kết quả";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "xetnghiemduongtinh", "Số dương tính", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duongtinhnhanketqua", "Số nhận kết quả", false, valueAxis, function (series, valueLabel) {
                            var pLabel = series.bullets.push(new am4charts.LabelBullet());
                            pLabel.label.text = "{percent.formatNumber('#.0')}%";
                            pLabel.locationX = 1;
                            pLabel.label.truncate = false;
                            pLabel.label.hideOversized = false;
                            pLabel.label.fontSize = 12;
                            pLabel.label.horizontalCenter = "left";
                            pLabel.label.dx = 5;
                            pLabel.locationY = 1;
                            pLabel.rotation = -90;
                            pLabel.label.fill = am4core.color("#fff");
                        });
                    });
                }
            }
        });
    };
    $scope.htcChar03 = function () {
        $.ajax({
            url: '/service/dashboard-htc/htc-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    $.each(resp.data, function (k, v) {
                        resp.data[k].percent = v.duongtinh == 0 ? 0 : ((v.chuyengui * 100) / v.duongtinh);
                    });
                    amchart.bar("dashboard-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Chuyển gửi điều trị thành công";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duongtinh", "Số dương tính", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "chuyengui", "Số chuyển gửi điều trị thành công", false, valueAxis, function (series, valueLabel) {
                            var pLabel = series.bullets.push(new am4charts.LabelBullet());
                            pLabel.label.text = "{percent.formatNumber('#.0')}%";
                            pLabel.locationX = 1;
                            pLabel.label.truncate = false;
                            pLabel.label.hideOversized = false;
                            pLabel.label.fontSize = 12;
                            pLabel.label.horizontalCenter = "left";
                            pLabel.label.dx = 5;
                            pLabel.locationY = 1;
                            pLabel.rotation = -90;
                            pLabel.label.fill = am4core.color("#fff");
                        });

                    });
                }
            }
        });
    };

    $scope.htcChar04 = function () {
        $.ajax({
            url: '/service/dashboard-htc/htc-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Phân tích ca dương tính theo thời gian báo cáo";
                        categoryAxis.fontSize = 10;
                        categoryAxis.dataFields.category = "phanloai";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soca", null, true, valueAxis);
                    });
                }
            }
        });
    };


    $scope.d21Char01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/d21-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let result = resp.data;
                    amchart.bar("indicator-chart01", result, function (chart, title, categoryAxis, valueAxis) {
                        if (result !== null && result.length > 0) {
                            title.text = "Số xét nghiệm và số nhận kết quả từ " + result[0].thang + " đến " + result[result.length - 1].thang;
                        } else {
                            title.text = "Số xét nghiệm và số nhận kết quả";
                        }
                        categoryAxis.dataFields.category = "thang";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soxn", "Số xét nghiệm", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhanketqua", "Số nhận kết quả", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d21Char02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d21-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["nhanketqua"] === 'undefined' ? "0" : resp.data[i]["nhanketqua"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số xét nghiệm và đã nhận kết quả theo huyện năm " + filter.year + "\nTổng số xét nghiệm và đã nhận kết quả: " + tong.toMoney();
                        categoryAxis.dataFields.category = "huyen";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhanketqua", null, false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d21Char03 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d21-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["nam"] === 'undefined' ? "0" : resp.data[i]["nam"] + "") + 
                                parseInt(typeof resp.data[i]["nu"] === 'undefined' ? "0" : resp.data[i]["nu"] + "");
                    }
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số xét nghiệm và đã nhận kết quả theo giới tính năm " + filter.year + "\nTổng số xét nghiệm và đã nhận kết quả: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d21Char04 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d21-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["duoi15"] === 'undefined' ? "0" : resp.data[i]["duoi15"] + "") + 
                                parseInt(typeof resp.data[i]["15den24"] === 'undefined' ? "0" : resp.data[i]["15den24"] + "") + 
                                parseInt(typeof resp.data[i]["25den49"] === 'undefined' ? "0" : resp.data[i]["25den49"] + "")+ 
                                parseInt(typeof resp.data[i]["tren49"] === 'undefined' ? "0" : resp.data[i]["tren49"] + "");
                    }
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số xét nghiệm và đã nhận kết quả theo nhóm tuổi năm " + filter.year + "\nTổng số xét nghiệm và đã nhận kết quả: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi15", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "15den24", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "25den49", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren49", ">49", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d21Char05 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d21-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = 'Số xét nghiệm và đã nhận kết quả theo nhóm đối tượng năm ' + filter.year + "\nTổng số xét nghiệm và đã nhận kết quả: " + tong.toMoney();
                    });
                }
            }
        });
    };
    $scope.d21Char06 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d21-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart06", data, function (chart, title) {
                        title.text = 'Số xét nghiệm và đã nhận kết quả theo loại dịch vụ năm ' + filter.year + "\nTổng số xét nghiệm và đã nhận kết quả: " + tong.toMoney();
                    });
                }
            }
        });

    };

    // TrangBN
    $scope.d22Char06 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d22-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart06", data, function (chart, title) {
                        title.text = 'Số xét nghiệm dương tính đã nhận kết quả theo loại dịch vụ năm ' + filter.year + "\nTổng số xét nghiệm dương tính đã nhận kết quả: " + tong.toMoney();;
                    });
                }
            }
        });

    };

    // TrangBN
    $scope.d23Char06 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d23-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart06", data, function (chart, title) {
                        title.text = 'Số chuyển gửi điều trị thành công theo loại dịch vụ năm ' + filter.year + "\nTổng số chuyển gửi điều trị thành công: " + tong.toMoney();
                    });
                }
            }
        });

    };

    // TrangBN
    $scope.d24Char06 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d24-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart06", data, function (chart, title) {
                        title.text = 'Số ca có kết quả XN mới nhiễm <6 tháng theo loại dịch vụ năm ' + filter.year + "\nTổng số ca xét nghiệm mới nhiễm <6 tháng: " + tong.toMoney();
                    });
                }
            }
        });

    };

    // TrangBN
    $scope.d25Char06 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d25-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart06", data, function (chart, title) {
                        title.text = 'Số có kết quả xét nghiệm tải lượng virus <1.000 bản sao/ml theo loại dịch vụ năm ' + filter.year;
                    });
                }
            }
        });

    };

    $scope.d22Char01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/d22-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                let result = resp.data;
                if (resp.success) {
                    amchart.bar("indicator-chart01", result, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        if (result !== null && result.length > 0) {
                            title.text = "Số xét nghiệm dương tính và số dương tính nhận kết quả từ " + result[0].thang + " đến " + result[result.length - 1].thang;
                        } else {
                            title.text = "Số xét nghiệm dương tính và số dương tính nhận kết quả";
                        }
                        categoryAxis.dataFields.category = "thang";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soxn", "Số xét nghiệm dương tính", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhanketqua", "Số nhận kết quả", false, valueAxis);


                    });
                }
            }
        });
    };

    $scope.d23Char01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/d23-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                let result = resp.data;
                if (resp.success) {
                    amchart.bar("indicator-chart01", result, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        if (result !== null && result.length > 0) {
                            title.text = "Số chuyển gửi và chuyển gửi điều trị thành công theo tháng từ " + result[0].thang + " đến " + result[result.length - 1].thang;
                        } else {
                            title.text = "Số chuyển gửi và chuyển gửi điều trị thành công theo tháng";
                        }
                        categoryAxis.dataFields.category = "thang";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sochuyengui", "Số chuyển gửi điều trị", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sochuyenguithanhcong", "Số chuyển gửi điều trị thành công", false, valueAxis);


                    });
                }
            }
        });
    };

    // TrangBN
    $scope.d23Char03 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d23-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["nam"] === 'undefined' ? "0" : resp.data[i]["nam"] + "") + 
                                parseInt(typeof resp.data[i]["nu"] === 'undefined' ? "0" : resp.data[i]["nu"] + "");
                    }
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số chuyển gửi điều trị thành công theo giới tính năm " + filter.year + "\nTổng số chuyển gửi điều trị thành công: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    });
                }
            }
        });
    };

    // TrangBN
    $scope.d22Char03 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d22-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["nam"] === 'undefined' ? "0" : resp.data[i]["nam"] + "") + 
                                parseInt(typeof resp.data[i]["nu"] === 'undefined' ? "0" : resp.data[i]["nu"] + "");
                    }
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số xét nghiệm dương tính đã nhận kết quả theo giới tính năm " + filter.year + "\nTổng số xét nghiệm dương tính đã nhận kết quả: " + tong.toMoney();;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d22Char04 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d22-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["duoi15"] === 'undefined' ? "0" : resp.data[i]["duoi15"] + "") + 
                                parseInt(typeof resp.data[i]["15den24"] === 'undefined' ? "0" : resp.data[i]["15den24"] + "") + 
                                parseInt(typeof resp.data[i]["25den49"] === 'undefined' ? "0" : resp.data[i]["25den49"] + "")+ 
                                parseInt(typeof resp.data[i]["tren49"] === 'undefined' ? "0" : resp.data[i]["tren49"] + "");
                    }
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số xét nghiệm dương tính đã nhận kết quả theo nhóm tuổi năm " + filter.year + "\nTổng số xét nghiệm dương tính đã nhận kết quả: " + tong.toMoney();;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi15", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "15den24", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "25den49", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren49", ">49", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d24Char01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/d24-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                let result = resp.data;
                if (resp.success) {
                    amchart.bar("indicator-chart01", result, function (chart, title, categoryAxis, valueAxis) {
                        if (result !== null && result.length > 0) {
                            title.text = "Số ca làm xét nghiệm nhiễm mới và kết quả mới nhiễm từ " + result[0].thang + " đến " + result[result.length - 1].thang;
                        } else {
                            title.text = "Số ca làm xét nghiệm nhiễm mới và kết quả mới nhiễm";
                        }
                        categoryAxis.dataFields.category = "thang";
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tongxnnhiemmoi", "Số ca làm xét nghiệm phát hiện nhiễm mới", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhohon6", "Kết quả xét nghiệm mới nhiễm <6 tháng", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "lonhon12", "Kết quả xét nghiệm mới nhiễm >12 tháng", false, valueAxis);

                    });
                }
            }
        });
    };

    // TrangBN
    $scope.d24Char03 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d24-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                let tong = 0;
                for (let i = 0; i < resp.data.length; i++) {
                    tong += parseInt(typeof resp.data[i]["nam"] === 'undefined' ? "0" : resp.data[i]["nam"] + "") + 
                            parseInt(typeof resp.data[i]["nu"] === 'undefined' ? "0" : resp.data[i]["nu"] + "");
                }
                if (resp.success) {
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số ca có kết quả mới nhiễm < 6 tháng theo giới tính năm " + filter.year + "\nTổng số ca xét nghiệm mới nhiễm <6 tháng: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d25Char01 = function () {
        $.ajax({
            url: '/service/dashboard-htc/d25-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                let result = resp.data;
                if (resp.success) {
                    amchart.bar("indicator-chart01", result, function (chart, title, categoryAxis, valueAxis) {
                        if (result !== null && result.length > 0) {
                            title.text = "Số ca làm xét nghiệm tải lượng virus và kết quả xét nghiệm từ " + result[0].thang + " đến " + result[result.length - 1].thang;
                        } else {
                            title.text = "Số ca làm xét nghiệm tải lượng virus và kết quả xét nghiệm";
                        }
                        categoryAxis.dataFields.category = "thang";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tongxntailuongvirus", "Số ca làm xét nghiệm tải lượng virus", false, valueAxis);

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhohon6", "Kết quả xét nghiệm tải lượng virus <200 bản sao/ml", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "lonhon12", "Kết quả xét nghiệm tải lượng virus 200-1.000 bản sao/ml", false, valueAxis);

                    });
                }
            }
        });
    };

    // TrangBN
    $scope.d25Char03 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d25-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;

                        title.text = "Số ca có kết quả tải lượng virus < 1000 bản sao/ml theo giới tính năm " + filter.year;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d22Char02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d22-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["soxn"] === 'undefined' ? "0" : resp.data[i]["soxn"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số xét nghiệm dương tính đã nhận kết quả theo huyện năm " + filter.year + "\nTổng số xét nghiệm dương tính đã nhận kết quả: " + tong.toMoney();
                        categoryAxis.dataFields.category = "huyen";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soxn", null, false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d23Char02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d23-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["sochuyenguithanhcong"] === 'undefined' ? "0" : resp.data[i]["sochuyenguithanhcong"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số chuyển gửi điều trị thành công theo huyện năm " + filter.year + "\nTổng số chuyển gửi điều trị thành công: " + tong.toMoney();
                        categoryAxis.dataFields.category = "huyen";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sochuyenguithanhcong", null, false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d23Char04 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d23-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["duoi15"] === 'undefined' ? "0" : resp.data[i]["duoi15"] + "") + 
                                parseInt(typeof resp.data[i]["15den24"] === 'undefined' ? "0" : resp.data[i]["15den24"] + "") + 
                                parseInt(typeof resp.data[i]["25den49"] === 'undefined' ? "0" : resp.data[i]["25den49"] + "")+ 
                                parseInt(typeof resp.data[i]["tren49"] === 'undefined' ? "0" : resp.data[i]["tren49"] + "");
                    }
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số chuyển gửi điều trị thành công theo nhóm tuổi năm " + filter.year + "\nTổng số chuyển gửi điều trị thành công: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi15", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "15den24", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "25den49", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren49", ">49", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d24Char02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d24-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["tongxnnhiemmoi"] === 'undefined' ? "0" : resp.data[i]["tongxnnhiemmoi"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số ca có kết quả xét nghiệm mới nhiễm < 6 tháng theo huyện năm " + filter.year + "\nTổng số ca xét nghiệm mới nhiễm <6 tháng: " + tong.toMoney();
                        categoryAxis.dataFields.category = "huyen";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tongxnnhiemmoi", null, false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d24Char04 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d24-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["duoi15"] === 'undefined' ? "0" : resp.data[i]["duoi15"] + "") + 
                                parseInt(typeof resp.data[i]["15den24"] === 'undefined' ? "0" : resp.data[i]["15den24"] + "") + 
                                parseInt(typeof resp.data[i]["25den49"] === 'undefined' ? "0" : resp.data[i]["25den49"] + "")+ 
                                parseInt(typeof resp.data[i]["tren49"] === 'undefined' ? "0" : resp.data[i]["tren49"] + "");
                    }
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số ca có kết quả XN mới nhiễm < 6 tháng theo nhóm tuổi năm " + filter.year + "\nTổng số ca xét nghiệm mới nhiễm <6 tháng: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi15", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "15den24", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "25den49", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren49", ">49", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d25Char02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d25-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số ca có kết quả xét nghiệm tải lượng virus <1.000 bản sao/ml theo huyện năm " + filter.year;
                        categoryAxis.dataFields.category = "huyen";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.fontSize = 10;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tongxntailuongvirus", null, false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d25Char04 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d25-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số ca có kết quả xét nghiệm tải lượng virus <1.000 bản sao/ml theo nhóm tuổi năm " + filter.year;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi15", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "15den24", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "25den49", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren49", ">49", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d25Char05 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d25-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = 'Số ca có kết quả xét nghiệm tải lượng virus <1.000 bản sao/ml theo nhóm đối tượng năm ' + filter.year;
                    });
                }
            }
        });
    };
    $scope.d24Char05 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d24-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = 'Số ca có KQXN mới nhiễm <6 tháng theo nhóm đối tượng năm ' + filter.year + "\nTổng số ca xét nghiệm mới nhiễm <6 tháng: " + tong.toMoney();
                    });
                }
            }
        });
    };
    $scope.d23Char05 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d23-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = 'Số chuyển gửi điều trị thành công theo nhóm đối tượng năm ' + filter.year + "\nTổng số chuyển gửi điều trị thành công: " + tong.toMoney();
                    });
                }
            }
        });
    };
    $scope.d22Char05 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-htc/d22-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        tong += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = 'Số XN dương tính đã nhận kết quả theo nhóm đối tượng năm ' + filter.year + "\nTổng số xét nghiệm dương tính đã nhận kết quả: " + tong.toMoney();;
                    });
                }
            }
        });
    };
});

