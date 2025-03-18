app.controller('confirm_book', function ($scope) {

    $scope.start = utils.getContentOfDefault(form.start, '');
    $scope.end = utils.getContentOfDefault(form.end, '');
    
    $scope.urlExcel = urlExcel;
    $scope.urlEmail = urlEmail;
    $scope.urlPdf = urlPdf;

    $scope.init = function () {
        $scope.fullname = $.getQueryParameters().fullname;
        $scope.code = $.getQueryParameters().code;
        $scope.sourceID = $.getQueryParameters().sourceID;
        $scope.sourceSiteID = $.getQueryParameters().sourceSiteID;
        $scope.resultID = $.getQueryParameters().resultID;
        $scope.$parent.select_search("#sourceSiteID", "Tất cả");
    };

    $scope.getUrl = function (url) {
        return url + "?start=" + $("#start").val()
                + "&end=" + $("#end").val()
                + "&fullname=" + $("#fullname").val()
                + "&code=" + $("#code").val()
                + "&sourceID=" + $("#sourceID").val()
                + "&sourceSiteID=" + $("#sourceSiteID").val()
                + "&resultID=" + $("#resultID option:selected").val();
    };

    $scope.bookPdf = function () {
        $scope.dialogReport($scope.getUrl($scope.urlPdf), {
            label: '<i class="fa fa-print"></i> Xuất excel',
            className: 'btn-primary',
            callback: function () {
                $scope.bookExcel();
            }
        });
    };

    $scope.bookExcel = function () {
        window.location.href = $scope.getUrl($scope.urlExcel);
    };

    $scope.bookEmail = function () {
        window.location.href = $scope.getUrl($scope.urlEmail);
    };
});