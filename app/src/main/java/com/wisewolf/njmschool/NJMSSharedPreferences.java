package com.wisewolf.njmschool;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wisewolf.njmschool.Models.ClassVideo;

public class NJMSSharedPreferences {
    private static NJMSSharedPreferences yourPreference;
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public static NJMSSharedPreferences getInstance(Context context) {

        if (yourPreference == null) {
            yourPreference = new NJMSSharedPreferences(context);
        }
        return yourPreference;
    }
    private NJMSSharedPreferences(Context context) {
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }


    public void saveIntData(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }


    public int getIntData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }

    public static class ConnectivityHelper {
        public static boolean isConnectedToNetwork(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean isConnected = false;
            if (connectivityManager != null) {
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
            }
            return isConnected;
        }
    }

    public void clearSharedPreference() {
        SharedPreferences preferences = sharedPreferences;
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences preferences = sharedPreferences;
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
