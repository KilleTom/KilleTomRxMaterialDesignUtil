
package cn.ypz.com.killetomrxmateria.rxwidget.raisebutton;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import cn.ypz.com.killetomrxmateria.R;

public class RxRaisedDropUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setupDefalut(View view) {
        StateListAnimator stateListAnimator
                = AnimatorInflater.loadStateListAnimator(view.getContext(),
                R.drawable.rx_raised_dropbutton_statelistanimator);
        view.setStateListAnimator(stateListAnimator);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setupDIY(View view ,int resId) {
        StateListAnimator stateListAnimator
                = AnimatorInflater.loadStateListAnimator(view.getContext(), resId);
        view.setStateListAnimator(stateListAnimator);
    }
}
