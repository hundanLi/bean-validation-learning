package io.github.hundanli.spring.service;

import io.github.hundanli.spring.bean.Person;
import io.github.hundanli.spring.constraint.IpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/22 17:28
 */
@Service
@Validated
public class PersonService {
    @Autowired
    private Validator validator;

    public void validatePerson(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        List<String> list = violations.stream().map(violation -> {
            return violation.getPropertyPath() + violation.getMessage() + ", the invalid value = " + violation.getInvalidValue();
        }).collect(Collectors.toList());
        System.out.println(list);
    }

    public void validatePerson(Person person, Class<?>... groups) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person, groups);
        List<String> list = violations.stream().map(violation -> {
            return violation.getPropertyPath() + violation.getMessage() + ", the invalid value = " + violation.getInvalidValue();
        }).collect(Collectors.toList());
        System.out.println(list);
    }


    public @NotNull Person constructPerson(@NotBlank String name, @Min(1) int age, @IpAddress String ipAddress) {
        Person person = new Person(name, age);
        person.setIpAddress(ipAddress);
        return person;
    }
}
