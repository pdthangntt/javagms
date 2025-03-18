app.controller('dashboard_pac', function ($scope, msg, amchart, locations) {
    $scope.vi_districts = [];
    $scope.options = {};
    $scope.filter = {
        districtID: {},
        wardID: {}
    };

    $scope.init = function () {
        if (window.location.hash != '') {
            $scope.filter.indicatorName = window.location.hash.replace("#", '');
        }
        $.getJSON("/static/backend/plugin/leaflet/gis/" + site_province.id + '.json').done(function (data) {
            $scope.vi_districts = data.level2s;
            $scope.report();
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

    $scope.initFilter = function () {
        var provinceID = "#provinceID";
        $scope.initProvince(provinceID, null, null);

        $(provinceID).change(function () {
            var pID = $(provinceID).val();
            $scope.$apply(function () {
                $scope.options.districts = [];
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
                        $scope.filter.districtID = {};
                    }
                    $scope.filter.districtID[id] = checked;
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
                }
            });

            $scope.switchConfig("input.switch-gender", "input.switch-all-gender");
            var g = 0;
            $('.switch-gender').each(function () {
                if ($scope.filter.gender == null || $scope.filter.gender.length == 0 || $scope.filter.gender.includes($(this).attr("id"))) {
                    $(this).iCheck('check');
                    g += 1;
                }
                if (g == Object.keys($scope.options['gender']).length) {
                    $("input.switch-all-gender").iCheck('check');
                }
            });

            $scope.switchConfig("input.switch-mot", "input.switch-all-mot");
            var mot = 0;
            $('.switch-mot').each(function () {
                if ($scope.filter.modesOfTransmision == null || $scope.filter.modesOfTransmision.length == 0 || $scope.filter.modesOfTransmision.includes($(this).attr("id"))) {
                    $(this).iCheck('check');
                    mot += 1;
                }
                if (mot == Object.keys($scope.options['modes-of-transmision']).length) {
                    $("input.switch-all-mot").iCheck('check');
                }
            });

            $scope.switchConfig("input.switch-tog", "input.switch-all-tog");
            var tog = 0;
            $('.switch-tog').each(function () {
                if ($scope.filter.testObjectGroup == null || $scope.filter.testObjectGroup.length == 0 || $scope.filter.testObjectGroup.includes($(this).attr("id"))) {
                    $(this).iCheck('check');
                    tog += 1;
                }
                if (tog == Object.keys($scope.options['test-object-group']).length) {
                    $("input.switch-all-tog").iCheck('check');
                }
            });
        });

        $(provinceID).val($scope.filter.provinceID).change();
    };

    $scope.getFilter = function () {
        var districtID = [];
        var gender = [];
        var modesOfTransmision = [];
        var testObjectGroup = [];
        $.each($scope.filter.districtID, function (k, v) {
            if (v) {
                districtID.push(k);
            }
        });
        $(".switch-gender").each(function (k, v) {
            if ($(this).is(":checked")) {
                gender.push($(this).attr("id"));
            }
        });
        $(".switch-mot").each(function (k, v) {
            if ($(this).is(":checked")) {
                modesOfTransmision.push($(this).attr("id"));
            }
        });
        $(".switch-tog").each(function (k, v) {
            if ($(this).is(":checked")) {
                testObjectGroup.push($(this).attr("id"));
            }
        });

        if (gender.length == 0 && $scope.options['gender']) {
            $.each($scope.options['gender'], function (k, v) {
                gender.push(k);
            });
        }

        if (modesOfTransmision.length == 0 && $scope.options['modes-of-transmision']) {
            $.each($scope.options['modes-of-transmision'], function (k, v) {
                modesOfTransmision.push(k);
            });
        }

        if (testObjectGroup.length == 0 && $scope.options['test-object-group']) {
            $.each($scope.options['test-object-group'], function (k, v) {
                testObjectGroup.push(k);
            });
        }

        $scope.filter.district = districtID;
        $scope.filter.gender = gender;
        $scope.filter.modesOfTransmision = modesOfTransmision;
        $scope.filter.testObjectGroup = testObjectGroup;
        if ($("input#day").length > 0 && $("input#day").val() != '') {
            $scope.filter.day = $("input#day").val();
        }
        return $scope.filter;
    };

    $scope.router = function () {
        window.location.hash = $scope.filter.indicatorName;
        if ($scope.filter.indicatorName === 'pac') {
            $scope.pacChar01();
            $scope.pacChar02();
            $scope.pacChar03();
            $scope.pacChar04();
        } else if ($scope.filter.indicatorName === 'pac-01') {
            $scope.d11Char01();
            $scope.d11Char02();
            $scope.d11Char03();
            $scope.d11Char04();
            $scope.d11Char05();
        } else if ($scope.filter.indicatorName === 'pac-02') {
            $scope.switchConfig();
            $("#isColChart03").on('ifChanged', function () {
                $scope.d12Char03();
            });
            $("#isColChart04").on('ifChanged', function () {
                $scope.d12Char04();
            });
            $("#isColChart05").on('ifChanged', function () {
                $scope.d12Char05();
            });
            $scope.d12Char01();
            $scope.d12Char02();
            $scope.d12Char03();
            $scope.d12Char04();
            $scope.d12Char05();
        } else if ($scope.filter.indicatorName === 'pac-03') {
            $scope.d13Char01();
            $scope.d13Char02();
            $scope.d13Char03();
            $scope.d13Char04();
            $scope.d13Char05();
        } else if ($scope.filter.indicatorName === 'pac-04') {
            $scope.d14Char01();
            $scope.d14Char02();
            $scope.d14Char03();
            $scope.d14Char04();
            $scope.d14Char05();
        }
    };



    $scope.report = function (elm) {
        if (typeof elm != 'undefined' && elm == 'indicator' && $scope.filter.indicatorName == 'pac-02') {
            $scope.filter.year = '-1';
        }
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
                        $scope.filter = resp.data.form;
                        if ($scope.filter.quantityPatient == null) {
                            $scope.filter.quantityPatient = resp.data.quantityPatient;
                        }
                        if ($scope.filter.indicatorName != 'pac'
                                && $scope.filter.indicatorName.indexOf('pac') != -1
                                && ($scope.filter.day == null || $scope.filter.day == "")) {
                            var today = new Date();
                            $scope.filter.day = (today.getDate() < 10 ? '0' + today.getDate() : today.getDate()) + '/' + (today.getMonth() < 9 ? '0' + (today.getMonth() + 1) : (today.getMonth() + 1)) + '/' + today.getFullYear();
                        }
                    });
                    $scope.initFilter();
                    setTimeout(function () {
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

    /**
     * @auth vvThành
     * @returns {undefined}
     */
    $scope.pacChar01 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/pac-chartgeo.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var colorSet = new am4core.ColorSet();
                    var colorArr = [];
                    for (var i = 0; i < 6; i++) {
                        colorArr.push(colorSet.next().hex);
                    }

                    var container = L.DomUtil.get('dashboard-chart01');
                    if (container != null) {
                        container._leaflet_id = null;
                    }
                    var map = L.map('dashboard-chart01');
                    map.createPane('labels');
                    map.getPane('labels').style.zIndex = 550;
                    map.getPane('labels').style.pointerEvents = 'none';

                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {}).addTo(map);
                    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}.png', {
                        pane: 'labels'
                    }).addTo(map);

                    function getColor(d) {
                        var i = d > 500 ? 5 :
                                d > 400 ? 4 :
                                d > 300 ? 3 :
                                d > 200 ? 2 :
                                d > 100 ? 1 : 0;
                        return colorArr[i];
                    }

                    function style(feature) {
                        var d = -1;
                        for (var i = 0; i < resp.data.length; i++) {
                            if (utils.removeDiacritical(resp.data[i].name).indexOf(utils.removeDiacritical(feature.geometry.name)) != -1) {
                                d = resp.data[i].quantity;
                            }
                        }
                        return {
                            fillColor: getColor(d),
                            fillOpacity: (d == -1 ? 0 : 0.7),
                            weight: 1,
                            opacity: 1,
                            color: '#000',
                            dashArray: '2'
                        };
                    }

                    var info = L.control();
                    info.onAdd = function (map) {
                        this._div = L.DomUtil.create('div', 'info');
                        this.update();
                        return this._div;
                    };
                    info.update = function (props) {
                        if (props) {
                            var d = -1;
                            for (var i = 0; i < resp.data.length; i++) {
                                if (utils.removeDiacritical(resp.data[i].name).indexOf(utils.removeDiacritical(props.name)) != -1) {
                                    d = resp.data[i].quantity;
                                }
                            }
                        }
                        this._div.innerHTML = '<h5>Bản đồ tình hình dịch</h5>' + (props && d != -1 ? '<b>' + props.name + '</b> ' + d + ' người nhiễm' : '');
                    };
                    info.addTo(map);

                    function highlightFeature(e) {
                        var layer = e.target;
                        info.update(layer.feature.geometry);
                    }
                    function resetHighlight(e) {
                        info.update();
                    }

                    function onEachFeature(feature, layer) {
                        layer.on({
                            mouseover: highlightFeature,
                            mouseout: resetHighlight,
                        });
                    }

                    var geojson = L.geoJson($scope.vi_districts, {style: style, onEachFeature: onEachFeature}).addTo(map);
                    var geoBounds = geojson.getBounds();

                    var legend = L.control({position: 'bottomright'});
                    legend.onAdd = function (map) {
                        var div = L.DomUtil.create('div', 'leaflet-legend'),
                                grades = [0, 100, 200, 300, 400, 500],
                                labels = [];
                        for (var i = 0; i < grades.length; i++) {
                            div.innerHTML +=
                                    '<i style="background:' + getColor(grades[i] + 1) + '"></i> ' +
                                    (grades[i] == 0 ? grades[i] : grades[i] + 1) + (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
                        }
                        return div;
                    };
                    legend.addTo(map);

                    map.fitBounds(geoBounds);
                }
            }
        });
    };

    $scope.pacChar02 = function () {

        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-pac/pac-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        categoryAxis.renderer.labels.template.rotation = -45;
                        title.text = "Kết quả 90-90-90 " + resp.message + " đến " + filter.year;
                        categoryAxis.dataFields.category = "phanloai";
                        categoryAxis.dataFields.category.wrap = true;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "soca", "Thực tế", true, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "them", "Chỉ tiêu còn thiếu", true, valueAxis);
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh
     * Show d1 chart03
     * @return
     */
    $scope.pacChar03 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/pac-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {

                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số người nhiễm HIV lũy tích, còn sống, tử vong, phát hiện mới tính từ " + resp.data[0]['nam'] + " đến " + filter.year;
                        categoryAxis.dataFields.category = "nam";
//                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tichluy", "Số lũy tích", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "consong", "Số còn sống", false, valueAxis);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "phathien", "Số phát hiện", false, valueAxisLine);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tuvong", "Số tử vong", false, valueAxisLine);
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh
     * Show d1 chart04
     * @return
     */
    $scope.pacChar04 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/pac-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart04", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số người nhiễm đang điều trị ARV từ " + resp.data[0]['nam'] + " đến " + filter.year;
                        categoryAxis.dataFields.category = "nam";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "dangdieutri", "Số bệnh nhân đang điều trị", false, valueAxis);
                        amchart.createLineSeries(chart, categoryAxis.dataFields.category, "moidieutri", "Số bệnh nhân mới điều trị", false, valueAxis);
                    });
                }
            }
        });
    };


    $scope.d11Char01 = function () {
        $.ajax({
            url: '/service/dashboard-pac/d11-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {

                    amchart.bar("indicator-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Lũy tích số người nhiễm HIV đến " + $scope.getFilter().day;
                        categoryAxis.dataFields.category = "nam";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "luytich", null, false, valueAxis);
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh
     * Show d11 chart02
     * @return
     */
    $scope.d11Char02 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d11-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {

                    let tong = 0;
                    let resultArray = resp.data;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resp.data[i]["sotichluy"] === 'undefined' ? "0" : resp.data[i]["sotichluy"] + "");
                    }

                    amchart.bar("indicator-chart02", resultArray, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Lũy tích số người nhiễm HIV theo huyện đến " + filter.day + "\nTổng số người nhiễm luỹ tích: " + tong.toMoney();
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.dataFields.category = "tenhuyen";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sotichluy", null, false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d11Char03 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d11-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    let tongNam = 0;
                    let tongNu = 0;
                    let resultArray = resp.data;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resultArray[i]["1524"] === 'undefined' ? "0" : resultArray[i]["1524"] + "") + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "") + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "") + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "");
                        if (resultArray[i]["gender"] !== 'undefined' && resultArray[i]["gender"] === "Nam") {
                            tongNam += parseInt(typeof resultArray[i]["1524"] === 'undefined' ? "0" : resultArray[i]["1524"] + "") + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "") + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "") + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "")
                        }

                        if (resultArray[i]["gender"] !== 'undefined' && resultArray[i]["gender"] === "Nữ") {
                            tongNu += parseInt(typeof resultArray[i]["1524"] === 'undefined' ? "0" : resultArray[i]["1524"] + "") + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "") + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "") + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "")
                        }
                    }

                    for (let i = 0; i < resultArray.length; i++) {
                        if (resultArray[i].gender === "Không rõ")
                            resultArray.splice(i, 1);
                    }

                    amchart.bar("indicator-chart03", resultArray, function (chart, title, categoryAxis, valueAxis) {

                        if (tong === 0) {
                            title.text = "Luỹ tích số người nhiễm HIV theo giới tính và nhóm tuổi đến " + filter.day;
                        } else {
                            title.text = "Luỹ tích số người nhiễm HIV theo giới tính và nhóm tuổi đến " + filter.day + "\nSố người nhiễm luỹ tích: " + "Nam " + tongNam.toMoney() + " - Nữ " + tongNu.toMoney() + " - Tổng cộng " + tong.toMoney();
                        }

                        categoryAxis.dataFields.category = "gender";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "015", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "1524", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "2549", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "49max", ">49", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d11Char04 = function () {
        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-pac/d11-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var tong = 0;
                    tong = parseInt(resp.data.tong);
                    var data = [];
                    for (var i in resp.data.data) {
                        if (resp.data.data[i] == 0) {
                            continue;
                        }
                        data.push({
                            key: i,
                            count: resp.data.data[i]
                        });
                    }
                    amchart.pie("indicator-chart04", data, function (chart, title) {
                        title.text = ' ' + filter.day;
                        if (resp.data.tong === 0) {
                            title.text = "Lũy tích số người nhiễm HIV theo nhóm đối tượng đến " + filter.day;
                        } else {
                            title.text = "Lũy tích số người nhiễm HIV theo nhóm đối tượng đến " + filter.day + "\nTổng số người nhiễm luỹ tích: " + tong.toMoney();
                        }
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh
     * Show d11 chart05
     * @return
     */
    $scope.d11Char05 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d11-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let sum = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        sum += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = "Lũy tích số người nhiễm HIV theo đường lây đến " + filter.day + "\nTổng số người nhiễm lũy tích: " + sum.toMoney();
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d12 chart02
     * @return
     */
    $scope.d12Char02 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d12-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {

                    let tong = 0;
                    let tongNam = 0;
                    let tongNu = 0;
                    let resultArray = resp.data;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resp.data[i]["male"] === 'undefined' ? "0" : resp.data[i]["male"] + "") + parseInt(typeof resp.data[i]["female"] === 'undefined' ? "0" : resp.data[i]["female"] + "") + parseInt(typeof resp.data[i]["unclear"] === 'undefined' ? "0" : resp.data[i]["unclear"] + "");
                        tongNam += parseInt(typeof resp.data[i]["male"] === 'undefined' ? "0" : resp.data[i]["male"] + "");
                        tongNu += parseInt(typeof resp.data[i]["female"] === 'undefined' ? "0" : resp.data[i]["female"] + "");
                    }

                    if (filter.year == '-1') {

                        amchart.bar("indicator-chart02", resultArray, function (chart, title, categoryAxis, valueAxis) {

                            var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                            valueAxisLine.renderer.opposite = true;
                            valueAxisLine.min = 0;
                            if (resp.data[resp.data.length - 1]['nam'] == new Date().getFullYear() && resp.data.length == 1) {
                                title.text = "Số người nhiễm HIV phát hiện mới theo giới tính năm " + resp.data[resp.data.length - 1]['nam'] + "\nSố người nhiễm phát hiện mới: " + "Nam " + tongNam.toMoney() + " - Nữ " + tongNu.toMoney() + " - Tổng cộng " + tong.toMoney();
                            } else {
                                title.text = "Số người nhiễm HIV phát hiện mới theo giới tính từ " + resp.data[0]['nam'] + " đến " + resp.data[resp.data.length - 1]['nam'] + "\nSố người nhiễm phát hiện mới: " + "Nam " + tongNam.toMoney() + " - Nữ " + tongNu.toMoney() + " - Tổng cộng " + tong.toMoney();
                            }
                            categoryAxis.dataFields.category = "nam";
                            amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "male", "Nam", true, valueAxis);
                            amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "female", "Nữ", true, valueAxis);
                            amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "unclear", "Không rõ", true, valueAxis);
                        });
                    } else {
                        let sum = 0;
                        for (let i = 0; i < resp.data.length; i++) {
                            sum += resp.data[i]["sotichluy"];
                        }
                        amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                            title.text = "Số người nhiễm HIV phát hiện mới theo huyện năm " + filter.year + "\nTổng số người nhiễm HIV phát hiện mới: " + sum.toMoney();
                            categoryAxis.renderer.labels.template.rotation = -20;
                            categoryAxis.dataFields.category = "tenhuyen";
                            amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sotichluy", null, false, valueAxis);
                        });
                    }
                }
            }
        });
    };
    $scope.d12Char01 = function () {
        $.ajax({
            url: '/service/dashboard-pac/d12-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                let sum = 0;
                for (var i in resp.data.data) {
                    sum += resp.data.data[i]["luytich"];
                }
                var tong = $scope.getFilter().year != '-1' ? "\nTổng số người nhiễm phát hiện mới trong năm: " + sum.toMoney() : "";
                if (resp.success) {
                    amchart.bar("indicator-chart01", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số người nhiễm HIV phát hiện mới " + resp.data.time + tong;
                        categoryAxis.dataFields.category = "nam";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "luytich", null, false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d12Char03 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d12-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var tong = 0;
                    let tongNam = 0;
                    let tongNu = 0;
                    var resultArray = resp.data;
                    let startYear = 0;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resultArray[i]["1525"] === 'undefined' ? "0" : resultArray[i]["1525"] + "")
                                + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "")
                                + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "")
                                + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "")
                                + parseInt(typeof resultArray[i]["khongro"] === 'undefined' ? "0" : resultArray[i]["khongro"] + "");

                        if (resultArray[i]["gender"] !== 'undefined' && resultArray[i]["gender"] === "Nam") {
                            tongNam += parseInt(typeof resultArray[i]["1525"] === 'undefined' ? "0" : resultArray[i]["1525"] + "") + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "") + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "") + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "")
                        }

                        if (resultArray[i]["gender"] !== 'undefined' && resultArray[i]["gender"] === "Nữ") {
                            tongNu += parseInt(typeof resultArray[i]["1525"] === 'undefined' ? "0" : resultArray[i]["1525"] + "") + parseInt(typeof resultArray[i]["2549"] === 'undefined' ? "0" : resultArray[i]["2549"] + "") + parseInt(typeof resultArray[i]["015"] === 'undefined' ? "0" : resultArray[i]["015"] + "") + parseInt(typeof resultArray[i]["49max"] === 'undefined' ? "0" : resultArray[i]["49max"] + "")
                        }

                        if (startYear === 0 && tong !== 0) {
                            startYear = parseInt(resultArray[i]["year"]);
                        }
                    }

                    let d = new Date();
                    let toYear = parseInt(d.getFullYear() + "");
                    amchart.bar("indicator-chart03", resultArray, function (chart, title, categoryAxis, valueAxis) {
                        if (filter.year === '-1' && startYear !== 0 && startYear !== toYear) {
                            title.text = "Số người nhiễm HIV phát hiện mới theo nhóm tuổi từ " + startYear + " đến " + toYear;
                            categoryAxis.dataFields.category = "year";
                        } else if (startYear === toYear || startYear === 0) {
                            title.text = "Số người nhiễm HIV phát hiện mới theo giới tính và nhóm tuổi năm " + toYear;
                            categoryAxis.dataFields.category = "year";
                        } else {
                            title.text = "Số người nhiễm HIV phát hiện mới theo giới tính và nhóm tuổi năm " + filter.year;
                            categoryAxis.dataFields.category = "gender";
                        }

                        if (tong !== 0 && filter.year !== "-1") {
                            title.text = title.text + "\nTổng số người nhiễm HIV phát hiện mới: " + "Nam " + tongNam.toMoney() + " - Nữ " + tongNu.toMoney() + " - Tổng cộng " + tong.toMoney();
                        }
                        if (tong !== 0 && filter.year === "-1") {
                            title.text = title.text + "\nTổng số người nhiễm HIV phát hiện mới: " + tong.toMoney();
                        }
                        var isCheck = $("#isColChart03").is(':checked');
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "015", "<15", isCheck, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "1525", "15-24", isCheck, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "2549", "25-49", isCheck, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "49max", ">49", isCheck, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khongro", "Không rõ", isCheck, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d12Char04 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d12-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (filter.year != '-1') {
                    if (resp.success) {
                        var data = [];
                        for (var i in resp.data.data) {
                            if (resp.data.data[i] == 0) {
                                continue;
                            }
                            data.push({
                                key: i,
                                count: resp.data.data[i]
                            });
                        }
                        amchart.pie("indicator-chart04", data, function (chart, title) {
                            title.text = 'Số người nhiễm HIV phát hiện mới theo nhóm đối tượng ' + resp.data.time + "\nTổng số người nhiễm HIV phát hiện mới: " + resp.data.tong.toMoney();
                        });
                    }
                } else {
                    if (resp.success) {
                        amchart.bar("indicator-chart04", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                            var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                            valueAxisLine.renderer.opposite = true;
                            valueAxisLine.min = 0;

                            title.text = 'Số người nhiễm HIV phát hiện mới theo nhóm đối tượng ' + resp.data.time + "\nTổng số người nhiễm HIV phát hiện mới: " + resp.data.tong.toMoney();
                            categoryAxis.dataFields.category = "nam";

                            if ($("#isColChart04").is(':checked')) {
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "1", "Nghiện trích ma túy", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "2", "Phụ nữ bán dâm", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "9", "Người bán máu hiến máu", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "10", "Bệnh nhân nghi ngờ AIDS", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "6", "Bệnh nhân lao", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "11", "Bệnh nhân mắc các nhiễm trùng LTQĐTD", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "12", "Thanh niên khám tuyển nghĩa vụ quân sự", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "5", "Phụ nữ mang thai", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "3", "Nam quan hệ tình dục với nam (MSM)", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "7", "Các đối tượng khác", true, valueAxis);
                            } else {
                                chart.cursor = new am4charts.XYCursor();
                                var option = (series) => {
                                    series.fillOpacity = 0.6;
                                    series.strokeWidth = 2;
                                    series.stacked = true;
                                };
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "1", "Nghiện trích ma túy", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "2", "Phụ nữ bán dâm", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "9", "Người bán máu hiến máu", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "10", "Bệnh nhân nghi ngờ AIDS", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "6", "Bệnh nhân lao", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "11", "Bệnh nhân mắc các nhiễm trùng LTQĐTD", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "12", "Thanh niên khám tuyển nghĩa vụ quân sự", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "5", "Phụ nữ mang thai", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "3", "Nam quan hệ tình dục với nam (MSM)", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "7", "Các đối tượng khác", true, valueAxis, null, option);
                            }
                        });
                    }
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d12 chart05
     * @param form
     * @return
     */
    $scope.d12Char05 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d12-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    if (filter.year == '-1') {
                        amchart.bar("indicator-chart05", resp.data.data, function (chart, title, categoryAxis, valueAxis) {
                            let tong = 0;
                            tong += resp.data.tong;
                            var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                            valueAxisLine.renderer.opposite = true;
                            valueAxisLine.min = 0;
                            if (resp.data.data[resp.data.data.length - 1]['nam'] == new Date().getFullYear() && resp.data.data.length == 1) {
                                title.text = "Số người nhiễm HIV phát hiện mới theo đường lây năm " + resp.data.data[resp.data.data.length - 1]['nam'] + "\nTổng số người nhiễm HIV phát hiện mới: " + tong.toMoney();
                            } else {
                                title.text = "Số người nhiễm HIV phát hiện mới theo đường lây từ " + resp.data.data[0]['nam'] + " đến " + resp.data.data[resp.data.data.length - 1]['nam'] + "\nTổng số người nhiễm HIV phát hiện mới: " + tong.toMoney();
                            }
                            categoryAxis.dataFields.category = "nam";

                            if ($("#isColChart05").is(':checked')) {
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "mau", "Lây qua đường máu", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tinhduc", "Lây qua đường tình dục", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "mesangcon", "Mẹ truyền sang con", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khongro", "Không rõ", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "truyenmau", "Truyền máu", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tainannghenghiep", "Tai nạn nghề nghiệp", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tiemchich", "Lây qua đường tiêm chích ma túy", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tdkhacgioi", "Tình dục khác giới", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tddonggioi", "Tình dục đồng giới", true, valueAxis);
                                amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "khongcothongtin", "Không có thông tin", true, valueAxis);
                            } else {
                                chart.cursor = new am4charts.XYCursor();
                                var option = (series) => {
                                    series.fillOpacity = 0.6;
                                    series.strokeWidth = 2;
                                    series.stacked = true;
                                };
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "mau", "Lây qua đường máu", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tinhduc", "Lây qua đường tình dục", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "mesangcon", "Mẹ truyền sang con", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "khongro", "Không rõ", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "truyenmau", "Truyền máu", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tainannghenghiep", "Tai nạn nghề nghiệp", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tiemchich", "Lây qua đường tiêm chích ma túy", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tdkhacgioi", "Tình dục khác giới", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "tddonggioi", "Tình dục đồng giới", true, valueAxis, null, option);
                                amchart.createLineSeries(chart, categoryAxis.dataFields.category, "khongcothongtin", "Không có thông tin", true, valueAxis, null, option);
                            }
                        });
                    } else {
                        var data = [];
                        let tong = 0;
                        tong += resp.data.tong;
                        for (var i in resp.data.data) {
                            if (resp.data.data[i] == 0) {
                                continue;
                            }
                            data.push({
                                key: i,
                                count: resp.data.data[i]
                            });
                        }
                        amchart.pie("indicator-chart05", data, function (chart, title) {
                            title.text = "Số người nhiễm HIV phát hiện mới theo đường lây năm " + filter.year + "\nTổng số người nhiễm HIV phát hiện mới: " + tong.toMoney();
                        });
                    }
                }
            }
        });
    };
    $scope.d13Char01 = function () {
        $.ajax({
            url: '/service/dashboard-pac/d13-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("indicator-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số người nhiễm HIV còn sống đến " + $scope.getFilter().day;
                        categoryAxis.dataFields.category = "nam";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "luytich", null, false, valueAxis);
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d13 chart02
     * @return
     */
    $scope.d13Char02 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d13-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["sotichluy"] === 'undefined' ? "0" : resp.data[i]["sotichluy"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số người nhiễm HIV còn sống theo huyện đến " + filter.day + "\nTổng số người nhiễm HIV còn sống: " + tong.toMoney();
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.dataFields.category = "tenhuyen";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sotichluy", null, false, valueAxis);
                    });
                }
            }
        });
    };


    $scope.d13Char03 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d13-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var tong = 0;
                    var resultArray = resp.data;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resp.data[i]["1525"] === 'undefined' ? "0" : resp.data[i]["1525"] + "") + parseInt(typeof resp.data[i]["2549"] === 'undefined' ? "0" : resp.data[i]["2549"] + "") + parseInt(typeof resp.data[i]["015"] === 'undefined' ? "0" : resp.data[i]["015"] + "") + parseInt(typeof resp.data[i]["49max"] === 'undefined' ? "0" : resp.data[i]["49max"] + "") + parseInt(typeof resp.data[i]["khongro"] === 'undefined' ? "0" : resp.data[i]["khongro"] + "");
                    }

                    for (let i = 0; i < resp.data.length; i++) {
                        if (resp.data[i].gender === "Không rõ")
                            resultArray.splice(i, 1);
                    }

                    amchart.bar("indicator-chart03", resultArray, function (chart, title, categoryAxis, valueAxis) {

                        if (tong === 0) {
                            title.text = "Số người nhiễm HIV còn sống theo giới tính và nhóm tuổi đến " + filter.day;
                        } else {
                            title.text = "Số người nhiễm HIV còn sống theo giới tính và nhóm tuổi đến " + filter.day + "\nTổng số người nhiễm HIV còn sống: " + tong.toMoney();
                        }

                        categoryAxis.dataFields.category = "gender";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "015", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "1525", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "2549", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "49max", ">49", false, valueAxis);
                    });
                }
            }
        });
    };
    $scope.d13Char04 = function () {
        var filter = $scope.getFilter();

        $.ajax({
            url: '/service/dashboard-pac/d13-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var data = [];
                    let sum = 0;
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        sum += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart04", data, function (chart, title) {
                        title.text = 'Số người nhiễm HIV còn sống theo nhóm đối tượng đến ' + filter.day + "\nTổng số người nhiễm HIV còn sống: " + sum.toMoney();
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d13 chart05
     * @return
     */
    $scope.d13Char05 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d13-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let sum = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        sum += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = "Số người nhiễm HIV còn sống theo đường lây đến " + filter.day + "\nTổng số người nhiễm HIV còn sống: " + sum.toMoney();
                    });
                }
            }
        });
    };

    $scope.d14Char01 = function () {
        $.ajax({
            url: '/service/dashboard-pac/d14-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("indicator-chart01", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        var valueAxisLine = chart.yAxes.push(new am4charts.ValueAxis());
                        valueAxisLine.renderer.opposite = true;
                        valueAxisLine.min = 0;
                        title.text = "Số người nhiễm HIV tử vong đến " + $scope.getFilter().day;
                        categoryAxis.dataFields.category = "nam";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "luytich", null, false, valueAxis);
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d14 chart02
     * @return
     */
    $scope.d14Char02 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d14-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let tong = 0;
                    for (let i = 0; i < resp.data.length; i++) {
                        tong += parseInt(typeof resp.data[i]["sotichluy"] === 'undefined' ? "0" : resp.data[i]["sotichluy"] + "");
                    }
                    amchart.bar("indicator-chart02", resp.data, function (chart, title, categoryAxis, valueAxis) {
                        title.text = "Số người nhiễm HIV tử vong theo huyện đến " + filter.day + "\nTổng số người nhiễm HIV tử vong: " + tong.toMoney();
                        categoryAxis.renderer.labels.template.rotation = -20;
                        categoryAxis.dataFields.category = "tenhuyen";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "sotichluy", null, false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d14Char03 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d14-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    var tong = 0;
                    var resultArray = resp.data;
                    for (let i = 0; i < resultArray.length; i++) {
                        tong += parseInt(typeof resp.data[i]["1525"] === 'undefined' ? "0" : resp.data[i]["1525"] + "") + parseInt(typeof resp.data[i]["2549"] === 'undefined' ? "0" : resp.data[i]["2549"] + "") + parseInt(typeof resp.data[i]["015"] === 'undefined' ? "0" : resp.data[i]["015"] + "") + parseInt(typeof resp.data[i]["49max"] === 'undefined' ? "0" : resp.data[i]["49max"] + "");
                    }

                    for (let i = 0; i < resp.data.length; i++) {
                        if (resp.data[i].gender === "Không rõ")
                            resultArray.splice(i, 1);
                    }

                    amchart.bar("indicator-chart03", resultArray, function (chart, title, categoryAxis, valueAxis) {

                        if (tong === 0) {
                            title.text = "Số người nhiễm HIV tử vong theo giới tính và nhóm tuổi đến " + filter.day;
                        } else {
                            title.text = "Số người nhiễm HIV tử vong theo giới tính và nhóm tuổi đến " + filter.day + "\nTổng số người nhiễm HIV tử vong : " + tong.toMoney();
                        }

                        categoryAxis.dataFields.category = "gender";
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "015", "<15", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "1525", "15-24", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "2549", "25-49", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "49max", ">49", false, valueAxis);
                    });
                }
            }
        });
    };

    $scope.d14Char04 = function () {

        $.ajax({
            url: '/service/dashboard-pac/d14-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.getFilter()),
            success: function (resp) {
                if (resp.success) {
                    let sum = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        sum += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart04", data, function (chart, title) {
                        title.text = 'Số người nhiễm HIV tử vong theo nhóm đối tượng đến ' + $scope.getFilter().day + "\nTổng số người nhiễm HIV tử vong: " + sum.toMoney();
                    });
                }
            }
        });
    };

    /**
     * @author DSNAnh 
     * Get data d14 chart05
     * @return
     */
    $scope.d14Char05 = function () {
        var filter = $scope.getFilter();
        $.ajax({
            url: '/service/dashboard-pac/d14-chart05.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify(filter),
            success: function (resp) {
                if (resp.success) {
                    let sum = 0;
                    var data = [];
                    for (var i in resp.data) {
                        if (resp.data[i] == 0) {
                            continue;
                        }
                        sum += resp.data[i];
                        data.push({
                            key: i,
                            count: resp.data[i]
                        });
                    }
                    amchart.pie("indicator-chart05", data, function (chart, title) {
                        title.text = "Số người nhiễm HIV tử vong theo đường lây đến " + filter.day + "\nTổng số người nhiễm HIV tử vong: " + sum.toMoney();
                    });
                }
            }
        });
    };
});
