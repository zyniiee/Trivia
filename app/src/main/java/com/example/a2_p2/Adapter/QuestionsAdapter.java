package com.example.a2_p2.Adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_p2.Model.QuestionModel;
import com.example.a2_p2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<QuestionModel> questionList;
    private Context context;
    private OnNextButtonClickListener onNextButtonClickListener;
    private Handler handler = new Handler();

    public QuestionsAdapter(List<QuestionModel> questionList, Context context, OnNextButtonClickListener listener) {
        this.questionList = questionList;
        this.context = context;
        this.onNextButtonClickListener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionModel question = questionList.get(position);
        holder.questionNumber.setText("Q" + (position + 1));
        holder.questionText.setText(Html.fromHtml(question.getQuestion()));
        holder.radioGroup.clearCheck();
        holder.radioGroup.removeAllViews();
        holder.feedbackText.setVisibility(View.GONE);

        List<String> answers = new ArrayList<>(question.getIncorrect_answers());
        answers.add(question.getCorrect_answer());
        Collections.shuffle(answers);

        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(answer);
            holder.radioGroup.addView(radioButton);
        }

        holder.nextButton.setOnClickListener(v -> {
            RadioButton selectedAnswer = holder.itemView.findViewById(holder.radioGroup.getCheckedRadioButtonId());
            if (selectedAnswer != null) {
                boolean isCorrect = selectedAnswer.getText().equals(question.getCorrect_answer());
                if (isCorrect) {
                    holder.feedbackText.setText("Congratulations! You got it right!");
                } else {
                    holder.feedbackText.setText("Nice try, but the right answer is: " + question.getCorrect_answer());
                }
                holder.feedbackText.setVisibility(View.VISIBLE);

                handler.postDelayed(() -> {
                    holder.feedbackText.setVisibility(View.GONE);
                    onNextButtonClickListener.onNextButtonClick(isCorrect);
                }, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionNumber;
        TextView questionText;
        RadioGroup radioGroup;
        TextView feedbackText;
        Button nextButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumber = itemView.findViewById(R.id.trivia_item_question_number);
            questionText = itemView.findViewById(R.id.trivia_item_question);
            radioGroup = itemView.findViewById(R.id.radio_group);
            feedbackText = itemView.findViewById(R.id.feedback_text);
            nextButton = itemView.findViewById(R.id.next_button);
        }
    }

    public interface OnNextButtonClickListener {
        void onNextButtonClick(boolean isCorrect);
    }
}
