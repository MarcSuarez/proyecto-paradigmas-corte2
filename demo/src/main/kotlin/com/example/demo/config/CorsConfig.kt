package com.example.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        
        // Permitir cualquier origen
        config.addAllowedOriginPattern("*")
        
        // Permitir cualquier método HTTP (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*")
        
        // Permitir cualquier header
        config.addAllowedHeader("*")
        
        // Permitir credenciales
        config.allowCredentials = true
        
        // Aplicar la configuración a todas las rutas
        source.registerCorsConfiguration("/**", config)
        
        return CorsFilter(source)
    }
}
