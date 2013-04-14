package org.upsam.civicrm.contact.model.contact;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactSummary implements Parcelable {
	/**
	 * Contact ID
	 */
	//@JsonProperty("contact_id")
	private int id;
	/**
	 * Nombre para mostrar
	 */
	@JsonProperty("display_name")
	private String name;
	/**
	 * Tipo de contaco
	 */
	@JsonProperty("contact_type")
	private String type;
	/**
	 * Subtipo de contaco
	 */
	@JsonProperty("contact_sub_type")
	private String subType;
	
	public static final Parcelable.Creator<ContactSummary> CREATOR = new Creator<ContactSummary>() {
		
		@Override
		public ContactSummary[] newArray(int size) {
			return new ContactSummary[size];
		}
		
		@Override
		public ContactSummary createFromParcel(Parcel source) {
			return new ContactSummary(source);
		}
	};
	/**
	 * 
	 */
	public ContactSummary() {
		super();
	}
	/**
	 * 
	 * @param source
	 */
	public ContactSummary(Parcel source) {
		this.id = source.readInt();
		String[] data = new String[3];
		source.readStringArray(data);
		this.name = data[0];
		this.type = data[1];
		this.subType = data[2];
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeStringArray(new String[]{this.name, this.type, this.subType});
	}
}
