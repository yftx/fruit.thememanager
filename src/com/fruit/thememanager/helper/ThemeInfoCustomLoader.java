package com.fruit.thememanager.helper;

import java.util.ArrayList;
import java.util.Iterator;

import com.fruit.thememanager.ThemeUtils;
import com.fruit.thememanager.ThemeUtils.ThemeType;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class ThemeInfoCustomLoader implements ThemeInfoLoader {

	private static ThemeInfoCustomLoader sInstance;

	public static ThemeInfoCustomLoader getInstance() {
		if (sInstance == null) {
			sInstance = new ThemeInfoCustomLoader();
		}
		return sInstance;
	}

	@Override
	public ThemeInfo loadThemeInfo(Context context, String themePkgName) {
		// TODO Auto-generated method stub
		ThemeInfo info = null;
		try {
			ThemeResources themeRes = new ThemeResources(context, themePkgName);
			info = new ThemeInfo();

			info.mPkgName = themePkgName;
			info.mThemeName = themeRes.loadString("theme_name");
			info.mThemeAuthor = themeRes.loadString("theme_author");
			info.mThemeDesc = themeRes.loadString("theme_description");
			info.mThemeType = ThemeType.THEME_CUSTOM;
			info.mThemeVer = Integer.parseInt(themeRes.loadString("theme_version"));
			info.mThemeSupportVer = Integer.parseInt(themeRes.loadString("theme_support_version"));
		} catch (Exception e) {
			e.printStackTrace();
			info = null;
		}
		return info;
	}

	@Override
	public ArrayList<ThemeInfo> loadInstalledThemes(Context context, String themeCategory) {
		// TODO Auto-generated method stub
		final PackageManager pkgManager = context.getPackageManager();
		Intent intent = new Intent(ThemeUtils.ACTION_PICK_THEME);
		intent.addCategory(themeCategory);
		Iterator<ResolveInfo> iterator =
			pkgManager.queryIntentActivities(intent, 0).iterator();
		ArrayList<ThemeInfo> list = new ArrayList<ThemeInfo>();

		while (iterator.hasNext()) {
			ResolveInfo info = iterator.next();
			ThemeInfo themeInfo = loadThemeInfo(context, info.activityInfo.packageName);
			if (themeInfo != null) {
				list.add(themeInfo);
			}
		}
		return list;
	}
}