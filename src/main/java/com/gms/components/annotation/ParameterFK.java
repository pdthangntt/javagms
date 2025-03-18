package com.gms.components.annotation;

import com.gms.components.annotation.interf.FieldParameterExists;
import com.gms.components.annotation.interf.FieldValueExists;
import com.gms.components.annotation.validation.ExitsFKValidator;
import com.gms.components.annotation.validation.ParameterFKValidator;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author vvthanh
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ParameterFKValidator.class)
@Documented
public @interface ParameterFK {

    String message() default "{ID không tồn tại}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends FieldParameterExists> service();

    String serviceQualifier() default "";

    String fieldName();

    String type();

    boolean mutiple();
}
