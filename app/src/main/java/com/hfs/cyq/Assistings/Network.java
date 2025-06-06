package com.hfs.cyq.Assistings;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.WaitDialog;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Network {
  private final OkHttpClient client;
  private final String token;

  public Network(Context context) {
    Databases databases = new Databases(context);
    token = databases.getToken();
    client = new OkHttpClient();
  }

  public interface NetworkCallback {
    void onSuccess(String data);
    void onFailure(Exception e);
  }

  public void getExam(NetworkCallback callback) {
    Request request =
        new Request.Builder()
            .url("https://hfs-be.yunxiao.com/v4/exam/home-page")
            .addHeader("hfs-token", token)
            .build();
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String data = response.body().string();
                  callback.onSuccess(data);
                } else {
                  callback.onFailure(new IOException("Request failed: " + response.code()));
                }
              }
            });
  }

  public void getExamDetails(String examId, NetworkCallback callback) {
    HttpUrl url =
        HttpUrl.parse("https://hfs-be.yunxiao.com/" + "v4/exam/overview")
            .newBuilder()
            .addQueryParameter("examId", examId)
            .build();
    Request request = new Request.Builder().url(url).addHeader("hfs-token", token).build();
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String data = response.body().string();
                  callback.onSuccess(data);
                } else {
                  callback.onFailure(new IOException("Request failed: " + response.code()));
                }
              }
            });
  }
    public void getSubjectRank(String examId,String paperId, NetworkCallback callback) {
    HttpUrl url =
        HttpUrl.parse("https://hfs-be.yunxiao.com/" + "v4/exam/paper/overview")
            .newBuilder()
            .addQueryParameter("examId", examId)
        .addQueryParameter("paperId", paperId)
            .build();
    Request request = new Request.Builder().url(url).addHeader("hfs-token", token).build();
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String data = response.body().string();
                  callback.onSuccess(data);
                } else {
                  callback.onFailure(new IOException("Request failed: " + response.code()));
                }
              }
            });
  }

  public void getLoginToken(String user, String password, NetworkCallback callback) {
    byte[] data = password.getBytes(StandardCharsets.UTF_8); // 或 "UTF-8"

    // Base64 编码（默认配置，无换行）
    String base64password = Base64.encodeToString(data, Base64.NO_WRAP);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("loginName", user);
      jsonObject.put("password", base64password);
      jsonObject.put("roleType", 2);
      jsonObject.put("loginType", 1);
      jsonObject.put("rememberMe", 1);
    } catch (JSONException e) {
      callback.onFailure(e);
    }
    RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json"));

    Request request =
        new Request.Builder()
            .url("https://hfs-be.yunxiao.com/v2/users/sessions")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .build();
    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String data = response.body().string();
                  callback.onSuccess(data);
                } else {
                  callback.onFailure(new IOException("Request failed: " + response.code()));
                }
              }
            });
  }
}
