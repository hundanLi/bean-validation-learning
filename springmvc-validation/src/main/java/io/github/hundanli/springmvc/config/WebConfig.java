package io.github.hundanli.springmvc.config;

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
