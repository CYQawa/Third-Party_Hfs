package com.hfs.cyq.exams.exam_details;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import com.hfs.cyq.Assistings.ExamModel.Exam;
import com.hfs.cyq.Assistings.ExamModel.Paper;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.annotation.NonNull;

public class ExamPagerAdapter extends FragmentStateAdapter {
    private final Exam exam;

    public ExamPagerAdapter(@NonNull FragmentActivity fragmentActivity, Exam exam) {
        super(fragmentActivity);
        this.exam = exam;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return OverviewFragment.newInstance(exam);
        } else {
            Paper paper = exam.getPapers().get(position - 1); // 调整索引
            return SubjectFragment.newInstance(paper);
        }
    }

    @Override
    public int getItemCount() {
        return exam.getPapers().size() + 1; // 总览 + 所有科目
    }
}
