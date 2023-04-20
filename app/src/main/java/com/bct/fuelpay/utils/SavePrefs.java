package com.bct.fuelpay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePrefs {

    public static  void saveString(String PreferenceName, Context context ,String key, String value){

        SharedPreferences sharedPref = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getString(String PreferenceName, Context context ,String key, String defaultValue){

        SharedPreferences sharedPref = context.getSharedPreferences(PreferenceName,Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    /*public static void saveUserArrayList(String PREFERENCE_FILENAME, Context context, String key, ArrayList<ModelUser> list){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }*/

    /*public static ModelUser getArrayList(String PREFERENCE_FILENAME, Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILENAME,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<ModelUser>>() {}.getType();
//        ModelUser category = new Gson().fromJson(json, ModelUser.class);
//        gson.fromJson(json, type);
        return  gson.fromJson(json, type);
    }

    public static void saveProductArrayList(String PREFERENCE_FILENAME, Context context, String key, ArrayList<Product> list){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }



    public static ArrayList<Product> getProductArrayList(String PREFERENCE_FILENAME, Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_FILENAME,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        return  gson.fromJson(json, type);
    }*/

}
