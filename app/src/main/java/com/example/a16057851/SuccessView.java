package com.example.a16057851;

import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class SuccessView {

    private MainActivity mainActivity;
    private ConstraintLayout successLayout;
    private ImageView[] starViews;
    private MediaPlayer mp;
    private static int STAR_COUNT = 5;

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
        this.starViews = new ImageView[STAR_COUNT];
        // Define media player victory sound
        this.initVictorySound();
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
        // Bring success view widgets to the front
        this.mainActivity.findViewById(R.id.successLayout).bringToFront();
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
        star.setId(this.mainActivity.genViewId());
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
        Animation animation = AnimationUtils.loadAnimation(this.mainActivity, R.anim.star);;
        // Set a random start offset for the animation
        animation.setStartOffset(this.mainActivity.getRandomNumber(0, 1000));
        // Apply animation set
        star.startAnimation(animation);
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
        cSet.connect(starId, ConstraintSet.TOP, this.successLayout.getId(), ConstraintSet.TOP, this.mainActivity.getRandomNumber(50, 1400));
        cSet.connect(starId, ConstraintSet.LEFT, this.successLayout.getId(), ConstraintSet.LEFT, this.mainActivity.getRandomNumber(50, 800));
        // Apply constraints
        cSet.applyTo(this.successLayout);
    }

    /**
     * Close success view and get new question.
     */
    public void replay() {
        // Reset victory sound if still playing
        if (this.mp.isPlaying()) {
            this.mp.stop();
            this.mp.release();
            this.initVictorySound();
        }
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

    /**
     * Initialise the victory sound.
     */
    private void initVictorySound() {
        this.mp = MediaPlayer.create(this.mainActivity, R.raw.victory);
    }

    /**
     * Checks if the success screen is visible or not.
     * @return boolean, True if it is, False otherwise.
     */
    public boolean isVisible() {
        return (this.successLayout.getVisibility() == View.VISIBLE);
    }
}
