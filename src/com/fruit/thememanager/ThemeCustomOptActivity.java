package com.fruit.thememanager;

import java.util.ArrayList;

import com.fruit.thememanager.helper.ThemeInfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeCustomOptActivity extends Activity
	implements View.OnClickListener, AdapterView.OnItemClickListener{

	static final String TAG = "ThemeCustomOptActivity";
	private static final int REQUEST_THEME_ITEM = 60;
	private static final int REQUEST_THEME_MORE = 70;
	
	private GridView mThemeGrid;
	
	private int mThemeCategory;
	private BaseAdapter mAdapter;
	private ArrayList<ThemeInfo> mArrayListTheme;
	private String mThemeName;
	private ThemeManager mThemeMgr ;
	private boolean mbForceUpdate;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_custom_opt);
		initViews();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String theme = mThemeMgr.getCurrentTheme(mThemeCategory);
		Log.d(TAG, "onResume mThemeName="+mThemeName+", theme="+theme);
		if(mbForceUpdate || !theme.equals(mThemeName)){
			mThemeName = new String(theme);
			mArrayListTheme = mThemeMgr.getAllTheme(mThemeCategory);
			mAdapter = getThemeAdapter(mThemeCategory, mArrayListTheme);
			mThemeGrid.setAdapter(mAdapter);
			
			mbForceUpdate = false;
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == REQUEST_THEME_ITEM){
    		if(resultCode == ThemeUtils.ACTIVITY_RESULT_DELETE){
    			Log.w(TAG, "onActivityResult theme delete");
    			mbForceUpdate = true;
    			
    			setResult(ThemeUtils.ACTIVITY_RESULT_DELETE);
    			this.finish();
    		}
    	}
    }
    

	private void initViews() {
        final Intent intent = getIntent();
        String title = null;
        
		if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            title = intent.getStringExtra(Intent.EXTRA_TITLE);
        } 
		if (intent.hasExtra(ThemeUtils.THEME_CATEGORY)) {
			mThemeCategory = intent.getIntExtra(ThemeUtils.THEME_CATEGORY, 
					ThemeUtils.CATEGORY_LAUNCHERICON);
        }
        
		if(title == null || mThemeCategory == 0){
			Log.e(TAG, "initViews get title , themeCategory error! ");
			return;
		}
		
		//get adpter
		mThemeMgr = ThemeManager.getInstance(this);
		mThemeName = new String(mThemeMgr.getCurrentTheme(mThemeCategory));
		mArrayListTheme = mThemeMgr.getAllTheme(mThemeCategory);

		mAdapter = getThemeAdapter(mThemeCategory, mArrayListTheme);
		
		if(mAdapter == null){
			Log.e(TAG, "initViews create adpter error! ");
			return;
		}
		
		//set title
		((TextView)findViewById(R.id.theme_custom_title)).setText(title);
		
		//hide or show button more
		if(mThemeCategory == ThemeUtils.CATEGORY_WALLPAPER){
			TextView textMore = (TextView) findViewById(R.id.theme_custom_more);
			textMore.setText(R.string.theme_wallpaper_more);
			textMore.setOnClickListener(this);
		}else{
			findViewById(R.id.theme_custom_more_bar).setVisibility(View.GONE);
		}
		
		mThemeGrid = (GridView) findViewById(R.id.theme_custom_show);
		mThemeGrid.setAdapter(mAdapter);
		mThemeGrid.setOnItemClickListener(this);
		
		mbForceUpdate = false;
	}

	
	private BaseAdapter getThemeAdapter(int category, ArrayList<ThemeInfo> listThemeInfo){
		BaseAdapter adpter = null;
		
		switch (category) {
		case ThemeUtils.CATEGORY_LAUNCHERICON:
			adpter = new ThemeShowAdapter(this, listThemeInfo, 
					ThemeUtils.THUMBIMG_LAUNCHERICON);
			break;
		case ThemeUtils.CATEGORY_LOCKSCREEN:
			adpter = new ThemeShowAdapter(this, listThemeInfo, 
					ThemeUtils.THUMBIMG_LOCKSCREEN);
			break;
		case ThemeUtils.CATEGORY_WALLPAPER:
			adpter = new ThemeShowImageAdapter(this, listThemeInfo, 
					ThemeUtils.THUMBIMG_WALLPAPER);
			break;
		case ThemeUtils.CATEGORY_LOCKWALLPAPER:
			adpter = new ThemeShowImageAdapter(this, listThemeInfo, 
					ThemeUtils.THUMBIMG_LOCKWALLPAPER);
			break;			
		default:
			break;
		}
		
		return adpter;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.theme_custom_more){
			if(mThemeCategory == ThemeUtils.CATEGORY_WALLPAPER){
		        final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
		        Intent chooser = Intent.createChooser(pickWallpaper,
		                getText(R.string.chooser_wallpaper));
		        startActivitySafely(chooser, REQUEST_THEME_MORE);
			}
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
			intent.putExtra(ThemeUtils.THEME_CATEGORY, mThemeCategory);
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