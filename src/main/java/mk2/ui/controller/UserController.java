package mk2.ui.controller;

import mk2.exception.DomainNotAvailableException;
import mk2.exception.EmailAddressAlreadyInUseException;
import mk2.exception.EmailAddressNotAssignedException;
import mk2.model.Mk2Domain;
import mk2.model.Mk2User;
import mk2.service.EmailAddressService;
import mk2.service.Mk2LdapDomainService;
import mk2.service.Mk2LdapUserService;
import mk2.ui.business.ChangePassword;
import mk2.ui.business.EmailAddressLogic;
import mk2.ui.model.EmailAddress;
import mk2.ui.model.Password;
import mk2.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	Mk2LdapUserService userService;

	@Autowired
	Mk2LdapDomainService domainService;

	@Autowired
	EmailAddressService emailAddressService;

	@Autowired
	UserUtil userUtil;

	@Autowired
	@Qualifier("PasswordValidator")
	private Validator passwordValidator;

	@Autowired
	@Qualifier("EmailValidator")
	private Validator emailValidator;

	@Autowired
	private ChangePassword passwordLogic;

	@Autowired
	private EmailAddressLogic emailAddressLogic;

	@RequestMapping(value = "/showAddresses", method = RequestMethod.GET)
	public String showAddresses(Model model) {
		String dn = userUtil.getCurrentUsersDn();
		Mk2User user = userService.findByDn(dn);

		model.addAttribute("addresses", user.getEmailAddresses());
		return "showAddresses";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword(Model model) {
		model.addAttribute("password", new Password());
		return "changePassword";
	}

	/**
	 * Warning: We use Spring's flash attributes here. So once
	 * this application starts to run in a cluster, the
	 * sessions must be kept in sync (e.g. using Redis)
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

	@PreAuthorize("hasRole('ROLE_DOMAIN_OWNER')")
	@RequestMapping(value = "/emailAddress/add", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String addEmailAddress(Model model) {
		EmailAddress address = new EmailAddress();
		model.addAttribute("address", address);

		prepareDomainsForEmailAddressForm(model);

		return "addEmailAddress";
	}

	private void prepareDomainsForEmailAddressForm(Model model) {
		String dn = userUtil.getCurrentUsersDn();
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(dn);
		model.addAttribute("ownedDomains", domainsForUser);
	}

	@PreAuthorize("hasRole('ROLE_DOMAIN_OWNER')")
	@RequestMapping(value = "/emailAddress/add", method = RequestMethod.POST)
	public String addEmailAddress(@Valid @ModelAttribute("address") EmailAddress address, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws DomainNotAvailableException, EmailAddressAlreadyInUseException {
		emailValidator.validate(address, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("address", address);

			prepareDomainsForEmailAddressForm(model);

			return "addEmailAddress";
		}

		emailAddressService.addAddress(userUtil.getCurrentUsersDn(), address.toString());

		flagMultiAssignment(address, redirectAttributes);

		redirectAttributes.addFlashAttribute("message", "Address added.");

		return "redirect:/";
	}

	/**
	 * FIXME: Technical debt ahead! This should be a DELETE, but HTML is a bit limited.
	 */
	@PreAuthorize("hasRole('ROLE_DOMAIN_OWNER')")
	@RequestMapping(value = "/emailAddress/delete", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
	public String deleteEmailAddress(@ModelAttribute("address") String email, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws EmailAddressNotAssignedException {
		log.debug("Will delete e-mail address '" + email + "'");

		try {
			emailAddressLogic.delete(email);

			redirectAttributes.addFlashAttribute("message", "Address removed");
		} catch (EmailAddressNotAssignedException ex) {
			redirectAttributes.addFlashAttribute("error", "You can only remove emails assigned to your account.");
		}

		return "redirect:/user/showAddresses";
	}

	private void flagMultiAssignment(EmailAddress address, RedirectAttributes redirectAttributes) {
		if (emailAddressService.isAddressAlreadyAssigned(address.toString(), userUtil.getCurrentUser().getUid())) {
			redirectAttributes.addFlashAttribute("warning", "Email address is assigned to multiple users.");
		}
	}
}