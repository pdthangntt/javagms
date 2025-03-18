app.controller('dashboard_opc', function ($scope, msg, amchart, locations) {
    $scope.options = {};
    $scope.filter = {
        site: []
    };

    $scope.init = function () {
        if (window.location.hash != '') {
            $scope.filter.indicatorName = window.location.hash.replace("#", '');
        }
        $scope.report();
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
                        if ($scope.filter.site.length === 0) {
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

    $scope.getFilter = function () {
        let siteID = [];
        let gender = [];
        let ages = [];
        var quarter = [];
        $(".switch-siteID").each(function (k, v) {
            if ($(this).is(":checked") && $(this).attr("data-show") == 'true') {
                siteID.push($(this).attr("id"));
            }
        });
        $(".switch-gender").each(function (k, v) {
            if ($(this).is(":checked")) {
                gender.push($(this).attr("id"));
            }
        });
        $(".switch-ages").each(function (k, v) {
            if ($(this).is(":checked")) {
                ages.push($(this).attr("id"));
            }
        });
        $(".switch-quarter").each(function (k, v) {
            if ($(this).is(":checked")) {
                quarter.push($(this).attr("id"));
            }
        });
        $scope.filter.gender = gender;
        $scope.filter.ages = ages;
        $scope.filter.site = siteID;
        $scope.filter.quarter = quarter;
        return $scope.filter;
    };

    $scope.initFilter = function () {
        var csite = 0;
        $scope.$apply(function () {
            $.each($scope.options.sites, function (k, v) {
                $scope.options.sites[k].show = true;
            });
            $scope.switchConfig("input.switch-siteID", "input.switch-all-siteID");
        });
        $('.switch-siteID').each(function () {
            if ($scope.filter.site == null || $scope.filter.site.length == 0) {
                $(this).iCheck('check');
                csite += 1;
            }
            if (csite == Object.keys($scope.options.sites).length) {
                $("input.switch-all-siteID").iCheck('check');
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
            }
        });

        $scope.switchConfig("input.switch-gender", "input.switch-all-gender");
        var g = 0;
        $('.switch-gender').each(function () {
            if ($scope.filter.gender == null || $scope.filter.gender.length == 0 || $scope.filter.gender.includes($(this).attr("id"))) {
                $(this).iCheck('check');
                g += 1;
            }
            if (g === Object.keys($scope.options['gender']).length) {
                $("input.switch-all-gender").iCheck('check');
            }
        });
        $scope.switchConfig("input.switch-ages", "input.switch-all-ages");
        var a = 0;
        $('.switch-ages').each(function () {
            if ($scope.filter.ages == null || $scope.filter.ages.length == 0 || $scope.filter.ages.includes($(this).attr("id"))) {
                $(this).iCheck('check');
                a += 1;
            }
            if (a === Object.keys($scope.options['ages']).length) {
                $("input.switch-all-ages").iCheck('check');
            }
        });
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

    $scope.router = function () {
        window.location.hash = $scope.filter.indicatorName;
        if ($scope.filter.indicatorName === 'opc') {
            $scope.opcChar01();
            $scope.opcChar02();
            $scope.opcChar03();
            $scope.opcChar04();
        } else if ($scope.filter.indicatorName === 'opc-01') {
            $scope.d11Chart01();
            $scope.d11Chart02();
            $scope.d11Chart03();
            $scope.d11Chart04();
        } else if ($scope.filter.indicatorName === 'opc-02') {
            $scope.d12Chart01();
            $scope.d12Chart02();
            $scope.d12Chart03();
            $scope.d12Chart04();
            $scope.d12Char05();
            $scope.d12Char06();
        } else if ($scope.filter.indicatorName === 'opc-03') {
            $scope.d13Chart01();
            $scope.d13Chart02();
            $scope.d13Chart03();
            $scope.d13Chart04();
            $scope.d13Char05();
            $scope.d13Char06();
        } else if ($scope.filter.indicatorName === 'opc-04') {
            $scope.d14Chart01();
            $scope.d14Chart02();
            $scope.d14Chart03();
            $scope.d14Chart04();
            $scope.d14Char05();
        } else if ($scope.filter.indicatorName === 'opc-05') {
            $scope.d15Chart01();
            $scope.d15Chart02();
            $scope.d15Chart03();
            $scope.d15Chart04();
            $scope.d15Char05();
        }
    };

    $scope.opcChar01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/opc-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số lũy tích điều trị và số điều trị mới";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "luytich", "Số lũy tích điều trị", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "moi", "Số điều trị mới", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });
    };


    $scope.d12Char05 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart05", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý theo bảo hiểm năm  " + resp.data.year + "\nTổng số đang quản lý: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "co", "Có BHYT", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khong", "Không có BHYT", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });
    };
    $scope.d12Char06 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart06", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý trễ hẹn tái khám năm " + resp.data.year + "\nTổng số đang QL trễ hẹn tái khám: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nho1", "Từ 1 tuần đến < 1 tháng", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tu1den3", "Từ 1 tháng đến 3 tháng", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "lon3", "> 3 tháng", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d13Char06 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart06.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart06", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị trễ hẹn tái khám năm " + resp.data.year+ "\nTổng số đang điều trị trễ hẹn tái khám: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nho1", "Từ 1 tuần đến < 1 tháng", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tu1den3", "Từ 1 tháng đến 3 tháng", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "lon3", "> 3 tháng", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d13Char05 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart05", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị theo bảo hiểm năm  " + resp.data.year+ "\nTổng số đang điều trị: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "co", "Có BHYT", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khong", "Không có BHYT", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });
    };
    $scope.d14Char05 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d14-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart05", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đăng ký mới theo bảo hiểm năm " + resp.data.year + "\nTổng số đăng ký mới: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "co", "Có BHYT", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khong", "Không có BHYT", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });
    };

    $scope.d15Char05 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d15-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart05", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân xét nghiệm tải lượng virus theo bảo hiểm năm " + resp.data.year + "\nTổng số làm XN TLVR: " + resp.data.total.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "co", "Có BHYT", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khong", "Không có BHYT", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });
    };


    $scope.opcChar02 = function () {

        $.ajax({
            url: '/service/dashboard-opc/opc-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {

                    amchart.bar("dashboard-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số đang quản lý và số đang điều trị";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "manage", "Số đang quản lý", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "manageBhyt", "Số đang QL có bảo hiểm", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "treatment", "Số đang điều trị", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "treatmentBhyt", "Số đang ĐT có bảo hiểm", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.opcChar03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/opc-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Theo dõi xét nghiệm tải lượng virus";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khongphathien", "Không phát hiện", true, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoinguongphathien", "Dưới ngưỡng phát hiện", true, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "duoi200", "Từ ngưỡng phát hiện đến < 200 bản sao/ml", true, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tu200den1000", "Từ 200 - < 1000 bản sao/ml", true, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tren1000", ">= 1000 bản sao/ml", true, valueAxis);
                    });
                }
            }
        });
    };
    $scope.opcChar04 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-opc/opc-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Bệnh nhân biến động điều trị";
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "dangkymoi", "Đăng ký mới", false, valueAxis, "", function (series) {
                            var circleBullet = series.bullets.push(new am4charts.CircleBullet());
                            circleBullet.circle.stroke = am4core.color("#fff");
                            circleBullet.circle.strokeWidth = 2;
                            circleBullet.tooltipText = "Đăng ký mới: [bold]{dangkymoi}[/]";
                        });
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "dieutrilai", "Điều trị lại", false, valueAxis, "", function (series) {
                            var circleBullet = series.bullets.push(new am4charts.CircleBullet());
                            circleBullet.circle.stroke = am4core.color("#fff");
                            circleBullet.circle.strokeWidth = 2;
                            circleBullet.tooltipText = "Điều trị lại: [bold]{dieutrilai}[/]";
                        });
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "botri", "Bỏ trị", false, valueAxis, "", function (series) {
                            var circleBullet = series.bullets.push(new am4charts.CircleBullet());
                            circleBullet.circle.stroke = am4core.color("#fff");
                            circleBullet.circle.strokeWidth = 2;
                            circleBullet.tooltipText = "Bỏ trị: [bold]{botri}[/]";
                        });
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tuvong", "Tử vong", false, valueAxis, "", function (series) {
                            var circleBullet = series.bullets.push(new am4charts.CircleBullet());
                            circleBullet.circle.stroke = am4core.color("#fff");
                            circleBullet.circle.strokeWidth = 2;
                            circleBullet.tooltipText = "Tử vong: [bold]{tuvong}[/]";
                        });
                    });
                }
            }
        });
    };
    
    $scope.d11Chart02 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d11-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                            tong += parseInt(typeof resp.data[i]["result"] === 'undefined' ? "0" : resp.data[i]["result"] + "");
                    }
                    amchart.bar("dashboard11-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân lũy tích theo cơ sở năm " + $scope.getFilter().year + "\nTổng số lũy tích: " + tong.toMoney();
                        categoryAxis.dataFields.category = "site";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "result", "Số lũy tích", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d12Chart02 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                            tong += parseInt(typeof resp.data[i]["result"] === 'undefined' ? "0" : resp.data[i]["result"] + "");
                    }
                    amchart.bar("dashboard11-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý theo cơ sở năm " + $scope.getFilter().year + "\nTổng số đang đang quản lý: " + tong.toMoney();
                        categoryAxis.dataFields.category = "site";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "result", "Số đang quản lý", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d13Chart02 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                            tong += parseInt(typeof resp.data[i]["result"] === 'undefined' ? "0" : resp.data[i]["result"] + "");
                    }
                    amchart.bar("dashboard11-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị theo cơ sở năm " + $scope.getFilter().year + "\nTổng số đang điều trị: " + tong.toMoney();
                        categoryAxis.dataFields.category = "site";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "result", "Số đang điều trị", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    $scope.d14Chart02 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d14-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                            tong += parseInt(typeof resp.data[i]["result"] === 'undefined' ? "0" : resp.data[i]["result"] + "");
                    }
                    
                    amchart.bar("dashboard11-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đăng ký mới theo cơ sở năm " + $scope.getFilter().year + "\nTổng số đăng ký mới: " + tong.toMoney();
                        categoryAxis.dataFields.category = "site";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "result", "Số đăng ký mới", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    $scope.d15Chart02 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d15-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                            tong += parseInt(typeof resp.data[i]["result"] === 'undefined' ? "0" : resp.data[i]["result"] + "");
                    }
                    
                    amchart.bar("dashboard11-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân xét nghiệm TLVR theo cơ sở năm " + $scope.getFilter().year + "\nTổng số làm xét nghiệm TLVR: " + tong.toMoney();
                        categoryAxis.dataFields.category = "site";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "result", "Số xét nghiệm TLVR", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d11Chart01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d11-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard11-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân lũy tích từ " + resp.data[0].min + " đến " + resp.data[0].max;
                        categoryAxis.dataFields.category = "yearSeries";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "year", "Số lũy tích", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d12Chart01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard11-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý từ " + resp.data[0].min + " đến " + resp.data[0].max;
                        categoryAxis.dataFields.category = "yearSeries";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "year", "Số đang quản lý", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d13Chart01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard11-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị từ " + resp.data[0].min + " đến " + resp.data[0].max;
                        categoryAxis.dataFields.category = "yearSeries";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "year", "Số đang điều trị", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    $scope.d14Chart01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d14-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard11-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đăng ký mới từ " + resp.data[0].min + " đến " + resp.data[0].max;
                        categoryAxis.dataFields.category = "yearSeries";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "year", "Số đăng ký mới", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d15Chart01 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d15-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard11-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân xét nghiệm TLVR từ " + resp.data[0].min + " đến " + resp.data[0].max;
                        categoryAxis.dataFields.category = "yearSeries";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "year", "Số xét nghiệm TLVR", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d11Chart03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d11-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                var last = resp.data.length - 1;
                let tong = parseInt(typeof resp.data[last]["nam"] === 'undefined' ? "0" : resp.data[last]["nam"] + "") + 
                            parseInt(typeof resp.data[last]["nu"] === 'undefined' ? "0" : resp.data[last]["nu"] + "");
                let nam = parseInt(typeof resp.data[last]["nam"] === 'undefined' ? "0" : resp.data[last]["nam"] + "");
                let nu = parseInt(typeof resp.data[last]["nu"] === 'undefined' ? "0" : resp.data[last]["nu"] + "");
                if (resp.success) {
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân lũy tích theo giới tính năm " + $scope.getFilter().year +"\n" + "Tổng số lũy tích: " + tong.toMoney() + " - Nam " + nam + " - Nữ " + nu;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d11Chart04 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d11-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    var last = resp.data.length - 1;
                    let tong = parseInt(typeof resp.data[last]["nl"] === 'undefined' ? "0" : resp.data[last]["nl"] + "") + 
                            parseInt(typeof resp.data[last]["te"] === 'undefined' ? "0" : resp.data[last]["te"] + "");
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân lũy tích theo nhóm tuổi năm " + $scope.getFilter().year +"\n" + "Tổng số lũy tích: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "te", "Trẻ em (< 15 tuổi)", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nl", "Người lớn (>= 15 tuổi)", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d12Chart03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    var last = resp.data.length - 1;
                    let nam = parseInt(typeof resp.data[last]["nam"] === 'undefined' ? "0" : resp.data[last]["nam"] + "");
                    let nu = parseInt(typeof resp.data[last]["nu"] === 'undefined' ? "0" : resp.data[last]["nu"] + "");
                    let tong = nam + nu;
                
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý theo giới tính năm " + $scope.getFilter().year + "\n" + "Tổng số đang quản lý: " + tong.toMoney() + " - Nam " + nam + " - Nữ " + nu;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d12Chart04 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d12-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    var last = resp.data.length - 1;
                    let tong = parseInt(typeof resp.data[last]["nl"] === 'undefined' ? "0" : resp.data[last]["nl"] + "") + 
                            parseInt(typeof resp.data[last]["te"] === 'undefined' ? "0" : resp.data[last]["te"] + "");
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang quản lý theo nhóm tuổi năm " + $scope.getFilter().year + "\n" + "Tổng số đang quản lý: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "te", "Trẻ em (< 15 tuổi)", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nl", "Người lớn (>= 15 tuổi)", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d13Chart03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    var last = resp.data.length - 1;
                    let nam = parseInt(typeof resp.data[last]["nam"] === 'undefined' ? "0" : resp.data[last]["nam"] + "");
                    let nu = parseInt(typeof resp.data[last]["nu"] === 'undefined' ? "0" : resp.data[last]["nu"] + "");
                    let tong = nam + nu;
                    amchart.bar("indicator-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị theo giới tính năm " + $scope.getFilter().year + "\n" + "Tổng số đang điều trị: " + tong.toMoney() + " - Nam " + nam + " - Nữ " + nu;
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d13Chart04 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d13-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    var last = resp.data.length - 1;
                    let tong = parseInt(typeof resp.data[last]["nl"] === 'undefined' ? "0" : resp.data[last]["nl"] + "") + 
                            parseInt(typeof resp.data[last]["te"] === 'undefined' ? "0" : resp.data[last]["te"] + "");
                    
                    amchart.bar("indicator-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đang điều trị theo nhóm tuổi năm " + $scope.getFilter().year + "\n" + "Tổng số đang điều trị: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "te", "Trẻ em (< 15 tuổi)", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nl", "Người lớn (>= 15 tuổi)", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d14Chart03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d14-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let nam = parseInt(resp.data.sum == null || typeof resp.data.sum["nam"] === 'undefined' ? "0" : resp.data.sum["nam"] + "");
                    let nu = parseInt(resp.data.sum == null || typeof resp.data.sum["nu"] === 'undefined' ? "0" : resp.data.sum["nu"] + "");
                    let tong = nam + nu;
                    amchart.bar("indicator-chart03", resp.data.result, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đăng ký mới theo giới tính năm " + $scope.getFilter().year + "\n" + "Tổng số đăng ký mới: " + tong.toMoney() + " - Nam " + nam.toMoney() + " - Nữ " + nu.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d14Chart04 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d14-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let nl = parseInt(resp.data.sum == null || typeof resp.data.sum["nl"] === 'undefined' ? "0" : resp.data.sum["nl"] + "");
                    let te = parseInt(resp.data.sum == null || typeof resp.data.sum["te"] === 'undefined' ? "0" : resp.data.sum["te"] + "");
                    let tong = nl + te;
                    amchart.bar("indicator-chart04", resp.data.result, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân đăng ký mới theo nhóm tuổi năm " + $scope.getFilter().year + "\n" + "Tổng số đăng ký mới: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "te", "Trẻ em (< 15 tuổi)", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nl", "Người lớn (>= 15 tuổi)", false, valueAxis);
                    });
                }
            }
        });
    };
    
    $scope.d15Chart03 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d15-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let nam = parseInt(resp.data.sum == null || typeof resp.data.sum["nam"] === 'undefined' ? "0" : resp.data.sum["nam"] + "");
                    let nu = parseInt(resp.data.sum == null || typeof resp.data.sum["nu"] === 'undefined' ? "0" : resp.data.sum["nu"] + "");
                    let tong = nam + nu;
                    amchart.bar("indicator-chart03", resp.data.result, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân xét nghiệm tải lượng virus theo giới tính năm " + $scope.getFilter().year + "\n" + "Tổng số làm XN TLVR: " + tong.toMoney() + " - Nam " + nam.toMoney() + " - Nữ " + nu.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;

                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nam", "Nam", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nu", "Nữ", false, valueAxis);
                    
                    });
                }
            }
        });
    };
    
    $scope.d15Chart04 = function () {
        $.ajax({
            url: '/service/dashboard-opc/d15-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let nl = parseInt(resp.data.sum == null || typeof resp.data.sum["nl"] === 'undefined' ? "0" : resp.data.sum["nl"] + "");
                    let te = parseInt(resp.data.sum == null || typeof resp.data.sum["te"] === 'undefined' ? "0" : resp.data.sum["te"] + "");
                    let tong = nl + te;
                    amchart.bar("indicator-chart04", resp.data.result, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số bệnh nhân xét nghiệm tải lượng virus theo nhóm tuổi năm " + $scope.getFilter().year + "\n" + "Tổng số làm XN TLVR: " + tong.toMoney();
                        categoryAxis.dataFields.category = "quy";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "te", "Trẻ em (< 15 tuổi)", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nl", "Người lớn (>= 15 tuổi)", false, valueAxis);
                    });
                }
            }
        });
    };
});
