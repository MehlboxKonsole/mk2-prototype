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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

		List<Mk2Domain> intermediateResult = generateAndMockIntermediateResult(1);
		List<Mk2Domain> actualResult = domainService.getDomainsForUser(userWithDomain);

		assertThat(actualResult, containsInAnyOrder(intermediateResult.toArray()));

	}

	@Test
	public void nullUsernameReturnEmptyResultAndWritesLogWarning() {
		List<Mk2Domain> actualResult = domainService.getDomainsForUser(null);

		assertThat(actualResult, is(empty()));

		verify(log, times(1)).warn(anyString());
	}

	@Test
	public void userWithMultipleDomainsProducesResultSetWithAllDomains() {
		String userName = "domain.junie";

		List<Mk2Domain> intermediateResult = generateAndMockIntermediateResult(5);
		List<Mk2Domain> actualResult = domainService.getDomainsForUser(userName);

		assertThat(actualResult, containsInAnyOrder(intermediateResult.toArray()));
	}

	private List<Mk2Domain> generateAndMockIntermediateResult(int numberOfDomains) {
		List<Mk2Domain> intermediateResult = new ArrayList<>(3);

		for (int i = 0; i < numberOfDomains; i++) {
			Mk2Domain fakeDomain = new Mk2Domain();
			intermediateResult.add(fakeDomain);
		}

		when(ldapTemplate.find(any(), eq(Mk2Domain.class))).thenReturn(intermediateResult);

		return intermediateResult;
	}


}
