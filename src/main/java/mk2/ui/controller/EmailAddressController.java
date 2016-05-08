package mk2.ui.controller;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

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

		Map<String, List<String>> sortedAddresses = sortAddresses(user);

		model.addAttribute("addresses", sortedAddresses);
		return "showAddresses";
	}

	private Map<String, List<String>> sortAddresses(Mk2User user) {
		HashMap<String, List<String>> addresses = new HashMap();
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
}
