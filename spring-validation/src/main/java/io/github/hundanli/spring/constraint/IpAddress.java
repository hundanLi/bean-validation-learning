package io.github.hundanli.spring.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 11:20
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IpAddressConstraintValidator.class})
public @interface IpAddress {

    String message() default "{javax.validation.constraints.IpAddress.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
