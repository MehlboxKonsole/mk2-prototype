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
