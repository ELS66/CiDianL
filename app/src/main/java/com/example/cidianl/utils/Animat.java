package com.example.cidianl.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

public class Animat {
    public static void rotation (View view, TextView oldText,TextView newText,int time) {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"rotationX",0,90);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view,"rotationX",-90,0);
        objectAnimator2.setInterpolator(new OvershootInterpolator(2.0f));
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldText.setVisibility(View.INVISIBLE);
                objectAnimator2.setDuration(time).start();
                newText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator1.setDuration(time).start();
    }
}
