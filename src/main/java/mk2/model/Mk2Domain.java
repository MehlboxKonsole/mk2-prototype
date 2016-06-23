package mk2.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.net.IDN;
import java.util.HashSet;
import java.util.Set;

@Entry(objectClasses = {"dNSDomain", "top"})
public final class Mk2Domain {

	@Id
	Name dn;

	@Attribute(name = "dc")
	String name;

	@Attribute(name = "associatedName")
	Set<String> owners;

	public Mk2Domain() {
		owners = new HashSet<>();
	}

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getName() {
		return name;
	}

	public String getName(boolean convertIdn) {
		if (convertIdn) {
			return IDN.toUnicode(name);
		}

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getOwners() {
		return owners;
	}

	public void setOwners(Set<String> owners) {
		this.owners = owners;
	}

	public void addOwner(String ownerDn) {
		owners.add(ownerDn);
	}

	public boolean isOwnedBy(String ownerDn) {
		return owners.contains(ownerDn);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Mk2Domain mk2Domain = (Mk2Domain) o;

		if (getDn() != null ? !getDn().equals(mk2Domain.getDn()) : mk2Domain.getDn() != null) return false;
		if (getName() != null ? !getName().equals(mk2Domain.getName()) : mk2Domain.getName() != null) return false;
		return getOwners() != null ? getOwners().equals(mk2Domain.getOwners()) : mk2Domain.getOwners() == null;

	}

	@Override
	public int hashCode() {
		int result = getDn() != null ? getDn().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		result = 31 * result + (getOwners() != null ? getOwners().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Mk2Domain{" +
				"dn=" + dn +
				", name='" + name + '\'' +
				", owners=" + owners +
				'}';
	}
}
