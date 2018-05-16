/*
 * Copyright 2016 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ypz.com.killetomrxmaterialdesignutil.rxwidget.raisebutton;


import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import cn.ypz.com.killetomrxmaterialdesignutil.R;

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
