package com.example.coosinstaff.model;

import android.content.Context;
import android.content.SharedPreferences;

public class CheckLogined {

    final static String FileName = "MyFileName";

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue){
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        return sharedPref.getString(settingName,defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue){
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName,settingValue);
        editor.apply();
    }

    public static void SharedPrefesSAVE(Context ctx, String phone_num){
        SharedPreferences prefs = ctx.getSharedPreferences("PHONE",0);
        SharedPreferences.Editor prefEDIT = prefs.edit();
        prefEDIT.putString("phone_num",phone_num);
        prefEDIT.commit();
    }
}
