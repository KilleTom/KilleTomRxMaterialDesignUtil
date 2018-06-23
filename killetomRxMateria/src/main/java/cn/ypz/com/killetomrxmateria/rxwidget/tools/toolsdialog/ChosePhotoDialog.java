package cn.ypz.com.killetomrxmateria.rxwidget.tools.toolsdialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.tools.RxPhotoTools;

public class ChosePhotoDialog extends BottomSheetDialog {
    public static int type = 0;
    private Activity activity;
    public ChosePhotoDialog(Activity activity, int theme,OnCancelListener cancelListener){
        super(activity,theme);
        this.activity =  activity;
        setContentView(R.layout.chose_photo_dialog);
    }

    public ChosePhotoDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.chose_photo_dialog);
    }


    protected ChosePhotoDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.chose_photo_dialog);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        findViewById(R.id.cancle).setOnClickListener(v -> {
            type = 0;
            cancel();
        });
        findViewById(R.id.camera).setOnClickListener(v -> {
            type = 1;
            Log.i("ypzPhoto",type+"");
            RxPhotoTools.openCameraImage(activity);
        });
        findViewById(R.id.photo).setOnClickListener(v -> {
            type = 2;
            Log.i("ypzPhoto",type+"");
            RxPhotoTools.openLocalImage(activity);
        });

    }

}
