package io.github.hundanli.spring.databinder;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

class DataBinderTest {

    @Test
    void validate() {
        Foo foo = new Foo();
        DataBinder binder = new DataBinder(foo);
        binder.setValidator(new FooValidator());
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                System.out.println(fieldError.getDefaultMessage() + ", invalid value: " + fieldError.getField() + " = " + fieldError.getRejectedValue());
            });
        }
    }
}