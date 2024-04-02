package com.cyperts.ExcellML.UserAndRole;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().requestMatchers("/api/**", "/user/**", "/files/**", "/mail/**")
				.permitAll().anyRequest().authenticated();
		http.headers().frameOptions().disable();
		return http.build();
	}

//	@Bean
//	public CommandLineRunner demo(RoleRepository roleRepo) {
//		return (args) -> {
//			if (!roleRepo.existsByName("ROLE_ADMIN")) {
//				Role role = new Role();
//				role.setName("ROLE_ADMIN");
//				roleRepo.save(role);
//			}
//		};
//	}
}