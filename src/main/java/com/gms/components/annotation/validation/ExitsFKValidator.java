package com.gms.components.annotation.validation;

import com.gms.components.annotation.ExistFK;
import com.gms.components.annotation.interf.FieldValueExists;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author vvthanh
 */
public class ExitsFKValidator implements ConstraintValidator<ExistFK, Object> {

    @Autowired
    private ApplicationContext applicationContext;

    private FieldValueExists service;
    private String fieldName;

    @Override
    public void initialize(ExistFK unique) {
        Class<? extends FieldValueExists> clazz = unique.service();
        this.fieldName = unique.fieldName();
        String serviceQualifier = unique.serviceQualifier();

        if (!serviceQualifier.equals("")) {
            this.service = this.applicationContext.getBean(serviceQualifier, clazz);
        } else {
            this.service = this.applicationContext.getBean(clazz);
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return this.service.fieldValueExists(o, this.fieldName);
    }
}
