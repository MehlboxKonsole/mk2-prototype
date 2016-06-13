package mk2.explore_apis;


import mk2.Mk2Application;
import mk2.config.LdapConfig;
import mk2.config.WebSecurityConfig;
import mk2.model.Mk2User;
import mk2.service.Mk2LdapUserService;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.naming.ldap.LdapName;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Mk2Application.class, WebSecurityConfig.class, LdapConfig.class})
@WebAppConfiguration
public class AddEmailAddressTest {

	@Autowired
	LdapTemplate ldapTemplate;

	@Autowired
	Mk2LdapUserService userService;

	@Test
	public void addAlternateMailAddressEntry() {
		LocalDateTime now = LocalDateTime.now();
		StringBuilder emailAddress = new StringBuilder();
		emailAddress.append("foo_")
				.append(now.getDayOfMonth())
				.append(now.getMonthValue())
				.append(now.getYear())
				.append(now.getHour())
				.append(now.getMinute())
				.append(now.getSecond())
				.append("@bar.local");
		String newEmailAddress = emailAddress.toString();

		LdapName userDn = LdapNameBuilder.newInstance()
				.add("dc", "eu")
				.add("dc", "e-mehlbox")
				.add("ou", "Users")
				.add("ou", "internal")
				.add("cn", "holger.steinhauer.test@e-mehlbox.eu")
				.build();

		Mk2User user = userService.findByDn(userDn.toString());

		user.addEmailAddress(newEmailAddress);

		ldapTemplate.update(user);

		Mk2User freshUser = userService.findByDn(userDn.toString());;

		assertThat(freshUser.getEmailAddresses(), hasItem(newEmailAddress));
	}

}
