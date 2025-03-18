package com.gms.entity.excel;

/**
 *
 * @author vvthanh
 */
public interface IExportExcel {

    byte[] run() throws Exception;

    String getFileName();
}
