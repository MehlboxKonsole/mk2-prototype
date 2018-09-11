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

package mk2.ui.business;

import mk2.ui.model.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
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
			changeSuccessful = changeInDirectory(passwordData);
		}


		return changeSuccessful;
	}

	private boolean changeInDirectory(Password passwordData) {
		boolean changedInDirectory = false;

		try {
			LdapShaPasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(passwordData.getNewPassword());

			ldapUserDetailsManager.changePassword(passwordData.getOldPassword(), encodedPassword);

			changedInDirectory = true;
		} catch (BadCredentialsException bce) {
			log.info("Could not update password. See exception for further details.", bce);
		}

		return changedInDirectory;
	}
}
