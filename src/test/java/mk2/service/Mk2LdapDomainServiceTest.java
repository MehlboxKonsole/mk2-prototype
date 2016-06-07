package mk2.service;

import mk2.model.Mk2Domain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.ldap.core.LdapTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class Mk2LdapDomainServiceTest {

	@Mock
	LdapTemplate ldapTemplate;

	@Mock
	Logger log;

	@InjectMocks
	Mk2LdapDomainService domainService;

	@Test
	public void emptySetIsReturnedIfUserOwnsNoDomain() {
		String userWithoutDomains = "example.user.without.domains";
		List<Mk2Domain> actualResult = domainService.getDomainsForUser(userWithoutDomains);

		assertThat(actualResult, is(empty()));
	}

	@Test
	public void userWithOneDomainReturnsSetWithOneEntry() {
		String userWithDomain = "example.user.with.one.domain";


		Mk2Domain fakeDomain = new Mk2Domain();
		List<Mk2Domain> intermediateResult = new ArrayList<>();
		intermediateResult.add(fakeDomain);
		when(ldapTemplate.find(any(), eq(Mk2Domain.class))).thenReturn(intermediateResult);

		List<Mk2Domain> actualResult = domainService.getDomainsForUser(userWithDomain);

		assertThat(actualResult, hasItem(fakeDomain));

	}

	@Test
	public void nullUsernameReturnEmptyResultAndWritesLogWarning() {
		String nullUserName = null;

		List<Mk2Domain> actualResult = domainService.getDomainsForUser(nullUserName);

		assertThat(actualResult, is(empty()));

		verify(log, times(1)).warn(anyString());
	}

	@Test
	public void userWithMultipleDomainsProducesResultSetWithAllDomains() {
		String userName = "domain.junie";

		List<Mk2Domain> intermediateResult = new ArrayList<>(3);
		Mk2Domain fakeDomain1 = new Mk2Domain();
		Mk2Domain fakeDomain2 = new Mk2Domain();
		intermediateResult.add(fakeDomain1);
		intermediateResult.add(fakeDomain2);

		when(ldapTemplate.find(any(), eq(Mk2Domain.class))).thenReturn(intermediateResult);

		List<Mk2Domain> actualResult = domainService.getDomainsForUser(userName);

		assertThat(actualResult, hasItems(fakeDomain1, fakeDomain2));
	}


}
