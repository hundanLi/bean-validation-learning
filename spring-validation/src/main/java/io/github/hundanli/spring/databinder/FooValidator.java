package io.github.hundanli.spring.databinder;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 14:11
 */
public class FooValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Foo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Field 'name' can't be blank!");
        Foo foo = (Foo) target;
        if (foo.getAge() < 0) {
            errors.rejectValue("age", "negativevalue", "Age can't be negative!");
        } else if (foo.getAge() > 120){
            errors.rejectValue("age", "too.darn.old", "Age is too old!");
        }

    }
}
