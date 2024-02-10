package sunuser.mylife.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sunuser.mylife.user.User;
import sunuser.mylife.user.UserRepository;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/profile-image/**", "/posts/media/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRep) {
        return username -> {
            User user = userRep.findByUsername(username).orElse(null);
            if (username != null) return user;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

	 @Bean
	 public CorsConfigurationSource corsConfiguration() {
	 CorsConfiguration configuration = new CorsConfiguration();
	 configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://192.168.0.104:3000"));
	 configuration.setAllowedMethods(Arrays.asList("*"));
	 configuration.setAllowedHeaders(Arrays.asList("*"));
	 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	 source.registerCorsConfiguration("/**", configuration);

	 return source;
	 }
}
