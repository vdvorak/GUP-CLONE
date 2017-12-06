package ua.com.gup.rent.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import ua.com.gup.rent.api.RentEndpoint_test;
import ua.com.gup.rent.api.RentOfferCalendarEndpoint;
import ua.com.gup.rent.api.RentOfferCategoryEndpoint;

import javax.servlet.Filter;

@Configuration
@ComponentScan(basePackageClasses = {RentEndpoint_test.class, RentOfferCategoryEndpoint.class, RentOfferCalendarEndpoint.class})
public class RentWebConfig {


    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {
        return registerFilter(new CharacterEncodingFilter("UTF-8", true), "CharacterEncodingFilter", 1, "/*");
    }


    private FilterRegistrationBean registerFilter(Filter filter, String name, int order, String... urlPatterns) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.addUrlPatterns(urlPatterns);
        registration.setName(name);
        registration.setOrder(order);
        return registration;

    }
}
