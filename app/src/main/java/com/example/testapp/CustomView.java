package com.example.testapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomView extends RelativeLayout {

    private ProgressBar progressBar;
    private TextView progressText;
    private ObjectAnimator animator;
    private int progress;
    private ViewGroup layoutWeekend;
    private boolean isCurrentDay;
    private TextView tvWeekend;
    private ImageView progressEmotion;

    public CustomView(Context context) {
        super(context);
        initViews();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        initAttrs(attrs, context);
    }


    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        initAttrs(attrs, context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
        initAttrs(attrs, context);
    }


    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_my_custom_view, this, true);
    }

    @SuppressLint({"Recycle", "CustomViewStyleable", "SetTextI18n"})
    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        layoutWeekend = findViewById(R.id.layout_weekend);
        layoutWeekend.setVisibility(GONE);
        animator = new ObjectAnimator();
        animator.setPropertyName("progress");
        animator.setTarget(progressBar);
        progressText = (TextView) findViewById(R.id.progress_text);
        tvWeekend = (TextView) findViewById(R.id.tv_weekend);
        progressEmotion = (ImageView) findViewById(R.id.progress_emotion);
        int progressCount = ta.getInt(R.styleable.MyCustomView_cus_progress, 0);
        isCurrentDay = ta.getBoolean(R.styleable.MyCustomView_cus_is_current_day, false);
        if (isCurrentDay) {
            layoutWeekend.setBackgroundResource(R.drawable.bg_currentday);
            tvWeekend.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            layoutWeekend.setBackgroundColor(Color.parseColor("#00000000"));
            tvWeekend.setTextColor(Color.parseColor("#2D2F33"));
        }
        progressText.setVisibility(INVISIBLE);
        this.setProgress(progressCount);
        if (progressCount != -1) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) progressText.getLayoutParams();
            float per = (float) 288 / 100;
            layoutParams.topMargin = dp2px((int) (per * ((float) (100 - progressCount)) + 10));
            progressText.setText(progressCount + "");
            progressText.setLayoutParams(layoutParams);
        }
        progressBar.setProgressDrawable(ta.getDrawable(R.styleable.MyCustomView_cus_background));
        tvWeekend.setText(ta.getString(R.styleable.MyCustomView_cus_weekend));
        progressEmotion.setImageResource(ta.getResourceId(R.styleable.MyCustomView_cus_emotion_img, -1));
        this.startAnim(ta.getInt(R.styleable.MyCustomView_cus_animation_duration, 1000));
    }

    boolean isUnKnown = false;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void startAnim(long duration) {
        int progressCount = progress;
        if (progressCount == -1) {
            isUnKnown = true;
            progressCount = 30;
        }
        if (animator.isRunning()) {
            animator.end();
        }
        animator.setFloatValues(0, progressCount);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float currentProcess = (float) animation.getAnimatedValue();
            progressBar.setProgress((int) currentProcess);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.show_in_out));
                progressText.setVisibility(isUnKnown ? INVISIBLE : VISIBLE);
                layoutWeekend.startAnimation(
                        isCurrentDay ? AnimationUtils.loadAnimation(getContext(), R.anim.show_scale)
                                : AnimationUtils.loadAnimation(getContext(), R.anim.show_in_out));
                layoutWeekend.setVisibility(VISIBLE);
            }
        });
        animator.start();
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }


    public void onViewClick(int bg, String color,int emotion) {
        progressEmotion.setImageResource(emotion);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(layoutWeekend);
        layoutWeekend.setBackgroundResource(R.drawable.bg_selected_day);
        animator.setPropertyName("translationZ");
        progressBar.setProgressDrawable(getResources().getDrawable(bg));
        if (animator.isRunning()) {
            animator.end();
        }
        animator.setFloatValues(0, 8);
        animator.setDuration(180);
        animator.addUpdateListener(animation -> {
            float currentProcess = (float) animation.getAnimatedValue();
            layoutWeekend.setTranslationZ(currentProcess);
            layoutWeekend.setElevation(currentProcess);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvWeekend.setTextColor(Color.parseColor(color));
            }
        });
        animator.start();

        startProgressTextAnimation();
    }

    private void startProgressTextAnimation() {
        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(progressText);
        animator.setPropertyName("progress");
        animator.setFloatValues(20, 24);
        animator.setDuration(100);
        if (animator.isRunning()) {
            animator.end();
        }
        animator.addUpdateListener(animation -> {
            float currentProcess = (float) animation.getAnimatedValue();
            progressText.setTextSize(currentProcess);
        });
        progressText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        animator.start();
    }

    public void reset(int bg, String color,int emotion) {
        progressEmotion.setImageResource(emotion);
        progressText.setTextSize(20);
        progressText.setTypeface(Typeface.DEFAULT);
        progressBar.setProgressDrawable(getResources().getDrawable(bg));
        layoutWeekend.setBackgroundColor(Color.parseColor("#00000000"));
        tvWeekend.setTextColor(Color.parseColor(color));
        layoutWeekend.setElevation(0);
    }
}
