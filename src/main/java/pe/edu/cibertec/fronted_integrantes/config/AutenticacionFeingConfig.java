package pe.edu.cibertec.fronted_integrantes.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutenticacionFeingConfig {
    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(10000, 10000);
    }
}
