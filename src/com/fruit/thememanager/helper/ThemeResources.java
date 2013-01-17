package com.fruit.thememanager.helper;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public final class ThemeResources {

	private static final String TAG = "ShareResourceLoader";
    public static final int INVALID_INT = -999999;
	private Context mContext;
	private Resources mResources;
	private String mResPkgName;

	public ThemeResources(Context context, String packageName) throws NameNotFoundException {
		this.mContext = context.createPackageContext(packageName, 
				Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);

		if (mContext != null) {
			this.mResources = mContext.getResources();
			this.mResPkgName = packageName;
		}else{
			Log.w(TAG, "ShareResourceLoader create fail! packageName="+packageName);
		}
	}

	public final String loadString(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;
		if (res != null && pkgName != null) {
			int resId = res.getIdentifier(resName, "string", mResPkgName);

			if (resId > 0) {
				return res.getString(resId);
			}else{
				Log.w(TAG, "loadString fail! resName="+resName);
			}
		}
		return null;
	}

	public final Drawable loadDrawable(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;
		Drawable d = null;
		
		if (res != null && pkgName != null) {
			int resId = res.getIdentifier(resName, "drawable", mResPkgName);

			if (resId > 0) {				
				try {
					d = mResources.getDrawable(resId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					d = null;
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					d = null;
					Log.w(TAG, "loadDrawable fail! OutOfMemoryError");
				}
			}else{
				Log.w(TAG, "loadDrawable fail! resName="+resName);
			}
		}
		return d;
	}

	public final Bitmap loadBitmap(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;
		Bitmap bitmap = null;

		if (res != null && pkgName != null) {
			int resId = res.getIdentifier(resName, "drawable", mResPkgName);

			if (resId > 0) {
				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inDither = false;
				option.inPreferredConfig = Bitmap.Config.ARGB_8888;
				try {
					bitmap = BitmapFactory.decodeResource(res, resId, option);
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					bitmap = null;
					Log.w(TAG, "loadDrawable fail! OutOfMemoryError");
				}
			}else{
				Log.w(TAG, "loadDrawable fail! resName="+resName);
			}
			
		}
		return bitmap;
	}

	public final String[] loadStringArray(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;
		String[] array = null;

		if (res != null && pkgName != null) {
			int listId = res.getIdentifier(resName, "array", pkgName);
			if (listId > 0) {
				array = res.getStringArray(listId);
			}else{
				Log.w(TAG, "loadStringArray fail! resName="+resName);
			}
		}
		return array;
	}

	public final int loadColor(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;

		if (res != null && pkgName != null) {
			int resId = res.getIdentifier(resName, "color", mResPkgName);

			if (resId > 0) {
				return res.getColor(resId);
			}else{
				Log.w(TAG, "loadColor fail! resName="+resName);
			}
		}
		return INVALID_INT;
	}

	public final XmlResourceParser loadXml(String resName) {
		final Resources res = mResources;
		final String pkgName = mResPkgName;
		if (res != null && pkgName != null) {
			int resId = res.getIdentifier(resName, "xml", mResPkgName);

			if (resId > 0) {
				return mResources.getXml(resId);
			}else{
				Log.w(TAG, "loadXml fail! resName="+resName);
			}
		}
		return null;
	}
}