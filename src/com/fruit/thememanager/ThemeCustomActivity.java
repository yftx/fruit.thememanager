package com.fruit.thememanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ThemeCustomActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "ThemeCustomActivity";
	
	private static final int REQUEST_THEME_ITEM = 60;
	
	private ListItemThemeElement mListItemWallpaper;
	private ListItemThemeElement mListItemAppIcon;
	private ListItemThemeElement mListItemLockWallpaper;
	private ListItemThemeElement mListItemLockScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.theme_custom);
		//initViews();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == REQUEST_THEME_ITEM){
    		if(resultCode == ThemeUtils.ACTIVITY_RESULT_DELETE){
    			setResult(ThemeUtils.ACTIVITY_RESULT_DELETE);
    			this.finish();
    		}
    	}
    }
    
	private void initViews() {
		// TODO Auto-generated method stub
		mListItemWallpaper = (ListItemThemeElement) findViewById(R.id.theme_wallpaper);
		mListItemAppIcon = (ListItemThemeElement) findViewById(R.id.theme_application_icon);
		mListItemLockWallpaper = (ListItemThemeElement) findViewById(R.id.theme_lock_wallpaper);
		mListItemLockScreen = (ListItemThemeElement) findViewById(R.id.theme_lockscreen);
		
		mListItemWallpaper.setOnClickListener(this);
		mListItemAppIcon.setOnClickListener(this);
		setItemElementName(mListItemAppIcon, ThemeUtils.CATEGORY_LAUNCHERICON);
		setItemElementName(mListItemWallpaper, ThemeUtils.CATEGORY_WALLPAPER);
		
		if(getResources().getBoolean(R.bool.config_function_lockscreen)){
			mListItemLockWallpaper.setOnClickListener(this);
			mListItemLockScreen.setOnClickListener(this);
			
			setItemElementName(mListItemLockScreen, ThemeUtils.CATEGORY_LOCKSCREEN);
			setItemElementName(mListItemLockWallpaper, ThemeUtils.CATEGORY_LOCKWALLPAPER);
		}else{
			findViewById(R.id.element_theme_lock).setVisibility(View.INVISIBLE);
			mListItemLockWallpaper.setVisibility(View.INVISIBLE);
			mListItemLockScreen.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setItemElementName(ListItemThemeElement itemView, int category){
		final PackageManager pkgMgr = getPackageManager();
		ThemeManager themeMgr = ThemeManager.getInstance(this);
		final String pkgName = themeMgr.getCurrentTheme(category);
		
		try {
			ApplicationInfo appInfo = pkgMgr.getApplicationInfo(pkgName, 0);
			if (appInfo != null) {
				itemView.setElementValue(appInfo.loadLabel(pkgMgr));
			}
			
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			
			itemView.setElementValue(R.string.theme_name_default);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		Intent intent = new Intent();
		intent.setClass(this, ThemeCustomOptActivity.class);
		if(id == R.id.theme_application_icon){
			intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.theme_app_icon));
			intent.putExtra(ThemeUtils.THEME_CATEGORY, ThemeUtils.CATEGORY_LAUNCHERICON);
		}else if(id == R.id.theme_wallpaper){
			intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.theme_wallpaper));
			intent.putExtra(ThemeUtils.THEME_CATEGORY, ThemeUtils.CATEGORY_WALLPAPER);
		}else if(id == R.id.theme_lockscreen){
			intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.theme_lockscreen));
			intent.putExtra(ThemeUtils.THEME_CATEGORY, ThemeUtils.CATEGORY_LOCKSCREEN);
		}else if(id == R.id.theme_lock_wallpaper){
			intent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.theme_lock_wallpaper));
			intent.putExtra(ThemeUtils.THEME_CATEGORY, ThemeUtils.CATEGORY_LOCKWALLPAPER);			
		}
		startActivityForResult(intent, REQUEST_THEME_ITEM);
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);	
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG,"onPause");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG,"onRestart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG,"onResume");
		initViews();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG,"onStart");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG,"onStop");
	}
	
	
}