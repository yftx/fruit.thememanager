package com.fruit.thememanager;

import com.fruit.thememanager.ThemeUtils.ThemeType;
import com.fruit.thememanager.helper.ThemeInfo;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeApplyActivity extends Activity implements View.OnClickListener, 
	AdapterView.OnItemSelectedListener {

	private static final String TAG = "ThemeApplyActivity";

	public static final String LAUNCHER_ACTIVY = "com.fruit.launcher";
			
	private static final int MSG_APPLY_THEME = 200;
	private static final int MSG_APPLY_THEME_SUCCESS = 201;
	private static final int MSG_APPLY_THEME_TIMEOUT = 202;
	private static final long APPLY_THEME_TIMEOUT = 10000;

	private Dialog mDialog;

	Handler mHandler;
//	private BroadcastReceiver mReceiver;

	private TextView mTitleView;
	private ImageView mDeleteButton;
	private Gallery mPreviewGallery;
	private ViewGroup mPreviewIndicator;
	private TextView mApplyButton;
	private PrevImageAdapter mAdapter;
	private ThemeInfo mThemeInfo;
	private ThemeManager mThemeMgr;
	
	private int mThemeCategory;

	public ThemeApplyActivity() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case MSG_APPLY_THEME:
					Log.d(TAG, "MSG_APPLY_THEME");
					mHandler.removeMessages(MSG_APPLY_THEME_TIMEOUT);
					if(mThemeMgr.applyTheme(mThemeCategory, mThemeInfo)){
						mHandler.sendEmptyMessageDelayed(MSG_APPLY_THEME_SUCCESS, 6000);
					}else{
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}
						
						Toast.makeText(ThemeApplyActivity.this, R.string.apply_theme_failed, 
								Toast.LENGTH_SHORT).show();
					}
					Log.d(TAG, "MSG_APPLY_THEME, applyTheme end");
					break;
				case MSG_APPLY_THEME_SUCCESS:
					Intent intent = new Intent();
					intent.setClassName(LAUNCHER_ACTIVY, LAUNCHER_ACTIVY + ".Launcher");
					startActivity(intent);
					ThemeApplyActivity.this.setResult(ThemeUtils.ACTIVITY_RESULT_APPLAY);
					ThemeApplyActivity.this.finish();
					Log.e(TAG, "MSG_APPLY_THEME_SUCCESS, startActivity launcher");
//					Process.killProcess(Process.myPid());
					break;
				case MSG_APPLY_THEME_TIMEOUT:
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
					
					Toast.makeText(ThemeApplyActivity.this, R.string.theme_apply_time_out, 
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};

