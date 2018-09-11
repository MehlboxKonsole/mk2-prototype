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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class Mk2LdapDomainService {

	private final static String DOMAIN_OWNER_ATTRIBUTE = "associatedName";
	private final static String OBJECT_CLASS = "dNSDomain";

	private Logger log = LoggerFactory.getLogger(Mk2LdapUserService.class);

	@Autowired
	LdapTemplate ldapTemplate;

	public List<Mk2Domain> getDomainsForUser(String owner) {
		if (owner == null) {
			log.warn("Can not search for domains if owner's user name is null.");
		}
		ContainerCriteria ldapQuery = query()
				.where("objectClass").is(OBJECT_CLASS)
				.and(DOMAIN_OWNER_ATTRIBUTE).is(owner);
		List<Mk2Domain> foundDomains = ldapTemplate.find(ldapQuery, Mk2Domain.class);

		return foundDomains;
	}
}
