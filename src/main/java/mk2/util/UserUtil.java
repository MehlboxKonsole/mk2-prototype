package mk2.util;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

	@Autowired
	Mk2LdapUserService userService;

	public String getCurrentUsersDn() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String dn = ((LdapUserDetails) auth.getPrincipal()).getDn();

		return dn;
	}

	public Mk2User getCurrentUser() {
		String userDn = getCurrentUsersDn();

		Mk2User user = userService.findByDn(userDn);

		return user;
	}
}
