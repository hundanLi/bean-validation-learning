package io.github.hundanli.springmvc.controller;

import io.github.hundanli.springmvc.bean.User;
import io.github.hundanli.springmvc.validator.UserValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 21:25
 */
@RestController
@RequestMapping("/dbc")
public class DataBinderController {

    private WebDataBinder binder;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new UserValidator());
        this.binder = binder;
    }

    @PostMapping("/user")
    public Object addUser(@RequestBody User user) {
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        Map<String, Object> map = new HashMap<>(4);
        if (bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                map.put(error.getField(), error.getDefaultMessage());
            });
            System.out.println(map);
            return map;
        }

        System.out.println(user);
        return user;
    }
}
