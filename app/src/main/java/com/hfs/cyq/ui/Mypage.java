package com.hfs.cyq.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.hfs.cyq.Assistings.Databases;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.hfs.cyq.Assistings.ExamModel.Userinformation;
import com.hfs.cyq.Assistings.Network;
import com.hfs.cyq.R;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

public class Mypage extends LinearLayout {
  private Context context;
  private Network network;
  private Databases databases;

  public Mypage(Context context) {
    super(context);
    this.context = context;
    network = new Network(context);
    databases = new Databases(context);
    init();
  }

  private void init() {
    LayoutInflater.from(context).inflate(R.layout.layout_my, this, true);

    MaterialButton outLogin_btn = findViewById(R.id.btn_logout);
        MaterialButton look_token = findViewById(R.id.btn_view_cookie);
        
        
    outLogin_btn.setOnClickListener(
        v -> {
          MessageDialog.show("提示", "确定退出登录吗？", "取消", "确定")
              .setCancelButton(
                  new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog baseDialog, View v) {
                      databases.clearToken();
                      restartActivity();
                      return false;
                    }
                  });
        });
        look_token.setOnClickListener(
        v -> {
          MessageDialog.show("以下是你的token（切勿给他人）", databases.getToken(), "确定");

        });


    Gson gson = new Gson();
    network.getMyMessage(
        new Network.NetworkCallback() {
          @Override
          public void onSuccess(String data) {
            Userinformation response = gson.fromJson(data, Userinformation.class);
            new Handler(Looper.getMainLooper())
                .post(
                    () -> {
                      ShapeableImageView im = findViewById(R.id.profile_image);
                      TextView name = findViewById(R.id.tv_username);
                            TextView sh = findViewById(R.id.tv_user_sh);

                      Picasso.get()
                          .load(response.getData().getAvatar())
                          .placeholder(R.drawable.placeholder)
                          .into(im);
                      name.setText(response.getData().getLinkedStudent().getStudentName());
                            sh.setText(response.getData().getLinkedStudent().getSchoolName() + " | " + response.getData().getLinkedStudent().getGrade());
                    });
          }

          @Override
          public void onFailure(Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void restartActivity() {
    if (context instanceof Activity) {
      Activity activity = (Activity) context;
      Intent intent = activity.getIntent();
      activity.finish(); // 结束当前 Activity
      activity.startActivity(intent); // 用相同的 Intent 重新启动
      activity.overridePendingTransition(0, 0); // 取消过渡动画，让重启无感知
    }
  }
}
