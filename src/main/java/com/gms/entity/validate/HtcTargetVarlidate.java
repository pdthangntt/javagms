package com.gms.entity.validate; 

import com.gms.entity.db.HtcTargetEntity;
import com.gms.entity.form.HtcTargetForm;
import com.gms.service.HtcTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author pdThang
 */
@Component
public class HtcTargetVarlidate implements Validator {

    @Autowired
    HtcTargetService htcTargetService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HtcTargetForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HtcTargetForm form = (HtcTargetForm) o;
        //Validate unique add 
        if (form.getID() == null && htcTargetService.existsByYearsAndSiteID(form.getYears(), form.getSiteID())) {
            errors.rejectValue("years", "years.error.message", String.format("Năm %s đã tồn tại", form.getYears()));
        }

        if (form.getID() != null) {
            //Validate unique year update
            HtcTargetEntity target = htcTargetService.findByYearsAndSiteID(form.getYears(), form.getSiteID());
            if (target != null && !target.getID().equals(form.getID())) {
                errors.rejectValue("years", "years.error.message", String.format("Năm %s đã tồn tại", form.getYears()));
            }
        }
        if (form.getNumberCustomer() < form.getNumberPositive() && form.getNumberPositive() > 0 ) {
            errors.rejectValue("numberPositive", "numberPositive.error.message", "Không được lớn hơn số khách hàng được xét nghiệm HIV");
        }
        if (form.getNumberCustomer() != null && form.getNumberCustomer() < form.getNumberGetResult() && form.getNumberGetResult() > 0) {
            errors.rejectValue("numberGetResult", "numberGetResult.error.message", "Không được lớn hơn số khách hàng được xét nghiệm HIV");
        }
        if (form.getNumberPositive() < form.getPositiveAndGetResult() && form.getPositiveAndGetResult() > 0) {
            errors.rejectValue("positiveAndGetResult", "positiveAndGetResult.error.message", "Không được lớn hơn số khách hàng có kết quả xét nghiệm HIV (+)");
        }
    }

}
