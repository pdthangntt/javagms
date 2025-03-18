package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.repository.impl.HtcLaytestRepositoryImpl;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LaytestDashboardController extends BaseController {

    @Autowired
    private HtcLaytestRepositoryImpl laytestRepositoryImpl;
    @Autowired
    HtcVisitRepositoryImpl htcVisitRepositoryImpl;
    /**
     * KC1.7a_TQ_XN theo nhóm
     * @auth TrangBN
     * @return
     */
    @RequestMapping(value = "/laytest-dashboard/chart01.json", method = RequestMethod.GET)
    public Response<?> actionChart01() {

        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        Map<String, Map<String, Object>> items = laytestRepositoryImpl.getDashboardChart01(getCurrentUser().getSite().getID(), ConfirmTestResultEnum.DUONG_TINH,getCurrentUser().getUser().getID(), fromTime, toTime);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> itemDefault = new HashMap<>();
        itemDefault.put("danhmuc", "");
        itemDefault.put("so_xn", Long.valueOf("0"));
        String key = null;
        for (Map.Entry<String, Map<String, Object>> e : items.entrySet()){
            key = e.getKey();
            result.add(items.getOrDefault(key, itemDefault));
        }
        return new Response<>(true, result);
    }

    /**
     * KC1.7b_TQ_XN (+) theo nhóm
     *
     * @return
     */
    @RequestMapping(value = "/laytest-dashboard/chart02.json", method = RequestMethod.GET)
    public Response<?> actionChart02() {
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        Map<String, Map<String, Object>> items = laytestRepositoryImpl.getDashboardChart02(getCurrentUser().getSite().getID(), getCurrentUser().getUser().getID(), fromTime, toTime);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> itemDefault = new HashMap<>();
        itemDefault.put("danhmuc", "");
        itemDefault.put("so_xn", Long.valueOf("0"));
        String key = null;
        for (Map.Entry<String, Map<String, Object>> e : items.entrySet()){
            key = e.getKey();
            itemDefault.put("danhmuc", key);
            result.add(items.getOrDefault(key, itemDefault));
        }
        return new Response<>(true, result);
    }

    /**
     * KC1.7c_TQ_XN &(+) theo tháng
     *
     * @auth vvThành
     * @return
     */
    @RequestMapping(value = "/laytest-dashboard/chart03.json", method = RequestMethod.GET)
    public Response<?> actionChart03() {
        LoggedUser currentUser = getCurrentUser();
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        Map<String, Map<String, Object>> items = laytestRepositoryImpl.getDashboardChart03(currentUser.getSite().getID(), currentUser.getUser().getID(), fromTime, toTime);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> itemDefault = new HashMap<>();
        itemDefault.put("rule", "0");
        itemDefault.put("so_duongtinh", Long.valueOf("0"));
        itemDefault.put("so_xn", Long.valueOf("0"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));
        String key = null;
        while (beginCalendar.before(finishCalendar)) {
            key = TextUtils.formatDate(beginCalendar.getTime(), "M");
            itemDefault = new HashMap<>(itemDefault);
            itemDefault.put("rule", key);
            result.add(items.getOrDefault(key, itemDefault));
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return new Response<>(true, "KC1.7c_TQ_XN &(+) theo tháng", result);
    }

    /**
     * KC1.7d_TQ_Chuyển gửi
     *
     * @return
     */
    @RequestMapping(value = "/laytest-dashboard/chart04.json", method = RequestMethod.GET)
    public Response<?> actionChart04() {
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);

        Map<String, Map<String, Object>> percentTestAge = laytestRepositoryImpl.getDashboardChart04(getCurrentUser().getSite().getID(),
                getCurrentUser().getUser().getID(),
                fromTime, toTime);

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> itemDefault = new HashMap<>();
        itemDefault.put("month", "0");
        itemDefault.put("col1",Long.parseLong("0"));
        itemDefault.put("col2",Long.parseLong("0"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        String key = null;
        while (beginCalendar.before(finishCalendar)) {
            key = "0" + ((beginCalendar.get(Calendar.MONTH) + 1) / 3 + 1) + TextUtils.formatDate(beginCalendar.getTime(), "/yyyy");
            itemDefault = new HashMap<>(itemDefault);
            itemDefault.put("month", String.format("Quý %s", key));
            result.add(percentTestAge.getOrDefault(String.format("Quý %s", key), itemDefault));
            beginCalendar.add(Calendar.MONTH, 3);
        }
        return new Response<>(true, result);
    }
}
