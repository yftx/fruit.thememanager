/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fruit.thememanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import com.fruit.thememanager.helper.ThemeInfo;

/**
 * GridView adapter to show the list of applications and shortcuts
 */
public class ThemeShowImageAdapter extends ArrayAdapter<ThemeInfo> {

    private final LayoutInflater mInflater;
    private final ThemeManager mThemeMgr;
    private final Context mContext;
    private final String mThumbImgName;

    public ThemeShowImageAdapter(Context context, ArrayList<ThemeInfo> apps, String thumbImgName) {
        super(context, 0, apps);
        mInflater = LayoutInflater.from(context);
        mThemeMgr = ThemeManager.getInstance(context);
        mContext = context;
        mThumbImgName = new String(thumbImgName);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ThemeInfo info = getItem(position);

        if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.theme_list_item_wallpaper, parent, false);
            convertView.setFocusable(false);
        }

        ImageView themePreview = (ImageView) convertView.findViewById(R.id.theme_item_preview);
        ImageView themeStatus = (ImageView) convertView.findViewById(R.id.theme_item_status);
        if(info != null){
			Drawable thumbRes = mThemeMgr.loadDrawable(info, mThumbImgName);
			if(thumbRes != null){
				themePreview.setImageDrawable(thumbRes);
			}
            
			if(info.mIsCurrent){
				themeStatus.setVisibility(View.VISIBLE);
			}else{
				themeStatus.setVisibility(View.GONE);
			}
			
            Drawable thumbDrawable = themePreview.getDrawable();
            if (thumbDrawable != null) {
                thumbDrawable.setDither(true);
            } else {
            }
        }

        return convertView;
    }
}