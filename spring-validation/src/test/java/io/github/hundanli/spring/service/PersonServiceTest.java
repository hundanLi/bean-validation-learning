package io.github.hundanli.spring.service;

import io.github.hundanli.spring.bean.Person;
import io.github.hundanli.spring.config.AppConfig;
import io.github.hundanli.spring.group.Insert;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class PersonServiceTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    void validatePerson() {
        PersonService service = context.getBean(PersonService.class);
        Person person = new Person(" ", 120);
        person.setIpAddress("267.0.0.1");
        service.validatePerson(person);
        service.validatePerson(person, Insert.class);
    }


    @Test
    void constructPerson() {
        PersonService service = context.getBean(PersonService.class);
        Person person = service.constructPerson(null, 1, "");
        System.out.println(person);
    }
}