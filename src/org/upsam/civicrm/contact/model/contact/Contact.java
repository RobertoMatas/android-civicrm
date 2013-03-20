package org.upsam.civicrm.contact.model.contact;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "contact_type")
@JsonSubTypes({ @Type(value = Individual.class, name = "Individual"), @Type(value = Organization.class, name = "Organization"), @Type(value = Household.class, name = "Household") })
public abstract class Contact implements Parcelable {
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
	 * Email primario del contacto
	 */
	private String email;
	/**
	 * Teléfono primario del contacto
	 */
	private String phone;
	/*
	 * Preferencias de comunicación 
	 */
	@JsonProperty("do_not_email")
	private char doNotEmail;
	@JsonProperty("do_not_phone")
	private char doNotPhone;
	@JsonProperty("do_not_mail")
	private char doNotMail;
	@JsonProperty("do_not_sms")
	private char doNotSms;
	@JsonProperty("do_not_trade")
	private char doNotTrade;
	
	/* 
	 * Datos demográficos
	 */
	private String gender;
	@JsonProperty("birth_date")
	private String birthDate;
	@JsonProperty("is_deceased")
	private char isDeceased;
	@JsonProperty("deceased_date")
	private String deceasedDate;

	/**
	 * 
	 */
	public Contact() {
		super();
	}

	/**
	 * 
	 */
	public Contact(Parcel in) {
		this.id = in.readInt();
		String[] data = new String[6];
		in.readStringArray(data);
		this.name = data[0];
		this.image = data[1];
		this.nick = data[2];
		this.phone = data[3];
		this.email = data[4];		
		this.subType = data[5];
	}

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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * @return the doNotEmail
	 */
	public char getDoNotEmail() {
		return doNotEmail;
	}

	/**
	 * @param doNotEmail the doNotEmail to set
	 */
	public void setDoNotEmail(char doNotEmail) {
		this.doNotEmail = doNotEmail;
	}

	/**
	 * @return the doNotPhone
	 */
	public char getDoNotPhone() {
		return doNotPhone;
	}

	/**
	 * @param doNotPhone the doNotPhone to set
	 */
	public void setDoNotPhone(char doNotPhone) {
		this.doNotPhone = doNotPhone;
	}

	/**
	 * @return the doNotMail
	 */
	public char getDoNotMail() {
		return doNotMail;
	}

	/**
	 * @param doNotMail the doNotMail to set
	 */
	public void setDoNotMail(char doNotMail) {
		this.doNotMail = doNotMail;
	}

	/**
	 * @return the doNotSms
	 */
	public char getDoNotSms() {
		return doNotSms;
	}

	/**
	 * @param doNotSms the doNotSms to set
	 */
	public void setDoNotSms(char doNotSms) {
		this.doNotSms = doNotSms;
	}

	/**
	 * @return the doNotTrade
	 */
	public char getDoNotTrade() {
		return doNotTrade;
	}

	/**
	 * @param doNotTrade the doNotTrade to set
	 */
	public void setDoNotTrade(char doNotTrade) {
		this.doNotTrade = doNotTrade;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the isDeceased
	 */
	public char getIsDeceased() {
		return isDeceased;
	}

	/**
	 * @param isDeceased the isDeceased to set
	 */
	public void setIsDeceased(char isDeceased) {
		this.isDeceased = isDeceased;
	}

	/**
	 * @return the deceasedDate
	 */
	public String getDeceasedDate() {
		return deceasedDate;
	}

	/**
	 * @param deceasedDate the deceasedDate to set
	 */
	public void setDeceasedDate(String deceasedDate) {
		this.deceasedDate = deceasedDate;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeStringArray(new String[] { this.name, this.image, this.nick, this.phone, this.email, this.subType });
	}

}
