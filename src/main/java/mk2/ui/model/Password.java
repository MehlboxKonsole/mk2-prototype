package mk2.ui.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class Password implements Serializable {

	@NotEmpty
	private String newPassword;

	@NotEmpty
	private String passwordConfirmation;

	@NotEmpty
	private String oldPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Password password = (Password) o;

		if (getNewPassword() != null ? !getNewPassword().equals(password.getNewPassword()) : password.getNewPassword() != null)
			return false;
		if (getPasswordConfirmation() != null ? !getPasswordConfirmation().equals(password.getPasswordConfirmation()) : password.getPasswordConfirmation() != null)
			return false;
		return getOldPassword() != null ? getOldPassword().equals(password.getOldPassword()) : password.getOldPassword() == null;

	}

	@Override
	public int hashCode() {
		int result = getNewPassword() != null ? getNewPassword().hashCode() : 0;
		result = 31 * result + (getPasswordConfirmation() != null ? getPasswordConfirmation().hashCode() : 0);
		result = 31 * result + (getOldPassword() != null ? getOldPassword().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Password{" +
				"newPassword='" + newPassword + '\'' +
				", passwordConfirmation='" + passwordConfirmation + '\'' +
				", oldPassword='" + oldPassword + '\'' +
				'}';
	}
}
