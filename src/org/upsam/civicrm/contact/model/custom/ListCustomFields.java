package org.upsam.civicrm.contact.model.custom;

import org.upsam.civicrm.contact.model.ListOfEntities;

public class ListCustomFields extends ListOfEntities<CustomField> {

	public CustomField getFieldById(int id) {
		for (CustomField customField : getValues()) {
			if (customField != null && customField.getId() == id) {
				return customField;
			}
		}
		return null;
	}
}
