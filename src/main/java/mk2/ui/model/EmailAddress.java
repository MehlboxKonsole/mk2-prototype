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

public class EmailAddress {

	private String localPart;
	private String domain;

	public EmailAddress() {
	}

	public EmailAddress(String localPart, String domain) {
		this.localPart = localPart;
		this.domain = domain;
	}

	public String getLocalPart() {
		return localPart;
	}

	public void setLocalPart(String localPart) {
		this.localPart = localPart;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EmailAddress that = (EmailAddress) o;

		if (getLocalPart() != null ? !getLocalPart().equals(that.getLocalPart()) : that.getLocalPart() != null)
			return false;
		return getDomain() != null ? getDomain().equals(that.getDomain()) : that.getDomain() == null;

	}

	@Override
	public int hashCode() {
		int result = getLocalPart() != null ? getLocalPart().hashCode() : 0;
		result = 31 * result + (getDomain() != null ? getDomain().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return localPart + "@" + domain;
	}
}
