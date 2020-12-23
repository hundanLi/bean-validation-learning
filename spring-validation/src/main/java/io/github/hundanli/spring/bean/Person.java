package io.github.hundanli.spring.bean;


import io.github.hundanli.spring.constraint.IpAddress;
import io.github.hundanli.spring.group.Insert;

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

    @IpAddress(groups = Insert.class)
    private String ipAddress;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
