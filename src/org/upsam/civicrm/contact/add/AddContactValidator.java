package org.upsam.civicrm.contact.add;

public interface AddContactValidator {

	boolean isValidEmail(String email);
	
	boolean isValidName(String name);
	
	boolean isValidPhone(String phone);
}
