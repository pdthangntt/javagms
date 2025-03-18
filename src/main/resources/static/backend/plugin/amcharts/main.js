function am4themes_localsi(target) {
    if (target instanceof am4core.ColorSet) {
        target.list = [
            am4core.color("#1BA68D"),
            am4core.color("#DF3520"),
            am4core.color("#64297B"),
            am4core.color("#E7DA4F"),
            am4core.color("#E77624")
        ];
    }
}


app.service('amchart', function (localStorageService) {
    var elm = this;
    elm.init = function () {
        var theme = 'am4themes_animated';
        if (localStorageService.isSupported) {
            if (typeof (amchartTheme) == 'undefined') {
                amchartTheme = true;
            }
            if (amchartTheme && $("#amchart-color").length == 0) {
                $("body").append('<div class="hidden-xs box-footer text-center navbar-fixed-bottom main-footer" id="amchart-color"><ul class="theme-switcher"></ul></div>');
            }
            if (amchartTheme && $("#amchart-color .theme-switcher li").length == 0) {
                $("#amchart-color .theme-switcher").html('<li class="theme-button theme-button-localsi"><a class="trans-all" data-theme="localsi" style="background-image: linear-gradient(45deg, #1BA68D 0%,#1BA68D 50%,red 50%,red 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-none"><a class="trans-all" data-theme="animated" style="background-image: linear-gradient(45deg, #67b7dc 0%,#67b7dc 50%,#c767dc 50%,#c767dc 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-dataviz"><a class="trans-all" data-theme="dataviz" style="background-image: linear-gradient(45deg, #283250 0%,#283250 50%,#902c2d 50%,#902c2d 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-material"><a class="trans-all" data-theme="material" style="background-image: linear-gradient(45deg, #E91E63 0%,#E91E63 50%,#9C27B0 50%,#9C27B0 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-kelly" active=""><a class="trans-all" data-theme="kelly" style="background-image: linear-gradient(45deg, #F3C300 0%,#F3C300 50%,#875692 50%,#875692 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-frozen"><a class="trans-all" data-theme="frozen" style="background-image: linear-gradient(45deg, #bec4f8 0%,#bec4f8 50%,#a5abee 50%,#a5abee 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-moonrisekingdom"><a class="trans-all" data-theme="moonrisekingdom" style="background-image: linear-gradient(45deg, #3a1302 0%,#3a1302 50%,#c79f59 50%,#c79f59 100%); border-color: #fff;"></a></li>' +
                        '<li class="theme-button theme-button-spiritedaway"><a class="trans-all" data-theme="spiritedaway" style="background-image: linear-gradient(45deg, #65738e 0%,#65738e 50%,#523b58 50%,#523b58 100%); border-color: #fff;"></a></li>');
                $(".theme-switcher li a[data-theme=" + (localStorageService.get('am4themes') || 'localsi') + "]").parent("li").addClass("active");

                Array.prototype.forEach.call(
                        document.querySelectorAll(".theme-switcher li"),
                        function (li) {
                            li.addEventListener("click", function () {
                                if (localStorageService.isSupported) {
                                    localStorageService.set('am4themes', $(this).find("a").attr("data-theme"));
                                    window.location.reload();
                                    return false;
                                }
                            });
                        });
            }
            if (localStorageService.get('am4themes') != null) {
                theme = 'am4themes_' + localStorageService.get('am4themes');
            }
        }
        am4core.useTheme(window[theme]);
        am4core.options.queue = false;
        am4core.options.onlyShowOnViewport = false;
    };

    elm.exportMenu = function (fn, data, options) {
        return [
            {
                label: "...",
                menu: [
                    {
                        label: "Mở",
                        type: "custom",
                        options: {
                            callback: function () {
                                var height = $(window).height() - 100;
                                bootbox.dialog({
                                    message: '<div id="chart-view" class="text-center" style="height:' + (height - 100) + 'px"><i class="fa fa-spin fa-spinner"></i> Loading...</div>',
                                    size: 'large',
                                    closeButton: false,
                                    buttons: {
                                        cancel: {
                                            label: "Đóng",
                                            className: 'btn-default'
                                        }
                                    }
                                });
                                setTimeout(function () {
                                    fn("chart-view", data, options);
                                }, 500);
                            }
                        }
                    },
                    {"type": "png", "label": "Png"},
                    {"type": "xlsx", "label": "Excel"},
                    {"type": "pdf", "label": "Pdf"},
                    {"type": "print", "label": "In"}
                ]
            }
        ];
    };

    //Use bar char
    elm.createColumnSeries = function (chart, category, field, name, stacked, valueAxis, options) {
        var series = chart.series.push(new am4charts.ColumnSeries());
        series.dataFields.valueY = field;
        series.dataFields.categoryX = category;
        series.name = name;
        series.columns.template.tooltipText = "{name}: [bold]{valueY}[/]";
        series.stacked = stacked;
        series.columns.template.width = am4core.percent(80);
        if (name == null) {
            series.columns.template.tooltipText = "[bold]{valueY}[/]";
            chart.legend.markers.template.disabled = true;
        }

        var valueLabel = series.bullets.push(new am4charts.LabelBullet());
        valueLabel.label.text = "{valueY}";
        valueLabel.label.truncate = false;
        valueLabel.label.hideOversized = false;
        valueLabel.label.fontSize = 12;
        valueLabel.label.align = "center";
        valueLabel.label.dy = -10;

        if (stacked == true) {
            valueLabel.locationY = 0.5;
            valueLabel.label.dy = 0;
            valueLabel.label.hideOversized = true;
        }

        if (options) {
            options(series, valueLabel);
        }
    };


    elm.createLineSeries = function (chart, category, field, name, stacked, valueAxis, valueLabelText, options) {
        var series = chart.series.push(new am4charts.LineSeries());
        if (typeof valueAxis != 'undefined' && valueAxis != null) {
            series.yAxis = valueAxis;
        }
        series.dataFields.valueY = field;
        series.dataFields.categoryX = category;
        series.name = name;
        series.tooltipText = "{name}: [bold]{valueY}[/]";
        series.stacked = stacked;
        series.strokeWidth = 2;

        if (options) {
            options(series);
        }

//        var circleBullet = series.bullets.push(new am4charts.CircleBullet());
//        circleBullet.circle.fill = am4core.color("#fff");
//        circleBullet.circle.strokeWidth = 2;

//        var valueLabel = series.bullets.push(new am4charts.LabelBullet());
//        valueLabel.label.text = typeof (valueLabelText) != 'undefined' ? valueLabelText : "{valueY}";
//        valueLabel.label.truncate = false;
//        valueLabel.label.hideOversized = false;
//        valueLabel.label.fontSize = 12;
//        valueLabel.label.align = "center";
//        valueLabel.label.dy = -10;
    };

    elm.pie = function (_id, data, options) {
        am4core.ready(function () {
            elm.init();
            var chart = am4core.create(_id, am4charts.PieChart);
            chart.hiddenState.properties.opacity = 0;
            chart.data = data;
            chart.language.locale["_decimalSeparator"] = ",";
            chart.language.locale["_thousandSeparator"] = ".";
            chart.innerRadius = am4core.percent(30);

            var series = chart.series.push(new am4charts.PieSeries());
            series.alignLabels = false;

            series.dataFields.category = "key";
            series.dataFields.value = "count";

            series.slices.template.stroke = am4core.color("#fff");
            series.slices.template.strokeWidth = 1;
            series.slices.template.strokeOpacity = 1;

            series.ticks.template.disabled = true;

            series.labels.template.radius = am4core.percent(-40);
            series.labels.template.fontSize = "10px";
            series.labels.template.text = "{value.percent.formatNumber('#.0')}%";
            series.labels.template.adapter.add("radius", function (radius, target) {
                if (target.dataItem && (target.dataItem.values.value.percent < 10)) {
                    return 0;
                }
                return radius;
            });

            series.labels.template.adapter.add("fill", function (color, target) {
                if (target.dataItem && (target.dataItem.values.value.percent < 10)) {
//                    return am4core.color("#fff");
                    return am4core.color("#000");
                }
//                return am4core.color("#fff");
                return am4core.color("#000");
            });

            chart.legend = new am4charts.Legend();
            chart.legend.position = "right";
            chart.legend.fontSize = "10px";
            chart.legend.width = am4core.percent(80);
            chart.legend.labels.template.truncate = false;
            chart.legend.labels.template.wrap = true;

            var markerTemplate = chart.legend.markers.template;
            markerTemplate.width = 10;
            markerTemplate.height = 10;

            var title = chart.titles.create();
            title.fontWeight = 600;
            title.wrap = true;

            chart.exporting.menu = new am4core.ExportMenu();
            chart.exporting.menu.items = elm.exportMenu(elm.pie, data, options);

            if (options) {
                options(chart, title, series);
            }
        });
    };

    elm.bar = function (_id, data, options) {
        am4core.ready(function () {
            elm.init();

            var chart = am4core.create(_id, am4charts.XYChart);
            chart.data = data;
            chart.hiddenState.properties.opacity = 0;
            chart.language.locale["_decimalSeparator"] = ",";
            chart.language.locale["_thousandSeparator"] = ".";

            chart.legend = new am4charts.Legend();
            chart.legend.position = "bottom";
            chart.legend.fontSize = "12px";
            chart.legend.labels.template.truncate = false;
            chart.legend.valueLabels.template.text = "";
            chart.legend.labels.template.wrap = true;

            var markerTemplate = chart.legend.markers.template;
            markerTemplate.width = 20;
            markerTemplate.height = 20;

            var title = chart.titles.create();
            title.fontWeight = 600;
            title.marginBottom = 10;
            title.wrap = true;

            chart.exporting.menu = new am4core.ExportMenu();
            chart.exporting.menu.items = elm.exportMenu(elm.bar, data, options);

            var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
            categoryAxis.fontSize = 10;
            categoryAxis.title.text = "";
            categoryAxis.renderer.grid.template.disabled = true;
            categoryAxis.renderer.grid.template.location = 0;
            categoryAxis.renderer.minGridDistance = 20;
            categoryAxis.renderer.cellStartLocation = 0.1;
            categoryAxis.renderer.cellEndLocation = 0.9;

            var label = categoryAxis.renderer.labels.template;
            label.wrap = true;
            label.maxWidth = 120;

            var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
            valueAxis.min = 0;
            valueAxis.extraMax = 0.1;
            valueAxis.renderer.labels.template.fontSize = 10;
//            valueAxis.renderer.grid.template.disabled = true;

            if (options) {
                options(chart, title, categoryAxis, valueAxis);
            }

//            chart.cursor = new am4charts.XYCursor();
//            chart.cursor.lineY.strokeWidth = 0;
        });
    };

    elm.map = function (_id, data, options) {
        am4core.ready(function () {
            elm.init();
            var chart = am4core.create(_id, am4maps.MapChart);
            chart.geodataSource.url = "/static/backend/plugin/amcharts/districts.json";
            chart.hiddenState.properties.opacity = 0;
//            chart.data = data;

            var title = chart.titles.create();
            title.fontWeight = 600;
            title.marginBottom = 30;
            title.wrap = true;
            title.textAlign = "middle";

            chart.exporting.menu = new am4core.ExportMenu();
            chart.exporting.menu.items = elm.exportMenu(elm.map, data, options);

            var polygonSeries = chart.series.push(new am4maps.MapPolygonSeries());
            polygonSeries.useGeodata = true;
            polygonSeries.heatRules.push({
                property: "fill",
                target: polygonSeries.mapPolygons.template,
                min: chart.colors.getIndex(1).brighten(1),
                max: chart.colors.getIndex(1).brighten(-0.3)
            });

            var heatLegend = chart.createChild(am4maps.HeatLegend);
            heatLegend.series = polygonSeries;
            heatLegend.align = "right";
            heatLegend.width = am4core.percent(20);
            heatLegend.marginRight = am4core.percent(4);
//            heatLegend.minValue = 0;
//            heatLegend.maxValue = 40000000;
            heatLegend.valign = "bottom";


            if (options) {
                options(chart, title, polygonSeries, heatLegend);
            }

        });
    };


    elm.forceDirectedTree = function (_id, data, options) {
        am4core.ready(function () {
            elm.init();
            var chart = am4core.create(_id, am4plugins_forceDirected.ForceDirectedTree);
            var networkSeries = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries());

            chart.data = data;
            networkSeries.dataFields.value = "value";
            networkSeries.dataFields.name = "name";
            networkSeries.dataFields.children = "children";
            networkSeries.nodes.template.tooltipText = "{name}:{value}";
            networkSeries.nodes.template.fillOpacity = 1;
            networkSeries.manyBodyStrength = -20;
            networkSeries.links.template.strength = 0.8;
            networkSeries.minRadius = am4core.percent(2);
            networkSeries.nodes.template.label.text = "{name}";
            networkSeries.fontSize = 10;

            if (options) {
                options(chart, networkSeries);
            }

        });
    };

});
