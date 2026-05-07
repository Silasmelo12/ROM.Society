package concept.com.example.club.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

    @Bean
    static RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.fromHierarchy(
                """
                        ROLE_ADMIN > ROLE_BARMAN
                        ROLE_ADMIN > ROLE_CONCIERGE
                        ROLE_ADMIN > ROLE_INFINITE
                        ROLE_INFINITE > ROLE_BLACK
                        ROLE_BLACK > ROLE_GOLD"""
        );
    }
}
