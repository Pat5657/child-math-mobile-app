package com.example.a16057851;

import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Random;

public class SuccessView {

    private MainActivity mainActivity;
    private ConstraintLayout successLayout;
    private ImageView[] starViews;
    private final MediaPlayer mp;

    /**
     * Constructor
     * @param mainActivity MainActivity
     */
    public SuccessView (MainActivity mainActivity) {
        // Define main view
        this.mainActivity = mainActivity;
        // Define success layout view
        this.successLayout = this.mainActivity.findViewById(R.id.successLayout);
        // Define stars displayed on the success screen
        this.starViews = new ImageView[5];
        // Define media player victory sound
        this.mp = MediaPlayer.create(this.mainActivity, R.raw.victory);
    }

    /**
     * Display the success view.
     */
    public void displaySuccessView() {
        // Play victory sound
        this.mp.start();
        // Define answer field
        TextView answerField = this.mainActivity.findViewById(R.id.txtAnswer);
        // Display answer
        answerField.setText(String.format("%d + %d = %d", this.mainActivity.getValA(), this.mainActivity.getValB(), this.mainActivity.getValidAnswer()));
        // Display stars
        for (int i = 0; i < this.starViews.length; i++) {
            // Create and display star
            ImageView star = this.loadStar();
            // Add star to list
            this.starViews[i] = star;
        }
        // Bring replay button and answer field to front
        this.mainActivity.findViewById(R.id.txtAnswer).bringToFront();
        this.mainActivity.findViewById(R.id.btnReplay).bringToFront();
        // Display success screen
        this.successLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Create an ImageView of a star on the success screen.
     * @return ImageView
     */
    private ImageView loadStar() {
        // Define star image view
        ImageView star = new ImageView(this.mainActivity);
        // Set star image
        star.setImageResource(R.drawable.star_kids);
        // Set star Id
        star.setId(this.genViewId());
        int starId = star.getId();
        // Add star to the layout
        this.successLayout.addView(star);
        // Set constraints
        this.setStarConstraints(starId);
        // Add star animations
        this.animateStar(star);
        // Return star
        return star;
    }

    /**
     * Add animations to a star on the success view.
     * @param star ImageView
     */
    private void animateStar(ImageView star) {
        // Define random offset for animation start
        int offsetStart = this.getRandomNumber(0, 1000);
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
    }

    /**
     * Set random widget constraints for the star.
     * @param starId int
     */
    private void setStarConstraints(int starId) {
        // Define constraint set
        ConstraintSet cSet = new ConstraintSet();
        // Clone success view
        cSet.clone(this.successLayout);
        // Set constraints
        cSet.connect(starId, ConstraintSet.TOP, this.successLayout.getId(), ConstraintSet.TOP, this.getRandomNumber(50, 1400));
        cSet.connect(starId, ConstraintSet.LEFT, this.successLayout.getId(), ConstraintSet.LEFT, this.getRandomNumber(50, 800));
        // Apply constraints
        cSet.applyTo(this.successLayout);
    }

    /**
     * Generate a random available view Id.
     * Since View.generateViewId() is only supported from API 17.
     * @return int
     */
    private int genViewId() {
        int result;
        // Generate new valid view id
        do {
            result = this.getRandomNumber(1, 256);
        } while (this.mainActivity.findViewById(result) != null);
        // Return id
        return result;
    }

    /**
     * Get a random number between a specified range.
     * @param min int
     * @param max int
     * @return int
     */
    private int getRandomNumber(int min, int max) {
        // Define random class
        Random random = new Random();
        // Return random number between the range
        return random.nextInt(max - min) + min;
    }

    /**
     * Close success view and get new question.
     */
    public void replay() {
        // Generate new question
        this.mainActivity.createQuestion();
        // Remove stars
        for (int i = 0; i < this.starViews.length; i++) {
            this.starViews[i].clearAnimation();
            this.successLayout.removeView(this.starViews[i]);
        }
        // Hide success screen
        this.successLayout.setVisibility(View.INVISIBLE);
    }
}
