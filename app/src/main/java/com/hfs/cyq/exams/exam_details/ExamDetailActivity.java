package com.hfs.cyq.exams.exam_details;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfs.cyq.R;
import com.hfs.cyq.Assistings.ExamModel.ExamDetailResponse;
import com.kongzue.dialogx.dialogs.PopNotification;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.hfs.cyq.Assistings.Network;
import com.google.gson.Gson;
import android.view.View;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.hfs.cyq.Assistings.ExamModel.Exam;
import com.hfs.cyq.exams.exam_details.ExamPagerAdapter;

public class ExamDetailActivity extends AppCompatActivity {
  private TabLayout tabLayout;
  private ViewPager2 viewPager;
  private String examId;
  private Network network;
  private Exam exam;
  private ExamPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exam_detail); // 必须添加

    // 在 Activity/Fragment 中添加实现
    MaterialToolbar topAppBar = findViewById(R.id.toolbar);
    setSupportActionBar(topAppBar); // 设置为ActionBar

    // 启用返回按钮（必须在setSupportActionBar之后调用）
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 处理点击事件
    topAppBar.setNavigationOnClickListener(
        v -> {
          // 返回上一页面
          finish();
            finishAfterTransition();
        //  }
        });

    network = new Network(this);
    Gson gson = new Gson();
    tabLayout = findViewById(R.id.tabLayout);
    viewPager = findViewById(R.id.viewPager);
    Intent intent = getIntent();
    examId = intent.getStringExtra("EXAM_ID");

    //  PopNotification.show(examId);
    WaitDialog.show("加载中……");
    network.getExamDetails(
        examId,
        new Network.NetworkCallback() {
          @Override
          public void onSuccess(String data) {
            WaitDialog.dismiss();
            ExamDetailResponse response = gson.fromJson(data, ExamDetailResponse.class);
            exam = response.getData();
            runOnUiThread(
                () -> {
                  if (isDestroyed() || isFinishing()) return;
                  setupViewPager();
                            topAppBar.setSubtitle(exam.getName());
                });
            // PopNotification.show(exam.getName());
          }

          @Override
          public void onFailure(Exception e) {
            WaitDialog.dismiss();
            e.printStackTrace();
          }
        });
  }

  private void setupViewPager() {
    adapter = new ExamPagerAdapter(this, exam);
    viewPager.setAdapter(adapter);

    // 关联TabLayout和ViewPager2
    new TabLayoutMediator(
            tabLayout,
            viewPager,
            (tab, position) -> {
              if (position == 0) {
                tab.setText("总览");
              } else {
                tab.setText(exam.getPapers().get(position - 1).getSubject());
              }
            })
        .attach();
    WaitDialog.dismiss();
  }
}
