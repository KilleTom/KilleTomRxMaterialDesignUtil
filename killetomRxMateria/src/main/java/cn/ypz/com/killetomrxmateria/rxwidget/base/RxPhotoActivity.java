package cn.ypz.com.killetomrxmateria.rxwidget.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import cn.ypz.com.killetomrxmateria.R;
import cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog.RxPermissionBaseActivity;
import cn.ypz.com.killetomrxmateria.rxwidget.toast.RxToast;
import cn.ypz.com.killetomrxmateria.rxwidget.toast.anim.RxToastType;
import cn.ypz.com.killetomrxmateria.rxwidget.tools.RxPhotoTools;
import cn.ypz.com.killetomrxmateria.rxwidget.tools.toolsdialog.ChosePhotoDialog;

public abstract class RxPhotoActivity extends RxPermissionBaseActivity {

    protected ChosePhotoDialog chosePhotoDialog;
    protected boolean isrefuse;
    protected Uri photoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_photo);
        chosePhotoDialog = new ChosePhotoDialog(this, R.style.ChosePhotoDialog, null);
        isrefuse = false;
    }

    @Override
    protected void permissionAllow() {
        openAgain();
    }

    @Override
    protected void permissionRefuse() {
        if (!isrefuse) {
            try {
                openAgain();
            } catch (Exception e) {
                Log.i("ypz", e.getMessage());
            }
            isrefuse = true;
        }
    }

    @Override
    protected void requestCodeError(int requestCode) {
        RxToast.choseType(RxToastType.RxToastErrorType, this, "权限申请不成功错误码：" + requestCode).show();
    }

    protected void openAgain() {
        if (ChosePhotoDialog.type == 1) RxPhotoTools.openCameraImage(this);
        else if (ChosePhotoDialog.type == 2) RxPhotoTools.openLocalImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (chosePhotoDialog != null) chosePhotoDialog.cancel();
        if (requestCode == RxPhotoTools.CameraRequestCode && resultCode == RESULT_OK) {
            photoUri = RxPhotoTools.cameraImageUri;
        } else if (requestCode == RxPhotoTools.PhotoAlbumRequestCode && resultCode == RESULT_OK) {
            photoUri = data.getData();
        }
        if (photoUri != null) photoUri(photoUri);
    }

    protected abstract void photoUri(Uri uri);
}
