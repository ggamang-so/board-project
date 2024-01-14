package com.ggamangso.boardproject.config;

import com.ggamangso.boardproject.domain.dto.UserAccountDto;
import com.ggamangso.boardproject.domain.dto.security.BoardPrincipal;
import com.ggamangso.boardproject.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin().and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .build();

    }

    // 정적인 요소들에 대해서 Security의 관리에서 완전 무시할 영역으로 권장되지 않고,
    // securityFileterChanin 내 HttpSecurity에서 authorizeHttpRequests permitAll() 사용을 권장함
    // 그러면 인증에서는 제외되지만 security관련 다른 설정들에 포함은 되서 보안적으로 안점함

  /*  @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // security에서 관리를 하지 않고 무시할 영역에 대한 표시
        // static resource, css - js 등
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }*/

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다. - username : " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
