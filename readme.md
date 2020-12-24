# Java数据校验

## 1.Hibernate数据校验

[Hibernate Validator参考文档](https://docs.jboss.org/hibernate/validator/6.1/reference/en-US/html_single/)

Hibernate Validator是Jakarta Bean Validation的参考实现。

### 1.1 快速开始

#### 1.1.1 添加依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>6.1.5.Final</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>jakarta.el</artifactId>
        <version>3.0.3</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.5.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### 1.1.2 应用约束注解

```java
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/24 10:09
 */
public class Car {
    @NotNull
    private String manufacturer;
    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;
    @Min(2)
    private int seatCount;

    public Car(@NotNull String manufacturer, @NotNull @Size(min = 2, max = 14) String licensePlate, @Min(2) int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
    }
 // getters and setters   
}
```

#### 1.1.3 校验约束

```java
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        // 构造ValidatorFactory
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        // 获取validator实例
        validator = validatorFactory.getValidator();
    }

    @Test
    void manufacturerIsNull() {
        Car car = new Car(null, "10086", 2);
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        assertEquals("不能为null", violations.iterator().next().getMessage());
    }

    @Test
    public void licensePlateTooShort() {
        Car car = new Car("Morris", "D", 4);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(1, constraintViolations.size());
        assertEquals(
                "个数必须在2和14之间",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void seatCountTooLow() {
        Car car = new Car("Morris", "DD-AB-123", 1);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(1, constraintViolations.size());
        assertEquals(
                "最小不能小于2",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void carIsValid() {
        Car car = new Car("Morris", "DD-AB-123", 2);

        Set<ConstraintViolation<Car>> constraintViolations =
                validator.validate(car);

        assertEquals(0, constraintViolations.size());
    }
}
```



### 1.2 更多用法

见官方参考文档：[Hibernate Validation Reference](https://docs.jboss.org/hibernate/validator/6.1/reference/en-US/html_single)



## 2.Spring数据校验

Spring可以跟Hibernate Validator集成，添加一些额外的功能。[Spring参考文档](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation-beanvalidation)

### 2.1 快速上手

#### 2.1.1 添加依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.2.8.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.5.Final</version>
</dependency>
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>jakarta.el</artifactId>
    <version>3.0.3</version>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.5.2</version>
    <scope>test</scope>
</dependency>
```

#### 2.1.2 校验器配置

```java
// 将校验器bean注册到ioc容器
@Configuration
@ComponentScan(basePackages = "io.github.hundanli.spring")
public class AppConfig {
    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}
```

#### 2.1.3 注入校验器

Person.java：

```java
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/22 17:28
 */
public class Person {
    @NotBlank
    private String name;
    @Min(1)
    @Max(120)
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    // getters and setters
}
```

PersonService.java

```java
import io.github.hundanli.spring.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/22 17:28
 */
@Service
public class PersonService {
    // 注入校验器  javax.validation.Validator
    @Autowired
    private Validator validator;

    // 校验方法
    public void validatePerson(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        List<String> list = violations.stream().map(violation -> {
            return violation.getPropertyPath() + violation.getMessage() + ", the invalid value = " + violation.getInvalidValue();
        }).collect(Collectors.toList());
        System.out.println(list);
    }
}
```

#### 2.1.4 测试校验

```java
class PersonServiceTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    void addPerson() {
        PersonService service = context.getBean(PersonService.class);
        Person person = new Person("k   ", -1);
        service.validatePerson(person);

    }
}
```



### 2.2 自定义约束

每个bean校验约束都包括两个部分：

- `@Constraint`注解，用于声明约束及其可配置属性
- `javax.validation.ConstraintValidator`接口的实现，用于实现约束的行为。

`@Constraint`注解需要引用一个对应的ConstraintValidator实现类来将声明与实现绑定。在运行时，当在Java Bean中遇到约束注解时，ConstraintValidatorFactory将实例化ConstraintValidator引用的实现。

默认情况下，LocalValidatorFactoryBean配置了一个SpringConstraintValidatorFactory，该工厂使用Spring创建ConstraintValidator实例。这使自定义ConstraintValidator像其他任何Spring bean一样能够使用依赖注入。

#### 2.2.1 定义`@Constraint`注解

自定义约束注解，用于校验IP地址是否合法：

```java
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 11:20
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IpAddressConstraintValidator.class})
public @interface IpAddress {

