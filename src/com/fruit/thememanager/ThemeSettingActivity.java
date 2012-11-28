package com.fruit.thememanager;

import java.util.ArrayList;

import com.fruit.thememanager.helper.ThemeInfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ThemeSettingActivity extends Activity implements View.OnClickListener, OnItemClickListener {

	static final String TAG = "ThemeSettingActivity";
	private static final int REQUEST_THEME_ITEM = 60;
	private static final int REQUEST_THEME_CUSTOM = 70;
	//private static final int REQUEST_THEME_ONLINE = 80;
	
	private GridView mThemeGrid;
	
	ThemeManager mThemeMgr;
	ThemeShowAdapter mAdapter;
	ArrayList<ThemeInfo> mArrayListTheme;
	String mThemeName;
	boolean mbForceUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_main);
		initViews();
		Log.i(TAG, "onCreate");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String theme = mThemeMgr.getCurrentTheme(ThemeUtils.CATEGORY_THEME);
		Log.i(TAG, "onResume mThemeName="+mThemeName+", theme="+theme);
		/*if(mbForceUpdate || !theme.equals(mThemeName))*/ {
			mThemeName = new String(theme);
			mArrayListTheme = mThemeMgr.getAllTheme(ThemeUtils.CATEGORY_THEME);
			mAdapter = new ThemeShowAdapter(this, mArrayListTheme, ThemeUtils.THUMBIMG_THEME); 
			mThemeGrid.setAdapter(mAdapter);
			mbForceUpdate = false;
		}
	}
	

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == ThemeUtils.ACTIVITY_RESULT_DELETE){
			Log.w(TAG, "onActivityResult theme delete");
			mbForceUpdate = true;
		}
    }
    
	private void initViews() {
		// TODO Auto-generated method stub
		mThemeMgr = ThemeManager.getInstance(this);
		mThemeName = new String(mThemeMgr.getCurrentTheme(ThemeUtils.CATEGORY_THEME));
		mArrayListTheme = mThemeMgr.getAllTheme(ThemeUtils.CATEGORY_THEME);
		mAdapter = new ThemeShowAdapter(this, mArrayListTheme, ThemeUtils.THUMBIMG_THEME);
		
		TextView customTheme = (TextView)findViewById(R.id.theme_custom_current_theme);
		customTheme.setOnClickListener(this);
		
		TextView onlineTheme = (TextView)findViewById(R.id.theme_online);
		onlineTheme.setOnClickListener(this);
		
		mThemeGrid = (GridView) findViewById(R.id.theme_install);
		mThemeGrid.setAdapter(mAdapter);
		mThemeGrid.setOnItemClickListener(this);
		
		mbForceUpdate = false;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.theme_custom_current_theme){
			Intent intent = new Intent(this, ThemeCustomActivity.class);
			startActivitySafely(intent, REQUEST_THEME_CUSTOM);
			    
			overridePendingTransition(R.anim.push_in, R.anim.push_out);			
		} else if(v.getId()==R.id.theme_online){
			//Intent intent=new Intent("");
			//startActivitySafely(intent, REQUEST_THEME_ONLINE);			
			
			try{
				Intent intent = new Intent();
				ComponentName market = new ComponentName("com.vollo.Market", "com.vollo.Market.marketBrowser");
				intent.setComponent(market);
				intent.setAction(Intent.ACTION_VIEW);  
				//intent.setAction("android.intent.category.MIAN");
				intent.putExtra("type", 4);
				startActivity(intent);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			overridePendingTransition(R.anim.push_in, R.anim.push_out);	
		        
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		ThemeInfo info = (ThemeInfo) parent.getItemAtPosition(position);

		if(info.mPkgName != null){
			Intent intent = new Intent();
			intent.setAction(ThemeUtils.ACTION_APPLY_THEME);
			intent.setClass(this, ThemeApplyActivity.class);
			intent.putExtra(ThemeUtils.PACKAGE_NAME, info.mPkgName);
			intent.putExtra(ThemeUtils.THEME_TYPE, ThemeUtils.getThemeType(info.mThemeType));
			intent.putExtra(ThemeUtils.THEME_CATEGORY, ThemeUtils.CATEGORY_THEME);
			startActivitySafely(intent, REQUEST_THEME_ITEM);
		}
	}
	public void	startActivitySafely(Intent intent, int requestCode){
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        }
	}	
}