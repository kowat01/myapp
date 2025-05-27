package kr.it.academy.myapp.config;

import kr.it.academy.myapp.handler.AfterLogoutHandler;
import kr.it.academy.myapp.handler.LoginFailureHandler;
import kr.it.academy.myapp.handler.LoginSuccessHandler;
import kr.it.academy.myapp.login.service.LoginDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginDetailService loginDetailService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/webjars/**", "/dist/**", "/plugins/**")
                .requestMatchers("/css/**", "/js/**", "/images/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        // ✅ 로그인 없이 접근 가능한 경로들
                        .requestMatchers(
                                "/", "/home", "/about", "/need", "/category", "/universe", "/timeline",
                                "/factions", "/official", "/used",
                                "/login/**", "/signup", "/terms", "/user/join",
                                "/uploads/**", "/css/**", "/js/**", "/images/**",
                                "/webjars/**", "/dist/**", "/plugins/**"
                        ).permitAll()

                        // ✅ 회원가입 POST 요청 허용
                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()

                        // ✅ 로그인 필요한 경로들
                        .requestMatchers("/board/**").authenticated()
                        .requestMatchers("/resources/**").authenticated()  // 자료실 경로 실제에 맞게 수정

                        // ✅ 관리자 전용
                        .requestMatchers("/user/**", "/api/user/**").hasRole("ADMIN")

                        // ✅ 그 외 경로는 로그인 없이 접근 허용
                        .anyRequest().permitAll()
                )
                .formLogin(f -> f
                        .loginPage("/login/form")
                        .loginProcessingUrl("/login/proc")
                        .successHandler(new LoginSuccessHandler())
                        .failureHandler(new LoginFailureHandler())
                        .permitAll()
                )
                .logout(f -> f
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(new AfterLogoutHandler())
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginDetailService);
        return provider;
    }
}
