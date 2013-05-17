package org.upsam.civicrm.contact.add;

import org.apache.commons.lang3.StringUtils;

public class AddContactValidatorImpl implements AddContactValidator {

	private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public boolean isValidEmail(String email) {
		if (StringUtils.isNotBlank(email))
			return email.matches(emailRegExp);
		return true;
	}

	@Override
	public boolean isValidName(String name) {
		return StringUtils.isNotBlank(name);
	}

	@Override
	public boolean isValidPhone(String phone) {
		if (StringUtils.isNotBlank(phone))
			return StringUtils.isNumeric(phone);
		return true;
	}

}
