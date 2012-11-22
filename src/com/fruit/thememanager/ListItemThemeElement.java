package com.fruit.thememanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItemThemeElement extends ListItem {

	private ImageView mElementIcon;
	private TextView mElementTitle;
	private TextView mElementValue;

	public ListItemThemeElement(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ListItemThemeElement(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ListItemThemeElement(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initViews(context, attrs, defStyle);
	}

	public void setElementValue(int resId) {
		mElementValue.setText(resId);
	}

	public void setElementValue(String text) {
		mElementValue.setText(text);
	}

	public void setElementValue(CharSequence text) {
		// TODO Auto-generated method stub
		mElementValue.setText(text);
	}
	
	private void initViews(Context context, AttributeSet attrs, int defStyle) {
		final LayoutInflater mInflater = LayoutInflater.from(context);

		View view = mInflater.inflate(R.layout.theme_custom_element, this, true);

		mElementIcon = (ImageView) view.findViewById(R.id.theme_list_item_element_icon);
		mElementTitle = (TextView) view.findViewById(R.id.theme_list_item_element_title);
		mElementValue = (TextView) view.findViewById(R.id.theme_list_item_element_value);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListItemThemeElement, defStyle, 0);

		Drawable icon = a.getDrawable(R.styleable.ListItemThemeElement_themeIcon);
		if (icon != null && mElementIcon != null) {
			mElementIcon.setImageDrawable(icon);
		}
		
		CharSequence title = a.getText(R.styleable.ListItemThemeElement_themeTitle);
		if (title != null && mElementTitle != null) {
			mElementTitle.setText(title);
		}

		CharSequence value = a.getText(R.styleable.ListItemThemeElement_themeValue);
		if (value != null && mElementValue != null) {
			mElementValue.setText(value);
		}

		a.recycle();
	}
}
