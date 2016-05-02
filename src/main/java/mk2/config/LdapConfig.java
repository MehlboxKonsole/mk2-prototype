package mk2.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class LdapConfig {

	@Bean
	@ConfigurationProperties(prefix = "mk2.ldap.contextSource")
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		return contextSource;
	}

}
