//package com.audit.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // Allow credentials
//        config.setAllowCredentials(true);
//
//        // Allow origins (adjust for production)
//
//        // Allow all headers
//        config.setAllowedHeaders(Arrays.asList(
//                "Origin",
//                "Content-Type",
//                "Accept",
//                "Authorization",
//                "X-Requested-With",
//                "Access-Control-Request-Method",
//                "Access-Control-Request-Headers"
//        ));
//
//        // Allow all HTTP methods
//        config.setAllowedMethods(Arrays.asList(
//                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
//        ));
//
//        // Expose headers to frontend
//        config.setExposedHeaders(Arrays.asList(
//                "Authorization",
//                "Content-Disposition"
//        ));
//
//        // Cache preflight response for 1 hour
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
//
//}
