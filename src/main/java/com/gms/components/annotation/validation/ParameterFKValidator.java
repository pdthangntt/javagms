package com.gms.components.annotation.validation;

import com.gms.components.annotation.ParameterFK;
import com.gms.components.annotation.interf.FieldParameterExists;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author vvthanh
 */
public class ParameterFKValidator implements ConstraintValidator<ParameterFK, Object> {

    @Autowired
    private ApplicationContext applicationContext;

    private FieldParameterExists service;
    private String fieldName;
    private String type;
    private boolean mutiple;

    @Override
    public void initialize(ParameterFK unique) {
        Class<? extends FieldParameterExists> clazz = unique.service();
        this.fieldName = unique.fieldName();
        this.type = unique.type();
        this.mutiple = unique.mutiple();
        String serviceQualifier = unique.serviceQualifier();

        if (!serviceQualifier.equals("")) {
            this.service = this.applicationContext.getBean(serviceQualifier, clazz);
        } else {
            this.service = this.applicationContext.getBean(clazz);
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return this.service.fieldParameterExists(o, this.type, this.mutiple, this.fieldName);
    }
}
