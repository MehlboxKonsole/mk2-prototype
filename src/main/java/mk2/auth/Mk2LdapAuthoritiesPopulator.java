package mk2.auth;

import mk2.service.Mk2LdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import java.util.HashSet;
import java.util.Set;

public class Mk2LdapAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator {

	private static final String DOMAIN_OWNER = "DOMAIN_OWNER";

	@Autowired
	private Mk2LdapUserService userService;

	/**
	 * Constructor for group search scenarios. <tt>userRoleAttributes</tt> may still be
	 * set as a property.
	 *
	 * @param contextSource   supplies the contexts used to search for user roles.
	 * @param groupSearchBase if this is an empty string the search will be performed from
	 */
	public Mk2LdapAuthoritiesPopulator(ContextSource contextSource, String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

	@Override
	protected Set<GrantedAuthority> getAdditionalRoles(DirContextOperations user, String username) {
		Set<GrantedAuthority> additionalRoles = new HashSet<>();

		if (userService.hasUserDomains(user.getNameInNamespace())) {
			additionalRoles.add(new SimpleGrantedAuthority(getRolePrefix() + DOMAIN_OWNER));
		}

		return additionalRoles;
	}

}
