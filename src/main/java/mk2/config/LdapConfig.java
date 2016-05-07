package mk2.config;

import mk2.mapper.Mk2LdapUsernameToDnMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.DefaultLdapUsernameToDnMapper;
import org.springframework.security.ldap.LdapUsernameToDnMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;

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

	@Bean
	public LdapTemplate ldapTemplate() {
		LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
		return ldapTemplate;
	}

	@Bean
	public LdapUsernameToDnMapper getLdapUsernameToDnMapper() {
		return new Mk2LdapUsernameToDnMapper();
	}

	@Bean
	public LdapUserDetailsManager getUserDetailsManager() {
		LdapUserDetailsManager ldapUserDetailsManager = new LdapUserDetailsManager(contextSource());
		ldapUserDetailsManager.setUsernameMapper(getLdapUsernameToDnMapper());
		return ldapUserDetailsManager;
	}
}
