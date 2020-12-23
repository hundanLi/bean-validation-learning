package io.github.hundanli.spring.constraint;

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
