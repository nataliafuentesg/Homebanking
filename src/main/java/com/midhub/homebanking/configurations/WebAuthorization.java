package com.midhub.homebanking.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/cards").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/transactions/accounts").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/loans").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/loans/pay-installment").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/deactivate", "/api/clients/current/accounts/deactivate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/transaction/payment").permitAll()
                .antMatchers("/web/assets/pages/**", "/web/assets/images/**").permitAll()
                .antMatchers("/web/assets/styles/**").permitAll()
                .antMatchers("/web/assets/js/**").permitAll()

                .antMatchers(HttpMethod.GET, "/api/clients", "/api/accounts").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/loans").hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers("/web/manager.html", "/web/manager.js", "/web/createLoanType.html").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/account.html","/web/transfers.html", "/web/loan-application.html").hasAuthority("CLIENT")
                .antMatchers("/web/create-Cards.html").hasAuthority("CLIENT")

                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/logout").authenticated()
                .anyRequest().denyAll()
                .and()


                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .and()
                .logout().logoutUrl("/api/logout");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
