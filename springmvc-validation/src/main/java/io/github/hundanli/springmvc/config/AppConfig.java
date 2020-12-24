package io.github.hundanli.springmvc.config;

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
