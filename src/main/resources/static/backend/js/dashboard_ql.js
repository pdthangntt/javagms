app.controller('dashboard_ql', function ($scope, msg, amchart, $uibModal, locations) {
    $scope.options = {};
    $scope.filter = {};
    $scope.vi_provinces = [];

    $scope.init = async function () {
        if (window.location.hash != '') {
            $scope.filter.indicatorName = window.location.hash.replace("#", '');
        }
        let data = await $.getJSON("/static/backend/plugin/leaflet/gis/vn.json");
//        let provinces = [];
//        for (let key in data) {
//            let item = await $.getJSON(`/static/backend/plugin/leaflet/gis/${key}.json`);
//            item.level2s = [];
//            provinces.push(item);
//        }
//        console.log(JSON.stringify(provinces));
        $scope.vi_provinces = data;
        $scope.report();
    };

    $scope.report = function () {
//        $(".dashboard-height").each(function () {
//            $(this).html('<i class="fa fa-sun-o fa-spin"></i>');
//        });
        loading.show();
        $.ajax({
            url: '/service/dashboard-ql/indicator.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.filter),
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $scope.$apply(function () {
                        $scope.options = resp.data.options;
                        $scope.filter = resp.data.form;
                    });

                    $scope.router();
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
        $scope.qlChar01();
        $scope.qlChar02();
        $scope.qlChar03();
        $scope.qlChar04();
    };

    $scope.qlChar01 = function () {
        $.ajax({
            url: '/service/dashboard-ql/ql-chart01.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.filter),
            success: function (resp) {
                if (resp.success) {
                    var colorArr = ['#fff', '#fff5c6', '#ffcaa6', '#ff6c66', '#ff2847', '#c8002a'];
                    var container = L.DomUtil.get('dashboard-chart01');
                    if (container != null) {
                        container._leaflet_id = null;
                    }
                    var map = L.map('dashboard-chart01', {
                        zoomSnap: 0.248
                    });
                    map.createPane('labels');
                    map.getPane('labels').style.zIndex = 550;
                    map.getPane('labels').style.pointerEvents = 'none';
//
//                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {}).addTo(map);
//                    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}.png', {
//                        pane: 'labels'
//                    }).addTo(map);

                    function getColor(d) {
                        var i = d > 1000 ? 5 :
                                d > 750 ? 4 :
                                d > 500 ? 3 :
                                d > 250 ? 2 :
                                d >= 1 ? 1 : 0;
                        return colorArr[i];
                    }

                    function style(feature) {
                        var d = -1;
                        for (var i = 0; i < resp.data.length; i++) {
//                                if (feature.geometry.level1_id == resp.data[i].id) {
//                                    d = resp.data[i].quantity;
//                                }
                            if (utils.removeDiacritical(resp.data[i].name).indexOf(utils.removeDiacritical(feature.geometry.name)) != -1) {
                                d = resp.data[i].htc;
                            }
                        }
                        return {
                            fillColor: getColor(d),
                            fillOpacity: (d == -1 ? 0.5 : 0.9),
//                                fillOpacity: (d == -1 ? 0 : 0.7),
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
                        var d = null;
                        if (props) {
                            for (var i = 0; i < resp.data.length; i++) {
                                if (utils.removeDiacritical(resp.data[i].name).indexOf(utils.removeDiacritical(props.name)) != -1) {
                                    d = resp.data[i];
                                }
                            }
                        }
                        let html = `<h5>Tình hình dịch ${props ? props.name : ''}</h5>`;
                        if (d) {
                            html += `<div style='text-align: left'>Số ca phát hiện trong năm: ${d.htc}</div>`;
                            html += `<div style='text-align: left'>Số ca nhiễm mới trong năm: ${d.early}</div>`;
                            html += `<div style='text-align: left'>Số ca hiện còn sống: ${d.quantity}</div>`;
                        }
                        this._div.innerHTML = html;
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

                    var geojson = L.geoJson($scope.vi_provinces, {style: style, onEachFeature: onEachFeature}).addTo(map);
                    var geoBounds = geojson.getBounds();

                    var legend = L.control({position: 'bottomright'});
                    legend.onAdd = function (map) {
                        var div = L.DomUtil.create('div', 'leaflet-legend'),
                                grades = [1, 250, 500, 750, 1000],
                                labels = [];
                        for (var i = 0; i < grades.length; i++) {
                            div.innerHTML +=
                                    '<i style="background:' + getColor(grades[i] + 1) + '"></i> ' +
                                    (grades[i] == 1 ? grades[i] : grades[i] + 1) + (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
                        }
                        return div;
                    };
                    legend.addTo(map);
                    map.fitBounds(geoBounds);
                }
            }
        });
    };


    $scope.itemp = [];
    $scope.itemd = [];
    $scope.months = [];
    $scope.levelDisplay = 'province';
    $scope.ql2ItemLoading = true;
    $scope.qlChar02 = function () {
        $scope.itemp = [];
        $scope.itemd = [];
        $scope.months = [];
        $scope.levelDisplay = 'province';
        $scope.ql2ItemLoading = true;
        $.ajax({
            url: '/service/dashboard-ql/ql-chart02.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.filter),
            success: function (resp) {
                if (resp.success) {
                    $scope.$apply(function () {
                        $scope.itemp = resp.data.provinces;
                        $scope.itemd = resp.data.districts;
                        $scope.levelDisplay = resp.data.levelDisplay;
                        $scope.months = resp.data.months;
                        $scope.ql2ItemLoading = false;
                    });
                }
            }
        });
    };

    $scope.detaill = function (provinceID, time, level, provinceName) {
        loading.show();
        $.ajax({
            url: '/service/dashboard-ql/ql-chart02-detail.json',
            data: {province_id: provinceID, time: time, level: level},
            method: 'GET',
            success: function (resp) {
                loading.hide();
                if (resp.success) {
                    $uibModal.open({
                        animation: true,
                        backdrop: 'static',
                        templateUrl: 'detaill',
                        controller: 'ql_detail_model',
                        size: 'xl',
                        resolve: {
                            params: function () {
                                return {
                                    item: resp.data.data,
                                    time: time,
                                    provinceName: provinceName,
                                };
                            }
                        }
                    });
                }
            }
        });
    };

    $scope.qlChar03 = function () {
        $.ajax({
            url: '/service/dashboard-ql/ql-chart03.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.filter),
            success: function (resp) {
                if (resp.success) {
                    amchart.bar("dashboard-chart03", resp.data, function (chart, title, categoryAxis, valueAxis) {
//                        title.text = "BIỂU ĐỒ SỐ PHÁT HIỆN MỚI QUA CÁC NĂM";
                        categoryAxis.dataFields.category = "nam";
                        categoryAxis.renderer.labels.template.rotation = -20;
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "nhiem_moi", "Số phát hiện mới", false, valueAxis);
                        amchart.createColumnSeries(chart, categoryAxis.dataFields.category, "tu_vong", "Số tử vong", false, valueAxis, function (series, valueLabel) {
                        });
                    });
                }
            }
        });

    };

    $scope.qlItem4 = [];
    $scope.qlItemLoading = false;
    $scope.qlChar04 = function () {
        $scope.qlItemLoading = true;
        $.ajax({
            url: '/service/dashboard-ql/ql-chart04.json',
            type: "POST",
            contentType: "application/json",
            dataType: 'json',
            data: JSON.stringify($scope.filter),
            success: function (resp) {
                if (resp.success) {
                    $scope.$apply(function () {
                        //Sắp xếp số phát hiện desc
                        resp.data.sort(function (a, b) {
                            return a.phat_hien < b.phat_hien ? 1 : -1;
                        });

                        $scope.qlItemLoading = false;
                        $scope.qlItem4 = resp.data;
                    });
                }
            }
        });
    };

});



app.controller('ql_detail_model', function ($scope, $uibModalInstance, params, msg) {
    $scope.item = params.item;
    $scope.time = params.time;
    $scope.provinceName = params.provinceName;

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');

    };
});
