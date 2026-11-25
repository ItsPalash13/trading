package com.example.trading.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global MVC config for API versioning via URL path prefixes.
 *
 * Versioned controllers keep short mappings (e.g. {@code /users}).
 * This class prepends {@code /api/v1} or {@code /api/v2} based on the
 * controller's package name ({@code ...web.v1...} or {@code ...web.v2...}).
 *
 * Example: {@code user.adapter.in.web.v1.UserV1Controller} with {@code @RequestMapping("/users")}
 * becomes {@code /api/v1/users/...}.
 *
 * Unversioned modules (e.g. auth) map their full path on the controller itself.
 *
 * Which versions are registered is controlled by {@code api.<module>.<version>.enabled}
 * in application.yml via {@link ConditionalOnApiVersion} on each versioned controller.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// Controllers under packages containing ".web.v1" → /api/v1/...
		configurer.addPathPrefix("/api/v1", c -> c.getPackageName().contains(".web.v1"));
		// Controllers under packages containing ".web.v2" → /api/v2/...
		configurer.addPathPrefix("/api/v2", c -> c.getPackageName().contains(".web.v2"));
	}
}
