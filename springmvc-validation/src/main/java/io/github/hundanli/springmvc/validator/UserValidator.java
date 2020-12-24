package io.github.hundanli.springmvc.validator;

import io.github.hundanli.springmvc.bean.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 21:26
 */
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "", "Name can't be blank!");
        User user = (User) target;
        if (user.getAge() == null) {
            errors.rejectValue("age", "", "Age can not be null!");
        }
        if (user.getAge() < 0) {
            errors.rejectValue("age", "", "Age can not be negative!");
        }

    }
}
