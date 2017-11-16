package ua.com.gup.rent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import ua.com.gup.rent.config.*;
import ua.com.gup.rent.repository.GenericRepository;

@EnableAutoConfiguration
public class RentUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{
                RentWebConfig.class,
                RentSwaggerConfig.class,
                RentRootConfig.class,
                RentMongoConfig.class,
                RentRootConfig.class,
                RentMailConfig.class,
                RentWebSecurityConfig.class,
                RentMethodSecurityConfig.class,
                RentUiApplication.class}, args);
    }


}
