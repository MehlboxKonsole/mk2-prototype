package mk2.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Mk2User implements Serializable {

	private String dn;
	private String uid;
	private String firstName;
	private String lastName;
	private String displayName;
	private Set<String> emailAddresses;
	private String accountStatus;

	public Mk2User() {	}

	public Mk2User(String dn, String uid, String firstName, String lastName, String displayName, Set<String> emailAddresses, String accountStatus) {
		this.dn = dn;
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.emailAddresses = emailAddresses;
		this.accountStatus = accountStatus;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
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
}
