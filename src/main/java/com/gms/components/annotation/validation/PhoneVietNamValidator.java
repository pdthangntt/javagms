package com.gms.components.annotation.validation;

import com.gms.components.annotation.PhoneVietNam;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author vvthanh
 */
public class PhoneVietNamValidator implements ConstraintValidator<PhoneVietNam, Object> {

    @Override
    public void initialize(PhoneVietNam a) {
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        try {
            String number = (String) t;
            if (number == null || number.equals("")) {
                return true;
            }
            Pattern pattern = Pattern.compile("\\d{10}|\\d{11}");
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
