package com.jsxnh.annotation;

import com.jsxnh.http.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestMapping {

    String value();
    HttpMethod method() default HttpMethod.GET;
    String produce();

}
