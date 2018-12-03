package com.mochoi.pomer.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    final int addHeight;
    View view;
    int startHeight;

    public ResizeAnimation(View view, int addHeight, int startHeight) {
        this.view = view;
        this.addHeight = addHeight;
        this.startHeight = startHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHeight + addHeight * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}