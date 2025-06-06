package com.hfs.cyq.exams.exam_details;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.hfs.cyq.Assistings.ExamModel.Exam;
import com.hfs.cyq.R;
import com.hfs.cyq.Assistings.ExamModel.Paper;
public class SubjectFragment extends Fragment {
    private static final String ARG_PAPER = "paper";

    public static SubjectFragment newInstance(Paper paper) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAPER, paper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        
        if (getArguments() != null) {
            Paper paper = (Paper) getArguments().getSerializable(ARG_PAPER);
            
            // 绑定科目数据到UI
            TextView tvSubject = view.findViewById(R.id.tvSubject);
            TextView tvScore = view.findViewById(R.id.tvScore);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            CircularProgressIndicator circularProgress = view.findViewById(R.id.circularProgress);
            
            tvSubject.setText(paper.getSubject());
            tvScore.setText(paper.getScore() + "/" + paper.getManfen());
            
            // 计算百分比并显示进度条
            float percentage = (Float.parseFloat(paper.getScore()) / paper.getManfen() * 100);
            progressBar.setProgress((int) percentage);
            circularProgress.setProgressCompat((int) percentage, true);
        }
        
        return view;
    }
}
