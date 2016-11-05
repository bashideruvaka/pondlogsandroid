package pondlogss.eruvaka.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * to handle all the shared preference related things like get and set values
 *
 */
public class SharedPreferenceHandle {

    private static SharedPreferenceHandle mSharedPreferenceHandle;
    private static String defaultPreferenceName = "com.eruvaka";
    private Editor editor;
    private static Context mContext;

    public static SharedPreferenceHandle getSharedPreferenceHandle(
            Context context) {
        mContext = context;
        if (mSharedPreferenceHandle == null)
            mSharedPreferenceHandle = new SharedPreferenceHandle();

        return mSharedPreferenceHandle;
    }

    private Editor getSharedPreferenceEditor(Context context) {
        return context.getSharedPreferences(defaultPreferenceName, 0).edit();
    }

    public void putString(String key, String value)

    {
        editor = getSharedPrefernces(mContext).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void putInt(String key, int value)

    {
        editor = getSharedPreferenceEditor(mContext);
        editor.putInt(key, value);
        editor.commit();

    }

    public void putBoolean(String key, boolean value)

    {
        editor = getSharedPreferenceEditor(mContext);
        editor.putBoolean(key, value);
        editor.commit();

    }

    public void putLong(String key, long value)

    {
        editor = getSharedPreferenceEditor(mContext);
        editor.putLong(key, value);
        editor.commit();

    }

    public void putFloat(String key, float value)

    {
        editor = getSharedPreferenceEditor(mContext);
        editor.putFloat(key, value);
        editor.commit();

    }

    public int getInt(String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPrefernces(mContext);

        return sharedPreferences.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {

        return getSharedPrefernces(mContext).getString(key, defaultValue);
    }

    private SharedPreferences getSharedPrefernces(Context context,
                                                  String preferenceName) {
        return context.getSharedPreferences(preferenceName, 0);
    }

    private SharedPreferences getSharedPrefernces(Context context) {

        return context.getSharedPreferences(defaultPreferenceName, 0);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                defaultPreferenceName, 0);

        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                defaultPreferenceName, 0);

        return sharedPreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                defaultPreferenceName, 0);

        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void clearSharedPreference() {
        editor = getSharedPrefernces(mContext).edit();
        editor.clear();
        editor.commit();
    }

    public void remove(String key) {
        editor = getSharedPrefernces(mContext).edit();
        editor.remove(key);
        editor.commit();
    }


}
