package com.fruit.thememanager.helper;

import java.util.ArrayList;

import com.fruit.thememanager.R;
import com.fruit.thememanager.ThemeUtils;
import com.fruit.thememanager.ThemeUtils.ThemeType;
import android.content.Context;
import android.content.res.Resources;

public final class ThemeInfoDefaultLoader implements ThemeInfoLoader {

	private static ThemeInfoDefaultLoader sInstance;

	public static ThemeInfoDefaultLoader getInstance() {
		if (sInstance == null) {
			sInstance = new ThemeInfoDefaultLoader();
		}
		return sInstance;
	}

	@Override
	public final ThemeInfo loadThemeInfo(Context context, String themePkgName) {
		// TODO Auto-generated method stub
		if(!themePkgName.equals(ThemeUtils.DEFAULT_THEME_PACKAGENAME)){
			return null;
		}
		
		final Resources res = context.getResources();
		ThemeInfo info = new ThemeInfo();

		info.mPkgName = new String(ThemeUtils.DEFAULT_THEME_PACKAGENAME); 
		info.mThemeName = res.getString(R.string.theme_name_default);
		info.mThemeAuthor = res.getString(R.string.theme_author_default);
		info.mThemeDesc = res.getString(R.string.theme_description_default);
		info.mThemeType = ThemeType.THEME_DEFAULT;
		info.mThemeVer = Integer.parseInt(res.getString(R.string.theme_version_default));
		info.mThemeSupportVer = Integer.parseInt(res.getString(R.string.theme_support_version_default));

		return info;
	}

	@Override
	public final ArrayList<ThemeInfo> loadInstalledThemes(Context context, String themeCategory) {
		// TODO Auto-generated method stub
		ArrayList<ThemeInfo> list = new ArrayList<ThemeInfo>(1);
		
		list.add(loadThemeInfo(context, ThemeUtils.DEFAULT_THEME_PACKAGENAME));
		return list;
	}
}