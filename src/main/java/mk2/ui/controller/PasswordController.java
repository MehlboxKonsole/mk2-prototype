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

import javax.validation.Valid;

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
	public String changePassword(@Valid Password password, BindingResult bindingResult, Model model) {
		passwordValidator.validate(password, bindingResult);

		boolean passwordChanged = passwordLogic.changePassword(bindingResult, password);

		if (!passwordChanged) {
			model.addAttribute("password", password);

			return "changePassword";
		}

		return "redirect:/";
	}
}