//		mReceiver = new BroadcastReceiver() {
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				// TODO Auto-generated method stub
//				if (intent.getBooleanExtra("result", false)) {
//					mHandler.removeMessages(MSG_APPLY_THEME_TIMEOUT);
//					mHandler.sendEmptyMessageDelayed(MSG_APPLY_THEME_SUCCESSFULLY, 2000);
//				} else {
//					Toast.makeText(ThemeApplyActivity.this, R.string.apply_theme_failed, 
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		final Intent intent = getIntent();
		String pkgName = null;
		ThemeUtils.ThemeType themeType = ThemeType.THEME_DEFAULT;
		if (intent.hasExtra(ThemeUtils.PACKAGE_NAME)) {
			pkgName = intent.getStringExtra(ThemeUtils.PACKAGE_NAME);
        } 
		if (intent.hasExtra(ThemeUtils.THEME_TYPE)) {
			themeType = ThemeUtils.getThemeType(intent.getIntExtra(ThemeUtils.THEME_TYPE, 0));
        }
		
		if (intent.hasExtra(ThemeUtils.THEME_CATEGORY)) {
			mThemeCategory = intent.getIntExtra(ThemeUtils.THEME_CATEGORY, 
					ThemeUtils.CATEGORY_LAUNCHERICON);
        }
		
		if(pkgName == null){
			Log.e(TAG, "onCreate fail! pkgname null!");
			ThemeApplyActivity.this.finish();
			return;
		}
		
		mThemeMgr = ThemeManager.getInstance(this);
		mThemeInfo = mThemeMgr.getThemeInfoByName(pkgName, themeType);
		if(mThemeInfo == null){
			Log.e(TAG, "onCreate fail! getThemeInfoByName fail!");
			ThemeApplyActivity.this.finish();
			return;
		}
		
		setContentView(R.layout.theme_apply);
		
		initView();

		// Register broadcast
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ThemeUtils.APPLY_DEFAULT_RESULT);
//		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		PackageManager packageManager = getPackageManager();
		String themePkgName = mThemeInfo.mPkgName;
		if(!themePkgName.equals(ThemeUtils.DEFAULT_THEME_PACKAGENAME) && 
				!ThemeUtils.findPackage(themePkgName, packageManager)){
			this.setResult(ThemeUtils.ACTIVITY_RESULT_DELETE);
			this.finish();
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mTitleView = (TextView) findViewById(R.id.theme_title);
		mApplyButton = (TextView) findViewById(R.id.theme_apply);
		mDeleteButton = (ImageView) findViewById(R.id.theme_delete);
		mPreviewGallery = (Gallery) findViewById(R.id.theme_gallery);
		mPreviewIndicator = (ViewGroup) findViewById(R.id.theme_preview_indicator);

		//set title
		mTitleView.setText(mThemeInfo.mThemeName);
		//set delete button state
		mApplyButton.setOnClickListener(this);
		if(mThemeInfo.mThemeType == ThemeType.THEME_DEFAULT || isSysAppPackage(mThemeInfo.mPkgName)){
			mDeleteButton.setVisibility(View.GONE);
		}else{
			mDeleteButton.setOnClickListener(this);
		}
		
		//get preview image 
		final String[] previewList = getPreviewImageList(mThemeCategory);
		final int size = (previewList == null) ? 0 : previewList.length;

		mAdapter = new PrevImageAdapter(this, previewList);
		mPreviewGallery.setAdapter(mAdapter);
		mPreviewGallery.setOnItemSelectedListener(this);

		// Select image indicator
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < size; i++) {
			ImageView indicator = (ImageView) inflater.inflate(R.layout.theme_nav_item, null);
			mPreviewIndicator.addView(indicator);
		}
		if(size < 2){
			mPreviewIndicator.setVisibility(View.INVISIBLE);
		}else{
			mPreviewIndicator.getChildAt(0).setSelected(true);
		}
	}
	
	private boolean isSysAppPackage(String packageName){
		PackageManager packageManager = getPackageManager();
    	try {
    		PackageInfo info = packageManager.getPackageInfo(packageName,PackageManager.PERMISSION_GRANTED);
    		final ApplicationInfo appInfo = info.applicationInfo;
    		if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
    			return true;
    		}
			return false;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String[] getPreviewImageList(int category) {
		String[] array = null;
		
		switch (category) {
		case ThemeUtils.CATEGORY_THEME:
			array = mThemeMgr.loadStringArray(mThemeInfo, ThemeUtils.ARRAY_PRE_THEME);
			break;
		case ThemeUtils.CATEGORY_LAUNCHERICON:
			array = mThemeMgr.loadStringArray(mThemeInfo, ThemeUtils.ARRAY_PRE_LAUNCHERICON);
			break;
		case ThemeUtils.CATEGORY_LOCKSCREEN:
			array = mThemeMgr.loadStringArray(mThemeInfo, ThemeUtils.ARRAY_PRE_LOCKSCREEN);
			break;
		case ThemeUtils.CATEGORY_WALLPAPER:
			array = mThemeMgr.loadStringArray(mThemeInfo, ThemeUtils.ARRAY_PRE_WALLPAPER);
			break;
		case ThemeUtils.CATEGORY_LOCKWALLPAPER:
			array = mThemeMgr.loadStringArray(mThemeInfo, ThemeUtils.ARRAY_PRE_LOCKWALLPAPER);	
			break;			
		default:
			break;
		}
		
		return array;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		unregisterReceiver(mReceiver);
//		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.theme_apply:
			applyTheme();
			break;
		case R.id.theme_delete:
			Uri uri = Uri.parse("package:" + mThemeInfo.mPkgName);
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(uri);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		final int size = mPreviewGallery.getChildCount();

		for (int i = 0; i < size; i++) {
			if (i == position) {
				mPreviewGallery.getChildAt(i).setSelected(true);
			} else {
				mPreviewGallery.getChildAt(i).setSelected(false);
			}
		}
		
		final int indicatorSize = mPreviewIndicator.getChildCount(); 
		if(position < indicatorSize){
			for (int j = 0; j<indicatorSize; j++){
				if(j == position){
					mPreviewIndicator.getChildAt(j).setSelected(true);
				}else{
					mPreviewIndicator.getChildAt(j).setSelected(false);	
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	private void applyTheme() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(ThemeUtils.PARAM_THME_NAME, mThemeInfo.mPkgName);
		intent.setAction(ThemeUtils.getThemeIntentAction(mThemeCategory));
		
		sendOrderedBroadcast(intent, null);
		mHandler.sendEmptyMessageDelayed(MSG_APPLY_THEME, 500);
		
		// Show progress dialog
		mDialog = new Dialog(this, R.style.FullHeightDialog);
		mDialog.setContentView(R.layout.theme_applying);
		mDialog.show();
		
		// send timeout message
		mHandler.sendEmptyMessageDelayed(MSG_APPLY_THEME_TIMEOUT, APPLY_THEME_TIMEOUT);
	}

	class PrevImageAdapter extends BaseAdapter {

		private String[] mPreviewList;
		private LayoutInflater mInflater;

		public PrevImageAdapter(Context context, String[] data) {
			mInflater = LayoutInflater.from(context);
			this.mPreviewList = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (mPreviewList == null) ? 0 : mPreviewList.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.theme_preview_item, null);
			}

			ImageView preview =
				(ImageView) convertView.findViewById(R.id.theme_preview_item);
			preview.setImageDrawable(mThemeMgr.loadDrawable(mThemeInfo, mPreviewList[position]));

			return convertView;
		}
	}
}