package fpt.aptech.ParkingApplication;

import fpt.aptech.ParkingApplication.configuration.FilterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingApplication.class, args);
	}
@Bean
    FilterRegistrationBean<FilterConfig> speFilterRegistrationBean() {
        final FilterRegistrationBean<FilterConfig> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new FilterConfig());
        filterRegistrationBean.addUrlPatterns("/profile");
        return filterRegistrationBean;
    }
}
