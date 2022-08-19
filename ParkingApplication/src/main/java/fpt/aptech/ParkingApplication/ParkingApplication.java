package fpt.aptech.ParkingApplication;

import fpt.aptech.ParkingApplication.configuration.FilterConfig;
import fpt.aptech.ParkingApplication.configuration.FilterConfigAdmin;
import fpt.aptech.ParkingApplication.configuration.FilterConfigHandle;
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
        filterRegistrationBean.addUrlPatterns("/booking");
        filterRegistrationBean.addUrlPatterns("/d-payment");
        filterRegistrationBean.addUrlPatterns("/e-payment");
        filterRegistrationBean.addUrlPatterns("/history");
        filterRegistrationBean.addUrlPatterns("/u/parking-history");
        filterRegistrationBean.addUrlPatterns("/list-booking");
        return filterRegistrationBean;
    }
    @Bean
    FilterRegistrationBean<FilterConfigAdmin> speFilterRegistrationBeanAdmin() {
        final FilterRegistrationBean<FilterConfigAdmin> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new FilterConfigAdmin());
        filterRegistrationBean.addUrlPatterns("/a/*");
        filterRegistrationBean.addUrlPatterns("/list-user");
        filterRegistrationBean.addUrlPatterns("/home/admin");
        filterRegistrationBean.addUrlPatterns("/user-details");
        return filterRegistrationBean;
    }
    @Bean
    FilterRegistrationBean<FilterConfigHandle> speFilterRegistrationBeanHandle() {
        final FilterRegistrationBean<FilterConfigHandle> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new FilterConfigHandle());
        filterRegistrationBean.addUrlPatterns("/scan-qrcode/*");
        filterRegistrationBean.addUrlPatterns("/home/handle");
        filterRegistrationBean.addUrlPatterns("/h/*");
        return filterRegistrationBean;
    }
}
