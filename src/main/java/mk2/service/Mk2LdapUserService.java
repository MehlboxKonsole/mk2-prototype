package mk2.service;

import mk2.mapper.Mk2UserAttributesMapper;
import mk2.model.Mk2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;

@Service
public class Mk2LdapUserService {

	private static final Logger log = LoggerFactory.getLogger(Mk2LdapUserService.class);

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	ContextSource contextSource;

	public Mk2User findByDn(final String dn) {
		String relativeName = dn;
		try {
			relativeName = LdapUtils.getRelativeName(dn, contextSource.getReadOnlyContext());
		} catch (NamingException e) {
			log.info("Given user DN [{}] seems to be invalid.", dn);
		}

		return ldapTemplate.lookup(relativeName, new Mk2UserAttributesMapper());
	}
}