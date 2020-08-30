package com.example.cidianl;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DictionarySave {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public DictionarySave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void save(String tag, List<String> dictionary) {
        if (dictionary == null || dictionary.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        String strJson = gson.toJson(dictionary);
        editor.clear();
        editor.putString(tag,strJson);
        editor.commit();
    }

    public List<String> load(String tag) {
        List<String> dictionary = new ArrayList<String>();
        String strJson = preferences.getString(tag,null);
        if (strJson == null) {
            return dictionary;
        }
        Gson gson = new Gson();
        dictionary = gson.fromJson(strJson,new TypeToken<List<String>>(){}.getType());
        return dictionary;
    }


}
