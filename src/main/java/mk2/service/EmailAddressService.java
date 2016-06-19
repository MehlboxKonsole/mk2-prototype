package mk2.service;

import mk2.exception.DomainNotAvailableException;
import mk2.exception.EmailAddressAlreadyInUseException;
import mk2.model.Mk2Domain;
import mk2.model.Mk2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class EmailAddressService {

	private static final String MAIN_MAIL_ATTRIBUTE = "mail";
	private static final String ALTERNATE_MAIL_ATTRIBUTE = "mailAlternateAddress";

	@Autowired
	Mk2LdapUserService userService;

	@Autowired
	Mk2LdapDomainService domainService;

	@Autowired
	LdapTemplate ldapTemplate;


	public void addAddress(final String fullUserDn, final String email) throws DomainNotAvailableException, EmailAddressAlreadyInUseException {
		Mk2User user = userService.findByDn(fullUserDn);

		checkNewEmailAddress(email, fullUserDn, user);

		user.addEmailAddress(email);

		userService.updateUser(user);
	}

	private void checkNewEmailAddress(String email, String fullUserDn, Mk2User user) throws DomainNotAvailableException, EmailAddressAlreadyInUseException {
		checkForDuplicatedAddress(email, user);

		checkUserOwnsDomain(email, fullUserDn);
	}

	private void checkForDuplicatedAddress(String email, Mk2User user) throws EmailAddressAlreadyInUseException {
		if (user.getEmailAddresses().contains(email)) {
			throw new EmailAddressAlreadyInUseException();
		}
	}

	private void checkUserOwnsDomain(String email, String fullUserDn) throws DomainNotAvailableException {
		List<Mk2Domain> domainsForUser = domainService.getDomainsForUser(fullUserDn);

		String domain = email.split("@")[1];

		if (domainsForUser.stream().filter(d -> d.getName().equals(domain)).count() == 0) {
			throw new DomainNotAvailableException();
		}
	}

	public boolean isAddressAlreadyAssigned(String email) {
		List<Mk2User> userWithEmailAddress = getUserWithEmailAddress(email);

		return !userWithEmailAddress.isEmpty();
	}

	private List<Mk2User> getUserWithEmailAddress(String email) {
		ContainerCriteria ldapQuery = query()
				.where(MAIN_MAIL_ATTRIBUTE).is(email)
				.or(ALTERNATE_MAIL_ATTRIBUTE).is(email);

		return ldapTemplate.find(ldapQuery, Mk2User.class);
	}

	public boolean isAddressAlreadyAssigned(String email, String... excludeUids) {
		if (excludeUids == null || excludeUids.length == 0) {
			return isAddressAlreadyAssigned(email);
		}

		List<Mk2User> usersWithEmailAddress = getUserWithEmailAddress(email);

		List<String> excludedUids = Arrays.asList(excludeUids);

		Set<Mk2User> remainingUsers = usersWithEmailAddress.stream()
				.filter(u -> !excludedUids.contains(u.getUid()))
				.collect(Collectors.toSet());

		return remainingUsers.size() > 0;
	}
}
