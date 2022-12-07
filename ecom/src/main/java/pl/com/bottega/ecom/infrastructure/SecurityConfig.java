package pl.com.bottega.ecom.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.com.bottega.ecom.user.UserFacade;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
@RequiredArgsConstructor
class SecurityConfig {

    private final UserFacade userFacade;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
        http.formLogin();
        http.authorizeHttpRequests((authz) ->
            authz.requestMatchers(HttpMethod.GET, "/categories", "/products").anonymous()
                .requestMatchers(HttpMethod.POST, "/users").anonymous()
                //.requestMatchers(HttpMethod.POST, "/categories", "/products").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.PUT, "/categories", "/products").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.userDetailsService((login) -> userFacade.findByEmail(login));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword);
            }
        };
    }
}
