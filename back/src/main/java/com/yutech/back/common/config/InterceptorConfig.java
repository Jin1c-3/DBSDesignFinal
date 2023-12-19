package com.yutech.back.common.config;

import com.yutech.back.Interceptor.EmpUserInterceptor;
import com.yutech.back.Interceptor.NormalUserInterceptor;
import com.yutech.back.Interceptor.SuperUserInterceptor;
import com.yutech.back.Interceptor.WageUserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	/**
	 * 任何拦截器都不拦截的路径
	 */
	private final String[] EXCLUDE_PATH_PATTERN = {
			"/**/login",
			"/**/login.html",
			"/**/*.js",
			"/**/*.css",
			"/**/*.woff",
			"/**/*.ttf",
			"/**/*.jpg",
			"/css/**",
			"/js/**",
			"/img/**",
			"/media/**",
			"/vendors/**",
			"/avatar/**",
			"/download/**",
			"/test/**",
			"/swagger-ui.html/**",
			"/swagger-ui.html#/**",
			"/swagger-resources/**",
			"/**/webjars/**",
			"/**/v3/**",
	};

	/**
	 * 注入拦截器，交给spring管理
	 */
	@Bean
	public EmpUserInterceptor getEmpUserInterceptor() {
		return new EmpUserInterceptor();
	}

	/**
	 * 注入拦截器，交给spring管理
	 */
	@Bean
	public SuperUserInterceptor getSuperUserInterceptor() {
		return new SuperUserInterceptor();
	}

	/**
	 * 注入拦截器，交给spring管理
	 */
	@Bean
	public NormalUserInterceptor getNormalUserInterceptor() {
		return new NormalUserInterceptor();
	}

	/**
	 * 注入拦截器，交给spring管理
	 */
	@Bean
	public WageUserInterceptor getWageUserInterceptor() {
		return new WageUserInterceptor();
	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getEmpUserInterceptor())
				.addPathPatterns("/**/emp-user/**")
				.excludePathPatterns(EXCLUDE_PATH_PATTERN);
		registry.addInterceptor(getSuperUserInterceptor())
				.addPathPatterns("/**/super-user/**")
				.excludePathPatterns(EXCLUDE_PATH_PATTERN);
		registry.addInterceptor(getNormalUserInterceptor())
				.addPathPatterns("/**/normal-user/**")
				.excludePathPatterns(EXCLUDE_PATH_PATTERN);
		registry.addInterceptor(getWageUserInterceptor())
				.addPathPatterns("/**/wage-user/**")
				.excludePathPatterns(EXCLUDE_PATH_PATTERN);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowCredentials(true)
				.allowedMethods("*")
				.maxAge(60 * 60 * 24);
	}
}
