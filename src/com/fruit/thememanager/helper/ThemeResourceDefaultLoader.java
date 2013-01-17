package com.fruit.thememanager.helper;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ThemeResourceDefaultLoader implements ThemeResourceLoader {

	@SuppressWarnings("unused")
	private ThemeInfo mThemeInfo;
	private ThemeResources mThemeRes;

	public ThemeResourceDefaultLoader(Context context, ThemeInfo info) {
		this.mThemeInfo = info;
		try {
			mThemeRes = new ThemeResources(context, context.getPackageName());
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Drawable loadDrawable(String resName) {
		// TODO Auto-generated method stub
		Drawable d = null;
		
		try {
			d = mThemeRes.loadDrawable(resName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			d=null;
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			d = null;
		}
		
		return d;
	}

	@Override
	public Bitmap loadBitmap(String resName) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		try {
			bmp = mThemeRes.loadBitmap(resName);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			bmp = null;
		}
		return bmp;
	}

	@Override
	public String loadString(String resName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int loadColor(String resName) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public String[] loadStringArray(String resName) {
		// TODO Auto-generated method stub
		return mThemeRes.loadStringArray(resName);
	}
}