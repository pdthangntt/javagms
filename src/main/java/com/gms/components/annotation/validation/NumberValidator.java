package com.gms.components.annotation.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author vvthanh
 */
public class NumberValidator implements ConstraintValidator<com.gms.components.annotation.Number, Object> {

    @Override
    public void initialize(com.gms.components.annotation.Number a) {
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        try {
            String number = String.valueOf(t);
            if (number == null || number.equals("")) {
                return true;
            }
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
