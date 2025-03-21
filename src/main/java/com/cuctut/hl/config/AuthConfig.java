package com.cuctut.hl.config;

import com.cuctut.hl.auth.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 权限认证拦截
        // 拦截相关请求接口，放行登录注册相关请求接口
        List<String> includePatterns = List.of(
                "/api/**"
        );
        List<String> excludePatterns = List.of(
                "/api/auth/register",
                "/api/auth/login",
                "/api/books/**",
                "/api/files/list/**",
                "/api/files/recent/**",
                "/api/notes/*/*"
        );
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(includePatterns)
                .excludePathPatterns(excludePatterns)
                .order(2);

    }
}
