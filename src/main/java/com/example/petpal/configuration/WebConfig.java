package nl.fontys.s3.todolistbackend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${corsheader}")
    private String corsHeader;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] corsHeaders = new String[1];
        corsHeaders[0] = corsHeader;

        registry.addMapping("/**") // Allow CORS for all endpoints
                .allowedOrigins(corsHeaders) // Set allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Set allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow credentials (cookies, authentication)
                .maxAge(3600); // Set how long the response from a pre-flight request can be cached
    }
}
