package mk2.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.ldap.LdapUsernameToDnMapper;
import org.springframework.stereotype.Component;

/**
 * Spring LDAP uses classes implementing LdapUsernameToDnMapper for user name
 * to User DN mappings. Sadly, the current version is still using the
 * deprecated DistinguishedName class. To avoid visual noise, I switch off
 * the warning via @SuppressWarnings
 */
@SuppressWarnings("deprecation")
@Component
@ConfigurationProperties(prefix = "mk2.ldap.userDnMapper")
public class Mk2LdapUsernameToDnMapper implements LdapUsernameToDnMapper {

	private String usernamePattern;
	private String userAttribute;
	private String userBaseDn;

	@Override
	public DistinguishedName buildDn(String username) {
		final String userName = String.format(usernamePattern, username);

		DistinguishedName dn = new DistinguishedName(userBaseDn);
		dn.add(userAttribute, userName);

		return dn;
	}

	public void setUsernamePattern(String usernamePattern) {
		this.usernamePattern = usernamePattern;
	}

	public void setUserAttribute(String userAttribute) {
		this.userAttribute = userAttribute;
	}

	public void setUserBaseDn(String userBaseDn) {
		this.userBaseDn = userBaseDn;
	}
}
