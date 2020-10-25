package com.example.a16057851;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Define valA + valB = validAnswer
    private int valA, valB, validAnswer;
    // Define views
    private TextView questionField;
    private View successView;
    // Define number cap to which answers and number can reach.
    private static int NUMBER_CAP = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define question field
        questionField = findViewById(R.id.txtQuestion);
        // Define success view
        successView = findViewById(R.id.successView);
        // Generate first question
        this.createQuestion();
    }

    /**
     * Generates new question.
     */
    @SuppressLint("DefaultLocale")
    public void createQuestion() {
        // Display pending message
        this.questionField.setText(R.string.loading);
        // Generate values
        this.genValues();
        // Display question
        this.questionField.setText(String.format("%d + %d = ?", this.valA, this.valB));
    }

    /**
     * Generates values for the question.
     */
    private void genValues() {
        // Define random generator
        Random r = new Random();
        // Get random ints to calculate
        this.valA = r.nextInt(NUMBER_CAP);
        this.valB = r.nextInt(NUMBER_CAP);
        // Calculate valid answer
        validAnswer = this.valA + this.valB;
        // If correct answer is too high
        if (validAnswer > NUMBER_CAP) {
            // Generate values again
            this.genValues();
        }
    }

    /**
     * Parse number from clicked image.
     * @param v View
     */
    @SuppressLint("SetTextI18n")
    public void inputNumber (View v) {
        // Get output field
        TextView outputField = findViewById(R.id.txtResult);
        // Get clicked number
        int num = this.getIntFromImageView((ImageView) v);
        // Compare selected number and valida answer
        boolean result = num == this.validAnswer;
        // If correct, get next question
        if (result) {
            outputField.setText(R.string.correct);
            // Display success screen
            // this.successView.setVisibility(View.VISIBLE);
            // Get next question
            createQuestion();
        } else { // Else, display incorrect message
            outputField.setText(R.string.incorrect);
        }
    }

    /**
     * Get the selected integer from a clicked image.
     * @param iv ImageView, Widget with a 0-9 number in the id.
     * @return Integer
     */
    private int getIntFromImageView (ImageView iv) {
        // Get widget name
        String ivName = getResources().getResourceEntryName(iv.getId());
        // Define regex
        String regex = String.format("[^0-%d]", NUMBER_CAP);
        // Extract number from
        int result = Integer.parseInt(ivName.replaceAll(regex, ""));
        // Return number
        return result;
    }
}