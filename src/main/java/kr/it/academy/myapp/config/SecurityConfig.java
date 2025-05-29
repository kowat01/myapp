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
                                "/", "/home",
                                "/about", "/need", "/category",
                                "/universe", "/timeline", "/factions",
                                "/shop/official", "/shop/used",
                                "/login/**", "/signup", "/terms",
                                "/user/terms", "/user/join", "/user/register",
                                "/uploads/**", "/css/**", "/js/**", "/images/**",
                                "/webjars/**", "/dist/**", "/plugins/**"
                        ).permitAll()

                        // ✅ 회원가입 및 중복확인 API
                        .requestMatchers("/api/user/check-id", "/api/user/check-nickname").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/").permitAll()

                        // ✅ 위키 게시판은 비로그인 허용
                        .requestMatchers("/board/list").permitAll()

                        // ✅ 로그인 필요한 마이페이지, 정보수정
                        .requestMatchers("/user/mypage", "/user/mypageedit").authenticated()

                        // ✅ 나머지 게시판은 로그인 필요
                        .requestMatchers("/board/**").authenticated()

                        // ✅ 관리자 전용
                        .requestMatchers("/user/**", "/api/user/**").hasRole("ADMIN")

                        // ✅ 그 외 요청 허용
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
