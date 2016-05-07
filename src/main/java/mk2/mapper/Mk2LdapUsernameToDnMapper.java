package mk2.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.ldap.LdapUsernameToDnMapper;

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
