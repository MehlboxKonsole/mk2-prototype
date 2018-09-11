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

package mk2.service;

import mk2.model.Mk2Domain;
import mk2.model.Mk2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.ldap.LdapName;
import java.util.List;

@Service
public class Mk2LdapUserService {

	private static final Logger log = LoggerFactory.getLogger(Mk2LdapUserService.class);

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	Mk2LdapDomainService domainService;

	@Autowired
	ContextSource contextSource;

	public Mk2User findByDn(final String dn) {
		String relativeName = dn;
		try {
			relativeName = LdapUtils.getRelativeName(dn, contextSource.getReadOnlyContext());
		} catch (NamingException e) {
			log.info("Given user DN [{}] seems to be invalid.", dn);
		}

		LdapName rdn = LdapNameBuilder.newInstance(relativeName).build();
		Mk2User user = ldapTemplate.findByDn(rdn, Mk2User.class);

		return user;
	}

	public boolean hasUserDomains(final String fullUserDn) {
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(fullUserDn);
		return ! domainsForUser.isEmpty();
	}

	public void updateUser(final Mk2User user) {
		ldapTemplate.update(user);
	}
}