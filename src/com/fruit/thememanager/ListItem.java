package com.fruit.thememanager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ListItem extends LinearLayout {

	public ListItem(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ListItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFocusable(true);
		setClickable(true);
	}
}
