package com.fruit.thememanager;

import com.fruit.thememanager.ThemeUtils.ThemeType;
import com.fruit.thememanager.helper.ThemeInfo;
import com.fruit.thememanager.helper.ThemeInfoLoader;
import com.fruit.thememanager.helper.ThemeLoaderHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ThemeProvider extends ContentProvider {

	private static final String TAG = "ThemeProvider";
	
	private DatabaseHelper mDBHelper;
	private static final String DATABASE_NAME = "themes.db";
	private static final int DATABASE_VERSION = 2;
	static final String TABLE_NAME = "theme";
	
	private static final String CONFIG_PREFIX = "defalt_";
	
	private static final String[] THEME_CATEGARY={
		ThemeUtils.THEME,
		ThemeUtils.LAUNCHERICON,
		ThemeUtils.LOCKSCREEN,
		ThemeUtils.WALLPAPER,
		ThemeUtils.LOCKWALLPAPER
	};
	
    private static final UriMatcher mUriMatcher;

    private static final int THEMES = 101;
    private static final int THEME_ITEM = 102;
    private static final int THEME_LOCK = 103;

    static {
    	mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	mUriMatcher.addURI(ThemeUtils.AUTHORITY, "theme/"+ThemeUtils.URI_THEMES, THEMES);
    	mUriMatcher.addURI(ThemeUtils.AUTHORITY, "theme/"+ThemeUtils.URI_THEMEITEM, THEME_ITEM);
    	mUriMatcher.addURI(ThemeUtils.AUTHORITY, "theme/"+ThemeUtils.URI_THEMELOCK, THEME_LOCK);
    }
    

    private static class DatabaseHelper extends SQLiteOpenHelper {

		private Context mContext;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Enter DatabaseHelper.onCreate()");
			db.execSQL("Create table " + TABLE_NAME
					+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "category TEXT,"
					+ "theme_name TEXT,"
					+ "package_name TEXT,"
					+ "author TEXT,"
					+ "type,"
					+ "support_version TEXT);");

			initDatabase(db);

			Log.d(TAG, "Leave DatabaseHelper.onCreate()");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

		private void initDatabase(SQLiteDatabase db) {
			PackageManager pmg = mContext.getPackageManager();
			ThemeInfoLoader loader = ThemeLoaderHelper.getThemeInfoLoader(ThemeType.THEME_CUSTOM);
    		final Resources res = mContext.getResources();
    		final String defPkgName = ThemeUtils.DEFAULT_THEME_PACKAGENAME;
    		final String defName = res.getString(R.string.theme_name_default);
    		final String defAuthor = res.getString(R.string.theme_author_default);
    		final String defSupportVer = res.getString(R.string.theme_support_version_default);
    		final int defType = ThemeUtils.getThemeType(ThemeType.THEME_DEFAULT);
    		
			for (String type : THEME_CATEGARY) {
				String configName = CONFIG_PREFIX + type;
				String pkgName = getStringConfig(mContext, configName);
				Log.d(TAG, "initDatabase: item = " + type);
		        if(pkgName != null){
		        	try {
		        		pmg.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
		        	} catch (NameNotFoundException e) {
		        		e.printStackTrace();
		        		pkgName = null;
		        	}       
		        }
		        ContentValues values = new ContentValues();
		        values.put(ThemeUtils.THEME_CATEGORY, type);
		        if(pkgName != null){
		        	ThemeInfo info = loader.loadThemeInfo(mContext, pkgName);
		        	if(info != null){
		        		values.put(ThemeUtils.PACKAGE_NAME, info.mPkgName);
						values.put(ThemeUtils.AUTHOR, info.mThemeAuthor);
						values.put(ThemeUtils.THEME_NAME, info.mThemeName);
						values.put(ThemeUtils.SUPPORTVER, info.mThemeSupportVer);
						values.put(ThemeUtils.THEME_TYPE, ThemeUtils.getThemeType(info.mThemeType));
		        	}
		        }else{
		        	values.put(ThemeUtils.PACKAGE_NAME, defPkgName);
		        	values.put(ThemeUtils.AUTHOR, defAuthor);
					values.put(ThemeUtils.THEME_NAME, defName);
					values.put(ThemeUtils.SUPPORTVER, defSupportVer);
					values.put(ThemeUtils.THEME_TYPE, defType);
		        }
				db.insert(TABLE_NAME, null, values);
			}
		}
		
		private String getStringConfig(Context context, String name){
	        final String packageName = context.getPackageName();
	        final Resources res = context.getResources();
	        
			int resId = res.getIdentifier(name, "string", packageName);
			if (resId > 0) {
				return res.getString(resId);
			}
			return null;
		}
	}
	
	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues arg) {
		Log.d(TAG, "Enter insert()");
		SQLiteDatabase sqlDB = mDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues(arg);
		long rowId = sqlDB.insert(TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri rowUri = ContentUris.appendId(
					ThemeUtils.CONTENT_URI.buildUpon(), rowId).build();
			Log.d(TAG, "Leave insert()");
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "Enter query()");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		qb.setTables(TABLE_NAME);
		Log.d(TAG, "query(): uri: " + uri.toString());

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		Log.d(TAG, "Leave query()");
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final int match = mUriMatcher.match(uri);
    	final SQLiteDatabase db = mDBHelper.getWritableDatabase();
    	int count = 0;

    	switch (match) {
    	case THEMES: 		
    		if(values.containsKey(ThemeUtils.THEME_CATEGORY)){
    			values.remove(ThemeUtils.THEME_CATEGORY);
    		}
            count = db.update(TABLE_NAME, values, null, null);
    		break;
    		
    	case THEME_ITEM: 	
            count = db.update(TABLE_NAME, values, ThemeUtils.THEME_CATEGORY+"=?", selectionArgs);
    		break;
    		
    	case THEME_LOCK: 	
            count = db.update(TABLE_NAME, values, "(" + ThemeUtils.THEME_CATEGORY + " = '" + ThemeUtils.LOCKSCREEN +
            		"') OR (" + ThemeUtils.THEME_CATEGORY + " = '" + ThemeUtils.LOCKWALLPAPER + "')", null);
    		break;
    		
		default:
			break;
    	}
    	
		if (count > 0) {
			sendNotify(uri);
		}
    	return count;
	}
	
    private void sendNotify(Uri uri) {
        String notify = uri.getQueryParameter(ThemeUtils.PARAMETER_NOTIFY);
        if (notify == null || "true".equals(notify)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }
}
