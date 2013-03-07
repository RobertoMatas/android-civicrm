package org.upsam.civicrm.contact.detail;

import java.io.ByteArrayInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ContactImageRequest extends SpringAndroidSpiceRequest<Bitmap> {

	private final String imageUrl;
	
	public ContactImageRequest(String imageUrl) {
		super(Bitmap.class);
		this.imageUrl = imageUrl;
	}

	@Override
	public Bitmap loadDataFromNetwork() throws Exception {
		byte[] img = getRestTemplate().getForObject(imageUrl, byte[].class);
		return img != null ? BitmapFactory.decodeStream(new ByteArrayInputStream(img)) : null;
	}

}
