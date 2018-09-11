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

import mk2.model.Mk2Domain;
import mk2.service.Mk2LdapDomainService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/domain")
public class DomainController {

	private final static String DOMAIN_TEMPLATE = "showMyDomains";

	@Autowired
	UserUtil userUtil;

	@Autowired
	Mk2LdapDomainService domainService;

	@PreAuthorize("hasRole('ROLE_DOMAIN_OWNER')")
	@RequestMapping(value = "/showMyDomains", method = RequestMethod.GET)
	public String showMyDomains(Model model) {

		String currentUsersDn = userUtil.getCurrentUsersDn();
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(currentUsersDn);
		model.addAttribute("domains", domainsForUser);

		return DOMAIN_TEMPLATE;
	}

}
