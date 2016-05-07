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

	}
}
