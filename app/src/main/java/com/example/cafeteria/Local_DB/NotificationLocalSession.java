package com.example.cafeteria.Local_DB;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationLocalSession {

    private static final String PREF_NAME = "notifications";
    private static final String ID_NOTIFICATIO = "id_not";

    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor editor;

    public NotificationLocalSession(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = mPreferences.edit();
    }

    public void createSession(int id_notification) {
        editor.putInt(NotificationLocalSession.ID_NOTIFICATIO, id_notification);
        editor.apply();
        editor.commit();
    }

    public static int getIdNot() {
        return mPreferences.getInt(ID_NOTIFICATIO,0);
    }

}
