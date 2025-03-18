package com.gms.components;

import com.google.gson.Gson;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.management.IntrospectionException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author vvthanh
 */
public class TextUtils {
    
    public static String getURLBase(HttpServletRequest request) {
        try {
            URL requestURL = new URL(request.getRequestURL().toString());
            String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
            return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
        } catch (MalformedURLException ex) {
            return "";
        }
        
    }
    
    public static Object getPropertyValue(Object bean, String property)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException, java.beans.IntrospectionException {
        Class<?> beanClass = bean.getClass();
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, property);
        if (propertyDescriptor == null) {
            throw new IllegalArgumentException("No such property " + property
                    + " for " + beanClass + " exists");
        }
        
        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod == null) {
            throw new IllegalStateException("No getter available for property "
                    + property + " on " + beanClass);
        }
        return readMethod.invoke(bean);
    }
    
    private static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass,
            String propertyname) throws IntrospectionException, java.beans.IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        PropertyDescriptor propertyDescriptor = null;
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor currentPropertyDescriptor = propertyDescriptors[i];
            if (currentPropertyDescriptor.getName().equals(propertyname)) {
                propertyDescriptor = currentPropertyDescriptor;
            }
            
        }
        return propertyDescriptor;
    }
    
    public static String removemarks(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        
        str = str.trim().toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = pattern.matcher(str).replaceAll("");
        return str.replaceAll(" ", "-");
    }

    /**
     * Get Client IP Address
     *
     * @auth vvThành
     * @param request
     * @return
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
    
    public static String readFile(String pathname) {
        try {
            String content = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(getFile(pathname)));
            content = reader.lines().collect(Collectors.joining("\n"));
            reader.close();
            return content;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get file by path in Spring boot
     *
     * @param pathname
     * @return
     */
    public static InputStream getFile(String pathname) {
        try {
            return new ClassPathResource(String.format("classpath:%s", pathname)).getInputStream();
        } catch (IOException e) {
            try {
                return new ClassPathResource(pathname).getInputStream();
            } catch (IOException ex) {
                return null;
            }
        }
    }

    /**
     * Ngày đầu tiên của quý
     *
     * @param quarter
     * @param year
     * @return
     */
    public static Date getFirstDayOfQuarter(int quarter, int year) {
        int month = 0;
        switch (quarter) {
            case 0:
                month = 0;
                break;
            case 1:
                month = 3;
                break;
            case 2:
                month = 6;
                break;
            case 3:
                month = 9;
                break;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /**
     * Ngày cuối cùng của quý
     *
     * @param quarter
     * @param year
     * @return
     */
    public static Date getLastDayOfQuarter(int quarter, int year) {
        int month = 2;
        switch (quarter) {
            case 0:
                month = 2;
                break;
            case 1:
                month = 5;
                break;
            case 2:
                month = 8;
                break;
            case 3:
                month = 11;
                break;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * Ngày đầu tiên của quý theo date
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(getQuarter(date), calendar.get(Calendar.YEAR));
    }

    /**
     * Ngày cuối cùng của quý theo date
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(getQuarter(date), calendar.get(Calendar.YEAR));
    }

    /**
     * Lấy quý theo date
     *
     * @param date
     * @return
     */
    public static int getQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int quarter = 1;
        if (month < 3) {
            quarter = 1;
        } else if (month < 6) {
            quarter = 2;
        } else if (month < 9) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter - 1;
    }

    /**
     * Chuyển đổi date sang định dạng yêu cầu
     *
     * @param date
     * @param format hh:mm:ii dd/MM/yy
     * @return
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Convert Date
     *
     * @param date
     * @param format
     * @return
     */
    public static Date convertDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Chuyển đổi định dạng date/datetime
     *
     * @param fromFormat hh:mm:ii dd/MM/yy
     * @param toFormat hh:mm:ii dd/MM/yy
     * @param date
     * @return
     */
    public static String formatDate(String fromFormat, String toFormat, String date) {
        try {
            Date dateConvert = new SimpleDateFormat(fromFormat).parse(date);
            return formatDate(dateConvert, toFormat);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Ngày cuối cùng của tháng
     *
     * @param month
     * @param year
     * @return
     */
    public static Date getLastDayOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth(year, month));
        return cal.getTime();
    }
    
    public static int lastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }
    
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * Ngày đầu tiên của tháng
     *
     * @param month
     * @param year
     * @return
     */
    public static Date getFirstDayOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * Ngày đầu tiên của năm
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /**
     * Ngày cuối cùng của năm
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    /**
     * Format số kiểu việt nam
     *
     * @param number
     * @return
     */
    public static String numberFormat(Object number) {
        try {
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat vn = NumberFormat.getInstance(localeVN);
            vn.setMinimumIntegerDigits(2);
            String format = vn.format(number);
            return format.equals("00") ? "0" : format;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Convert key map to json
     *
     * @param map
     * @return
     */
    public static String ketMapToJson(Map map) {
        Gson gson = new Gson();
        return gson.toJson(map.keySet());
    }

    /**
     * convert Option To String
     *
     * @auth vvThanh
     * @param options
     * @param value
     * @return
     */
    public static String convertOptionToString(Map<String, String> options, List<String> value, String c) {
        String context = "";
        for (String v : value) {
            if (!context.equals("")) {
                context += c + " ";
            }
            context += options.getOrDefault(v, "");
        }
        return context;
    }
    
    public static String removeDiacritical(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");
        
        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        return str;
    }
    
    public static Date getDay(Date date, Integer day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + day);
        return cal.getTime();
    }

    /**
     * Loại bỏ ký tự đặc biệt trong uimask
     *
     * @param str
     * @return
     */
    public static String removeSpecialUiMaskChar(String str) {
        str = removeDiacritical(str);
        str = str.replaceAll("a|A|z|Z|w|W|b|B|d|D|s|S", "T");
//        str = str.replaceAll("0|1|2|3|4|5|6|7|8", "9");
        return str;
    }

    /**
     * Convert sang % 2 số
     *
     * @param value
     * @param sum
     * @return
     */
    public static String toPercent(int value, int sum) {
//        Locale localeVN = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(2);
        double percent = 0;
        if (sum > 0) {
            percent = value / Double.valueOf(sum);
        }
        return format.format(percent);
    }

    /**
     * Chuẩn hóa tên
     *
     * Cắt space 2 đầu và chuẩn hóa 1 dấu space giữa tên Upercase chứ cái đầu
     * tiên của tên
     *
     * @param name
     * @return
     */
    public static String toFullname(String name) {
        try {
            if (StringUtils.isBlank(name)) {
                return "";
            }
            name = name.toLowerCase().replaceAll("\\s+", " ").trim();
            String[] split = name.split(" ");
            StringBuilder strB = new StringBuilder();
            String firstChar;
            for (String string : split) {
                firstChar = String.valueOf(string.trim().charAt(0));
                strB.append(string.replaceAll("\\s+", " ").trim().replaceFirst(firstChar, firstChar.toUpperCase()));
                strB.append(" ");
            }
            return strB.toString().trim();
        } catch (Exception e) {
            return name;
        }
    }

    /**
     * Kiểm tra tính đúng đắn của ngày tháng
     *
     * @param dateToValidate
     * @return
     */
    public static boolean validDate(String dateToValidate) {
        if (StringUtils.isEmpty(dateToValidate)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Convert sang ngày không có thời gian
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateWithoutTime(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }
    
    public static void main(String[] args) {
        Date lastDayOfMonth = TextUtils.getLastDayOfMonth(4, 2021);
        System.out.println("--> " + TextUtils.toFullname("ONG THẾ HÒA"));
    }
}
