package com.example.trading.common.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * Registers the annotated bean only when {@code api.<module>.<version>.enabled=true}.
 * Defaults to enabled when the property is missing.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ApiVersionCondition.class)
public @interface ConditionalOnApiVersion {

	String module();

	String version();
}
