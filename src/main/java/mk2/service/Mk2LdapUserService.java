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
}