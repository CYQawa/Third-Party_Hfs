
package com.hfs.cyq.exams;

import java.util.List;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import com.hfs.cyq.R;
import com.hfs.cyq.Assistings.ExamModel.ExamListdata;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private List<ExamListdata> examList;
    private OnItemClickListener listener;

    // 构造函数
    public ExamAdapter(List<ExamListdata> examList, OnItemClickListener listener) {
        this.examList = examList;
        this.listener = listener;
    }
    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamName, tvExamId, tvTotalScore;

        public ExamViewHolder(View itemView) {
            super(itemView);
            tvExamName = itemView.findViewById(R.id.tvExamName);
            tvExamId = itemView.findViewById(R.id.tvExamId);
            tvTotalScore = itemView.findViewById(R.id.tvTotalScore);
        }
    }

    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamViewHolder holder, int position) {
        ExamListdata exam = examList.get(position);
        
        holder.tvExamName.setText(exam.getName());
        holder.tvExamId.setText("考试ID: " + exam.getId());
        holder.tvTotalScore.setText("日期: " + exam.getTime());

        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(exam); // 传递整个考试对象
            }
        });
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(ExamListdata exam);
    }
}