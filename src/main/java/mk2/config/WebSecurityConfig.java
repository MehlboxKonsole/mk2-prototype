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
//		auth
//				.inMemoryAuthentication()
//					.withUser("user").password("password").roles("USER")
//					.and()
//					.withUser("admin").password("password").roles("ADMIN");

		auth
				.ldapAuthentication()
					.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
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
