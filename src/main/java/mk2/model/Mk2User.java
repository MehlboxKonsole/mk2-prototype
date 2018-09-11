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

package mk2.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entry(objectClasses = {"qmailUser", "top"})
public final class Mk2User implements Serializable {

	@Id
	private Name userDn;

	@Attribute(name = "uid")
	private String uid;

	@Attribute(name = "givenName")
	private String firstName;

	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "displayName")
	private String displayName;

	@Attribute(name = "mailAlternateAddress")
	private Set<String> emailAddresses;

	@Attribute(name = "accountStatus")
	private String accountStatus;

	public Mk2User() {
		this.emailAddresses = new HashSet<>();
	}

	public Mk2User(LdapName dn, String uid, String firstName, String lastName, String displayName, Set<String> emailAddresses, String accountStatus) {
		this.userDn = dn;
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.emailAddresses = emailAddresses;
		this.accountStatus = accountStatus;
	}

	public String getDn() {
		return (userDn != null ? userDn.toString() : null);
	}

	public void setDn(LdapName dn) {
		this.userDn = dn;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Set<String> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(Set<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public void addEmailAddress(String emailAddress) {
		if (this.emailAddresses == null) {
			this.emailAddresses = new HashSet<>();
		}

		this.emailAddresses.add(emailAddress);
	}

	public void removeEmailAddress(String emailAddress) {
		if (this.emailAddresses != null) {
			this.emailAddresses.remove(emailAddress);
		}
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Mk2User user = (Mk2User) o;

		if (userDn != null ? !userDn.equals(user.userDn) : user.userDn != null) return false;
		if (getDn() != null ? !getDn().equals(user.getDn()) : user.getDn() != null) return false;
		if (getUid() != null ? !getUid().equals(user.getUid()) : user.getUid() != null) return false;
		if (getFirstName() != null ? !getFirstName().equals(user.getFirstName()) : user.getFirstName() != null)
			return false;
		if (getLastName() != null ? !getLastName().equals(user.getLastName()) : user.getLastName() != null)
			return false;
		if (getDisplayName() != null ? !getDisplayName().equals(user.getDisplayName()) : user.getDisplayName() != null)
			return false;
		if (getEmailAddresses() != null ? !getEmailAddresses().equals(user.getEmailAddresses()) : user.getEmailAddresses() != null)
			return false;
		return getAccountStatus() != null ? getAccountStatus().equals(user.getAccountStatus()) : user.getAccountStatus() == null;

	}

	@Override
	public int hashCode() {
		int result = userDn != null ? userDn.hashCode() : 0;
		result = 31 * result + (getDn() != null ? getDn().hashCode() : 0);
		result = 31 * result + (getUid() != null ? getUid().hashCode() : 0);
		result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
		result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
		result = 31 * result + (getDisplayName() != null ? getDisplayName().hashCode() : 0);
		result = 31 * result + (getEmailAddresses() != null ? getEmailAddresses().hashCode() : 0);
		result = 31 * result + (getAccountStatus() != null ? getAccountStatus().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Mk2User{" +
				"userDn=" + userDn +
				", dn='" + userDn + '\'' +
				", uid='" + uid + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", displayName='" + displayName + '\'' +
				", emailAddresses=" + emailAddresses +
				", accountStatus='" + accountStatus + '\'' +
				'}';
	}
}
