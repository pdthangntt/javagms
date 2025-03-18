package com.gms.entity.db;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public interface IBaseEntity {

    public void setIgnoreSet();

    public void setAttributeLabels();

    public boolean set(String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException;

    public boolean set(Object object) throws IllegalAccessException, NoSuchFieldException;

    public boolean set(Object object, List<String> ignoreAttributes) throws IllegalAccessException, NoSuchFieldException;

    public Object get(String fieldName) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException;

}
