package ca.me.band.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * This configuration class will contain spring beans to deal with the application security.
 *
 * @author Rodrigo Januario da Silva
 * @version 1.0.0
 */
@Configuration
open class SecurityConfig {
	@Bean
	open fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()
}