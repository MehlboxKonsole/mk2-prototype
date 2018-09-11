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

package mk2.ui.model;

import java.io.Serializable;

public class Password implements Serializable {

	private String newPassword;

	private String passwordConfirmation;

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
