/*
 * This file is part of MehlboxKonsole2.
 *
 *     MehlboxKonsole2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     MehlboxKonsole2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with MehlboxKonsole2.  If not, see <https://www.gnu.org/licenses/>.
 */

package mk2.config;

import mk2.auth.Mk2LdapAuthoritiesPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	private final LdapContextSource contextSource;

	private static final String LDAP_GROUP_SEARCH_BASE = "ou=Groups";
	private static final String LDAP_GROUP_SEARCH_FILTER = "uniqueMember={0}";

	@Inject
	public WebSecurityConfig(LdapContextSource contextSource) {
		this.contextSource = contextSource;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.headers()
				.addHeaderWriter(new StaticHeadersWriter("Server", "To Serve And Protect"))
				.addHeaderWriter(new StaticHeadersWriter("X-Content-Type-Options", "nosniff"))
				.addHeaderWriter(new StaticHeadersWriter("X-Frame-Options", "DENY"));

		http
				.authorizeRequests()
				.antMatchers("/imprint", "/templates/error", "/static/**", "/generated/**", "/webjars/**", "/css/**", "/favicon.ico").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/error/403")
				.and()
				.logout()
				.permitAll();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// FIXME DN pattern needs to be configurable
		auth
				.ldapAuthentication()
				.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
				.userDnPatterns("uid={0},ou=Users")
				.groupSearchBase(LDAP_GROUP_SEARCH_BASE)
				.groupSearchFilter(LDAP_GROUP_SEARCH_FILTER)
				.contextSource(contextSource);
	}

	@Bean
	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		Mk2LdapAuthoritiesPopulator authoritiesPopulator = new Mk2LdapAuthoritiesPopulator(contextSource, LDAP_GROUP_SEARCH_BASE);

		authoritiesPopulator.setDefaultRole("ROLE_USER");
		authoritiesPopulator.setGroupSearchFilter(LDAP_GROUP_SEARCH_FILTER);

		return authoritiesPopulator;
	}

}
