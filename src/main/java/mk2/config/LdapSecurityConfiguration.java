package mk2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

@Configuration
public class LdapSecurityConfiguration {

    private static final String LDAP_GROUP_SEARCH_BASE = "ou=Groups";
    private static final String LDAP_GROUP_SEARCH_FILTER = "uniqueMember={0}";

    /*
        FIXME: Check if we need the Group Search here or where it should go now...

        The old code used this:
        //				.groupSearchBase(LDAP_GROUP_SEARCH_BASE)
        //				.groupSearchFilter(LDAP_GROUP_SEARCH_FILTER)

     */
    @Bean
    AuthenticationManager ldapAuthenticationManager(
            BaseLdapPathContextSource contextSource) {

        LdapBindAuthenticationManagerFactory factory =
                new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=Users");
        factory.setUserDetailsContextMapper(new PersonContextMapper());
        return factory.createAuthenticationManager();
    }

}