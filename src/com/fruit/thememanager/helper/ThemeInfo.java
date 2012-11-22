package com.fruit.thememanager.helper;

import com.fruit.thememanager.ThemeUtils.ThemeType;

import android.content.Intent;

public final class ThemeInfo {
	public String mPkgName;
	public String mThemeName;
	public String mThemeAuthor;
	public String mThemeDesc;
	public ThemeType mThemeType;
	public int mThemeVer;
	public int mThemeSupportVer;
	public boolean mIsCurrent;
	public Intent mIntent;

	public ThemeInfo(){
		mIsCurrent = false;
	}
	
	public ThemeInfo(ThemeInfo item) {
		// TODO Auto-generated constructor stub
		if(item.mPkgName != null){
			mPkgName = new String(item.mPkgName);
		}
		if(item.mThemeName != null){
			mThemeName = new String(item.mThemeName);
		}
		if(item.mThemeAuthor != null){
			mThemeAuthor = new String(item.mThemeAuthor);
		}
		if(item.mThemeDesc != null){
			mThemeDesc = new String(item.mThemeDesc);
		}
		if(item.mIntent != null){
			mIntent = new Intent(item.mIntent);
		}
		
		mThemeType = item.mThemeType;
		mThemeSupportVer = item.mThemeSupportVer;
		mIsCurrent = item.mIsCurrent;
	}

	@Override
	public final String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder("packageName:");
		sb.append(mPkgName).append("--").append(mThemeName);

		return sb.toString();
	}
}