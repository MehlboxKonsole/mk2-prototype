package mk2.ui.controller;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@Autowired
	private Mk2LdapUserService userService;

	@Autowired
	private UserUtil userUtil;

	@RequestMapping("/")
	public String home(Model model) {
		// TODO needs error handling...

		String dn = userUtil.getCurrentUsersDn();

		Mk2User user = userService.findByDn(dn);
		model.addAttribute("user", user);

		return "home";
	}


}
