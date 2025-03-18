var utils = {};
utils.buildFormData = function (model, source) {
    var data = {};
    for (var k in source) {
        data[model + '[' + k + ']'] = source[k];
    }
    return data;
};

utils.getKeyword = function (str) {
    str = str.trim();
    str = utils.removeDiacritical(str);
    str = utils.removeSpecialChar(str);
    str = utils.stripMultiSpace(str);
    return str.toLowerCase();
};

utils.createAlias = function (str) {
    str = str.trim();
    str = utils.removeDiacritical(str);
    str = utils.removeSpecialChar(str);
    str = utils.stripMultiSpace(str);
    str = str.replace(/\s/g, '-');
    return str.toLowerCase();
};

utils.removeDiacritical = function (str) {
    str = str.replace(/(à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ)/g, 'a');
    str = str.replace(/(è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ)/g, 'e');
    str = str.replace(/(ì|í|ị|ỉ|ĩ)/g, 'i');
    str = str.replace(/(ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ)/g, 'o');
    str = str.replace(/(ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ)/g, 'u');
    str = str.replace(/(ỳ|ý|ỵ|ỷ|ỹ)/g, 'y');
    str = str.replace(/(đ)/g, 'd');
    str = str.replace(/(À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ)/g, 'A');
    str = str.replace(/(È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ)/g, 'E');
    str = str.replace(/(Ì|Í|Ị|Ỉ|Ĩ)/g, 'I');
    str = str.replace(/(Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ)/g, 'O');
    str = str.replace(/(Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ)/g, 'U');
    str = str.replace(/(Ỳ|Ý|Ỵ|Ỷ|Ỹ)/g, 'Y');
    str = str.replace(/(Đ)/g, 'D');
    return str;
};

utils.removeSpecialChar = function (str) {
    return str.replace(/[^A-Za-z0-9\-\s]/g, '');
};

utils.stripMultiSpace = function (str) {
    return str.replace(/\s+/g, ' ');
};


Number.prototype.toMoney = function (decimals, decimal_sep, thousands_sep) {
    var n = this,
            c = isNaN(decimals) ? 2 : Math.abs(decimals), //if decimal is zero we must take it, it means user does not want to show any decimal
            d = decimal_sep || ',', //if no decimal separator is passed we use the dot as default decimal separator (we MUST use a decimal separator)
            t = (typeof thousands_sep === 'undefined') ? '.' : thousands_sep, //if you don't want to use a thousands separator you can pass empty string as thousands_sep value

            sign = (n < 0) ? '-' : '',
            //extracting the absolute value of the integer part of the number and converting to string
            i = parseInt(n = Math.abs(n).toFixed(c)) + '',
            j = ((j = i.length) > 3) ? j % 3 : 0;

    var sp = n.split(".");
    if (sp.length == 1 || eval(sp[1]) == 0) {
        c = 0;
    }
    return sign + (j ? i.substr(0, j) + t : '') + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : '');
};

utils.getContentOfDefault = function (content, defaulValue) {
    return typeof (content) == 'undefined' || content == null || content == '' ? defaulValue : content;
};

utils.timestampToStringDate = function (unixtime) {
    if (unixtime == null || unixtime == '' || unixtime == 0) {
        return '';
    }
    let d = new Date(unixtime);
    return [d.getDate(), d.getMonth() + 1, d.getFullYear()]
            .map(n => n < 10 ? `0${n}` : `${n}`).join('/');
};

utils.toDate = function (unixtime) {
    if (unixtime == null || unixtime == '' || unixtime == 0) {
        return null;
    }
    let dateParts = unixtime.split("/");
    return new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]);
};

utils.getAge = function (dateString) {
    let today = new Date();
    let birthDate = utils.toDate(dateString);
    let age = today.getFullYear() - birthDate.getFullYear();
    let m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
};

/**
 * dd/mm/yyyy
 * @param {type} dateFromString
 * @param {type} dateToString
 * @returns {Number}
 */
utils.monthDiff = function (dateFromString, dateToString) {
    let dateFrom = utils.toDate(dateFromString);
    let dateTo = utils.toDate(dateToString);
    return dateTo.getMonth() - dateFrom.getMonth() +
            (12 * (dateTo.getFullYear() - dateFrom.getFullYear()))
};

utils.getMonthOfAge = function (dateFromString) {
    let dateFrom = utils.toDate(dateFromString);
    let dateTo = new Date();
    return dateTo.getMonth() - dateFrom.getMonth() +
            (12 * (dateTo.getFullYear() - dateFrom.getFullYear()))
};

/**
 * So sánh không phân biệt hoa thường, dấu cách dầu cuối và ở giữa
 * 
 * @param {type} model
 * @param {type} target
 * @returns {undefined}
 */
utils.compareString = function (model, target) {
    model = model.trim();
    model = utils.stripMultiSpace(model);

    target = target.trim();
    target = utils.stripMultiSpace(target);

    return model.toLowerCase() === target.toLowerCase();
};