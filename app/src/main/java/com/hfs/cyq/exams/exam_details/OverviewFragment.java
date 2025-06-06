package com.hfs.cyq.exams.exam_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.hfs.cyq.Assistings.ExamModel.Exam;
import com.hfs.cyq.R;
public class OverviewFragment extends Fragment {
    private static final String ARG_EXAM = "exam";

    public static OverviewFragment newInstance(Exam exam) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXAM, exam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        
        if (getArguments() != null) {
            Exam exam = (Exam) getArguments().getSerializable(ARG_EXAM);
            
            // 绑定总览数据到UI
            TextView tvExamName = view.findViewById(R.id.tvExamName);
            TextView tvTotalScore = view.findViewById(R.id.tvTotalScore);
            TextView tvGradeRank = view.findViewById(R.id.tvGradeRank);
            TextView tvlevel = view.findViewById(R.id.tvlevel);
            ProgressBar progressBar = view.findViewById(R.id.OverprogressBar);
            
            tvExamName.setText(exam.getName());
            tvTotalScore.setText(exam.getScore() + "/" + exam.getManfen());
            tvGradeRank.setText("· "+exam.getCompare().getCurGradeRank()+" 名");
            tvlevel.setText("· "+exam.getLevel());
            
            float percentage = (Float.parseFloat(exam.getScore()) / exam.getManfen() * 100);
            progressBar.setProgress((int) percentage);
        }
        
        return view;
    }
}