package com.gms.scheduled;

import com.gms.service.ParameterService;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * second, minute, hour, day of month, month, day(s) of week
 *
 * @author vvthanh
 */
abstract class BaseScheduled {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    @Autowired
    private ParameterService parameterService;

    public Logger getLogger() {
        return logger;
    }

    public SimpleDateFormat getDate() {
        return dateFormat;
    }
}
