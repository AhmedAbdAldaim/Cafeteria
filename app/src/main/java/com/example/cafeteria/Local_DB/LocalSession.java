package com.example.cafeteria.Local_DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalSession {

    private static final String PREF_NAME = "Cafeteria";
    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String PASSWORD = "password";
    private static final String CAFETERIA = "cafeteria";
    private static final String LOCATION = "location";
    private static final String STATUS = "status";
    private static final String MACTADDRESS = "macaddress";
    private static final String EXPIR_DATE = "expir_date";
    private static final String isSessionCreated = "isSessionCreated";
    private static final String LOCAL = "LOCAL";

    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor editor;

    public LocalSession(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = mPreferences.edit();
    }

    public void createSession(String token,String Id,String name,String phone,String cafeteria,String location,String status,String macaddress,String expir_date,String password) {
        editor.putBoolean(LocalSession.isSessionCreated, true);
        editor.putString(LocalSession.TOKEN,token);
        editor.putString(LocalSession.ID, Id);
        editor.putString(LocalSession.NAME,name);
        editor.putString(LocalSession.PHONE,phone);
        editor.putString(LocalSession.CAFETERIA,cafeteria);
        editor.putString(LocalSession.LOCATION,location);
        editor.putString(LocalSession.STATUS,status);
        editor.putString(LocalSession.MACTADDRESS,macaddress);
        editor.putString(LocalSession.EXPIR_DATE,expir_date);
        editor.putString(LocalSession.PASSWORD,password);
        editor.putString(LocalSession.LOCAL, Locale.getDefault().getLanguage());
        editor.apply();
        editor.commit();
    }


    public static Boolean getIsSessionCreated() {
        return mPreferences.getBoolean(LocalSession.isSessionCreated, false);
    }

    public static String getId() {
        return mPreferences.getString(ID,"");
    }
    public static String getName() {
        return mPreferences.getString(NAME,"");
    }
    public static String getPhone() {
        return mPreferences.getString(PHONE,"");
    }
    public static String getPassword() {
        return mPreferences.getString(PASSWORD,"");
    }
    public static String getCafeteria() {
        return mPreferences.getString(CAFETERIA,"");
    }
    public static String getLocation() {
        return mPreferences.getString(LOCATION,"");
    }
    public static String getStatus() {
        return mPreferences.getString(STATUS,"");
    }
    public static String getMactaddress() {
        return mPreferences.getString(MACTADDRESS,"");
    }
    public static String getExpirDate() {
        return mPreferences.getString(EXPIR_DATE,"");
    }
    public static String getToken() {
        return mPreferences.getString(TOKEN,"");
    }


    public static void clearSession() {
            editor.clear();
            editor.apply();
            editor.commit();
    }
}
