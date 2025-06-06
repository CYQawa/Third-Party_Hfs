package com.hfs.cyq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import com.hfs.cyq.Assistings.ExamModel.ApiResponse;
import com.google.gson.Gson;
import android.view.View;
import com.hfs.cyq.Assistings.ExamModel.UserData;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hfs.cyq.Assistings.Databases;
import com.hfs.cyq.Assistings.Network;
import com.hfs.cyq.databinding.ActivityMainBinding;
import com.hfs.cyq.ui.Exampage;
import com.hfs.cyq.ui.Homepage;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;

public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;
  private FrameLayout frameContainer;

  private View currentPage;
  private Databases databases;
  private Network network;
  private TextView btnCancel;
  private TextView btnTokenL;
  private TextView btnSubmit;
  private RelativeLayout boxUserName;
  private EditText editUserName;
  private RelativeLayout boxPassword;
  private EditText editPassword;
  private TextView btnLicense;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    databases = new Databases(this);
    network = new Network(this);
    String token = databases.getToken();

    if (token == null) {
      // 无 Token，跳转到登录界面
      new FullScreenDialog(
              new OnBindView<FullScreenDialog>(R.layout.login_layout) {
                @Override
                public void onBind(FullScreenDialog dialog, View v) {
                  btnCancel = v.findViewById(R.id.btn_cancel);
                  btnSubmit = v.findViewById(R.id.btn_submit);
                  boxUserName = v.findViewById(R.id.box_userName);
                  editUserName = v.findViewById(R.id.edit_userName);
                  boxPassword = v.findViewById(R.id.box_password);
                  editPassword = v.findViewById(R.id.edit_password);
                  btnLicense = v.findViewById(R.id.btn_license);
                  btnTokenL = v.findViewById(R.id.btn_importtoken);

                  initFullScreenLoginDemo(dialog);
                }
              })
          .setCancelable(false)
          .setAllowInterceptTouch(false)
          .show();
    }

    frameContainer = findViewById(R.id.fragment_container);
    switchPage(this, new Homepage(this));
    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
    bottomNav.setOnItemSelectedListener(
        item -> {
          int itemId = item.getItemId();
          if (itemId == R.id.nav_home) {
            switchPage(this, new Homepage(this));
            return true;
          } else if (itemId == R.id.nav_exam) {
            switchPage(this, new Exampage(this));
            return true;
          } else if (itemId == R.id.nav_profile) {

            return true;
          }
          return false;
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.binding = null;
  }

  private void switchPage(Context context, View newPage) {
    View oldPage = currentPage;
    frameContainer.addView(newPage);
    newPage.setAlpha(0f);
    newPage.setTranslationY(500f);
    ObjectAnimator newAlpha = ObjectAnimator.ofFloat(newPage, "alpha", 0f, 1f);
    ObjectAnimator newTranslateY = ObjectAnimator.ofFloat(newPage, "translationY", 500f, 0f);
    ObjectAnimator newScaleX = ObjectAnimator.ofFloat(newPage, "scaleX", 0.5f, 1f);
    ObjectAnimator newScaleY = ObjectAnimator.ofFloat(newPage, "scaleY", 0.5f, 1f);
    AnimatorSet enterSet = new AnimatorSet();
    enterSet.playTogether(newAlpha, newTranslateY, newScaleX, newScaleY);
    enterSet.setDuration(500);
    enterSet.setInterpolator(new DecelerateInterpolator());
    if (oldPage != null) {
      ObjectAnimator oldAlpha = ObjectAnimator.ofFloat(oldPage, "alpha", 1f, 0f);
      ObjectAnimator oldTranslateY = ObjectAnimator.ofFloat(oldPage, "translationY", 0f, -500f);
      AnimatorSet exitSet = new AnimatorSet();
      exitSet.playTogether(oldAlpha, oldTranslateY);
      exitSet.setDuration(300);
      AnimatorSet combinedSet = new AnimatorSet();
      combinedSet.playTogether(enterSet, exitSet);
      combinedSet.addListener(
          new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              frameContainer.removeView(oldPage);
            }
          });
      combinedSet.start();
    } else {
      enterSet.start();
    }
    currentPage = newPage;
  }

  private void initFullScreenLoginDemo(final FullScreenDialog fullScreenDialog) {
    btnCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            PopNotification.show("请完成登录，以获取token");
          }
        });

    btnCancel.setText("取消");
    btnSubmit.setText("下一步");

    btnTokenL.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            new InputDialog("已有token？", "输入你的token", "确定", "取消")
                .setCancelable(false)
                .setOkButton(
                    new OnInputDialogButtonClickListener<InputDialog>() {
                      @Override
                      public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                        databases.saveToken(inputStr);
                        fullScreenDialog.dismiss();
                        return false;
                      }
                    })
                .show();
          }
        });

    btnSubmit.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if ((editUserName.getText().toString().trim()).isEmpty()) {

              return;
            }

            boxUserName.animate().x(-getDisplayWidth()).setDuration(300);
            boxPassword.setX(getDisplayWidth());
            boxPassword.setVisibility(View.VISIBLE);
            boxPassword.animate().x(0).setDuration(300);

            editPassword.setFocusable(true);
            editPassword.requestFocus();

            btnCancel.setText("上一步");
            btnCancel.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    boxUserName.animate().x(0).setDuration(300);
                    boxPassword.animate().x(getDisplayWidth()).setDuration(300);

                    editUserName.setFocusable(true);
                    editUserName.requestFocus();

                    initFullScreenLoginDemo(fullScreenDialog);
                  }
                });

            btnSubmit.setText("登录");
            btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                    if ((editPassword.getText().toString().trim()).isEmpty()) {
                      TipDialog.show("请输入密码", TipDialog.TYPE.WARNING);
                      return;
                    }

                    network.getLoginToken(
                        editUserName.getText().toString(),
                        editPassword.getText().toString(),
                        new Network.NetworkCallback() {
                          @Override
                          public void onSuccess(String data) {

                            Gson gson = new Gson();
                            ApiResponse apiResponse = gson.fromJson(data, ApiResponse.class);
                            if (apiResponse.getCode() != 0) {
                              WaitDialog.dismiss();
                              MessageDialog.show(
                                  "登陆失败",
                                  "Msg: " + apiResponse.getMsg(),
                                  "确定");

                              return;
                            } // 根据接口文档判断成功码
                            UserData userData = apiResponse.getData();
                            databases.saveToken(userData.getToken());
                            fullScreenDialog.dismiss();
                            WaitDialog.dismiss();

                            PopNotification.show("登陆成功");
                          }

                          @Override
                          public void onFailure(Exception e) {
                            WaitDialog.dismiss();
                            PopNotification.show(e.toString());
                            MessageDialog.show(
                                "请求失败！", "报错： " + e.toString() + "\n请检查网络连接正常", "确定");
                            e.printStackTrace();
                          }
                        });
                  }
                });
          }
        });
  }

  private int getDisplayWidth() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.widthPixels;
  }
}
