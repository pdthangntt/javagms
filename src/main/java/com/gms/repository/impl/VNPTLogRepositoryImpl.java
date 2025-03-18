package com.gms.repository.impl;

import com.gms.components.TextUtils;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class VNPTLogRepositoryImpl extends BaseRepositoryImpl {

    public HashMap<String, String> getDasboard(String ID, String status, String from, String to) {
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("ID", ID);
        params.put("status", status);
        params.put("from", StringUtils.isEmpty(from) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, from));
        params.put("to", StringUtils.isEmpty(to) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, to));
        List<Object[]> result = query("opc/api_log.sql", params).getResultList();

        Object[] object = result.get(0);
        if (object[0] == null || object[0].toString().equals("")) {
            data.put("success", "0");
            data.put("error", "0");
            return data;
        }
        data.put("success", object[0].toString());
        data.put("error", object[1].toString());

        return data;
    }

}
