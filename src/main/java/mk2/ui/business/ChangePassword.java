package mk2.ui.business;

import mk2.ui.model.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ChangePassword {

	private static final Logger log = LoggerFactory.getLogger(ChangePassword.class);

	@Autowired
	LdapUserDetailsManager ldapUserDetailsManager;

	public boolean changePassword(BindingResult bindingResult, Password passwordData) {
		boolean changeSuccessful = false;

		if (!bindingResult.hasErrors()) {
			changeSuccessful= changeInDirectory(passwordData);
		}


		return changeSuccessful;
	}

	private boolean changeInDirectory(Password passwordData) {
		boolean changedInDirectory = false;

		try {
			LdapShaPasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();
			String encodedPassword = passwordEncoder.encodePassword(passwordData.getNewPassword(), null);

			ldapUserDetailsManager.changePassword(passwordData.getOldPassword(), encodedPassword);

			changedInDirectory = true;
		} catch (BadCredentialsException bce) {
			log.info("Could not update password. See exception for further details.", bce);
		}

		return changedInDirectory;
	}
}
