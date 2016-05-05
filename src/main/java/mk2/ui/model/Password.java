package mk2.ui.model;

import java.io.Serializable;

public class Password implements Serializable {

	private String password;
	private String passwordConfirmation;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Password password1 = (Password) o;

		if (getPassword() != null ? !getPassword().equals(password1.getPassword()) : password1.getPassword() != null)
			return false;
		return getPasswordConfirmation() != null ? getPasswordConfirmation().equals(password1.getPasswordConfirmation()) : password1.getPasswordConfirmation() == null;

	}

	@Override
	public int hashCode() {
		int result = getPassword() != null ? getPassword().hashCode() : 0;
		result = 31 * result + (getPasswordConfirmation() != null ? getPasswordConfirmation().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Password{" +
				"password='" + password + '\'' +
				", passwordConfirmation='" + passwordConfirmation + '\'' +
				'}';
	}
}
