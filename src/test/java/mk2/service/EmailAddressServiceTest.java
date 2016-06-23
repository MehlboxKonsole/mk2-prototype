package mk2.service;

import mk2.exception.DomainNotAvailableException;
import mk2.exception.EmailAddressAlreadyInUseException;
import mk2.exception.EmailAddressNotAssignedException;
import mk2.model.Mk2Domain;
import mk2.model.Mk2User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isIn;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailAddressServiceTest {

	@Mock
	private Mk2LdapUserService userService;

	@Mock
	private Mk2LdapDomainService domainService;

	@Mock
	private LdapTemplate ldapTemplate;

	@InjectMocks
	private EmailAddressService emailAddressService;

	private String userDn = LdapNameBuilder.newInstance()
			.add("dc", "eu")
			.add("dc", "e-mehlbox")
			.add("ou", "Users")
			.add("ou", "internal")
			.add("cn", "test.user")
			.build()
			.toString();


	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void newAddressForNotOwnedDomainShouldThrowException() throws DomainNotAvailableException, EmailAddressAlreadyInUseException {
		String email = "foo@does-not.exist";

		Mk2User user = new Mk2User();

		when(domainService.getDomainsForUser(userDn)).thenReturn(new ArrayList<>());
		when(userService.findByDn(userDn)).thenReturn(user);

		expectedException.expect(DomainNotAvailableException.class);

		emailAddressService.addAddress(userDn, email);
	}

	@Test
	public void emailAlreadyAssignedToUserShouldThrowException() throws EmailAddressAlreadyInUseException, DomainNotAvailableException {
		String domainName = "already-in.use";
		String email = "foo@" + domainName;

		Mk2User user = new Mk2User();
//		user.setDn(userDn);
		user.addEmailAddress(email);

		Mk2Domain domain = new Mk2Domain();
		domain.setName(domainName);
		List<Mk2Domain> domainList = new ArrayList<>();
		domainList.add(domain);

		when(domainService.getDomainsForUser(userDn)).thenReturn(domainList);
		when(userService.findByDn(userDn)).thenReturn(user);

		expectedException.expect(EmailAddressAlreadyInUseException.class);

		emailAddressService.addAddress(userDn, email);
	}

	@Test
	public void emailAddressUsedByOtherUserShouldReturnTrue() {
		String email = "foo@shared.domain";
		LdapName userDn = LdapNameBuilder.newInstance("cn=foo@bar.com,ou=example,dc=local").build();

		Mk2User user1 = new Mk2User();
		user1.setDn(userDn);
		user1.setUid("foo.bar");
		user1.addEmailAddress(email);

		List<Mk2User> listOfUsersWithEmailAddress = new ArrayList<>(1);
		listOfUsersWithEmailAddress.add(user1);

		when(ldapTemplate.find(any(), eq(Mk2User.class))).thenReturn(listOfUsersWithEmailAddress);

		boolean actualResult = emailAddressService.isAddressAlreadyAssigned(email);

		assertThat(actualResult, is(true));

	}

	@Test
	public void emailAddressNotAssignedToAnyUserShouldReturnFalse() {
		String email = "not@assigned.address";

		when(ldapTemplate.find(any(), eq(Mk2User.class))).thenReturn(new ArrayList<>(0));

		boolean actualResult = emailAddressService.isAddressAlreadyAssigned(email);

		assertThat(actualResult, is(false));
	}

	@Test
	public void emailAddressAssignedToAnotherUserShouldReturnTrue() {
		String email = "shared@address.local";
		String uidToCheck = "given.user";
		String uid = "another.user";

		Mk2User userToCheck = new Mk2User();
		userToCheck.addEmailAddress(email);
		userToCheck.setUid(uidToCheck);

		Mk2User user = new Mk2User();
		user.addEmailAddress(email);
		user.setUid(uid);

		List<Mk2User> userList = new ArrayList<>(2);
		userList.add(userToCheck);
		userList.add(user);

		when(ldapTemplate.find(any(), eq(Mk2User.class))).thenReturn(userList);

		boolean addressAlreadyAssigned = emailAddressService.isAddressAlreadyAssigned(email, uidToCheck);

		assertThat(addressAlreadyAssigned, is(true));
	}

	@Test
	public void nullListOfExcludedUsersReturnTrueIfAddressIsUsedByAnyUser() {
		String email = "shared@address.local";
		String uidToCheck = "given.user";
		String uid = "another.user";

		Mk2User userToCheck = new Mk2User();
		userToCheck.addEmailAddress(email);
		userToCheck.setUid(uidToCheck);

		Mk2User user = new Mk2User();
		user.addEmailAddress(email);
		user.setUid(uid);

		List<Mk2User> userList = new ArrayList<>(2);
		userList.add(userToCheck);
		userList.add(user);

		when(ldapTemplate.find(any(), eq(Mk2User.class))).thenReturn(userList);

		boolean addressAlreadyAssigned = emailAddressService.isAddressAlreadyAssigned(email, (String) null);

		assertThat(addressAlreadyAssigned, is(true));

	}

	@Test
	public void removeEmailAddressFromUser_should_throwException_given_aNotAssignedEmailAddress() throws EmailAddressNotAssignedException {
		String email = "not@assigned.address";
		LdapName userFullDn = LdapNameBuilder.newInstance("cn=existing.user,dc=example,dc=com").build();

		Mk2User user = new Mk2User();
		user.setDn(userFullDn);

		when(userService.findByDn(userFullDn.toString())).thenReturn(user);

		expectedException.expect(EmailAddressNotAssignedException.class);

		emailAddressService.removeEmailAddressFromUser(email, userFullDn.toString());
	}

	@Test
	public void removeEmailAddressFromUser_should_notThrowException_given_anAssignedEmailAddress() throws EmailAddressNotAssignedException {
		String email = "info@example.local";
		LdapName userFullDn = LdapNameBuilder.newInstance("cn=existing.user,dc=example,dc=com").build();

		Mk2User user = new Mk2User();
		user.setDn(userFullDn);
		user.addEmailAddress(email);

		when(userService.findByDn(userFullDn.toString())).thenReturn(user);

		Mk2User actualUser = emailAddressService.removeEmailAddressFromUser(email, userFullDn.toString());

		assertThat(email, not(isIn(actualUser.getEmailAddresses())));
	}
}
