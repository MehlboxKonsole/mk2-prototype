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

package mk2.validator.ui;

import mk2.ui.model.Password;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("PasswordValidator")
public class PasswordValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Password.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "mk2.error.oldPassword.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "mk2.error.newPassword.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirmation", "mk2.error.passwordConfirmation.empty");

		Password password = (Password) target;

		if (!password.getNewPassword().equals(password.getPasswordConfirmation())) {
			errors.rejectValue("passwordConfirmation", "mk2.error.passwordConfirmation.notEqual");
		}

		if (password.getNewPassword().equals(password.getOldPassword())) {
			errors.rejectValue("newPassword", "mk2.error.newPassword.equalsOld");
		}
	}
}
