package mk2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	LdapContextSource contextSource;
	
	private static final String LDAP_GROUP_SEARCH_BASE = "ou=Groups";
	private static final String LDAP_GROUP_SEARCH_FILTER = "uniqueMember={0}";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.headers()
					.addHeaderWriter(new StaticHeadersWriter("Server", "To Serve And Protect"))
					.addHeaderWriter(new StaticHeadersWriter("X-XSS-Protection", "1"))
					.addHeaderWriter(new StaticHeadersWriter("X-Content-Type-Options", "nosniff"))
					.addHeaderWriter(new StaticHeadersWriter("X-Frame-Options", "DENY"));

		http
				.authorizeRequests()
					.antMatchers("/error", "/static/**", "/webjars/**").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage("/login")
					.permitAll()
					.and()
				.logout()
					.permitAll();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.ldapAuthentication()
					.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
//					.userDnPatterns("cn={0}@e-mehlbox.eu,ou=internal,ou=Users,dc=e-mehlbox,dc=eu")
					.userDnPatterns("cn={0}@e-mehlbox.eu,ou=internal,ou=Users")
					.groupSearchBase(LDAP_GROUP_SEARCH_BASE)
					.groupSearchFilter(LDAP_GROUP_SEARCH_FILTER)
					.contextSource(contextSource);
	}

	@Bean
	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator () {
		DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, LDAP_GROUP_SEARCH_BASE);

		authoritiesPopulator.setDefaultRole("ROLE_USER");
		authoritiesPopulator.setGroupSearchFilter(LDAP_GROUP_SEARCH_FILTER);

		return authoritiesPopulator;
	}

}
