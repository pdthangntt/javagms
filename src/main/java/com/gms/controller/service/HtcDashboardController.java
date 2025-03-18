package com.gms.controller.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.htc.DashboardObjectGroupPercentForm;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HtcDashboardController extends BaseController {

    @Autowired
    private HtcVisitRepositoryImpl htcVisitRepositoryImpl;
    @Autowired
    private ParameterService parameterService;

    /**
     * @auth vvThành
     *
     * Sửa lại NTGiang Bỏ input fromDate, toDate
     * @param services
     * @return
     */
    @RequestMapping(value = "/htc-dashboard/objectgroup.json", method = RequestMethod.GET)
    public Response<?> actionObjectGroup(
            @RequestParam(name = "service") String services) {
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        return new Response<>(true, htcVisitRepositoryImpl.getDashboardObjectGroup(getCurrentUser().getSite().getID(),
                fromTime, toTime, Arrays.asList(services.split(",", -1))
        ));
    }

    /**
     * @auth vvThành
     * @param service
     * @return
     */
    @RequestMapping(value = "/htc-dashboard/visit.json", method = RequestMethod.GET)
    public Response<?> actionVisit(@RequestParam(name = "service") String service) {
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        List<String> services = Arrays.asList(service.split(",", -1));

        Map<String, List<Long>> data = htcVisitRepositoryImpl.getDashboardVisit(getCurrentUser().getSite().getID(),
                fromTime, toTime, services);

        Map<String, List<Long>> result = new LinkedHashMap<>();
        List<Long> dataDefault = new ArrayList<>();
        dataDefault.add(Long.parseLong("0"));
        dataDefault.add(Long.parseLong("0"));
        dataDefault.add(Long.parseLong("0"));
        dataDefault.add(Long.parseLong("0"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        String key = null;
        while (beginCalendar.before(finishCalendar)) {
            key = TextUtils.formatDate(beginCalendar.getTime(), "M");
            result.put(key, data.getOrDefault(key, dataDefault));
            beginCalendar.add(Calendar.MONTH, 1);
        }
        return new Response<>(true, result);
    }

    /**
     * @auth TrangBN
     * @param services
     * @return
     */
    @RequestMapping(value = "/htc-dashboard/target-test.json", method = RequestMethod.GET)
    public Response<?> actionTargetTest(
            @RequestParam(name = "service") String services) {

        Map<String, List<Long>> data = htcVisitRepositoryImpl.getDashboardTarget(getCurrentUser().getSite().getID(),
                null, Arrays.asList(services.split(",", -1)));

        if (data == null) {
            data = new HashMap<>();
        }

        Map<String, List<Long>> result = new LinkedHashMap<>();
        List<Long> dataDefault = new ArrayList<>();
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(data.get("total") != null ? data.get("total").get(4) : 0L);
        dataDefault.add(Long.parseLong("0"));

        // Get current quarter
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        String currentQuarter = (currentDate.get(Calendar.MONTH) - 1) / 3 + 1 + "";

        result.put("1", data.getOrDefault("1", dataDefault));
        result.put("2", data.getOrDefault("2", dataDefault));
        result.put("3", data.getOrDefault("3", dataDefault));
        if (!currentQuarter.equals("3")) {
            result.put("4", data.getOrDefault("4", dataDefault));
        }
        result.put("5", data.getOrDefault("total", dataDefault));

        return new Response<>(true, result);
    }

    @RequestMapping(value = "/htc-dashboard/target-positive.json", method = RequestMethod.GET)
    public Response<?> actionTargetPositive(
            @RequestParam(name = "service") String services) {

        Map<String, List<Long>> data = htcVisitRepositoryImpl.getDashboardTarget(getCurrentUser().getSite().getID(), 2l, Arrays.asList(services.split(",", -1)));
        if (data == null) {
            data = new HashMap<>();
        }

        Map<String, List<Long>> result = new LinkedHashMap<>();
        List<Long> dataDefault = new ArrayList<>();
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(0L);
        dataDefault.add(data.get("total") != null ? data.get("total").get(4) : 0L);
        dataDefault.add(Long.parseLong("0"));

        // Get current quarter
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        String currentQuarter = (currentDate.get(Calendar.MONTH) - 1) / 3 + 1 + "";

        result.put("1", data.getOrDefault("1", dataDefault));
        result.put("2", data.getOrDefault("2", dataDefault));
        result.put("3", data.getOrDefault("3", dataDefault));
        if (!currentQuarter.equals("3")) {
            result.put("4", data.getOrDefault("4", dataDefault));
        }
        result.put("5", data.getOrDefault("total", dataDefault));

        return new Response<>(true, result);
    }

    /**
     * Lấy danh sách sách dương tính trong tháng, số lượng chuyển gửi, ddieeufd
     * trị thành công
     *
     * @param services
     * @auth TrangBN
     * @return
     */
    @RequestMapping(value = "/htc-dashboard/positive-trans.json", method = RequestMethod.GET)
    public Response<?> actionPositiveTrans(@RequestParam(name = "service") String services) {
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        String fromTime = TextUtils.formatDate(new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime(), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);

        Map<String, List<Long>> percentTestAge = htcVisitRepositoryImpl.getDashboardPositiveTransfer(getCurrentUser().getSite().getID(),
                fromTime, toTime, Arrays.asList(services.split(",", -1)));

        Map<String, List<Long>> result = new LinkedHashMap<>();
        List<Long> dataDefault = new ArrayList<>();
        dataDefault.add(Long.parseLong("0"));
        dataDefault.add(Long.parseLong("0"));

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        String key = null;
        while (beginCalendar.before(finishCalendar)) {
            key = "0" + ((beginCalendar.get(Calendar.MONTH) + 1) / 3 + 1) + TextUtils.formatDate(beginCalendar.getTime(), "/yyyy");
            result.put(String.format("Quý %s", key), percentTestAge.getOrDefault(key, dataDefault));
            beginCalendar.add(Calendar.MONTH, 3);
        }

        return new Response<>(true, result);
    }

    @RequestMapping(value = "/htc-dashboard/positive-object.json", method = RequestMethod.GET)
    public Response<?> actionPositiveObject(
            @RequestParam(name = "service") String services) {
        Date d = new Date();
        String fromTime = TextUtils.formatDate(TextUtils.getFirstDayOfYear(d), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);
        Map<String, Long> percentPositiveObject = htcVisitRepositoryImpl.getDashboardPositiveObjectGroup(getCurrentUser().getSite().getID(),
                fromTime,
                toTime,
                Arrays.asList(services.split(",", -1)));
        return new Response<>(true, percentPositiveObject);
    }

    @RequestMapping(value = "/htc-dashboard/object-group-percent.json", method = RequestMethod.GET)
    public Response<?> actionObjectGroupPercent(
            @RequestParam(name = "service") String services) {
        List<ParameterEntity> objectGroups = parameterService.getTestObjectGroup();
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String fromTime = TextUtils.formatDate(new GregorianCalendar(cal.get(Calendar.YEAR) - 1, (TextUtils.getQuarter(cal.getTime())) * 3, 1).getTime(), FORMATDATE);
        String toTime = TextUtils.formatDate(d, FORMATDATE);

        Map<String, DashboardObjectGroupPercentForm> result = htcVisitRepositoryImpl.getDashboardObjectGroupPercent(getCurrentUser().getSite().getID(),
                fromTime, toTime, Arrays.asList(services.split(",", -1)), objectGroups);

        Set<String> oids = new HashSet<>();
        for (Map.Entry<String, DashboardObjectGroupPercentForm> entry : result.entrySet()) {
            if (entry.getValue().getXetnghiem() == 0 || entry.getValue().getDuongtinh() == 0) {
                continue;
            }
            oids.add(entry.getValue().getoCode());
        }

        //Convert to data google map
        List<String> cols = new ArrayList<>();
        Map<String, Long> defaultData = new LinkedHashMap<>();
        for (ParameterEntity item : objectGroups) {
            if (!item.getParentID().equals(Long.valueOf("0")) || !oids.contains(item.getCode())) {
                continue;
            }
            cols.add(item.getValue());
            defaultData.put(item.getCode(), Long.valueOf("0"));
        }

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(TextUtils.convertDate(fromTime, FORMATDATE));
        Calendar finishCalendar = Calendar.getInstance();
        finishCalendar.setTime(TextUtils.convertDate(toTime, FORMATDATE));

        Map<String, List<Long>> data = new LinkedHashMap();
        Map<String, Long> vData;
        while (beginCalendar.before(finishCalendar)) {
            vData = new LinkedHashMap<>(defaultData);
            String key = ((beginCalendar.get(Calendar.MONTH) + 1) / 3 + 1) + TextUtils.formatDate(beginCalendar.getTime(), "/yyyy");
            String mKey = String.format("Quý 0%s", key);
            for (Map.Entry<String, Long> vEntry : vData.entrySet()) {
                for (Map.Entry<String, DashboardObjectGroupPercentForm> childEntry : result.entrySet()) {
                    if (String.format("%s-%s", vEntry.getKey(), key).equals(childEntry.getKey()) && childEntry.getValue().getXetnghiem() > 0) {
                        vData.put(childEntry.getValue().getoCode(), Long.valueOf((childEntry.getValue().getDuongtinh() * 100) / childEntry.getValue().getXetnghiem()));
                    }
                }
            }
            data.put(mKey, new ArrayList<>(vData.values()));
            beginCalendar.add(Calendar.MONTH, 3);
        }

        HashMap<String, Object> response = new HashMap();
        response.put("cols", cols);
        response.put("data", data);
        return new Response<>(true, response);
    }

}
