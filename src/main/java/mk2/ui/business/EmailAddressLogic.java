package mk2.ui.business;


import mk2.exception.EmailAddressNotAssignedException;
import mk2.service.EmailAddressService;
import mk2.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailAddressLogic {

	@Autowired
	UserUtil userUtil;

	@Autowired
	EmailAddressService emailAddressService;

	public void delete(String email) throws EmailAddressNotAssignedException {
		String userFullDn = userUtil.getCurrentUsersDn();

		emailAddressService.removeEmailAddressFromUser(email, userFullDn);
	}

}
