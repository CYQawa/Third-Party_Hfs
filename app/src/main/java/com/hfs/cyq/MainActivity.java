package com.hfs.cyq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hfs.cyq.Assistings.Databases;
import com.hfs.cyq.Assistings.ExamModel.ApiResponse;
import com.hfs.cyq.Assistings.ExamModel.UserData;
import com.hfs.cyq.Assistings.Network;
import com.hfs.cyq.databinding.ActivityMainBinding;
import com.hfs.cyq.ui.Exampage;
import com.hfs.cyq.ui.Homepage;
import com.hfs.cyq.ui.Mypage;
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
  private Homepage homepage;
  private Exampage exampage;
  private Mypage mypage;
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

  // 添加动画控制变量
  private AnimatorSet currentAnimator;
  private boolean isSwitching = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    databases = new Databases(this);
    network = new Network(this);

    // 初始化所有页面实例

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
      return;
    }

    homepage = new Homepage(this);
    exampage = new Exampage(this);
    mypage = new Mypage(this);
    frameContainer = findViewById(R.id.fragment_container);
    switchPage(homepage); // 初始显示首页

    BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
    bottomNav.setOnItemSelectedListener(
        item -> {
          if (isSwitching) return false;
          int itemId = item.getItemId();
          if (itemId == R.id.nav_home) {
            switchPage(homepage);
            return true;
          } else if (itemId == R.id.nav_exam) {
            switchPage(exampage);
            return true;
          } else if (itemId == R.id.nav_profile) {
            switchPage(mypage);
            return true;
          }
          return false;
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // 清除所有动画引用
    if (currentAnimator != null) {
      currentAnimator.cancel();
      currentAnimator = null;
    }
    this.binding = null;
  }

  private void switchPage(View newPage) {
    // 防止快速切换导致的动画重叠
    if (isSwitching || currentPage == newPage) {
      return;
    }

    // 设置切换状态
    isSwitching = true;

    View oldPage = currentPage;

    // 检查新页面是否已有父容器，如果有则先移除
    if (newPage.getParent() != null) {
      ((ViewGroup) newPage.getParent()).removeView(newPage);
    }

    // 添加新页面到容器
    frameContainer.addView(newPage);
    newPage.setAlpha(0f);

    // 创建淡入动画
    ObjectAnimator fadeIn = ObjectAnimator.ofFloat(newPage, "alpha", 0f, 1f);
    fadeIn.setDuration(250);
    fadeIn.setInterpolator(new DecelerateInterpolator());

    AnimatorSet animatorSet = new AnimatorSet();

    if (oldPage != null) {
      // 创建淡出动画
      ObjectAnimator fadeOut = ObjectAnimator.ofFloat(oldPage, "alpha", 1f, 0f);
      fadeOut.setDuration(150);

      // 同时执行淡出和淡入
      animatorSet.playTogether(fadeOut, fadeIn);

      animatorSet.addListener(
          new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              frameContainer.removeView(oldPage);
              currentPage = newPage;
              isSwitching = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
              if (oldPage.getParent() != null) {
                frameContainer.removeView(oldPage);
              }
              newPage.setAlpha(1f);
              currentPage = newPage;
              isSwitching = false;
            }
          });
    } else {
      animatorSet.play(fadeIn);
      animatorSet.addListener(
          new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              currentPage = newPage;
              isSwitching = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
              newPage.setAlpha(1f);
              currentPage = newPage;
              isSwitching = false;
            }
          });
    }

    animatorSet.start();
    currentAnimator = animatorSet;
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
                        restartActivity();
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
                              MessageDialog.show("登陆失败", "Msg: " + apiResponse.getMsg(), "确定");

                              return;
                            } // 根据接口文档判断成功码
                            UserData userData = apiResponse.getData();
                            databases.saveToken(userData.getToken());
                            fullScreenDialog.dismiss();
                            WaitDialog.dismiss();

                            PopNotification.show("登陆成功");
                            restartActivity();
                          }

                          @Override
                          public void onFailure(Exception e) {
                            WaitDialog.dismiss();
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

  private void restartActivity() {
    if (this instanceof Activity) {
      Activity activity = (Activity) this;
      Intent intent = activity.getIntent();
      activity.finish(); // 结束当前 Activity
      activity.startActivity(intent); // 用相同的 Intent 重新启动
      activity.overridePendingTransition(0, 0); // 取消过渡动画，让重启无感知
    }
  }
}
