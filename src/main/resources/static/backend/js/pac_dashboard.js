app.controller('pac_dashboard', function ($scope, msg, amchart, locations) {
    $scope.vi_districts = [];
    $scope.options = {};

    $scope.init = function () {
        $scope.switchConfig("input.checkbox-switch");
        $.getJSON("/static/backend/plugin/leaflet/gis/" + site_province.id + '.json').done(function (data) {
            $scope.vi_districts = data.level2s;
            $scope.report();
        });
    };

    $scope.switchConfig = function (elm) {
        elm = $(typeof (elm) == 'undefined' ? 'input.checkbox-switch' : elm);
        $(elm).iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        $(elm).on('ifChanged', function () {
            let checked = $(this).is(':checked');
            let value = $(this).val();
            if (checked) {
                $scope.report(value);
            }
        });
    };

    $scope.report = function (request) {
        request = request ? request : '1';
        $.ajax({
            url: '/service/pac-dashboard/chartgeo.json',
            type: "GET",
            success: function (resp) {
                if (resp.success) {
                    $scope.$apply(function () {
                        $scope.newTotal = resp.data.newTotal;
                        $scope.patientTotal = resp.data.patientTotal;
                        $scope.allTotal = resp.data.allTotal;
                        $scope.opcTotal = resp.data.opcTotal;
                    });
                    var container = L.DomUtil.get('dashboard-chartgeo');
                    if (container != null) {
                        container._leaflet_id = null;
                    }
                    var map = L.map('dashboard-chartgeo');
                    map.createPane('labels');
                    map.getPane('labels').style.zIndex = 550;
                    map.getPane('labels').style.pointerEvents = 'none';

                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
                    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_only_labels/{z}/{x}/{y}.png', {
                        pane: 'labels'
                    }).addTo(map);

                    var geojson = L.geoJson($scope.vi_districts, {style: {
                            fillColor: '#ddd',
                            fillOpacity: 0.7,
                            weight: 1,
                            opacity: 1,
                            color: '#000',
                            dashArray: '2'
                        },
                        filter: function (feature, layer) {
                            if (resp.data.districtName != null) {
                                return utils.removeDiacritical(resp.data.districtName).indexOf(utils.removeDiacritical(feature.name)) != -1;
                            }
                            return true;
                        }
                    }).addTo(map);

//                    var legend = L.control({position: 'topright'});
//                    legend.onAdd = function (map) {
//                        var div = L.DomUtil.create('div', 'leaflet-legend');
//                        if (request === '1') {
//                            div.innerHTML += '<i class="markers-group-green"></i><span>' + 'Tích luỹ</span><br>';
//                        } else if (request === '2') {
//                            div.innerHTML += '<i class="markers-group-green"></i><span>' + 'Còn sống</span><br>';
//                        } else if (request === '3') {
//                            div.innerHTML += '<i class="markers-group-red"></i><span>' + 'Tử vong</span><br>';
//                        }
//                        return div;
//                    };
//
//                    legend.addTo(map);

                    var markers = L.markerClusterGroup({
                        showCoverageOnHover: false,
                        pane: 'labels',
                        iconCreateFunction: function (cluster) {
                            return L.divIcon({
                                className: request === '3' ? 'markers-group-red' : 'markers-group-green',
                                iconSize: [30, 30],
                                html: cluster.getChildCount()
                            });
                        }
                    });

                    var geoBounds = geojson.getBounds();
                    angular.forEach(resp.data.chartgeo, (item, id) => {
                        if (geoBounds.contains(new L.LatLng(item.permanent_lat, item.permanent_lng))) {
                            var marker = L.marker(new L.LatLng(item.permanent_lat, item.permanent_lng), {icon: L.divIcon({
                                    className: request === '3' ? 'markers-red' : 'markers-green',
                                    iconSize: [10, 10],
                                })});
                            if (request === '1') {
                                markers.addLayer(marker);
                            } else if (request === '2' && item.death_time !== null) {
                                markers.addLayer(marker);
                            } else if (request === '3' && item.death_time === null) {
                                markers.addLayer(marker);
                            }
                        }
                    });
                    map.addLayer(markers);
                    map.fitBounds(geoBounds);
                }
            }
        });
    };
});
