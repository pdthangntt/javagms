package com.gms.components.annotation.validation;

import com.gms.components.annotation.Unique;
import com.gms.components.annotation.interf.FieldValueUnique;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author vvthanh
 */
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    private ApplicationContext applicationContext;

    private FieldValueUnique service;
    private String fieldName;

    @Override
    public void initialize(Unique unique) {
        Class<? extends FieldValueUnique> clazz = unique.service();
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
        return !this.service.fieldValueUnique(o, this.fieldName);
    }
}
