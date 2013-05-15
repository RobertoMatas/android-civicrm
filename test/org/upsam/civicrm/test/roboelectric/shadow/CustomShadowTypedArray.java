package org.upsam.civicrm.test.roboelectric.shadow;

import android.content.res.TypedArray;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.shadows.ShadowTypedArray;

@Implements(TypedArray.class)
public class CustomShadowTypedArray extends ShadowTypedArray {

	@Implementation
	public int getResourceId(int index, int defValue) {
		return defValue;
	}
	
	@Implementation
	public float getDimension(int index, float defValue) {
		return defValue;
	}
}
