package springbootapp.minivms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springbootapp.minivms.interceptor.UserTypeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserTypeInterceptor())
                .addPathPatterns("/buyer/**", "/supplier/**", "/worker/**");
    }
}
