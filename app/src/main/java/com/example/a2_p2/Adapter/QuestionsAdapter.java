package com.example.a2_p2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public QuestionsAdapter(List<QuestionModel> questionList, Context context) {
        this.questionList = questionList != null ? questionList : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_question_view, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionModel question = questionList.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, questionNumberTextView;
        RadioGroup radioGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.trivia_item_question_number);
            questionTextView = itemView.findViewById(R.id.trivia_item_question);
            radioGroup = itemView.findViewById(R.id.radio_group);
        }

        public void bind(QuestionModel question, int position) {
            questionNumberTextView.setText("Q" + (position + 1));
            questionTextView.setText(question.getQuestion());

            // Clear previous RadioButtons
            radioGroup.removeAllViews();

            // Combine correct and incorrect answers, and shuffle them
            List<String> answers = new ArrayList<>(question.getIncorrectAnswers());
            answers.add(question.getCorrectAnswer());
            Collections.shuffle(answers);

            // Create RadioButtons for each answer
            for (String answer : answers) {
                RadioButton radioButton = new RadioButton(itemView.getContext());
                radioButton.setText(answer);
                radioButton.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
                radioGroup.addView(radioButton);
            }
        }
    }
}
