package org.upsam.civicrm.contact.model;

import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

public class Individual extends Contact {
	/**
	 * 
	 */
	@JsonProperty("current_employer")
	private String currentEmployer;
	/**
	 * 
	 */
	public Individual() {
		super();
	}
	/**
	 * 
	 * @param in
	 */
	public Individual(Parcel in) {
		super(in);
		this.currentEmployer = in.readString();
	}

	public static final Parcelable.Creator<Individual> CREATOR = new Creator<Individual>() {
		
		@Override
		public Individual[] newArray(int size) {
			return new Individual[size];
		}
		
		@Override
		public Individual createFromParcel(Parcel source) {
			return new Individual(source);
		}
	};

	/**
	 * @return the currentEmployer
	 */
	public String getCurrentEmployer() {
		return currentEmployer;
	}

	/**
	 * @param currentEmployer the currentEmployer to set
	 */
	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}
	/* (non-Javadoc)
	 * @see org.upsam.civicrm.contact.model.Contact#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.currentEmployer);
	}
	
}
