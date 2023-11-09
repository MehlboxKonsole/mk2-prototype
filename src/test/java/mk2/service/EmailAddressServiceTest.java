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

package mk2.service;

import mk2.exception.DomainNotAvailableException;
import mk2.exception.EmailAddressAlreadyInUseException;
import mk2.exception.EmailAddressNotAssignedException;
import mk2.model.Mk2User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailAddressServiceTest {

  @Mock
  private Mk2LdapUserService userService;
  @Mock
  private Mk2LdapDomainService domainService;
  @Mock
  private LdapTemplate ldapTemplate;
  @InjectMocks
  private EmailAddressService emailAddressService;
  private final String userDn = LdapNameBuilder.newInstance()
      .add("dc", "eu")
      .add("dc", "e-mehlbox")
      .add("ou", "Users")
      .add("cn", "test.user")
      .build()
      .toString();

  @Test
  public void newAddressForNotOwnedDomainShouldThrowException() {
    String email = "foo@does-not.exist";

    Mk2User user = new Mk2User();

    when(domainService.getDomainsForUser(userDn)).thenReturn(new ArrayList<>());
    when(userService.findByDn(userDn)).thenReturn(user);

    assertThrows(DomainNotAvailableException.class, () -> emailAddressService.addAddress(userDn, email));
  }

  @Test
  public void emailAlreadyAssignedToUserShouldThrowException() {
    String domainName = "already-in.use";
    String email = "foo@" + domainName;

    Mk2User user = new Mk2User();
    user.addEmailAddress(email);

    when(userService.findByDn(userDn)).thenReturn(user);

    assertThrows(EmailAddressAlreadyInUseException.class, () -> emailAddressService.addAddress(userDn, email));
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
  public void removeEmailAddressFromUser_should_throwException_given_aNotAssignedEmailAddress() {
    String email = "not@assigned.address";
    LdapName userFullDn = LdapNameBuilder.newInstance("cn=existing.user,dc=example,dc=com").build();

    Mk2User user = new Mk2User();
    user.setDn(userFullDn);

    when(userService.findByDn(userFullDn.toString())).thenReturn(user);

    assertThrows(EmailAddressNotAssignedException.class, () ->
        emailAddressService.removeEmailAddressFromUser(email, userFullDn.toString())
    );
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

    assertThat(email, not(is(in(actualUser.getEmailAddresses()))));
  }
}
