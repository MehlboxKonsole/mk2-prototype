package mk2.service;

import mk2.model.Mk2Domain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Mk2LdapUserServiceTest {

	@Mock
	Mk2LdapDomainService domainService;

	@InjectMocks
	Mk2LdapUserService userService;

	@Test
	public void userWithDomainsShouldReturnTrue() {
		String userName = "user.with.domains";

		List<Mk2Domain> intermediateResult = new ArrayList<>(2);
		intermediateResult.add(new Mk2Domain());
		intermediateResult.add(new Mk2Domain());

		when(domainService.getDomainsForUser(any())).thenReturn(intermediateResult);

		boolean actualResult = userService.hasUserDomains(userName);

		assertThat(actualResult, is(equalTo(true)));
	}

	@Test
	public void userWithoutDomainsShouldReturnFalse() {
		String userName = "user.without.domains";

		List<Mk2Domain> intermdiateResult = new ArrayList<>(0);
		when(domainService.getDomainsForUser(any())).thenReturn(intermdiateResult);

		boolean actualResult = userService.hasUserDomains(userName);

		assertThat(actualResult, is(equalTo(false)));
	}

}