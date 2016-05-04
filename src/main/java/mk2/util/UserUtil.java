package mk2.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

	public String getCurrentUsersDn() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String dn = ((LdapUserDetails) auth.getPrincipal()).getDn();

		return dn;
	}

}