    String message() default "{javax.validation.constraints.IpAddress.message}"; // message，用于错误提示，需要在classpath:ValidationMessages.properties配置文件中定义 

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
```



#### 2.2.2 定义`ConstraintValidator`实现类

```java
import sun.net.util.IPAddressUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 11:36
 */
public class IpAddressConstraintValidator implements ConstraintValidator<IpAddress, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return IPAddressUtil.isIPv4LiteralAddress(value) || IPAddressUtil.isIPv6LiteralAddress(value);
    }
}
```

#### 2.2.3 编写消息属性文件

在src/main/resources目录下创建ValidationMessages.properties文件：

```properties
javax.validation.constraints.IpAddress.message = "IP地址不合法"
```



### 2.3 分组校验

javax.validation.Validator还支持分组校验，只需要几个步骤即可：

1. 定义分组标记接口
2. 在使用约束注解时指定分组字段
3. 在校验时调用validate(T object, Class<?>... groups);方法

#### 2.3.1 定义分组接口

```java
public interface Insert {

}
```

#### 2.3.2 使用注解时指定group属性

```java
@IpAddress(groups = Insert.class)
private String ipAddress;

```

#### 2.3.3 校验时指定分组类型

```java
public void validatePerson(Person person, Class<?>... groups) {
    Set<ConstraintViolation<Person>> violations = validator.validate(person, groups);
    List<String> list = violations.stream().map(violation -> {
        return violation.getPropertyPath() + violation.getMessage() + ", the invalid value = " + violation.getInvalidValue();
    }).collect(Collectors.toList());
    System.out.println(list);
}
```



### 2.4 方法校验

Spring还支持方法参数和返回值的校验。使用方法分两步：

1. 注册MethodValidationPostProcessor后置处理器的bean
2. 在类上标注`@Validated`注解
3. 在方法参数或者返回值上标注校验注解

#### 2.4.1 注册MethodValidationPostProcessor

```java
@Bean
public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
}
```

#### 2.4.2 标注类和方法

```java
/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/22 17:28
 */
@Service
@Validated
public class PersonService {

    public @NotNull Person constructPerson(@NotBlank String name, @Min(1) int age, @IpAddress String ipAddress) {
        Person person = new Person(name, age);
        person.setIpAddress(ipAddress);
        return person;
    }
}

```



### 2.5 DataBinder

从Spring 3开始，可以使用`org.springframework.validation.Validator`来配置DataBinder实例。配置完成后，可以通过调用binder.validate()来调用Validator。任何验证错误都会自动添加到binder对象的BindingResult中。

#### 2.5.1 定义Validator

Foo.java：

```java
public class Foo {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

FooValidator.java：

```java
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
```

#### 2.5.2 使用DataBinder校验

```java
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
```



## 2.Spring mvc数据校验

在Spring MVC项目中，除了具备Spring数据校验的所有方法，默认情况下，如果Bean Validation（例如，Hibernate Validator）存在于类路径中，则LocalValidatorFactoryBean将注册为全局校验器，以便于支持在Controller方法参数上使用`@Valid`或者`@Validated`注解进行数据校验。

### 2.1 Spring mvc项目搭建

#### 2.1.1 添加依赖

创建maven项目，修改打包方式为war，并添加依赖：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-learning</artifactId>
        <groupId>io.github.hundanli</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springmvc-validation</artifactId>
    <packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.8.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.2</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>6.1.5.Final</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish</groupId>
            <artifactId>jakarta.el</artifactId>
            <version>3.0.3</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.5.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>springmvc-validation</finalName>
    </build>
</project>
```



#### 2.1.2 编写配置类

AppConfig.java

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 17:31
 */
@Configuration
@ComponentScan(
        basePackages = "io.github.hundanli.springmvc",
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Controller.class
        )},
        useDefaultFilters = false
)
public class AppConfig {
}
```

WebConfig.java

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 16:32
 */
@EnableWebMvc
@Configuration
@ComponentScan(
        basePackages = "io.github.hundanli.springmvc",
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ANNOTATION,
                        classes = Controller.class
                )
        },
        useDefaultFilters = false
)
public class WebConfig implements WebMvcConfigurer {

}
```

WebAppInitializer.java

```java
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 17:23
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
}
```



### 2.2 Controller方法参数校验

#### 2.2.1 编写Bean

User.java

```java
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/23 16:22
 */
public class User {
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @Email
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
```

#### 2.2.2 编写Controller

```java
import io.github.hundanli.springmvc.bean.User;
import org.springframework.http.HttpEntity;
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

```



### 2.3 DataBinder参数校验

除了通过Controller的方法参数自动校验，还可以通过结合DataBinder和org.springframework.validation.Validator来手动校验。分以下两个步骤：

1. 实现org.springframework.validation.Validator接口
2. 在Controller初始化DataBinder，绑定Validator
3. 调用binder.validate()方法进行校验

#### 2.3.1 实现Validator接口

```java
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
```

#### 2.3.2 初始化DataBinder

```java
@RestController
@RequestMapping("/dbc")
public class DataBinderController {

    private WebDataBinder binder;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new UserValidator());
        this.binder = binder;
    }
    //...
}
```

#### 2.3.3 调用校验方法

```java
    @PostMapping("/user")
    public Object addUser(@RequestBody User user) {
        // 数据校验
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
```





