package cn.ypz.com.killetomrxmateria.rxwidget.permissiondialog;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import cn.ypz.com.killetomrxmateria.rxwidget.base.BaseActivity;

public abstract class RxPermissionBaseActivity extends BaseActivity {

    protected abstract void permissionAllow();

    protected abstract void permissionRefuse();

    protected abstract void requestCodeError(int requestCode);


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RxPermissions.RxPermissionRequestCode)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                permissionAllow();
            else permissionRefuse();
        else requestCodeError(requestCode);
    }
}
