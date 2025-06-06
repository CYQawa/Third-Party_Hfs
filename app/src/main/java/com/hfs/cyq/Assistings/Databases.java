package com.hfs.cyq.Assistings;
import android.content.Context;
import android.content.SharedPreferences;

public class Databases {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String HFS_TOKEN = "hfs_token";
    private SharedPreferences sharedPreferences;
    
    public Databases(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public  void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HFS_TOKEN, token);
        editor.apply(); // 异步保存，避免阻塞主线程
    }

    // 获取 Token
    public String getToken() {
        return sharedPreferences.getString(HFS_TOKEN, null);
    }

    // 清除 Token（用于退出登录）
    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(HFS_TOKEN);
        editor.apply();
    }
}
