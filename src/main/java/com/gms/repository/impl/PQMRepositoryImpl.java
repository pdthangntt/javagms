package com.gms.repository.impl;

import com.gms.entity.form.pac.PQMTable01Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pdThang
 */
@Repository
@Transactional
public class PQMRepositoryImpl extends BaseRepositoryImpl {

    public List<PQMTable01Form> getTable01(String from, String to) {

        List<PQMTable01Form> data = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();

        params.put("from", from);
        params.put("to", to);

        List<Object[]> result = query("pqm_table01.sql", params).getResultList();
        PQMTable01Form row;
        for (Object[] object : result) {
            row = new PQMTable01Form();
            row.setSite(object[0] == null ? null : object[0].toString());

            row.setPositive(Integer.valueOf(object[1].toString()));
            row.setRecent(Integer.valueOf(object[2].toString()));
            row.setArt(Integer.valueOf(object[3].toString()));
            data.add(row);
        }
        return data;
    }

}
