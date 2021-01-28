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

package mk2.ui.controller;

import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@Autowired
	private Mk2LdapUserService userService;

	@Autowired
	private UserUtil userUtil;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		// TODO needs error handling...

		String dn = userUtil.getCurrentUsersDn();

		Mk2User user = userService.findByDn(dn);
		model.addAttribute("user", user);

		return "home";
	}

	@GetMapping(value = "/imprint")
	public String imprint(Model model) {
		return "imprint";
	}

}
