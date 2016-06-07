package mk2.ui.controller;

import mk2.model.Mk2Domain;
import mk2.service.Mk2LdapDomainService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = "/showMyDomains", method = RequestMethod.GET)
	public String showMyDomains(Model model) {

		String currentUsersDn = userUtil.getCurrentUsersDn();
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(currentUsersDn);
		model.addAttribute("domains", domainsForUser);

		return DOMAIN_TEMPLATE;
	}

}
