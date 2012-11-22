package com.fruit.thememanager.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public abstract interface ThemeResourceLoader {

	public abstract Drawable loadDrawable(String resName);
	public abstract Bitmap loadBitmap(String resName);
	public abstract String loadString(String resName);
	public abstract int loadColor(String resName);
	public abstract String[] loadStringArray(String resName);
}