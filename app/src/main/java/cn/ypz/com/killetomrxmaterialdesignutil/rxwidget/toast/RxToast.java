package cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.ypz.com.killetomrxmaterialdesignutil.R;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastErrorAnimation;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastIcon;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastInfoAnimation;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastSuccessAnimation;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastText;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastTextFade;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastType;
import cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.toast.anim.RxToastWarningAnimation;

public class RxToast {

    @ColorInt
    public static int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    @ColorInt
    public static int ERROR_COLOR = Color.parseColor("#D50000");
    @ColorInt
    public static int INFO_COLOR = Color.parseColor("#3F51B5");
    @ColorInt
    public static int SUCCESS_COLOR = Color.parseColor("#388E3C");
    @ColorInt
    public static int WARNING_COLOR = Color.parseColor("#FFA900");
    @ColorInt
    public static int NORMAL_COLOR = Color.parseColor("#353A3E");

    public static int NO_COLOR = 0;

    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    public static int textSize = 16; // in SP

    private static boolean tintIcon = true;
    private static boolean useAnim = true;

    private RxToast() {
    }

    @CheckResult
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message) {
        return choseType(rxToastType, context, message, Toast.LENGTH_SHORT);
    }


    @CheckResult
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message, int duration) {
        return choseType(rxToastType, context, message, duration, null);
    }

    @CheckResult
    public static Toast choseType(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message, int duration, RxToastIcon toastImage) {
        boolean isResetIcon = false;
        Log.i("ypz", String.valueOf(rxToastType) + tintIcon);
        if (rxToastType != RxToastType.RxToastNormalType && tintIcon && toastImage == null)
            isResetIcon = true;
        switch (rxToastType) {
            case RxToastNormalType:
                return custom(context, message, duration, NO_COLOR, toastImage);
            case RxToastInfoType:
                if (isResetIcon)
                    toastImage = new RxToastInfoAnimation(context);
                return custom(context, message, duration, INFO_COLOR, toastImage);
            case RxToastErrorType:
                if (isResetIcon)
                    toastImage = new RxToastErrorAnimation(context);
                return custom(context, message, duration, ERROR_COLOR, toastImage);
            case RxToastSuccessType:
                if (isResetIcon)
                    toastImage = new RxToastSuccessAnimation(context);
                return custom(context, message, duration, SUCCESS_COLOR, toastImage);
            case RxToastWarningType:
                if (isResetIcon)
                    toastImage = new RxToastWarningAnimation(context);
                return custom(context, message, duration, WARNING_COLOR, toastImage);
        }
        Log.i("ypz", "isGo");
        return custom(context, message, duration, NO_COLOR, toastImage);
    }

    @CheckResult
    @SuppressLint("ShowToast")
    public static Toast custom(@NonNull Context context, @NonNull CharSequence charSequence, int duration, @ColorInt int bgColor, RxToastIcon toastImage) {
        //init TextView
        final RxToastTextFade toastTextView = new RxToastTextFade(context);
        toastTextView.setGravity(Gravity.CENTER);
        toastTextView.setText(charSequence);
        toastTextView.setTextColor(DEFAULT_TEXT_COLOR);
        toastTextView.setTypeface(currentTypeface);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return custom(context, toastTextView, duration, bgColor, toastImage);
    }

    @CheckResult
    @SuppressLint("ShowToast")
    public static Toast custom(@NonNull Context context, @NonNull RxToastText text, int duration, @ColorInt int bgColor, RxToastIcon toastImage) {
        final Toast toast = Toast.makeText(context, "", duration);
        //init LinearLayout
        final LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        toastLayout.setGravity(Gravity.CENTER);
        toastLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        Drawable drawableFrame;
        if (bgColor != NO_COLOR)
            drawableFrame = ToastyUtils.tint9PatchDrawableFrame(context, bgColor);
        else
            drawableFrame = ToastyUtils.getDrawable(context, R.drawable.rxtoast_frame);
        ToastyUtils.setBackground(toastLayout, drawableFrame);
        //set anim time
        int animTime = 0;
        if (useAnim) {
            if (duration == Toast.LENGTH_SHORT)
                animTime = 500;
            else
                animTime = 1000;
        }
        //load image into linearLayout if exist
        if (tintIcon && toastImage != null) {
            //set imageLayout
            toastImage.setDuration(animTime);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dp2px(context, 22f)
                    , dp2px(context, 22f));
            imageParams.setMargins(0, dp2px(context, 1f), dp2px(context, 5f), 0);
            //config image
            toastImage.setColor(DEFAULT_TEXT_COLOR);
            toastLayout.addView(toastImage, imageParams);
        }
        //add text
        text.setColor(DEFAULT_TEXT_COLOR);
        text.setDuration(animTime);
        toastLayout.addView(text, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT));
        toast.setView(toastLayout);
        return toast;
    }

    private static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class Config {
        @ColorInt
        private int DEFAULT_TEXT_COLOR = RxToast.DEFAULT_TEXT_COLOR;
        @ColorInt
        private int ERROR_COLOR = RxToast.ERROR_COLOR;
        @ColorInt
        private int INFO_COLOR = RxToast.INFO_COLOR;
        @ColorInt
        private int SUCCESS_COLOR = RxToast.SUCCESS_COLOR;
        @ColorInt
        private int WARNING_COLOR = RxToast.WARNING_COLOR;

        private Typeface typeface = RxToast.currentTypeface;
        private int textSize = RxToast.textSize;

        private boolean tintIcon = RxToast.tintIcon;
        private boolean useAnim = RxToast.useAnim;
        private Toast toast;

        private Config() {

        }

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        public static void reset() {
            RxToast.DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
            RxToast.ERROR_COLOR = Color.parseColor("#D50000");
            RxToast.INFO_COLOR = Color.parseColor("#3F51B5");
            RxToast.SUCCESS_COLOR = Color.parseColor("#388E3C");
            RxToast.WARNING_COLOR = Color.parseColor("#FFA900");
            RxToast.currentTypeface = LOADED_TOAST_TYPEFACE;
            RxToast.textSize = 16;
            RxToast.tintIcon = true;
            RxToast.useAnim = true;
        }

        @CheckResult
        public Config setTextColor(@ColorInt int textColor) {
            DEFAULT_TEXT_COLOR = textColor;
            return this;
        }

        @CheckResult
        public Config setErrorColor(@ColorInt int errorColor) {
            ERROR_COLOR = errorColor;
            return this;
        }

        @CheckResult
        public Config setInfoColor(@ColorInt int infoColor) {
            INFO_COLOR = infoColor;
            return this;
        }

        @CheckResult
        public Config setSuccessColor(@ColorInt int successColor) {
            SUCCESS_COLOR = successColor;
            return this;
        }

        @CheckResult
        public Config setWarningColor(@ColorInt int warningColor) {
            WARNING_COLOR = warningColor;
            return this;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        @CheckResult
        public Config setUseAnim(boolean useAnim) {
            this.useAnim = useAnim;
            return this;
        }

        public void apply() {
            RxToast.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR;
            RxToast.ERROR_COLOR = ERROR_COLOR;
            RxToast.INFO_COLOR = INFO_COLOR;
            RxToast.SUCCESS_COLOR = SUCCESS_COLOR;
            RxToast.WARNING_COLOR = WARNING_COLOR;
            RxToast.currentTypeface = typeface;
            RxToast.textSize = textSize;
            RxToast.tintIcon = tintIcon;
            RxToast.useAnim = useAnim;
            //如果有链式调用了show的方法将自动显示然后重新reset回Rxtoast的所有属性状态
            if (toast != null){
                toast.show();
                reset();
            }
        }

        public Config show(RxToastType rxToastType, @NonNull Context context, @NonNull CharSequence message) {
            Log.i("ypz", String.valueOf(toast));
            RxToast.tintIcon = tintIcon;
            toast = choseType(rxToastType, context, message);
            Log.i("ypz", String.valueOf(toast));
            return this;
        }
    }
}
