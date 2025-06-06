package com.hfs.cyq.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.hfs.cyq.Assistings.Databases;
import com.hfs.cyq.Assistings.Network;
import com.hfs.cyq.R;
import com.hfs.cyq.exams.ExamAdapter;
import com.hfs.cyq.Assistings.ExamModel.ExamListdata;
import com.hfs.cyq.Assistings.ExamModel.ExamResponseData;
import com.hfs.cyq.exams.exam_details.ExamDetailActivity;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Exampage extends LinearLayout implements ExamAdapter.OnItemClickListener {
  private Context context;
  private Databases databases;
  private Network network;
  private RecyclerView recyclerView;
  private ExamAdapter adapter;
  private List<ExamListdata> examList = new ArrayList<>();

  @Override
  public void onItemClick(ExamListdata exam) {
   // PopNotification.show(exam.getName() + "\nID：" + exam.getId());
    Intent intent = new Intent(context, ExamDetailActivity.class);
    intent.putExtra("EXAM_ID", exam.getId()); // int
    context.startActivity(intent);
  }

  public Exampage(Context context) {
    super(context);
    this.context = context;
    network = new Network(context);
    databases = new Databases(context);
    init(); // 初始化布局和逻辑
  }

  private void init() {
    LayoutInflater.from(context).inflate(R.layout.exam_layout, this, true);
    recyclerView = findViewById(R.id.recyclerView);
    adapter = new ExamAdapter(examList, this);
    // 加载布局
    WaitDialog.show("加载中……");
    network.getExam(
        new Network.NetworkCallback() {
          @Override
          public void onSuccess(String data) {
            new Handler(Looper.getMainLooper())
                .post(
                    () -> {
                      Gson gson = new Gson();
                      try {
                        ExamResponseData response = gson.fromJson(data, ExamResponseData.class);
                        //
                        if (response.getCode() != 0) {
                          WaitDialog.dismiss();
                          MessageDialog.show(
                                  "请求错误！",
                                  "Msg: " + response.getMsg() + "\n请确保token有效，或进行重新登录",
                                  "确定",
                                  "重新登陆")
                              .setCancelButton(
                                  new OnDialogButtonClickListener<MessageDialog>() {
                                    @Override
                                    public boolean onClick(MessageDialog baseDialog, View v) {
                                      databases.clearToken();
                                      restartActivity();
                                      return false;
                                    }
                                  });
                          return;
                        } // 根据接口文档判断成功码
                        List<ExamResponseData.Exam> exams = response.getData().getList();

                        // 遍历考试列表
                        for (ExamResponseData.Exam exam : exams) {
                          long examId = exam.getExamId();
                          String name = exam.getName();
                          Long time = exam.getTime();
                          Instant instant = Instant.ofEpochMilli(time);
                          DateTimeFormatter formatter =
                              DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                  .withZone(ZoneId.of("Asia/Shanghai"));

                          String formattedTime = formatter.format(instant);

                          examList.add(
                              new ExamListdata(Long.toString(examId), name, formattedTime));

                          recyclerView.setLayoutManager(new LinearLayoutManager(context));

                          recyclerView.setAdapter(adapter);
                        }
                        WaitDialog.dismiss();

                      } catch (Exception e) {
                        e.printStackTrace();
                      }
                    });
          }

          @Override
          public void onFailure(Exception e) {
            WaitDialog.dismiss();
            PopNotification.show(e.toString());
            MessageDialog.show("请求失败！", "报错： " + e.toString() + "\n请检查网络连接正常", "确定");
            e.printStackTrace();
          }
        });

    // 绑定页面内的组件和逻辑

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
