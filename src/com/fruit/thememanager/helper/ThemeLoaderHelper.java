package com.fruit.thememanager.helper;

import com.fruit.thememanager.ThemeUtils.ThemeType;

import android.content.Context;
import android.util.Log;

public class ThemeLoaderHelper {

	public static ThemeResourceLoader getThemeResLoader(Context context, ThemeInfo themeInfo) {
		ThemeResourceLoader loader = null;

		switch (themeInfo.mThemeType) {
		case THEME_DEFAULT:
			loader = new ThemeResourceDefaultLoader(context, themeInfo);
			break;
		case THEME_CUSTOM:
			loader = new ThemeResourceCustomLoader(context, themeInfo);
			break;
		default:
			break;
		}
		return loader;
	}

	public static ThemeInfoLoader getThemeInfoLoader(ThemeType themeType) {
		ThemeInfoLoader loader = null;

		Log.d("ThemeLoaderHelper", "Theme type=" + themeType);
		switch (themeType) {
		case THEME_DEFAULT:
			loader = ThemeInfoDefaultLoader.getInstance();
			break;
		case THEME_CUSTOM:
			loader = ThemeInfoCustomLoader.getInstance();
			break;
		default:
			break;
		}
		return loader;
	}
}