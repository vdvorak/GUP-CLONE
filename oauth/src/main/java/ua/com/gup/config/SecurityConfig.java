package ua.com.gup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import ua.com.gup.config.oauth2.TokenStoreService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@ImportResource(value = {"classpath:spring/security/spring-security.xml"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //dependencies  Bean for OAuth2
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public TokenStore tokenStore() {
        return new TokenStoreService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(){
        ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter();
        return clientCredentialsTokenEndpointFilter;
    }

    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler(){
        return new OAuth2AccessDeniedHandler();
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint(){
        OAuth2AuthenticationEntryPoint  oauthAuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
       // oauthAuthenticationEntryPoint.setRealmName("test");
        return oauthAuthenticationEntryPoint;
    }

    @Bean
    public OAuth2AuthenticationEntryPoint clientAuthenticationEntryPoint(){
        OAuth2AuthenticationEntryPoint  oauthAuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
       // oauthAuthenticationEntryPoint.setTypeName("Basic");
        return oauthAuthenticationEntryPoint;
    }
}

