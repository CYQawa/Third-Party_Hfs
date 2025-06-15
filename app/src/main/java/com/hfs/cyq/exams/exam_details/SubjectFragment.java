package com.hfs.cyq.exams.exam_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.hfs.cyq.Assistings.Databases;
import com.hfs.cyq.Assistings.ExamModel.Paper;
import com.hfs.cyq.Assistings.ExamModel.PaperOverview;
import com.hfs.cyq.Assistings.Network;
import com.hfs.cyq.R;
import com.kongzue.dialogx.dialogs.PopNotification;

public class SubjectFragment extends Fragment {
  private static final String ARG_EXAM_ID = "exam_id";
  private static final String ARG_PAPER = "paper";
  private Paper paper;
  private long examId;
  private Databases databases;
  private Gson gson;
    private Network network;
  private TextView cl;
  private TextView gl;
  private TextView le;

  public static SubjectFragment newInstance(Paper paper, long examId) {
    SubjectFragment fragment = new SubjectFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PAPER, paper);
    args.putLong(ARG_EXAM_ID, examId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      databases = new Databases(requireActivity());
      network = new Network(requireActivity());
      gson = new Gson();
      paper = (Paper) getArguments().getSerializable(ARG_PAPER);
      examId = getArguments().getLong(ARG_EXAM_ID);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_subject, container, false);

    if (getArguments() != null) {

      // 绑定科目数据到UI
      TextView tvSubject = view.findViewById(R.id.tvSubject);
      TextView tvScore = view.findViewById(R.id.tvScore);
      cl = view.findViewById(R.id.tvClassRank);
      gl = view.findViewById(R.id.tvGradeRank);
      le = view.findViewById(R.id.tvlevel);
      ProgressBar progressBar = view.findViewById(R.id.progressBar);
      CircularProgressIndicator circularProgress = view.findViewById(R.id.circularProgress);

      tvSubject.setText(paper.getSubject());
      tvScore.setText(paper.getScore() + "/" + paper.getManfen());

      // 计算百分比并显示进度条
      float percentage = (Float.parseFloat(paper.getScore()) / paper.getManfen() * 100);
      progressBar.setProgress((int) percentage);
      circularProgress.setProgressCompat((int) percentage, true);

      fetchPaperOverview();
    }
    return view;
  }

  private void fetchPaperOverview() {
    network.getSubjectRank(
        Long.toString(examId),
        paper.getPaperId(),
        new Network.NetworkCallback() {
          @Override
          public void onSuccess(String data) {
            PaperOverview response = gson.fromJson(data, PaperOverview.class);
            cl.setText("· " + response.getData().getCompare().getCr() + " 名");
            gl.setText("· " + response.getData().getCompare().getGr() + " 名");
            le.setText("· " + response.getData().getLevel());
          }

          @Override
          public void onFailure(Exception e) {
            e.printStackTrace();
          }
        });
  }
}
