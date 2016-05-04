package mk2.service;

import mk2.mapper.Mk2UserAttributesMapper;
import mk2.model.Mk2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

@Service
public class Mk2LdapUserService {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	ContextSource contextSource;

	public Mk2User findByDn(String dn) {
		return ldapTemplate.lookup(dn, new Mk2UserAttributesMapper());
	}

}
