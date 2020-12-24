package io.github.hundanli.springmvc.controller;

import io.github.hundanli.springmvc.bean.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 16:29
 */
@RestController
public class UserController {

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody @Validated User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            Map<String, Object> map = new HashMap<>(4);
            bindingResult.getFieldErrors().forEach(error -> {
                map.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        System.out.println(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
