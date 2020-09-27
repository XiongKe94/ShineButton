package com.sackcentury.shinebuttonlib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sackcentury.shinebuttonlib.listener.SimpleAnimatorListener;

/**
 * @author Chad
 * @title com.sackcentury.shinebuttonlib
 * @description
 * @modifier
 * @date
 * @since 16/7/5 下午2:27
 **/
public class ShineButton extends AppCompatImageView {
    private static final String TAG = "ShineButton";
    private boolean isChecked = false;
    private int unCheckColor;
    private int checkColor;
    private Drawable checkDrawable;
    private Drawable unCheckDrawable;

    DisplayMetrics metrics = new DisplayMetrics();


    Activity activity;
    ShineView shineView;
    ValueAnimator shakeAnimator;
    ShineView.ShineParams shineParams = new ShineView.ShineParams();

    OnCheckedChangeListener listener;

    private int bottomHeight;
    private int realBottomHeight;
    Dialog mFixDialog;
    private OnClickInterceptListener onClickInterceptListener;

    public ShineButton(Context context) {
        super(context);
        if (context instanceof Activity) {
            init((Activity) context);
        }
    }

    public ShineButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton(context, attrs);
    }


    public ShineButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context, attrs);
    }

    private void initButton(Context context, AttributeSet attrs) {

        if (context instanceof Activity) {
            init((Activity) context);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShineButton);
        unCheckColor = a.getColor(R.styleable.ShineButton_un_check_color, Color.GRAY);
        checkColor = a.getColor(R.styleable.ShineButton_check_color, Color.BLACK);
        shineParams.allowRandomColor = a.getBoolean(R.styleable.ShineButton_allow_random_color, false);
        shineParams.animDuration = a.getInteger(R.styleable.ShineButton_shine_animation_duration, (int) shineParams.animDuration);
        shineParams.bigShineColor = a.getColor(R.styleable.ShineButton_big_shine_color, shineParams.bigShineColor);
        shineParams.clickAnimDuration = a.getInteger(R.styleable.ShineButton_click_animation_duration, (int) shineParams.clickAnimDuration);
        shineParams.enableFlashing = a.getBoolean(R.styleable.ShineButton_enable_flashing, false);
        shineParams.shineCount = a.getInteger(R.styleable.ShineButton_shine_count, shineParams.shineCount);
        shineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineButton_shine_distance_multiple, shineParams.shineDistanceMultiple);
        shineParams.shineTurnAngle = a.getFloat(R.styleable.ShineButton_shine_turn_angle, shineParams.shineTurnAngle);
        shineParams.smallShineColor = a.getColor(R.styleable.ShineButton_small_shine_color, shineParams.smallShineColor);
        shineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineButton_small_shine_offset_angle, shineParams.smallShineOffsetAngle);
        shineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineButton_shine_size, shineParams.shineSize);
        shineParams.maskColor = a.getColor(R.styleable.ShineButton_mask_color, shineParams.maskColor);
        checkDrawable = a.getDrawable(R.styleable.ShineButton_checkDrawable);
        unCheckDrawable = a.getDrawable(R.styleable.ShineButton_unCheckDrawable);
        a.recycle();
        updateViewState();
    }

    public void setFixDialog(Dialog fixDialog) {
        mFixDialog = fixDialog;
    }

    public int getColor() {
        return checkColor;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setUnCheckColor(int unCheckColor) {
        this.unCheckColor = unCheckColor;
    }

    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }

    public void setChecked(boolean checked, boolean anim) {
        setChecked(checked, anim, true);
    }

    private void setChecked(boolean checked, boolean anim, boolean callBack) {
        isChecked = checked;
        if (checked) {
            if (anim) {
                showAnim();
            }
        } else {
            if (anim) {
                setCancel();
            }
        }
        if (callBack) {
            onListenerUpdate(checked);
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, false, false);
    }

    private void onListenerUpdate(boolean checked) {
        if (listener != null) {
            listener.onCheckedChanged(this, checked);
        }
    }

    public void setCancel() {
        if (shakeAnimator != null) {
            shakeAnimator.end();
            shakeAnimator.cancel();
        }
    }

    public void setAllowRandomColor(boolean allowRandomColor) {
        shineParams.allowRandomColor = allowRandomColor;
    }

    public void setAnimDuration(int durationMs) {
        shineParams.animDuration = durationMs;
    }

    public void setBigShineColor(int color) {
        shineParams.bigShineColor = color;
    }

    public void setClickAnimDuration(int durationMs) {
        shineParams.clickAnimDuration = durationMs;
    }

    public void enableFlashing(boolean enable) {
        shineParams.enableFlashing = enable;
    }

    public void setShineCount(int count) {
        shineParams.shineCount = count;
    }

    public void setShineDistanceMultiple(float multiple) {
        shineParams.shineDistanceMultiple = multiple;
    }

    public void setShineTurnAngle(float angle) {
        shineParams.shineTurnAngle = angle;
    }

    public void setSmallShineColor(int color) {
        shineParams.smallShineColor = color;
    }

    public void setSmallShineOffAngle(float angle) {
        shineParams.smallShineOffsetAngle = angle;
    }

    public void setShineSize(int size) {
        shineParams.shineSize = size;
    }

    public void setMaskColor(int maskColor) {
        shineParams.maskColor = maskColor;
    }

    public void setShapeResource(int checkRaw, int unCheckRaw) {
        checkDrawable = ContextCompat.getDrawable(activity, checkRaw);
        unCheckDrawable = ContextCompat.getDrawable(activity, unCheckRaw);
    }

    public void setOnCheckStateChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setOnClickInterceptListener(OnClickInterceptListener onClickInterceptListener) {
        this.onClickInterceptListener = onClickInterceptListener;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof OnButtonClickListener) {
            super.setOnClickListener(l);
        } else {
            if (onButtonClickListener != null) {
                onButtonClickListener.setListener(l);
            }
        }
    }

    OnButtonClickListener onButtonClickListener;

    public void init(Activity activity) {
        this.activity = activity;
        onButtonClickListener = new OnButtonClickListener();
        setOnClickListener(onButtonClickListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calPixels();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void showAnim() {
        if (activity != null) {
            shineView = new ShineView(activity, this, shineParams);
            ViewGroup rootView;
            if ( mFixDialog != null && mFixDialog.getWindow() != null ) {
                rootView = (ViewGroup) mFixDialog.getWindow().getDecorView();
                View innerView = rootView.findViewById(android.R.id.content);
                rootView.addView(shineView, new ViewGroup.LayoutParams(innerView.getWidth(), innerView.getHeight()));
            } else {
                rootView = (ViewGroup) activity.getWindow().getDecorView();
                rootView.addView(shineView, new ViewGroup.LayoutParams(rootView.getWidth(), rootView.getHeight()));
            }
            doShareAnim();
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    public void removeView(View view) {
        if (activity != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(view);
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    private void doShareAnim() {
        shakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f);
        shakeAnimator.setInterpolator(new LinearInterpolator());
        shakeAnimator.setDuration(500);
        shakeAnimator.setStartDelay(180);
        invalidate();
        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        /*shakeAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setSrcColor(btnFillColor);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setSrcColor(isChecked ? btnFillColor : btnColor);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                setSrcColor(btnColor);
            }

        });*/
        shakeAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void calPixels() {
        if (activity != null && metrics != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int[] location = new int[2];
            getLocationInWindow(location);
            Rect visibleFrame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
            realBottomHeight = visibleFrame.height() - location[1];
            bottomHeight = metrics.heightPixels - location[1];
        }
    }

    public class OnButtonClickListener implements OnClickListener {
        public void setListener(OnClickListener listener) {
            this.listener = listener;
        }

        OnClickListener listener;

        public OnButtonClickListener() {
        }

        public OnButtonClickListener(OnClickListener l) {
            listener = l;
        }

        @Override
        public void onClick(View view) {
            if (onClickInterceptListener != null && onClickInterceptListener.onIntercept()) {
                return;
            }
            if (!isChecked) {
                isChecked = true;
                showAnim();
            } else {
                isChecked = false;
                setCancel();
            }
            updateViewState();
            onListenerUpdate(isChecked);
            if (listener != null) {
                listener.onClick(view);
            }
        }
    }

    public void updateViewState() {
        if (isChecked) {
            if (checkDrawable != null) {
                tintImageViewDrawable(checkDrawable, checkColor);
            }
        } else {
            if (unCheckDrawable != null) {
                tintImageViewDrawable(unCheckDrawable, unCheckColor);
            }
        }
    }

    private void tintImageViewDrawable(Drawable drawable, int tintColor) {
        Drawable drawable1 = DrawableCompat.wrap(drawable.mutate());
        if (tintColor != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DrawableCompat.setTint(drawable, tintColor);
            } else {
                drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            }
        }
        this.setImageDrawable(drawable1);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, boolean checked);
    }

    public interface OnClickInterceptListener {
        boolean onIntercept();
    }
}
