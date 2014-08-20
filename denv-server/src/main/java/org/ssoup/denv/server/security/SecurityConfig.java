package org.ssoup.denv.server.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // no check for Cross-site request forgery for REST calls32

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // http.httpBasic().init(http);
        http.anonymous().init(http);

        //http.authorizeRequests().antMatchers("/**").fullyAuthenticated();
        http.authorizeRequests().antMatchers("/**").anonymous();
    }
}
