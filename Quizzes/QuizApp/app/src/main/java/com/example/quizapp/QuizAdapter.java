/*
package com.example.quizapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuestionViewHolder> {

    private List<Question> questions;
    // Store user answers (key = position, value = user input)
    private HashMap<Integer, Object> userAnswers = new HashMap<>();

    public QuizAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.txtQuestion.setText("Q" + (position + 1) + ": " + q.getText());

        holder.optionsLayout.removeAllViews(); // Clear previous views

        if (q.getType().equalsIgnoreCase("MCQ")) {
            // 🔹 MCQ: RadioButtons
            RadioGroup radioGroup = new RadioGroup(holder.itemView.getContext());
            for (int i = 0; i < q.getOptions().size(); i++) {
                RadioButton rb = new RadioButton(holder.itemView.getContext());
                rb.setText((char)('A' + i) + ") " + q.getOptions().get(i));
                int finalI = i;
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        userAnswers.put(position, (char)('A' + finalI) + "");
                    }
                });
                radioGroup.addView(rb);
            }
            holder.optionsLayout.addView(radioGroup);

        } else if (q.getType().equalsIgnoreCase("MSQ")) {
            // 🔹 MSQ: CheckBoxes
            ArrayList<String> selectedOptions = new ArrayList<>();
            for (int i = 0; i < q.getOptions().size(); i++) {
                CheckBox cb = new CheckBox(holder.itemView.getContext());
                cb.setText((char)('A' + i) + ") " + q.getOptions().get(i));
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedOptions.add(cb.getText().toString().substring(0, 1));
                    } else {
                        selectedOptions.remove(cb.getText().toString().substring(0, 1));
                    }
                    userAnswers.put(position, new ArrayList<>(selectedOptions));
                });
                holder.optionsLayout.addView(cb);
            }

        } else if (q.getType().equalsIgnoreCase("NAT")) {
            // 🔹 NAT: EditText
            EditText editText = new EditText(holder.itemView.getContext());
            editText.setHint("Enter numerical answer");
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userAnswers.put(position, s.toString().trim());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
            holder.optionsLayout.addView(editText);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public HashMap<Integer, Object> getUserAnswers() {
        return userAnswers;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        LinearLayout optionsLayout;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            optionsLayout = itemView.findViewById(R.id.optionsLayout);
        }
    }
}
*/
// Update 4
package com.example.quizapp;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private HashMap<Integer, Object> userAnswers = new HashMap<>();

    public QuizAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.txtQuestion.setText("Q" + (position + 1) + ": " + q.getText());

        // Load question image with Glide
        if (q.getImageUriString() != null) {
            holder.imgQuestion.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(q.getImageUriString()))
                    .into(holder.imgQuestion);
        } else {
            holder.imgQuestion.setVisibility(View.GONE);
        }

        holder.optionsLayout.removeAllViews();

        if (q.getType().equalsIgnoreCase("MCQ")) {
            RadioGroup radioGroup = new RadioGroup(holder.itemView.getContext());
            for (int i = 0; i < q.getOptions().size(); i++) {
                RadioButton rb = new RadioButton(holder.itemView.getContext());
                rb.setText((char)('A' + i) + ") " + q.getOptions().get(i));
                int finalI = i;
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        userAnswers.put(position, (char)('A' + finalI) + "");
                    }
                });
                radioGroup.addView(rb);
            }
            holder.optionsLayout.addView(radioGroup);

        } else if (q.getType().equalsIgnoreCase("MSQ")) {
            ArrayList<String> selectedOptions = new ArrayList<>();
            for (int i = 0; i < q.getOptions().size(); i++) {
                CheckBox cb = new CheckBox(holder.itemView.getContext());
                cb.setText((char)('A' + i) + ") " + q.getOptions().get(i));
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedOptions.add(cb.getText().toString().substring(0, 1));
                    } else {
                        selectedOptions.remove(cb.getText().toString().substring(0, 1));
                    }
                    userAnswers.put(position, new ArrayList<>(selectedOptions));
                });
                holder.optionsLayout.addView(cb);
            }

        } else if (q.getType().equalsIgnoreCase("NAT")) {
            EditText editText = new EditText(holder.itemView.getContext());
            editText.setHint("Enter numerical answer");
            editText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userAnswers.put(position, s.toString().trim());
                }
                @Override public void afterTextChanged(Editable s) {}
            });
            holder.optionsLayout.addView(editText);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public HashMap<Integer, Object> getUserAnswers() {
        return userAnswers;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        LinearLayout optionsLayout;
        ImageView imgQuestion; // Added ImageView

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtQuestion);
            optionsLayout = itemView.findViewById(R.id.optionsLayout);
            imgQuestion = itemView.findViewById(R.id.imgQuestion); // Link to ImageView
        }
    }
}