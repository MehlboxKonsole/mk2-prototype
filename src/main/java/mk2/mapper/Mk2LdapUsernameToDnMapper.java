package mk2.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.ldap.LdapUsernameToDnMapper;

/**
 * Spring LDAP uses classes implementing LdapUsernameToDnMapper for user name
 * to User DN mappings. Sadly, the current version is still using the
 * deprecated DistinguishedName class. To avoid visual noise, I switch off
 * the warning via @SuppressWarnings
 */
@SuppressWarnings("deprecations")
@ConfigurationProperties(prefix = "mk2.ldap.userDnMapper")
public class Mk2LdapUsernameToDnMapper implements LdapUsernameToDnMapper {

	private String usernamePattern = "%s@e-mehlbox.eu";
	private String userAttribute = "cn";
	private String userBaseDn = "ou=internal,ou=Users";

	@Override
	public DistinguishedName buildDn(String username) {
		final String userName = String.format(usernamePattern, username);

		DistinguishedName dn = new DistinguishedName(userBaseDn);
		dn.add(userAttribute, userName);

		return dn;
	}
}
