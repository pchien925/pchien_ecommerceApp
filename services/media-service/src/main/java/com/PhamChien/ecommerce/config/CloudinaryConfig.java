package com.PhamChien.ecommerce.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dh0jqp0gf");
        config.put("api_key", "996897339239969");
        config.put("api_secret", "siKTB7FYvyEzFMnXi25V4mKQnkI");
        return new Cloudinary(config);
    }
}
