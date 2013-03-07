package org.upsam.civicrm.contact.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.upsam.civicrm.contact.model.email.ListEmails;
import org.upsam.civicrm.contact.model.telephone.ListPhones;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "contact_type")
@JsonSubTypes(
		{ 
			@Type(value = Individual.class, name = "Individual"), 
			@Type(value = Organization.class, name = "Organization"), 
			@Type(value = Household.class, name = "Household") 
		})
public abstract class Contact {
	/**
	 * Contact ID
	 */
	@JsonProperty("contact_id")
	private int id;
	/**
	 * Nombre para mostrar
	 */
	@JsonProperty("display_name")
	private String name;
	/**
	 * Nick name
	 */
	@JsonProperty("nick_name")
	private String nick;
	/**
	 * Imagen del contacto
	 */
	@JsonProperty("image_URL")
	private String image;
	/**
	 * Subtipo de contacto
	 */
	@JsonProperty("contact_sub_type")
	private String subType;
	/**
	 * Lista de emails del contacto
	 */
	@JsonIgnore
	private ListEmails emails;
	/**
	 * Lista de teléfonos del contacto
	 */
	@JsonIgnore
	private ListPhones phones;
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick
	 *            the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @param subType
	 *            the subType to set
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * @return the emails
	 */
	public ListEmails getEmails() {
		return emails;
	}

	/**
	 * @param emails the emails to set
	 */
	public void setEmails(ListEmails emails) {
		this.emails = emails;
	}

	/**
	 * @return the phones
	 */
	public ListPhones getPhones() {
		return phones;
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(ListPhones phones) {
		this.phones = phones;
	}

}
