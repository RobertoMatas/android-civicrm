package org.upsam.civicrm.contact.model;

import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

public class Organization extends Contact {
	/**
	 * Sic code
	 */
	@JsonProperty("sic_code")
	private String sicCode;
	
	/**
	 * 
	 */
	public Organization() {
		super();
	}
	/**
	 * 
	 * @param in
	 */
	public Organization(Parcel in) {
		super(in);
		this.sicCode = in.readString();
	}

	public static final Parcelable.Creator<Organization> CREATOR = new Creator<Organization>() {
		
		@Override
		public Organization[] newArray(int size) {
			return new Organization[size];
		}
		
		@Override
		public Organization createFromParcel(Parcel source) {
			return new Organization(source);
		}
	};

	/**
	 * @return the sicCode
	 */
	public String getSicCode() {
		return sicCode;
	}

	/**
	 * @param sicCode
	 *            the sicCode to set
	 */
	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}
	/* (non-Javadoc)
	 * @see org.upsam.civicrm.contact.model.Contact#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.sicCode);
	}

}
