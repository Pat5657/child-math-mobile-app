package com.example.a16057851;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class AppleMovable {

    private ImageView ivApple;
    private MainActivity mainActivity;
    private ConstraintLayout mainLayout;

    private static int MIN_LEFT_CONSTRAINT = 32;
    private static int MAX_LEFT_CONSTRAINT = 270;
    private static int MIN_TOP_CONSTRAINT = 280;
    private static int MAX_TOP_CONSTRAINT = 320;

    public AppleMovable(MainActivity mainActivity) {
        // Define main view
        this.mainActivity = mainActivity;
        this.mainLayout = this.mainActivity.findViewById(R.id.mainLayout);
        // Create apple image view
        this.createApple();
        // Assign listener
        this.ivApple.setOnTouchListener(handleTouch);
    }

    /**
     * Create the apple in the main view.
     */
    private void createApple() {
        // Define apple image view
        ImageView apple = new ImageView(this.mainActivity);
        // Set apple image
        apple.setImageResource(R.drawable.apple);
        // Set apple Id
        apple.setId(this.mainActivity.genViewId());
        // Add apple to main view
        this.mainLayout.addView(apple);
        // Assign apple image view
        this.ivApple = apple;
        // Set dimensions
        this.setDimensions();
        // Set constraints
        this.setConstraints();
    }

    /**
     * Set dimensions of the apple image view.
     */
    private void setDimensions() {
        // Define apple layout params
        ViewGroup.LayoutParams layoutParams = this.ivApple.getLayoutParams();
        // Set dimensions
        layoutParams.width = convertToDp(94);
        layoutParams.height = convertToDp(94);
        // Apply changes
        this.ivApple.setLayoutParams(layoutParams);
    }

    /**
     * Set random widget constraints for the apple.
     */
    private void setConstraints() {
        // Define apple id
        int appleId = this.ivApple.getId();
        // Define constraint set
        ConstraintSet cSet = new ConstraintSet();
        // Clone success view
        cSet.clone(this.mainLayout);
        // Set constraints
        cSet.connect(
                appleId,
                ConstraintSet.LEFT,
                this.mainLayout.getId(),
                ConstraintSet.LEFT,
                this.convertToDp(this.mainActivity.getRandomNumber(MIN_LEFT_CONSTRAINT, MAX_LEFT_CONSTRAINT))
        );
        cSet.connect(
                appleId,
                ConstraintSet.TOP,
                this.mainLayout.findViewById(R.id.txtQuestion).getId(),
                ConstraintSet.BOTTOM,
                this.convertToDp(this.mainActivity.getRandomNumber(MIN_TOP_CONSTRAINT, MAX_TOP_CONSTRAINT))
        );
        // Apply constraints
        cSet.applyTo(this.mainLayout);
    }

    /**
     * Remove the apple image from the view.
     */
    public void removeView() {
        this.mainLayout.removeView(this.ivApple);
    }

    /**
     * Convert pixels to DP unit.
     * @param pixels int
     * @return int
     */
    private int convertToDp(int pixels) {
        float scale = this.mainActivity.getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    /**
     * Touch listener to drag a view around.
     */
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        float dX, dY;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    };
}
