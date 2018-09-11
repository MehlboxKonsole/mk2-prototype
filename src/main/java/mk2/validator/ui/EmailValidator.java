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

package mk2.validator.ui;

import mk2.model.Mk2Domain;
import mk2.model.Mk2User;
import mk2.service.Mk2LdapDomainService;
import mk2.ui.model.EmailAddress;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component("EmailValidator")
public class EmailValidator implements Validator {

	@Autowired
	private Mk2LdapDomainService domainService;

	@Autowired
	private UserUtil userUtil;

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailAddress.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "localPart", "mk2.error.email.localPart.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "domain", "mk2.error.email.domain.empty");

		EmailAddress emailAddress = (EmailAddress) target;

		Mk2User currentUser = userUtil.getCurrentUser();
		String currentUsersFullDn = userUtil.getCurrentUsersDn();
		checkUserOwnsDomains(emailAddress, currentUsersFullDn, errors);
		checkForDuplicatedEmailAddress(emailAddress, currentUser, errors);
	}

	private void checkForDuplicatedEmailAddress(EmailAddress emailAddress, Mk2User currentUser, Errors errors) {
		if (currentUser.getEmailAddresses().contains(emailAddress.toString())) {
			errors.rejectValue("localPart", "mk2.error.email.localPart.duplicate");
		}
	}

	private void checkUserOwnsDomains(EmailAddress emailAddress, String currentUserFullDn, Errors errors) {
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(currentUserFullDn);


		if (domainsForUser.stream().filter(d -> d.getName().equals(emailAddress.getDomain())).count() == 0) {
			errors.rejectValue("domain", "mk2.error.email.domain.notOwnedByUser");
		}
	}
}
