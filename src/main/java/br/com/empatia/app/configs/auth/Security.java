package br.com.empatia.app.configs.auth;

import br.com.empatia.app.auth.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_URL = "/auth/login";
    private static final String[] PUBLIC_MATCHERS = {
            "/swagger-ui.html",
//            "/email/verify",
    };
    private static final String[] PUBLIC_MATCHERS_POST = {
            "/auth/register",
//            "/auth/password/email"
//            "/auth/password/reset
//            "/auth/email/resend",
    };
    private final UserDetailsService userDetails;
    private final JwtUtils utils;

    @Autowired
    public Security(UserServiceImpl userDetails, JwtUtils utils) {
        this.userDetails = userDetails;
        this.utils = utils;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/swagger-ui/**", "/api/v1/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

                .and()
                .addFilter(AuthorizationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager(), utils, userDetails));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }

    public AuthenticationFilter AuthorizationFilter() throws Exception {
        AuthenticationFilter AuthenticationFilter = new AuthenticationFilter(authenticationManager(), utils);
        AuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        return AuthenticationFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
