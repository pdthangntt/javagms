package com.gms.controller.importation;

import com.gms.entity.form.ImportForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vvthanh
 */
interface IBaseController<T> {

    public T mapping(Map<String, String> cols, List<String> cells);

    public HashMap<String, HashMap<String, String>> getOptions();

    public ImportForm initForm();

}
