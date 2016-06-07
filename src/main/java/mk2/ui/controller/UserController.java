package mk2.ui.controller;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import mk2.ui.business.ChangePassword;
import mk2.ui.model.Password;
import mk2.util.UserUtil;
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
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	Mk2LdapUserService userService;

	@Autowired
	UserUtil userUtil;

	@Autowired
	@Qualifier("PasswordValidator")
	private Validator passwordValidator;

	@Autowired
	private ChangePassword passwordLogic;

	@RequestMapping(value = "/showAddresses", method = RequestMethod.GET)
	public String showAddresses(Model model) {
		String dn = userUtil.getCurrentUsersDn();
		Mk2User user = userService.findByDn(dn);

		Map<String, List<String>> sortedAddresses = sortAddresses(user);

		model.addAttribute("addresses", sortedAddresses);
		return "showAddresses";
	}

	private Map<String, List<String>> sortAddresses(Mk2User user) {
		HashMap<String, List<String>> addresses = new HashMap<>();
		if (user == null || user.getEmailAddresses() == null || user.getEmailAddresses().isEmpty()) {
			return addresses;
		}

		for (String currentAddress : user.getEmailAddresses()) {
			String[] split = currentAddress.split("@");


			if (!addresses.containsKey(split[1])) {
				addresses.put(split[1], new ArrayList<>());
			}

			addresses.get(split[1]).add(currentAddress);
		}

		addresses.values().forEach(Collections::sort);

		return addresses;
	}


	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(Model model) {
		model.addAttribute("password", new Password());
		return "changePassword";
	}


	/**
	 * Warning: We use Spring's flash attributes here. So once
	 *          this application starts to run in a cluster, the
	 *          sessions must be kept in sync (e.g. using Redis)
	 */
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