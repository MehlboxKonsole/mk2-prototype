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
