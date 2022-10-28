package pl.com.bottega.ecom.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.com.bottega.ecom.user.UserFacade;

@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserFacade userFacade;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
        http.formLogin();
        http.authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/categories", "/products")
            .anonymous()
            .and()
            .authorizeRequests()
            .mvcMatchers(HttpMethod.POST, "/users")
            .anonymous();
        http.authorizeRequests().anyRequest().authenticated();
        http.userDetailsService((login) -> userFacade.findByEmail(login));
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
