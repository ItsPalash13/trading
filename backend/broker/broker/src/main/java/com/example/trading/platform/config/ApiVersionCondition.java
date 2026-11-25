package com.example.trading.platform.config;

import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Reads {@code api.<module>.<version>.enabled} from the Environment.
 * New modules/versions only need YAML + {@link ConditionalOnApiVersion} — no properties class changes.
 */
public class ApiVersionCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnApiVersion.class.getName());
		if (attributes == null) {
			return true;
		}

		String module = String.valueOf(attributes.get("module"));
		String version = String.valueOf(attributes.get("version"));
		String property = "api." + module + "." + version + ".enabled";

		return context.getEnvironment().getProperty(property, Boolean.class, true);
	}
}
