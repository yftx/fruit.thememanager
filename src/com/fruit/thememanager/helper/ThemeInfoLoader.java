package com.fruit.thememanager.helper;

import java.util.ArrayList;


import android.content.Context;

public abstract interface ThemeInfoLoader {

	public abstract ThemeInfo loadThemeInfo(Context context, String themePkgName);
	public abstract ArrayList<ThemeInfo> loadInstalledThemes(Context context, String themeCategory);
}