package com.example.a2_p2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private int score;

    public QuestionsAdapter(List<QuestionModel> questionList, Context context) {
        this.questionList = questionList != null ? questionList : new ArrayList<>();
        this.context = context;
        this.score = 0;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_item, parent, false);
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

    public int getScore() {
        return score;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, questionNumberTextView, feedbackTextView;
        RadioGroup radioGroup;
        Button nextButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionNumberTextView = itemView.findViewById(R.id.trivia_item_question_number);
            questionTextView = itemView.findViewById(R.id.trivia_item_question);
            radioGroup = itemView.findViewById(R.id.radio_group);
            feedbackTextView = itemView.findViewById(R.id.feedback_text);
            nextButton = itemView.findViewById(R.id.next_button);
        }

        public void bind(QuestionModel question, int position) {
            questionNumberTextView.setText("Q" + (position + 1));
            questionTextView.setText(question.getQuestion());
            feedbackTextView.setVisibility(View.GONE);

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

            nextButton.setOnClickListener(v -> {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = itemView.findViewById(selectedId);
                    String selectedAnswer = selectedRadioButton.getText().toString();

                    if (selectedAnswer.equals(question.getCorrectAnswer())) {
                        feedbackTextView.setText("Congratulations! You got it right!");
                        feedbackTextView.setVisibility(View.VISIBLE);
                        score++;
                    } else {
                        feedbackTextView.setText("Oops! That's not quite right. The correct answer is " + question.getIncorrectAnswers());
                        feedbackTextView.setVisibility(View.VISIBLE);
                    }
                    // Move to the next question after a delay
                    itemView.postDelayed(() -> {
                        // Implement logic to go to the next question
                        // For example, you can notify the activity to update the current question index
                    }, 2000);
                } else {
                    Toast.makeText(itemView.getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
