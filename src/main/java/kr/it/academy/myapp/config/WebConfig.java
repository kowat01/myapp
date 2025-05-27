package kr.it.academy.myapp.config;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ 업로드 경로: properties에 선언한 경로와 동일해야 함
    private static final String UPLOAD_PATH = "C:/upload/warhammer/uploads/";

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    // ✅ 파일 업로드 용량 설정
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(30, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(30, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    // ✅ Multipart 파일 처리기
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    // ✅ 정적 리소스 매핑 추가: /uploads/** → 로컬 폴더로 연결
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 로컬 경로를 브라우저에서 /uploads/**로 접근 가능하게 연결
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + UPLOAD_PATH.replace("\\", "/"));
    }
}
