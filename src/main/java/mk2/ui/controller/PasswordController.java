package mk2.ui.controller;

import mk2.ui.business.ChangePassword;
import mk2.ui.model.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Provides controls for changing a user's password
 *
 * This controller provides everything needed by the UI to
 * provide a user with a "Change password" functionality.
 *
 * Warning: We use Spring's flash attributes here. So once
 *          this application starts to run in a cluster, the
 *          sessions must be kept in sync (e.g. using Redis)
 */
@Controller
public class PasswordController {

	@Autowired
	@Qualifier("PasswordValidator")
	private Validator passwordValidator;

	@Autowired
	private ChangePassword passwordLogic;

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(Model model) {
		model.addAttribute("password", new Password());
		return "changePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(@Valid Password password, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		passwordValidator.validate(password, bindingResult);

		boolean passwordChanged = passwordLogic.changePassword(bindingResult, password);

		if (!passwordChanged) {
			model.addAttribute("password", password);

			return "changePassword";
		}

		redirectAttributes.addFlashAttribute("message", "Password change successful.");
		return "redirect:/";
	}
}
