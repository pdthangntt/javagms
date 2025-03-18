package com.gms.components.annotation;

import com.gms.components.annotation.validation.NumberValidator;
import com.gms.components.annotation.validation.PhoneVietNamValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author vvthanh
 */
@Constraint(validatedBy = NumberValidator.class)
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
public @interface Number {

    String message() default "Fields are not matching";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Number[] value();
    }
}
