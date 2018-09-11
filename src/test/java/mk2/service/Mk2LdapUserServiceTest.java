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

/*
 * This file is part of MehlboxKonsole2.
 *
 *     MehlboxKonsole2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

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