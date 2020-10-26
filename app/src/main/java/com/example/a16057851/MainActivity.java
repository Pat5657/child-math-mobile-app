package com.example.a16057851;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Define valA + valB = validAnswer
    private int valA, valB, validAnswer;
    private ImageView[] starViews = new ImageView[5];
    // Define views
    private TextView questionField;
    private ConstraintLayout successLayout;
    // Define number cap to which answers and number can reach.
    private static int NUMBER_CAP = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define question field
        this.questionField = findViewById(R.id.txtQuestion);
        // Define success layout view
        this.successLayout = findViewById(R.id.successLayout);
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
        // If correct, display success screen
        if (result) {
            this.displaySuccessView();
        } else { // Else, display incorrect message
            outputField.setText(R.string.incorrect);
        }
    }

    /**
     * Handle displaying success view after a successful answer.
     */
    private void displaySuccessView() {
        // Define answer field
        TextView answerField = findViewById(R.id.txtAnswer);
        // Display answer
        answerField.setText(String.format("%d + %d = %d", this.valA, this.valB, this.validAnswer));
        // Display stars
        for (int i = 0; i < 5; i++) {
            ImageView star = this.loadStars();
            this.starViews[i] = star;
        }
        // Bring replay button and answer field to front
        findViewById(R.id.txtAnswer).bringToFront();
        findViewById(R.id.btnReplay).bringToFront();
        // Display success screen
        this.successLayout.setVisibility(View.VISIBLE);
    }

    private ImageView loadStars() {
        // Define constraint set
        ConstraintSet cSet = new ConstraintSet();
        // Define star image view
        ImageView star = new ImageView(this);
        // Set star image
        star.setImageResource(R.drawable.star_kids);
        // Set Id
        star.setId(this.genViewId());
        int starId = star.getId();
        // Add star to the layout
        this.successLayout.addView(star);
        // Set constraints
        cSet.clone(this.successLayout);
        cSet.connect(starId, ConstraintSet.TOP, this.successLayout.getId(), ConstraintSet.TOP, this.getRandomNumber(50, 1400));
        cSet.connect(starId, ConstraintSet.LEFT, this.successLayout.getId(), ConstraintSet.LEFT, this.getRandomNumber(50, 800));
        cSet.applyTo(this.successLayout);
        // Define random offset for animation start
        int offsetStart = this.getRandomNumber(0, 2000);
        // Define animation set
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setStartOffset(offsetStart);
        // Add scale animations
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1);
        scale.setDuration(5000);
        scale.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(scale);
        // Add rotate animation
        RotateAnimation rotate = new RotateAnimation(0, 360);
        rotate.setDuration(5000);
        rotate.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(rotate);
        // Add translate animation
        TranslateAnimation trans = new TranslateAnimation(0, 200, 0, 100);
        trans.setDuration(5000);
        trans.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(trans);
        // Add fade out animation
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(2500);
        fadeOut.setDuration(2500);
        fadeOut.setRepeatCount(Animation.INFINITE);
        animationSet.addAnimation(fadeOut);
        // Apply animation set
        star.startAnimation(animationSet);
        // Return star
        return star;
    }

    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private int genViewId() {
        int result;
        // Define random generator
        Random r = new Random();
        // Generate new valid view id
        do {
            result = r.nextInt(255);
        } while (findViewById(result) != null);
        // Return id
        return result;
    }

    /**
     * Close success view and get new question.
     * @param v View
     */
    public void replay(View v) {
        // Generate new question
        this.createQuestion();
        // Remove stars
        for (int i = 0; i < this.starViews.length; i++) {
            this.starViews[i].clearAnimation();
            this.successLayout.removeView(this.starViews[i]);
        }
        // Hide success screen
        this.successLayout.setVisibility(View.INVISIBLE);
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