package mk2.mapper;

import mk2.model.Mk2User;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.HashSet;
import java.util.Set;

public class Mk2UserAttributesMapper implements AttributesMapper<Mk2User> {

	@Override
	public Mk2User mapFromAttributes(Attributes attributes) throws NamingException {
		Mk2User user = new Mk2User();

		Attribute mailAlternateAddressAttr = attributes.get("mailAlternateAddress");
		Set<String> mailAlternateAddress = new HashSet<>();

		if (mailAlternateAddressAttr != null && mailAlternateAddressAttr.size() > 0) {
			for (int i = 0; i < mailAlternateAddressAttr.size(); i++) {
				mailAlternateAddress.add((String) mailAlternateAddressAttr.get(i));
			}
		}

		user.setFirstName((String) attributes.get("givenName").get());
		user.setLastName((String) attributes.get("sn").get());
		user.setDisplayName((String) attributes.get("displayName").get());
		user.setAccountStatus((String) attributes.get("accountStatus").get());
		user.setEmailAddresses(mailAlternateAddress);

		return user;
	}
}
