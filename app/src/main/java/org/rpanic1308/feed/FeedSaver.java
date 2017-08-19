package org.rpanic1308.feed;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.rpanic1308.ceres.MainFeedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team_ on 01.04.2017.
 */

public class FeedSaver {

    public final static String FeedSaveKey = "feed";

    public static void saveList(List<FeedItem> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainFeedActivity.mainActivity);
        System.out.println("FeedSaver | prefs != null: " + (prefs != null));
        String s = "";
        for(int i = 0 ; i < list.size() ; i++){
            FeedItem item = list.get(i);
            s += item.toCSVString();
            if(i < list.size()-1){
                s += ";newLine;";
            }
        }
        prefs.edit().putString(FeedSaveKey, s).commit();
    }

    public static List<FeedItem> loadList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainFeedActivity.mainActivity);
        String s = prefs.getString(FeedSaveKey, "");
        System.out.println("FeedSaver | " + s);
        if(s.equals("")){
            return new ArrayList<>();
        }
        String[] arr = s.split(";newLine;");
        List<FeedItem> list = new ArrayList<>();
        for(String s2 : arr){
            list.add(FeedItem.fromCSVString(s2));
        }
        return list;
    }

    public static FeedItem buildRemover(long id){
        FeedItem item = new FeedItem();
        item.setType("remove");
        item.setContent(Long.toString(id));
        item.setTimeStamp(System.currentTimeMillis());
        return item;
    }
}
