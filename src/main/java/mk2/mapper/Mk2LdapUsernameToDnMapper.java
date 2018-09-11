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

/*
 * This file is part of MehlboxKonsole2.
 *
 *     MehlboxKonsole2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

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
