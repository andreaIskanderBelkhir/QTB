package it.tirocinio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity 
@Configuration 
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private MyUserDetailsService myUserDetailsService;
    
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    
     public SecurityConfiguration(MyUserDetailsService musds) {
		this.myUserDetailsService=musds;
	}
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 
    	
    	
        http.csrf().disable()  
            .requestCache().requestCache(new CustomRequestCache()) 
            .and().formLogin()  
            .loginPage(LOGIN_URL).permitAll()
            .loginProcessingUrl(LOGIN_PROCESSING_URL)  
            .failureUrl(LOGIN_FAILURE_URL)
            .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
         .and().authorizeRequests() 
        .antMatchers("/Homepage").authenticated()
        .antMatchers("/admin").hasRole("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
    	return new PasswordEnconderTest();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
    	DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    	auth.setPasswordEncoder(passwordEncoder());
    	auth.setUserDetailsService(this.myUserDetailsService);
    	return auth;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
    	auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/VAADIN/**",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**");
    }
}