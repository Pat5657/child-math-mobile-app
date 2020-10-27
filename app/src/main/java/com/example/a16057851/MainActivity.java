package com.example.a16057851;

import androidx.appcompat.app.AppCompatActivity;

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
    private SuccessView successView;
    // Define number cap to which answers and number can reach.
    private static int NUMBER_CAP = 9;
    private AppleMovable[] movableApples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define question field
        this.questionField = findViewById(R.id.txtQuestion);
        // Define success view
        this.successView = new SuccessView(this);
        // Generate first question
        this.createQuestion();
        // Initialise apple dragging
        this.initApples();
    }

    /**
     * Initialises each apple image with the dragging feature.
     */
    private void initApples() {
        // Define array of movable apples
        this.movableApples = new AppleMovable[NUMBER_CAP];
        // Create apples and add them to list of movable apples
        for (int i = 0; i < NUMBER_CAP; i++) {
            this.movableApples[i] = new AppleMovable(this);
        }
    }

    /**
     * Returns valA.
     * @return int
     */
    public int getValA() {
        return this.valA;
    }

    /**
     * Return valB.
     * @return int
     */
    public int getValB() {
        return this.valB;
    }

    /**
     * Return validAnswer.
     * @return int
     */
    public int getValidAnswer() {
        return this.validAnswer;
    }

    /**
     * Generates new question.
     */
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
    public void inputNumber (View v) {
        // Get output field
        TextView outputField = findViewById(R.id.txtResult);
        // Get clicked number
        int num = this.getIntFromImageView((ImageView) v);
        // Compare selected number and valida answer
        boolean result = num == this.validAnswer;
        // If correct, display success screen
        if (result) {
            this.successView.displaySuccessView();
        } else { // Else, display incorrect message
            outputField.setText(R.string.incorrect);
        }
    }

    /**
     * Close success view and get new question.
     * @param v View
     */
    public void replay(View v) {
        // Reposition apples
        for (int i = 0; i < NUMBER_CAP; i++) {
            // Remove existing apples from view
            this.movableApples[i].removeView();
            // Add new apple back on the plate
            this.movableApples[i] = new AppleMovable(this);
        }
        // Reload the
        this.successView.replay();
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
        // Extract number from name
        int result = Integer.parseInt(ivName.replaceAll(regex, ""));
        // Return number
        return result;
    }

    /**
     * Generate a random available view Id.
     * Since View.generateViewId() is only supported from API 17.
     * @return int
     */
    public int genViewId() {
        int result;
        // Generate new valid view id
        do {
            result = this.getRandomNumber(1, 256);
        } while (this.findViewById(result) != null);
        // Return id
        return result;
    }

    /**
     * Get a random number between a specified range.
     * @param min int
     * @param max int
     * @return int
     */
    public int getRandomNumber(int min, int max) {
        // Define random class
        Random random = new Random();
        // Return random number between the range
        return random.nextInt(max - min) + min;
    }
}