package mk2.ui.controller;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmailAddressController {

	@Autowired
	Mk2LdapUserService userService;

	@Autowired
	UserUtil userUtil;

	@RequestMapping(value = "/showAddresses", method = RequestMethod.GET)
	public String showAll(Model model) {
		String dn = userUtil.getCurrentUsersDn();

		Mk2User user = userService.findByDn(dn);
		model.addAttribute("user", user);

		return "showAddresses";
	}
}
