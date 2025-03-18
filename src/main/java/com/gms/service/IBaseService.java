package com.gms.service;

import java.util.List;

/**
 *
 * @author vvthanh
 */
public interface IBaseService<T, PK> { 

    public List<T> findAll(); 

    public T findOne(PK ID);
    
    public T save(T condition);
    
}
